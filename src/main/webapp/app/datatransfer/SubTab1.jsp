<%@page import="com.carel.supervisor.base.config.ProductInfoMgr"%>
<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLDiv"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean"
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="com.carel.supervisor.presentation.bo.BDataTransfer"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="java.util.List" import="java.util.ArrayList"
	import="java.util.Properties" 
	import="java.util.Collection"
	import="java.io.FileInputStream" 
	import="com.carel.supervisor.presentation.bo.helper.PortInfo"
	import="com.carel.supervisor.presentation.helper.ModbusSlaveCommander"
	import="java.io.File" %>
	
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	BDataTransfer bdt = (BDataTransfer) ut.getBoTrx();
	String language = sessionUser.getLanguage();
	LangService langsrv = LangMgr.getInstance().getLangService(language);
	int site = sessionUser.getIdSite();
	String comment = langsrv.getString("datatransfer", "comment");//"---commento";
	String fielddesc = langsrv.getString("datatransfer", "fielddesc");//"---fielddesc";
	String varavail = langsrv.getString("datatransfer", "variables");//"---varavail";
	String varsel = langsrv.getString("datatransfer", "variables");//"---varsel";
	String listbox = langsrv.getString("datatransfer", "variables");//"---listbox";
	//add by kevin
	String s_availablevars = langsrv.getString("reportconf","availablevars");
	String s_configuredvars = langsrv.getString("reportconf","configuredvars");
	// String variables = langsrv.getString("datatransfer", "variables");//"---variables";//
	// String device = langsrv.getString("datatransfer", "device");//"---device";//
	// String variable = langsrv.getString("datatransfer", "variable");//"---variable";//
	String nullsel = langsrv.getString("datatransfer", "nullselected");//"---nullsel";//
	String dupl = langsrv.getString("datatransfer", "dupl");//"---dupl";//
	String servererr = langsrv.getString("datatransfer", "servererr");//"---servererr";//
	String serverok = langsrv.getString("datatransfer", "serverok");//"---serverok";//
	String serveralert = langsrv.getString("datatransfer", "serveralert");//"---serveralert";//
	String maxdim = langsrv.getString("datatransfer","maxdimension");//"---maxdim";//
	String kb = langsrv.getString("datatransfer","kb");;//"---kb";//
	
	//add by glisten
	String strIdMbDev = ut.remProperty("idmbdev");
	int idmbdev = strIdMbDev.isEmpty() ? 0 : Integer.parseInt(strIdMbDev);
	
	// mbslave configuration
	MbSlaveConfBean beanMbSlave = new MbSlaveConfBean(sessionUser);
	if( idmbdev > 0 )
		beanMbSlave.loadDevice(idmbdev);
	
	// combo devices/models
	DeviceListBean devs = new DeviceListBean(site,language);
	DeviceBean tmp_dev = null;
	int[] ids = devs.getIds();
	StringBuffer div_dev = new StringBuffer();
	int device=0;
	for (int i=0;i<devs.size();i++){
		tmp_dev = devs.getDevice(ids[i]);
		div_dev.append("<option "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class= '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</option>\n");
	}
	
	DevMdlBeanList devmdllist = new DevMdlBeanList();
	DevMdlBean[] devmdl = devmdllist.retrieveDevMdl(site,language);
	StringBuffer combodev = new StringBuffer();
	StringBuffer typedev = new StringBuffer();
	combodev.append("<option value='-1'>-------------------</option>");
	String ss="";
	for (int i=0;i<devmdl.length;i++)
	{
		DevMdlBean tmp = devmdl[i];
		combodev.append("<option value="+tmp.getIddevmdl()+" "+ss+">"+tmp.getDescription()+"</option>\n");
	}	
	
	// device address
	StringBuffer address_options = new StringBuffer();
	Collection<Integer> addresses = beanMbSlave.getDevAddresses();	
	int address = beanMbSlave.getDeviceAddress();
	for(int i = 1; i <= 247; i++) {
		if( i == address || !addresses.contains(i) ) {
			address_options.append("<option ");
			address_options.append(i == address ? "selected" : "");
			address_options.append(" value=\"");
			address_options.append(i);
			address_options.append("\">");
			address_options.append(i);
			address_options.append("</option>\n");
		}
	}
	
	
	
	
	//int rows = 10;
	String idlistbox = "vars";
	
	DeviceListBean dlb = new DeviceListBean(site, language);
	//int ndev = dlb.size();
	int[] iddev = dlb.getIds();
	ArrayList a = new ArrayList();
	
	ArrayList b = new ArrayList();
				
	String CarelPath = BaseConfig.getCarelPath();
	String confpath = CarelPath + File.separator + "scheduler" + File.separator + "conf" + File.separator + "manager.properties";
	Properties p = new Properties();
	p.load(new FileInputStream(confpath));
	
	String write_enable = beanMbSlave.getWriteEnable() ? "checked" : "";
	String sitename = sessionUser.getSiteName();
	boolean displayPrintIcon = addresses.size()>0;
	
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<p class="StandardTxt"><%=comment%> 
	<input type="hidden" id="_devices" value="<%=langsrv.getString("datatransfer", "device")%>" />
	<input type="hidden" id="_variables" value="<%=langsrv.getString("datatransfer", "variable")%>" />
	<input type="hidden" id="delall" value="<%=langsrv.getString("setaction", "delall")%>" />
	<input type="hidden" id="operationend" value="<%=serverok%>" />
	<input type="hidden" name="deleteallparamquestion" id="deleteallparamquestion" value="<%=langsrv.getString("setaction", "confirmremoveall")%>" />
	
	<input type="hidden" id="nullselected" value="<%=nullsel%>" />
	<input type="hidden" id="doubleElement" value="<%=dupl%>" />
	<input type="hidden" id="servererr" value="<%=servererr%>" />
	<input type="hidden" id="serverok" value="<%=serverok%>" />
	<input type="hidden" id="serveralert" value="<%=serveralert%>" />
	<input type="hidden" id="alert_var_exists" name="alert_var_exists" value="<%=langsrv.getString("datatransfer", "alert_var_exists")%>">
</p>
<fieldset class="field">
	<legend class="standardTxt"><%=langsrv.getString("datatransfer", "var_list")%></legend>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
			<TR class='standardTxt'>
				<TD width="12%" align="left">
					<INPUT onclick='reload_actions(1);' type='radio' id='d' name='devmodl' checked="checked"/><%=langsrv.getString("datatransfer","devices")%></TD>
				<TD width="*" align="left">
					<INPUT onclick="enableModelSelection();" type='radio' id='m' name='devmodl'/><%=langsrv.getString("datatransfer","device_models")%>
					&nbsp;
					<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:275px;'>
					<%=combodev.toString()%>
					</SELECT>
				</TD>
				<TD align="center"></TD>
				<TD align="center"><input type="hidden" id="maxdimension" name="maxdimension" size="4" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" maxlength="10" value="100"></TD>
				<TD align="right">
					<img id="imgAddVar" name="imgAddVar" src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="addDevVar()">
				</TD>
			</TR>
		</TABLE>
		<table width="100%" cellpadding="0" cellspacing="2">
			<tr height="10px"></tr>
			<tr>
				<td  class='th' width="49%"><%=langsrv.getString("datatransfer","devices")%></td>
				<td  class='th' width="49%" ><%=langsrv.getString("datatransfer","variables")%></td>
				<th width='2%'>&nbsp;</th>
			</tr>
			<tr>
			<td width="49%" >
				<div id="div_dev">
					<select onclick=reload_actions(0); ondblclick="reload_actions(0);" id="dev" name='sections' size='10' class='selectB'>
					<%=div_dev.toString()%>
					</select>
				</div>
			 </td>
			<td width="49%">
				<div id="div_var">
					<select name=sections id='var' multiple size=10  class='selectB'>
					</select>
				</div>
			 </td>
			<td width="2%">&nbsp;</td>
		 </tr>
		</table>
</fieldset>
<br><br>
<div id="divDatatransferCurrConf">
			<fieldset class='field'>
				<legend class='standardTxt'><%=langsrv.getString("datatransfer", "curr_conf")%></legend>
				<div id="divdevvarTab">
				</div>
			</fieldset>	
		</div>

