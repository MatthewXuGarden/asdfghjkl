var idlistbox = null;

function resetIndex1(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	document.getElementById(idlistbox+'1').selectedIndex=-1;
	//document.getElementById(idlistbox+'to2').disabled=true;
	//document.getElementById(idlistbox+'to1').disabled=false;
}

function resetIndex2(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	
	document.getElementById(idlistbox+'2').selectedIndex=-1;
	//document.getElementById(idlistbox+'to1').disabled=true;
	//document.getElementById(idlistbox+'to2').disabled=false;
}

function to2(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	to2New(list1,list2);
}
function to2New(list1,list2)
{
	var indice1 = list1.selectedIndex; 
	
	var sizeList2 = list2.length;
	if (indice1 < 0)
	{
    	alert(document.getElementById("nullselected").value);
    	return;
    }
    	
    var a = list1.options[indice1];
    var b = list2.options[sizeList2];
    var iscontained = false;
    for (i=0;i<sizeList2;i++)
    {
	    if (list2.options[i].value==a.value)
	    {
	    	alert(document.getElementById("doubleElement").value);
	    	iscontained = true;
	    	break;
	    }
	}
    
    if (iscontained==false)
    {
	    list2.options.add(new Option(a.text,a.value));
		list1.remove(indice1);
	    list2.selectedIndex=sizeList2;
	    list2.focus();
	}	
    updateClass(list1);
	updateClass(list2);
}

function multipleto2(obj,devobj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	multipleto2New(list1,list2,devobj);
}
function multipleto2New(list1,list2,devobj)
{
	//has many device
	if(devobj != null)
	{
		var deviceid = devobj.id;
		
		//get device description
		var device = document.getElementById(deviceid);
		var deviceDes = device.options[device.selectedIndex].text;
		
		
		//check empity
		var isEmpity = true;
		//check double
		var iscontained = false;
		var sizeList2 = list2.length;
		for(var i=0;i<sizeList2;i++)
		{
			list2.options[i].selected = false;
		}
		var addArray = new Array(); 
		var leftIndexArray = new Array();
		var j = 0;
		while (list1.selectedIndex != -1)
		{
			isEmpity = false;
			var add = list1.options[list1.selectedIndex];
			for (i=0;i<sizeList2;i++)
			{
			    if (list2.options[i].value==add.value)
			    {
			    	alert(document.getElementById("doubleElement").value);
			    	iscontained = true;
			    	break;
			    }
			}
			if(iscontained == false)
			{
				var oOption = document.createElement("OPTION");
				oOption.text=deviceDes+" -> "+add.text;
				oOption.value=add.value;;
				addArray[j] = oOption;
				leftIndexArray[j++] = list1.selectedIndex;
			}
			list1.options[list1.selectedIndex].selected = false;
		}
		if(isEmpity == true)
		{
	    	return;
		}
	    if (iscontained==false)
	    {
	    	for(var p=0;p<addArray.length;p++)
	    	{
	    		list2.options.add(new Option(addArray[p].text,addArray[p].value));
	    		list2.options[sizeList2+p].selected = true;
	    	}
	    	for(p = leftIndexArray.length-1;p>=0;p--)
	    	{
	    		list1.remove(leftIndexArray[p]);
	    	}
		}
	    if(list1!=null && list2!=null ){
	    	for( i=0;i<list1.length;i++)
			{
		    	list1.options[i].className = i%2==0?"Row1":"Row2";
			}
			for( i=0;i<list2.length;i++)
			{
				list2.options[i].className = i%2==0?"Row1":"Row2";
			}
	    }
	}
	//only in one device
	else
	{		
		//check empity
		var isEmpity = true;
		//check double
		var iscontained = false;
		var sizeList2 = list2.length;
		for(var i=0;i<sizeList2;i++)
		{
			list2.options[i].selected = false;
		}
		var addArray = new Array(); 
		var leftIndexArray = new Array();
		var j = 0;
		while (list1.selectedIndex != -1)
		{
			isEmpity = false;
			var add = list1.options[list1.selectedIndex];
			for (i=0;i<sizeList2;i++)
			{
			    if (list2.options[i].value==add.value)
			    {
			    	alert(document.getElementById("doubleElement").value);
			    	iscontained = true;
			    	break;
			    }
			}
			if(iscontained == false)
			{
				var oOption = document.createElement("OPTION");
				oOption.text=add.text;
				oOption.value=add.value;;
				addArray[j] = oOption;
				leftIndexArray[j++] = list1.selectedIndex;
			}
			list1.options[list1.selectedIndex].selected = false;
		}
		if(isEmpity == true)
		{
	    	return;
		}
	    if (iscontained==false)
	    {
	    	for(var p=0;p<addArray.length;p++)
	    	{
	    		list2.options.add(new Option(addArray[p].text,addArray[p].value));
	    		list2.options[sizeList2+p].selected = true;
	    	}
	    	for(p = leftIndexArray.length-1;p>=0;p--)
	    	{
	    		list1.remove(leftIndexArray[p]);
	    	}
		}
	    if(list1!=null && list2!=null ){
		    for( i=0;i<list1.length;i++)
			{
				list1.options[i].className = i%2==0?"Row1":"Row2";
			}
			for( i=0;i<list2.length;i++)
			{
				list2.options[i].className = i%2==0?"Row1":"Row2";
			}
	    }
	}	
	updateClass(list1);
	updateClass(list2);
}

