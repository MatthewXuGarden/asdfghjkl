package com.carel.supervisor.dispatcher.enhanced;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;


/**
 * @author FabioV
 *
 */
/**
 * @author FabioV
 *
 */
/**
 * @author FabioV
 *
 */
/**
 * @author FabioV
 *
 */
/**
 * @author FabioV
 *
 */
public class SyncLog
{
    private String server = "127.0.0.1";
    private int ftpPort = 21;
    private long dimLogPrec = 0;
    private long dimLog = 0;
    private long dimL00 = 0;
    private long dimL00Prec = 0;
    private String User = "PVRemote";
    private String Password = "";
    private String dateString = "";
    private SyncLoader syncLoader = null;
    private String AlarmsDir = "";
    private FTPClient ftp = null;
    private LinkedList<ProExportRecord> proexport;
	private boolean initializedProExport = false;

    //Costruttore
    public SyncLog(String server, int ftpPort, String User, String Password)
    {
        this.server = server;
        this.ftpPort = ftpPort;

        //this.dimLogPrec = dimLogPrec;
        //this.dimLog = dimLog;
        this.User = User;
        this.Password = Password;

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        dateString = sdf.format(dt);
    }

    public SyncLog(String server, long dimLog, String User, String Password)
    {
        this(server, (int) 21, User, Password);
    }

    public SyncLog(long dimLog, String User, String Password)
    {
        this("127.0.0.1", (int) 21, User, Password);
    }

    public void Finalize()
    {
        File f = new File(dateString + "_nd_0.ncf");
        f.delete();
        f = null;

        f = new File(dateString + "_driver.ini");
        f.delete();
        f = null;

        f = new File(dateString + "_driver.cct");
        f.delete();
        f = null;
        syncLoader = null;
    }

    public SyncRecNode GetNode()
    {
        if (syncLoader != null)
        {
            return syncLoader.GetNode();
        }
        else
        {
            return null;
        }
    }

    public Map GetNodeConfiguration()
    {
        if (syncLoader != null)
        {
            return syncLoader.GetNodeConfiguration();
        }
        else
        {
            return null;
        }
    }

    /**
     * @author FabioV
     *Se riesce ad allineare ritorna vero, e la nuova dimensione del log.
     */
    public boolean DoAlign(Vector Alarms)
    {
        ftp = new FTPClient();

        if (OpenConnection() && Login())
        {
            //Scarico la configurazione
            if (DownloadConfig())
            {
                if (DownLoadLog())
                {
                    CopyFiles(dateString + "_nd_0.ncf", AlarmsDir + "/nd_0.ncf");
                    CopyFiles(dateString + "_driver.ini", AlarmsDir + "/driver.ini");
                    CopyFiles(dateString + "_driver.cct", AlarmsDir + "/driver.cct");
                    
                    /*fabio boldrin arricchimento sync*/
                    try{
	                    File oldf;
	                    if((oldf = new File(AlarmsDir + "/pro_export.ini")).exists())
	                    {
	                    	if(new File(oldf.getAbsolutePath()+".old").exists())
	                    		new File(oldf.getAbsolutePath()+".old").delete();
	                    	oldf.renameTo(new File(oldf.getAbsolutePath()+".old"));
	                    }
	                    CopyFiles(dateString + "_pro_export.ini", AlarmsDir + "/pro_export.ini");
                    }
                    catch (Exception e)
                    {
                    	LoggerMgr.getLogger(this.getClass()).error(e);
                    }
                    /*---*/

                    //Adesso in base ai valori delle dimensioni carico 
                    //le parti che mi interessano in un array da restituire verso il lato db.
                    syncLoader = new SyncLoader(AlarmsDir, dimLog, dimL00, dimLogPrec, dimL00Prec);

                    syncLoader.Alarms(Alarms);
                }
            }

            //Devo allineare i log 
            CloseConnection();
        }
        else
        {
            return false;
        }

        ftp = null;

        return true;
    }

    public LinkedList<ProExportRecord> loadPVPexport()
	{
    	initializedProExport = false;
    	proexport = new LinkedList<ProExportRecord>();
    	try
		{
			LineNumberReader lnr = new LineNumberReader(new FileReader(AlarmsDir + "/pro_export.ini"));
			String linestr="";
			while((linestr = lnr.readLine())!=null)
			{
				StringTokenizer strtok = new StringTokenizer(linestr, ";_=");
				if("VAR".equalsIgnoreCase(strtok.nextToken()))
				{
					int gid = Integer.parseInt(strtok.nextToken());
					int add = Integer.parseInt(strtok.nextToken());
					int type = Integer.parseInt(strtok.nextToken());
					int powerdeg = Integer.parseInt(strtok.nextToken());
					String description = strtok.nextToken();
					proexport.add(new ProExportRecord(gid,add,type,powerdeg,description));
				}
			}
			initializedProExport = true;
			return proexport;
		} catch (Exception e)
		{
			initializedProExport  = false;
			e.printStackTrace();
			return null;
		}
	}
    
    public LinkedList<ProExportRecord> getPVPexport()
	{
    	if (initializedProExport)
    		return proexport;
    	else
    		return null;
	}

