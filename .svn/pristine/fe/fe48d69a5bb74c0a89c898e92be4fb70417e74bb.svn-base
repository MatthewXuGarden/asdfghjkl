package supervisor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.system.PvproInfo;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.enhanced.SyncEnhancedServer;
import com.carel.supervisor.presentation.polling.ThreadController;
import com.carel.supervisor.remote.manager.RasServerMgr;


public class PVStarter implements ServletContextListener
{
	private Demo objDemo = null;
	private Thread threadDemo = null;
	
	public PVStarter()
    {
        super();
        System.out.println("PlantVisorPro - init");

        try
        {
            BaseConfig.init();
        }
        catch (Throwable e)
        {
        	e.printStackTrace();
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public void contextInitialized(ServletContextEvent arg0)
    {
        System.out.println("PVStarter - contextInitialized");
        
        ScriptInvoker script = new ScriptInvoker();
        try
        {
        	script.execute(new String[] { "taskkill", "/F", "/IM", "java.exe"},BaseConfig.getLogFile());
        }
        catch (Throwable e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        // Sezione relativa all'inizializzazione del motore
        try
        {
        	if(Information.getInstance().canStartEngine())
        	{
        		// check license
                if( PvproInfo.getInstance().getLicenseOverload() > 0 ) {
            		EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S038",null);
            	} else 
            	// check logging threshold
                if( PvproInfo.getInstance().isLoggingOverload() ) {
                    EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S037",null);
                } else
	            
        		if (!DirectorMgr.getInstance().isInitialized())
	            {
	                DirectorMgr.getInstance().loadConfiguration();
	
	                if (DirectorMgr.getInstance().isInitialized())
	                {
	                    DirectorMgr.getInstance().startEngine();
	                }
	            }
	
	            EventMgr.getInstance().log(new Integer(1), "System", "Start",
	                EventDictionary.TYPE_INFO, "S007", null);
        	}
        	
        	//2010-4-13, add by Kevin
        	//startEngine calls updateAllWhenLoad(). so calls only when engine is not started
        	if(DirectorMgr.getInstance().isStarted() == false)
        	{
        		DirectorMgr.getInstance().updateAllWhenLoad();
        	}
            Runtime.getRuntime().addShutdownHook(new Thread() { 
            	public void run() { 
            		releaseAll(); 
            	} 
            });
        }
        catch (Throwable e) {
        	e.printStackTrace();
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        // start demo thread; Demo replaced Deader class
        if( BaseConfig.isDemo() ) {
        	objDemo = new Demo(8 * 60); // 8 hours
        	threadDemo = new Thread(objDemo, "demo");
        	threadDemo.start();
        }
    }

    public void contextDestroyed(ServletContextEvent arg0)
    {
        System.out.println("PVStarter - contextDestroyed");
        
        // stop demo thread
        if( objDemo != null ) {
        	objDemo.stop();
        	if( threadDemo != null )
        		threadDemo.interrupt();
        }
        
        releaseAll();
    }
    
    private void releaseAll()
    {
    	DirectorMgr.getInstance().stopPVPRO();
    }
}
