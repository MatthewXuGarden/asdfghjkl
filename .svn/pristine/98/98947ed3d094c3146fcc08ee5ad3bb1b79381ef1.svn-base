/*
  * SlideMenu.js
*/
var GLB_DIV_MENU_CONTAINER = "crossDivNew";
var FRAME_1 = "body";
var FRAME_2 = "bodytab";

function SmMouseOver2(td)
{	
	var bgurl = td.style.backgroundImage;
	var bgurl_moveover = bgurl.substring(0,bgurl.lastIndexOf(".")-1)+"2"+bgurl.substring(bgurl.lastIndexOf("."),bgurl.length);
	td.style.backgroundImage = bgurl_moveover;
}
function SmMouseOut2(td)
{
	var bgurl = td.style.backgroundImage;
	var bgurl_moveout = bgurl.substring(0,bgurl.lastIndexOf(".")-1)+"1"+bgurl.substring(bgurl.lastIndexOf("."),bgurl.length);
	td.style.backgroundImage = bgurl_moveout;
}
function SmRemoveIconMouseout(td)
{
	td.onmouseout = new Function("");
}
function SmAddIconMouseout(td)
{
	td.onmouseout = new Function("SmMouseOut2(this)");
}
function SmClick2(td,divId)
{
	SmSmallIconAddMouseover(td);
	SmRemoveIconMouseout(td);
	var oTop1 = null;
	var oTop2 = null;
	oTop1 = top.frames[FRAME_1];
	
	if(oTop1 != null && oTop1 != undefined)
		oTop2 = oTop1.frames[FRAME_2];
	if(oTop2 == null || oTop2 == undefined)
		return;
	if(oTop2.document.getElementById(GLB_DIV_MENU_CONTAINER) == null)
		return;
	var div_menu = oTop2.document.getElementById(GLB_DIV_MENU_CONTAINER);
	div_menu.style.width = top.frames["manager"].MENU_WIDTH +"px";
	div_menu.style.height = top.frames["manager"].MENU_HEIGHT+"px";
	div_menu.innerHTML = document.getElementById(divId).innerHTML;
	div_menu.style.visibility="visible";
	div_menu.style.display = "block";
	var alarm = top.frames["allarm"];
	div_menu.style.top = oTop1.document.body.clientHeight-div_menu.clientHeight-30+"px";
	locObj = td;
	var curLeft = 0;
	while (locObj.offsetParent)
	{
		curLeft += locObj.offsetLeft;
		locObj = locObj.offsetParent;
	}
	div_menu.style.left = curLeft+alarm.document.body.clientWidth+td.clientWidth/2-div_menu.clientWidth/2+"px";
	//at first hide to hide the old veil. It is necessary!
	oTop2.hideMenuVeil();
	oTop2.showMenuVeil();
	//reset menu div's z-index
	div_menu.style.zIndex = "99999";
}
function SmHideMemu()
{
	var oTop1 = null;
	var oTop2 = null;
	oTop1 = top.frames[FRAME_1];
	if(oTop1 != null && oTop1 != undefined)
		oTop2 = oTop1.frames[FRAME_2];
	if(oTop2 != null && oTop2 != undefined)
	{
		var div_menu = oTop2.document.getElementById(GLB_DIV_MENU_CONTAINER);
		div_menu.style.visibility="hidden";
		div_menu.style.display = "none";
		oTop2.hideMenuVeil();
		SmSmallAllIconAddMouseover();
	}
}
function SmSmallAllIconAddMouseover()
{
	var tr = document.getElementById("menutr");
	var tds = tr.childNodes;
	for(var i=0;i<tds.length;i++)
	{
		SmAddIconMouseout(tds[i]);
		SmMouseOut2(tds[i]);
	}
}
function SmSmallIconAddMouseover(except)
{
	var tr = document.getElementById("menutr");
	var tds = tr.childNodes;
	for(var i=0;i<tds.length;i++)
	{
		if(tds[i] != except)
		{
			SmAddIconMouseout(tds[i]);
			SmMouseOut2(tds[i]);
		}
	}
}
