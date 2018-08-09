package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public class ExternalDial extends External
{
    protected String user = "";
    protected String pass = "";
    protected String number = "";
    protected String centra = "";
    protected String device = "";
    protected String action = "";

    public ExternalDial(String user, String pass, String number, String centra, String device,
        String action)
    {
        super(CommID.SERVICE_DIAL, device, "D");

        this.user = "\"" + user + "\"";
        this.pass = "\"" + pass + "\"";
        this.number = "\"" + number + "\"";
        this.centra = "\"" + centra + "\"";
        this.device = "\"" + device + "\"";
        this.action = "\"" + action + "\"";
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "DialService.jar;" + this.pathex + "DispatcherLight.jar;",
            "com.carel.supervisor.service.Starter", user, pass, number, centra, device, action
        };
    }

    protected String getLogFile()
    {
        return "DialService.log";
    }
}
