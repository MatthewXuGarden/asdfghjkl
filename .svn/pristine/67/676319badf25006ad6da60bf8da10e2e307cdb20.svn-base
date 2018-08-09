package com.carel.supervisor.presentation.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.polling.ThreadController;
import com.carel.supervisor.presentation.polling.TimeInfo;
import com.carel.supervisor.presentation.polling.TimeInfoList;
import com.carel.supervisor.presentation.polling.TimeRow;
import com.carel.supervisor.presentation.polling.TimeSite;
import com.carel.supervisor.presentation.polling.TimeSiteList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class BTimeTable extends BoMaster
{
    private static final int REFRESH_TIME = 10000;
    private static int screenw = 1024;
    private static int screenh = 768;
    
    public BTimeTable(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "initialize();enableAction(1);disableAction(2);resizeTableTab();");
        p.put("tab2name", "enableAction(1);disableAction(2);resizeTableTabConfiguration();");
        
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "polling.js");
        p.put("tab2name", "rsitetime.js");
        
        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    	//UserTransaction ut = us.getCurrentUserTransaction();
    	
    	String cmd = prop.getProperty("cmd");
    if(tabName.equalsIgnoreCase("tab1name")){	
        if(cmd.equalsIgnoreCase("save")){
        	TimeInfo timeinfo = new TimeInfo();
        	timeinfo.setName(prop.getProperty("desc"));
        	int idline = timeinfo.save();
        	int nmaxRighe = Integer.parseInt(prop.getProperty("maxRigheTime"));
        	TimeRow timerow = null;       	
        	for(int i=1; i <= nmaxRighe; i++){
        	 if(can_save_row(prop,i)){	
        		timerow = new TimeRow();
        		timerow.setIdrmtimetable(idline);
        		timerow.setHour_from(Integer.parseInt(prop.getProperty("hourfrom_"+i)));
        		timerow.setMinute_from(Integer.parseInt(prop.getProperty("minutfrom_"+i)));
        		timerow.setHour_to(Integer.parseInt(prop.getProperty("hour_"+i)));
        		timerow.setMinute_to(Integer.parseInt(prop.getProperty("minutes_"+i)));
        		timerow.setDelta(Integer.parseInt(prop.getProperty("range")));
        		timerow.setNslot(i);
        		timerow.save();
        	  }
        	}
        
        EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL05", new Object[]{us.getUserName(),prop.getProperty("desc"),});
        
        }
        if(cmd.equalsIgnoreCase("remove")){  //rimozione fascia oraria
        	int idrmtimetable = Integer.parseInt(prop.getProperty("removeFascia"));
        	String nomeFascia = delete_fascia_oraria(idrmtimetable);
        	EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL06", new Object[]{us.getUserName(),nomeFascia});
        }
      }
      if(cmd.equalsIgnoreCase("save_sitetime")){ //Save su tabella rmtimesite 
    	  TimeSite timeSite = new TimeSite();
		  timeSite.setIdrmtimetable(Integer.parseInt(prop.getProperty("fascie")));
		  timeSite.setIdsite(Integer.parseInt(prop.getProperty("site")));
		  if(prop.getProperty("abilita") != null)
			  timeSite.setStatus("true");
		  else
			  timeSite.setStatus("false");
		  timeSite.save();
		  //Loggo inserimento associazione sito-fascia oraria
		  String sql = "select rmtimetable.name as nf,cfsite.name as ns from rmtimesite inner join rmtimetable on rmtimetable.idrmtimetable=rmtimesite.idrmtimetable inner join cfsite on cfsite.idsite=rmtimesite.idsite  where rmtimesite.idrmtimetable = ? and rmtimesite.idsite= ? ";
	      RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(timeSite.getIdrmtimetable()),new Integer(timeSite.getIdsite())});
	      String nomeFascia = (String) recordSet.get(0).get("nf");
	      String nomeSito = (String) recordSet.get(0).get("ns");
	      EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                  "POL08", new Object[]{us.getUserName(),nomeSito,nomeFascia});
      }
     if(cmd.equalsIgnoreCase("remove_timesite")){  //rimozione sito fascia oraria
    		int id = Integer.parseInt(prop.getProperty("id"));
    	//Loggo inserimento associazione sito-fascia oraria
  		  String sql = "select rmtimetable.name as nf,cfsite.name as ns from rmtimesite inner join rmtimetable on rmtimetable.idrmtimetable=rmtimesite.idrmtimetable inner join cfsite on cfsite.idsite=rmtimesite.idsite  where rmtimesite.id = ? ";
  	      RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(id)});
  	      String nomeFascia = (String) recordSet.get(0).get("nf");
  	      String nomeSito = (String) recordSet.get(0).get("ns");
  	      EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL09", new Object[]{us.getUserName(),nomeSito,nomeFascia});
    		delete_rmpolling_idsite(id);	//rimozione da rmpollingstatus idsite corrispondente perchè il motore deve ripartire
    		delete_sitefascia_oraria(id);	
     }
      if(cmd.equalsIgnoreCase("set")){ //Per update fascie richiamato dal bottone V
    		int id = Integer.parseInt(prop.getProperty("removeFascia"));
    		TimeInfo timeinfo = new TimeInfo();
        	timeinfo.setName(prop.getProperty("desc"));
        	timeinfo.setIdrmtimetable(id);
        	timeinfo.update();
        	//Loggo la modifica di una fascia oraria
        	EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL07", new Object[]{us.getUserName(), prop.getProperty("desc")});
        	
        	//delete_rmpollingstatus_time(id); //rimozione da pollingstatus di idsite perchè devo riavviare il motore a seguito di modifiche effettuate
        	delete_time(id);
        	int nmaxRighe = Integer.parseInt(prop.getProperty("maxRigheTime"));
        	TimeRow timerow = null;       	
        	for(int i=1; i <= nmaxRighe; i++){
        	 if(can_save_row(prop,i)){	
        		timerow = new TimeRow();
        		timerow.setIdrmtimetable(id);
        		timerow.setHour_from(Integer.parseInt(prop.getProperty("hourfrom_"+i)));
        		timerow.setMinute_from(Integer.parseInt(prop.getProperty("minutfrom_"+i)));
        		timerow.setHour_to(Integer.parseInt(prop.getProperty("hour_"+i)));
        		timerow.setMinute_to(Integer.parseInt(prop.getProperty("minutes_"+i)));
        		timerow.setDelta(Integer.parseInt(prop.getProperty("range")));
        		timerow.setNslot(i);
        		timerow.save();
        	  }
        	}
      }
      if(cmd.equalsIgnoreCase("setSite")){
    	  TimeSite timesite = new TimeSite();
    	  timesite.setId(Integer.parseInt(prop.getProperty("id")));
    	  timesite.setIdrmtimetable(Integer.parseInt(prop.getProperty("fascie")));
    	  timesite.setIdsite(Integer.parseInt(prop.getProperty("site")));
    	  if(prop.getProperty("abilita") != null)
    		  timesite.setStatus("true");
    	  else
    		  timesite.setStatus("false");
    	  timesite.update();
    	  timesite.delete_rmpollingstatus(); //Devo togliere il sito da rmpollingstatus a seguito di modifiche
    	  //Loggo modifica associazione sito-fascia oraria
  		  String sql = "select rmtimetable.name as nf,cfsite.name as ns from rmtimesite inner join rmtimetable on rmtimetable.idrmtimetable=rmtimesite.idrmtimetable inner join cfsite on cfsite.idsite=rmtimesite.idsite  where rmtimesite.id = ? ";
  	      RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(timesite.getId())});
  	      String nomeFascia = (String) recordSet.get(0).get("nf");
  	      String nomeSito = (String) recordSet.get(0).get("ns");
  	      EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL10", new Object[]{us.getUserName(),nomeSito,nomeFascia});
      }
      
      if(!cmd.equalsIgnoreCase("save") && !cmd.equalsIgnoreCase("remove"))
    	  restartEngine(us);
    }
    
    private boolean can_save_row(Properties prop, int indice){
    	if(prop.getProperty("hour_"+indice).equals("") ||
    	   prop.getProperty("minutes_"+indice).equals("")  
    	)
    		return false;
    	else
    		return true;
    }
    
    
