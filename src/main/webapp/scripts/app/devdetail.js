var two_month = 5184000;
var six_month = 15552000;
var year = 31536000;
var one_month = two_month / 2;
var one_week = one_month / 4;
var onehalf_year = year + six_month;
var selectedstate = 0;
var selectedhome = 0;
var maxselectedstate = 12;
var maxselectedhome = 6;
var currentrow;

function dev_save()
{
	var defLang = document.getElementById("defLang").value;
	if (document.getElementById(defLang).value=="")
	{
		alert(document.getElementById("missingdefault").value);
	}
	else
	{
	var isenab = document.getElementById("isenabled").checked;
	document.getElementById("chk_state").value=isenab;
	document.getElementById("cmd").value="dev_save";
	var ofrm = document.getElementById("frm_dev_save");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
	}
}



function dev_var_save()
{
	var showConfirm = needShowConfirm();
	var canpost = hide_set(-2);
	if (canpost)
	{
		if(showConfirm)
		{
			if(confirm(document.getElementById("log_save_confirm").value) == false)
				return;
		}
		document.getElementById("cmd").value="dev_var_save";
		var ofrm = document.getElementById("frm_dev_var_save");
		if (ofrm!=null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function freq_disab(idvar)
{
	if (document.getElementById("hist"+idvar).checked)
	{
		document.getElementById("freq"+idvar).value=300;
		document.getElementById("delta"+idvar).value="0";
		document.getElementById("prof"+idvar).selectedIndex=3;
		document.getElementById("freq"+idvar).disabled=false;
		document.getElementById("delta"+idvar).disabled=false;
		document.getElementById("prof"+idvar).disabled=false;
		document.getElementById("delta"+idvar).focus();
	}
	else
	{
		
		document.getElementById("freq"+idvar).value=300;
		document.getElementById("delta"+idvar).value="0";
		document.getElementById("prof"+idvar).selectedIndex=3;
		document.getElementById("freq"+idvar).disabled=true;
		document.getElementById("delta"+idvar).disabled=true;
		document.getElementById("prof"+idvar).disabled=true;
		
	}
}

function freq_disab_novarmin(idvar)
{
	if (document.getElementById("hist"+idvar).checked)
	{
		document.getElementById("prof"+idvar).selectedIndex=3;
		document.getElementById("prof"+idvar).disabled=false;	
		document.getElementById("freq"+idvar).disabled=false;	
		document.getElementById("freq"+idvar).value=300;
	}
	else
	{
		document.getElementById("prof"+idvar).selectedIndex=3;
		document.getElementById("prof"+idvar).disabled=true;
		document.getElementById("freq"+idvar).value=300;
		document.getElementById("freq"+idvar).disabled=true;
	}
}


function load_var_desc(idvar)
{
	document.getElementById("idvar").value=idvar;
	document.getElementById("cmd").value="load_var_desc";
	var ofrm = document.getElementById("desc_var_save");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function descr_var_save()
{
	document.getElementById("cmd").value="desc_var_save";
	var ofrm = document.getElementById("desc_var_save");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function enableButton()
{
	var idvar = document.getElementById("idvar").value;
	if (idvar!=-1)
	{
		enableAction(1);
	}
}



function detailOnload()
{
	if (document.getElementById("nospace").value=="NO")
	{
		alert(document.getElementById("s_nospace").value);
	}
	if (!document.getElementById("historerror").value=="")
	{
		alert(document.getElementById("historerror").value);
	}
}

function goUp(obj)
{
  var list=document.getElementById(obj.id);
  if (list.selectedIndex>0)
  {
    //non sono sulla prima entry
    from=list.selectedIndex;
    to=list.selectedIndex - 1;
    var auxValue =list[from].value;
    var auxDescr =list[from].innerHTML;

    list[from].value=list[to].value;
    list[from].innerHTML=list[to].innerHTML;

    list[to].value=auxValue;
    list[to].innerHTML=auxDescr;
    
    list.selectedIndex=to;
   }
}

function goDown(obj)
{
  var list=document.getElementById(obj.id);
  var dim = list.length;
  if (list.selectedIndex<(dim -1))
  {
    //non sono sull'ultima entry
    from=list.selectedIndex;
    to=list.selectedIndex + 1;
    var auxValue =list[from].value;
    var auxDescr =list[from].innerHTML;

    list[from].value=list[to].value;
    list[from].innerHTML=list[to].innerHTML;

    list[to].value=auxValue;
    list[to].innerHTML=auxDescr;
  
    list.selectedIndex=to;
   }
}

function HScroll()
{
  try
  {
    head = document.getElementById("thead");
	data = document.getElementById("tdata");
	head.scrollLeft=data.scrollLeft;

    if (head.scrollLeft!=data.scrollLeft)
    {
      data.scrollLeft=head.scrollLeft;
      return false;
    }
  }
  catch(e)
  {
  }
}





/**
* Per ridimensionare la tabella variabili
*/
function resizeTableVariable()
{
	var hdev = MTcalcObjectHeight("trVarList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-10);
	}
}

/**
* Per ridimensionare la tabella variabili
*/
function resizeTableDescrList()
{
	var hdev = MTcalcObjectHeight("trDescrList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-29);
	}
}


function readonlyclick(obj)
{
	var oDiv = document.getElementById("readonlytable");
	var oDiv2 = document.getElementById("readwritetable");
	var rwimg = document.getElementById("rwimg");
	
	if(oDiv != null)
	{
		if(oDiv.style.visibility == "hidden")
		{
			oDiv.style.visibility = "visible";
			oDiv.style.display = "block";
			obj.src = "images/lsw/close.gif";
			obj.title = document.getElementById("close").value;
			/*
			rwimg.src = "images/lsw/open.gif";
			rwimg.title = document.getElementById("open").value;

			oDiv2.style.visibility = "hidden";
			oDiv2.style.display = "none";
			*/
		}
		else
		{
			oDiv.style.visibility = "hidden";
			oDiv.style.display = "none";
			obj.src = "images/lsw/open.gif";
			obj.title = document.getElementById("open").value;
			/*
			rwimg.src = "images/lsw/close.gif";
			rwimg.title = document.getElementById("close").value;

			oDiv2.style.visibility = "visible";
			oDiv2.style.display = "block";
			*/
		}
	}
}

function readwriteclick(obj)
{
	var oDiv = document.getElementById("readwritetable");
	var oDiv2 = document.getElementById("readonlytable");
	var roimg = document.getElementById("roimg");
	
	if(oDiv != null)
	{
		if(oDiv.style.visibility == "hidden")
		{
			oDiv.style.visibility = "visible";
			oDiv.style.display = "block";
			obj.src = "images/lsw/close.gif";
			obj.title = document.getElementById("close").value;
			/*
			roimg.src = "images/lsw/open.gif";
			roimg.title = document.getElementById("open").value;

			oDiv2.style.visibility = "hidden";
			oDiv2.style.display = "none";
			*/
		}
		else
		{
			oDiv.style.visibility = "hidden";
			oDiv.style.display = "none";
			obj.src = "images/lsw/open.gif";
			obj.title = document.getElementById("open").value;
			/*
			roimg.src = "images/lsw/close.gif";
			roimg.title = document.getElementById("close").value;

			oDiv2.style.visibility = "visible";
			oDiv2.style.display = "block";
			*/
		}
	}
}

function dev_varpos_save()
{
	document.getElementById("cmd").value = "dev_varpos_save";
	
	if(document.getElementById("ab").value == "b")
	{
		varpos_fill_order();
	}
	
	var ofrm = document.getElementById("frm_dev_varpos_save");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function dev_varpos_select()
{
	disableAction(1);
	enableAction(2);

	document.getElementById("cmd").value = "dev_varpos_select";
	document.getElementById("ab").value = "a";
	
	varpos_fill_order();
	
	var ofrm = document.getElementById("frm_dev_varpos_save");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function dev_varpos_order()
{
	disableAction(2);
	enableAction(1);

	document.getElementById("cmd").value = "dev_varpos_order";
	document.getElementById("ab").value = "b";
	
	var ofrm = document.getElementById("frm_dev_varpos_save");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function checkradiooption(id)
{
	if(document.getElementById("STAT"+id).checked == true)
	{
		if((selectedstate+1) > maxselectedstate)
		{
			alert(document.getElementById("maxstate").value);
			document.getElementById(""+document.getElementById("prev"+id).value+id).checked = true;
		}
		else
		{
			if(document.getElementById("prev"+id).value=="HOME")
			{
				selectedhome--;
			}
			selectedstate++;
			document.getElementById("prev"+id).value = "STAT";
		}
	}
	if(document.getElementById("HOME"+id).checked == true)
	{
		if((selectedhome+1) > maxselectedhome)
		{
			alert(document.getElementById("maxprobe").value);
			document.getElementById(""+document.getElementById("prev"+id).value+id).checked = true;
		}
		else
		{
			if(document.getElementById("prev"+id).value=="STAT")
			{
				selectedstate--;
			}
			selectedhome++;
			document.getElementById("prev"+id).value = "HOME";
		}
	}
	if(document.getElementById("MAIN"+id).checked == true)
	{
		if(document.getElementById("prev"+id).value=="STAT")
		{
			selectedstate--;
		}
		if(document.getElementById("prev"+id).value=="HOME")
		{
			selectedhome--;
		}
		document.getElementById("prev"+id).value = "MAIN";
	}
	if(document.getElementById("NONE"+id).checked == true)
	{
		if(document.getElementById("prev"+id).value=="STAT")
		{
			selectedstate--;
		}
		if(document.getElementById("prev"+id).value=="HOME")
		{
			selectedhome--;
		}
		document.getElementById("prev"+id).value = "NONE";
	}
}

function inittab6()
{
	//conta state e home
	var radios = document.getElementsByTagName("input");
	for(var k=0; k<radios.length; k++)
	{
		if(radios[k].type=="radio")
		{
			if(radios[k].id.substring(0,4)=="STAT" && radios[k].checked)
				selectedstate++;
			if(radios[k].id.substring(0,4)=="HOME" && radios[k].checked)
				selectedhome++;
		}
	}
}

/*
function displaytablevar(section)
{
	document.getElementById('orderstatetable').style.visibility = "hidden";
	document.getElementById('orderstatetable').style.display = "none";

	document.getElementById('orderprobetable').style.visibility = "hidden";
	document.getElementById('orderprobetable').style.display = "none";

	document.getElementById('orderrotable').style.visibility = "hidden";
	document.getElementById('orderrotable').style.display = "none";

	document.getElementById('orderrwtable').style.visibility = "hidden";
	document.getElementById('orderrwtable').style.display = "none";

	document.getElementById(section).style.visibility = "visible";
	document.getElementById(section).style.display = "block";
}
*/

function varpos_rowsel(arow, atable)
{
	if(currentrow) 
		currentrow.className = "Row1";
	if(arow.className!="selectedRow")
	{
		currentrow = arow;
		arow.className="selectedRow";
	}
	else
	{
		currentrow=null;
		arow.className="Row1";
	}
}

function varpos_moveup(table)
{
	if( !currentrow )
		return;
	
	if (currentrow.parentElement.parentElement.id != table)
		return;
	
	if(currentrow.rowIndex>1)
	{
		var ttt = currentrow.parentElement;
		var ri = currentrow.rowIndex;
		ttt.insertBefore(currentrow,ttt.rows[ri-2]);
	}
}

function varpos_movedown(table)
{
	if( !currentrow )
		return;

	if (currentrow.parentElement.parentElement.id != table)
		return;

	if(currentrow.rowIndex < currentrow.parentElement.rows.length)
	{
		var ttt = currentrow.parentElement;
		var ri = currentrow.rowIndex;
		ttt.insertBefore(ttt.rows[ri],currentrow);
	}
}

function varpos_fill_order()
{
	document.getElementById("varposorder").value = "";
	
	var statesRows = document.getElementById("orderstatetable2").tBodies[0].rows;
	var probesRows = document.getElementById("orderprobetable2").tBodies[0].rows;
	var roRows = document.getElementById("orderrotable2").tBodies[0].rows;
	var rwRows = document.getElementById("orderrwtable2").tBodies[0].rows;
	
	var ordertxt = "";
	
	for(var i1 = 0; i1 < statesRows.length; i1++)
	{
		ordertxt = ordertxt + statesRows[i1].cells[0].innerHTML + ",";
	}

	for(var i2 = 0; i2 < probesRows.length; i2++)
	{
		ordertxt = ordertxt + probesRows[i2].cells[0].innerHTML + ",";
	}

	for(var i3 = 0; i3 < roRows.length; i3++)
	{
		ordertxt = ordertxt + roRows[i3].cells[0].innerHTML + ",";
	}

	for(var i4 = 0; i4 < rwRows.length; i4++)
	{
		ordertxt = ordertxt + rwRows[i4].cells[0].innerHTML + ",";
	}
	
	document.getElementById("varposorder").value = ordertxt;
}

function isMainVarSave(){
	var x = document.getElementById("mainvarsave").value;
	if(x == "ok"){
		//alert(document.getElementById("mainvarsaveOK").value)
	}
	else if(x != "nomainvarsave")
	{
		alert(document.getElementById("mainvarsaveKO").value);
	}
	document.getElementById("mainvarsave").value = "";
}


function image_file_filter(input_name)
{
	var input_file = document.getElementById(input_name).value;
	if( input_file != "" && input_file.length > 4 ) {
		var exts = ".png,.jpg,jpeg,.gif,.bmp".split(",");
		for(var i=0; i<exts.length; i++) {
	  		if( input_file.substring(input_file.length-4,input_file.length).toLowerCase() == exts[i] )
				return true;
		}
	}
	else if (input_file == "")  //img default
	{
		return true;
	}
	return false;
} 


function image_submit()
{	
	if( !image_file_filter("changeimg") ) {
		alert(document.getElementById("isntimage").value);
		return false;
	}

	MTstartServerComm();
	document.getElementById("uploadfrm").submit();	
}
