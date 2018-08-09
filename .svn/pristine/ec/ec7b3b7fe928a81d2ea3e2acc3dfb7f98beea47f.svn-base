<%@ page language="java"%>
<%@ page import="com.carel.supervisor.presentation.kpi.KpiMgr"%>
<%@ page import="com.carel.supervisor.dataaccess.db.RecordSet"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@ page import="com.carel.supervisor.dataaccess.db.DatabaseMgr" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.carel.supervisor.presentation.helper.VirtualKeyboard" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	
	RecordSet kpist1rs = DatabaseMgr.getInstance().executeQuery(null, "select * from kpigroups");
	String[] kpigrpsname = new String[10];
	Integer[] kpigrpsid = new Integer[10];
	for(int i = 0;i<10;i++)
	{
		kpigrpsid[i] = 0;
		kpigrpsname[i] = "";
	}
	for(int i = 0;i<kpist1rs.size()&&i<10;i++)
	{
		Integer idg = (Integer)kpist1rs.get(i).get("idgrp");
		kpigrpsid[idg-1] = idg;
		kpigrpsname[idg-1] = (String)kpist1rs.get(i).get("name");
	}
	
	String iddevs = "";
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String virtkey = "off";
	if (OnScreenKey)
	{
		virtkey = "on";
	}
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<input type='hidden' id='virtkeyboard' value='<%=virtkey%>' />
<INPUT type="hidden" id="selall2gr" value="<%=multiLanguage.getString("kpi","kpiselall2gr")%>" />
<INPUT type="hidden" id="deselall2gr" value="<%=multiLanguage.getString("kpi","kpideselall2gr")%>" />
<FORM id="kpi_page2_form" name="kpi_page2_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="archtabtoload" name="archtabtoload" value="2"/> 
<INPUT type="hidden" id='kpicmd' name='kpicmd' value="kpiconf1"/>
<INPUT type="hidden" id="kpinoduplicates" name="kpinoduplicates" value="<%=multiLanguage.getString("kpi","kpinoduplicatednames")%>" />
<INPUT type="hidden" id="errmessage" name="errorerror" value="<%=sessionUser.getPropertyAndRemove("error")%>" />
<%
// query modified to exclude 'Internal IO' model
// Nicola Compagno 25032010
RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,"select count(*) from cfdevice where iscancelled='FALSE' and code != '-1.000'");
int countdev = (Integer)rs.get(0).get(0);
if(countdev>0)
{
%>
<p class='standardTxt'><%=multiLanguage.getString("kpi","comment2")%></p>
<table border="0" width="98%" height="95%" cellspacing="1" cellpadding="1">
	<TR>
		<TD>
			<FIELDSET class="field">
				<LEGEND class="standardTxt">
					<%=multiLanguage.getString("kpi","kpigrpsfield")%>
				</LEGEND>
             <TABLE><TR><TD height="3px"></TD></TR></TABLE>
             <TABLE border="0" width="98%" align="center" cellpadding="1" cellspacing="1">
					<TR align="center">
						<TD class='standardTxt' width="2%">
							Gr1
						</TD>
						<TD width="8%">
							<INPUT class='standardTxt' type="hidden" id='gr1h' name="gr1h" value="<%=kpigrpsid[0]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr1' name="gr1" value="<%=kpigrpsname[0]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt' width="2%">
							Gr2
						</TD>
						<TD width="8%">
							<INPUT class='standardTxt' type="hidden" id='gr2h' name="gr2h" value="<%=kpigrpsid[1]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr2' name="gr2" value="<%=kpigrpsname[1]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt' width="2%">
							Gr3
						</TD>
						<TD width="8%">
							<INPUT class='standardTxt' type="hidden" id='gr3h' name="gr3h" value="<%=kpigrpsid[2]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr3' name="gr3" value="<%=kpigrpsname[2]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt' width="2%">
							Gr4
						</TD>
						<TD width="8%">
							<INPUT class='standardTxt' type="hidden" id='gr4h' name="gr4h" value="<%=kpigrpsid[3]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr4' name="gr4" value="<%=kpigrpsname[3]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt' width="2%">
							Gr5
						</TD>
						<TD width="8%">
							<INPUT class='standardTxt' type="hidden" id='gr5h' name="gr5h" value="<%=kpigrpsid[4]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr5' name="gr5" value="<%=kpigrpsname[4]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
					</TR>
					<TR align="center">
						<TD class='standardTxt'>
							Gr6
						</TD>
						<TD>
							<INPUT class='standardTxt' type="hidden" id='gr6h' name="gr6h" value="<%=kpigrpsid[5]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr6' name="gr6" value="<%=kpigrpsname[5]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt'>
							Gr7
						</TD>
						<TD>
							<INPUT class='standardTxt' type="hidden" id='gr7h' name="gr7h" value="<%=kpigrpsid[6]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr7' name="gr7" value="<%=kpigrpsname[6]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt'>
							Gr8
						</TD>
						<TD>
							<INPUT class='standardTxt' type="hidden" id='gr8h' name="gr8h" value="<%=kpigrpsid[7]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr8' name="gr8" value="<%=kpigrpsname[7]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt'>
							Gr9
						</TD>
						<TD>
							<INPUT class='standardTxt' type="hidden" id='gr9h' name="gr9h" value="<%=kpigrpsid[8]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr9' name="gr9" value="<%=kpigrpsname[8]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
						<TD class='standardTxt'>
							Gr10
						</TD>
						<TD>
							<INPUT class='standardTxt' type="hidden" id='gr10h' name="gr10h" value="<%=kpigrpsid[9]%>" />
							<INPUT class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" id='gr10' name="gr10" value="<%=kpigrpsname[9]%>" onkeydown="checkBadChar(this,event);" onblur="noBadCharOnBlur(this,event);checkName(this);verifyGroup(this);" />
						</TD>
					</TR>
				</TABLE>
			</FIELDSET>
		</TD>
	</TR>
	<TR style="height: 5px;">
	<TD style="height: 5px;">
		<!-- TABLE><TR> <TD height="3px">&nbsp;</TD> </TR></TABLE -->
	</TD>
	</TR>
	<TR>
		<TD>
			<FIELDSET class="field">
				<LEGEND class="standardTxt">
					<%=multiLanguage.getString("kpi","kpidevsfield")%>
				</LEGEND>
             <TABLE><TR><TD height="3px"></TD></TR></TABLE>
				<TABLE align="center" id="kpidevicetable" class="table" border="0" width="98%" height="100%">
					<THEAD>
						<TR class='Row1'>
							<TH class="th" width="*">
								<%=multiLanguage.getString("kpi", "kpidevdesc")%>
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr1")%>
								<input type="radio" id="radiogr1" onclick="all2gr(1);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr2")%>
								<input type="radio" id="radiogr2" onclick="all2gr(2);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr3")%>
								<input type="radio" id="radiogr3" onclick="all2gr(3);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr4")%>
								<input type="radio" id="radiogr4" onclick="all2gr(4);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr5")%>
								<input type="radio" id="radiogr5" onclick="all2gr(5);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr6")%>
								<input type="radio" id="radiogr6" onclick="all2gr(6);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr7")%>
								<input type="radio" id="radiogr7" onclick="all2gr(7);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr8")%>
								<input type="radio" id="radiogr8" onclick="all2gr(8);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr9")%>
								<input type="radio" id="radiogr9" onclick="all2gr(9);" />
							</TH>
							<TH class="th" width="5%" align="center" valign="middle">
								<%=multiLanguage.getString("kpi", "kpigr10")%>
								<input type="radio" id="radiogr10" onclick="all2gr(10);" />
							</TH>
							<!-- TH class="th" width="5%" align="center" valign="middle">
								< %=multiLanguage.getString("kpi", "kpigrnone")%>
							</TH -->
						</TR>
					</THEAD>
					<TBODY style="overflow: auto; max-height: 200px;">
