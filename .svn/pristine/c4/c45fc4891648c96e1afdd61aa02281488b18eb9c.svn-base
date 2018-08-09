var LVF_GRP_SEP = "##";
var LVF_ROW_SEP = "^?";
var LVF_COL_SEP = "^^";
var LVF_SCOST = 33;

function LVF_click(obj)
{
	oFisa.changeStateRow(obj.id);
	oFisa.render();
}

function LVF_globalAction(obj)
{
	oFisa.collapseAll();
	if(oFisa.generaState)
	{
		obj.style.backgroundImage = "url(images/lsw/openGroup.gif)";
		obj.title= document.getElementById("closeall").value;		
	}	
	else
	{
		obj.style.backgroundImage = "url(images/lsw/closeGroup.gif)";
		obj.title= document.getElementById("openall").value;		
	}
	oFisa.render();
	
}

function ListViewFisa(nameT,idT,cols,aSize,strData)
{
	this.idTableName = nameT;
	this.idTableNum  = idT;
	this.numCols     = cols;
	this.aSizeCols   = aSize;
	this.counter     = 0;
	this.arGroups    = new Array();
	this.generaState = true;
	this.colSum      = 0;
	for(var i=0; i<aSize.length; i++)
		this.colSum = (this.colSum + aSize[i]);
	
	this.init = LVF_init;
	this.render = LVF_render;
	this.changeStateRow = LVF_changeStateRow;
	this.collapseAll = LVF_collapseAll;
	
	this.init(strData);
}

function LVF_init(strData)
{
	var arGrpList = strData.split(LVF_GRP_SEP);
	var arRows = null;
	var oGroup = null;
	var grpName = "";
	
	for(var i=0; i<arGrpList.length; i++)
	{
		arRows = arGrpList[i].split(LVF_ROW_SEP);
		this.arGroups.push(new ListViewFisaGroup(arRows,this.counter));
		this.counter++;
	}
}

function LVF_render()
{
	var oIdTbBody = document.getElementById(this.idTableName);
	var oGrpToRender = null;
	var addScost = true;
	
	var sHtml = "";
	sHtml += "<table id=\"TableData"+this.idTableNum+"\" class=\"table\" onMouseUp=\"document.onmousemove=null;return false\" cellspacing=\"1px\">\n";
	sHtml += "<tbody id=\"TDBdy"+this.idTableNum+"\">";
	
	// Control FOR
	for(k=0; k<this.arGroups.length; k++)
	{
		if(this.arGroups[k].display)
		{
			addScost = false;
			break;
		}
	}
	
	// Add scont in event of no rows display in group
	var dimTrGroup = this.colSum;
	if(addScost)
		dimTrGroup = (this.colSum + LVF_SCOST);
		
	for (k=0; k<this.arGroups.length; k++)
	{
		oGrpToRender = this.arGroups[k];
		
		var buf = "";		
		buf +="<tr id=\""+this.idTableNum+"TDtr"+(k+1)+"\" class=\"GroupRow\">\n";
		buf +="<td align=\"center\" class=\"tdFisa\" colspan=\""+this.numCols+"\"><div id=\"Grp"+k+"\" onclick=\"LVF_click(this);\" class=\"LV_DIV\" ";
		if(oGrpToRender.display)
			buf += " title='"+document.getElementById("close").value+"' style=\"width:"+(dimTrGroup)+"px;background-image:url(images/lsw/openGroup.gif);background-repeat:no-repeat;\">"+oGrpToRender.name+"</div></td>";
		else
			buf += " title='"+document.getElementById("open").value+"' style=\"width:"+(dimTrGroup)+"px;background-image:url(images/lsw/closeGroup.gif);background-repeat:no-repeat;\">"+oGrpToRender.name+"</div></td>";
		buf +="</tr>\n";
		
		if(oGrpToRender.display)
		{
			for(var i=0; i<oGrpToRender.rows; i++)
			{
				var arCols = oGrpToRender.arCols[i];
				
				buf += "<tr class=\"Row1\">";
				for(var j=0; j<arCols.length; j++)
					buf += "<td class=\"tdFisa\"><div class=\"LV_DIV\" style=\"width:"+this.aSizeCols[j]+"px\" onselectstart=\"return false;\" nowrap>"+arCols[j]+"</div></td>"
				
				buf += "</tr>\n";
			}
		}
				
		sHtml += buf;
	}
	sHtml += "</tbody>"
	sHtml += "</table>";
	oIdTbBody.innerHTML = sHtml;
}

function LVF_changeStateRow(idGrp)
{
	for(var i=0; i<this.arGroups.length; i++)
	{
		if(this.arGroups[i].id == idGrp)
		{
			if(this.arGroups[i].display)
				this.arGroups[i].display = false;
			else
				this.arGroups[i].display = true;
		
			break;
		}
	}
}

function LVF_collapseAll()
{
	for(var i=0; i<this.arGroups.length; i++)
		this.arGroups[i].display = !this.generaState;
	this.generaState = !this.generaState;
}

function ListViewFisaGroup(arData,idx)
{
	// Default
	this.id = "Grp"+idx;
	this.name = "";
	this.rows = 0;
	this.arCols = new Array();
	this.display = false;
	
	// Init	
	if(arData != null && arData.length > 0)
	{
		this.name = arData[0];
		if(arData.length > 1)
		{
			var data = arData.slice(1);
			var tmpRows = null;
			this.rows = data.length;
			for(var i=0; i<data.length; i++)
			{
				tmpCol = data[i].split(LVF_COL_SEP);
				this.arCols.push(tmpCol);
			}
		}
		this.display = true;
	}
}