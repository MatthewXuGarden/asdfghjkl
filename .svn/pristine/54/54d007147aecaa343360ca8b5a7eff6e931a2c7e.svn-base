var selectedVal="";
var last_sel = -1;
var row_class = -1;
var save_on_black="images/actions/save_on_black.png";

function remove_measure(){
	var x = document.getElementById("combo_misure");
	if (confirm(document.getElementById("delmeasure").value))
	{
		var unit_misura = document.getElementById("combo_misure").value;
  		CommSend("servlet/ajrefresh","POST","cmd=remove_unitmeasure&combo_misure="+unit_misura+"","Removemeasure", true);
		selectedVal = unit_misura;
	}
	return false;
}

function Callback_Removemeasure()
{
	var measure = new Array(1);
	var attributes = xmlResponse.getElementsByTagName('attrib');
	measure[0] = attributes[0].childNodes[0].nodeValue;	//esito
	if(measure[0] == "ok"){
		var x = document.getElementById("combo_misure");
		var valore_da_eliminare = x.options[x.selectedIndex].innerHTML
		x.options[x.selectedIndex]=null;
		var num_combo = document.getElementById("num").value;
		/*
		for(i=0; i < num_combo; i++){
			var combo_i = document.getElementById("misura_"+i);
			if(valore_da_eliminare != combo_i.value){
				var indice_da_eliminare = -1
				for(z=0; z < combo_i.options.length; z++){
					if(combo_i.options[z].value == valore_da_eliminare){
						indice_da_eliminare = z;
					}
				}
				if(indice_da_eliminare != -1)
					combo_i.options[indice_da_eliminare]=null;
			}
		}
		*/
		alert(document.getElementById("deletemeasureok").value);
	}
	else
		alert(document.getElementById("deletemeasurefailed").value);

	devdescReload();
}

function save_update_measure()
{
	var ret = hide_set(-2); //setta la riga in tabella per non lasciarla in sospeso
	if (ret<0)
		return;
	if(document.getElementById("unit_misura").value == '')
	{
		alert(document.getElementById("missingfield").value);
		return false;
	}
	document.getElementById("unit_misura").value = document.getElementById("unit_misura").value.replace(/^\s+/,'').replace(/\s+$/,''); //Rimuovo spazi a dx e sx
	var x = document.getElementById("combo_misure");
	for(i=0; i< x.options.length; i++)
	{
			if(document.getElementById("unit_misura").value == x[i].innerHTML)
			{
				alert(document.getElementById("measurejustpres").value);
				return false;
			}
	}
	if (confirm(document.getElementById("confermmeasure").value))//Inserisco nuova unit? di minsura
	{
		//document.getElementById("cmd").value="new_unitmeasure";
		var unit_misura = document.getElementById("unit_misura").value;
		//CommSend("servlet/ajrefresh","POST","cmd=new_unitmeasure&unit_misura="+unit_misura+"","Setnewmeasure", true);
		CommSend("servlet/ajrefresh","POST","cmd=new_unitmeasure&unit_misura="+escape(unit_misura)+"","Setnewmeasure", true);
		//CommSend("servlet/ajrefresh","POST","cmd=new_unitmeasure&unit_misura="+encodeURIComponent(unit_misura)+"","Setnewmeasure", true);
  		//CommSend("servlet/ajrefresh","POST","cmd=new_unitmeasure&unit_misura="+encodeURI(unit_misura)+"","Setnewmeasure", true);
	}
	return false;
}

function Callback_Setnewmeasure()
{
	var measure = new Array(2);
	var attributes = xmlResponse.getElementsByTagName('attrib');
	measure[0] = attributes[0].childNodes[0].nodeValue;	//esito
	measure[1] = attributes[1].childNodes[0].nodeValue;	//id
	if(measure[0] == "ok")
	{
		var num_combo = Number(document.getElementById("num").value);
		var combo_misure = document.getElementById("combo_misure");
		var nome=document.getElementById("unit_misura").value;
		combo_misure.options.add(new Option(nome,measure[1]));
		alert(document.getElementById("insertmeasureok").value);
	}
	else
		alert(document.getElementById("insertmeasurefailed").value);

	devdescReload();
}

function devdescReload()
{
	var curiddev = document.getElementById("curdevid").value;
	top.frames['manager'].loadTrx('devdetail/FramesetTab.jsp&folder=devdetail&bo=BDevDetail&type=redirect&iddev='+curiddev);
}

function setValue(x){
	if(x.options[x.selectedIndex].innerHTML =='')
	{
		document.getElementById("unit_misura").value='';
		disabilita_remove_image();
	}
	else
	{
		abilita_remove_image();
		//document.getElementById("unit_misura").value = x.options[x.selectedIndex].innerHTML;
	}
}

function abilita_save_image()
{
	document.getElementById("save_img").src = save_on_black;
}

