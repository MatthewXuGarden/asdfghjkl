package com.carel.supervisor.dispatcher.engine.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dispatcher.DispatcherMgr;

public class LanConnection 
{
	private static final String CMD = "llan";
	private static final String PUSR = "code";
	private static final String PPWD = "pass";
	private static final String REDIRECT = "redirect";
	
	private static final String protocol = "https";
	private static final String port = "443";
	private static final String root = "/Remote/remote";
	
	public static boolean startCommLanToRemote(String ipRemoto)
	{
		System.setProperty("javax.net.ssl.trustStore",DispatcherMgr.getInstance().getCertificatePath());
		
		BufferedInputStream bif = null;
        BufferedOutputStream bof = null;
        boolean ris = false;
        String usr = "";
        String pwd = "";
        
        SiteInfo si = null;
        try {
			si = SiteInfoList.retrieveSiteById(1);
			usr = si.getCode();
			pwd = si.getPassword();
		} 
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(LanConnection.class);
            logger.error(e);
        }
        
        try
        {
        	if(si != null)
        	{
	        	HttpsURLConnection conn = openHttpConnection(ipRemoto);
	            bof = new BufferedOutputStream(conn.getOutputStream());
	            // èatch to avoid redirection on 8443 when connectiong to newer RemotePros
	            bof.write((PUSR+"="+usr+"&cmd="+CMD+"&"+PPWD+"="+pwd+"&"+REDIRECT+"=false").getBytes());
	            bof.flush();
	            bof.close();
	
	            bif = new BufferedInputStream(conn.getInputStream());
	
	            byte[] buffer = new byte[conn.getContentLength()];
	            bif.read(buffer);
	            bif.close();
	
	            String credential = new String(buffer);
	            if(credential != null && credential.equalsIgnoreCase("OK"))
            	{
	            	ris = true;
            	}else if( credential != null && credential.equalsIgnoreCase("KO")){
            		Logger logger = LoggerMgr.getLogger(LanConnection.class);
            		logger.info("Please Check if the Identifier and password of the RemoteValue is right  ");
            	}
	            
        	}
        }
        catch (ConnectException e)
        {
        	if(e.getMessage().contains("refused")){
        		Logger logger = LoggerMgr.getLogger(LanConnection.class);
        		logger.info("Please Check if the IP of the RemoteValue is right and if the RemoteValue still works ");
        		
        	}else if(e.getMessage().contains("timed out")){
        		Logger logger = LoggerMgr.getLogger(LanConnection.class);
        		logger.info("Please Check the firewall and IP of RemoteValue is right ");
        	}
        } catch (IOException e) {
        	Logger logger = LoggerMgr.getLogger(LanConnection.class);
    		logger.error(e);
		}
       
        return ris;
	}
	
	private static HttpsURLConnection openHttpConnection(String urlConn)
    {
		String urlPath = protocol+"://"+urlConn+":"+port+root;
        HttpsURLConnection conn = null;
        
        try
        {
            URL url = new URL(urlPath);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(true);
            conn.setHostnameVerifier(new HostProNameVerifier());
        }
        catch (Exception e)
        {
            if (conn != null)
                conn.disconnect();

            conn = null;

            Logger logger = LoggerMgr.getLogger(LanConnection.class);
            logger.error(e);
        }
        return conn;
    }
}
