var kpiresultkpiresult = "<TABLE width='100%' class='standardTxt'>"+
			"<TBODY>"+
			"<TR>"+
			"<TD width='*'></TD>"+
			"<TD width='10%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"<TD width='5%'></TD>"+
			"</TR>"+
			"</TBODY>"+
			"</TABLE>";
			
var kpiresulttablestring_a = "<TD align='center' colspan='11'>";
var kpiresulttablestring_b = "</TD>";

var kpidevicestocompute;
var kpicomputeddevices;

//report var
var group;
var def;
var sol;
var min;
var max;
var minp;
var maxp;

//graph var
var startdate;
var enddate;

function compute_result()
{
	if(!verifyfields()) return;
	group = document.getElementById('kpicurrentgroup').innerHTML;
	def = document.getElementById('def').checked;
	sol = document.getElementById('sol').checked;
	min = document.getElementById('min').value;
	max = document.getElementById('max').value;
	minp = document.getElementById('minp').value;
	maxp = document.getElementById('maxp').value;
		
	if(!isDate(document.getElementById("yyyy").value, 
				document.getElementById("mm").value,
				document.getElementById("dd").value))
	{
		alert(document.getElementById('invaliddate').value);
		return;
	}
	var tb = document.getElementById("kpiresultstable").tBodies[0];
	if(tb!=null && tb.rows!=null && tb.rows.length>0)
	{
		while(tb.rows.length>0)
		{
			tb.deleteRow(0);
		}
	}
	kpidevicestocompute = document.getElementById("kpiconfcontainer").getElementsByTagName("div");
	kpicomputeddevices = 0;
	if(kpidevicestocompute!=null && kpidevicestocompute.length>0)
	{
		kpicomputeddevices = 0;
		disableAction(1);
		disableAction(2);
		disableAction(3);

		setReportFields();

		compute_result2(kpidevicestocompute[0].id);
	}
}

function compute_result2(ble)
{
	var tblrs=document.getElementById("kpiresultstable");
	tblrs.style.visibility ="visible";
	tblrs.style.display ="block";
	var newrow;
	if(tblrs.tBodies[0].rows!=null)
	{
		newrow = tblrs.tBodies[0].insertRow(tblrs.tBodies[0].rows.length);
	}
	else
	{
		newrow = tblrs.tBodies[0].insertRow(0);
	}
	var c = newrow.insertCell(0);
	c.align='center';
	c.colSpan='12';
	c.style.border='1px solid black';
	c.innerHTML=document.getElementById("kpiresultcomp").value;
	var thisdev = kpidevicestocompute[kpicomputeddevices].id;
	var mydate = document.getElementById("yyyy").value
		+ '-' + (document.getElementById("mm").value.length < 2 ? "0" : "")	+ document.getElementById("mm").value
		+ '-' +	(document.getElementById("dd").value.length < 2 ? "0" : "") + document.getElementById("dd").value
		+ ' ' + (document.getElementById("hh").value.length < 2 ? "0" : "") + document.getElementById("hh").value
		+ ':00:00.0';
	
	var querystring = "kpicmd=kpiresults&"
		+ "idgrp=" + (document.getElementById(thisdev+"grp").value) + "&"
		+ "enddate=" + mydate + "&"
		+ "timewindow=" + document.getElementById("timewin").value + "&"
		+ "min=" + (document.getElementById("min").value) + "&"
		+ "minp=" + (document.getElementById("minp").value) + "&"
		+ "max=" + (document.getElementById("max").value) + "&"
		+ "maxp=" + (document.getElementById("maxp").value) + "&"
		+ "def=" + (document.getElementById("def").checked) + "&"
		+ "sol="+(document.getElementById("sol").checked) + "&"
		+ "iddevmdl=" + (document.getElementById(thisdev+"iddevmdl").value) + "&"
		+ "mastervarmdl=" + (document.getElementById(thisdev+"mastervarmdl").value) + "&"
		+ "defvarmdl=" + (document.getElementById(thisdev+"defvarmdl").value) + "&"
		+ "solenoidvarmdl=" + (document.getElementById(thisdev+"solenoidvarmdl").value) + "&"
		+ "iddevice=" + (document.getElementById(thisdev+"iddevice").value) + "&"
		+ "vmid=" + (document.getElementById(thisdev+"vmid").value) + "&"
		+ "vdid=" + (document.getElementById(thisdev+"vdid").value) + "&"
		+ "vsid=" + (document.getElementById(thisdev+"vsid").value);
		CommSend("servlet/ajrefresh", "POST", encodeURI(querystring), "kpi_loadres", false);
}

