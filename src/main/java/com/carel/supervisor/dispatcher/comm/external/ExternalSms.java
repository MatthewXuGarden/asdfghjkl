package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public class ExternalSms extends External
{
    private String prefisso = "";
    private String receiver = "";
    private String filePath = "";
    private String provider = "";
    private String calltype = "";
    private String message = "";
    private String device = "";

    public ExternalSms(String pref, String receiver, String path, String provi, String type,
        String msg, String device)
    {
        super(CommID.SERVICE_SMS, device, "S");
        
        /*
         * Introduzione controllo caratteri "
         */
        if(msg != null)
        	msg = Replacer.replace(msg, "\"", "'");
        
        this.prefisso = "\"" + pref + "\"";
        this.receiver = "\"" + receiver + "\"";
        this.filePath = "\"" + path + "\"";
        this.provider = "\"" + provi + "\"";
        this.calltype = "\"" + type + "\"";
        this.message = "\"" + msg + "\"";
        this.device = "\"" + device + "\"";
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "SmsService.jar;" + this.pathex + "DispatcherLight.jar;",
            "com.carel.supervisor.service.Starter", prefisso, receiver, filePath, provider, calltype,
            message, device
        };
    }

    protected String getLogFile()
    {
        return "SmsService.log";
    }
}
