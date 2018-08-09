<%@ page language="java"  pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bo.BAlrSched" 
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.timebands.TimeBands"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService langService = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	
	String noRemove=trxUserLoc.remProperty("notremovetimeband");
	
	String arg = sessionUser.getPropertyAndRemove("arg");
	if(arg != null && arg.equals("") == false)
	{
		arg = arg.replaceAll("1asjksdf933893hyfhlsa9339089sidfjh"," ");
		arg = arg.replaceAll("1654sdfgklasdkaiekj9393938ij464646","+");
	}
	//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";
		
	boolean permict = (permission==0)?false:true;

	String dom = langService.getString("cal","sun");
	String lun = langService.getString("cal","mon");
	String ma = langService.getString("cal","tue");
	String mer = langService.getString("cal","wed");
	String gio = langService.getString("cal","thu");
	String ven = langService.getString("cal","fri");
	String sab = langService.getString("cal","sat");

	String gen = langService.getString("cal","january");
	String feb = langService.getString("cal","february");
	String mar = langService.getString("cal","march");
	String apr = langService.getString("cal","april");
	String mag = langService.getString("cal","may");
	String giu = langService.getString("cal","june");
	String lug = langService.getString("cal","july");
	String ago = langService.getString("cal","august");
	String set = langService.getString("cal","september");
	String ott = langService.getString("cal","october");
	String nov = langService.getString("cal","november");
	String dic = langService.getString("cal","december");
		
	
	
	String value=langService.getString("timebandsTables","value");

	String timebands=langService.getString("timebandsPage","timebands");
	String type=langService.getString("timebandsPage","type");
	String desc=langService.getString("timebandsPage","desc");

	
	String daily=langService.getString("timebandsPage","daily");
	String weekly=langService.getString("timebandsPage","weekly");
	String monthly=langService.getString("timebandsPage","monthly");
	String yearly=langService.getString("timebandsPage","yearly");
	String special=langService.getString("timebandsPage","special");
	
	
	String monday=langService.getString("timebandsPage","monday");
	String tuesday=langService.getString("timebandsPage","tuesday");
	String wednesday=langService.getString("timebandsPage","wednesday");
	String thursday=langService.getString("timebandsPage","thursday");
	String friday=langService.getString("timebandsPage","friday");
	String saturday=langService.getString("timebandsPage","saturday");
	String sunday=langService.getString("timebandsPage","sunday");
	String from=langService.getString("timebandsPage","from");
	String to=langService.getString("timebandsPage","to");
	
	
	
	String descMsg=langService.getString("timebandsMsg","descmsg");
	String dateMsg=langService.getString("timebandsMsg","datemsg");
	String daysMsg=langService.getString("timebandsMsg","daysmsg");
	String noRemoveMsg=langService.getString("timebandsMsg","noremovemsg");
	String removeDefaultForbid=langService.getString("timebandsMsg","removeDefaultForbid");	
	String ideMsg=langService.getString("timebandsMsg","ideMsg");	
	
	String noAddMsg = langService.getString("timetable","duplicatevalue");
	String noModMsg = langService.getString("timetable","isremovable");
	
	String tableTextMsg=langService.getString("timebandsMsg","tabletextmsg");	
	String daySelectMsg=langService.getString("timebandsMsg","dayselectmsg");	
	
	String rowDuplicateMsg=langService.getString("timebandsMsg","rowduplicatemsg");	
	String timeBandsOverLappedMsg=langService.getString("timebandsMsg","timebandsoverlappedmsg");	
	
	String confirmtimebdel = langService.getString("alrsched","confirmtimebdel");
	
	
	
	

	StringBuffer select=new StringBuffer("\n<select "+perm+" name=\"type\" id=\"type\" value=\"1\" onchange=\"changePseudoFrame(); \">");
	select.append("\n<option value=\"1\">"+daily+"</option>");
	select.append("\n<option value=\"2\">"+weekly+"</option>");
	select.append("\n<option value=\"3\">"+monthly+"</option>");
	select.append("\n<option value=\"5\">"+yearly+"</option>");
	select.append("\n<option value=\"4\">"+special+"</option>");
	select.append("</select>");
	
	StringBuffer bodySelectHoursFrom= new StringBuffer();
	for(int i=0;i<24;i++){
		if(i<10)
			bodySelectHoursFrom.append("\n<option value=\"0"+i+"\">0"+i+"</option>");
		else
			bodySelectHoursFrom.append("\n<option value=\""+i+"\">"+i+"</option>");
	}//for
	
	StringBuffer bodySelectHoursTo= new StringBuffer();
	for(int i=0;i<25;i++){
		if(i<10)
			bodySelectHoursTo.append("\n<option value=\"0"+i+"\">0"+i+"</option>");
		else
			bodySelectHoursTo.append("\n<option value=\""+i+"\">"+i+"</option>");
	}//for
	
	StringBuffer bodySelectMinuts= new StringBuffer();
	bodySelectMinuts.append("\n<option value=\"00\">00</option>");
	for(int i=1;i<6;i++){
	bodySelectMinuts.append("\n<option value=\""+i*10+"\">"+i*10+"</option>");	
		
	}
	StringBuffer month31Table=new StringBuffer();
	month31Table.append("<div class=\"standardTxt\">");
	for(int i=0;i<31;i++){
		if(i%7==0)
					month31Table.append("\n<br>");		
		month31Table.append("\n<div style=\"float:left;cursor:pointer;width:22px;background-color:#EFF1FE\" id=\"day");
	
		month31Table.append(i+1);
		month31Table.append("\" onclick=\" dayClick(");
		month31Table.append((i+1)<10?("0"+(i+1)):""+(i+1));
		month31Table.append(") \">");
		month31Table.append((i+1)<10?("0"+(i+1)):""+(i+1));
		month31Table.append("</div>");		
	}//for

	month31Table.append("</div>");
	
	StringBuffer yearTable=new StringBuffer();

	TimeBands timeBands=new TimeBands();

	timeBands.setScreenH(sessionUser.getScreenHeight());
	timeBands.setScreenW(sessionUser.getScreenWidth());
	
	timeBands.loadData(sessionUser);
	timeBands.setWidth(900);
	timeBands.setHeight(143);
	String htmlTimeBands = timeBands.getHTMLTimeBandsTable("userTable",language, permict);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
