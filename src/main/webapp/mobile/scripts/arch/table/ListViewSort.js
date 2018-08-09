// Regular expressions for normalizing white space.
var whtSpEnds = new RegExp("^\\s*|\\s*$", "g");
var whtSpMult = new RegExp("\\s\\s+", "g");

// This code is necessary for browsers that don't reflect the DOM
// constants (like IE).
if (document.ELEMENT_NODE == null) {
  document.ELEMENT_NODE = 1;
  document.TEXT_NODE = 3;
}

var m_i = 1
var m_idTime = null;

function checkClick(i,table,tableData,tableBody,col,nameTable)
{
  m_i = i;
  m_idTime = setTimeout("sortTable('" + table + "', '" + tableData + "','" + tableBody + "','" + col + "','"+nameTable+"')", 200);
}

function checkClickRefresh(i,table,tableData,tableBody,col,nameTable,type)
{
	m_i = i; 
	sortTable(table,tableData,tableBody,col,nameTable,type);
}

function sortTable(table,tableData,tableBody,col,nameTable,type) 
{
	clearTimeout(m_idTime);
	if (m_i == 2)
		return;
	
	var oLsw = null;
	if(oLswContainer != null)
		oLsw = oLswContainer.getLsw(nameTable);
	
	if(oLsw != null)
		oLsw.idxColOrder = col;
	
	var vOrder = changeOrderImage(table,col,type);
	
	//mi prendo la tabella e dove ho sostituito i valori originali con le colonne vuote ripristino i valori per poterla ordinare correttamente
	var tblEl = document.getElementById(tableBody);
	
	if(document.getElementById(tableBody) != null && document.getElementById(tableBody).rows[0]!=null)
	{
		var columns=document.getElementById(tableBody).rows[0].cells.length;
		var rows=document.getElementById(tableBody).rows.length;
		
		// Get the table section to sort.
		// Set up an array of reverse sort flags, if not done already.
		if (tblEl.reverseSort == null)
	    	tblEl.reverseSort = new Array();
	
		// If this column was the last one sorted, reverse its sort direction.
		if (col == tblEl.lastColumn)
	    	tblEl.reverseSort[col] = !tblEl.reverseSort[col];
	
	  	// Remember this column as the last one sorted.
	  	tblEl.lastColumn = col;
	
	  	var oldDsply = tblEl.style.display;
	  	tblEl.style.display = "none";
	 
		var tmpEl;
		var i, j;
		var minVal, minIdx;
		var testVal;
		var cmp;
		var bType = detectBrowserType();
		var priCol = -1;
		if(bType == 0)
			var arDataEx = buildDataArray(tblEl, col, nameTable);
		else {
			var priority_col = document.getElementById(nameTable + "_priority_col");
			priCol = priority_col ? parseInt(priority_col.value,10) : -1;
		}
		
		for (i = 0; i < tblEl.rows.length - 1; i++) 
		{
			minIdx = i;
	    	
	    	if(bType > 0) {
	    		if( col != priCol )
	    			minVal = getTextValue(tblEl.rows[i].cells[col]);
	    		else
	    			minVal = document.getElementById("pr_" + getTextValue(tblEl.rows[i].cells[col])).value;
	    	}
	    	else
	    		minVal = arDataEx[i];
		    
	    	for (j = i; j < tblEl.rows.length; j++) 
	    	{
	    		if(bType > 0) {
	    			if( col != priCol )
	    				testVal = getTextValue(tblEl.rows[j].cells[col]);
	    			else
	    				testVal = document.getElementById("pr_" + getTextValue(tblEl.rows[j].cells[col])).value;
	    		}
	    		else
	    			testVal = arDataEx[j];
	    			
	    		cmp = compareValues(minVal, testVal);
	        	        	
		
	        	// Reverse order
	      		/*if (tblEl.reverseSort[col])
	      		{
	        		cmp = -cmp;
	        	}*/
	        	
	        	cmp = (cmp * vOrder);
				
	      		if (cmp > 0) 
	      		{
	        		minIdx = j;
	        		minVal = testVal;
	      		}
	    	}
	    	
	    	if (minIdx > i) 
	    	{
	    		
	    		if(bType > 0)
	    		{	
	    			tmpEl = tblEl.removeChild(tblEl.rows[minIdx]);
	      			tblEl.insertBefore(tmpEl, tblEl.rows[i]);
	      		}
	      		else
	      		{
	      			tblEl.rows[i].swapNode(tblEl.rows[minIdx]);
	      			tmpAr = arDataEx[minIdx];
	      			arDataEx[minIdx] = arDataEx[i];
	      			arDataEx[i] = tmpAr;
	      		}
	      		
	      		// Aggiorno matrice in memoria
	      		tmpMRow = oLsw.mData[minIdx];
	      		oLsw.mData[minIdx] = oLsw.mData[i];
	      		oLsw.mData[i] = tmpMRow;
	    	}
		}
		
		for (i = 0; i < tblEl.rows.length; i++) 
		{
			if(i%2==0){
				tblEl.rows[i].className ='Row2';
			}else{
				tblEl.rows[i].className ='Row1';
			}
			
		}
			
		tblEl.style.display = oldDsply;
	}
	return false;
}

