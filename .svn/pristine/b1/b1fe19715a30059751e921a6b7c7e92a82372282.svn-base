package com.carel.supervisor.dataaccess.db.types;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


public class TypeMgr extends InitializableBase
{
    private static final String JAVA_TYPE = "javatype";
    private static final String CLASS = "class";
    private static final String TYPE = "type";
    private static final String ELEMENT = "element";
    private Map registry = new HashMap();
    private Map registryByType = new HashMap();

    public TypeMgr(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        String type = null;
        String className = null;
        String javaTypeName = null;
        int javaType = -1;
        IDBType dbType = null;
        XMLNode xmlTmp = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);

            if (!xmlTmp.getNodeName().equals(ELEMENT))
            {
                FatalHandler.manage(this,
                    CoreMessages.format("DTCE0010", xmlTmp.getNodeName(),
                        ELEMENT));
            }

            type = retrieveAttribute(xmlTmp, TYPE, "DTCE0013", String.valueOf(i));
            className = retrieveAttribute(xmlTmp, CLASS, "DTCE0013",
                    String.valueOf(i));
            javaTypeName = retrieveAttribute(xmlTmp, JAVA_TYPE, "DTCE0013",
                    String.valueOf(i));

            try
            {
                javaType = Integer.parseInt(javaTypeName);
            }
            catch (Exception e)
            {
                FatalHandler.manage(this,
                    CoreMessages.format("DTCE0013",
                        new Object[] { JAVA_TYPE, javaTypeName, String.valueOf(
                                i) }));
            }

            if (registry.containsKey(type))
            {
                dbType = ((IDBType) registry.get(type));
                dbType.addType(javaType);
            }
            else
            {
                try
                {
                    dbType = (IDBType) Class.forName(className).newInstance();
                }
                catch (Exception ex)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("DTCE0013",
                            new Object[] { CLASS, className, String.valueOf(i) }));
                }

                dbType.addType(javaType);
                registry.put(type, dbType);
            }

            registryByType.put(new Integer(javaType), dbType);
        }
    }

    public IDBType get(String type)
    {
        return (IDBType) registry.get(type);
    }

    public IDBType[] getListTypes(String[] types)
    {
        IDBType[] dbTypes = new IDBType[types.length];

        for (int i = 0; i < types.length; i++)
        {
            dbTypes[i] = (IDBType) registry.get(types[i]);
        }

        return dbTypes;
    }

    public IDBType getByType(int type)
    {
        return (IDBType) registryByType.get(new Integer(type));
    }
}
