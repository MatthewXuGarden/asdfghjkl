package com.carel.supervisor.dataaccess.dataconfig;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class SystemConfMgr extends InitializableBase
{
    private static SystemConfMgr me = new SystemConfMgr();
    private static final String TABLE = "table";
    private String tableName = "";
    private Map conf = new HashMap();
    
    private SystemConfMgr()
    {
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
    	tableName = retrieveAttribute(xmlStatic.getNode(0), TABLE, "BSSE0003");
    	innerInit();
    }

    private void innerInit() throws InvalidConfigurationException
    {
    	try
        {
            String sql = "select key,value,valueint,defaultvalue,defaultvalueint from " + tableName;
            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
            SystemConf systemConf = null;
            Record record = null;

            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                systemConf = new SystemConf(record);
                conf.put(systemConf.getKey(), systemConf);
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new InvalidConfigurationException("");
        }
    }
    
    public static SystemConfMgr getInstance()
    {
        return me;
    }

    public SystemConf get(String key)
    {
        return (SystemConf)conf.get(key);
    }
    
    //Salvataggio 
    
    public synchronized void save(String key, String newValue, float newValueInt) throws InvalidConfigurationException, DataBaseException
    {
        //Salvare su DB nuovi valori
    	String update = "update systemconf set value=?, valueint=? where key=?";
    	Object[] param = new Object[3];
    	param[0] = newValue;
    	param[1] = new Float(newValueInt);
    	param[2] = key;
    	DatabaseMgr.getInstance().executeStatement(null,update,param);
    	
    }
    
    // Inserimento
    
    public synchronized void insert(String key, String newValue, float newValueInt) 
    	throws InvalidConfigurationException, DataBaseException
    {
    	String update = "insert into systemconf (key,value,valueint,lastupdate) values (?,?,?,?)";
    	Object[] param = new Object[4];
    	param[0] = key;
    	param[1] = newValue;
    	param[2] = new Float(newValueInt);
    	param[3] = new Timestamp(System.currentTimeMillis());
    	DatabaseMgr.getInstance().executeStatement(null,update,param);
    }
    
    //Ripristino default per chiave
    
    public synchronized void restoreDefault(String key) throws InvalidConfigurationException, DataBaseException
    {
        //Salvare su DB valore di default
    	String update = "update systemconf set value=defaultvalue, valueint=defaultvalueint";
    	DatabaseMgr.getInstance().executeStatement(null,update,null);  	
    	
    	//Caricare in memoria il valore di default
    }
    
    public synchronized void refreshSystemInfo() throws InvalidConfigurationException
    {
    	conf.clear();
    	innerInit();
    }

}
