package com.carel.supervisor.base.config;

public interface IProductInfo
{
	public void load() throws Exception;
	public String get(String key);
	public void set(String key, String value) throws Exception;
	public void store(String key, String value) throws Exception;
    public void remove(String key) throws Exception;
    public long getTime(String key);
}
