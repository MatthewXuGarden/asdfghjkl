var selectRow=-1;
var groups = null;

function initialize()
{
	enableAction(1);
	disableAction(2);
	disableAction(3);
	disableAction(4);
	document.getElementById("user").disabled=false;
	document.getElementById("user").focus();
	document.getElementById("profile").selectedIndex=0;
	document.getElementById("profile").disabled=false;
	
	if (document.getElementById("doubleuser").value=="yes")
	{
		alert(document.getElementById("s_doubleuser").value);
		document.getElementById("doubleuser").value="";
	}
	if (document.getElementById("carel_exist").value=="yes")
	{
		alert(document.getElementById("s_carel_exist").value);
		document.getElementById("carel_exist").value="";
	}
	//2009-12-23, add account policy, Kevin
	var policytype = document.getElementById("s_policy").value;
	var expirationdate = document.getElementById("s_expirationdate").value;
	var loginretry = document.getElementById("s_loginretry").value;
	var standard = document.getElementById("s_standard").value;
	var strict = document.getElementById("s_strict").value;
	if(policytype == standard)
	{
		document.getElementsByName("policytype")[0].checked = true;
		document.getElementById("expirationSel").disabled = true;
		document.getElementById("logonretriesSel").disabled = true;
	}
	else if(policytype == strict)
	{
		document.getElementsByName("policytype")[1].checked = true;
		document.getElementById("expirationSel").value = expirationdate;
		document.getElementById("logonretriesSel").value = loginretry;
	}
	//--end
	
	//2009-12-30, add allow remote support checkbox, Kevin
	var remotesupport = document.getElementById("s_remotesupport").value;
	var remotesupportO = document.getElementById("remotesupport");
	if(remotesupport == "0")
	{
		remotesupportO.checked = false;
	}
	else
	{
		remotesupportO.checked = true;
	}
	//--end
}

//2009-12-23, add account policy, Kevin
function policytypechange(policytype)
{
	var standard = document.getElementById("s_standard").value;
	var strict = document.getElementById("s_strict").value;
	if(policytype == standard)
	{
		document.getElementById("expirationSel").disabled = true;
		document.getElementById("logonretriesSel").disabled = true;
	}
	else if(policytype == strict)
	{
		document.getElementById("expirationSel").disabled = false;
		document.getElementById("logonretriesSel").disabled = false;
	}
	document.getElementById("policychanged").value = "1";
	saveOnly();
}
function saveOnly()
{
	enableAction(3);
	disableAction(1);
	if(document.getElementById("user").disabled == false)
	{
		document.getElementById("user").value = "";
		document.getElementById("password").value = "";
		document.getElementById("cpassword").value = "";
		document.getElementById("profile").options[0].selected=true
	}
}
//2009-12-30, add remote support, Kevin
function remotesupportchange()
{
	document.getElementById("remotesupportchanged").value = "1";
	saveOnly();
}
function policyCombochanged()
{
	document.getElementById("policychanged").value = "1";
	saveOnly();
}
function unblock()
{
	document.getElementById("cmd").value="6";
	var ofrm = document.getElementById("frm_ldapuser");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}
//--end

function load(id){
	var carel_prefix = document.getElementById("carel_prefix").value;
	var informationRow = new Array();
	informationRow=id.split(";")
	
	var user = "";
	var profile = informationRow[1];
	if (profile==-5)
	{
		user = informationRow[0].substring(carel_prefix.length,informationRow[0].length);
	}
	else
	{
		user = informationRow[0];	
	}
	
	document.getElementById("user").value=user;
	document.getElementById("user_to_modify").value=document.getElementById("user").value;
	document.getElementById("password").value="";
	document.getElementById("cpassword").value="";
	if(informationRow[2] == "FALSE")
	{
		document.getElementById("unblockbutton").disabled = true;
	}
	else if(informationRow[2] == "TRUE")
	{
		document.getElementById("unblockbutton").disabled = false;
	}
	document.getElementById("user").disabled=true;
	var profiles = document.getElementById("profile"); 
	profiles.disabled=false;
	for (i=0;i<profiles.length;i++)
	{
		if(profiles.options[i].value==informationRow[1])
			profiles.options[i].selected=true;
	}
	if(user=="admin"||profile==-5)  //se amministratore oppure carel
	{
		document.getElementById("profile").disabled=true;
	}
	
	enableAction(3);
	disableAction(1);
	disableAction(2);
	disableAction(4);
}


