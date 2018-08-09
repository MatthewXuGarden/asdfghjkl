package com.carel.supervisor.dispatcher.comm.field.impl;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.ICommConfig;
import com.carel.supervisor.dispatcher.comm.field.ICommConnector;


public abstract class CommBase extends InitializableBase implements ICommConnector
{
    private static boolean isLoaded = false;
    private String name = null;
    protected boolean blockingError = false;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public static synchronized void loadMe()
    {
        if (!isLoaded)
        {
            isLoaded = true;
            System.loadLibrary("commbase");
        }
    }

    public native short cloadSubSystem();
    public native short cunloadSubSystem();
    public native short cinitSubSystem();
    public native short cdoneSubSystem();
    public native short crunDefaultCommand(String device);
    public native short cgetConfigObject(ICommConfig conf);
    public native short cgetSubSystemMessages(DevicesMessages deviceMessages);
    public native short csetSubSystemMessages(DevicesMessages deviceMessages);
}
