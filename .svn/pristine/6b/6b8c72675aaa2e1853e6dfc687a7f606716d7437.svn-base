package com.carel.supervisor.plugin.energy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class EnergyEngine extends Poller {

	public int contatorenumeri = 0;
	private static int contathread = 0;
	private HashMap<Integer, EnergyEngine.EngineRecord> helpmap;
	
	private EnergyLoggerEx loggerEx;

	public EnergyEngine() {
		super();
		setName("EnergyEngine_" + (contathread++));
		contatorenumeri = 0;
		helpmap = new HashMap<Integer, EngineRecord>();
		loggerEx = null;
	}

	public void run() {
		Long l = null;
		try {
			
			// execute cycle every "initsleep" mseconds (see 'energysite' table)
			l = new Long(EnergyMgr.getInstance().getIntegerProperty("initsleep", 600000));
			Thread.sleep(l);
		} catch (Exception e) {
		}
		loggerEx = new EnergyLoggerEx(EnergyMgr.getInstance().getEnergyProfile(),
			EnergyMgr.getInstance().getSiteConfiguration().getConsumerList());
		while (this.isStarted()) {
			try {
				
				// execute cycle every "initsleep" mseconds (see 'energysite' table)
				l = new Long(EnergyMgr.getInstance().getIntegerProperty("initsleep", 600000));

				synchronized (EnergyMgr.enginemonitor) {
					// load data from energylogsupport table (contains timestamp of
					// 'firstrecord', 'lastrecord', 'firstsample', 'lastsample')
					initHelpMap();
					
					// save data on energylog table
					saveEnergyLog();
					
					// update data on energylogsupport table
					saveHelpMap();
					
					// execute extended logger
					if( EnergyMgr.getInstance().getStringProperty("active_cfg").equals("time_slot") )
						loggerEx.execute();
				}
 
				Long totsleep = 0l;
				while(totsleep<l && this.isStarted()){
					totsleep+=2000l;
					Thread.sleep(2000l);
				}
				
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
	}

	private void saveHelpMap() {
		for(Iterator<Integer> itr = helpmap.keySet().iterator();itr.hasNext();){
			Integer key = itr.next();
			if( key != null ) {
				try {
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select count(idvariable) from energylogsupport where idvariable=" + key);
					if( (Integer)rs.get(0).get(0) != 0 )
						DatabaseMgr.getInstance().executeStatement(
							"update energylogsupport set firstrecord=?, lastrecord=?, firstsample=?, lastsample=? where idvariable=?;",
							new Object[] {
									helpmap.get(key).firstrecord,
									new Timestamp(helpmap.get(key).getLastrecord().getTime()-60000),
									helpmap.get(key).getFirstsample(),
									helpmap.get(key).getLastsample(),
									key }
						);
					else
						DatabaseMgr.getInstance().executeStatement(null,
							"insert into energylogsupport values (?,?,?,?,?);",
							new Object[] {
								key,
								helpmap.get(key).getFirstrecord(),
								new Timestamp(helpmap.get(key).getLastrecord().getTime() - 60000),
								helpmap.get(key).getFirstsample(),
								helpmap.get(key).getLastsample() },
						false);
				}
				catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}
	}

	private void initHelpMap() {
		helpmap = new HashMap<Integer, EngineRecord>();
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
					"select * from energylogsupport");
			for (int i = 0; i < rs.size(); i++) {
				helpmap.put((Integer) rs.get(i).get("idvariable"),
					new EnergyEngine.EngineRecord((Integer) rs.get(i).get("idvariable"),
							(Timestamp) rs.get(i).get("firstrecord") != null ? (Timestamp) rs.get(i).get("firstrecord")	: new Timestamp(0),
							(Timestamp) rs.get(i).get("lastrecord") != null ? (Timestamp) rs.get(i).get("lastrecord") : new Timestamp(0),
							(Timestamp) rs.get(i).get("firstsample") != null ? (Timestamp) rs.get(i).get("firstsample") : new Timestamp(0),
							(Timestamp) rs.get(i).get("lastsample") != null ? (Timestamp) rs.get(i).get("lastsample") : new Timestamp(0)));
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

	}

	private void saveEnergyLog() 
	{
		Connection c = null;
		for (Iterator<EnergyConsumer> itr = EnergyMgr.getInstance().getSiteConfiguration().getConsumerList().iterator(); itr.hasNext();) {
			
			EnergyConsumer ec = itr.next();
			if (helpmap.get(ec.getIdkwh()) == null) 
			{
				helpmap.put(ec.getIdkwh(), new EngineRecord(ec.getIdkwh(), new Timestamp(0), new Timestamp(0), new Timestamp(0), new Timestamp(0)));
			}
			try {
				// retrieve data for analysis
				c = DatabaseMgr.getInstance().getConnection(null);

				
				//select data from hsvarhistor, where inserrtime > 'lastrecord' timestamp
				StringBuffer sb = new StringBuffer();
				sb.append("select hsvarhistor.* from hsvarhistor,cfvariable where cfvariable.idvariable = "); 
				sb.append(ec.getIdkwh());
				sb.append(" and hsvarhistor.idvariable=cfvariable.idvariable ");
				sb.append(" and hsvarhistor.inserttime>='"
						+ helpmap.get(ec.getIdkwh()).getLastrecord()
						+ "' order by inserttime limit "
						+ (EnergyMgr.getInstance().getIntegerProperty("querylimit", 20)) + ";");
				Statement s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = s.executeQuery(sb.toString());

				// parse query
				LinkedHashMap<Integer, LinkedHashMap<Timestamp, Float>> valuesmap = getValues(rs);

				if (valuesmap == null || valuesmap.size() == 0) {
					c.close();
					continue; // continue with the next consumer
				}
				
				// process valuesmap
				// allocation
				Boolean virtual = false;
				Timestamp t1;
				Timestamp tfirst;
				Timestamp t2;
				Timestamp tx = new Timestamp(System.currentTimeMillis());
				Long x = 0l;
				Long x1 = 0l;
				Long x2 = 0l;
				Float v1 = 0f;
				Float vfirst;
				Float v2 = 0f;
				Float roundvalue = 0f;
				Boolean keepfirst = false;
				
				// for each variable's map
				for (Iterator<Integer> itrvariables = valuesmap.keySet().iterator(); itrvariables.hasNext();) 
				{
					//questo giro lo fa una volta sola
					t1 = null;
					tfirst = null;
					t2 = null;
					v1 = Float.NaN;
					vfirst = null;
					v2 = Float.NaN;
					Integer idvar = itrvariables.next();

					try { 
						// get last good sample from log
						ResultSet rss = s.executeQuery("select * from energylog where idvariable= " + idvar
										+ " and tstamp>=(select max(tstamp)-interval '1 min' "
										+ " from energylog where idvariable= " + idvar + " );");
						if (!rss.next()) 
						{
							// log has no first sample, new variable
							tfirst = null;
						} else {
							// found
							tfirst = rss.getTimestamp("tstamp");
							Boolean wasNull = rss.wasNull();
							vfirst = rss.getFloat("value");
							wasNull = wasNull.booleanValue() || rss.wasNull();  
							if (wasNull || vfirst.equals(Float.NaN) || vfirst.isNaN() || vfirst.equals(0f)) {
								tfirst = null;
							}
						}
					} catch (Exception e) {
						// error -> assume no sample
						tfirst = null;
					}
					
					LinkedHashMap<Timestamp, Float> map = valuesmap.get(idvar); //unico elemento
					// iterator of timestamp-values
					Iterator<Timestamp> itrvalues = map.keySet().iterator();
					if (!itrvalues.hasNext())
						continue; // no data
					
					if(tfirst==null)
					{ //new energy log
						t1 = itrvalues.next();
						
						// if there isn't any sample go ahead to the first good
						// sample					
						while (itrvalues.hasNext() && 
								(t1==null || map.get(t1)==null || map.get(t1).equals(Float.NaN) || map.get(t1).isNaN() || map.get(t1).equals(0f))) 
						{
							t1 = itrvalues.next();
						}
						// if i can't find a sample through all the map i skip the
						// variable
						if (map.get(t1) != null && 
								(map.get(t1).equals(Float.NaN) || map.get(t1).isNaN() || map.get(t1).equals(0f))) 
						{
							continue;
						}
					} 
					else 
					{
						t1 = tfirst;
						v1 = vfirst;
						keepfirst = true;
					}

					// analysing
					while (itrvalues.hasNext()) 
					{
						// look for the first end sample != nan
						t2 = itrvalues.next();
						while ((map.get(t2).equals(Float.NaN) || map.get(t2).isNaN() || map.get(t2).equals(0f)) 
								&& itrvalues.hasNext()) 
						{
							t2 = itrvalues.next();
						}
						if (map.get(t2).equals(Float.NaN) || map.get(t2).isNaN() || map.get(t2).equals(0f)) 
						{
							// end of map, skip
							continue;
						}
						x1 = t1.getTime();
						if(x1 % 3600000==0)
						{
							x = x1;
						}else
						{
							x = x1 - (x1 % 3600000) + 3600000;
						}
						tx.setTime(x);
						x2 = t2.getTime();
						while (x < x2) 
						{
							virtual = new Boolean((Math.abs((x1 % 3600000) - 3600000) > 180000)
									&& (Math.abs(x2 % 3600000) > 180000) && (x1%360000!=0));
							if(!keepfirst)
							{
								v1 = map.get(t1);
							}
							v2 = map.get(t2);
							
							roundvalue = (v2 - v1) * (x - x1) / (x2 - x1) + v1;
							tx.setTime(x);
							
							if(roundvalue!=null && !roundvalue.isNaN() && !roundvalue.equals(Float.NaN)) {
								helpmap.get(idvar).setFirstsample(tx);
								helpmap.get(idvar).setLastsample(tx);
								
								// check if the record already exists in the log
								boolean bExists = false;
								try {
									ResultSet rsSelect = s.executeQuery("SELECT * FROM energylog WHERE idvariable = " + idvar + " AND tstamp = '" + tx + "';");
									rsSelect.last();
									bExists = rsSelect.getRow() > 0;
								}
								catch(SQLException e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
								}
								
								try {
									if( bExists ) {
										if (!virtual) 
										{
											String sql = " update energylog set tstamp='"
													+ tx
													+ "', "
													+ " value="
													+ (roundvalue.equals(Float.NaN) ? "null" : roundvalue)
													+ " , "
													+ " virtual="
													+ virtual
													+ ", "
													+ "missed=false where idvariable="
													+ idvar
													+ " and tstamp='"
													+ tx
													+ "';";
											s.executeUpdate(sql);
										}
									}
									else {
										String sql = "insert into energylog values ("
											+ idvar
											+ ",'"
											+ tx
											+ "',"
											+ (roundvalue.equals(Float.NaN) ? "null" : roundvalue) + "," + virtual
											+ " , false);";
										s.executeUpdate(sql);
									}
								}
								catch(Exception e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
								}
								Thread.sleep(1l);
							}
							x += 3600000;
						}
						t1 = t2;
						keepfirst=false;
					}
				}
				c.commit();
				c.close();
			} catch (Exception e) 
			{
				try 
				{
					c.rollback();
					c.close();
				} catch (Exception ee) {
				}
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
	}

	private LinkedHashMap<Integer, LinkedHashMap<Timestamp, Float>> getValues(ResultSet rs_energy) 
	{
		LinkedHashMap<Integer, LinkedHashMap<Timestamp, Float>> hm = new LinkedHashMap<Integer, LinkedHashMap<Timestamp, Float>>();
		Integer idvar = null;
		Timestamp ts = null;

		Float val;
		try {
			while (rs_energy.next()) 
			{
				idvar = rs_energy.getInt("idvariable");
				if (hm.get(idvar) == null) 
				{
					hm.put(idvar, new LinkedHashMap<Timestamp, Float>());
				}
				
				ts = (Timestamp) rs_energy.getTimestamp("inserttime");
				helpmap.get(idvar).setFirstrecord(ts);
				val = rs_energy.getFloat("value");
				if (!rs_energy.wasNull())
					hm.get(idvar).put(ts, val);
				else
					hm.get(idvar).put(ts, Float.NaN);
				long freq = 1000L * rs_energy.getInt("frequency");
				int acc = 0;
				boolean cont = true;
				for (int j = 1; (j <= 63) && (cont == true); j++) 
				{
					try 
					{
						long a = new Long(rs_energy.getInt("n" + j));
						acc += a;
						val = rs_energy.getFloat("value" + j);
						if (rs_energy.wasNull())
							val = Float.NaN;
						if (j == 63) 
						{
							cont = false;
							continue;
						}
						long nexta = new Long(rs_energy.getInt("n" + (j + 1)));
						hm.get(idvar).put(new Timestamp(ts.getTime() + freq * acc),	nexta != 0f ? val : Float.NaN);
						if (nexta == 0) 
						{
							cont = false;
							continue;
						}
					} catch (SQLException e)
					{
						LoggerMgr.getLogger(EnergyMgr.class).error(e);
					}
				}
				helpmap.get(idvar).setLastrecord(ts);
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(EnergyMgr.class).error(e);
		}
		return hm;
	}

	class EngineRecord {
		private Integer idvar;
		private Timestamp firstrecord;
		private Timestamp lastrecord;
		private Timestamp firstsample;
		private Timestamp lastsample;

		public EngineRecord(Integer idvar, Timestamp firstrecord,
				Timestamp lastrecord, Timestamp firstsample,
				Timestamp lastsample) {
			this.idvar = idvar;
			this.firstrecord = firstrecord;
			this.lastrecord = lastrecord;
			this.firstsample = firstsample;
			this.lastsample = lastsample;
		}

		public Integer getIdvar() {
			return idvar;
		}

		public Timestamp getFirstrecord() {
			return firstrecord;
		}

		public void setFirstrecord(Timestamp ts) {
			if (ts == null)
				return;
			if ((firstrecord == null && ts != null)
					|| firstrecord.getTime() < 1 || ts.before(firstrecord)) {
				firstrecord = ts;
			}
		}

		public Timestamp getLastrecord() {
			return lastrecord;
		}

		public void setLastrecord(Timestamp ts) {
			if (ts == null)
				return;
			if ((lastrecord == null && ts != null) || ts.after(lastrecord)) {
				lastrecord = ts;
			}
		}

		public Timestamp getFirstsample() {
			return firstsample;
		}

		public void setFirstsample(Timestamp ts) {
			if (ts == null)
				return;
			if ((firstsample == null && ts != null)
					|| firstsample.getTime() < 1l || ts.before(firstsample)) {
				firstsample = new Timestamp(ts.getTime());
			}
		}

		public Timestamp getLastsample() {
			return lastsample;
		}

		public void setLastsample(Timestamp ts) {
			if (ts == null)
				return;
			if ((lastsample == null && ts != null) || ts.after(lastsample)) {
				lastsample = new Timestamp(ts.getTime());
			}
		}

		public String toString(){
			return ""+idvar+"-("+firstrecord+","+lastrecord+","+firstsample+","+lastsample+")";
		}
	}
}
