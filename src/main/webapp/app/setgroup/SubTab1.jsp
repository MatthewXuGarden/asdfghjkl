<%@ page language="java" import="java.util.*" 
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
import="com.carel.supervisor.dataaccess.language.*"
import="com.carel.supervisor.presentation.tabmenu.*"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.dataaccess.db.*"
import="java.sql.Timestamp"
import="java.util.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="com.carel.supervisor.presentation.bean.GroupListBean"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String language = sessionUser.getLanguage();
	int site = sessionUser.getIdSite();
	int idgroup = Integer.parseInt(sessionUser.getProperty("group")) ;
	String jsession = request.getSession().getId();
	
	//sezione stringhe multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	String description = lan.getString("setarea","descr");
	String descdefault = lan.getString("setarea","descrdefault");
	String descrmultilang = lan.getString("setarea","descrmultilang");
	String devices = lan.getString("setarea","devices");
	String availabledevices = lan.getString("setarea","availabledevices");
	String devicesofarea = lan.getString("setarea","devicesofarea");
	String nullselected = lan.getString("dbllistbox","nullselected");
	String lang = lan.getString("setarea","lang");
	String missingDefault = lan.getString("setarea","missingdefault");
	String lastdev = lan.getString("groupview","lastdev");

	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");		
	
	//retrieve lingua default
	LangUsedBeanList langList = new LangUsedBeanList();
	
	String langDefcode = sessionUser.getDefaultLanguage();
	String langDef = sessionUser.getDefaultLanguageDescription();
	
	//retrieve tutte lingue
	LangUsedBean[] langNotDefault = langList.retrieveNotDefaultLanguages(site);
	
	//POPOLAZIONE TEXT DESCRIZIONI GRUPPO
	//default
	String valueDefault = TableExtBean.retrieveDescritionFromTableById(site,langDefcode,idgroup,"cfgroup");
	
	//altre
	String[] desc = new String[langNotDefault.length];
	for (int i=0;i<langNotDefault.length;i++)
	{
		desc[i] = TableExtBean.retrieveDescritionFromTableById(site,langNotDefault[i].getLangcode(),idgroup,"cfgroup");
		if (desc[i]==null) desc[i]=valueDefault;
	}
			
	//CREAZIONE TABELLA LINGUE
	String header[] = new String[2];
	header[0]= lang;
	header[1]= description;
	
	HTMLElement[][] tableData = null;
	tableData = new HTMLElement[langNotDefault.length][];
	for (int i=0;i<langNotDefault.length;i++)
	{
	tableData[i]= new HTMLElement[2];
	tableData[i][0] = new HTMLSimpleElement(langNotDefault[i].getLangdescription());
	tableData[i][1] = new HTMLSimpleElement("<input "+(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+langNotDefault[i].getLangcode()+"' name='"+langNotDefault[i].getLangcode()+"' value='"+desc[i]+"' size='30' maxlength='30' onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/>");
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
	
	//CASO MODIFICA GROUP
		//LISTBOX DISPOSITIVI ORFANI
	GroupListBean groups = sessionUser.getGroup();
	int idGlobalGroup = groups.getGlobalGroup().getGroupId();
	DeviceListBean devListGlobalGroup = new DeviceListBean(site,language,idGlobalGroup,true);
	int[] idsdevices = devListGlobalGroup.getIds();
		
	List c = new ArrayList(); 
	if (idsdevices!=null)
	{
		for (int i=0;i<idsdevices.length;i++)
		{
			DeviceBean aux = devListGlobalGroup.getDevice(idsdevices[i]);
			ListBoxElement tmp = new ListBoxElement(aux.getDescription(), String.valueOf(aux.getIddevice()));
			c.add(tmp);
		}
	}
		
		
	//LISTBOX DISPOSITIVI ASSOCIATI AL GRUPPO
		
	DeviceListBean devListOfGroup = new DeviceListBean(site,language,idgroup,true);
	idsdevices = devListOfGroup.getIds();
	List d = new ArrayList();
	if (idsdevices!=null)
	{
		for (int i=0;i<idsdevices.length;i++)
		{
			DeviceBean aux = devListOfGroup.getDevice(idsdevices[i]);
			ListBoxElement tmp = new ListBoxElement(aux.getDescription(), String.valueOf(aux.getIddevice()));
			d.add(tmp);
		}
	}
	
	DblListBox list2 = new DblListBox(c,d,isProtected,true,true,null,true);
	list2.setScreenH(sessionUser.getScreenHeight());
	list2.setScreenW(sessionUser.getScreenWidth());
	
	list2.setHeight(200);
	if (idsdevices!=null)
		list2.setRowsListBox(idsdevices.length);
	
	list2.setWidthListBox(380);
	list2.setIdlistbox("dispositivi");
	list2.setHeaderTable1(availabledevices);
	list2.setHeaderTable2(devicesofarea);
	String listbox2 = list2.getHtmlDblListBox();
	

%>
<input type="hidden" id="lastdev" name="lastdev" value="<%=lastdev%>"/>
<input type="hidden" id="nullselected" name="nullselected" value="<%=nullselected%>"/>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<INPUT type="hidden" id="missingdefault" name="missingdefault" value="<%=missingDefault%>"/>
<table border="0" height="95%" width="100%" cellspacing="1" cellpadding="1">
	
	<tr height="25%">
		<td><FORM name="frmsetgroup" id="frmsetgroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
			<INPUT type="hidden" id="devices1" name="devices1" value="">
			<INPUT type="hidden" id="devices2" name="devices2" value="">
			<INPUT type="hidden" id="group" name="group" value="<%=idgroup%>">
			<INPUT type="hidden" id="defLang" name="defLang" value="<%=langDefcode%>"/>
			<input type="hidden" id="cmd" name="cmd" value=""/>
			<input type="hidden" id="archgoback" name="archgoback" value="false"/>
			<FIELDSET class="field" style="">
			<LEGEND class="standardTxt"><%=description%></LEGEND>
				<TABLE class="standardTxt" border="0" align="center" width="100%">
					<TR>
						<TD width="15%"><%=descdefault%>(<%=langDef.trim()%>)</TD>
						<TD width="25%" class="standardTxt"><input  <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" size="30" maxlength="30" id="<%=langDefcode%>" name="<%=langDefcode%>" value="<%=valueDefault%>" onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/> *</TD>
						<TD width="*">&nbsp;</TD>
						<TD width="15%" align="right"><%=descrmultilang%></TD>
						<td width="40%" align="right"><%=langtab%></td>
					</TR>
				</TABLE>
			</FIELDSET>
			</FORM>
		</td>
	</tr>
	<TR height="*">
		<TD>
			<FIELDSET class="field" style="height:95%;">
			<LEGEND class="standardTxt"><%=devices%></LEGEND>			
			<TABLE width="100%" height="98%" border="0">
					<TR>
						<TD>
							<%=listbox2%>
						</TD>
					</TR>
				</TABLE>			
			</FIELDSET>
		</TD>
	</TR>
</table>