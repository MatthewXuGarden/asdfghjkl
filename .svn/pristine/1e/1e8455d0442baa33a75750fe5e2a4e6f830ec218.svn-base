package com.carel.supervisor.presentation.helper;

import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class Buzzer {

	private static final String buzzerKey = "buzzer";
	
	private static boolean isBuzzerOn = false;
	
	public static boolean isBuzzerOn()
    {
        return isBuzzerOn;
    }

	public static void setBuzzerOn(boolean buzzerOn)
    {
        String valore = "";
        RecordSet rs = null;
        
        String sql = "select value from productinfo where key='"+buzzerKey+"'";

        if (buzzerOn)
            valore = "on";
        else
            valore = "off";
        
        Object[] param = new Object[]{valore};
        
        try
        {
        	int port = 1980;
    		String result = SocketComm.sendCommand("localhost", port, "MIO;11;"+(buzzerOn?"1":"0"));
        	
    		if(result.equals("0"))
    		{
    			rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
    	        
                if ((rs != null) && (rs.size() > 0))
                {
                    String sql_upd = "update productinfo set value=?, lastupdate=current_timestamp where key='"+buzzerKey+"'";
                    DatabaseMgr.getInstance().executeStatement(null, sql_upd, param);
                }
                else
                {
                    String sql_ins = "insert into productinfo values ('"+buzzerKey+"', ?, current_timestamp)";
                    DatabaseMgr.getInstance().executeStatement(null, sql_ins, param);
                }
    		}
    		else
    			return;
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(VirtualKeyboard.class);
            logger.error("Error on setting buzzer status: "+e);
        }
        
        isBuzzerOn = buzzerOn;
    }
	
	public static void init()
    {
        RecordSet rs = null;
        String sql = "select value from productinfo where key='"+buzzerKey+"'";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(VirtualKeyboard.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
        	isBuzzerOn = ("on".equals(rs.get(0).get(0).toString()));
        	
        	//send activation to InfoService
        	setBuzzerOn (isBuzzerOn);
        }
        else
        {
            // if the key is does not exists, then set default value to "on" (buzzer active)
        	String sql_ins = "insert into productinfo values ( ?, ?, current_timestamp)";
            Object[] param = new Object[]{buzzerKey,"on"};
            
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql_ins, param);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(Buzzer.class);
                logger.error(e);
            }
            
            //send activation to InfoService
            setBuzzerOn (true);
        }
    } 
}
