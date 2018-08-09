<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.SiteBookletBean"
	import="java.util.Properties"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
SiteBookletBean bean = new SiteBookletBean(Integer.parseInt(ut.getProperty("idbooklet")));
bean.loadAll();
String header = bean.header.isEmpty() ? lang.getString("sitebooklet", "dpr_date") : bean.header;

// contacts
String surname_0 = "";
String address_0 = "";
String phone_0 = "";
String fax_0 = "";
String e_mail_0 = "";
Properties propOwner = bean.aContacts[SiteBookletBean.CTYPE_OWNER];
if( propOwner != null ) {
	surname_0 = propOwner.getProperty("surname");
	address_0 = propOwner.getProperty("address");
	phone_0 = propOwner.getProperty("phone");
	fax_0 = propOwner.getProperty("fax");
	e_mail_0 = propOwner.getProperty("e_mail");
}
String surname_1 = "";
String phone_1 = "";
String fax_1 = "";
String e_mail_1 = "";
Properties propManager = bean.aContacts[SiteBookletBean.CTYPE_MANAGER];
if( propManager != null ) {
	surname_1 = propManager.getProperty("surname");
	phone_1 = propManager.getProperty("phone");
	fax_1 = propManager.getProperty("fax");
	e_mail_1 = propManager.getProperty("e_mail");
}
String address_2 = "";
String phone_2 = "";
String fax_2 = "";
String e_mail_2 = "";
Properties propPlant = bean.aContacts[SiteBookletBean.CTYPE_PLANT];
if( propPlant != null ) {
	address_2 = propPlant.getProperty("address");
	phone_2 = propPlant.getProperty("phone");
	fax_2 = propPlant.getProperty("fax");
	e_mail_2 = propPlant.getProperty("e_mail");
}
String surname_3 = "";
String phone_3 = "";
String fax_3 = "";
String e_mail_3 = "";
String name_3 = "";
Properties propHeadPlant = bean.aContacts[SiteBookletBean.CTYPE_HEAD_OF_PLANT];
if( propHeadPlant != null ) {
	surname_3 = propHeadPlant.getProperty("surname");
	phone_3 = propHeadPlant.getProperty("phone");
	fax_3 = propHeadPlant.getProperty("fax");
	e_mail_3 = propHeadPlant.getProperty("e_mail");
	name_3 = propHeadPlant.getProperty("name");
}
String surname_4 = "";
String name_4 = "";
String address_4 = "";
String phone_4 = "";
String fax_4 = "";
String e_mail_4 = "";
String other_4 = "";
Properties propHeadAudit = bean.aContacts[SiteBookletBean.CTYPE_HEAD_OF_AUDIT];
if( propHeadAudit != null ) {
	surname_4 = propHeadAudit.getProperty("surname");
	name_4 = propHeadAudit.getProperty("name");
	phone_4 = propHeadAudit.getProperty("phone");
	fax_4 = propHeadAudit.getProperty("fax");
	e_mail_4 = propHeadAudit.getProperty("e_mail");
	other_4 = propHeadAudit.getProperty("other");
}
%>

<html>
<head>
  	<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
  	<style>
  	table.sbtext {
  		width: 100%;
  	}
	table.sbtext td {
		font-family: Tahoma,Verdana,Arial,sans-serif;
	 	font-size: 12pt;
	 	color:black;
	}  	

  	table.sbtextbig {
  		width: 100%;
  	}
	table.sbtextbig td {
		font-family: Tahoma,Verdana,Arial,sans-serif;
	 	font-size: 16pt;
	 	color:black;
	}  	
  	</style>
</head>

<body onLoad="window.print()">