function multipleto2MaxItemsChk(obj,devobj,maxitems)
{
	//has many device
	if(devobj != null)
	{
		var tmp = obj.id;
		var deviceid = devobj.id;
		idlistbox = tmp.substring(0,(tmp.length-1));
		var list1 = document.getElementById(idlistbox+'1');
		var list2 = document.getElementById(idlistbox+'2');
		
		//get device description
		var device = document.getElementById(deviceid);
		var deviceDes = device.options[device.selectedIndex].text;
		
		
		//check empity
		var isEmpity = true;
		//check double
		var iscontained = false;
		var sizeList2 = list2.length;
		for(var i=0;i<sizeList2;i++)
		{
			list2.options[i].selected = false;
		}
		var addArray = new Array(); 
		var leftIndexArray = new Array();
		var j = 0;
		var items = 0;
		while (list1.selectedIndex != -1)
		{
			isEmpity = false;
			var add = list1.options[list1.selectedIndex];
			for (i=0;i<sizeList2;i++)
			{
			    if (list2.options[i].value==add.value)
			    {
			    	alert(document.getElementById("doubleElement").value);
			    	iscontained = true;
			    	break;
			    }
			}
			if(iscontained == false)
			{
				var oOption = document.createElement("OPTION");
				oOption.text=deviceDes+" -> "+add.text;
				oOption.value=add.value;;
				addArray[j] = oOption;
				leftIndexArray[j++] = list1.selectedIndex;
				items++;
			}
			list1.options[list1.selectedIndex].selected = false;
		}
		if(items+sizeList2> maxitems)
		{
			alert(document.getElementById("maxelements").value+" "+maxitems);
			return;
		}
		if(isEmpity == true)
		{
			alert(document.getElementById("nullselected").value);
	    	return;
		}
		var enable = true;
	    if (iscontained==false)
	    {
	    	for(var p=0;p<addArray.length;p++)
	    	{
	    		list2.options.add(new Option(addArray[p].text,addArray[p].value));
	    		list2.options[sizeList2+p].selected = true;
	    		if(enable == true)
	    		{
		    	    if (isIP(addArray[p].text) == false) 
		    	    {
	    	    		if (document.getElementById("listacomm") && (document.getElementById("listacomm").value != ""))
	    	    		{
	    	    			enable = false;
	    	    			disableAction(1);
	    	    		}
		    	    }
	    		}
	    	}
	    	for(p = leftIndexArray.length-1;p>=0;p--)
	    	{
	    		list1.remove(leftIndexArray[p]);
	    	}
		}
	}
	//only in one device
	else
	{
		tmp = obj.id;
		idlistbox = tmp.substring(0,(tmp.length-1));
		list1 = document.getElementById(idlistbox+'1');
		list2 = document.getElementById(idlistbox+'2');
		
		//check empity
		var isEmpity = true;
		//check double
		var iscontained = false;
		var sizeList2 = list2.length;
		for(var i=0;i<sizeList2;i++)
		{
			list2.options[i].selected = false;
		}
		var addArray = new Array(); 
		var leftIndexArray = new Array();
		var j = 0;
		var items = 0;
		while (list1.selectedIndex != -1)
		{
			isEmpity = false;
			var add = list1.options[list1.selectedIndex];
			for (i=0;i<sizeList2;i++)
			{
			    if (list2.options[i].value==add.value)
			    {
			    	alert(document.getElementById("doubleElement").value);
			    	iscontained = true;
			    	break;
			    }
			}
			if(iscontained == false)
			{
				var oOption = document.createElement("OPTION");
				oOption.text=add.text;
				oOption.value=add.value;;
				addArray[j] = oOption;
				leftIndexArray[j++] = list1.selectedIndex;
				items++;
			}
			list1.options[list1.selectedIndex].selected = false;
		}
		if(items+sizeList2> maxitems)
		{
			alert(document.getElementById("maxelements").value+" "+maxitems);
			return;
		}
		if(isEmpity == true)
		{
			alert(document.getElementById("nullselected").value);
	    	return;
		}
		var enable = true;
	    if (iscontained==false)
	    {
	    	for(var p=0;p<addArray.length;p++)
	    	{
	    		list2.options.add(new Option(addArray[p].text,addArray[p].value));
	    		for(var q=0;q<list2.length;q++){
	    			list2.options[q].className = (q%2==0)?"Row1":"Row2";
	    		}
	    		list2.options[sizeList2+p].selected = true;
	    		if(enable == true)
	    		{
		    	    if (isIP(addArray[p].text) == false) 
		    	    {
	    	    		if (document.getElementById("listacomm") && (document.getElementById("listacomm").value != ""))
	    	    		{
	    	    			enable = false;
	    	    			disableAction(1);
	    	    		}
		    	    }
	    		}
	    	}
	    	
	    		
	    	for(p = leftIndexArray.length-1;p>=0;p--)
	    	{
	    		list1.remove(leftIndexArray[p]);
	    	}
	    	for(q=0;q<list1.length;q++){
    			list1.options[q].className = (q%2==0)?"Row1":"Row2";
    		}
		}
	}
}
function to1(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var indice2 = list2.selectedIndex; 
		
	var sizeList1 = list1.length;
	if (indice2 < 0)
	{
    	alert(document.getElementById("nullselected").value);
    	return;
    }
    	
    
    var a = list2.options[indice2];
    var b = list1.options[sizeList1];
	list1.options.add(new Option(a.text,a.value));
	list2.remove(indice2);
    list1.selectedIndex=sizeList1;	
    list1.focus();
}


