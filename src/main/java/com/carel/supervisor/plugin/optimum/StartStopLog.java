package com.carel.supervisor.plugin.optimum;

import java.text.SimpleDateFormat;
import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;

import java.sql.*;

import org.jfree.data.category.DefaultCategoryDataset;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;


public class StartStopLog {
	// logged parameters
	public static final int LOG_UnitOnOff				= 0;
	public static final int LOG_InternalTemperature		= 1; 
	public static final int LOG_ExternalTemperature		= 2; 
	public static final int LOG_AnticipatedTimeStart	= 3; 
	public static final int LOG_AnticipatedTimeStop		= 4;
	public static final int nUNIT_ON = 1;
	public static final int nUNIT_OFF = 0;
	// labels
	public static final String LABEL_ON					= " ON";
	public static final String LABEL_OFF				= " OFF";
	// graph colors
	public static final String COL_InternalTemperature	= "#FF8000"; // orange 
	public static final String COL_ExternalTemperature	= "#0000FF"; // blue
	public static final String COL_AnticipatedTimeStart	= "#FF0000"; // red
	public static final String COL_AnticipatedTimeStop	= "#00FF00"; // green
	
	
	public static void logParameters(
			int idAlgorithm,
			int nUnitOnOff,
			float fInternalTemperature,
			float fExternalTemperature,
			int nAnticipatedTimeStart,
			int nAnticipatedTimeStop)
	{
		// use the same timestamp for all parameters
		java.sql.Timestamp timestamp = new java.sql.Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
		String sql = "insert into opt_startstop_log values(?, ?, ?, ?);";
		try {
			
			if(nUnitOnOff==nUNIT_ON)
				DatabaseMgr.getInstance().executeMultiStatement(null, sql, new Object[][] {
						{ timestamp, idAlgorithm, LOG_UnitOnOff, (float)nUnitOnOff },
						{ timestamp, idAlgorithm, LOG_InternalTemperature, fInternalTemperature },
						{ timestamp, idAlgorithm, LOG_ExternalTemperature, fExternalTemperature },
						{ timestamp, idAlgorithm, LOG_AnticipatedTimeStart, (float)nAnticipatedTimeStart },
					});
			if(nUnitOnOff==nUNIT_OFF)
				DatabaseMgr.getInstance().executeMultiStatement(null, sql, new Object[][] {
						{ timestamp, idAlgorithm, LOG_UnitOnOff, (float)nUnitOnOff },
						{ timestamp, idAlgorithm, LOG_InternalTemperature, fInternalTemperature },
						{ timestamp, idAlgorithm, LOG_ExternalTemperature, fExternalTemperature },
						{ timestamp, idAlgorithm, LOG_AnticipatedTimeStop, (float)nAnticipatedTimeStop },
					});
			
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopLog").error(e);
		}
	}
	
	
	// return string array with names of logged parameters
	public static String[] getLogParamNames(String language)
	{
		LangService lang = LangMgr.getInstance().getLangService(language);
		String[] astrNames = new String[] {
			lang.getString("opt_startstop", "UnitOnOff"),
			lang.getString("opt_startstop", "InternalTemperature"),
			lang.getString("opt_startstop", "ExternalTemperature"),
			lang.getString("opt_startstop", "AnticipatedTimeStart"),
			lang.getString("opt_startstop", "AnticipatedTimeStop")
		};
		return astrNames;
	}

	
	public static Timestamp getFirstTimestamp()
	{
		try {
			String sql = "select min(timestamp) from opt_startstop_log;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() > 0 )
				return (Timestamp)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopLog").error(e);
		}
		
		return null;
	}