<%
			RecordSet rs2 = KpiMgr.getDevices(language);
			//RecordSet devs = KpiMgr.getDevicesGroups();
			for (int ii = 0; ii < rs2.size(); ii++) {
				String descdev = (String)rs2.get(ii).get("description");
				Integer iddev = new Integer(rs2.get(ii).get("iddevice").toString());
				iddevs = iddevs + iddev.toString() + ";";
				RecordSet grpsrs = KpiMgr.getDeviceGroups(iddev);
				HashMap<Integer, Boolean> tmphm=new HashMap<Integer, Boolean>();
				for(int kkk=0;kkk<grpsrs.size();kkk++){
					tmphm.put((Integer)grpsrs.get(kkk).get(0), true);
				} 
%>
						<TR class='<%=(ii%2==0?"Row1":"Row2") %>'>
							<TD class='standardTxt'>
								<%=descdev%>
								<INPUT type="hidden" id="kpiiddev<%=iddev%>" name="kpiiddev" value="<%=iddev%>" />
								<INPUT type="hidden" id="kpicheckbox<%=iddev%>" name="kpicheckbox<%=iddev%>" value="" />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr1_<%=iddev%>" name="gr1_<%=iddev%>" <%=tmphm.get(1)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr2_<%=iddev%>" name="gr2_<%=iddev%>" <%=tmphm.get(2)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr3_<%=iddev%>" name="gr3_<%=iddev%>" <%=tmphm.get(3)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr4_<%=iddev%>" name="gr4_<%=iddev%>" <%=tmphm.get(4)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr5_<%=iddev%>" name="gr5_<%=iddev%>" <%=tmphm.get(5)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr6_<%=iddev%>" name="gr6_<%=iddev%>" <%=tmphm.get(6)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr7_<%=iddev%>" name="gr7_<%=iddev%>" <%=tmphm.get(7)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr8_<%=iddev%>" name="gr8_<%=iddev%>" <%=tmphm.get(8)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr9_<%=iddev%>" name="gr9_<%=iddev%>" <%=tmphm.get(9)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr10_<%=iddev%>" name="gr10_<%=iddev%>" <%=tmphm.get(10)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<!-- TD align="center" valign="middle">
								<INPUT type="checkbox" id="grn_< %=iddev%>" name="gr_< %=iddev%>" < %=null==grp?"checked=\"cheched\"":""%> />
							</TD -->
						</TR>
						<%}
						//iddevs = iddevs.substring(0, iddevs.length() - 1);
