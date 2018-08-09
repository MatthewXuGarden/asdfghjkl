package com.carel.supervisor.plugin.energy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class EnergyMgr {

	private static EnergyMgr me;
	private Boolean startedplugin = false;
	// private Boolean loaded = false;
	private EnergyConfiguration energyconfig;
	private EnergyActiveThread activeThread;
	private EnergyEngine engineThread;
	private EnergySchedulerConfig energyschedconfig;
	private Properties myproperties;
	private EnergyProfile energyProfile;
	public static final Object activemonitor = new Object();
	public static final Object enginemonitor = new Object();
	private static boolean bTimeSlot = false; 
	
	private EnergyMgr() {
		loadProperties();
		energyProfile = new EnergyProfile();
		energyProfile.loadXML(myproperties.getProperty("energyprofile"));
	}

	public static EnergyMgr getInstance() {
		if (me == null)
			me = new EnergyMgr();
		return me;
	}

	public void load() {
		synchronized (enginemonitor) {
			try {
				energyconfig = new EnergyConfiguration();
				energyProfile.loadXML(myproperties.getProperty("energyprofile"));
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				EventMgr.getInstance().error(1, "Energy", "energy", "EN04", new Object[] {});
			}
		}
	}

	public void loadScheduler() {
		synchronized (activemonitor) {
			try {
				energyschedconfig = new EnergySchedulerConfig();
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				EventMgr.getInstance().error(1, "Energy", "energy", "EN04", new Object[] {});
			}
		}
	}

	public void loadProperties() {
		myproperties = new Properties();
		try {
			RecordSet rs =  DatabaseMgr.getInstance().executeQuery(null, "select * from energysite");
			for(int i=0;i<rs.size();i++)
			{
				myproperties.setProperty(rs.get(i).get("key").toString(), rs.get(i).get("value").toString());
			}
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		bTimeSlot = getStringProperty("active_cfg").equals("time_slot");
	}
	
	public void startActive() {
		if (energyconfig.getLoaded()) {
			activeThread = new EnergyActiveThread(5000l);
			activeThread.startPoller();
		}
	}

	public void stopActive() {
		if (activeThread != null) {
			activeThread.stopPoller();
			activeThread = null;
		}
	}

	public void startEngine() {
		if (energyconfig.getLoaded()) {
			engineThread = new EnergyEngine();
			engineThread.startPoller();
		}
	}

	public void stopEngine() {
		if (engineThread != null) {
			engineThread.stopPoller();
			engineThread = null;
		}
	}

	public Boolean getActiveRunning() {
		if (activeThread != null) {
			return activeThread.isStarted();
		} else {
			return false;
		}
	}

	public Boolean getLoggerRunning() {
		if (activeThread != null) {
			return activeThread.isStarted();
		} else {
			return false;
		}
	}

	public EnergyConfiguration getSiteConfiguration() {
		return energyconfig;
	}

	public EnergySchedulerConfig getSchedulerConfiguration() {
		return energyschedconfig;
	}

	public Boolean getStartedplugin() {
		return startedplugin;
	}

	protected void setStartedplugin(Boolean started) {
		startedplugin = started;
	}

	public Boolean getLoaded() {
		if (energyconfig != null) {
			return energyconfig.getLoaded();
		} else {
			return false;
		}
	}

	public Map<String, EnergyReportRecord> getInstantReport() {
		HashMap<String, EnergyReportRecord> ret = new HashMap<String, EnergyReportRecord>();
		List<EnergyConsumer> cl = energyconfig.getConsumerList();
		Float sitekw = 0f;
		Float sitekwh = 0f;
		Float sitekgco2 = 0f;
		Float sitecost = 0f;
		Float consumerkw = 0f;
		Float consumerkwh = 0f;
		Float consumerkgco2 = 0f;
		Float consumercost = 0f;
		HashMap<Integer, Float> groupkw = new HashMap<Integer, Float>();
		HashMap<Integer, Float> groupkwh = new HashMap<Integer, Float>();
		HashMap<Integer, Float> groupkgco2 = new HashMap<Integer, Float>();
		HashMap<Integer, Float> groupcost = new HashMap<Integer, Float>();
		
		// active configuration
		String strActiveCfg = getStringProperty("active_cfg");
		boolean bTimeSlotEnabled = strActiveCfg.equals("time_slot");
		Timestamp tsBegin = new Timestamp(0);
		GregorianCalendar gcNow = new GregorianCalendar();
		gcNow.add(GregorianCalendar.DAY_OF_MONTH, 1); // to be able to catch current day too
		Timestamp tsEnd = new Timestamp(gcNow.getTimeInMillis());
		
		for (Iterator<EnergyConsumer> itr = cl.iterator(); itr.hasNext();) {
			EnergyConsumer ec = itr.next();
			if( !ec.isEnabled()
				|| (energyconfig.getGroup(ec.getIdgroup())!=null &&	!energyconfig.getGroup(ec.getIdgroup()).isEnabled()) )
				continue;
			
			//kw is get from device
			consumerkw = ec.getKw();
			
			// time slot related data
			double anConsumerKWh[] = null;
			double anConsumerCost[] = null; 
			if( bTimeSlotEnabled ) {
				anConsumerKWh = new double[EnergyProfile.TIMESLOT_NO + 1];
				anConsumerCost = new double[EnergyProfile.TIMESLOT_NO + 1];
				ec.getKWhCost(tsBegin, tsEnd, anConsumerKWh, anConsumerCost);
				consumerkwh = ec.getKwh(tsBegin,tsEnd);
				consumerkgco2 = ec.getKgCO2(consumerkwh);
				consumercost = new Float(anConsumerCost[EnergyProfile.TIMESLOT_NO]);
			}
			else
			{
				consumerkwh = ec.getKwh();
				consumerkgco2 = ec.getKgCO2(consumerkwh);
				consumercost = ec.getCost(consumerkwh);
			}
			
			ret.put("cons" + ec.getIdgroup() + "." + ec.getIdconsumer(), new EnergyReportRecord(ec.getIdconsumer(),
					ec.getName(), consumerkw, consumerkwh, consumerkgco2, consumercost,
					anConsumerKWh, anConsumerCost));
			
			// group
			if (groupkw.get(ec.getIdgroup()) == null) {
				groupkw.put(ec.getIdgroup(), 0.0f);
			}
			groupkw.put(ec.getIdgroup(), groupkw.get(ec.getIdgroup()) + consumerkw);
			if (groupkwh.get(ec.getIdgroup()) == null) {
				groupkwh.put(ec.getIdgroup(), 0.0f);
			}
			groupkwh.put(ec.getIdgroup(), groupkwh.get(ec.getIdgroup()) + consumerkwh);
			if (groupkgco2.get(ec.getIdgroup()) == null) {
				groupkgco2.put(ec.getIdgroup(), 0.0f);
			}
			groupkgco2.put(ec.getIdgroup(), groupkgco2.get(ec.getIdgroup()) + consumerkgco2);
			if (groupcost.get(ec.getIdgroup()) == null) {
				groupcost.put(ec.getIdgroup(), 0.0f);
			}
			if(energyconfig.getGroup(ec.getIdgroup())!=null && energyconfig.getGroup(ec.getIdgroup()).isEnabled())
				groupcost.put(ec.getIdgroup(), groupcost.get(ec.getIdgroup()) + consumercost);
			
			// site
			sitekw += consumerkw;
			sitekwh += consumerkwh;
			sitekgco2 += consumerkgco2;
			sitecost += consumercost;
		}
		for (Iterator<Integer> itr = groupkw.keySet().iterator(); itr.hasNext();) {
			Integer k = itr.next();
			ret.put("group" + k, new EnergyReportRecord(-k, energyconfig.getGroup(k).getName(), groupkw.get(k),
					groupkwh.get(k), groupkgco2.get(k), groupcost.get(k),
					null, null));
		}
		if( energyconfig.getGroup(-1) == null || !energyconfig.getGroup(-1).isEnabled() || energyconfig.getGroup(-1).getConsumers().isEmpty() ) {
			ret.put("site", new EnergyReportRecord(0, "---", sitekw, sitekwh, sitekgco2, sitecost,
					null, null));
		} else {
			if( energyconfig.getGroup(-1) != null && energyconfig.getGroup(-1).isEnabled() && !energyconfig.getGroup(-1).getConsumers().isEmpty() ) {
				//EnergyConsumer ec = energyconfig.getGroup(-1).getConsumer(-1);
				//consumerkw = ec.getKw();
				EnergyGroup ec = energyconfig.getGroup(-1);
				consumerkw = ec.getGroupKw();
		
				// time slot related data
				if( bTimeSlotEnabled ) {
					double anConsumerKWh[] = null;
					double anConsumerCost[] = null; 
					anConsumerKWh = new double[EnergyProfile.TIMESLOT_NO + 1];
					anConsumerCost = new double[EnergyProfile.TIMESLOT_NO + 1];
					ec.getKWhCost(tsBegin, tsEnd, anConsumerKWh, anConsumerCost);
					consumercost = new Float(anConsumerCost[EnergyProfile.TIMESLOT_NO]);
					consumerkwh = ec.getKwh(tsBegin,tsEnd);
					consumerkgco2 = ec.getKgCO2(consumerkwh);
				}
				else
				{
					//fixed price, get kwh from device
					//consumerkwh = ec.getKwh();
					consumerkwh = ec.getGroupKwh();
					consumerkgco2 = ec.getKgCO2(consumerkwh);
					consumercost = ec.getCost(consumerkwh);
				}
				
				ret.put("site", new EnergyReportRecord(0, "---", consumerkw, consumerkwh, consumerkgco2, consumercost,
						null, null));
				ret.put("other", new EnergyReportRecord(0, "---", consumerkw-sitekw, consumerkwh-sitekwh, consumerkgco2-sitekgco2, consumercost-sitecost,
						null, null));
			}
		}
		return ret;
	}

	public boolean getSchedulerLoaded() {
		if (energyschedconfig != null) {
			return energyschedconfig.getLoaded();
		} else {
			return false;
		}
	}

	public boolean getRunning() {
		if (engineThread != null) {
			return engineThread.isStarted();
		} else {
			return false;
		}

	}
	
	public String getStringProperty(String key){
		try{
			return myproperties.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer getIntegerProperty(String key){
		try{
			return new Integer(myproperties.getProperty(key));
		} catch (Exception e) {
			return null;
		}
	}

	public Timestamp getTimestampProperty(String key){
		try{
			return Timestamp.valueOf(myproperties.getProperty(key));
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getStringProperty(String key, String def){
		try{
			if(myproperties.getProperty(key)==null)
				return def;
			return myproperties.getProperty(key);
		} catch (Exception e) {
			return def;
		}
	}

	public Integer getIntegerProperty(String key, Integer def){
		try{
			if(myproperties.getProperty(key)==null)
				return def;
			return new Integer(myproperties.getProperty(key));
		} catch (Exception e) {
			return def;
		}
	}

	public EnergyReport getGroupReport(Integer group, Timestamp begin, Timestamp end, Integer type, Integer step){
		if(energyconfig.getGroup(group)==null || !energyconfig.getGroup(group).isEnabled()){
			return null;
		}
		EnergyReport rep = new EnergyReport(begin, end, type, step);
		List<EnergyConsumer> cl = energyconfig.getGroup(group).getConsumers();

		EnergyReport mini = miniReport(cl, begin, end, step);
		joinReports(mini, rep, "");

		// time detail
		Timestamp begints = new Timestamp(begin.getTime());
		Timestamp endts = null;
		Integer intervals = rep.getIntervalsNumber();
		for(int i = 1;i<=intervals;i++) {
			endts = rep.endTimestamp(begints);
			if(endts.after(end)) 
				break;
			mini = miniReport(cl, begints, endts, step);
			joinReports(mini, rep, "d"+i+".");
			
			begints = endts;
		}
		return rep;
	}
	
	public EnergyReport getConsReport(Integer group, Integer cons, Timestamp begin, Timestamp end, Integer type, Integer step)
	{
		EnergyGroup eg = energyconfig.getGroup(group); 
		if( eg == null || !eg.isEnabled() )
			return null;
		EnergyConsumer ec = eg.getConsumer(cons);
		if( ec == null || !ec.isEnabled() )
			return null;
		
		EnergyReport rep = new EnergyReport(begin, end, type, step);
		List<EnergyConsumer> cl = new LinkedList<EnergyConsumer>();
		cl.add(ec);
		
		EnergyReport mini = miniReport(cl, begin, end, step);
		joinReports(mini, rep, "");

		// time detail
		Timestamp begints = new Timestamp(begin.getTime());
		Timestamp endts = null;
		Integer intervals = rep.getIntervalsNumber();
		for(int i = 1;i<=intervals;i++) {
			endts = rep.endTimestamp(begints);
			if(endts.after(end)) 
				break;
			mini = miniReport(cl, begints, endts, step);
			joinReports(mini, rep, "d"+i+".");
			
			begints = endts;
		}
		return rep;
	}
	
	private void joinReports(EnergyReport from, EnergyReport to, String prefix) {
		if (from == null || to == null)
			return;
		for(Iterator<String> itr = from.getReportRecords().keySet().iterator();itr.hasNext();){
			String key = itr.next();
			to.putReportRecord(prefix+key, from.getReportRecord(key));
		}

	}
	private EnergyReport miniReport(List<EnergyConsumer> cl, Timestamp begints, Timestamp endts, Integer step){
		if (cl.isEmpty())
			return null;
		if (cl.get(0) == null)
			return null;
		Integer grp = cl.get(0).getIdgroup();
		EnergyReport rep = new EnergyReport(begints, endts, 0, 0);
		
		Float consumerkw = 0f;
		Float consumerkwh = 0f;
		Float consumerkgco2 = 0f;
		Float consumercost = 0f;

		Float groupkw = 0f;
		Float groupkwh = 0f;
		Float groupkgco2 = 0f;
		Float groupcost = 0f;
		
		// time slot related data
		double anGroupKWh[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		double anGroupCost[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		
		for (Iterator<EnergyConsumer> itr = cl.iterator(); itr.hasNext();) {
			EnergyConsumer ec = itr.next();
			if(!ec.isEnabled()) 
				continue;
			// consumer
			consumerkwh = ec.getKwh(begints, endts);
			consumerkw = ec.getKw(begints, endts, consumerkwh);
			consumerkgco2 = ec.getKgCO2(consumerkwh);
			double anKWh[] = null;
			double anCost[] = null;
			if( bTimeSlot ) { // fix kW and CO2 value
				anKWh = new double[EnergyProfile.TIMESLOT_NO + 1];
				anCost = new double[EnergyProfile.TIMESLOT_NO + 1];
				ec.getKWhCost(begints, endts, anKWh, anCost);
				consumercost = new Float(anCost[EnergyProfile.TIMESLOT_NO]);
			}
			else {
				consumercost = ec.getCost(consumerkwh);
			}
			rep.putReportRecord("cons" + ec.getIdgroup() + "." + ec.getIdconsumer(),
				new EnergyReportRecord(ec.getIdconsumer(), ec.getName()+EnergyModel.getMeterModel(ec.getIdkw(),ec.getIdkwh()), consumerkw, consumerkwh, consumerkgco2, consumercost,
						anKWh, anCost));
			// group
			if(!consumerkw.isNaN())
				groupkw += consumerkw;
			if(!consumerkwh.isNaN())
				groupkwh += consumerkwh;
			if(!consumerkgco2.isNaN())
				groupkgco2 += consumerkgco2;
			if(!consumercost.isNaN())
				groupcost += consumercost;
			
			if(bTimeSlot)
			{
				for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
					anGroupKWh[i] += anKWh[i];
					anGroupCost[i] += anCost[i];
				}
			}
		}
		rep.putReportRecord("group"+cl.get(0).getIdgroup(), 
				new EnergyReportRecord(-grp, energyconfig.getGroup(grp).getName(), groupkw, groupkwh, groupkgco2, groupcost,
						anGroupKWh, anGroupCost));
		return rep;
	}
	
	public EnergyReport getReport(Timestamp begin, Timestamp end, Integer type, Integer step){

		EnergyReport rep = new EnergyReport(begin, end, type, step);

		Float sitekw = 0f;
		Float sitekwh = 0f;
		Float sitekgco2 = 0f;
		Float sitecost = 0f;
		// time slot related data
		double anSiteKWh[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		double anSiteCost[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		
		Float otherkw = 0f;
		Float otherkwh = 0f;
		Float otherkgco2 = 0f;
		Float othercost = 0f;
		// time slot related data
		double anOtherKWh[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		double anOtherCost[] = new double[EnergyProfile.TIMESLOT_NO + 1];
		//main other
		//get report for the entire period
		//cycle on all existing groups
		for(Iterator<EnergyGroup> itr = energyconfig.getGroups().iterator(); itr.hasNext();){
			EnergyGroup g = itr.next();
			if( g.getId() == -1 || !g.isEnabled() )
				continue;

			//get report of group, in the specified time
			EnergyReport subrep = getGroupReport(g.getId(), begin, end, type, step);
			
			EnergyReportRecord subrec = subrep.getReportRecord("group"+g.getId());
			
			//sum values to obtain total counts of Kw, Kwh, Kgco2, Cost
			if(subrec!=null){
				sitekw += subrec.getKw();
				sitekwh += subrec.getKwh();
				sitekgco2 += subrec.getKgco2();
				sitecost += subrec.getCost();
				// time slot related data
				double anKWh[] = subrec.getKWhTS();
				double anCost[] = subrec.getCostTS();
				if( anKWh != null && anCost != null ) {
					for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
						anSiteKWh[i] += anKWh[i];
						anSiteCost[i] += anCost[i];
					}
				}
			}
			joinReports(subrep, rep, "");
		}
		if( energyconfig.getGroup(-1)==null || !energyconfig.getGroup(-1).isEnabled() || energyconfig.getGroup(-1).getConsumers().isEmpty() ) {
			rep.putReportRecord("site", new EnergyReportRecord(0, "---", sitekw, sitekwh, sitekgco2, sitecost, anSiteKWh, anSiteCost));
		} else {
			if( energyconfig.getGroup(-1) != null && energyconfig.getGroup(-1).isEnabled() && !energyconfig.getGroup(-1).getConsumers().isEmpty() ) {
				//get 'other' count  of Kw, Kwh, Kgco2, Cost
				// (N.B. all 'other*' variables refer to global site consumption
				// while 'site*' variables refer to all energymeter (sum) consumption)
				//EnergyConsumer ec = energyconfig.getGroup(-1).getConsumer(-1);
				EnergyGroup ec = energyconfig.getGroup(-1);
				otherkw = ec.getKw(begin, end, step);
				otherkwh = ec.getKwh(begin, end);
				otherkgco2 = ec.getKgCO2(otherkwh);
				othercost = ec.getCost(otherkwh);
				ec.getKWhCost(begin, end, anOtherKWh, anOtherCost);
				rep.putReportRecord("site", new EnergyReportRecord(0, "---", otherkw, otherkwh, otherkgco2, othercost, anOtherKWh, anOtherCost));
				double anKWh[] = new double[EnergyProfile.TIMESLOT_NO + 1];
				double anCost[] = new double[EnergyProfile.TIMESLOT_NO + 1];
				for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
					anKWh[i] = anOtherKWh[i] - anSiteKWh[i];
					anCost[i] = anOtherCost[i] - anSiteCost[i];
				}
				rep.putReportRecord("site", new EnergyReportRecord(0, "---", otherkw, otherkwh,	otherkgco2, othercost,
					anOtherKWh, anOtherCost));
				rep.putReportRecord("other", new EnergyReportRecord(0, "---", otherkw-sitekw, otherkwh-sitekwh,	otherkgco2-sitekgco2, othercost-sitecost,
					anKWh, anCost));
			}
		}
		
		//detailed other 
		//get detailed reports, one for each time "interval" (example: 7 reports for the week)
		Timestamp otherEndTs = null;
		Timestamp otherBeginTs = begin;
		
		for(Integer istep = 1; istep<=rep.getIntervalsNumber();istep++){
			Integer refgrpday = -1;
			sitekw = 0f;
			sitekwh = 0f;
			sitekgco2 = 0f;
			sitecost = 0f;
			anSiteKWh = new double[EnergyProfile.TIMESLOT_NO + 1];
			anSiteCost = new double[EnergyProfile.TIMESLOT_NO + 1];
			
			//report for each group
			for(Iterator<EnergyGroup> itr = energyconfig.getGroups().iterator(); itr.hasNext();){
				EnergyGroup g = itr.next();
				if( !g.isEnabled() )
					continue;
				refgrpday = g.getId();
				EnergyReportRecord subrec = rep.getReportRecord("d"+istep+".group"+g.getId());
				if(subrec!=null){
					sitekw += subrec.getKw();
					sitekwh += subrec.getKwh();
					sitekgco2 += subrec.getKgco2();
					sitecost += subrec.getCost();
					double anKWh[] = subrec.getKWhTS();
					double anCost[] = subrec.getCostTS();
					if( anKWh != null && anCost != null ) {
						for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
							anSiteKWh[i] += anKWh[i];
							anSiteCost[i] += anCost[i];
						}
					}
				}
			}
			/* removed in order to provide "Other" reports even if there is no group defined
			if(rep.getReportRecord("d"+istep+".group"+refgrpday)==null){
				continue;
			}
			*/
			//report for "other" group
			if( energyconfig.getGroup(-1)==null || !energyconfig.getGroup(-1).isEnabled() || energyconfig.getGroup(-1).getConsumers().isEmpty() ) {
				rep.putReportRecord("d"+istep+".site", new EnergyReportRecord(0, "---", sitekw, sitekwh, sitekgco2, sitecost, anSiteKWh, anSiteCost));
			} else {
				if( energyconfig.getGroup(-1) != null && energyconfig.getGroup(-1).isEnabled() && !energyconfig.getGroup(-1).getConsumers().isEmpty() ){
					//EnergyConsumer ec = energyconfig.getGroup(-1).getConsumer(-1);
					EnergyGroup ec = energyconfig.getGroup(-1);
					//FIX on detailed report calculation - Nicola Compagno 16/2/2010
					//calculate new  'end' timestamp (to get detailed report of period)
					otherEndTs = nextTimestamp(otherBeginTs, step);
					
					otherkw = ec.getKw(otherBeginTs, otherEndTs, step);
					otherkwh = ec.getKwh(otherBeginTs, otherEndTs);
					
					// time slot related data
					anOtherKWh = new double[EnergyProfile.TIMESLOT_NO + 1];
					anOtherCost = new double[EnergyProfile.TIMESLOT_NO + 1];
					ec.getKWhCost(otherBeginTs, otherEndTs, anOtherKWh, anOtherCost);
					double anKWh[] = new double[EnergyProfile.TIMESLOT_NO + 1];
					double anCost[] = new double[EnergyProfile.TIMESLOT_NO + 1];
					for(int i = 0; i <= EnergyProfile.TIMESLOT_NO; i++) {
						anKWh[i] = anOtherKWh[i] - anSiteKWh[i];
						anCost[i] = anOtherCost[i] - anSiteCost[i];
					}
					
					//shift report period to next step
					otherBeginTs = otherEndTs;
					//end FIX
					
					otherkgco2 = ec.getKgCO2(otherkwh);
					othercost = ec.getCost(otherkwh);
					
					rep.putReportRecord("d"+istep+".site", new EnergyReportRecord(0, "---", otherkw, otherkwh, otherkgco2, othercost, anOtherKWh, anOtherCost));
					rep.putReportRecord("d"+istep+".other", new EnergyReportRecord(0, "---", otherkw-sitekw, otherkwh-sitekwh, otherkgco2-sitekgco2, othercost-sitecost, anKWh, anCost));
				}
			}
		}
		return rep;
	}

	public EnergyReport getDailyReport() {
		GregorianCalendar begin = new GregorianCalendar();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		GregorianCalendar end = (GregorianCalendar)begin.clone();
		end.add(Calendar.HOUR_OF_DAY, 24);
		return getReport(new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.DAILY, EnergyReport.HOUR);
	}
	
	public EnergyReport getWeeklyReport(Integer weeknum,Integer year) {
		GregorianCalendar begin = new GregorianCalendar();
		//week number is calculated with ISO8601 standard (almost 4 days within the first week)
		begin.setMinimalDaysInFirstWeek(4);
		begin.setFirstDayOfWeek(Calendar.MONDAY);
		if(year ==null){
			year = begin.get(Calendar.YEAR);
		}
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.WEEK_OF_YEAR, weeknum);
		begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.DAY_OF_YEAR, 7);
		return getReport(new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.WEEKLY, EnergyReport.DAY);
	}
	public EnergyReport getWeeklyReport(Integer weeknum) {
		return getWeeklyReport(weeknum,null);
	}

	public EnergyReport getGroupDailyReport(Integer numgrp) {
		GregorianCalendar begin = new GregorianCalendar();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		GregorianCalendar end = (GregorianCalendar)begin.clone();
		end.add(Calendar.HOUR_OF_DAY, 24);
		return getGroupReport(numgrp, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.DAILY, EnergyReport.HOUR);
	}
	
	public EnergyReport getGroupWeeklyReport(Integer weeknum, Integer numgrp,Integer year) {
		GregorianCalendar begin = new GregorianCalendar();
		//week number is calculated with ISO8601 standard (almost 4 days within the first week)
		begin.setMinimalDaysInFirstWeek(4);
		begin.setFirstDayOfWeek(Calendar.MONDAY);
		if(year == null)
			year = begin.get(Calendar.YEAR);
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.WEEK_OF_YEAR, weeknum);
		begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.DAY_OF_YEAR, 7);
		return getGroupReport(numgrp, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.WEEKLY, EnergyReport.DAY);
	}

	public EnergyReport getConsDailyReport(Integer numgrp, Integer numcons) {
		GregorianCalendar begin = new GregorianCalendar();
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		GregorianCalendar end = (GregorianCalendar)begin.clone();
		end.add(Calendar.HOUR_OF_DAY, 24);
		return getConsReport(numgrp, numcons, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.DAILY, EnergyReport.HOUR);
	}
	
	public EnergyReport getConsWeeklyReport(Integer weeknum, Integer numgrp, Integer numcons) {
		GregorianCalendar begin = new GregorianCalendar();
		//week number is calculated with ISO8601 standard (almost 4 days within the first week)
		begin.setMinimalDaysInFirstWeek(4);
		begin.setFirstDayOfWeek(Calendar.MONDAY);
		int year = begin.get(Calendar.YEAR);
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.WEEK_OF_YEAR, weeknum);
		begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.DAY_OF_YEAR, 7);
		return getConsReport(numgrp, numcons, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.WEEKLY, EnergyReport.DAY);
	}

	public EnergyReport getMonthlyReport(Integer month,Integer year) {
		GregorianCalendar begin = new GregorianCalendar();
		if(year==null){
			year = begin.get(Calendar.YEAR);
		}
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.MONTH, month-1);
		begin.set(Calendar.DAY_OF_MONTH, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.MONTH, 1);
		return getReport(new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.MONTHLY, EnergyReport.DAY);
	}
	
	public EnergyReport getMonthlyReport(Integer month) {
		return getMonthlyReport(month,null);
	}
	
	public EnergyReport getGroupMonthlyReport(Integer month, Integer numgrp,Integer year) {
		GregorianCalendar begin = new GregorianCalendar();
		if(year == null)
			year = begin.get(Calendar.YEAR);
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.MONTH, month-1);
		begin.set(Calendar.DAY_OF_MONTH, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.MONTH, 1);
		return getGroupReport(numgrp, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.MONTHLY, EnergyReport.DAY);
	}
	
	public EnergyReport getConsMonthlyReport(Integer month, Integer numgrp, Integer numcons) {
		GregorianCalendar begin = new GregorianCalendar();
		int year = begin.get(Calendar.YEAR);
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.MONTH, month-1);
		begin.set(Calendar.DAY_OF_MONTH, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.MONTH, 1);
		return getConsReport(numgrp, numcons, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.MONTHLY, EnergyReport.DAY);
	}

	public EnergyReport getYearlyReport(Integer year) {
		GregorianCalendar begin = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.DAY_OF_YEAR, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.YEAR, 1);
		return getReport(new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.YEARLY, EnergyReport.MONTH);
	}

	public EnergyReport getGroupYearlyReport(Integer year, Integer numgrp) {
		GregorianCalendar begin = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.DAY_OF_YEAR, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.YEAR, 1);
		return getGroupReport(numgrp, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.YEARLY, EnergyReport.MONTH);
	}

	public EnergyReport getConsYearlyReport(Integer year, Integer numgrp, Integer numcons) {
		GregorianCalendar begin = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		begin.setTimeInMillis(0);
		begin.set(Calendar.YEAR, year);
		begin.set(Calendar.DAY_OF_YEAR, 1);
		EGUtils.setMidnight(begin);
		end.setTime(begin.getTime());
		end.add(Calendar.YEAR, 1);
		return getConsReport(numgrp, numcons, new Timestamp(begin.getTimeInMillis()), new Timestamp(end.getTimeInMillis()), EnergyReport.YEARLY, EnergyReport.MONTH);
	}

	public Timestamp nextTimestamp(Timestamp begints, int step) {
		GregorianCalendar gc = new GregorianCalendar();
		switch (step) {
		case 10000: // EnergyReport.HOUR
//			return new Timestamp(begints.getTime()+3600000l);
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.HOUR_OF_DAY, 1);
			return new Timestamp(gc.getTimeInMillis());
		case 10001: // EnergyReport.DAY
//			return new Timestamp(begints.getTime()+86400000l);
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.DAY_OF_MONTH, 1);
			return new Timestamp(gc.getTimeInMillis());
		case 10002: // EnergyReport.MONTH
