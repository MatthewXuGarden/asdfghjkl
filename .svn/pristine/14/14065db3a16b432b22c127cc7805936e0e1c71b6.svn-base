package supervisor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class ServOnlyDownload extends HttpServlet {

	private static final long serialVersionUID = -7985621729443177427L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException
	{
		downloadFile(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		downloadFile(request, response);
	}

	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		// recupero valore per path del file passato
		String filePath = request.getParameter("filepath");
		
		String fileName = "";
		
   		// continuo solo se viene passato un nome di file valido
		if ((filePath != null) && (! "".equals(filePath)))
    	{
			UserSession us = null;
	
	        // verifico validità UserSession
	        us = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	
	        if (ServletHelper.validateSession(us))
	        {
	       		
	       		fileName = fileName + filePath.substring(filePath.lastIndexOf(File.separator)+1);

	        	response.setContentType("application/unknown");
	        	response.setHeader("Content-Disposition","attachment; filename="+fileName+";");
		        		
	            FileInputStream in = null;
	            OutputStream out = null;
	            try
	            {
		
	                  out = new BufferedOutputStream( response.getOutputStream() );
	                  in = new FileInputStream(filePath);
	                  int c = -1;
	                  while( ( c = in.read() ) != -1 ) out.write( c );
		
	            }
	            catch( Exception e1 )
	            {
	                	  response.sendError( HttpServletResponse.SC_NOT_FOUND );
	            }
	            finally
	            {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    if( in != null ) try { in.close(); } catch( Exception e2 ) {logger.error(e2);}
	                if( out != null ) try { out.close(); } catch( Exception e3 ) {logger.error(e3);}
	            }
	
        	}
        }
		
	}
	
}
