// Site Booklet
// actions
var ACTION_SAVE		= 1;
var ACTION_ADD		= 2;
var ACTION_BACK		= 3;
var ACTION_NEXT		= 4;
var ACTION_PRINT	= 5;
// commands
var COMMAND_SAVE	= "save";
var COMMAND_MODIFY	= "mod";
var COMMAND_ADD		= "add";
var COMMAND_DEL		= "del";
// pages
var selectedPage	= 0;
var aPages			= null; // array with pages sequence created onPageLoad
// objMandatory contains an array for each page; used by checkMandatoryFields(page)
// 1st level array: all fields are required
// 2nd level string: field required
// 2nd level array: at least 1 checkbox required
// 2nd level object: { checkbox: "name", fields: [ "name1", "name2" ] }, all fields are required if checkbox is checked
var objMandatory	= {
	page3:  [ "header", "smk", "system_type", "circuit_id" ],
	page4:  [ "text" ],
	page5:  [ "surname_0", "address_0", "phone_0", "fax_0", "e_mail_0" /*, "surname_1", "phone_1", "fax_1", "e_mail_1"*/ ],
	page6:  [ "address_2", "phone_2", "fax_2", "e_mail_2", "surname_3", "phone_3", "fax_3", "e_mail_3", "name_3" ],
	page7:  [ "surname_4", "name_4", "phone_4", "fax_4", "e_mail_4", "note2" ],
	page8:  [ ["cb_direct_expansion", "cb_secondary_fluid", "cb_site_type_other"],
	          { checkbox: "cb_site_type_other", fields: [ "site_type_other" ] }, 
	          ["cb_ref_type_0", "cb_ref_type_1", "cb_ref_type_2", "cb_ref_type_3", "cb_ref_type_4", "cb_ref_type_5", "cb_ref_type_6", "cb_ref_type_7", "cb_ref_type_8"],
	          { checkbox: "cb_ref_type_0", fields: [ "ref_type_0" ] } ],
	page9:  [ ["cb_water_ethyleneglycol", "cb_water_propyleneglycol", "cb_organic_mixtures", "cb_refrigerant_type", "cb_secondary_fluid_other"],
	          { checkbox: "cb_water_ethyleneglycol", fields: [ "water_ethyleneglycol" ] },
	          { checkbox: "cb_water_propyleneglycol", fields: [ "water_propyleneglycol" ] },
	          { checkbox: "cb_organic_mixtures", fields: [ "organic_mixtures", "organic_mixtures_brand", "organic_mixtures_type" ] },
	          { checkbox: "cb_refrigerant_type", fields: [ "refrigerant_type" ] },
	          { checkbox: "cb_secondary_fluid_other", fields: [ "secondary_fluid_other" ] },
	          "kg" ],
	page10: [ "type_model", "registration_number", "built_year",
	          ["cb_level_sensors", "cb_level_indicators"],
	          { checkbox: "cb_level_sensors", fields: [ "brand_model", "min_value", "max_value" ] },
	          { checkbox: "cb_level_indicators", fields: [ "level_estimation", "level_reference" ] } ],
	page11: [ "safety_pressure_pos", "safety_pressure_series", "safety_pressure_action", "safety_pressure_ped_category", "replacement_type", "replacement_series", "replacement_pressure_calibration", "replacement_ped_category" ],
	page12: [ "safety_valve_pos", "safety_valve_series", "safety_valve_pressure_calibration", "replacement_type", "replacement_series", "replacement_pressure_calibration", "replacement_ped_category" ],
	page26: [ "recovered_ref_type", "recovered_qantity", "replaced_ref_type", "replaced_qantity", "recovery_equipment", "iso_equipment", "operator_stamp" ],
	page30: [ "prevention_plan_quantity", ["cb_interval_quarterly", "cb_interval_semiannual", "cb_interval_annual"],
	          "pp_label0", "pp_label1_1", "pp_label2_1", "pp_label3_1", 
	          //{ checkbox: "cb_check_critical_points", fields: [ ["cb_cp_electricity_supply", "cb_cp_capacitors", "cb_cp_hi_pressure_pipe", "cb_cp_evaporators", "cb_cp_lo_pressure_pipe", "cb_cp_filters_valves"] ] }
	          ["cb_check_ref_charge"], ["cb_check_loss_central"], ["cb_check_critical_points"], ["cb_cp_electricity_supply"], ["cb_cp_capacitors"], ["cb_cp_hi_pressure_pipe"], ["cb_cp_evaporators"], ["cb_cp_lo_pressure_pipe"], ["cb_cp_filters_valves"] ],
	page31: [ "instrument_type", "instrument_sensitivity", ["cb_no_leaks", "cb_leaks"], "operator_stamp",
	          { checkbox: "cb_leaks", fields: [ "leakage_description", "corrective_action", "ref_to_restore_quantity" ] },
			  ["cb_level_sensors", "cb_level_indicators"],
			  { checkbox: "cb_level_sensors", fields: [ "brand_model", "min_value", "max_value" ] },
			  { checkbox: "cb_level_indicators", fields: [ "level_estimation", "level_reference" ] } ],
	page71: [ "tech_company", "procedure_description" ]
};
// records
var idrecordSelected = null;
// exceptions
var bSecondaryFluid = false;


