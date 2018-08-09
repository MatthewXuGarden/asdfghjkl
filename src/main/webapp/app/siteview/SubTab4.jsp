<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" isErrorPage="true" 
import="com.carel.supervisor.presentation.session.* "
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.DeviceListBean"
import="com.carel.supervisor.presentation.bean.DeviceBean"
import="com.carel.supervisor.presentation.bean.GroupListBean"
import="com.carel.supervisor.presentation.bo.master.IMaster"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.bo.BSiteView"
import="com.carel.supervisor.dispatcher.DispatcherMgr"
import="java.util.Properties"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%
String path = request.getContextPath();
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String jsession = request.getSession().getId();
String language =  sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

//device tirati su in base alla profilatura (tiro su quelli dei gruppi visibili dall'utente)
StringBuffer combo_dev = new StringBuffer();
GroupListBean groups = sessionUser.getGroup();
int[] ids_group = groups.getIds();
DeviceListBean deviceList =  new DeviceListBean(idsite,language,ids_group);

DeviceBean tmp_dev = null;
int[] ids = deviceList.getIds();
combo_dev.append("<OPTION value='-1'>-------------------</OPTION>");

for (int i=0;i<deviceList.size();i++)
{
	tmp_dev = deviceList.getDevice(ids[i]);
	if (tmp_dev.islogic()==false)
	{
		combo_dev.append("<OPTION value='"+tmp_dev.getIddevice()+"'>"+tmp_dev.getDescription()+"</OPTION>");
	}
}
	
int scr_h = sessionUser.getScreenHeight(); 
String propagated = sessionUser.getCurrentUserTransaction().remProperty("propagated");

UserTransaction userTrx = sessionUser.getCurrentUserTransaction();
Properties prop = sessionUser.getProperties();//userTrx.getSystemParameter();
String importdevmdlcode = "";
String filename = "";
String languages = "";
String sameprofcode = "";
String importdeviceinfo = "";
String loadxmlerror = "";


if(prop != null)
{
	importdevmdlcode = prop.getProperty("devmdlcode");
	filename = prop.getProperty("filename");
	languages = prop.getProperty("langs");
	sameprofcode = prop.getProperty("sameprofcode");
	loadxmlerror = prop.getProperty("loadxmlerror");
	prop.remove("devmdlcode");
	prop.remove("filename");
	prop.remove("langs");
	prop.remove("sameprofcode");
	prop.remove("loadxmlerror");
}
if(importdevmdlcode == null)
{importdevmdlcode = "";}
if(languages == null)
{languages = "";}
if(sameprofcode == null)
{sameprofcode = "";}
if(loadxmlerror == null)
{
	loadxmlerror = "";
}
if(filename == null)
{
	filename = "";
}
//userTrx.setSystemParameter(new Properties());
if(importdevmdlcode != null && !importdevmdlcode.equals("") &&
	filename != null && !filename.equals("") && loadxmlerror.equals(""))
{
	importdeviceinfo = lang.getString("propagate","confof")+"&nbsp;&nbsp;<B>"+importdevmdlcode+"</B>&nbsp;&nbsp;"+
		lang.getString("propagate","impfrom")+"&nbsp;&nbsp;<B>"+filename+"</B>";
}
String langexist = "false";
if(languages != null &&!languages.equals(""))
{
	String curr_lan = sessionUser.getLanguage();
	if(!"EN_en".equalsIgnoreCase(curr_lan))
	{
		String[] langs = languages.split(";");
		for(int i=0;i<langs.length;i++)
		{
			if(curr_lan.equalsIgnoreCase(langs[i]))
			{
				langexist = "true";
				break;
			}
		}
	}
	else
	{
		langexist = "true";
	}
	
	prop.remove("langs");
}
else
{
	languages = "";
}
BSiteView siteBo = (BSiteView)userTrx.getBoTrx();


String titleProf = lang.getString("propagate","cpgraphtitle");
String source = lang.getString("propagate","cpgraphsource");
String target = lang.getString("propagate","cpgraphtarget");
String maxselectstr = lang.getString("devdetail","maxselectstr");


