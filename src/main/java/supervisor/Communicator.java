package supervisor;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Communicator extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/xml";
    private static int i = 0;
    private static final String DOC_TYPE = null;

    public Communicator()
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
        response.setContentType(CONTENT_TYPE);
        i = i + 1;

        PrintWriter out = response.getWriter();
        out.println(
            "<gauge type=\"thermomether\"><attribute-list><attribute name=\"visibility\" " +
            "value=\"true\"/><attribute name=\"enabled\" value=\"true\"/></attribute-list>" +
            "<value-list><value x=\"" + String.valueOf(i) +
            "\"/></value-list></gauge>");

        if (DOC_TYPE != null)
        {
            out.println(DOC_TYPE);
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        byte[] buff = new byte[10000];
        InputStream oInput = request.getInputStream();
        int iRead = 0;
        String sTmp = "";

        while ((iRead = oInput.read(buff)) > 0)
        {
            sTmp = sTmp + new String(buff, 0, iRead);
        }

        oInput.close();
        System.out.println(sTmp);
        doGet(request, response);
    }

    public void init() throws ServletException
    {
    }
}
