package com.carel.supervisor.dispatcher.engine.mail;

public class DispMailMessage
{
    private String sender = "";
    private String receiver = "";
    private String body = "";
    private String subject = "";
    private String attach = "";
    private String[] ccreceivers = null;

    public DispMailMessage(String sender, String receiver, String subject, String body)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.ccreceivers = new String[0];
    }

    public DispMailMessage(String sender, String receiver, String subject, String body, String[] cc)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.ccreceivers = cc;
    }

    public boolean hasCC()
    {
        return this.ccreceivers.length > 0;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getCcreceivers()
    {
        String ret = "";

        for (int i = 0; i < ccreceivers.length; i++)
        {
            ret += (ccreceivers[i] + ";");
        }

        ret = ret.substring(0, ret.length() - 1);

        return ret;
    }

    public void setCcreceivers(String[] ccreceivers)
    {
        this.ccreceivers = ccreceivers;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setAttach(String path)
    {
        this.attach = path;
    }

    public String getAttach()
    {
        return this.attach;
    }

    public boolean hasAttach()
    {
        return (this.attach.length() > 0) ? true : false;
    }
}
