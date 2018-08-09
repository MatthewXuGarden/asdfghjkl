package com.carel.supervisor.presentation.bean.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.alarms.Alarm;
import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.session.UserSession;

public class SearchAlarm {
	private final static int NUM_ALARMS_SEARCH_4_PAGE = 50;
	private final static int ROW_LIMIT = 5000;

	public SearchAlarm() {
	}

	public AlarmList find(UserSession userSession, int numPage) throws DataBaseException, Exception {
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

		String alarm = userSession.getProperty("selectedAlarm");

		if ((alarm == null) || alarm.equals("undefined")) {
			alarm = "";
		}

		String resetcombo = userSession.getProperty("reset");

		if (resetcombo == null) {
			resetcombo = "";
		}

		String chkack = userSession.getProperty("chkack");

		if (chkack == null) {
			chkack = "false";
		}

		String chkdel = userSession.getProperty("chkdel");

		if (chkdel == null) {
			chkdel = "false";
		}

		String chkres = userSession.getProperty("chkres");

		if (chkres == null) {
			chkres = "false";
		}

		String userack = userSession.getProperty("userack");

		if (userack == null) {
			userack = "";
		}

		String userdel = userSession.getProperty("userdel");

		if (userdel == null) {
			userdel = "";
		}

		String userres = userSession.getProperty("userres");

		if (userres == null) {
			userres = "";
		}

		String priority = userSession.getProperty("prioritySel");
        
        if (priority == null)
        {
        	priority = "";
        }
		
		StringBuffer sql = new StringBuffer(
				"select hsalarm.*, cftableext.description from hsalarm, cftableext"
						+ " where cftableext.idsite = "	+ String.valueOf(userSession.getIdSite()) + " "
						+ " and cftableext.tablename='cfvariable' "
						+ " and cftableext.tableid = hsalarm.idvariable "
						+ " and cftableext.languagecode = '" + lang + "' "
						+ " and hsalarm.idsite = " + String.valueOf(userSession.getIdSite()) + " ");

		if (!device.equals("")) // selezionato un device
		{
			sql.append(" and hsalarm.iddevice = " + device + " ");
		} else {
			// nessun device selezionato allora prendo tutti i device del
			// gruppo selezionato (se nessuno,quello globale)
			int[] idsdevices = null;
			int groupSelected = 0;

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

		if (!alarm.equals("")) // selezionato tipo di allarme
		{
			if (!device.equals("")) {
				sql.append(" and hsalarm.idvariable = " + alarm	+ " and hsalarm.iddevice = " + device + " ");
			} else {
				// se c'è il tipo di allarme ma non il dispositivo -> Ricerca solo x CATEGORIA di ALLARME
				sql.append(" and hsalarm.idvariable in (" +
						"select hsalarm.idvariable from hsalarm,cfvariable " + 
						"where hsalarm.idvariable=cfvariable.idvariable and cfvariable.grpcategory="+ alarm+ " and "+
						"cfvariable.idsite="+ new Integer(userSession.getIdSite())+
						" and hsalarm.idsite="+ new Integer(userSession.getIdSite()) + ")");
			}
		}
		else
	        if (!priority.equals("")) //selezionata priorità
	        {
	        	if (priority.equalsIgnoreCase("4"))
	        	   	sql.append(" and cast(hsalarm.priority as INTEGER) >= " + priority + " ");
	        	else
	        	   	sql.append(" and hsalarm.priority = " + priority + " ");
	        }
		
		sql.append(" and hsalarm.pvcode = '" + BaseConfig.getPlantId() + "' ");

		// gestione intervallo di date
		Timestamp now = new Timestamp(System.currentTimeMillis());
		/*
		String tmp = DateUtils.date2String(now, "dd/MM/yyyy");
		String[] today = tmp.split("/");
		GregorianCalendar thismoment = new GregorianCalendar(Integer.parseInt(today[2]), 
				Integer.parseInt(today[1]) - 1, 
				Integer.parseInt(today[0]));
		*/
		String tmp = DateUtils.date2String(now, "dd/MM/yyyy HH:mm:ss");
        String[] today = tmp.split("/");
        String year = today[2].split(" ")[0];
        String times = today[2].split(" ")[1];
        String[] time = times.split(":");
        GregorianCalendar thismoment = new GregorianCalendar(
        		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]));

