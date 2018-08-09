package com.carel.supervisor.dataaccess.language;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class LangMgr implements IInitializable
{
    private static LangMgr meLangMgr = new LangMgr();

    //private static String m_sLanguage = null;
    //private static String m_sCountry = null;
    //private static Locale m_oLocale;
    private Map map = new HashMap();
    private boolean initialized = false;

    private LangMgr()
    {
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            XMLNode xmlNodeTmp = null;

            try
            {
                xmlNodeTmp = xmlStatic.getNode(0);

                String path = xmlNodeTmp.getAttribute("value");
                init();
            }
            catch (Exception ex)
            {
                FatalHandler.manage(this, "...", ex);
            }

            initialized = true;
        }
    }

    public void init() throws Exception
    {
        String name = null;
    	LangService langService = null;
        LangUsedBeanList langList = new LangUsedBeanList();
        LangUsedBean[] langs = langList.retrieveAllLanguage(1,true);
        String sql = "select code, subcode, description from cftext where idsite = 1 and languagecode = ?";
        RecordSet recordset = null;
        for (int i = 0; i < langs.length; i++)
        {
        	name = langs[i].getLangcode();
        	recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{name});
            langService = new LangService(name);
            langService.addResource(recordset);
            map.put(name, langService);
        }
    }

    public static LangMgr getInstance()
    {
        return meLangMgr;
    }

    public LangService getLangService(String language)
    {
        return (LangService) map.get(language);
    }
}
