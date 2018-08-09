package com.carel.supervisor.director.ac;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.AcCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class AcProcess {

	public static void executePropagation(AcConfigBeanList list) throws Exception
	{
		AcConfigBean tmp = null;
		Float alarm = null;
		Float v_origin = null;
		
		SetContext set = null;
		Integer idMaster = new Integer(0);
		
		//SetContext set = preparateSet(true);
		
		int[] types = new int[]{0,0,0,0}; //counters x segnalazione negli eventi
		String[] messages = new String[]{
				"Alarm (probe broken): setting the support variable value",
				"Off-line: setting the support variable value",
				"Set value from master",
				"Value out of bounds: setting the support variable value"
				};
		
		Logger logger = LoggerMgr.getLogger(AcProcess.class);
		
		//Sezione: tbl "ac_slave"
		if ((list != null) && (list.size() > 0))
	    {
			// ciclo sulle configurazioni salvate su db
			for (int i = 0; i < list.size(); i++)
			{
				tmp = list.get(i);
				
				if (idMaster.intValue() != tmp.getIddevmaster().intValue()) //cambio master
				{
					if (idMaster.intValue() != 0)
					{
						executeSet(set); //propago sugli slaves del master precedente
					}
					
					idMaster = tmp.getIddevmaster(); //nuovo master
					set = preparateSet(true, tmp.getIddevmaster()); //preparo nuovo set per nuovo master
				}
				
				//verifica se la variabile di allarme è attiva
	            //(valida se allarme sempre presente)
				//alarm = ControllerMgr.getInstance().getFromField(tmp.getIdalarm()).getCurrentValue();
	            
	            //verifica se almeno una delle var di allarme è attiva:
	            alarm = ctrlAlarmsOfMaster(tmp.getIddevmaster());
	            
	            //recupero msg in lingua inglese (perchè c'è sempre in ogni PVPro) o stringhe cablate in inglese:
	            //LangService l = LangMgr.getInstance().getLangService("EN_en");
				
				//se allarme sonda rotta relativo alla variabile è "ON" setto la variabile passando il valore di supporto (def)
				if (!alarm.equals(Float.NaN) && alarm == 1f)
				{
					/**
	                System.out.println("Allarme sonda rotta");
					System.out.println("Set variabile di supporto");
	                **/
	                
					try
					{
						SetWrp wrp = set.addVariable(tmp.getIdvarslave(),tmp.getDef());
						//wrp.setCheckChangeValue(false);
						
						types[0]++;
					}
					catch (Exception e)
					{
			            // PVPro catch block:
						logger.info("Hardware error: " + "support variable value not set - Alarm active");
					}
				}
				//se la variabile di allarme è OFF, controllo se il master è propagabile 
				else
				{
					try
					{
						v_origin = ControllerMgr.getInstance().getFromField(tmp.getIdvarmaster()).getCurrentValue();
					}
					catch (Exception e)
					{
			            // PVPro catch block:
			            //logger.info("Unable to retrieve current value from Master");
			            
			            v_origin = new Float(Float.NaN);
					}
					
					if (v_origin.equals(Float.NaN))
					{
						//Set v_destination con valore di supporto
						/**
	                    System.out.println("Offline");
						System.out.println("Set variabile di supporto");
	                    **/
	                    
	                    //sezione di sicurezza nel caso non esista la var nel dev fisico slave, ma esista nel suo modello:
						try
						{
							SetWrp wrp = set.addVariable(tmp.getIdvarslave(),tmp.getDef());
							//wrp.setCheckChangeValue(false);
							
							types[1]++;
						}
						catch (Exception e)
						{
				            // PVPro catch block:
							logger.info("Hardware error: " + "support variable value not set - Off line");
						}
					}
					else
					{
						//Controllo che v_destination rientri nei limiti per la variabile
						if ((v_origin >= tmp.getMin()) && (v_origin <= tmp.getMax()))
						{
							//esito positivo set destination con valore origin "v_origin"
							/**
	                        System.out.println("set da master");
	                        **/
	                        
							try
							{
								SetWrp wrp = set.addVariable(tmp.getIdvarslave(),v_origin);
								//wrp.setCheckChangeValue(false);
								
								types[2]++;
							}
							catch (Exception e)
							{
					            // PVPro catch block:
								logger.info("Hardware error: " + "master variable value not set - On line");
							}
						}
						else
						{
							//Set v_destination con valore di supporto
						    /**
	                        System.out.println("Valore fuori limiti");
	                        **/
	                        
							try
							{
								SetWrp wrp = set.addVariable(tmp.getIdvarslave(),tmp.getDef());
								//wrp.setCheckChangeValue(false);
								
								types[3]++;
							}
							catch (Exception e)
							{
					            // PVPro catch block:
								logger.info("Hardware error: " + "support variable value not set - Out of range");
							}
						}
					}
				}
			}// chiuso for
			
			if (idMaster.intValue() != 0) //se ho preparato almeno un set che non ho ancora propagato:
			{
				executeSet(set); //propago sugli slaves dell'ultimo master
				set = null;
			}
        }
		
		//Sezione: tbl "ac_heartbit_mdl"
        if ((list != null) && (list.size() > 0))
        {
            // ***** BEGIN *****
        	// accodamento Heart-bits (var digitale con valore=1 + var int/analog con valore scelto) x ogni device slave:
        	String sql = "select cfd.iddevice, cfv1.idvariable as var1, cfv2.idvariable as var2, hb.value " + 
        				" from cfdevice as cfd, ac_heartbit_mdl as hb, cfvariable as cfv1, cfvariable as cfv2 " + 
        				" where cfd.iddevice IN (select DISTINCT iddevslave from ac_slave) " + 
        				" AND cfd.iddevmdl = (select iddevmdl from cfdevmdl where code = hb.code AND idsite=1) AND cfd.idsite=1 " + 
        				" AND cfv1.idsite=1 AND cfv2.idsite=1 AND " + 
        				" cfv1.idvariable = (select idvariable from cfvariable where idvarmdl = (select idvarmdl from cfvarmdl " + 
        				" where code = hb.digvar AND iddevmdl = cfd.iddevmdl AND idsite=1) " +
        				" AND iddevice = cfd.iddevice AND idsite=1) AND " + 
        				" cfv2.idvariable = (select idvariable from cfvariable where idvarmdl =(select idvarmdl from cfvarmdl " + 
        				" where code = hb.vcode AND iddevmdl = cfd.iddevmdl AND idsite=1) " +
        				" AND iddevice = cfd.iddevice AND idsite=1)";
        	
        	RecordSet rs = null;
        	Record rc = null;
        	
        	int idd = -1; //idvar digitale
        	float vald = 1.0f; //value x idd
        	int idv = -1; //idvar int/analog
        	float valv = 0.0f; //value x idv
        	
        	try
            {
                rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
            }
            catch (Exception e)
            {
                // PVPro catch block:
                logger.error(e);
            }
            
            if ((rs != null) && (rs.size() > 0))
            {
                set = preparateSet(true, null); //preparo ultimo set per sole var heartbit + vars correlate
            	
            	for (int i = 0; i < rs.size(); i++)
                {
                	rc = rs.get(i);
                	idd = -1;
                	idv = -1;
                	valv = 0.0f;
                	
                	// var digitale:
                	idd = ((Integer)(rc.get("var1"))).intValue();
                	SetWrp wrp = set.addVariable(idd, vald);
                	//wrp.setCheckChangeValue(false);
                	
                	// var int/analog + time:
                	idv = ((Integer)(rc.get("var2"))).intValue();
                	valv = ((Float)(rc.get("value"))).floatValue()/60; //valore in minuti
                	wrp = set.addVariable(idv, valv);
                	//wrp.setCheckChangeValue(false);
                }
            	
            	executeSet(set); //propago ultimo set creato
            	set = null;
            	
            } // ***** END *****.
        	
        	//report semplificato x il log:
        	for (int j = 0; j < types.length; j++)
        	{
        		if (types[j] > 0)
        		{
        			logger.info(messages[j]+" ("+types[j]+" var"+(types[j]>1?"s":"")+")");
        		}
        	}
        }
        else
        {
            EventMgr.getInstance().info(1,"DP broadcast","ac","AC07",null);
            
            logger.info("No values to propagate");
        }
	}
	
    private static float ctrlAlarmsOfMaster(Integer iddevmaster)
    {
        float allarmi = 0f;
        Integer idvaralarm = null;
        
        String sql = "select distinct idvaralarm from ac_slave where iddevmaster = ?";
        
        RecordSet rs = null;
        Object[] params = new Object[] {iddevmaster};
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        }
        catch (Exception e)
        {
            // PVPro catch block:
            Logger logger = LoggerMgr.getLogger(AcProcess.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int i = 0; i < rs.size(); i++)
            {
                if (allarmi != 1f)
                {
                    idvaralarm = (Integer)(rs.get(i).get("idvaralarm"));
                    
                    //se "idvaralarm" != Null:
                    if ((idvaralarm.intValue() != 0) && (idvaralarm.intValue() != -1))
                    {
                        try
                        {
                            allarmi = ControllerMgr.getInstance().getFromField(idvaralarm).getCurrentValue();
                        }
                        catch (Exception e)
                        {
                            // PVPro catch block:
                            Logger logger = LoggerMgr.getLogger(AcProcess.class);
                            logger.error(e);
                            
                            allarmi = 1f;
                        }
                    }
                }
            }
        }
        
        return allarmi;
    }
    
    public static boolean ctrl_ac_tables()
    {
    	boolean changed = false;
    	RecordSet rs = null;
    	
        //controllo tabelle "ac_master" e "ac_slave" per consistenza valori salvati rispetto ai devs del site:
    	String sql_mstrslv = "select iddevmaster from ac_slave where iddevmaster not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
    	
    	String sql_mstr = "select iddevmaster from ac_master where iddevmaster not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
    	
    	String sql_slv = "select iddevslave from ac_slave where iddevslave not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
    	
    	//elimino dati non più validi:
        String sql_ctrl_mstrslv = "delete from ac_slave where iddevmaster not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
        
        String sql_ctrl_mstr = "delete from ac_master where iddevmaster not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
        
        String sql_ctrl_slv = "delete from ac_slave where iddevslave not in (select iddevice from cfdevice where iscancelled='FALSE' and idsite=1)";
        
        try {
		
        	rs = null;
        	rs = DatabaseMgr.getInstance().executeQuery(null, sql_mstrslv, null);
			changed = ((rs != null) && (rs.size()) > 0);
        	
        	if (! changed)
			{
        		rs = null;
        		rs = DatabaseMgr.getInstance().executeQuery(null, sql_mstr, null);
				changed = ((rs != null) && (rs.size()) > 0);
			}
        	
        	if (! changed)
			{
        		rs = null;
        		rs = DatabaseMgr.getInstance().executeQuery(null, sql_slv, null);
				changed = ((rs != null) && (rs.size()) > 0);
			}
		}
        catch (DataBaseException e1)
		{
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcProcess.class);
            logger.error(e1);
		}
        
        //se la configurazione del campo è cambiata, aggiorno anche quella del modulo:
        if (changed)
        {
	        try
	        {
	            DatabaseMgr.getInstance().executeStatement(sql_ctrl_mstrslv, null);
	            
	            DatabaseMgr.getInstance().executeStatement(sql_ctrl_mstr, null);
	            
	            DatabaseMgr.getInstance().executeStatement(sql_ctrl_slv, null);
	            
	            //Logger logger = LoggerMgr.getLogger(AcProcess.class);
	            //logger.info("Verify AC Module devices structure according to site configuration");
	            
	            //EventMgr.getInstance().info(1,"DP broadcast","ac","AC09",null);
	        }
	        catch (Exception e)
	        {
	            // PVPro-generated catch block:
	            Logger logger = LoggerMgr.getLogger(AcProcess.class);
	            logger.error(e);
	        }
        }
        
        if (changed)
        {
            // Log modifica conf secondo attuali linee sito:
    		EventMgr.getInstance().info(1,"DP broadcast","ac","AC09",null);
    		
            // se started segnalo cambiamento al motore:
            if (AcManager.getInstance().isRunning())
            {
                // segnala di caricare la nuova configurazione dal db prima di propagare:
                AcManager.getInstance().setChanged(changed);
            }
        }
        
        return changed;
    }
    
	private static SetContext preparateSet(boolean loggable, Integer master)
	{
		SetContext set = new SetContext();
		set.setUser("DP broadcast");
		set.setLoggable(loggable);
		
		String lang = "";
		
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
			lang = "EN_en";
		}
		
		set.setLanguagecode(lang);
		
		AcCallBack accb = new AcCallBack();
		accb.setDescr(master);
		
		set.setCallback(accb);
		
		return set;
	}
	
	private static void executeSet(SetContext set)
	{
		//attesa sul semaforo booleano della SetDequeuerMgr
		while (SetDequeuerMgr.getInstance().isWorking());
		
		SetDequeuerMgr.getInstance().add(set, PriorityMgr.getInstance().getPriority(AcProcess.class.getName()));
	}
	
	protected static void dequeueAll()
	{
		SetDequeuerMgr.getInstance().dequeueAllByPriority(PriorityMgr.getInstance().getPriority(AcProcess.class.getName()));
	}
}