%>
<%
			rs2 = KpiMgr.getLogicDevices(language);
			//RecordSet devs = KpiMgr.getDevicesGroups();
			for (int ii = 0; ii < rs2.size(); ii++) {
				String descdev = (String)rs2.get(ii).get("description");
				Integer iddev = new Integer(rs2.get(ii).get("iddevice").toString());
				iddevs = iddevs + iddev.toString() + ";";
				RecordSet grpsrs = KpiMgr.getDeviceGroups(iddev);
				HashMap<Integer, Boolean> tmphm=new HashMap<Integer, Boolean>();
				for(int kkk=0;kkk<grpsrs.size();kkk++){
					tmphm.put((Integer)grpsrs.get(kkk).get(0), true);
				}
%>
						<TR class='<%=(ii%2==0?"Row1":"Row2") %>'>
							<TD class='standardTxt'>
								<%=descdev%>
								<INPUT type="hidden" id="kpiiddev<%=iddev%>" name="kpiiddev" value="<%=iddev%>" />
								<INPUT type="hidden" id="kpicheckbox<%=iddev%>" name="kpicheckbox<%=iddev%>" value="" />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr1_<%=iddev%>" name="gr1_<%=iddev%>" <%=tmphm.get(1)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr2_<%=iddev%>" name="gr2_<%=iddev%>" <%=tmphm.get(2)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr3_<%=iddev%>" name="gr3_<%=iddev%>" <%=tmphm.get(3)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr4_<%=iddev%>" name="gr4_<%=iddev%>" <%=tmphm.get(4)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr5_<%=iddev%>" name="gr5_<%=iddev%>" <%=tmphm.get(5)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr6_<%=iddev%>" name="gr6_<%=iddev%>" <%=tmphm.get(6)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr7_<%=iddev%>" name="gr7_<%=iddev%>" <%=tmphm.get(7)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr8_<%=iddev%>" name="gr8_<%=iddev%>" <%=tmphm.get(8)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr9_<%=iddev%>" name="gr9_<%=iddev%>" <%=tmphm.get(9)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<TD align="center" valign="middle">
								<INPUT type="checkbox" id="gr10_<%=iddev%>" name="gr10_<%=iddev%>" <%=tmphm.get(10)!=null?"checked=\"cheched\"":""%> />
							</TD>
							<!-- TD align="center" valign="middle">
								<INPUT type="checkbox" id="grn_< %=iddev%>" name="gr_< %=iddev%>" < %=null==grp?"checked=\"cheched\"":""%> />
							</TD -->
						</TR>
						<%}
						iddevs = iddevs.substring(0, iddevs.length() - 1);
%>
					</TBODY>
				</TABLE>
			</FIELDSET>
		</TD>
	</TR>
	<TR>
		<TD align="right">
			<TABLE border="0" cellpadding="0" cellspacing="0" width="170px" height="30px" onclick="kpinextpage();" style="cursor: pointer;">
				<TR>
					<TD width="170px" height="30px" class="groupCategory">
						<%=multiLanguage.getString("kpi","kpiconfpage2")%>
						<SCRIPT>disableAction(1);</SCRIPT>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<%
}
else
{
%>
<table border="0" width="98%" height="95%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top">
		<td class="tdTitleTable" align="center"><%=multiLanguage.getString("grpview","nodevice")%></td>
	</tr>
</table>
<script type="text/javascript">disableAction(1);</script>
<%
}
%>
</FORM>
<INPUT type="hidden" id="idsdevs" value="<%=iddevs %>" />