	/**
     * QUESTA FUNZIONE APRE LA CONNESSIONE FTP AL SERVER IN UNA CERTA PORTA
     * TCP.
     */
    private boolean OpenConnection()
    {
        try
        {
            //int reply;
            ftp.setRemoteHost(server);
            ftp.setRemotePort(ftpPort);
            ftp.setTimeout(500000);
            ftp.connect();
            System.out.println("Connected to " + server + ".");
        }
        catch (FTPException e)
        {
            if (ftp.connected())
            {
                try
                {
                    ftp.quit();
                }
                catch (FTPException g)
                {
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }

            System.err.println("Could not connect to server.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            if (ftp.connected())
            {
                try
                {
                    ftp.quit();
                }
                catch (FTPException g)
                {
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }

            System.err.println("Could not connect to server.");
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * QUESTA FUNZIONE CHIUDE LA CONNESSIONE FTP
     */
    private void CloseConnection()
    {
        try
        {
            try
            {
                ftp.quit();
            }
            catch (FTPException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (IOException f)
        {
            // do nothing
        }
    }

    private boolean Login()
    {
        try
        {
            ftp.login(User, Password);
        }
        catch (FTPException e)
        {
            //TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

        return true;
    }

    private boolean DownloadConfig()
    {
        //Use passive mode as default because most of us are
        // behind firewalls these days.
        //ftp.enterLocalPassiveMode();
        try
        {
            ftp.chdir("data");
            ftp.chdir("nodes");
            ftp.chdir("node0");

            OutputStream output;

            output = new FileOutputStream(dateString + "_nd_0.ncf");
            ftp.get(output, "nd_0.ncf");

            output.close();
            output = null;

            output = new FileOutputStream(dateString + "_driver.ini");
            ftp.get(output, "driver.ini");

            output.close();
            output = null;

            output = new FileOutputStream(dateString + "_driver.cct");
            ftp.get(output, "driver.cct");

            output.close();
            output = null;
            
            /*fabio boldrin - arricchimento sync*/
            try
            {
	            output = new FileOutputStream(dateString + "_pro_export.ini");
	            ftp.chdir("/data");
	            ftp.get(output, "pro_export.ini");
	            ftp.chdir("nodes");
	            ftp.chdir("node0");
            }
            catch(Exception ex)
            {
            	LoggerMgr.getLogger(this.getClass()).info("No pro export");
            }
            /*---*/
            
            /* long size = ftp.size("alarms.log");
             System.out.println("Size in byte = "+size);*/
        }
        catch (FTPException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        	LoggerMgr.getLogger(this.getClass()).error(e);

            return false;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        	LoggerMgr.getLogger(this.getClass()).error(e);

            return false;
        }

        return true;
    }

    boolean DownLoadLog()
    {
        //Per prima cosa mi ricavo l'ident del nodo, in modo da riconoscere in quale cartella
        //ho archiviato i file di log
        INIFile objINI = new INIFile(dateString + "_nd_0.ncf");

        try
        {
            Integer ident = objINI.getIntegerProperty("Node", "Ident");
            objINI = null;

            //Adesso per prima cosa vado a vedere se il file esiste nella cartella nodeIdent, e mi prendo
            //la dimensione del file .log e .l00 e poi faccio il restart se la dimensione è 
            //diversa da zero, se no lo scarico tutto.
            AlarmsDir = "node" + ident.toString();

            File alarmsLog = new File(AlarmsDir + "/alarms.log");
            File alarmsL00 = new File(AlarmsDir + "/alarms.l00");
            Date dateL00L = new Date(alarmsL00.lastModified());
            Date dateL00R = null;

            try
            {
                dateL00R = ftp.modtime("alarms.l00");
                dimL00 = ftp.size("alarms.l00");
            }
            catch (FTPException e)
            {
                dimL00 = 0;
                dateL00R = null;
            }

            dimLogPrec = alarmsLog.length();
            ;
            dimLog = ftp.size("alarms.log");
            dimL00Prec = alarmsL00.length();
            ftp.setType(FTPTransferType.BINARY);

            try
            {
                File dir = new File(AlarmsDir);
                dir.mkdir();
                dir = null;
            }
            catch (Exception e)
            {
            }

            //Il file 00 esiste sul cartella ma non c'è in ftp
            if (alarmsL00.exists() && (dimL00 == 0))
            {
                //Siamo in una situazione anomala nel locale, azzero i log nel remoto
                if (alarmsLog.delete() && alarmsL00.delete())
                {
                    //Adesso scarico  tutto il file di log
                    File dir = new File(AlarmsDir);
                    dir.mkdir();
                    dir = null;
                }

                //Scarico di nuovo il file alarms.log e lo processo
            }
            else if (((dateL00R != null) && dateL00L.after(dateL00R)) ||
                    (!alarmsL00.exists() && (dimL00 > 0)))
            {
                alarmsL00.delete();
                alarmsLog.renameTo(alarmsL00);

                ftp.resume();
                ftp.get(AlarmsDir + "/alarms.l00", "alarms.l00");
                alarmsL00.setLastModified(dateL00R.getTime());
                dimLogPrec = 0; //Scarico tutto il file alarms.log				
            }

            ftp.resume();
            ftp.get(AlarmsDir + "/alarms.log", "alarms.log");

            try
            {
            	ftp.chdir("..");
            	ftp.chdir("..");
                ftp.delete("online.txt");
            }
            catch (Exception ne)
            {
            }

            alarmsL00 = null;
            alarmsLog = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        	LoggerMgr.getLogger(this.getClass()).error(e);

            return false;
        }

        return true;
    }

    private void CopyFiles(String fileNameOrig, String fileNameCopy)
    {
        try
        {
            // Create channel on the source
            FileChannel srcChannel = new FileInputStream(fileNameOrig).getChannel();

            // Create channel on the destination
            FileChannel dstChannel = new FileOutputStream(fileNameCopy).getChannel();

            // Copy file contents from source to destination
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

            // Close the channels
            srcChannel.close();
            dstChannel.close();
            srcChannel = null;
            dstChannel = null;
        }
        catch (IOException e)
        {
        }
    }
}
