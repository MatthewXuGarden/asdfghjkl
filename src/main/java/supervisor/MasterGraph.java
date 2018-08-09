package supervisor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.graph.LoadDevice;
import com.carel.supervisor.presentation.graph.LoadGraph;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class MasterGraph extends HttpServlet 
{
	private static final long serialVersionUID = -8099751944176355894L;
	private static final String ACTION_TYPE="actionType";
	private static final int LOAD_DEVICE=1;
	private static final int LOAD_CURVE=2;

	public MasterGraph() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws IOException	
	{
		// Retrieve UserSession and check
		UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		if (ServletHelper.validateSession(userSession))
        {
			Enumeration param = request.getParameterNames();
	        Properties properties = new Properties();
	    	String path = request.getContextPath();
	    	response.setCharacterEncoding("UTF-8");
		    request.setCharacterEncoding("UTF-8");
			String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+path+"/";
			 
	        while (param.hasMoreElements())
	        {
	            String name = param.nextElement().toString();
	            properties.setProperty(name, request.getParameter(name).toString());
	        }
	        
	        properties.setProperty("basePath",basePath);
	        
			int actionType=new Integer(properties.getProperty(ACTION_TYPE)).intValue();
			String rsp=null;
			
			switch(actionType)
			{
				case LOAD_DEVICE:
					
					LoadDevice loadDevice=new LoadDevice(properties,userSession);
					rsp=loadDevice.getHTML();
					response.setContentType("text/xml; charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print(rsp);
					out.flush();
					out.close();
				break;
				
			case LOAD_CURVE:
								
				// ENHANCEMENT 20090225 We store the TimePeriod and MainTime Parameters in the current local transaction
				try {									
					String lastSelectedTimePeriod = request.getParameter("timeperiod");
					String lastSelectedMainTime = request.getParameter("mainTime");
					if (null != lastSelectedTimePeriod && null != lastSelectedMainTime
							&& !"null".equals(lastSelectedTimePeriod) && !"null".equals(lastSelectedMainTime)) {
						int intTimePeriod = Integer.parseInt(lastSelectedTimePeriod);
						// if the intTimePeriod is < 0 (the user is zooming), we don't store the parameter
						if (intTimePeriod >= 0) {
							if (GraphConstant.TYPE_HACCP.equals(request.getParameter("typeGraph"))) {
								// must convert the time period value before storing (the DB scale range is 0..8, the flash one is 8..0)
								userSession.getCurrentUserTransaction().setProperty("sesHACCPTimePeriod", Integer.toString((8-Integer.parseInt(lastSelectedTimePeriod))));
								userSession.getCurrentUserTransaction().setProperty("sesHACCPMainTime", lastSelectedMainTime);					
							} 
							else { // Historical
								// must convert the time period value before storing (the DB scale range is 0..8, the flash one is 8..0)
								userSession.getCurrentUserTransaction().setProperty("sesHistTimePeriod", Integer.toString((8-Integer.parseInt(lastSelectedTimePeriod))));
								userSession.getCurrentUserTransaction().setProperty("sesHistMainTime", lastSelectedMainTime);	
							}
						}
					}
				} catch (NumberFormatException nfe) {						
				}
					
					// Un-comment the next section to enable probes for test and performance evaluation
					
					// dump della request
//					LoggerMgr.getLogger(this.getClass()).debug("------------- REQUEST -------------\n");
//					Set<String> requestParams = request.getParameterMap().keySet();
//
//					try {
//						for (String iterable_element : requestParams) {
//							LoggerMgr.getLogger(this.getClass()).debug("paramName:"+iterable_element+" value="+request.getParameter(iterable_element));
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
					//long t1 = System.currentTimeMillis();
					String noFlash = request.getParameter("noFlash");
					if( noFlash == null ) {
						// ENHANCEMENT 20090213 - Save variable color and range every time its graph is requested
						// We are to save those info before data retrieval
						try {
							new ConfigurationGraphBeanList().saveVariableColorAndRange(properties, userSession);
						} catch (Exception e) {
					        LoggerMgr.getLogger(this.getClass()).error(e);
						}
					}
					//LoggerMgr.getLogger(this.getClass()).debug("- - - - - - - SAVE CONFIG ELAPSED (ms):"+(System.currentTimeMillis()-t1));
					
					LoadGraph loadGraph= new LoadGraph(properties,userSession);
					rsp=loadGraph.getHTML();
					//LoggerMgr.getLogger(this.getClass()).debug("\n+++++++++++++++ RESPONSE +++++++++++++\n"+rsp);
					ServletOutputStream out1 = response.getOutputStream();
					DataOutputStream dataOutputStream = new DataOutputStream(out1);
					if( noFlash == null )
						out1.println("output=");
					else
						response.setContentType("text/xml");
				    dataOutputStream.writeBytes(rsp);
				    out1.flush();
				    out1.close();
				    //LoggerMgr.getLogger(this.getClass()).debug("\n- - - - - -  OVERALL ELAPSED (ms):"+(System.currentTimeMillis()-t1));
				break;
			}
        }
        else {
        	ServletHelper.invalidateSession(request.getRequestedSessionId(), request);
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(ServletHelper.PARAM_ERROR,
                "Session timeout");
        }		
	}
}
