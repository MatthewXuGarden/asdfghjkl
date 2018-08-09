package com.carel.supervisor.plugin.energy;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.language.*;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class EnergyGroup {

	private Integer id		= 0;
	private String name		= "";
	private Boolean enabled	= true;

	private List<EnergyConsumer> consumers;

	public EnergyGroup(int idGroup) {
		this(idGroup,null);	
	}
	
	public EnergyGroup(int idGroup,String language) {
		id = idGroup;
		consumers = new LinkedList<EnergyConsumer>();
		if( id != 0 )
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energygroup where idgroup = ?;", new Object[] { id });
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				name = UtilBean.trim(r.get("name"));
				enabled = (Boolean)r.get("enabled");
				// retrieve consumers
				rs = DatabaseMgr.getInstance().executeQuery(null, "select energyconsumer.*, dev.iddevice  from  "
					+ " energyconsumer, cfdevice as dev,  cfvariable as kw, cfvariable as kwh "
					+ " where energyconsumer.idgroup = " + id
					+ " and energyconsumer.idkw=kw.idvariable and "
					+ " energyconsumer.idkwh=kwh.idvariable and kw.iddevice=dev.iddevice and "
					+ " dev.iscancelled='FALSE' and kw.iscancelled='FALSE' and kwh.iscancelled='FALSE' "
					+ " order by idconsumer;");
				for(int i = 0; i < rs.size(); i++) {
					r = rs.get(i);
					if( r.get("name") != null ) {
						EnergyConsumer c = new EnergyConsumer(r);
						if(language!=null){
							RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(
								null,
								" select description from cftableext"
										+ " where tableid=" + r.get("iddevice") + " and tablename='cfdevice' " 
										+ "and idsite=1 and languagecode='" + language + "';");					
							if(rs2.size()>0){
								c.setName((String)rs2.get(0).get(0));
							}
						}
						addConsumer(c);
					}
				}
			}
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyGroup").error(e);
		}
	}
	
	public EnergyGroup(Record record) {
		consumers = new LinkedList<EnergyConsumer>();
		id = (Integer)record.get("idgroup");
		name = UtilBean.trim(record.get("name"));
		enabled = (Boolean)record.get("enabled");
	}

	public List<EnergyConsumer> getConsumers() {
		return consumers;
	}

	public String getName() {
		return name;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void addConsumer(EnergyConsumer c) {
		consumers.add(c);
	}

	public EnergyConsumer getConsumer(int idcons) {
		for (Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
			EnergyConsumer ec = itr.next();
			if (ec.getIdconsumer() == idcons)
				return ec;
		}
		return null;
	}

	public EnergyConsumer getConsumer(int idKw, int idKwh) {
		for(Iterator<EnergyConsumer> it = consumers.iterator(); it.hasNext();) {
			EnergyConsumer ec = it.next();
			if( ec.getIdkw() == idKw && ec.getIdkwh() == idKwh )
				return ec;
		}
		return null;
	}
	
	public Integer getId() {
		return id;
	}

	public Float getGroupKw() {
		Float acc = 0f;
		try {
			for (Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
				EnergyConsumer ec = itr.next();
				if (Float.NaN == ec.getKw())
					return Float.NaN;
				else
					acc += ec.getKw();
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return acc;
	}

	public Float getGroupKwh() {
		Float acc = 0f;
		try {
			for (Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
				EnergyConsumer ec = itr.next();
				if (Float.NaN == ec.getKwh())
					return Float.NaN;
				else
					acc += ec.getKwh();
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return acc;
	}
	
	
	public Float getKw(Timestamp begin, Timestamp end, Integer step) {
		Float fGroupValue = 0f;
		try {
			for(Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
				EnergyConsumer ec = itr.next();
				Float fConsValue = ec.getKw(begin, end, step);
				if( Float.NaN != fConsValue )
					fGroupValue += fConsValue;
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return fGroupValue;
	}

	
	public Float getKw(Timestamp begin, Timestamp end, double kWh) {
		Float fGroupValue = 0f;
		try {
			for(Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
				EnergyConsumer ec = itr.next();
				Float fConsValue = ec.getKw(begin, end, kWh);
				if( Float.NaN != fConsValue )
					fGroupValue += fConsValue;
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return fGroupValue;
	}
	

	public Float getKwh(Timestamp begin, Timestamp end) {
		Float fGroupValue = 0f;
		try {
			for(Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
				EnergyConsumer ec = itr.next();
				Float fConsValue = ec.getKwh(begin, end);
				if( Float.NaN != fConsValue )
					fGroupValue += fConsValue;
			}
		} catch (Exception e) {
			return Float.NaN;
		}
		return fGroupValue;
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
		// last double used to keep totals
		for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
			anKWh[i] = 0;
			anCost[i] = 0;
		}
		for(Iterator<EnergyConsumer> itr = consumers.iterator(); itr.hasNext();) {
			EnergyConsumer ec = itr.next();
			double[] anConsKWh = new double[anKWh.length];
			double[] anConsCost = new double[anCost.length];
			ec.getKWhCost(tsBegin, tsEnd, anConsKWh, anConsCost);
			for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
				anKWh[i] += anConsKWh[i];
				anCost[i] += anConsCost[i];
			}
		}
	}
	
	
	public void addGroup(UserSession us, Properties prop)
	{
		String strCons = prop.getProperty("cons");
		String[] astrCons = strCons.split(";");
		int maxConsumer = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.MAXCONSUMERS, 200);
		int totalConsumer = EnergyConsumer.getTotallyConsumerNumber(null);
		if(astrCons != null)
		{
			if(totalConsumer+astrCons.length>maxConsumer)
			{
				us.getCurrentUserTransaction().setProperty("maxconsumerreached", String.valueOf(totalConsumer+astrCons.length));
				return;
			}
		}
		
		int id = getNextGroupId();
		String strGroupName = prop.getProperty("name");
		boolean bEnabled = prop.getProperty("enabled") != null;
		try{
			String sql = "insert into energygroup values (?, ?, ?);";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { id, strGroupName, bEnabled } );
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// add consumers
		if( astrCons != null ) {
			for(int i = 0; i < astrCons.length; i++) {
				String[] astrIds = astrCons[i].split(",");
				int idDevice = Integer.parseInt(astrIds[0]);
				String strConsumerName = EnergyModel.getDeviceName(us.getLanguage(), idDevice);
				int idVarKw = Integer.parseInt(astrIds[1]);
				int idVarKwh = Integer.parseInt(astrIds[2]);
				EnergyConsumer.addConsumer(id, strConsumerName, idVarKw, idVarKwh);
			}
		}
	}
	
	
	public void deleteGroup()
	{
		try {
			String sql = "delete from energygroup where idgroup = ?;";
			DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { id });
			// remove consumers
			for(Iterator<EnergyConsumer> it = consumers.iterator(); it.hasNext();)
				EnergyConsumer.deleteConsumer(id, it.next().getIdconsumer());
		} catch(Exception e) { 
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	
	public void updateGroup(UserSession us, Properties prop)
	{
		String strCons = prop.getProperty("cons");
		String[] astrCons = strCons.split(";");
		int maxConsumer = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.MAXCONSUMERS, 200);
		int totalConsumer = EnergyConsumer.getTotallyConsumerNumber(id);
		if(astrCons != null)
		{
			if(totalConsumer+astrCons.length>maxConsumer)
			{
				us.getCurrentUserTransaction().setProperty("maxconsumerreached", String.valueOf(totalConsumer+astrCons.length));
				return;
			}
		}
		String strGroupName = prop.getProperty("name");
		boolean bEnabled = prop.getProperty("enabled") != null;
		
		try{
			String sql = "update energygroup set name = ?, enabled = ? where idgroup = ?;";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { strGroupName, bEnabled, id } );
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// delete consumers
		if( astrCons != null && astrCons[0] != "" ) {
			for(Iterator<EnergyConsumer> it = consumers.iterator(); it.hasNext();) {
				boolean bDelete = true;
				EnergyConsumer ec = it.next();
				for(int i = 0; i < astrCons.length; i++) {
					String[] astrIds = astrCons[i].split(",");
					int idVarKw = Integer.parseInt(astrIds[1]);
					int idVarKwh = Integer.parseInt(astrIds[2]);
					if( ec.getIdkw() == idVarKw && ec.getIdkwh() == idVarKwh ) {
						bDelete = false;
						break;
					}
				}
				if( bDelete )
					EnergyConsumer.deleteConsumer(id, ec.getIdconsumer());
			}
			// add consumers
			for(int i = 0; i < astrCons.length; i++) {
				String[] astrIds = astrCons[i].split(",");
				int idVarKw = Integer.parseInt(astrIds[1]);
				int idVarKwh = Integer.parseInt(astrIds[2]);
				EnergyConsumer ec = getConsumer(idVarKw, idVarKwh);
				if( ec == null ) {
					int idDevice = Integer.parseInt(astrIds[0]);
					String strConsumerName = EnergyModel.getDeviceName(us.getLanguage(), idDevice);
					EnergyConsumer.addConsumer(id, strConsumerName, idVarKw, idVarKwh);
				}
			}
		}
		else {
			for(Iterator<EnergyConsumer> it = consumers.iterator(); it.hasNext();)
				EnergyConsumer.deleteConsumer(id, it.next().getIdconsumer());
		}
	}
	
	
	public static int getNextGroupId()
	{
		int idNext = 1;
		try {
			String sql = "select idgroup from energygroup order by idgroup;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				int idGroup = (Integer)rs.get(i).get(0);
				if( idGroup > 0 ) {
					if( idNext < idGroup )
						return idNext;
					else
						idNext = idGroup + 1;
				}
			}
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyGroup").error(e);
		}
		return idNext;
	}
	
	
	public static String getHtmlGroupTable(UserSession session)
	{
		String language = session.getLanguage();
		LangService lang = LangMgr.getInstance().getLangService(language);
		
		try {
			// data
			String sql = "select * from energygroup order by idgroup;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				astrClickRowFunction[i] = r.get("idgroup").toString();
				data[i] = new HTMLElement[3];
				data[i][0] = new HTMLSimpleElement((String)r.get("name"));
				sql = "select count(idconsumer) from energyconsumer where idgroup = ? and idkw > 0 and idkwh > 0;";
				data[i][1] = new HTMLSimpleElement(
					DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { (Integer)r.get("idgroup") }).get(0).get(0).toString()
				);
				data[i][2] = new HTMLSimpleElement(lang.getString("energy", (Boolean)r.get("enabled") ? "enabled" : "disabled"));
			}
			
			// header
			String[] headerTable = new String[3];
	        headerTable[0] = lang.getString("energy", "group_name");
	        headerTable[1] = lang.getString("energy", "group_devices");
	        headerTable[2] = lang.getString("energy", "status");
			
	        // table
	        HTMLTable table = new HTMLTable("groupTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectGroup('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyGroup('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(session.getScreenWidth());
	        table.setScreenH(session.getScreenHeight());
	        table.setHeight(150);
	        table.setColumnSize(0, 350);
	        table.setColumnSize(1, 50);
	        table.setColumnSize(2, 100);
	        table.setWidth(890);
	        return table.getHTMLText();
		} catch(Exception e) { 
			LoggerMgr.getLogger("EnergyGroup").error(e);
		}
		
		return "";
	}
}
