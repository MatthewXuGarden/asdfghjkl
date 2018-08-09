package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

import java.util.HashMap;
import java.util.Map;


public class LangUsedBeanList
{
    private Map langUsedList = new HashMap();

    public LangUsedBean[] retrieveAllLanguage(int idsite)
    	throws DataBaseException
    {
    	return retrieveAllLanguage(idsite,false);
    }
    public LangUsedBean[] retrieveAllLanguage(int idsite,boolean reload)
        throws DataBaseException
    {
    	LangUsedBean[] list = LanguageUsedMgr.getInstance().getLanguagesSite(idsite);
    	if (null == list || reload)
    	{
    		String sql = "select * from cfsiteext where idsite = ? order by isdefault desc";
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { new Integer(idsite) });
            LanguageUsedMgr.getInstance().addLanguagesSites(idsite, rs);
            list = LanguageUsedMgr.getInstance().getLanguagesSite(idsite);
    	}
    	
    	return list;
    }

    public static String getDefaultLanguage(int idsite)
        throws DataBaseException
    {
        String sql = "select languagecode from cfsiteext where idsite = ? and isdefault = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), "TRUE" });

        if (rs.size() > 0)
        {
            return rs.get(0).get("languagecode").toString();
        }
        else
        {
            return "";
        }
    }

    public static String getDefaultLanguageDescription(int idsite)
        throws DataBaseException
    {
        String sql = "select * from cfsiteext where idsite = ? and isdefault = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), "TRUE" });
        LangUsedBean defLang = new LangUsedBean(rs.get(0));

        return defLang.getLangdescription();
    }
    
    public static String getDefaultLanguageEncoding(int idsite)
	    throws DataBaseException
	{
	    String sql = "select * from cfsiteext where idsite = ? and isdefault = ?";
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[] { new Integer(idsite), "TRUE" });
	    LangUsedBean defLang = new LangUsedBean(rs.get(0));
	
	    return defLang.getLangEncoding();
	}


    public LangUsedBean[] retrieveNotDefaultLanguages(int idsite)
        throws DataBaseException
    {
        String sql = "select * from cfsiteext where idsite = ? and isdefault = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), "FALSE" });

        LangUsedBean[] list = new LangUsedBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            list[i] = new LangUsedBean(rs.get(i));
            langUsedList.put(list[i].getLangcode(), list[i]);
        }

        return list;
    }
}
