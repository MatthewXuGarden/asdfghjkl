package com.carel.supervisor.plugin.energy;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
//import com.carel.supervisor.field.Variable;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.kpi.KpiMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class EnergyConsumer {

//	public static final String HOUR = "hour";
//	public static final String DAY = "day";
//	public static final String MONTH = "month";
	
//	private Variable v;
//	private EnergyGroup group;
	private Integer idkw;
	private Integer idkwh;
	private String name;
	private Boolean enabled;
	private Integer idconsumer;
	private Integer idgroup;
	private Integer iddevice = 0;

//	public EnergyConsumer(int idvariable, EnergyGroup g) throws Exception {
//		v = ControllerMgr.getInstance().getFromField(idvariable);
//	}

	public EnergyConsumer(Record record) {
		idconsumer = (Integer) record.get("idconsumer");
		idgroup = (Integer) record.get("idgroup");
		idkw = (Integer) record.get("idkw");
		idkwh = (Integer) record.get("idkwh");
		name = UtilBean.trim(record.get("name"));
		enabled = (Boolean) record.get("enabled");
		iddevice = (Integer) record.get("iddevice");
	}

//	public EnergyGroup getGroup() {
//		return group;
//	}

//	public Variable getV() {
//		return v;
//	}

	public Integer getIdkw() {
		return idkw;
	}

	public String getKwDescription(String language) {
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(
					null,
					"select description from cftableext where idsite=1 and tablename='cfvariable'" + " and tableid="
							+ idkw + " and languagecode='" + language + "'");
			if (rs == null)
				return "";
			if (rs.size() > 0) {
				return UtilBean.trim(rs.get(0).get(0));
			} else {
				return "";
			}
		} catch (DataBaseException e) {
			return "";
		}

	}

	public Integer getIdkwh() {
		return idkwh;
	}

	public String getKwhDescription(String language) {
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(
					null,
					"select description from cftableext where idsite=1 and tablename='cfvariable'" + " and tableid="
							+ idkwh + " and languagecode='" + language + "'");
			if (rs == null)
				return "";
			if (rs.size() > 0) {
				return UtilBean.trim(rs.get(0).get(0));
			} else {
				return "";
			}
		} catch (DataBaseException e) {
			return "";
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public Integer getIdconsumer() {
		return idconsumer;
	}

	public Integer getIdDev() {
		return iddevice;
	}

	public String getDeviceDescription(String language) {
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(
					null,
					"select description from cftableext where idsite=1 and tablename='cfdevice'" + " and tableid="
							+ iddevice + " and languagecode='" + language + "'");
			if (rs == null)
				return "";
			if (rs.size() > 0) {
				return UtilBean.trim(rs.get(0).get(0));
			} else {
				return "";
			}
		} catch (DataBaseException e) {
			return "";
		}

	}

	public Float getKw() {

		try {
			return new Float(ControllerMgr.getInstance().getFromField(idkw).getCurrentValue());
		} catch (Exception e) {
			return Float.NaN;
		}
	}

	public Float getKwh() {
		try {
			return new Float(ControllerMgr.getInstance().getFromField(idkwh).getCurrentValue());
		} catch (Exception e) {
			return Float.NaN;
		}
	}

//	public Float getKgCO2() {
//		try {
//			return new Float(ControllerMgr.getInstance().getFromField(idkwh).getCurrentValue())
//					* new Float(EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("kgco2"));
//		} catch (Exception e) {
//			return Float.NaN;
//		}
//	}

