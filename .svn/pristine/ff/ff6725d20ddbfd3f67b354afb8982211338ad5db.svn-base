// actions
var ACTION_ADD			= 1;
var ACTION_MOD			= 2;
var ACTION_DEL			= 3;
var ACTION_SAVE			= 4;

var idGroupSelected		= 0;
var idModelSelected		= 0;

// limits
var MAX_GROUPS			= 0;
var MAX_CONSUMERS		= 0;

//aliases
var listGroups			= null;
var listAllDevs			= null;
var listGroupDevs		= null;
var COL_GROUP_ID		= 1;
var COL_GROUP_NAME		= 3;


function onLoadGroups()
{
	MAX_GROUPS = parseInt(document.getElementById("maxnumgroup").value, 10);
	MAX_CONSUMERS = parseInt(document.getElementById("maxnumconsumer").value, 10);

	// set aliases
	listGroups = Lsw1;
	listAllDevs = listbox1;
	listGroupDevs = listbox2;
	
	if( document.getElementById("idGroup").value != 0 )
		enableAction(ACTION_SAVE);
	else
		enableAction(ACTION_ADD);
	if(document.getElementById("duplicatedSiteMeterGroupName").value != "")
		alert(document.getElementById("emeterusedingroup").value+document.getElementById("duplicatedSiteMeterGroupName").value);
	
	if(document.getElementById("maxconsumerreached").value != "")
		alert(document.getElementById("maxconsumerreached").value);
}


function addGroup()
{
	// check group limit
	if( listGroups.mData.length > MAX_GROUPS ) {
		alert(document.getElementById("alert_max_group").value);
		return;
	}
	
	// check group name
	var strGroupName = document.getElementById("name").value;
	if( strGroupName == "" ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	for(var i = 0; i < listGroups.mData.length; i++) {
		if( strGroupName == listGroups.mData[i][COL_GROUP_NAME] ) {
			alert(document.getElementById("dup_group_name").value);
			return;
		}
	}

	document.getElementById("cmd").value = "add";
	document.getElementById("cons").value = listGroupDevs2String();
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function modifyGroup()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idGroup").value = idGroupSelected;
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function removeGroup()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;

	document.getElementById("cmd").value = "remove";
	document.getElementById("idGroup").value = idGroupSelected;
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function saveGroup()
{
	// check group name
	var strGroupName = document.getElementById("name").value;
	if( strGroupName == "" ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	var idGroup = document.getElementById("idGroup").value;	
	for(var i = 0; i < listGroups.mData.length; i++) {
		if( idGroup != listGroups.mData[i][COL_GROUP_ID] && strGroupName == listGroups.mData[i][COL_GROUP_NAME] ) {
			alert(document.getElementById("dup_group_name").value);
			return;
		}
	}

	document.getElementById("cmd").value = "save";
	document.getElementById("cons").value = listGroupDevs2String();
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectGroup(id)
{
	if( id != idGroupSelected )
		idGroupSelected = id;
	else
		idGroupSelected = 0;
	checkButtons();
}


function onModifyGroup(id)
{
	idGroupSelected = id;
	modifyGroup();
}


function onSelectModel(id)
{
	idModelSelected = id;
	if( idModelSelected > 0 )
		CommSend("servlet/ajrefresh", "POST",
			"idModel=" + idModelSelected + "&cons=" + listGroupDevs2String()
			, "onModelDevs", true);
	else
		listAllDevs.length = 0;
}


function multipleto1(obj,devobj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var meterModel = document.getElementById('model');
	var meterModelName = meterModel.options[meterModel.selectedIndex].text;
	var flag = true;
	if(meterModelName.length!=0){
		meterModelName = " - " + meterModelName;
	}else{
		flag = false;  
	}
	
	for(var i=0;i< list2.length;i++)
	{
		if(list2.options[i].selected ){
			/* if meterModelName is selected && the selected options' text contain meterModelName in Group devices select, 
			 * remove the  " -  + meterModelName" from option's text .*/
			if( flag && list2.options[i].text.indexOf(meterModelName) > 0){
				var t =list2.options[i].text.split(" - ");
				var newStr = ""
				if(t.length>0){
					for(var j=0;j<t.length-1;j++){
						if(j==0){
							newStr = t[j];
						}else{
							newStr += " - "+t[j];
						}
					}
				}
				list2.options[i].text = newStr;
			}else{
				list2.options.remove(i);
				i--;
			}
		}
	}
	
	multipleto1New(list1,list2,devobj);
	updateClass(list2);
}


function multipleto2(obj,devobj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	// need to check the length of selected in list1 and  length of list2 to compare with MAX_CONSUMERS;
	var list1SelectedNum = 0;
	var list1Length = list1.length;
	for(var i=0;i< list1Length;i++)
	{
		if(list1.options[i].selected ){
			list1SelectedNum++;
		}
	}
	if( (list2.options.length+list1SelectedNum) > MAX_CONSUMERS ) {
		alert(document.getElementById("alert_max_cons").value);
		return;
	}
	/* GLOBAL became a regular group
	if( document.getElementById("idGroup").value == -1 && list2.options.length >= 1 ) {
		alert(document.getElementById("one_site_counter").value);
		return;
	}
	*/
	if( document.getElementById("idGroup").value != "-1" ) {	
		var siteCounter = document.getElementById("sitecounter").value;
		if( siteCounter != "" ) {
			var astrSiteCounters = siteCounter.split(";");
			for(var i = 0; i < astrSiteCounters.length; i++) {
				if( list1.options[list1.selectedIndex].value.indexOf(astrSiteCounters[i]) > 0 ) {
					alert(document.getElementById("dup_site_counter").value);
					return;
				}
			}
		}
	}
	var meterModel = document.getElementById('model');
	var meterModelName = meterModel.options[meterModel.selectedIndex].text;
	if(meterModelName.length!=0){
		meterModelName = " - " + meterModelName;
	}
//	list1.options[list1.selectedIndex].text  = list1.options[list1.selectedIndex].text + meterModelName; 
	for(var i=0;i< list1Length;i++)
	{
		if(list1.options[i].selected ){
			list1.options[i].text  = list1.options[i].text + meterModelName;
		}
	}
	
	multipleto2New(list1,list2,devobj);
}


function Callback_onModelDevs(response)
{
	listAllDevs.length = 0;
	var aXmlDevs = response.getElementsByTagName("dev");
	var message = response.getElementsByTagName("message");
	if(message.length > 0){
		var serveralert = document.getElementById("serveralert").value;
		alert(serveralert);
	}
	for(var i = 0; i < aXmlDevs.length; i++) {
		listAllDevs.options.add(new Option(aXmlDevs[i].getAttribute("name"), aXmlDevs[i].getAttribute("id")));
		listAllDevs.options[i].className = i % 2 == 0 ? "Row1" : "Row2";
	}
}


function listGroupDevs2String()
{
	var devs = "";
	for(var i = 0; i < listGroupDevs.length; i++) {
		if( i > 0 )
			devs += ";";
		devs += listGroupDevs.options[i].value;
	}
	return devs;
}


function checkButtons()
{
	if( idGroupSelected != 0 )
		enableAction(ACTION_MOD);
	else
		disableAction(ACTION_MOD);
	if( idGroupSelected > 0 )
		enableAction(ACTION_DEL);
	else
		disableAction(ACTION_DEL);
}
