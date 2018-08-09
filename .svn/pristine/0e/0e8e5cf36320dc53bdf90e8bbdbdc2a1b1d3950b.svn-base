package com.carel.supervisor.presentation.assistance;

import java.io.File;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.presentation.bo.helper.BackupHelper;

public class ClearCommisioning
{
    public String user = "";
   
    public ClearCommisioning(String usr)
    {
        this.user = usr; 
    }
    
    public void startWork()
    {
        if(stopEngine())
            if(backupTables())
                if(clearTables())
                {
                    EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S030",this.user);
                }
        // Sempre
        startEngine();
    }
    
    private boolean stopEngine()
    {
        boolean ris = false;
        long sleep = 1000L;
        
        try
        {
            if (DirectorMgr.getInstance().isStarted())
            {
                DirectorMgr.getInstance().stopEngine(this.user);
                sleep = 10000L;
                ControllerMgr.getInstance().reset();
            }
            
            if (DispatcherMgr.getInstance().isServiceRunning())
            {
                DispatcherMgr.getInstance().stopService();
                sleep = 10000L;
            }
            
            ris = true;
            Thread.sleep(sleep);
        }
        catch(Exception e) {}
        return ris;
    }
    
    private boolean backupTables()
    {
        boolean ris = false;
        ScriptInvoker script = new ScriptInvoker();
        String pathBatch = BaseConfig.getCarelPath()+"backup"+File.separator+"commreset.bat";
        long ctime = System.currentTimeMillis();
        try
        {
            script.execute(new String[]{pathBatch,"hsevent",""+ctime},BaseConfig.getLogFile());
            script.execute(new String[]{pathBatch,"hsalarm",""+ctime},BaseConfig.getLogFile());
            script.execute(new String[]{pathBatch,"hsaction",""+ctime},BaseConfig.getLogFile());
            script.execute(new String[]{pathBatch,"hsactionqueue",""+ctime},BaseConfig.getLogFile());
            ris = true;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(BackupHelper.class);
            logger.error(e);
        }
        
        return ris;
    }
    
    private boolean clearTables()
    {
        boolean ris = false;
        String sqlHsEvent = "truncate hsevent";
        String sqlHsAlarm = "truncate hsalarm";
        String sqlHsDocDisp = "truncate hsdocdispsent";
        String sqlHsAction = "truncate hsaction";
        String sqlHsActionQueue = "truncate hsactionqueue";
        
        try
        {
            DatabaseMgr.getInstance().executeStatement(null,sqlHsEvent,null);
            DatabaseMgr.getInstance().executeStatement(null,sqlHsAlarm,null);
            DatabaseMgr.getInstance().executeStatement(null,sqlHsDocDisp,null);
            DatabaseMgr.getInstance().executeStatement(null,sqlHsAction,null);
            DatabaseMgr.getInstance().executeStatement(null,sqlHsActionQueue,null);
            ris = true;
        }
        catch(Exception e){
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return ris; 
    }
    
    private boolean startEngine()
    {
        boolean ris = false;
        
        try
        {
            if(Information.getInstance().canStartEngine())
            {
                if(DirectorMgr.getInstance().isStopped())
                {
                    DirectorMgr.getInstance().reloadConfiguration(this.user);
                    DirectorMgr.getInstance().startEngine(this.user);
    
                    if (!DirectorMgr.getInstance().isStopped())
                    {
                        if (!DispatcherMgr.getInstance().isServiceRunning())
                            DispatcherMgr.getInstance().startService(true);
                    }
                }
            } 
            else
                EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
            
            ris = true;
            Thread.sleep(1000L);
        }
        catch(Exception e){}
        
        return ris;
    }
}
