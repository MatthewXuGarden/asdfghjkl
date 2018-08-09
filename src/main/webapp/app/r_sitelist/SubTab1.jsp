<%@ page language="java" import="java.util.*" 
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.presentation.tabmenu.*"
	import="com.carel.supervisor.dataaccess.dataconfig.*"
	import="java.sql.Timestamp"
%>
<%@ page import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement" %>
<%@ page import="com.carel.supervisor.base.config.ProductInfoMgr" %>
<%@ page import="com.carel.supervisor.base.config.BaseConfig" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	String path = request.getContextPath();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
 
	LangService lang = LangMgr.getInstance().getLangService(language);
	SiteInfo[] siteList =  SiteInfoList.retriveRemoteSite();
	//retrieve da sessione
	String cmd = sessionUser.getPropertyAndRemove("cmd");
	
	
	//stringhe multilingua
	String site = lang.getString("r_sitelist","site");
	String identify = lang.getString("r_sitelist","identify");
	String password = lang.getString("r_sitelist","password");
	String phonenumber = lang.getString("r_sitelist","phonenumber");
	String modem = lang.getString("r_sitelist","modem");
	String lan = lang.getString("r_sitelist","lan");
	String comment1 = lang.getString("r_sitelist","comment1");
	String selectallfields = lang.getString("r_sitelist","selectallfields");
	
	
	// creazione tabella
	String h_name = lang.getString("r_sitelist","name");
	String h_type = lang.getString("r_sitelist","type");
	String h_connectiontype = lang.getString("r_sitelist","connectiontype");
	String confirmsitedel = lang.getString("r_sitelist","confirmsitedel");
	
	
	int rows = siteList.length;
	String[] header = new String[]{h_name,h_type,h_connectiontype};
	HTMLElement[][] tabledata = new HTMLElement[rows][];
	String[] ClickRowFunction = new String[rows];
    String[] DBLClickRowFunction = new String[rows];
    SiteInfo tmp_site = null;
	for (int i=0;i<rows;i++)
	{
		tmp_site = siteList[i];
		tabledata[i] = new HTMLElement[3];
		tabledata[i][0] = new HTMLSimpleElement(tmp_site.getName());
		if (tmp_site.getType().equalsIgnoreCase("PVP"))
			tabledata[i][1] = new HTMLSimpleElement("PlantVisorPRO");
		else if (tmp_site.getType().equalsIgnoreCase("PVE"))
			tabledata[i][1] = new HTMLSimpleElement("PlantVisor Enhanced");
		else if (tmp_site.getType().equalsIgnoreCase("PW1"))
			tabledata[i][1] = new HTMLSimpleElement("PlantWatch");
		tabledata[i][2] = new HTMLSimpleElement(tmp_site.getConnectiontype());
		ClickRowFunction[i] = String.valueOf(tmp_site.getId());
        DBLClickRowFunction[i] = String.valueOf(tmp_site.getId());
	}
	
	HTMLTable table = new HTMLTable("sites", header, tabledata); 
	table.setSgClickRowAction("selectedLine('$1');");
    table.setSnglClickRowFunction(ClickRowFunction);
    table.setDbClickRowAction("modify_site();");
    table.setDlbClickRowFunction(DBLClickRowFunction);
	table.setScreenH(sessionUser.getScreenHeight());
	table.setScreenW(sessionUser.getScreenWidth());
	table.setWidth(900);
    table.setHeight(225);
    table.setAlignType(1,1);
    table.setAlignType(2,1);
    table.setColumnSize(0,405);
    table.setColumnSize(1,220);
    table.setColumnSize(2,220);
    
    String htmlTable = table.getHTMLText();
    
    String rel_name="";
    String rel_type="";
    String rel_indentify="";
    String rel_password = "";
    String rel_typeconnection = "";
    String rel_phonenumber = "";
    
    String id_site_to_load = sessionUser.getProperty("site_to_modify");
    SiteInfo site_to_load = null;
    //sezione reload della pagina con i campi valorizzati x modifica
    String type = "";
    String connection = "";
    if (cmd!=null)
    {
    	if (cmd.equals("reload"))	
    	{
		    site_to_load = SiteInfoList.retrieveSiteById(Integer.parseInt(id_site_to_load)); 
	    	rel_name = site_to_load.getName();
	    	rel_indentify = site_to_load.getCode();
	    	rel_password = site_to_load.getPassword();
	    	rel_phonenumber = site_to_load.getPhone();
	    }
	 	else if (cmd.equals("double"))
	 	{
	 		rel_name = sessionUser.getProperty("name");
	    	rel_indentify = sessionUser.getProperty("identify");
	    	rel_password = sessionUser.getProperty("password");
	    	rel_phonenumber = sessionUser.getProperty("phonenumber");
	    	type = sessionUser.getProperty("type");
	    	if (type==null) type="";
	    	connection = sessionUser.getProperty("connectiontype");
	 	}
	 }
    
String s_doubleid = lang.getString("r_sitelist","doubleid");
String s_ip = lang.getString("r_sitelist","ip");

int currentRowsInTable = table.getRows();
int totalsRowsInTable = 0;
try {
	totalsRowsInTable = Integer.parseInt(BaseConfig.getProductInfo("license"));
}catch(Exception e){}

