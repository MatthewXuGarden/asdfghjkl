package supervisor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.ZipperFile;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.ide.ExportMgr;
import com.carel.supervisor.director.ide.XmlStream;
import com.carel.supervisor.presentation.bo.helper.BackupHelper;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;

public class ServDownload extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		fileDownload(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
	
		fileDownload(request, response);
	}

	public void fileDownload(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		
		// stringhe per path di salvataggio file in upload:
		String confDir = "conf";
		String ruleDir = "rule"; //da config. regole
		String jspDir = "jsp";
		String devDir = "dev";
		String rulesDir = "xmlrules"; //da config. editor
		String imgtoplogo = "toplogoimg";
		String imglogin = "loginimg";
		String backupDir = "backup";
		String xmlConf = "xmlconf";
		String extraPath = "";
		String r = "";
		
		// recupero valore per dir di destinazione del file passato
		String tipoFile = request.getParameter("tipofile");
		
		UserSession us = null;

        //BEGIN: selezione azione in base al file passato (vedere bo BSystem).
        // Retrive UserSession
        us = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);

        if (ServletHelper.validateSession(us))
        {
    		
        	Properties properties = new Properties();
    		Transaction transaction = us.getTransaction();
    		
    		String langcode = us.getLanguage();
    		LangService lan = LangMgr.getInstance().getLangService(langcode);
    		
        	// "sitemgrexp" (*.conf):
        	if (confDir.equalsIgnoreCase(tipoFile))
        	{

        		r = BackupHelper.backupConf(us.getUserName());
        		//properties.setProperty("sitemgrexp", "OK - Saved");
        		String lastPathDir = "";
        		if ((r != null) && (! "".equals(r)))
        		{
        			lastPathDir = lastPathDir + r.substring(r.lastIndexOf(File.separator)+1);
        		}
        		//properties.setProperty("sitemgrexp", lastPathDir);
        		properties.setProperty("pathexp", r);

        		transaction.setSystemParameter(properties);
        		
        		//response.setContentType("application/x-msdownload");
        		response.setContentType("application/unknown");
        		response.setHeader("Content-Disposition","attachment; filename="+lastPathDir+";");
        		
                  FileInputStream in = null;
                  OutputStream out = null;
                  try {
                         //in = getServletContext().getResourceAsStream(r);
                         out = new BufferedOutputStream( response.getOutputStream() );
                         in = new FileInputStream(r);
                         int c = -1;
                         while( ( c = in.read() ) != -1 ) out.write( c );
                         //properties.setProperty("sitemgrexp", "OK - Saved");
                         //return;

                  }catch( Exception e )
                  {
                	  response.sendError( HttpServletResponse.SC_NOT_FOUND );
                  
                  }finally
                  {
                       if( in != null ) try { in.close(); } catch( Exception e ) {properties.setProperty("sitemgrexp", e.getMessage());}
                       if( out != null ) try { out.close(); } catch( Exception e ) {properties.setProperty("sitemgrexp", e.getMessage());}
                  }

        	}else
        	// "rulemgrimp" (*.rule):
        	if (ruleDir.equalsIgnoreCase(tipoFile))
        	{

				r = BackupHelper.backupRule(us.getUserName());
				//properties.setProperty("rulemgrexp", "OK - Saved");
				String lastPathDir = "";
				if ((r != null) && (! "".equals(r)))
				{
					lastPathDir = lastPathDir + r.substring(r.lastIndexOf(File.separator)+1);
				}
				//properties.setProperty("rulemgrexp", lastPathDir);
				properties.setProperty("pathexp", r);

				transaction.setSystemParameter(properties);
				
				//response.setContentType("application/x-msdownload");
        		response.setContentType("application/unknown");
        		response.setHeader("Content-Disposition","attachment; filename=" + lastPathDir + ";");

                  FileInputStream in = null;
                  OutputStream out = null;
                  try {
                         //in = getServletContext().getResourceAsStream(r);
                         out = new BufferedOutputStream( response.getOutputStream() );
                         in = new FileInputStream(r);
                         int c = -1;
                         while( ( c = in.read() ) != -1 ) out.write( c );
                         //properties.setProperty("rulemgrexp", "OK - Saved");
                         //return;

                  }catch( Exception e )
                  {
                	  response.sendError( HttpServletResponse.SC_NOT_FOUND );
                	  
                  }finally
                  {
                       if( in != null ) try { in.close(); } catch( Exception e ) {properties.setProperty("rulemgrexp", e.getMessage());}
                       if( out != null ) try { out.close(); } catch( Exception e ) {properties.setProperty("rulemgrexp", e.getMessage());}
                  }
			
        	}else
        	// "xmlconffile" (*.xml):
        	if (xmlConf.equalsIgnoreCase(tipoFile))
        	{
        		try {
					XmlStream a = ExportMgr.getInstance().getExporter("site").exporter(us.getLanguage());
					String path = BaseConfig.getCarelPath();
					String siteXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + a.getXML("site").toString();
					String descXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + a.getXML("desc").toString();

					r = path + "ide" + File.separator + "export" + File.separator;
					ZipperFile.zip(r, "SITE", new String[]{"Site", "SiteDictionary"}, "xml", new String[]{siteXML, descXML});
					r += "SITE.zip";
					
					String lastPathDir = "";
					if ((r != null) && (! "".equals(r)))
					{
						lastPathDir = lastPathDir + r.substring(r.lastIndexOf(File.separator)+1);
					}
					//properties.setProperty("rulemgrexp", lastPathDir);
					properties.setProperty("pathexp", r);

					transaction.setSystemParameter(properties);
					
					//response.setContentType("application/x-msdownload");
	        		response.setContentType("application/unknown");
	        		response.setHeader("Content-Disposition","attachment; filename=" + lastPathDir + ";");

	                  FileInputStream in = null;
	                  OutputStream out = null;
	                  try {
	                         //in = getServletContext().getResourceAsStream(r);
	                         out = new BufferedOutputStream( response.getOutputStream() );
	                         in = new FileInputStream(r);
	                         int c = -1;
	                         while( ( c = in.read() ) != -1 ) out.write( c );
	                         //properties.setProperty("rulemgrexp", "OK - Saved");
	                         //return;

	                  }catch( Exception e )
	                  {
	                	  response.sendError( HttpServletResponse.SC_NOT_FOUND );
	                	  
	                  }finally
	                  {
	                       if( in != null ) try { in.close(); } catch( Exception e ) {properties.setProperty("rulemgrexp", e.getMessage());}
	                       if( out != null ) try { out.close(); } catch( Exception e ) {properties.setProperty("rulemgrexp", e.getMessage());}
	                  }
					
				}
        		catch (Exception e)
        		{
					e.printStackTrace();
				}

				us.getTransaction().setSystemParameter(properties);
        	}else
        	// "importrules" (*.xml):
        	if (rulesDir.equalsIgnoreCase(tipoFile))
        	{
        		
        	}else
        	// "impjsp" (*.jsp):
        	if (jspDir.equalsIgnoreCase(tipoFile))
        	{
        			
        	}else
        	// "impdevcreator" (*.xml):
        	if (devDir.equalsIgnoreCase(tipoFile))
        	{
        	        		
        	}else
        	// "changeimgtop" (picture):
        	if (imgtoplogo.equalsIgnoreCase(tipoFile))
        	{
        				
        	}else
        	// "changeimg" (picture):
        	if (imglogin.equalsIgnoreCase(tipoFile))
        	{
        						
        	}
        	
        	//String fileName = r;
        	
        	// messaggio di conferma per l'utente
        	//PrintWriter fw = response.getWriter();
        	
        	//fw.write("<html><head>");
        	//fw.write("<script>function okmsg(){alert('" + fileName + "\\n   - OK! -');}</script>");
        	//fw.write("</head><body onload='okmsg();'>");
        	//fw.write("</body></html>");
        	
        	//fw.flush();
        	//fw.close();
        	
        }
     //return;   
	}
	
}