%>
<script language='javascript'>
var arg = '<%=arg%>';
</script>
<input type='hidden' id='confirmtimebdel' value='<%=confirmtimebdel%>'/>
<input type="hidden" id="duplicatecodemsg" value="<%=langService.getString("alrsched","msgduplicate")%>">

<div  style="display:none;float:right" id="monthNumber" class="standardTxt">1</div>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<table border="0" width="98%" height="70%" cellspacing="1" cellpadding="1">
		<tr height="100%" valign="top" id="trEvnCondList">
		  <td><%=htmlTimeBands%></td>
		</tr>
		<tr height="10px">
		  <td/>
		</tr>
		<tr>
				<td>
						<form name="frm_timebands"  id="frm_timebands" action="servlet/master;jsessionid=<%=jsession%>" method="post">
						
								<fieldset class="field">
										<legend class="standardTxt"><%=timebands%></legend>
										<table width="100%"  height="100%" cellspacing="1" cellpadding="1" >
												<tr height="100%">
														<td width="25%"  valign="top">
																<table  class="standardTxt" width="100%">
																		<tr>
																				<td height="5px">&nbsp;
																				</td>
																		</tr>
																		<tr>
																				<td valign="top" class="standardTxt"><%=desc%><br><br>
																						<input <%=perm%> class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text"  name="desc" id="desc" maxlength="30" size="30" onblur="noBadCharOnBlur(this,event);" onkeydown='checkBadChar(this,event);'/> *<br><br>
																						<%=type%><br><br><%=select.toString()%><br><br><br><br><br><br>
																				</td>
																		</tr>
																</table>
														</td>
														<td valign="top">&nbsp;<div id="pseudo_frame"></div>
														</td>
											</tr>
								</table>
							
					</fieldset>
								<input type="hidden" id="action" name="action" value="" />
								<input type="hidden" id="idTimeBands" name="idTimeBands" value="-1" />
								<input type="hidden" id="timeBandValue" name="timeBandValue" value="" />
			</form>
			</td>
		</tr>
