package com.carel.supervisor.director;

import java.sql.Timestamp;
import java.util.Calendar;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class TimeControl {
	
	public static final String USER="user";    // cambio utente
	public static final String DST="dst";      // Daylight Saving Time cambio dell'ora solare
	public static final String OTHER="other";  // Mantiene retrocompatibilità adesso non server e sottolineo server più anzi server ancora ma...
	
	public static final int NO_CHANCHED=0;
	
	public static final int USER_CHANGE_TIME_FORWARD=1;
	public static final int USER_CHANGE_TIME_BACK=2;
	public static final int DST_CHANGE_TIME_FORWARD=0;
	public static final int DST_CHANGE_TIME_BACK=0;
	
	
	private static final long CLOCK_PRECISION_FORWARD=300000L;   //tempo in millisecondi oltre il quale considero lo spostamento dell'orologio in avanti fenomeno causato dall'utente in avanti
	private static final long CLOCK_PRECISION_BACK=-180000L;      //tempo in millisecondi oltre il quale considero lo spostamento dell'orologio in dietro fenomeno causato dall'utente in avanti
	
	private static int DTS_STATUS=Calendar.getInstance().get(Calendar.DST_OFFSET);
	
	private static int WARNING=0;
	private static int INFO=1;
	
	
	public static int checkChangeClock(long oldTime,long newTime,long freqGCD) throws DataBaseException{
		if (newTime - oldTime - (freqGCD * 1000L) > CLOCK_PRECISION_FORWARD){
			DTS_STATUS=Calendar.getInstance().get(Calendar.DST_OFFSET);  
			Object []objects= new Object []{SeqMgr.getInstance().next(null,"hstimecontrol","idtimecontrol"),
						new Timestamp(newTime), new Timestamp(oldTime),USER };
				DatabaseMgr.getInstance().executeStatement(null,"INSERT INTO hstimecontrol VALUES(?,?,?,?);",objects);
				eventLog(new Timestamp(newTime),new Timestamp(oldTime),WARNING);
			  return USER_CHANGE_TIME_FORWARD;
		  }//if 
		else if (newTime - oldTime - (freqGCD * 1000L) < CLOCK_PRECISION_BACK){
			DTS_STATUS=Calendar.getInstance().get(Calendar.DST_OFFSET);
			Object []objects= new Object []{SeqMgr.getInstance().next(null,"hstimecontrol","idtimecontrol"),
						new Timestamp(newTime), new Timestamp(oldTime),USER };
			DatabaseMgr.getInstance().executeStatement(null,"INSERT INTO hstimecontrol VALUES(?,?,?,?);",objects);
			eventLog(new Timestamp(newTime),new Timestamp(oldTime),WARNING);
			 return USER_CHANGE_TIME_BACK;
		  }//if 
		else if(DTS_STATUS!=Calendar.getInstance().get(Calendar.DST_OFFSET)){
			DTS_STATUS=Calendar.getInstance().get(Calendar.DST_OFFSET);
		
			Object []objects= new Object []{SeqMgr.getInstance().next(null,"hstimecontrol","idtimecontrol"),
					new Timestamp(newTime), new Timestamp(oldTime),DST };
			DatabaseMgr.getInstance().executeStatement(null,"INSERT INTO hstimecontrol VALUES(?,?,?,?);",objects);
			eventLog(new Timestamp(newTime),new Timestamp(oldTime),INFO);
			if(DTS_STATUS==0)
				return DST_CHANGE_TIME_BACK;
			return DST_CHANGE_TIME_FORWARD;

		}//if
		return NO_CHANCHED;
		
	}//checkChangeClock
	
	
	private static void eventLog(Timestamp newTime,Timestamp oldTime,int type){
		try {
			EventMgr.getInstance().log(new Integer(1), "System", "Action",
					type==0?EventDictionary.TYPE_WARNING:EventDictionary.TYPE_INFO, type==0?"S041":"S042",
                    new Object[]{DateUtils.date2String(newTime,"yyyy-MM-dd hh:mm:ss"),DateUtils.date2String(oldTime,"yyyy-MM-dd hh:mm:ss")});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}  
	}//eventLog
	
}//TimeControl