//Richiamata da Ajax    
    public String executeDataAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
//    	 TODO Auto-generated method stub
		String toReturn = "";
		if ("tab1name".equals(tabName.trim()))
		{
			if ("set".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				Object[] values = new Object[1];
				values[0] = new Integer(prop.getProperty("idrmtimetable"));  //idrmtimetable della tabella rmtimetable
				String sql = "select name,hour_from,minute_from,hour_to,minute_to,delta from rmtimetable inner join rmtime on rmtimetable.idrmtimetable = rmtime.idrmtimetable where rmtimetable.idrmtimetable = ? order by nslot";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);
				toReturn = "<timetable>";
				for (int i = 0; i < rs.size(); i++)
		        {
					toReturn += "<attrib id='" + TimeInfo.getNAME()       + "'>" + rs.get(i).get("name").toString() + "</attrib>";
					toReturn += "<attrib id='" + TimeRow.getHOUR_FROM()   + "'>" + rs.get(i).get("hour_from").toString() + "</attrib>";
					toReturn += "<attrib id='" + TimeRow.getMINUTE_FROM() + "'>" + rs.get(i).get("minute_from").toString() + "</attrib>";
					toReturn += "<attrib id='" + TimeRow.getHOUR_TO()     + "'>" + rs.get(i).get("hour_to").toString() + "</attrib>";
					toReturn += "<attrib id='" + TimeRow.getMINUTE_TO()   + "'>" + rs.get(i).get("minute_to").toString() + "</attrib>";
					toReturn += "<attrib id='" + TimeRow.getDELTA()       + "'>" + rs.get(i).get("delta").toString() + "</attrib>";
					
		        }
				toReturn += "</timetable>";
				//System.out.println("<----XML---->");
				//System.out.println(toReturn);
			}
		}
		if ("setsite".equalsIgnoreCase(prop.getProperty("cmd")))
		{
			Object[] values = new Object[1];
			values[0] = new Integer(prop.getProperty("id"));  //id della tabella rmtimesite
			String sql = "select * from rmtimesite where id = ? ";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);
			toReturn = "<timesitetable>";
			for (int i = 0; i < rs.size(); i++)
	        {
				toReturn += "<attrib id='" + TimeSite.getIDSITE()        + "'>" + rs.get(i).get("idsite").toString() + "</attrib>";
				toReturn += "<attrib id='" + TimeSite.getIDRMTIMETABLE() + "'>" + rs.get(i).get("idrmtimetable").toString() + "</attrib>";
				toReturn += "<attrib id='" + TimeSite.getSTATUS()        + "'>" + rs.get(i).get("status").toString() + "</attrib>";
	        }
			toReturn += "</timesitetable>";
			//System.out.println("<----XML2---->");
			//System.out.println(toReturn);
		}
		
		if("isRemovable".equalsIgnoreCase(prop.getProperty("cmd"))){
			Object[] values = new Object[1];
			values[0] = new Integer(prop.getProperty("idrmtimetable"));
			String sql = "select * from rmtimesite where idrmtimetable = ? ";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);
			toReturn = "<timesitetable>";
			if(rs.size() > 0)
				toReturn += "<attrib id='esito'>KO</attrib>";
			else
				toReturn += "<attrib id='esito'>OK</attrib>";
			toReturn += "</timesitetable>";
			//System.out.println("<--ISREMOCVABLE-->");
			//System.out.println(toReturn);
		}
			
		return toReturn;
    }
    
    public TimeInfo[] getTimeTableList()
    {
        TimeInfo[] timeinfo = null;

        try
        {
        	timeinfo = TimeInfoList.retriveTimeTable();
        }
        catch (Exception e)
        {
        }

        return timeinfo;
    }
    
    public TimeInfo[] getTimeTableListJoinTime()
    {
        TimeInfo[] timeinfo = null;

        try
        {
        	timeinfo = TimeInfoList.retriveTimeTableJoin();
        }
        catch (Exception e)
        {
        }

        return timeinfo;
    }
    
    public String getHTMLTable(String tabName, String language, String title, int height, int width)
	{
    	if ("tab1name".equals(tabName.trim()))
		{
			//TimeInfo [] timerInfo = getTimeTableList();
    		TimeInfo [] timerInfo = getTimeTableListJoinTime();
			
			
			int rows = timerInfo.length;

			LangService lan = LangMgr.getInstance().getLangService(language);
			String[] ClickRowFunction = new String[rows];
			String[] DBLClickRowFunction = new String[rows];
			HTMLElement[][] data = new HTMLElement[rows][];
			
			String intervalli=null;
			String separatore=" - ";
			for (int i = 0; i < rows; i++)
			{
				data[i] = new HTMLElement[2];
				//data[i][0] = new HTMLSimpleElement(String.valueOf(timerInfo[i].getIdrmtimetable()));
				data[i][0] = new HTMLSimpleElement(timerInfo[i].getName());
				intervalli = timerInfo[i].getFrom_time()+separatore+timerInfo[i].getTo_time()+" / ";
		    		for(int g =0; g < (timerInfo[i]).getSlot().size()-1; g+=2){
		    			intervalli += ((String)(timerInfo[i]).getSlot().get(g))+separatore+((timerInfo[i]).getSlot().get(g+1))+" / ";
		    	}
		    	intervalli = intervalli.substring(0,intervalli.length()-3);
		    	data[i][1] = new HTMLSimpleElement(intervalli);
				ClickRowFunction[i] = String.valueOf(timerInfo[i].getIdrmtimetable());
				DBLClickRowFunction[i] = String.valueOf(timerInfo[i].getIdrmtimetable());
			}
			
			// header tabella
			String[] headerTable = new String[2];
			//headerTable[0] = lan.getString("timetable", "indice");
			headerTable[0] = lan.getString("timetable", "timetable_name");
			headerTable[1] = lan.getString("timetable", "slots");
			
			HTMLTable table = new HTMLTable("fasce", headerTable, data);
			
			table.setScreenH(screenh);
			table.setScreenW(screenw);
			table.setColumnSize(0, 440);
			table.setColumnSize(1, 420);
			table.setDbClickRowAction("setselectedLine('$1');");
	        table.setDlbClickRowFunction(DBLClickRowFunction);
		
			table.setSgClickRowAction("selectedLine('$1');");
			table.setSnglClickRowFunction(ClickRowFunction);
			table.setWidth(width);
			table.setHeight(height);
			
			return table.getHTMLText();
		}
    	if ("tab2name".equals(tabName.trim()))
		{
    		TimeSite [] timerInfo = getTimeTableListJoin();
			int rows = timerInfo.length;

			LangService lan = LangMgr.getInstance().getLangService(language);
			String[] ClickRowFunction = new String[rows];
			String[] DBLClickRowFunction = new String[rows];
			HTMLElement[][] data = new HTMLElement[rows][];

			for (int i = 0; i < rows; i++)
			{
				data[i] = new HTMLElement[3];
				//data[i][0] = new HTMLSimpleElement(timerInfo[i].getValue(TimeSite.getID()));
				if(timerInfo[i].getValue(TimeSite.getSTATUS()).equalsIgnoreCase("true"))
					data[i][0] = new HTMLSimpleElement("<div align='center'><img src='images/true.gif'/></div>");
				else
					data[i][0] = new HTMLSimpleElement("<div align='center'>&nbsp;</div>");
				data[i][1] = new HTMLSimpleElement(timerInfo[i].getValue(TimeSite.getNAMES()));
				data[i][2] = new HTMLSimpleElement(timerInfo[i].getValue(TimeSite.getNAMEF()));
				//data[i][3] = new HTMLSimpleElement(timerInfo[i].getValue(TimeSite.getSTATUS()));
				
				ClickRowFunction[i] = timerInfo[i].getValue(TimeSite.getID());
				DBLClickRowFunction[i] = timerInfo[i].getValue(TimeSite.getID());
			}

			// header tabella
			String[] headerTable = new String[3];
			//headerTable[0] = lan.getString("timetable", "indice");
			headerTable[0] = lan.getString("r_siteaccess", "timesiteenable");
			headerTable[1] = lan.getString("r_siteaccess", "timesite");
			headerTable[2] = lan.getString("timetable", "timetable_name");
			
			HTMLTable table = new HTMLTable("fasceSite", headerTable, data);
			
			table.setScreenH(screenh);
			table.setScreenW(screenw);
			//table.setColumnSize(0, 150);
			table.setColumnSize(0, 283);
			table.setColumnSize(1, 283);
			table.setColumnSize(2, 283);
			
			table.setDbClickRowAction("selectedLine('$1');");
	        table.setDlbClickRowFunction(DBLClickRowFunction);
			
			table.setSgClickRowAction("selectedSiteLine('$1');");
			table.setSnglClickRowFunction(ClickRowFunction);
	        table.setWidth(width);
			table.setHeight(height);
			
			//System.out.println(table.getHTMLText());
			
			return table.getHTMLText();
		}
		return "";
	}
    
    //Cose da fare quando Elimino una fascia oraria
    private String delete_fascia_oraria(int idrmtimetable) throws Exception {
    	String sql = "select name from rmtimetable where idrmtimetable = ? ";
    	RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idrmtimetable)});
    	String nomeFascia = (String) recordSet.get(0).get("name");
 
    	sql = "delete from rmtimetable where idrmtimetable = ? ";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(idrmtimetable)});
    	
    	sql = "delete from rmtime where idrmtimetable = ? ";
		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(idrmtimetable)});
    
		return nomeFascia;
    }
    
    private void delete_time(int idrmtimetable) throws Exception {
    	String sql = "select idsite from rmtimesite where idrmtimetable = ? ";
    	RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idrmtimetable)});
    	Record record = null;
    	ArrayList idsite = new ArrayList();
    	for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            idsite.add((Integer)record.get("idsite"));
        }
    	
    	StringBuffer buffIdsite = new StringBuffer();
    	for(int y =0; y < idsite.size(); y++)
    		buffIdsite.append(idsite.get(y)+",");
    	
    	if(buffIdsite.length() > 0){
    		String fineid = buffIdsite.substring(0,buffIdsite.length()-1);
    	
    		Date today = Calendar.getInstance().getTime();
    		String data = DateUtils.date2String(today,"yyyy-MM-dd");
    		//Elimino eventuali siti dalla tabella rmpollingstatus perchè si riparte da zero
    		sql = "delete from rmpollingstatus where idsite in ("+fineid+") and current_row = ? ";
    		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{data});
    	}
    	
    	sql = "delete from rmtime where idrmtimetable = ? ";
		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(idrmtimetable)});
    }
    
    private void delete_sitefascia_oraria(int id) throws Exception {
    	String sql = "delete from rmtimesite where id = ? ";
    	
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(id)});
    }
    
    private void delete_rmpolling_idsite(int id) throws Exception {
    	String sql = "delete from rmpollingstatus where rmpollingstatus.id in ( " +
    			" select rmpollingstatus.id from rmpollingstatus " +
    			" inner join rmtimesite on rmpollingstatus.idsite = rmtimesite.idsite " +
    			" where rmtimesite.idsite = ? and current_row = ? " +
    			" )";
    
    	Date today = Calendar.getInstance().getTime();
    	String data = DateUtils.date2String(today,"yyyy-MM-dd");
    	
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(id),data});
    }
    
    public TimeSite[] getTimeTableListJoin()
    {
    	TimeSite[] timeinfo = null;

        try
        {
        	timeinfo = TimeSiteList.retriveTimeSiteJoin();
        }
        catch (Exception e)
        {
        }

        return timeinfo;
    }
    
    public static String createSelectSite()
    {
      StringBuffer sb = new StringBuffer();	
      try
      {	
    	SiteInfo[] siteInfo = SiteInfoList.retriveRemoteSite();
        
        sb.append("<option value=\"-1\">---------------</option>\n");
        for (int i = 0; i < siteInfo.length; i++)
        {
           sb.append("<option value=\"" + siteInfo[i].getId() + "\">" +
        		   siteInfo[i].getName() + "</option>\n");
        }
       }catch(Exception e){} 
      return sb.toString();
    }
    
    public static String createSelectFascie()
    {
      StringBuffer sb = new StringBuffer();	
      try
      {	
    	TimeInfo[] timeInfo = TimeInfoList.retriveTimeTable();
        
        sb.append("<option value=\"-1\">---------------</option>\n");
        for (int i = 0; i < timeInfo.length; i++)
        {
           sb.append("<option value=\"" + timeInfo[i].getIdrmtimetable() + "\">" +
        		   timeInfo[i].getName() + "</option>\n");
        }
       }catch(Exception e){} 
      return sb.toString();
    }
    
    /**
     * Ad ogni modifica di fascie o associazione fascia-sito devo far ripartire il motore
     * per avere le modifiche in near real-time
     * @param cmd
     */
    private void restartEngine(UserSession us){
     try{	
    	if(ThreadController.getInstance().getThreadStarted().isAlive()){
    		ThreadController.getInstance().getStartedThread().stopStartThread();
    		ThreadController.getInstance().newThread();
    		ThreadController.getInstance().startThread();
    		EventMgr.getInstance().info(new Integer(us.getIdSite()),us.getUserName(), "Polling",
                    "POL11", new Object[]{us.getUserName()});
    	}
     }catch(Exception e){
    	 Logger logger = LoggerMgr.getLogger(this.getClass());
         logger.error(e);
     }
  
    }
    public static void setScreenH(int height) {
    	screenh = height;
    }
    
    public static void setScreenW(int width) {
    	screenw = width;
    }
}