//	public Float getCost() {
//		try {
//			return new Float(ControllerMgr.getInstance().getFromField(idkwh).getCurrentValue())
//					* new Float(EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("cost"));
//		} catch (Exception e) {
//			return Float.NaN;
//		}
//	}

	public Integer getIdgroup() {
		return idgroup;
	}

	public Float getKw(Timestamp begin, Timestamp end, Integer step) {
		try{
			Float time = getHoursBetween(begin,end);
			return getKwh(begin, end) / time;
		} catch (Exception e) {
			return Float.NaN;
		}
	}

	public Float getKw(Timestamp begin, Timestamp end, double kWh) {
		try {
			Float time = getHoursBetween(begin,end);
			return (float)kWh / time;
		} catch (Exception e) {
			return Float.NaN;
		}
	}
	
	private Float getHoursBetween(Timestamp begin, Timestamp end) {
		return new Float((end.getTime()-begin.getTime())/(1000l*60l*60l));
	}

	public Float getKwh(Timestamp begin, Timestamp end) {
		try{
			return ((Float)DatabaseMgr.getInstance().executeQuery(null, "select value from energylog where idvariable="+
					idkwh+" and tstamp between '"+
					begin+"' and '"+end+"' order by tstamp desc limit 1;").get(0).get("value") - 
					(Float)DatabaseMgr.getInstance().executeQuery(null, "select value from energylog where idvariable="+
							idkwh+" and tstamp between '"+
							begin+"' and '"+end+"' order by tstamp limit 1;").get(0).get("value"));
		} catch (Exception e) {
			return Float.NaN;
		}
	}

	public Float getKgCO2(Float kwh) {
		try {
			return kwh * new Float(EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("kgco2"));
		} catch (Exception e) {
			return Float.NaN;
		}
	}

	public Float getCost(Float kwh) {
		try {
			return kwh * new Float(EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("cost"));
		} catch (Exception e) {
			return Float.NaN;
		}
	}
	
	
	// new time slot related records
	public void getKWhCost(Timestamp tsBegin, Timestamp tsEnd, double[] anKWh, double[] anCost)
	{
		Calendar c = Calendar.getInstance();
		Date dtBegin = new Date(tsBegin.getTime());
		Date dtEnd = new Date(tsEnd.getTime());
		// last double used to keep totals
		int anTimeSlot[] = new int[24]; // 24 timeslots / day
		for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
			anKWh[i] = 0;
			anCost[i] = 0;
		}
		try {
			String sqlSelect = "select * from energylogex where date >= ? and date <= ? and (idvariable = ? or idvariable = ?) order by date, idvariable, flags";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlSelect, new Object[] { dtBegin, dtEnd, EnergyLoggerEx.IDVAR_TIMESLOT, getIdkwh() });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Date day = (Date)r.get("date");
				int nFlags = (Integer)r.get("flags"); 
				if( (nFlags & EnergyLoggerEx.FLAG_TIMESLOT) == EnergyLoggerEx.FLAG_TIMESLOT ) {
					for(int iHour = 0; iHour < 24; iHour++) {
						anTimeSlot[iHour] = ((Double)r.get("h" + iHour)).intValue();
					}
				}
				else if( (nFlags & EnergyLoggerEx.FLAG_METER) == EnergyLoggerEx.FLAG_METER ) {
					for(int iHour = 0; iHour < 24; iHour++) {
						double nPrevMeter = (Double)r.get("h" + iHour);
						double nMeter = (Double)r.get("h" + (iHour+1));
						c.setTime(day);
						c.add(Calendar.HOUR_OF_DAY, iHour);
						if(c.getTime().before(tsBegin))
							continue;
						else if(!c.getTime().before(tsEnd))
							break;
						if( nPrevMeter != EnergyLoggerEx.NOT_AVAILABLE && nMeter != EnergyLoggerEx.NOT_AVAILABLE ) {
							int iTimeSlot = anTimeSlot[iHour];
							double nKWh = nMeter - nPrevMeter;
							anKWh[iTimeSlot] += nKWh;						// time slot meter count
							anKWh[EnergyProfile.TIMESLOT_NO] += nKWh;		// total meter count
						}
					}
				}
				else if( (nFlags & EnergyLoggerEx.FLAG_COST) == EnergyLoggerEx.FLAG_COST ) {
					for(int iHour = 0; iHour < 24; iHour++) {
						c.setTime(day);
						c.add(Calendar.HOUR_OF_DAY, iHour);
						if(c.getTime().before(tsBegin))
							continue;
						else if(!c.getTime().before(tsEnd))
							break;
						double nCost = (Double)r.get("h" + iHour);
						if( nCost != EnergyLoggerEx.NOT_AVAILABLE ) {
							int iTimeSlot = anTimeSlot[iHour];
							anCost[iTimeSlot] += nCost;							// time slot cost count
							anCost[EnergyProfile.TIMESLOT_NO] += nCost;			// total cost count
						}
					}
					
				}
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public static void addConsumer(int idGroup, String strConsumerName, int idVarKw, int idVarKwh)
	{
		try{
			/* GLOBAL became a regular group
			if( idGroup > 0 ) {*/
				String sql = "insert into energyconsumer values (?, ?, ?, ?, ?, true);";
				DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { idGroup, getNextConsumerId(idGroup),
						idVarKw, idVarKwh,
						strConsumerName } );
			
			/*}
			else {
				String sql = "update energyconsumer set idkw = ?, idKwh = ?, name = ?, enabled = TRUE where idgroup = ?";
				DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { idVarKw, idVarKwh, strConsumerName, idGroup } );
			}
			*/
		} catch (Exception e) {
			LoggerMgr.getLogger("EnergyConsumer").error(e);
		}
	}
	
	
	public static void deleteConsumer(int idGroup, int idConsumer)
	{
		try {
			String sql;
			/* GLOBAL became a regular group
			if( idGroup > 0 ) {
			*/
				sql = "delete from energyconsumer where idgroup = ? and idconsumer = ?;";
				DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup, idConsumer });
			/*}
			else {
				sql = "update energyconsumer set idkw = null, idKwh = null, name = null, enabled = FALSE where idgroup = ?";
				DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup } );
			}*/
			// clean log
			sql = "delete from energylog where idvariable not in (select idkwh from energyconsumer);";
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			sql = "delete from energylogsupport where idvariable not in (select idkwh from energyconsumer);";
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyConsumer").error(e);
		}
	}
	
	public static int getTotallyConsumerNumber(Integer excludeGroup)
	{
		String sql = "select count(*) from energyconsumer ";
		try{
		if(excludeGroup != null)
		{
			sql += "where idgroup<>?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { excludeGroup});
			if(rs != null && rs.size()>0)
				return (Integer)rs.get(0).get(0);
		}
		else
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,null);
			if(rs != null && rs.size()>0)
				return (Integer)rs.get(0).get(0);
		}
		}catch(DataBaseException ex)
		{
			
		}
		return 0;
	}
	
	public static int getNextConsumerId(int idGroup)
	{
		int idNext = 1;
		try {
			String sql = "select idconsumer from energyconsumer where idgroup = ? order by idconsumer;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idGroup });
			for(int i = 0; i < rs.size(); i++) {
				int idConsumer = (Integer)rs.get(i).get(0);
				if( idConsumer > 0 ) {
					if( idNext < idConsumer )
						return idNext;
					else
						idNext = idConsumer + 1;
				}
			}
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyConsumer").error(e);
		}
		return idNext;
	}
	
	
	public EnergyDevice[] getConsumerDevices()
	{
		String sql = "select * from energydevice where idconsvar=?;";
		return null;
	}
	
	
	public static String getHtmlConsumerTable(UserSession session)
	{
		String language = session.getLanguage();
		LangService lang = LangMgr.getInstance().getLangService(language);
		
		try {
			// data
			String sql = "select distinct on(idkwh) idkwh, name from energyconsumer;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				astrClickRowFunction[i] = r.get("idkwh").toString();
				data[i] = new HTMLElement[2];
				data[i][0] = new HTMLSimpleElement((String)r.get("name"));
				sql = "select count(iddevice) from energydevice where idconsvar = ?;";
				data[i][1] = new HTMLSimpleElement(
					DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { (Integer)r.get("idkwh") }).get(0).get(0).toString()
				);
			}
			
			// header
			String[] headerTable = new String[2];
	        headerTable[0] = lang.getString("energy", "energy_meters");
	        headerTable[1] = lang.getString("energy", "no_of_devices");
			
	        // table
	        HTMLTable table = new HTMLTable("consumerTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectConsumer('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyConsumer('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setAlignType(1, 1);
	        table.setScreenW(session.getScreenWidth());
	        table.setScreenH(session.getScreenHeight());
	        table.setHeight(150);
	        table.setColumnSize(0, 400);
	        table.setColumnSize(1, 50);
	        table.setWidth(890);
	        return table.getHTMLText();
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyConsumer").error(e);
		}
		
		return "";
	}


	public static String getHtmlConsumerOptions(UserSession session)
	{
		StringBuffer sb = new StringBuffer();
		try {
			String sql = "select distinct on(idconsvar) idconsvar, energyconsumer.name, energymodel.name as model from energydevice"
				+ " inner join energyconsumer on idconsvar=idkwh"
				+ " inner join energymodel on idvarmdlkwh in (select idvarmdl from cfvariable where idvariable=idconsvar and idsite=1);";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				sb.append("<option value=\"" + r.get("idconsvar").toString() + "\">");
				sb.append(r.get("name").toString());
				sb.append(" - ");
				sb.append(r.get("model").toString());
				sb.append("</option>\n");
			}
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyConsumer").error(e);
		}
		return sb.toString();
	}
}
