<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceBean" %>
<%@ page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceListBean" %>
<%@ page import="com.carel.supervisor.dataaccess.varaggregator.VarAggregator" %>
<%@ page import="com.carel.supervisor.presentation.bo.helper.VarDetailHelper" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	
  // language
	LangService lan = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	// strings
	String devconfcomment5a = lan.getString("devdetail","devconfcomment5");
	String devconfcomment5b = lan.getString("devdetail","devconfcomment5b");
	String readonlyvars = lan.getString("devdetail","rovars");
	String readwritevars = lan.getString("devdetail","rwvars");
	String code = lan.getString("dtlview","col5");
	String description = lan.getString("dtlview", "detaildevicecol3");
	String visibility = lan.getString("devdetail","vis");
	String state = lan.getString("devdetail","stat");
	String probe = lan.getString("devdetail","probe");
	String readonlytab = lan.getString("devdetail","rotab");
	String none = lan.getString("devdetail","none");
	String on = lan.getString("devdetail","on");
	String off = lan.getString("devdetail","off");
	String open = lan.getString("htmlfisa","open");
	String close = lan.getString("htmlfisa","close");
	String moveup = lan.getString("devdetail","moveup");
	String movedown = lan.getString("devdetail","movedown");
	String orderstr = lan.getString("devdetail","sworder");
	String selectstr = lan.getString("devdetail","swsel");
	String maxstate = lan.getString("devdetail","maxstate");
	String maxprobe = lan.getString("devdetail","maxprobe");
	String statedescription = lan.getString("devdetail","statedescr");
	String probedescription = lan.getString("devdetail","probedescr");
	String readonlydescription = lan.getString("devdetail","rodescr");
	String readwritedescription = lan.getString("devdetail","rwdescr");
	String mainvarsaveOK = lan.getString("devdetail","mainvarsaveOK");
	String mainvarsaveKO = lan.getString("devdetail","mainvarsaveKO");

	String mainvarsave = sessionUser.getPropertyAndRemove("varposresult");
 	if(mainvarsave == null)
 		mainvarsave ="nomainvarsave";

	// id device
	int iddev = Integer.parseInt(sessionUser.getProperty("iddev"));
	DeviceBean device= DeviceListBean.retrieveSingleDeviceById(idsite, iddev, sessionUser.getLanguage());
	String descr= device.getDescription();
	boolean islogic=device.islogic();
	
	if(sessionUser.getProperty("ab")==null)
		sessionUser.setProperty("ab","a");
	
	int table_width = sessionUser.getScreenWidth()-VarDetailHelper.OFFSET_WIDTH-150;
	int scrollW = VarDetailHelper.VSCROLLBAR_WIDTH;
%>

<form id="frm_dev_varpos_save" name="frm_dev_varpos_save" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" id="cmd" name="cmd" />
	<input type="hidden" id="ab" name="ab" value="<%=sessionUser.getProperty("ab")%>" />
	<input type="hidden" id="iddev" name="iddev" value="<%=iddev%>" />
	<input type='hidden' id='open' value="<%=open%>" />
	<input type='hidden' id='close' value="<%=close%>" />
	<input type='hidden' id='maxstate' value="<%=maxstate%>" />
	<input type='hidden' id='maxprobe' value="<%=maxprobe%>" />
	<input type='hidden' id='varposorder' name='varposorder' value="" />
	<input type='hidden' id='mainvarsave' name='mainvarsave' value="<%=mainvarsave%>" />
	<input type='hidden' id='mainvarsaveOK' name='mainvarsaveOK' value="<%=mainvarsaveOK%>" />
	<input type='hidden' id='mainvarsaveKO' name='mainvarsaveKO' value="<%=mainvarsaveKO%>" />
	
	<table cellpadding="2" cellspacing="1" width="99%">
		<tr>
			<td width="*"  valign="middle">
				<p class="tdTitleTable"><%=descr%></p>
			</td>
			<td align="right" valign="middle" width="170px" height="30px" class="groupCategory" onclick="<%=sessionUser.getProperty("ab").equals("a")?"dev_varpos_order()":"dev_varpos_select()"%>">
				<%=sessionUser.getProperty("ab").equals("a")?orderstr:selectstr%>
			</td>
		</tr>
	</table>
	<p class="standardTxt"><%=sessionUser.getProperty("ab").equals("a")?devconfcomment5a:devconfcomment5b%></p>
