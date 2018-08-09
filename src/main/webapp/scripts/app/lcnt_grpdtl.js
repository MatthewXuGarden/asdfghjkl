function load_tab1()
{
	setTimeout("reloadPage()",60000);
}

function goBackToLN()
{
	var menu_item = document.getElementById("menu_item").value;
	
	top.frames['manager'].loadTrx('nop&folder=lucinotte&bo=BLuciNotte&type=menu&desc='+menu_item+'');
}

function go2DtlDev(iddev)
{
	top.frames['manager'].loadTrx('nop&iddev='+iddev+'&type=click&folder=dtlview&bo=BDtlView&desc=ncode01');
}

function reloadPage()
{
	document.location.reload();
	setTimeout("reloadPage()",60000);
}
