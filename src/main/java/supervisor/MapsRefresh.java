package supervisor;

import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.maps.MapData;
import com.carel.supervisor.presentation.session.UserSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MapsRefresh extends HttpServlet
{
    public MapsRefresh()
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
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(this.getClass());
        out.println(", using the GET method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        Properties properties = ServletHelper.retrieveParameters(request);

        //      Retrive UserSession
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),
                request);

        MapData mapData = new MapData(properties, userSession);

        PrintWriter out = response.getWriter();
        out.println(
        		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"+
        		"<head>"+
        		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
        		"</head>");
        out.println("<HTML>");
        out.println("<BODY>");

        out.print(mapData.getUpdateScript());

        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    public void init() throws ServletException
    {
    }
}
