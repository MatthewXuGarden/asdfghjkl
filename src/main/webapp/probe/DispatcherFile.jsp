<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
	import="java.io.File"
	import="java.util.Properties"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>

<%
	try
	{
		Properties properties = ServletHelper.retrieveParameters(request);
		String num = properties.getProperty("param0");
		long numDays = 10L;
		long lastModify = System.currentTimeMillis();
		
		try {
			numDays = Long.parseLong(num);
		}
		catch(Exception e){
		}
		
		numDays = (numDays * 86400000L);
		numDays = lastModify - numDays;
			
		File f = new File("c:/Carel/PvPro");
		File[] doc = f.listFiles();
		if(doc != null)
		{
			for(int i=0; i<doc.length; i++)
			{
				lastModify = doc[i].lastModified();
				if(lastModify < numDays)
					doc[i].delete();
			}
		}
	}
	catch(Exception e) {
	}	
	
%>
0
<% } else { response.sendError(404); } %>