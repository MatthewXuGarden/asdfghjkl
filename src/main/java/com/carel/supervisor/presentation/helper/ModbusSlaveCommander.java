package com.carel.supervisor.presentation.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;

public class ModbusSlaveCommander 
{
	private static String STATE_STOPPED = "STOPPED";
	private static String STATE_RUNNING = "RUNNING";
	private static String STARTTYPE_AUTO_START = "AUTO_START";
	private static String STARTTYPE_DEMAND_START = "DEMAND_START";
	private static String STARTTYPE_DISABLED= "DISABLED";
	
	public static int E_STATE_STOPPED = 0;
	public static int E_STATE_RUNNING = 1;
	public static final int E_STARTTYPE_AUTO_START = 1;
	public static final int E_STARTTYPE_DEMAND_START = 2;
	public static final int E_STARTTYPE_DISABLED= 3;
	public static void startService()
	{
		try
		{
			String command = BaseConfig.getCarelPath()+"mdslave"+File.separator+"act"+File.separator+"start_mdslave.bat";
	    	Process child = Runtime.getRuntime().exec(command);
	    	child.waitFor();
    	}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.presentation.helper.ModbusSlaveCommander");
            logger.error(e);
		}
	}
	public static void stopService()
	{
		try
		{
			String command = BaseConfig.getCarelPath()+"mdslave"+File.separator+"act"+File.separator+"stop_mdslave.bat";
	    	Process child = Runtime.getRuntime().exec(command);
	    	child.waitFor();
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.presentation.helper.ModbusSlaveCommander");
            logger.error(e);
		}
	}
	public static void setStarttype(int starttype)
	{
		String filename = "";
		switch(starttype)
		{
			case E_STARTTYPE_AUTO_START:
				filename = "set_service_auto.bat";
				break;
			case E_STARTTYPE_DEMAND_START:
				filename = "set_service_manual.bat";
				break;
			case E_STARTTYPE_DISABLED:
				filename = "set_service_disabled.bat";
				break;
			default:
				return;
		}
		try
		{
			String command = BaseConfig.getCarelPath()+"mdslave"+File.separator+"act"+File.separator+filename;
	    	Process child = Runtime.getRuntime().exec(command);
	    	child.waitFor();
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.presentation.helper.ModbusSlaveCommander");
            logger.error(e);
		}
	}
	public static int[] getStateAndStarttype()
	{
		int[] result = null;
		try
		{
			String command = BaseConfig.getCarelPath()+"mdslave"+File.separator+"act"+File.separator+"state_starttype.bat";
			Process child = Runtime.getRuntime().exec(command);
			child.waitFor();
			String filepath= BaseConfig.getCarelPath()+"mdslave"+File.separator+"act"+File.separator+"state_starttype.txt";
			File file = new File(filepath);
			if(file.exists())
			{
				result = new int[2];
				BufferedReader in = new BufferedReader(new FileReader(file));
				String str = null;
				int i = 0;
				while((str=in.readLine()) != null)
				{
					if(i == 0)
					{
						if(str.indexOf(STATE_RUNNING) != -1)
						{
							result[i] = E_STATE_RUNNING;
						}
						else if(str.indexOf(STATE_STOPPED) != -1)
						{
							result[i] = E_STATE_STOPPED;
						}
						else
						{
							in.close();
							return null;
						}
					}
					else if(i == 1)
					{
						if(str.indexOf(STARTTYPE_AUTO_START) != -1)
						{
							result[i] = E_STARTTYPE_AUTO_START;
						}
						else if(str.indexOf(STARTTYPE_DEMAND_START) != -1)
						{
							result[i] = E_STARTTYPE_DEMAND_START;
						}
						else if(str.indexOf(STARTTYPE_DISABLED) != -1)
						{
							result[i] = E_STARTTYPE_DISABLED;
						}
						else
						{
							in.close();
							return null;
						}
						break;
					}
					i++;
				}
				in.close();
			}
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.presentation.helper.ModbusSlaveCommander");
            logger.error(e);
		}
		return result;	
	}
}
