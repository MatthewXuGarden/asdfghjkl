package com.carel.supervisor.director;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.device.DeviceStatusMgr;

public class AlarmLedMgr extends Thread{

	private static boolean check = false;
	private static boolean activeAlarms = false;
	private int port = 1980;
	
	// string commands for 'Internal IO' led managment
	private String LED_ON ="MIO;7";
	private String LED_OFF ="MIO;8";
	
	private String TIME_ON ="1000";
	private String TIME_OFF ="1000";
	
	private String RESULT_OK ="0";
	
	public AlarmLedMgr()
	{
		setName("AlarmLedMgr");
	}
	public void run()
	{
		check = true;
		String result = "";
		
		//There is no need to execute the Thread if a DEMO version is running
		if(BaseConfig.isDemo())
			return;
		
		while (check)
		{
			try
			{
				if(DeviceStatusMgr.getInstance().existAlarm())
				{
					if (!activeAlarms)
					{
						// if there are active alarms
						// start led blinking
						result = SocketComm.sendCommand("localhost", port, LED_ON+";"+TIME_ON+";"+TIME_OFF);
						
						if(result.equals(RESULT_OK))
						{
							activeAlarms = true;
						}
						else
						{
							LoggerMgr.getLogger(AlarmLedMgr.class).error("Error during sending Led-on command");
						}
					}
				}
				else
				{
					if(activeAlarms)
					{
						// stop led blinking
						result = SocketComm.sendCommand("localhost", port, LED_OFF);
						activeAlarms = false;
						
						if(!result.equals(RESULT_OK))
						{
							LoggerMgr.getLogger(AlarmLedMgr.class).error("Error during sending Led-off command");
						}
					}
				}
			
				// check every 5 secs
				Thread.sleep(5000);
			
			}catch(Exception e)
			{
				LoggerMgr.getLogger(AlarmLedMgr.class).error(e);
			}
		}
		
		// always led-off in case of thread stop
		result = SocketComm.sendCommand("localhost", port, LED_OFF);
		if(!result.equals(RESULT_OK))
		{
			LoggerMgr.getLogger(AlarmLedMgr.class).error("Error during sending Led-off command");
		}
		
		activeAlarms = false;
	}
	
	public void stopCheckAlarms()
	{	
		check = false;
		
		// wait 5 secs (to ensure the last 'alarm check cycle' is concluded
		// and there are not 'write' operation on FTD2IO after dll has been closed)
		// -- only if there is the need to close all dlls (isMustCreateProtocolFile) --
		if(DirectorMgr.getInstance().isMustCreateProtocolFile())
		{
			try
			{
				Thread.sleep(5000);
			}
			catch (Exception e)
			{
				LoggerMgr.getLogger(AlarmLedMgr.class).error(e);
			}
		}
	}
}
