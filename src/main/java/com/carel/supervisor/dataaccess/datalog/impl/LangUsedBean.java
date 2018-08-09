package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class LangUsedBean
{
    private static final String IDSITE = "idsite";
    private static final String LANGUAGECODE = "languagecode";
    private static final String ISDEFAULT = "isdefault";
    private static final String DESCRIPTION = "description";
    private static final String ENCODING = "encoding";
    private int idsite = -1;
    private String langcode = null;
    private String langdescription = null;
    private String langencoding = null;
    private String isdefault = null;

    public LangUsedBean()
    {
    }

    public LangUsedBean(Record record) throws DataBaseException
    {
        this.idsite = Integer.parseInt(record.get(IDSITE).toString());
        this.langcode = record.get(LANGUAGECODE).toString().trim();
        this.isdefault = record.get(ISDEFAULT).toString().trim();

        Record recordLang = LanguageUsedMgr.getInstance().getLanguage(langcode);
        if (null == recordLang)
        {
	        String sql = "select description,encoding from cflanguage where languagecode = ?";
	        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	                new Object[] { this.langcode });
	        recordLang = rs.get(0);
	        LanguageUsedMgr.getInstance().addLanguage(langcode, recordLang);
        }
        this.langdescription = recordLang.get(DESCRIPTION).toString().trim();
        this.langencoding = recordLang.get(ENCODING).toString().trim();
    }

    public int getIdsite()
    {
        return idsite;
    }

    public void setIdsite(int idsite)
    {
        this.idsite = idsite;
    }

    public String getIsdefault()
    {
        return isdefault;
    }

    public void setIsdefault(String isdefault)
    {
        this.isdefault = isdefault;
    }

    public String getLangcode()
    {
        return langcode;
    }

    public void setLangcode(String langcode)
    {
        this.langcode = langcode;
    }

    public String getLangdescription()
    {
        return langdescription;
    }

    public void setLangdescription(String langdescription)
    {
        this.langdescription = langdescription;
    }
    
    public String getLangEncoding() {
    	return this.langencoding;
    }
}
