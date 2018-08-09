package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.dataaccess.db.Record;


public class SystemConf
{
    private final static String KEY = "key";
    private final static String VALUE = "value";
    private final static String VALUEINT = "valueint";
    private final static String DEFAULTVALUE = "defaultvalue";
    private final static String DEFAULTVALUEINT = "defaultvalueint";
    
	private String key = null;
    private String value = null;
    private float valueNum = 0;
    private String defaultValue = null;
    private float defaultNumValue = 0;
    private boolean isNumber = false;

    public SystemConf(Record record)
    {
    	key = (String)record.get(KEY);
    	value = (String)record.get(VALUE);
    	valueNum = ((Float)record.get(VALUEINT)).floatValue();
    	defaultValue = (String)record.get(DEFAULTVALUE);
    	defaultNumValue = ((Float)record.get(DEFAULTVALUEINT)).floatValue();
    	if (null != value)
    	{
    		isNumber = true;
    	}
    }

    /**
     * @return: double
     */
    public float getDefaultNumValue()
    {
        return defaultNumValue;
    }

    /**
     * @param defaultIntValue
     */
    protected void setDefaultNumValue(float defaultNumValue)
    {
        this.defaultNumValue = defaultNumValue;
    }

    /**
     * @return: String
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * @param defaultValue
     */
    protected void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * @return: String
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key
     */
    protected void setKey(String key)
    {
        this.key = key;
    }

    /**
     * @return: String
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     */
    protected void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @return: double
     */
    public float getValueNum()
    {
        return valueNum;
    }

    /**
     * @param valueint
     */
    protected void setValueNum(float valueNum)
    {
        this.valueNum = valueNum;
    }
    
    public boolean isNumber()
    {
    	return this.isNumber;
    }
}
