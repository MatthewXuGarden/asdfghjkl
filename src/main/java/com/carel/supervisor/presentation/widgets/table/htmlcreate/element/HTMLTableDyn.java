package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

public class HTMLTableDyn extends HTMLElement
{
    private final static String LVD_ROW_SEP = "^?";
    private final static String LVD_COL_SEP = "^^";
    public final static int LEFT = 0;
    public final static int CENTER = 1;
    public final static int RIGHT = 2;
    public final static int SCROLLWIDTH = 20 ; //default scrollbar width in pixel    
    private String tableName = "";
    private String tableTitle = "";
    private int tableId = 0;
    private int cols = 0;
    private int width = 0;
    private int height = 0;

    //  Risoluzione in base allo schermo
    private int screenW = 1024;
    private int screenH = 768;
    
    // Align type colum
    private int[] typeAlign = null;

    // Size colum
    private int[] columnsSize = null;

    // Label Header
    private String[] header = null;

    // Data
    private HTMLElement[][] data = null;

    // IdRows
    private String[] rowsId = null;
    private String[] idClientFld = null;

    public HTMLTableDyn(String tName, int tId, int cols, int width, int height,
        String[] header, HTMLElement[][] data, String[] fildsName)
    {
        this.tableName = tName;
        this.tableId = tId;
        this.tableTitle = "";
        this.cols = cols;
        this.width = width;
        this.height = height;

        this.header = header;
        this.data = data;
        this.rowsId = initializeIdRows(data.length);
        this.columnsSize = initializeColSize(this.cols);
        this.typeAlign = initializeAlign(this.cols);
        this.idClientFld = fildsName;
    }

    public void recWidthBaseOnScreenW(int w) {
        this.screenW = w;
        setWidth(width);
    }
    
    public void recHeightBaseOnScreenH(int h) {
        this.screenH = h;
        setHeight(height);
    }
    
    
    public void setHeight(int height)
    {
        this.height = (int)Math.round(((double)(height*this.screenH)/900));
    }

    
    public void setWidth(int value)
    {
        this.width = (int)Math.round(((double)(value*(this.screenW))/900));
    }
    
    
    private int recalWidthColum(int value)
    {
    	// return (int)Math.round(((double)(value*(this.screenW))/(900)));
        float fattore = 6;
        if(this.screenW != 1024)
        {
            float a = ((float)this.screenW) / ((float)1024);
            fattore = ((fattore) * ((float)this.screenW))/1024;
            fattore *= a;
            fattore = Math.round(fattore);
            fattore++;
        }
        return (int)Math.round(((double)(value*(this.screenW-44-SCROLLWIDTH))/(900)))+(int)fattore;    	
    }
    
    public void setClientFilds(String[] idFld) throws Exception
    {
        if (idFld.length != this.idClientFld.length)
        {
            throw new Exception("Length mismatch");
        }

        for (int i = 0; i < this.idClientFld.length; i++)
        {
            this.idClientFld[i] = idFld[i];
        }
    }

    public void setTableTitle(String title)
    {
        this.tableTitle = title;
    }

    public void setAlign(int[] align) throws Exception
    {
        if (align.length != this.typeAlign.length)
        {
            throw new Exception("Length mismatch");
        }

        for (int i = 0; i < this.typeAlign.length; i++)
        {
            this.typeAlign[i] = align[i];
        }
    }

    public void setColsWidth(int[] width) throws Exception
    {
        if (width.length != this.columnsSize.length)
        {
            throw new Exception("Length mismatch");
        }

        for (int i = 0; i < this.columnsSize.length; i++)
        {
        	/*Vecchio calcolo della largezza celle
            this.columnsSize[i] = width[i];
            */
        	this.columnsSize[i] = recalWidthColum(width[i]);
        }
    }

    private int[] initializeAlign(int cols)
    {
        this.typeAlign = new int[cols];

        for (int i = 0; i < this.typeAlign.length; i++)
        {
            this.typeAlign[i] = LEFT;
        }

        return this.typeAlign;
    }

    private int[] initializeColSize(int cols)
    {
        this.columnsSize = new int[cols];

        int dim = Math.round((this.width / cols));

        for (int i = 0; i < this.columnsSize.length; i++)
        {
            this.columnsSize[i] = (int) dim;
        }

        return this.columnsSize;
    }

    private String[] initializeIdRows(int rows)
    {
        this.rowsId = new String[rows];

        for (int i = 0; i < this.rowsId.length; i++)
        {
            this.rowsId[i] = String.valueOf(i + 1);
        }

        return this.rowsId;
    }

