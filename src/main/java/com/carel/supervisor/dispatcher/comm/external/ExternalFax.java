package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public class ExternalFax extends External
{
    private String receiver = "";
    private String sender = "";
    private String path = "";
    private String numReceiver = "";
    private String numSender = "";
    private String centralino = "";
    private String device = "";

    public ExternalFax(String sender, String receiver, String path, String numReceiver,
        String numSender, String centra, String device)
    {
        super(CommID.SERVICE_FAX, device, "F");
        
        if(sender != null)
        	sender = Replacer.replace(sender, "\"", "'");
        
        if(receiver != null)
        	receiver = Replacer.replace(receiver, "\"", "'");
        
        this.sender = "\"" + sender + "\"";
        this.receiver = "\"" + receiver + "\"";
        this.path = "\"" + path + "\"";
        this.numReceiver = "\"" + numReceiver + "\"";
        this.numSender = "\"" + numSender + "\"";
        this.centralino = "\"" + centra + "\"";
        this.device = "\"" + device + "\"";
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "FaxService.jar;" + this.pathex + "DispatcherLight.jar;",
            "com.carel.supervisor.service.Starter", sender, receiver, path, numReceiver, numSender,
            centralino, device
        };
    }

    protected String getLogFile()
    {
        return "FaxService.log";
    }
}
