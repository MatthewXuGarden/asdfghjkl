<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper" 
	import="com.carel.supervisor.presentation.bo.BHsreport" 
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
	import="com.carel.supervisor.presentation.bean.ReportBeanListPres"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	String comment = lang.getString("hsreport","comment");
	int screenh = sessionUser.getScreenHeight();
	int screenw = sessionUser.getScreenWidth();
	
	BHsreport hreport = (BHsreport)sessionUser.getCurrentUserTransaction().getBoTrx();
	String basePath = hreport.getRepositoryPath();
	FileDialogBean fileDlg = new FileDialogBean(request);
	
	ReportBeanListPres reportBean = new ReportBeanListPres();
	String cur_page = "";
	cur_page = sessionUser.getPropertyAndRemove("AlPageNumber");
	if (null==cur_page)
		reportBean.loadFromDataBaseReport(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(cur_page);
		reportBean.loadFromDataBaseReport(sessionUser,t_page);
		reportBean.setPageNumber(t_page);
	}
	
	reportBean.setScreenH(screenh);
	reportBean.setScreenW(screenw);
	reportBean.setHeight(280);
	reportBean.setWidth(900);
	String htmlTable = reportBean.getHTMLHSReportTable(idsite,language,"");
	boolean isLocal = FileDialogBean.isLocal(request);
%>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<P class='standardTxt'><%=comment%></P>

<table border="0" cellpadding="1" cellspacing="1" width="98%" height="82%">
	<tr height="100%" valign="top" id="trArcRepList">
		<td><%=htmlTable%></td>
	</tr>
	<tr>
		<td>
			<fieldset class='field'>
			<legend class='standardTxt'><%=lang.getString("hsreport","filtro")%></legend>
				<table border="0" cellpadding="1" cellspacing="1">
					<tr>
						<td width="33px"><input onclick="hsFilterReport(this);" type="radio" class='bigRadio' name="hsrtype" value="" id="hsrtype1" checked/></td>
						<td class="mediumTxt" width="100px"><%=lang.getString("hsreport","all")%></td>
						<td width="33px"><input onclick="hsFilterReport(this);" type="radio" class='bigRadio' name="hsrtype" value="TRUE" id="hsrtype2"/></td>
						<td class="mediumTxt" width="100px"><%=lang.getString("hsreport","haccp")%></td>
						<td width="33px"><input onclick="hsFilterReport(this);" type="radio" class='bigRadio' name="hsrtype" value="FALSE" id="hsrtype3"/></td>
						<td class="mediumTxt" width="100px"><%=lang.getString("hsreport","storico")%></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
<FORM name="form_print" id="form_print"" action="servlet/master;jsessionid=<%=jsession%>" method="post">
</FORM>
<input type="hidden" id="msgdelete" value="<%=lang.getString("hsreport","msgdel")%>">
<input type="hidden" id="msgexportall" value="<%=lang.getString("hsreport","msgexportall")%>">
<input type="hidden" id="msgdeleteall" value="<%=lang.getString("hsreport","msgdeleteall")%>">
<input type="hidden" id="hsfilter" value="">
<input type="hidden" id="currentrow" value="-1">
<input type="hidden" id="filename" value="">
<input type="hidden" id="filepath" value="<%=basePath%>">
<input type='hidden' id='save_confirm' value='<%=lang.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lang.getString("fdexport","exporterror") %>" />
<input type='hidden' id='log_str' value='<%=lang.getString("hsreport","storico") %>' />
<input type='hidden' id='haccp_str' value='<%=lang.getString("hsreport","haccp") %>' />
<input type='hidden' id='isLocal' value='<%=isLocal %>'/>
<%=fileDlg.renderFileDialog()%>
</html>
