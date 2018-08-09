package com.carel.supervisor.dataaccess.datalog.impl;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class LanguageUsedMgr
{
	private static LanguageUsedMgr me = new LanguageUsedMgr();
	private Map data = new HashMap();
	private Map sites = new HashMap();
	
    private LanguageUsedMgr()
    {
    }
    
    public static LanguageUsedMgr getInstance()
    {
    	return me;
    }
    
    public void addLanguage(String languagecode, Record record)
    {
    	if (!data.containsKey(languagecode))
    	{
    		data.put(languagecode, record);
    	}
    }
    
    public Record getLanguage(String languagecode)
    {
    	return (Record)data.get(languagecode);
    }
    
    public void addLanguagesSites(int idSite, RecordSet recordSet) throws DataBaseException
    {
    	Integer id = new Integer(idSite);
//    	if (!sites.containsKey(id))
//    	{
//    		LangUsedBean[] list = new LangUsedBean[recordSet.size()];
//
//            for (int i = 0; i < recordSet.size(); i++)
//            {
//                list[i] = new LangUsedBean(recordSet.get(i));
//            }
//
//    		sites.put(id, list);
//    	}
    	LangUsedBean[] list = new LangUsedBean[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            list[i] = new LangUsedBean(recordSet.get(i));
        }

		sites.put(id, list);
    }
    
    public LangUsedBean[] getLanguagesSite(int idSite)
    {
    	return (LangUsedBean[])sites.get(new Integer(idSite));
    }
    
    public void reloadLanguagesSites(int idSite, RecordSet recordSet) throws DataBaseException
    {
    	sites.remove(new Integer(idSite));
    	LangUsedBean[] list = new LangUsedBean[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            list[i] = new LangUsedBean(recordSet.get(i));
        }

		sites.put(new Integer(idSite), list);
    }
}