	public static Timestamp getLastTimestamp()
	{
		try {
			String sql = "select max(timestamp) from opt_startstop_log;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() > 0 )
				return (Timestamp)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopLog").error(e);
		}
		
		return null;
	}
	
	
	// type: daily, weekly, yearly
	// outputs: dataA, dataB
	public static void retriveDataSets(int idAlgorithm, Calendar calBegin, Calendar calEnd, String type, String language, DefaultCategoryDataset dataA, DefaultCategoryDataset dataB)
	{
		String[] astrNames = getLogParamNames(language);
		SimpleDateFormat dtFormatA = new SimpleDateFormat("dd");
		SimpleDateFormat dtFormatB = new SimpleDateFormat("dd");
		Timestamp tsBegin = new Timestamp(calBegin.getTimeInMillis());
		Timestamp tsEnd = new Timestamp(calEnd.getTimeInMillis());
		
		// variables used to compute average values
		boolean bAvg = true;
		int iAvg = 0;
		int[] anAvgInternalTemperatureStart = new int[53]; // 53 weeks / year or 31 days / month
		int[] anAvgInternalTemperatureStop = new int[53]; // 53 weeks / year or 31 days / month
		int[] anAvgExternalTemperatureStart = new int[53];
		int[] anAvgExternalTemperatureStop = new int[53];
		int[] anAvgAnticipatedTimeStart = new int[53];
		int[] anAvgAnticipatedTimeStop = new int[53];
		
		// init data sets
		if( type.equals("yearly") ) {
			for(int i = 1; i < anAvgInternalTemperatureStart.length; i++)
				addZero(astrNames, dataA, String.valueOf(i), dataB, String.valueOf(i));
		}
		else {
			Calendar cal = (Calendar)calBegin.clone();
			while( cal.before(calEnd) ) {
				addZero(astrNames, dataA, dtFormatA.format(cal.getTime()), dataB, dtFormatB.format(cal.getTime()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		
		// retrieve data sets
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_startstop_log where timestamp >= ? and timestamp < ? and idalg = ? order by timestamp, idparam",
				new Object[] {
					tsBegin, tsEnd, idAlgorithm
				}
			);
			String strColA = null;
			String strColB = null;
			int nUnitOnOff = 0;

			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idParam = (Integer)r.get("idparam");
				String strName = astrNames[idParam];
				Float fValue = (Float)r.get("value");
				if( idParam == StartStopLog.LOG_UnitOnOff ) {
					Timestamp ts = (Timestamp)r.get("timestamp");
					nUnitOnOff = fValue != 0 ? 1 : 0;
					if( bAvg && "yearly".equals(type) ) {
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeInMillis(ts.getTime());
						iAvg = cal.get(GregorianCalendar.WEEK_OF_YEAR);
						strColA = String.valueOf(iAvg);
						strColB = String.valueOf(iAvg);
					}
					else if( bAvg ) {
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeInMillis(ts.getTime());
						iAvg = cal.get(GregorianCalendar.DAY_OF_MONTH);
						strColA = dtFormatA.format(ts);
						strColB = dtFormatB.format(ts);
					}
					else {
						strColA = dtFormatA.format(ts);
						strColB = dtFormatB.format(ts);
					}
				}
				else if( idParam == StartStopLog.LOG_InternalTemperature
					|| idParam == StartStopLog.LOG_ExternalTemperature ) {
					if( bAvg ) {
						dataA.addValue(fValue + dataA.getValue(strName, strColA + (nUnitOnOff == 1 ? LABEL_ON : LABEL_OFF)).floatValue(), strName, strColA + (nUnitOnOff == 1 ? LABEL_ON : LABEL_OFF));
						if( idParam == StartStopLog.LOG_InternalTemperature )
							if( nUnitOnOff == 1 )
								anAvgInternalTemperatureStart[iAvg]++;
							else
								anAvgInternalTemperatureStop[iAvg]++;
						if( idParam == StartStopLog.LOG_ExternalTemperature )
							if( nUnitOnOff == 1 )
								anAvgExternalTemperatureStart[iAvg]++;
							else
								anAvgExternalTemperatureStop[iAvg]++;
					}
					else {
						dataA.addValue(fValue, strName, strColA + (nUnitOnOff == 1 ? LABEL_ON : LABEL_OFF));
					}
				}
				else if( idParam == StartStopLog.LOG_AnticipatedTimeStart && nUnitOnOff == 1 ) {
					if( bAvg ) {
						dataB.addValue(fValue + dataB.getValue(strName, strColB).floatValue(), strName, strColB);
						anAvgAnticipatedTimeStart[iAvg]++;
					}
					else {
						dataB.addValue(fValue, strName, strColB);
					}
				}
				else if( idParam == StartStopLog.LOG_AnticipatedTimeStop && nUnitOnOff == 0 ) {
					if( bAvg ) {
						dataB.addValue(fValue + dataB.getValue(strName, strColB).floatValue(), strName, strColB);
						anAvgAnticipatedTimeStop[iAvg]++;
					}
					else {
						dataB.addValue(fValue, strName, strColB);
					}
				}
			}
			
			if( bAvg ) {
				// compute average values
				if( "yearly".equals(type) ) {
					for(int i = 1; i < anAvgInternalTemperatureStart.length; i++) {
						addAvg(astrNames, dataA, String.valueOf(i), dataB, String.valueOf(i),
							anAvgInternalTemperatureStart[i], anAvgInternalTemperatureStop[i],
							anAvgExternalTemperatureStart[i], anAvgExternalTemperatureStop[i],
							anAvgAnticipatedTimeStart[i], anAvgAnticipatedTimeStop[i]
						);
					}
				}
				else {
					Calendar cal = (Calendar)calBegin.clone();
					while( cal.before(calEnd) ) {
						iAvg = cal.get(Calendar.DAY_OF_MONTH);
							addAvg(astrNames, dataA, dtFormatA.format(cal.getTime()), dataB, dtFormatB.format(cal.getTime()),
								anAvgInternalTemperatureStart[iAvg], anAvgInternalTemperatureStop[iAvg],
								anAvgExternalTemperatureStart[iAvg], anAvgExternalTemperatureStop[iAvg],
								anAvgAnticipatedTimeStart[iAvg], anAvgAnticipatedTimeStop[iAvg]
							);
						cal.add(Calendar.DAY_OF_YEAR, 1);
					}					
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopLog").error(e);
		}
	}
	
	
	private static void addNull(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB)
	{
		try { dataA.getValue(astrNames[LOG_InternalTemperature], colA + LABEL_ON);
			dataA.addValue(null, astrNames[LOG_InternalTemperature], colA + LABEL_ON);
		} catch(Exception e) {}
		try { dataA.getValue(astrNames[LOG_ExternalTemperature], colA + LABEL_ON);
			dataA.addValue(null, astrNames[LOG_ExternalTemperature], colA + LABEL_ON);
		} catch(Exception e) {}
		try { dataA.getValue(astrNames[LOG_InternalTemperature], colA + LABEL_OFF);
			dataA.addValue(null, astrNames[LOG_InternalTemperature], colA + LABEL_OFF);
		} catch(Exception e) {}
		try { dataA.getValue(astrNames[LOG_ExternalTemperature], colA + LABEL_OFF); 
			dataA.addValue(null, astrNames[LOG_ExternalTemperature], colA + LABEL_OFF);
		} catch(Exception e) {}			
		try { dataB.getValue(astrNames[LOG_AnticipatedTimeStart], colB);
			dataB.addValue(null, astrNames[LOG_AnticipatedTimeStart], colB);
		} catch(Exception e) {}			
		try { dataB.getValue(astrNames[LOG_AnticipatedTimeStop], colB);
			dataB.addValue(null, astrNames[LOG_AnticipatedTimeStop], colB);
		} catch(Exception e) {}			
	}
    

	private static void addZero(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB)
	{
		dataA.addValue(0f, astrNames[LOG_InternalTemperature], colA + LABEL_ON);
		dataA.addValue(0f, astrNames[LOG_ExternalTemperature], colA + LABEL_ON);
		dataA.addValue(0f, astrNames[LOG_InternalTemperature], colA + LABEL_OFF);
		dataA.addValue(0f, astrNames[LOG_ExternalTemperature], colA + LABEL_OFF);
		dataB.addValue(0f, astrNames[LOG_AnticipatedTimeStart], colB);
		dataB.addValue(0f, astrNames[LOG_AnticipatedTimeStop], colB);
	}
	

	private static void addAvg(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB,
			int nAvgITStart, int nAvgITStop, int nAvgETStart, int nAvgETStop, int nAvgTStart, int nAvgTStop)
	{
		if( nAvgITStart != 0 )
			dataA.addValue(new Float(dataA.getValue(astrNames[LOG_InternalTemperature], colA + LABEL_ON).floatValue() / nAvgITStart), astrNames[StartStopLog.LOG_InternalTemperature], colA + LABEL_ON);
		else
			dataA.addValue(null, astrNames[LOG_InternalTemperature], colA + LABEL_ON);
		if( nAvgITStop != 0 )
			dataA.addValue(new Float(dataA.getValue(astrNames[LOG_InternalTemperature], colA + LABEL_OFF).floatValue() / nAvgITStop), astrNames[StartStopLog.LOG_InternalTemperature], colA + LABEL_OFF);
		else
			dataA.addValue(null, astrNames[LOG_InternalTemperature], colA + LABEL_OFF);
		if( nAvgETStart != 0 )
			dataA.addValue(new Float(dataA.getValue(astrNames[LOG_ExternalTemperature], colA + LABEL_ON).floatValue() / nAvgETStart), astrNames[StartStopLog.LOG_ExternalTemperature], colA + LABEL_ON);
		else
			dataA.addValue(null, astrNames[LOG_ExternalTemperature], colA + LABEL_ON);
		if( nAvgETStop != 0 )
			dataA.addValue(new Float(dataA.getValue(astrNames[LOG_ExternalTemperature], colA + LABEL_OFF).floatValue() / nAvgETStop), astrNames[StartStopLog.LOG_ExternalTemperature], colA + LABEL_OFF);
		else
			dataA.addValue(null, astrNames[LOG_ExternalTemperature], colA + LABEL_OFF);
		if( nAvgTStart != 0 )
			dataB.addValue(new Float(dataB.getValue(astrNames[LOG_AnticipatedTimeStart], colB).floatValue() / nAvgTStart), astrNames[StartStopLog.LOG_AnticipatedTimeStart], colB);
		else
			dataB.addValue(null, astrNames[LOG_AnticipatedTimeStart], colB);
		if( nAvgTStop != 0 )
			dataB.addValue(new Float(dataB.getValue(astrNames[LOG_AnticipatedTimeStop], colB).floatValue() / nAvgTStop), astrNames[StartStopLog.LOG_AnticipatedTimeStop], colB);
		else
			dataB.addValue(null, astrNames[LOG_AnticipatedTimeStop], colB);
	}
	

	public static void delete(int idAlgorithm)
	{
		try {
			String sql = "DELETE FROM opt_startstop_log WHERE idalg=?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idAlgorithm });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopLog").error(e);
		}
	}
}
