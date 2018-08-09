var timer_counter = 0;
var dPage="485";
function getHeadW(idx){
	var tab=document.getElementById("sTab");
	var w=tab.rows[0].cells[idx].childNodes;
	if(w.length == 1)
		return parseInt(w[0].style.width);
	else
	{
		for(var i=0;i<w.length;i++)
		{
			if(w[i].className != "hideicon")
			{
				return parseInt(w[i].style.width);
			}
		}
	}
	return parseInt(w[0].style.width);
}
function getHeadV(idx){
	var tab=document.getElementById("sTab");
	var v=tab.rows[0].cells[idx].style.display;
	return v; 
}
function tab1_init(){
	var isDebugSessionOn = document.getElementById("isDebugSessionOn").value;
	if (isDebugSessionOn == "false") {
		updateButton(false);
	} else {
		updateButton(true);
	}
	var param = "";
	param += "cmd=subtab1_init";
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab1_init, true);
}
function tab2_init(){
	var isDebugSessionOn = document.getElementById("isDebugSessionOn").value;
	if (isDebugSessionOn == "false") {
		updateButton(false);
	} else {
		updateButton(true);
	}
	var param = "";
	param += "cmd=subtab2_init";
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab1_init, true);
}
function updateButton(ison){
	if(ison == true){
		disableAction(1);
		enableAction(2);
		enableAction(3);
	}else{
		enableAction(1);
		disableAction(2);
		enableAction(3);
	}
	enableAction(4);
}

