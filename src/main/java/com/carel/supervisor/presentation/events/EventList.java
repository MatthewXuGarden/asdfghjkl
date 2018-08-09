package com.carel.supervisor.presentation.events;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.Event;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.helper.SiteListHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import java.util.HashMap;
import java.util.Map;


public class EventList
{
    private int NUM_EVENTS_4_PAGE = 50;
    private String title = "";
    private Event[] list = null;
    private int width = 0;
    private int height = 0;
    private int pageNumber = 1;
    private int pageTotal = 1;
    private boolean localVisibility = true;
    private SiteListHelper siteListHelper = null;
    private int screenw = 1024;
    private int screenh = 768;

        
    public EventList()
    {
    }

    public EventList(String title)
    {
        this.title = title;
    }

    public void loadFromDataBase(UserSession userSession, int numPage)
    {
        try
        {
            int site = userSession.getIdSite();
            String language = userSession.getLanguage();
            localVisibility = userSession.localVisibility();
            list = null;

            siteListHelper = new SiteListHelper();
            siteListHelper.load();

            StringBuffer sql = new StringBuffer();

            if (localVisibility)
            {
                sql.append("select * from hsevent where idsite = ? order by lastupdate desc ");
            }
            else
            {
                sql.append("select * from hsevent order by lastupdate desc ");
            }

            if (numPage == 0)
            {
                RecordSet recordSet = null;

                if (localVisibility)
                {
                    String sqlTmp = "select count(1) as count from hsevent where idsite = ?";
                    recordSet = DatabaseMgr.getInstance().executeQuery(null, sqlTmp,
                            new Object[] { new Integer(site) });
                }
                else
                {
                    String sqlTmp = "select count(1) as count from hsevent";
                    recordSet = DatabaseMgr.getInstance().executeQuery(null, sqlTmp, null);
                }

                if (recordSet.size() > 0)
                {
                    pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / NUM_EVENTS_4_PAGE;
                }
                else
                {
                    pageNumber = 1;
                }

                numPage = pageNumber + 1;
                pageNumber++;
            } //page


            //simon add for get total count. at 2009-2-27
            String totalSql="select count(1) as count from hsevent where idsite = ? ";
            RecordSet totRec = DatabaseMgr.getInstance().executeQuery(null,totalSql,new Object[]{new Integer(site)});
            if (totRec.size() > 0) {
            	this.pageTotal = ((Integer) totRec.get(0).get("count")).intValue() / NUM_EVENTS_4_PAGE+1;
            }else{
            	this.pageTotal = 1;
            }
            //end. simon add
            
            sql.append("limit ? offset ? ");

            RecordSet recordset = null;

            if (localVisibility)
            {
                recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                        new Object[]
                        {
                            new Integer(site), new Integer(NUM_EVENTS_4_PAGE),
                            new Integer((numPage - 1) * NUM_EVENTS_4_PAGE)
                        });
            }
            else
            {
                recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                        new Object[]
                        {
                            new Integer(NUM_EVENTS_4_PAGE),
                            new Integer((numPage - 1) * NUM_EVENTS_4_PAGE)
                        });
            }

