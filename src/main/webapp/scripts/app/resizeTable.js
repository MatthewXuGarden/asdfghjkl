/**
* Per ridimensionare la tabella per la ricerca allarmi trovati
*/
function resizeTableAlarmFound()
{
	var hdev = MTcalcObjectHeight("trAlrList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-60);
	}
}

/**
* Per ridimensionare la tabella per la ricerca eventi trovati
*/
function resizeTableEventFound()
{
	var hdev = MTcalcObjectHeight("trEvnList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-60);
	}
}

/**
* Per ridimensionare la tabella archivio report
*/
function resizeTableArchReport()
{
	var hdev = MTcalcObjectHeight("trArcRepList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-30);
	}
}

/**
* Per ridimensionare la tabella archivio report
*/
function resizeTableArchAlrReport()
{
	var hdev = MTcalcObjectHeight("trAlrRepList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-50);
	}
}


/**
* Per ridimensionare la tabella dispositivi logici
*/
function resizeTableLogicDevice()
{
	var hdev = MTcalcObjectHeight("trDevLogicList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-10);
	}
}

/**
* Per ridimensionare la tabella dispositivi logici
*/
function resizeTableVarLogicDevice()
{
	var hdev = MTcalcObjectHeight("trVarLogicList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-10);
	}
}

/**
* Per ridimensionare la tabella dispositivi logici
*/
function resizeTableDevLogList()
{
	var hdev = MTcalcObjectHeight("trLogicList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-20);
	}
}


/**
* Per ridimensionare la tabella plugin
*/
function resizeTablePlugin()
{
	var hdev = MTcalcObjectHeight("trPluginList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-30);
	}
}

/**
* Per ridimensionare la tabella lista gruppi
*/
function resizeTableGroupView()
{
	var hdev = MTcalcObjectHeight("trGroupList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-10);
	}
}


