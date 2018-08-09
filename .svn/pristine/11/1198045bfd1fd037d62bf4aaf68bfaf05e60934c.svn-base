package supervisor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.helper.MultipartRequest;
import com.carel.supervisor.updater.UpdateMgr;

public class SRVLUpdate extends HttpServlet
{
	private static final long serialVersionUID = -5186136446716131522L;
	
	private static final String VERSION 	= "VERSION";
	private static final String TOKEN 		= "TOKEN";
	private static final String CHECKINFO  	= "CHECKINFO";
	private static final String STARTDAEMON	= "STARTDAEMON";
	private static final String CHKERROR	= "CHKERROR";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String ris = "KO";
		String contentType = request.getContentType();
		String rcmd = request.getParameter("cmd");
		String sToken = request.getParameter("token");
		
		/*
		 * Receiving file 
		 */
		if(contentType.indexOf("multipart/form-data") >= 0 ) 
		{
			/*
        	 * Only if the client has the correct token
        	 */
			UpdateMgr.getInstance().initDirectoryForUpload();
        		
			MultipartRequest multireq = new MultipartRequest(request.getContentType(), 
															request.getContentLength(), 
															request.getInputStream(), 
															UpdateMgr.getInstance().getDirectory(), 
															request.getContentLength());
			sToken = multireq.getURLParameter("token");
			if(UpdateMgr.getInstance().checkToken(sToken))
        	{
    			/*
    			File f = multireq.getFile("upload");
    			String fileName = multireq.getFileSystemName("upload");
    			*/
    			ris = "OK";
        	}
		}
        /*
         * Checks authentication and 
         * send back PVP's version
         */
		else if(rcmd.equalsIgnoreCase(VERSION))
        {
        	String ident = request.getParameter("ident");
        	String passw = request.getParameter("passw");
        	ris = UpdateMgr.getInstance().checkAndGetVersion(ident,passw);
        }
		/*
		 * Generate token for the communication
		 */
		else if(rcmd.equalsIgnoreCase(TOKEN))
        {
			ris = String.valueOf(UpdateMgr.getInstance().generateToken());
        }
        /*
         * Write check information file
         */
        else if(rcmd.equalsIgnoreCase(CHECKINFO))
        {
        	if(UpdateMgr.getInstance().checkToken(sToken))
        	{
	        	try
	        	{
		        	ris = UpdateMgr.getInstance().writeCheckFile(request.getParameter("filename"), 
		        												 request.getParameter("filelen"), 
		        												 request.getParameter("filecrc"));
	        	}
	        	catch(Exception e) {
	        		Logger logger = LoggerMgr.getLogger(this.getClass());
	                logger.error(e);
	        	}
        	}
        }
        /*
         * Start updater service
         */
        else if(rcmd.equalsIgnoreCase(STARTDAEMON))
        {
        	if(UpdateMgr.getInstance().checkToken(sToken))
        	{
        		if(UpdateMgr.getInstance().startDaemon())
        			ris = "OK";
        		else
        			ris = "KO";
        	}
        }
        /*
         * Check error on:
         * - CRC
         * - UNZIP
         */
        else if(rcmd.equalsIgnoreCase(CHKERROR))
        {
        	ris = "";
        	
        	// Check error on CRC
        	if(UpdateMgr.getInstance().checkCRCError())
        		ris += "C";
        	
        	// Check error on UNZIP
        	if(UpdateMgr.getInstance().checkUNZIPError())
        		ris += "U";
        }
		
		// Response to the client
		PrintWriter out = response.getWriter();
        out.write(ris);
        out.flush();
        out.close();
	}
}
