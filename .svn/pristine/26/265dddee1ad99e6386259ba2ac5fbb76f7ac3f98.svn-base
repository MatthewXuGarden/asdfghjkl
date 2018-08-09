package com.carel.supervisor.plugin.optimum;

import java.text.SimpleDateFormat;
import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;

import java.sql.*;

import org.jfree.data.category.DefaultCategoryDataset;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;


public class NightFreeCoolingLog {
	// logged parameters
	public static final int LOG_UnitOnOff				= 0; 
	public static final int LOG_InternalTemperature		= 1; 
	public static final int LOG_ExternalTemperature		= 2; 
	public static final int LOG_InternalHumidity		= 3; 
	public static final int LOG_ExternalHumidity		= 4;
	public static final int LOG_AnticipatedTimeStart	= 5;
	public static final int LOG_InternalEnthalpy		= 6; 
	public static final int LOG_ExternalEnthalpy		= 7;

	
	public static void logParameters(
		int idAlgorithm,
		int nUnitOnOff,
		float fInternalTemperature,
		float fExternalTemperature,
		int nInternalHumidity,
		int nExternalHumidity,
		int nAnticipatedTimeStart,
		float fInternalEntalpy,
		float fExternalEntalpy
	)
	{
		// use the same timestamp for all parameters
		Timestamp timestamp = new Timestamp(GregorianCalendar.getInstance().getTimeInMillis());
		String sql = "insert into opt_nightfreecooling_log values(?, ?, ?, ?);";
		try {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql, new Object[][] {
				{ timestamp, idAlgorithm, LOG_UnitOnOff, (float)nUnitOnOff },
				{ timestamp, idAlgorithm, LOG_InternalTemperature, fInternalTemperature },
				{ timestamp, idAlgorithm, LOG_ExternalTemperature, fExternalTemperature },
				{ timestamp, idAlgorithm, LOG_InternalHumidity, (float)nInternalHumidity},
				{ timestamp, idAlgorithm, LOG_ExternalHumidity, (float)nExternalHumidity },
				{ timestamp, idAlgorithm, LOG_AnticipatedTimeStart, (float)nAnticipatedTimeStart },
				{ timestamp, idAlgorithm, LOG_InternalEnthalpy, fInternalEntalpy },
				{ timestamp, idAlgorithm, LOG_ExternalEnthalpy, fExternalEntalpy },
			});
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingLog").error(e);
		}
	}
	
	
	// return string array with names of logged parameters
	public static String[] getLogParamNames(String language)
	{
		LangService lang = LangMgr.getInstance().getLangService(language);
		String[] astrNames = new String[] {
			lang.getString("opt_nightfreecooling", "UnitOnOff"),
			lang.getString("opt_nightfreecooling", "InternalTemperature"),
			lang.getString("opt_nightfreecooling", "ExternalTemperature"),
			lang.getString("opt_nightfreecooling", "InternalHumidity"),
			lang.getString("opt_nightfreecooling", "ExternalHumidity"),
			lang.getString("opt_nightfreecooling", "AnticipatedTimeStart"),
			lang.getString("opt_nightfreecooling", "InternalEnthalpy"),
			lang.getString("opt_nightfreecooling", "ExternalEnthalpy"),
		};
		return astrNames;
	}

	
	public static Timestamp getFirstTimestamp()
	{
		try {
			String sql = "select min(timestamp) from opt_nightfreecooling_log;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() > 0 )
				return (Timestamp)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingLog").error(e);
		}
		
		return null;
	}


	public static Timestamp getLastTimestamp()
	{
		try {
			String sql = "select max(timestamp) from opt_nightfreecooling_log;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() > 0 )
				return (Timestamp)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingLog").error(e);
		}
		
		return null;
	}
	
	
	// type: daily, weekly, yearly
	// outputs: dataA, dataB, dataT
	public static void retriveDataSets(int idAlgorithm, Calendar calBegin, Calendar calEnd, String type, String language, DefaultCategoryDataset dataA, DefaultCategoryDataset dataB, DefaultCategoryDataset dataT)
	{
		String[] astrNames = getLogParamNames(language);
		SimpleDateFormat dtFormatA = new SimpleDateFormat("dd");
		SimpleDateFormat dtFormatB = new SimpleDateFormat("dd");
		SimpleDateFormat dtFormatT = new SimpleDateFormat("dd");
		Timestamp tsBegin = new Timestamp(calBegin.getTimeInMillis());
		Timestamp tsEnd = new Timestamp(calEnd.getTimeInMillis());
		
		// variables used to compute average values
		boolean bAvg = false;
		int[] anAvg = new int[53]; // 53 weeks / year
		int iAvg = 0;

		// init data sets
		if( type.equals("yearly") ) {
			for(int i = 1; i < anAvg.length; i++)
				addZero(astrNames, dataA, String.valueOf(i), dataB, String.valueOf(i), dataT, String.valueOf(i));
			bAvg = true;
		}
		else {
			Calendar cal = (Calendar)calBegin.clone();
			while( cal.before(calEnd) ) {
				addNull(astrNames, dataA, dtFormatA.format(cal.getTime()), dataB, dtFormatB.format(cal.getTime()), dataT, dtFormatT.format(cal.getTime()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		
		// retrieve data sets
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_nightfreecooling_log where timestamp >= ? and timestamp < ? and idalg = ? order by timestamp, idparam",
				new Object[] {
					tsBegin, tsEnd, idAlgorithm
				}
			);
			String strColA = null;
			String strColB = null;
			String strColT = null;
			int nUnitOnOff = 0;

			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer idParam = (Integer)r.get("idparam");
				String strName = astrNames[idParam];
				Float fValue = (Float)r.get("value");
				if( idParam == LOG_UnitOnOff ) {
					Timestamp ts = (Timestamp)r.get("timestamp");
					nUnitOnOff = fValue != 0 ? 1 : 0;
					if( bAvg ) {
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeInMillis(ts.getTime());
						iAvg = cal.get(GregorianCalendar.WEEK_OF_YEAR);
						anAvg[iAvg]++;
						strColA = String.valueOf(iAvg);
						strColB = String.valueOf(iAvg);
						strColT = String.valueOf(iAvg);
					}
					else {
						strColA = dtFormatA.format(ts);
						strColB = dtFormatB.format(ts);
						strColT = dtFormatT.format(ts);
					}
				}
				else if( idParam == LOG_InternalEnthalpy || idParam == LOG_ExternalEnthalpy ) {
					if( bAvg )
						dataA.addValue(fValue + dataA.getValue(strName, strColA).floatValue(), strName, strColA);
					else
						dataA.addValue(fValue, strName, strColA);
				}
				else if( idParam == LOG_InternalTemperature	|| idParam == LOG_ExternalTemperature
					|| idParam == LOG_InternalHumidity || idParam == LOG_ExternalHumidity ) {
					if( bAvg )
						dataT.addValue(fValue + dataT.getValue(strName, strColT).floatValue(), strName, strColT);
					else
						dataT.addValue(fValue, strName, strColT);
				}
				else if( idParam == LOG_AnticipatedTimeStart ) {
					if( bAvg )
						dataB.addValue(fValue + dataB.getValue(strName, strColB).floatValue(), strName, strColB);
					else
						dataB.addValue(fValue, strName, strColB);
				}
			}
			
			if( bAvg ) {
				// compute average values
				for(int i = 1; i < anAvg.length; i++) {
					if( anAvg[i] > 0 )
						addAvg(astrNames, dataA, String.valueOf(i), dataB, String.valueOf(i), dataT, String.valueOf(i), anAvg[i]);
					else
						addNull(astrNames, dataA, String.valueOf(i), dataB, String.valueOf(i), dataT, String.valueOf(i));
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingLog").error(e);
		}
	}
	
	
    private static void addNull(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB, DefaultCategoryDataset dataT, String colT)
	{
		dataA.addValue(null, astrNames[LOG_InternalEnthalpy], colA);
		dataA.addValue(null, astrNames[LOG_ExternalEnthalpy], colA);
		dataB.addValue(null, astrNames[LOG_AnticipatedTimeStart], colB);
    	dataT.addValue(null, astrNames[LOG_InternalTemperature], colT);
		dataT.addValue(null, astrNames[LOG_ExternalTemperature], colT);
		dataT.addValue(null, astrNames[LOG_InternalHumidity], colT);
		dataT.addValue(null, astrNames[LOG_ExternalHumidity], colT);
	}
    
    
    private static void addZero(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB, DefaultCategoryDataset dataT, String colT)
	{
		dataA.addValue(0f, astrNames[LOG_InternalEnthalpy], colA);
		dataA.addValue(0f, astrNames[LOG_ExternalEnthalpy], colA);
		dataB.addValue(0f, astrNames[LOG_AnticipatedTimeStart], colB);
    	dataT.addValue(0f, astrNames[LOG_InternalTemperature], colT);
		dataT.addValue(0f, astrNames[LOG_ExternalTemperature], colT);
		dataT.addValue(0f, astrNames[LOG_InternalHumidity], colT);
		dataT.addValue(0f, astrNames[LOG_ExternalHumidity], colT);
	}
	

	private static void addAvg(String[] astrNames, DefaultCategoryDataset dataA, String colA, DefaultCategoryDataset dataB, String colB, DefaultCategoryDataset dataT, String colT, int n)
	{
		dataA.addValue(new Float(dataA.getValue(astrNames[LOG_InternalEnthalpy], colA).floatValue() / n), astrNames[LOG_InternalEnthalpy], colA);
		dataA.addValue(new Float(dataA.getValue(astrNames[LOG_ExternalEnthalpy], colA).floatValue() / n), astrNames[LOG_ExternalEnthalpy], colA);
		dataB.addValue(new Float(dataB.getValue(astrNames[LOG_AnticipatedTimeStart], colB).floatValue() / n), astrNames[LOG_AnticipatedTimeStart], colB);
		dataT.addValue(new Float(dataT.getValue(astrNames[LOG_InternalTemperature], colT).floatValue() / n), astrNames[LOG_InternalTemperature], colT);
		dataT.addValue(new Float(dataT.getValue(astrNames[LOG_ExternalTemperature], colT).floatValue() / n), astrNames[LOG_ExternalTemperature], colT);
		dataT.addValue(new Float(dataT.getValue(astrNames[LOG_InternalHumidity], colT).floatValue() / n), astrNames[LOG_InternalTemperature], colT);
		dataT.addValue(new Float(dataT.getValue(astrNames[LOG_ExternalHumidity], colT).floatValue() / n), astrNames[LOG_ExternalTemperature], colT);
	}
	

	public static void delete(int idAlgorithm)
	{
		try {
			String sql = "DELETE FROM opt_nightfreecooling_log WHERE idalg=?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idAlgorithm });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingLog").error(e);
		}
	}
}
