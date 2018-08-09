package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;

public class HTMLTableFisa extends HTMLElement
{
    private final static String LVF_GRP_SEP = "##";
    private final static String LVF_ROW_SEP = "^?";
    private final static String LVF_COL_SEP = "^^";
    private String tableName = "";
    private String tableTit = "";
    private String lang = "";
    private int tableId = 0;
    private int cols = 0;
    private int width = 0;
    private int height = 0;
    private int[] typeAlign = null;
    private int[] columnsSize = null;
    private String[] header = null;
    private HTMLSimpleGroup[] tableData = null;

    public HTMLTableFisa(String tName, int tId, int width, int height,
        String[] header, HTMLSimpleGroup[] data, int[] colSize, String language)
    {
        this.tableName = tName;
        this.tableId = tId;
        this.width = width;
        this.height = height;
        this.header = header;
        this.tableData = data;
        this.columnsSize = colSize;
        this.lang = language;

        this.cols = this.header.length;
    }

    public void setTableTitle(String title)
    {
        this.tableTit = title;
    }

    public void setTypeAlignCols(int[] newAlign) throws Exception
    {
        if (newAlign.length != this.typeAlign.length)
        {
            throw new Exception("arrays size not equals");
        }

        this.typeAlign = newAlign;
    }

    public void setTypeAlignCol(int idx, int type) throws Exception
    {
        this.typeAlign[idx] = type;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int[] getColumnsSize()
    {
        return columnsSize;
    }

    public int getTableId()
    {
        return tableId;
    }

    public void setTableId(int tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public int getCols()
    {
        return cols;
    }

    public int getLength()
    {
        return 0;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        StringBuffer sb = new StringBuffer();
        LangService lang_serv = LangMgr.getInstance().getLangService(lang);
        
        // DIV principale
        sb.append(
            "\n<DIV id=\"LWFTitle\" align=\"left\" style=\"position:relative;height:" +
            this.height + "px;width:");
        sb.append(this.width +
            "px;background-color:#eaeaea; -moz-user-select: none; display:inline\" tabIndex=\"-1\"");
        sb.append(
            "hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

        // DIV contenitore per titolo tabella
        sb.append("\n<DIV class=\"tdTitleTable\" style=\"height:18px;width:" +
            this.width + "px;background-color:#eaeaea;overflow:hidden;\"");
        sb.append(
            " align=\"left\" tabIndex=\"-1\" hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

        // DIV per titolo tabella
        sb.append("\n<DIV style=\"float:left\">" + this.tableTit + "</DIV>");

        // DIV per pulsante di collapse all
        sb.append(
            "\n<DIV style=\"float:right;background-image:url(images/lsw/openGroup.gif);background-repeat:no-repeat;width:20px;cursor:pointer;\" title='"+lang_serv.getString("htmlfisa","closeall")+"' onclick=\"LVF_globalAction(this);\">&nbsp;</DIV></DIV>");

        // DIV List
        sb.append(
            "\n<DIV id=\"LWFlist\" align=\"left\" style=\"POSITION:RELATIVE;HEIGHT:" +
            this.height + "px;WIDTH:" + this.width +
            "px;BACKGROUND-COLOR:#eaeaea; -moz-user-select:none;display:inline\" tabIndex=\"-1\" hideFocus=\"true\" oncontextmenu=\"return false\" onselectstart=\"return false\">");

        // DIV Border
        sb.append(
            "<DIV id=\"LWFborder\" align=\"left\" STYLE=\"POSITION:RELATIVE;TOP:0;LEFT:0;HEIGHT:" +
            this.height + "px;WIDTH:" + this.width +
            "px; BACKGROUND-COLOR:#cacaca\">");

        // DIV Container
        sb.append(
            "<DIV id=\"LWFContainer\" align=\"left\" STYLE=\"border:2px solid;border-color:#cacaca;POSITION:RELATIVE;TOP:1;LEFT:1;HEIGHT:" +
            (this.height - 2) + "px;WIDTH:" + (this.width - 2) +
            "px;BACKGROUND-COLOR:#cacaca\"> ");

        // DIV Header
        sb.append("\n<DIV id=\"LWFCtTitle" + this.tableId +
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
        sb.append("\n<DIV id=\"LWFCtDataName" + this.tableId +
            "\" onscroll=\"LV_HScroll(" + this.tableId +
            ",'F');\" align=\"left\" STYLE=\"VISIBILITY:VISIBLE;overflow-X:auto;overflow-Y:auto;background-color:#cacaca;height:" +
            (this.height) + "px;width:" + (this.width - 2) +
            "px\"></DIV></DIV></DIV></DIV></DIV>\n\n");

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
        sb.append("var oFisa = new ListViewFisa('LWFCtDataName" + this.tableId +
            "'," + this.tableId + "," + this.cols + ",aSize,strData);\n");
        sb.append("oFisa.render();\n");
        sb.append("</script>\n");

        return sb.toString();
    }

    private String createStringData()
    {
        StringBuffer sb = new StringBuffer();
        String tmpRow = "";
        HTMLSimpleGroup hsg = null;
        HTMLSimpleElement[][] se = null;

        if (this.tableData != null)
        {
            for (int i = 0; i < this.tableData.length; i++)
            {
                tmpRow = "";
                hsg = this.tableData[i];
                
                if(hsg != null)
                {
	                se = hsg.getRowsGroup();
	
	                for (int j = 0; j < se.length; j++)
	                {
	                    for (int z = 0; z < se[j].length; z++)
	                    {
	                        if (z != (se[j].length - 1))
	                        {
	                            tmpRow += (se[j][z].getHTMLText() + LVF_COL_SEP);
	                        }
	                        else
	                        {
	                            tmpRow += se[j][z].getHTMLText();
	                        }
	                    }
	
	                    if (j != (se.length - 1))
	                    {
	                        tmpRow += LVF_ROW_SEP;
	                    }
	                }

	                sb.append(hsg.getLabelGroup() + LVF_ROW_SEP + tmpRow);
	
	                if (i != (this.tableData.length - 1))
	                {
	                    sb.append(LVF_GRP_SEP);
	                }
                }
            }
        }

        return sb.toString();
    }
}
