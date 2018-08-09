package com.carel.supervisor.presentation.menu.configuration;

import com.carel.supervisor.presentation.menu.MenuSection;
import java.util.ArrayList;
import java.util.List;


public class MenuConfig
{
    private List listSection = null;
    private String confName = null;

    public MenuConfig(String cName)
    {
        this.confName = cName;
    }

    public String getConfName()
    {
        return this.confName;
    }

    public void setConfName(String cName)
    {
        this.confName = cName;
    }

    public void addSection(MenuSection obj)
    {
        boolean found = false;
        if (listSection == null)
        {
            listSection = new ArrayList();
        }

        // Modifiche per gestione link plugin
        MenuSection msTmp = null;
        for(int i=0; i<listSection.size(); i++)
        {
            msTmp = (MenuSection)listSection.get(i);
            if(msTmp != null)
            {
                if(msTmp.getSectionName() != null && 
                   msTmp.getSectionName().equalsIgnoreCase(obj.getSectionName()))
                {
                    // Replace section with same section name
                    listSection.remove(i);
                    listSection.add(i,obj);
                    found = true;
                    break;
                }
            }
        }
        
        if(!found)
            listSection.add(obj);
    }

    public MenuSection getSection(String id)
    {
        MenuSection tmp = null;

        for (int i = 0; i < listSection.size(); i++)
        {
            tmp = (MenuSection) listSection.get(i);

            if ((tmp != null) && (tmp.getSectionName().equalsIgnoreCase(id)))
            {
                break;
            }
        }

        return tmp;
    }

    public String[] getSectionName()
    {
        String[] sections = new String[listSection.size()];

        for (int i = 0; i < sections.length; i++)
        {
            sections[i] = ((MenuSection) listSection.get(i)).getSectionName();
        }

        return sections;
    }
}