function newCell(ids,width,display,num){
	var c = document.createElement("td");
	c.className = "td";
	if(width!=0){
		c.style.width=width;
	}
	c.id=ids;
	if(ids == "")
		c.className = "td nopadding";
	c.hint=num;
	// bad rendering on Firefox
	c.style.display=display;
	c.align="left";
	c.vAlign="middle";
	return c;
}
function generateSortTable(headtab,datatab){
	var w=28*50+170*2+5;
	var h=document.body.clientHeight-100;
	var pa=headtab.parentNode;
	var framDIV=newDIV("LWborder0","relative","#ffffff",w,h,0,0,false,false,true);
	var headDIV=newDIV("LWCtHead0","relative","#ffffff",w-15,0,0,0,false,false,true);
	var dataDIV=newDIV("LWCtDataName0","relative","#cacaca",w,h-55,0,0,false,false,false);
	pa.appendChild(framDIV);
	framDIV.appendChild(headDIV);
	framDIV.appendChild(dataDIV);
	headDIV.appendChild(headtab);
	dataDIV.appendChild(datatab);
	addEvent(dataDIV,"scroll",lvscroll);
	var $hideicon = $(".hideicon");
	if($hideicon.length>0)
		$hideicon.click(function(event){
			var str = $(this).attr("columns");
			var strHead = "",strData="";
			var columns = str.split(",");
			if(columns.length>0)
			{
				var showColumn = columns[0]-1;
				strHead += "td.th:eq("+showColumn+")";
				strData += "td:eq("+showColumn+")";
				$(strHead+" > div.showicon",$("tr")).html("");
				$(strHead,$("tr")).show();
				$(strData,$("#dataTab tr")).show();
				$(strHead,$("tr")).attr("class","th nopadding");
				strHead = "";
				strData="";
				for(var i=0;i<columns.length;i++)
			    {
					if(i>0)
					{
						strHead += ",";
						strData += ",";
					}
					strHead += "td.th:eq("+columns[i]+")";
					strData += "td:eq("+columns[i]+")";
			    }
				 $(strHead,$("tr")).hide();
	             $(strData,$("#dataTab tr")).hide();
			}
			event.stopPropagation();
			event.preventDefault();
		});
	var $showicon = $("div.showicon");
	if($showicon.length>0)
		$showicon.click(function(event){
			var str = $(this).attr("columns");
			var strHead = "",strData="";
			var columns = str.split(",");
			if(columns.length>0)
			{
				var showColumn = columns[0]-1;
				strHead += "td.th:eq("+showColumn+")";
				strData += "td:eq("+showColumn+")";
				$(strHead,$("tr")).hide();
				$(strData,$("#dataTab tr")).hide();
				strHead = "";
				strData="";
				for(var i=0;i<columns.length;i++)
			    {
					if(i>0)
					{
						strHead += ",";
						strData += ",";
					}
					strHead += "td.th:eq("+columns[i]+")";
					strData += "td:eq("+columns[i]+")";
			    }
				 $(strHead,$("tr")).show();
	             $(strData,$("#dataTab tr")).show();
			}
			event.stopPropagation();
			event.preventDefault();
		});
}
function newDIV(idx,posi,backcolor,w,h,t,l,s_x,s_y,overf){
	var div=document.createElement("div");
	div.id=idx;
	div.align="left";
	with(div.style){
		position=posi;
        if(l>0) left=l;  
        if(t>0) top=t;
        if(w>0) width=w;   
        if(h>0) height=h;
        backgroundColor=backcolor; 
        if(s_x==true) overflowX="scroll";
        if(s_y==true) overflowY="scroll";
        if(overf==true) overflow="hidden";
        else  overflow="auto";
    }  
    return div;
}
function Callback_tab1_init(xml){
	if((xml.getElementsByTagName("starttime")!=null) && (xml.getElementsByTagName("starttime")[0].childNodes[0].nodeValue!="")){
		document.getElementById("debugTime").innerHTML=document.getElementById("debugstarttime").value+":<br>"+xml.getElementsByTagName("starttime")[0].childNodes[0].nodeValue;
	}else{
		document.getElementById("debugTime").innerHTML="";
	}
	if(document.getElementById("subtab").value==dPage)
	{
		var avg_t = document.getElementById("avg_polling_time");
		createAvgPollingTimeTable(xml,avg_t);
	}
	var tab=createTable(xml);
	generateSortTable(document.getElementById("sTab"),tab);
	document.getElementById("sTab").style.display="table-cell";
	updateDebugSessionInfo(xml);
	MTstopServerComm();
	getValueTimer();
	timer_counter = 1;
//	Callback_getValueTimer(xml);
}
function createAvgPollingTimeTable(xml,table)
{
	document.getElementById("avg_title").innerHTML = "<b>"+document.getElementById("avgtime").value+"</b>";
	var avg_t =xml.getElementsByTagName("avg_t");
	var row1 = table.insertRow(table.rows.length);
	var row2 = table.insertRow(table.rows.length);
	for(var i=0;i<avg_t.length;i++)
	{
		var c = row1.insertCell(-1);
		c.style.width = "50px";
		c.className = "th";
		c.style.textAlign = "center";
		c.innerHTML = "Line"+ avg_t[i].childNodes[0].nodeValue;
		var c_v = row2.insertCell(-1);
		c_v.id = "avg_v"+avg_t[i].childNodes[0].nodeValue;
		c_v.className = "standardTxt";
		c_v.style.textAlign = "center";
		c_v.innerHTML = avg_t[i].childNodes[1].nodeValue;
	}
}
function createTable(xml){
	var devs=xml.getElementsByTagName("dev");
	var tab=document.createElement("table");
	tab.className="table";
	tab.id="dataTab";
	tab.setAttribute("cellSpacing", "1");
	tab.setAttribute("cellPadding", "1"); 
	var tbody=document.createElement("tbody");
	tab.appendChild(tbody);
	var headTab=document.getElementById("sTab");
	var mode=xml.getElementsByTagName("mode")[0].childNodes[0].nodeValue;
	for(var i=0;i<devs.length;i++){
		row = document.createElement("tr");
		row.className = (i%2==0)?"Row2":"Row1";
		tbody.appendChild(row);
		var devid=devs[i].getElementsByTagName("devid")[0].childNodes[0].nodeValue;
		var chk=devs[i].getElementsByTagName("chk")[0].childNodes[0].nodeValue;
		if(chk=="1") 
			chk=" checked='checked' "; 
		else
			chk="";
		var mycheckbox = "";
		if(document.getElementById("subtab").value==dPage){
			mycheckbox = "<input type=\"checkbox\"  name=\"devchk\" "+chk+" value=\""+devs[i].getElementsByTagName("devid")[0].childNodes[0].nodeValue+"\"> ";
		}
		var temp = ""+mycheckbox+devs[i].getElementsByTagName("serialid")[0].childNodes[0].nodeValue+"";
		appendCellWithoutCheck(row,"devNum_"+devid,0,temp);
		var j= 1;
		appendCell(row,"devicetype_"+devid,j++,"<img id='led_"+devid+"' src='images/led/L0.gif'>"+devs[i].getElementsByTagName("devicetype")[0].childNodes[0].nodeValue);
		if(document.getElementById("subtab").value==dPage){
			appendCell(row,"desc_"+devid,j++,devs[i].getElementsByTagName("desc")[0].childNodes[0].nodeValue);
			appendCell(row,"appliccode_"+devid,j++,devs[i].getElementsByTagName("appliccode")[0].childNodes[0].nodeValue);
			appendCell(row,"release_"+devid,j++,devs[i].getElementsByTagName("release")[0].childNodes[0].nodeValue);
			appendCell(row,"comport_"+devid,j++,devs[i].getElementsByTagName("comport")[0].childNodes[0].nodeValue);
			appendCell(row,"noanswer_"+devid,j++,"");
			appendCell(row,"errchk_"+devid,j++,"");
			appendCell(row,"nooffline_"+devid,j++,"");
		}else{
			appendCell(row,"desc_"+devid,j++,devs[i].getElementsByTagName("desc")[0].childNodes[0].nodeValue);
			appendCell(row,"",j++,"","none");
			appendCell(row,"st_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"treg_"+devid,j++,"");
			appendCell(row,"difftreg_st_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"toff_"+devid,j++,"");
			appendCell(row,"ton_"+devid,j++,"");
			appendCell(row,"diffton_toff_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"tdef_"+devid,j++,"");
			appendCell(row,"difftoff_tdef_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"tsat_"+devid,j++,"");
			appendCell(row,"tasp_"+devid,j++,"");
			appendCell(row,"sh_"+devid,j++,"");
			appendCell(row,"shset_"+devid,j++,"");
			appendCell(row,"diffsh_shset_"+devid,j++,"");
			appendCell(row,"valv_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"req_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"defr_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"cop_"+devid,j++,"");
			appendCell(row,"cooling_"+devid,j++,"");
			appendCell(row,"consumption_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"th2o_in_"+devid,j++,"");
			appendCell(row,"th2o_out_"+devid,j++,"");
			appendCell(row,"h2o_diff_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"comp_speed_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"liq_inj_"+devid,j++,"");
			appendCell(row,"",j++,"","none");
			appendCell(row,"envelope_"+devid,j++,"");
		}
		
	}
	return tab;
}
function appendCellWithoutCheck(row,id,colNum,contents){
	var cel=newCell(id,0,"table-cell",colNum);
	row.appendChild(cel);
	setCellValueWithoutCheck(cel,contents);
}
function appendCell(row,id,colNum,contents,display){
	var d = "table-cell";
	if(display != undefined)
		d = display; 
	var cel=newCell(id,0,d,colNum);
	row.appendChild(cel);
	setCellValue(cel,contents);
}
function debug_start(){
	var selectedDeviceid = "";
	var num = document.getElementsByName("devchk").length;
	for(var i=0;i<num;i++){
		if(document.getElementsByName("devchk")[i].checked == true){
			selectedDeviceid += document.getElementsByName("devchk")[i].value+";";
		}
	}
	if(selectedDeviceid == ""){
		alert(document.getElementById("txtnodeviceselect").value);
		return;
	}
	if(confirm(document.getElementById("txtconfirmstart").value)){
		disableChk(true);
		var timeout = document.getElementById("timeoutSel").value;
		var param = "cmd=start";
		param += "&selecteddev="+selectedDeviceid.substring(0, selectedDeviceid.length-1);
		param += "&timeout="+timeout;
		MTstartServerComm();
		new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab1_start, true);
	}
}
function Callback_tab1_start(xml){
	MTstopServerComm();
	updateButton(true);
	disableChk(true);
	document.getElementById("isDebugSessionOn").value="true";
	Concurrent.Thread.create(resetValues);
}
function debug_stop(){
	if(confirm(document.getElementById("txtconfirmstop").value)){
		var num = document.getElementsByName("devchk").length;
		var param = "cmd=stop";
		MTstartServerComm();
		new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab1_stop, true);	
	}
}
function Callback_tab1_stop(xml){
	MTstopServerComm();
	updateButton(false);
	disableChk(false);
	document.getElementById("isDebugSessionOn").value="false";
}
function debug_reset(){
	if(confirm(document.getElementById("txtconfirmreset").value)){
		var param = "cmd=reset";
		MTstartServerComm();
		new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab1_reset, true);
	}
}
function Callback_tab1_reset(xml){
	MTstopServerComm();
	updateButton(true);
}

function lvscroll(){
	var obj=document.getElementById("LWCtDataName0");
	{
		obj.parentNode.childNodes[0].scrollLeft=obj.scrollLeft;
		obj.parentNode.childNodes[1].scrollLeft=obj.scrollLeft;
	}
}
function forceRefresh()
{
	timer_counter ++;
	getValueTimer();
}
function getValueTimer(){
	var param = "";
	if(document.getElementById("subtab").value==dPage){
		param += "cmd=refresh&subtab=1";
	}else{
		param += "cmd=refreshSub2&subtab=2";
	}
	//param += "&subtab="+((document.getElementById("subtab").value==dPage)?"1":"2");
	new AjaxRequest("servlet/ajrefresh", "POST", param, Callback_getValueTimer,false);	
	disableAction(4);
}
function Callback_getValueTimer(xml){
	updateDebugSessionInfo(xml);
	var rowAry=new Array();
	var avg_t =xml.getElementsByTagName("avg_t");
	if(document.getElementById("subtab").value==dPage)
	{
		for(var i=0;i<avg_t.length;i++)
		{
			rowAry[i] = new Array();
			rowAry[i][0] = avg_t[i].childNodes[0].nodeValue;
			rowAry[i][1] = avg_t[i].childNodes[1].nodeValue;
		}
		Concurrent.Thread.create(cc_avgpollingtime,rowAry);
	}
	var devs=xml.getElementsByTagName("dev");
	rowAry=new Array();
	if(document.getElementById("subtab").value==dPage){
		for(i=0;i<devs.length;i++){
			rowAry[i]=new Array();
			rowAry[i][0]=devs[i].getElementsByTagName("devid")[0].childNodes[0].nodeValue;
			rowAry[i][1]=getValueFromXML(devs[i],"noanswer");
			rowAry[i][2]=getValueFromXML(devs[i],"errchk");
			rowAry[i][3]=getValueFromXML(devs[i],"nooffline");
			rowAry[i][4]="";
			rowAry[i][5]="";
			rowAry[i][6]="";
			rowAry[i][7]="";
			rowAry[i][8]="";
			rowAry[i][9]="";
			rowAry[i][10]="";
			rowAry[i][11]="";
			rowAry[i][12]="";
			rowAry[i][13]="";
			rowAry[i][14]="";
			rowAry[i][15]="";
			rowAry[i][16]="";
			rowAry[i][17]="";
			rowAry[i][18]=getValueFromXML(devs[i],"led");
		}
		enableAction(4);
	}else{
		for(var i=0;i<devs.length;i++){
			rowAry[i]=new Array();
			rowAry[i][0]=devs[i].getElementsByTagName("devid")[0].childNodes[0].nodeValue;
			rowAry[i][1]="";
			rowAry[i][2]="";
			rowAry[i][3]="";
			rowAry[i][4]=getValueFromXML(devs[i],"st");
			rowAry[i][5]=getValueFromXML(devs[i],"treg");
			rowAry[i][6]=getValueFromXML(devs[i],"toff");
			rowAry[i][7]=getValueFromXML(devs[i],"ton");
			rowAry[i][8]=getValueFromXML(devs[i],"tdef");
			rowAry[i][9]=getValueFromXML(devs[i],"tsat");
			rowAry[i][10]=getValueFromXML(devs[i],"tasp");
			rowAry[i][11]=getValueFromXML(devs[i],"valv");
			rowAry[i][12]=getValueFromXML(devs[i],"req");
			rowAry[i][13]=getValueFromXML(devs[i],"defr");
			rowAry[i][14]=getValueFromXML(devs[i],"fa");
			rowAry[i][15]=getValueFromXML(devs[i],"fb");
			rowAry[i][16]=getValueFromXML(devs[i],"fc");
			rowAry[i][17]=getValueFromXML(devs[i],"p1");
			rowAry[i][18]=getValueFromXML(devs[i],"led");
			rowAry[i][19]=getValueFromXML(devs[i],"sh");
			rowAry[i][20]=getValueFromXML(devs[i],"shset");
			rowAry[i][21]=getValueFromXML(devs[i],"cop");
			rowAry[i][22]=getValueFromXML(devs[i],"cooling");
			rowAry[i][23]=getValueFromXML(devs[i],"consumption");
			rowAry[i][24]=getValueFromXML(devs[i],"th2o_in");
			rowAry[i][25]=getValueFromXML(devs[i],"th2o_out");
			rowAry[i][26]=getValueFromXML(devs[i],"h2o_diff");
			rowAry[i][27]=getValueFromXML(devs[i],"comp_speed");
			rowAry[i][28]=getValueFromXML(devs[i],"liq_inj");
			rowAry[i][29]=getValueFromXML(devs[i],"envelope");
			rowAry[i][30]=getValueFromXML(devs[i],"avg_tsat");
			rowAry[i][31]=getValueFromXML(devs[i],"avg_cop");
		}
		enableAction(1);
	}
	kevinTest(rowAry);
}
function updateDebugSessionInfo(xml){
	var mode=xml.getElementsByTagName("mode")[0].childNodes[0].nodeValue;
	if(mode=="1"){
		updateButton(true);
		var time=xml.getElementsByTagName("time")[0].childNodes[0].nodeValue;
		if(document.getElementById("subtab").value==dPage){
			document.getElementById("timeoutSel").value=time;
			disableChk(true);
		}else{
			var chkAry=document.getElementsByName("devchk");
			for(var j=0;j<chkAry.length;j++){
				chkAry[j].disabled=true;
			}
		}
		document.getElementById("isDebugSessionOn").value="true";
	}else{
		if(document.getElementById("subtab").value==dPage){
			updateButton(false);
			disableChk(false);	
			document.getElementById("isDebugSessionOn").value="false";
		}else{
			enableAction(4);
			var chkAry=document.getElementsByName("devchk");
			for(var j=0;j<chkAry.length;j++){
				chkAry[j].disabled=true;
			}
		}
	}
	if((xml.getElementsByTagName("starttime")!=null) && (xml.getElementsByTagName("starttime")[0].childNodes[0].nodeValue!="")){
		document.getElementById("debugTime").innerHTML=document.getElementById("debugstarttime").value+":<br>"+xml.getElementsByTagName("starttime")[0].childNodes[0].nodeValue;
	}else{
		document.getElementById("debugTime").innerHTML="";
	}
}
function getValueFromXML(xml,tagname){
	var t=(xml.getElementsByTagName(tagname)[0]==null)?"---":xml.getElementsByTagName(tagname)[0].childNodes[0].nodeValue;
	return t;
}

function setCellValueWithoutCheck(id,val){
	var cel;
	if(typeof(id)=="string"){
		cel=document.getElementById(id);
	}else{
		cel=id;
	}
	var w=getHeadW(cel.hint);
	cel.innerHTML="<DIV style='WIDTH: "+w+"px; HEIGHT: 18px; OVERFLOW: hidden;'>"+val+"</DIV>";
}
function setCellValue(id,val){
	var cel;
	if(typeof(id)=="string"){
		cel=document.getElementById(id);
	}else{
		cel=id;
	}
	var w=getHeadW(cel.hint);
	if(checkData(val)){
	 val = Math.round(val * 100) / 100;
	}
	cel.innerHTML="<DIV style='WIDTH: "+w+"px; HEIGHT: 18px; OVERFLOW: hidden;'>"+val+"</DIV>";
}
function cc_avgpollingtime(rowAry)
{
	var tab = document.getElementById("avgpollingtime");
	for(var i=0;i<rowAry.length;i++)
	{
		var code = rowAry[i][0];
		var v_obj = document.getElementById("avg_v"+code);
		if(v_obj != null && v_obj != undefined)
		{
			v_obj.innerHTML = rowAry[i][1];
		}
	}
}
function kevinTest(rowAry){
	var tab=document.getElementById("serialTab");
	var chkAry=document.getElementsByName("devchk");
	//set new value
	var max1=0,min1=0,max2=0,min2=0;
	var mav1=0;miv1=0;mav2=0,miv2=0,first1=true,first2=true;
	if(document.getElementById("subtab").value==dPage){
		for(var i=0;i<rowAry.length;i++){
			var devid=rowAry[i][0];
			document.getElementById("led_"+devid).src = "images/led/L"+rowAry[i][18]+".gif";
			setSerialCalculateCell("noanswer_"+devid,rowAry[i][1]);
			setSerialCalculateCell("errchk_"+devid,rowAry[i][2]);
			setSerialCalculateCell("nooffline_"+devid,rowAry[i][3]);
		}
	}else{
		for(var i=0;i<rowAry.length;i++){
			var devid=rowAry[i][0];
			document.getElementById("led_"+devid).src = "images/led/L"+rowAry[i][18]+".gif";
			setCellValue("st_"+devid,rowAry[i][4]);
			setCellValue("treg_"+devid,rowAry[i][5]);
			var result = setDiffCalculateCell("difftreg_st_"+devid,false,rowAry[i][5],rowAry[i][4]);
			resetColorTitle("difftreg_st_"+devid);
			if(result)
			{
				chkthermdynamicColor("difftreg_st_"+devid,"red",Math.abs(rowAry[i][4]-rowAry[i][5])>5);
			}
			setCellValue("toff_"+devid,rowAry[i][6]);
			setCellValue("ton_"+devid,rowAry[i][7]);
			result = setDiffCalculateCell("diffton_toff_"+devid,(rowAry[i][14]=="0" || rowAry[i][16]=="0"),rowAry[i][7],rowAry[i][6]);
			resetColorTitle("diffton_toff_"+devid);
			if(result)
			{
				chkthermdynamicColor("diffton_toff_"+devid,"red",(rowAry[i][7]-rowAry[i][6])<0,"Probes switched, probes not working, wrong positions");
				chkthermdynamicColor("diffton_toff_"+devid,"#FFFF00",(rowAry[i][7]-rowAry[i][6])>5,"Wrong probes position, air flow disturbed");
			}
			setCellValue("tdef_"+devid,rowAry[i][8]);
			result = setDiffCalculateCell("difftoff_tdef_"+devid,(rowAry[i][14]==0 || rowAry[i][15]==0),rowAry[i][6],rowAry[i][8]);
			resetColorTitle("difftoff_tdef_"+devid);
			if(result)
			{
				chkthermdynamicColor("difftoff_tdef_"+devid,"red",(rowAry[i][6]-rowAry[i][8])<0,"Probes switched, probes not working, wrong positions");
				chkthermdynamicColor("difftoff_tdef_"+devid,"#FFFF00",(rowAry[i][6]-rowAry[i][8])>5,"Wrong probes position");
			}
			setCellValue("tsat_"+devid,rowAry[i][9]);
			resetColorTitle("tsat_"+devid);
			chkthermdynamicColor("tsat_"+devid,"red",Math.abs(rowAry[i][30]-rowAry[i][9])>5,"Bad cable, limits error, trasducer KO, high load loss");
			setCellValue("tasp_"+devid,rowAry[i][10]);
			setCellValue("sh_"+devid,rowAry[i][19]);
			resetColorTitle("sh_"+devid);
			chkthermdynamicColor("sh_"+devid,"red",rowAry[i][19]<4,"Check valves, setting, values read by the probes");
			setCellValue("shset_"+devid,rowAry[i][20]);
			resetColorTitle("shset_"+devid);
			chkthermdynamicColor("shset_"+devid,"#FFFF00",rowAry[i][20]<=4,"Set SH too low");
			result = setDiffCalculateCell("diffsh_shset_"+devid,(rowAry[i][17]==0 ),rowAry[i][19],rowAry[i][20]);
			resetColorTitle("diffsh_shset_"+devid);
			if(result)
			{
				chkthermdynamicColor("diffsh_shset_"+devid,"#FFFF00",Math.abs(rowAry[i][19]-rowAry[i][20])>4,"SH not stable, check regulation parameters of valves");
			}
			setCellValue("valv_"+devid,rowAry[i][11]);
			setCellValue("req_"+devid,rowAry[i][12]);
			setCellValue("defr_"+devid,rowAry[i][13]);
			setCellValue("cop_"+devid,rowAry[i][21]);
			resetColorTitle("cop_"+devid);
			chkthermdynamicColor("cop_"+devid,"red",rowAry[i][21]<(rowAry[i][31]-1),"Bad cable, limits error, trasducer KO, high load loss");
			setCellValue("cooling_"+devid,rowAry[i][22]);
			setCellValue("consumption_"+devid,rowAry[i][23]);
			setCellValue("th2o_in_"+devid,rowAry[i][24]);
			setCellValue("th2o_out_"+devid,rowAry[i][25]);
			setCellValue("h2o_diff_"+devid,rowAry[i][26]);
			resetColorTitle("h2o_diff_"+devid);
			chkthermdynamicColor("h2o_diff_"+devid,"red",(rowAry[i][25]-rowAry[i][24])<0,"Probes switched");
			chkthermdynamicColor("h2o_diff_"+devid,"#FFFF00",(rowAry[i][25]-rowAry[i][24])>5,"Check probes position");
			setCellValue("comp_speed_"+devid,rowAry[i][27]);
			setCellValue("liq_inj_"+devid,rowAry[i][28]);
			resetColorTitle("liq_inj_"+devid);
			chkthermdynamicColor("liq_inj_"+devid,"#FFFF00",rowAry[i][28]>=1);
			setCellValue("envelope_"+devid,rowAry[i][29]);
			resetColorTitle("envelope_"+devid);
			chkthermdynamicColor("envelope_"+devid,"#FFFF00",rowAry[i][29]>1);
		}
	}
	if(timer_counter>1)
	{
		timer_counter --;
	}
	else if(timer_counter == 1)
	{
		setTimeout("getValueTimer()",1000*document.getElementById("refreshSel").value);
	}
	sorttable.shell(document.getElementById("LWCtDataName0").childNodes[0]);
}
function setSerialCalculateCell(id,val){
	 if(val=="---"){
		setCellValue(id,"---");
	}else{
		setCellValue(id,val);
		chkSerialColor(id,val);
	}
}
function setDiffCalculateCell(id,condition,va1,va2){
	if(condition){
		setCellValue(id,"***");
	}else if((va1=="---") || (va2=="---")){
		setCellValue(id,"---");
	}else if((va1=="***") || (va2=="***")){
		setCellValue(id,"***");
	}else{
		setCellValue(id,(va1-va2));
		return true;
	}
	return false;
}
//function writeTopMessage(xml){
//	var topmsg = xml.getElementsByTagName("topmsg")[0].childNodes[0].nodeValue;
//	top.frames["header"].writeNotify(topmsg);
//}
function disableChk(flag){
	var timeSel=document.getElementById("timeoutSel");
	timeSel.disabled=flag;
	var chkAry=document.getElementsByName("devchk");
	for(var j=0;j<chkAry.length;j++){
		chkAry[j].disabled=flag;
	}
}
function resetColorTitle(cel)
{
	var c=document.getElementById(cel);
	if(c==null) return;
	c.bgColor="";
	c.title = "";
}
function chkthermdynamicColor(cel,color,condition,title){
	var c=document.getElementById(cel);
	if(c==null) return false;
	if(condition)
	{
		c.bgColor=color;
		if(title != undefined)
			c.title = title;
	}
	return true;
}
function chkSerialColor(c,v){
	var cel=document.getElementById(c);
	if(cel==null) return false;
	if(checkData(v)){
		if(v>50){
			if(v>100){
				cel.bgColor="red";
			}else{
				cel.bgColor="yellow";
			}
		}else{
			cel.className="td";
			cel.bgColor="";
		}
	}
	return true;
}
function checkData(txt){  
	var regExp=/^(\+|-)?[\d\%\.]{1,}$/;
	return regExp.test(txt);
}
function checkSelType(hname,textBox,val){
	var obj=document.getElementById(hname);
	if(obj.checked==true){
		document.getElementById(textBox).disabled=val;
	}
}
function resetValues(){
	var tab=document.getElementById("dataTab");
	var startCol=2;
	if(document.getElementById("subtab").value==dPage){
		startCol=5;
	}
	for(var i=0;i<tab.rows.length;i++){
		var statusDevicename = tab.rows[i].cells[2].innerHTML;
		//by Kevin, replace other device's status to offline
		statusDevicename.replace(/images\/led\/L1.gif/,"images/led/L0.gif");
		statusDevicename.replace(/images\/led\/L2.gif/,"images/led/L0.gif");
		statusDevicename.replace(/images\/led\/L3.gif/,"images/led/L0.gif");
		for(var j=startCol;j<tab.rows[0].cells.length;j++){
			var cel=tab.rows[i].cells[j];
			var w=getHeadW(cel.hint);
			cel.innerHTML="<DIV style='WIDTH: "+w+"px; HEIGHT: 18px; OVERFLOW: hidden;'> </DIV>";
			cel.className="td";
			cel.bgColor="";
		}
	}
}


// modbus debug
function mb_debug_init()
{
	enableAction(1);
	enableAction(2);
	enableAction(3);
}

function mb_debug_set_level()
{
	document.getElementById("cmd").value = "mb_debug_set_level";
	var form = document.getElementById("frm_set_debug_level");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}

function mb_debug_refresh()
{
	document.getElementById("cmd").value = "mb_debug_refresh";
	var form = document.getElementById("frm_set_debug_level");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}

function exportLog()
{
	fdSaveFile('exp', 'log', export_log);
	var date = new Date();
	fdSetFile("PVProModbus_" + date.format("yyyyMMddhhmmss") + ".log");
}

function export_log(fdLocal, path, fileName)
{
	if( fdLocal ) {	
		document.getElementById("cmd").value = "export";
		if( !strEndsWith(path, ".log") )
			document.getElementById("exp").value = path + ".log";
		var form = document.getElementById("frm_set_debug_level");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
	else {
		var date = new Date();
		fileName = "PVProModbus_"+ date.format("yyyyMMddhhmmss")+".log";
		log_export_callback = fileName;
		path = document.getElementById("remoteFolder").value + fileName;
		new AjaxRequest("servlet/master", "POST", "cmd=export&exp="+path, Callback_export_log, true);
	}
}

function Callback_export_log()
{
	MTstopServerComm();
	path = document.getElementById("remoteFolder").value + log_export_callback;
	window.open(top.frames["manager"].getDocumentBase() + "app/report/popup.html?" + path);
}

function strEndsWith(str, sufix)
{
	if( str.length >= sufix.length )
		return str.substr(str.length - sufix.length, sufix.length).toLowerCase() == sufix.toLowerCase();
	return false;
}
