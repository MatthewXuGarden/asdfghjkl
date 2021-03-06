package com.carel.supervisor.base.system;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.SocketComm;


public class SystemInfoExt
{
    private static SystemInfoExt me = new SystemInfoExt();
    private int port = 1980;
    
    private SystemInfoExt()
    {
        try
        {
            port = Integer.parseInt(BaseConfig.getProductInfo("info"));
        }
        catch (Exception e)
        {
            port = 1980;
        }
    }

    public static SystemInfoExt getInstance()
    {
        return me;
    }

    public String getMacAddrees()
    {
        //return SocketComm.sendCommand("localhost", port, "GSI;1");
    	return "001999B83BEC";
    	//return "901BOE4A64FE";
    }
    
    public String getMacAddress()
    {
        //return SocketComm.sendCommand("localhost", port, "GSI;11");
    	return "001999B83BEC";
    	//return "901BOE4A64FE";

    }
    
    public String getTotalRam()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;2");
    }

    public String getUsedRam()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;3");
    }

    public String getCpuInfo()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;4");
    }

    public String getRamPerUsage()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;5");
    }

    public String getCpuPerUsage()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;6");
    }

    public String getCpuUsage()
    {
        return SocketComm.sendCommand("localhost", port, "GSI;10");
    }

    public String getDiskUsage(String driver)
    {
        return SocketComm.sendCommand("localhost", port, "GSI;" + driver);
    }
}
