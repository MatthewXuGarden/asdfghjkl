// actions
var ACTION_MOD			= 1;
var ACTION_SAVE			= 2;

var idConsumerSelected	= 0;
var bMultipleLatch		= false;

//aliases
var listConsumers		= null;
var listAvailableDevs	= null;
var listConsumerDevs	= null;
var COL_CONSUMER_ID		= 1;
var COL_CONSUMER_NAME	= 3;
var COL_NUM_DEVICES		= 4;


function onLoadConsumerDevices()
{
	if( !document.getElementById("idConsumer") )
		return;
	
	// set aliases
	listConsumers = Lsw1;
	listAvailableDevs = listbox1;
	listConsumerDevs = listbox2;
	
	if( document.getElementById("idConsumer").value != 0 )
		enableAction(ACTION_SAVE);
}


function modifyConsumerDevices()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idConsumer").value = idConsumerSelected;
	var form = document.getElementById("frm_set_consumer");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function saveConsumerDevices()
{
	document.getElementById("cmd").value = "save";
	document.getElementById("devs").value = listConsumerDevs2String();
	var form = document.getElementById("frm_set_consumer");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectConsumer(id)
{
	if( id != idConsumerSelected )
		idConsumerSelected = id;
	else
		idConsumerSelected = 0;
	checkButtons();
}


function onModifyConsumer(id)
{
	idConsumerSelected = id;
	modifyConsumerDevices();
}


function multipleto1(obj, devobj)
{
	var length = listConsumerDevs.options.length;
	for(var i = 0; i < length; i++)	{
		if( listConsumerDevs.options[i].selected ) {
			var value = listConsumerDevs.options[i].value;
			var n = value.indexOf(",");
			n = value.indexOf(",", n + 1);
			listConsumerDevs.options[i].value = value.substr(0, n);
			listConsumerDevs.options[i].text = listConsumerDevs.options[i].text.substr(0, listConsumerDevs.options[i].text.indexOf(", "));
		}
	}
	multipleto1New(listAvailableDevs, listConsumerDevs, devobj);
}


function multipleto2(obj, devobj)
{
	var weight = parseFloat(document.getElementById("weight").value);
	if( isNaN(weight) ) {
		alert(document.getElementById("alert_req_weight").value);
		return;
	}

	var ambiguous = "";
	var length = listAvailableDevs.options.length;
	for(var i = 0; i < length; i++)	{
		if( listAvailableDevs.options[i].selected ) {
			if( parseInt(listAvailableDevs.options[i].value.split(",")[1]) == 0 ) {
				ambiguous += listAvailableDevs.options[i].text;
				ambiguous += "\n";
			}
		}
	}
	if( ambiguous.length > 0 ) {
		alert(ambiguous + document.getElementById("alert_ambiguous").value.replace(". ", ".\n"));
		return;
	}
	
	for(var i = 0; i < length; i++)	{
		if( listAvailableDevs.options[i].selected ) {
			listAvailableDevs.options[i].value += "," + weight;
			listAvailableDevs.options[i].text += ", " + weight;
		}
	}
	multipleto2New(listAvailableDevs, listConsumerDevs, devobj);
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


function listConsumerDevs2String()
{
	var devs = "";
	for(var i = 0; i < listConsumerDevs.length; i++) {
		if( i > 0 )
			devs += ";";
		devs += listConsumerDevs.options[i].value;
	}
	return devs;
}


function checkButtons()
{
	if( idConsumerSelected != 0 )
		enableAction(ACTION_MOD);
	else
		disableAction(ACTION_MOD);
}
