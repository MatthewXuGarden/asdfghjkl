<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper" 
	import="com.carel.supervisor.presentation.bo.BHsprint"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
	import="com.carel.supervisor.presentation.bean.ReportBeanListPres"
	import="com.carel.supervisor.dispatcher.DispatcherMgr"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	String comment = lang.getString("hsprint","comment");
	int screenh = sessionUser.getScreenHeight();
	int screenw = sessionUser.getScreenWidth();
	
	ReportBeanListPres reportBean = new ReportBeanListPres();
	String cur_page = "";
	cur_page = sessionUser.getPropertyAndRemove("AlPageNumber");
	if (null==cur_page)
		reportBean.loadFromDataBase(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(cur_page);
		reportBean.loadFromDataBase(sessionUser,t_page);
		reportBean.setPageNumber(t_page);
	}
	
	reportBean.setScreenH(screenh);
	reportBean.setScreenW(screenw);
	reportBean.setHeight(330);
	reportBean.setWidth(900);
	String htmlTable = reportBean.getHTMLHSPrintTable(idsite,language,"");
	String basePath = DispatcherMgr.getInstance().getRepositoryPath();
	FileDialogBean fileDlg = new FileDialogBean(request);
	boolean isLocal = FileDialogBean.isLocal(request);
%>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<%=fileDlg.renderFileDialog()%>
<p class='standardTxt'><%=comment%></p>
<table border="0" cellpadding="1" cellspacing="1" width="100%" height="88%">
	<tr height="100%" valign="top" id="trAlrRepList">
		<td><%=htmlTable%></td>
	</tr>
</table>

<input type="hidden" id="msgdelete" value="<%=lang.getString("hsprint","msgdel")%>">
<input type="hidden" id="msgexportall" value="<%=lang.getString("hsprint","msgexportall")%>">
<input type="hidden" id="msgdeleteall" value="<%=lang.getString("hsprint","msgdeleteall")%>">
<input type="hidden" id="currentrow" value="-1">
<input type="hidden" id="filename" value="">
<input type="hidden" id="filepath" value="<%=basePath%>">
<input type='hidden' id='save_confirm' value='<%=lang.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lang.getString("fdexport","exporterror") %>" />
<input type='hidden' id='isLocal' value='<%=isLocal %>'/>
</html>
