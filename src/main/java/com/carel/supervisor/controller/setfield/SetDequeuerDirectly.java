package com.carel.supervisor.controller.setfield;

import java.util.Iterator;
import java.util.List;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.base.util.GeneralQueue;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.database.zipped.VariableZippedManager;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DataCollector;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettings;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettingsMap;
import com.carel.supervisor.field.modbusfunmgrs.DeviceFunctionMapper;
import com.carel.supervisor.field.modbusfunmgrs.IFunctionMgr;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.presentation.bean.rule.RelayBean;
import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.io.CioRAS;

//Specifico per protocollo Carel
public class SetDequeuerDirectly{

	private int setDelay = 2; //espresso in secondi
	private int maxSetRetry = 50; //espresso in numero di tentativi
	private int readDelay = 2; //espresso in secondi
	private int readRetry = 50; //espresso in numero di tentativi
	private int sleepafterset = 5; //espresso in numero di tentativi
	private DataCollector dataCollector = null; 
	private GeneralQueue<SetContext> queue = new GeneralQueue<SetContext>(); //coda
	private boolean start = false;
	
	public SetDequeuerDirectly()
    {
		this.dataCollector = FieldConnectorMgr.getInstance().getDataCollector();
		this.start = true;
		try
		{
			setDelay = (int) SystemConfMgr.getInstance().get("setDelay").getValueNum();
			maxSetRetry = (int) SystemConfMgr.getInstance().get("maxSetRetry").getValueNum();
			readDelay = (int) SystemConfMgr.getInstance().get("readDelay").getValueNum();
			readRetry = (int) SystemConfMgr.getInstance().get("readRetry").getValueNum();
		}
		catch (Exception e)
		{
			//inserito per motivi di debug
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
    
    public boolean isStopped(){
        return (!start);
    }
	//alcune variabili cambiano di stato appena inviate => non le devo controllare
	public void add(SetContext setContext)
	{
		add(setContext, PriorityMgr.getInstance().getDefaultPriority());
	}
	
	public void add(SetContext setContext, int priority)
	{
		add(setContext, priority, false);
	}
	
	public void add(SetContext setContext, int priority, boolean isRollback){
		
		//// Controllo Parametri Plugin
		if (ParametersMgr.isPluginRegistred() && ParametersMgr.getParametersCFG().getEnabled()){
			// Se il plugin � registrato e attivo allora gestisco il set della variabile
			
			Iterator<Integer> i = setContext.keys();
			Integer iddev =null;
			while (i.hasNext()){
				iddev = (Integer) i.next();
				List<SetWrp> l = setContext.get(iddev);
				for (int j = 0; j < l.size(); j++) {
					SetWrp var = l.get(j);
					Float val = var.getNewValue();
					Integer idvar = var.getVar().getInfo().getId();
					try {
						ParametersMgr.getInstance().checkVariationAndLog(idvar, val, setContext.getUser(),!ParametersMgr.getParametersCFG().getAggregatenotification(), isRollback);
					} catch (DataBaseException e) {
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
					}
				}
			}
			
		}
		//// Fine Controllo Parametri Plugin
		
		setContext.getCallback().onStart(setContext);
		//queue.enqueue(setContext); //old version
		queue.enqueue(setContext, priority);	
	}
	
    
	public String toString()
	{
		return queue.toString();
	}
	

	
	private void sleep(int seconds)
	{
		try
		{
			Thread.sleep((long)(seconds * 1000L));
		}
		catch(Exception e)
		{
		}
	}
	
	public int tryToSend(SetContext setContext)
	{
		int result=-1;
		int idvariable = 0;
		SetDequeuerMgr.getInstance().setWorking(true);
		SetWrp wrp = null;
		Iterator<Integer> iterator = setContext.keys();
		Integer idDevice = null;
		boolean exit = false;
		setContext.getCallback().onStart(setContext);
		while(iterator.hasNext()) //La granularit� � a livello di dispositivo
		{
			if (isStopped())
			{
				break;
			}
			idDevice = (Integer)iterator.next();
			List<SetWrp> variables = setContext.get(idDevice);
			for(int i = 0; i < variables.size(); i++)
			{
				wrp = variables.get(i);
				idvariable = wrp.getVar().getInfo().getId();
				exit = (!singleSend(setContext, wrp));
				if (exit || isStopped())
				{
					break; //Se errore e non posso continuare, esco dal ciclo
				}
			}
			if (exit || isStopped()) 
			{
				break; //Non faccio neanche il controllo successivo
			}
			setContext.execute(); //Solo se tutte ok entro in questo flusso
			sleep(sleepafterset);
//			Verifico le variabili del dispositivo	
			int readCounter = 0;
			for(int i = 0; i < variables.size(); i++)
			{
				wrp = variables.get(i);
				if (wrp.getCode() == DataConnBase.SET_ON_QUEUE_OK)
				{
					exit = (!singleCheck(setContext, wrp, readCounter));
					if (exit || isStopped())
					{
						break; //Se errore e non posso continuare, esco dal ciclo
					}
				}
				
			}
			if (exit || isStopped())
			{
				break; //Non faccio neanche il controllo successivo
			}
			setContext.execute(); //Solo se tutte ok entro in questo flusso
		}
		if (isStopped())
		{
			setContext.getCallback().onStop(setContext);
			if(setContext.isLoggable() && setContext.getIsTest() == true)
			{
				int idrelay = 0;
				int idsite = wrp.getVar().getInfo().getDeviceInfo().getSite();
				String language = setContext.getLanguagecode();
				RelayBean relayBean = RelayBeanList.getRelayBeanByVariableid(idsite, language, idvariable);
				if(relayBean != null)
				{
					idrelay = relayBean.getIdrelay();
					CioRAS ioRas = new CioRAS(-1);
					ioRas.setTestResult(idrelay, false);
				}
			}
		}
		else
		{
			result=setContext.getCallback().onEnd(setContext);
			if(setContext.isLoggable() && setContext.getIsTest() == true)
			{
				int idrelay = 0;
				int idsite = wrp.getVar().getInfo().getDeviceInfo().getSite();
				String language = setContext.getLanguagecode();
				RelayBean relayBean = RelayBeanList.getRelayBeanByVariableid(idsite, language, idvariable);
				if(relayBean != null)
				{
					idrelay = relayBean.getIdrelay();
					CioRAS ioRas = new CioRAS(-1);
					ioRas.setTestResult(idrelay, wrp.getCode()==DataConnBase.SET_OK);
				}
			}
		}
		SetDequeuerMgr.getInstance().setWorking(false);
		return result;
	}
	
	private boolean singleCheck(SetContext setContext, SetWrp wrp, int readCounter)
	{
		if (!wrp.isCheckChangeValue())
		{ //Variabile della quale non faccio check, rimane in stato inviato
			//wrp.setCode(DataConnBase.SET_OK);
			//setContext.logCode(wrp); //Loggo il codice
			//setContext.getCallback().executeOnOk(setContext, wrp);
			return true;
		}
		
        float valueField = 0;
        Variable var = wrp.getVar();
        int readRetryTmp = readRetry;
        try
        {
	        if (var.getInfo().getDeviceInfo().getModel().intValue() == 18) //MPX
	        {
	        	readRetryTmp = 400; //400
	        }
        }
        catch(Exception e)
        {
        }
        
        boolean again = true;
        while(again)
        {
        	if (isStopped())
        	{
        		return true;
        	}
        	readCounter++;
        	if (readCounter <= readRetryTmp)
        	{
		        try
		        {
		        	dataCollector.getFromField(var);
		            valueField = var.getCurrentValue();
		        }
		        catch (Exception e)
		        {
		        	setContext.logCode(wrp); //Loggo il codice
		        	return manageError(setContext,wrp);
		        }
		
		        if (Float.isNaN(valueField))
		        {
		            again = true;
		            sleep(readDelay);
		        }
		        else if (wrp.getNewValue() == valueField)
		        {
		        	wrp.setCode(DataConnBase.SET_OK);
		        	setContext.logCode(wrp); //Loggo il codice
		        	setContext.getCallback().executeOnOk(setContext, wrp);
		        	return true;
		        }
		        else
		        {
		        	again = true;
		        	//System.out.println("COUNTER READ: " + readCounter);
		        	sleep(readDelay);
		        }
        	}
        	else
        	{
        		wrp.setCode(DataConnBase.READ_TIMEOUT);
        		setContext.logCode(wrp); //Loggo il codice
        		return manageError(setContext,wrp);
        	}
        }
        return true;
	}
	
	private boolean singleSend(SetContext setContext, SetWrp wrp)
	{
		//Fase di presetting START
		VariableInfo varInfo = wrp.getVar().getInfo();
		wrp.getVar().retrieveAndSaveValue(0,null,false);
		float oldvalue = wrp.getVar().getCurrentValue();
        
		if ((null != varInfo.getMaxValue()) && (!"".equals(varInfo.getMaxValue().trim())))
        {
            String tmp = varInfo.getMaxValue().trim();

            if (tmp.startsWith("pk"))
            {
                int idMax = Integer.parseInt(tmp.substring(2));
                Variable varMax = null;
                try
                {
                	varMax = ControllerMgr.getInstance().getFromField(idMax);
                }
                catch(Exception e)
                {
                	LoggerMgr.getLogger(this.getClass()).error(e);
                	varMax = new Variable(varInfo);
                }
                float max = varMax.getCurrentValue();
 
                if (!Float.isNaN(max))
                {
                    if (max < wrp.getNewValue())
                    {
                        //throw new ValueHighException(String.valueOf(max));
                        wrp.setCode(DataConnBase.SET_MAX);
                		setContext.logFirstTime(wrp, oldvalue);
                        return manageError(setContext, wrp);
                    }
                }
            }
            else
            {
            	tmp = Replacer.replace(tmp, ",", "."); //sostituisco separatore migliaia nel max
                
            	Float max = null;
				try
				{
					max = Float.valueOf(tmp);
				}
				catch (Exception e)
				{
					max = new Float(Float.NaN);
					LoggerMgr.getLogger(this.getClass()).error(e);
				}

                if (max.floatValue() < wrp.getNewValue())
                {
                    //throw new ValueHighException(max.toString());
                    wrp.setCode(DataConnBase.SET_MAX);
            		setContext.logFirstTime(wrp, oldvalue);
                    return manageError(setContext, wrp);
                }
            }
        }

        if ((null != varInfo.getMinValue()) && (!"".equals(varInfo.getMinValue().trim())))
        {
            String tmp = varInfo.getMinValue().trim();

            if (tmp.startsWith("pk"))
            {
                int idMin = Integer.parseInt(tmp.substring(2));
                Variable varMin = null;
                try
                {
                	varMin = ControllerMgr.getInstance().getFromField(idMin);
                }
                catch(Exception e)
                {
                	LoggerMgr.getLogger(this.getClass()).error(e);
                }
                float min = varMin.getCurrentValue();

                if (!Float.isNaN(min))
                {
                    if (min > wrp.getNewValue())
                    {
                        //throw new ValueLowException(String.valueOf(min));
                        wrp.setCode(DataConnBase.SET_MIN);
                		setContext.logFirstTime(wrp, oldvalue);
                        return manageError(setContext, wrp);
                    }
                }
            }
            else
            {
            	tmp = Replacer.replace(tmp, ",", "."); //sostituisco separatore migliaia nel min
                
            	Float min = null;
				try
				{
					min = Float.valueOf(tmp);
				}
				catch (Exception e)
				{
					min = new Float(Float.NaN);
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
                
                if (min.floatValue() > wrp.getNewValue())
                {
                    //throw new ValueLowException(min.toString());
                    wrp.setCode(DataConnBase.SET_MIN);
            		setContext.logFirstTime(wrp, oldvalue);
                    return manageError(setContext, wrp);
                }
            }
        }
        
        wrp.getVar().setValue(wrp.getNewValue());
        
        /*
         * Se arrivo qui ho superato i controlli su MAX e MIN 
         * del valore della variabile
         */
        Integer idDevice = varInfo.getDevice();
        
        /*HAMMERING*/
        /*technical intervention for zippedvar modbusv2*/
        if(varInfo.getAddressOut()>=100000){
        	float val = wrp.getNewValue();
    		IFunctionMgr fmgr = DeviceFunctionMapper.getInstance().getFunctionMgr(varInfo.getDeviceInfo().getModel());
    		if(fmgr!=null){
    			val = fmgr.applyInverseFunction(wrp.getVar());
    		}else{
    			// (y-b)/a
    			ModbusSettings abn = ModbusSettingsMap.get(varInfo.getModel());
    			if(abn!=null)
    				val = (val-abn.getBvalue())/abn.getAvalue();
    			// (y-b)/a
    		}
    		wrp.getVar().setValue(val);
        	//        	wrp.setNewValue(val);
        }
        /*technical intervention for zippedvar modbusv2*/
        /*HAMMERING*/
        
        VariableZippedManager zippedMgr = VariableZippedManager.getInstance();
        if(zippedMgr.isZipVariable(idDevice,varInfo.getId()))
        {
        	// get the value on the whole 'varDimension' bit size before setting new zipped variable value
        	int valueOfDimension = zippedMgr.getValueOfDimension(varInfo.getDevice(),varInfo.getId());
        	
           	// sets the value on the zipped var 
        	zippedMgr.setValue(varInfo.getDevice(),varInfo.getId(),Float.toString(wrp.getNewValue()));
            
        	// get the value of the whole 'varDimension' bit size, with the updated zipped variable value
        	String value = zippedMgr.getValue(varInfo.getDevice(),varInfo.getId(),valueOfDimension);
            if(value != null)
            {        		
            	wrp.getVar().setValue(Float.valueOf(value));
            }
        }

		//Fase di presetting END
        
		int code = DataConnBase.SET_QUEUE_FULL;
		int counterSet = 0;
		while(code == DataConnBase.SET_QUEUE_FULL)
		{
			if (isStopped())
        	{
        		return true;
        	}
			counterSet++;
			code = dataCollector.setOnField(wrp.getVar());
			if (code == DataConnBase.SET_QUEUE_FULL) //Coda piena 
			{
				//System.out.println("COUNTER SET: " + counterSet);
				if (counterSet <= maxSetRetry)
				{
					sleep(setDelay);
				}
				else
				{
					code = DataConnBase.SET_TIMEOUT;
				}
			}
		}
		wrp.setCode(code);
		setContext.logFirstTime(wrp, oldvalue); //Loggo il codice
		if (code == DataConnBase.SET_ON_QUEUE_OK) //ERRORE di qualsiasi tipo, compreso il timeout
		{
			return true;
		}
		else
		{
			return manageError(setContext, wrp);
		}
	}
	
	private boolean manageError(SetContext setContext, SetWrp wrp)
	{
		boolean goOn = setContext.getCallback().continueOnError(setContext, wrp);
		if (!goOn)
		{
			setContext.execute(); // Se errore � bloccante, scarico tutto sul DB prima di chiamare la call-back
		}
		setContext.getCallback().executeOnError(setContext, wrp);
		return goOn;
	}
	
	public boolean inProgress()
	{
		return false;
	}
	
	protected void delAllByPriority(int priority)
	{
		queue.removeAllByPriority(priority);
	}
}
