var last_edit = null;
var last_bold = null;

function save_desc_menu()
{
	//before saving, set last modify
	if (last_edit!=null)
		d_save(document.getElementById(last_edit),last_bold)
	
	//post
	var ofrm = document.getElementById("frm_menu_desc");
	if (ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}


function d_edit(obj_name, bold)
{
	var obj = obj_name;
	if(typeof(obj_name) =="string")
		obj = document.getElementById(obj_name);
	var vk_state = document.getElementById("vk_state").value;
	var cssVirtualKeyboardClass = (vk_state == '1') ? 'keyboardInput,' : '' ;
	
	//enable save button
	enableAction(1);
	
	var id = obj.id;
	var desc = obj.innerHTML;
	if (desc.indexOf("INPUT")==-1 && desc.indexOf("input")==-1)
	{
		save_last(obj);
		if (bold)
		{
			desc = desc.replace(/<\/?B>/ig,"");
			desc = desc.replace("</B>","");
			var input = "<input id=\"i_"+id+"\" class=\""+cssVirtualKeyboardClass+"standardTxt\" type=\"text\" onkeydown=\"checkNoTagChar(this,event);\" maxlength=\"40\" style=\"width:100%;font-weight:bold\" id=\""+id+"\" value=\""+desc+"\" />";
		}
		else
		{
			var input = "<input id=\"i_"+id+"\" class=\""+cssVirtualKeyboardClass+"standardTxt\" type=\"text\" onkeydown=\"checkNoTagChar(this,event);\" maxlength=\"40\" style=\"width:100%\" id=\""+id+"\" value=\""+desc+"\" />";
		}
		
		obj.innerHTML=input;
		last_edit = id;
		last_bold = bold;
		
	}
	else
	{
		var id = obj.id;
		var desc = document.getElementById("i_"+id).value;
		document.getElementById(id+"_val").value = desc;
		if (bold)
		{
			desc = "<B>"+desc+"</B>";
		}
		obj.innerHTML=desc;
	}
	
	
}

function d_save(obj, bold )
{
	var id = obj.id;
	if (document.getElementById("i_"+id)!=null)
	{
		var id = obj.id;
		var desc = document.getElementById("i_"+id).value;
		document.getElementById(id+"_val").value = desc;
		
		if (bold)
		{
			desc = "<B>"+desc+"</B>";
		}
		obj.innerHTML=desc;
		
	}
}

function save_last(obj)
{
	var id = obj.id;
	if (last_edit!=null && last_edit!=id)
	{
		//if (typeof VKI_visible != 'undefined')  // Virtual Keyboard ON
//		{
//			if(VKI_visible != "")
//			{
//				VKI_close();
//			}
//			d_save(document.getElementById(last_edit),last_bold);
//		}
//		else    // Virtual Keyboard OFF
//		{
			d_save(document.getElementById(last_edit),last_bold);
//		}
	}
}



//

//open and close view of slaves group
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
//			row.style.display = 'block';
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
