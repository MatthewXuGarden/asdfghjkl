package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

import java.util.ArrayList;
import java.util.List;

public class HTMLSimpleGroup
{
    private String idGroup = "";
    private String labelGroup = "";
    private List rowslist = null;
    private int colNum = 0;
    private HTMLSimpleElement[][] rows = null;
    
    public HTMLSimpleGroup(String id, String label, int colNum)
    {
        this.idGroup = id;
        this.labelGroup = label;
        this.rowslist = new ArrayList();
        this.colNum = colNum;
        this.rows = null;
    }
    
    public HTMLSimpleGroup(String id, String label, HTMLSimpleElement[][] rows)
    {
        this.idGroup = id;
        this.labelGroup = label;
        this.rows = rows;
    }
    
    public int getColNumber() {
    	return this.colNum;
    }
    
    public void addRow(HTMLSimpleElement[] row) {
    	this.rowslist.add(row);
    }
    
    public void setRows(HTMLSimpleElement[][] rows) {
    	this.rows = rows;
    }
    
    public String getIdGrop()
    {
        return this.idGroup;
    }

    public String getLabelGroup()
    {
        return this.labelGroup;
    }

    public HTMLSimpleElement[][] getRowsGroup()
    {
    	HTMLSimpleElement[][] ret = new HTMLSimpleElement[this.rowslist.size()][this.colNum];
    	
    	for(int i=0; i<ret.length; i++)
    		ret[i]  = (HTMLSimpleElement[])this.rowslist.get(i);

    	return ret;
    }

    public int getNumRowsGroup()
    {
        return this.rows.length;
    }
}
