var GL_defCls_odd = "Row1";
var GL_defCls_even = "Row2";
var GL_ClsSelect = "selectedRow";


function ListView(title,idTbBody,nTable,nRow,nColumn,aValue,aHeaderValue,Size,aAlgn,sClick,sDbClick,iRowHeight,colorRow)
{
	ListView(title,idTbBody,nTable,nRow,nColumn,aValue,aHeaderValue,Size,aAlgn,sClick,sDbClick,iRowHeight,colorRow,1,1);
}

function ListView(title,idTbBody,nTable,nRow,nColumn,aValue,aHeaderValue,Size,aAlgn,sClick,sDbClick,iRowHeight,colorRow,page,totalpage)
{
	this.title=title;
	this.idTableName = idTbBody;
	this.idTableNum  = nTable;
	this.numRows     = nRow;
	this.numCols     = nColumn;
	this.mData       = aValue;
	
	this.rowHeight   = iRowHeight;
	if(this.rowHeight == null || this.rowHeight == undefined)
		this.rowHeight = 16;
	
	this.colorRowSel = colorRow;
		
	this.aHeader=aHeaderValue;
	
	//page attribute
	this.currentPage   = page; //currentPage
	this.typeButton    =  "left";
	
	// convert col sizes to percents
	var nTotCols = 0;
	for(var i = 0; i < aSize.length; i++)
		nTotCols += parseInt(aSize[i], 10);
	var tabTitle = document.getElementById("LWCtTitle" + nTable).getElementsByTagName("table")[0];
	for(var i = 0; i < aSize.length; i++) {
		aSize[i] = parseInt(parseInt(aSize[i], 10) / nTotCols * 100, 10);
		tabTitle.rows[0].cells[i].style.width = aSize[i] + "%"; 
	}
	
	// Cols Attribute
	this.aSizeCols   = aSize;
	this.aSizeDiny   = aSize;
	this.aAligCols   = aAlgn;
	// Rows Attribute
	this.aSgCkRows   = sClick;
	this.aDbCkRows   = sDbClick;
	
	this.idxColOrder = -1;
	this.verColOrder = 0; // 0-ASC, 1-DESC
	
	this.rowSelected = null;
	this.rowClassSel = null;
	
	this.refresh = LV_refresh;
	this.render = LV_render;
	this.selectRow = LV_selectRow;
	this.showHide = LV_showHide;
	
	this.getDataToPostRefresh = LV_getDataToPostRefresh;
	this.getDataToPostPage=LV_getDataToPostPage
	
	this.saveCurrentDim = LV_saveCurrentDim;
	this.updatePage=LV_updatePage;
	this.setPageNumber=LV_setPageNumber;
	this.printTableDevice=LV_printTableDevice;
	this.printTableAlarm=LV_printTableAlarm;
	this.printTableAlarmCall=LV_printTableAlarmCall;
	this.printTableEvent=LV_printTableEvent;
	this.printTableAlarmSearch =LV_printTableAlarmSearch;
	this.printTableAlarmEventSearch =LV_printTableAlarmEventSearch;
	this.printTableAlarmDtl =LV_printTableAlarmDtl;
	this.exportAlEv=LV_exportAlEv;
	this.setPageTotal=LV_setPageTotal;
	this.totalpage=totalpage;
}

function LV_updatePage(type){
	if(type=="left"){
		if(this.currentPage!=1)
			this.currentPage--;
	}//if
	if(type=="right" && this.currentPage<this.totalpage){
		
		this.currentPage++;
	}//if
	if(type=="superleft"){
		this.currentPage=1;
	}//if
	if(type=="superright"){
		this.currentPage=this.totalpage;
		disableRightArrows(this.idTableNum);
	}//if
	//this.typeButtun=type;	
	
	//document.getElementById("page").innerHTML = "Page "+ this.currentPage;
	
	//Blocco il Refresh
	top.frames['body'].frames['bodytab'].R_lock=0;
	
	//Post i dati
	top.frames['body'].frames['Poller'].actionPollerPage();
	
	

}//updatePage