</table>
<div style="display:none;">

<!--DAILY START-->
<div id="1" >
		<table width="100%" height="100%" border="0">
				<tr>
					<td width="30%" > 
								<table>
										<tr>
												<td class="standardTxt"><%=from%>
												</td>
												<td>
														<select <%=perm%> id="hour_from"  ><%=bodySelectHoursFrom.toString()%>
														</select>:<select <%=perm%> id="minut_from" ><%=bodySelectMinuts.toString()%>
														</select> 
												</td>
									</tr>
									<tr>
											<td class="standardTxt"><%=to%>
											</td>
											<td>
													<select <%=perm%> id="hour_to" onchange="control2425()"><%=bodySelectHoursTo.toString()%>
													</select>:<select <%=perm%> id="minut_to" ><%=bodySelectMinuts.toString()%>
													</select> 
											</td>
								</tr>
						</table>
				</td>
		<td width="10%" valign="middle" align="center">
			<div>
				<img <%=(permict)?"onclick=\"arrowAdd('1');return false;\"":""%> src="images/dbllistbox/arrowdx_on.png" align="middle" /><p/><img <%=(permict)?"onclick=\"deleteRow('1');return false;\"":""%> src="images/dbllistbox/delete_on.png" align="middle" />
			</div>
		</td>
		<td valign="top" width="70%"> 
				<div class="timeBandsHeader">
						<div> 
								<table class='table' width="100%" cellspacing="1" cellpadding="1">
										<tbody>
										<tr class="th"> 
												<td class="tdmini" width="20%">#</td>
												<td class="tdmini" width="80%"><%=value%></td>
										</tr>
										</tbody>
								</table>
						</div>
				
						<div id="divTable1" class="timeBandsBody"> 
								<table>
									<tbody>
									</tbody>
								</table>
						</div>
				</div>
		</td>
</tr>
</table>

</div>	



<!--WEEKLY START-->
<div id="2" class="standardTxt">
<table width="100%" height="100%" >
			<tr height="100%">
			<td width="30%" > 
					<br>
					<div  class="standardTxt" style="float:left">
							<input <%=perm%> type="checkbox" id="monday" name="monday"><%=monday%>
							<input <%=perm%> type="checkbox" id="tuesday" name="tuesday"><%=tuesday%>
							<input <%=perm%> type="checkbox" id="wednesday" name="wednesday"><%=wednesday%>
								<input <%=perm%> type="checkbox" id="thursday" name="thursday"><%=thursday%>
						<br>
						
								<input <%=perm%> type="checkbox" id="friday" name="friday"><%=friday%>
								<input <%=perm%> type="checkbox" id="saturday" name="saturday"><%=saturday%>
								<input <%=perm%> type="checkbox" id="sunday" name="sunday"><%=sunday%>
						</div>
						<br>
						<br>
						<br>
						<div style="float:left" class="standardTxt" >
			
			<table>
				<tr>
					<td class="standardTxt"><%=from%></td>
					<td><select <%=perm%> id="hour_from" ><%=bodySelectHoursFrom.toString()%>
							</select>:<select <%=perm%> id="minut_from" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
				<tr>
				<td class="standardTxt"><%=to%></td>
					<td><select <%=perm%> id="hour_to" onchange="control2425()" ><%=bodySelectHoursTo.toString()%>
							</select>:<select <%=perm%> id="minut_to" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
	
				</table>
				
			</div>
		</td>		
		<td width="10%" valign="middle" align="center">
			<div>
				<img onclick="arrowAdd('2');return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" /><p/>
				<img  onclick="deleteRow('2');return false;" src="images/dbllistbox/delete_on.png" align="middle" />
			</div>
		</td>

		<td valign="top" width="70%">
			<div class="timeBandsHeader">		
				<div> 
					<table class="table" width="100%" cellspacing="1" cellpadding="1">
							<tr class="th"> 
								<td class="tdmini" width="10%" >#</td>
								<td class="tdmini" width="34%"><%=value%></td>
								<td class="tdmini" width="8%"><%=monday%></td>
								<td class="tdmini" width="8%"><%=tuesday%></td>
								<td class="tdmini" width="8%"><%=wednesday%></td>
								<td class="tdmini" width="8%"><%=thursday%></td>
								<td class="tdmini" width="8%"><%=friday%></td>
								<td class="tdmini" width="8%"><%=saturday%></td>
								<td class="tdmini" width="8%"><%=sunday%></td>
						</tr>
					</table>
				</div>
				<div id="divTable2" class="timeBandsBody">
					<table width="100%" cellspacing="1" cellpadding="1">
						<tbody></tbody>
					</table>
				</div>
			</div>
					
	</td>
	</tr>
	</table>

