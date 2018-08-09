package com.carel.supervisor.plugin.base;

import java.sql.Timestamp;

import java.util.Properties;


public class BasePluginImpl implements Plugin
{
    private Properties properties;

    public BasePluginImpl(Properties properties)
    {
        this.properties = properties;
    }

    public Properties getProperties()
    {
        // TODO Auto-generated method stub
        return properties;
    }

    public String getActivation()
    {
        return (String) properties.get(ACTIVATION);
    }

    public String getCode()
    {
        return (String) properties.get(CODE);
    }

    public int getIdplugin()
    {
        return ((Integer) properties.get(IDPLUGIN)).intValue();
    }

    public Timestamp getInstall()
    {
        return (Timestamp) properties.get(INSTALL);
    }

    public String getIsactive()
    {
        return (String) properties.get(ISACTIVE);
    }

    public String getIscancelled()
    {
        return (String) properties.get(ISCANCELLED);
    }

    public String getName()
    {
        return (String) properties.get(NAME);
    }

    public String getPlugincode()
    {
        return (String) properties.get(PLUGINCODE);
    }

    public Timestamp getRegistration()
    {
        return (Timestamp) properties.get(REGISTRATION);
    }

    public String getType()
    {
        return (String) properties.get(TYPE);
    }

    public String getVersion()
    {
        return (String) properties.get(VERSION);
    }

    public int install()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void startPlugin()
    {
        // TODO Auto-generated method stub
    }

    public void stopPlugin()
    {
        // TODO Auto-generated method stub
    }
}