function multipleto1(obj,devobj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	multipleto1New(list1,list2,devobj);
}
function multipleto1New(list1,list2,devobj)
{
	//has many device
	if(devobj != null)
	{
		var tmpDevice = devobj.id;
		var device = document.getElementById(tmpDevice);
		var deviceDes = device.options[device.selectedIndex].text;
		var sizeList1 = list1.length;
		for(var i=0;i<sizeList1;i++)
		{
			list1.options[i].selected = false;
		}
		var isEmpity = true;
		var addArray = new Array();
		var rightIndexArray = new Array();
		var i=0;
		var j=0;
		while(list2.selectedIndex != -1)
		{
			isEmpity = false;
			var deleteOption = list2.options[list2.selectedIndex];
			//substring end with -1, because -1 is the space
			var deleteDeviceDes = deleteOption.text.substring(0,deleteOption.text.lastIndexOf("->")-1);
			//substring start from +3, because +2 is the space
			var deleteVarDes = deleteOption.text.substring(deleteOption.text.lastIndexOf("->")+3,deleteOption.text.length);
			if(deleteDeviceDes == deviceDes)
			{
				var oOption = document.createElement("OPTION");
				oOption.text = deleteVarDes;
				oOption.value = deleteOption.value;
				addArray[i++] = oOption;
			}
			rightIndexArray[j++] = list2.selectedIndex;
			list2.options[list2.selectedIndex].selected = false;
		}
		if(isEmpity == true)
		{
	    	return;
		}
		
		for(i=0;i<addArray.length;i++)
		{
			list1.options.add(new Option(addArray[i].text,addArray[i].value));
			list1.options[sizeList1+i].selected = true;
		}
		for(i=rightIndexArray.length-1;i>=0;i--)
		{
			list2.remove(rightIndexArray[i]);
		}
		if(list1!=null && list2!=null ){
			for( i=0;i<list1.length;i++)
			{
				list1.options[i].className = i%2==0?"Row1":"Row2";
			}
			for( i=0;i<list2.length;i++)
			{
				list2.options[i].className = i%2==0?"Row1":"Row2";
			}
		}
	}
	//only one device
	else
	{
		var sizeList1 = list1.length;
		for(var i=0;i<sizeList1;i++)
		{
			list1.options[i].selected = false;
		}
		var isEmpity = true;
		var addArray = new Array();
		var rightIndexArray = new Array();
		var i=0;
		while(list2.selectedIndex != -1)
		{
			isEmpity = false;
			var deleteOption = list2.options[list2.selectedIndex];
			
			var oOption = document.createElement("OPTION");
			oOption.text = deleteOption.text;
			oOption.value = deleteOption.value;
			addArray[i] = oOption;
			rightIndexArray[i++] = list2.selectedIndex;
			list2.options[list2.selectedIndex].selected = false;
		}
		if(isEmpity == true)
		{
	    	return;
		}
		for(i=0;i<addArray.length;i++)
		{
			list1.options.add(new Option(addArray[i].text,addArray[i].value));
			list1.options[sizeList1+i].selected = true;
		}
		for(i=rightIndexArray.length-1;i>=0;i--)
		{
			list2.remove(rightIndexArray[i]);
		}
		if(list1!=null && list2!=null ){
			for( i=0;i<list1.length;i++)
			{
				list1.options[i].className = i%2==0?"Row1":"Row2";
			}
			for( i=0;i<list2.length;i++)
			{
				list2.options[i].className = i%2==0?"Row1":"Row2";
			}
		}
	}
	updateClass(list1);
	updateClass(list2);
}

