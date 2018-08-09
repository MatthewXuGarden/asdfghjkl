package com.carel.supervisor.plugin.optimum;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import java.util.Vector;


public class OptimumManager
{
    private String defaultLanguage = "EN_en";
    private OptimumThread ot = null;

	private static OptimumManager myInstance = new OptimumManager();

    public static OptimumManager getInstance()
    {
    	return myInstance;
    }

    public void init() throws DataBaseException
    {
    	defaultLanguage = loadDefaultLanguage();
		
	    // load start stop algorithms
    	abeanStartStop.clear();
	    StartStopBean[] listStartStop = StartStopBean.getStartStopList();
	    for(int i = 0; i < listStartStop.length; i++)
	    	abeanStartStop.add(listStartStop[i]);
	    // load night free cooling algorithms
	    abeanNightFreeCooling.clear();
	    NightFreeCoolingBean[] listNightFreeCooling = NightFreeCoolingBean.getNightFreeCoolingList();
	    for(int i = 0; i < listNightFreeCooling.length; i++)
	    	abeanNightFreeCooling.add(listNightFreeCooling[i]);
	    // load lights configuration
	    beanLights = new LightsBean();
    }
    
    private String loadDefaultLanguage() throws DataBaseException
    {
    	String sql = "select languagecode from cfsiteext where isdefault=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{"TRUE"});
    	return rs.get(0).get(0).toString();
    }

    //*** START PLUGIN ***
    public void startOptimum(String user) throws DataBaseException
    {
    	init();
        
    	ot = new OptimumThread(60000);
        
        if (ot.isStopped())
        {
        	ot.startPoller();
        }

	    if (ot.isStarted())
	    {
	        //log plugin (package) start ??
	    }
    }
    
    // *** stop plugin ***
    public void stopOptimum(String user)
    {
    	if(ot != null)
    		ot.stopPoller();
    	ot = null;
        
    	//log plugin (package) stop ??
    }


    // Optimum Start/Stop
    private Vector<StartStopBean> abeanStartStop = new Vector<StartStopBean>(); 
                                
    
    public synchronized StartStopBean getStartStop(int idAlgorithm)
    {
    	for(int i = 0; i < abeanStartStop.size(); i++)
    		if( abeanStartStop.get(i).getAlgorithId() == idAlgorithm )
    			return abeanStartStop.get(i);
    	return new StartStopBean();
    }
    
    
    public synchronized void addStartStop(StartStopBean bean)
    {
    	for(int i = 0; i < abeanStartStop.size(); i++)
    		if( abeanStartStop.get(i).getAlgorithId() == bean.getAlgorithId() )
    			return;
    	abeanStartStop.add(bean);
    }


    public synchronized void delStartStop(StartStopBean bean)
    {
    	for(int i = 0; i < abeanStartStop.size(); i++) {
    		if( abeanStartStop.get(i).getAlgorithId() == bean.getAlgorithId() ) {
    			abeanStartStop.remove(i);
    			return;
    		}
    	}
    }
    
    
    public synchronized void updateStartStop()
    {
    	for(int i = 0; i < abeanStartStop.size(); i++)
    	{
    		abeanStartStop.get(i).resetTimeBand();
    		abeanStartStop.get(i).updateTimeBand();
    		abeanStartStop.get(i).resetAllFlags();
    	}
    		
    }


    public synchronized void resetStartStop()
    {
    	for(int i = 0; i < abeanStartStop.size(); i++)
    		abeanStartStop.get(i).resetAllFlags();
    }

    
    public synchronized void executeStartStop()
    {
    	for(int i = 0; i < abeanStartStop.size(); i++)
    		abeanStartStop.get(i).execute();
    }
    

    // Optimum NightFreeCooling
    private Vector<NightFreeCoolingBean> abeanNightFreeCooling = new Vector<NightFreeCoolingBean>();
    
    
    public synchronized NightFreeCoolingBean getNightFreeCooling(int idAlgorithm)
    {
    	for(int i = 0; i < abeanNightFreeCooling.size(); i++)
    		if( abeanNightFreeCooling.get(i).getAlgorithId() == idAlgorithm )
    			return abeanNightFreeCooling.get(i);
    	return new NightFreeCoolingBean();
    }
    
    
    public synchronized void addNightFreeCooling(NightFreeCoolingBean bean)
    {
    	for(int i = 0; i < abeanNightFreeCooling.size(); i++)
    		if( abeanNightFreeCooling.get(i).getAlgorithId() == bean.getAlgorithId() )
    			return;
    	abeanNightFreeCooling.add(bean);
    }


    public synchronized void delNightFreeCooling(NightFreeCoolingBean bean)
    {
    	for(int i = 0; i < abeanNightFreeCooling.size(); i++) {
    		if( abeanNightFreeCooling.get(i).getAlgorithId() == bean.getAlgorithId() ) {
    			abeanNightFreeCooling.remove(i);
    			return;
    		}
    	}
    }
    
    
    public synchronized void executeNightFreeCooling()
    {
    	for(int i = 0; i < abeanNightFreeCooling.size(); i++) {
    		NightFreeCoolingBean bean = abeanNightFreeCooling.get(i);
    		if( bean.isSunriseSchedule() && beanLights.isConfigured() )
    			bean.setTimeOff(beanLights.getSunrise());
    		bean.execute();
    	}
    }
    
    
    // Optimum Lights
    private LightsBean beanLights = new LightsBean();
    
    
    public LightsBean getLights()
    {
    	return beanLights;
    }
}
