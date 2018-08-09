package com.carel.supervisor.dispatcher.comm.external;

public class ExternalDialRem extends ExternalDial
{
    private String proto = "";
    private String port = "";
    private String url = "";
    private String cert = "";

    public ExternalDialRem(String user, String pass, String number, String centra, String device,
        String action, String proto, String port, String url, String cert)
    {
        super(user, pass, number, centra, device, action);
        this.proto = proto;
        this.port = port;
        this.url = url;
        this.cert = cert;
    }

    protected String[] getParams()
    {
        return new String[]
        {
            "java", "-classpath",
            this.pathex + "DialService.jar;" + this.pathex + "DispatcherLight.jar;",
            "com.carel.supervisor.service.Starter", user, pass, number, centra, device, action,
            proto, port, url, cert
        };
    }

    protected String getLogFile()
    {
        return "DialService.log";
    }
}
