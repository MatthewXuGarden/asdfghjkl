package com.carel.supervisor.presentation.timebands;

import java.util.ArrayList;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class TimeBands
{
    //Per motivi di conversione costanti in presentation queste sono quelle storicizzate sotto le presentate
    private static final int YEAR_ONE_SHOT = 4; //Questa è SPECIAL
    private static final int YEAR_REPEAT = 5; //Questa è YEARLY
    private static final int YEARLY = 4;
    private static final int SPECIAL = 5;
    private static final int NUM_TYPE = 5;
    private int rows = 10;
    private int width = 0;
    private int height = 0;
    private String title = "";
    private int pageNumber = 1;
    private ArrayList description = null;
    private ArrayList type = null;
    private ArrayList value = null;
    private ArrayList id = null;
    private String[] translate = new String[NUM_TYPE];
    private int screenw = 1024;
    private int screenh = 768;

    
    public TimeBands()
    {
    }

    public TimeBands(String title)
    {
        this.title = title;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setTitle(String tit)
    {
        this.title = tit;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void loadData(UserSession userSession) throws Exception
    {
        TimeBandList timeBandList = new TimeBandList(null,
                BaseConfig.getPlantId(), new Integer(userSession.getIdSite()));
        TimeBandBean bandBean = null;
        description = new ArrayList();
        type = new ArrayList();
        value = new ArrayList();
        id = new ArrayList();

        for (int i = 0; i < timeBandList.size(); i++)
        {
            bandBean = timeBandList.getTimeBand(i);
            id.add(bandBean.getIdtimeband());
            description.add(bandBean.getTimecode());
            type.add(new Integer(bandBean.getTimetype()));
            value.add(bandBean.getTimeband());
        } //for

        String language = userSession.getLanguage();
        LangService langService = LangMgr.getInstance().getLangService(language);
        translate[0] = langService.getString("timebandsPage", "daily");
        translate[1] = langService.getString("timebandsPage", "weekly");
        translate[2] = langService.getString("timebandsPage", "monthly");
        translate[3] = langService.getString("timebandsPage", "special");
        translate[4] = langService.getString("timebandsPage", "yearly");
    } //loadData

    private HTMLElement[][] getTimeBandsTable()
    {
        HTMLElement[][] tableData = null;

        rows = description.size();

        int cols = 3;
        int c_tmp = 0;

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            for (int i = 0; i < rows; i++)
            {
                c_tmp = 0;
                tableData[i] = new HTMLElement[cols];

                if (((Integer) id.get(i)).intValue() < 0)
                {
                    tableData[i][c_tmp++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif' >");
                }
                else
                {
                    tableData[i][c_tmp++] = new HTMLSimpleElement("");
                }

                tableData[i][c_tmp++] = new HTMLSimpleElement((String) description.get(
                            i));
                tableData[i][c_tmp++] = new HTMLSimpleElement(translate[((Integer) type.get(
                            i)).intValue() - 1]);

                //tableData[i][2] = new HTMLSimpleElement((String)value.get(i));
            }
        }

        return tableData;
    }

    private HTMLTable getHTMLTimeBandsTablePrv(String tableName,
        String language, boolean isPermict)
    {
        HTMLElement[][] data = getTimeBandsTable();
        HTMLTable timeBandsTable = new HTMLTable(tableName,
                getHeaderTable(language), data, true, true);

        timeBandsTable.setScreenH(screenh);
        timeBandsTable.setScreenW(screenw);
        
        timeBandsTable.setColumnSize(0, 50);
        timeBandsTable.setColumnSize(1, 397);
        timeBandsTable.setColumnSize(2, 398);

        timeBandsTable.setHeight(height);
        timeBandsTable.setWidth(width);
        timeBandsTable.setTableId(1);

        timeBandsTable.setTableTitle(this.title);

        timeBandsTable.setAlignType(new int[] { 1, 0, 0 });

        int numRows = description.size();

        if (numRows != 0)
        {
            String[] dblclick = new String[numRows];

            for (int i = 0; i < numRows; i++)
            {
                dblclick[i] = new String(description.get(i) + ";;" +
                        type.get(i) + ";;" + value.get(i) + ";;" + id.get(i));
            }

            if (dblclick.length == 0)
            {
                dblclick = new String[] { "nop" };
            }

            if (isPermict)
            {
                timeBandsTable.setDbClickRowAction("loadTimeBands('$1');");
                timeBandsTable.setDlbClickRowFunction(dblclick);

                timeBandsTable.setSgClickRowAction("resetTimeBands('$1');");
                timeBandsTable.setSnglClickRowFunction(dblclick);
            }
        }

        return timeBandsTable;
    }

    public String getHTMLTimeBandsTable(String tableName, String language,
        boolean isPermict)
    {
        return getHTMLTimeBandsTablePrv(tableName, language, isPermict)
                   .getHTMLText().toString();
    }

    public String getHTMLTimeBandsTableRefresh(String tableName,
        String language, boolean isPermict)
    {
        return getHTMLTimeBandsTablePrv(tableName, language, isPermict)
                   .getHTMLTextBufferRefresh().toString();
    }

    private String[] getHeaderTable(String language)
    {
        LangService langService = LangMgr.getInstance().getLangService(language);
        int cols = 3;
        int c_tmp = 0;
        String[] header = new String[cols];

        header[c_tmp++] = langService.getString("alrsched", "ide");
        header[c_tmp++] = langService.getString("timebandsTables", "desc");
        header[c_tmp++] = langService.getString("timebandsTables", "type");

        //langService.getString("timebandsTables","value")};
        return header;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
}
