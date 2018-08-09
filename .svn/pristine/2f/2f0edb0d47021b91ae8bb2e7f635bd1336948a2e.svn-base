<%@ page language="java" 
  import="java.util.*" 
  import="com.carel.supervisor.presentation.session.*"
  import="com.carel.supervisor.presentation.helper.*"
  import="com.carel.supervisor.dataaccess.language.*"
  import="com.carel.supervisor.presentation.bean.*"
  import="com.carel.supervisor.dataaccess.dataconfig.*"
  import="com.carel.supervisor.presentation.fs.*"
%>

<%@page import="com.carel.supervisor.plugin.base.Plugin"%>

<%
  UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
  UserTransaction ut = sessionUser.getCurrentUserTransaction();
  boolean isProtected = ut.isTabProtected();
  String jsession = request.getSession().getId();
  String language = sessionUser.getLanguage();
  LangService lan = LangMgr.getInstance().getLangService(language);
  FSUtilAux[] utils = FSUtilBean.retrieveUtils(language);
  /*
  int screen_width = sessionUser.getScreenWidth();
  int screen_height = sessionUser.getScreenHeight();
  String html = FSUtilBean.getHTMLTable(utils,language);
  */
  String msgempty = lan.getString("fs","noutilavailable"); 
  boolean onlympxproForSmoothLine = ut.removeAttribute("onlympxpro")==null?true:false;
%>

<!--<div class="standardTxt"><%=lan.getString("fs","comment2") %> </div>-->
<%
if(utils!=null && utils.length>0)
{
%>
<input type='hidden' id='maxnumutil' value='<%=lan.getString("fs","maxnumutil")%>'/>


<%=FSUtilBean.getHTMLRackTable(sessionUser)%>
<p>
<FORM name="frm_util" id="frm_util" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table>
    <tr valign="middle">
      <td class="standardTxt"><%=lan.getString("fs", "rack_selected")%>&nbsp;
      <input id="rack_name" name="rack_name" type="text" readonly size="70" maxlength="100" class="standardTxt"></td>
      <td width="15px">&nbsp;</td>
      <td class="standardTxt"><%=lan.getString("fs", "new_alg")%>&nbsp;<input id="new_alg" name="new_alg" type="checkbox"></td>
  </tr>
</table>
<input type='hidden' id='idRackSelected' name='idRackSelected'/>
<input type='hidden' id='ids_util' name='ids_util'/>
</FORM>
<fieldset class='field' style="height:70%">     
<legend class='standardTxt'><%=lan.getString("fs","tab2name")%></legend>
<table width="100%" height="95%">
 <tr>
  <td><%=FSUtilBean.getHTMLRackUtilities(sessionUser)%></td>
 </tr>
</table>
</fieldset>
</p>
<input type='hidden' id='max_util' value='<%=FSUtilBean.MAX_UTILS %>'/>
<%
}
else
{
%>
<p class="mediumTxt" id="noracks"><b><%=msgempty%></B></p>
<%}%>
<script>
var onlympxproForSmoothLine = <%=onlympxproForSmoothLine%>;
if(!onlympxproForSmoothLine)
{
	alert('<%=lan.getString("fs","onlympxprosmoothline")%>');
}
</script>