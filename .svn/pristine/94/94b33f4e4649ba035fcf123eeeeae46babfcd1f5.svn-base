package supervisor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.presentation.svgmaps.SvgMapsTranslator;
import com.carel.supervisor.presentation.svgmaps.SvgMapsUtils;


public class SRVLExportDatapoints extends HttpServlet
{
    /**
	 * 
	 */
	private static final String XML_CONTENT = "text/xml;charset=UTF-8";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
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
        throws ServletException, IOException
    {
    	String sResponse = "";
    	
    	// XML response management
    	response.setContentType(XML_CONTENT);
    	sResponse = SvgMapsUtils.getSiteDatapoint();
    	response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(sResponse);
      	out.flush();
      	out.close();
      	return;
    }
}