<!-- page3: Site booklet -->
<!--
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "sitebooklet")%></h1>
		<br><h3><%=header%></h3></th>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3" align="center">
			<table style="border:1px solid black;width:525px;height:300px;"><tr valign="top"><td><%=bean.note%></td></tr></table>
		</td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td width="68%" colspan="2"><input type="checkbox" name="cb_ref_low_temp_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_low_temp_name)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_low_temp_name")%></td>
		<td width="32%"><%=bean.ref_low_temp_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_ref_low_temp_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_low_temp_id)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_low_temp_id")%></td>
		<td><%=bean.ref_low_temp_id%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_ref_avg_temp_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_avg_temp_name)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_avg_temp_name")%></td>
		<td><%=bean.ref_avg_temp_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_ref_avg_temp_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_avg_temp_id)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_avg_temp_id")%></td>
		<td><%=bean.ref_avg_temp_id%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_cond_supermarket_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_supermarket_name)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_supermarket_name")%></td>
		<td><%=bean.cond_supermarket_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_cond_supermarket_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_supermarket_id)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_supermarket_id")%></td>
		<td><%=bean.cond_supermarket_id%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_cond_units_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_units_name)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_units_name")%></td>
		<td><%=bean.cond_units_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_cond_units_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_units_id)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_units_id")%></td>
		<td><%=bean.cond_units_id%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_heat_pump_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_heat_pump_name)%>>&nbsp;<%=lang.getString("sitebooklet", "heat_pump_name")%></td>
		<td><%=bean.heat_pump_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_heat_pump_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_heat_pump_id)%>>&nbsp;<%=lang.getString("sitebooklet", "heat_pump_id")%></td>
		<td><%=bean.heat_pump_id%></td>
	</tr>
</table>
</div>
-->

<!-- page3: Cover -->
<div style="width:595px;page-break-after:always;">
<table border="0" align="center">
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "sitebooklet")%></h1>
	</tr>
	<tr>
		<th colspan="2">			
			<%=bean.header%>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "smk")%></td>
		<td width="50%"><%=bean.smk%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "system_type")%></td>
		<td width="50%"><%=bean.system_type%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "circuit_id")%></td>
		<td width="50%"><%=bean.circuit_id%></td>
	</tr>
</table>
</div>

<!-- page4: Instructions -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr><th><h2><%=lang.getString("sitebooklet", "instructions")%></h2></th></tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td><%=bean.text.replace("\n", "<br>")%></td></tr>
</table>
</div>

<!-- page5: Contacts -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "owner_details")%></h2>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td width="45%"><%=lang.getString("sitebooklet", "owner_company")%></td>
		<td width="55%"><%=surname_0%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "address")%></td>
		<td><%=address_0%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><%=phone_0%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><%=fax_0%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><%=e_mail_0%></td>
	</tr>
	<tr height="100px"><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<th colspan="2">
			<h2><%=lang.getString("sitebooklet", "manager_details")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<th colspan="2">
			<h3><%=lang.getString("sitebooklet", "manager_diff")%></h3>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "manager_company")%></td>
		<td><%=surname_1%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><%=phone_1%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><%=fax_1%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><%=e_mail_1%></td>
	</tr>
</table>
</div>

<!-- page6: Contacts -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "plant_details")%></h2>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td width="45%"><%=lang.getString("sitebooklet", "address")%></td>
		<td width="55%"><%=address_2%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><%=phone_2%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><%=fax_2%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><%=e_mail_2%></td>
	</tr>
	<tr height="100px"><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<th colspan="2">
			<br><br><h2><%=lang.getString("sitebooklet", "head_of_plant")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "surname")%></td>
		<td><%=surname_3%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><%=phone_3%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><%=fax_3%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><%=e_mail_3%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	
	<tr>
		<td><%=lang.getString("sitebooklet", "name")%></td>
		<td><%=name_3%></td>
	</tr>
</table>
</div>

<!-- page7: Contacts -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="2">
			<h2><%=lang.getString("sitebooklet", "head_of_audit")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td width="40%"><%=lang.getString("sitebooklet", "surname")%></td>
		<td width="60%"><%=surname_4%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "name")%></td>
		<td><%=name_4%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><%=phone_4%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><%=fax_4%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><%=e_mail_4%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "other")%></td>
		<td><%=other_4%></td>
	</tr>
	<tr height="100px"><td colspan="2">&nbsp;</td></tr>
	<tr>
		<th colspan="2">
			<br><br><h2><%=lang.getString("sitebooklet", "note2")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td colspan="2" align="left"><pre><%=bean.site_usage%></pre></td>
	</tr>