            Record record = null;
            list = new Event[recordset.size()];

            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                list[i] = new Event(record);
            }

            sql = new StringBuffer();
            sql.append("select * from cfmessage where languagecode = ?");
            recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                    new Object[] { language });

            Map message = new HashMap();

            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                message.put(UtilBean.trim(record.get("messagecode")),
                    UtilBean.trim(record.get("description")));
            }

            sql = new StringBuffer();
            sql.append("select * from cfcategory where languagecode = ?");

            recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                    new Object[] { language });

            Map category = new HashMap();

            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                category.put(UtilBean.trim(record.get("categorycode")),
                    UtilBean.trim(record.get("description")));
            }

            for (int i = 0; i < list.length; i++)
            {
                list[i].setCategory((String) category.get(list[i].getCategorycode()));
                list[i].setMessage((String) message.get(list[i].getMessagecode()));
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

    
    private HTMLTable getHTMLEventTablePrv(String tableName, UserSession us)
    {
    	String language = us.getLanguage();
        LangService lan = LangMgr.getInstance().getLangService(language);

        String[] header = null;

        if (localVisibility)
        {
            header = new String[]
                {
                    lan.getString("evnview", "data"), lan.getString("evnview", "user"),
                    lan.getString("evnview", "type"), lan.getString("evnview", "category"),
                    lan.getString("evnview", "description")
                };
        }
        else
        {
            header = new String[]
                {
                    lan.getString("evnview", "data"), lan.getString("evnview", "site"),
                    lan.getString("evnview", "user"), lan.getString("evnview", "type"),
                    lan.getString("evnview", "category"), lan.getString("evnview", "description")
                };
        }

        HTMLTable eventTable = new HTMLTable(tableName, header, getEventTable(), true, false);
        eventTable.setScreenH(screenh);
        eventTable.setScreenW(screenw);
        eventTable.setWidth(width);
        eventTable.setHeight(height);
        eventTable.setPage(true);
        eventTable.setPageNumber(pageNumber);
        eventTable.setPageTotal(pageTotal);
        eventTable.setColumnSize(0, 83);
        eventTable.setColumnSize(1, 61);
        eventTable.setColumnSize(2, 55);
        eventTable.setColumnSize(3, 100);
        eventTable.setAlignType(3, 1);
        if (localVisibility)
        {
        	eventTable.setAlignType(2, 1);
            eventTable.setColumnSize(4, 523);
        }
        else
        {
        	eventTable.setColumnSize(3, 48);
        	eventTable.setColumnSize(4,85);
            eventTable.setColumnSize(5, 455);
        }

        
        String[] clickId = new String[this.list.length];

        for (int i = 0; i < clickId.length; i++)
        {
            clickId[i] = String.valueOf(list[i].getIdevent()+"_"+list[i].getIdSite());
        }

        if (us.isMenuActive("evndtl"))
        {
	        eventTable.setSgClickRowAction(
	        "top.frames['manager'].loadTrx('nop&folder=evndtl&bo=BEvnDtl&type=click&id=$1&desc=ncode09');");
	        eventTable.setSnglClickRowFunction(clickId);
        }        
        eventTable.setTableTitle(this.title);
        eventTable.setRowHeight(18);
        eventTable.setTypeColum(0, HTMLTable.TDATA);

        return eventTable;
    }

    private HTMLElement[][] getEventTable()
    {
        HTMLElement[][] tableData = new HTMLElement[this.list.length][];
        String tmp = "";
        String nota = "";

        for (int i = 0; i < this.list.length; i++)
        {
            try
            {
                if (localVisibility)
                {
                    tableData[i] = new HTMLElement[5];
                }
                else
                {
                    tableData[i] = new HTMLElement[6];
                }

                tableData[i][0] = new HTMLSimpleElement(
                        "<div style='visibility:hidden;display:none'>" +
                        String.valueOf(list[i].getLastupdate().getTime()) + "</div>" +
                        DateUtils.date2String(list[i].getLastupdate(), "yyyy/MM/dd HH:mm:ss"));

                int c = 1;

                if (!localVisibility)
                {
                    Integer idSite = list[i].getIdSite();
                    String name = siteListHelper.getName(idSite);
                    tableData[i][c] = new HTMLSimpleElement(name);
                    c++;
                }

                tableData[i][c] = new HTMLSimpleElement(list[i].getUser());

                c++;

                if (list[i].getType().intValue() == 1)
                {
                    tableData[i][c] = new HTMLSimpleElement(
                            "<div><img src='images/event/error.gif'/><div style='visibility:hidden;'>1</div></div>");
                }
                else if (list[i].getType().intValue() == 2)
                {
                    tableData[i][c] = new HTMLSimpleElement(
                            "<div><img src='images/event/alert.gif'/><div style='visibility:hidden;'>2</div></div>");
                }
                else if (list[i].getType().intValue() == 3)
                {
                    tableData[i][c] = new HTMLSimpleElement(
                            "<div><img src='images/event/info.gif'/><div style='visibility:hidden;'>3</div></div>");
                }

                c++;
                tableData[i][c] = new HTMLSimpleElement(list[i].getCategory());
                
                c++;
                //tableData[i][c] = new HTMLSimpleElement(list[i].getMessage());
                tmp = list[i].getMessage();
                
                NoteLogList listaNote = new NoteLogList();
                listaNote.retrieve(this.list[i].getIdSite(), "hsevent", this.list[i].getIdevent());
                
                if (listaNote.size() > 0)
                {
                	nota = Replacer.replace(listaNote.getNote(listaNote.size() - 1).getNote(), "%!", " ");
                	tmp = "<img src='images/event/mininote.gif' title='" + nota + "' /> " + tmp;
                }
                
                tableData[i][c] = new HTMLSimpleElement(tmp);
                
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }

        return tableData;
    }

    public String getHTMLEventTable(String tableName, String language, UserSession us)
    {
        return getHTMLEventTablePrv(tableName, us).getHTMLText().toString();
    }

    public String getHTMLEventTableRefresh(String tableName, UserSession us)
    {
        return getHTMLEventTablePrv(tableName, us).getHTMLTextBufferRefresh().toString();
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
    
    public void setPageNumber(int page)
    {
    	this.pageNumber = page;
    }

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	public void setPageRows(Integer page_rows)
	{
		this.NUM_EVENTS_4_PAGE = page_rows;
	}

	public Integer getPageRows()
	{
		return this.NUM_EVENTS_4_PAGE;
	}
}
