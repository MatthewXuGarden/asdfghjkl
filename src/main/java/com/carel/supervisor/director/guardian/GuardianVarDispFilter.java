package com.carel.supervisor.director.guardian;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.controller.time.TimeValidityFactory;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class GuardianVarDispFilter 
{
	private static GuardianVarDispFilter gvdf = new GuardianVarDispFilter();
	
	private Map<Integer,Integer> filter = null;
	private Map<Integer,Integer> priority = null;
	private TimeBandList tbl = null;
	private boolean enableAllAlarms = false;
	
	/*
	 * Metodo richiamato in reflection da
	 * com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl
	 */
	public static boolean verifyVariable(Integer idvar,Integer prio) {
		return GuardianVarDispFilter.getInstance().checkVariable(idvar, prio, 
																new java.util.Date(System.currentTimeMillis()));
	}
	
	private GuardianVarDispFilter() {
		load();
	}
	
	public static GuardianVarDispFilter getInstance() {
		return gvdf;
	}
	
	public void relaod() {
		load();
	}
	
	public boolean isAllAlarmsEnable() {
		return this.enableAllAlarms;
	}
	
	public boolean checkVariable(Integer idvar,Integer prio,Date now)
	{
		boolean ris = false;
		// Controllo se abilitata regola di default
		if(enableAllAlarms)
		{
			// Controllo se siamo in fascia
			ris = innerCheckVariable(0,prio,now);
			// Nel caso in cui la regola di default esista ma non è in fascia, controllo la variabile
			if(!ris)
				ris = innerCheckVariable(idvar,prio,now);
		}
		else
		{
			// Controllo la variabile se è in fascia
			ris = innerCheckVariable(idvar,prio,now);
		}
		return ris;
	}
	
	private boolean innerCheckVariable(int idvar,int prio,Date now)
	{
		TimeBandBean tbb = null;
		TimeValidity tv = null;
		Integer idtime = null;
		boolean ris = false;
		// Controllo se id variable contenuto nel filtro
		if(filter.containsKey(new Integer(idvar)))
		{
			// Recupero il suo timeband
			idtime = filter.get(new Integer(idvar));
			tbb = tbl.get(idtime);
			if(tbb != null)
			{
				tv = TimeValidityFactory.createTime(tbb);
				// Verifico se in fascia
				if(tv != null)
					ris = tv.isValid(now);
			}
		}
		if(!ris)
		{
			// Potrebbe essere una regola per priorità
			if(priority.containsKey(new Integer(prio)))
			{
				idtime = priority.get(new Integer(prio));
				tbb = tbl.get(idtime);
				if(tbb != null)
				{
					tv = TimeValidityFactory.createTime(tbb);
					if(tv != null)
						ris = tv.isValid(now);
				}
			}
		}
		return ris;
	}
	
	/*
	 * Recupero la lista delle variabili e i relativi timeband di riferimento.
	 */
	private void load()
	{
		/*
		 * Recupero:
		 * - id variable
		 * - id timeband
		 * - codetype [V,P]
		 */
		String sql = 
		"select idvariable,UNO.idtimeband,UNO.condtype from cfvarcondition as v," +
		"(select c.idcondition,r.idtimeband,c.condtype from cfrule as r,cfcondition as c where r.isenabled='TRUE' " +
		"and (c.condtype='V' or c.condtype='P') and r.idcondition=c.idcondition) as UNO where v.idcondition=UNO.idcondition";

		// Variabile di stato per attivazione condizione ALL ALARMS
		enableAllAlarms = false;
		// Oggetti memoria
		filter = new HashMap<Integer, Integer>();
		priority = new HashMap<Integer, Integer>();
		try 
		{
			Integer idvar = null;
			Integer idtib = null;
			String type = "";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if(rs != null)
			{
				Record r = null;
				// Per ogni riga recupearata
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					// Controllo il tipo di condizione, se su variabile o su priorità
					type = UtilBean.trim(r.get("condtype"));
					// se variabile
					if(type != null && type.equalsIgnoreCase("V"))
					{
						// Recupero id variabile
						idvar = (Integer)r.get("idvariable");
						// Se zero allora è ALL ALARMS
						if(idvar.intValue() == 0)
							enableAllAlarms = true;
						// Recuper timeband
						idtib = (Integer)r.get("idtimeband");
						// Metto in memoria
						this.filter.put(idvar,idtib);
					}
					else if(type != null && type.equalsIgnoreCase("P"))
					{
						// Priorità
						idvar = (Integer)r.get("idvariable");
						idtib = (Integer)r.get("idtimeband");
						this.priority.put(idvar,idtib);
					}
				}
			}
			// Load Timeband
			this.tbl = new TimeBandList(null,"firstPV",1);
		} 
		catch (Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
	}
}
