package com.carel.supervisor.base.util;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


public class ExceptionHandler extends InitializableBase
{
    //private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String ELEMENT = "element";
    private static ExceptionHandler me = new ExceptionHandler();
    private Map exceptions = new HashMap();
    private int idMain = 0;
    private boolean initialized = false;
    private boolean active = false;

    private ExceptionHandler()
    {
    }

    public static ExceptionHandler getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            try
            {
                XMLNode xmlTmp = null;

                //String name = null;
                String value = null;

                for (int i = 0; i < xmlStatic.size(); i++)
                {
                    xmlTmp = xmlStatic.getNode(i);
                    checkNode(xmlTmp, ELEMENT, "BSSE0001");

                    //name = retrieveAttribute(xmlTmp, NAME, "BSSE0002");
                    value = retrieveAttribute(xmlTmp, VALUE, "BSSE0002");

                    if (value.equalsIgnoreCase("true"))
                    {
                        active = true;
                    }
                    else
                    {
                        active = false;
                    }
                }
            }
            catch (Exception ex)
            {
                FatalHandler.manage(this,
                    CoreMessages.format("BSSE0004", ex.getMessage()), ex);
            }

            initialized = true;
        }
    }

    public boolean initialized()
    {
        return initialized;
    }

    public String addException(Throwable throwable)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(out));

        String key = out.toString();

        if (!active)
        {
            return key;
        }

        Counter counter = (Counter) exceptions.get(key);

        if (null == counter)
        {
            idMain++;
            counter = new Counter(idMain);
            exceptions.put(key, counter);
        }

        counter.add();

        if (1 == counter.num())
        {
            return "[exceptionID:" + String.valueOf(counter.getId()) + "] " +
            key;
        }
        else
        {
            return "[exceptionID:" + String.valueOf(counter.getId()) +
            "] [count:" + String.valueOf(counter.num()) + "]";
        }
    }

    class Counter
    {
        private int count = 0;
        private int id = 0;

        Counter(int id)
        {
            this.id = id;
        }

        public void add()
        {
            count++;
        }

        public int num()
        {
            return count;
        }

        public int getId()
        {
            return id;
        }
    }
}
