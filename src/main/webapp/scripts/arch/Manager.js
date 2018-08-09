var m_cnt;										
var m_objSpanCollection;
var m_menuHeightCollection = new Array();
var previousMenu;					
var menuMain = new MenuMain(3);
var m_MM_selected = null;
var jsessionid = null;

// Server Comm
var m_glb_communication = 0;

var ManagerObjTarget = null;
var ManagerObjTimer = 0;

//2010-3-10, add by Kevin, used to click outside menu, hide the menu
//if click inside menu, set crossDivNewClicked = true
//onmousedown will check crossDivNewClicked
//if == true, means click inside menu
//if == false, means click outside menu
var crossDivNewClicked = false;
function crossDivNewClick()
{
	crossDivNewClicked = true;
}
//--


function getSessionId()
{
	var tmp = "jsessionid="+jsessionid;
	return tmp;
}

function Item(label, link)
{
	this.label = label;
	this.link = link;
}

function setSession(data)
{
	jsessionid = data;
}
function MenuTag(head, length, imgname,bgImg,bgColor)
{
	this.head = head;
	this.item = new Array(length);
	this.size = -1;
	this.img = imgname;
	this.bgImg = bgImg;
	this.bgColor = bgColor;
}

function MenuMain(length, eLabel, eQuestion)
{
	this.menu = new Array(length);
	this.count = -1;
	this.exitLabel = eLabel;
	this.exitQuestion = eQuestion;
}

function addItem(menu, label, link)
{
	var o = new Item(label,link);
	menu.size++;
	menu.item[menu.size]=o;
}

function addMenu(head, length, imgsrc,bgImg,bgColor)
{
	var o = new MenuTag(head, length, imgsrc,bgImg,bgColor);
	menuMain.count++;
	menuMain.menu[menuMain.count]=o;
	return o;
}

function loadTrx(trxName)
{
	Redirect("servlet/master","?trx="+trxName, top.frames['body']);
}

function loadTrxTab(trxName)
{
	Redirect("servlet/master","?trx="+trxName, top.frames['body'].frames['bodytab']);
}

function loadTrxBody(trxName)
{
	Redirect("servlet/master","?trx="+trxName, top.frames['body'].frames['bodytab']);
}

function evid(currLoc, name)
{

	if(currLoc != null && currLoc.className != "Menu_AreaSelected")
		currLoc.className=name;
	
	// Gestione Macro Categoria selezionata
	if((currLoc != null) && (currLoc.id != "undefined"))
	{
		if((m_MM_selected == null) && (currLoc.id == "td_0") && (name != "Menu_AreaOver"))
		{
			m_MM_selected = currLoc;
			m_MM_selected.className = "Menu_AreaSelected";		
		}
			
		if((m_MM_selected != null) && (name != "Menu_AreaOver"))
			m_MM_selected.className = "Menu_AreaSelected";
	}
}

function loadLanguage(value)
{
	Redirect("servlet/login","?language="+value);
}

function reload()
{
	var oDiv = top.frames["menuPVPRO2"].document.getElementById("MainMenu");
	
	if(oDiv != null)
	{
		var html2 = CreateMenuHtml2();
		top.frames["menuPVPRO2"].document.getElementById("MainMenu").innerHTML = html2;
	}
	else
		setTimeout("reload()",500);
}
	
function alarmPoller() 
{
	setTimeout("alarmPoller()",2000);
}
	
function InitializeMenu() 
{
	reload();
}

function ControlMenu() 
{
	objMenu = this.parentNode.childNodes[1];
	if (previousMenu != objMenu)
	{	
		for (var i = 0; i < m_objSpanCollection.length; i++)
		{
			var objSpan = m_objSpanCollection[i];
			objSpan.childNodes[1].style.display = "none";
			objSpan.childNodes[1].childNodes[0].style.display = "none";
		}
	}
	m_cnt = 1;	
	if (objMenu.style.display == "none")
	{
		ShowMenu(objMenu);
		previousMenu = objMenu;
	}
	else
	{
		HideMenu(objMenu);
		previousMenu = null;
	}
}

function ShowMenu(objMenu)
{
	var objList = objMenu.childNodes[0];	// get the Linkslist of the Menulist
	if (m_cnt < 10)
	{
		objMenu.style.display = "block";
		for (var i = 0; i < m_objSpanCollection.length; i++)
			if (objMenu.parentNode == m_objSpanCollection[i])
				   objMenu.style.height = objMenu.clientHeight + (m_menuHeightCollection[i]/10);
				   
		m_cnt++;
		ShowMenu(objMenu);
	}
	if (m_cnt >= 10)
	{
		
		for (var i = 0; i < m_objSpanCollection.length; i++)
			if (objMenu.parentNode == m_objSpanCollection[i])
				objMenu.style.height = m_menuHeightCollection[i];
		objList.style.display = "block"; 
	}
}

function HideMenu(objMenu)
{	
	var objList = objMenu.childNodes[0];	// get the Linkslist of the Menulist
	if (m_cnt < 10)
	{
		for (var i = 0; i < m_objSpanCollection.length; i++)
			if (objMenu.parentNode == m_objSpanCollection[i])
				if (objMenu.clientHeight > m_menuHeightCollection[i]/10)
				objMenu.style.height = objMenu.clientHeight - (m_menuHeightCollection[i]/10);
		objList.style.display = "none";
		m_cnt++;
		HideMenu(objMenu);
	}
	if (m_cnt >= 10)
	{
		objMenu.style.height = 0;
		objMenu.style.display = "none";
	}
}
	
