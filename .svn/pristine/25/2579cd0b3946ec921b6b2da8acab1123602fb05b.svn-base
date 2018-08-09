<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>

<%
	String home = System.getenv("PVPRO_HOME");
	String cmd = request.getParameter("cmd");
	String cmb = request.getParameter("fileToList");
	
	File listFiles[] = null;
	if(cmd != null)
	{
		if(cmd.equalsIgnoreCase("LIST"))
		{
			if(cmb != null && cmb.equalsIgnoreCase("1"))
			{
				
				File f = new File(home+File.separator+"guardian"+File.separator+"log");
				listFiles = f.listFiles();	
			}
			else if(cmb != null && cmb.equalsIgnoreCase("2"))
			{
				File f = new File(home+File.separator+File.separator+"log");
				listFiles = f.listFiles();	
			}
			else if(cmb != null && cmb.equalsIgnoreCase("3"))
			{
				home = System.getenv("ProgramFiles");
				File f = new File(home+File.separator+File.separator+"PostgreSQL"+File.separator+"8.2"+
								   File.separator+"data"+File.separator+"pg_log");
				listFiles = f.listFiles();	
			}
			else if(cmb != null && cmb.equalsIgnoreCase("4"))
			{
				File f = new File(home+File.separator+File.separator+"engine"+File.separator+"logs");
				listFiles = f.listFiles();	
			}
			else if(cmb != null && cmb.equalsIgnoreCase("5"))
			{
				File f = new File(home+File.separator+File.separator+"Services"+File.separator+"manual"+File.separator+"logs");
				listFiles = f.listFiles();	
			}
			else if(cmb != null && cmb.equalsIgnoreCase("6"))
			{
				File f = new File(home+File.separator+File.separator+"Services");
				listFiles = f.listFiles();
					
			}
		}
		else if(cmd.equalsIgnoreCase("OPEN"))
		{
			String name = request.getParameter("fname");
			String type = request.getParameter("ftype");
			response.setContentType("text/plain");
   			response.setHeader("Content-Disposition", "attachment;filename=\""+name+"\"");
   			File f = null;
   			
   			if(type != null && type.equalsIgnoreCase("1"))
   			{
   				f = new File(home+File.separator+"guardian"+File.separator+"log"+File.separator+name);
   			}
   			else if(type != null && type.equalsIgnoreCase("2"))
   			{
   				f = new File(home+File.separator+File.separator+"log"+File.separator+name);
   			}
   			else if(type != null && type.equalsIgnoreCase("3"))
   			{
   				home = System.getenv("ProgramFiles");
   				f = new File(home+File.separator+File.separator+"PostgreSQL"+File.separator+"8.2"+
								   File.separator+"data"+File.separator+"pg_log"+File.separator+name);
   			}
   			else if(type != null && type.equalsIgnoreCase("4"))
   			{
   				f = new File(home+File.separator+File.separator+"engine"+File.separator+"logs"+File.separator+name);
   			}
   			else if(type != null && type.equalsIgnoreCase("5"))
   			{
   				f = new File(home+File.separator+File.separator+"Services"+File.separator+"manual"+File.separator+"logs"+File.separator+name);
   			}
   			else if(type != null && type.equalsIgnoreCase("6"))
   			{
   				f = new File(home+File.separator+File.separator+"Services"+File.separator+name);
   			}
   			
			InputStream in = new FileInputStream(f);
			ServletOutputStream outs = response.getOutputStream();
			int bit = 256;
			try 
			{
				while ((bit) >= 0) 
				{
        			bit = in.read();
        			outs.write(bit);
        		}
			} 
			catch(Exception ioe) 
			{
            	ioe.printStackTrace(System.out);
            }
            finally
            {
            	outs.flush();
            	outs.close();
            	in.close();
            }
		}
	}
%>
<html>
<head>
<title>PlantVisorPRO Logs Reader</title>
<script>

function listFile()
{
	document.getElementById("cmd").value="LIST";	
	document.getElementById("frmlogrd").submit();
}

function openFile(type,sFile)
{
	document.getElementById("cmd").value="OPEN";
	document.getElementById("ftype").value=type;	
	document.getElementById("fname").value=sFile;
	document.getElementById("frmlogrd").submit();
}
</script>
</head>
<body>
<form id="frmlogrd" name="frmlogrd" action="LogsReader.jsp" method="post">
<input type="hidden" name="cmd" id="cmd" value="NOP"/>
<input type="hidden" name="ftype" id="ftype" value="NOP"/>
<input type="hidden" name="fname" id="fname" value="NOP"/>


<table border="1" width="100%" cellpadding="1" cellspacing="1">
	<tr>
		<td width="15%">
			<button onclick="listFile();">LIST</button>
		</td>
		<td width="*">
			<select id="fileToList" name="fileToList">
				<option value="0">-------</option>
				<option value="1">Guardian</option>
				<option value="2">PlantVisorPRO</option>
				<option value="3">PostgresSQL</option>
				<option value="4">Tomcat</option>
				<option value="5">Server</option>
				<option value="6">Dispatcher</option>
			</select>
		</td>
		
	</tr>
	<tr>
		<td colspan="2">
			<table border="0" width="100%" cellpadding="1" cellspacing="1">
				<%if(listFiles != null) {%>
					<%for(int i=0; i<listFiles.length; i++)	{ %>
					<tr>
						<td width="25%">
							<%=new java.util.Date(listFiles[i].lastModified()) %>
						</td>
						<td width="*">
							<%=listFiles[i].getName() %>
						</td>
						<td width="10%">
							<%=(listFiles[i].length()/1024L) %> Kb
						</td>
						<td width="10%">
							<button onclick="openFile('<%=cmb %>','<%=listFiles[i].getName() %>');">OPEN</button>
						</td>
					</tr>
					<%} %>
				<%} %>
			</table>
		</td>
	</tr>
</table>

</form>
</body>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>