package com.carel.supervisor.base.dump;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.dump.xml.DumperXMLFactory;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.io.FileUtil;
import com.carel.supervisor.base.xml.XMLNode;


public class DumperMgr extends InitializableBase
{
    private static final String CLASS = "class";
    private static final String TYPE = "type";
    private static final String ELEMENT = "element";
    private static final String REGISTRY = "registry";
    private static final String CONFIG = "config";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static IDumperFactory dumperFactory = new DumperXMLFactory();
    private static DumperMgr meDumperMgr = new DumperMgr();
    private Map registry = new HashMap();
    private boolean initialized = false;
    private Properties dumperFileList = null;

    private DumperMgr()
    {
        super();
    }

    public static DumperMgr getInstance()
    {
        return meDumperMgr;
    }

    public static DumpWriter createDumpWriter(String name, Object value)
    {
        return dumperFactory.createDumpWriter(name, value.getClass());
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            try
            {
                for (int j = 0; j < xmlStatic.size(); j++)
                {
                    XMLNode xmlStaticTmp = xmlStatic.getNode(j);

                    if (xmlStaticTmp.getNodeName().equals(REGISTRY))
                    {
                        XMLNode xmlTmp = null;
                        String type = null;
                        String className = null;

                        for (int i = 0; i < xmlStaticTmp.size(); i++)
                        {
                            xmlTmp = xmlStaticTmp.getNode(i);
                            checkNode(xmlTmp, ELEMENT, "BSSE0001");
                            type = retrieveAttribute(xmlTmp, TYPE, "BSSE0002");
                            className = retrieveAttribute(xmlTmp, CLASS,
                                    "BSSE0002");

                            try
                            {
                                registry.put(type,
                                    (IDumperObject) FactoryObject.newInstance(
                                        className));
                            }
                            catch (Exception e)
                            {
                                FatalHandler.manage(this,
                                    CoreMessages.format("BSSE0003", className),
                                    e);
                            }
                        }
                    }
                    else if (xmlStaticTmp.getNodeName().equals(CONFIG))
                    {
                        dumperFileList = retrieveProperties(xmlStaticTmp, NAME,
                                VALUE, "BSSE0002");
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

    public IDumperObject getDumper(String classManaged)
    {
        IDumperObject o = (IDumperObject) registry.get(classManaged);

        if (null == o)
        {
            return getDefaultDumper();
        }
        else
        {
            return o;
        }
    }

    public IDumperObject getDefaultDumper()
    {
        return (IDumperObject) registry.get(java.lang.Object.class.getName());
    }

    public void dump(Class dumperId, DumpWriter DumpWriter, boolean append)
        throws Exception
    {
        String fileName = dumperFileList.getProperty(dumperId.getName());
        FileUtil file = new FileUtil(fileName, append);
        file.appendFile(DumpWriter.getStringBuffer().toString());
        file.close();
    }
}
