package com.carel.supervisor.presentation.bean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.HsPrintBean;
import com.carel.supervisor.dataaccess.datalog.impl.HsReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class ReportBeanListPres extends ReportBeanList
{
	private static final String HACCP = "haccp";
	private static final String HISTORY = "history";
	
	private static int screenw = 1024;
	private static int screenh = 768;
	private int width = 0;
    private int height = 0;
    
	private HsPrintBean[] prints;
	private HsReportBean[] reports;
	private int pageNumber = 1;
    private int pageTotal = 1;
	
    public ReportBeanListPres()
    {
        super(); 
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
    
    public static String getHTMLReportTable(int idsite, String language, String title)
    {
    	Map<Integer, ReportBean> reports=null;
		try {
			reports = retrieveReport(idsite);
		} catch (DataBaseException e) {
			
			e.printStackTrace();
		}
    
        LangService lang = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[reports.size()];

        HTMLElement[][] dati = new HTMLElement[reports.size()][];

        String s_now = lang.getString("report", "now");
        String s_daily = lang.getString("report", "daily");
        String s_weekly = lang.getString("report", "weekly");
        String s_custom = lang.getString("report", "custom");
        String description_type = "";
       
 
        // order by code
        String[] descr = new String[reports.keySet().size()];
        ReportBean r = null;
        Map<String,Integer> ord_map = new HashMap<String,Integer>();
        int i = 0;
        for (Iterator<Integer> itr = reports.keySet().iterator(); itr.hasNext();i++)
        {
        	r = reports.get(itr.next());
        	descr[i] = r.getCode();
        	ord_map.put(r.getCode(), r.getIdreport());
        }
        Arrays.sort(descr);
    
        for (i=0;i<descr.length;i++) {
        	ReportBean report = reports.get(ord_map.get(descr[i]));
            dati[i] = new HTMLElement[5];
            dati[i][0] = new HTMLSimpleElement("<div class='tableTouchCellImg'><input type=radio class='bigRadio' id='rdio"+report.getIdreport()+"'></div>");
            dati[i][1] = new HTMLSimpleElement("<div class='tableTouchCell'>"+report.getCode()+"</div>");

            switch(report.getTimelength()){
	            case 0:
	                description_type = s_now;
	                break;
	            case -1:
	                description_type = s_custom;
	                break;
	            case 86400:
	            	description_type = s_daily;
	            	break;
				case 604800:
					 description_type = s_weekly;
	            	break;
            }           

            StringBuffer timeTableValue = new StringBuffer();
            if( report.getStep() == -1 ) {
				Integer timeValues[] = report.getTimeValues();
				for(int t=0; t < timeValues.length; t++) {
					if( t > 0 )
						timeTableValue.append(";");
					timeTableValue.append(timeValues[t].toString());
				}
            }
            
            dati[i][2] = new HTMLSimpleElement("<div class='tableTouchCell'>"+description_type+"</div>"
            	+ " <input style='display:none;visibility:block' id='type_"+report.getIdreport()+"' value='"+report.getTimelength()+"' />"
            	+ "<input style='display:none;visibility:block' id='freq_"+report.getIdreport()+"' value='"+report.getStep()+"' />"
            	+ "<input style='display:none;visibility:block' id='code_"+report.getCode()+"' />"
            	+ "<input style='display:none;visibility:block' id='otype_"+report.getIdreport()+"' value='"+report.getOutputtype()+"' />"
            	+ "<input style='display:none;visibility:block' id='codebyid_"+report.getIdreport()+"' value='"+report.getCode()+"'/>"
            	+ (timeTableValue.length() > 0 ? ("<input style='display:none;visibility:block' id='time_"+report.getIdreport()+"' value='"+timeTableValue.toString()+"' />") : ""));
            dati[i][3] = new HTMLSimpleElement("<div class='tableTouchCell'>"+report.getOutputtype()+"</div>");
            dati[i][4] = new HTMLSimpleElement("<div class='tableTouchCell'>"+String.valueOf(report.getVariables().length)+"</div>");
            

            ClickRowFunction[i] = String.valueOf(report.getIdreport());
        }

        //    	header tabella
        String[] headerTable = new String[5];
        headerTable[0] = "";
        headerTable[1] = lang.getString("report", "description");
        headerTable[2] = lang.getString("report", "type");
        headerTable[3] = lang.getString("report", "output");
        headerTable[4] = lang.getString("report", "numvars"); //aggiungere su db
        

        HTMLTable table = new HTMLTable("reportTable", headerTable, dati);
        table.setSgClickRowAction("selectedLineReport('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);
        table.setDbClickRowAction("modifyReport('$1')");
        table.setDlbClickRowFunction(ClickRowFunction);
        table.setAlignType(new int[] {1, 0, 1, 1, 1 });
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 43);
        table.setColumnSize(1, 400);
        table.setColumnSize(2, 140);
        table.setColumnSize(3, 80);
        table.setColumnSize(4, 80);
        table.setRowHeight(25);
        table.setWidth(880);
        table.setHeight(148);
       
        String htmlTable = table.getHTMLText();

        return htmlTable;
    }
    
    //End Ing. Gilioli
    
    
    
    
    public static String getHTMLReportTable(String type, int idsite, String language, String title, int width, int height)
    {
    	Map<Integer, ReportBean> reports = null;
    	try{
			if(HACCP.equalsIgnoreCase(type)){
				reports = retrieveHaccpReport(idsite);
			}else if (HISTORY.equalsIgnoreCase(type)){
				reports = retrieveHistoricalReport(idsite);
			}else{
				return "";
			}
    	}catch(Exception e){
    		return "";
    	}
        LangService lang = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[reports.size()];

        HTMLElement[][] dati = new HTMLElement[reports.size()][];

        String s_now = lang.getString("reportconf", "now");
        String s_daily = lang.getString("reportconf", "daily");
        String s_weekly = lang.getString("reportconf", "weekly");
        String description_type = "";

        int i=0;
        for (Iterator<ReportBean> itr = reports.values().iterator(); itr.hasNext();) {
            ReportBean report = itr.next();
            dati[i++] = new HTMLElement[3];
            dati[i++][0] = new HTMLSimpleElement(report.getCode());

            if (report.getOutputtype().equals("N"))
            {
                description_type = s_now;
            }
            else if (report.getOutputtype().equals("D"))
            {
                description_type = s_daily;
            }
            else if (report.getOutputtype().equals("W"))
            {
                description_type = s_weekly;
            }

            dati[i][1] = new HTMLSimpleElement(description_type);
            dati[i][2] = new HTMLSimpleElement(String.valueOf(report.getVariables().length));

            ClickRowFunction[i] = String.valueOf(report.getIdreport());
        }

        //    	header tabella
        String[] headerTable = new String[3];
        headerTable[0] = lang.getString("reportconf", "description");
        headerTable[1] = lang.getString("reportconf", "type");
        headerTable[2] = lang.getString("reportconf", "numvars"); //aggiungere su db

        HTMLTable table = new HTMLTable("report", headerTable, dati);
        table.setSgClickRowAction("selectedLineReport('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);
        table.setDbClickRowAction("modifyReport('$1')");
        table.setDlbClickRowFunction(ClickRowFunction);
        table.setAlignType(new int[] { 0, 1, 1 });
        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setColumnSize(0, 443);
        table.setColumnSize(1, 200);
        table.setColumnSize(2, 200);
        table.setRowHeight(18);
        table.setWidth(width);
        table.setHeight(height);

        String htmlTable = table.getHTMLText();

        return htmlTable;
    }
    
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }
    
    public String getHTMLHSReportTable(int idSite, String language,String title)
    {
    	return getHTMLHSReportTablePrv(idSite,language,title).getHTMLText();
    }
    public String getHTMLHSReportTableRefresh(int idSite, String language,String title)
    {
    	return getHTMLHSReportTablePrv(idSite,language,title).getHTMLTextBufferRefresh().toString();
    }
    public HTMLTable getHTMLHSReportTablePrv(int idsite,String language, String title)
    {
    	LangService lang = LangMgr.getInstance().getLangService(language);
    	HsReportBean report = null;
    	
    	int size = 0;
    	if(reports != null)
    		size = reports.length;
    	
        String[] ClickRowFunction = new String[size];

        HTMLElement[][] dati = new HTMLElement[size][6];

        String s_now = lang.getString("reportconf", "now");
        String s_daily = lang.getString("reportconf", "daily");
        String s_weekly = lang.getString("reportconf", "weekly");
        String s_custom = lang.getString("report", "custom");

        for (int i = 0; i < size; i++)
        {
        	report = reports[i];
            
        	// COLUMN 0 :  RADIOBUTTON
        	dati[i][0] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCellImg'><input type='checkbox' name='rdio' onclick='chkClick(this)' id='chk").append(buildParameters(report.getPath(),report)).append("'></div>").toString());
        	
        	// COLUMN 1 :  DESCRIPTION
            dati[i][1] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(report.getCode()).append("</div>").toString());
            // Timestamp from = new Timestamp(report.getCreation().getTime()-report.getStep());
            
            // COLUMN 2 & 3: FROM & TO
            if(report.getStep()==0) {
            	dati[i][2] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(DateUtils.date2String(report.getCreation(),"yyyy/MM/dd HH:mm")).append("</div>").toString());
	            dati[i][3] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append("</div>").toString());
            } else {
                dati[i][2] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(DateUtils.date2String(report.getFrom(),"yyyy/MM/dd HH:mm")).append("</div>").toString());
                dati[i][3] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(DateUtils.date2String(report.getTo(),"yyyy/MM/dd HH:mm")).append("</div>").toString());
            }
            
         
            // COLUMN 4: ISHACCP
            if(report.getIshaccp().equalsIgnoreCase("TRUE"))
            	dati[i][4] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append("HACCP").append("</div>").toString());
            else
            	dati[i][4] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(lang.getString("hsreport", "storico")).append("</div>").toString());
            
            /*
            // COLUMN 5: TYPE
            
            int step = report.getStep();
            switch(step)
            {
            case 0:
            	dati[i][4] = new HTMLSimpleElement(s_now);
                break;
            case -1:
            	dati[i][4] = new HTMLSimpleElement(s_custom);
                break;
            case 86400:
            	dati[i][4] = new HTMLSimpleElement(s_daily);
            	break;
			case 604800:
				dati[i][4] = new HTMLSimpleElement(s_weekly);
            	break;
            }           */

            
            String rpt = report.getPath();
            String temp="";
            if("html".equalsIgnoreCase(rpt.substring(rpt.length()-4))|| "htm".equalsIgnoreCase(rpt.substring(rpt.length()-3))){
                dati[i][5] = new HTMLSimpleElement("<div class='tableTouchCellImg'><img src=\'images/actions/html_img.png\'></div>");
                temp=String.valueOf(report.getIdhs())+"|html|"+report.getPath();
            }else if("pdf".equalsIgnoreCase(rpt.substring(rpt.length()-3))){
                dati[i][5] = new HTMLSimpleElement("<div class='tableTouchCellImg'><img src=\'images/actions/pdf2_on.png\'></div>");
                temp=String.valueOf(report.getIdhs())+"|pdf|"+report.getPath();
            }else if("csv".equalsIgnoreCase(rpt.substring(rpt.length()-3))){
                dati[i][5] = new HTMLSimpleElement("<div class='tableTouchCellImg'><img src=\'images/actions/csv_img.png\'></div>");
                temp=String.valueOf(report.getIdhs())+"|csv|"+report.getPath();
            }
            ClickRowFunction[i] =temp;
        }

        String[] headerTable = new String[6];
        headerTable[1] = lang.getString("hsreport", "col0");
        headerTable[2] = lang.getString("hsreport", "col1");
        headerTable[3] = lang.getString("hsreport", "col2");
        headerTable[4] = lang.getString("hsreport", "col3");
        //headerTable[4] = lang.getString("hsreport", "col5");
        headerTable[5] = "";
        headerTable[0] = "";

        HTMLTable table = new HTMLTable("hsreport",headerTable,dati);
        table.setPage(true);
        table.setPageNumber(pageNumber);
        table.setPageTotal(pageTotal);
        table.setTableTitle(lang.getString("hsreport", "tablename"));
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(width);
        table.setHeight(height);
        table.setColumnSize(1,385);
        table.setColumnSize(2,125);
        table.setColumnSize(3,125);
        table.setColumnSize(4,80);
        table.setColumnSize(5,45);
        //table.setColumnSize(5,35);
        table.setAlignType(0, HTMLTable.CENTER);
        table.setAlignType(2, 1);
        table.setAlignType(3, 1);
        table.setAlignType(4, 1);
        table.setAlignType(5, 1);
        //table.setAlignType(5, 1);
        table.setRowHeight(25);
        
        table.setSgClickRowAction("selectHSRow('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);

        return table;
    }
    private String buildParameters(String name,HsReportBean report)
    {
    	if("html".equalsIgnoreCase(name.substring(name.length()-4))|| "htm".equalsIgnoreCase(name.substring(name.length()-3))){
    		return String.valueOf(report.getIdhs())+"|html|"+report.getPath();
        }else if("pdf".equalsIgnoreCase(name.substring(name.length()-3))){
            return String.valueOf(report.getIdhs())+"|pdf|"+report.getPath();
        }else if("csv".equalsIgnoreCase(name.substring(name.length()-3))){
            return String.valueOf(report.getIdhs())+"|csv|"+report.getPath();
        }
    	return "";
    }
    //add by kevin. Init reports list by page number
    public void loadFromDataBaseReport(UserSession userSession, int numPage)
    {
        try
        {
        	String filter = userSession.getCurrentUserTransaction().getProperty("filter");
        	reports = ReportBeanList.loadReportHistory(userSession.getIdSite(),filter, numPage);
            pageTotal = ReportBeanList.getTotalPage();
            pageNumber = numPage;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    //add by kevin. Init reports list by page number
    public void loadFromDataBase(UserSession userSession, int numPage)
    {
        try
        {
        	prints = ReportBeanList.loadPrintHistory(userSession.getIdSite(), numPage);
            pageTotal = ReportBeanList.getTotalPage();
            pageNumber = numPage;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    private HTMLTable getHTMLHSPrintTablePrv(int idsite,String language, String title)
    {
    	LangService lang = LangMgr.getInstance().getLangService(language);
    	
    	HsPrintBean print = null;
    	
    	int size = 0;
    	if(prints != null)
    		size = prints.length;
    	
        String[] ClickRowFunction = new String[size];

        HTMLElement[][] dati = new HTMLElement[size][4];

        for (int i = 0; i < size; i++)
        {
        	print = prints[i];
                    	
        	dati[i][0] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCellImg'>").append("<input type='checkbox' onclick='chkClick(this)' id='chk"+print.getId()+"' value='"+print.getPath()+"'>").append("</div>").toString());
        	dati[i][1] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(DateUtils.date2String(print.getData(),"yyyy/MM/dd HH:mm:ss")).append("</div>").toString());
            dati[i][2] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(print.getActioncode()).append("</div>").toString());
            dati[i][3] = new HTMLSimpleElement(new StringBuffer().append("<div class='tableTouchCell'>").append(print.getPath()).append("</div>").toString());
            
            ClickRowFunction[i] = String.valueOf(print.getId());
        }

        String[] headerTable = new String[4];
        headerTable[0] = "";
        headerTable[1] = lang.getString("hsprint","col0");
        headerTable[2] = lang.getString("hsprint","col1");
        headerTable[3] = lang.getString("hsprint","col2");

        HTMLTable table = new HTMLTable("hstableprint",headerTable,dati);
        table.setPage(true);
        table.setPageNumber(pageNumber);
        table.setPageTotal(pageTotal);
        table.setTableTitle(lang.getString("hsprint","tablename"));
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(width);
        table.setHeight(height);
        table.setColumnSize(0,30);
        table.setColumnSize(1,150);
        table.setColumnSize(2,300);
        table.setColumnSize(3,300);
        table.setAlignType(0, HTMLTable.CENTER);
        table.setAlignType(3, HTMLTable.CENTER);
        table.setRowHeight(25);
        
        table.setSgClickRowAction("hsSelectRow('$1')");
        table.setSnglClickRowFunction(ClickRowFunction);

        return table;
    }
    public String getHTMLHSPrintTable(int idSite, String language,String title)
    {
    	return getHTMLHSPrintTablePrv(idSite,language,title).getHTMLText();
    }
    public String getHTMLHSPrintTableRefresh(int idSite, String language,String title)
    {
    	return getHTMLHSPrintTablePrv(idSite,language,title).getHTMLTextBufferRefresh().toString();
    }
}
