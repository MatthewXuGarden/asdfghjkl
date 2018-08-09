package supervisor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.main.DispDocLinker;
import com.carel.supervisor.dispatcher.main.DispatcherDQ;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.remote.engine.Exporter;
import com.carel.supervisor.remote.manager.RemoteMgr;

public class SRVLRemote extends HttpServlet
{
    private static final String CHECK = "CHECK";
    private static final String AUTH = "AUTH";
    private static final String CLOSE = "CLOSE";
    private static final String END = "END";
    private static final String EXPORT = "EXP";
    private static final String GET = "GET";
    
    private static final String RAS = "RASIN";
    private static final String DIAL = "DIALIN";
    private static final String LLAN = "LLAN";
    
    private static final String VER = "VER";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doCommand(request, response, "GET");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doCommand(request, response, "POST");
    }

    public void doCommand(HttpServletRequest request, HttpServletResponse response, String method)
        throws ServletException, IOException
    {
        response.setContentType("text/html");

        Properties prop = ServletHelper.retrieveParameters(request);
        String rcmd = prop.getProperty("cmd");

        if (rcmd != null)
        {
        	//add by Kevin, bug 10533, RemotePRO alignment plugin is a must
        	if(!PacketMgr.getInstance().isFunctionAllowed("datatransfer"))
        		return;
            if (rcmd.equalsIgnoreCase(CHECK))
            {
                PrintWriter out = response.getWriter();
                RCMDCheck(prop, out);
                out.flush();
                out.close();
            }
            else if (rcmd.equalsIgnoreCase(AUTH))
            {
                PrintWriter out = response.getWriter();
                RCMDAuthentication(prop, out);
                out.flush();
                out.close();
            }
            else if (rcmd.equalsIgnoreCase(CLOSE))
            {
                PrintWriter out = response.getWriter();
                RCMDClose(prop, out);
                out.flush();
                out.close();
            }
            else if (rcmd.equalsIgnoreCase(END))
            {
            	String str = ProductInfoMgr.getInstance().getProductInfo().get("needRemoteACK");
            	boolean needRemoteACK = Boolean.valueOf(str!=null?str:"false");
            	if(needRemoteACK)
            		actionSuccess(prop);
            }
            else if (rcmd.equalsIgnoreCase(EXPORT))
            {
                PrintWriter out = response.getWriter();
                RCMDExportData(prop, out);
                out.flush();
                out.close();
            }
            else if (rcmd.equalsIgnoreCase(GET))
            {
                try
                {
                    RCMDsendFile(prop, response);
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
            /*
             * Richiamato dal servizio di RAS
             */
            else if (rcmd.equalsIgnoreCase(RAS))
            {
                String ret = "KO";
                String ipLocal = request.getParameter("local");
                String ipRemote = request.getParameter("remote");
                String device = request.getParameter("device");

                PrintWriter out = response.getWriter();
                out.write(ret);
                out.flush();
                out.close();
            }
            /*
             * Richiamato dal servizio di DIAL
             */
            else if (rcmd.equalsIgnoreCase(DIAL))
            {
                String ret = "KO";
                String ipLocal = request.getParameter("local");
                String ipRemote = request.getParameter("remote");

                PrintWriter out = response.getWriter();
                out.write(ret);
                out.flush();
                out.close();
            }
            /*
             * Punto di ingresso del locale in caso di connessione con il remoto via LAN
             */
            else if (rcmd.equalsIgnoreCase(LLAN))
            {
            	SiteInfo si = null;
            	String ret = "KO";
                String ipLocal = request.getRemoteAddr();
                String usr     = request.getParameter("code");
                String pwd     = request.getParameter("pass");
                
                si = SiteInfoList.authenticateLocal(null,usr,pwd,"PVP");
                if(si != null)
                {
                	ret = "OK";
                	RemoteMgr.getInstance().openLocalSite(si.getId(),ipLocal,"LAN","PVP");
                }
                
                PrintWriter out = response.getWriter();
                out.write(ret);
                out.flush();
                out.close();
            }
            else if ( rcmd.equalsIgnoreCase(VER) ) {
                PrintWriter out = response.getWriter();
            	RCMDVersion(out);
                out.flush();
                out.close();
            }
        }
    }
    
    // METODI CHIAMATI SUL LOCALE
    
    private void RCMDCheck(Properties prop, PrintWriter out)
    {
        try {
            out.write("OK");
        }
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void RCMDAuthentication(Properties prop, PrintWriter out)
    {
        StringBuffer sb = new StringBuffer();
        SiteInfo si = null;
        String status = "1";
        
        try
        {
            si = SiteInfoList.retrieveSiteById(1);
        }
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        try 
        {
			if(DeviceStatusMgr.getInstance().existAlarm()) 
				status = "2";
        }
        catch(Exception e) {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        sb.append("<response for='auth'>");
        sb.append("<user><![CDATA[" + ((si != null) ? si.getCode() : "") + "]]></user>");
        sb.append("<pass><![CDATA[" + ((si != null) ? si.getPassword() : "") + "]]></pass>");
        sb.append("<type><![CDATA[" + ((si != null) ? si.getType() : "") + "]]></type>");
        sb.append("<status><![CDATA[" + status + "]]></status>");
        sb.append("</response>");

        try
        {
            out.write(sb.toString());
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void RCMDExportData(Properties prop, PrintWriter out)
    {
        String lang = prop.getProperty("lang");
        String last = prop.getProperty("last");
        String tabs = prop.getProperty("tables");
        String[] tabList = new String[0];

        long lLast = 0;

        try
        {
            lLast = Long.parseLong(last);
        }
        catch (Exception e)
        {
        }

        if (tabs != null)
        {
            tabList = StringUtility.split(tabs, ";");
        }

        try
        {
            Exporter exp = new Exporter(lang, lLast);

            if (exp.export(null, tabList))
            {
                out.write(exp.getMessageToRemote());
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void RCMDsendFile(Properties prop, HttpServletResponse response)
        throws Exception
    {
        String ext = prop.getProperty("ext");

        if ((ext != null) && ext.equalsIgnoreCase("pvpro"))
        {
            PrintWriter out = response.getWriter();
            sendTextFile(prop, out);
            out.flush();
            out.close();
        }
        else if ((ext != null) && ext.equalsIgnoreCase("zip"))
        {
            BufferedOutputStream bof = new BufferedOutputStream(response.getOutputStream());
            sendZipFile(prop, bof);
            bof.flush();
            bof.close();
        }
    }

    private void sendZipFile(Properties prop, BufferedOutputStream out)
    {
        String id = prop.getProperty("id");
        String file = prop.getProperty("file");
        String ext = prop.getProperty("ext");
        FileInputStream fis = null;

        try
        {
            File f = new File(RemoteMgr.getInstance().getPath() + id + "_" + file + "." + ext);
            fis = new FileInputStream(f);
            int ch = 0;

            while ((ch = fis.read()) > -1)
                out.write(ch);

            fis.close();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void sendTextFile(Properties prop, PrintWriter out)
    {
        String id = prop.getProperty("id");
        String ext = prop.getProperty("ext");
        String line = "";

        try
        {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(RemoteMgr.getInstance()
                                                                                           .getPath() +
                        id + "." + ext));
            byte[] buffer = new byte[bis.available()];

            while (line != null)
            {
                buffer = new byte[bis.available()];
                bis.read(buffer);
                line = new String(buffer);

                if ((line != null) && (line.length() > 0))
                {
                    out.write(line);
                }
                else
                {
                    line = null;
                }
            }

            bis.close();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void RCMDClose(Properties prop, PrintWriter out)
    {
        DispatcherMgr.getInstance().setServiceDialState(false);

        String sdev = prop.getProperty("remote");
        String ssta = prop.getProperty("state");

        if (sdev != null)
            DispatcherMgr.getInstance().resetDeviceStatus(sdev);
        
        if(ssta != null && ssta.equalsIgnoreCase("F"))
        {
        	try {
				EventMgr.getInstance().log(new Integer(1),"Remote","Action",new Integer(2),"R007",null);
			} 
        	catch (Exception e1){}
        }
    }

    private void RCMDVersion(PrintWriter out)
	{
	    try {
	    	String version = BaseConfig.getProductInfo("version");
	    	String fix = BaseConfig.getProductInfo("fix");
	    	if( fix != null && !fix.isEmpty() )
	    		version += "." + fix;
	        out.write(version);
	    }
	    catch (Exception e) {
	        LoggerMgr.getLogger(this.getClass()).error(e);
	    }
	}
    private void actionSuccess(Properties prop)
    {
		HSActionQBeanList obj = new HSActionQBeanList();
		DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
        HSActionQBean action = obj.getLastRemoteAction(zMem.getModemId());
        if(action != null)
        {
        	String recName = DispatcherDQ.getReceiverString(action.getReceivers(),action.getType());
        	try
            {
                new HSActionQBeanList().sendedScheduledAction(action, action.getChannel());

                try
                {
                    Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
                    Object[] p = 
                    {
                         DispatcherMgr.getInstance().decodeActionType(action.getType()), recName
                    };
                    
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                               EventDictionary.TYPE_INFO, "D041", p);
                }
                catch (Exception e1)
                {
                	e1.printStackTrace();
                	Logger logger = LoggerMgr.getLogger(this.getClass());
                	logger.error(e1);
                }
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
    }
}