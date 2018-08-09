package com.carel.supervisor.presentation.helper;

import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.script.ScriptInvoker;


public class FtpCommander 
{
    public static void startFTP(String server)
    {
        ScriptInvoker script = new ScriptInvoker();
        try
        {
            script.execute(new String[] { "net", "start", "\""+server+"\"" },"C:\\RESULT.TXT" );
        }
        catch (Exception e){}
    }
    
    public static void stopFTP(String server)
    {
        ScriptInvoker script = new ScriptInvoker();
        try
        {
            script.execute(new String[] {  "net", "stop", "\""+server+"\"" },"C:\\RESULT.TXT" );
        }
        catch (Exception e){}
    }
    
    public static boolean testFTP()
    {
        String resp = SocketComm.sendCommand("localhost",21,"");
        if(resp != null && resp.equalsIgnoreCase("ERROR"))
            return false;
        else
            return true;
    }
}