function changePageNumber(idTable,type,obj)
  {
	
	var oLswTmp = null;
  	var sHtml = "";
  	if(top.frames['body'].frames['bodytab'].oLswContainer != null)
  	{
  		for(var i=0; i<top.frames['body'].frames['bodytab'].oLswContainer.getSize(); i++)
  		{
  			oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(i);
  			if(oLswTmp.idTableNum == idTable){
  				if(((obj.id == "f"+idTable+"3") || (obj.id == "f"+idTable+"4"))&& (oLswTmp.currentPage>1)){
  					oLswTmp.updatePage(type);
  				}else if(((obj.id == "f"+idTable+"1") || (obj.id == "f"+idTable+"2")) && (oLswTmp.currentPage<oLswTmp.totalpage)){
  					oLswTmp.updatePage(type);
  				}
  			}//if
  		}
  	}//if 		
  	
  	
 }//changePageNumber

function disableRightArrows(idTable){
	document.getElementById("f"+idTable+"1").src = "images/lsw/dxdx_off.png";
	document.getElementById("f"+idTable+"2").src = "images/lsw/dx_off.png";
	document.getElementById("f"+idTable+"1").style.cursor = "arrow";
	document.getElementById("f"+idTable+"2").style.cursor = "arrow";
}
function disableLeftArrows(idTable) {
	document.getElementById("f" + idTable + "3").src = "images/lsw/sx_off.png";
	document.getElementById("f" + idTable + "4").src = "images/lsw/sxsx_off.png";
	document.getElementById("f" + idTable + "3").style.cursor = "arrow";
	document.getElementById("f" + idTable + "4").style.cursor = "arrow";
}
function enableRightArrows(idTable){
	document.getElementById("f"+idTable+"1").src = "images/lsw/dxdx_on.png";
	document.getElementById("f"+idTable+"2").src = "images/lsw/dx_on.png";
	document.getElementById("f"+idTable+"1").style.cursor = "pointer";
	document.getElementById("f"+idTable+"2").style.cursor = "pointer";
}
function enableLeftArrows(idTable) {
	document.getElementById("f" + idTable + "3").src = "images/lsw/sx_on.png";
	document.getElementById("f" + idTable + "4").src = "images/lsw/sxsx_on.png";
	document.getElementById("f" + idTable + "3").style.cursor = "pointer";
	document.getElementById("f" + idTable + "4").style.cursor = "pointer";
}
function LV_refresh(nRow,nColumn,aValue,sClick,sDbClick,glbTbName)
{
	this.numRows     = nRow;
	this.numCols     = nColumn;
	this.mData       = aValue;
	this.aSgCkRows   = sClick;
	this.aDbCkRows   = sDbClick;
	this.render();
	
	// Ordino
	if(this.idxColOrder != -1)
		checkClickRefresh(1,glbTbName,"TABLEDATA"+this.idTableNum,"TDBdy"+this.idTableNum,this.idxColOrder,this.idTableName,1);
}

