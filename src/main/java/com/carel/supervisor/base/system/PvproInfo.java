package com.carel.supervisor.base.system;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bean.DeviceListBean;


public class PvproInfo {
    private static PvproInfo me = new PvproInfo();

    
    // logging threshold
    private static Integer nLoggedVariables = null;

    
    // BackupTool info variables
    private static long nBTRead = 0; // last blackboard query
    private static String strBTAlert = "";
    
    
    private PvproInfo()
    {
        // clear BT alert at startup
        try {
        	String sql = "delete from blackboard where app='BT' and \"type\"='alert'";
        	DatabaseMgr.getInstance().executeStatement(sql, null);
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    	}
    }    
    
    
    public static PvproInfo getInstance()
    {
        return me;
    }
    

    public int getLoggedVariables()
    {
    	if( nLoggedVariables == null ) {
	    	try {
	        	String sql = "select count(*) from cfvariable where iscancelled='FALSE' and (idhsvariable is null or isHaccp='TRUE')";
	        	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	    		nLoggedVariables = new Integer(recordset.get(0).get(0).toString());
	    	} catch(DataBaseException e) {
	    		LoggerMgr.getLogger(this.getClass()).error(e);
	    		nLoggedVariables = new Integer(0);
	    	}
    	}
    	
    	return nLoggedVariables.intValue();
    }
    
    
    public void resetLoggedVariables()
    {
    	// force counting of logged variables
    	nLoggedVariables = null;
    }
    
    
    public boolean isLoggingOverload()
    {
    	try {
	    	return getLoggedVariables() > getLoggingThreshold();
    	} catch(Exception e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    		return false;
    	}
    }
    
    
    public int getLoggingOverload()
    {
    	try {
	    	return getLoggedVariables() - getLoggingThreshold();
    	} catch(Exception e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    		return 0;
    	}
    }
    
    
    public int getLoggingThreshold()
    {
    	try {
	    	return Integer.parseInt(ProductInfoMgr.getInstance().getProductInfo().get("logging_threshold"));
    	} catch(NumberFormatException e) {
			// in case it was called before the ProductInfo properties loading
			// situation happen during SRVLRefresh isLoggingOverload call
			// logging threshold check not affected because the engine it is started after the ProductInfoMgr
			return 0;
    	}
    }

    
    public int getLicense()
    {
    	try {
	    	ProductInfo productInfo = new ProductInfo();
	    	productInfo.load();    	
	    	return new Integer(productInfo.get(ProductInfo.LICENSE));
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return 0;
		}
    }
    
    
    public int getActiveDevices()
    {
    	try {
	    	return (Integer)DeviceListBean.countActiveDevice();
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return 0;
		}
    }
    
    
    public int getLicenseOverload()
    {
    	try {
	    	Integer numMaxDevices = getLicense();
	    	Integer numTotDevices = getActiveDevices();
	    	if( numTotDevices > numMaxDevices )
	    		return numTotDevices - numMaxDevices; 
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return 0;
    }
    
    
    public boolean isBTAlert()
    {
    	strBTAlert = getBTAlert();
    	return strBTAlert.length() > 0;
    }

    
    public String getBTAlert()
    {
    	long nCurrentTime = System.currentTimeMillis();
    	if( nCurrentTime - nBTRead < 60 * 1000 )
    		return strBTAlert;
    	nBTRead = nCurrentTime;
    	
    	try {
        	String sql = "select params from blackboard where app='BT' and \"type\"='alert' order by \"timestamp\" desc";
        	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        	if( recordset.size() > 0 )
        		strBTAlert = recordset.get(0).get(0).toString();
        	else
        		strBTAlert = "";
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    		strBTAlert = "";
    	}
    	
    	return strBTAlert;
    }
    
    
    
}
