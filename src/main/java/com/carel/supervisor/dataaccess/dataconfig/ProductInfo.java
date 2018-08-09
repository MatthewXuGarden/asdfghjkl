package com.carel.supervisor.dataaccess.dataconfig;

import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class ProductInfo implements IProductInfo
{
    public static final String ACTIVATION="activation";
    public static final String IMG="img";
    public static final String IMGTOP="imgtop";
    public static final String INFO="info";
    public static final String INSTALLATION="installation";
    public static final String NAME="name";
    public static final String PRINT="print";
    public static final String PRODUCTCODE="productcode";
    public static final String TYPE="type";
    public static final String VERSION="version";
    public static final String LICENSE="license";
    public static final String FHSFILES="fhsfiles";
    public static final String USERCOMBOBOX = "usercombobox";
	
	private Properties properties = new Properties();
    private Properties proptime = new Properties();
    
	public ProductInfo(){}

    public void load() throws Exception
    {
    	String sql = "select key,value,lastupdate from productinfo";
    	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
    	Record record = null;
    	String key = null;
    	String value = null;
    	long updtime = 0L;
        
    	for (int i = 0; i < recordset.size(); i++)
    	{
    		record = recordset.get(i);
    		key = (String)record.get(0);
    		value = (String)record.get(1);
            
            try
            {
                updtime = ((Timestamp)record.get(2)).getTime();
                proptime.setProperty(key,""+updtime);
            }catch(Exception e){}
            
    		properties.setProperty(key, value);
    	}
    }

    public String get(String key)
    {
    	return properties.getProperty(key);
    }
    
    public long getTime(String key)
    {
        long ret = -1L;
        String sLong = proptime.getProperty(key);
        try{ret = Long.parseLong(sLong); }catch(Exception e){}
        return ret;
    }
    
    public void set(String key, String value) throws Exception
    {
    	properties.setProperty(key,value);
    	
    	String sql = "update productinfo set value = ?, lastupdate = ? where key = ?";
    	Object[] values = new Object[3];
    	values[0] = value;
    	values[1] = new Timestamp(System.currentTimeMillis());
    	values[2] = key;
    	
    	DatabaseMgr.getInstance().executeStatement(null,sql,values);
    }

	public void store(String key, String value) throws Exception
	{
    	String sql = "insert into productinfo values (?,?,?)";
        long currTime = System.currentTimeMillis();
    	Object[] values = new Object[3];
    	values[0] = key;
    	values[1] = value;
    	values[2] = new Timestamp(currTime);
    	
    	DatabaseMgr.getInstance().executeStatement(null,sql,values);

    	properties.setProperty(key,value);
        proptime.setProperty(key,""+currTime);
	}
    
    public void remove(String key) throws Exception
    {
        String sql = "delete from productinfo where key = ?";
        DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{key});
        properties.remove(key);
    }
}
