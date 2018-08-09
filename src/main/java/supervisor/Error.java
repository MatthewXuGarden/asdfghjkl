package supervisor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.io.FileSystemUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;


public class Error extends HttpServlet
{
	private static final String TABBODY_PATH = "/arch/include/Tabbody.jsp";
	private static final String CONTENT = "text/xml; charset=UTF-8";
	
    public Error()
    {
        super();
    }

    public void destroy()
    {
        super.destroy(); // Just puts "destroy" string in log

        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {	
    	response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
    	
        UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        Properties properties = ServletHelper.retrieveParameters(request);
        
        String cmd = properties.getProperty("cmd");
        String mail_body = properties.getProperty("mail_body");
        String dest_email = properties.getProperty("dest_email");
    	
        String xmlResp = "";
        
        if (cmd.equals("savelog") || cmd.equals("sendmail")) //zip logs. Calls diagnose.bat
        {
	        xmlResp = "<response>";
		    String command = BaseConfig.getCarelPath()+"diagnose"+File.separator+"diagnose.bat";      
		    try 
		    {    
		        Process child = Runtime.getRuntime().exec(command);    
		        InputStream in = child.getInputStream();    
		        in.close();     
	            child.waitFor();
	            String defaultpath = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator+"diagnose.zip";
	            String local = properties.getProperty("local");
	            String file = defaultpath;
	            if("true".equalsIgnoreCase(local))
	            {
	              String path = properties.getProperty("path");
	          	  
	           	  if(!path.equals(""))
	           	  {
	           		  File fin = new File(defaultpath);
		           	  File fout = new File(path);
	           		  FileSystemUtils.copyFile(fin, fout);
	           		  file = path;
	           	  }
	            }
	            else
	            {
	            	File fin = new File(defaultpath);
	            	if(!fin.exists())
	            		throw new Exception();
	            	String path = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + 
        			File.separator+"diagnose_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".zip";
	            	File fout = new File(path);
	            	fin.renameTo(fout);
	            	file = path;
	            }
	            
	            // send zip file by email
	            if(cmd.equals("sendmail"))
	            {
	            	String res = sendEmailLogs(us,dest_email,mail_body,file);
	            	xmlResp += res;
	            	xmlResp += "</response>";
	            }
	            else
	            {
	            	xmlResp += "<file>"+file+"</file></response>";
	            }
		      }
		      catch (Exception e) 
		      {
		    	  Logger logger = LoggerMgr.getLogger(this.getClass());
		    	  logger.error(e);
		          e.printStackTrace(); 
		          xmlResp += "<file><![CDATA[ERROR]]></file></response>";
		      }    
        }
	    
        // Response
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(xmlResp);
        response.getWriter().flush();
    }

    public String sendEmailLogs(UserSession us, String dest_email, String mail_body, String zip_filepath) throws Exception
    {
        saveSupportEMail(us,dest_email);
        
        LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
        String subject = lang.getString("support","logsfrom") + us.getSiteName();
        String body = "";
        
        
        EMemory zMem = (EMemory) DispMemMgr.getInstance().readConfiguration("E");
        if(zMem != null && zMem.getType().equalsIgnoreCase("L"))
        {
        	// Send By Lan
        	DispMailMessage mail = new DispMailMessage(zMem.getSender(),dest_email,subject,body);
        	mail.setAttach(zip_filepath);
        	mail.setBody(mail_body);

        	DispMailSender mailservice = new DispMailSender(zMem.getSmtp(),zMem.getUser(),zMem.getPass(),mail);
        	mailservice.setEncryption(zMem.getEncryption());
        	mailservice.setPort(zMem.getPort());
        	
        	if (!mailservice.sendMessage())
        	{
        		EventMgr.getInstance().error(new Integer(us.getIdSite()),us.getUserName(),"Action","LOG02",new Object[]{});
        		return "ERROR_MAIL";
        	}
        	else
        	{
        		EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(),"Action", "LOG01",new Object[]{});
        		return "ok";
        	}
        }
        else if(zMem != null && zMem.getType().equalsIgnoreCase("D"))
        {
        	// Send By Provider
        	HSActionQBeanList actionQList = new HSActionQBeanList();
            HSActionQBean actionQ = null;
            Integer key = null;
            
            try
            {
                key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                actionQ = new HSActionQBean(key.intValue(),BaseConfig.getPlantId(),us.getIdSite(),1,1,1,
                		  zMem.getModemId(),"E",1,zip_filepath,mail_body,"-1","-1");

                actionQList.addAction(actionQ);
                if(actionQList.insertActions())
                {
            		EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(),"Action","LOG07",new Object[]{});
            		return "ok";
                }
                return "ERROR";
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(Error.class);
                logger.error(e);
                return "ERROR";
            }
        }
        else
        {
        	// No Email Configuration
        	return "ERROR_MAIL";
        }
    }
    
    private void saveSupportEMail(UserSession us,String email) throws Exception
    {
    	ProductInfo pi = new ProductInfo();
    	pi.set("supportemail",email);
    }
    
    public void init() throws ServletException
    {
    }
}