function LV_setPageNumber(idTable,pageNumber){
	//if(this.typeButtun=="superright")
	//	this.currentPage=pageNumber;
	//debugger;
	if(this.currentPage==0)
		this.currentPage=pageNumber;
	//2010-12-22, Kevin Ge, add input field for paging
	document.getElementById("page"+idTable).innerHTML = "<input type=text size='4' id='ListViewPaging_"+idTable+"' class='keyboardInput' onkeydown='checkOnlyNumber(this,event);pageSetInput_keydown(this,"+idTable+",event)' onblur='onlyNumberOnBlur(this);' value='"+this.currentPage+"'>";
	var vkeytype = document.getElementById("vkeytype");
	if(vkeytype != null && typeof(vkeytype) != "undefined" && typeof(buildKeyboardInputs) == "function")
	{
	  buildKeyboardInputs();
	}
}
function LV_setPageTotal(idTable,pageTotal){
	this.totalpage=pageTotal;
	document.getElementById("tot"+idTable).innerHTML = "/"+this.totalpage;
	checkNavigation(idTable,this.currentPage,this.totalpage);
}
function checkNavigation(idTable,cur,total){
	var btn=document.getElementById("f"+idTable+"1");
	if(btn==null) return false; //if navigation buttons is not exist. break out.
	if(total==1){
		disableRightArrows(idTable);
		disableLeftArrows(idTable);
	}else if(cur==1){
		disableLeftArrows(idTable);
		enableRightArrows(idTable);
	}else if(cur==total){
		enableLeftArrows(idTable);
		disableRightArrows(idTable);
	}else{
		enableRightArrows(idTable);
		enableLeftArrows(idTable);
	}
	return true;
}
function LV_render()
{
	var oIdTbBody = document.getElementById(this.idTableName);
	var scl = "";
	var i = 0;
	var k = 0;
	var buf = "";
	var rh = this.rowHeight+"px";
	var dbClickAction = "";
	var sgClickAction = "";
	var myAr = new Array(this.numRows);
	for (k=0; k<this.numRows; k++)
	{
		buf = "";
		dbClickAction = "";
		sgClickAction = "";
				
		if((this.mData[k][0]) != ""){
			scl = this.mData[k][0];
		}else{
			scl = (k%2==0) ? GL_defCls_even : GL_defCls_odd;
		}
		if (this.mData[k][2]!="nop")
			dbClickAction = LV_createAction(this.aDbCkRows,this.mData[k][2],this.mData[k]);
		if (this.mData[k][1]!="nop")
			sgClickAction = LV_createAction(this.aSgCkRows,this.mData[k][1],"");
				
		//only for scheduler dashboard
		if(this.rowHeight == 2008)
		{
			buf +="<tr id=\""+this.idTableNum+"TDtr"+(k+1)+"\" ondblclick=\""+dbClickAction+"\" onclick=\""+sgClickAction+"\" class=\""+scl+"\">\n";
			for (i=3; i<this.numCols+3; i++)
			{
				if((i == 6 && this.numCols==5) || (i==5 && this.numCols==4))
				{
					buf += "<td align=\""+LV_decodeAlign(this.aAligCols[i-3])+"\" valign='middle' class=\"td\"><DIV id=\""+this.idTableNum+"divtd"+ new String(k) +""+ new String(i-3) +"\" style=\"width:400px;\" class=\"LV_DIV\" onselectstart=\"return false;\" nowrap>"+(this.mData[k])[i]+"</DIV></td>\n";
				}
				else
				{
					buf += "<td align=\""+LV_decodeAlign(this.aAligCols[i-3])+"\" valign='middle' class=\"td\"><DIV id=\""+this.idTableNum+"divtd"+ new String(k) +""+ new String(i-3) +"\" style=\"width:"+this.aSizeDiny[i-3]+"px;\" class=\"LV_DIV\" onselectstart=\"return false;\" nowrap>"+(this.mData[k])[i]+"</DIV></td>\n";
				}
			}
		}
		else
		{
			buf +="<tr id=\""+this.idTableNum+"TDtr"+(k+1)+"\" ondblclick=\""+dbClickAction+"\" onclick=\"R_rowSelect(this,'"+this.idTableName+"'); "+sgClickAction+"\" class=\""+scl+"\">\n";
			for (i=3; i<this.numCols+3; i++)
				buf += "<td align=\""+LV_decodeAlign(this.aAligCols[i-3])+"\" class=\"td\" width=\"" + this.aSizeDiny[i-3] + "%\" ><DIV id=\""+this.idTableNum+"divtd"+ new String(k) +""+ new String(i-3) +"\" style=\"overflow:hidden;height:"+rh+"\" class=\"LV_DIV\" onselectstart=\"return false;\" nowrap>"+(this.mData[k])[i]+"</DIV></td>\n";
		}
		buf += "</tr>";
		myAr[k] = buf;
		//sHtml += buf;
	}
	
	var sHtml = "<table id=\"TableData"+this.idTableNum+"\" class=\"table\" width=\"100%\" onMouseUp=\"document.onmousemove=null;return false\" cellspacing=\"1px\"><tbody id=\"TDBdy"+this.idTableNum+"\">"+myAr.join("")+"</tbody></table>"; 

	//sHtml += "</tbody>"
	//sHtml += "</table>";
	oIdTbBody.innerHTML = sHtml;
	
	//2011-6-1, add by Kevin. Below is for checkbox after AJAX refresh
	if(typeof(checkboxList) != "undefined" && checkboxList != null)
	{
		for (k=0; k<this.numRows; k++)
		{
			if(checkboxList.indexOf(this.mData[k][1]) != -1 && document.getElementById("chk"+this.mData[k][1]) != null)
			{
				document.getElementById("chk"+this.mData[k][1]).checked = true;
			}
		}
	}
	if(typeof(updateCheckAllCheckbox) == "function")
	{
		updateCheckAllCheckbox();
	}
	//-----
	
	top.frames['body'].frames['bodytab'].R_lock=1;
	checkNavigation(this.idTableNum,this.currentPage,this.totalpage);
}

