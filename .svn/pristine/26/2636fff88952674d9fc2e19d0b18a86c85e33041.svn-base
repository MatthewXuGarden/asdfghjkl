package com.carel.supervisor.plugin.fs;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;


public class FSClock extends Poller
{
    private FSManager mgr = null;
    private long sleep = 60000L;

    public FSClock(FSManager mgr, long sleep)
    {
    	this.setName("Floating");
        this.sleep = sleep*1000;
        this.mgr = mgr;
    }

    public void run()
    {
    	try {
			Thread.sleep(60000);
			LoggerMgr.getLogger(this.getClass()).info(this.getClass()+" plugin start");
		} catch (InterruptedException e1) {
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}

		loadcache();
		
		while (this.isStarted())
        {
            try
            {
                mgr.executeFSPressureControl(this.sleep);
            }
            catch (Exception e)
            {
            	Logger logger = LoggerMgr.getLogger(FSClock.class);
                logger.error(e);
            }

            // Sleep
            try
            {
                Thread.sleep(this.sleep);
            }
            catch (InterruptedException e)
            {
            	Logger logger = LoggerMgr.getLogger(FSClock.class);
                logger.error(e);
            }
        }
    }

    public void loadcache() {
    	/*
    	 * cache di ottimizzazione 
    	 * carico tutte le variabili necessarie in una mappa per non ricreare gli oggetti ad ogni loop
    	 */
    	FSManager.getInstance().setFieldvariables(new HashMap<Integer, Variable>());
		try {
	    	RecordSet rs1 = DatabaseMgr.getInstance().executeQuery(null,"select * from fsrack where idrack != -1",null);
	    	RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null,"select * from fsutil",null);
	    	int[] idvars = new int[rs1.size()*4+rs2.size()];
	    	int counter=0;
	    	String aux = "";
	    	for(int i=0; i<rs1.size(); i++)
	    	{
	    		aux = rs1.get(i).get("aux").toString();
	    		idvars[counter++]=((Float)rs1.get(i).get("setpoint")).intValue();
	    		if (!aux.equalsIgnoreCase("old"))
	    		{
		    		idvars[counter++]=((Float)rs1.get(i).get("minset")).intValue();
		    		idvars[counter++]=((Float)rs1.get(i).get("maxset")).intValue();
		    		idvars[counter++]=((Float)rs1.get(i).get("gradient")).intValue();
	    		}
	    	}
	    	for(int i=0; i<rs2.size(); i++)
	    	{
	    		idvars[counter++]=(Integer)rs2.get(i).get(1);
	    	}
	        Variable[] variables;
			while(!ControllerMgr.getInstance().isLoaded()) {
				try{Thread.sleep(1500);}catch(Exception ex){}
			}
			variables = ControllerMgr.getInstance().getFromField(idvars);
	        for(int i=0;i<variables.length;i++)
	        {
	        	FSManager.getInstance().getFieldvariables().put(variables[i].getInfo().getId(), variables[i]);
	        }
//	        LoggerMgr.getLogger(this.getClass()).info("Cache loaded");
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			try{
				FSManager.getInstance().getFieldvariables().clear();
				if(FSManager.getInstance().isRunning())
					FSManager.getInstance().stopFS("admin");
			}catch(Exception ek){}
		}
		/*
		 * fine cache
		 */
	}
}