</table>
</div>

<!-- page8: Site type -->
<div style="width:595px;page-break-after:always;">
<table class="sbtextbig">
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "site_type")%></h1>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td width="50%"><input type="checkbox" name="cb_direct_expansion" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_direct_expandion)%>>&nbsp;<%=lang.getString("sitebooklet", "direct_expansion")%></td>
		<td width="50%">&nbsp;</td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_secondary_fluid" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_secondary_fluid)%>>&nbsp;<%=lang.getString("sitebooklet", "secondary_fluid")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_site_type_other" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_other)%>>&nbsp;<%=lang.getString("sitebooklet", "other")%></td>
		<td><%=bean.site_type_other%></td>
	</tr>
	<tr height="100px"><td colspan="3">&nbsp;</td>
	<tr>
		<th colspan="3"><br><br><h1><%=lang.getString("sitebooklet", "refrigerant_type")%></h1>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_1" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_1)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_1")%></td>
		<td><input type="checkbox" name="cb_ref_type_2" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_2)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_2")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_3" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_3)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_3")%></td>
		<td><input type="checkbox" name="cb_ref_type_4" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_4)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_4")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_5" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_5)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_5")%></td>
		<td><input type="checkbox" name="cb_ref_type_6" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_6)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_6")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_7" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_7)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_7")%></td>
		<td><input type="checkbox" name="cb_ref_type_8" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_8)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_type_8")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_0" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_0)%> onClick="onClickRefTypeOther(this.checked)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_0")%></td>
		<td><%=bean.ref_type_other%></td>
	</tr>
</table>
</div>

<!-- page9: Secondary fluid -->
<%if( bean.isSecondaryFluid() ) {%>
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "secondary_fluid_used")%></h1>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><input type="checkbox" name="cb_water_ethyleneglycol" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_water_ethyleneglycol)%>>&nbsp;<%=lang.getString("sitebooklet", "W_EG")%></td>
		<td width="50%"><%=bean.water_ethyleneglycol%>&nbsp;(%)</td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_water_propyleneglycol" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_water_propyleneglycol)%>>&nbsp;<%=lang.getString("sitebooklet", "W_PG")%></td>
		<td><%=bean.water_propyleneglycol%>&nbsp;(%)</td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_organic_mixtures" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_organic_mixtures)%>>&nbsp;<%=lang.getString("sitebooklet", "organic_mixtures")%></td>
		<td><%=bean.organic_mixtures%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "brand")%></td>
		<td><%=bean.organic_mixtures_brand%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td>&nbsp;</td>	
		<td><%=lang.getString("sitebooklet", "type")%></td>
		<td><%=bean.organic_mixtures_type%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_refrigerant_type" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_refrigerant_type)%>>&nbsp;<%=lang.getString("sitebooklet", "refrigerant_type")%>:</td>
		<td><%=bean.refrigerant_type%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_secondary_fluid_other" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_secondary_fluid_other)%>>&nbsp;<%=lang.getString("sitebooklet", "other_specify")%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3" align="left"><%=bean.secondary_fluid_other%></td>
	</tr>
	<tr height="100px"><td colspan="3">&nbsp;</td>
	<tr>
		<th colspan="3"><br><br><h1><%=lang.getString("sitebooklet", "first_charge_ref")%></h1>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "kg")%></td>
		<td><%=bean.secondary_fluid_kg%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "date")%></td>
		<td><%=SiteBookletBean.formatBookletDate(bean.secondary_fluid_date)%></td>
	</tr>
</table>
</div>
<%}%>

