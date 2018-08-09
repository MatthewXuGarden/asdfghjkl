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

%>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceListBean" %>
<%@ page import="com.carel.supervisor.presentation.bo.helper.VarDetailHelper" %>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	//sezione multilingua
	String description = lan.getString("devdetail","measure");
	String descdefault = lan.getString("devdetail","description");
	String descmisure = lan.getString("devdetail","descrmeasure");
	
  String devconfcomment4 = lan.getString("devdetail","devconfcomment4");
  String missingdefault = lan.getString("devdetail","missingdefault");
  String missingfield = lan.getString("devdetail","missingfield");
  String measurejustpres = lan.getString("devdetail","measurejustpres");
  String confermmeasure = lan.getString("devdetail","confermmeasure");
  String modmeasure = lan.getString("devdetail","modmeasure");
  String measurein = lan.getString("devdetail","measurein");
  String delmeasure = lan.getString("devdetail","delmeasure");
  String actionsaveOK = lan.getString("devdetail","actionsaveOK");
  String actionsaveKO = lan.getString("devdetail","actionsaveKO");
  
	// Alessandro : aggiunto supporto alla Virtual Keyboard
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();  
  
    // id device
	int iddev = Integer.parseInt(sessionUser.getProperty("iddev"));
	
  DeviceBean device= DeviceListBean.retrieveSingleDeviceById(idsite,iddev, language);
	String descr= device.getDescription();
	
	String s_idvar = sessionUser.getPropertyAndRemove("idvar");
	int idvar= -1;
	if (s_idvar!=null)
		idvar = Integer.parseInt(s_idvar);
	
	
	
	
	//StringBuffer table = new StringBuffer();
	
	//Combo unità di misura
	StringBuffer unitMeasurementList=new StringBuffer();
  UnitOfMeasurementBeanList unitOfMeasurementBeanList= new UnitOfMeasurementBeanList();
  unitOfMeasurementBeanList.loadAllUnitOfMeasurement();
  UnitOfMeasurementBean unitOfMeasurementBean=null;

  for(int i=0;i<unitOfMeasurementBeanList.size();i++){
    unitOfMeasurementBean=unitOfMeasurementBeanList.getUnitOfMeasurement(i);
    unitMeasurementList.append("<option value='");
    unitMeasurementList.append(unitOfMeasurementBean.getIdUnitOfMeasurement());
    unitMeasurementList.append("'>");
    unitMeasurementList.append(unitOfMeasurementBean.getDescription());
    unitMeasurementList.append("</option>");
  
  }//for
	
	String insertmeasureok = lan.getString("devdetail","insertmeasureok");
	String insertmeasurefailed = lan.getString("devdetail","insertmeasurefailed");
	String deletemeasureok = lan.getString("devdetail","deletemeasureok");
	String deletemeasurefailed = lan.getString("devdetail","deletemeasurefailed");
	
	//Se inserisco una nuova unità di misura
	//String action = sessionUser.getProperty("combo_insert");
	//if(action != null && action.equalsIgnoreCase("ok"))
//		comment = lan.getString("devdetail","insertmeasureok");
//	if(action != null && action.equalsIgnoreCase("failed"))
//		comment = lan.getString("devdetail","insertmeasurefailed");
	
	//Se tolgo un'unità di misura
	//action = sessionUser.getProperty("combo_delete");
	//if(action != null && action.equalsIgnoreCase("ok"))
	//	comment = lan.getString("devdetail","deletemeasureok");
	//if(action != null && action.equalsIgnoreCase("failed"))
	//	comment = lan.getString("devdetail","deletemeasurefailed");
	
	//Se modifico un'unità di misura
//	action = sessionUser.getProperty("combo_update");
//	if(action != null && action.equalsIgnoreCase("ok"))
//		comment = lan.getString("devdetail","updatemeasureok");
//	if(action != null && action.equalsIgnoreCase("failed"))
//		comment = lan.getString("devdetail","updatemeasurefailed");
	
//	sessionUser.removeProperty("combo_insert");
//	sessionUser.removeProperty("combo_delete");
//	sessionUser.removeProperty("combo_update");
	
	String actionsave = sessionUser.getPropertyAndRemove("actionsave");
 	if(actionsave == null)
 		actionsave="noactionsave";
 
	String table = VarDetailHelper.getHTMLDescriptionsTable(idsite,iddev,language,sessionUser.getScreenWidth(),sessionUser.getScreenHeight());
	
%>
<form id="desc_var_save" name="desc_var_save" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="curdevid" name="curdevid" value="<%=iddev%>"/>
<input type="hidden" id="missingdefault" name="missingdefault" value="<%=missingdefault%>"/>
<input type="hidden" id="idvar" name="idvar" value="<%=idvar%>"/>

<input type="hidden" id="language" name="language" value="<%=language%>"/>
<input type="hidden" id="missingfield" value="<%=missingfield%>"/>
<input type="hidden" id="measurejustpres" value="<%=measurejustpres%>"/>
<input type="hidden" id="confermmeasure" value="<%=confermmeasure%>"/>
<input type="hidden" id="modmeasure" value="<%=modmeasure%>"/>
<input type="hidden" id="measurein" value="<%=measurein%>"/>
<input type="hidden" id="delmeasure" value="<%=delmeasure%>"/>
<input type="hidden" id="actionsave" value="<%=actionsave%>"/>
<input type="hidden" id="actionsaveOK" value="<%=actionsaveOK%>"/>
<input type="hidden" id="actionsaveKO" value="<%=actionsaveKO%>"/>
<input type="hidden" id="insertmeasureok" value="<%=insertmeasureok%>"/>
<input type="hidden" id="insertmeasurefailed" value="<%=insertmeasurefailed%>"/>
<input type="hidden" id="deletemeasureok" value="<%=deletemeasureok%>"/>
<input type="hidden" id="deletemeasurefailed" value="<%=deletemeasurefailed%>"/>
<input type="hidden" id="cmd" name="cmd" />
<input type="hidden" id="idvar" name="idvar" value="<%=idvar%>" />
<input type="hidden" id="vk_state" value="<%= (OnScreenKey) ? '1' : '0' %>" />
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<p class="tdTitleTable"><%=descr%></p>
<p class="standardTxt"><%=devconfcomment4%></p>
<%=table%>
<br/>
<fieldset class='field'>
	<legend class="standardTxt"><%=description%></legend>
	<table class="standardTxt" border="0">
		<tr>
			<td class="standardTxt"><%=descdefault%></td><td><input class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" size="25" maxlength="20" id="unit_misura" name="unit_misura" value="" onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);abilita_save_image();'/>&nbsp;*</td>
			<td><img src="images/actions/save_on_black.png" id="save_img" onclick="save_update_measure();" style="cursor:pointer" /></td>
			<td width="50px">&nbsp;</td>
			<td class="standardTxt"><%=descmisure%></td>
			<td><select class='standardTxt' name="combo_misure" onchange="setValue(this);"><%=unitMeasurementList.toString()%></select></td>
			<td>
				<div id="imageOn" style="display:none">
					<img src="images/actions/remove_on_black.png" id="remove_img" onclick="remove_measure();" style="cursor:pointer;" />
				</div>
				<div id="imageOff" style="display:inline">
					<img src="images/actions/remove_off.png" id="remove_img" />
				</div>
			</td>
			<td width="5%">&nbsp;</td>			
		</tr>
	</table>
</fieldset>
</form>