/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.Hashtable;
import java.util.zip.CRC32;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.session.UserSession;

/**
 * @author team pvpro 20090326
 *
 */
public class PrintXML
{
	/*
    public void printXMLStr(UserSession session, XMLNode node, String model) throws KeyNotMountedException, Exception
    {

                IGenericWriter writer = GenericWriterFactory.getWriter(session);
                writer.openFile(BaseConfig.getExportModels(session), model+".xml", false);
                initialize(writer);
                CRC32 crc= new CRC32();
                crc=printCRCFile(crc, node.getNode(0));            
                //System.out.println("CRC da java :" +crc.getValue());
                printPreOrder(writer, node,crc);
                writer.close();
        

    }
    private void initialize(IGenericWriter writer) throws Exception
    {

            writer.appendFile("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.appendFile("\r\n");


    }
    private void printPreOrder(IGenericWriter writer, XMLNode node,CRC32 crc) throws Exception
    {
        Hashtable codif =new Hashtable();
        codif.put("IDVARMDL", "idVarMdl");
        codif.put("ADDRESSIN","addressIn");
        codif.put("ADDRESSOUT","addressOut");
        codif.put("DOCVERSION", "docVersion");
        codif.put("DOCDATE", "docDate");
        codif.put("CODE", "code");
        codif.put("SWVERSION", "swVersion");
        codif.put("LITTLENDIAN", "littleEndian");
        codif.put("VALUE", "value");
        codif.put("MANUFACTURER", "manufacturer");
        codif.put("DESCRIPTIONKEY", "descriptionKey");
        codif.put("TYPE", "type");
        codif.put("SIGNED", "signed");
        codif.put("DECIMAL", "decimal");
        codif.put("MINVALUE", "minValue");
        codif.put("MAXVALUE", "maxValue");
        codif.put("MEASUREUNIT", "measureUnit");
        codif.put("IDDEVFATHER", "idDevFather");
        codif.put("PRIORITY", "priority");
        codif.put("READWRITE", "readWrite");
        codif.put("FREQUENCY", "frequency");
        codif.put("GRAPH", "graph");
        codif.put("PRINT", "print");
        codif.put("PMAIN", "pMain");        
        codif.put("PDESC", "pDesc");
        codif.put("PHISTOR", "pHistor");
        codif.put("PSET", "pSet");
        codif.put("PLIST", "pList");
        codif.put("VALUEEND","valueEnd");
        codif.put("VALUESTART", "valueStart");
        codif.put("NAME", "name");
        codif.put("VARDIMENSION", "varDimension");
        codif.put("VARLENGTH", "varLength");
        codif.put("BITPOSITION", "bitPosition");
        codif.put("LANGDEFAULT", "langDefault");
        codif.put("KEY", "key");
        codif.put("SHORTDESCR", "shortDescr");
        codif.put("VERSION", "version");
        codif.put("FUNCTYPEREAD", "funcTypeRead");
        codif.put("FUNCTYPEWRITE", "funcTypeWrite");
        codif.put("IDE", "ide");

            if (node.hasAttribute())
            {
                writer.appendFile("<"+ node.getNodeName() +" ");
                
                if (node.getNodeName().toString().equalsIgnoreCase("Check"))
                {
                    String Hres= Long.toHexString(crc.getValue());
                    Hres="00000000"+Hres;
                    Hres=Hres.substring(Hres.length()-8).toUpperCase();
                    node.setAttrib("code", Hres);

                }//fine aggiunta
                
                
                
                for (int i=1;i<=node.sizeAttribute();i++)
                {
                    writer.appendFile(codif.get(node.getKey(i))+"=");
                    writer.appendFile("\""+node.getAttribute(node.getKey(i))+"\" ");
                }
                if (node.hasChildren())
                {
                    writer.appendFile(">");
                    writer.appendFile("\r\n");
                    for (int i=0;i<node.size();i++)
                    {
                        printPreOrder(writer,node.getNode(i),crc);
                    }
                    writer.appendFile("</"+node.getNodeName()+">");
                    writer.appendFile("\r\n");
                    return;
                }
                else
                {
                    writer.appendFile("/>");
                    writer.appendFile("\r\n");
                    return;
                        
                }
                
                
            }
            else
                if (node.hasChildren())
                {
                     writer.appendFile("<"+ node.getNodeName() +">");
                    writer.appendFile("\r\n");
                    for (int i=0;i<node.size();i++)
                    {
                        printPreOrder(writer,node.getNode(i),crc);
                    }
                    writer.appendFile("</"+node.getNodeName()+">");
                    writer.appendFile("\r\n");
                    return;
                }
                else
                {
                    writer.appendFile("<"+ node.getNodeName() +">");
                    writer.appendFile("\r\n");
                    writer.appendFile("</"+node.getNodeName()+">");
                    writer.appendFile("\r\n");
                    return;
                }


        
    }

    private CRC32 printCRCFile(CRC32 crc, XMLNode node) throws Exception
    {
        Hashtable codif =new Hashtable();
        codif.put("IDVARMDL", "idVarMdl");
        codif.put("ADDRESSIN","addressIn");
        codif.put("ADDRESSOUT","addressOut");
        codif.put("DOCVERSION", "docVersion");
        codif.put("DOCDATE", "docDate");
        codif.put("CODE", "code");
        codif.put("SWVERSION", "swVersion");
        codif.put("LITTLENDIAN", "littleEndian");
        codif.put("VALUE", "value");
        codif.put("MANUFACTURER", "manufacturer");
        codif.put("DESCRIPTIONKEY", "descriptionKey");
        codif.put("TYPE", "type");
        codif.put("SIGNED", "signed");
        codif.put("DECIMAL", "decimal");
        codif.put("MINVALUE", "minValue");
        codif.put("MAXVALUE", "maxValue");
        codif.put("MEASUREUNIT", "measureUnit");
        codif.put("IDDEVFATHER", "idDevFather");
        codif.put("PRIORITY", "priority");
        codif.put("READWRITE", "readWrite");
        codif.put("FREQUENCY", "frequency");
        codif.put("GRAPH", "graph");
        codif.put("PRINT", "print");
        codif.put("PMAIN", "pMain");        
        codif.put("PDESC", "pDesc");
        codif.put("PHISTOR", "pHistor");
        codif.put("PSET", "pSet");
        codif.put("PLIST", "pList");
        codif.put("VALUEEND","valueEnd");
        codif.put("VALUESTART", "valueStart");
        codif.put("NAME", "name");
        codif.put("VARDIMENSION", "varDimension");
        codif.put("VARLENGTH", "varLength");
        codif.put("BITPOSITION", "bitPosition");
        codif.put("LANGDEFAULT", "langDefault");
        codif.put("KEY", "key");
        codif.put("SHORTDESCR", "shortDescr");
        codif.put("VERSION", "version");
        codif.put("FUNCTYPEREAD", "funcTypeRead");
        codif.put("FUNCTYPEWRITE", "funcTypeWrite");
        codif.put("IDE", "ide");

            
            if (node.hasAttribute())
            {
                crc.update(("<"+ node.getNodeName() +" ").getBytes("UTF8"));
                for (int i=1;i<=node.sizeAttribute();i++)
                {
                    
                    crc.update((codif.get(node.getKey(i))+"=").getBytes("UTF8"));
                    crc.update(("\""+node.getAttribute(node.getKey(i))+"\" ").getBytes("UTF8"));

                    
                }
                if (node.hasChildren())
                {
                    crc.update((">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    for (int i=0;i<node.size();i++)
                    {
                        crc=printCRCFile(crc,  node.getNode(i));
                    }
                    crc.update(("</"+node.getNodeName()+">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    return crc;
                }
                else
                {
                    crc.update(("/>").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    return crc;
                        
                }
                
                
            }
            else
                if (node.hasChildren())
                {
                    crc.update(("<"+ node.getNodeName() +">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    for (int i=0;i<node.size();i++)
                    {
                        crc=printCRCFile(crc,node.getNode(i));
                    }
                    crc.update(("</"+node.getNodeName()+">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    return crc;
                }
                else
                {
                    crc.update(("<"+ node.getNodeName() +">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    crc.update(("</"+node.getNodeName()+">").getBytes("UTF8"));
                    crc.update(("\r\n").getBytes("UTF8"));
                    return crc;
                }
        
    }
    */
}
