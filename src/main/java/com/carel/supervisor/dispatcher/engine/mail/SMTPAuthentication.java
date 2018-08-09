package com.carel.supervisor.dispatcher.engine.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthentication extends Authenticator
{
	private String username = "";
    private String password = "";
    
    public SMTPAuthentication(String user,String pass)
    {
    	this.username = user;
    	this.password = pass;
    }
    
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
