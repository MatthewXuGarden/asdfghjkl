var oLswContainer = new LswContainer();
var R_lock = 0;

// Resize Var
m_test  = 0;
m_div = null;
m_divLength = 0;
m_scost = 0;
m_object = new Array();
m_scostobj = new Array();
m_iTable = 0;
m_nTable = "";

function LswContainer()
{
	this.container 	 = new Array();
	this.addLsw  	 = R_addLsw;
	this.getLsw  	 = R_getLsw;
	this.getSize 	 = R_getSize;
	this.getLswByIdx = R_getLswByIdx;
}

function R_addLsw(oLsw)
{
	this.container.push(oLsw);
}

function R_getLsw(sName)
{
	var oLsw = null;
	var bFound = false;
	for(var i=0; i<this.container.length; i++)
	{
		oLsw = this.container[i];
		if(oLsw.idTableName == sName)	
		{
			bFound = true;
			break;	
		}
	}
	if(bFound)
		return oLsw;
	else
		return null;
}

function R_getSize()
{
	return this.container.length;
}

function R_getLswByIdx(idx)
{
	return this.container[idx];
}

function R_rowSelect(objRow,tableName)
{
	if(oLswContainer != null)
	{
		var oLsw = oLswContainer.getLsw(tableName);
		oLsw.selectRow(objRow);
	}
}

function R_showHideColumn(div, divLength, dim, iTable, nTable)
{
	if(oLswContainer != null){
		var oLsw = oLswContainer.getLsw(nTable);
		oLsw.showHide(div, divLength, dim, iTable);
	}
}

function R_resize(evt, index, div, divLength, iTable,nTable)
{
	var oLsw = null;
	
	if(nTable == null || nTable == "")
		nTable = m_nTable;
	else
		m_nTable = nTable;
		
	if(oLswContainer != null)
		oLsw = oLswContainer.getLsw(nTable);
	
	if (index == 1)
	{
		var oDiv00 = document.getElementById(iTable + 'div' + div);
		m_div = div;
		m_divLength = divLength;
		m_iTable = iTable;
		var k = oDiv00.style.width;
		var m = k.replace("px", "");
		m_scost = Number(m) - Number(evt.clientX);
		
		var oI = document.getElementById(iTable + 'd' + div);
		var i = 0;
		m_object = new Array();
		while (oI != null)
		{
			m_object[i] = oI;
			var q = m_object[i].style.left;
			var a = q.replace("px", "");
			m_scostobj[i] = Number(a) - Number(evt.clientX);
			i++;
			oI = document.getElementById(iTable + 'd' + (div + i));
		}
		
		
	}
	
	if (m_div == null)
		return;
	
	if (m_test == 1 && index == 2)
	{
		var oDiv00 = document.getElementById(m_iTable + 'div' + m_div);
		var oDivtd00 = null;
			
		if (parseInt(oDiv00.style.width) > 10  || (parseInt(oDiv00.style.width) < (m_scost + Number(evt.clientX))))
		{
			
			var nw = m_scost + Number(evt.clientX);
			if(nw < 0)
				nw = 15;
			oDiv00.style.width =  nw;
			
			var iK = 0;
			for (iK = 0; iK < m_object.length; iK++)
				m_object[iK].style.left = m_scostobj[iK] + Number(evt.clientX);
		}
	}
	else
	{
		m_test = index;
		if (index == 0)
		{
			var oDiv00 = document.getElementById(m_iTable + 'div' + m_div);
			if(m_divLength==0){
				//var trs = document.getElementById(nTable).getElementsByTagName('tr').length;
				m_divLength = oLsw.numRows;    // if the row's data load from js , divLength should be change.
			}
			for (i=0; i<m_divLength; i++)
			{
				oDivtd00 = document.getElementById(m_iTable + 'divtd' + i + m_div);
				if(oDivtd00 != null)
					oDivtd00.style.width = oDiv00.style.width;
			}
			
			if(oLsw != null)
				oLsw.saveCurrentDim(oDiv00.style.width,m_div);
						
		}
	}
}

function forceReloadPoller() {
	if(top.frames['body'] && top.frames['body'].frames['Poller']) {
		try {
			top.frames['body'].frames['Poller'].location.reload();
		}
		catch(e) {
		}
	}
	setTimeout("R_setLockStatus()",20000);
}

function R_setLockStatus() {
	R_lock = 1;
}