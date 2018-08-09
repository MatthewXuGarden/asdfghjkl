package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.support.Information;


public class MaintenancePoller extends Poller
{
    public MaintenancePoller()
    {
    	setName("MaintenancePoller");
    }

    public void run()
    {
        int counter = 1440;

        do
        {
            try
            {
                if (1440 == counter)
                {
                    execute();
                    counter = 0;
                }

                if (!isStopped())
                {
                    try
                    {
                        Thread.sleep(60000L);
                    }
                    catch (Exception e)
                    {
                    }

                    counter++;
                }
            }
            catch (Exception e)
            {
                LoggerMgr.getLogger(this.getClass()).error(e);
            }
        }
        while (!isStopped());
    }

    private void execute()
    {
        try
        {
            MaintenanceMgr.getInstance().activate();
        }
        catch (Exception e)
        {
            LoggerMgr.getLogger(this.getClass()).error(e);
        }
    	
        /*
         * Controllo giornaliero della licenza
         */
        try 
        {
        	licenseChecker();	
        }
        catch(Exception e) {
        	LoggerMgr.getLogger(this.getClass()).error(e);
        }
        
        /*
         * Tolto controllo giornaliero della demo.
         * E' stato risolto con una classe esterna nella demo stessa
         */
    }
    
    private void licenseChecker()
    {
    	Information.getInstance().periodic();
    	if(Information.getInstance().isTrialValid())
    	{
    		// Perido di prova in scadenza tra N gg
    		EventMgr.getInstance().log(new Integer(1),"System","Start",
    		EventDictionary.TYPE_WARNING,"S029",new Object[]{new Long(Information.getInstance().getCountDown())});
    	}
    	else
    	{
    		try
    		{
    			if(!Information.getInstance().canStartEngine())
    				EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
    		}
    		catch(Exception e){
    			/*
    			 *  Non voglio tracciare l'exception
    			 *  Se funziona OK altrimenti perdo un messaggio negli eventi ininfluente
    			 */
    		}
    	}
    }
}