function Callback_kpi_loadres()
{
	//alert("ecco che aggiungo i risultati alla pagina");
	insertintoresulttable(xmlResponse);//kpiresultkpiresult_a+'-'+xmlResponse.xml+kpiresultkpiresult_b;
	kpicomputeddevices++;
	if(kpicomputeddevices < kpidevicestocompute.length)
	{
		setTimeout("compute_result2("+kpidevicestocompute[kpicomputeddevices].id+")", 100);
	}
	else
	{
		// visualizzo la legenda
		var legendTable=document.getElementById("legendTable");
	    legendTable.style.visibility ="visible";
	    legendTable.style.display ="block";
	    //
	    //alert(document.getElementById('kpifinished').value);
		enableAction(1);
		enableAction(2);
		enableAction(3);
	}
}

function insertintoresulttable(xmlt)
{
	var myrow=document.getElementById("kpiresultstable").tBodies[0].rows[document.getElementById("kpiresultstable").tBodies[0].rows.length-1];
	var result = xmlt.getElementsByTagName('result')[0].childNodes;
	myrow.deleteCell(0);
	for(var kk = result.length-1; kk>2; kk--)
	{
		var mycell = myrow.insertCell(0);
		mycell.width='5%';
		mycell.align='center';
		mycell.className='standardTxt';
		var strinner = result[kk].childNodes[0].nodeValue;
		if(strinner!=null && strinner!='null' && strinner!='' && strinner!='---')
			mycell.innerHTML = strinner;
		else
			mycell.innerHTML = '---';
		var a = new Number(strinner.substr(0,strinner.length-1));
		var b = new Number(document.getElementById("maxp").value);
		var c = new Number(document.getElementById("minp").value);
		if(result[kk].nodeName=='pover' && strinner!='---' && a >= b)
		{
			mycell.style.backgroundColor = "#ff0000";
			mycell.style.color = "#ffffff";
		}
		if(result[kk].nodeName=='punder' && strinner!='---' && a >= c)
		{
			mycell.style.backgroundColor = "#0000ff";
			mycell.style.color = "#ffffff";
		}
	}
	var mycell = myrow.insertCell(0);
	mycell.className='standardTxt';
	mycell.innerHTML = result[2].childNodes[0].nodeValue;	
}

function Callback_kpi_exportres()
{
	var sPath=xmlResponse.getElementsByTagName('resfile')[0].childNodes[0].nodeValue;
	if(sPath=='---')
	{
		return;
	}
	var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+sPath;
	window.open(sUrl);
}

function verifyfields()
{
	var min = 0;
	var max = 0;
	var tmp = document.getElementsByTagName('input');
	if(document.getElementById('min').value==null ||
		document.getElementById('min').value=='' ||
		document.getElementById('minp').value==null ||
		document.getElementById('minp').value=='' ||
		document.getElementById('max').value==null ||
		document.getElementById('max').value=='' ||
		document.getElementById('maxp').value==null ||
		document.getElementById('maxp').value=='' ||
		document.getElementById('yyyy').value==null ||
		document.getElementById('yyyy').value=='')
	{
		alert(document.getElementById('emptyfieldserror').value);
		return false;
	}
		
	for(var i = 0; i< tmp.length; i++)
	{
		if((tmp[i].id.substring(0,4)=='minp' || tmp[i].id.substring(0,4)=='maxp') && (tmp[i].value<0 || tmp[i].value>100))
		{
			alert(document.getElementById('percentageerror').value);
			return false;
		}
	}
	
	min = parseInt(document.getElementById('min').value);
	max = parseInt(document.getElementById('max').value);
	
	if( min > max )
	{
		alert(document.getElementById("minmaxerror").value);
		return false;
	}
	
	return true;
}

function graph_result(){
	disableAction(3);
	var data = "";
	var results = document.getElementById("kpiresultstable").rows;
	
	for(var j=0;j < results[0].cells.length; j++)
	{
		data += "\""+results[0].cells[j].innerHTML + "\";";
	}
	data += "\n";

	for(var i=1;i < results.length;i++)
	{
		data += "\""+results[i].cells[0].getElementsByTagName('a')[0].innerHTML + "\";";
		//console.log("results[i].cells[0].innerHTML:"+results[i].cells[0].firstElementChild.textContent );
		for(var j=1;j < results[i].cells.length; j++)
		{
			data += results[i].cells[j].innerHTML + ";";
		}
		data += "\n";
	}
	var startdate=document.getElementById("reportkpistart").value;
	var enddate=document.getElementById("reportkpistop").value;
	/*
	 * DYN: URL not correctly encoded
	var querystring = "kpicmd=kpigraph&"+
		"kpigroup=\""+escape(group)+"\"&"+
		"kpisol="+escape(sol)+"&"+
		"kpidef="+escape(def)+"&"+
		"kpimin="+escape(min)+"&"+
		"kpiminp="+escape(minp)+"&"+
		"kpimax="+escape(max)+"&"+
		"kpimaxp="+escape(maxp)+"&"+
		"kpistart="+escape(startdate)+"&"+
		"kpistop="+escape(enddate)+"&"+
		"data="+escape(data);
	*/
	
	var querystring = "kpicmd=kpigraph&"
		+ "kpigroup=\"" + group + "\"&"
		+ "kpisol=" + sol + "&"
		+ "kpidef=" + def + "&"
		+ "kpimin=" + min + "&"
		+ "kpiminp=" + minp + "&"
		+ "kpimax=" + max + "&"
		+ "kpimaxp=" + maxp + "&"
		+ "kpistart=" + startdate + "&"
		+ "kpistop=" + enddate + "&"
		+ "data=" + data;
	//alert(querystring);
	CommSend("servlet/ajrefresh","POST",encodeURI(querystring),"graph_result",false);
}