function onPageLoad()
{
	// create page sequence
	bSecondaryFluid = document.getElementById("secondary_fluid").value == 'true';
	if( bSecondaryFluid )
		aPages = [ 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 26, 30, 31, 71 ];
	else
		aPages = [ 3, 4, 5, 6, 7, 8, 10, 11, 12, 26, 30, 31, 71 ];
		
	MAdisableAll();
	unlockModUser();
	selectedPage = parseInt(document.getElementById("page").value, 10);
	
	if( selectedPage == 11 || selectedPage == 12 || selectedPage == 26 || selectedPage == 31 || selectedPage == 71 ) {
		//if( document.getElementById("cmd").value == COMMAND_MODIFY )
		if( !isReset() && !isReadOnly() )
			enableAction(ACTION_SAVE);
		idrecordSelected = document.getElementById("idrecord").value; 
		if( idrecordSelected != "" ) {
			if( !isReset() )
				enableAction(ACTION_ADD);
			//if( !isReadOnly() )
			//	enableAction(ACTION_DEL);
		}
	}
	else {
		if( !isReset() && !isReadOnly() )
			enableAction(ACTION_SAVE);
	}
	
	if( isBooklet() ) {
		var iPos = -1;
		for(var i = 0; i < aPages.length; i++) {
			if( aPages[i] == selectedPage ) {
				iPos = i;
				break;
			}
		}
		if( iPos > 0 )
			enableAction(ACTION_BACK);
		if( iPos >= 0 && iPos < aPages.length - 1 )
			enableAction(ACTION_NEXT);
		enableAction(ACTION_PRINT);
	}
}


