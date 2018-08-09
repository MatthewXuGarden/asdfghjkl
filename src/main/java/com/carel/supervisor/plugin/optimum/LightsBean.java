package com.carel.supervisor.plugin.optimum;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.BackGroundCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


// integrate SunriseSunset algorithm with PVPRO db and device variables
public class LightsBean {
	// enabled
	private boolean bEnabled = false;
	
	// location
	private double dLatitude;
	private double dLongitude;
	
	// PVPRO variables assigned to Start/Stop algorithm
	private int idVarDay = 0;					// variable id for Day output parameter
	private int idVarNight = 0;					// variable id for Night output parameter
	
	// status
	private java.util.Date dtSunrise = null;	// computed sunrise
	private java.util.Date dtSunset = null;		// computed sunset
	private boolean bNight = false;				// maintain plugin status
	private int nToday = 0;						// compute dtSunrise/dtSunset at day transition
	
	// user name from event list
	private static final String strUserName = "Geo-Lighting";
	
    
	public LightsBean()
	{	
		loadParameters();
	}

	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public void setEnabled(boolean b)
	{
		try {
			String strValue = b ? "1" : "0";
			DatabaseMgr.getInstance().executeStatement("update opt_lights_settings set value = ? where name = ?",
				new Object[] { strValue, "Enabled" });
			bEnabled = b;
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public boolean isConfigured()
	{
		return idVarDay != 0 && idVarNight != 0;		
	}
	
	
	public boolean isDay()
	{
		return !bNight;
	}
	
	
	public boolean isNight()
	{
		return bNight;
	}

	
	public double getLatitude()
	{
		return dLatitude;
	}
	
	
	public double getLongitude()
	{
		return dLongitude;
	}
	
	
	public int getDayVar()
	{
		return idVarDay;
	}
	
	
	public int getNightVar()
	{
		return idVarNight;
	}
	
	
	public java.util.Date getSunrise()
	{
		return dtSunrise;
	}
	
	
	public String getSunriseString()
	{
		String sunrise = "***";
		if( dtSunrise != null ) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			sunrise = df.format(dtSunrise);
		}
		return sunrise;
	}
	
	
	public java.util.Date getSunset()
	{
		return dtSunset;
	}

	
	public String getSunsetString()
	{
		String sunset = "***";
		if( dtSunset != null ) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			sunset = df.format(dtSunset);
		}
		return sunset;
	}
	
	
	public Float getNight()
	{
		if( idVarNight > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarNight).getCurrentValue();
				if( !Float.isNaN(fValue) )
					return new Float(fValue);
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		return null;
	}
	
	
	public Float getDay()
	{
		if( idVarDay > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarDay).getCurrentValue();
				if( !Float.isNaN(fValue) )
					return new Float(fValue);
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		return null;		
	}

	
	public synchronized void execute()
	{
		// algorithm not started
		if( !bEnabled )
			return;
		
		// compute sunrise/sunset at day transition
		java.util.Date dtNow = new java.util.Date();
		if( dtNow.getDate() != nToday ) {
			dtSunrise = SunriseSunset.getSunrise(dLatitude, dLongitude);
			dtSunset = SunriseSunset.getSunset(dLatitude, dLongitude);
			nToday = dtNow.getDate();
		}
		
		if(dtSunrise == null && dtSunset==null)
		{
			return;
		}
		
		if(dtSunrise == null && dtSunset!=null) //if there is only sunrise time, only execute switch to 'night' state
		{
			if(dtNow.after(dtSunset) && !bNight)
			{
				bNight =! bNight;
				switchVarState();
			}
			return;
		}
		
		if(dtSunrise!=null && dtSunset==null) //if there is only sunrise time, only execute switch to 'day' state
		{
			if(dtNow.after(dtSunrise) && bNight)
			{
				bNight =! bNight;
				switchVarState();
			}
			return;
		}
		
		// switch day/night variables
		if( ((dtNow.before(dtSunrise) || dtNow.after(dtSunset))&& !bNight) ||
			((dtNow.after(dtSunrise) && dtNow.before(dtSunset)) && bNight) ) 
		{
			bNight =! bNight;
			switchVarState();
		}
	}
	
	
	public synchronized void loadParameters()
	{
		try {
			// retrieve parameters
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_lights_settings order by name");
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				String strName = r.get("name").toString();
				String strValue = r.get("value").toString();
				if( strName.equals("Enabled") )
					bEnabled = Integer.parseInt(strValue) != 0;
				else if( strName.equals("Latitude") )
					dLatitude = Double.parseDouble(strValue);
				else if( strName.equals("Longitude") )
					dLongitude = Double.parseDouble(strValue);
				else if( strName.equals("VarDay") )
					idVarDay = Integer.parseInt(strValue);
				else if( strName.equals("VarNight") )
					idVarNight = Integer.parseInt(strValue);
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// compute sunrise/sunset
		dtSunrise = SunriseSunset.getSunrise(dLatitude, dLongitude);
		dtSunset = SunriseSunset.getSunset(dLatitude, dLongitude);
		java.util.Date dtNow = new java.util.Date();
					
		if(dtSunrise==null && dtSunset==null)
			bNight = SunriseSunset.isWholeDayNight(dLatitude, dLongitude, dtNow);
		
		if(dtSunrise==null && dtSunset!=null)
			bNight = dtNow.after(dtSunset);
		
		if(dtSunrise!=null && dtSunset==null)
			bNight = dtNow.before(dtSunrise);
		
		if (dtSunrise!=null && dtSunset!=null)
			bNight = (dtNow.before(dtSunrise) || dtNow.after(dtSunset));

		nToday = dtNow.getDate();
	}
	
	
	public synchronized void saveParameters(Properties prop)
	{
		Enumeration<Object> it = prop.keys();
		while( it.hasMoreElements() ) {
			String strName = (String)it.nextElement();
			String strValue = prop.getProperty(strName);
			try {
				// to avoid updating db with bad values
				Double.parseDouble(strValue);
			}
			catch(NumberFormatException e) {
				continue;
			}
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_lights_settings where name = '" + strName + "';");
				if( rs.size() > 0 )
					DatabaseMgr.getInstance().executeStatement("update opt_lights_settings set value = ? where name = ?",
							new Object[] { strValue, strName });
				else
					DatabaseMgr.getInstance().executeStatement("insert into opt_lights_settings values(?, ?)",
						new Object[] { strName, strValue });
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update algorithm parameters
		loadParameters();
	}
	

	protected synchronized void switchVarState()
	{
		SetContext objContext = new SetContext();
		String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		objContext.setLanguagecode(lang);
		objContext.setUser(strUserName);
		objContext.setLoggable(true);
		objContext.setCallback(new BackGroundCallBack());
		SetWrp sw = objContext.addVariable(idVarDay, bNight ? 0 : 1);
		sw.setCheckChangeValue(true);
		sw = objContext.addVariable(idVarNight, bNight ? 1 : 0);
		sw.setCheckChangeValue(true);		
		SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	}
}
