package supervisor;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.carel.supervisor.base.log.*;
import com.carel.supervisor.director.vscheduler.*;
import com.carel.supervisor.plugin.optimum.OptimumManager;
import com.carel.supervisor.plugin.optimum.StartStopBean;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.*;

/**
 * Servlet implementation class VisualScheduler
 */
public class VisualScheduler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public VisualScheduler() {
        super();
    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	noCache(request, response);
    	
    	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
    	String app = request.getParameter("app");
    	CData objData = new CData(userSession.getIdSite(), userSession.getLanguage());
    	if( app != null ) {
    		objData.loadDB(app);
    	}
    	else {
	    	if( SchedulerHook.isRunning() ) {
	    		while( SchedulerHook.isDataLoading() ) {
	    			try {
	    				Thread.sleep(100);
	    			} catch(Exception e) {
	    			}
	    		}
	    		objData.loadUIDB();
	    	}
	    	else {
	    		objData.loadDB(true);
	    	}
    	}
    	response.setContentType("text/xml; charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	response.getOutputStream().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//    	response.getOutputStream().print(objData.getTextXML());
    	response.getOutputStream().write(objData.getTextXML().getBytes("UTF-8"));
    	response.flushBuffer();
    }
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		noCache(request, response);
		
		try {
       		UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
    		BufferedReader br = request.getReader();
    		String str = br.readLine();
        	String app = request.getParameter("app");
    		CData objData = new CData(userSession.getIdSite(), userSession.getLanguage());
    	    objData.loadXML(str, userSession.getUserName());
    	    if( objData.updateDB() ) {
    	    	if( app != null ) {
    	    		if( app.equals("opt_startstop") ) {
    		        	StartStopBean.schedulerChanged();
    		        	OptimumManager.getInstance().updateStartStop();
    		        	OptimumManager.getInstance().resetStartStop();
    	    		}
    	    	}
    	    	else {
    	    		SchedulerHook.dbChanged();
    	    	}
    	    }
    	    response.getOutputStream().print("</OK>");
    	    response.flushBuffer();
    	} catch(Exception e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    	}
	}
	

	private void noCache(HttpServletRequest request, HttpServletResponse response)
	{
    	// prevent cache
    	String strBrowser = request.getHeader("User-Agent");
    	if( strBrowser.indexOf("MSIE") >= 0 ) {
    		response.setHeader("Cache-Control", "no-store");
    	}
    	else {
    		// doesn't works on IE under https
    		response.setHeader("Cache-Control","no-cache");	//HTTP 1.1
    		response.setHeader("Pragma","no-cache");		//HTTP 1.0
    	}
	}
}
