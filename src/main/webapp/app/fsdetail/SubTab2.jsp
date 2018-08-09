<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.fs.*"
import="com.carel.supervisor.plugin.fs.*"
import="com.carel.supervisor.controller.ControllerMgr"

%>
<%@ page import="com.carel.supervisor.presentation.fs.FSRackBean" %>
<%@ page import="java.util.Map" %>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	String jsession = request.getSession().getId();
	// Alessandro : added virtual keyboard support
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	// recupero iddevice dalla sessione utente:
	int idCurrRack = Integer.parseInt(sessionUser.getProperty("idrack"));

	String combo_racks = FSRackBean.getComboRacks(idCurrRack, language);
	
	FSRack rack = FSRackBean.getActualRackFromDB(idCurrRack,language);

	boolean old = "old".equalsIgnoreCase(rack.getAux())?true:false;
	
	String setpt = ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getFormattedValue();	
	String setpt_um = ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getInfo().getMeasureunit();
	String maxset = "";
	String minset = "";
	String gradient = "";
	
	String maxset_um = "";
	String minset_um = "";
	String gradient_um = "";
	
	String timewin = "";
	String waittime = "";
	String maxofftime = "";
	String maxoffutil = "";
	
	if (old)
	{
		maxset = rack.getId_maxset().toString();
		minset = rack.getId_minset().toString();
		gradient = rack.getId_gradient().toString();
		maxset_um = "bar/°C";
		minset_um = "bar/°C";
		gradient_um = "bar/°C";
	}
	else
	{
		maxset = ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getFormattedValue();
		minset = ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getFormattedValue();
		gradient = ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getFormattedValue();
		maxset_um = ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getInfo().getMeasureunit();
		minset_um = ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getInfo().getMeasureunit();
		gradient_um = ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getInfo().getMeasureunit();
	}
	
	timewin = ""+(rack.getTimewindow()/60);
	waittime = ""+(rack.getWaittime()/60);
	maxofftime = rack.getMaxofftime().toString();
	maxoffutil = rack.getMaxoffutil().toString();
	
	int height = sessionUser.getScreenHeight();
	int width = sessionUser.getScreenWidth();
	String util_table = FSConfig.getConfigUtilsTable(sessionUser,rack, language, height, width);
	
	String err = lan.getString("fsdetail","errlimit");
	err = err.replace("$1","0");
	err = err.replace("$2","100");
	String err1 = lan.getString("fsdetail","errlimit1");
	err1 = err1.replace("$1","10");
	err1 = err1.replace("$2","180");
	String err2 = lan.getString("fsdetail","errlimit2");
	err2 = err2.replace("$1","10");
	err2 = err2.replace("$2","180");
	String err3 = lan.getString("fsdetail","errlimit3");
	err3 = err3.replace("$1","0");
	err3 = err3.replace("$2","100");
	// new algorithm
	String err4 = lan.getString("fsdetail","errlimit4");
	err4 = err4.replace("$1", "3");
	err4 = err4.replace("$2", "10");
	String err5 = lan.getString("fsdetail","errlimit5");
	err5 = err5.replace("$1", "10");
	err5 = err5.replace("$2", "60");
	String err6 = lan.getString("fsdetail","errlimit6");
%>
<p class='standardTxt'></p>
<form id='frm_fsdtl' name='frm_fsdtl' method='post' action='servlet/master;jsessionid=<%=jsession%>'>
<input type='hidden' id='cmd_combo' name='cmd_combo' value='' />
<input type='hidden' id='idrack' name='idrack' value='' />
<input type='hidden' id='rackoffline' value='<%=lan.getString("fs","rackoffline")%>' />
<input type='hidden' id='rackoffline_newalg' value='<%=lan.getString("fs","rackoffline_newalg")%>' />
<input type='hidden' id='err' name='err' value="<%=err%>" />
<input type='hidden' id='err1' name='err1' value="<%=err1%>" />
<input type='hidden' id='err2' name='err2' value="<%=err2%>" />
<input type='hidden' id='err3' name='err3' value="<%=err3%>" />
<input type='hidden' id='err4' name='err4' value="<%=err4%>">
<input type='hidden' id='err5' name='err5' value="<%=err5%>">
<input type='hidden' id='err6' name='err6' value="<%=err6%>">
<input type='hidden' id='setAllDCConfirm' name='setAllDCConfirm' value="<%=lan.getString("fsdetail","setAllDCConfirm")%>" />
<input type='hidden' id='setAllDCConfirm_newalg' value="<%=lan.getString("fsdetail","setAllDCConfirm_newalg")%>" />
<input type='hidden' id='insertmaxoffutil' value="<%=lan.getString("fsdetail","insertmaxoffutil")%>"/>
<input type='hidden' id='vk_state' value='<%= (OnScreenKey) ? "1" : "0" %>' />

<p class='standardTxt'><b><%=lan.getString("fsdetail","rack")%> &nbsp; 	<%=combo_racks%></b></p>
</form>

