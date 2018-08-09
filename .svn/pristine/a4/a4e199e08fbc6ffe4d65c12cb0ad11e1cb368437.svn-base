package com.carel.supervisor.dataaccess.monitor;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.xml.XMLNode;
import java.util.Properties;


public class StatementMgr extends InitializableBase implements IInitializable,
    IStatement
{
    private static StatementMgr me = new StatementMgr();
    private final static String CLASS = "class";
    private final static String NAME = "name";
    private final static String VALUE = "value";
    private IStatement stat = null;

    private StatementMgr()
    {
    }

    public static StatementMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        Properties prop = retrieveProperties(xmlStatic, NAME, VALUE, "BSSE0002");
        String className = retrieveAttribute(prop, CLASS, "BSSE0002");

        try
        {
            stat = (IStatement) FactoryObject.newInstance(className);
        }
        catch (Exception e)
        {
            stat = new StatementDummy();
        }
    }

    public ICounter retrieve(String sql)
    {
        return stat.retrieve(sql);
    }

    public Object[][] result()
    {
        return stat.result();
    }
    
    public void clear()
    {
    	stat.clear();
    }
}
