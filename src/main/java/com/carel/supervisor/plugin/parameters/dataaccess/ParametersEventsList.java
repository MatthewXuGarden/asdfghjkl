package com.carel.supervisor.plugin.parameters.dataaccess;

import java.text.SimpleDateFormat;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.presentation.bo.helper.SiteListHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class ParametersEventsList {

    private final static int NUM_ALARMS_4_PAGE = 50;
    private ParametersEvent[] parametersEventsList = null;
    
    private int pageNumber = 1;
    private int pageTotal = -1;
    private int screenw = 1024;
    private int screenh = 768;
    private int width = 0;
    private int height = 0;
    private String title = "";
	
    public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	
	
    public ParametersEventsList(String language, String dbId, int idsite,
            int numPage, boolean onlyToNotify, int iddevice)
            throws DataBaseException
    {
    	
    	String sql = "select parameters_events.*,cfdevice.iddevice, a_desc.description as dev_descr,b_desc.description as var_descr " +
    			" from parameters_events " +
    			" left outer join cfvariable on parameters_events.idvariable = cfvariable.idvariable " +
    			" left outer join cfdevice on cfvariable.iddevice = cfdevice.iddevice " +
    			
    			" left outer join cftableext a_desc on a_desc.tableid = cfdevice.iddevice "+
    		    "   and a_desc.tablename='cfdevice' "+
    		    "   and a_desc.languagecode='"+language+"' and a_desc.idsite = 1 "+
    		       
    			" left outer join cftableext b_desc on b_desc.tableid = cfvariable.idvariable "+
    		    "   and b_desc.tablename='cfvariable' "+
    		    "   and b_desc.languagecode='"+language+"' and b_desc.idsite = 1 "; 

    	String sqlWhere="";
    	if (!onlyToNotify && iddevice==-1)
    	{
    		sqlWhere="";
    	}
    	else
    	{
    		boolean isfirst=true;
    		sqlWhere +="where ";
	    	if (onlyToNotify)
	    	{
	    		if (isfirst) { isfirst=false; } else { sqlWhere+=" and "; }
	    		sqlWhere +=" tonotify = 1 ";	
	    	}
	    	
	    	if (iddevice!=-1)
	    	{
	    		if (isfirst) { isfirst=false; } else { sqlWhere+=" and "; }
	    		sqlWhere+=" (cfdevice.iddevice="+iddevice+") or (parameters_events.eventtype = '"+ParametersMgr.TAKENPHOTOGRAPHYCODE+"' ) ";
	    	}
    	}
    	sql+=sqlWhere;
    	if ((numPage == 0)||(pageTotal==-1))
        {
    		//devo trovare l'ultima pagina
            String sqlTmp = "select count(1) as count " +
            sql.toString().substring(new String("select parameters_events.*,cfdevice.iddevice, a_desc.description as dev_descr,b_desc.description as var_descr ").length());
	        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
	                sqlTmp, null);

	        int lastpaqe;
	        if (recordSet.size() > 0)
	        {
	            lastpaqe = ((Integer) recordSet.get(0).get("count")).intValue() / NUM_ALARMS_4_PAGE+1;
	        }
	        else
	        {
	        	lastpaqe = 1;
	        }
	
	        //params[params.length - 1] = new Integer(pageNumber * NUM_ALARMS_4_PAGE);
	        if (numPage == 0) 
	        	{
	        		numPage =lastpaqe;
	        		pageNumber=lastpaqe ;
	        	}
	        
	        if (pageTotal==-1) pageTotal=lastpaqe;
        }
    	
    	//se numPage > -1 allora paginazione dei risultati
    	Object[] params = null;
    	if (numPage>-1)
    	{
	    	sql +=" order by datetime desc limit ? offset ? ";
	    	
	    	params = new Object[2];
	    	params[0] = NUM_ALARMS_4_PAGE;
	    	params[1] = new Integer( (numPage-1) * NUM_ALARMS_4_PAGE);
    	}
    	
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                sql.toString(), params);
        
        Record record = null;
        parametersEventsList = new ParametersEvent[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
        	record = recordSet.get(i);
        	ParametersEvent pe = new ParametersEvent(record);
        	parametersEventsList[i]=pe;
        }
    	
    }
    
    public String getHTMLTable(String tableName, UserSession us, boolean canModify)
    {
        return getHTMLTablePrv(tableName, us, canModify).getHTMLText().toString();
    }
    
    public String getHTMLTableRefresh(String tableName, UserSession us , boolean canModify)
    {
        return getHTMLTablePrv(tableName, us, canModify).getHTMLTextBufferRefresh().toString();
    }
    
    private HTMLTable getHTMLTablePrv(String tableName, UserSession us, boolean canModify)
    {
    	String language = us.getLanguage();
        HTMLElement[][] data = getParametersEventsTable(us, canModify);
        HTMLTable eventTable = new HTMLTable(tableName,
        		getParametersEventsHeader(language), data, true, true);
        eventTable.setPage(true);
        eventTable.setPageNumber(pageNumber);
        eventTable.setPageTotal(pageTotal);
        
        eventTable.setScreenH(this.screenh);
        eventTable.setScreenW(this.screenw-50);
        
        eventTable.setColumnSize(0, 60);	
        eventTable.setColumnSize(1, 80);
        eventTable.setColumnSize(2, 50);
        eventTable.setColumnSize(3, 150);
        eventTable.setColumnSize(4, 150);
        eventTable.setColumnSize(5, 40);
        eventTable.setColumnSize(6, 40);
        eventTable.setColumnSize(7, 40);

        eventTable.setAlignType(7, 1);
        
        eventTable.setHeight(height);
        eventTable.setWidth(width);
        eventTable.setTableId(0);
        eventTable.setTableTitle(this.title);
        eventTable.setTypeColum(0, HTMLTable.TDATA);

        eventTable.setRowHeight(25);

        return eventTable;
    }
    
    private HTMLElement[][] getParametersEventsTable(UserSession us, boolean canModify)
    {
    	String language = us.getLanguage();
        HTMLElement[][] tableData = null;
        SiteListHelper help = new SiteListHelper();
        LangService lan = LangMgr.getInstance().getLangService(language);

        try
        {
            help.load();
        }
        catch (Exception e)
        {
        }

        int rows = parametersEventsList .length;

        int idx = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            boolean dtlviewEnabled = us.isMenuActive("dtlview");
            for (int i = 0; i < rows; i++)
            {
            	boolean isPhoto = parametersEventsList[i].getEventtype().equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE);
            	boolean isRollback = parametersEventsList[i].getCanrollback();
                idx = 0;
        
                int nCol = 8;

                tableData[i] = new HTMLElement[nCol];
                
                tableData[i][idx++]=new HTMLSimpleElement(sdf.format(parametersEventsList[i].getDatetime()));
                tableData[i][idx++]=new HTMLSimpleElement(parametersEventsList[i].getUsername());

                if (parametersEventsList[i].getEventtype().equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE )){
                	tableData[i][idx++]=new HTMLSimpleElement(lan.getString("parameters", "photo"));
                }
                else if (parametersEventsList[i].getEventtype().equalsIgnoreCase(ParametersMgr.ROLLBACKCODE )){
                	tableData[i][idx++]=new HTMLSimpleElement(lan.getString("parameters", "rollback"));
                }
                else //altrimenti � stata una modifica
                	tableData[i][idx++]=new HTMLSimpleElement(lan.getString("parameters", "modify") );

                if (!isPhoto)
                {   
                	if(dtlviewEnabled)
                		tableData[i][idx++]=new HTMLSimpleElement("<a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+parametersEventsList[i].getIddevice()+"&desc=ncode01') >"+parametersEventsList[i].getDev_descr()+"</a>");
                	else
                		tableData[i][idx++]=new HTMLSimpleElement(parametersEventsList[i].getDev_descr());
	                tableData[i][idx++]=new HTMLSimpleElement(parametersEventsList[i].getVar_descr());
	                tableData[i][idx++]=new HTMLSimpleElement(parametersEventsList[i].getStartingvalue().toString());
	                tableData[i][idx++]=new HTMLSimpleElement(parametersEventsList[i].getEndingvalue().toString());
	                
	            }
                else{
	                tableData[i][idx++]=new HTMLSimpleElement("");
	                tableData[i][idx++]=new HTMLSimpleElement("");
	                tableData[i][idx++]=new HTMLSimpleElement("");
	                tableData[i][idx++]=new HTMLSimpleElement("");                	
                }
                
                if(isRollback && canModify)
                {
                	//il tasto di rollback lo mostro se l'azione lo permette e se devo mostrarlo in pagina (canModify)
                	tableData[i][idx++]=new HTMLSimpleElement("<img src=\"./images/actions/rollback_on_black.png\" onclick=\"parameters_rollback("+parametersEventsList[i].getId()+");\">");
                }
                else
                {
                	tableData[i][idx++]=new HTMLSimpleElement("");
                }
            }
            
        }

        return tableData;
    }
    
    
    private String[] getParametersEventsHeader(String LanguageUsed)
    {
        LangService l = LangMgr.getInstance().getLangService(LanguageUsed);
        String[] intestation = null;
        int idx = 0;

        int nCol = 8;
        
       intestation = new String[nCol];
       
        intestation[idx++] = l.getString("parameters", "date");
        intestation[idx++] = l.getString("parameters", "user");
        intestation[idx++] = l.getString("parameters", "type");
        intestation[idx++] = l.getString("parameters", "device");
        intestation[idx++] = l.getString("parameters", "variable");
        intestation[idx++] = l.getString("parameters", "startingvalue");
        intestation[idx++] = l.getString("parameters", "endingvalue");
        intestation[idx++] = l.getString("parameters", "rollback");

        return intestation;
    }
    
    public int size()
    {
        return parametersEventsList.length;
    }

    public ParametersEvent getByPosition(int pos)
    {
        return parametersEventsList[pos];
    }

    public ParametersEvent[] getParametersEventsList()
    {
        return this.parametersEventsList;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }
    public void setScreenH(int a) {
    	this.screenh = a;
    }
    
    public void setScreenW(int a) {
    	this.screenw = a;
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
}
