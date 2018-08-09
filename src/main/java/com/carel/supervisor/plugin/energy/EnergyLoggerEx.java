package com.carel.supervisor.plugin.energy;

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.Record;

// energy extended logger
// store meter records at the end of each hour
// in order to provide cost analysis based on EnergyProfile
public class EnergyLoggerEx {
	public static final int FLAG_METER			= 0x01;
	public static final int FLAG_COST			= 0x02;
	public static final int FLAG_TIMESLOT		= 0x04;
	public static final double NOT_AVAILABLE	= -1;
	public static final int IDVAR_TIMESLOT		= 0;	// idvariable used to keep time slot record
	
	EnergyProfile ep;
	List<EnergyConsumer> listConsumers;
	GregorianCalendar gcToday;
	int iTimeSlot;				// current time slot index
	EPTimeSlot objTimeSlot;		// current time slot
	int nPrevHour;				// previous hour; used to detect transition from hour to hour
	int nCurrentYear;			// current year; used to detect year transition (in order to update time intervals in EnergyProfile)
	private Hashtable<Integer, Double> objKWh; // cache meter readings from previous hour

	
	public EnergyLoggerEx(EnergyProfile ep, List<EnergyConsumer> listConsumers)
	{
		this.ep = ep;
		this.listConsumers = listConsumers;
		// initialize meter readings
		objKWh = new Hashtable<Integer, Double>();
		for(int i = 0; i < listConsumers.size(); i++) {
			EnergyConsumer ec = listConsumers.get(i);
			objKWh.put(ec.getIdkwh(), NOT_AVAILABLE);
		}
		init();
	}
	
	
	public void execute()
	{
		GregorianCalendar gcNow = new GregorianCalendar();
		checkEnergyProfile(gcNow.get(GregorianCalendar.YEAR));
		int nHour = gcNow.get(GregorianCalendar.HOUR_OF_DAY);
		if( nHour == nPrevHour ) {
			// update meter
			String sqlUpdateMeter = "update energylogex set h" + nHour
				+ "=?, lastupdate=current_timestamp where date=? and idvariable=? and flags=?";
			Date dt = new Date(gcToday.getTimeInMillis());
			// look for unavailable meters
			for(int i = 0; i < listConsumers.size(); i++) {
				EnergyConsumer ec = listConsumers.get(i);
				if( ec.isEnabled() && objKWh.get(ec.getIdkwh()) == NOT_AVAILABLE )
				try {
					double nKWh = ControllerMgr.getInstance().getFromField(ec.getIdkwh()).getCurrentValue();
					if( !Double.isNaN(nKWh) ) {
						try {
							DatabaseMgr.getInstance().executeStatement(null, sqlUpdateMeter,
									new Object[] { nKWh, dt, ec.getIdkwh(), FLAG_METER });
							objKWh.put(ec.getIdkwh(), nKWh);
						} catch(DataBaseException e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
						}
					}
				}
				catch(Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
			return;
		}
		
		// hour transition
		boolean bDayTransition = (nHour == 0 && nPrevHour == 23); // detect day transition
		Date dt = new Date(gcToday.getTimeInMillis());
		Date dt1 = null;
		if( bDayTransition ) {
			gcNow.set(GregorianCalendar.HOUR_OF_DAY, 0);
			gcNow.set(GregorianCalendar.MINUTE, 0);
			gcNow.set(GregorianCalendar.SECOND, 0);
			gcNow.set(GregorianCalendar.MILLISECOND, 0);
			initTimeSlotRecord((GregorianCalendar)gcNow.clone());
			dt1 = new Date(gcNow.getTimeInMillis());
		}
		
		// update meter for current hour
		String sqlUpdateMeter = "update energylogex set h" + (bDayTransition ? 24 : nHour)
		+ "=?, lastupdate=current_timestamp where date=? and idvariable=? and flags=?";
		// update cost for previous hour
		String sqlUpdateCost = "update energylogex set h" + nPrevHour
		+ "=?, lastupdate=current_timestamp where date=? and idvariable=? and flags=?";
		for(int i = 0; i < listConsumers.size(); i++) {
			EnergyConsumer ec = listConsumers.get(i);
			if( ec.isEnabled() )
			try {
				if( bDayTransition ) {
					// create daily records
					try {
						String sqlInsert = "insert into energylogex values(?, ?, ?, current_timestamp);";
						DatabaseMgr.getInstance().executeStatement(null, sqlInsert,
							new Object[] { dt1, ec.getIdkwh(), FLAG_METER });
						DatabaseMgr.getInstance().executeStatement(null, sqlInsert,
							new Object[] { dt1, ec.getIdkwh(), FLAG_COST });
					} catch(DataBaseException e) {
						LoggerMgr.getLogger(this.getClass()).error(e);
					}
				}

				// meter reading
				double nKWh = ControllerMgr.getInstance().getFromField(ec.getIdkwh()).getCurrentValue();
				if( !Double.isNaN(nKWh) ) {
					try {
						DatabaseMgr.getInstance().executeStatement(null, sqlUpdateMeter,
							new Object[] { nKWh, dt, ec.getIdkwh(), FLAG_METER });
						if( objKWh.get(ec.getIdkwh()) != NOT_AVAILABLE ) {
							double nKWhDelta = nKWh - objKWh.get(ec.getIdkwh());
							double nCost = nKWhDelta * objTimeSlot.getCost();
							LoggerMgr.getLogger(this.getClass()).info("cost " + nKWhDelta + " * " + objTimeSlot.getCost() + " = " + nCost);
							DatabaseMgr.getInstance().executeStatement(null, sqlUpdateCost,
								new Object[] { nCost, dt, ec.getIdkwh(), FLAG_COST });
						}
						if( bDayTransition ) {
							String sqlUpdate = "update energylogex set h0=?, lastupdate=current_timestamp"
								+ " where date=? and idvariable=? and flags=?";
							DatabaseMgr.getInstance().executeStatement(null, sqlUpdate,
								new Object[] { nKWh, dt1, ec.getIdkwh(), FLAG_METER });
						}
						objKWh.put(ec.getIdkwh(), nKWh);
					} catch(DataBaseException e) {
						LoggerMgr.getLogger(this.getClass()).error(e);
					}
				}
				else {
					objKWh.put(ec.getIdkwh(), NOT_AVAILABLE);
				}
			} catch(Exception e) {
				objKWh.put(ec.getIdkwh(), NOT_AVAILABLE);
				LoggerMgr.getLogger(this.getClass()).error(e);
            }
		}
		
		nPrevHour = nHour;
		iTimeSlot = ep.getTimeSlotIndex(gcNow);
		objTimeSlot = ep.getTimeSlot(iTimeSlot);
		if( bDayTransition )
			gcToday = gcNow;
		updateTimeSlotRecord(nHour, iTimeSlot);
	}
	
	
	private void init()
	{
		gcToday = new GregorianCalendar();
		iTimeSlot = ep.getTimeSlotIndex(gcToday);
		objTimeSlot = ep.getTimeSlot(iTimeSlot);
		int nHour = gcToday.get(GregorianCalendar.HOUR_OF_DAY);
		nCurrentYear = gcToday.get(GregorianCalendar.YEAR);
		gcToday.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gcToday.set(GregorianCalendar.MINUTE, 0);
		gcToday.set(GregorianCalendar.SECOND, 0);
		gcToday.set(GregorianCalendar.MILLISECOND, 0);
		initTimeSlotRecord((GregorianCalendar)gcToday.clone());
		Date dt = new Date(gcToday.getTimeInMillis());
		
		String sqlSelect = "select * from energylogex where date=? and idvariable=? and flags=?";
		String sqlInsert = "insert into energylogex values(?, ?, ?, current_timestamp);";
		for(int i = 0; i < listConsumers.size(); i++) {
			EnergyConsumer ec = listConsumers.get(i);
			if( ec.isEnabled() )
			try {
				Timestamp ts = null;
				while( ts == null ) {
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlSelect,
						new Object[] { dt, ec.getIdkwh(), FLAG_METER });
					if( rs.size() == 1 ) {
						Record r = rs.get(0);
						objKWh.put(ec.getIdkwh(), (Double)r.get("h" + nHour));
						ts = (Timestamp)r.get("lastupdate");
						if( objKWh.get(ec.getIdkwh()) == NOT_AVAILABLE ) {
							// update hour record
							String sqlUpdate = "update energylogex set h" + nHour
								+ "=?, lastupdate=current_timestamp where date=? and idvariable=? and flags=?";
							try {
								double nKWh = ControllerMgr.getInstance().getFromField(ec.getIdkwh()).getCurrentValue();
								if( Double.isNaN(nKWh) )
									nKWh = NOT_AVAILABLE;
								DatabaseMgr.getInstance().executeStatement(null, sqlUpdate,
									new Object[] { nKWh, dt, ec.getIdkwh(), FLAG_METER });
								objKWh.put(ec.getIdkwh(), nKWh);
							} catch(Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e); // data not available
				            }
						}
					}
					else {
						// create daily records
						DatabaseMgr.getInstance().executeStatement(null, sqlInsert,
							new Object[] { dt, ec.getIdkwh(), FLAG_METER });
						DatabaseMgr.getInstance().executeStatement(null, sqlInsert,
								new Object[] { dt, ec.getIdkwh(), FLAG_COST });
					}
				}
			} catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		nPrevHour = nHour;
	}
	
	
	private void initTimeSlotRecord(GregorianCalendar cal)
	{
		try {
			Date dt = new Date(cal.getTimeInMillis());
			String sql = "select * from energylogex where date=? and idvariable=? and flags=?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
					new Object[] { dt, IDVAR_TIMESLOT, FLAG_TIMESLOT });
			if( rs.size() != 1 ) {
				String sqlInsert = "insert into energylogex values(?, ?, ?, current_timestamp,"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object params[] = new Object[27];
				params[0] = dt;
				params[1] = IDVAR_TIMESLOT;
				params[2] = FLAG_TIMESLOT;
				for(int iHour = 0; iHour < 24; iHour++) {
					cal.set(GregorianCalendar.HOUR_OF_DAY, iHour);
					params[iHour + 3] = ep.getTimeSlotIndex(cal);
				}
				DatabaseMgr.getInstance().executeStatement(null, sqlInsert, params);
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void updateTimeSlotRecord(int nHour, int iTimeSlot)
	{
		try {
			String sqlUpdate = "update energylogex set h" + nHour
			+ "=?, lastupdate=current_timestamp where date=? and idvariable=? and flags=?";
			Date dt = new Date(gcToday.getTimeInMillis());
			DatabaseMgr.getInstance().executeStatement(null, sqlUpdate,
					new Object[] { iTimeSlot, dt, IDVAR_TIMESLOT, FLAG_TIMESLOT });
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void checkEnergyProfile(int nYear)
	{
		if( nYear != nCurrentYear ) {
			// EnergyProfile expired; needs to be reloaded
			EnergyMgr.getInstance().load();
			nCurrentYear = nYear;
		}
	}
}
