package com.carel.supervisor.director.ide;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.xml.XMLNode;


public class ExportMgr extends InitializableBase implements IInitializable
{
    private static ExportMgr me = new ExportMgr();
    private static final String NAME = "name";
    private static final String CLASS = "class";
    private Map map = new HashMap();

    private ExportMgr()
    {
    }

    public static ExportMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        String name = null;
        String className = null;
        XMLNode xml = null;
        IExporter exporter = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xml = xmlStatic.getNode(i);
            name = xml.getAttribute(NAME);
            className = xml.getAttribute(CLASS);

            try
            {
                exporter = (IExporter) FactoryObject.newInstance(className,
                        new Class[] { XMLNode.class }, new Object[] { xml });
                map.put(name, exporter);
            }
            catch (Exception e)
            {
                //LDAC TO DO	
                throw new InvalidConfigurationException("");
            }
        }
    }

    public IExporter getExporter(String name)
    {
        return (IExporter) map.get(name);
    }

    /*public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        XmlStream a = ExportMgr.getInstance().getExporter("site").exporter("IT_it");

        /*PrintWriter printerWriter = new PrintWriter(new BufferedWriter(
                new FileWriter("c:\\site.xml", false)));

        printerWriter.write(a.getXML("site").toString());
        printerWriter.flush();
        printerWriter.close();
        printerWriter = new PrintWriter(new BufferedWriter(new FileWriter("c:\\desc.xml", false)));
        printerWriter.write(a.getXML("desc").toString());
        printerWriter.close();*/

        //LDAC TO DO : sistemare l'encoding
        /*ZipperFile.zip("c:\\", "Site", "xml",
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            a.getXML("site").toString());
        ZipperFile.zip("c:\\", "SiteDictionary", "xml",
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            a.getXML("desc").toString());
        /*a = ExportMgr.getInstance().getExporter("rule").exporter("IT_it");
        ZipperFile.zip("c:\\", "Rule", "xml",
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                a.getXML("ruler").toString());*/
  /*  }*/
}