function LV_createAction(sAction, sVal,data)
{
	var rst;
	
	if(sAction != null && sAction != ""){
		if("goToAlarmOrEvent($1)"==sAction){
			rst=sAction.replace("$1", sVal+","+(data[data.length-1].length>0?1:0));
		}else{
			rst=sAction.replace("$1", sVal);
		}
	}else{
		rst="";
	}
	return rst;
}

function LV_selectRow(objRow)
{
	var same = false;
		
	if(this.rowSelected != null)
	{
		if(objRow.id == this.rowSelected.id)
			same = true;
		
		this.rowSelected.className = this.rowClassSel;
	}
	
	if(!same) 
	{
		this.rowSelected = objRow;
		this.rowClassSel = this.rowSelected.className;
		if(this.colorRowSel)
			this.rowSelected.className = GL_ClsSelect;
	}
	else
		this.rowSelected = null;		
}

function LV_showHide(div, divLength, dim, iTable)
{
	m_i=2;
	var oDiv00 = document.getElementById(iTable + 'div' + div);
	var oDivtd00 = null;
	var iScost = 0;
	
	if (parseInt(oDiv00.style.width) <= 15)
	{
		iScost = dim;
	}
	else
	{
		iScost = 15;
	}
	var scost = parseInt(oDiv00.style.width) - iScost;
	oDiv00.style.width =  iScost;
	for (i=0; i<divLength; i++)
	{
		oDivtd00 = document.getElementById(iTable + 'divtd' + i + div);
		if(oDivtd00 != null)
			oDivtd00.style.width = oDiv00.style.width;
	}

	var oI = document.getElementById(iTable + 'd' + div);	
	
	var iK = 0;
	while (oI != null)
	{
		oI.style.left = parseInt(oI.style.left) - scost;
		iK++;
		oI = document.getElementById(iTable + 'd' + (div + iK));
	}
	
	this.saveCurrentDim(oDiv00.style.width, div);
}

function LV_changeColor(o, i)
{
	var path = String(o.getAttribute("background"));

	if (path == "")
	{
		 if (i == 0)
			o.style.backgroundColor="#555555";
		else
			o.style.backgroundColor="#888888";
	}
}

