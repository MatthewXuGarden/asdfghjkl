package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public class ExternalPing extends External
{
    private String device = "";

    public ExternalPing(String device)
    {
        super(CommID.SERVICE_SMS, device, "P");
        this.device = "\"" + device + "\"";
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "PingService.jar;" + this.pathex + "DispatcherLight.jar;",
            "com.carel.supervisor.service.Starter", device
        };
    }

    protected String getLogFile()
    {
        return "PingService.log";
    }
}
