package com.carel.supervisor.base.config;

import java.util.Properties;

public class ProductInfoDummy implements IProductInfo
{
	private Properties properties = new Properties();
	
    public ProductInfoDummy()
    {
    }
    
    public void load() throws Exception
    {
    	properties.setProperty("version","ALFA");
    	properties.setProperty("installation","Local");
    	properties.setProperty("type","Enterprise");
    	properties.setProperty("activation","XXX");
    }

    public String get(String key)
    {
    	return properties.getProperty(key);
    }
    
    public void set(String key,String value)
    {
    	
    }

	public void store(String key, String value) throws Exception
	{
	}
    
    public void remove(String key) throws Exception
    {
        
    }
    
    public long getTime(String key)
    {
        return 0L;
    }
}