function Callback_graph_result()
{
	window.open(top.frames["manager"].getDocumentBase() + 'kpigraph/KPIGraph.jsp;'+top.frames["manager"].getSessionId()+'?&xmlfile='+
			xmlResponse.getElementsByTagName('filename')[0].childNodes[0].nodeValue
			+ "&basepath=" + top.frames["manager"].getDocumentBase(),
			'','scrollbars=yes,menubar=no,height=650,width=850,resizable=yes,toolbar=no,location=yes,status=no');
	enableAction(3);
}

function setReportFields(){
	var dstop = new Date();
	dstop.setDate(document.getElementById("dd").value);
	dstop.setMonth(document.getElementById("mm").value-1);
	dstop.setFullYear(document.getElementById("yyyy").value);
	dstop.setHours(document.getElementById("hh").value);
	dstop.setMinutes(0);
	dstop.setSeconds(0);
	dstop.setMilliseconds(0);
	
	var dstart = new Date();
	var tw0 = document.getElementById("timewin").value;
	var tw1 = tw0.split("_");
	var tw2 = Number(tw1[1]);
	var tw3 = 1;
	if("h"==tw1[0])
		tw3*=3600000;
	if("g"==tw1[0])
		tw3*=86400000;
	if("m"==tw1[0])
		tw3*=2592000000;
	tw3*=tw2;
	
	dstart.setTime(dstop.getTime()-tw3);
	dstart.setMinutes(0);
	dstart.setSeconds(0);
	dstart.setMilliseconds(0);
	
	var fillhh1 = dstart.getHours()<10?"0":"";
	var fillhh2 = dstop.getHours()<10?"0":"";
	var fillday1 = dstart.getDate()<10?"0":"";
	var fillday2 = dstop.getDate()<10?"0":"";
	var fillmonth1 = dstart.getMonth()<10?"0":"";
	var fillmonth2 = dstop.getMonth()<10?"0":"";
	
	document.getElementById("reportkpistart").value = dstart.getFullYear()+"-"+
					fillmonth1+(dstart.getMonth()+1)+"-"+fillday1+dstart.getDate()+" "+
					fillhh1+dstart.getHours()+":00:00";
	document.getElementById("reportkpistop").value = dstop.getFullYear()+"-"+
					fillmonth2+(dstop.getMonth()+1)+"-"+fillday2+dstop.getDate()+" "+
					fillhh2+dstop.getHours()+":00:00";
}

function kpi_fdSaveFile()
{
	fdSaveFile('','csv',kpi_savefile);
	var date = new Date();
	fdSetFile("KPI_"+date.format("yyyyMMddhhmmss"));
}
function kpi_savefile(local,path,filename)
{
	path += ".csv";
	
	var data = "";
	var results = document.getElementById("kpiresultstable").rows;
	
	for(var j=0;j < results[0].cells.length; j++)
	{
		data += "\""+results[0].cells[j].innerHTML + "\";";
	}
	data += "\n";

	for(var i=1;i < results.length;i++)
	{
		data += "\""+results[i].cells[0].getElementsByTagName('a')[0].innerHTML + "\";";
		for(j=1;j < results[i].cells.length; j++)
		{
			data += results[i].cells[j].innerHTML + ";";
		}
		data += "\n";
	}
	
	/*
	 * DYN: URL not correctly encoded
	var querystring = "kpicmd=kpiexportresults&"+
		"kpigroup=\""+escape(group)+"\"&"+
		"kpisol=\""+escape(sol)+"\"&"+
		"kpidef=\""+escape(def)+"\"&"+
		"kpimin="+escape(min)+"&"+
		"kpiminp="+escape(minp)+"&"+
		"kpimax="+escape(max)+"&"+
		"kpimaxp="+escape(maxp)+"&"+
		"data="+escape(data);
	*/

	var querystring = "cmd=kpiexportresults&"
		+ "kpigroup=\"" + group + "\"&"
		+ "kpisol=\"" + sol + "\"&"
		+ "kpidef=\"" + def + "\"&"
		+ "kpimin=" + min + "&"
		+ "kpiminp=" + minp + "&"
		+ "kpimax=" + max + "&"
		+ "kpimaxp=" + maxp + "&"
		+ "data=" +  data+ "&"
		+ "path=" + path + "&"
		+ "local=" + local;
	CommSend("servlet/ajrefresh", "POST", encodeURI(querystring),"kpiexp_back("+local+")",true);
}
function Callback_kpiexp_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}
