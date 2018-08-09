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
	import="com.carel.supervisor.presentation.bean.UnitOfMeasurementBeanList" 
	import="com.carel.supervisor.presentation.bean.UnitOfMeasurementBean"
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
	
	String langDefaultcode = sessionUser.getDefaultLanguage();
	String valueDefault = TableExtBean.retrieveDescritionFromTableById(site,langDefaultcode,idgroup,"cfgroup");
	
	if (!idvar.equals("")) idEditVar = Integer.parseInt(idvar);
	if (cmd.equals("set"))
	{
		editVar = list.retrieveGroupVarById(sessionUser.getIdSite(),idEditVar,langDefaultcode);
	}
	
	//sezione stringhe multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
	String description = lan.getString("setarea","descr");
	String descdefault = lan.getString("setarea","descrdefault");
	String descrmultilang = lan.getString("setarea","descrmultilang");
	String listGroupVariable = lan.getString("setgroup","listgroupvariable");
	String lang = lan.getString("setarea","lang");
	String type = lan.getString("setgroup","type");
	String digital = lan.getString("setgroup","digital");
	String analog = lan.getString("setgroup","analog");
	String integer = lan.getString("setgroup","integer");
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
	String uminsert = lan.getString("setgroup","uminsert");
	String unitMeasurement = lan.getString("logicvariablePage","unitmeasurement");
	String comment = lan.getString("setgroup","comment");	
	//TABELLA LISTA VARIABILI DI GRUPPO
	
	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");		
	
	GroupVarBean[] listVar = list.retrieveGroupVarByIdGroup(site,idgroup,language);
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
	varTable.setHeight(105);
	varTable.setColumnSize(0,867);
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
		
	if (cmd.equals("set"))
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
	else if ((cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))
	{
		for (int i=0;i<languagescode.length;i++)
		{
		tableData[i]= new HTMLElement[2];
		tableData[i][0] = new HTMLSimpleElement(languages[i]);
		tableData[i][1] = new HTMLSimpleElement("<input "+(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+languagescode[i]+"' name='"+languagescode[i]+"' value='' size='30' maxlength='30' onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/>");
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
			
		tableData[i][1] = new HTMLSimpleElement("<input "+(isProtected?"disabled":"") + " class='lswtype"+cssVirtualKeyboardClass+"' type='text' id='"+languagescode[i]+"' name='"+languagescode[i]+"' value='"+value+"' size='30' maxlength='30'/>");
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

DeviceListBean dev = new DeviceListBean(site,language,idgroup,true);
StringBuffer deviceCombo = new StringBuffer("");
if ((cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))
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
	if ((idsdevices[i]==devSelected)&&(!cmd.equals("clean"))&&(!cmd.equals("rem"))&&(!cmd.equals("add"))&&(!cmd.equals("mod")))
		def = "selected";
	else
			def = "";
	deviceCombo.append("<OPTION "+def+" value=\""+idsdevices[i]+"\">"+dev.getDevice(idsdevices[i]).getDescription()+"</OPTION>\n");
}

String deviceValue = deviceCombo.toString();

//VARIABILI RW DEL DEVICE
int iddevice = devSelected;
String vartype = sessionUser.getProperty("variableType");

if ((vartype==null)||vartype.equals(""))
	 vartype="1";
int varType = Integer.parseInt(vartype);
if (cmd.equals("set"))
{
	varType = editVar.getType();
}
else if ((cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))
	varType = 1;

VarphyBean[] vars = new VarphyBeanList().getListVarRwByType(language,site,iddevice,varType);

List a = new ArrayList(); 
if ((cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))
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
DeviceListBean devices = new DeviceListBean(site,language);
List b = new ArrayList();
String dev_desc = "";
if (cmd.equals("set"))
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
		dev_desc = devices.getDevice(listOfVars[i].getDevice().intValue()).getDescription();
		ListBoxElement tmp = new ListBoxElement(dev_desc+" -> " +listOfVars[i].getShortDescription(),String.valueOf(listOfVars[i].getId()));
		b.add(tmp);
	}
}
else if ((cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))
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
				dev_desc = devices.getDevice(listOfVars[i].getDevice().intValue()).getDescription();
				ListBoxElement tmp = new ListBoxElement(dev_desc+" -> " + listOfVars[i].getShortDescription(),String.valueOf(listOfVars[i].getId()));
				b.add(tmp);
			}
	}
}

