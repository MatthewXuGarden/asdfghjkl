// util.js, jt_.js required
var idInputField	= null;
var selectedPlace	= null;
var selectedFolder	= null;
var selectedFileRow	= null;
var extensionFilter	= null;
var FOLDER			= "images/filedialog/folder_opened.png";
var FILE			= "images/filedialog/file.png";
var caption			= null;
var fnNotify		= null;
var bOk				= false;
var bLoad			= false;
var bFNChanged		= false;	// file name changed by the user or fdSetFile
var pathFTP			= "";
var nError			= 0;


function fdSaveFile(id, filter, notify, init)
{
	fnNotify = notify;
	if( fdLocal ) {
		if( init )
			init();
		onFileDialog(false, id, filter);
	}
	else if( fnNotify )
		fnNotify(fdLocal);
}


function fdLoadFile(id, filter, notify)
{
	fnNotify = notify;
	if( fdLocal )
		onFileDialog(true, id, filter);
	else if( fnNotify )
		fnNotify(fdLocal);
}


function fdSetFile(fileName)
{
	var obj = document.getElementById("fdFileName");
	if(obj != null && obj != undefined)
	{
		obj.value = fileName;
		bFNChanged = true;
		checkControls();
	}
}


function onFileDialog(b, id, filter)
{
	bLoad = b;
	idInputField = id;
	extensionFilter = filter;
	caption = document.getElementById(bLoad ? "fdLoad" : "fdSave").value;
	var table = document.getElementById("fdCaption").getElementsByTagName("tbody")[0];
	table.rows[0].cells[0].firstChild.nodeValue = caption;
	if( bLoad ) {
		document.getElementById("fdFileName").setAttribute("readonly", bLoad);
		//document.getElementById("fdFileName").removeAttribute("onChange");
	}
	else {
		document.getElementById("fdFileName").removeAttribute("readonly");
		//document.getElementById("fdFileName").setAttribute("onChange", "onFileNameChanged()");
	}
	showLayer("divFileDialog", true);
	jt_BodyZ.toTop(document.getElementById("divFileDialog"));
	jt_Veil.show(true);
	// get places
	CommSend("servlet/ajrefresh", "GET", "cmd=FileDialog&path=&filter=", "FileDialog", true);
}


function onFileDialogOk()
{
	if( !checkControls() ) {
		alert(document.getElementById("fdSetAllReq").value);
		return;
	}
	
	var path = "";
	if( selectedFolder != null ) {
		var fileName = document.getElementById("fdFileName").value;
		if( fileName.length > 0 )
			path = selectedFolder + "/" + fileName;
	}

	var inputField = document.getElementById(idInputField);
	if( inputField ) {
		inputField.value = path;
		if( inputField.onchange != null )
			inputField.onchange();
	}

	closeFileDialog();

	if( fnNotify ) {
		fnNotify(fdLocal, path, fileName);
		fnNotify = null;
	}
}


function onFileDialogCancel()
{
	closeFileDialog();
	fnNotify = null;
}


function closeFileDialog()
{
	idInputField = null;
	selectedFolder = null;
	selectPlace(-1, null);
	bOk = false;
	bFNChanged = false;
	clearFiles();
	jt_Veil.show(false);
	showLayer("divNetworkFolder", false);
	showLayer("divFileDialog", false);
}


function Callback_FileDialog(xml)
{
	var xmlRoot = xml.getElementsByTagName("FileDialog")[0];
	var type = xmlRoot.getAttribute("type");
	if( type == "files" ) {
		var htmlTable = "<table width='100%' cellspacing='0' style='table-layout:fixed;'>";
		selectedFolder = xmlRoot.getElementsByTagName("path")[0].getAttribute("name");
		var folders = xmlRoot.getElementsByTagName("folder");
		var table = document.getElementById("fdCaption").getElementsByTagName("tbody")[0];
		table.rows[0].cells[0].firstChild.nodeValue = caption + " " + selectedFolder;
		if( isParentFolder(selectedFolder) ) {
			htmlTable += "<tr valign='middle' onClick='onFolderSelected(this)' onDblClick='onFolderChanged(this)'>";
			htmlTable += "<td align='center' width='10%'><img src='" + FOLDER + "'></td>";
			htmlTable += "<td>..</td>";
			htmlTable += "</tr>";
		}
		for(var i = 0; i < folders.length; i++) {
			var folder = folders[i]; 
			htmlTable += "<tr valign='middle' onClick='onFolderSelected(this)' onDblClick='onFolderChanged(this)'>";
			htmlTable += "<td align='center' width='10%'><img src='" + FOLDER + "'></td>";
			htmlTable += "<td>"	+ folder.getAttribute('name') + "</td>";
			htmlTable += "</tr>";
		}
		var files = xmlRoot.getElementsByTagName("file");
		for(var i = 0; i < files.length; i++) {
			var file = files[i]; 
			htmlTable += "<tr valign='middle' onClick='onFileSelected(this)'>";
			htmlTable += "<td align='center' width='10%'><img src='" + FILE + "'></td>";
			htmlTable += "<td>"	+ file.getAttribute('name') + "</td>";
			htmlTable += "</tr>";
		}
		htmlTable += "</table>";
		document.getElementById("fdFiles").innerHTML = htmlTable;
		nError = 0;
		var errors = xmlRoot.getElementsByTagName('error');
		if( errors.length > 0 ) {
			nError = parseInt(errors[0].getAttribute('code'), 10);
			alert(document.getElementById("fdError" + nError).value);
		}
	}
	else if( type == "places" ) {
		var htmlTable = "<table id='fdPlaces' name='fdPlaces' align='center' width='98%' cellspacing='15'>";
		var places = xmlRoot.getElementsByTagName("place");
		for(var i = 0; i < places.length; i++) {
			htmlTable += "<tr onClick=\"onPlaceChanged(" + i + ",'" + places[i].getAttribute("path") + "')\">";
			htmlTable += "<td align='center'>";
			htmlTable += "<img src='" + places[i].getAttribute("icon") + "'>";
			htmlTable += "<br>" + places[i].getAttribute("name");
			htmlTable += "<br><input id='place" + i + "' name='place" + i + "' type='hidden' value='" + places[i].getAttribute("path") + "'>";
			htmlTable += "</td></tr>";
			if( places[i].getAttribute("type") == "FTP" )
				pathFTP = places[i].getAttribute("path").toUpperCase();
		}
		htmlTable += "</table>";
		document.getElementById("fdPlaces").innerHTML = htmlTable;
	}
	showLayer("divNetworkFolder", false);
	checkControls();
}


