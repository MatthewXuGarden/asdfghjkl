package supervisor;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.assistance.Variables;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Assistance extends HttpServlet
{
    public Assistance()
    {
        super();
    }

    public void destroy()
    {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
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
        Enumeration param = request.getParameterNames();
        Properties properties = new Properties();
        String path = request.getContextPath();
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() + path + "/";

        while (param.hasMoreElements())
        {
            String name = param.nextElement().toString();
            properties.setProperty(name, request.getParameter(name).toString());
        }

        properties.setProperty("basePath", basePath);

        Variables variables = null;

        try
        {
            variables = new Variables(properties);
        } //try 
        catch (Exception e)
        {
           LoggerMgr.getLogger(this.getClass()).error(e);
        } //catch

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.print(variables.getHTMLVarTable());
        out.flush();
        out.close();
    }

    public void init() throws ServletException
    {
    }
}