String msglimitlicense = lang.getString("r_sitelist","licenselimit");
%>
<INPUT type="hidden" id="confirmsitedel" value="<%=confirmsitedel%>">
<INPUT type="hidden" id="s_lan" value="<%=lan%>">
<INPUT type="hidden" id="s_phonenumber" value="<%=phonenumber%>">
<INPUT type="hidden" id="s_ip" value="<%=s_ip%>">
<INPUT type="hidden" id="s_doubleid" value="<%=s_doubleid%>">
<INPUT type="hidden" id="selectallfields" value="<%=selectallfields%>">
<INPUT type="hidden" id="currentrows" value="<%=currentRowsInTable%>">
<INPUT type="hidden" id="totalrows" value="<%=totalsRowsInTable%>">
<INPUT type="hidden" id="licenselimit" value="<%=msglimitlicense%>">


<table border="0" width="100%" height="98%" cellspacing="1" cellpadding="1">
<tr><td>
		<p class='standardTxt'><%=comment1%></p>
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr height="100%" valign="top" id="trSiteList"><td>
<%=htmlTable%>
</td></tr>
<tr><td>
<FORM id="frm_r_sitelist" name="frm_r_sitelist" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="site_to_modify" name="site_to_modify" value="<%=id_site_to_load%>">
<INPUT type="hidden" id="cmd" name="cmd" value="<%=cmd%>">
<FIELDSET class='field'>
<LEGEND class='standardTxt'><%=site%></LEGEND>
	<TABLE class='standardTxt'>
		<TR valign="top">
			<TD><%=h_name%></TD> 
			<TD><INPUT class='standardTxt' type="text" id='name' name='name' value='<%=rel_name%>' maxlength="30" size="30" onblur="noBadCharOnBlur(this);" onkeydown='checkBadChar(this,event);'/> *</TD>
			<TD width="150">&nbsp;</TD>
			<TD><%=h_type%></TD>
			<TD width="20px"></TD>
			<TD><SELECT class='standardTxt' id='type' name='type' onchange='changeType()'> 
				<OPTION value='0'>--------------------------</OPTION>
				<OPTION <%=(((site_to_load!=null)&&(site_to_load.getType().equalsIgnoreCase("PVP"))))||(type.equalsIgnoreCase("PVP"))?"selected":"" %> value='PVP'>PlantVisorPRO</OPTION>
				<OPTION <%=(((site_to_load!=null)&&(site_to_load.getType().equalsIgnoreCase("PVE"))))||(type.equalsIgnoreCase("PVE"))?"selected":"" %> value='PVE'>PlantVisor Enhanced</OPTION>
			<!--<OPTION <%=(((site_to_load!=null)&&(site_to_load.getType().equalsIgnoreCase("PW1"))))||(type.equalsIgnoreCase("PW1"))?"selected":"" %> value='PW1'>PlantWatch</OPTION>-->
			</SELECT> *</TD>
		</TR>
		<TR>
			<TD><%=identify%></TD>
			<TD><INPUT class='standardTxt' type="text" id='identify' name='identify' value='<%=rel_indentify%>' maxlength="30" size="30" onblur="noBadCharOnBlur(this);" onkeydown='checkBadChar(this,event);'/> *</TD>
			<TD>&nbsp;</TD>
			<TD><%=password%></TD>
			<TD width="20px"></TD>
			<TD><INPUT class='standardTxt' type="password" id='password' name='password' value='<%=rel_password%>' maxlength="16" size="30" onblur="noBadCharOnBlur(this);" onkeydown='checkBadChar(this,event);'/> *</TD>
		</TR>
		<TR>
			<TD><%=h_connectiontype%></TD>
			<TD><SELECT class='standardTxt' id='connectiontype' name='connectiontype' onchange="onchange_connection();">
				<OPTION value='0'>---------</OPTION>
				<OPTION <%=((site_to_load!=null)&&(site_to_load.getConnectiontype().equalsIgnoreCase("MODEM")))||(connection.equalsIgnoreCase("MODEM"))?"selected":"" %> value='MODEM'><%=modem%></OPTION>
			<% if ((site_to_load!=null)&&(!site_to_load.getType().equalsIgnoreCase("PVE"))||((site_to_load==null)&&(!type.equalsIgnoreCase("PVE"))))
			{%>	 
				<OPTION <%=((site_to_load!=null)&&(site_to_load.getConnectiontype().equalsIgnoreCase("LAN")))||(connection.equalsIgnoreCase("LAN"))?"selected":"" %> value='LAN'><%=lan%></OPTION>
			<%}%> 
			
			</SELECT> *</TD>
			<TD>&nbsp;</TD>
			<TD><p id='conn_desc'><%=phonenumber%></p></TD>
			<TD width="20px"></TD>
			<TD><INPUT class='standardTxt' type="text" id='phonenumber' name='phonenumber' value='<%=rel_phonenumber%>' maxlength="30" size="30" onblur="checkOnlyAnalogOnBlur(this,event);" onkeydown="checkOnlyAnalog(this,event);"/> *</TD>
		</TR>
	</TABLE>
</FIELDSET>
</FORM>
</td></tr>
</table>