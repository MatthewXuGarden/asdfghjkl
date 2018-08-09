package com.carel.supervisor.remote.commlayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import javax.net.ssl.HttpsURLConnection;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.remote.bean.ImportBeanList;
import com.carel.supervisor.remote.bean.SynchBeanList;
import com.carel.supervisor.remote.engine.Importer;
import com.carel.supervisor.remote.engine.connection.ActiveConnections;
import com.carel.supervisor.remote.manager.RemoteMgr;


public class CommLayerHw extends Poller
{
    private String device = "";
    private String myIP = "";
    private String itIP = "";
    private String urlConn = "";
    private String language = "";
    private SiteInfo site = null;
    private String cert = "";

    CommLayerHw(String proto, String port, String root, String d, String i, String r, String lang,
        String cert)
    {
        this.device = d;
        this.myIP = i;
        this.itIP = r;
        this.language = lang;
        this.cert = cert;
        this.urlConn = proto + "://" + this.itIP + ":" + port + root;
    }

    public void run()
    {
        boolean state = false;

        try
        {
            System.setProperty("javax.net.ssl.trustStore", this.cert);
            
            /*
             * Controllo sulla licenza.
             * Se la licenza è valida e il periodo di prova non è scaduto
             * effettuo la sincronizzazione dei dati.
             */
            if(Information.getInstance().canStartEngineLight())
            {
	            if (checkAuthentication())
	            {
	                state = true;
	
	                try
	                {
	                    Object[] p = {this.site.getCode()};
	                    EventMgr.getInstance().log(new Integer(1),"Remote","Action",EventDictionary.TYPE_INFO,"R000",p);
	                }
	                catch (RuntimeException e1){}
	                
	               
	                SiteInfoList.updateTimeLastDialup(null,this.site.getId(),String.valueOf(System.currentTimeMillis()));
	
	                XMLNode nodes = exportData(this.site.getId());
	
	                if (nodes != null)
	                {
	                    String id = nodes.getNode("id").getTextValue();
	                    String table = "";
	
	                    downloadFile(id, id, "pvpro");
	
	                    XMLNode tables = nodes.getNode("tab");
	
	                    if (tables != null)
	                    {
	                        if (tables.size() > 0)
	                        {
	                            // Start synch message
	                            try
	                            {
	                                EventMgr.getInstance().log(new Integer(1), "Remote", "Action",
	                                    EventDictionary.TYPE_INFO, "R001", null);
	                            }
	                            catch (Exception e1)
	                            {
	                            }
	
	                            // Download files
	                            for (int i = 0; i < tables.size(); i++)
	                            {
	                                table = tables.getNode(i).getTextValue();
	                                downloadFile(id, table, "zip");
	                            }
	
	                            // End synch message
	                            try
	                            {
	                                EventMgr.getInstance().log(new Integer(1), "Remote", "Action",
	                                    EventDictionary.TYPE_INFO, "R006", null);
	                            }
	                            catch (Exception e1)
	                            {
	                            }
	
	                            // Insert row in cfsiteimport
	                            new ImportBeanList().insertImport(null, this.site.getId(), id);
	                        }
	                    }
	                }
	            }
            }
            
            // Close communication with local
            closeHttpConnection(state);
            
            // Remote
            ActiveConnections.getInstance().remConnection(this.device,true);
            
            try
            {
            	if(this.site != null)
            	{
            		Object[] p = {this.site.getCode()};
            		EventMgr.getInstance().log(new Integer(1),"Remote","Action",EventDictionary.TYPE_INFO,"R002",p);
            	}
            }
            catch (RuntimeException e1){}
            
            /*
             * Controllo sulla licenza.
             * Se la licenza è valida e il periodo di prova non è scaduto
             * effettuo la sincronizzazione dei dati.
             */
            if(Information.getInstance().canStartEngineLight())
            {
	            try
	            {
	                new Importer().startImport(null);
	            }
	            catch (Exception e)
	            {
	                Logger logger = LoggerMgr.getLogger(this.getClass());
	                logger.error(e);
	            }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    // -- Step 1
    // -- Check Local Credential
    private boolean checkAuthentication()
    {
        BufferedInputStream bif = null;
        BufferedOutputStream bof = null;
        boolean ris = false;
        String user = "";
        String pass = "";
        String type = "";
        int status = 0;

        try
        {
            HttpsURLConnection conn = openHttpConnection();
            bof = new BufferedOutputStream(conn.getOutputStream());
            bof.write(("remote=" + this.myIP + "&cmd=auth").getBytes());
            bof.flush();
            bof.close();

            bif = new BufferedInputStream(conn.getInputStream());

            byte[] buffer = new byte[bif.available()];
            bif.read(buffer);
            bif.close();

            String credential = new String(buffer);

            XMLNode xmlNode = null;
            XMLNode tmp = null;

            if (credential != null)
            {
                try
                {
                    xmlNode = XMLNode.parse(credential);
                }
                catch (Exception e)
                {
                    xmlNode = null;
                }

                if (xmlNode != null)
                {
                    tmp = xmlNode.getNode("user");

                    if (tmp != null)
                    {
                        user = tmp.getTextValue();
                    }

                    tmp = xmlNode.getNode("pass");

                    if (tmp != null)
                    {
                        pass = tmp.getTextValue();
                    }

                    tmp = xmlNode.getNode("type");

                    if (tmp != null)
                    {
                        type = tmp.getTextValue();
                    }
                    
                    tmp = xmlNode.getNode("status");

                    if (tmp != null)
                    {
                        try {
                        	status = Integer.parseInt(tmp.getTextValue());
                        }catch(Exception e){}
                    }

                    this.site = SiteInfoList.authenticateLocal(null, user, pass, type);

                    if (this.site != null)
                    {
                        ris = true;
                        ActiveConnections.getInstance().putClientOnConnection(this.device,""+this.site.getId());
                        SiteInfoList.updateSiteStatus(null,this.site.getId(),status);
                    }
                }
            }

            conn.disconnect();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return ris;
    }

    // Step 2
    // Request export to Local Site
    private XMLNode exportData(int idSite)
    {
        BufferedInputStream bif = null;
        BufferedOutputStream bof = null;
        XMLNode xmlNode = null;
        Timestamp time = this.site.getLastconnection();

        String tableList = "";
        String[] tables = SynchBeanList.getTableConf(null, idSite);

        for (int i = 0; i < tables.length; i++)
        {
            tableList += (tables[i] + ";");
        }

        if (tableList.length() > 1)
        {
            tableList = tableList.substring(0, tableList.length() - 1);
        }

        long lTime = 0;

        if (time != null)
        {
            lTime = time.getTime();
        }

        try
        {
            HttpsURLConnection conn = openHttpConnection();
            bof = new BufferedOutputStream(conn.getOutputStream());
            bof.write(("remote=" + this.myIP + "&cmd=exp&lang=" + this.language + "&last=" + lTime +
                "&tables=" + tableList).getBytes());
            bof.flush();
            bof.close();

            bif = new BufferedInputStream(conn.getInputStream());

            byte[] buffer = new byte[bif.available()];
            bif.read(buffer);
            bif.close();

            String resp = new String(buffer);

            if (resp != null)
            {
                xmlNode = XMLNode.parse(resp);
            }

            conn.disconnect();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return xmlNode;
    }

    // Step 3
    // Retrive file from 'local' to 'remote' filesystem
    private void downloadFile(String idExp, String fileName, String ext)
    {
        if ((ext != null) && ext.equalsIgnoreCase("pvpro"))
        {
            writeFileTxt(idExp, fileName, ext);
        }
        else if ((ext != null) && ext.equalsIgnoreCase("zip"))
        {
            writeFileZip(idExp, fileName, ext);
        }
    }

    // -- Step 5 
    // -- Goodbye Local
    private void closeHttpConnection(boolean state)
    {
        BufferedInputStream bif = null;
        BufferedOutputStream bof = null;
        String message = "remote=" + this.myIP + "&cmd=close&state=" + (state ? "T" : "F");

        try
        {
            HttpsURLConnection conn = openHttpConnection();
            bof = new BufferedOutputStream(conn.getOutputStream());
            bof.write(message.getBytes());
            bof.flush();
            bof.close();
            bif = new BufferedInputStream(conn.getInputStream());
            bif.close();
            conn.disconnect();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        //Incoming.getInstance().remConnClient(this.device);
    }

    // Open HTTPS connection to Local
    private HttpsURLConnection openHttpConnection()
    {
        HttpsURLConnection conn = null;

        try
        {
            URL url = new URL(this.urlConn);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(true);
            conn.setHostnameVerifier(new HostProNameVerifier());
        }
        catch (Exception e)
        {
            if (conn != null)
            {
                conn.disconnect();
            }

            conn = null;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return conn;
    }
    
    private HttpURLConnection openHttpConn()
    {
        HttpURLConnection conn = null;

        try
        {
            URL url = new URL(this.urlConn);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(true);
        }
        catch (Exception e)
        {
            if (conn != null)
            {
                conn.disconnect();
            }

            conn = null;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return conn;
    }

    private void writeFileTxt(String idExp, String fileName, String ext)
    {
        BufferedInputStream bif = null;
        BufferedOutputStream bof = null;
        FileOutputStream fos = null;
        String resp = "";

        try
        {
            fos = new FileOutputStream(RemoteMgr.getInstance().getPath() + fileName + "." + ext);

            HttpsURLConnection conn = openHttpConnection();
            bof = new BufferedOutputStream(conn.getOutputStream());
            bof.write(("remote=" + this.myIP + "&cmd=get&id=" + idExp + "&file=" + fileName +
                "&ext=" + ext + "").getBytes());
            bof.flush();
            bof.close();

            bif = new BufferedInputStream(conn.getInputStream());

            byte[] buffer = null;

            while (resp != null)
            {
                buffer = new byte[bif.available()];
                bif.read(buffer);
                resp = new String(buffer);

                if ((resp != null) && (resp.length() > 0))
                {
                    fos.write(resp.getBytes());
                }
                else
                {
                    resp = null;
                }
            }

            bif.close();
            fos.flush();
            fos.close();
            conn.disconnect();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void writeFileZip(String idExp, String fileName, String ext)
    {
        FileOutputStream fos = null;
        BufferedInputStream bif = null;
        BufferedOutputStream bof = null;

        try
        {
            HttpsURLConnection conn = openHttpConnection();
            bof = new BufferedOutputStream(conn.getOutputStream());
            bof.write(("remote=" + this.myIP + "&cmd=get&id=" + idExp + "&file=" + fileName +
                "&ext=" + ext + "").getBytes());
            bof.flush();
            bof.close();

            bif = new BufferedInputStream(conn.getInputStream());

            File f = new File(RemoteMgr.getInstance().getPath() + idExp + "_" + fileName + "." +
                    ext);
            fos = new FileOutputStream(f);

            int ch = 0;

            while ((ch = bif.read()) > -1)
            {
                fos.write(ch);
            }

            fos.flush();
            fos.close();
            bif.close();

            conn.disconnect();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    public int checkPveStatus()
    {
    	BufferedInputStream bif = null;
    	int state = 0;
        try
        {
            HttpURLConnection conn = openHttpConn();
            bif = new BufferedInputStream(conn.getInputStream());
            byte[] buffer = new byte[bif.available()];
            bif.read(buffer);
            bif.close();
            conn.disconnect();
            String sHtml = new String(buffer);
            if(sHtml != null)
            {
            	if(sHtml.indexOf("Alr.gif") != -1)
            		state = 2;
            	else
            		state = 1;
            }
        }
        catch(Exception e) {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
        	logger.error(e);
        }
        
        return state;
    }
}