// serve per partire col focus su una tabella
function setfocuslist(idlistafocus)
{	
	idlistbox = idlistafocus;
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	
	if (list1.length!=0)
	{
		list1.focus();
		list1.selectedIndex=0;
		return true;
	}
	if (list2.length!=0)
	{
		list2.focus();
		list2.selectedIndex=0;
		return true;
	}
}

function getList2Value(obj)
{
	var values = "";
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list2 = document.getElementById(idlistbox+'2');
	
	for (i=0;i<list2.length;i++)
	{
		values = values + list2.options[i].value;
		values = values + ",";
	}
	values = values.substring(0,(values.length-1));
	return values;
}

function getList1Value(obj)
{
	var values = "";
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	
	for (i=0;i<list1.length;i++)
	{
		values = values + list1.options[i].value;
		values = values + ",";
	}
	values = values.substring(0,(values.length-1));
	return values;
}

function to1Rem(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var indice2 = list2.selectedIndex; 
		
	var sizeList1 = list1.length;
	if (indice2 < 0)
	{
    	alert(document.getElementById("nullselected").value);
    	return;
    }
    	
    var a = list2.options[indice2];
    var b = list1.options[sizeList1];
    
	list2.remove(indice2);
    list1.selectedIndex=0;	
    list1.focus();
    
    //se st� eliminando un ip valido, ctrl se abilitare Save oppure no:
    if (isIP(a.text))
    {
    	if (document.getElementById("listacomm") && (document.getElementById("listacomm").value != ""))
    	{
    		// vedere setaction.js:
    		if ((!isIPinList(list2)) && (list2.length > 0)) //se lista destinazione vuota dopo delete IP abilito Save
    		{
    			disableAction(1); //senza Modem conf. e senza IP valido non abilito Save
    		}
    		else
    		{
    			enableAction(1); //senza Modem conf. ma con IP valido selezionato abilito Save
    		}
    	}
    }
    else
    	if (list2.length == 0)
    	{
    		enableAction(1);
    	}
}