        if (optionDate.equals("xh")) //ULTIME x ORE
        {
        	//contemplo ore e minuti per il calcolo:
        	thismoment = new GregorianCalendar(
            		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        	int lastxhours = Integer.parseInt(userSession.getProperty("xhval"));
        	
        	thismoment.add(GregorianCalendar.HOUR_OF_DAY, -lastxhours); //sottraggo x ore

            Timestamp lastxh = new Timestamp(thismoment.getTimeInMillis());

            sql.append("and ((hsalarm.lastupdate between '"+lastxh+"' and '"+now+"') or (hsalarm.endtime is null)) ");
        }
        
        if (optionDate.equals("24h")) //ULTIME 24 ORE
        {
        	//contemplo ore e minuti per il calcolo:
        	thismoment = new GregorianCalendar(
            		Integer.parseInt(year), Integer.parseInt(today[1]) - 1, Integer.parseInt(today[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        	thismoment.add(GregorianCalendar.DAY_OF_MONTH, -1); //sottraggo 1 giorno

            Timestamp last24h = new Timestamp(thismoment.getTimeInMillis());

            sql.append("and ((hsalarm.lastupdate between '"+last24h+"' and '"+now+"') or (hsalarm.endtime is null)) ");
        }
		
		if (optionDate.equals("week")) // ULTIMA SETTIMANA
		{
			thismoment.add(5, -7); // sottraggo 7 giorni field 5 = day

			Timestamp lastweek = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + lastweek
					+ "' and '" + now + "' ");
		}

		else if (optionDate.equals("month")) // ULTIMO MESE
		{
			thismoment.add(2, -1); // sottraggo un mese field 2 = month

			Timestamp lastmonth = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + lastmonth
					+ "' and '" + now + "' ");
		}

		else if (optionDate.equals("3month")) // ULTIMI 3 MESI
		{
			thismoment.add(2, -3); // sottraggo 3 mesi field 2 = month

			Timestamp last3month = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + last3month
					+ "' and '" + now + "' ");
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

			sql.append("and hsalarm.lastupdate between '" + dFrom + "' and '"
					+ dTo + "' ");
		}

		// //////////////////////////////////////////////////////////////////
		if (chkack.equals("true")) // prendo ackuser da parametro
		{
			if (!userack.equals("")) {
				sql.append(" and LOWER(hsalarm.ackuser) = '"
						+ userack.toLowerCase() + "' ");
			} else {
				sql.append(" and hsalarm.ackuser is not null ");
			}
		}

		if (chkdel.equals("true")) // prendo delactionuser da parametro
		{
			if (!userdel.equals("")) {
				sql.append(" and LOWER(hsalarm.delactionuser) = '"
						+ userdel.toLowerCase() + "'");
			} else {
				sql.append(" and hsalarm.delactionuser is not null ");
			}
		}

		if (chkres.equals("true")) // reset ackuser da parametro
		{
			if (!userres.equals("")) {
				sql.append(" and LOWER(hsalarm.resetuser) = '"
						+ userres.toLowerCase() + "' ");
			} else {
				sql.append(" and hsalarm.resetuser is not null ");
			}
		}

		//TOTAL PAGE NUMBER CALCULATION
		//if (numPage == 0) {
		int pageNumber = 0;
		String sqlTmp = "select count(1) as count "
				+ sql.toString()
						.substring(new String("select hsalarm.*, cftableext.description ").length());
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
				sqlTmp);// , paramsTmp);

		if (rs.size() > 0) {
			int n = ((Integer) rs.get(0).get("count")).intValue();
			pageNumber = (n/ NUM_ALARMS_SEARCH_4_PAGE )+1;
			if (n>ROW_LIMIT)
			{
				userSession.setProperty("limit_search", "ko");
//				return new AlarmList();
			}		}

		else {
				pageNumber = 1;
		}

			//numPage = pageNumber + 1;
		//} // page
		//END TOTAL PAGE NUMBER CALCULATION
				
		sql.append(" order by starttime desc");