<form id='frm_fsdtl_write' name='frm_fsdtl_write' method='post' action='servlet/master;jsessionid=<%=jsession%>'>	
<input type='hidden' id='cmd' name='cmd' value='' />
<input type='hidden' id='new_alg' name='new_alg' value='<%=rack.isNewAlg()%>'>
<table border='0' align="center" width="100%">
 <tr valign="top">
	<td valign='top' width="25%">
	
	<FIELDSET class='field'>
	<LEGEND class='standardTxt'><%=lan.getString("fsdetail","confrack") %>&nbsp;&nbsp;</LEGEND>
		<br />
	<TABLE class="standardTxt" height="20%" width='100%' border='0' cellpadding="1" cellspacing="5">
		<tr>
		 <TD width="15%"><b><%=lan.getString("fsdetail","currentset") %></b></TD>
		 <TD width="5%" align="left">&nbsp;<b><%=setpt%></b></TD>
		 <TD width="5%" align="left"><b><%= setpt_um%></b></TD>
		</tr>
		<tr>
		 <TD width="15%"><%=lan.getString("fsdetail","minset") %></TD>
		 <TD width="5%"><input id='minsetpt' name='minsetpt' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>  value='<%=minset%>' onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);"/></TD>
		 <TD width="5%"align="left"><%= minset_um%></TD>
		</tr>
		<tr>
		 <TD width="15%"><%=lan.getString("fsdetail","maxset") %></TD>
		 <TD width="5%"><input id='maxsetpt' name='maxsetpt' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=maxset%>' onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);"/></TD>
		 <TD width="5%" align="left"><%= maxset_um%></TD>
		</tr>
		<tr>
		 <TD width="15%"><%=lan.getString("fsdetail","grad") %></TD>
		 <TD width="5%"><input id='gradval' name='gradval' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=gradient%>' onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);"/></TD>
		 <TD width="5%" align="left"><%= gradient_um%></TD>
		</tr>
<%if( rack.isNewAlg() ) {%>		
		<input id='Sb' name='Sb' type='hidden' value='<%=FSManager.getSb()%>'>
		<tr>
		 <TD><%=lan.getString("fsdetail","sample_period")%></TD>
		 <TD><b><input id='t' name='t' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=FSManager.getT() / 60%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left"> min</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail", "yellowstatus")%></TD>
		 <TD><b><input id='YELLOW_Q' name='YELLOW_Q' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=FSManager.getYELLOW_Q()%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left">&nbsp;</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail", "orangestatus")%></TD>
		 <TD><b><input id='ORANGE_Q' name='ORANGE_Q' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=FSManager.getORANGE_Q()%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left">&nbsp;</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail", "redstatus")%></TD>
		 <TD><b><input id='RED_Q' name='RED_Q' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=FSManager.getRED_Q()%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left">&nbsp;</TD>
		</tr>
<%} else {%>
		<tr>
		 <TD><%=lan.getString("fsdetail","timewindow") %></TD>
		 <TD><b><input id='timewinval' name='timewinval' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=timewin%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left"> min</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail","freqdc") %></TD>
		 <TD><b><input id='waittimeval' name='waittimeval' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=waittime%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left"> min</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail","tmaxoffline") %></TD>
	   <TD><b><input id='maxofftimeval' name='maxofftimeval' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=maxofftime%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left"> %</TD>
		</tr>
		<tr>
		 <TD><%=lan.getString("fsdetail","maxnutiloffline") %></TD>
		 <TD><b><input id='maxoffutilval' name='maxoffutilval' size='5' type='text' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> value='<%=maxoffutil%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></b></TD>
		 <TD align="left"></TD>
		</tr>
<%}%>		
	</TABLE>
		<br />
	</FIELDSET>
	
	</td>
	<td width="75%">
	
	<FIELDSET class='field'>
	<LEGEND class='standardTxt'><%=lan.getString("fsdetail","utils") %></LEGEND>
	<div align="center">
	<TABLE class="standardTxt" height="50%" border='0' cellpadding="0" cellspacing="0">
		<TR valign="middle">
			<TD valign="middle" align="right" class='standardTxt'>
			<input type = "checkbox" name = "setAllDC" id = "setAllDC" onclick="setAllDCs(this);" />
			&nbsp;&nbsp;<%=lan.getString("fsdetail",rack.isNewAlg() ? "set_all_tsh" : "setalldc") %>
			&nbsp;&nbsp;<input type='text' disabled ="disabled"  value='<%=rack.isNewAlg() ? "" : "70"%>' id='maxDC' name='maxDC' size='4' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> <%=rack.isNewAlg() ? "" : "onkeydown='checkOnlyNumber(this,event);' onblur='onlyNumberOnBlur(this);'"%> />
			<%=rack.isNewAlg() ? "K" : "%"%>
			</TD>
		</TR>
		<TR>
			<TD align="center" colspan='2'><%=util_table%></TD>
		</TR>
	</TABLE>
	</div>
	</FIELDSET>
	</td>
 </tr>
</table>
</form>

<%if( rack.isNewAlg() ) { // change tooltip for TSH column%>
<script>
var tshHeader = document.getElementById("LWCtTitle0").firstChild.rows[0].cells[3];
tshHeader.title = '<%=lan.getString("fsdetail", "tsh_desc")%>';
</script>
<%}%>