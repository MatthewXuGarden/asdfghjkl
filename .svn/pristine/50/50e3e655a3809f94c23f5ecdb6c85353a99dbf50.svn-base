package com.carel.supervisor.dataaccess.language;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class LangService
{
    private Map sections = new HashMap();
    private String language = null;
    private static final LangDummy LANG_DUMMY = new LangDummy();
    //Prima release. La seconda deve usare i Locale
    public LangService(String languagePar)
    {
        language = languagePar;
    }

    public String getString(String section, String id)
    {
    	LangSection l = (LangSection) sections.get(section);
    	if (null == l)
    	{
    		l = LANG_DUMMY;
    	}
        return l.get(id);
    }

    public LangSection getSection(String section)
    {
    	LangSection l = (LangSection) sections.get(section);
    	if (null == l)
    	{
    		l = LANG_DUMMY;
    	}
        return l;
    }

    public String getLanguage()
    {
        return language;
    }

    public DateFormat formatDate(Locale locale)
    {
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,
            locale);
    }

    public void addResource(RecordSet recordset)
    {
        Record record = null;
        String code = null;
        for(int i = 0; i < recordset.size(); i++)
        {       
        	record = recordset.get(i);
        	code = (String)record.get("code");
			addResource(code, record);
        }
    }

    private void addResource(String code, Record record)
    {
        LangSection langResource = (LangSection) sections.get(code);

        if (null == langResource)
        {
            langResource = new LangSection(code);
            sections.put(code, langResource);
        }

        langResource.addResource((String)record.get("subcode"), (String)record.get("description"));
    }
}
