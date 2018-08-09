// Site Booklet - Catalog
// actions
var ACTION_MODIFY	= 1;
var ACTION_SAVE		= 2;
var ACTION_ADD		= 3;
var ACTION_RESET	= 4;
// commands
var COMMAND_ADD		= "add";
var COMMAND_RESET	= "reset";
// booklet
var idsite			= 0;
// aliases
var aaCatalog		= null;
var FLAG_CAT_DEFAULT= 0x00;
var FLAG_CAT_BOOKLET= 0x01;
var FLAG_CAT_RESET	= 0x02;
var COL_ID			= 1;
var COL_FLAGS		= 9;


function onCatalogLoad()
{
	var objNewBooklet = document.getElementById("idNewBooklet");
	if( objNewBooklet ) {
		onModifyBooklet(objNewBooklet.value);
	}
	else {
		aaCatalog = Lsw1.mData;
		enableAction(ACTION_ADD);
		checkActions();
	}
}


function onAdd()
{
	disableAction(ACTION_ADD);
	document.getElementById("divCatalogEntry").style.display = "block";
	enableAction(ACTION_SAVE);
}


function onSave()
{
	if( !document.getElementById("name").value || !document.getElementById("description").value ) {
		alert(document.getElementById("required_fields").value);
		return;
	}
	
	document.getElementById("cmd").value = COMMAND_ADD;
	var form = document.getElementById("frm_site_booklet_cat");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onViewEdit()
{
	if( idsite > 0 )
		onModifyBooklet(idsite);
}


function onReset()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;
	
	document.getElementById("cmd").value = COMMAND_RESET;
	document.getElementById("idsite").value = idsite;
	var form = document.getElementById("frm_site_booklet_cat");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectBooklet(id)
{
	if( id != idsite )
		idsite = id;
	else
		idsite = 0;
	checkActions();
}


function onModifyBooklet(id)
{
	top.frames['manager'].loadTrx("nop&folder=sitebooklet&bo=BSiteBooklet&type=click&idbooklet=" + id);
}


function checkActions()
{
	if( idsite > 0 ) {
		enableAction(ACTION_MODIFY);
		for(var i = 0; i < aaCatalog.length; i++) {
			if( aaCatalog[i][COL_ID] == idsite ) {
				if( (aaCatalog[i][COL_FLAGS] & FLAG_CAT_RESET) == FLAG_CAT_RESET )
					disableAction(ACTION_RESET);
				else
					enableAction(ACTION_RESET);
				break;
			}
		}
	}
	else {
		disableAction(ACTION_MODIFY);
		disableAction(ACTION_RESET);
	}
}
