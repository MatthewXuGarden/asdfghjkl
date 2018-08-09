package com.carel.supervisor.base.test;

import junit.framework.TestCase;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.ICloneable;
import com.carel.supervisor.base.util.SimpleContainer;


public class SimpleContainerTest extends TestCase
{
	private SimpleContainer sc = null;
	private static final Logger logger = LoggerMgr.getLogger(SimpleContainerTest.class);
	
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SimpleContainerTest.class);
    }

    public void testSimpleContainer()
    {
        sc = new SimpleContainer();
    }

    public void testSimpleContainerInt()
    {
        //sc=new SimpleContainer(-1);
        sc = new SimpleContainer(10);
    }

    public void testPutStringICloneable()
    {
        sc = new SimpleContainer();
        sc.put("", new IntegerClonable(new Integer(3)));
        sc.put("", null);
    }

    public void testGetString() throws Exception
    {
        sc = new SimpleContainer();

        IntegerClonable ic = new IntegerClonable(new Integer(3));
        sc.put("key1", ic);

        Object object = sc.get("key1");

        if (!object.toString().equals(ic.toString()))
        {
            throw new TestException("Comportamento non corretto");
        }
    }

    public void testKeysString()
    {
        sc = new SimpleContainer();

        for (int i = 0; i < 10; i++)
        {
            sc.put("key" + i, new IntegerClonable(new Integer(i)));
        }

        Object[] object = sc.keys();

        for (int i = 0; i < object.length; i++)
        {
            logger.info(object[i].toString());
        }
    }

    public void testHasKeyString() throws Exception
    {
        sc = new SimpleContainer();

        for (int i = 0; i < 10; i++)
        {
            sc.put("key" + i, new IntegerClonable(new Integer(i)));
        }

        if (!sc.hasKey("key1"))
        {
            throw new TestException("Comportamento non corretto");
        }

        if (sc.hasKey("ey1"))
        {
            throw new TestException("Comportamento non corretto");
        }

        if (sc.hasKey(null))
        {
            throw new TestException("Comportamento non corretto");
        }
    }
}


class IntegerClonable implements ICloneable
{
    private Integer integer;

    public IntegerClonable(Integer integer)
    {
        this.integer = integer;
    }

    public Object clone()
    {
        return new IntegerClonable(integer);
    } //clone
}
