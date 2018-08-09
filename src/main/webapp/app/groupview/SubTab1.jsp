<%@ page language="java" import="java.util.*"
import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.presentation.tabmenu.*"
	import="java.sql.Timestamp"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String jsession = request.getSession().getId();
	int site = sessionUser.getIdSite();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String reallydeletegroup = lan.getString("groupview","reallydeletegroup");
    String groupcomment1 = lan.getString("groupview","groupcomment1");	
    
	GroupListBean groups= new GroupListBean();
	groups.retrieveAllGroupsNoGlobal(site,language);

// creazione tabella
	int[] idsgroups = groups.getIds();
	int rows = idsgroups.length;
	final int columns = 2;
	HTMLElement[][] dati = new HTMLElement[rows][];
	String[] headerTable = {lan.getString("groupview", "headertable"),lan.getString("siteview","devconfig")};
	String[] dblClickRowFunction = new String[rows];
	String[] ClickRowFunction = new String[rows];
	String[] rowsClasses = new String[rows];
	boolean[] columnsOrder = {true};
	
	if (rows != 0)
	{
		for (int i = 0; i < rows; i++)
		{
				//dati
				dati[i] = new HTMLElement[columns];
				dati[i][0] = new HTMLSimpleElement("<div class='tableTouchCell'>"+groups.get(idsgroups[i]).getDescription()+"</div>");
				//attributi
				dati[i][0].addAttribute("name", String.valueOf(groups.get(idsgroups[i]).getGroupId()));
				dati[i][0].addAttribute("id", String.valueOf(groups.get(idsgroups[i]).getGroupId()));
				if (sessionUser.isMenuActive("setgroup"))
				{
					dati[i][1] = new HTMLSimpleElement("<div class='tableTouchCellImg'><img src='images/actions/maintenance_on_black.png' style='cursor:pointer;' onclick=top.frames['manager'].loadTrx('nop&folder=setgroup&bo=BSetGroup&type=click&group="+groups.get(idsgroups[i]).getGroupId()+"&desc=ncode05')></div>");
				}
				else
				{
					dati[i][1] = new HTMLSimpleElement("<div class='tableTouchCellImg'><img src='images/actions/maintenance_off.png'></div>");
				}
				dblClickRowFunction[i] = String.valueOf(groups.get(idsgroups[i]).getGroupId());
				ClickRowFunction[i] = String.valueOf(groups.get(idsgroups[i]).getGroupId());
					
		}
	}
	
	HTMLTable table = new HTMLTable("areas", headerTable, dati);
	if (sessionUser.isMenuActive("setgroup"))
	{
		table.setDbClickRowAction("top.frames['manager'].loadTrx('nop&folder=setgroup&bo=BSetGroup&type=click&group=$1&desc=ncode05')");
		table.setDlbClickRowFunction(dblClickRowFunction);
	}
	table.setAlignType(1,HTMLTable.CENTER);
	table.setSgClickRowAction("selectedGroup('$1')");
	table.setSnglClickRowFunction(ClickRowFunction);
	table.setScreenH(sessionUser.getScreenHeight());
	table.setScreenW(sessionUser.getScreenWidth());
	table.setHeight(335);
	table.setWidth(900);
	table.setColumnSize(0,800);
	table.setRowHeight(25);
//	table.setColumnSize(1,428);
	String htmlTable = table.getHTMLText();
%>
<INPUT type="hidden" id="reallydeletegroup" name="reallydeletegroup" value="<%=reallydeletegroup%>"/>
<table border="0" width="100%" height="95%" cellspacing="1" cellpadding="1">
<tr><td>
<p class="StandardTxt"><%=groupcomment1%></p>
</td></tr>
	<tr>
		<td style="height:5px"></td>
	</tr>
	<tr height="100%" valign="top" id="trGroupList">
		<TD><%=htmlTable%></TD>
	</tr>
	<TR>
		<TD>
			<FORM id="frmremovegroup" name="frmremovegroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
				<INPUT type="hidden" id="removeGroup" name="removeGroup" value=""/>
				<INPUT type="hidden" id="cmd" name="cmd" value="rem"/>
			</FORM>
		</TD>
	</TR>
</table>