<!-- page10: Site reference -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "site_reference")%></h1></th>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "type_model")%></td>
		<td width="50%"><%=bean.type_model%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "reg_num")%></td>
		<td width="50%"><%=bean.registration_number%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "built_year")%></td>
		<td width="50%"><%=bean.built_year%></td>
	</tr>
	<tr height="100px"><td colspan="3">&nbsp;</td>
	<tr>
		<th colspan="3"><h2><%=lang.getString("sitebooklet", "fluid_level")%></h2>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_sensors" <%=bean.checkSiteReferenceFlag(SiteBookletBean.SREF_level_sensors)%>>&nbsp;<%=lang.getString("sitebooklet", "level_sensors")%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "brand_model")%></td>
		<td><%=bean.brand_model%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "min_max_val")%></td>
		<td><%=bean.min_value%> / <%=bean.max_value%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_indicators" <%=bean.checkSiteReferenceFlag(SiteBookletBean.SREF_level_indicators)%>>&nbsp;<%=lang.getString("sitebooklet", "level_indicators")%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td width="5%">&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "level_estimation")%></td>
		<td><%=bean.level_estimation%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "compared_to")%></td>
		<td><%=bean.level_reference%></td>
	</tr>
</table>
</div>

<!-- page11: Safety devices - Limitation of pressure -->
<%
for(int i = 0; bean.aSafetyDevices1 != null && i < bean.aSafetyDevices1.length; i++) {
	Properties prop								= bean.aSafetyDevices1[i];
	String safety_pressure_pos					= prop.getProperty("safety_pressure_pos");
	String safety_pressure_series				= prop.getProperty("safety_pressure_series");
	String safety_pressure_action				= prop.getProperty("safety_pressure_action");
	String safety_pressure_ped_category			= prop.getProperty("safety_pressure_ped_category");
	String replacement_type						= prop.getProperty("replacement_type");
	String replacement_series					= prop.getProperty("replacement_series");
	String replacement_pressure_calibration		= prop.getProperty("replacement_pressure_calibration");
	String replacement_ped_category				= prop.getProperty("replacement_ped_category");
	String replacement_expires					= prop.getProperty("replacement_expires");
	String replacement_date						= SiteBookletBean.formatBookletDate(prop.getProperty("replacement_date"));
%>
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "safety_devices")%></h1>
		<br><h2><%=lang.getString("sitebooklet", "pressure_threshold")%><br><%=lang.getString("sitebooklet", "safety_pressure")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "pos_on_plant")%></td>
		<td width="50%"><%=safety_pressure_pos%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "type_brand_mod")%></td>
		<td><%=safety_pressure_series%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_action")%></td>
		<td><%=safety_pressure_action%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "ped_category")%></td>
		<td><%=safety_pressure_ped_category%></td>
	</tr>
	<tr height="50px"><td colspan="2">&nbsp;</td>
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "replacement")%></h2></th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "sefety_device_type")%></td>
		<td><%=replacement_type%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "brand_mod_series")%></td>
		<td><%=replacement_series%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_calibration")%></td>
		<td><%=replacement_pressure_calibration%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "ped_category")%></td>
		<td><%=replacement_ped_category%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_expires")%></td>
		<td><%=replacement_expires%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_date")%></td>
		<td><%=replacement_date%></td>
	</tr>
</table>
</div>
<%}%>

