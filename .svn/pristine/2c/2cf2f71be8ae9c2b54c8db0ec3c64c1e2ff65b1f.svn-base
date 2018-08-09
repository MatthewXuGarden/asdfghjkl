package com.carel.supervisor.director.guardian;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Random;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;


public class GuardianCheck
{
    private static final String SERVICE_NAME = "PVPRO_Guardian";
    private static String keyPVPRO = new String();
    private static boolean keySent = false;

    private GuardianCheck()
    {
    }

    public static boolean isLive()
    {
    	if( keyPVPRO.isEmpty() ) {
        	// generate a random key to be used by Guardian during probe checking
    		Random rnd1 = new Random();
    		Random rnd2 = new Random(System.currentTimeMillis());
    		long nKey = rnd1.nextLong() ^ rnd2.nextLong();
    		keyPVPRO = String.format("%X", nKey);
    	}

    	boolean response = "OK".equalsIgnoreCase(send("live" + keyPVPRO));
    	if( response )
    		keySent = true;
    	
    	return response;
    }

    
    public static boolean isGuardian(String key)
    {
    	if( keyPVPRO.equals(key) ) {
    		return true;
    	}
    	else {
    		if( !keySent )
    			isLive(); // send key to Guardian
    		return false;
    	}
    }
    
    
    public static String send(String cmd)
    {
        int port = 1974;
        String machine = "localhost";

        try
        {
            port = (int) SystemConfMgr.getInstance().get("guardianport").getValueNum();
        }
        catch (Exception e)
        {
            //Prendo il defalut
        }

        try
        {
            machine = SystemConfMgr.getInstance().get("machine").getValue();
        }
        catch (Exception e)
        {
            //Prendo il defalut
        }

        String ris = null;
        
        if ((null != machine) && (!"".equals(machine)))
        {
            Socket s = null;
            OutputStreamWriter os = null;
            InputStreamReader is = null;
            StringBuffer sb = new StringBuffer();
            int c = -1;

            try
            {
                s = new Socket(machine, port);
                os = new OutputStreamWriter(s.getOutputStream(),"UTF8");
                is = new InputStreamReader(s.getInputStream(),"UTF8");

                os.write(cmd);
                os.flush();
                s.shutdownOutput();

                while ((c = is.read()) != -1)
                {
                    sb.append((char) c);
                }

                ris = sb.toString();            
            }

            catch (ConnectException e)
            {
                LoggerMgr.getLogger(GuardianCheck.class).error(e);
            }
            catch (IOException e)
            {
                LoggerMgr.getLogger(GuardianCheck.class).error(e);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (Exception e)
                {
                }

                try
                {
                    os.close();
                }
                catch (Exception e)
                {
                }

                try
                {
                    s.close();
                }
                catch (Exception e)
                {
                }
            }
        }
        else
        {
        	ris = "OK";
        }
        return ris;
    }

    public static void startGuardian()
    {
        try
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);

            String machine = SystemConfMgr.getInstance().get("machine").getValue();

            if (machine.equalsIgnoreCase("localhost"))
            {
                machine = "";
            }
            else
            {
                machine = "\\\\" + machine;
            }

            ScriptInvoker script = new ScriptInvoker();
            logger.info("Starting guardian");
            keySent = false;
            script.execute(new String[] { "sc", machine, "start", SERVICE_NAME },
                BaseConfig.getLogFile());
            Thread.sleep(10000); //10 secondi di tempo per aspettare che parta
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);
            logger.error(e);
        }
    }

    public static void restartGuardian()
    {
        try
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);

            String machine = SystemConfMgr.getInstance().get("machine").getValue();
            ScriptInvoker script = new ScriptInvoker();

            if (machine.equalsIgnoreCase("localhost"))
            {
                machine = "";
            }
            else
            {
                machine = "\\\\" + machine;
            }

            logger.info("Stopping guardian");
            script.execute(new String[] { "sc", machine, "stop", SERVICE_NAME },
                BaseConfig.getLogFile());

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
            }

            logger.info("Starting guardian");
            keySent = false;
            script.execute(new String[] { "sc", machine, "start", SERVICE_NAME },
                BaseConfig.getLogFile());

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);
            logger.error(e);
        }
    }
    
    public static void stopGuardian()
    {
        try
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);

            String machine = SystemConfMgr.getInstance().get("machine").getValue();
            ScriptInvoker script = new ScriptInvoker();

            if (machine.equalsIgnoreCase("localhost"))
            {
                machine = "";
            }
            else
            {
                machine = "\\\\" + machine;
            }

            logger.info("Stopping guardian");
            script.execute(new String[] { "sc", machine, "stop", SERVICE_NAME },
                BaseConfig.getLogFile());

            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {}
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);
            logger.error(e);
        }
    }
    
    public static void enableGuardian(boolean status)
    {
        try
        {
            String cmd = "disabled";
            if(status)
                cmd = "auto";
            
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);

            String machine = SystemConfMgr.getInstance().get("machine").getValue();
            ScriptInvoker script = new ScriptInvoker();

            if (machine.equalsIgnoreCase("localhost"))
            {
                machine = "";
            }
            else
            {
                machine = "\\\\" + machine;
            }

            logger.info("Disable guardian");
            script.execute(new String[]{"sc",machine,"config",SERVICE_NAME,"start= ",cmd},BaseConfig.getLogFile());

            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {}
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(GuardianCheck.class);
            logger.error(e);
        }
    }
    
    /**
     * Controllo via socket della presenza o meno della finestra di allarme attiva.
     */
    public static String isEnableWin()
    {
    	String ret = "0";
    	ret = send("checkwin");
    	if(ret == null || ret == "" || ret.equalsIgnoreCase("OK"))
    		ret = "0";
    	return ret;
    }
    
    /**
     * Get via socket dei messaggi presenti in finestra.
     */
    public static String getEnableWin()
    {
    	return send("getwin");
    }
    
    /**
     * Stop del suono
     */
    public static void snoozeSound() 
    {
    	send("snzsndgp");
    }
    
    /**
     * Stop della finestra
     */
    public static void closeWindow() 
    {
    	send("ackgp");
    }
}
