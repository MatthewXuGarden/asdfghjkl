<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.io.InputStream.*"%>
<%@page import="java.nio.charset.Charset"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String xmlfile = request.getParameter("xmlfile");

BufferedReader br = new BufferedReader(
		new InputStreamReader(
				new FileInputStream(
						new File(System.getenv("PVPRO_HOME")+File.separator+"PvPro"+File.separator+xmlfile+".xml")), Charset.forName("UTF-8")));
String s;
while( (s = br.readLine()) != null )
	out.println(s);
%>