</div>	

<!--MONTHLY START-->
<div id="3">

	<table width="100%" height="100%" >
	<tr>
	
		<td width="30%"> 
			<div style="float:left" class="standardTxt" >
			<table>
				<tr>
		          	<td colspan="2" id="cal_month"></td>
		        </tr>
		        <tr>
					<td class="standardTxt"><%=from%></td>
					<td><select <%=perm%> id="hour_from" ><%=bodySelectHoursFrom.toString()%>
							</select>:<select <%=perm%> id="minut_from" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
				<tr>
				<td class="standardTxt"><%=to%></td>
					<td><select <%=perm%> id="hour_to" onchange="control2425()" ><%=bodySelectHoursTo.toString()%>
							</select>:<select id="minut_to" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
			</table>
				
			</div>
		
		
		</td>
		<td width="10%" valign="middle" align="center">
			<div>
				<img onclick="arrowAdd('3');return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" /><p/>
				<img  onclick="deleteRow('3');return false;" src="images/dbllistbox/delete_on.png" align="middle" />
			</div>
		</td>
		<td valign="top" width="70%"> 
				<div class="timeBandsHeader">
						<div> 
								<table class='table' width="100%" cellspacing="1" cellpadding="1">
										<tbody>
										<tr class="th"> 
												<td class="tdmini" width="20%">#</td>
												<td class="tdmini" width="80%"><%=value%></td>
										</tr>
										</tbody>
								</table>
						</div>
				
						<div id="divTable3" class="timeBandsBody"> 
								<table width="100%" cellspacing="1" cellpadding="1">
									<tbody>
									</tbody>
								</table>
						</div>
				</div>
</td>
</tr>
</table>

</div>	


<!--YEARLY START-->
<div id="5">

	<table width="100%" height="100%" >
	<tr>

		<td width="30%" valign="top" > 
			<div style="float:left" class="standardTxt" >
			<table>
				<tr>
		          	<td colspan="2" id="cal_year"></td>
		        </tr>
		        <tr>
					<td class="standardTxt"><%=from%></td>
					<td><select <%=perm%> id="hour_from" ><%=bodySelectHoursFrom.toString()%>
							</select>:<select <%=perm%> id="minut_from" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
				<tr>
				<td class="standardTxt"><%=to%></td>
					<td><select <%=perm%> id="hour_to" onchange="control2425()" ><%=bodySelectHoursTo.toString()%>
							</select>:<select <%=perm%> id="minut_to" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
			</table>
				
			</div>
		
		
		</td>
		<td width="10%" valign="middle" align="center">
			<div>
				<img onclick="arrowAdd('5');return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" /><p/>
				<img  onclick="deleteRow('5');return false;" src="images/dbllistbox/delete_on.png" align="middle" />
			</div>
		</td>		
		<td valign="top" width="70%"> 
				<div class="timeBandsHeader">
						<div> 
								<table class='table' width="100%" cellspacing="1" cellpadding="1">
										<tbody>
										<tr class="th"> 
												<td class="tdmini" width="20%">#</td>
												<td class="tdmini" width="80%"><%=value%></td>
										</tr>
										</tbody>
								</table>
						</div>
				
						<div id="divTable5" class="timeBandsBody"> 
								<table width="100%" cellspacing="1" cellpadding="1">
									<tbody>
									</tbody>
								</table>
						</div>
				</div>
		</td>
</tr>
</table>

</div>	


<!--SPECIAL START-->


