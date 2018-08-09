<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" pageEncoding="UTF-8"
	import="java.sql.Connection"
	import="java.sql.Statement"
	import="com.carel.supervisor.base.crypter.Crypter"
	import="com.carel.supervisor.dataaccess.db.DatabaseMgr"
	import="com.carel.supervisor.dataaccess.db.Record"
	import="com.carel.supervisor.dataaccess.db.RecordSet"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLNullSizeElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable"
%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String sql = request.getParameter("sqlcommand");
	StringBuffer sb = new StringBuffer();
	String sTable = "";
	Connection conn = null;
	Statement statement = null;
	if(sql != null)
	{
		try
		{
			RecordSet rs = null;
			Record r = null;
			String colName[] = null;
			Object val = null;
			HTMLElement[][] data = null;
			
			conn = DatabaseMgr.getInstance().getConnection(null);
			
			// SELECT ONLY!
			conn.setReadOnly(true);
			
			statement = conn.createStatement();
			
			if(sql.toUpperCase().startsWith("SELECT ")) {
				rs = new RecordSet(statement.executeQuery(sql));
			} else {
				sb.append("Only SELECT statements here. You were told, kiddo.");
			}
			
			/*
			if(sql.toUpperCase().startsWith("SELECT ")) 
				rs = DatabaseMgr.getInstance().executeQuery(null,sql,null);
			else
				DatabaseMgr.getInstance().executeStatement(null,sql,null);
			*/
			if(rs != null)
			{
				
				colName = rs.getColumnNames();
				
				data = new HTMLElement[rs.size()][colName.length];
				
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)	
					{
						for(int j=0; j<colName.length; j++) {
							sb.append(r.get(colName[j])+"\t");
							val = r.get(colName[j]);
							if(val != null)
								val = val.toString();
							else
								val = "NULL";
							
							data[i][j] = new HTMLSimpleElement(val.toString()); 
						}
						sb.append("\n");
					}
				}
				
				HTMLTable deviceTable = new HTMLTable("SQLCommander",colName,data,true,true);
        deviceTable.setHeight(500);
        deviceTable.setWidth(960);
        
        sTable = deviceTable.getHTMLText();
			}
		}
		catch(Exception e) {
			sb = new StringBuffer("ERRORE: "+e.getMessage());
		}
		finally {
			//statement.close();
			conn.close();
		}
	}
	
%>


<html>
<head>
<base href="<%=basePath%>">
	<link rel="stylesheet" href="stylesheet/plantVisorIE.css" type="text/css" />
	<script type="text/javascript" src="scripts/arch/Refresh.js"></script>
  <script type="text/javascript" src="scripts/arch/table/ListView.js"></script>
  <script type="text/javascript" src="scripts/arch/table/ListViewDyn.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewSort.js"></script>
	
<script>
var oLswContainer = new LswContainer();

function gosql(event)
{
	if(event.keyCode == 13)
		document.getElementById("frmsql").submit();
}
</script>

</head>

<body onkeydown="gosql(event);">
<form id="frmsql" name="frmsql" action="arch/manager/DBCommander.jsp;jsessionid=<%=session.getId() %>" method="post">
<table border="0" cellspacing="1" cellpadding="1" width="100%">
	<tr>
		<td><H2>Query console. Only "select" statements allowed.</H2></td>	
	</tr>
	<tr>
		<td><input style="width:100%;" type="text" name="sqlcommand" id="sqlcommand"/></td>
	</tr>
	<tr>
		<td align="center"><%=sTable%></td>	
	</tr>
	<tr>
		<td>
			<textarea style="width:100%;" rows="30" name="sqlresult" id="sqlresult"><%=sb.toString()%></textarea>
		</td>
	</tr>
</table>
</form>
</body>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>