function LV_HScroll(iTable,sType)
{
  if(sType == null || sType == undefined || sType == "")	
  	sType = "";
  	
  try
  {
    oLWCtTitle = document.getElementById("LW"+sType+"CtTitle"+iTable);
	oLWCtDataName = document.getElementById("LW"+sType+"CtDataName"+iTable);
	oLWCtTitle.scrollLeft=oLWCtDataName.scrollLeft;

    if (oLWCtTitle.scrollLeft!=oLWCtDataName.scrollLeft){
    	//removed by Kevin, because it will cause page flicker.
      //oLWCtDataName.scrollLeft=oLWCtTitle.scrollLeft;
      return false;
    }
  }catch(e){
  }
}

function LV_getDataToPostRefresh()
{
	var sHtml = "<input name='pd"+this.idTableNum+"' type='hidden' value='";
	sHtml += this.numRows+"#"+this.numCols+"#"+this.currentPage+"#refresh#false#22'></input>";
	return sHtml;	
}


function LV_getDataToPostPage()
{
	var sHtml = "<input name='pd"+this.idTableNum+"' type='hidden' value='";
	sHtml += this.numRows+"#"+this.numCols+"#"+this.currentPage+"#page#false#22'></input>";
	return sHtml;
}



function LV_decodeAlign(num)
{
	num = Number(num);
	switch(num)
	{
		case 1:
			return "center";
			break;
		case 2:
			return "right";
			break;
		default:
			return "left";
			break;
	}
}


function LV_saveCurrentDim(dim,col)
{
	dim = dim.replace("px","");
	this.aSizeDiny[col] = dim;
}



//Manuel Gilioli
function printTable(idTable,type){
	var oLswTmp = null;
  	var sHtml = "";
  	if(top.frames['body'].frames['bodytab'].oLswContainer != null)
  	{
  		for(var i=0; i<top.frames['body'].frames['bodytab'].oLswContainer.getSize(); i++)
  		{
  			oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(i);
  			if(oLswTmp.idTableNum == idTable) {
  				var str="";
  				if(type=="device")
					str=oLswTmp.printTableDevice();
				if(type=="alarm")
					str=oLswTmp.printTableAlarm();
				if(type=="alarmcall")
					str=oLswTmp.printTableAlarmCall();
				if(type=="event")
					str=oLswTmp.printTableEvent();
				if(type=="alarmsearch")
					str=oLswTmp.printTableAlarmSearch();
				if(type=="alarmevent")
					str=oLswTmp.printTableAlarmEventSearch();
				if(type=="alarmdtl")
					str=oLswTmp.printTableAlarmDtl();
				if(type=="exportAlEv"){
					oLswTmp.exportAlEv();
					break;
				}
				openPrintPage(str);
			}//if
  		}//for
  	}//if 		
}//printTable



function LV_printTableDevice(){
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table border='0' cellspacing=\"0\" cellpadding=\"0\">";
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[2]+"</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"</th>";
	str+="\n</thead>";
	str+="\n<tbody>";
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr>";
		str+="\n<td width='40%' class=\"tdprint\">"+this.mData[i][4]+"</td>";
		
		str+="\n<td width='20%' class=\"tdprint\">"+cleanDataB(this.mData[i][5])+"</td>";
		str+="\n<td width='20%' class=\"tdprint\">"+cleanDataB(this.mData[i][6])+"</td>";
		str+="\n<td width='20%' class=\"tdprint\">"+cleanDataB(this.mData[i][7])+"</td>";
		str+="\n</tr>";
	}
	str+="\n</tbody>";
	str+="\n</table>";
	str+="<br><br>";		
	return str;
}

function LV_printTableAlarm(){
	
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str = "<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\">";
	
	//check if there are "area" and "group" columns
	var area = false;	
	if (this.numRows>0 && this.mData[0].length>10)
		area = true;
		
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	//str+="\n<th class=\"thprint\">"+this.aHeader[2]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[5]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[6]+"&nbsp;</th>";
	
	if (area)
	{
		str+="\n<th class=\"thprint\">"+this.aHeader[7]+"&nbsp;</th>";
//		str+="\n<th class=\"thprint\">"+this.aHeader[8]+"&nbsp;</th>";
	}
	
	str+="\n</thead>";	
	
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr >";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		//str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][2+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][3+3])+"&nbsp;</td>";
		
		str+="\n<td class=\"tdprint\" "+(area?"align='center'":"")+">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"align='center'":"")+">"+cleanData(this.mData[i][5+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][6+3])+"&nbsp;</td>";
		
		if (area)
		{
			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][7+3])+"&nbsp;</td>";
