package com.carel.supervisor.presentation.menu;

import java.util.Properties;


public class TabObj
{
    private String idTab = "";
    private Properties propTab = null;

    public TabObj(String idTab)
    {
        this.idTab = idTab;
        this.propTab = new Properties();
    }

    public TabObj(String idTab, Properties prop)
    {
        this.idTab = idTab;
        this.propTab = prop;
    }

    public String getIdTab()
    {
        return this.idTab;
    }

    public void addProperties(String name, String val)
    {
        this.propTab.put(name, val);
    }

    public String getProperties(String name)
    {
        if (this.propTab.getProperty(name) == null)
        {
            return "";
        }
        else
        {
            return this.propTab.getProperty(name);
        }
    }
}
