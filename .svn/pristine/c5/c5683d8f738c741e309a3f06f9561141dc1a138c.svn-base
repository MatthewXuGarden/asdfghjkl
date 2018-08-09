package supervisor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.svgmaps.SvgMapsCustomTranslator;
import com.carel.supervisor.presentation.svgmaps.SvgMapsTranslator;


public class SRVLSvgMaps extends HttpServlet
{
    /**
	 * 
	 */
	private static final String JSON_CONTENT = "application/json";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, ServletException
    {
        controller(request, response, "GET");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        controller(request, response, "POST");
    }


    
    public void controller(HttpServletRequest request,
        HttpServletResponse response, String method)
        throws ServletException, IOException, ServletException
    {
    	
    	String sResponse="";
    	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
    	if(userSession==null || (request.getRequestedSessionId()!=null && ServletHelper.isInvalidId(request.getRequestedSessionId())))
    	{
    		sResponse = "{\"error\":300,\"errorstring\":\"Unknown method\"}";
    	}
    	else
    	{
    		boolean isCustomReq = false;
        	request.setCharacterEncoding("UTF-8");
        	Map request_params = request.getParameterMap();
        	Iterator itParams = request_params.keySet().iterator();
        	
        	while(itParams.hasNext())
        	{
        		String param = (String)itParams.next();
        		if (param.startsWith("get_"))
        		{
        			sResponse = SvgMapsCustomTranslator.manageJsonFncRequest(request);
        			isCustomReq = true;
        			break;
        		}
        	}
    	
        	if(!isCustomReq)
        		sResponse = SvgMapsTranslator.manageJsonRequest(request);
    	}
    	
    	//JSON response management
    	response.setContentType(JSON_CONTENT);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(sResponse);
      	out.flush();
      	out.close();
      	return;
    }
}
