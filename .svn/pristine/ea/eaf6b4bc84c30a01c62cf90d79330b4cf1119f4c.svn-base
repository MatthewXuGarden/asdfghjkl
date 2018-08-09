// menu.js
var btnPrev = null;
var btnNext = null;


function onLoadMenu()
{
	btnPrev = document.getElementById("btnPrev");
	btnNext = document.getElementById("btnNext");
	TiStartClock('pvpro_time');	
}


function onSelectPage(page)
{
	top.frames['body'].frames['bodytab'].location.href = top.frames['manager'].getDocumentBase()
		+ "mobile/" + page + ";"
		+ top.frames['manager'].getSessionId();
}

function goToMaps()
{
	top.frames['manager'].loadTrx('nop&folder=mstrmaps&bo=BMasterMaps&type=click');
}


function onLogout()
{
	if( confirm(document.getElementById("exit_confirmation").value ) )
		top.frames['manager'].Redirect("servlet/logout");
}


function showPrev()
{
	btnPrev.style.display = "block";
}


function hidePrev()
{
	btnPrev.style.display = "none";
}


function onBtnPrev()
{
	top.frames['body'].frames['bodytab'].onBtnPrev();
}


function showNext()
{
	btnNext.style.display = "block";
}


function hideNext()
{
	btnNext.style.display = "none";
}


function onBtnNext()
{
	top.frames['body'].frames['bodytab'].onBtnNext();
}


function hideRightButtons()
{
	hidePrev();
	hideNext();
}