//			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][8+3])+"&nbsp;</td>";
		}
		str+="\n</tr>";
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;

}//LV_printTableAlarm


function LV_printTableAlarmDtl(){
	
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str = "<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\">";
	
	//check if there are "area" and "group" columns
	var area = false;	
	if (this.numRows>0 && this.mData[0].length>9)
		area = true;	
	
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[2]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[5]+"&nbsp;</th>";
	
	if (area)
	{
		str+="\n<th class=\"thprint\">"+this.aHeader[6]+"&nbsp;</th>";
//		str+="\n<th class=\"thprint\">"+this.aHeader[7]+"&nbsp;</th>";
	}
	
	str+="\n</thead>";	
	
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr >";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][2+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][3+3])+"&nbsp;</td>";
		
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][5+3])+"&nbsp;</td>";
		
		if (area)
		{
			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][6+3])+"&nbsp;</td>";
//			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][7+3])+"&nbsp;</td>";
		}
		str+="\n</tr>";
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;

}//LV_printTableAlarmDtl



function LV_printTableAlarmCall(){
	
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\">";
	
	//check if there are "area" and "group" columns
	var area = false;
	if (this.numRows>0 && this.mData[0].length>12)
		area = true;
		
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[5]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[6]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[7]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[8]+"&nbsp;</th>";
	
	if (area)
	{
		str+="\n<th class=\"thprint\">"+this.aHeader[9]+"&nbsp;</th>";
//		str+="\n<th class=\"thprint\">"+this.aHeader[10]+"&nbsp;</th>";
	}
	
	str+="\n</thead>";	
	
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr >";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][3+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][5+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][6+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][7+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][8+3])+"&nbsp;</td>";
		if (area)
		{
			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][9+3])+"&nbsp;</td>";
//			str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][10+3])+"&nbsp;</td>";
		}

		str+="\n</tr>";
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;


}//LV_printTableAlarmCall

