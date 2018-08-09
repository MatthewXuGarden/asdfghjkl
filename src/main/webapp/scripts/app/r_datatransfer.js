//TAB1
function save_site()
{
	var ofrm = document.getElementById("site_form");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}



//TAB3
function save_amm()
{
	var ofrm = document.getElementById("amm_form");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

/**
* Per ridimensionare la tabella Siti (TAB1)
*/
function resizeTableSiteList()
{
	var hdev = MTcalcObjectHeight("trSiteList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-10);
	}
}