function onPlaceChanged(row, place)
{
	selectPlace(row, place);
	if( selectedPlace.length > 0 )
		CommSend("servlet/ajrefresh", "GET", "cmd=FileDialog&path=" + selectedPlace + "&filter=" + extensionFilter, "FileDialog", true);
	else 
		showLayer("divNetworkFolder", true);
}


function onFileSelected(obj)
{
	if( selectedFileRow != null )
		selectedFileRow.className = "Row1";
	obj.className = "selectedRow";
	selectedFileRow = obj;
	document.getElementById("fdFileName").value = selectedFileRow.cells[1].firstChild.nodeValue;
	bFNChanged = false;
	checkControls();
}


function onFolderSelected(obj)
{
	onFolderChanged(obj);
}


function onFolderChanged(obj)
{
	if( selectedFileRow != null )
		selectedFileRow.className = "Row1";
	obj.className = "selectedRow";
	selectedFileRow = obj;
	if( bLoad || !bFNChanged )
		document.getElementById("fdFileName").value = "";
	var folder = selectedFileRow.cells[1].firstChild.nodeValue;
	var path = selectedFolder + "/" + folder;
	CommSend("servlet/ajrefresh", "GET", "cmd=FileDialog&path=" + path + "&filter=" + extensionFilter, "FileDialog", true);
}


function onNetworkFolder()
{
	var netFolder = document.getElementById("fdNetworkFolder").value;
	if( netFolder.length > 2
		&& (netFolder[0] == "\\" || netFolder[0] == "/")
		&& (netFolder[1] == "\\" || netFolder[1] == "/")
	)
		CommSend("servlet/ajrefresh", "GET", "cmd=FileDialog&path=" + netFolder + "&filter=" + extensionFilter, "FileDialog", true);
}


function selectPlace(row, place)
{
	var table = document.getElementById("fdCaption").getElementsByTagName("tbody")[0];
	table.rows[0].cells[0].firstChild.nodeValue = document.getElementById("fdSave").value;;	
	var table = document.getElementById("fdPlaces").getElementsByTagName("tbody")[0];
	if( table != null )
	for(var i = 0; i < table.rows.length; i++) {
		if( i == row )
			table.rows[i].className = "trFDSelectedPlace";
		else
			table.rows[i].className = "";
	}
	selectedPlace = place;
}


function clearFiles()
{
	selectedFileRow = null;
	document.getElementById("fdFiles").innerHTML = "";
	document.getElementById("fdFileName").value = "";
}


function isParentFolder(folderName)
{
	var folder = folderName.toUpperCase();
	if( folder.length > 3 )	{
		if( folder[0] != "C" && folder[0] != "D" && folder[0] != "/")
			return true;
		if( pathFTP.length > 0 && pathFTP == folder )
			return false;
		var n = folder[0] == "/" ? 3 : 1; 
		for(var i=0, j=0; i < folder.length; i++)
			if( folder[i] == "/" )
				if( ++j > n )
					return true;
	}
	return false;
}


function filterFileName(event)
{
	return (bFilter = filterInput(2, event, false, '- ._'));
}


function onFileNameChanged()
{
	bFNChanged = true;
	checkControls();
}


function checkControls()
{
	bOk = (nError == 0) && (selectedFolder != null) && (document.getElementById("fdFileName").value.length > 0);
	/*
	var className = bOk ? "groupCategory_small" : "groupCategoryDisabled_small";
	var button = document.getElementById("fdOk").getElementsByTagName("tbody")[0].rows[0];
	if( button.className != className )
		button.className = className;
	*/
	return bOk;
}


function fileDialogEnable(id,enabled,load,filter)
{
	var input = document.getElementById(id);
	if(input != null)
	{
		input.disabled = !enabled;
	}
	var button = document.getElementById(id+"img");
	if(button != null)
	{
		button.disabled = !enabled;
		if(button.disabled == true)
		{
			if(load == true)
			{
				button.src = "images/filedialog/load_off.png";
			}
			else
			{
				button.src = "images/filedialog/save_off.png";
			}
			button.onclick = new Function("");
		}
		else
		{
			if(load == true)
			{
				button.src = "images/filedialog/load_on.png";
			}
			else
			{
				button.src = "images/filedialog/save_on.png";
			}
			button.onclick = new Function("onFileDialog(" + load + ",'" + id + "','" + filter + "')");
		}
	}
}
