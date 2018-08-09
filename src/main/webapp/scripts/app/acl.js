//global variables
var selectRow = -1;
var groups = null;
var imageclick = false;
var idprofile = -1;
var nameprofile = "";

// ###############################
// Onload page, Add, Remove, Edit Profile
// ###############################

function initialize() {
	enableAction(1);
	disableAction(2);
	disableAction(3);
	enableAction(4);
	document.getElementById("desc").disabled = false;
	document.getElementById("menuvisible").disabled = false;
	document.getElementById("desc").focus();
	document.getElementById("desc").readOnly = false;

	if (document.getElementById("usedprofile").value == "yes") {
		alert(document.getElementById("s_profileused").value);
	}

	if (document.getElementById("overwriteProf").value == "yes") {
		var overwritep = confirm(document.getElementById("s_overwriteProf").value);
		if(overwritep){
			var oFrm =document.getElementById("profuploadform");
			if(oFrm != null)
			{   
//				record filepath for local overwrite.   get filepath from session for remote . (testcase id 7303)
				if(document.getElementById("bLocal").value == "true"){
					var inputField = document.getElementById("importconf");
					if( inputField ) {
						inputField.value = document.getElementById("filename").value;
					}
				}
				document.getElementById("overwrite").value="yes";
				MTstartServerComm();				
				oFrm.submit();
				return;
			}
		}
		else {
			if(document.getElementById("bLocal").value != "true"){
				CommSend("servlet/ajrefresh","POST","action_type=clean_profile&path="+document.getElementById("filename").value,true);
			}
		}
	}
	if (document.getElementById("doubleprofile").value == "yes") {
		alert(document.getElementById("s_doubleprofile").value);
	}
	if (document.getElementById("loadxmlerror").value == "yes") {
		alert(document.getElementById("uploadfail").value);
	}
	if (document.getElementById("isadmin").value == "yes") {
		alert(document.getElementById("adminimportforbit").value);
	}
	if (document.getElementById("higherVersion").value == "yes") {
		alert(document.getElementById("s_higherVersion").value);
	}
	if (document.getElementById("lowerVersion").value == "yes") {
		alert(document.getElementById("s_lowerVersion").value);
	}
	if (document.getElementById("errorVersion").value == "yes") {
		alert(document.getElementById("s_errorVersion").value);
	}
	
	var idsgroups = document.getElementById("idsgroups").value;
	groups = idsgroups.split("-");
	function_number = document.getElementById("funct_number").value;

	resetFunctions();

}

function resetFunctions() {
	
	//preset table "only-read" selection
	var section_list = document.getElementById("section_list").value.split(";");
	var group_name = "";
	
	for (var i=0; i<section_list.length;i++ )
	{
		group_name = section_list[i];
		document.getElementById("check_"+group_name).checked=true;
		check_group(group_name);
		//set_group_buttons(group_name,'off');
	}

	if (document.getElementById("grouptable") != null) // null (only ADVANCED
	{
		if (groups != "") {
			for (i = 0; i < groups.length; i++) {
				var group = document.getElementById("g" + groups[i]);
				group.checked = true;
			}
		}
	}

}
function resetProfile(param) {
	if(imageclick == true)
	{
		imageclick = false;
		return;
	}
	var aparams = param.split("-"); 
	var id = aparams[0];
	idprofile = id;
	nameprofile = aparams[1];
	if (selectRow == id) {
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(5);
		selectRow = -1;
		resetFunctions();
		document.getElementById("desc").disabled = false;
		document.getElementById("desc").readOnly = false;
		document.getElementById("menuvisible").disabled = false;
		document.getElementById("desc").focus();
	}// if
	else {
		enableAction(2);
		enableAction(5);
		disableAction(1);
		disableAction(3);
		selectRow = id;
		resetFunctions();
		document.getElementById("desc").disabled = true;
		document.getElementById("menuvisible").disabled = true;
	}// else

	document.getElementById("rowselected").value = id;
	document.getElementById("desc").value = "";

}// reset
var isadminedit = false; 
function loadProfile(param) {
//  don't show export button
	if(selectRow != -1)
		resetProfile(param);
//  show export button	
//	selectRow = -1;
//	resetProfile(param);
	imageclick = true;
	var id = param.split("-")[0]; // id
	
/*
	if (id == 0)
	{

		alert(document.getElementById("s_nomodadminprofile").value);
	}
	else {
*/	
		// 2) disable menu,tab,buttons according to negative logic in profile maps
		
		// AJAX REQUEST: retrieve profilemaps configuration
	
	if (id == 0)
	{
		isadminedit = true;
		//alert(document.getElementById("s_nomodadminprofile").value);
	}else {
		isadminedit = false;
	}
	
	CommSend("servlet/ajrefresh","POST","action_type=load_profile&idprofile="+id,"loadProfile",true);
	
	//params filter
	var filter = param.split("-")[3];
	document.getElementById("param_filter").value=filter;
	
	// description
	var desc = param.split("-")[1]; 
	
	//menuvisible
	var menuvisible = param.split("-")[2];
	document.getElementById("menuvisible").checked = ("true"==menuvisible);
	document.getElementById("menuvisible").disabled = false;
	enableAction(3);
	disableAction(1);
	disableAction(2);

	document.getElementById("desc").disabled = false;
	document.getElementById("desc").value = desc;
	
	
	if (id == 1 || id == 2 || id == 3)
	{		
		document.getElementById("desc").readOnly=true;
		document.getElementById("menuvisible").focus();
	}
	else 
	{
		document.getElementById("desc").readOnly=false;
		document.getElementById("desc").focus();
	}
	
	//if (param.split("-")[4] != "") // groups to disable
		
	var group_to_disab = param.split("-")[5].split(";");
	if (group_to_disab != "")
	{
		for (j = 0; j < group_to_disab.length; j++) 
		{
			var groupuncheck = document.getElementById("g"+ group_to_disab[j]);
			if (groupuncheck != null) {
				groupuncheck.checked = false;
			}
		}
	 }
			//}
//	} 
}


