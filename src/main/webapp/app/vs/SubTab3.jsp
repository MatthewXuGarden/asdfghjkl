<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
import="com.carel.supervisor.presentation.vscheduler.*"
import="com.carel.supervisor.base.config.ProductInfoMgr"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();

int idsite= sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

String jsession = request.getSession().getId();

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

// categories
VSCategory cat = new VSCategory(idsite, language);
cat.setScreenSize(sessionUser.getScreenWidth(), sessionUser.getScreenHeight());

// symbols
StringBuffer strbufSymbols = new StringBuffer();
String astrFiles[] = FileDialogBean.getFiles(BaseConfig.getAppHome() + VSBase.SYMBOL_PATH, ".png");
if( astrFiles != null ) {
	for(int i = 0; i < astrFiles.length; i++) {
		strbufSymbols.append("<option value='");
		strbufSymbols.append(astrFiles[i]);
		strbufSymbols.append("'>");
		String strName = lan.getString("vs", astrFiles[i]);
		strbufSymbols.append(strName.length() == 0 ? astrFiles[i] : strName); 
		strbufSymbols.append("</option>\n");
	}
}

// alert
String strAlert = sessionUser.getProperty("vs_alert");
if( strAlert != null )
	sessionUser.removeProperty("vs_alert");
else
	strAlert = "";

String strVarThreshold = ProductInfoMgr.getInstance().getProductInfo().get("vs_var_threshold");
String strCatNo = ProductInfoMgr.getInstance().getProductInfo().get("vscatno");
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type="hidden" id="delcat_conf" name="delcat_conf" value="<%=lan.getString("vs", "delcat_conf")%>">
<input type="hidden" id="reqfields" name="reqfields" value="<%=lan.getString("vs", "reqfields")%>">
<input type="hidden" id="map_empty" name="map_empty" value="<%=lan.getString("vs", "map_empty")%>">
<input type="hidden" id="checkcat" name="checkcat" value="<%=lan.getString("vs", "checkcat")%>">
<input type="hidden" id="checkmap" name="checkmap" value="<%=lan.getString("vs", "checkmap")%>">
<input type="hidden" id="type1" name="type1" value="<%=lan.getString("vs", "type1")%>">
<input type="hidden" id="type2" name="type2" value="<%=lan.getString("vs", "type2")%>">
<input type="hidden" id="type3" name="type3" value="<%=lan.getString("vs", "type3")%>">
<input type="hidden" id="type4" name="type4" value="<%=lan.getString("vs", "type4")%>">
<input type="hidden" id="alert" name="alert" value="<%=strAlert%>">
<input type="hidden" id="warning" name="warning" value="<%=lan.getString("vs", "import_warning")%>">
<input type="hidden" id="remoteFolder" name="remoteFolder" value="<%=BaseConfig.getCarelPath() + "PvPro\\TempExports\\"%>">
<input type="hidden" id="vs_var_threshold" name="vs_var_threshold" value="<%=strVarThreshold%>">
<input type="hidden" id="alert_var_threshold" name="alert_var_threshold" value="<%=lan.getString("vs", "alert_var_threshold")%>">
<input type="hidden" id="vs_cat_no" name="vs_cat_no" value="<%=strCatNo%>">

<%=(new FileDialogBean(request)).renderFileDialog()%>

<%=cat.getHTMLCategoryTable()%>
<form id="frm_set_category" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="id" name="id">
<input type="hidden" id="map" name="map">
<input type="hidden" id="impexp" name="impexp">

<br>

<table width="100%">
  <tr>
    <td width="45%">
      <table>
         <tr valign="middle">
           <td width="40%" class="standardTxt"><%=lan.getString("vs", "cat_name")%></td>
           <td width="60%" class="standardTxt">
             <input id="name" name="name" type="text" size="40" maxlength="32" class="<%=bOnScreenKey?"keyboardInput":"standardTxt"%>">
           </td>
         </tr>
       </table>
     </td>
    <td>&nbsp;</td>
     <td width="45%">
      <table>
        <tr valign="middle">
          <td style="width:30%" class="standardTxt"><%=lan.getString("vs", "symbol")%></td>
          <td align="right" width="60%" class="standardTxt">
            <select id="symbol" name="symbol" style="width:240px;" onChange="onSymbolChanged(this.value)">
              <option value=""></option>
              <%=strbufSymbols%>              
            </select>
          </td>
          <td align="center" width="10%" class="standardTxt"><img id="imgSymbol" name="imgSymbol" src="images/scheduler/skin/icon_no_symbol.png"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form>

<div id="uploadwin" class="uploadWin">
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('uploadwin').style.display='none';">X</div>
		<%=lan.getString("vs", "import_caption")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div>
		  <form id="frm_upload_categories" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='multipart/form-data'>
            <input type="hidden" name="tipofile" value="VSCatImp" />
			<div id="uploadwinotherpath">
				<div id="oplabel" style="width:100px;"><span id="otherasDIV"><%=lan.getString("filedialog", "filename")%></span></div>
				<div id="oploadbox"><span id="other_span_load"><input type="file" class="mybutton" id="upload_file" name="upload_file" size='30%'></span></div>
				<div id="opsavebox"><span id="other_span_save"></span></div>		
			</div>
			<div id="uploadwinbuttons">
				<table border="0">
					<tr>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="submit_file();"><%=lan.getString("filedialog", "ok")%></td>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('uploadwin').style.display='none'"><%=lan.getString("filedialog", "cancel")%></td>
					</tr>
				</table>
			</div>
		  </form>
		</div>	
	</div>
</div>


<fieldset class='field' style="height:210px;width:97%">		
	<legend class='standardTxt'><%=lan.getString("vs","settings")%></legend>
	<table width="100%">
	 <tr>
	  <td><%=cat.getHTMLDevMdlTable()%></td>
	  <td><%=cat.getHTMLVarMdlTable()%></td>
	  <td width="1%" align="right" valign="top"><img id="imgAddDevVarMdl" name="imgAddDevVarMdl" src="images/actions/addsmall_off.png" style="cursor:pointer;" onclick="addDevVarMdl();"></td>
	 </tr>
	</table>
</fieldset>

<p></p>

<fieldset class='field' style="height:210px;width:97%">		
<legend class='standardTxt'><%=lan.getString("vs","cat_map")%></legend>
<table width="100%">
 <tr>
  <td width="99%"><%=cat.getHTMLCatMapTable()%></td>
  <td width="1%" align="right" valign="top"><img id="imgRemoveDevVarMdl" name="imgRemoveDevVarMdl" src="images/actions/removesmall_off.png" style="cursor:pointer;" onclick="removeDevVarMdl();"></td>
 </tr>
</table>
</fieldset>
