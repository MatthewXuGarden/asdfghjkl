var LVD_ROW_SEP = "^?";
var LVD_COL_SEP = "^^";

function ListViewDyn(nameT,idT,cols,aSize,strData,aFields)
{
	this.idTableName = nameT;
	this.idTableNum  = idT;
	this.numCols     = cols;
	this.matrix      = new Array();
	this.aSizeCols   = aSize;
	this.rowSel 	 = null;
	this.counter	 = 1;
	this.fields      = aFields;
	this.stateaction = 0; // 0 -> insert, 1 -> update
	
	this.colSum = 0;
	for(var i=0; i<aSize.length; i++)
		this.colSum = (this.colSum + aSize[i]);
	
	this.init 	= LVD_init;
	this.render = LVD_render;
	this.add    = LVD_add;
	this.remove = LVD_remove;
	this.evidRow = LVD_evidRow;
	this.preUpdate = LVD_preUpdate;
	this.buildPost = LVD_buildPost;
		
	this.init(strData);
}

function LVD_init(strData)
{
	var i=0;
	var j=0;
	var arRows = strData.split(LVD_ROW_SEP);
	var arCols = null;
	for(i;i<arRows.length;i++)
		this.matrix.push(arRows[i].split(LVD_COL_SEP));
}

function LVD_render()
{
	var oIdTbBody = document.getElementById(this.idTableName);
	var arRows = null;
	var idrow = -1;
	var strow = "db";
	var sHtml = "";
	sHtml += "<table id=\"TableData"+this.idTableNum+"\" class=\"table\" onMouseUp=\"document.onmousemove=null;return false\" cellspacing=\"1px\">\n";
	sHtml += "<tbody id=\"TDBdy"+this.idTableNum+"\">";
	
	for(var i=0; i<this.matrix.length; i++)
	{
		arRows = this.matrix[i];
		idrow = arRows[0];
		state = arRows[arRows.length-1];
		
		if(state != 3) {
			sHtml += "<tr id=\"tr_"+idrow+"\" class=\"Row1\" onclick=\"LVD_selRows(this);\" ondblclick=\"LVD_dblclick(this);\">";
			for(var j=1; j<arRows.length-1; j++)
				sHtml += "<td align=\"left\" class=\"td\"><div id=\"div_"+idrow+"_"+j+"\" class=\"LV_DIV\" style=\"width:"+this.aSizeCols[j-1]+"px\" onselectstart=\"return false;\" nowrap>"+arRows[j]+"</div></td>";
		
			sHtml += "</tr>";
		}
	}
	
	sHtml += "</tbody>"
	sHtml += "</table>";
	oIdTbBody.innerHTML = sHtml;
	this.selrow = null;
}

function LVD_add(arData)
{
	if(this.stateaction == 0) {
		var arRo = new Array();
		arRo.push("C"+this.counter);
		this.counter++;
		for(var i=0; i<arData.length; i++)
			arRo.push(arData[i]);
		arRo.push(2);
		this.matrix.push(arRo);
	}
	else if(this.stateaction == 1)
	{
		if(this.rowSel != null)
		{
			var counter = 0;
			var arrow = null;
			var idrow = LVD_prvGetIndex(this.rowSel.id);
			if(idrow != -1)
			{
				for(var i=0; i<this.matrix.length; i++)
				{
					arrow = this.matrix[i];
					if(arrow != null)
					{
						if(arrow[0] == idrow) {
							for(var j=1; j<arrow.length-1; j++) {
								arrow[j] = arData[counter];
								counter++;
							}
							//solo se non è una new insert allora diventa un upd:
							if (arrow[arrow.length-1] != 2) arrow[arrow.length-1] = 4;
						}
					}
				}
			}
		}
		this.stateaction = 0;
	}
	
	this.render();
}

function LVD_remove()
{
	if(this.stateaction == 0)
	{
		if(this.rowSel != null)
		{
			var state = 0;
			var idrow = this.rowSel.id;
			var id_address = idrow.substring(3,idrow.length);
			
			if (Number(id_address)<0)
			{
				alert(document.getElementById("noremaddressfromide").value);
				return false;
			}
			var arrow = null;
			if(idrow != null && idrow.length > 3)
			{
				idrow = idrow.substring(3);
				for(var i=0; i<this.matrix.length; i++)
				{
					arrow = this.matrix[i];
					if(arrow != null)
					{
						if(arrow[0] == idrow) 
						{
							state = arrow[arrow.length-1];
							state = Number(state);
							switch(state){
								case 1:
									arrow[arrow.length-1] = 3;
									break;
								case 4:
									arrow[arrow.length-1] = 3;
									break;
								case 2:
									this.matrix.splice(i,1);
									break;
							}
							break;
						}
					}
				}
				this.render();
			}
		}
	}
}

function LVD_preUpdate()
{
	if(this.rowSel != null)
	{
		var state = 0;
		var counter = 0;
		var arrow = null;
		var idrow = LVD_prvGetIndex(this.rowSel.id);
		if(idrow != -1)
		{
			for(var i=0; i<this.matrix.length; i++)
			{
				arrow = this.matrix[i];
				if(arrow != null)
				{
					if(arrow[0] == idrow) {
						for(var j=1; j<arrow.length-1; j++){
							document.getElementById(this.fields[counter]).value = arrow[j];
							counter++;
						}
					}
				}
			}
		}
		this.stateaction = 1;
	}
}

function LVD_dblclick(obj)
{
	if(oDyn != null)
		oDyn.preUpdate();
}

function LVD_selRows(obj)
{
	if(oDyn != null)
		oDyn.evidRow(obj);
}

function LVD_evidRow(obj)
{
	if(this.rowSel != null)
		this.rowSel.className = "Row1";
	
	this.rowSel = obj;
	this.rowSel.className = "selectedRow";
	
	if(this.stateaction == 1)
	{
		for(var i=0; i<this.fields.length; i++)
			document.getElementById(this.fields[i]).value = "";
		this.stateaction = 0;
	}
}

function LVD_buildPost(sDiv)
{
	var oDiv = document.getElementById(sDiv);
	var arrow = null;
	var sHtml = "";
	var sData = "";
	var idx = "";
	if(oDiv != null && oDiv != undefined)
	{
		for(var i=0; i<this.matrix.length; i++)
		{
			arrow = this.matrix[i];
			if(arrow != null )	
			{
				sData = "";
				sHtml += "<input type=\"hidden\" name=\"BAH_"+arrow[0]+"\" value=\"";
				for(var j=0; j<arrow.length; j++)
					sData += arrow[j]+",";
				sData = sData.substring(0,sData.length-1);
				sHtml += sData + "\" />";
			}
		}
		oDiv.innerHTML = sHtml;
	}
}

function LVD_prvGetIndex(str)
{
	if(str != null && str.length > 3)
		return str.substring(3);
	else
		return -1;
}