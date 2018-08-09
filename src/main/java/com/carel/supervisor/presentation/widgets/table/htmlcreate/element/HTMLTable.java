package com.carel.supervisor.presentation.widgets.table.htmlcreate.element;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
//import com.carel.supervisor.base.util.UtilityString;


public class HTMLTable extends HTMLElement
{
    public static final int TSTRING = 0;
    public static final int TNUMBER = 1;
    public static final int TDATA = 2;
    private static final int SIZE = 7;
    private static final int SIZE_BOLD = 6;
    public final static int LEFT = 0;
    public final static int CENTER = 1;
    public final static int RIGHT = 2;
    public final static int SCROLLWIDTH = 20 ; //default scrollbar width in pixel
    public final static boolean doHammer = true;
    private String tableTitle = "";
    private String tableName = "";
    private String[] tableHeader = null;
    private String[] dblClickRowFunction = null;
    private String[] snglClickRowFunction = null;
    private String[] rowsClasses = null;
    private int[] typeAlign = null;
    private int[] columnsSize = null;
    private int[] typeColum = null;
    private HTMLElement[][] tableData = null;
    private boolean doOrder = true;
    private boolean doResiz = true;
    private boolean isPage = false;
    private int pageNumber = 1;
    private int pageTotal = 1;
    private int rows = 0;
    private int columns = 0;
    private int width = 0;
    private int height = 0;
    private int tableId = 0;

    // Azioni da applicare sul DBCLICK e SIGNLECLICK della riga
    private String dbClickRowAction = "";
    private String sgClickRowAction = "";

    // Gestione scrollbar verticale
    // private boolean scroll = true;

    // Row Height Default
    private int rowHeight = 16;

    // Row Select Color
    private boolean rowSelectColor = true;
    
    // Risoluzione in base allo schermo
    private int screenW = 1024;
    private int screenH = 768;
    
    
    public HTMLTable(String tableName, String[] tableTitle, HTMLElement[][] tableData,
        boolean defaultOrder, boolean defaultResize)
    {
        this.tableTitle = "";
        this.tableName = tableName;
        this.tableHeader = tableTitle;
        this.tableData = tableData;
        this.rows = (tableData != null) ? tableData.length : 0;
        this.columns = tableTitle.length;
        this.doOrder = defaultOrder;
        this.doResiz = defaultResize;
        this.columnsSize = new int[this.columns];
        this.dblClickRowFunction = new String[this.rows==0?1:this.rows];
        this.snglClickRowFunction = new String[this.rows==0?1:this.rows];
        this.rowsClasses = new String[this.rows==0?1:this.rows];
        this.typeAlign = initializeAlign(this.columns);
        this.typeColum = initializeType(this.columns);
        this.tableId = 0;
    }

    public HTMLTable(String tableName, String[] tableTitle, HTMLElement[][] tableData)
    {
        this(tableName, tableTitle, tableData, false, false);
    }
    
    public void setScreenW(int w) {
        this.screenW = w;
    }
    