function Callback_loadProfile()   //onload disable menu,tabs,buttons of profile loaded
{
	var tmp ="";
	
	// 1) enable ALL
	var section_list = document.getElementById("section_list").value.split(";");
	for (var i=0; i<section_list.length;i++ )
	{
		group_name = section_list[i];
		
		document.getElementById("check_"+group_name).checked=true;
		check_group(group_name);
		
		//if there are buttons -> enable all
		if (document.getElementById("num_tab_" + group_name)!=null)
			set_group_buttons(group_name,'on');
		
		// check if group checked has child
		var child = document.getElementById('has_child_' + group_name);
		if (child != null) 
		{
			child_section = document.getElementById('has_child_' + group_name).value;
			document.getElementById("check_" + child_section).checked = true;
			check_group(child_section);
			
			//if there are buttons -> enable all
			if (document.getElementById("num_tab_" + child_section)!=null)
				set_group_buttons(child_section,'on');
		}
	}
	//params filter
	
	
	//menu
	var menu_hide = xmlResponse.getElementsByTagName("menu");
	
	if (menu_hide!=null)
	{
		for (var i=0;i<menu_hide.length;i++)
		{
			tmp = menu_hide[i].getAttribute('menu');
			document.getElementById("check_"+tmp).checked=false;
			check_group(tmp);
		}
	}
	
	//tab
	var tab_hide = xmlResponse.getElementsByTagName("tab"); 
	if (tab_hide!=null)
	{
		for (var i=0;i<tab_hide.length;i++)
		{
			tmp = tab_hide[i].getAttribute('tab');
			
			document.getElementById("check_"+tmp).checked=false;
			tmp = tmp.split("_");
			
			set_subgroup_buttons(tmp[0],tmp[1],'off');
		}
	}
	
	//buttons
	var button_hide = xmlResponse.getElementsByTagName("but");
	if (button_hide!=null)
	{
		for (var i=0;i<button_hide.length;i++)
		{
			
			tmp = button_hide[i].getAttribute('but');
			//alert(i + " " + tmp);
			setButton("button_"+tmp,'off');
		}
	}
	
}
function removeProfile() {
	if (selectRow == 0) {
		alert(document.getElementById("s_noremadminprofile").value);
	} 
	else if (selectRow == 1 || selectRow == 2 || selectRow == 3) {
		alert(document.getElementById("s_noremfactoryprofile").value);
	}
	else {
		if (true == confirm(document.getElementById("s_confirmdeleteprofile").value)) {
			document.getElementById("cmd").value = "3";
			var ofrm = document.getElementById("frm_acl");
			if (ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}// removeProfile

function addProfile() {
	if (document.getElementById("desc").value == "") {
		alert(document.getElementById("s_insertdescription").value);
	} else if (document.getElementById("desc").value.toLowerCase() == "carel") {
		alert(document.getElementById("no_carel_prof").value);
	} else {
		document.getElementById("cmd").value = "4";
		var ofrm = document.getElementById("frm_acl");
		if (ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}// addProfile

function modifyProfile() {
	if(isadminedit){
		alert(document.getElementById("s_nomodadminprofile").value);
		return ;
	}
	
	if (document.getElementById("desc").value == "") {
		alert(document.getElementById("s_insertdescription").value);
	} else {
		document.getElementById("cmd").value = "5";
		var ofrm = document.getElementById("frm_acl");
		if (ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}// modifyProfile

/**
 * Per ridimensionare la tabella utenti
 */
function resizeTableUsersPriv() {
	var hdev = MTcalcObjectHeight("trUserList1");
	if (hdev != 0) {
		MTresizeHtmlTable(0, hdev - 10);
	}
	var hdev1 = MTcalcObjectHeight("trUserPrivList");
	if (hdev1 != 0) {
		MTresizeHtmlTable(2, hdev1 - 8);
	}
}

// ##################################
// Profile table management
// ##################################

// open and close view of slaves group
function change_view(group_name) {
	var rows = document.getElementById("num_tab_" + group_name).value;
	var open = false;
	for ( var i = 0; i < rows; i++) {
		var row = document.getElementById(group_name + "_" + i);
		if (row.style.visibility == 'visible') {
			row.style.visibility = 'hidden';
			row.style.display = 'none';
		} else {
			row.style.visibility = 'visible';
			row.style.display = '';
			open = true;
		}
	}

	// change img
	if (open) {
		document.getElementById("img_" + group_name).src = 'images/button/down_on.png';
	} else {
		document.getElementById("img_" + group_name).src = 'images/button/dx_on.png';
	}
}

// checking master group, also slave groups are checked
function check_group(group_name) {
	var rows = document.getElementById("num_tab_" + group_name).value;
	var check = document.getElementById("check_" + group_name).checked;
	var buttons_number = 0;

	for ( var i = 0; i < rows; i++) 
	{
	
		document.getElementById("check_" + group_name + "_" + i).checked = check;

		if (!check) {
			if (document.getElementById("num_button_" + group_name + i) != null) {
				buttons_number = document.getElementById("num_button_"
						+ group_name + i).value;
				set_subgroup_buttons(group_name, i, 'off');
			}
		}
		change_check_subgroup(group_name, i);
	}

	// check if group checked has child
	var child = document.getElementById('has_child_' + group_name);
	if (child != null) {
		var child_section = document.getElementById('has_child_' + group_name).value;
		document.getElementById("check_" + child_section).checked = check;
		check_group(child_section);
	}
}

// changing check on subgroup also check of group must be changed
function change_check_subgroup(group_name, tab_index) {
	// if exist a PRESET
	
	if (document.getElementById('preset_' + group_name + tab_index) != null) {
		preset_management(group_name, tab_index);
	}

	var status_subgroup = document.getElementById("check_" + group_name
			+ "_" +tab_index).checked;
	
	if (!status_subgroup) 
	{

		set_subgroup_buttons(group_name, tab_index, 'off');
	} 
	else 
	{
		document.getElementById("check_" + group_name).checked = true;
	}
}

function uncheck_master(group_name)
{
	var rows = document.getElementById("num_tab_" + group_name).value;
	//var check = document.getElementById("check_" + group_name).checked;
	var tmp_check = 0;
	var to_uncheck = true;

	for ( var i = 0; i < rows; i++) 
	{
		tmp_check = document.getElementById("check_" + group_name + "_" + i).checked;
		if (tmp_check) {
			to_uncheck = false;
			break;
		}
	}

	if (to_uncheck)
	{
		document.getElementById("check_" + group_name).checked = false;
	}
	else
	{
		document.getElementById("check_" + group_name).checked = true;
	}
}

function change_button_status(obj, group_name, tab_index) {
	var idbutton = obj.id;
	var status_obj = document.getElementById("val_" + idbutton);
	var status = status_obj.value;
	if (status == 'on') {
		// set off
		setButton(idbutton, 'off');
	} else {
		// set on
		setButton(idbutton, 'on');
		document.getElementById('check_' + group_name + "_" +tab_index).checked = true;
	}
}

// set buttons on/off: group_name = name of group; value = 0/1 on/off for all
// buttons of subgroups
function set_group_buttons(group_name, value)
{
	var tab_num = document.getElementById("num_tab_" + group_name).value;
	if (tab_num != null && tab_num != 0) {
		for ( var i = 0; i < tab_num; i++) {
			set_subgroup_buttons(group_name, i, value);
		}
	}
	if (value == 'on') {
		document.getElementById('check_' + group_name).checked = true;
	}
	
	// check if group checked has child
	var child = document.getElementById('has_child_' + group_name);
	if (child != null) 
	{
		child_section = document.getElementById('has_child_' + group_name).value;
		if (value == 'on') 
		{
			document.getElementById("check_" + child_section).checked = true;
		}
		check_group(child_section);
		
		
		if (document.getElementById("num_tab_" + child_section)!=null)
			set_group_buttons(child_section,value);
	}
}

// set buttons on/off: subgroup_name = name of group+index of tab; value = 0/1
// on/off for all buttons of subgroup
function set_subgroup_buttons(group_name, tab_index, value) 
{
	if (document.getElementById("num_button_" + group_name + tab_index) != null) {
		var number = document.getElementById("num_button_" + group_name
				+ tab_index).value;
		for ( var i = 0; i < number; i++) {
			if (value == 'on') 
			{
				setButton('button_' + group_name + '_tab' + tab_index + 'name_'
						+ i, 'on');
				document.getElementById('check_' + group_name + "_" +tab_index).checked = true;
				document.getElementById('check_' + group_name).checked = true;

			} else {
				setButton('button_' + group_name + '_tab' + tab_index + 'name_'
						+ i, 'off');
			}
		}
	}
}

function preset_management(group_name, tab_index) {
	var preset = document.getElementById('preset_' + group_name + tab_index).value;
	var sets = preset.split(";");
	for ( var i = 0; i < sets.length; i++) {
		setButton('button_' + group_name + '_tab' + tab_index + 'name_' + i,
				sets[i]);
	}
}

function setButton(id, value) {
	
	if (value == 'on') {
		// Alessandro: cause of the new pvpro 2.0 layout the 'ON' icons are of black color in the body 
		// and white in the action buttons so we must distinguish one from each other
		//if (id == 'button_dtlview_tab0name_0') alert("metto in on");
		// Alessandro: this first replacement is for the icons already on but of white color
		document.getElementById(id).src = document.getElementById(id).src
				.replace('_on.', '_on_black.');		
		document.getElementById(id).src = document.getElementById(id).src
				.replace('_off.', '_on_black.'); 
		document.getElementById("val_" + id).value = "on";
	} else {
		//if (id == 'button_dtlview_tab0name_0') alert("metto in off");
		document.getElementById(id).src = document.getElementById(id).src
				.replace('_on_black.', '_off.');
		document.getElementById("val_" + id).value = "off";
	}
}


function userprofilexp_savefile(local,path,filename)
{
	if(idprofile == 0)
	{
		alert(document.getElementById("adminexportforbit").value);
		return;
	}
	path += ".PCFG";
	filename += ".PCFG";
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "action_type=export_profile&idprofile="+idprofile+"&path="+path+"&filename="+filename+"&local="+local,"profilexp_back("+local+")",true);
}

function Callback_profilexp_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}

function profupload(){
	var oFrm =document.getElementById("profuploadform");
	if(oFrm != null)
	{
		MTstartServerComm();
		oFrm.submit();
	}
}

function onFileDialogPre(a1,a2){
	if(fdLocal == true){
		onFileDialog(true,a1,a2);
	}else{
		var win = document.getElementById("fdFieldRemote");
		win.style.left = (document.body.clientWidth - 450) / 2 + "px";
		win.style.top = (document.body.clientHeight - 120) / 2 + "px";
		document.getElementById("fdFieldRemote").style.display = "block";
		showLayer("fdFieldRemote", true);
		jt_BodyZ.toTop(document.getElementById("fdFieldRemote"));
		jt_Veil.show(true);
	}
}

function fdRemoteCancel(){
	showLayer("fdFieldRemote", false);
	jt_Veil.show(false);
}

function fdSaveFileUserProfile(id, filter, notify)
{	if(idprofile == 0)
	{
		alert(document.getElementById("adminexportforbit").value);
		return;
	}else{
		fdSaveFile(id, filter, notify);
		fdSetFile(nameprofile);
	}
}
