<%@ page language="java"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.LogicDeviceBeanList"
import="com.carel.supervisor.presentation.bean.LogicDeviceBean"
import="java.util.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"


%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String number = lan.getString("devloglist","number");
	String description = lan.getString("devloglist","description");
	String devlogcomment1 = lan.getString("devloglist","devlogcomment1");
	
	
	LogicDeviceBeanList devlistbean =  new LogicDeviceBeanList();
	devlistbean.loadDeviceComplete(null,idsite,language);
	
	String[] header = new String[]{number,description };
	HTMLElement[][] tableData = new HTMLElement[devlistbean.size()][];
	String[] dblclick = new String[devlistbean.size()];
	
	LogicDeviceBean tmp_dev = null;
	for (int i=0;i<devlistbean.size();i++)
	{
		tmp_dev = devlistbean.getLogicDevice(i);
		tableData[i]= new HTMLElement[2];
		tableData[i][0]= new HTMLSimpleElement(String.valueOf(i+1));
		tableData[i][1]= new HTMLSimpleElement(tmp_dev.getDescription());
		dblclick[i] = String.valueOf(tmp_dev.getIddevice());
	}
	
	HTMLTable saved_device_table = new HTMLTable("", header, tableData);
	if (sessionUser.isMenuActive("devdetail"))
	{
		saved_device_table.setDlbClickRowFunction(dblclick);
		saved_device_table.setDbClickRowAction("top.frames['manager'].loadTrx('nop&folder=devdetail&bo=BDevDetail&type=click&iddev=$1&desc=ncode10');");
	}
	saved_device_table.setScreenH(sessionUser.getScreenHeight());
	saved_device_table.setScreenW(sessionUser.getScreenWidth());
	saved_device_table.setWidth(880);
	saved_device_table.setHeight(335);
	saved_device_table.setAlignType(0,1);
	saved_device_table.setColumnSize(0,80);
	saved_device_table.setColumnSize(1,755);
	
	String sav_dev = saved_device_table.getHTMLText();
	
	
%>
<p class='standardTxt'> <%=devlogcomment1%></p>
<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
<tr height="100%" valign="top" id="trLogicList">
	<td><%=sav_dev%></td>
</tr>
</table>
