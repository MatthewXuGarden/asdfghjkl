package com.carel.supervisor.base.test;

import junit.framework.TestCase;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.BaseContainer;
import com.carel.supervisor.base.util.ICloneable;
import com.carel.supervisor.base.util.SimpleContainer;


public class BaseContainerTest extends TestCase
{
    private BaseContainer bc = null;
    private static final Logger logger = LoggerMgr.getLogger(BaseContainerTest.class);

    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();
        junit.textui.TestRunner.run(BaseContainerTest.class);
    }

    public void testPut()
    {
        bc = new BaseContainer();

        Object s = null;
        bc.put("key", (ICloneable) s);
        bc.put(null, null);
        bc.put("key", null);
        bc.put(null, (ICloneable) s);
    }

    public void testGet() throws Exception
    {
        bc = new BaseContainer();

        StringClonable sc = new StringClonable("Prova");
        bc.put("key", sc);

        Object object = bc.get("key");

        if (!object.toString().equals(sc.toString()))
        {
            throw new TestException("Comportamento non corretto");
        }
    }

    public void testClear()
    {
        bc = new BaseContainer();
        bc.clear();
    }

    public void testKeys()
    {
        bc = new BaseContainer();

        StringClonable sc = null;

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, sc);
        } //for

        Object[] object = bc.keys();
        logger.info("");

        for (int i = 0; i < 10; i++)
        {
        	logger.info(object[i].toString());
        } //for
    }

    public void testClone()
    {
        bc = new BaseContainer();

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, new StringClonable("Stringa " + i));
        } //for

        Object clone = bc.clone();
        Object[] object = (Object[]) ((SimpleContainer) clone).keys();
        logger.info("Test Clone");

        for (int i = 0; i < 10; i++)
        {
        	logger.info(object[i].toString());
        } //for
    }

    public void testSize()
    {
        bc = new BaseContainer();

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, new StringClonable("Stringa " + i));
        } //for

        logger.info("Size=" + bc.size());
    }

    public void testHasKey() throws Exception
    {
        bc = new BaseContainer();

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, new StringClonable("Stringa " + i));
        } //for

        if (!bc.hasKey("key1"))
        {
            throw new TestException("Comportamento non corretto");
        }

        if (bc.hasKey(""))
        {
            throw new TestException("Comportamento non corretto");
        }

        if (bc.hasKey(null))
        {
            throw new TestException("Comportamento non corretto");
        }
    }

    public void testIsEmpty() throws Exception
    {
        bc = new BaseContainer();

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, new StringClonable("Stringa " + i));
        } //for

        if (bc.isEmpty())
        {
            throw new TestException("Comportamento non corretto");
        }

        bc = new BaseContainer();

        if (!bc.isEmpty())
        {
            throw new TestException("Comportamento non corretto");
        }
    }

    public void testAdd() throws Exception
    {
        BaseContainer tmpBc = new BaseContainer();
        bc = new BaseContainer();

        for (int i = 0; i < 10; i++)
        {
            new StringClonable("Prova" + i);
            bc.put("key" + i, new StringClonable("Stringa " + i));
        } //for

        tmpBc.add(bc);

        if (tmpBc.size() != 10)
        {
            throw new TestException("Comportamento non corretto");
        }
    }
}


class StringClonable implements ICloneable
{
    private String str;

    public StringClonable(String str)
    {
        this.str = str;
    }

    public Object clone()
    {
        return new StringClonable(str);
    } //clone
}
