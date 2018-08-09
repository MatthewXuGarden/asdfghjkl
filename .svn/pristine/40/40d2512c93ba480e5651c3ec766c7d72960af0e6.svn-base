package com.carel.supervisor.plugin.optimum;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;

public class NightFreeCooling {

	// inputs
	protected float fInternalTemperature;		// degrees
	protected float fExternalTemperature;		// degrees
	protected float fTemperatureSetpoint;		// degrees
	protected float fTemperatureDeadband;		// degrees
	protected int nInternalHumidity;			// percentage
	protected int nExternalHumidity;			// percentage
	protected int nHumiditySetpoint;			// percentage
	protected int nHumidityDeadband;			// percentage
	protected int nMaxTimeStart;				// minutes
	protected int nSummerStartMonth;
	protected int nSummerStartDay;
	protected int nSummerEndMonth;
	protected int nSummerEndDay;
	protected int nTimeOff;						// minutes (range: 0..1339)
	protected float fFanConsumption;			// Kw/h
	protected int nFanCapacity;					// m3/h
	protected int nUnitOnOff = nUNIT_OFF;		// On/Off status: 1 - unit on, 0 unit off
	
	// outputs
	protected int nAnticipatedTimeStart;		// minutes
	
	
	// constants
	protected static final int nRec = 3;		// number or records
	protected static final int nUNIT_ON = 1;
	protected static final int nUNIT_OFF = 0;
	protected static final int nUNIT_OFFLINE = -1;
	private static final int nONE_MINUTE = 1;		//minutes
	private static final int nTWO_MINUTES = 2;		//minutes
	private static final int nTHREE_MINUTES = 3;	//minutes
	private static final int nFIVE_MINUTES = 5;		//minutes
	private static final int nTHIRTY_MINUTES = 30; 	//minutes
	
	// feedback
	protected float fExternalEntalpy;
	protected float fInternalEntalpy;
	protected float fEntalpySetpoint;
	protected float fEntalpyDifference;	
	protected float fEntalpyMinSetpoint;
	protected float fEntalpyMaxSetpoint;
	protected int nElapsedTimeStart;		// minutes
	private boolean bSave = false;			// save elapsed time parameters when the setpoint is reached
	protected boolean bExistStartTimeResult = false;
	protected boolean bStartCalcInProgress = false;
	protected boolean bStartVentilation = false;
	protected boolean bExistsSunriseTime = false;
	protected Vector<Float> afEntalpyDifferenceStart = new Vector<Float>();
	protected Vector<Integer> anElapsedTimeStart = new Vector<Integer>(); 
	
	
	
	
	public NightFreeCooling(float fTemperatureSetpoint, int nHumiditySetpoint, int nTimeOff)
	{
		this.fTemperatureSetpoint = fTemperatureSetpoint;
		this.nHumiditySetpoint = nHumiditySetpoint;
		this.nTimeOff = nTimeOff;
		this.fEntalpySetpoint = calculateEnthalpy(fTemperatureSetpoint,nHumiditySetpoint);
		this.fEntalpyMinSetpoint = calculateEnthalpy(fTemperatureSetpoint-fTemperatureDeadband,nHumiditySetpoint-nHumidityDeadband);
		this.fEntalpyMaxSetpoint = calculateEnthalpy(fTemperatureSetpoint+fTemperatureDeadband,nHumiditySetpoint+nHumidityDeadband);
	}
	
	
	public void setInternalTemperature(float fInternalTemperature)
	{
		this.fInternalTemperature = fInternalTemperature;
	}
	
	public float getInternalTemperature()
	{
		return this.fInternalTemperature;
	}

	public void setExternalTemperature(float fExternalTemperature)
	{
		this.fExternalTemperature = fExternalTemperature;
	}
	
	public float getExternalTemperature()
	{
		return this.fExternalTemperature;
	}
	
	public void setInternalHumidity(int nInternalHumidity)
	{
		this.nInternalHumidity = nInternalHumidity;
	}
	