//			GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.MONTH, 1);
			return new Timestamp(gc.getTimeInMillis());
		default:
			return null;
		}
	}
	
	
	public void setProperty(String key, String value)
	{
		//because column "value" has constraint not-null
		if(value == null)
			return;
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energysite where key = ?", new Object[] { key });
			if( rs.size() >= 1 )
				DatabaseMgr.getInstance().executeStatement(null, "update energysite set value=?, lastupdate=current_timestamp where key=?",
					new Object[] { value, key });
			else
				DatabaseMgr.getInstance().executeStatement(null, "insert into energysite values(?, ?, current_timestamp)",
					new Object[] { key, value });
			myproperties.setProperty(key, value);
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public EnergyProfile getEnergyProfile()
	{
		return energyProfile;
	}
	
	
	// retrieve consumer using device id
	public EnergyConsumer consumerLookup(int idDevice)
	{
		EnergyConsumer ec = energyconfig.getConsumer(-1);
		if( ec != null && ec.getIdDev() == idDevice ) {
			return ec;
		}
		else {
			List<EnergyConsumer> listCons = energyconfig.getConsumerList();
			for(int i = 0; i < listCons.size(); i++) {
				ec = listCons.get(i);
				if( ec.getIdDev() == idDevice ) {
					return ec;
				}
			}
		}
		return null;
	}
	
	
	// retrieve consumer using kWh variable id
	public EnergyConsumer consumerLookupByVariable(int idVariable)
	{
		List<EnergyConsumer> listCons = energyconfig.getConsumerList();
		for(int i = 0; i < listCons.size(); i++) {
			EnergyConsumer ec = listCons.get(i);
			if( ec.getIdkwh() == idVariable )
				return ec;
		}
		return null;
	}
}
