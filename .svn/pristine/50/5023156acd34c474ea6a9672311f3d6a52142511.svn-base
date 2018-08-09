var clock = {
	idModelSelected: 0,
	BUTTON_ADD:1,
	BUTTON_MOD:2,
	BUTTON_DEL:3,
	BUTTON_SAV:4,
	columnName:['master','year','month','day','weekday','hour','minute'],
	xml_vars:null,
	xml_clock:null,
	onPageLoad: function(){
		enableAction(this.BUTTON_ADD);
		disableAction(this.BUTTON_MOD);
		disableAction(this.BUTTON_DEL);
		disableAction(this.BUTTON_SAV);
		for (var i=0; i < this.columnName.length; i++)
		{
			$("#clock_frm").append("<div class=\"divrow "+this.columnName[i]+"row\" onclick =\"clock.clickRow('"+this.columnName[i]+"')\" ondblclick=\"clock.editRow('"+this.columnName[i]+"')\">"+
				"<span class=\"label standardTxt\">"+$("#"+this.columnName[i]+"Label").val()+"</span>"+
				"<div class=\"combo standardTxt divFloat\">"+
					"<div class=\"comboDiv\"></div><div class=\"labelDiv\"></div>"+
				"</div>"+
				"<div class=\"btn divFloat addbtn\"><img src=\"images/actions/addsmall_on_black.png\" onclick=\"clock.editRow('"+this.columnName[i]+"')\"></div>"+
				"<div class=\"btn divFloat delbtn\"><img src=\"images/actions/removesmall_off.png\"></div>"+
				"<div class=\"descr standardTxt divFloat\"></div>"+
				"<div class=\"clearer\">"+
				"</div>");
			$("#clock_frm").prepend("<input type=\"hidden\" id=\""+this.columnName[i]+"\" name=\""+this.columnName[i]+"\" value=\"\"/>");
		}
	},
	addModel: function (){
		this.xml_vars = null;
		this.xml_clock = null;
		$("#editDiv").show();
		$(".dev .comboDiv").show();
		$(".dev .labelDiv").hide();
		for (var i=0; i < this.columnName.length; i++)
		{
			$("."+this.columnName[i]+"row").hide();
		}
		enableAction(this.BUTTON_SAV);
		disableAction(this.BUTTON_ADD);
		$("#cmd").val("add");
	},
	modifyModel: function()
	{
		if( this.idModelSelected >0 )
		{
			this.xml_vars = null;
			this.xml_clock = null;
			$("#cmd").val("edit");
			disableAction(this.BUTTON_ADD);
			disableAction(this.BUTTON_MOD);
			disableAction(this.BUTTON_DEL);
			enableAction(this.BUTTON_SAV);
			$("#iddevmdl").val(this.idModelSelected);
			CommSend("servlet/ajrefresh","POST","cmd=modifyModel&iddevmdl="+this.idModelSelected+"","editModel",true);
		}
	},
	removeModel: function()
	{
		if( !confirm($("#confirm").val()) )
			return;
		if( this.idModelSelected >0 )
		{
			$("#cmd").val("remove");
			$("#iddevmdl").val(this.idModelSelected);
			var form = $("#clock_frm");
			if( form != null ) {
				MTstartServerComm();
				form.submit();
			}
		}
	},
	saveModel: function()
	{
		var allEmpty = true;
		for (var i=0; i < this.columnName.length; i++) 
		{
			if($("#"+this.columnName[i]).val() != "")
			{
				allEmpty = false;
				break;
			}
		}
		if(allEmpty)
		{
			alert($("#atleastone").val());
			return;
		}
		var form = $("#clock_frm");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	},
	onSelectModel:function(id)
	{
		if( id != this.idModelSelected )
			this.idModelSelected = id;
		else
			this.idModelSelected = 0;
		this.checkButtons();
	},
	onModifyModel:function(id)
	{
		this.idModelSelected = id;
		this.modifyModel();
	},
	checkButtons:function()
	{
		if( this.idModelSelected >0 ) {
			enableAction(this.BUTTON_MOD);
			enableAction(this.BUTTON_DEL);
		}
		else {
			disableAction(this.BUTTON_MOD);
			disableAction(this.BUTTON_DEL);
		}
	},
	loadVar:function(obj)
	{
		for (var i=0; i < this.columnName.length; i++)
		{
			this.resetRow(this.columnName[i]);
		}
		var iddevmdl = obj.value;
		if(iddevmdl != -1)
		{
			$("#iddevmdl").val(iddevmdl);
			CommSend("servlet/ajrefresh","POST","cmd=loadvars&iddevmdl="+iddevmdl+"","loadVars",true);
		}
		else
		{
			this.addModel();
		}
	},
	initRowAfterLoad:function()
	{
		if(clock.xml_vars != null && clock.xml_vars.length>0)
		{
			var masterCombo = "";
			var rwCombo = "";
			for(var i=0;i<clock.xml_vars.length;i++)
			{
				masterCombo += "<option value='"+clock.xml_vars[i].childNodes[3].childNodes[0].nodeValue+"'>"+clock.xml_vars[i].childNodes[0].childNodes[0].nodeValue+"</option>";
				if(clock.xml_vars[i].childNodes[1].childNodes[0].nodeValue=="3" &&
					clock.xml_vars[i].childNodes[2].childNodes[0].nodeValue!="1")
					rwCombo += "<option value='"+clock.xml_vars[i].childNodes[3].childNodes[0].nodeValue+"'>"+clock.xml_vars[i].childNodes[0].childNodes[0].nodeValue+"</option>";
			}
			for (var i=0; i < this.columnName.length; i++)
			{
				var combo = "<select id='combo_"+this.columnName[i]+"' onchange=\"clock.varComboChange('"+this.columnName[i]+"',this)\"><option value=''>--------------------------------------------------------"+"</option>";
				if(this.columnName[i] == "master")
					combo += masterCombo+"</select>";
				else
					combo += rwCombo + "</select>";
				$("."+this.columnName[i]+"row .combo .comboDiv ").html(combo);
			}
		}
		if(clock.xml_clock != null && clock.xml_clock.length>0)
		{
			$("#editDiv").show();
			$("#iddevmdl").val(this.idModelSelected);
			var str = "<select disabled><option>"+clock.xml_clock[0].childNodes[0].childNodes[0].nodeValue+"</option></select>";
			$(".dev .labelDiv").html(str);
			$(".dev .labelDiv").show();
			$(".dev .comboDiv").hide();
			var varCode = clock.xml_clock[0].childNodes[1];
			var varDescription = clock.xml_clock[0].childNodes[2];
			for (var i=0; i < this.columnName.length; i++) 
			{
				$("."+this.columnName[i]+"row .combo .labelDiv").html(varDescription.childNodes[i].childNodes[0].nodeValue);
				$("#"+this.columnName[i]).val(varCode.childNodes[i].childNodes[0].nodeValue);
				$("#combo_"+this.columnName[i]).val(varCode.childNodes[i].childNodes[0].nodeValue);
				if(varCode.childNodes[i].childNodes[0].nodeValue != "")
				{
					$("."+this.columnName[i]+"row .delbtn").html("<img src='images/actions/removesmall_on_black.png' onclick=\"clock.resetRow('"+this.columnName[i]+"')\">");
				}
			}
		}
		else
		{
			for (var i=0; i < this.columnName.length; i++) 
			{
				$("."+this.columnName[i]+"row .combo .labelDiv").html("");
				$("#"+this.columnName[i]).val("");
			}
		}
		for (var i=0; i < this.columnName.length; i++)
		{
			$("."+this.columnName[i]+"row").show();
		}
	},
	clickRow:function(type)
	{
		this.hideComboShowLabel(type);
	},
	editRow:function(type)
	{
		$("."+type+"row .combo .comboDiv").show();
		$("."+type+"row .combo .labelDiv").hide();
	},
	resetRow:function(type)
	{
		$("."+type+"row .combo .comboDiv").hide();
		$("."+type+"row .combo .labelDiv").hide();
		$("."+type+"row .combo .labelDiv").html("");
		$("#"+type).val("");
		$("."+type+"row .combo .comboDiv select").val("");
		$("."+type+"row .delbtn").html("<img src='images/actions/removesmall_off.png'>");
	},
	hideComboShowLabel:function(type)
	{
		for (var i=0; i < this.columnName.length; i++)
		{
			if(type == this.columnName[i])
				continue;
			$("."+this.columnName[i]+"row .combo .comboDiv").hide();
			$("."+this.columnName[i]+"row .combo .labelDiv").show();
		}
	},
	varComboChange:function(type,obj)
	{
		$("#"+type).val(obj.value);
		if(obj.value != "")
		{
			$("."+type+"row .delbtn").html("<img src='images/actions/removesmall_on_black.png' onclick=\"clock.resetRow('"+type+"')\">");
			$("."+type+"row .combo .labelDiv").html(obj.options[obj.selectedIndex].text);
		}
		else
		{
			$("."+type+"row .delbtn").html("<img src='images/actions/removesmall_off.png'>");
			$("."+type+"row .combo .labelDiv").html("");
		}
	}
}

function Callback_loadVars(xmlResponse)
{
	//variabili del device x combo
	clock.xml_vars = xmlResponse.getElementsByTagName("var");
	clock.initRowAfterLoad();
}
function Callback_editModel(xmlResponse)
{
	clock.xml_vars = xmlResponse.getElementsByTagName("var");
	clock.xml_clock = xmlResponse.getElementsByTagName("clock");
	clock.initRowAfterLoad();
}