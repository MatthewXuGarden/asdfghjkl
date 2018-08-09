package com.carel.supervisor.base.profiling;

import java.io.Serializable;
import java.util.*;

public class UserProfile implements Serializable
{
    private Map sections;
    private boolean isDummy = true;
    

    public UserProfile()
    {
        sections = new HashMap();
        isDummy = true;
    }

    public SectionProfile getSection(String sectionName)
    {
        return (SectionProfile) sections.get((String) sectionName);
    }

    public void addSection(String sectionName, SectionProfile value)
    {
        this.isDummy = false;
        sections.put(sectionName, value);
    }

    public Collection sections()
    {
        return sections.values();
    }

    public boolean isDummy()
    {
        return this.isDummy;
    }

    public void setNoDummy()
    {
        this.isDummy = false;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String keySection = "";
        String keyProperty = "";

        Iterator i = sections.keySet().iterator();

        while (i.hasNext())
        {
            keySection = (String) i.next();

            SectionProfile tmpSp = (SectionProfile) sections.get(keySection);

            buffer.append("Sezione: " + keySection + "\n");

            Iterator j = tmpSp.listProperty();

            while (j.hasNext())
            {
                keyProperty = (String) j.next();
                buffer.append("\t-> " + keyProperty + ":\n");

                String[] vls = tmpSp.getValues(keyProperty);

                for (int k = 0; k < vls.length; k++)
                {
                    buffer.append("\t\t" + vls[k] + "\n");
                }
            }
        }

        return buffer.toString();
    }
}