DblListBox variableList = new DblListBox(a,b,isProtected,true,true,null,true);
variableList.setSrcButton2("images/dbllistbox/arrowsx_on.png");
variableList.setFncButton2("to1Rem(this);varGroups_reload();return false;");
if (vars!=null)
	variableList.setRowsListBox(vars.length);

variableList.setScreenH(sessionUser.getScreenHeight());
variableList.setScreenW(sessionUser.getScreenWidth());
variableList.setLeftRowsListBox(15);
variableList.setRightRowsListBox(15);

variableList.setWidthListBox(390);
variableList.setWidth_select(850);
variableList.setHeaderTable1(varsofdevice);
variableList.setHeaderTable2(varsofgroupvar);
variableList.setIdlistbox("variabili");
variableList.setCustomFunction(true);
variableList.setCustomFuncName("checkIdvarLenthByDB(255)");
String listaVariabili = variableList.getHtmlDblListBox();


//combo measureunit
StringBuffer umCombo = new StringBuffer("");
umCombo.append("<SELECT class='standardTxt' type='combobox' id='measureunit' name='measureunit'>");
UnitOfMeasurementBeanList umList = new UnitOfMeasurementBeanList();
umList.loadAllUnitOfMeasurement();
UnitOfMeasurementBean tmp = null;
String measureunit = "";
String selected = "";
if (cmd.equals("set"))
{
	measureunit = editVar.getMeasureunit();
}
else if (cmd.equals("set2")||cmd.equals("reload"))
{
	measureunit = sessionUser.getProperty("measureunit");
}
for (int i=0;i<umList.size();i++)
{
	
	tmp = umList.getUnitOfMeasurement(i);
	if ((tmp.getDescription().equals(measureunit))&&(!cmd.equals("clean"))&&(!cmd.equals("rem"))&&(!cmd.equals("add"))&&(!cmd.equals("mod")))
		selected = "selected";
	else
		selected = "";
	umCombo.append("<OPTION "+selected+" value='"+tmp.getDescription()+"'>"+tmp.getDescription()+"</OPTION>");
}
umCombo.append("</SELECT>");



%>
<INPUT type="hidden" id="uminsert" value="<%=uminsert%>"/>
<INPUT type="hidden" id="novar" name="novar" value="<%=novar%>"/>
<INPUT type="hidden" id="missingdefault" name="missingdefault" value="<%=missingDefault%>"/>
<INPUT type="hidden" id="nullselected" name="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" id="languages" name="languages" value="<%=arrayLang%>"/>
<INPUT type="hidden" id="reallydeletevar" name="reallydeletevar" value="<%=reallydeletevar%>"/>
<INPUT type="hidden" id="confirmChangeVariable" name="confirmChangeVariable" value="<%=confirmChangeVariable%>"/>
<INPUT type="hidden" id="doubleElement" name="doubleElement" value="<%=doubleElement%>"/>
<INPUT type="hidden" id="exceedLength" name="exceedLength" value="<%= lan.getString("group_var","exceed_length")%>"/> 
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<FORM name="frmsetgroup" id="frmsetgroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="group" name="group" value="<%=idgroup%>"/>
<INPUT type="hidden" id="varsList" name="varsList" value=""/>
<INPUT type="hidden" id="defLang" name="defLang" value="<%=langDefcode%>"/>
<INPUT type="hidden" id="cmd" name="cmd" value="<%=cmd%>"/>
<INPUT type="hidden" id="idvar" name="idvar" value="<%=(idEditVar!=-1)?String.valueOf(idEditVar):""%>"/>

