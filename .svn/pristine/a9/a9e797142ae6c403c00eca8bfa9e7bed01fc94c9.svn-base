package com.carel.supervisor.dispatcher.engine.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.sun.mail.util.MailSSLSocketFactory;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class DispMailSender
{
    private static final String SMTP_PARAM = "mail.smtp.host";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    
    private static final String XMAIL_PARAM = "X-Mailer";
    private static final String XMAIL_PARAM_VAL = "Dispatcher PVPro Mailer";
    private String smtpServer = "";
    private String smtpPort = "25";
    private String smtpUser = "";
    private String smtpPass = "";
    private List messages = null;
    private boolean authEnable = false;
    private String encryption = "NONE";
    
    // Aggiunti parametri per il retry
    private int retrynum = 0;
    private int retryafter = 0;
    private long timetogo = 0L;

    public DispMailSender(String smtp)
    {
        this.smtpServer = smtp;
        this.messages = new ArrayList();
    }

    public DispMailSender(String smtp, DispMailMessage msg)
    {
        this(smtp);
        this.messages.add(msg);
    }
    
    /*
     * Metodo per invio mail tramite SMTP con autenticazione
     * User
     * Password
     */
    public DispMailSender(String smtp,String user,String pass,DispMailMessage msg)
    {
        this(smtp,msg);
        this.smtpUser = user;
        this.smtpPass = pass;
        if(this.smtpUser != null && this.smtpUser.length() > 0)
        	authEnable = true;
    }
    
    public void addMessage(DispMailMessage msg)
    {
        this.messages.add(msg);
    }
    public boolean sendMessage()
    {
    	int result=sendMessageResult();
    	boolean messageok=(result>0)?true:false;
    	return messageok;
    }
    public int sendMessageResult()
    {
    	Authenticator auth = null;
        Session session = null;
        Message msg = null;
        DispMailMessage lmsg = null;
        boolean messageok = true;
        int result=1;

        try
        {
            Properties props = new Properties();
            props.put(SMTP_PARAM, this.smtpServer);
            props.put("mail.smtp.port", this.smtpPort);
            
            if(this.authEnable)
            {
            	props.put(SMTP_AUTH, "true");
            	auth = new SMTPAuthentication(this.smtpUser,this.smtpPass);
            	
            	if( encryption.equalsIgnoreCase("TLS") ) {
                    props.put("mail.smtp.user", smtpUser);
                    //props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.starttls.enable", "true");
                	
                    MailSSLSocketFactory sf = new MailSSLSocketFactory();
                	sf.setTrustAllHosts(true);
                	props.put("mail.smtp.ssl.socketFactory", sf);
            	}
            	else if( encryption.equalsIgnoreCase("SSL") ) {
            		props.put("mail.smtp.user", smtpUser);
            		props.put("mail.smtp.port", "465");
            		props.put("mail.smtp.starttls.enable", "true");
            	    props.put("mail.smtp.ssl.enable", "true");
            		props.put("mail.smtp.socketFactory.port", "465");
            		//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            		props.put("mail.smtp.socketFactory.class", "com.carel.supervisor.dispatcher.engine.mail.DummySSLSocketFactory");
            		props.put("mail.smtp.socketFactory.fallback", "false");
            	}
            	
            }
            else
            {
            	props.put(SMTP_AUTH, "false");
            }
            
            for (int i = 0; i < this.messages.size(); i++)
            {
                lmsg = (DispMailMessage) this.messages.get(i);
                
                session = Session.getInstance(props, auth);
                
                msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(lmsg.getSender()));
                msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(lmsg.getReceiver()));

                if (lmsg.hasCC())
                {
                    msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(lmsg.getReceiver()));
                }

                msg.setSubject(lmsg.getSubject());

                msg.setHeader(XMAIL_PARAM, XMAIL_PARAM_VAL);
                msg.setSentDate(new Date());

                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setContent(lmsg.getBody(),"text/html; charset=UTF-8");
                
                MimeBodyPart mbp2 = null;

                boolean setBodyMsg = true;
                if (lmsg.hasAttach())
                {
                	if(lmsg.getAttach().endsWith("PDF_Alive.pdf"))
                    {
                		setBodyMsg = false;
                    	String aliveContent = ProductInfoMgr.getInstance().getProductInfo().get("alive_mail_content");
    						if(aliveContent == null || (!aliveContent.equals("no")))
    							setBodyMsg = true; 
                    }
                	mbp2 = new MimeBodyPart();
                    mbp2.attachFile(lmsg.getAttach());
                }

                Multipart mp = new MimeMultipart();
                if (setBodyMsg)
                	mp.addBodyPart(mbp1);

                if (mbp2 != null)
                {
                    mp.addBodyPart(mbp2);
                }

                msg.setContent(mp);

               	Transport.send(msg);
            }
        }
        catch (javax.mail.MessagingException jme)
        {
            messageok = false;
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(jme);
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_ERROR, "D000", new Object[] { jme.getMessage() });
            result=-1;
        }
        catch (Exception e)
        {
            messageok = false;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_ERROR, "D000", new Object[] { e.getMessage() });
            result=-1;
        }

        return result;
    }
    
    // Gestione del retry per Email via lan
    public void setRetryNum(int rn) {
        this.retrynum = rn;
    }
    
    public void setRetryAfter(int ra) {
        this.retryafter = ra;
    }
    
    public void resetTimeToGo() 
    {
        this.retrynum--;
        if(this.retrynum < 0)
            this.timetogo = -1L;
        else
            this.timetogo = System.currentTimeMillis() + (this.retryafter * 60000L);
    }
    
    public long getTimeToGo() {
        return this.timetogo;
    }
    
    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
    
    public void setPort(String port)
    {
    	this.smtpPort = port;
    }
}
