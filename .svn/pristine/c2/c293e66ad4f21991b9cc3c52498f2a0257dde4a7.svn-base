<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.bo.BRSiteAcces"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
	import="com.carel.supervisor.base.conversion.DateUtils"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%@ page import="com.carel.supervisor.remote.engine.connection.ActiveConnections" %>
<%
	int DISTRIBUTION = 0;
	
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String jsession = request.getSession().getId();
	LangService lan = LangMgr.getInstance().getLangService(language);
	BRSiteAcces bsite = (BRSiteAcces)sessionUser.getCurrentUserTransaction().getBoTrx();
	
	String cinstlan = sessionUser.getCurrentUserTransaction().getProperty("curinstal");
	String ctype = sessionUser.getCurrentUserTransaction().getProperty("curtype");
	String ipforlan = sessionUser.getCurrentUserTransaction().getProperty("ipforlan");
	
	String	cfp = sessionUser.getCurrentUserTransaction().getProperty("comefrompost");
	String	ist = ActiveConnections.getInstance().getTypeLocalOut();
	String iplocal = ActiveConnections.getInstance().getIpLocalInOut();
	int idconnect = ActiveConnections.getInstance().getIdLocalOut();
	
	SiteInfo[] list = bsite.getSiteList();
	if(iplocal != null)
	{
		iplocal = iplocal.trim();	
		if(iplocal.length() > 0)
		{
			if(ist != null && ist.equalsIgnoreCase("PVP"))
				iplocal = request.getScheme()+"://"+iplocal+":"+request.getServerPort();
			else
				iplocal = "http://"+iplocal;
		}
	}
	else
	 iplocal = "";
	 
	if(iplocal != null && iplocal.equalsIgnoreCase("") && ctype != null && ctype.equalsIgnoreCase("LAN") && 
		 ipforlan != null && !ipforlan.equalsIgnoreCase("NOP"))
	{
		if(cinstlan != null && cinstlan.equalsIgnoreCase("PVP"))
		{
			ist = cinstlan;
			iplocal = request.getScheme()+"://"+ipforlan+":"+request.getServerPort();
		}
		else
			iplocal = "http://"+ipforlan;
	}
		
	String alarmsite = lan.getString("r_siteaccess","countalarmsite");	
	String state = lan.getString("r_siteaccess","state");
	String type = lan.getString("r_siteaccess","type");
	String chan = lan.getString("r_siteaccess","chanel");
	String number = lan.getString("r_siteaccess","number");
	String conn = lan.getString("r_siteaccess","conn");
	String synch = lan.getString("r_siteaccess","sync");
	
	String off = lan.getString("r_siteaccess","ttoffline");
	String con = lan.getString("r_siteaccess","ttconnonline");
	String han = lan.getString("r_siteaccess","tthangonline");
	String nosite = lan.getString("r_siteaccess","nosite");
	String closeconn_a = lan.getString("r_siteaccess","closeconn_a");
	String closeconn_b = lan.getString("r_siteaccess","closeconn_b");
	
	 int dim = list.length;
	 int counter = 0;
	 String bgTable = "";
	 
	 // Alarm Site
	 int alarmSite = 0;
	 for(int i=0; i<list.length; i++) {
	 	if(list[i].getSiteStatus() == 2)
	 		alarmSite++;
	 }
	 
	 if(alarmSite > 0)
	 	bgTable = "style='background-Color:#FF0000'";