<!-- page12: Safety devices - Pressure relief -->
<%
for(int i = 0; bean.aSafetyDevices2 != null && i < bean.aSafetyDevices2.length; i++) {
	Properties prop								= bean.aSafetyDevices2[i];
	String safety_valve_pos						= prop.getProperty("safety_valve_pos");
	String safety_valve_series					= prop.getProperty("safety_valve_series");
	String safety_valve_pressure_calibration	= prop.getProperty("safety_valve_pressure_calibration");
	String replacement_type						= prop.getProperty("replacement_type");
	String replacement_series					= prop.getProperty("replacement_series");
	String replacement_pressure_calibration		= prop.getProperty("replacement_pressure_calibration");
	String replacement_ped_category				= prop.getProperty("replacement_ped_category");
	String replacement_expires					= prop.getProperty("replacement_expires");
	String replacement_date						= SiteBookletBean.formatBookletDate(prop.getProperty("replacement_date"));
%>
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "safety_devices")%></h1>
		<br><h2><%=lang.getString("sitebooklet", "pressure_relief")%><br><%=lang.getString("sitebooklet", "safety_valve")%></h2>
		</th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "pressure_relief")%></h2></th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "pos_on_plant")%></td>
		<td><%=safety_valve_pos%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "type_brand_mod")%></td>
		<td><%=safety_valve_series%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_calibration_ped")%></td>
		<td><%=safety_valve_pressure_calibration%></td>
	</tr>
	<tr height="50px"><td colspan="2">&nbsp;</td>
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "replacement")%></h2></th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "sefety_device_type")%></td>
		<td><%=replacement_type%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "brand_mod_series")%></td>
		<td><%=replacement_series%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_calibration")%></td>
		<td><%=replacement_pressure_calibration%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "ped_category")%></td>
		<td><%=replacement_ped_category%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_expires")%></td>
		<td><%=replacement_expires%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_date")%></td>
		<td><%=replacement_date%></td>
	</tr>
</table>
</div>
<%}%>

<!-- page26: Refrigerant recovery -->
<%
for(int i = 0; bean.aRefrigerantRecovery != null && i < bean.aRefrigerantRecovery.length; i++) {
	Properties prop					= bean.aRefrigerantRecovery[i];
	String recovered_ref_type		= prop.getProperty("recovered_ref_type");
	String recovered_qantity		= prop.getProperty("recovered_qantity");
	String recovery_date			= SiteBookletBean.formatBookletDate(prop.getProperty("recovery_date"));
	String replaced_ref_type		= prop.getProperty("replaced_ref_type");
	String replaced_qantity			= prop.getProperty("replaced_qantity");
	String intervention_date		= SiteBookletBean.formatBookletDate(prop.getProperty("intervention_date"));
	String recovery_equipment		= prop.getProperty("recovery_equipment");
	String iso_equipment			= prop.getProperty("iso_equipment");
	String operator_stamp			= prop.getProperty("operator_stamp");
	String user_name				= prop.getProperty("user_name");
	String date						= SiteBookletBean.formatBookletDate(prop.getProperty("date"));
%>
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "ref_recovery")%></h1>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "ref_type_used")%></td>
		<td width="50%"><%=recovered_ref_type%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "quantity")%></td>
		<td><%=recovered_qantity%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "recovery_date")%></td>
		<td><%=recovery_date%></td>		
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "replaced_ref_type")%></td>
		<td width="50%"><%=replaced_ref_type%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "replaced_ref_quantity")%></td>
		<td><%=replaced_qantity%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "intervention_date")%></td>
		<td><%=intervention_date%></td>		
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "recovery_equipment")%></td>
	</tr>
	<tr>
		<td><%=iso_equipment%></td>
		<td><%=recovery_equipment%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "company_name")%></td>
		<td width="50%"><%=operator_stamp%></td>
	</tr>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "user_name")%></td>
		<td width="50%"><%=user_name%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "date")%></td>
		<td><%=date%></td>		
	</tr>
</table>
</div>
<%}%>