int permission = sessionUser.isButtonActive("siteview", "tab5name","Save")?2:1;

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
FileDialogBean fileDlg = new FileDialogBean(request);
boolean isProtected = userTrx.isTabProtected();
%>
<input type='hidden' id='propagated' value="<%=propagated%>">
<input type='hidden' id='loadxmlerror' value="<%=loadxmlerror%>">
<input type='hidden' id='notexported' value='<%=lang.getString("dtlview","notexported") %>'>
<input type='hidden' id='selectmaster' value='<%=lang.getString("propagate","selectmaster")%>'>
<input type='hidden' id='selectslave' value='<%=lang.getString("propagate","selectslave")%>'>
<input type='hidden' id='importdevmdlcode' value='<%=importdevmdlcode %>'>
<input type='hidden' id='langexist' value='<%=langexist %>'>
<input type='hidden' id='sameprofcode' value='<%=sameprofcode %>'>
<input type='hidden' id='adminprofileupdate' value="<%=lang.getString("propagate","adminprofileupdate") %>">
<input type='hidden' id='confirmsameprofilecodeupdate' value='<%=lang.getString("propagate","confirmsameprofilecodeupdate") %>'>
<input type='hidden' id='langnotexist' value='<%=lang.getString("propagate","langnotexist") %>'>
<input type='hidden' id='selectpropag' value='<%=lang.getString("propagate","selectpropag")%>'>
<input type='hidden' id='noslaves' value='<%=lang.getString("propagate","noslaves")%>'>
<input type='hidden' id='scr_h' value='<%=scr_h%>'>
<input type="hidden" id="maxselectstr" name="maxselectstr" value="<%=maxselectstr%>">
<input type="hidden" id="permission" name="permission" value="<%=permission%>"/>
<input type="hidden" id="isprotected" value="<%=isProtected %>"/>
<input type='hidden' id='save_confirm' value='<%=lang.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lang.getString("fdexport","exporterror") %>" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<%= fileDlg.renderFileDialog() %>

