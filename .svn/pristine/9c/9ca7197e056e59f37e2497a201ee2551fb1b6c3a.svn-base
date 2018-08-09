var arActBtt = new Array();
var prefixDiv = "ActBttDiv";
var prefixImg = "ActBttImg";
var prefixEn = "En";
var prefixDs = "Ds";
var bttnum = 0;
var canCommit = true;

function getSessId()
{
	return top.frames['manager'].getSessionId();
}

// User API - Start
function enableAllAction() {
	MAenableAll();
}

function disableAllAction() {
	MAdisableAll();
}

function enableAction(idx) {
	MAenable(idx);	
}

function disableAction(idx) {
	MAdisable(idx);	
}

function overAction(idx,imgsrc) {
	MAchangeImage(idx,imgsrc);
}

function outAction(idx,imgsrc) {
	MAchangeImage(idx,imgsrc);	
}

function isEnableAction(idx) {
	var objdiv = document.getElementById((prefixDiv+prefixEn+idx));
	var ret = false;
	if(objdiv != null)
	{
		if(objdiv.style.display == "block")
			ret = true;	
	}
	return ret;
}

// User API - End

function MAButtonNum(numbtt) {
	bttnum = numbtt;
	MAdisableAll();
}

function MAenableAll()
{
	for(var i=1; i<=bttnum; i++)	
		MAchangeState(i,true);
}

function MAdisableAll()
{
	for(var i=1; i<=bttnum; i++)	
		MAchangeState(i,false)	;
}

function MAenable(idx) {
	MAchangeState(idx,true);
}

function  MAdisable(idx) {
	MAchangeState(idx,false);
}

function MAchangeImage(idx,imgsrc) {
	var objimg = document.getElementById((prefixImg+prefixEn+idx));
	if(objimg != null)
		objimg.src = imgsrc;
}

function MAchangeState(idx,state)
{
	var curobjDivEn = null;
	var curobjDivDs = null;
	
	curobjDivEn = document.getElementById((prefixDiv+prefixEn+idx));
	curobjDivDs = document.getElementById((prefixDiv+prefixDs+idx));
	if(curobjDivEn != null && curobjDivDs != null)
	{
		if(state)
		{
			curobjDivEn.style.visibility="visible";
			curobjDivEn.style.display="block";
			curobjDivDs.style.visibility="hidden";
			curobjDivDs.style.display="none";
		}
		else
		{
			curobjDivEn.style.visibility="hidden";
			curobjDivEn.style.display="none";
			curobjDivDs.style.visibility="visible";
			curobjDivDs.style.display="block";
		}
	}
}

function go_to_device()
{
	var target_device = document.getElementById("combo_dev").value;
	top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=redirect&iddev='+target_device+'&desc=ncode01');
}
function go_to_group()
{
	var str = document.getElementById("combo_group").value;
	top.frames['manager'].Redirect("servlet/master","?trx="+str,top.frames['body'].frames['bodytab']);
}
function MApostWithKey(sFunCommit,e,isCustom)
{
	if(isCustom) return;
	var objMsg = "";
	if(sFunCommit == null || sFunCommit == "NOP" || canCommit== false)
		return false;
	
	if(e.keyCode == 13)
	{		
		objMsg = document.getElementById("s_commit");
		if(objMsg != null)
		{
			try
			{
				if(confirm(document.getElementById("s_commit").value))
				{
					canCommit = false;
					eval(sFunCommit);
				}
			}
			catch(e){}
		}
	}
}