<%
int rowColor=0;
if(sessionUser.getProperty("ab").equals("a"))
{
	// variables list (no alarms)
	VarphyBean[] vars = VarAggregator.retrieveByDescription(idsite, sessionUser.getLanguage(), iddev);	
	
%>
	<table border="0" width="100%" cellspacing="1" cellpadding="2" align="center">
		<tr>
			<td width="3%">
				<table class="table" cellpadding="2" cellspacing="1">
					<tr>
						<th class="th" style="background-repeat:no-repeat;">
							<img id="roimg" src="images/lsw/close.gif" style="cursor:pointer;" title="<%=close%>" onclick="readonlyclick(this);" />
						</th>
					</tr>
				</table>
			</td>
			<td width="*">
				<table class="table" cellpadding="2" cellspacing="1" width="40%">
					<tr>
						<th class="th" style="background-repeat:no-repeat;">
							<%=readonlyvars%>
						</th>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table id="readonlytable" width="100%" align="center">
	<tr>
		<td>
			<div style='width:<%=table_width+scrollW %>'>
				<table width="<%=table_width %>px" cellpadding="0" cellspacing="1" align="left">
					<tr>
						<th class="th" align="center" rowspan="2" width='*'><%=description%></th>
						<th class="th" align="center" rowspan="2" width='118px'><%=code%></th>
						<th class="th" align="center" colspan="4"><%=visibility%></th>
					</tr>
					<tr>
						<th class="th" align="center" width='60px'><div style="">
							<%=state%>
				  			<table><tr><td style="width:30px;height:10px;background:#ffff80;border:1px solid white;"></td></tr></table>
						</div></th>
						<th class="th" align="center" width='60px'><div style="">
							<%=probe%>
				  			<table><tr><td style="width:30px;height:10px;background:#80ff00;border:1px solid white;"></td></tr></table>
						</th>
						<th class="th" align="center" width='60px'>
							<%=readonlytab%>
				  			<table><tr><td style="width:30px;height:10px;background:#80ffff;border:1px solid white;"></td></tr></table>
						</th>
						<th class="th" align="center" width='60px'>
							<%=none%>
						</th>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="width:<%=table_width+scrollW %>px;height:200pt; overflow:auto;">
				<table width="<%=table_width %>px" class="table" cellpadding="0" cellspacing="1" align="left">
	  		<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(vars[i].getReadwrite().trim().equals("1"))
	{	
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"readonlytable\");'>");
			out.println("<td class='td' style='display: none'>");
				out.println(vars[i].getId());
			out.println("</td>");
			out.println("<td class='td' width='*'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='td' width='118px'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
			out.println("<td class='td' align='center' width='60px'>");
				out.println("<input type=\"radio\" id=\"STAT"+vars[i].getId()+"\" name=\"radioro"+vars[i].getId()+"\" value=\"STAT\" "+(vars[i].getDisplay().trim().equals("STAT")?"checked=\"checked\"":"")+" "+(vars[i].getType()==1?"":"disabled=\"disabled\"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
			out.println("</div></td>");
			out.println("<td class='td' align='center' width='60px'>");
				out.println("<input type=\"radio\" id=\"HOME"+vars[i].getId()+"\" name=\"radioro"+vars[i].getId()+"\" value=\"HOME\" "+(vars[i].getDisplay().trim().equals("HOME")?"checked=\"checked\"":"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
			out.println("</td>");
			out.println("<td class='td' align='center' width='60px'>");
				out.println("<input type=\"radio\" id=\"MAIN"+vars[i].getId()+"\" name=\"radioro"+vars[i].getId()+"\" value=\"MAIN\" "+(vars[i].getDisplay().trim().equals("MAIN")?"checked=\"checked\"":"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
			out.println("</td>");
			out.println("<td class='td' align='center' width='60px'>");
				out.println("<input type=\"radio\" id=\"NONE"+vars[i].getId()+"\" name=\"radioro"+vars[i].getId()+"\" value=\"NONE\" "+(vars[i].getDisplay().trim().equals("NONE")?"checked=\"checked\"":"")+" onclick='checkradiooption("+vars[i].getId()+");' />");
			out.println("</td>");
			out.println("<td class='standardTxt' align='center' style='display: none;'>");
				out.println("<input type=\"text\" id=\"type"+vars[i].getId()+"\" value=\""+vars[i].getType()+"\" />");
				out.println("<input type=\"text\" id=\"prev"+vars[i].getId()+"\" value=\""+vars[i].getDisplay().trim()+"\" />");
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
			</tbody>
	  		</table>
	  		</div>
  		</td>
  		<td width="150px" align="center" valign="top">
  			<img src="images/varpos/allcols.JPG" style="border: 1px solid #808080"/>
  		</td> 
  	</tr>
	</table>
	
	<!-- expand button readwrite vars -->
	
	<!-- readwrite vars table-->
  	<br/>
  	<table border="0" width="100%" cellspacing="1" cellpadding="2" align="center">
		<tr>
			<td width="3%">
				<table class="table" cellpadding="2" cellspacing="1">
					<tr>
						<th class="th" style="background-repeat:no-repeat;">
							<img id="rwimg" src="images/lsw/close.gif" style="cursor:pointer;" title="<%=close%>" onclick="readwriteclick(this);">
						</th>
					</tr>
				</table>
			</td>
			<td width="*">
				<table class="table" cellpadding="2" cellspacing="1" width="40%">
					<tr>
						<th class="th" style="background-repeat:no-repeat;">
							<%=readwritevars%>
						</th>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table id="readwritetable" width="100%" align="center">
	<tr>
		<td>
			<div style='width:<%=table_width+scrollW %>'>
				<table width="<%=table_width %>px" cellpadding="0" cellspacing="1" align="left">
					<tr>
						<th class="th" align="center" rowspan="2" width='*'><%=description%></th>
						<th class="th" align="center" rowspan="2" width='118px'><%=code%></th>
						<th class="th" align="center" colspan="2"><%=visibility%></th>
					</tr>
					<tr>
						<th class="th" align="center" width='90px'>
							<%=on%>
					 		<table><tr><td style="width:30px;height:10px;background:#ff80c0;border:1px solid white;"></td></tr></table>
						</th>
						<th class="th" align="center" width='90px'>
							<%=off%>
						</th>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="width:<%=table_width+scrollW %>px;height:200pt; overflow:auto;">
				<table width="<%=table_width %>px" class="table" cellpadding="0" cellspacing="1" align="left" >
		  	<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(!vars[i].getReadwrite().trim().equals("1") && vars[i].getButtonpath()==null)
	{
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"readwritetable\");'>");
			out.println("<td class='td' width='*'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='td' width='118px'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
			out.println("<td class='td' align='center' width='90px'>");
				out.println("<input type=\"radio\" id=\"MAIN"+vars[i].getId()+"\" name=\"radiorw"+vars[i].getId()+"\" value=\"MAIN\" "+(vars[i].getDisplay().trim().equals("MAIN")?"checked=\"checked\"":"")+" />");
			out.println("</td>");
			out.println("<td class='td' align='center' width='90px'>");
				out.println("<input type=\"radio\" id=\"NONE"+vars[i].getId()+"\" name=\"radiorw"+vars[i].getId()+"\" value=\"NONE\" "+(vars[i].getDisplay().trim().equals("NONE")?"checked=\"checked\"":"")+" />");
			out.println("</td>");
			out.println("<td class='standardTxt' align='center' style='display: none;'>");
				out.println("<input type=\"text\" id=\"type"+vars[i].getId()+"\" value=\""+vars[i].getType()+"\" />");
				out.println("<input type=\"text\" id=\"prev"+vars[i].getId()+"\" value=\""+vars[i].getDisplay().trim()+"\" />");
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
			</tbody>
		  </table>
		  </div>
  </td>
  <td width="15%" align="center" valign="top">
  <img src="images/varpos/allcols.JPG" style="border: 1px solid #808080" />
  </td> 
  </tr>
	</table>
  <br/>
<%
}
else if(sessionUser.getProperty("ab").equals("b"))
{
	// variables list (no alarms)
	VarphyBean[] vars = VarAggregator.retrieveByPriority(idsite, sessionUser.getLanguage(), iddev);	
%>

	<!-- state -->
	<div>
	<fieldset class="fldMainVariables">
		<legend class="tdTitleTable" style="font-size: 14px;"><%=statedescription%></legend> 
		<table id="orderstatetable" cellpadding="1" cellspacing="1" width="98%" align="center" >
		<tr style='border: 1px #000000 solid;'>
			<td align="center" valign="top" width="*">
				<table id="orderstatetable2" width="100%" class="table" cellpadding="1" cellspacing="1">
  				<thead>
	  				<tr>
				  		<th class="th" align="center" rowspan="2">
				  			<%=description%>
				  		</th>
				  		<th class="th" width="10%" align="center" rowspan="2">
				  			<%=code%>
				  		</th>
	  				</tr>
  				</thead>
  				<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(vars[i].getReadwrite().trim().equals("1") && vars[i].getDisplay().equalsIgnoreCase("STAT"))
	{
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"orderstatetable\");'>");
			out.println("<td name='varposid' style='display: none;'>");
				out.println(vars[i].getId());
			out.println("</td>");			
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
				</tbody>
				</table>
			</td>
			<td align="center" valign="middle" width="5%">
				<table>
				<tr>
					<td>
						<img src="images/dbllistbox/arrowup_on.png" style="cursor:pointer;" title="<%=moveup%>" onclick="varpos_moveup('orderstatetable2');" />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td> 
						<img src="images/dbllistbox/arrowdown_on.png" style="cursor:pointer;" title="<%=movedown%>" onclick="varpos_movedown('orderstatetable2');" />
					</td>
				</tr>
				</table>
			</td>
			<td align="center" valign="top" width="15%"><img src="images/varpos/state.JPG" style="border: 1px solid #808080"/></td>
		</tr>
		</table>
	</fieldset>  
	</div>
	
	<table><tr><td height="3px">&nbsp;</td></tr></table>
  
  <!-- probe -->
	<div>
		<fieldset class="fldMainVariables">
			<legend class="tdTitleTable" style="font-size: 14px;"><%=probedescription%></legend> 
			<table id="orderprobetable" cellpadding="1" cellspacing="1" width="98%" align="center" >
			<tr>
				<td align="center" valign="top" width="*">
					<table id="orderprobetable2" width="100%" class="table" cellpadding="1" cellspacing="1">
  					<thead>
  					<tr>
  						<th class="th" align="center" rowspan="2">
  							<%=description%>
  						</th>
				  		<th class="th" width="10%" align="center" rowspan="2">
				  			<%=code%>
				  		</th>
  					</tr>
  					</thead>
  					<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(vars[i].getReadwrite().trim().equals("1")&&vars[i].getDisplay().equalsIgnoreCase("HOME"))
	{
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"orderprobetable\");'>");
			out.println("<td name='varposid' style='display: none;'>");
				out.println(vars[i].getId());
			out.println("</td>");			
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
					</tbody>
  					</table>
				</td>
				<td align="center" valign="middle" width="5%">
					<table>
					<tr>
						<td>
							<img src="images/dbllistbox/arrowup_on.png" style="cursor:pointer;" title="<%=moveup%>" onclick="varpos_moveup('orderprobetable2');" />
						</td>
					</tr>
					<tr>
						<td></td>
					</tr>
					<tr>
						<td> 
							<img src="images/dbllistbox/arrowdown_on.png" style="cursor:pointer;" title="<%=movedown%>" onclick="varpos_movedown('orderprobetable2');" />
						</td>
					</tr>
					</table>
				</td>
				<td align="center" valign="top" width="15%">
					<img src="images/varpos/probe.JPG" style="border: 1px solid #808080"/>
				</td>
			</tr>
  			</table>
  		</fieldset>
	</div>
	
	<table><tr><td height="3px">&nbsp;</td></tr></table>
  
	<!-- ro -->
	<div>
		<fieldset class="fldMainVariables">
			<legend class="tdTitleTable" style="font-size: 14px;"><%=readonlydescription%></legend> 
			<table id="orderrotable" cellpadding="1" cellspacing="1" width="98%" align="center" >
			<tr>
				<td align="center" valign="top" width="*">
					<table id="orderrotable2" width="100%" class="table" cellpadding="1" cellspacing="1">
  					<thead>
					  	<tr>
					  		<th class="th" align="center" rowspan="2">
					  			<%=description%>
					  		</th>
					  		<th class="th" width="10%" align="center" rowspan="2">
					  			<%=code%>
					  		</th>
					  	</tr>
					</thead>
  					<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(vars[i].getReadwrite().trim().equals("1")&&vars[i].getDisplay().equalsIgnoreCase("MAIN"))
	{
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"orderrotable\");'>");
			out.println("<td name='varposid' style='display: none;'>");
				out.println(vars[i].getId());
			out.println("</td>");			
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
					</tbody>
  					</table>
				</td>
				<td align="center" valign="middle" width="5%">
					<table>
					<tr>
						<td>
							<img src="images/dbllistbox/arrowup_on.png" style="cursor:pointer;" title="<%=moveup%>" onclick="varpos_moveup('orderrotable2');" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
					</tr>
					<tr>
						<td>
							<img src="images/dbllistbox/arrowdown_on.png" style="cursor:pointer;" title="<%=movedown%>" onclick="varpos_movedown('orderrotable2');" />
						</td>
					</tr>
					</table>
				</td>
				<td align="center" valign="top" width="15%">
					<img src="images/varpos/mainro.JPG" style="border: 1px solid #808080"/>
				</td>
			</tr>
  			</table>
  		</fieldset>
	</div>
	
	<table><tr><td height="3px">&nbsp;</td></tr></table>
  
	<!-- rw -->
	<div>
		<fieldset class="fldMainVariables">
			<legend class="tdTitleTable" style="font-size: 14px;"><%=readwritedescription%></legend> 
			<table id="orderrwtable" cellpadding="1" cellspacing="1" width="98%" align="center" >
			<tr>
				<td align="center" valign="top" width="*">
				<table id="orderrwtable2" width="100%" class="table" cellpadding="1" cellspacing="1">
			  	<thead>
			  	<tr>
			  		<th class="th" align="center" rowspan="2">
			  			<%=description%>
			  		</th>
			  		<th class="th" width="10%" align="center" rowspan="2">
			  			<%=code%>
			  		</th>
			  	</tr>
			  	</thead>
			  	<tbody>
<%
for(int i = 0; i<vars.length ;i++)
{
	if(!vars[i].getReadwrite().trim().equals("1")&&vars[i].getDisplay().equalsIgnoreCase("MAIN") && vars[i].getButtonpath()==null)
	{
		rowColor++;
		out.println("<tr class='"+(rowColor%2==0?"Row1":"Row2")+"' style='cursor:pointer' onclick='varpos_rowsel(this,\"orderrwtable\");'>");
			out.println("<td name='varposid' style='display: none;'>");
				out.println(vars[i].getId());
			out.println("</td>");			
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDescription());
			out.println("</td>");
			out.println("<td class='standardTxt'>");
				out.println(vars[i].getShortDesc());
			out.println("</td>");
		out.println("</tr>");
	}
}
%>
				</tbody>
				</table>
			</td>
			<td align="center" valign="middle" width="5%">
				<table>
				<tr>
					<td>
						<img src="images/dbllistbox/arrowup_on.png" style="cursor:pointer;" title="<%=moveup%>" onclick="varpos_moveup('orderrwtable2');" />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td> 
						<img src="images/dbllistbox/arrowdown_on.png" style="cursor:pointer;" title="<%=movedown%>" onclick="varpos_movedown('orderrwtable2');" />
					</td>
				</tr>
				</table>
			</td>
			<td align="center" valign="top" width="15%">
				<img src="images/varpos/mainrw.JPG" style="border: 1px solid #808080"/>
			</td>
		</tr>
  		</table>
  	</fieldset>
	</div>
	<br/>
<%
}
%>

</form>