<table border="0" width="100%" height="95%" cellspacing="0" cellpadding="3">
	<tr height="1%"><td><p class='standardTxt'><%=comment%></p></td></tr>
	<tr height="10%">
		<td>
			<%=vartab%>
		</td>
	</tr>
	<tr height="10%">
		<td>
<FIELDSET class="field">
	<LEGEND class="standardTxt"><%=description%></LEGEND>
		<TABLE class="standardTxt" width="100%">
				<TR>
					<TD width="15%"><%=descdefault%>(<%=langDef.trim()%>)</TD>
					<TD width="25%" class="standardTxt"><input <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" size="30" maxlength="30" id="<%=langDefcode%>" name="<%=langDefcode%>" value="<%=(cmd.equals("set"))?editVar.getDescription():(((sessionUser.getProperty(langDefcode))==null)||(cmd.equals("clean"))||(cmd.equals("rem"))||(cmd.equals("add"))||(cmd.equals("mod")))?"":sessionUser.getProperty(langDefcode)%>" onblur='noBadCharOnBlur(this,event)' onkeydown='checkBadChar(this,event);'/> *</TD>
					<TD width="*"></TD>
					<TD width="15%" align="right"><%=descrmultilang%></TD>
					<td width="40%" align="right"><%=langtab%></td>
			</TR>
		</TABLE>
</FIELDSET>
		</td>
	</tr>
	<tr height="10%">
		<td>
<FIELDSET class="field" style="height:85%">
	<LEGEND class="standardTxt"><%=type%></LEGEND>
		<TABLE  align='left' class="standardTxt" width="100%" border="0">
			<TR>
				<TD width="10%"><input <%=(varType==1)?"checked":""%> id="variableType1" <%=(isProtected?"disabled":"")%> type="radio" name="variableType" value="1" onclick="varGroups_reloadChangeVariable(this)"><%=digital%></TD>
				<TD width='*'>&nbsp;</TD>
				<TD width="10%"><input <%=(varType==2)?"checked":""%> id="variableType2" <%=(isProtected?"disabled":"")%> type="radio" name="variableType" value="2" onclick="varGroups_reloadChangeVariable(this)"><%=analog%></TD>
				<TD width='*'>&nbsp;</TD>
				<TD width="10%"><input <%=(varType==3)?"checked":""%> id="variableType3" <%=(isProtected?"disabled":"")%> type="radio" name="variableType" value="3" onclick="varGroups_reloadChangeVariable(this)"><%=integer%></TD>
				<TD width='*'>&nbsp;</TD>
				<TD width="15%"><%=unitMeasurement%></TD>
				<TD width="10%"><%=umCombo.toString()%></TD>			
			</TR>
		</TABLE>
</FIELDSET>
		</td>
		
	<tr><td style="height: 2px;"></td></tr>
	<tr height="*">
		<td>
<FIELDSET class="field" style="height:98%;">
	<LEGEND class="standardTxt"><%=variables%></LEGEND>
		<TABLE width="100%" border="0" height="98%">
			<TR>
				<TD width="30%" class="standardTxt"><%=deviceofgroup%>: <b><%=valueDefault%></b></TD>
				<TD width="70%"><SELECT class="standardTxt"  <%=(isProtected?"disabled":"")%> id="device" name="device" onchange="varGroups_reload()"><%=deviceValue%></SELECT></TD>
			</TR>
		    <!-- TR><td colspan="2" height=""> &nbsp;</td></TR -->
		
			<TR height="80%;">
				<td class="standardTxt" colspan="2"><%=listaVariabili%></td>
			</TR>
		</table>			
	</FIELDSET>
		</td>
	</tr>
</table>
</FORM>