	public int getInternalHumidity()
	{
		return this.nInternalHumidity;
	}
	
	public void setExternalHumidity(int fExternalHumidity)
	{
		this.nExternalHumidity = fExternalHumidity;
	}
	
	public int getExternalHumidity()
	{
		return this.nExternalHumidity;
	}
	
	public float getInternalEnthalpy()
	{
		return this.fInternalEntalpy;
	}
	
	public float getExternalEnthalpy()
	{
		return this.fExternalEntalpy;
	}
	
	public int getUnitOnOff()
	{
		return nUnitOnOff;
	}
	
	public float getTemperatureSetpoint()
	{
		return this.fTemperatureSetpoint;
	}
	
	public int getHumiditySetpoint()
	{
		return this.nHumiditySetpoint;
	}
	
	public float getEnthalpySetpoint()
	{
		return this.fEntalpySetpoint;
	}
	
	public float getTemperatureDeadband()
	{
		return this.fTemperatureDeadband;
	}
	
	public int getHumidityDeadband()
	{
		return this.nHumidityDeadband;
	}
	
	public int getTimeOff()
	{
		return this.nTimeOff;
	}
	
	public int getMaxTimeStart()
	{
		return this.nMaxTimeStart;
	}
	
	public float getFanConsumption()
	{
		return this.fFanConsumption;
	}
	
	public int getFanCapacity()
	{
		return this.nFanCapacity;
	}
		
