
function goBackToAC()
{
	var menu_item = document.getElementById("menu_item").value;
	top.frames['manager'].loadTrx('nop&folder=ac&bo=BAC&type=menu&desc='+menu_item+'');
}

function go2DtlDev(iddev)
{
	top.frames['manager'].loadTrx('nop&iddev='+iddev+'&type=click&folder=dtlview&bo=BDtlView&desc=ncode01');
}
