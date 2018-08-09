package com.carel.supervisor.presentation.bean.search;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.presentation.alarmsevents.AlarmEvent;
import com.carel.supervisor.presentation.alarmsevents.AlarmEventList;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.session.UserSession;

public class SearchAlarmEvent {
	private final static int NUM_ALEV_SEARCH_4_PAGE = 50;
	private final static int ROW_LIMIT = 5000;
    private String title = "";
    private int pageNumber = 1;
    private int pageTotal=1;
    
	public SearchAlarmEvent() {
	}

	public AlarmEventList find(UserSession userSession, int numPage,boolean showAlarm, boolean showEvent, boolean showParams) throws DataBaseException, Exception {
		GroupListBean groups = userSession.getGroup();
		String lang = userSession.getLanguage();
		
		String optionDate = userSession.getProperty("dataselect");

		if (optionDate == null) {
			optionDate = "week";
		}

		String datefrom = userSession.getProperty("datefrom");
		String dateto = userSession.getProperty("dateto");

		String group = userSession.getProperty("selectedGroup");

		if ((group == null) || group.equals("undefined")) {
			group = String.valueOf(userSession.getGroup().getGlobalGroup().getGroupId());
		}

		String device = userSession.getProperty("selectedDevice");

		if ((device == null) || device.equals("undefined")) {
			device = "";
		}

		String category = userSession.getProperty("EScategory"); 

		if (category == null) {
			category = "";
		}
		
		String alarm  =   userSession.getProperty("selectedAlarm");	
		if (alarm == null) alarm = userSession.getProperty("alarmSel");
		
		String userack =  userSession.getProperty("userack");
		String userdel =  userSession.getProperty("userdel");
		String userres =  userSession.getProperty("userres");
		
		String priority = userSession.getProperty("priority");
		if (priority == null) priority = userSession.getProperty("prioritySel");
        
        if (priority == null)
        {
        	priority = "";
        }
		
        Timestamp dstart = null;
        Timestamp dend = null;
        
        // DATE OPTION MANAGEMENT
        
        Timestamp now = new Timestamp(System.currentTimeMillis());

		String tmp = DateUtils.date2String(now, "dd/MM/yyyy HH:mm:ss");
        String[] today = tmp.split("/");
        String year = today[2].split(" ")[0];
        String times = today[2].split(" ")[1];
        String[] time = times.split(":");
        GregorianCalendar thismoment = new GregorianCalendar(
        		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]));
        if (optionDate.equals("xh")) //Last X hours
        {
        	thismoment = new GregorianCalendar(
            		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        	int lastxhours = Integer.parseInt(userSession.getProperty("xhval"));
        	
        	thismoment.add(GregorianCalendar.HOUR_OF_DAY, -lastxhours); //sottraggo x ore

            Timestamp lastxh = new Timestamp(thismoment.getTimeInMillis());
            dstart = lastxh;
            dend = now;
            
        }
        
        if (optionDate.equals("24h")) //last 24 hours
        {
        	thismoment = new GregorianCalendar(
            		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        	thismoment.add(GregorianCalendar.DAY_OF_MONTH, -1); //sottraggo 1 giorno

            Timestamp last24h = new Timestamp(thismoment.getTimeInMillis());
            dstart = last24h;
            dend = now;
        }
		
		if (optionDate.equals("week")) // ULTIMA SETTIMANA
		{
			thismoment.add(5, -7); // sottraggo 7 giorni field 5 = day

			Timestamp lastweek = new Timestamp(thismoment.getTimeInMillis());
			dstart = lastweek;
            dend = now;
		}

		else if (optionDate.equals("month")) // ULTIMO MESE
		{
			thismoment.add(2, -1); // sottraggo un mese field 2 = month

			Timestamp lastmonth = new Timestamp(thismoment.getTimeInMillis());

			dstart = lastmonth;
            dend = now;
		}

		else if (optionDate.equals("3month")) // ULTIMI 3 MESI
		{
			thismoment.add(2, -3); // sottraggo 3 mesi field 2 = month

			Timestamp last3month = new Timestamp(thismoment.getTimeInMillis());

			dstart = last3month;
            dend = now;
		}

		else if (optionDate.equals("fromto")) // FORM X TO Y
		{
			String[] from = datefrom.split("/");
			Timestamp dFrom = new Timestamp(new GregorianCalendar(Integer
					.parseInt(from[2]), Integer.parseInt(from[1]) - 1, Integer
					.parseInt(from[0])).getTimeInMillis());

			String[] to = dateto.split("/");
			Timestamp dTo = new Timestamp(new GregorianCalendar(Integer
					.parseInt(to[2]), Integer.parseInt(to[1]) - 1, Integer
					.parseInt(to[0]), 23, 59, 59).getTimeInMillis());
			dstart = dFrom;
            dend = dTo;
		}
		///  END DATE MANAGEMENT
		
		StringBuffer sql = new StringBuffer("select 'A' as t,idalarm as id,hsalarm.pvcode,hsalarm.idsite,hsalarm.iddevice,hsalarm.idvariable,hsalarm.priority,starttime,endtime, cftableext.description, " +
				"null as type, null as categorycode, null as messagecode, null as userevent, null as parameters from hsalarm, cfvariable, cftableext ");
		
		if (showAlarm){
	        /////////////// PART 1: ALARM QUERY //////////////////////
			sql.append(
//					"select 'A' as t,idalarm as id,pvcode,hsalarm.idsite,iddevice,idvariable,priority,starttime,endtime, cftableext.description, " +
//					"null as type, null as categorycode, null as messagecode, null as userevent, null as parameters from hsalarm, cftableext" +
							 " where cftableext.idsite = "	+ String.valueOf(userSession.getIdSite()) + " "
							+ " and cftableext.tablename='cfvariable' "
							+ " and cftableext.tableid = hsalarm.idvariable "
							+ " and cfvariable.idvariable = hsalarm.idvariable "
							+ " and cftableext.languagecode = '" + lang + "' "
							+ " and hsalarm.idsite = " + String.valueOf(userSession.getIdSite()) + " ");
	
			if (!device.equals("")) // selezionato un device
			{
				sql.append(" and hsalarm.iddevice = " + device + " ");
			} else {
				// nessun device selezionato allora prendo tutti i device del
				// gruppo selezionato (se nessuno,quello globale)
				int groupSelected = 0;
				int[] idsdevices = null;
				groupSelected = Integer.parseInt(group);
				idsdevices = groups.getDeviceStructureList().retrieveIdsByGroupId(groupSelected);
	
				if (!(idsdevices.length == 0)) // se ho zero dispositivi non faccio
												// la query per device
				{
					if (idsdevices.length == 1) // 1 device-> query semplice
					{
						sql.append(" and hsalarm.iddevice = "
								+ Integer.parseInt(String.valueOf(idsdevices[0]))
								+ " ");
					} else // + device query diversa
					{
						sql.append(" and hsalarm.iddevice in (");
	
						for (int i = 0; i < (idsdevices.length - 1); i++) {
							sql.append(""+ Integer.parseInt(String.valueOf(idsdevices[i])) + ", ");
						}
	
						sql.append(""+Integer.parseInt(String.valueOf(idsdevices[idsdevices.length - 1]))+ ") ");
					}
				}
			}
	
		    if (userack!=null && !(userack.trim().length()==0)){
		    	sql.append(" and hsalarm.ackuser = '" + userack + "' ");
		    }
		    if (userdel!=null && !(userdel.trim().length()==0)){
		    	sql.append(" and hsalarm.delactionuser = '" + userdel + "' ");
		    }
		    if (userres!=null && !(userres.trim().length()==0)){
		    	sql.append(" and hsalarm.resetuser = '" + userres + "' ");
		    }	    
		    if (alarm!=null && !(alarm.trim().length()==0)){
		    	if (!device.equals("")) {
		    		sql.append(" and hsalarm.idvariable = " + alarm	+ " ");
			    }else{
			    	sql.append(" and cfvariable.grpcategory = " + alarm	+ " ");
			    }
		    }
		    
			sql.append(" and hsalarm.pvcode = '" + BaseConfig.getPlantId() + "' ");
	
			// date
			sql.append("and hsalarm.lastupdate between '" + dstart+ "' and '" + dend + "' ");
			
			if (!priority.equals("")) //selezionata priorità
	        {
	        	if (priority.equalsIgnoreCase("4"))
	        	   	sql.append(" and cast(hsalarm.priority as INTEGER) >= " + priority + " ");
	        	else
	        	   	sql.append(" and hsalarm.priority = " + priority + " ");
	        }
	        	
		}
		else sql.append("where false ");
		///////////////PARTE 2: QUERY EVENTI   ////////////////////////
		
//		if ( showAlarm && (showEvent || showParams) )
			sql.append("UNION ");
		
		sql.append("select 'E' as t, idevent as id,pvcode,idsite,null,null,null,lastupdate as starttime,null,null, " +
					"type,categorycode,messagecode,userevent,parameters from hsevent ");
			
		if (showEvent)
		{
			sql.append("where idsite = "+String.valueOf(userSession.getIdSite())+" and " +
					" ( categorycode = '"+category+"'  or '' = '"+category+"') ");
	
			// date management
	
	        sql.append("and lastupdate between '"+dstart+"' and '"+dend+"' ");
			
			//aggiunta parametro user
			String user = userSession.getProperty("userevent");
	
	        if (user == null)
	        {
	            user = "";
	        }
			if (!user.equals(""))
			{
			    sql.append(" and LOWER(userevent) = '"+user.toLowerCase()+"' ");
			}
		
		}
		else sql.append("where false ");
		if ( (showAlarm || showEvent) && (showParams&& (category.trim().equals("") || category.trim().equals("Action") || category.trim().equals("parameters") ))) 
			sql.append("UNION ");		
		
		if ((showParams)
			 && (category.trim().equals("") || category.trim().equals("Action") || category.trim().equals("parameters") ))
		{
			///////  PARTE 3: Se Presente, Modifiche dal Campo su Controllo Parametri			
			sql.append("SELECT 'P' as t, parameters_events.id as id, '"+BaseConfig.getPlantId()+"' as pvcode, parameters_events.idsite as idsite,  ");
			sql.append("	cfvariable.iddevice as iddevice, parameters_events.idvariable as idvariable, ");
			sql.append("	null as priority, datetime as starttime, null as endtime, cftableext.description as  description, ");
			sql.append("	null as type, null as categorycode, null as messagecode, username as userevent,	''||cftableext.description||';'||startingvalue||';'||endingvalue as parameters ");
			sql.append("FROM parameters_events, cftableext, cfvariable ");
			sql.append("WHERE username IN (SELECT username FROM parameters_cfmaincfg) ");
			sql.append("and parameters_events.idsite = "+String.valueOf(userSession.getIdSite())+" ");
			sql.append("and parameters_events.eventtype = '"+ParametersMgr.MODIFYDBCODE+"' ");
			sql.append("and parameters_events.datetime between '" + dstart+ "' and '" + dend + "' ");
			sql.append("and cftableext.idsite = "+String.valueOf(userSession.getIdSite())+" ");
			sql.append("and cftableext.tablename='cfvariable' ");
			sql.append("and cftableext.tableid = parameters_events.idvariable ");
			sql.append("and cftableext.languagecode = '"+lang+"' ");
			sql.append("and parameters_events.idsite = cfvariable.idsite ");
			sql.append("and parameters_events.idvariable = cfvariable.idvariable ");
		} 
		

		AlarmEventList elements = new AlarmEventList();
		
		// TOTAL PAGE NUMBER
		StringBuffer sql_count = new StringBuffer("select count (*) from (");
		sql_count.append(sql);
		sql_count.append(" ) as count");
		
		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql_count.toString());
		
		if (recordSet!=null && recordSet.size()>0)
		{
			if (((Integer)recordSet.get(0).get("count"))>ROW_LIMIT)
			{
				userSession.setProperty("limit_search", "ko");
	//			return elements;
			}
			elements.setPageTotal(((Integer)recordSet.get(0).get("count"))/(NUM_ALEV_SEARCH_4_PAGE)+1);
		}
		else
		{
			elements.setPageTotal(1);
		}
		// END TOTAL PAGE NUMBER
		
		sql.append("order by starttime desc ");
		
		if (numPage!=-1)
		{
			sql.append("limit "+NUM_ALEV_SEARCH_4_PAGE+" offset "+NUM_ALEV_SEARCH_4_PAGE*(numPage-1));
		}else{
			sql.append("limit "+ROW_LIMIT);
		}
		
		DeviceStructureList deviceStructureList = userSession.getGroup()
				.getDeviceStructureList();
		DeviceStructure deviceStructure = null;
		
		////////////// MESSAGE CODE FOR EVENTS  //////////////////
		String query = "select * from cfmessage where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });
        
        Record rec = null;

        Map<String,String> message = new HashMap<String,String>();

        for (int i = 0; i < recordSet.size(); i++)
        {
            rec = recordSet.get(i);
            message.put(UtilBean.trim(rec.get("messagecode")),
                UtilBean.trim(rec.get("description")));
        }

        query = "select * from cfcategory where languagecode = ?";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, query,
                new Object[] { userSession.getLanguage() });

        Map<String,String> categ = new HashMap<String,String>();

        for (int i = 0; i < recordSet.size(); i++)
        {
            rec = recordSet.get(i);
            categ.put(UtilBean.trim(rec.get("categorycode")),
                UtilBean.trim(rec.get("description")));
        }
		
		
		//////// END EMSSAGE CODE
        recordSet = DatabaseMgr.getInstance().executeQuery(null,sql.toString());
		
		for (int i = 0; i < recordSet.size(); i++) 
		{
			rec = recordSet.get(i);
			//IF ALARM
			if ("A".equalsIgnoreCase(rec.get("t").toString()))
			{
				deviceStructure = deviceStructureList.get((Integer) rec.get("iddevice"));
				AlarmEvent alarmEv = new AlarmEvent(rec, deviceStructure);
				elements.addAlarmEvent(alarmEv);
			}
			else if ("E".equalsIgnoreCase(rec.get("t").toString())) // ELSE EVENT
			{
				AlarmEvent event =new AlarmEvent(rec,message,categ);
				event.setId(event.getIdevent());
	        	elements.addAlarmEvent(event);
			}
			else if ("P".equalsIgnoreCase(rec.get("t").toString())) // PRARAMETER PLUGIN EVENT
			{
				LangService l = LangMgr.getInstance().getLangService(lang);
				String msg = l.getString("alevsearch", "devModMsg");
				String[] p = rec.get("parameters").toString().split(";");
				msg=MessageFormat.format(msg, (Object[]) p);
				
				deviceStructure = deviceStructureList.get((Integer) rec.get("iddevice"));
				
				AlarmEvent event =new AlarmEvent(rec,msg,deviceStructure.getDescription());
				event.setId(event.getIdevent());
	        	elements.addAlarmEvent(event);
			}
			
		}

		elements.setPageNumber(numPage);
	
		
		return elements;
	}

}
