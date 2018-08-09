package com.carel.supervisor.presentation.helper;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class VirtualKeyboard
{

    private static VirtualKeyboard instance = new VirtualKeyboard();
    
    private final String onScreenKey = "onscreenkey";
    
    // la classe css che dovrebbe definire il layout della tastiera
    private String cssClass = "keyboardInput";
    
    //di default la virtualkeyboard è disabilitata:
    private boolean isOnScreenKeyboard = false;

    //si carica in mem. la prima volta che viene chiamata:
    private VirtualKeyboard()
    {
        load();
    }
    
    public static VirtualKeyboard getInstance()
    {
        return instance;
    }
        
    public boolean isOnScreenKey()
    {
        return isOnScreenKeyboard;
    }
    
    public void setOnScreenKey(boolean onScreen)
    {
        String valore = "";
        RecordSet rs = null;
        
        String sql = "select value from productinfo where key='"+onScreenKey+"'";

        if (onScreen)
            valore = "yes";
        else
            valore = "no";
        
        Object[] param = new Object[]{valore};
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        
            if ((rs != null) && (rs.size() > 0))
            {
                String sql_upd = "update productinfo set value=?, lastupdate=current_timestamp where key='"+onScreenKey+"'";
                DatabaseMgr.getInstance().executeStatement(null, sql_upd, param);
            }
            else
            {
                String sql_ins = "insert into productinfo values ('"+onScreenKey+"', ?, current_timestamp)";
                DatabaseMgr.getInstance().executeStatement(null, sql_ins, param);
            }
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(VirtualKeyboard.class);
            logger.error(e);
        }
        
        isOnScreenKeyboard = onScreen;
    }
    
    public void load()
    {
        RecordSet rs = null;
        String sql = "select value from productinfo where key='"+onScreenKey+"'";
        
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
            isOnScreenKeyboard = ("yes".equals(rs.get(0).get(0).toString()));
        else
        {
            String sql_ins = "insert into productinfo values ( ?, ?, current_timestamp)";
            Object[] param = new Object[]{onScreenKey,(isOnScreenKeyboard?"yes":"no")};
            
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql_ins, param);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
    }

    public void setCssClass(String cssClass){
    	this.cssClass = cssClass;
    }
    
    
	public String getCssClass() {
		if(isOnScreenKeyboard)
			return this.cssClass;
		else
			return "";
	}
    
}