	public synchronized void execute()
	{
		if(isOutsideWorkingDates())
			return;
		
		int nCurrentTime = getCurrentTime();
		
		// compute all enthalpies 
		calculateIntExtEntalpies();
		
		// compute enthalpy difference
		fEntalpyDifference = Math.abs(fEntalpySetpoint - fExternalEntalpy);
		// fix division by 0
		if( fEntalpyDifference == 0f )
			fEntalpyDifference = 0.1f;
		/*
		LoggerMgr.getLogger(this.getClass()).info("Eint=" + fInternalEntalpy + " Eext=" + fExternalEntalpy + " Edif=" + fEntalpyDifference + " Eset=" + fEntalpySetpoint + " Emin=" + fEntalpyMinSetpoint + " Emax=" + fEntalpyMaxSetpoint);
		*/
		// *** COMPUTE ANTICIPATED TIME START ***
		// only after midnight and before the 'calculatedTimeStart' is passed
		if(bExistsSunriseTime)//executes algorithm only if the sunrise time (set by the user or given by the 'Lights' plugin) exists
		{
			if(nCurrentTime < getComputedTimeOn())
			{
				bExistStartTimeResult = true;
				bStartCalcInProgress = true;
				bStartVentilation = false;
				
				if(anElapsedTimeStart.size() > 0)
				{
					float fSumEnthalpyDifference = 0;
					for(int i = 0; i < afEntalpyDifferenceStart.size(); i++)
						fSumEnthalpyDifference += afEntalpyDifferenceStart.get(i);

					int nSumElapsedTimeStart = 0;
					for(int i = 0; i < anElapsedTimeStart.size(); i++)
						nSumElapsedTimeStart += anElapsedTimeStart.get(i);
					
					nAnticipatedTimeStart = (int)(fEntalpyDifference
						* ((float)nSumElapsedTimeStart / fSumEnthalpyDifference));
					
					if( nAnticipatedTimeStart > nMaxTimeStart )
						nAnticipatedTimeStart = nMaxTimeStart;
				}
				else {
					nAnticipatedTimeStart = nMaxTimeStart;
				}
				/*
				LoggerMgr.getLogger(this.getClass()).info("PreStart=" + nAnticipatedTimeStart + " NextStart: " + getComputedTimeOn() / 60 + ":" + getComputedTimeOn() % 60);
				*/
			}
			// *** MANAGE SWITCH ON AND ELAPSED TIME CALCULATION AFTER THE CALCULATED TIME START IS PASSED ***
			else if(nCurrentTime >= getComputedTimeOn() && nCurrentTime < getTimeOff())  
			{
				/*
				LoggerMgr.getLogger(this.getClass()).info("[IF1]");
				*/
				bStartCalcInProgress = false;
				
				if(nUnitOnOff == nUNIT_ON && fInternalEntalpy <= fEntalpyMinSetpoint)
				{
					if( bSave ) {
						updateElapsedTimeStart();
						saveFeedbackParameters("ElapsedTimeStart", anElapsedTimeStart);
						bSave = false;
					}
				}
				
				
				// turn unit on/off and check elapsed time
				if(fInternalEntalpy > fEntalpyMaxSetpoint)
				{
					/*
					LoggerMgr.getLogger(this.getClass()).info("[IF2] Fcon=" + nFanConsumption + " Fcap=" + nFanCapacity);
					*/
					if(fExternalEntalpy < fInternalEntalpy - (((3600 * fFanConsumption)/(1.2*nFanCapacity)) + 4)) // check if the external entalpy
					{
						/*
						LoggerMgr.getLogger(this.getClass()).info("[IF3]");
						*/
						bStartVentilation = true;
						if( nUnitOnOff == nUNIT_OFF )				
						{						 
							/*
							LoggerMgr.getLogger(this.getClass()).info("[IF4][ON]");
							*/
							// switch on
							changeVentilationState(nUNIT_ON);
							updateEnthalpyDifferenceStart();
							saveFeedbackParameters("EnthalpyDifferenceStart", afEntalpyDifferenceStart);
							bSave = true;
						}
					}
					else
					{
						if( nUnitOnOff == nUNIT_ON)
						{
							/*
							LoggerMgr.getLogger(this.getClass()).info("[OFF]");
							*/
							// switch off ventilation if external conditions doesn't allow to decrease the internal Enthalpy
							// in this case remove also the last feedbackParameters saved on DB
							bExistStartTimeResult = false;
							changeVentilationState(nUNIT_OFF);
							removeLastEnthalpyDifferenceStart();
							saveFeedbackParameters("EnthalpyDifferenceStart", afEntalpyDifferenceStart);
							bSave = false;
						}
						
					}																																 																										 			
				}
			}
			// *** MANAGE SWITCH OFF (DUE TO SCHEDULED OFF TIME)*** 
			else if(nCurrentTime >= getTimeOff() && nCurrentTime < getTimeOff() + nFIVE_MINUTES)
			{
				if(nUnitOnOff == nUNIT_ON)
				{
					// switch off
					changeVentilationState(nUNIT_OFF);
					if( bSave )
					{
						updateElapsedTimeStart(nTHIRTY_MINUTES); // add thirty minutes to the time passed
																 // because the time with ventilation ON was not enough
																 // to reach the enthalpy setpoint
						saveFeedbackParameters("ElapsedTimeStart", anElapsedTimeStart);
						bSave = false;
					}
				}
			}
		}
	}
	
	
	public int getComputedTimeOn()
	{
		return nTimeOff - nAnticipatedTimeStart;
	}
	
	
	private void updateElapsedTimeStart()
	{
		anElapsedTimeStart.add(nElapsedTimeStart);
		if( anElapsedTimeStart.size() > nRec )
			anElapsedTimeStart.remove(0);
	}
	
	private void updateElapsedTimeStart(int addedMinutes)
	{
		anElapsedTimeStart.add(nElapsedTimeStart+addedMinutes);
		if( anElapsedTimeStart.size() > nRec )
			anElapsedTimeStart.remove(0);
	}
	
	private void updateEnthalpyDifferenceStart()
	{
		afEntalpyDifferenceStart.add(fEntalpyDifference);
		if( afEntalpyDifferenceStart.size() > nRec )
			afEntalpyDifferenceStart.remove(0);
	}
	
	private void removeLastEnthalpyDifferenceStart()
	{
		if( afEntalpyDifferenceStart.size() > 0 )
			afEntalpyDifferenceStart.remove(afEntalpyDifferenceStart.size()-1);
	}
	
