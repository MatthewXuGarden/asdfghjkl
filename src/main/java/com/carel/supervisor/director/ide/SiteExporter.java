package com.carel.supervisor.director.ide;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.io.BufferFileWriter;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class SiteExporter implements IExporter
{
    private static final String COLUMN = "column";
    private static final String XML = "xml";
    private static final String SITE = "Site";
    private static final String DEVICE = "d";
    private static final String VARIABLE = "v";
    private static final String RELAY = "r";
    private static final String KEY = "key";
    private static final String DESCR = "d";
    private static final String DICTIONARY = "dictionary";
    private static final String CODE = "code";
    private static final String CODE_EXP = "c";
    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String ITEM = "item";
    private static final String SECTION = "section";
    private static final String DEFAULT = "defaultOnNotFound";
    
    private static final String DEVICE_ADDRESS = "address";
    private static final String DEVICE_ADDRESS_XML = "da";
   
    private static final String LINE_CODE="lc";
    private static final String VARIABLE_ADDRESS = "addressin";
    private static final String VARIABLE_ADDRESS_XML = "vai";
    
	private static final String VARIALE_BIT_POSITION="bitposition";
	private static final String VARIALE_BIT_POSITION_XML = "bp";
	
	private static final String VARIALE_VAR_LENGTH="varlength";
	private static final String VARIALE_VAR_LENGTH_XML="vl";
	
    private String[] columns = null;
    private String[] xml = null;

    public SiteExporter(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        XMLNode xmlNode = null;
        columns = new String[xmlStatic.size()];
        xml = new String[xmlStatic.size()];

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlNode = xmlStatic.getNode(i);
            columns[i] = xmlNode.getAttribute(COLUMN);
            xml[i] = xmlNode.getAttribute(XML);
        }
    }
    
    public XmlStream exporter(String language) throws Exception
    {
    	String path = BaseConfig.getCarelPath();
		String filePath = path + "ide" + File.separator + "export" + File.separator;
    	
    	String nameNodeFile = "site";
        String pathNodeFile = filePath + "Site.xml";
        //PrintWriter xmlNodeFile = new PrintWriter(new BufferedWriter(new FileWriter(pathNodeFile, true)));
        BufferedWriter xmlNodeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathNodeFile),"UTF-8"));
        //BufferedWriter xmlNodeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathNodeFile)));
        //BufferFileWriter xmlNodeFile = new BufferFileWriter(pathNodeFile, true);
    	String xmlNodeStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + SITE + " key=\"1\">";
    	xmlNodeFile.write(xmlNodeStart);
    	String xmlNodeEnd = "</" + SITE + ">";
    	//XMLNode xmlNode = new XMLNode(SITE, "");
    	//xmlNode.setAttribute(KEY, "1");
    	
    	String nameNodeDicFile = "desc";
    	String pathNodeDicFile = filePath + "SiteDictionary.xml";
    	//PrintWriter xmlNodeDicFile = new PrintWriter(new BufferedWriter(new FileWriter(pathNodeDicFile, true)));
    	BufferedWriter xmlNodeDicFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathNodeDicFile),"UTF-8"));
    	//BufferedWriter xmlNodeDicFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathNodeDicFile)));
    	//BufferFileWriter xmlNodeDicFile = new BufferFileWriter(pathNodeDicFile, true);
    	String xmlNodeDicStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + DICTIONARY + " defaultOnNotFound=\"true\">";
    	xmlNodeDicFile.write(xmlNodeDicStart);
    	String xmlNodeDicEnd = "</" + DICTIONARY + ">"; 
    	//XMLNode xmlNodeDic = new XMLNode(DICTIONARY, "");
    	//xmlNodeDic.setAttribute(DEFAULT, "true");

        /*sql = "select cfdevice.iddevice as iddevice, cftableext.description as description, cfdevmdl.code as code " +
        		"from cfdevmdl,cfdevice inner join cftableext on cfdevice.iddevice = cftableext.tableid " +
        		"where cfdevice.iscancelled = ? and cftableext.languagecode=? and " +
        		"cftableext.tablename='cfdevice' and cfdevice.idsite=1 and cftableext.idsite=1 and cfdevice.iddevmdl=cfdevmdl.iddevmdl " +
        		"order by cfdevice.idline, cfdevice.address";

        RecordSet recordSetDev = DatabaseMgr.getInstance().executeQuery(null,
                sql, new Object[] { "FALSE", language });  */
        
        /*
        sql = "select iddevice, description,code, idline, address from " +
        		"(select cfdevice.iddevice as iddevice, cftableext.description as description, cfdevmdl.code as code, " +
        		"cfdevice.idline as idline,cfdevice.address as address from cfdevmdl,cfdevice " +
        		"inner join cftableext on cfdevice.iddevice = cftableext.tableid where cfdevice.iscancelled = ? and " +
        		"cftableext.languagecode=? and cftableext.tablename='cfdevice' and cfdevice.idsite=1 and cftableext.idsite=1 and " +
        		"cfdevice.iddevmdl=cfdevmdl.iddevmdl " +
        		"UNION " +
        		"select cfdevice.iddevice as iddevice, cftableext.description as description, cfdevice.code as code, cfdevice.idline as idline, " +
        		"cfdevice.address as address from cfdevice inner join cftableext on cfdevice.iddevice = cftableext.tableid where " +
        		"cfdevice.iscancelled = ? and cftableext.languagecode=? and cfdevice.islogic='TRUE' and cftableext.tablename='cfdevice' and " +
        		"cfdevice.idsite=1 and cftableext.idsite=1) as foo order by idline, address ";
        
        RecordSet recordSetDev = DatabaseMgr.getInstance().executeQuery(null,
                sql, new Object[] { "FALSE", language,"FALSE", language });
     	*/
        
        /*
         * NUOVO
         */
        String sql = "select d.iddevice as iddevice,t.description as description," +
        	  "CASE WHEN m.code is null THEN ''||d.iddevice  ELSE m.code END as code," +
        	  "CASE WHEN d.idline is null THEN 0  ELSE d.idline END as idline," +
        	  "CASE WHEN d.address is null THEN d.iddevice  ELSE d.address END as address," +
        	  "d.islogic as logic from cfdevice as d left outer join cfdevmdl as m on " +
        	  "d.iddevmdl=m.iddevmdl,cftableext as t where d.iscancelled='FALSE' and t.idsite=1 and " +
        	  "t.tablename='cfdevice' and t.tableid=d.iddevice and t.languagecode=? order by d.idline, d.address ";
        
        RecordSet recordSetDev = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{language});
        // END 
        
        Map relay = extractRelay();
        
        DeviceList deviceList = new DeviceList();
        Device device = null;
        Integer idDev = null;
        Record recordDev = null;
        Record recordVar = null;

        for (int i = 0; i < recordSetDev.size(); i++)
        {
            recordDev = recordSetDev.get(i);
            idDev = (Integer) recordDev.get("iddevice");
            deviceList.addDevice(idDev, new Device(recordDev));
            
            device = deviceList.getDevice(idDev);
            
            StringBuffer buffer = new StringBuffer();
            
            //Visto che i campi che possono essere esportati sono dichiarati nel file 
            //SuperVisorConfig.xml, viene aggiunta questa variabile in modo tale che se è presente
            //type come campo da esportare => esso sarà il primo a essere ordinato nella query
            String orderByType = "";
            
            for (int j = 0; j < columns.length; j++)
            {
                buffer.append("cfvariable.");
                buffer.append(columns[j]);
                buffer.append(" as ");
                buffer.append(columns[j]);
                buffer.append(",");
                
                if (columns[j].equalsIgnoreCase("type"))
                {
                	orderByType = "cfvariable.type,";
                }
            }
            
            buffer.append("cfvariable.addressin as addressin, cfvariable.bitposition  as bitposition, cfvariable.varlength as varlength, ");
            buffer.append("cftableext.description as description");

            /*String sql = "select cfvarmdl.code as code, cfvariable.iddevice as iddevice,cfvariable.idvariable as idvar, " +
                buffer.toString() +
                " from cfvarmdl,cfvariable inner join cftableext on cfvariable.idvariable = cftableext.tableid " +
                "where cfvariable.iscancelled = ? and cftableext.languagecode=? and " +
                "cftableext.tablename='cfvariable' and cfvariable.idsite=1 and cftableext.idsite=1 and cfvariable.idvarmdl=cfvarmdl.idvarmdl "+
                "AND cfvariable.idhsvariable is not null order by " + orderByType + "cftableext.description";*/
            
            sql = "select cfvariable.code as code, cfvariable.iddevice as iddevice,cfvariable.idvariable as idvar, " +
            buffer.toString() +
            " from cfvariable inner join cftableext on cfvariable.idvariable = cftableext.tableid " +
            "where cfvariable.iscancelled = ? and cftableext.languagecode=? and cfvariable.iddevice=? and " +
            "cftableext.tablename='cfvariable' and cfvariable.idsite=1 and cftableext.idsite=1 "+
            "AND cfvariable.idhsvariable is not null order by " + orderByType + "cftableext.description";

            RecordSet recordSetVar = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", language, idDev });

            device = deviceList.getDevice(idDev); 
            
            if (null != device) //altrimenti sono variabili speciali, per esempio variabili di evento
            {
		        XMLNode xmlNodeDev = null;
		        XMLNode xmlNodeDevDic = null;
		        XMLNode xmlNodeVar = null;
		        XMLNode xmlNodeVarDic = null;
		        String devIsLogic = "false";
		        
	        	recordDev = recordSetDev.get(i);
	        	try {
	        		devIsLogic = UtilBean.trim(recordDev.get("logic"));
	        	}
	        	catch(Exception e){
	        		devIsLogic = "false";
	        	}
	        	
	        	Integer idLine = (Integer)recordDev.get("idline");
	        	Integer lineCode = null;
	        	
	        	if ((idLine != null) && (idLine.intValue() != 0))
	        	{
	        		sql = "SELECT code from cfline WHERE idsite=1 AND idline = ?";
	        		RecordSet recordSetLine = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { recordDev.get("idline") });
	        		lineCode = (Integer)recordSetLine.get(0).get(CODE);
	        	}
	        	else
	        		lineCode = idLine;
	        	
	        	idDev = (Integer)recordDev.get("iddevice");
	            
	        	xmlNodeDev = new XMLNode(DEVICE, "");
	            xmlNodeDev.setAttribute(KEY, idDev.toString());
	            xmlNodeDev.setAttribute(CODE_EXP, replace((String)recordDev.get("code")));
	            xmlNodeDev.setAttribute(DESCR, "%" + idDev.toString() + ".0%");
	            xmlNodeDev.setAttribute(DEVICE_ADDRESS_XML,""+recordDev.get(DEVICE_ADDRESS));
	            xmlNodeDev.setAttribute(LINE_CODE,""+lineCode.toString());
	            
	            //xmlNode.addNode(xmlNodeDev);
	            
	            xmlNodeDevDic = new XMLNode(SECTION, "");
	
	            //xmlNodeDevDic.setAttribute(VALUE,
	            //    replace((String) recordDev.get("description")));
	            xmlNodeDevDic.setAttribute(NAME, idDev.toString());
	            
	            //xmlNodeDic.addNode(xmlNodeDevDic);
	            
	            device = (Device) deviceList.getDevice(idDev);
	            //xmlNodeVar = new XMLNode(VARIABLE, "");
	            //xmlNodeVar.setAttribute(DESCR, "%" + idDev.toString() + ".0%");
	            xmlNodeVarDic = new XMLNode(ITEM, "");
	            //xmlNodeDev.addNode(xmlNodeVar);
	            xmlNodeDevDic.addNode(xmlNodeVarDic);
	            xmlNodeVarDic.setAttribute(CODE, "0");
	            xmlNodeVarDic.setAttribute(VALUE, replace((String) recordDev.get("description")));
	
	            int idvar = 0;
	            int adrVar = 0;
	            
	            for (int j = 0; j < recordSetVar.size(); j++)
	            {
	                //recordVar = device.getVariable(j);
	            	recordVar = recordSetVar.get(j);
	            	idvar = ((Integer)recordVar.get("idvar")).intValue();
	                xmlNodeVar = new XMLNode(VARIABLE, "");
	                xmlNodeDev.addNode(xmlNodeVar);
	                xmlNodeVar.setAttribute(CODE_EXP, replace((String)recordVar.get("code")));

	                try 
	                {
	                	adrVar = ((Integer)recordVar.get(VARIABLE_ADDRESS));
	                	if(devIsLogic != null && devIsLogic.equalsIgnoreCase("true"))
	                	{
	                		if(adrVar == 0)
	                			adrVar = idvar;
	                	}
	                }
	                catch(Exception e){}
	                
	                xmlNodeVar.setAttribute(VARIABLE_ADDRESS_XML,""+adrVar);
	                xmlNodeVar.setAttribute(VARIALE_BIT_POSITION_XML,((Integer)recordVar.get(VARIALE_BIT_POSITION)).toString());
	                xmlNodeVar.setAttribute(VARIALE_VAR_LENGTH_XML,((Integer)recordVar.get(VARIALE_VAR_LENGTH)).toString());
	                
	                for (int k = 0; k < columns.length; k++)
	                {
	                    xmlNodeVar.setAttribute(xml[k], String.valueOf(recordVar.get(columns[k])));
	                }
	                
	                if (relay.containsKey(new Integer(idvar)))
	                {
	                	xmlNodeVar.setAttribute(RELAY,"true");
	                }
	                
	                xmlNodeVar.setAttribute(DESCR, "%" + idDev.toString() + "." + idvar + "%");
	                xmlNodeVarDic = new XMLNode(ITEM, "");
	                xmlNodeDevDic.addNode(xmlNodeVarDic);
	                xmlNodeVarDic.setAttribute(CODE, String.valueOf(idvar));
	                xmlNodeVarDic.setAttribute(VALUE, replace((String) recordVar.get("description")));
	            }
	            
	            //xmlNodeFile.write(new String(xmlNodeDev.toString().getBytes("UTF-8"),"UTF-8"));
	            xmlNodeFile.write(xmlNodeDev.toString());
	            //xmlNodeFile.write(xmlNodeDev.getStringBuffer().toString());
	            xmlNodeFile.flush();
	            
	            //xmlNodeDicFile.write(new String(xmlNodeDevDic.toString().getBytes("UTF-8"),"UTF-8"));
	            xmlNodeDicFile.write(xmlNodeDevDic.toString());
	            //xmlNodeDicFile.write(xmlNodeDevDic.getStringBuffer().toString());
	            xmlNodeDicFile.flush();
            }
        }

        XmlStream xmlStream = new XmlStream();
        
        xmlNodeFile.write(xmlNodeEnd);
        xmlNodeFile.flush();
        xmlNodeFile.close();
        
        xmlStream.addFile(nameNodeFile, pathNodeFile);
        //xmlStream.addXML("site", xmlNode);
        
        xmlNodeDicFile.write(xmlNodeDicEnd);
        xmlNodeDicFile.flush();
        xmlNodeDicFile.close();
        
        xmlStream.addFile(nameNodeDicFile, pathNodeDicFile);
        //xmlStream.addXML("desc", xmlNodeDic);

        return xmlStream;
    }

    private String replace(String s)
    {
        String sTmp = Replacer.replace(s, "<", "&lt;");
        sTmp = Replacer.replace(sTmp, "&", "&amp;");
        sTmp = Replacer.replace(sTmp, ">", "&gt;");
        sTmp = Replacer.replace(sTmp, "\"", "&quot;");
        sTmp = Replacer.replace(sTmp, "'", "&apos;");

        return sTmp;
    }

    public void importer(XMLNode xmlNode) throws Exception
    {
    }
    
    private Map extractRelay() throws DataBaseException
    {
    	Map map = new HashMap();
    	String sql = "select idvariable from cfrelay where iscancelled='FALSE'";
    	RecordSet r = DatabaseMgr.getInstance().executeQuery(null,sql);
    	for(int i = 0; i < r.size(); i++)
    	{
    		map.put((Integer)r.get(i).get(0),"");
    	}
    	return map;
    }
}