function LV_printTableEvent(){
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\">";

	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[2]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>";
	
	str+="\n</thead>";		
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr >"
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+(this.mData[i][2+3])+"</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][3+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>";
		str+="\n</tr>";
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;


}//LV_printTableEvent
function LV_printTableAlarmEventSearch(){
	
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\" cellpadding=\"0\">";
	
	var area = false;
	for (var i=0; i<this.numRows; i++)
	{
		if ((this.mData[i][5+3])!=""||(this.mData[i][6+3])!="")
			area = true;
			break;
	}
	
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[2]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[5]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+document.getElementById("str_priority").value+"&nbsp;</th>";
	str+="\n</thead>";	
	
	for (var i=0; i<this.numRows; i++)
	{
		str+="\n<tr >";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][2+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" align=\"center\">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][5+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" align=\"center\">"+this.mData[i][4+3].split("<div style='visibility:hidden;display:none'>")[1].split("</div>")[0]+"&nbsp;</td>";
		str+="\n</tr>";
		
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;
}
function LV_printTableAlarmSearch(){
	
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+this.title+"</b><br><br>"+strTime+"<br><br>\n<table cellspacing=\"0\" cellpadding=\"0\">";
	
	var area = false;

	//check if there are "area" and "group" columns
	if (this.numRows>0 && this.mData[0].length>13)
		area = true;
	
	str+="\n<thead>";
	str+="\n<th class=\"thprint\">"+this.aHeader[0]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[1]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[2]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[3]+"&nbsp;</th>";
	//str+="\n<th class=\"thprint\">"+this.aHeader[4]+"&nbsp;</th>"; //colonna del link al dettaglio dev.
	str+="\n<th class=\"thprint\">"+this.aHeader[5]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\">"+this.aHeader[6]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\" >"+this.aHeader[7]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\" >"+this.aHeader[8]+"&nbsp;</th>";
	str+="\n<th class=\"thprint\" >"+this.aHeader[9]+"&nbsp;</th>";
	if (area)
	{
		str+="\n<th class=\"thprint\" >"+this.aHeader[10]+"&nbsp;</th>";  
		str+="\n<th class=\"thprint\" >"+this.aHeader[11]+"&nbsp;</th>";   
	}
	str+="\n</thead>";	
	
	for (var i=0; i<this.numRows; i++){
		str+="\n<tr >";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][0+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][1+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][2+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][3+3])+"&nbsp;</td>";
		//str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][4+3])+"&nbsp;</td>"; //colonna del link al dettaglio dev.
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][5+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\">"+cleanData(this.mData[i][6+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][7+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" "+(area?"":"align='center'")+">"+cleanData(this.mData[i][8+3])+"&nbsp;</td>";
		str+="\n<td class=\"tdprint\" align='center'>"+cleanData(this.mData[i][9+3])+"&nbsp;</td>";
		if (area)
		{
			str+="\n<td class=\"tdprint\" align='center' >"+cleanData(this.mData[i][10+3])+"&nbsp;</td>";   
			str+="\n<td class=\"tdprint\" align='center' >"+cleanData(this.mData[i][11+3])+"&nbsp;</td>";   
		}
		str+="\n</tr>";
	}//for
	str+="\n</table>";
	str+="<br><br>";		
	return str;

}//LV_printTableAlarmSearch

function openPrintPage(str){
	var objTagBase = document.getElementsByTagName("BASE");
	var htmlStr="\n<html>\n<head>\n<base href=\""+objTagBase[0].href +"\"><link rel=\"stylesheet\" href=\""+objTagBase[0].href+"stylesheet/plantVisorIE.css\" type=\"text/css\" />\n<style>";
	htmlStr+=".tdprint {font-family: Tahoma;font-size: 8pt;border-width: 1px;border-color: #152525;border-spacing: 0px;padding: 1px 0.5em;border-style: solid;}";
	htmlStr+="</style></head>\n<body>";
	htmlStr+="\n"+str+"<br><br>\n</body>\n</html>";
	var newin=top.window.open('','titolo','menubar=yes,scrollbars=yes,resizable=yes,status=no,location=no,toolbar=yes');
	newin.document.write(htmlStr);
	newin.document.close();
}//openPrintPage

function cleanData(data){
	var str="";
	
	if((data!=null)&&(data!=""))
		str+="&nbsp;"+data;	
	str+="&nbsp;";
	return str;
}//cleanData

function cleanDataB(data){
	var str="";
	
	
	if(data!=null&&data!="")
	{
		var ccc = document.createElement("DIV");
		document.body.appendChild(ccc);
		ccc.innerHTML = data;
				
		var datas = ccc.getElementsByTagName("div"); ;
		
		var data0 = datas[0].childNodes[0].nodeValue;
		var data1 = datas[1].childNodes[0].nodeValue;
		
		if(data0!=null&&data0!="")
		{
			str+=data0 + "&nbsp;"; 
		}
		else 
			str+="&nbsp";
		if(data1!=null&&data1!="")
		{
			str+=data1;
		}
		else 
			str+="&nbsp";
	}
	else 
		str+="&nbsp";
	return str;
}

function cleanDateDevice(data){
	var value= new Array();
	if(data==null){
		value[0]="";
		value[1]="";
		return value;
	}//if
	
	var tmp = new Array();  
	tmp= data.split("<");
	if(tmp[1]!=null){
		var tmp1= new Array();
		tmp1=tmp[1].split(">");
		value[0]=tmp1[1];
	}//if
	else{
	value[0]="";
	}//else
	if(tmp[3]!=null){
		var tmp1= new Array();
		tmp1=tmp[3].split(">");
		value[1]=tmp1[1];
	}//if
	else{
	value[0]="";
	}//else
	return value;
}//cleanDateDevice

function LV_exportAlEv(){
	var str="";
	var tmpsrc = "";
	var tmpsrc2 = "";
//  				str+=this.aHeader[0]+";";
//  				str+=this.aHeader[1]+";";
//  				str+=this.aHeader[2]+";";
//  				str+=this.aHeader[3]+";";
//  				str+=this.aHeader[4]+";";
//  				str+="\n";	
				for (var i=0; i<this.numRows; i++){
  					//alert(TTT.mData[i][0+3]);
  					//if(exp_id.indexOf())
  					/*
  					if(this.mData[i][4+3].length>0&&exp_id.indexOf("A")==-1)
  						continue;
  					if(this.mData[i][4+3].length==0&&exp_id.indexOf("E")==-1)
  						continue;
  					*/
  					if((this.mData[i][4+4].length>0&&exp_id.indexOf("A")!=-1) || (this.mData[i][4+4].length==0&&exp_id.indexOf("E")!=-1))
  					{
	  					str+=this.mData[i][0+3].split("</div>")[1]+";";
	  					
	  					tmpsrc = this.mData[i][1+3].split("&nbsp;")[0];
	  					//caso evento con link attivo:
	  					if (tmpsrc.split("<a")[1]==undefined)
	  						str+=tmpsrc+" - ";
	  					else
	  					{
	  						if (tmpsrc.split("<a")[1]==undefined)
	  							str+=tmpsrc;
	  						else
	  						{
	  							str+=tmpsrc.split("<a")[0];
	  							tmpsrc2 = tmpsrc.split("<u>")[1];
	  							str+=tmpsrc2.split("</u>")[0];
	  							str+=tmpsrc2.split("</a>")[1]+" - ";
	  						}
	  					}
	  					//str+=this.mData[i][1+3].split("&nbsp;")[0]+";";
	  					
	  					str+=this.mData[i][2+3].split("&nbsp;")[0]+";";
	  					str+=(this.mData[i][4+3].split("&nbsp;")[0]=="<img src='images/ok.gif'/>"?"":this.mData[i][4+3].split("&nbsp;")[0])+";";
	  					str+=(this.mData[i][5+3].split("</div>")[1]==undefined?'':this.mData[i][5+3].split("</div>")[1])+";\n";
  					}
  				}

  				saveiframe.document.clear();
  				saveiframe.document.open();
  				saveiframe.document.write(str);
  				saveiframe.document.execCommand('SaveAs',false,'alarms_events_export.csv');
  				saveiframe.document.clear();
  				saveiframe.document.close();
  				flowNeedSave = false;
	
}//printTable

function pageSetInput_keydown(obj,idTable,event)
{
	if(event.keyCode == 13)
	{
		pageSetInput_enter(obj,idTable);
	}
}
function pageSetInput_imgClick(idTable)
{
	var obj = document.getElementById("ListViewPaging_"+idTable);
	if(obj != null)
		pageSetInput_enter(obj,idTable);
}
function pageSetInput_enter(obj,idTable)
{
	var oLswTmp = null;
  	var sHtml = "";
  	if(top.frames['body'].frames['bodytab'].oLswContainer != null)
  	{
  		for(var i=0; i<top.frames['body'].frames['bodytab'].oLswContainer.getSize(); i++)
  		{
  			oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(i);
  			if(oLswTmp.idTableNum == idTable)
  			{
  				var value = Number(obj.value);
  				if(value<1)
  				{
  					value = 1;
  				}
  				else if(value>oLswTmp.totalpage)
  				{
  					value=oLswTmp.totalpage;
  				}
  				oLswTmp.currentPage = value;
  				oLswTmp.updatePage("pagenumber");
  			}
  		}
  	} 		
}
