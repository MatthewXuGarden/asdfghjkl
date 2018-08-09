<%@ page language="java" 

import="java.util.Properties"

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.ac.AcMdl"
import="com.carel.supervisor.director.ac.AcProperties"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();

int idsite = sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

int idmstrdevmdl = -1;

Properties properties = transaction.getSystemParameter();
if(properties != null)
{		
	if(properties.get("idmstrdevmdl") != null)
	{
		//idmstrdevmdl = ((Integer)properties.get("idmstrdevmdl")).intValue();
		idmstrdevmdl = Integer.parseInt((String)properties.get("idmstrdevmdl"));
	}
}
else
{
	properties = new Properties();
}

String combo_dev = AcMdl.getComboDevice(idsite, language, idmstrdevmdl);

transaction.setSystemParameter(null);

String combo_cus = AcMdl.getComboCus(idsite, language);

int nmaxvars = (new AcProperties()).getProp("ac_maxvariable");
String cusStatus = (nmaxvars>3?"enab":"disab");
boolean cusEnab = (nmaxvars>3?true:false);

double acbasetime = (new AcProperties()).getProp("ac_clock"); //recupero tempo di ctrl del modulo (in sec.)
acbasetime = acbasetime * 1.2; //tempo min settabile = 120% del tempo di ctrl del modulo
acbasetime = acbasetime / 60; //visualizzo il tempo in minuti

String identifier = lan.getString("ac","identifier");
String var_orig = lan.getString("ac","var_orig");
String var_dest = lan.getString("ac","var_dest");
String var_extra = lan.getString("ac","var_extra");
String var_alrm = lan.getString("ac","var_alrm");
String lista_dev = lan.getString("ac","lista_dev");
String str_min = lan.getString("ac","min");
String str_max = lan.getString("ac","max");
String str_def = lan.getString("ac","def");
String dest_dig_var = lan.getString("ac","dest_dig_var");
String dest_time = lan.getString("ac","dest_time");
String hb_no_ok = lan.getString("ac","heartbit_no_ok");
String mintime_no_ok = lan.getString("ac","mintime_no_ok");
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<input type="hidden" id="acbasetime" value="<%=acbasetime%>" />

<input type="hidden" id="mintime_no_ok" value="<%=mintime_no_ok%>" />
<input type="hidden" id="heartbit_no_ok" value="<%=hb_no_ok%>" />
<input type="hidden" id="badvalues" value="<%=lan.getString("ac","badvalues")%>" />
<input type="hidden" id="delrow" value="<%=lan.getString("ac","delrow")%>" />
<input type="hidden" id="ok_save_tab4" value="<%=lan.getString("ac","ok_save_tab4")%>" />
<input type="hidden" id="salva" value="<%=lan.getString("ac","salva")%>" />
<input type="hidden" id="wantChangeDev" value="<%=lan.getString("ac","wantChangeDev")%>" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<input type="hidden" id="changeDev" name="changeDev" value="false" />
<div class='standardTxt'><%=lan.getString("ac","comment4")%></div>	

<FORM name="ac_frm" id="ac_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
<input type='hidden' id='cmd' name='cmd'/>
<input type='hidden' id='modMaster' name='modMaster' value='false'/>
<input type='hidden' id='modSlave' name='modSlave' value='false'/>

<table width='98%'>
	<TR>
		<TD class='standardTxt' align='left'><%=lista_dev%>:&nbsp;&nbsp;<%=combo_dev%></TD>
	</TR>
	<TR style='height:10px'><TD></TD></TR>
</table>