%>
<form name="frmrreload" id="frmrreload" method="POST" action="servlet/master;jsessionid=<%=jsession%>"></form>
<form name="frmlocallist" id="frmlocallist" method="POST" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="curlang" name="curlang" value="<%=language%>"/>
<input type="hidden" id="cursite" name="cursite" value=""/>
<input type="hidden" id="curphone" name="curphone" value=""/>
<input type="hidden" id="curiplocal" name="curiplocal" value="<%=iplocal%>"/>
<input type="hidden" id="curconn" name="curconn" value="<%=idconnect%>"/>
<input type="hidden" id="curaction" name="curaction" value=""/>
<input type="hidden" id="curtype" name="curtype" value=""/>
<input type="hidden" id="curinstal" name="curinstal" value="<%=ist%>"/>
<input type="hidden" id="comefrompost" name="comefrompost" value="<%=cfp%>"/>
</form>
<%
	if ( dim > 0) 
	{
%>

<input type='hidden' id ='closeconn_a' value='<%=closeconn_a%>'/>
<input type='hidden' id ='closeconn_b' value='<%=closeconn_b%>'/>

<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td style="height:5px;"></td>
	</tr>
	<tr>
		<td colspan="9" align="center">
				<table border="0" width="100%" cellspacing="1" cellpadding="1" <%=bgTable%> >
					<tr>
						<td class="standardTxt" align="center"><b><%=alarmsite%>: <%=alarmSite%></b></td>
					</tr>
				</table>	
		</td>
	</tr>
	<tr>
		<td style="height:5px;"></td>
	</tr>
	<tr>
		<td width="3%"  class="standardTxt" align="center">&nbsp;</td>
		<td width="4%"  class="standardTxt" align="center"><b><%=state%></b></td>
		<td width="*"   class="standardTxt" align="center">&nbsp;</td>
		<td width="6%"  class="standardTxt" align="center"><b><%=type%></b></td>
		<td width="8%" class="standardTxt" align="center"><b><%=chan%></b></td>
		<td width="10%" class="standardTxt" align="center"><b><%=number%></b></td>
		<td width="15%" class="standardTxt" align="center"><b><%=conn%></b></td>
		<td width="15%" class="standardTxt" align="center"><b><%=synch%></b></td>
		<td width="7%" class="standardTxt" align="center"><b></b></td>
	</tr>
</table>

<div id="contlistsite" style="height:360px;overflow:auto;">

<table border="0" width="100%" cellspacing="1" cellpadding="1">
<%
  	for (int i=0; i<dim; i++) 
  	{
%>
	<%if (counter == 0) {%>
	<tr>
	<%}%>
		<td>
			<table class="table" border="0" width="100%" cellspacing="1" cellpadding="1" id="RTb_<%=list[i].getId()%>">
				<tbody>
				<tr>
					<td width="3%"  class="standardTxt" align="left"><b><%=(i+1)%></b></td>
					<td width="4%"  class="standardTxt" align="center"><img id="RStatus_<%=list[i].getId()%>" src="images/led/L<%=list[i].getSiteStatus()%>.gif"></td>
					<td width="*"   class="standardTxt" align="left"><%=list[i].getName()%></td>
					<td width="6%"  class="standardTxt" align="center"><img src="images/led/<%=list[i].getType()%>.gif"></td>
					<td width="8%" class="standardTxt" align="center"><%=list[i].getConnectiontype()%></td>
					<td width="10%" class="standardTxt" align="right"><%=list[i].getPhone()%></td>
					<td width="15%" class="standardTxt" align="center"><div id="RDial_<%=list[i].getId()%>"><%=((list[i].getLastDialup()==null)?"---":DateUtils.date2String(list[i].getLastDialup(),"yyyy/MM/dd HH:mm"))%></div></td>
					<td width="15%" class="standardTxt" align="center"><div id="RDate_<%=list[i].getId()%>"><%=((list[i].getLastconnection()==null)?"---":DateUtils.date2String(list[i].getLastconnection(),"yyyy/MM/dd HH:mm"))%></div></td>
					<td width="7%">
						<table border="0" width="100%" cellspacing="0" cellpadding="0">
							<tr>
								<td class="standardTxt" align="center"><img style="cursor:pointer;" src="<%=(list[i].getLastconnection()==null)?"images/remote/nooffline.png":"images/remote/offline.png"%>" onclick="browsOffline(this,<%=list[i].getId()%>,'<%=list[i].getLastconnection()%>');" id="bttoffline<%=list[i].getId()%>" title="<%=off%>"></img></td>
								<td class="standardTxt" align="center"><img style="cursor:pointer;" title="<%=(list[i].getId()==idconnect)?han:con%>" src="<%=(list[i].getId()==idconnect)?"images/remote/disconnection.png":"images/remote/connection.png"%>" id="bttonline<%=list[i].getId()%>" onclick="browsOnline(this,<%=list[i].getId()%>,'<%=list[i].getPhone()%>','<%=list[i].getConnectiontype()%>','<%=list[i].getType()%>');"></img></td>
							</tr>
						</table>
					</td>
				</tr>
				
				</tbody>
			</table>
		</td>
	<%if (counter == DISTRIBUTION) {%>
	</tr>
	<%
		counter = 0;
	} else {counter +=1;}%>
	
<% 	}
%>
</table>
</div>
<% 
	} else { %>
  <table border="0" width="100%" cellspacing="1" cellpadding="1"><tr><td class="tdTitleTable" align="center"><%=nosite%></td></tr></table>
<% } %>

<SCRIPT type="text/javascript">
<!--
	var thisbodyx,thisbodyy;
	if (self.innerHeight) // all except Explorer
	{
		thisbodyx = self.innerWidth;
		thisbodyy = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		thisbodyx = document.documentElement.clientWidth;
		thisbodyy = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		thisbodyx = document.body.clientWidth;
		thisbodyy = document.body.clientHeight;
	}
	if(document.getElementById('contlistsite'))
		document.getElementById('contlistsite').style.height = thisbodyy-198;
//-->
</SCRIPT>

 