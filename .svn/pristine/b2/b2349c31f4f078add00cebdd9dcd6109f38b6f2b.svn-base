<%@ page language="java" 
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
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String language = sessionUser.getLanguage();
	int site = sessionUser.getIdSite();
	int idgroup = Integer.parseInt(sessionUser.getProperty("group")) ;
	String idvar = sessionUser.getProperty("idvar");
	if (idvar==null) idvar="";
	String cmd = sessionUser.getPropertyAndRemove("cmd");
	if (cmd==null) cmd="";
	String jsession = request.getSession().getId();
	GroupVarBeanList list = new GroupVarBeanList();
	GroupVarBean editVar = null;
	int idEditVar = -1;
	
	if (!idvar.equals("")) idEditVar = Integer.parseInt(idvar);
	if (cmd.equals("set_alr"))
	{
		editVar = list.retrieveGroupVarById(sessionUser.getIdSite(),idEditVar,language);
	}
	
	//sezione stringhe multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
	String description = lan.getString("setarea","descr");
	String descdefault = lan.getString("setarea","descrdefault");
	String descrmultilang = lan.getString("setarea","descrmultilang");
	String listGroupVariable = lan.getString("setgroup","listgroupvariable");
	String lang = lan.getString("setarea","lang");
	String type = lan.getString("setgroup","type");
	String variables = lan.getString("setgroup","variables");
	String reallydeletevar = lan.getString("setgroup","reallydeletevar");
	String confirmChangeVariable = lan.getString("setgroup","confirmchangevariable");
	String doubleElement = lan.getString("dbllistbox","doublelement");
	String nullselected = lan.getString("dbllistbox","nullselected");
	String missingDefault = lan.getString("setarea","missingdefault");
	String novar = lan.getString("setgroup","novar");
	String varsofdevice = lan.getString("setgroup","varsofdevice");
	String varsofgroupvar = lan.getString("setgroup","varsofgroupvar");
	String deviceofgroup = lan.getString("setgroup","deviceofgroup");
	//TABELLA LISTA VARIABILI DI GRUPPO
	
	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");		
	
	GroupVarBean[] listVar = list.retrieveGroupAlrByIdGroup(site,idgroup,language);
	int rows = listVar.length;
	String headerVar[] = new String[1];
	headerVar[0]= listGroupVariable;
	HTMLElement[][] tableDataVar = null;
	String[] dblClickRowFunction = null;
	String[] ClickRowFunction = null;
	
	tableDataVar = new HTMLElement[rows][];
	dblClickRowFunction = new String[rows];
	ClickRowFunction = new String[rows];
	
	for (int i=0;i<rows;i++)
	{
	tableDataVar[i]= new HTMLElement[1];
	tableDataVar[i][0] = new HTMLSimpleElement(listVar[i].getDescription());
	tableDataVar[i][0].addAttribute("id", String.valueOf(listVar[i].getIdGroupVar()));
	tableDataVar[i][0].addAttribute("name", String.valueOf(listVar[i].getIdGroupVar()));
	ClickRowFunction[i]= String.valueOf(listVar[i].getIdGroupVar());
	dblClickRowFunction[i]= "";
	}
	
	HTMLTable varTable = new HTMLTable("", headerVar, tableDataVar);
	varTable.setSgClickRowAction("selectedVarGroup('$1')");
	varTable.setDbClickRowAction("modifyVarGroup()");
	varTable.setDlbClickRowFunction(dblClickRowFunction);
	varTable.setSnglClickRowFunction(ClickRowFunction);
	varTable.setScreenH(sessionUser.getScreenHeight());
	varTable.setScreenW(sessionUser.getScreenWidth());
	varTable.setWidth(900);
	varTable.setHeight(100);
	varTable.setColumnSize(0,860);
	varTable.setTableId(1);
	String vartab = varTable.getHTMLText();
	
	
	
	//SEZIONE DESCRIZIONI VARIABILI IN MULTILINGUA
	//retrieve lingua default
	String sql = "select cflanguage.languagecode,cflanguage.description from cfsiteext,cflanguage where cflanguage.languagecode = cfsiteext.languagecode and cfsiteext.idsite = ? and cfsiteext.isdefault = ? ";
	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,new Object[] {new Integer(site),"TRUE"});
	String langDefcode = rs.get(0).get("languagecode").toString();
	String langDef = rs.get(0).get("description").toString();
	//retrieve tutte lingue
	sql = "select cflanguage.languagecode,cflanguage.description from cfsiteext,cflanguage where cflanguage.languagecode = cfsiteext.languagecode and cfsiteext.idsite = ? and cfsiteext.isdefault = ? ";
	rs = DatabaseMgr.getInstance().executeQuery(null, sql,new Object[] {new Integer(site),"FALSE"});
	String[] languages = new String[rs.size()];
	String[] languagescode = new String[rs.size()];
	String arrayLang = "";
	
	for (int i=0;i<rs.size();i++)
	{
		languagescode[i]=rs.get(i).get("languagecode").toString();  //tutti codici lingue
		languages[i]=rs.get(i).get("description").toString().trim();		//tutte descr lingue
		arrayLang = arrayLang + rs.get(i).get("languagecode").toString().trim() + ",";
	}
	arrayLang = arrayLang + langDefcode;
		
	//Fill descrizioni 
	String header[] = new String[2];
	header[0]= lang;
	header[1]= description;
	String value = "";
	HTMLElement[][] tableData = null;
	tableData = new HTMLElement[languages.length][];
		
	if (cmd.equals("set_alr"))
	{
		for (int i=0;i<languagescode.length;i++)
		{
		tableData[i]= new HTMLElement[2];
		tableData[i][0] = new HTMLSimpleElement(languages[i]);
		value = TableExtBean.retrieveDescritionFromTableById(site,languagescode[i],Integer.parseInt(idvar),"cfgroupvar");
		if (value.equals("")) 
			value = TableExtBean.retrieveDescritionFromTableById(site,langDefcode,Integer.parseInt(idvar),"cfgroupvar");
		tableData[i][1] = new HTMLSimpleElement("<input " +(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+languagescode[i]+"' name='"+languagescode[i]+"' value='"+value+"' size='30' maxlength='30' onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/>");
		}
	}
	else if ((cmd.equals("clean"))||(cmd.equals("rem_alr"))||(cmd.equals("add_alr"))||(cmd.equals("mod_alr")))
	{
		for (int i=0;i<languagescode.length;i++)
		{
		tableData[i]= new HTMLElement[2];
		tableData[i][0] = new HTMLSimpleElement(languages[i]);
		tableData[i][1] = new HTMLSimpleElement("<input " +(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+languagescode[i]+"' name='"+languagescode[i]+"' value='' size='30' maxlength='30' onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/>");
		}
	}
	else
	{ 
		for (int i=0;i<languagescode.length;i++)
		{
		tableData[i]= new HTMLElement[2];
		tableData[i][0] = new HTMLSimpleElement(languages[i]);
		
		value = sessionUser.getProperty(languagescode[i]);
		if(value == null) 
			value = "";
			
		tableData[i][1] = new HTMLSimpleElement("<input " +(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+languagescode[i]+"' name='"+languagescode[i]+"' value='"+value+"' size='30' maxlength='30'/>");
		}
	}
	
	HTMLTable languageTable = new HTMLTable("", header, tableData);
	languageTable.setScreenH(sessionUser.getScreenHeight());
	languageTable.setScreenW(sessionUser.getScreenWidth());
	languageTable.setWidth(339);
	languageTable.setHeight(75);
	languageTable.setRowHeight(20);
	languageTable.setColumnSize(0,40);
	languageTable.setColumnSize(1,235);
	languageTable.setTableId(2);
	String langtab = languageTable.getHTMLText();
	
//POPLAZIONE COMBOBOX DEVICE

DeviceListBean dev = new DeviceListBean(site,language,idgroup);
StringBuffer deviceCombo = new StringBuffer("");
if ((cmd.equals("clean"))||(cmd.equals("rem_alr"))||(cmd.equals("add_alr"))||(cmd.equals("mod_alr")))
{
	deviceCombo.append("<OPTION selected value=\"\">------------------</OPTION>\n");
}
else
{
	deviceCombo.append("<OPTION value=\"\">------------------</OPTION>\n");
}

int idsdevices[]= null;
String def = null;
String devselected = sessionUser.getProperty("device");
if  ((devselected==null)||(devselected.equals(""))) 
	devselected="-1";	
int devSelected= Integer.parseInt(devselected);
idsdevices=dev.getIds();	
for (int i=0;i<idsdevices.length;i++)
{
	if ((idsdevices[i]==devSelected)&&(!cmd.equals("clean"))&&(!cmd.equals("rem_alr"))&&(!cmd.equals("add_alr"))&&(!cmd.equals("mod_alr")))
		def = "selected";
	else
			def = "";
	deviceCombo.append("<OPTION "+def+" value=\""+idsdevices[i]+"\">"+dev.getDevice(idsdevices[i]).getDescription()+"</OPTION>\n");
}

String deviceValue = deviceCombo.toString();

//VARIABILI RW DEL DEVICE
int iddevice = devSelected;

VarphyBean[] vars = new VarphyBeanList().getAlarmVarPhy(language,site,iddevice);

List a = new ArrayList(); 
if ((cmd.equals("clean"))||(cmd.equals("rem_alr"))||(cmd.equals("add_alr"))||(cmd.equals("mod_alr")))
{
}
else if (vars.length!=0)
{
	for(int i=0;i<vars.length;i++)
	{
		ListBoxElement tmp = new ListBoxElement(vars[i].getShortDescription(),String.valueOf(vars[i].getId()));
		a.add(tmp);
	}	
}
List b = new ArrayList();
if (cmd.equals("set_alr"))
{
	String[] pks = editVar.getParameters().split(";");
	int ids[] = new int[pks.length];
	for (int i=0;i<pks.length;i++)
	{
		ids[i]=Integer.parseInt(pks[i].substring(2));
	}
	VarphyBean[] listOfVars = new VarphyBeanList().getListVarByIds(site,language,ids);
	for (int i=0;i<listOfVars.length;i++)
	{
		ListBoxElement tmp = new ListBoxElement(listOfVars[i].getShortDescription(),String.valueOf(listOfVars[i].getId()));
		b.add(tmp);
	}
}
else if ((cmd.equals("clean"))||(cmd.equals("rem_alr"))||(cmd.equals("add_alr"))||(cmd.equals("mod_alr")))
{
}
else
{
	String varsList = sessionUser.getProperty("varsList");
	if (varsList==null) varsList="";
	if (!varsList.equals(""))
	{
			String[] varsToArray = varsList.split(",");
			int[] varsOfVariable = new int[varsToArray.length];
			for (int i=0;i<varsToArray.length;i++)
				 varsOfVariable[i]= Integer.parseInt(varsToArray[i]);
			VarphyBean[] listOfVars = new VarphyBeanList().getListVarByIds(site,language,varsOfVariable);
			for (int i=0;i<listOfVars.length;i++)
			{
				ListBoxElement tmp = new ListBoxElement(listOfVars[i].getShortDescription(),String.valueOf(listOfVars[i].getId()));
				b.add(tmp);
			}
	}
}

DblListBox variableList = new DblListBox(a,b,isProtected);
variableList.setSrcButton2("images/dbllistbox/delete_on.png");
variableList.setFncButton2("to1Rem(this);varGroups_reload();return false;");
variableList.setScreenH(sessionUser.getScreenHeight());
variableList.setScreenW(sessionUser.getScreenWidth());
variableList.setRowsListBox(6);
variableList.setWidthListBox(350);
variableList.setHeaderTable1(varsofdevice);
variableList.setHeaderTable2(varsofgroupvar);
variableList.setIdlistbox("variabili");
String listaVariabili = variableList.getHtmlDblListBox();


%>
<INPUT type="hidden" id="novar" name="novar" value="<%=novar%>"/>
<INPUT type="hidden" id="missingdefault" name="missingdefault" value="<%=missingDefault%>"/>
<INPUT type="hidden" id="nullselected" name="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" id="languages" name="languages" value="<%=arrayLang%>"/>
<INPUT type="hidden" id="reallydeletevar" name="reallydeletevar" value="<%=reallydeletevar%>"/>
<INPUT type="hidden" id="confirmChangeVariable" name="confirmChangeVariable" value="<%=confirmChangeVariable%>"/>
<INPUT type="hidden" id="doubleElement" name="doubleElement" value="<%=doubleElement%>"/>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<FORM name="frmsetgroup" id="frmsetgroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="group" name="group" value="<%=idgroup%>"/>
<INPUT type="hidden" id="varsList" name="varsList" value=""/>
<INPUT type="hidden" id="defLang" name="defLang" value="<%=langDefcode%>"/>
<INPUT type="hidden" id="cmd" name="cmd" value="<%=cmd%>"/>
<INPUT type="hidden" id="idvar" name="idvar" value="<%=(idEditVar!=-1)?String.valueOf(idEditVar):""%>"/>
<p class='standardTxt'>Commento alla pagina</p>
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td>
			<%=vartab%>
		</td>
	</tr>
</table>

<p></p>
<FIELDSET class="field">
	<LEGEND class="standardTxt"><%=description%></LEGEND>
		<TABLE class="standardTxt">
			<TR>
				<TD><%=descdefault%>(<%=langDef.trim()%>)</TD><TD><input <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" size="30" maxlength="30" id="<%=langDefcode%>" name="<%=langDefcode%>" value="<%=(cmd.equals("set_alr"))?editVar.getDescription():(((sessionUser.getProperty(langDefcode))==null)||(cmd.equals("clean"))||(cmd.equals("rem_alr"))||(cmd.equals("add_alr"))||(cmd.equals("mod_alr")))?"":sessionUser.getProperty(langDefcode)%>" onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/> *</TD>
				<TD width="30px"></TD>
				<TD><%=descrmultilang%></TD><td><%=langtab%></td>
			</TR>
			<TR height="15px"></TR>
		</TABLE>
</FIELDSET>
<p></p>
<FIELDSET class="field">
	<LEGEND class="standardTxt"><%=variables%></LEGEND>
		<TABLE>
			<TR>
				<TD class="standardTxt"><%=deviceofgroup%></TD>
				<TD><SELECT <%=(isProtected?"disabled":"")%> class="standardTxt" id="device" name="device" onchange="varGroups_reload()"><%=deviceValue%></TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE class="standardTxt" align='center'>
			<TR>
				<td><%=listaVariabili%></td>
			</TR>
		</TABLE>
	</FIELDSET>
</FORM>