		// per la paginazione
		sql.append(" limit " + new Integer(NUM_ALARMS_SEARCH_4_PAGE)
				+ " offset "
				+ new Integer((numPage - 1) * NUM_ALARMS_SEARCH_4_PAGE) + " ");
		
		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
				sql.toString());
				
		AlarmList alarmFound = new AlarmList();
		AlarmLog alarmLog = null;
		List<AlarmLog> alarmLogList = new ArrayList<AlarmLog>();
		DeviceStructureList deviceStructureList = userSession.getGroup()
				.getDeviceStructureList();
		DeviceStructure deviceStructure = null;

		int[] idvar = new int[recordSet.size()];

		for (int i = 0; i < recordSet.size(); i++) {
			alarmLog = new AlarmLog(recordSet.get(i), null);
			idvar[i] = alarmLog.getIdvariable();
			deviceStructure = deviceStructureList.get(alarmLog.getIddevice());

			Alarm temp = new Alarm(alarmLog, deviceStructure);
			alarmFound.addAlarm(temp);
			alarmLogList.add(alarmLog);
		}

		alarmFound.setPageNumber(numPage);
		alarmFound.setPageTotal(pageNumber);
		
		return alarmFound;
	}

	public AlarmList findRemote(UserSession userSession, int numPage)
			throws DataBaseException, Exception {
		String lang = userSession.getLanguage();

		// parametro site
		String s_idsite = userSession.getProperty("id_site");
		int idsite = -1;

		if (s_idsite != null) {
			idsite = Integer.parseInt(s_idsite);
		}

		// parametro category
		String s_category = userSession.getProperty("category");
		int category = -1;

		if (!s_category.equals("")) {
			category = Integer.parseInt(s_category);
		}

		String optionDate = userSession.getProperty("dataselect");

		if (optionDate == null) {
			optionDate = "week";
		}

		String datefrom = userSession.getProperty("datefrom");
		String dateto = userSession.getProperty("dateto");

		StringBuffer sql = new StringBuffer(
				"select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable "
						+ "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and "
						+ "cftableext.languagecode = '"
						+ lang
						+ "' and hsalarm.pvcode = '"
						+ BaseConfig.getPlantId()
						+ "' ");

		// List params = new ArrayList();
		// params.add(lang);
		// params.add(BaseConfig.getPlantId());

		if (idsite != -1) {
			sql.append(" and hsalarm.idsite = " + new Integer(idsite) + " ");
			// params.add(new Integer(idsite));
		}

		if (category != -1) {
			sql
					.append("and hsalarm.idvariable in (select hsalarm.idvariable from hsalarm,cfvariable "
							+ "where hsalarm.idvariable=cfvariable.idvariable and cfvariable.grpcategory="
							+ new Integer(category)
							+ " and "
							+ "cfvariable.idsite="
							+ new Integer(idsite)
							+ " and hsalarm.idsite="
							+ new Integer(idsite)
							+ ")");
			// params.add(new Integer(category));
			// params.add(new Integer(idsite));
			// params.add(new Integer(idsite));
		}

		// gestione intervallo di di date
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String tmp = DateUtils.date2String(now, "dd/MM/yyyy");
		String[] today = tmp.split("/");
		GregorianCalendar thismoment = new GregorianCalendar(Integer
				.parseInt(today[2]), Integer.parseInt(today[1]) - 1, Integer
				.parseInt(today[0]));

		if (optionDate.equals("week")) // ULTIMA SETTIMANA
		{
			thismoment.add(5, -7); // sottraggo 7 giorni field 5 = day

			Timestamp lastweek = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + lastweek
					+ "' and '" + now + "' ");
			// params.add(lastweek);
			// params.add(now);
		}

		else if (optionDate.equals("month")) // ULTIMO MESE
		{
			thismoment.add(2, -1); // sottraggo un mese field 2 = month

			Timestamp lastmonth = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + lastmonth
					+ "' and '" + now + "' ");
			// params.add(lastmonth);
			// params.add(now);
		}

		else if (optionDate.equals("3month")) // ULTIMI 3 MESI
		{
			thismoment.add(2, -3); // sottraggo 3 mesi field 2 = month

			Timestamp last3month = new Timestamp(thismoment.getTimeInMillis());

			sql.append("and hsalarm.lastupdate between '" + last3month
					+ "' and '" + now + "' ");
			// params.add(last3month);
			// params.add(now);
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

			sql.append("and hsalarm.lastupdate between '" + dFrom + "' and '"
					+ dTo + "' ");
			// params.add(dFrom);
			// params.add(dTo);
		}

		// //////////////////////////////////////////////////////////////////
		if (numPage == 0) {
			int pageNumber = 0;
			// Object[] paramsTmp = params.toArray();
			String sqlTmp = "select count(1) as count "
					+ sql.toString()
							.substring(new String("select * ").length());
			RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
					sqlTmp);// , paramsTmp);

			if (recordSet.size() > 0) {
				pageNumber = ((Integer) recordSet.get(0).get("count"))
						.intValue()
						/ NUM_ALARMS_SEARCH_4_PAGE;
			}

			else {
				pageNumber = 1;
			}

			numPage = pageNumber + 1;
		} // page

		sql.append(" order by starttime ");

		// per la paginazione
		sql.append(" limit " + new Integer(NUM_ALARMS_SEARCH_4_PAGE)
				+ " offset "
				+ new Integer((numPage - 1) * NUM_ALARMS_SEARCH_4_PAGE) + " ");

		// params.add(new Integer(NUM_ALARMS_SEARCH_4_PAGE));
		// params.add(new Integer((numPage - 1) * NUM_ALARMS_SEARCH_4_PAGE));

		// Object[] param = params.toArray();

		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
				sql.toString());// , param);

		AlarmList alarmFound = new AlarmList();
		AlarmLog alarmLog = null;
		List<AlarmLog> alarmLogList = new ArrayList<AlarmLog>();
		DeviceStructureList deviceStructureList = userSession.getGroup()
				.getDeviceStructureList();
		DeviceStructure deviceStructure = null;

		int[] idvar = new int[recordSet.size()];

		for (int i = 0; i < recordSet.size(); i++) {
			alarmLog = new AlarmLog(recordSet.get(i), null);
			idvar[i] = alarmLog.getIdvariable();
			deviceStructure = deviceStructureList.get(alarmLog.getIddevice());

			Alarm temp = new Alarm(alarmLog, deviceStructure);
			alarmFound.addAlarm(temp);
			alarmLogList.add(alarmLog);
		}

		alarmFound.setPageNumber(numPage);

		return alarmFound;
	}
}