<!-- page30: Prevention plan -->
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="4"><h1><%=lang.getString("sitebooklet", "prevention_plan")%></h1>
		<br>
		<h2><%=lang.getString("sitebooklet", "preventive_measures")%></h2>
		</th>
	</tr>
	<tr><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td></tr>
	<tr>
		<td colspan="4"><%=lang.getString("sitebooklet", "first_charge_ref_ver")%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>	
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "date")%></td>
		<td colspan="2"><%=SiteBookletBean.formatBookletDate(bean.prevention_plan_date)%></td>		
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>	
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "quantity")%></td>
		<td colspan="2"><%=bean.prevention_plan_quantity%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>	
	<tr>
		<td colspan="4"><%=lang.getString("sitebooklet", "inspections_intervals")%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>	
	<tr valign="top">
		<td><%=bean.pp_label0%></td>
		<td align="center">
			<input type="checkbox" name="cb_interval_quarterly" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_quarterly)%>>
			&nbsp;<%=bean.pp_label1_1%>
			<br><%=bean.pp_label1_2%>
			<br><%=bean.pp_label1_3%>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_semiannual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_semiannual)%>>
			&nbsp;<%=bean.pp_label2_1%>
			<br><%=bean.pp_label2_2%>
			<br><%=bean.pp_label2_3%>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_annual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_annual)%>>
			&nbsp;<%=bean.pp_label3_1%>
			<br><%=bean.pp_label3_2%>
			<br><%=bean.pp_label3_3%>
		</td>
	</tr>
	<tr height="50px"><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td>&nbsp;</td>
		<th colspan="2">
		<h2><%=lang.getString("sitebooklet", "control_actions")%></h2>
		</th>
		<td>&nbsp;</td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>	
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_ref_charge" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_ref_charge)%>>&nbsp;<%=lang.getString("sitebooklet", "check_ref_charge")%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_loss_central" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_loss_central)%>>&nbsp;<%=lang.getString("sitebooklet", "check_loss_central")%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_critical_points" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_critical_points)%>>&nbsp;<%=lang.getString("sitebooklet", "check_critical_points")%></td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_electricity_supply" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_electricity_supply)%>>&nbsp;<%=lang.getString("sitebooklet", "electricity_supply")%></td>
		<td><input type="checkbox" name="cb_cp_capacitors" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_capacitors)%>>&nbsp;<%=lang.getString("sitebooklet", "capacitors")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_hi_pressure_pipe" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_hi_pressure_pipe)%>>&nbsp;<%=lang.getString("sitebooklet", "hi_pressure_pipe")%></td>
		<td><input type="checkbox" name="cb_cp_evaporators" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_evaporators)%>>&nbsp;<%=lang.getString("sitebooklet", "evaporators")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_lo_pressure_pipe" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_lo_pressure_pipe)%>>&nbsp;<%=lang.getString("sitebooklet", "lo_pressure_pipe")%></td>
		<td><input type="checkbox" name="cb_cp_filters_valves" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_filters_valves)%>>&nbsp;<%=lang.getString("sitebooklet", "filters_valves")%></td>
		<td>&nbsp;</td>
	</tr>
</table>
</div>