    public void setUniqueId(String[] unique) throws Exception
    {
        if (unique.length != this.rowsId.length)
        {
            throw new Exception("Length mismatch");
        }

        this.rowsId = unique;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        StringBuffer sb = new StringBuffer();

        // DIV principale
        sb.append(
            "\n<DIV id=\"LWDTitle\" align=\"left\" style=\"position:relative;height:" +
            this.height + "px;width:");
        sb.append(this.width +
            "px;background-color:#eaeaea; -moz-user-select: none; display:inline\" tabIndex=\"-1\"");
        sb.append(
            "hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

        if (this.tableTitle.trim().length() > 0)
        {
            // DIV contenitore per titolo tabella
            sb.append(
                "\n<DIV class=\"tdTitleTable\" style=\"height:18px;width:" +
                this.width + "px;background-color:#eaeaea;overflow:hidden;\"");
            sb.append(
                " align=\"left\" tabIndex=\"-1\" hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

            // DIV per titolo tabella
            sb.append("\n<DIV style=\"float:left\">" + this.tableTitle +
                "</DIV></DIV>");
        }

        // DIV List
        sb.append(
            "\n<DIV id=\"LWDlist\" align=\"left\" style=\"POSITION:RELATIVE;HEIGHT:" +
            this.height + "px;WIDTH:" + this.width +
            "px;BACKGROUND-COLOR:#eaeaea; -moz-user-select:none;display:inline\" tabIndex=\"-1\" hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

        // DIV Border
        sb.append(
            "<DIV id=\"LWDborder\" align=\"left\" STYLE=\"POSITION:RELATIVE;TOP:0;LEFT:0;HEIGHT:" +
            this.height + "px;WIDTH:" + this.width +
            "px; BACKGROUND-COLOR:#cacaca\">");

        // DIV Container
        sb.append(
            "<DIV id=\"LWDContainer\" align=\"left\" STYLE=\"border:2px solid;border-color:#cacaca;POSITION:RELATIVE;TOP:1;LEFT:1;HEIGHT:" +
            (this.height - 2) + "px;WIDTH:" + (this.width - 2) +
            "px;BACKGROUND-COLOR:#cacaca\"> ");

        // DIV Header
        sb.append("\n<DIV id=\"LWDCtTitle" + this.tableId +
            "\" align=\"left\" STYLE=\"POSITION:RELATIVE;WIDTH:" +
            (this.width - 17) +
            "px;overflow:hidden;BACKGROUND-COLOR:#cacaca;\"> ");

        // Table Header
        sb.append("\n<table id=\"" + this.tableName +
            "\" class=\"table\" onMouseUp=\"document.onmousemove=null;return false\" cellspacing=\"1px\"><tr>");

        for (int i = 0; i < this.cols; i++)
        {
            sb.append("\n<th id=\"" + this.tableName + "Th0" + i +
                "\" class=\"th\" title=\"" + this.header[i] + "\">");
            sb.append("<DIV id=\"" + this.tableName + "DivTh0" + i +
                "\" style=\"width:" + this.columnsSize[i] +
                "px;height:18px;text-overflow:ellipsis;overflow:hidden;\">" +
                this.header[i] + "</DIV></th>");
        }

        sb.append("\n</tr></table></DIV>");

        // DIV Table Data Container
        sb.append("\n<DIV id=\"LWDCtDataName" + this.tableId +
            "\" onscroll=\"LV_HScroll(" + this.tableId +
            ",'D');\" align=\"left\" STYLE=\"VISIBILITY:VISIBLE;overflow-X:auto;overflow-Y:auto;background-color:#cacaca;height:" +
            (this.height - 26) + "px;width:" + (this.width - 2) +
            "px\"></DIV></DIV></DIV></DIV></DIV></DIV>\n\n");

        sb.append(createScriptData());

        return sb;
    }

    private String createScriptData()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<script>\n");

        sb.append("var strData = \"" + createStringData() + "\";\n");

        String colSize = "";

        for (int i = 0; i < this.cols; i++)
        {
            colSize += ((this.columnsSize[i]) + ",");
        }

        colSize = colSize.substring(0, colSize.length() - 1);
        sb.append("var aSize = new Array(" + colSize + ");\n");

        String clFields = "";

        for (int i = 0; i < this.idClientFld.length; i++)
        {
            clFields += (("\"" + this.idClientFld[i] + "\"") + ",");
        }

        clFields = clFields.substring(0, clFields.length() - 1);
        sb.append("var clFields = new Array(" + clFields + ");\n");

        sb.append("var oDyn = new ListViewDyn('LWDCtDataName" + this.tableId +
            "'," + this.tableId + "," + this.cols +
            ",aSize,strData,clFields);\n");
        sb.append("oDyn.render();\n");
        sb.append("</script>\n");

        return sb.toString();
    }

    private String createStringData()
    {
        StringBuffer sb = new StringBuffer();
        HTMLElement[] hElem = null;

        if (this.data != null)
        {
            for (int i = 0; i < this.data.length; i++)
            {
                hElem = this.data[i];

                if (hElem != null)
                {
                    sb.append(this.rowsId[i] + LVD_COL_SEP);

                    for (int j = 0; j < this.data[i].length; j++)
                    {
                        if (j != (this.data[i].length - 1))
                        {
                            sb.append(this.data[i][j].getHTMLText() +
                                LVD_COL_SEP);
                        }
                        else
                        {
                            sb.append(this.data[i][j].getHTMLText() +
                                LVD_COL_SEP + "1");
                        }
                    }
                }

                if (i != (this.data.length - 1))
                {
                    sb.append(LVD_ROW_SEP);
                }
            }
        }

        return sb.toString();
    }

    public int getLength()
    {
        return 0;
    }
}
