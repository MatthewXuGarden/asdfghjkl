package supervisor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfilingMgr;

public class SRVLAccount extends HttpServlet
{
	private static final long serialVersionUID = -5186136446716131522L;
	
	private static final String AUTH = "AUTH";
	private static final String XML  = "XML";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		String rcmd = request.getParameter("cmd");
		String ris = "KO";
				
		/**
		 * Check authentication 
		 * and generate the control token
		 */
		if(rcmd.equalsIgnoreCase(AUTH))
        {
        	IProfiler profiler = ProfilingMgr.getInstance().getProfiler();
        	ris = profiler.checkRemoteCredential(request.getParameter("ident"), request.getParameter("passw"));
        }
		else if(rcmd.equalsIgnoreCase(XML))
		{
			String xdata = request.getParameter("xml");
			try
			{
				IProfiler profiler = ProfilingMgr.getInstance().getProfiler();
				ris = profiler.manageUserXmlData(1,xdata);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Logger logger = LoggerMgr.getLogger(SRVLAccount.class);
	            logger.error(e);
			}
		}
		
		// Response to the client
		PrintWriter out = response.getWriter();
        out.write(ris);
        out.flush();
        out.close();
	}
}