    public void setScreenH(int h) {
        this.screenH = h;
    }
    public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTot) {
		this.pageTotal = pageTot;
	}
    public void setRowSelectColor(boolean state)
    {
        this.rowSelectColor = state;
    }

    public boolean getRowSelectColor()
    {
        return this.rowSelectColor;
    }

    public void setRowHeight(int sizeH)
    {
        this.rowHeight = sizeH;
    }

    public int getRowHeight()
    {
        return this.rowHeight;
    }

    public void setTableTitle(String tit)
    {
        this.tableTitle = tit;
    }

    public String getTableTitle()
    {
        return this.tableTitle;
    }

    public void setTableId(int i)
    {
        this.tableId = i;
    }

    public int getTableId()
    {
        return this.tableId;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = (int)Math.round(((double)(height*this.screenH)/768));
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = recalWidth(width);
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public void setColumnSize(int pos, int value)
    {
        
        columnsSize[pos] = recalWidthColum(value);
    }

    public String getDbClickRowAction()
    {
        return dbClickRowAction;
    }

    public void setDbClickRowAction(String dbClickRowAction)
    {
        this.dbClickRowAction = dbClickRowAction;
    }

    public String getSgClickRowAction()
    {
        return sgClickRowAction;
    }

    public void setSgClickRowAction(String sgClickRowAction)
    {
        this.sgClickRowAction = sgClickRowAction;
    }

    public StringBuffer getHTMLTextBuffer()
    {
        int size = 0;

        for (int j = 0; j < tableHeader.length; j++)
        {
            if (null != tableData)
            {
                for (int i = 0; i < tableData.length; i++)
                {
                    //size = tableData[i][j].getLength() * SIZE;
                    size = getDataColumSize(tableData[i][j], j);

                    if (columnsSize[j] < size)
                    {
                        columnsSize[j] = size;
                    }
                }
            }

            size = (tableHeader[j].length() + 4) * SIZE_BOLD;

            if (columnsSize[j] < size)
            {
                columnsSize[j] = size;
            }
        }
        
        StringBuffer html = new StringBuffer();
        html.append(NTcreateDivTbTitle(height, width));
        html.append(NTcreateDTableRows());

        return html;
    }

    public StringBuffer getHTMLTextBufferNoWidthCalc()
    {
        StringBuffer html = new StringBuffer();
        html.append(NTcreateDivTbTitle(height, width));
        html.append(NTcreateDTableRows());

        return html;
    }

    public StringBuffer getHTMLTextBufferRefresh()
    {
        StringBuffer html = new StringBuffer();
        html.append(NTcreateDTableRowsRefresh());
        return html;
    }

    private int getDataColumSize(HTMLElement elem, int idx)
    {
        int rsize = 0;

        try
        {
            switch (this.typeColum[idx])
            {
            case TSTRING:

                if (elem != null)
                {
                    rsize = elem.getLength() * SIZE;
                }

                break;

            case TNUMBER:

                if (elem != null)
                {
                    rsize = elem.getLength() * SIZE;
                }

                break;

            case TDATA:

                if (elem != null)
                {
                    rsize = 130;
                }

                break;
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return rsize;
    }

    private String NTcreateDivTbTitle(int h, int w)
    {
        StringBuffer sb = new StringBuffer();
        if (((this.tableTitle != null) && (this.tableTitle.length() > 0)) || this.isPage)
        	sb.append("<div id=\"LWTitle"+this.tableId+"\" class=\"LWTitle\" style=\"height:" + (h+23) +
        			"px; width:" + w + "px;");
        else
        	sb.append("<div id=\"LWTitle"+this.tableId+"\" class=\"LWTitle\" style=\"height:" + (h+5) +
        			"px; width:" + w + "px;");
        sb.append(
            "\" tabIndex=\"-1\" hideFocus=\"true\" ");
        sb.append(" oncontextmenu=\"return false\" onselectstart=\"return false\">\n");

        if (((this.tableTitle != null) && (this.tableTitle.length() > 0)) || this.isPage)
        {
            sb.append(
                "<div class=\"tdTitleTable\" style=\"height:18px;text-align:left\" tabIndex=\"-1\" hideFocus=\"true\" oncontextmenu=\"return false\"");
            sb.append("onselectstart=\"return false\">");
            sb.append("<div style=\"float:left;margin-left:4px;\">");
            sb.append(this.tableTitle);
            sb.append("</div>");

            sb.append("</div>");
        }

        sb.append("" + NTcreateDivList(h, w) + paginTable() + "</div>\n");

        return sb.toString();
    }

    private String NTcreateDivList(int h, int w)
    {
        String sb = "";
        sb = "<div id=\"LWlist"+this.tableId+"\" class=\"LWList\" style=\"height:" + h +
            "px; width:" + w + "px;" +
            "\" tabIndex=\"-1\" hideFocus=\"true\"" +
            " oncontextmenu=\"return false\" onselectstart=\"return false\">";

        return sb + "\n" + NTcreateDivBorder(h, w) + "</div>\n";
    }

    private String NTcreateDivBorder(int h, int w)
    {
        String sb = "";
        sb = "<div id=\"LWborder"+this.tableId+"\" class=\"LWBorder\" style=\"height:" +
            h + "px;" + "width:" + (w - 2) + "px;\">";

        return sb + "\n" + NTcreateDivContainer(h, w) + "</div>\n";
    }

    private String NTcreateDivContainer(int h, int w)
    {
        String sb = "";
        sb = "<div id=\"LWContainer"+this.tableId+"\" class=\"LWContainer\" style=\"height:" +
            (h - 2) + "px;" + "width:" + (w - 2) + "px;\"> ";

        return sb + "\n" + NTcreateDivTitle(h, w) + NTcreateDivData(h, w) + "</div>\n";
    }

    private String NTcreateDivTitle(int h, int w)
    {
        String sb = "";
        sb = "<div id=\"LWCtTitle" + this.tableId +
            "\" class=\"LWCtTitle\" style=\"width:" +
           /* ((this.scroll) ? (w - 17) : */ (w - 17) /*)*/ + "px;" +
            "\"> ";

        return sb + "\n" + NTcreateHTable(h, w) + "</div>\n";
    }

    private String NTcreateDivData(int h, int w)
    {
        String sb = "";
        sb = "<div id=\"LWCtDataName" + this.tableId + "\" onscroll=\"LV_HScroll(" +
            this.tableId + ");\" class=\"LWCtDataName\" style=\"" +
            "height:" + (h - 26) +
            "px; width:" + (w - 2) + "px;overflow:auto;\"></div>\n";

        return sb;
    }

    private String NTcreateHTable(int h, int w)
    {
        String sTableHead = "";
        sTableHead = "<table id=\"" + tableName + "\" class=\"table\" border=\"0\" onMouseUp=\"" +
            "document.onmousemove=null;return false\" cellspacing=\"1px\"><tr>";

        return sTableHead + "\n" + NTcreateHTableRows() + "</tr></table>\n";
    }

    private String NTcreateHTableRows()
    {
        StringBuffer sb = new StringBuffer("");

        for (int i = 0; i < columns; i++)
        {
            // Write TH Element
            sb.append("<th id=\"" + tableName + "th0" + i +
                "\" class=\"th\" onmouseover=\"LV_changeColor(this, 0)\"");
            sb.append("onMouseOut=\"LV_changeColor(this, 1)\" title=\"" + tableHeader[i] +
                "\" style=\"background-repeat:no-repeat;\">");

            // only for scheduler dashboard
            if((this.rowHeight == 2008 && columns == 5 && i==3) ||(this.rowHeight == 2008 && columns == 4 && i==2))
            {
            	sb.append("<div id=\"" + this.tableId + "div" + i + "\" style=\"width:400" +
            			 "px;height:18px;cursor:pointer;overflow:hidden;\"");
            }
            else
            {
            	sb.append("<div id=\"" + this.tableId + "div" + i + "\" style=\"width:" +
            			columnsSize[i] + "px;height:18px;cursor:pointer;overflow:hidden;\"");
            }

            if (this.doOrder)
            {
                sb.append(" onClick=\"checkClick(1,'" + tableName + "','TABLEDATA" + this.tableId +
                    "','TDBdy" + this.tableId + "'," + i + ",'LWCtDataName" + this.tableId +
                    "');\"");
                sb.append(" onDblClick=\"R_showHideColumn(" + i + ", " + rows + ", " +
                    columnsSize[i] + ", " + this.tableId + ",'LWCtDataName" + this.tableId +
                    "')\"");
            }

            sb.append(">" + tableHeader[i]);
            sb.append("</div>");

            // Write DIV resize
            if (this.doResiz)
            {
                sb.append("<div id=\"" + this.tableId + "d" + i +
                    "\" style=\"position:absolute;left:" + calcLeftValDivResize(i) +
                    "px;top:0;width:5px;cursor:e-resize\"");
                sb.append("onMouseDown=\"R_resize(event, 1, " + i + ", " + rows + ", " +
                    this.tableId + ",'LWCtDataName" + this.tableId + "')\">&nbsp;</div>");
            }

            sb.append("</th>\n\t");
        }

        return sb.toString();
    }

    private String NTcreateDTableRows()
    {
        StringBuffer sb = new StringBuffer("");
        HTMLElement htmlElem = null;

        try
        {
            String sAValue = "var aValue = new Array(";
            String sASize = "var aSize = new Array(";
            String sAlign = "var aAlgn = new Array(";
            String sHeader = "var aHeaderValue = new Array(";

            sb.append("<script>\n");

            for (int i = 0; i < this.rows; i++)
            {
                sb.append("var aC" + i + " = new Array(");
                sb.append("\"" +
                    (((this.rowsClasses.length > 0) && (this.rowsClasses[i] != null))
                    ? this.rowsClasses[i] : "") + "\",");
                sb.append("\"" +
                    (((this.snglClickRowFunction.length > 0) &&
                    (this.snglClickRowFunction[i] != null)) ? this.snglClickRowFunction[i] : "") +
                    "\",");
                sb.append("\"" +
                    (((this.dblClickRowFunction.length > 0) &&
                    (this.dblClickRowFunction[i] != null)) ? this.dblClickRowFunction[i] : "") +
                    "\",");

                
                if (this.tableData != null) {
                	int nCols = this.tableData[0].length;
                	for (int j = 0; j < nCols;) {
                        String stream = "";
                        htmlElem = this.tableData[i][j];

                        if (htmlElem != null)
                        {
                            stream = htmlElem.getHTMLText();
                            if(stream == null)
                            	stream = "";
                            stream = Replacer.replace(stream, "\"", "'");
                        }

                        sb.append("\"" + stream + "\"");
                		
                        if( ++j < nCols )
                        	sb.append(",");
                	}
                }
                else for (int j = 0; j < this.columns; j++) {
                    String stream = "";

                    sb.append("\"" + stream + "\"");

                    if ((this.columns - 1) != j)
                    {
                        sb.append(",");
                    }
                }

                sb.append(");\n");

                sAValue += ("aC" + i);

                if ((this.rows - 1) != i)
                {
                    sAValue += ",";
                }
            }

            sb.append(sAValue + ");\n");

            for (int i = 0; i < this.columns; i++)
            {
                sASize += ("'" + columnsSize[i] + "'");

                if ((this.columns - 1) != i)
                {
                    sASize += ",";
                }

                sAlign += ("'" + this.typeAlign[i] + "'");

                if ((this.columns - 1) != i)
                {
                    sAlign += ",";
                }

                sHeader += ("\"" + this.tableHeader[i] + "\"");

                if ((this.columns - 1) != i)
                {
                    sHeader += ",";
                }
            }

            sb.append(sASize + ");\n");
            sb.append(sAlign + ");\n");

            //aggiungo headers
            sb.append(sHeader + ");\n");

            sb.append("var Lsw" + this.tableId + " = new ListView(\"" + this.tableTitle +
                "\",\"LWCtDataName" + this.tableId + "\", " + this.tableId + ", " + this.rows +
                ", " + this.columns + ", aValue,aHeaderValue, aSize, aAlgn,\"" +
                this.sgClickRowAction + "\",\"" + this.dbClickRowAction + "\"," + this.rowHeight +
                "," + this.rowSelectColor + "," + this.pageNumber + "," + this.pageTotal +");\n");
            sb.append("Lsw" + this.tableId + ".render();\n");
            sb.append("oLswContainer.addLsw(Lsw" + this.tableId + ");");
            sb.append("</script>");
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return sb.toString();
    }

    private String NTcreateDTableRowsRefresh()
    {
        StringBuffer sb = new StringBuffer("");
        HTMLElement htmlElem = null;

        try
        {
            String sAValue = "var aValue = new Array(";
            String sASize = "var aSize = new Array(";
            String sAlign = "var aAlgn = new Array(";

            sb.append("<script>\n");

            for (int i = 0; i < this.rows; i++)
            {
                sb.append("var aC" + i + " = new Array(");
                sb.append("\"" +
                    (((this.rowsClasses.length > 0) && (this.rowsClasses[i] != null))
                    ? this.rowsClasses[i] : "") + "\",");
                sb.append("\"" +
                    (((this.snglClickRowFunction.length > 0) &&
                    (this.snglClickRowFunction[i] != null)) ? this.snglClickRowFunction[i] : "") +
                    "\",");
                sb.append("\"" +
                    (((this.dblClickRowFunction.length > 0) &&
                    (this.dblClickRowFunction[i] != null)) ? this.dblClickRowFunction[i] : "") +
                    "\",");

                for (int j = 0; j < this.columns; j++)
                {
                    String stream = "";

                    if (this.tableData != null)
                    {
                        htmlElem = this.tableData[i][j];

                        if (htmlElem != null)
                        {
                            stream = htmlElem.getHTMLText();
                            if(stream == null)
                            	stream = "";
                            stream = Replacer.replace(stream, "\"", "'");
                        }
                    }

                    sb.append("\"" + stream + "\"");

                    if ((this.columns - 1) != j)
                    {
                        sb.append(",");
                    }
                }

                sb.append(");\n");

                sAValue += ("aC" + i);

                if ((this.rows - 1) != i)
                {
                    sAValue += ",";
                }
            }

            sb.append(sAValue + ");\n");

            for (int i = 0; i < this.columns; i++)
            {
                sASize += ("'" + columnsSize[i] + "'");

                if ((this.columns - 1) != i)
                {
                    sASize += ",";
                }

                sAlign += ("'" + this.typeAlign[i] + "'");

                if ((this.columns - 1) != i)
                {
                    sAlign += ",";
                }
            }

            sb.append(sASize + ");\n");
            sb.append(sAlign + ");\n");
            sb.append("var oLswRef = null;\n");
            sb.append("try { ");
            sb.append("if(top.frames['body'].frames['bodytab'].oLswContainer != null){");
            sb.append(
                "oLswRef = top.frames['body'].frames['bodytab'].oLswContainer.getLsw('LWCtDataName" +
                this.tableId + "');}\n");
            sb.append("if(oLswRef != null){\n");
            sb.append("oLswRef.refresh(" + this.rows + "," + this.columns + ",aValue,");
            sb.append("\"" + this.sgClickRowAction + "\",\"" + this.dbClickRowAction + "\",\"" +
                this.tableName + "\");");
            if (isPage)
            {
                sb.append("oLswRef.setPageNumber(" + this.tableId + "," + this.pageNumber + ");");
                sb.append("oLswRef.setPageTotal(" + this.tableId + "," + this.pageTotal + ");");
            } //if
//            System.out.println("page total="+this.pageTotal);
            sb.append("}\n");
            sb.append("} catch(e){}");
            sb.append("</script>");
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return sb.toString();
    }

    /*
     * NT
     * Calcola la posizione corretta per il DIV per ridimensionamento
     */
    private int calcLeftValDivResize(int idx)
    {
        int ret = 0;

        for (int i = 0; i <= idx; i++)
        {
            ret += (columnsSize[i] + 11);
        }

        return ret;
    }

    public void setRowsClasses(String[] rowsClasses)
    {
        this.rowsClasses = rowsClasses;
    }

    public void setDlbClickRowFunction(String[] dlbclkRF)
    {
        this.dblClickRowFunction = dlbclkRF;
    }

    public void setSnglClickRowFunction(String[] snglclkRF)
    {
        this.snglClickRowFunction = snglclkRF;
    }

    public void setAlignType(int col, int type)
    {
        try
        {
            this.typeAlign[col] = type;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public void setAlignType(int[] type)
    {
        this.typeAlign = type;
    }

    private int[] initializeAlign(int numCol)
    {
        int[] ret = new int[numCol];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = 0;
        }

        return ret;
    }

    public void setAlignColum(int[] type)
    {
        if (this.typeColum.length == type.length)
        {
            this.typeColum = type;
        }
    }

    public void setTypeColum(int col, int type)
    {
        try
        {
            this.typeColum[col] = type;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private int[] initializeType(int numCol)
    {
        int[] ret = new int[numCol];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = 0;
        }

        return ret;
    }

    public int getLength()
    {
        return 0;
    }

    public void setPage(boolean isPage)
    {
        this.isPage = isPage;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    private String paginTable()
    {
        StringBuffer sb = null;
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        String vkClass="";
        if(OnScreenKey)
        {
        	vkClass = "class='keyboardInput'";
        }
        if (isPage)
        {
            sb = new StringBuffer();
            sb.append("<div class='rewFwdBox'>");

            sb.append("<table><tr valign='middle'><td>");
            sb.append("<img id=\"f"+this.tableId+"4\" src=\"images/lsw/sxsx_on.png\" onclick=\"changePageNumber(");
            sb.append(this.tableId);
            sb.append(",'superleft',this)\">");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<img id=\"f"+this.tableId+"3\" src=\"images/lsw/sx_on.png\" onclick=\"changePageNumber(");
            sb.append(this.tableId);
            sb.append(",'left',this)\">");
            sb.append("</td>");
            
			//2010-12-22, Kevin Ge, add page number input field
            sb.append("<td align='right' class='pageNum' id=\"page" + this.tableId +
                "\"><input type=text size='1' id='ListViewPaging_"+this.tableId+"' "+vkClass+" onkeydown='checkOnlyNumber(this,event);pageSetInput_keydown(this,"+this.tableId+",event);' onblur='onlyNumberOnBlur(this);' value='"+this.pageNumber+"'></td>");
            sb.append("<td class='pageTot' id=\"tot" + this.tableId +
                    "\">" + "/"+this.pageTotal + "</td>");
            //go to button
            sb.append("<td>");
            sb.append("<img src=\"images/lsw/dxdxgo_on.png\" onclick=\"pageSetInput_imgClick("+this.tableId+")\">");
            sb.append("</td>");
            
            sb.append("<td>");
            sb.append("<img id=\"f"+this.tableId+"2\" src=\"images/lsw/dx_on.png\" onclick=\"changePageNumber(");
            sb.append(this.tableId);
            sb.append(",'right',this)\" />");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<img id=\"f"+this.tableId+"1\" src=\"images/lsw/dxdx_on.png\" onclick=\"changePageNumber(");
            sb.append(this.tableId);
            sb.append(",'superright',this)\" />");
            sb.append("</td></tr></table>");

            sb.append("</div>");

            return sb.toString();
        }

        return "";
    }

    public int getColumns() {
        return this.columns;
    }
    
    private int recalWidth(int value)
    {
        return (int)Math.round(((double)(value*(this.screenW-44-SCROLLWIDTH))/900));
    }
    
    private int recalWidthColum(int value)
    {
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
    
    /*
    private void columnSizeChecker()
    {
        int viewPortTb = this.width - 17;
        System.out.println("viewPortTb " + viewPortTb);
        
        int summCols = 0;
        for(int i=0; i<columnsSize.length; i++)
            summCols += columnsSize[i]+10;
        
        System.out.println("summCols " + summCols);
        if(summCols > viewPortTb)
        {
            int delta = summCols - viewPortTb;
            System.out.println("Delta " + delta);
            delta = (int)(delta / columnsSize.length);
            System.out.println("Da ripartire " + delta);
            for(int i=0; i<columnsSize.length; i++)
                columnsSize[i] = columnsSize[i]-delta-3;
        }
    }
    */
}
