package com.carel.supervisor.base.config;

import com.carel.supervisor.base.xml.XMLNode;
import java.util.Properties;


public class InitializableBase implements IInitializable
{

    public String retrieveAttribute(XMLNode xmlStatic, String attribute,
        String code) throws InvalidConfigurationException
    {
        String value = xmlStatic.getAttribute(attribute);

        if (null == value)
        {
            FatalHandler.manage(this, CoreMessages.format(code, attribute));
        }

        return value;
    }

    public String retrieveAttribute(XMLNode xmlStatic, String attribute,
        String code, String extra) throws InvalidConfigurationException
    {
        String value = xmlStatic.getAttribute(attribute);

        if (null == value)
        {
            FatalHandler.manage(this,
                CoreMessages.format(code, attribute, extra));
        }

        return value;
    }

    public String retrieveAttribute(Properties properties, String attribute,
        String code) throws InvalidConfigurationException
    {
        String value = properties.getProperty(attribute);

        if (null == value)
        {
            FatalHandler.manage(this, CoreMessages.format(code, attribute));
        }

        return value;
    }

    public void checkNode(XMLNode xmlStatic, String attribute, String code)
        throws InvalidConfigurationException
    {
        if (!xmlStatic.getNodeName().equals(attribute))
        {
            FatalHandler.manage(this,
                CoreMessages.format(code, xmlStatic.getNodeName(), attribute));
        }
    }

    public Properties retrieveProperties(XMLNode xmlStatic, String keyName,
        String valueName, String errorCode)
        throws InvalidConfigurationException
    {
        Properties properties = new Properties();
        XMLNode xmlTmp = null;
        String name = null;
        String value = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);
            name = xmlTmp.getAttribute(keyName);

            if (null == name)
            {
                FatalHandler.manage(this,
                    CoreMessages.format(errorCode, keyName));
            }

            value = xmlTmp.getAttribute(valueName);

            if (null == value)
            {
                FatalHandler.manage(this,
                    CoreMessages.format(errorCode, valueName));
            }

            properties.setProperty(name, value);
        }

        return properties;
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        //EMPTY IMPLEMENTATION	
    }
}