/**
 * Funzione richiamata per il logout dell'utente.
 * Richiama la servlet di logout che effettua un invalidazione della sessione.
*/
function logoutUser(from)
{
	if(from == 1)
		Redirect("servlet/logout");
	else
	{
		if(confirm (menuMain.exitQuestion))
			Redirect("servlet/logout");
	}
}

/**
 * Questa funzione viene introdotta per gestire il redirect tramite l'oggetto 
 * top.location javascript.
 * strWhere: Stringa con l'url da caricare
 * objFrame: Oggetto frame sul quale caricare l'url. Nel caso sia null or undefined viene considerato top.
*/
function Redirect(strWhere,strParam, objFrame)
{
	try {
		MgrLoadTrxGraph(true);
	}
	catch(e){}
	
	var strDocBase = getDocumentBase();
	if (strParam == null)
	{
		strParam = "";
	}
	var tmp = strDocBase + strWhere + ";"+getSessionId()+strParam;
	
	clearTimeout(ManagerObjTimer);
	
	if(objFrame != null && objFrame != undefined)
	{
		ManagerObjTarget = objFrame;
		ManagerObjTimer = setTimeout("delayRequest('"+tmp+"');",300);
		//objFrame.location.href = tmp;
	}
	else
	{
		ManagerObjTarget = top;
		ManagerObjTimer = setTimeout("delayRequest('"+tmp+"')",300);
		//top.location.href = tmp;
	}
}

function delayRequest(objUrl)
{
	if(ManagerObjTarget != null)
		ManagerObjTarget.location.href = objUrl;
}

function getDocumentBase()
{
	var objTagBase = document.getElementsByTagName("BASE");
	var strDocBase = "";
	
	if(objTagBase != null && objTagBase != undefined)
		strDocBase = objTagBase[0].href;
	
	return strDocBase;
}

// Server Communication Functions
function setServerCom() {
	m_glb_communication = 1;
}
function resetServerCom() {
	m_glb_communication = 0;
}
//by Kevin
function checkHideMenuDiv()
{
	if(crossDivNewClicked == true)
	{
		crossDivNewClicked = false;
	}
	else
	{
		var oTop1 = null;
		var oTop2 = null;
		oTop1 = top.frames["body"];
		if(oTop1 != null)
			oTop2 = oTop1.frames["bodytab"];
		var div_menu = oTop2.document.getElementById("crossDivNew");
		if(div_menu.style.visibility == "visible")
		{
			top.frames["menuPVPRO2"].SmHideMemu();
		}
	}
}
//--
function checkServerCom() {
	if(m_glb_communication == 1) {
		var msg = document.getElementById("msg02").value
		if(msg == null || msg == "")
			msg = "Server Communication Running. Please wait.";

		alert(msg);
	}
}
function isServerCom() {
	if(m_glb_communication == 1)
		return true;
	else
		return false;
}


function canChangeLink(trx_link)
{
	if(top.frames['body'] != null)
	{
		if(top.frames['body'].frames['bodytab'] != null)
		{
			try 
			{
				if(!top.frames['body'].frames['bodytab'].MioAskModUser())
					return false;
			}
			catch(e){}
		}
	}
	
	top.frames['manager'].loadTrx(trx_link);
		
}

function MgrLoadTrxGraph(bState)
{
	var objTab = null;
	try
	{
		objTab = top.frames['header'];
		if(objTab != null)
		{
			var objDivLoading = objTab.document.getElementById("loadingMaskContainer");
			if(objDivLoading != null)
			{
				if(bState)
				{
					objDivLoading.style.backgroundImage="url(images/ajax-loader_black.gif)";
					objDivLoading.style.backgroundRepeat="no-repeat";
					objDivLoading.innerHTML = "<font size='8'>&nbsp;</font>";
					
					top.frames['body'].frames['Dummy'].document.body.style.cursor="wait";
					top.frames['body'].frames['bodytab'].document.body.style.cursor="wait";
				}
				else
				{
					objDivLoading.style.backgroundImage="";
					objDivLoading.innerHTML = "";
					
					top.frames['body'].frames['Dummy'].document.body.style.cursor="default";
					top.frames['body'].frames['bodytab'].document.body.style.cursor="default";
				}
			}
		}
	}
	catch(e) {}	
}
function collectResolution(){
	var param= "?cmd=manager_initresolution";
	var screenw,screenh;
	try {
		screenh= screen.height;
	}catch(e){ 
		screenh = 768; 
	}
	
	try { 
		screenw = screen.width; 
	}catch(e){
		screenw = 1024;
	}
	param+="&screenw="+screenw+"&screenh="+screenh;
	var subWin=window.open(GetBase()+"ExternalAccess;" + getSessionId() +param,"","height=10, width=40, toolbar= no, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=2000,left=3000");
	subWin.close();
//	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_collectResolution, true);
	return true;
}
function Callback_collectResolution(){
}
function GetBase() {
	var oBaseColl = document.all.tags('BASE');
	return ((oBaseColl && oBaseColl.length) ? oBaseColl[0].href : null);
}