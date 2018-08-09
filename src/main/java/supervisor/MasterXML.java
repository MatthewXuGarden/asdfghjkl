package supervisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.https2xml.XMLResponse;

public class MasterXML extends HttpServlet 
{
	private static final long serialVersionUID = 222395927374972610L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		//getParameterNames();
		Enumeration<?> param = request.getParameterNames();
		//Properties用法
        Properties properties = new Properties();
	    request.setCharacterEncoding("UTF-8");
       	response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/xml");
	    //I/O流
		BufferedReader reader = request.getReader();
		//遍历
		while (param.hasMoreElements())
        {
            String name = param.nextElement().toString();
            properties.setProperty(name, request.getParameter(name));
        }
        
		if(request.getParameter("input")==null)
		{
			String input = "";
			String line = "";
			while((line = reader.readLine())!=null)
				input += line;
			properties.setProperty("input", input);
		}
		
		XMLResponse responseXML = null;
		try 
		{
			//构造函数
			responseXML = new XMLResponse(XMLNode.parse(properties.getProperty("input")));
			//ServletOutputStream out = response.getOutputStream();
			//out.print(responseXML.getResponse());
			//out.flush();
			//out.close();
			response.getWriter().print(responseXML.getResponse().toString());
		} 
		catch (Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
}
