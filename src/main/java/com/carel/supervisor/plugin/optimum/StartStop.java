package com.carel.supervisor.plugin.optimum;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;

// optimize the START/STOP of the A/C units
// START: bringing the temperature inside the building from a first temperature to a second temperature
// (TSet-point) at a predetermined time each day.
// STOP: stop the units at a predetermined time each day to use the inertia of the building
// that maintain the temperature stable for a certain time period.
public class StartStop {
	// inputs
	protected float fInternalTemperature;	// degrees
	protected float fExternalTemperature;	// degrees
	protected float fTemperatureSetpoint;	// degrees
	protected float fDiffStartSetpoint;		// degrees
	protected float fDiffStopSetpoint;		// degrees
	protected int nCurrentTime;				// minutes (range: 0..1339)
	protected int nTimeOn;					// minutes (range: 0..1339)
	protected int nTimeOff;					// minutes (range: 0..1339)
	protected int nMinTimeStart;			// minutes
	protected int nMaxTimeStart;			// minutes
	protected int nMinTimeStop;				// minutes
	protected int nMaxTimeStop;				// minutes
	protected int nSummerWinter;			// 0 = summer, 1 = winter, 2 = auto
	// outputs
	protected int nAnticipatedTimeStart;	// minutes
	protected int nAnticipatedTimeStop;		// minutes
	protected int nUnitOnOff = nUNIT_OFF;	// command: 1 - unit on, 0 unit off
	// constants
	protected static final int nC1 = 10000;
	protected static final int nRec = 3;	// number or records
	protected static final int nUNIT_ON = 1;
	protected static final int nUNIT_OFF = 0;
	protected static final int nUNIT_OFFLINE = -1;
	protected static final int SUMMER = 0;
	protected static final int WINTER = 1;
	protected static final int AUTO = 2;
	private static final int nONE_MINUTE = 1;		// minutes
	private static final int nTWO_MINUTES = 2;		// minutes
	private static final int nTHREE_MINUTES = 3;	// minutes
	// feedback
	protected float fTemperatureDifference;	// degree
	protected int nElapsedTimeStart;		// minutes
	protected int nElapsedTimeStop;			// minutes
	protected int nPrevTimeStart;			// minutes
	protected int nPrevTimeStop;			// minutes
	protected Vector<Float> afTemperatureDifferenceStart = new Vector<Float>();
	protected Vector<Float> afTemperatureDifferenceStop = new Vector<Float>();
	protected Vector<Integer> anElapsedTimeStart = new Vector<Integer>(); 
	protected Vector<Integer> anElapsedTimeStop = new Vector<Integer>();
	protected boolean bExistStartTimeResult = false;
	protected boolean bExistStopTimeResult = false;
	protected boolean bStartCalcInProgress = false;
	protected boolean bStopCalcInProgress = false;
	protected boolean bStartActionDone = false;
	protected boolean bStopActionDone = false;
	
