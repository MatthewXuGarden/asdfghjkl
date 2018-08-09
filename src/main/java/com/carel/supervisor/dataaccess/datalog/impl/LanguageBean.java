package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.dataaccess.db.Record;


public class LanguageBean
{
    private static final String TABLEID = "tableid";
    private static final String DESCRIPTION = "description";
    private static final String LONGDESCRIPTION = "longdescr";
    private static final String SHORTDESCRIPTION = "shortdescr";
    private int id = 0;
    private String description = "";
    private String longDescription = "";
    private String shortDescription = "";

    public LanguageBean(int i, String description, String shortDescription,
        String longDescription)
    {
        this.id = i;
        this.description = description;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public LanguageBean(Record rec)
    {
        this.id = ((Integer) (rec.get(TABLEID))).intValue();

        String desc = (String) rec.get(DESCRIPTION);
        String shortDesc = (String) rec.get(SHORTDESCRIPTION);
        String longDesc = (String) rec.get(LONGDESCRIPTION);

        this.description = desc;
        this.shortDescription = shortDesc;
        this.longDescription = longDesc;

        // Check UNICODE STRING
        if ((this.description != null) &&
                (this.description.indexOf("\\u") != -1))
        {
            this.description = UtilityString.loadConvert(this.description.toCharArray(),
                    0, this.description.length());
        }

        if ((this.shortDescription != null) &&
                (this.shortDescription.indexOf("\\u") != -1))
        {
            this.shortDescription = UtilityString.loadConvert(this.shortDescription.toCharArray(),
                    0, this.shortDescription.length());
        }

        if ((this.longDescription != null) &&
                (this.longDescription.indexOf("\\u") != -1))
        {
            this.longDescription = UtilityString.loadConvert(this.longDescription.toCharArray(),
                    0, this.longDescription.length());
        }
    }

    public int getId()
    {
        return this.id;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getLongDescription()
    {
        return this.longDescription;
    }

    public void setId(int s)
    {
        this.id = s;
    }

    public void setDescription(String s)
    {
        this.description = s;
    }

    public void setLongDescription(String s)
    {
        this.longDescription = s;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }
}
