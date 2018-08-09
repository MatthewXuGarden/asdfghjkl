package supervisor;

import com.carel.supervisor.presentation.helper.ServletHelper;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Graph extends HttpServlet
{
    public Graph()
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
        //response.setContentType("text/html");
        Properties properties = ServletHelper.retrieveParameters(request);
        properties.get("requesttype");

        ServletOutputStream out = response.getOutputStream(); //.print("ddd");

        //File file= new File("c:\\prova.dat");
        //FileOutputStream outputStream= new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        out.println("output=");
        dataOutputStream.writeBytes("ciao");
        dataOutputStream.writeInt((int) new Float(12).floatValue());

        //dataOutputStream.writeFloat();
        //PrintWriter out = response.getWriter();
        out.flush();
        out.close();
    }

    public void init() throws ServletException
    {
        // Put your code here
    }
}
