package com.carel.supervisor.presentation.bean.search;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.*;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchEvent
{
	private final static int ROW_LIMIT = 5000;
    private final static int NUM_EVENTS_SEARCH_4_PAGE = 50;
    private Event[] list = null;
    private String title = "";
    private int width = 0;
    private int height = 0;
    private int pageNumber = 1;
    private int pageTotal=1;
	private int screenw = 1024;
    private int screenh = 768;
    
    public SearchEvent(String title)
    {
        this.title = title;
    }

    public SearchEvent()
    {
    }

    public void find(UserSession userSession, int numPage)
        throws DataBaseException, Exception
    {
        String user = userSession.getProperty("userevent");

        if (user == null)
        {
            user = "";
        }

        String optionDate = userSession.getProperty("dataselect");

        if (optionDate == null)
        {
            optionDate = "week";
        }

        String dateFrom = userSession.getProperty("datefrom");
        String dateTo = userSession.getProperty("dateto");
        String category = userSession.getProperty("EScategory");

        StringBuffer sql = new StringBuffer(
                "select * from hsevent where idsite = ? ");
        List params = new ArrayList();
        Record record = null;
        params.add(String.valueOf(userSession.getIdSite()));

        // Gestione intervallo di di date
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String tmp = DateUtils.date2String(now, "dd/MM/yyyy");
        String[] today = tmp.split("/");
        GregorianCalendar thismoment = new GregorianCalendar(Integer.parseInt(
                    today[2]), Integer.parseInt(today[1]) - 1,
                Integer.parseInt(today[0]));

        if (optionDate.equals("week")) //ULTIMA SETTIMANA
        {
            thismoment.add(5, -7); //sottraggo 7 giorni   field 5 = day

            Timestamp lastweek = new Timestamp(thismoment.getTimeInMillis());

            sql.append("and lastupdate between ? and ? ");
            params.add(lastweek);
            params.add(now);
        }

        else if (optionDate.equals("month")) //ULTIMO MESE
        {
            thismoment.add(2, -1); //sottraggo un mese	field 2 = month

            Timestamp lastmonth = new Timestamp(thismoment.getTimeInMillis());

            sql.append("and lastupdate between ? and ? ");
            params.add(lastmonth);
            params.add(now);
        }

        else if (optionDate.equals("3month")) //ULTIMI 3 MESI
        {
            thismoment.add(2, -3); //sottraggo 3 mesi	field 2 = month

            Timestamp last3month = new Timestamp(thismoment.getTimeInMillis());

            sql.append("and lastupdate between ? and ?");
            params.add(last3month);
            params.add(now);
        }

        else if (optionDate.equals("fromto")) //FORM X TO Y
        {
            String[] from = dateFrom.split("/");
            Timestamp dFrom = new Timestamp(new GregorianCalendar(
                        Integer.parseInt(from[2]),
                        Integer.parseInt(from[1]) - 1, Integer.parseInt(from[0])).getTimeInMillis());

            String[] to = dateTo.split("/");
            Timestamp dTo = new Timestamp(new GregorianCalendar(
                        Integer.parseInt(to[2]), Integer.parseInt(to[1]) - 1,
                        Integer.parseInt(to[0]), 23, 59, 59).getTimeInMillis());

            sql.append("and lastupdate between ? and ? ");
            params.add(dFrom);
            params.add(dTo);
        }

        //aggiunta parametro user
        if (!user.equals(""))
        {
            sql.append(" and LOWER(userevent) = ?");
            params.add(user.toLowerCase());
        }

        if (!category.equals(""))
        {
            sql.append(" and categorycode = ?");
            params.add(category);
        }

        if (numPage == 0)
        {
            int pageNumber = 0;
            Object[] paramsTmp = params.toArray();
            String sqlTmp = "select count(1) as count " +
                sql.toString().substring(new String("select * ").length());
            RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                    sqlTmp, paramsTmp);

            if (recordSet.size() > 0)
            {
                pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / NUM_EVENTS_SEARCH_4_PAGE;
            }

            else
            {
                pageNumber = 1;
            }

            numPage = pageNumber + 1;
        } //page

        pageNumber = numPage;


        //simon add for get total count. at 2009-2-27
        String sqlTmp = "select count(1) as count " +sql.toString().substring(new String("select * ").length());
	    RecordSet totSet = DatabaseMgr.getInstance().executeQuery(null,sqlTmp, params.toArray());
	
	    if (totSet.size() > 0){
			int n = ((Integer) totSet.get(0).get("count")).intValue();
	    	this.pageTotal = ((Integer) totSet.get(0).get("count")).intValue() / NUM_EVENTS_SEARCH_4_PAGE+1;
 			if (n>ROW_LIMIT)
			{
				userSession.setProperty("limit_search", "ko");
//				list = new Event[0];
//				return ;
			}	    }else{
	    	this.pageTotal = 1;
	    }
        //end. simon add
        
        // order
        sql.append(" order by lastupdate desc ");
        //per la paginazione
        sql.append(" limit ? offset ? ");

        params.add(new Integer(NUM_EVENTS_SEARCH_4_PAGE));
        params.add(new Integer((numPage - 1) * NUM_EVENTS_SEARCH_4_PAGE));

        Object[] param = params.toArray();

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                sql.toString(), param);
        Event[] events = new Event[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            events[i] = new Event(recordSet.get(i));
        }

        String query = "select * from cfmessage where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });

        Map message = new HashMap();

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            message.put(UtilBean.trim(record.get("messagecode")),
                UtilBean.trim(record.get("description")));
        }

        query = "select * from cfcategory where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });

        Map categ = new HashMap();

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            categ.put(UtilBean.trim(record.get("categorycode")),
                UtilBean.trim(record.get("description")));
        }

        for (int i = 0; i < events.length; i++)
        {
            events[i].setCategory((String) categ.get(
                    events[i].getCategorycode()));
            events[i].setMessage((String) message.get(
                    events[i].getMessagecode()));
        }

        this.list = events;
    }

    public void findRemote(UserSession userSession, int numPage)
        throws DataBaseException, Exception
    {
        String s_idsite = userSession.getProperty("ESid_site");

        int idsite = Integer.parseInt(s_idsite);

        String optionDate = userSession.getProperty("dataselect");

        if (optionDate == null)
        {
            optionDate = "week";
        }

        String dateFrom = userSession.getProperty("datefrom");
        String dateTo = userSession.getProperty("dateto");
        String category = userSession.getProperty("EScategory");

        StringBuffer sql = new StringBuffer("select * from hsevent where");
        List params = new ArrayList();
        Record record = null;

        if (idsite != -1)
        {
            sql.append(" idsite = ? and");
            params.add(new Integer(idsite));
        }

        // Gestione intervallo di di date
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String tmp = DateUtils.date2String(now, "dd/MM/yyyy");
        String[] today = tmp.split("/");
        GregorianCalendar thismoment = new GregorianCalendar(Integer.parseInt(
                    today[2]), Integer.parseInt(today[1]) - 1,
                Integer.parseInt(today[0]));

        if (optionDate.equals("week")) //ULTIMA SETTIMANA
        {
            thismoment.add(5, -7); //sottraggo 7 giorni   field 5 = day

            Timestamp lastweek = new Timestamp(thismoment.getTimeInMillis());

            sql.append(" lastupdate between ? and ? ");
            params.add(lastweek);
            params.add(now);
        }

        else if (optionDate.equals("month")) //ULTIMO MESE
        {
            thismoment.add(2, -1); //sottraggo un mese	field 2 = month

            Timestamp lastmonth = new Timestamp(thismoment.getTimeInMillis());

            sql.append(" lastupdate between ? and ? ");
            params.add(lastmonth);
            params.add(now);
        }

        else if (optionDate.equals("3month")) //ULTIMI 3 MESI
        {
            thismoment.add(2, -3); //sottraggo 3 mesi	field 2 = month

            Timestamp last3month = new Timestamp(thismoment.getTimeInMillis());

            sql.append(" lastupdate between ? and ?");
            params.add(last3month);
            params.add(now);
        }

        else if (optionDate.equals("fromto")) //FORM X TO Y
        {
            String[] from = dateFrom.split("/");
            Timestamp dFrom = new Timestamp(new GregorianCalendar(
                        Integer.parseInt(from[2]),
                        Integer.parseInt(from[1]) - 1, Integer.parseInt(from[0])).getTimeInMillis());

            String[] to = dateTo.split("/");
            Timestamp dTo = new Timestamp(new GregorianCalendar(
                        Integer.parseInt(to[2]), Integer.parseInt(to[1]) - 1,
                        Integer.parseInt(to[0]), 23, 59, 59).getTimeInMillis());

            sql.append(" lastupdate between ? and ? ");
            params.add(dFrom);
            params.add(dTo);
        }

        if (!category.equals(""))
        {
            sql.append(" and categorycode = ?");
            params.add(category);
        }

        if (numPage == 0)
        {
            int pageNumber = 0;
            Object[] paramsTmp = params.toArray();
            String sqlTmp = "select count(1) as count " +
                sql.toString().substring(new String("select * ").length());
            RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                    sqlTmp, paramsTmp);

            if (recordSet.size() > 0)
            {
                pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / NUM_EVENTS_SEARCH_4_PAGE;
            }

            else
            {
                pageNumber = 1;
            }

            numPage = pageNumber + 1;
        } //page

        pageNumber = numPage;

        //simon add for get total count. at 2009-2-27
        String sqlTmp = "select count(1) as count " +
        sql.toString().substring(new String("select * ").length());
	    RecordSet totSet = DatabaseMgr.getInstance().executeQuery(null,sqlTmp, params.toArray());
	
	    if (totSet.size() > 0){
	    	this.pageTotal = ((Integer) totSet.get(0).get("count")).intValue() / NUM_EVENTS_SEARCH_4_PAGE+1;
	    }else{
	    	this.pageTotal = 1;
	    }
        //end. simon add
        
        //per la paginazione
        sql.append(" limit ? offset ? ");

        params.add(new Integer(NUM_EVENTS_SEARCH_4_PAGE));
        params.add(new Integer((numPage - 1) * NUM_EVENTS_SEARCH_4_PAGE));

        Object[] param = params.toArray();

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                sql.toString(), param);
        Event[] events = new Event[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            events[i] = new Event(recordSet.get(i));
        }

        String query = "select * from cfmessage where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });

        Map message = new HashMap();

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            message.put(UtilBean.trim(record.get("messagecode")),
                UtilBean.trim(record.get("description")));
        }

        query = "select * from cfcategory where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });

        Map categ = new HashMap();

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            categ.put(UtilBean.trim(record.get("categorycode")),
                UtilBean.trim(record.get("description")));
        }

        for (int i = 0; i < events.length; i++)
        {
            events[i].setCategory((String) categ.get(
                    events[i].getCategorycode()));
            events[i].setMessage((String) message.get(
                    events[i].getMessagecode()));
        }

        this.list = events;
    }

    public HTMLTable getHTMLEventTablePrv(String tableName, UserSession us)
    {
    	String language = us.getLanguage();
        LangService lan = LangMgr.getInstance().getLangService(language);

        String[] header = 
            {
                lan.getString("evnview", "data"),
                lan.getString("evnview", "user"),
                lan.getString("evnview", "type"),
                lan.getString("evnview", "category"),
                lan.getString("evnview", "description")
            };

        HTMLTable eventTable = new HTMLTable("EventTable", header,
                createData(), true, false);
        eventTable.setScreenH(screenh);
        eventTable.setScreenW(screenw);
        eventTable.setWidth(width);
        eventTable.setHeight(height);
        eventTable.setRowHeight(18);
        eventTable.setPage(true);
        eventTable.setPageNumber(pageNumber);
        eventTable.setPageTotal(pageTotal);
        eventTable.setColumnSize(3, 86);
        eventTable.setColumnSize(4, 500);
        eventTable.setTypeColum(0, HTMLTable.TDATA);
        eventTable.setAlignType(new int[]{1,1,1,1,0});
        

        String[] clickId = new String[this.list.length];

        for (int i = 0; i < clickId.length; i++)
        {
            clickId[i] = String.valueOf(list[i].getIdevent());
        }

        if (us.isMenuActive("evndtl"))
        {
        	eventTable.setSnglClickRowFunction(clickId);
        	eventTable.setSgClickRowAction(
            "top.frames['manager'].loadTrx('nop&folder=evndtl&bo=BEvnDtl&type=click&id=$1&desc=ncode09');");
        }
        

        
        eventTable.setTableTitle(this.title);

        return eventTable;
    }

    private HTMLElement[][] createData()
    {
        HTMLElement[][] tableData = null;
        String tmp = "";
        String nota = "";
        
        tableData = new HTMLElement[this.list.length][];

        for (int i = 0; i < this.list.length; i++)
        {
            try
            {
                tableData[i] = new HTMLElement[5];
                tableData[i][0] = new HTMLSimpleElement(
                        "<div style='visibility:hidden;display:none'>" +
                        getLongDate(list[i].getLastupdate()) + "</div>" +
                        DateUtils.date2String(this.list[i].getLastupdate(),
                            "dd/MM/yyyy HH:mm:ss"));
                tableData[i][1] = new HTMLSimpleElement(this.list[i].getUser());
                if (list[i].getType().intValue() == 1)
                {
                    tableData[i][2] = new HTMLSimpleElement(
                            "<div ><img src='images/event/error.gif'/><div style='visibility:hidden;'>1</div></div>");
                }
                else if (list[i].getType().intValue() == 2)
                {
                    tableData[i][2] = new HTMLSimpleElement(
                            "<div><img src='images/event/alert.gif'/><div style='visibility:hidden;'>2</div></div>");
                }
                else if (list[i].getType().intValue() == 3)
                {
                    tableData[i][2] = new HTMLSimpleElement(
                            "<div><img src='images/event/info.gif'/><div style='visibility:hidden;'>3</div></div>");
                }
                tableData[i][3] = new HTMLSimpleElement(this.list[i].getCategory());
                //tableData[i][4] = new HTMLSimpleElement(this.list[i].getMessage());
                
                tmp = list[i].getMessage();
                
                NoteLogList listaNote = new NoteLogList();
                listaNote.retrieve(this.list[i].getIdSite(), "hsevent", this.list[i].getIdevent());
                
                if (listaNote.size() > 0)
                {
                	nota = Replacer.replace(listaNote.getNote(listaNote.size() - 1).getNote(), "%!", " ");
                	tmp = "<img src='images/event/mininote.gif' title='"+ nota +"' /> " + tmp;
                }
                
                tableData[i][4] = new HTMLSimpleElement(tmp);
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
        return getHTMLEventTablePrv(tableName, us)
                   .getHTMLTextBufferRefresh().toString();
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

    private String getLongDate(Date in)
    {
        if (in != null)
        {
            return String.valueOf(in.getTime());
        }
        else
        {
            return "";
        }
    }
    
    public int getNumOfEvents()
    {
    	return list.length;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
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

	public void setList(Event[] list) {
		this.list = list;
	}

	

}