function onPageSelect(page)
{
	document.getElementById("new_page").value = page; 
	var form = document.getElementById("frm_page_select");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onNext()
{
	var iPos = -1;
	for(var i = 0; i < aPages.length; i++) {
		if( aPages[i] == selectedPage ) {
			iPos = i;
			break;
		}
	}
	if( iPos >= 0 && iPos < aPages.length - 1 ) {
		var nextPage = aPages[iPos + 1];
		if( isReadOnly() || MioAskModUser() )
			onPageSelect(nextPage);
	}
}


function onBack()
{
	var iPos = -1;
	for(var i = 0; i < aPages.length; i++) {
		if( aPages[i] == selectedPage ) {
			iPos = i;
			break;
		}
	}
	if( iPos > 0 ) {
		var prevPage = aPages[iPos - 1];
		if( isReadOnly() || MioAskModUser() )
			onPageSelect(prevPage);
	}
}


function onSave()
{
	if( !checkMandatoryFields("page" + selectedPage) ) {
		alert(document.getElementById("required_fields").value);
		return;
	}
	if( selectedPage == 71 && document.getElementById("procedure_description").value.length > 2000 ) {
		alert(document.getElementById("limit2000").value);
		return;
	}
	
	var cmd = document.getElementById("cmd");
	if( cmd )
		cmd.value = COMMAND_SAVE;
	var form = document.getElementById("frm_site_booklet");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function isReadOnly()
{
	var readonly = document.getElementById("readonly");
	return !readonly || readonly.value == "false" ? false : true;
}


function isBooklet()
{
	var booklet = document.getElementById("booklet");
	return booklet.value == "true";
}


function isReset()
{
	var reset = document.getElementById("reset");
	return reset.value == "true";
}


function checkMandatoryFields(page)
{
	var aFields = objMandatory[page];
	if( aFields ) {
		for(var i = 0; i < aFields.length; i++) {
			var xVar = aFields[i];
			if( typeof xVar === 'string' ) {
				if( document.getElementsByName(xVar)[0].value == "" )
					return false;
			}
			else if( xVar['checkbox'] ) {
				if( document.getElementsByName(xVar['checkbox'])[0].checked ) {
					for(var j = 0; j < xVar['fields'].length; j++) {
						var xxVar = xVar['fields'][j];
						if( typeof xxVar === 'string' ) {
							if( document.getElementsByName(xxVar)[0].value == "" )
								return false;
						}
						else {
							var bChecked = false;
							for(var k = 0; k < xxVar.length; k++)
								if( document.getElementsByName(xxVar[k])[0].checked )
									bChecked = true;
							if( !bChecked )
								return false;
						}
					}
				}
			}
			else {
				var bChecked = false;
				for(var j = 0; j < xVar.length; j++)
					if( document.getElementsByName(xVar[j])[0].checked )
						bChecked = true;
				if( !bChecked )
					return false;
			}
		}
	}
	return true;
}


function onModify()
{
	var cmd = document.getElementById("cmd");
	if( cmd )
		cmd.value = COMMAND_MODIFY;
	var idrecord = document.getElementById("idrecord");
	if( idrecord )
		idrecord.value = idrecordSelected;
	var form = document.getElementById("frm_site_booklet");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onAdd()
{
	var cmd = document.getElementById("cmd");
	if( cmd )
		cmd.value = COMMAND_ADD;
	var form = document.getElementById("frm_site_booklet");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onDelete()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;
	
	var cmd = document.getElementById("cmd");
	if( cmd )
		cmd.value = COMMAND_DEL;
	var idrecord = document.getElementById("idrecord");
	if( idrecord )
		idrecord.value = idrecordSelected;
	var form = document.getElementById("frm_site_booklet");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectRecord(td, idrecord)
{
	var tabRecords = document.getElementById("tabRecords");
	if( tabRecords )
		for(var i = 0; i < tabRecords.rows[0].cells.length; i++)
			tabRecords.rows[0].cells[i].className = 'sbnotselected';
	td.className = 'sbselected';
	idrecordSelected = idrecord;
	//enableAction(ACTION_MODIFY);
	//enableAction(ACTION_DEL);
	onModify();
}


function onSelectRowRecord(tr, idrecord)
{
	var tabRecords = document.getElementById("tabRecords");
	if( tabRecords )
		for(var i = 1; i < tabRecords.rows.length-1; i++)
			tabRecords.rows[i].className = 'sbnotselected';
	tr.className = 'sbselected';
	idrecordSelected = idrecord;
	//enableAction(ACTION_MODIFY);
	//enableAction(ACTION_DEL);
}


function onPrint()
{
	var url = document.getElementById("documentPath").value;
	window.open(url);
}


function onClickRadioStyleCheckbox(aCheckboxes, checkbox)
{
	if( checkbox.checked ) {
		for(var i = 0; i < aCheckboxes.length; i++) {
			var pageCheckbox = document.getElementsByName(aCheckboxes[i])[0];
			if( pageCheckbox != checkbox )
				pageCheckbox.checked = false;
		}
	}
}


// Site type

function onClickSiteType(checkbox)
{
	var aCheckboxes = [ "cb_direct_expansion", "cb_secondary_fluid", "cb_site_type_other" ];
	onClickRadioStyleCheckbox(aCheckboxes, checkbox);
	if( checkbox.name != "cb_site_type_other" && checkbox.checked ) {
		document.getElementById("site_type_other").value = "";
		document.getElementById("site_type_other").disabled = true;
	}
}


function onClickSiteTypeOther(checked)
{
	if( checked ) {
		document.getElementById("site_type_other").disabled = false;
	}
	else {
		document.getElementById("site_type_other").value = "";
		document.getElementById("site_type_other").disabled = true;
	}
}


function onClickRefType(checkbox)
{
	var aCheckboxes = [ "cb_ref_type_0", "cb_ref_type_1", "cb_ref_type_2", "cb_ref_type_3", "cb_ref_type_4", "cb_ref_type_5", "cb_ref_type_6", "cb_ref_type_7", "cb_ref_type_8" ];
	onClickRadioStyleCheckbox(aCheckboxes, checkbox);
	if( checkbox.name != "cb_ref_type_0" && checkbox.checked ) {
		document.getElementById("ref_type_0").value = "";
		document.getElementById("ref_type_0").disabled = true;
	}
}


function onClickRefTypeOther(checked)
{
	if( checked ) {
		document.getElementById("ref_type_0").disabled = false;
	}
	else {
		document.getElementById("ref_type_0").value = "";
		document.getElementById("ref_type_0").disabled = true;
	}
}


// Secondary fluid

function onClickSecondaryFluid(checkbox)
{
	if( checkbox.checked )
		return;
	
	var aPage = objMandatory["page9"];
	for(var i = 0; i < aPage.length; i++) {
		var xVar = aPage[i];
		if( typeof xVar === 'object' && xVar['checkbox'] ) {
			if( xVar['checkbox'] == checkbox.name ) {
				var aFields = xVar['fields'];
				for(var j = 0; j < aFields.length; j++)
					document.getElementsByName(aFields[j])[0].value = "";
				break;
			}
		}
	}
}


// Site reference

function onClickFluidLevel(checkbox)
{
	var aCheckboxes = ["cb_level_sensors", "cb_level_indicators"];
	onClickRadioStyleCheckbox(aCheckboxes, checkbox);

	var aPage = objMandatory["page10"];
	for(var i = 0; i < aPage.length; i++) {
		var xVar = aPage[i];
		if( typeof xVar === 'object' && xVar['checkbox'] ) {
			if( xVar['checkbox'] != checkbox.name ) {
				var aFields = xVar['fields'];
				for(var j = 0; j < aFields.length; j++)
					document.getElementsByName(aFields[j])[0].value = "";
				break;
			}
		}
	}
}


// Prevention plan

function onClickInspectionInterval(checkbox)
{
	var aCheckboxes = [ "cb_interval_quarterly", "cb_interval_semiannual", "cb_interval_annual" ];
	onClickRadioStyleCheckbox(aCheckboxes, checkbox);
}


function onClickCriticalPoints(checked)
{
	var aCheckboxes = [ "cb_cp_electricity_supply", "cb_cp_capacitors", "cb_cp_hi_pressure_pipe", "cb_cp_evaporators", "cb_cp_lo_pressure_pipe", "cb_cp_filters_valves" ];	
	if( checked ) {
	}
	else {
		for(var i = 0; i < aCheckboxes.length; i++)
			document.getElementsByName(aCheckboxes[i])[0].checked = false;
	}
}


// Leak verify

function onClickTestResult(checkbox)
{
	var aCheckboxes = [ "cb_no_leaks", "cb_leaks" ];
	onClickRadioStyleCheckbox(aCheckboxes, checkbox);
	if( (checkbox.name == "cb_no_leaks" && checkbox.checked)
		|| (checkbox.name == "cb_leaks" && !checkbox.checked) ) {
		var aFields = [ "leakage_description", "corrective_action", "ref_to_restore_quantity" ];
		for(var i = 0; i < aFields.length; i++)
			document.getElementsByName(aFields[i])[0].value = "";
	}
}
