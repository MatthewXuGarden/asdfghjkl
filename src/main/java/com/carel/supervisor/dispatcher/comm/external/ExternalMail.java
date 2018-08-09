package com.carel.supervisor.dispatcher.comm.external;

import com.carel.supervisor.dispatcher.comm.field.impl.CommID;


public class ExternalMail extends External
{
    private String user = "";
    private String pass = "";
    private String device = "";
    private String sender = "";
    private String receiver = "";
    private String body = "";
    private String subject = "";
    private String attach = "";
    private String smtp = "";

    public ExternalMail(String u, String p, String send, String rec, String body, String sub,
        String path, String smtp, String device)
    {
        super(CommID.SERVICE_DIALER, device, "E");

        this.user = "\"" + u + "\"";
        this.pass = "\"" + p + "\"";
        this.sender = "\"" + send + "\"";
        this.receiver = "\"" + rec + "\"";
        this.body = "\"" + body + "\"";
        this.subject = "\"" + sub + "\"";
        this.attach = "\"" + path + "\"";
        this.smtp = "\"" + smtp + "\"";
        this.device = "\"" + device + "\"";
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "MailService.jar;" + this.pathex + "DispatcherLight.jar;" + this.pathex +
            "smtp.jar;" + this.pathex + "activation.jar;" + this.pathex + "mail.jar;",
            "com.carel.supervisor.service.Starter", user, pass, sender, receiver, body, subject,
            attach, smtp, device
        };
    }

    protected String getLogFile()
    {
        return "MailService.log";
    }
}
