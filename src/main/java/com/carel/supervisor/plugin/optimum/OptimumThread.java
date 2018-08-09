package com.carel.supervisor.plugin.optimum;

import java.util.Calendar;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;

public class OptimumThread  extends Poller{

	private long pollTime = 60000; // poll cycle: 1 minute
	
	private int currentDay;
	private int currentMonth;
	private int currentYear;
	private Calendar cal;
	
	public OptimumThread (long pollTime)
	{
		try
		{	
			this.setName("optimum");
			this.pollTime = pollTime;
			cal = Calendar.getInstance();
			currentDay = cal.get(Calendar.DAY_OF_MONTH);
			currentMonth = cal.get(Calendar.MONTH);
			currentYear = cal.get(Calendar.YEAR);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
		}
	}
	
	public void run()
    {
    	LoggerMgr.getLogger(this.getClass()).info(this.getClass()+" plugin start");

    	try {
    		do{
    			cal = Calendar.getInstance();
    			boolean isDateChanged = checkDateChange(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    			
    			if( DirectorMgr.getInstance().isStarted() && DispatcherMgr.getInstance().isServiceRunning() )
    			{
    				if (isDateChanged)
    					OptimumManager.getInstance().updateStartStop();
    				OptimumManager.getInstance().executeStartStop();
    			}
    			
    			if( DirectorMgr.getInstance().isStarted() && DispatcherMgr.getInstance().isServiceRunning() )    			
    				OptimumManager.getInstance().getLights().execute();

    			if( DirectorMgr.getInstance().isStarted() && DispatcherMgr.getInstance().isServiceRunning() ) {
    				OptimumManager.getInstance().executeNightFreeCooling();
    			}
    			
        		// TODO: improve the polling mechanism --> poll every 'pollTime' considering the execution time
        		Thread.sleep(pollTime);
    		}
    		while(this.isStarted());
		}
    	catch (InterruptedException e1) {
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}
    }
	
	protected boolean checkDateChange(int day, int month, int year)
	{
		if(day != currentDay || month != currentMonth || year != currentYear)
		{
			currentDay = day;
			currentMonth = month;
			currentYear = year;
			return true;
		}
			
		return false;
	}
}