<div id="4">

	<table width="100%" height="100%">	
	<tr>

		<td width="30%" valign="top" > 
			<div style="float:left" class="standardTxt" >
			<table>
				<tr>
		          	<td colspan="2" id="cal_spec"></td>
		        </tr>
		        <tr>
					<td class="standardTxt"><%=from%></td>
					<td><select <%=perm%> id="hour_from" ><%=bodySelectHoursFrom.toString()%>
							</select>:<select <%=perm%> id="minut_from" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
				<tr>
				<td class="standardTxt"><%=to%></td>
					<td><select <%=perm%> id="hour_to" onchange="control2425()" ><%=bodySelectHoursTo.toString()%>
							</select>:<select <%=perm%> id="minut_to" ><%=bodySelectMinuts.toString()%>
							</select> 
					</td>
				</tr>
			</table>
				
			</div>
		
		
		</td>
		<td width="10%" valign="middle" align="center">
			<div>
				<img onclick="arrowAdd('4');return false;" src="images/dbllistbox/arrowdx_on.png" align="center" /><p/>
				<img  onclick="deleteRow('4');return false;" src="images/dbllistbox/delete_on.png" align="center" />
			</div>
		</td>
		<td valign="top" width="70%"> 
				<div class="timeBandsHeader">
						<div> 
								<table class='table' width="100%" cellspacing="1" cellpadding="1">
										<tbody>
										<tr class="th"> 
												<td class="tdmini" width="20%">#</td>
												<td class="tdmini" width="80%"><%=value%></td>
										</tr>
										</tbody>
								</table>
						</div>
				
						<div id="divTable4" class="timeBandsBody"> 
								<table width="100%" cellspacing="1" cellpadding="1">
									<tbody>
									</tbody>
								</table>
						</div>
				</div>
			</td>
</tr>
</table>

</div>

<div id="cal_div">
<div id="caldar">
	<input type="hidden" name="tester" id="tester" value=""/>
	<input class="standardTxt" disabled type="text" name="tester_year" id="tester_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
	<select class="standardTxt" disabled name="tester_month" id="tester_month">
		<option value="1"><%=gen%></option>
		<option value="2"><%=feb%></option>
		<option value="3"><%=mar%></option>
		<option value="4"><%=apr%></option>
		<option value="5"><%=mag%></option>
		<option value="6"><%=giu%></option>
		<option value="7"><%=lug%></option>
		<option value="8"><%=ago%></option>
		<option value="9"><%=set%></option>
		<option value="10"><%=ott%></option>
		<option value="11"><%=nov%></option>
		<option value="12"><%=dic%></option>
	</select>
	<input class="standardTxt" disabled type="text" name="tester_day" id="tester_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
	<p></p>
	<div id="cal_tester_display"></div>
	<script type="text/javascript">
		var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
		var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
		cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
		renderCalendar(cal1);
	</script>
</div>
</div>
<div id="month1"><%=gen%></div>
<div id="month2"><%=feb%></div>
<div id="month3"><%=mar%></div>
<div id="month4"><%=apr%></div>
<div id="month5"><%=mag%></div>
<div id="month6"><%=giu%></div>
<div id="month7"><%=lug%></div>
<div id="month8"><%=ago%></div>
<div id="month9"><%=ago%></div>
<div id="month10"><%=ott%></div>
<div id="month11"><%=nov%></div>
<div id="month12"><%=dic%></div>

<div id="descMsg"><%=descMsg%></div>
<div id="dateMsg"><%=dateMsg%></div>
<div id="daysMsg"><%=daysMsg%></div>

<div id="noRemoveMsg"><%=noRemoveMsg%></div>
<div id="removeDefaultForbid"><%=removeDefaultForbid%></div>
<div id="ideMsg"><%=ideMsg%></div>
<div id="noAddMsg"><%=noAddMsg%></div>
<div id="noModMsg"><%=noModMsg%></div>
<div id="noRemove"><%=noRemove%></div>

<div id="tableTextMsg"><%=tableTextMsg%></div>
<div id="daySelectMsg"><%=daySelectMsg%></div>
<div id="rowDuplicateMsg"><%=rowDuplicateMsg%></div>
<div id="timeBandsOverLappedMsg"><%=timeBandsOverLappedMsg%></div>


</div>
 