function reset_table(id){
	if(selectRow==id)
	{
		initialize();
		selectRow=-1;
	}
	else
	{
		enableAction(2);
		disableAction(1);
		disableAction(3);
		enableAction(4);
		selectRow=id;
	}		
	document.getElementById("rowselected").value=id;
	document.getElementById("user").value="";
	document.getElementById("user_to_modify").value = "";
	document.getElementById("profile").value="";
	document.getElementById("password").value="";
	document.getElementById("cpassword").value="";
}


//Per i valori del cmd controllare il Bo BSetLdap
function removeUser()
{
	var user=selectRow.split(";")[0];
	
	if (user=="admin")
	{
		alert(document.getElementById("noremuser").value);
	}
	else
	{
		if(true==confirm(document.getElementById("s_deleteuser").value))
		{
			document.getElementById("cmd").value="0";
			var ofrm = document.getElementById("frm_ldapuser");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}

function addUser()
{
	var new_user = document.getElementById("user").value;
	if(checkFields()==0)
			return;
	document.getElementById("cmd").value="1";
	var ofrm = document.getElementById("frm_ldapuser");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}



function modifyUser()
{
	//2009-12-23, add account policy, Kevin
    //when the user click "save", maybe he/she only want to save policy
	var policychanged = document.getElementById("policychanged").value;
	var remotesupportchanged = document.getElementById("remotesupportchanged").value;
	var user=document.getElementById("user").value;
	if((policychanged == "1"|| remotesupportchanged == "1")&& user == "")
	{
		document.getElementById("cmd").value="2";
		var ofrm = document.getElementById("frm_ldapuser");
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
		return;
	}
	//end

	if(checkFields(true)==0)
		return;

	document.getElementById("cmd").value="2";
	var ofrm = document.getElementById("frm_ldapuser");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}//modifyUser

function checkFields(no_passwd){
	var profile=document.getElementById("profile").value;
	var user=document.getElementById("user").value;
	var password= document.getElementById("password").value;
	var cpassword= document.getElementById("cpassword").value;
	var standard = document.getElementsByName("policytype")[0].checked;
	
	if((user=="")||(user==null)){
		alert(document.getElementById("s_insertname").value);
		return 0;
	}
	
	if( !no_passwd && (password=="" || password==null) ) {
		alert(document.getElementById("s_insertpassw").value);
		return 0;
	}
	
	if( password ) {
		if(standard == true)
		{
			if(password.length<6){
				document.getElementById("password").value="";
				document.getElementById("cpassword").value="";
				alert(document.getElementById("s_tooshortpassw").value);
			
				return 0;
			}
		}
		else
		{
			if(password.length<8 || !strictCheckAtleast1char1number1speicial(password))
			{
				document.getElementById("password").value="";
				document.getElementById("cpassword").value="";
				alert(document.getElementById("s_strictpassw").value);
				return 0;
			}
		}
	}
	
	if(password!=cpassword){
		alert(document.getElementById("s_confirmpassw").value);
		document.getElementById("password").value="";
		document.getElementById("cpassword").value="";
		return 0;
	}
	
	if(profile==-1){
		alert(document.getElementById("s_selectprofile").value);
		return 0;
	}
		
	return 1;
}

function onchange_prof()
{
	var combo_profile = document.getElementById("profile");
	if (combo_profile.value==-5)
	{
		alert(document.getElementById("carel_adv").value);
	}
}

/**
 * Per ridimensionare la tabella utenti
 */
function resizeTableUsers() {
	var hdev = MTcalcObjectHeight("trUserList");
	if (hdev != 0) {
		MTresizeHtmlTable(1, hdev - 20);
	}
}





