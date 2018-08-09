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

// groups
VSGroup grp = new VSGroup(idsite, language);
grp.setScreenSize(sessionUser.getScreenWidth(), sessionUser.getScreenHeight());

// categories
VSCategory cat = new VSCategory();
Properties aPropCat[] = cat.getCategories();
StringBuffer strbufCategories = new StringBuffer();
StringBuffer strbufSymbols = new StringBuffer();
for(int i = 0; i < aPropCat.length; i++) {
	strbufCategories.append("<option value='");
	strbufCategories.append(aPropCat[i].getProperty(VSCategory.CATEGORY_ID));
	strbufCategories.append("'>");
	strbufCategories.append(aPropCat[i].getProperty(VSCategory.CATEGORY_NAME));
	strbufCategories.append("</option>\n");
	strbufSymbols.append("<input type='hidden' id='");
	strbufSymbols.append("cat" + aPropCat[i].getProperty(VSCategory.CATEGORY_ID));
	strbufSymbols.append("' name='");
	strbufSymbols.append("cat" + aPropCat[i].getProperty(VSCategory.CATEGORY_ID));
	strbufSymbols.append("' value='");
	strbufSymbols.append(aPropCat[i].getProperty(VSCategory.CATEGORY_SYMBOL));
	strbufSymbols.append("'>\n");
}

String strCmdVar = ProductInfoMgr.getInstance().getProductInfo().get("vscommandvar");
boolean bCmdVar = strCmdVar != null && (strCmdVar.equalsIgnoreCase("yes") || strCmdVar.equalsIgnoreCase("true"));
String strVarThreshold = ProductInfoMgr.getInstance().getProductInfo().get("vs_var_threshold");
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type="hidden" id="delgroup_conf" name="delgroup_conf" value="<%=lan.getString("vs", "delgroup_conf")%>">
<input type="hidden" id="group_empty" name="group_empty" value="<%=lan.getString("vs", "group_empty")%>">
<input type="hidden" id="reqfields" name="reqfields" value="<%=lan.getString("vs", "reqfields")%>">
<input type="hidden" id="checkdevs" name="checkdevs" value="<%=lan.getString("vs", "checkdevs")%>">
<input type="hidden" id="checkgroup" name="checkgroup" value="<%=lan.getString("vs", "checkgroup")%>">
<input type="hidden" id="type1" name="type1" value="<%=lan.getString("vs", "type1")%>">
<input type="hidden" id="type2" name="type2" value="<%=lan.getString("vs", "type2")%>">
<input type="hidden" id="type3" name="type3" value="<%=lan.getString("vs", "type3")%>">
<input type="hidden" id="type4" name="type4" value="<%=lan.getString("vs", "type4")%>">
<input type="hidden" id="vscommandvar" name="vscommandvar" value="<%=bCmdVar%>">
<input type="hidden" id="vs_var_threshold" name="vs_var_threshold" value="<%=strVarThreshold%>">
<input type="hidden" id="alert_var_threshold" name="alert_var_threshold" value="<%=lan.getString("vs", "alert_var_threshold")%>">
<%=strbufSymbols%>

<fieldset class='field'>		
<legend class='standardTxt'><%=lan.getString("vs","groups_cfg")%></legend>
<br>
<table class='standardTxt' width="100%">
<tr>
<td width="50%"><%=lan.getString("vs","vars_supported")%>&nbsp;<%=strVarThreshold%></td>
<td width="50%"><%=lan.getString("vs","vars_configured")%>&nbsp;<%=grp.getVariablesNo()%></td>
</tr>
</table>
<br>
<%=grp.getHTMLGroupTable()%>
</fieldset>

<form id="frm_set_group" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="id" name="id">
<input type="hidden" id="devs" name="devs">
<input type="hidden" id="cmd_var" name="cmd_var" value="0">
<input type="hidden" id="cmd_reverse_logic" name="cmd_reverse_logic" value="false">
<table width="100%">
  <tr>
    <td width="45%">
      <table>
         <tr valign="middle">
           <td width="40%" class="standardTxt"><%=lan.getString("vs", "group_name")%></td>
           <td width="60%" class="standardTxt">
             <input id="name" name="name" type="text" size="40" maxlength="100" class="<%=bOnScreenKey?"keyboardInput":"standardTxt"%>">
           </td>
         </tr>
       </table>
     </td>
    <td>&nbsp;</td>
     <td width="45%">
      <table>
        <tr valign="middle">
          <td width="30%" class="standardTxt"><%=lan.getString("vs", "category")%></td>
          <td align="right" width="60%" class="standardTxt">
            <select id="category" name="category" width="240" style="width:240px;" onChange="onCategoryChanged(this.value)">
              <option value=""></option>
              <%=strbufCategories%>              
            </select>
          </td>
          <td align="center" width="10%" class="standardTxt"><img id="imgSymbol" name="imgSymbol" src="images/scheduler/skin/icon_no_symbol.png"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form>

<fieldset class='field' style="height:<%=bCmdVar ? 35 : 50%>%">		
<legend class='standardTxt'><%=lan.getString("vs","group_settings")%></legend>
<table width="100%" height="100%">
 <tr>
  <td><%=grp.getHTMLGroupDevs()%></td>
 </tr>
</table>
</fieldset>

<%if( bCmdVar ) {%>
<p></p>
<fieldset class='field' style="height:35%">		
<legend class='standardTxt'><%=lan.getString("vs","command_settings")%></legend>
	<table width="100%">
	 <tr>
	  <td><%=grp.getHTMLDevTable()%></td>
	  <td><%=grp.getHTMLVarTable()%></td>
	  <td width="1%" align="right" valign="top"><img id="imgAddCmdVar" name="imgAddCmdVar" src="images/actions/addsmall_off.png" style="cursor:pointer;" onclick="addCmdVar();"></td>
	 </tr>
	</table>
	<table>
	 <tr class="standardTxt">
	 <td><%=lan.getString("vs","command_variable")%>&nbsp;
	 <input id="name_cmd_variable" id="name_cmd_variable" type="text" size="100" readonly>
	 </td>
	 <td><%=lan.getString("vs","reverse_logic")%>&nbsp;
	 <input id="cb_reverse_logic" id="cb_reverse_logic" type="checkbox" onClick="onClickRLogic(this.checked)" disabled>
	 </td>
	 <td width="1%" align="right" valign="top">
	 <img id="imgRemoveCmdVar" name="imgRemoveCmdVar" src="images/actions/removesmall_off.png" style="cursor:pointer;" onclick="removeCmdVar();">
	 </td>
	 </tr>
	</table>
</fieldset>
<%}%>
