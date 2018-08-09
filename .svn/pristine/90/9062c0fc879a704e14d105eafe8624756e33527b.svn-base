package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.kpi.KpiMgr;
import com.carel.supervisor.presentation.session.UserSession;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;


public class EnergyDevice {
	private String strDescription;
	private Integer idDevice;
	private Integer idSolenoidVarMdl;
	private Integer idSolenoid;
	private Float fWeight;
	private Integer nTimeOn;

	
	public EnergyDevice(Integer idDevice, Integer idSolenoidVarMdl, String strDescription)
	{
		this.idDevice = idDevice;
		this.idSolenoidVarMdl = idSolenoidVarMdl;
		this.strDescription = strDescription;
	}

	
	public EnergyDevice(Integer idDevice, Integer idSolenoidVarMdl, String strDescription, Float fWeight)
	{
		this.idDevice = idDevice;
		this.idSolenoidVarMdl = idSolenoidVarMdl;
		this.strDescription = strDescription;		
		this.fWeight = fWeight;
	}
	
	
	public String getDescription()
	{
		return strDescription;
	}
	
	
	public Integer getIdDevice()
	{
		return idDevice;
	}
	
	
	public Integer getIdSolenoidVarMdl()
	{
		return idSolenoidVarMdl;
	}
	
	
	public Float getWeight()
	{
		return fWeight;
	}
	
	
	private void retrieveSolenoid()
	{
		try {
			String sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? and idhsvariable is null;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice, idSolenoidVarMdl });
			if( rs.size() > 0 )
				idSolenoid = (Integer)rs.get(0).get(0);
		} catch (Exception e) {
			LoggerMgr.getLogger(EnergyDevice.class).error(e);
		}
	}
	
	
	private Integer retrieveTimeOn(Timestamp begin, Timestamp end)
	{
		nTimeOn = null;
		if( idSolenoid == null )
			retrieveSolenoid();
		if( idSolenoid != null )
			nTimeOn = 0;
		else
			return nTimeOn;
		Long nTimeCounter = 0l;
		
		String sql = "(select * from hsvarhistor " +
			"where hsvarhistor.idvariable = "+idSolenoid+" " +
			"and hsvarhistor.inserttime < '"+begin+"' " +
			"order by inserttime desc limit 1) " +
			"union " +
			"( select * from hsvarhistor "+
			"where hsvarhistor.idvariable = "+idSolenoid+" "+
			"and hsvarhistor.inserttime > '"+begin+"' and hsvarhistor.inserttime < '"+end+"') "+
			"union "+ 
			"(select * from hsvarhistor "+
			"where hsvarhistor.idvariable = "+idSolenoid+" "+
			"and hsvarhistor.inserttime > '"+end+"' "+
			"order by inserttime limit 1)";
		try
		{
			Connection dbCon = DatabaseMgr.getInstance().getConnection(null);
			ResultSet rs = dbCon.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
			
			Timestamp prevTimestamp = null;
			Float prevValue = null;
			Timestamp ts;
			Float val;
			while (rs.next())
			{
				ts = (Timestamp) rs.getTimestamp("inserttime");
				val = rs.getFloat("value");
				if( rs.wasNull() )
					val = Float.NaN;
				// update time counter
				if( ts.compareTo(begin) >= 0 && ts.compareTo(end) <= 0 ) {						
					if( prevTimestamp != null ) {
						if( prevValue.equals(1.0f) )
							nTimeCounter += (ts.getTime() - prevTimestamp.getTime());
					}
				}
				prevTimestamp = ts;
				prevValue = val;

				long freq = 1000L * rs.getInt("frequency");
				int acc = 0;
				boolean cont = true;
				for (int j = 1; (j <= 63) && (cont == true); j++)
				{
					try
					{
						long a = new Long(rs.getInt("n" + j));
						acc += a;
						val = rs.getFloat("value" + j);
						if(rs.wasNull())
							val = Float.NaN;
						if(j==63)
						{
							cont=false;
							continue;
						}
						long nexta = new Long(rs.getInt("n" + (j+1)));
						if( nexta == 0f )
							val = Float.NaN;
						// update time counter
						Timestamp thisTimestamp = new Timestamp(ts.getTime() + freq * acc);
						if( thisTimestamp.compareTo(begin) >= 0 && thisTimestamp.compareTo(end) <= 0 ) {						
							if( prevTimestamp != null ) {
								if( prevValue.equals(1.0f) )
									nTimeCounter += (thisTimestamp.getTime() - prevTimestamp.getTime());
							}
						}
						prevTimestamp = thisTimestamp;
						prevValue = val;

						if (nexta == 0)
						{
							cont = false;
							continue;
						}
					} catch (SQLException e)
					{
						LoggerMgr.getLogger(EnergyDevice.class).error(e);
					}
				}
			}
		} catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
		}
		nTimeCounter /= 60000; // value in minutes
		nTimeOn = nTimeCounter.intValue();
		return nTimeOn; 
	}
	
	
	public static void writeConsumerReport(UserSession session, Integer idConsVar, Timestamp begin, Timestamp end, BufferedWriter writer)
		throws IOException
	{
		EnergyConsumer meter = EnergyMgr.getInstance().consumerLookupByVariable(idConsVar);
		if( meter == null )
			return;
		LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());
		// site name
		writer.write(lang.getString("energy", "repsite"));
		writer.write(";");
		writer.write(session.getSiteName());
		writer.write(";\n");
		// meter
		Float kWhMeter = meter.getKwh(begin, end);
		writer.write(meter.getName());
		writer.write(" [" + lang.getString("energy", "enerkwh") + "];");
		writer.write(kWhMeter.isNaN() ? "***" : kWhMeter.toString());
		writer.write(";\n");
		// interval
		writer.write(lang.getString("energy", "enerfrom"));
		writer.write(";");
		writer.write(begin.toString());
		writer.write(";\n");
		writer.write(lang.getString("energy", "enerto"));
		writer.write(";");
		writer.write(end.toString());
		writer.write(";\n");
		// header
		writer.write(lang.getString("energy", "enerconfigurationdevice"));
		writer.write(";");
		writer.write(lang.getString("energy", "consumption"));
		writer.write(" [" + lang.getString("energy", "enerkwh") + "];");
		writer.write(lang.getString("energy", "time_on"));
		writer.write(" [" + lang.getString("energy", "min") + "];");
		writer.write(lang.getString("energy", "time_on"));
		writer.write(";");
		writer.write(lang.getString("energy", "device_weight"));
		writer.write(" [" + lang.getString("energy", "btu") + "];\n");
		// devices
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);		
		Float fTotal = 0f;
		EnergyDevice[] aEnergyDevices = getConsumerDevices(idConsVar, session.getLanguage());
		for(int i = 0; i < aEnergyDevices.length; i++) {
			EnergyDevice ed = aEnergyDevices[i];
			Integer nTimeOn = ed.retrieveTimeOn(begin, end);
			if( nTimeOn != null ) {
				fTotal += (ed.fWeight * nTimeOn);
			}
		}
		for(int i = 0; i < aEnergyDevices.length; i++) {
			EnergyDevice ed = aEnergyDevices[i];
			writer.write(ed.getDescription());
			writer.write(";");
			Float kWhDevice = null;
			if( !kWhMeter.isNaN() && ed.nTimeOn != null ) {
				kWhDevice = ((ed.fWeight * ed.nTimeOn) / fTotal) * kWhMeter;
			}
			writer.write(kWhDevice != null ? df.format(kWhDevice) : "***");
			writer.write(";");
			writer.write(ed.nTimeOn != null ? ed.nTimeOn.toString() : "***");
			writer.write(";");
			String strTimeOn = "";
			if( ed.nTimeOn != null ) {
				Integer nDays = ed.nTimeOn / (24 * 60);
				strTimeOn += nDays.toString();
				strTimeOn += "d";
				Integer nMinutes = ed.nTimeOn % (24 * 60);
				Integer nHours = nMinutes / 60;
				strTimeOn += nHours.toString();
				strTimeOn += "h";
				nMinutes %= 60;
				strTimeOn += nMinutes.toString();
				strTimeOn += "m";
			}
			else {
				strTimeOn = "***";
			}
			writer.write(strTimeOn);
			writer.write(";");
			writer.write(ed.fWeight.toString());
			writer.write(";\n");			
		}
	}
		
	
	public static void updateConsumerDevices(Integer idConsumer, String devs)
	{
		try {
			String sql = "delete from energydevice where idconsvar=?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idConsumer });
			if( !devs.isEmpty() ) {
				sql = "insert into energydevice values(?, ?, ?, ?);";
				String[] aDevs = devs.split(";");
				Object[][] aaObjects = new Object[aDevs.length][4];
				for(int i = 0; i < aDevs.length; i++) {
					String[] aParams = aDevs[i].split(",");
					aaObjects[i][0] = idConsumer;
					aaObjects[i][1] = new Integer(Integer.parseInt(aParams[0]));
					aaObjects[i][2] = new Integer(Integer.parseInt(aParams[1]));
					aaObjects[i][3] = new Float(Float.parseFloat(aParams[2]));
				}
				DatabaseMgr.getInstance().executeMultiStatement(null, sql, aaObjects);				
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(EnergyDevice.class).error(e);
		}
	}
	
	
	public static EnergyDevice[] getAvailableDevices(String language)
	{
		EnergyDevice[] aEnergyDevices = null;
		// retrieve all devices configured in KPI plugin but not assigned to meters
		String sql = "select distinct on(iddevice, solenoidvarmdl) iddevice, kpidevices.idgrp, iddevmdl, solenoidvarmdl, description from kpidevices"
			+ " inner join kpiconf on kpidevices.iddevice not in (select iddevice from energydevice) and kpidevices.idgrp=kpiconf.idgrp and kpiconf.iddevmdl in (select iddevmdl from cfdevice where cfdevice.iddevice=kpidevices.iddevice)"
			+ " and solenoidvarmdl is not null"
			+ " inner join cftableext on cftableext.idsite=1 and cftableext.tablename='cfdevice' and cftableext.tableid=kpidevices.iddevice and cftableext.languagecode=?;";
		try	{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { language });
			aEnergyDevices = new EnergyDevice[rs.size()];
			for(int i = 0, j = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				EnergyDevice ed = new EnergyDevice(
					(Integer)r.get("iddevice"),
					(Integer)r.get("solenoidvarmdl"),
					(String)r.get("description")
				);
				if( j > 0 ) {
					if( ed.idDevice.equals(aEnergyDevices[j-1].idDevice) )
						aEnergyDevices[j-1].idSolenoidVarMdl = 0; // device it is configured in multiple groups with different solenoidvarmdl
					else
						aEnergyDevices[j++] = ed;
				}
				else {
					aEnergyDevices[j++] = ed;
				}
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(EnergyDevice.class).error(e);
		}
		return aEnergyDevices;
	}
	
	
	public static EnergyDevice[] getConsumerDevices(Integer idConsumer, String language)
	{
		EnergyDevice[] aEnergyDevices = null;		
		// retrieve all devices assigned to energy meter
		String sql = "select energydevice.*, cftableext.description from energydevice inner join cftableext"
			+ " on idconsvar=? and cftableext.idsite=1 and cftableext.tablename='cfdevice' and cftableext.tableid=iddevice and cftableext.languagecode=?;";
		try	{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idConsumer, language });
			aEnergyDevices = new EnergyDevice[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				aEnergyDevices[i] = new EnergyDevice(
					(Integer)r.get("iddevice"),
					(Integer)r.get("solenoidvarmdl"),
					(String)r.get("description"),
					(Float)r.get("weight")
				);
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(EnergyDevice.class).error(e);
		}
		return aEnergyDevices;
	}
}