<table width="98%">
	<TR>
		<TD class='standardTxt'>
			<table id='t_master' class='table' cellspacing='1' cellpadding='1' width='100%'>
				<tr height='21px' class='th'>
					<td width='6%' align='center'><b><%=identifier%></b></td>
					<td width='35%' align="center"><b><%=var_orig%></b></td>
					<td width='35%' align="center"><b><%=var_alrm%></b></td>
					<td width='5%' align="center"><b><%=str_min%></b></td>
					<td width='5%' align="center"><b><%=str_max%></b></td>
					<td width='5%' align="center"><b><%=str_def%></b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_origset(1);select_origrow(1);' ondblclick='mod_origline(1);'><td class='standardTxt'><b>Tamb</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_origset(2);select_origrow(2);' ondblclick='mod_origline(2);'><td class='standardTxt'><b>rH%</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_origset(3);select_origrow(3);' ondblclick='mod_origline(3);'><td class='standardTxt'><b>Tdew</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<% if (cusEnab) { %>
				<tr height='21px' id='orig_Cus1' style='cursor:pointer' class='Row1' onclick='hide_origset(4);select_origrow(4);' ondblclick='mod_origline(4);'><td class='standardTxt'><b>Extra1</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' id='orig_Cus2' style='cursor:pointer' class='Row1' onclick='hide_origset(5);select_origrow(5);' ondblclick='mod_origline(5);'><td class='standardTxt'><b>Extra2</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' id='orig_Cus3' style='cursor:pointer' class='Row1' onclick='hide_origset(6);select_origrow(6);' ondblclick='mod_origline(6);'><td class='standardTxt'><b>Extra3</b></td><td></td><td></td><td></td><td></td><td></td><td align="center"></td><td align="center"></td></tr>
				<% } %>
			</table>
		</TD>
	</TR>
	<TR style='height:10px'><TD></TD></TR>
	<TR>
		<TD class='standardTxt' >
			<table id='t_extra' class='table' cellspacing='1' cellpadding='1' width='100%'>
				<tr height='21px' class='th'>
					<td width='2%' align='center'><b><%=identifier%></b></td>
					<td width='30%' align="center"><b><%=var_extra%></b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_extset(1);select_extrow(1);' ondblclick='mod_extline(1);'><td class='standardTxt'><b>Tglass</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_extset(2);select_extrow(2);' ondblclick='mod_extline(2);'><td class='standardTxt'><b>Output</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
			</table>
		</TD>
	</TR>
	<TR style='height:10px'><TD></TD></TR>
	<TR>
		<TD class='standardTxt' >
			<table id='t_slave' class='table' cellspacing='1' cellpadding='1' width='100%'>
				<tr height='21px' class='th'>
					<td width='2%' align="center"><b><%=identifier%></b></td>
					<td width='30%' align="center"><b><%=var_dest%></b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
					<td width='1%' align="center"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(1);select_destrow(1);' ondblclick='mod_destline(1);'><td class='standardTxt'><b>Tamb</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(2);select_destrow(2);' ondblclick='mod_destline(2);'><td class='standardTxt'><b>rH%</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(3);select_destrow(3);' ondblclick='mod_destline(3);'><td class='standardTxt'><b>Tdew</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<% if (cusEnab) { %>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(4);select_destrow(4);' ondblclick='mod_destline(4);'><td class='standardTxt'><b>Extra1</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(5);select_destrow(5);' ondblclick='mod_destline(5);'><td class='standardTxt'><b>Extra2</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<tr height='21px' style='cursor:pointer' class='Row1' onclick='hide_destset(6);select_destrow(6);' ondblclick='mod_destline(6);'><td class='standardTxt'><b>Extra3</b></td><td cellpadding=2></td><td align="center"></td><td align="center"></td></tr>
				<% } %>
			</table>
		</TD>
	</TR>
	<TR style='height:10px'><TD></TD></TR>
    <TR style='height:10px'><TD class='standardTxt'><%=lan.getString("ac","commentHB")%></TD></TR>
	<TR style='height:5px'><TD></TD></TR>
	<TR>
		<TD class='standardTxt' >
			<table id="t_safety" class="table" cellspacing="1" cellpadding="1" width="100%">
				<tr height='21px' class='th'>
					<td width='5%' align="center"><b><%=identifier%></b></td>
					<td width='40%' align="center"><b><%=dest_dig_var%></b></td>
					<td width='40%' align="center"><b><%=dest_time%></b></td>
					<td width='5%' align="center"><b><%=str_def%></b></td>
				</tr>
				<tr height="21px" style="cursor:pointer" class="Row1">
					<td width='5%' class="standardTxt"><b>Heart bit</b></td>
					<td width='40%' align="left" class="standardTxt"></td>
					<td width='40%' align="left" class="standardTxt"></td>
					<td width='5%' align="center" class="standardTxt"></td>
				</tr>
			</table>
		</TD>
	</TR>
</table>
<div style='height:10px'>&nbsp;</div>
<table width='100%'>
	<TR>
		<TD class='standardTxt' align='left'>
			<fieldset class='field'>
				<LEGEND  class="standardTxt">&nbsp;<span><%=lan.getString("ac","enabcus")%></span>&nbsp;</LEGEND>
				&nbsp; <%=lan.getString("ac","commentEXTRA")%>: &nbsp; &nbsp; <%=combo_cus%>
			</fieldset>
		</TD>
	</TR>
</table>

</FORM>
