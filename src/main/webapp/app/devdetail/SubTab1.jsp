<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceListBean" %>
<%@ page import="com.carel.supervisor.presentation.bo.helper.DeviceStatus" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();

	int iddev = Integer.parseInt(sessionUser.getProperty("iddev"));

	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");
	
	DeviceListBean devList= new DeviceListBean(idsite,language);
  	DeviceBean device= devList.retrieveSingleDeviceById(idsite,iddev, language);
	String descr= device.getDescription();
	String image = device.getImageDevice();
	
	// Espongo idDevice per Logic Device SDK	
	descr += "  [" + iddev+ "]";	
	
	String isenabled = DeviceStatus.isEnabled(iddev);
	if (isenabled==null) isenabled="FALSE";
	//sezione stringhe multilingua
	String description = lan.getString("devdetail","description");
	String descdefault = lan.getString("devdetail","descdefault");
	String descrmultilang = lan.getString("devdetail","descrmultilang");
	String state = lan.getString("devdetail","state");
	String chkstate = lan.getString("devdetail","chkstate");
	String lang = lan.getString("devdetail","lang");
	String devconfcomment1 = lan.getString("devdetail","devconfcomment1");
	String missingdefault = lan.getString("devdetail","missingdefault");
		
	//retrive lingua default e altre
	LangUsedBeanList langList = new LangUsedBeanList();
	String langDefcode = sessionUser.getDefaultLanguage();
	String langDef = sessionUser.getDefaultLanguageDescription();
	
	LangUsedBean[] langNotDefault = langList.retrieveNotDefaultLanguages(idsite);
	
	//POPOLAZIONE TEXT DESCRIZIONI DISPOSITIVI
	//default
	String valueDefault = TableExtBean.retrieveDescritionFromTableById(idsite,langDefcode,iddev,"cfdevice");
	
	//altre
	String[] desc = new String[langNotDefault.length];
	for (int i=0;i<langNotDefault.length;i++)
	{
		desc[i] = TableExtBean.retrieveDescritionFromTableById(idsite,langNotDefault[i].getLangcode(),iddev,"cfdevice");
		if (desc[i]==null) desc[i]=valueDefault;
	}
	
	//tabella lingue
	String header[] = new String[2];
	header[0]= lang;
	header[1]= description;
	
	HTMLElement[][] tableData = null;
	tableData = new HTMLElement[langNotDefault.length][];
	for (int i=0;i<langNotDefault.length;i++)
	{
	tableData[i]= new HTMLElement[2];
	tableData[i][0] = new HTMLSimpleElement(langNotDefault[i].getLangdescription());
	tableData[i][1] = new HTMLSimpleElement("<input "+(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text'id='"+langNotDefault[i].getLangcode()+"' name='"+langNotDefault[i].getLangcode()+"' value='"+desc[i]+"' size='60' maxlength='100' onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'/>");
	}
	
	
	HTMLTable languageTable = new HTMLTable("", header, tableData);
	languageTable.setScreenH(sessionUser.getScreenHeight());
	languageTable.setScreenW(sessionUser.getScreenWidth());
	languageTable.setWidth(339);
	languageTable.setHeight(75);
	languageTable.setRowHeight(20);
	languageTable.setColumnSize(0,40);
	languageTable.setColumnSize(1,235);
	String langtab = languageTable.getHTMLText();
	
	FileDialogBean fileDlg = new FileDialogBean(request);
%>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<%=fileDlg.renderFileDialog()%>

<INPUT type="hidden" id="missingdefault" name="missingdefault" value="<%=missingdefault%>">
<input id="isntimage" type="hidden" value="<%=lan.getString("mgr","isntimage")%>">

<p class="tdTitleTable"><%=descr%></p>
<p class="standardTxt"><%=devconfcomment1%></p>
<FORM name="frm_dev_save" id="frm_dev_save"" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td>
			<INPUT type="hidden" id="defLang" name="defLang" value="<%=langDefcode%>"/>
			<INPUT type="hidden" id="cmd" name="cmd" value=""/>
			<INPUT type="hidden" id="iddev" name="iddev" value="<%=iddev%>"/>
			<INPUT type="hidden" id="chk_state" name="chk_state" value=""/>
			<FIELDSET class='field'>
			<LEGEND class="standardTxt"><%=description%></LEGEND>
			<TABLE class="standardTxt">
				<TR>
					<TD align="center" class='standardTxt'><%=descdefault%> (<%=langDef.trim()%>)</TD><TD><input <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" size="60" maxlength="100" id="<%=langDefcode%>" name="<%=langDefcode%>" value="<%=valueDefault%>" onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'/>
					</TD>
					<TD width="5px"> *</TD>
					<TD align="center"><%=descrmultilang%></TD><td><%=langtab%></td>
				</TR>
				<TR height="15px"></TR>
			</TABLE>
			</FIELDSET>
		</td>
	</tr>
</table>
<br>
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<TR>
		<TD><FIELDSET class='field'>
			<LEGEND class="standardTxt"><%=state%></LEGEND>	
			<TABLE>
				<TR>
					<TD><INPUT <%=(isProtected?"disabled":"")%> <%=(isenabled.equalsIgnoreCase("FALSE"))?"checked":""%> type="checkbox" id="isenabled" name="isenabled" /></TD>
					<TD class="standardTxt"><%=chkstate%></TD>
					</TR>
			</TABLE>
		</FIELDSET>
		</TD>
	</TR>		
</TABLE>
</FORM>
<br>
<form name="uploadfrm" id="uploadfrm" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
<input type="hidden" name="tipofile" value="devimg_<%=iddev%>">
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<TR>
		<TD><FIELDSET class='field'>
			<LEGEND class="standardTxt"><%=lan.getString("devdetail","image")%></LEGEND>	
			<TABLE width="100%">
			<tr valign="middle">
			<td align="left" width="33%">
			<%=fileDlg.inputLoadFile("changeimg", "png,jpg,jpeg,gif,bmp", "size='30%'", false)%>
			</td>
			<td align="left" width="1%">
			<img title="<%=lan.getString("devdetail","change_image")%>" style="cursor:pointer" src="images/actions/import_on_black.png" onclick="image_submit()">
			</td>
			<td align="center" width="33%"><img src="images/devices/<%=image%>"></td>
			<td align="right" width="33%"><img src="images/varpos/mainimg.JPG"></td>
			</tr>
			</TABLE>
		</FIELDSET>
		</TD>
	</TR>
</table>
</form>
	