	private void calculateIntExtEntalpies()
	{
		this.fEntalpySetpoint = calculateEnthalpy(fTemperatureSetpoint,nHumiditySetpoint);
		this.fEntalpyMinSetpoint = calculateEnthalpy(fTemperatureSetpoint-fTemperatureDeadband,nHumiditySetpoint-nHumidityDeadband);
		this.fEntalpyMaxSetpoint = calculateEnthalpy(fTemperatureSetpoint+fTemperatureDeadband,nHumiditySetpoint+nHumidityDeadband);
		
		this.fExternalEntalpy = calculateEnthalpy(this.fExternalTemperature,this.nExternalHumidity);
		this.fInternalEntalpy = calculateEnthalpy(this.fInternalTemperature,this.nInternalHumidity);		
	}
	
	private float calculateEnthalpy(float temperature, int humidity)
	{
		float temperature10 = temperature*10;
		float humidity10 = humidity*10;
		float extAbsHum = calculateAbsHum(temperature10, humidity10);
		float enthalpy = (float)(0.1006*temperature10+0.1805*temperature10*extAbsHum+2501*extAbsHum);
		
		int enthInt= (int)(enthalpy * 10);
		return ((float)enthInt /10);
	}
	
	private float calculateAbsHum(float temperature, float humidity)
	{
		float Fs = (float)1.004; 
		float patm = 1000; // mbar
		float pvap = calculateSatPressure(temperature)*humidity/1000;
		return (float)(0.62198*Fs*pvap/(patm/10-Fs*pvap));
	}
	
	private float calculateSatPressure(float temperature)
	{
		float a = 0;
		float absTemp = (float)(temperature/10+273.15);
		float absTempQuadr = absTemp*absTemp;
		if(temperature > 0)
		{
			a = (float)((-5800.2206)/absTemp-5.51625598-0.048640239*absTemp);
			a = (float)(a + (4.1764768E-05)*absTempQuadr-(1.4452093E-8)*absTemp*absTempQuadr);
			a = (float)(a + 6.5459673*Math.log(absTemp));
			a = (float)Math.exp(a);
		}
		else
		{
			a = (float)(-5674.359/absTemp-0.51525828-0.009677843*absTemp);
			a = (float)(a + (6.2215701E-7)*absTempQuadr+(2.0747825E-9)*absTemp*absTempQuadr);
			a = (float)(a - (9.484024E-13)*absTempQuadr*absTempQuadr+4.1635019*Math.log(absTemp)); 
		}
		return a;
	}
	
	protected synchronized void saveFeedbackParameters(String strName, Vector aoParams)
	{
		// overridden on derived class
	}
	
	protected synchronized void changeVentilationState(int nState)
	{
		// overridden on derived class
	}
	
	
	// return current time in minutes (range: 0..1339)
	private static int getCurrentTime()
	{
		GregorianCalendar cal = new GregorianCalendar();
		return cal.get(GregorianCalendar.HOUR_OF_DAY) * 60 + cal.get(GregorianCalendar.MINUTE);
	}
	
	protected boolean isOutsideWorkingDates()
	{
		boolean bReversed = nSummerEndMonth < nSummerStartMonth
			|| (nSummerEndMonth == nSummerStartMonth && nSummerEndDay < nSummerStartDay);
		GregorianCalendar cal = new GregorianCalendar();
		int nMonth = cal.get(Calendar.MONTH) + 1;
		int nDay = cal.get(Calendar.DAY_OF_MONTH);
		boolean bInside = bReversed
			? nMonth > nSummerStartMonth || (nMonth == nSummerStartMonth && nDay >= nSummerStartDay) 
				|| nMonth < nSummerEndMonth || (nMonth == nSummerEndMonth && nDay <= nSummerEndDay) 
			: (nMonth > nSummerStartMonth || (nMonth == nSummerStartMonth && nDay >= nSummerStartDay))
				&& (nMonth < nSummerEndMonth || (nMonth == nSummerEndMonth && nDay <= nSummerEndDay));
		return !bInside;
	}
}
