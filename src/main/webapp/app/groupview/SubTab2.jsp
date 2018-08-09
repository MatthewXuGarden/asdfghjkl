<%@ page language="java" import="java.util.*" 
import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.presentation.tabmenu.*"
	import="com.carel.supervisor.presentation.dbllistbox.*"
	import="com.carel.supervisor.dataaccess.db.*"
	import="java.sql.Timestamp"
	import="com.carel.supervisor.dataaccess.datalog.impl.*"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	int site = sessionUser.getIdSite();
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
	String nodev = lan.getString("groupview","nodev");
	String groupcomment2 = lan.getString("groupview","groupcomment2");
	
	//retrieve lingua default
	LangUsedBeanList langList = new LangUsedBeanList();
	
	String langDefcode = sessionUser.getDefaultLanguage();
	String langDef = sessionUser.getDefaultLanguageDescription();
	
	//retrieve altre lingue
	LangUsedBean[] langNotDefault = langList.retrieveNotDefaultLanguages(site);
	
	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");
		
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
	tableData[i][1] = new HTMLSimpleElement("<input class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+langNotDefault[i].getLangcode()+"' name='"+langNotDefault[i].getLangcode()+"' value='' size='30' maxlength='30' onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/>");
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
	
	//NUOVO GRUPPO
	//LISTBOX DISPOSITIVI DI GLOBALE-GLOBALE  ->    ASSOCIATI VUOTO
	
	GroupListBean listOfGroups = new GroupListBean();
	DeviceListBean devListGlobalGroup = new DeviceListBean(site,language,1,true);
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
	
	
	List d = new ArrayList();
	DblListBox list2 = new DblListBox(c,d,false,true,true,null,true);
	list2.setScreenH(sessionUser.getScreenHeight());
	list2.setScreenW(sessionUser.getScreenWidth());
//	if (idsdevices!=null)
	list2.setRowsListBox(15);
	list2.setLeftRowsListBox(15);
	list2.setWidthListBox(390);
	//list2.setHeight(300);
	list2.setIdlistbox("dispositivi");
	list2.setHeaderTable1(availabledevices);
	list2.setHeaderTable2(devicesofarea);
	String listbox2 = list2.getHtmlDblListBox();
	
	
	GroupListBean groups= new GroupListBean();
	groups.retrieveAllGroups(site,language);
		int groupNumber = groups.getIds().length;
	String s_grouplimit = lan.getString("groupview","s_grouplimit");
	

%>
<input type="hidden" id="s_grouplimit" value="<%=s_grouplimit%>"/>
<input type="hidden" id="groupNumber" value="<%=groupNumber%>"/>
<input type="hidden" id="nodev" name="nodev" value="<%=nodev%>"/>
<input type="hidden" id="nullselected" name="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" id="missingdefault" name="missingdefault" value="<%=missingDefault%>"/>
<%=(OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "")%>

<table border="0" width="100%" height="95%" spacing="1" cellpadding="1">
	<tr height="5%">
		<td><p class="StandardTxt"><%=groupcomment2%></p></td>
	</tr>
	<tr height="10%">
		<td>
			<FORM name="frmnewgroup" id="frmnewgroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
			<INPUT type="hidden" id="devices" name="devices" value="">
			<INPUT type="hidden" id="cmd" name="cmd" value="add"/>
			<INPUT type="hidden" id="defLang" name="defLang" value="<%=langDefcode%>"/>
			<FIELDSET class="field">
			<LEGEND class="standardTxt"><%=description%></LEGEND>
				<TABLE class="standardTxt" border="0" width="100%">
					<TR >
						<TD width="15%"><%=descdefault%>(<%=langDef.trim()%>)</TD>
						<TD width="25%" class="standardTxt"><input <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" size="30" maxlength="30" id="<%=langDefcode%>" name="<%=langDefcode%>" value="" onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/> *</TD>
						<td width="*"> &nbsp;</td>
						<TD align="right" width="15%"><%=descrmultilang%></TD>
						<td align="right" width="40%"><%=langtab%></td>
					</TR>
				</TABLE>
			</FIELDSET>
			<input type="hidden" name="archtabtoload" id="archtabtoload" value="0"/>
			</FORM>
		</td>
	</tr>
	<TR height="*">
		<TD>
			<FIELDSET class="field" style="height:85%">
			<LEGEND class="standardTxt"><%=devices%></LEGEND>
				<TABLE width="100%" style="height:98%;">
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