// 20090116 #5289 no upper limit on the items' number
function to2notRemove1(obj) {
	// "-1" means "no upper limit"
	to2notRemove1MaxItemsChk(obj, -1);
}

// 20090116 issue #5289
function to2notRemove1MaxItemsChk(obj, maxitems)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var indice1 = list1.selectedIndex; 
	
	var sizeList2 = list2.length;
	if (indice1 < 0)
	{
    	alert(document.getElementById("nullselected").value);
    	return;
    }
    	
	// check on the upper limit of the items in the table #5289
	if (maxitems != -1 && sizeList2 >= maxitems) {
		alert(document.getElementById("maxelements").value+" "+maxitems);
		return true;
	}	
	
    var a = list1.options[indice1];
    var b = list2.options[sizeList2];
    var iscontained = false;
    for (i=0;i<sizeList2;i++)
    {
	    if (list2.options[i].value==a.value)
	    {
	    	alert(document.getElementById("doubleElement").value);
	    	iscontained = true;
	    	break;
	    }
	}
    
    if (iscontained==false)
    {
		list2.options.add(new Option(a.text,a.value));
		list1.selectedIndex=0;
	    list2.selectedIndex=sizeList2;
	    list1.selectedIndex=0;
	    list2.focus();
	    
	    // vedere setaction.js:
	    if (isIP(a.text)) enableAction(1); //abilito Save anche senza Modem solo se inserisco un IP valido
	    else
	    {
	    	if (isIPinList(list2))
	    	{
	    		enableAction(1); //senza Modem conf. ma con IP valido selezionato abilito Save
	    	}
	    	else
	    		if (document.getElementById("listacomm") && (document.getElementById("listacomm").value != ""))
	    		{
	    			disableAction(1); //senza Modem conf. e senza IP valido selezionato disabilito Save
	    		}
	    		else
	    		{
	    			enableAction(1);
	    		}
	    }
	}
}

function OnDivScroll(idselect,size)
{
    var lstCollegeNames = document.getElementById(idselect.id);

    
    if (lstCollegeNames.options.length > size)
    {
        lstCollegeNames.size=lstCollegeNames.options.length;
    }
    else
    {
        lstCollegeNames.size=size;
    }
}

function OnSelectFocus(obj,size)
{
    if (document.getElementById("divCollegeNames").scrollLeft != 0)
    {
        document.getElementById("divCollegeNames").scrollLeft = 0;
    }

    var lstCollegeNames = obj;
    
    if( lstCollegeNames.options.length >size)
    {
        lstCollegeNames.focus();
        lstCollegeNames.size=size;
    }
}

// ***************
// Utilities x setaction:
// ***************

//ctrl se il valore � un IP:
function isIP(valore)
{
	var numeri = valore.split("-> "); //ricavo valore IP
	
	if (numeri.length > 1)
	{
		var nums = numeri[1].split("."); //ricavo componenti IP
		
		if (nums.length != 4) return false; //IP formato da 4 numeri fra 0 e 255
		
		return ((nums[0]>=0) && (nums[0]<=255) && (nums[1]>=0) && (nums[1]<=255) && (nums[2]>=0) && (nums[2]<=255) && (nums[3]>=0) && (nums[3]<=255));
	}
	else
		return false;
}

//ctrl se in addressbook c'� almeno un ip valido:
function isIPinList(lista)
{
	var sizeLista = lista.length;
    	
    for (var i=0; i < sizeLista; i++)
    {
	    if (isIP(lista.options[i].text))
	    {
	    	return true;
	    }
	}
	
	return false;
}


function containsElement(listbox, element)
{
	for(var i = 0; i < listbox.length; i++)
		if( listbox.options[i].value == element.value )
			return true;
	return false;
}
function removeDuplicated(source,target)
{
	for(var i=0;i<source.length;i++)
	{
		var sourceId = source.options[i].value;
		for(var j=target.length-1;j>=0;j--)
		{
			var targetId = target.options[j].value;
			if(sourceId == targetId)
			{
				target.remove(j);
			}
		}
	}
	updateClass(target);
}
function updateClass(list)
{
	for(var i=0;i<list.length;i++)
	{
		list.options[i].className = (i%2==0)?"Row1":"Row2";
	}
}