	protected boolean bSaveStart = false;			// save elapsed time parameters when the setpoint is reached
	protected boolean bSaveStop = false;			// save elapsed time parameters when the setpoint is reached
	
	
	public StartStop(float fTemperatureSetpoint, float fDiffStartSetpoint, float fDiffStopSetpoint,
		int nTimeOn, int nTimeOff,
		int nMinTimeStart, int nMaxTimeStart,
		int nMinTimeStop, int nMaxTimeStop,
		int nSummerWinter)
	{
		this.fTemperatureSetpoint = fTemperatureSetpoint;
		this.fDiffStartSetpoint = fDiffStartSetpoint;
		this.fDiffStopSetpoint = fDiffStopSetpoint;
		this.nTimeOn = nTimeOn;
		this.nTimeOff = nTimeOff;
		this.nMinTimeStart = nMinTimeStart;
		this.nMaxTimeStart = nMaxTimeStart;
		this.nMinTimeStop = nMinTimeStop;
		this.nMaxTimeStop = nMaxTimeStop;
		this.nSummerWinter = nSummerWinter;
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
	
	
	public int getUnitOnOff()
	{
		return nUnitOnOff;
	}
	
	
	public float getTemperatureSetpoint()
	{
		return this.fTemperatureSetpoint;
	}
	
	
	public int getSummerWinter()
	{
		return nSummerWinter;
	}
	
	
	public float getDiffStartSetpoint()
	{
		return this.fDiffStartSetpoint;
	}
	
	
	public float getDiffStopSetpoint()
	{
		return this.fDiffStopSetpoint;
	}
	
	
	public int getTimeOn()
	{
		return this.nTimeOn;
	}
	
	public int getTimeOff()
	{
		return this.nTimeOff;
	}
	
	public int getMinTimeStart()
	{
		return this.nMinTimeStart;
	}
	
	
	public int getMaxTimeStart()
	{
		return this.nMaxTimeStart;
	}
	
	
	public int getMinTimeStop()
	{
		return this.nMinTimeStop;
	}
	
	
	public int getMaxTimeStop()
	{
		return this.nMaxTimeStop;
	}
	
	
	public synchronized void execute()
	{
		nCurrentTime = getCurrentTime();

		// compute temperature difference
		fTemperatureDifference = Math.abs(fTemperatureSetpoint - fExternalTemperature);
		// fix division by 0
		if( fTemperatureDifference == 0f )
			fTemperatureDifference = 0.1f;
		
		// *** COMPUTE ANTICIPATED TIME START ***
		// only if the calculated time start is not passed
		// (on the first iteration 'nAnticipatedTimeStart' = 0 )
		if(nCurrentTime <  getComputedTimeOn()) {
		
			// proper reset of the internal flags
			bExistStartTimeResult = true;
			bExistStopTimeResult = true;
			bStartCalcInProgress = true;
			bStopCalcInProgress = false;
			bStartActionDone = false;
			bStopActionDone = false;
			
			if( anElapsedTimeStart.size() > 0 ) {
				float fSumTemperatureDifference = 0;
				for(int i = 0; i < afTemperatureDifferenceStart.size(); i++)
					fSumTemperatureDifference += afTemperatureDifferenceStart.get(i);

				int nSumElapsedTimeStart = 0;
				for(int i = 0; i < anElapsedTimeStart.size(); i++)
					nSumElapsedTimeStart += anElapsedTimeStart.get(i);
					
				// Summer / Winter rules
				if( nSummerWinter == SUMMER ) {
					if( fInternalTemperature < fTemperatureSetpoint )
						nAnticipatedTimeStart = nMinTimeStart;
					else
						nAnticipatedTimeStart = (int)(fTemperatureDifference * ((float)nSumElapsedTimeStart / fSumTemperatureDifference));
				}
				else if( nSummerWinter == WINTER ) {
					if( fInternalTemperature > fTemperatureSetpoint )
						nAnticipatedTimeStart = nMinTimeStart;
					else
						nAnticipatedTimeStart = (int)(fTemperatureDifference * ((float)nSumElapsedTimeStart / fSumTemperatureDifference));
				}
				else {
					nAnticipatedTimeStart = (int)(fTemperatureDifference * ((float)nSumElapsedTimeStart / fSumTemperatureDifference));
				}
				// bound rules
				if( nAnticipatedTimeStart < nMinTimeStart )
					nAnticipatedTimeStart = nMinTimeStart;
				if( nAnticipatedTimeStart > nMaxTimeStart )
					nAnticipatedTimeStart = nMaxTimeStart;			
			}
			else {
				//anticipated time start is 'maxtimestart' in case there is not log data from the previous days
				nAnticipatedTimeStart = nMaxTimeStart;				
			}
			/*
			LoggerMgr.getLogger(this.getClass()).info("[" + nTimeOn / 60 + ":" + nTimeOn % 60 + "-" + nTimeOff / 60 + ":" + nTimeOff % 60 + "] " + nCurrentTime / 60 + ":" + nCurrentTime % 60 + " PreStart=" + nAnticipatedTimeStart + " Text=" + fExternalTemperature + " Tint=" + fInternalTemperature);
			*/
		}
		
		//  *** COMPUTE ANTICIPATED TIME STOP ***
		// only in the timeband [computedTimeOn - computedTimeOff]
    	// (on the first itereation 'nAnticipatedTimeStop' = 0 )
		if(nCurrentTime >= getComputedTimeOn() && nCurrentTime <  getComputedTimeOff()) {
			
			// proper reset of the internal flags
			bExistStopTimeResult = true;
			bStopCalcInProgress = true;
			bStopActionDone = false;
			
			if( anElapsedTimeStop.size() > 0 ) {
				float fSumTemperatureDifference = 0;
				for(int i = 0; i < afTemperatureDifferenceStop.size(); i++)
				{
					float val = afTemperatureDifferenceStop.get(i);
					fSumTemperatureDifference += nC1/val;
				} 
				
				int nSumElapsedTimeStop = 0;
				for(int i = 0; i < anElapsedTimeStop.size(); i++)
					nSumElapsedTimeStop += anElapsedTimeStop.get(i);
				
				
				// Summer / Winter rules
				if( nSummerWinter == SUMMER ) {
					if( fInternalTemperature > fTemperatureSetpoint )
						nAnticipatedTimeStop = nMinTimeStop;
					else
						nAnticipatedTimeStop = (int)(((float)nC1 / fTemperatureDifference * nSumElapsedTimeStop) / fSumTemperatureDifference);
				}
				else if( nSummerWinter == WINTER ) {
					if( fInternalTemperature < fTemperatureSetpoint )
						nAnticipatedTimeStop = nMinTimeStop;
					else
						nAnticipatedTimeStop = (int)(((float)nC1 / fTemperatureDifference * nSumElapsedTimeStop) / fSumTemperatureDifference);
				}
				else {
					nAnticipatedTimeStop = (int)(((float)nC1 / fTemperatureDifference * nSumElapsedTimeStop) / fSumTemperatureDifference);
				}
				// bound rules
				if( nAnticipatedTimeStop < nMinTimeStop )
					nAnticipatedTimeStop = nMinTimeStop;
				if( nAnticipatedTimeStop > nMaxTimeStop )
					nAnticipatedTimeStop = nMaxTimeStop;
			}
			else {	
				//anticipated time start is 'mintimestop' in case there is not log data from the previous days
				nAnticipatedTimeStop = nMinTimeStop;
			}
			/*
			LoggerMgr.getLogger(this.getClass()).info("[" + nTimeOn / 60 + ":" + nTimeOn % 60 + "-" + nTimeOff / 60 + ":" + nTimeOff % 60 + "] " + nCurrentTime / 60 + ":" + nCurrentTime % 60 + " PreStop=" + nAnticipatedTimeStop + " Text=" + fExternalTemperature + " Tint=" + fInternalTemperature);
			*/
		}
		
		// *** TURN UNIT ON AND CHECK ELAPSED 'TIMESTOP' TIME ***
		if( nUnitOnOff == nUNIT_OFF)  // save elapsedTimeStop only if the A/C unit is off
									  // turn ON A/C unit only if it is OFF
		{
			if((nCurrentTime<getComputedTimeOn() || nCurrentTime>=getComputedTimeOff())) // check if we're outside the 'calculated' ON timeband 
																						 // to avoid 'fake' data logging in case the A/C turns off (from external command) inside the 'calculated' ON timeband
			{
				if (nPrevTimeStop <= nCurrentTime) 						// manage the case when the temperature setpoint (in OFF action) is reached the day after  
					nElapsedTimeStop = nCurrentTime - nPrevTimeStop;	// it is not necessary to manage this with the ON action (continous timeband are not implemented yet) 
				else
					nElapsedTimeStop = 1440 - nPrevTimeStop + nCurrentTime;
				
				if( ((fInternalTemperature < fTemperatureSetpoint - fDiffStopSetpoint || fInternalTemperature > fTemperatureSetpoint + fDiffStopSetpoint))
					|| nElapsedTimeStop > nMaxTimeStop ) {
					if( bSaveStop ) {
						//LoggerMgr.getLogger(this.getClass()).info("Internal Temperature raised setpoint after STOP action");
						updateElapsedTimeStop();
						saveFeedbackParameters("ElapsedTimeStop", anElapsedTimeStop);
						bSaveStop = false;
					}
				}
			}
			
			if(!(getTimeOn()==0 && getTimeOff()==0)) // don't switch ON the AC unit when there is not schedulation for the current day
													 // (to avoid multiple switch-ON/switch-OFF from 00:00 to 00:01) 
			{
				if((nCurrentTime >= getComputedTimeOn() && nCurrentTime < getTimeOn())  
						|| (nCurrentTime >= getTimeOn() && nCurrentTime < getTimeOn() + nTHREE_MINUTES)) // switch ON the A/C unit in the 'pre-start' timeband 
																						 			  // if the internal temperature is above the setpoint
																						 			  // or when the 'scheduled-on' time happens in every case
				{
					// switch on
					//LoggerMgr.getLogger(this.getClass()).info("Swicth on AC unit");
					/*
					LoggerMgr.getLogger(this.getClass()).info("[" + nTimeOn / 60 + ":" + nTimeOn % 60 + "-" + nTimeOff / 60 + ":" + nTimeOff % 60 + "] " + nCurrentTime / 60 + ":" + nCurrentTime % 60 + " [AC ON] PreStart=" + nAnticipatedTimeStart + " Text=" + fExternalTemperature + " Tint=" + fInternalTemperature);
					*/
					changeACstate(nUNIT_ON);
					bExistStartTimeResult = true;
					bStartCalcInProgress = false;
					bStartActionDone = true;
					updateTemperatureDifferenceStart();
					saveFeedbackParameters("TemperatureDifferenceStart", afTemperatureDifferenceStart);
					nPrevTimeStart = nCurrentTime; // to make the exact calculation of the elapsed start time
					bSaveStart = true;
				}
			}
		}
		
		//*** TURN UNIT OFF AND CHECK ELAPSED 'TIMESTART' TIME ***
		else if( nUnitOnOff == nUNIT_ON)  // save elapsedTimeStart only if the A/C unit is ON
			  							  // turn OFF A/C unit only if it is ON
		{
			if(nCurrentTime>=getComputedTimeOn() && nCurrentTime<getComputedTimeOff())  // check if we're inside the 'calculated' ON timeband
																						// to avoid 'fake' data logging in case the A/C turns on (from external command) outside the 'calculated' ON timeband 																						
			{
				nElapsedTimeStart = nCurrentTime - nPrevTimeStart;
			
				if( ((fInternalTemperature > fTemperatureSetpoint - fDiffStartSetpoint && fInternalTemperature < fTemperatureSetpoint + fDiffStartSetpoint))
					|| nElapsedTimeStart > nMaxTimeStart ) {
					if( bSaveStart ) {
						//LoggerMgr.getLogger(this.getClass()).info("Internal Temperature raised setpoint after START action");
						updateElapsedTimeStart();
						saveFeedbackParameters("ElapsedTimeStart", anElapsedTimeStart);
						bSaveStart = false;
					}
				}
			}
			
			if(!(getTimeOn()==0 && getTimeOff()==0)) // don't switch OFF the AC unit when there is not schedulation for the current day
				 									 // (to avoid multiple switch-ON/switch-OFF from 00:00 to 00:01) 
			{	
				if((nCurrentTime >= getComputedTimeOff() && nCurrentTime < getTimeOff()) 
						|| (nCurrentTime >= getTimeOff() && nCurrentTime < getTimeOff() + nTHREE_MINUTES)) // switch OFF the A/C unit in the 'pre-stop' timeband 
					 									 								 				// if the internal temperature is above the setpoint
					 									 								 				// or when the 'scheduled-off' time happens in every case
				{ 
					// switch off
					//LoggerMgr.getLogger(this.getClass()).info("Swicth off AC unit");
					/*
					LoggerMgr.getLogger(this.getClass()).info("[" + nTimeOn / 60 + ":" + nTimeOn % 60 + "-" + nTimeOff / 60 + ":" + nTimeOff % 60 + "] " + nCurrentTime / 60 + ":" + nCurrentTime % 60 + " [AC OFF] PreStop=" + nAnticipatedTimeStop + " Text=" + fExternalTemperature + " Tint=" + fInternalTemperature);
					*/
					changeACstate(nUNIT_OFF);
					bExistStopTimeResult = true;
					bStopCalcInProgress = false;
					bStopActionDone = true;
					updateTemperatureDifferenceStop();
					saveFeedbackParameters("TemperatureDifferenceStop", afTemperatureDifferenceStop);
					nPrevTimeStop = nCurrentTime; // to make the exact calculation of the elapsed stop time
					bSaveStop = true;
				}
			}
		}
	}
	
	
	public int getComputedTimeOn()
	{
		return (nTimeOn - nAnticipatedTimeStart)>=0?(nTimeOn - nAnticipatedTimeStart):0;
	}
	

	public int getComputedTimeOff()
	{
		return (nTimeOff - nAnticipatedTimeStop)>0?(nTimeOff - nAnticipatedTimeStop):0;
	}
	
	
	private void updateElapsedTimeStart()
	{
		anElapsedTimeStart.add(nElapsedTimeStart);
		if( anElapsedTimeStart.size() > nRec )
			anElapsedTimeStart.remove(0);
	}
	

	private void updateElapsedTimeStop()
	{
		anElapsedTimeStop.add(nElapsedTimeStop);
		if( anElapsedTimeStop.size() > nRec )
			anElapsedTimeStop.remove(0);
	}
	
	
	private void updateTemperatureDifferenceStart()
	{
		afTemperatureDifferenceStart.add(fTemperatureDifference);
		if( afTemperatureDifferenceStart.size() > nRec )
			afTemperatureDifferenceStart.remove(0);
	}
	
	private void updateTemperatureDifferenceStop()
	{
		afTemperatureDifferenceStop.add(fTemperatureDifference);
		if( afTemperatureDifferenceStop.size() > nRec )
			afTemperatureDifferenceStop.remove(0);
	}
	
	
	
	protected synchronized void saveFeedbackParameters(String strName, Vector aoParams)
	{
		// overridden on derived class
	}
	
	
	protected synchronized void changeACstate(int nState)
	{
		// overridden on derived class
	}
	
	
	// return current time in minutes (range: 0..1339)
	private static int getCurrentTime()
	{
		GregorianCalendar cal = new GregorianCalendar();
		return cal.get(GregorianCalendar.HOUR_OF_DAY) * 60 + cal.get(GregorianCalendar.MINUTE);
	}
}