<!-- page31: Leakage verification -->
<%
for(int i = 0; bean.aLeakageVerification != null && i < bean.aLeakageVerification.length; i++) {
	Properties prop						= bean.aLeakageVerification[i];
	String instrument_type				= prop.getProperty("instrument_type");
	String instrument_sensitivity		= prop.getProperty("instrument_sensitivity");
	String test_date					= SiteBookletBean.formatBookletDate(prop.getProperty("test_date"));
	String leakage_description			= prop.getProperty("leakage_description");
	String corrective_action			= prop.getProperty("corrective_action");
	String ref_to_restore_quantity		= prop.getProperty("ref_to_restore_quantity");
	String brand_model					= prop.getProperty("brand_model");
	String min_value					= prop.getProperty("min_value");
	String max_value					= prop.getProperty("max_value");
	String level_estimation				= prop.getProperty("level_estimation");
	String level_reference				= prop.getProperty("level_reference");
	String operator_stamp				= prop.getProperty("operator_stamp");
	String user_name					= prop.getProperty("user_name");
	String date							= SiteBookletBean.formatBookletDate(prop.getProperty("date"));
	int flags							= Integer.parseInt(prop.getProperty("flags"));
%>
<div style="width:595px;page-break-after:always;">
<table class="sbtext">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "leakage_verification")%></h1>
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="2" width="45%"><%=lang.getString("sitebooklet", "instrument_type")%></td>
		<td width="55%"><%=instrument_type%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "instrument_sensitivity")%></td>
		<td><%=instrument_sensitivity%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "test_date")%></td>
		<td><%=test_date%></td>		
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "test_result")%></td>
		<td colspan="2">
			<table width="100%"><tr>
				<td width="55%"><input type="checkbox" name="cb_no_leaks" <%=bean.checkLeakageVerificationFlag(flags, SiteBookletBean.LV_no_leaks)%>>&nbsp;<%=lang.getString("sitebooklet", "no_leaks")%></td>
				<td width="45%" align="right"><input type="checkbox" name="cb_leaks" <%=bean.checkLeakageVerificationFlag(flags, SiteBookletBean.LV_leaks)%>>&nbsp;<%=lang.getString("sitebooklet", "leaks")%></td>
			</tr></table>
		</td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><%=lang.getString("sitebooklet", "leakage_description")%></td>
	</tr>
	<tr height="80px" valign="top">
		<td colspan="3"><pre><%=leakage_description%></pre></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><%=lang.getString("sitebooklet", "corrective_action")%></td>
	</tr>
	<tr height="80px" valign="top">
		<td colspan="3"><pre><%=corrective_action%></pre></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="3"><%=lang.getString("sitebooklet", "ref_to_restore")%></td>
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "kg")%></td>
		<td><%=ref_to_restore_quantity%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<th colspan="3"><h2><%=lang.getString("sitebooklet", "fluid_level")%></h2>
	</tr>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_sensors" <%=bean.checkLeakageVerificationFlag(flags, SiteBookletBean.LV_level_sensors)%>>&nbsp;<%=lang.getString("sitebooklet", "level_sensors")%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "brand_model")%></td>
		<td><%=brand_model%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "min_max_val")%></td>
		<td><%=min_value%> / <%=max_value%></td>
	</tr>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_indicators" <%=bean.checkLeakageVerificationFlag(flags, SiteBookletBean.LV_level_indicators)%>>&nbsp;<%=lang.getString("sitebooklet", "level_indicators")%></td>
	</tr>
	<tr>
		<td width="5%">&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "level_estimation")%></td>
		<td><%=level_estimation%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "compared_to")%></td>
		<td><%=level_reference%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "company_name")%></td>
		<td width="50%"><%=operator_stamp%></td>
	</tr>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "user_name")%></td>
		<td width="50%"><%=user_name%></td>
	</tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "date")%></td>
		<td><%=date%></td>		
	</tr>
</table>
</div>
<%}%>

<!-- page71: Notices -->
<div style="width:595px;">
<table style="border:1px solid black;border-collapse:collapse;" class="sbtext">
	<tr>
		<th style="border:1px solid black;border-style:inset;" width="20%"><%=lang.getString("sitebooklet", "tech_company")%></th>
		<th style="border:1px solid black;border-style:inset;" width="10%"><%=lang.getString("sitebooklet", "user_name")%></th>
		<th style="border:1px solid black;border-style:inset;" width="*"><%=lang.getString("sitebooklet", "procedure_description")%></th>
		<th style="border:1px solid black;border-style:inset;" width="20%"><%=lang.getString("sitebooklet", "DATE")%></th>
	</tr>
<%
for(int i = 0; bean.aNotices != null && i < bean.aNotices.length; i++) {
	Properties prop = bean.aNotices[i];
	String tech_company						= prop.getProperty("tech_company");
	String user_name						= prop.getProperty("user_name");
	String procedure_description			= prop.getProperty("procedure_description");
	String date								= SiteBookletBean.formatBookletDate(prop.getProperty("date"));
%>
	<tr>
		<td style="border:1px solid black;border-style:inset;"><%=tech_company%></td>
		<td style="border:1px solid black;border-style:inset;"><%=user_name%></td>
		<td style="border:1px solid black;border-style:inset;"><%=procedure_description%></td>
		<td style="border:1px solid black;border-style:inset;"><%=date%></td>
	</tr>
<%}%>
</table>
</div>

</body>
</html>
