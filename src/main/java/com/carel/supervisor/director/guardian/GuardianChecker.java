package com.carel.supervisor.director.guardian;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class GuardianChecker extends Thread
{
    private boolean stop = false;
    private long timestamp = 0;
    private boolean isGuardianRunning = false;
    private int count = 0;
    
    private boolean nop = false;
    
    public GuardianChecker()
    {
    	setName("GuardianChecker");
    }
    
    public void setNop() {
        this.nop = true;
        try {
            Thread.sleep(1000L);
        }catch(InterruptedException ie){}
        GuardianCheck.stopGuardian();
    }
    
    private void clearGuardianTables()
    {
        String sql = "truncate alarmsctrl";
        try {
            DatabaseMgr.getInstance().executeStatement(null,sql,null);
        }catch(Exception e){}
        
        sql = "truncate hsalarmsctrl";
        try {
            DatabaseMgr.getInstance().executeStatement(null,sql,null);
        }catch(Exception e){}
        
        sql = "truncate rulestate";
        try {
            DatabaseMgr.getInstance().executeStatement(null,sql,null);
        }catch(Exception e){}
    }
    
    public void resetNop() 
    {
        clearGuardianTables();
        this.nop = false;
    }
    
    public void run()
    {
        while (!stop)
        {
            // there is no control over services start order
        	// so it is ok to wait until the 1st read
        	try {
                Thread.sleep(60L * 1000L);
            }
            catch (InterruptedException e){}

        	timestamp = System.currentTimeMillis();
            
            if(!this.nop)
            {
                if( GuardianCheck.isLive() )
                {
                    isGuardianRunning = true;
                    count = 0;
                }
                else
                {
                	count++;
                	isGuardianRunning = false;
                	if (count >= 10)
                	{
    	                EventMgr.getInstance().error(new Integer(1), "System", "Action", "G003", null);
    	                GuardianCheck.restartGuardian();
    	                count = 0;
                	}
                }
            }
        }
    }

    public void stopCheck()
    {
        this.stop = true;
    }

    public long getLastTimeStamp()
    {
        return timestamp;
    }

    public boolean isGuardianRunning()
    {
        return isGuardianRunning || timestamp == 0; // it is ok to return true until the 1st read
    }
}
