<%@ page language="java" 

import="java.util.Properties"

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNUtils"

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

int iddevmdl = -1;

Properties properties = transaction.getSystemParameter();
if(properties != null)
{		
	if(properties.get("iddevmdl") != null)
	{
		iddevmdl = Integer.parseInt((String)properties.get("iddevmdl"));
	}
}
else
{
	properties = new Properties();
}

transaction.setSystemParameter(null);

String combo_dev = LNUtils.getComboDevice(idsite, language, iddevmdl);

String inversa = lan.getString("lucinotte","inversa");
String var_cmd = lan.getString("lucinotte","var_cmd");
String var_stato = lan.getString("lucinotte","var_stato");
String lista_dev = lan.getString("lucinotte","listadev");
String luci = lan.getString("lucinotte","luci");
String notte = lan.getString("lucinotte","notte");
String status = lan.getString("lucinotte","status");
String comando = lan.getString("lucinotte", "comando");

%>

<input type="hidden" id="selcmd" value="<%=lan.getString("lucinotte","selcmd")%>" />
<input type="hidden" id="salva" name="salva" value="<%=lan.getString("lucinotte","salva")%>" />
<input type="hidden" id="wantChangeDev" name="wantChangeDev" value="<%=lan.getString("lucinotte","wantChangeDev")%>" />
<input type="hidden" id="changeDev" name="changeDev" value="false" />

<p class='standardTxt'><%=lan.getString("lucinotte","comment6")%></p>	

<FORM name="lcnt_frm" id="lcnt_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
<input type='hidden' id='cmd' name='cmd'/>
<input type='hidden' id='modificato' name='modificato' value='false'/>
  <table width="98%" align="center">
	<TR>
		<TD class='standardTxt' align='left'><b><%=lista_dev%>:</b>&nbsp;&nbsp;<%=combo_dev%></TD>
	</TR>
	<TR style='height:10px'>
	    <TD></TD>
	</TR>
  </table>
  <table width="75%" align="center">
	<TR>
		<TD class='standardTxt'>
			<table id='tbl_cmd' class='table' cellspacing='1' cellpadding='1' width='100%'>
				<tr height='21px' class='th'>
					<td width='15%' align='center'><b><%=comando%></b></td>
					<td width='70%' align="center"><b><%=var_cmd%></b></td>
					<td width='15%' align="center"><b><%=inversa%></b></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b>ON/OFF</b></td>
					<td id="c_onoff">&nbsp;</td>
					<td align="center"><input type="checkbox" id="cmd_onoff_inv" name="cmd_onoff_inv" /></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b><%=luci%></b></td>
					<td id="c_luci">&nbsp;</td>
					<td align="center"><input type="checkbox" id="cmd_luci_inv" name="cmd_luci_inv" /></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b><%=notte%></b></td>
					<td id="c_notte">&nbsp;</td>
					<td align="center"><input type="checkbox" id="cmd_notte_inv" name="cmd_notte_inv" /></td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR style='height:30px'><TD>&nbsp;</TD></TR>
	<TR>
		<TD class='standardTxt' >
			<table id='tbl_status' class='table' cellspacing='1' cellpadding='1' width='100%'>
				<tr height='21px' class='th'>
					<td width='15%' align='center'><b><%=status%></b></td>
					<td width='70%' align="center"><b><%=var_stato%></b></td>
					<td width='15%' align="center"><b><%=inversa%></b></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b>ON/OFF</b></td>
					<td id="s_onoff">&nbsp;</td>
					<td align="center"><input type="checkbox" id="st_onoff_inv" name="st_onoff_inv" /></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b><%=luci%></b></td>
					<td id="s_luci">&nbsp;</td>
					<td align="center"><input type="checkbox" id="st_luci_inv" name="st_luci_inv" /></td>
				</tr>
				<tr height='21px' style='cursor:pointer' class='Row1'>
					<td class='standardTxt'>&nbsp;<b><%=notte%></b></td>
					<td id="s_notte">&nbsp;</td>
					<td align="center"><input type="checkbox" id="st_notte_inv" name="st_notte_inv" /></td>
				</tr>
			</table>
		</TD>
	</TR>
  </table>
</FORM>