function buildDataArray(objTb, objCol, nameTable)
{
	var arData = new Array();
	var priority_col = document.getElementById(nameTable + "_priority_col");
	var priCol = priority_col ? parseInt(priority_col.value,10) : -1;
	
	if( objCol != priCol ) {
		for(i=0; i<objTb.rows.length; i++)
			arData[i] = getTextValue(objTb.rows[i].cells[objCol]);
	}
	else {
		for(i=0; i<objTb.rows.length; i++) {
			var priority_name = getTextValue(objTb.rows[i].cells[objCol]); 
			arData[i] = document.getElementById("pr_" + priority_name).value;
		}
	}
	return arData;
}

function getTextValue(el) 
{
	var i;
  	var s;
	s="";
	for (i = 0; i < el.childNodes.length; i++)
  	{
    	if (el.childNodes[i].nodeType == document.TEXT_NODE)
      		s += el.childNodes[i].nodeValue;
		else if (el.childNodes[i].nodeType == document.ELEMENT_NODE && el.childNodes[i].tagName == "BR")
			s += " ";
		else
			s += getTextValue(el.childNodes[i]); 
	}
	return normalizeString(s);
}

function normalizeString(s) 
{
  s = s.replace(whtSpMult, " ");  // Collapse any multiple whites space.
  s = s.replace(whtSpEnds, "");   // Remove leading or trailing white space.
  return s;
}

function compareValues(v1, v2) {

  var f1, f2;

  // If the values are numeric, convert them to floats.

  f1 = parseFloat(v1);
  f2 = parseFloat(v2);
  if (!isNaN(f1) && !isNaN(f2)) {
    v1 = f1;
    v2 = f2;
  }

  // Compare the two values.
  if (v1 == v2)
    return 0;
  if (v1 > v2)
    return 1
  return -1;
}

function changeOrderImage(table,col,type)
{
	var oTable = document.getElementById(table);
	var element = oTable.rows[0].cells;
	var path = String(element[col].getAttribute("background"));
	var vOrder = 1;
	
	for (i = 0; i < element.length; i++)
	{
		if (i != col)
			element[i].setAttribute("background", "");
	}

	if ((path == "") || (path == "images/lsw/down.gif"))
	{
		if(type != 1)
			element[col].setAttribute("background", "images/lsw/up.gif");
		vOrder = 1;
	}
	else
	{
		if(type != 1)
			element[col].setAttribute("background", "images/lsw/down.gif");
		vOrder = -1;
	}
	
	if(type == 1)
		vOrder *= -1;
		 
	return vOrder;
}

function detectBrowserType()
{
	if(document.all)
  		return 0;
  	else
  		return 1;
}