package com.carel.supervisor.dispatcher.action;

import java.sql.Timestamp;
import java.util.Iterator;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.setfield.AlarmsCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.HsRelayBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.engine.rele.DispReleTimer;
import com.carel.supervisor.dispatcher.tech.clock.TechClock;
import com.carel.supervisor.field.VariableMgr;
import com.carel.supervisor.controller.VariablesAccess;
import com.carel.supervisor.dispatcher.tech.clock.SetClock;


public class VAction extends DispatcherAction
{
    public static final String SPLIT = ";";
     

    public VAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
    }

    protected String[] initializedRecepients(String recepient)
    {
        String[] recRet = new String[0];

        if (recepient != null)
        {
            recRet = recepient.split(SPLIT);
        }

        return recRet;
    }

    public int[] putActionInQueue() throws Exception
    {
        HSActionBeanList actionbean = new HSActionBeanList();
        int[] ret = new int[0];
        int idAct = -1;
        int idCfAct = -1;
//        boolean ris = false;
        String[] infoSender = new String[0];
        String defLang = "EN_en";
        
        boolean isAlarm = this.isAlarm();
        boolean isStart = true;
        if(this.getEndTime().size() > 0 && this.getEndTime().get(0) != null)
        	isStart = false;
        
        try
        {
            infoSender = this.getInfoSender(this.getIdSite());

            if ((infoSender != null) && (infoSender.length == 3))
            {
                defLang = infoSender[2];
            }
        }
        catch (Exception e){}

        try
        {
            idAct = ((Integer) this.getKeyAction().get(0)).intValue();
            idCfAct = ((Integer) this.getIdAction().get(0)).intValue();
        }
        catch (Exception e){}

        String[] val = this.getRecepients();
        String cur = "";
        
        if(idCfAct == 1)
        {
//        	ris = 
        	setTechVariable(val,defLang);
        }
        else
        {
	        if (val != null)
	        {
	            for (int i = 0; i < val.length; i++)
	            {
	                cur = val[i];
	
	                if (this.getTypeAction().equalsIgnoreCase("L"))
	                {
//	                    ris = 
	                    setRele(cur, defLang, isAlarm, isStart);
	                }
	                else
	                {
//	                	ris = 
	                	setVariable(cur, defLang, isAlarm, isStart);
	                }
	            }
	        }
        }
        
        // Comunque setto lo stato dell'azione a eseguita, visto che il risultato dell'operazione lo sapr� solo dopo
        actionbean.updateToSendActionList(new int[] { idAct }, true);
        
        return ret;
    }

    private boolean setRele(String value, String lang,boolean isAlarm,boolean isStarted)
    {
        boolean ret = false;
        int idx = value.indexOf("=");
        String sIdVar = "";
        int iIdVar = 0;
        String sVal = "";
        long iTime = 0;
        String[] relayInfo = null;
        
        if(idx != -1)
        {
            sIdVar = value.substring(0, idx);
            sVal = value.substring(idx+1);

            try
            {
                iIdVar = Integer.parseInt(sIdVar);
                relayInfo = getIdVarRele(iIdVar);
                
                // Id della variabile che ha generato l'allarme.
                Integer idAlarmVar = (Integer)getIdVariable().get(0);
                
                if(isStarted)
                {
                	if(sVal != null && sVal.equalsIgnoreCase("1"))
                    	sVal = relayInfo[3];
                    else
                    {
                    	if(relayInfo[3].equalsIgnoreCase("1"))
                    		sVal = "0";
                    	else
                    		sVal = "1";
                    }
	                
                	SetContext setContext = new SetContext();
                	setContext.setLanguagecode(lang);
                	setContext.addVariable(Integer.parseInt(relayInfo[0]), Float.parseFloat(sVal), idAlarmVar);	
                	setContext.setCallback(new AlarmsCallBack());
            		setContext.setUser("Dispatcher");
                    SetDequeuerMgr.getInstance().add(setContext);
                	
	                /*
	                 * Valutazione tipo di rientro relay
	                 * M -> Manuale, nessuna azione da parte del dispatcher
	                 * A -> Automatico, rientra quando tutti gli allarmi che interagiscono con il relay rientrano
	                 * T -> A tempo, rientra dopo un tot di minuti impostati in configurazione del relay
	                 */
	                if(relayInfo[1].equalsIgnoreCase("T"))
	                {
	                	iTime = Long.parseLong(relayInfo[2]);
	                	if ((iTime > 0))
	                        new DispReleTimer(Integer.parseInt(relayInfo[0]), sVal, (iTime * 1000L), lang).resetRele();
	                }
	                else if(relayInfo[1].equalsIgnoreCase("A"))
	                {
	                	HsRelayBean.getInstance().add(iIdVar);
	                }
                }
                else
                {
                	if((relayInfo[1].equalsIgnoreCase("A")) && (HsRelayBean.getInstance().remove(iIdVar)))
                	{
                		if(sVal != null && sVal.equalsIgnoreCase("1"))
                		{
                			if(relayInfo[3].equalsIgnoreCase("1"))
                        		sVal = "0";
                        	else
                        		sVal = "1";
                		}
                		else
                		{
                			sVal = relayInfo[3];
                		}
                		
                		
                		
                		SetContext setContext = new SetContext();
                		setContext.setLanguagecode(lang);
                    	setContext.addVariable(Integer.parseInt(relayInfo[0]), Float.parseFloat(sVal), idAlarmVar);	
                    	setContext.setCallback(new AlarmsCallBack());
                		setContext.setUser("Dispatcher");
                        SetDequeuerMgr.getInstance().add(setContext);
                	}
                }
                
                ret = true;
            }
            catch (Exception e)
            {
                try {
                	EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",EventDictionary.TYPE_ERROR,"D055",new Object[]{sIdVar});
                }catch (Exception ex) {}
            }
        }

        return ret;
    }

    private boolean setVariable(String value, String lang,boolean isAlarm,boolean isStarted)
    {
    	boolean ret = true;
    
    	if(isStarted)
    	{
    		ret = false;
	        int idx = value.indexOf("=");
	        String sIdVar = "";
	        int iIdVar = 0;
	        String sVal = "";
	        Integer idAlarmVar = (Integer)getIdVariable().get(0);
	        
	        if (idx != -1)
	        {
	            sIdVar = value.substring(0, idx);
	            sVal = value.substring(idx + 1);
	            iIdVar = Integer.parseInt(sIdVar);
	
	            SetContext setContext = new SetContext();
	            setContext.setLanguagecode(lang);
	            if(!sVal.startsWith("id")){
	            	setContext.addVariable(iIdVar, Float.parseFloat(sVal), idAlarmVar);	
	            }else{
	            	try {
						setContext.addVariable(iIdVar, ControllerMgr.getInstance().getFromField(new Integer(sVal.substring(2))).getCurrentValue(), idAlarmVar);
					} catch (Exception e) {
						LoggerMgr.getLogger(this.getClass()).error(e);
					}
	            }
	            setContext.setCallback(new AlarmsCallBack());
	            setContext.setUser("Dispatcher");
	            SetDequeuerMgr.getInstance().add(setContext);
	        }
    	}

        return ret;
    }

    private String[] getIdVarRele(int idRele)
    {
        String sql = "select idVariable,resettype,resettime,activestate from cfrelay where idrelay=? and iscancelled=?";
        Object[] p = { new Integer(idRele),"FALSE" };
        String[] sRet = new String[4];
        
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, p);
            Record r = null;

            if ((rs != null) && (rs.size() > 0))
            {
                r = rs.get(0);
                sRet[0] = ""+((Integer) r.get("idvariable")).intValue();
                sRet[1] = UtilBean.trim(r.get("resettype"));
                sRet[2] = ""+((Integer) r.get("resettime")).intValue();
                sRet[3] = ""+((Integer) r.get("activestate")).intValue();
            }
        }
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return sRet;
    }
    

	private boolean setTechVariable(String[] idvarsmdl, String lang)
    {
    	if( SetClock.clockMaster().isRunning() )
    		return false;
    	else {
    		SetClock.clockMaster().setJob(idvarsmdl, lang);
    		new Thread(SetClock.clockMaster(),"SetClock").start();
    		return true;
    	}
    }

    
    //LDAC TO DO RIMUOVERE

    /*private String[] writeEventInformation(String lang, int site, int var, boolean ris, String val)
    {
        String devd = "";
        String vard = "";
        String[] ret = new String[0];

        Map descs = VariableHelper.getDescriptions(lang, site, new int[] { var });

        if (descs != null)
        {
            ret = (String[]) descs.get(new Integer(var));
            devd = ret[1];
            vard = ret[0];
        }

        if (ris)
        {
            try
            {
                Object[] p = { devd, vard, val };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", new Integer(3),
                    "D052", p);
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            try
            {
                Object[] p = { devd, vard };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", new Integer(1),
                    "D053", p);
            }
            catch (Exception e)
            {
            }
        }

        return ret;
    }*/
}