<div style="border:0;margin:0;padding:1px;width:98%">
	<div style="margin:3px">
		<p class='standardTxt'><%=lang.getString("propagate","comment")%></p>
	</div>
	<div style="width:100%;padding:1px">
		<fieldset>
			<legend class='standardTxt'><%=lang.getString("propagate","sourcesel")%></legend>
			<table cellpadding="0" cellspacing="0" border="0" width='100%'>
				<tr>
					<td width="3%"><input type="radio" id="combsource" name="combosource" checked onclick="combsourceclick();"></td>
					<td class='standardTxt' width='15%'><%=lang.getString("propagate","master")%></td>
					<td class='standardTxt' width='30%'>
						<select onchange='reload_son();' class='standardTxt' id='id_master2' name='id_master2' style='width:100%;'>
							<%=combo_dev.toString()%>
						</select>
					</td>
					<td width="5%">&nbsp;</td>
					<td width="47%" class="standardTxt">
					</td>
				</tr>
			</table>
			<form style="border:0;padding:0;margin:0" name="deviceuploadform" id="deviceuploadform" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="deviceconfig" />
				<input type='hidden' name='alr2' id='alr_2' value=''/>
				<input type='hidden' name='um2' id='um_2' value=''/>
				<input type='hidden' name='haccp2' id='haccp_2' value=''/>
				<input type='hidden' name='hist2' id='hist_2' value=''/>
				<input type='hidden' name='prior2' id='prior_2' value=''/>
				<input type='hidden' name='graphconf2' id='graphconf_2' value=''/>
				<input type='hidden' name='descr2' id='descr_2' value=''/>			
				<table width='100%' cellpadding="0" cellspacing="0" border="0">
					<tr>	
						<td width="3%"><input type="radio" id="filesource" name="filesource" onclick="filesourceclick();"></td>
						<td class='standardTxt' width='15%'><%=lang.getString("propagate","importconf") %>
						</td>
						<td class='standardTxt' valign='middle' width="30%">
							<%= fileDlg.inputLoadFile("importconf", "", "size='30%' onchange=\"enabImport('importconf','importbutton','dcfg');\"", true)%>
						</td>
						<td width="5%" valign='middle' >
							<img title="<%=lang.getString("propagate","importdevconf")%>" id='importbutton' style="margin-left:20px" src="images/actions/import_off.png" onclick="importdevice('importconf','importbutton');" />
						</td>
						<td width="47%" >
							<%=importdeviceinfo %>
						</td>
					</tr>
				</table>
			</form>				
		</fieldset>		
	</div>
	<div style="width:100%;padding:1px">
			<form id="frm_propagate" action="servlet/master;jsessionid=<%=jsession%>" method="post">
				<input type='hidden' id='exp_file' name='exp_file' value=''/>
				<!-- imp_file  -->
				<input type='hidden' id='updatesameprofcode' name='updatesameprofcode' value='false'/>
				<input type='hidden' id='id_master' name='id_master' value='-1'/>
				<input type='hidden' id='cmd' name='cmd' value='propagate'>
				<p/>
				<fieldset>
					<legend class='standardTxt'><%=lang.getString("propagate","crossdevconfcopy") %></legend>
					<table border=0 width='100%'>
						<tr>
							<td valign='top'>
								<table  class='standardTxt' id='funct' height='100%'>
									<tr height="20px">
										<td><input type='checkbox' id='alr' name='alr'></td>
										<td><%=lang.getString("propagate","alarms")%></td>
										</tr>
									<tr height="20px">
										<td><input type='checkbox' id='descr' name='descr' onclick="checklanguage(this);"></td>
										<td><%=lang.getString("propagate","descr")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='um' name='um'></td>
										<td><%=lang.getString("propagate","um")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='haccp' name='haccp'></td>
										<td><%=lang.getString("propagate","haccp")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='hist' name='hist'></td>
										<td><%=lang.getString("propagate","hist")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='prior' name='prior'></td>
										<td><%=lang.getString("propagate","prior")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='graphconf' name='graphconf'></td>
										<td><%=lang.getString("propagate","graphconf")%></td>
									</tr>
									<tr height="20px">
										<td><input type='checkbox' id='images' name='images'></td>
										<td><%=lang.getString("propagate","images")%></td>
									</tr>
								</table>			
							</td>
							<td width="3%">&nbsp;</td>
							<td valign="top" align="center"  width="60%">
								<table border='0' cellpadding="0" cellspacing="0" width="99%">
									<tr>
										<td>
											<table border='0' id='h_table' class='table' cellpadding="0" cellspacing="1" width="100%">
												<tr class='th'>
													<td width="80%" align="center"><b><%=lang.getString("propagate","slave")%></b></td>
													<td width="10%" align="center"><b><%=lang.getString("propagate","line")%></b></td>
													<td width="10%" align="center"><input disabled id='s_all' type='checkbox' onclick='selectAll(this);' /></td>
												</tr>
											</table>
										</td>
										<td width='17px'>
											<table border='0' cellspacing="0" cellpadding="0" width='100%'><tr><td>&nbsp;</td></tr></table>
										</td>
									</tr>
								</table>
							<div id='div_devices' style="width:99%;height:120pt; overflow:auto;background-color:cacaca;"></div>
							</td>
						</tr>
					</table>
					</fieldset>
		<input type='hidden' id='ids_slaves' name='ids_slaves'/>
		<input type='hidden' id='ids_profiles' name='ids_profiles'/>
			<p/>
			<fieldset>
				<legend class='standardTxt'><%=titleProf %></legend>
				<table border="0" cellpadding="1" cellspacing="1" width="100%" class='standardTxt'>
					<tr>
						<td width="" valign="middle">
								<nobr><%=source %> <%=siteBo.getProfiles() %></nobr>
						</td>
						<td width="3%">&nbsp;</td>
						<td width="60%">
								<table border='0' cellpadding="0" cellspacing="0" width="99%">
									<tr>
										<td>
											<table border='0' id='h_table' class='table' cellpadding="0" cellspacing="1" width="100%">
												<tr class='th'>
													<td width="80%" align="center"><b><%= target %></b></td>
													<td width="10%" align="center">
														<input disabled id='p_all' type='checkbox' onclick='selectAllP(this);' />
													</td>
												</tr>
											</table>
										</td>
										<td width='17px'>
											<table border='0' cellspacing="0" cellpadding="0" width='100%'><tr><td>&nbsp;</td></tr></table>
										</td>
									</tr>
								</table>
							<div id='div_profiles' style="width:99%;height:60pt;overflow:auto;background-color:cacaca;"></div>
						</td>
						<td width="*"><br/></td>
					</tr>
				</table>
			</fieldset>		
	</form>
	</div>
</div>