function abilita_remove_image()
{
	document.getElementById("imageOn").style.display="inline";
	document.getElementById("imageOff").style.display="none";
}

function disabilita_save_image()
{
	document.getElementById("save_img").src = save_off;
}

function disabilita_remove_image()
{
	document.getElementById("imageOn").style.display="none";
	document.getElementById("imageOff").style.display="inline";
}

function isActionSave()
{
	var x = document.getElementById("actionsave").value;
	if(x == "ok")
	{
		alert(document.getElementById("actionsaveOK").value)
	}
	else if(x != "noactionsave")
	{
		alert(document.getElementById("actionsaveKO").value)
	}
	document.getElementById("actionsave").value = "";
}

function descr_var_save()
{
	var ret = hide_set(-2); //setta la riga in tabella per non lasciarla in sospeso
	if (ret<0)
		return;
	document.getElementById("cmd").value="desc_var_save";
	var ofrm = document.getElementById("desc_var_save");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

// ################# Biolo: VERSIONE PAGINA LEGGERA  ####################

var last_sel = -1;
var row_class = -1;
var CELL_CODE = 1;
var CELL_DESCR = 2;
var CELL_UM = 3;
var emptyvardescerr = 0;

function mod_line(line)
{
	if(emptyvardescerr<0 || line == last_sel)
		return;
	var tab_line = line;
	//al click disabilito una eventuale seconda selezione
	//hide_set(last_sel,'testa'); NO - gi� fatto dal singleclick
	
	var table = document.getElementById("TableData1");

	// Alessandro : check the state for the virtual keyboard
	var vk_state = document.getElementById("vk_state").value;
	var cssVirtualKeyboardClass = (vk_state == '1') ? 'keyboardInput,' : '' ;
	
	// short desc
	var code = document.getElementById("shortDesc_"+line).value;
	code = rlp(code);
	table.rows[tab_line].cells[CELL_CODE].innerHTML="<input class='"+cssVirtualKeyboardClass+"standardTxt' style='width:100%;' maxlenght='45' id='set_sd' type='textarea' value=\""+code+"\" />";
	
	//descr
	var descr = document.getElementById("description_"+line).value;
	descr = rlp(descr);
	table.rows[tab_line].cells[CELL_DESCR].innerHTML="<input style='width:100%;' maxlenght='250' class='"+cssVirtualKeyboardClass+"standardTxt' id='set_d' type='textarea' value=\""+descr+"\" onblur='MioNoAtOnBlur(this);'/>";
	
	//um
	var um = document.getElementById("misura_"+line).value;
	table.rows[tab_line].cells[CELL_UM].innerHTML=document.getElementById("combo_um").value;
	document.getElementById("set_um").value=um;
	if (document.getElementById("set_um").selectedIndex==-1)
	{
		modifyCombo(um);
	}
			
	last_sel = line;
}


function hide_set(line)
{
	emptyvardescerr = 0;
	if (last_sel!=-1&&last_sel!=line&&document.getElementById("set_sd")!=null)
	{
		var descr = document.getElementById("set_d").value;
		descr = rlp(descr);
		if (descr==null||descr==''||descr.length==0)
		{
			select_row(last_sel);
			emptyvardescerr = -100;
			return -100;
		}
	}
	var tab_line = last_sel;
	if (line==last_sel) return;
	if (last_sel!=-1&&document.getElementById("set_sd")!=null)
	{
		var table = document.getElementById("TableData1");
		//code
		var code = document.getElementById("set_sd").value;
		code = rlp(code);
		document.getElementById("shortDesc_"+last_sel).value=code;
		table.rows[tab_line].cells[CELL_CODE].innerHTML=code;
		//descr
		var descr = document.getElementById("set_d").value;
		descr = rlp(descr);
		document.getElementById("description_"+last_sel).value=descr;
		table.rows[tab_line].cells[CELL_DESCR].innerHTML=descr;
		//um
		var um = "";
		if (document.getElementById("set_um").selectedIndex!=-1)
		{
			um = document.getElementById("set_um").options[document.getElementById("set_um").selectedIndex].innerHTML;
		}
		document.getElementById("misura_"+last_sel).value = um;
		table.rows[tab_line].cells[CELL_UM].innerHTML= um;
	}
}

function select_row(row)
{
	var n_row = row;
	var table = document.getElementById("TableData1");
	if (row_class>=0)
	{
		table.rows[row_class].className = row_class % 2 == 0 ? "Row1" : "Row2";
	}
	row_class = n_row;
	if (row_class>=0)
	{
		table.rows[row_class].className="selectedRow";
	}
}

function modifyCombo(um)
{
	if (um!="")
	{
		var combo = document.getElementById("set_um");
		combo.options.add(new Option(um,um));	
		document.getElementById("set_um").value=um;
	}
}

function rlp(str)
{
	try {
		return str.replace(/\"/g,"'");
	}
	catch(e) {
		return str;
	} 
}
