package com.carel.supervisor.ide;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class DevMdlImport
{
    private static final String CODE = "Code";

    //private static final String PROTOCOL = "Protocol";
    private static final String HWPLATFORM = "HWPlatform";
    private static final String SWVERSION = "SWVersion";
    private static final String IMAGE = "Image";
    private static final String MANUFACTURER = "Manifacturer";
    private static final String DESCRIPTION = "Description";
    private static final String LITTLEENDIAN = "LittleEndian";
    private static final String TYPE = "Type";
    private static final String ANALOGIC = "Analogic";
    private static final String INTEGER = "Integer";
    private static final String DIGITAL = "Digital";
    private static final String ALARM = "Alarm";
    //private static final String ADDRESS = "Address";
    //private static final String BITPOSITION = "BitPosition";
    private String deviceImage = null;
    private List<String> buttonImages = new ArrayList<String>();

    private Integer idDevMdl = null;
    
    public DevMdlImport()
    {
    }

    public int importStructure(XMLNode xmlNode) throws Exception
    {
        String code = xmlNode.getAttribute(CODE);
        
        //epurazione "code" da chars non ammessi da win in nomi di files e dirs:
        code = Replacer.replace(code, "/", "_");
        code = Replacer.replace(code, ">", "_");
        code = Replacer.replace(code, "<", "_");
        code = Replacer.replace(code, "*", "_");
        
        if(xmlNode != null)
        {
        	try {
        		deviceImage = xmlNode.getAttribute(IMAGE);
        	}catch(Exception e){}
        }
        
        String sql = "select count(1) from cfdevmdl where idsite=1 and code=?";
        RecordSet r = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{code});

        if (0 < ((Integer) r.get(0).get(0)).intValue())
        {
            return 1; //se c'è già non devo fare insert, ma update
        }

        //INSERT INTO CFDEVMDL
        Object[] device = new Object[9];
        idDevMdl = SeqMgr.getInstance().next(null, "cfdevmdl", "iddevmdl");
        device[0] = idDevMdl;
        device[1] = new Integer(1);
        device[2] = code;
        device[3] = xmlNode.getAttribute(MANUFACTURER);
        device[4] = xmlNode.getAttribute(HWPLATFORM);
        device[5] = xmlNode.getAttribute(SWVERSION);
        device[6] = xmlNode.getAttribute(IMAGE);
        deviceImage = (String) device[6];
        device[7] = xmlNode.getAttribute(LITTLEENDIAN, "True").toUpperCase();
        device[8] = "TRUE";
        sql = "insert into cfdevmdl values (?,?,?,?,?,?,?,?,?)";

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, device);
        }
        catch (DataBaseException e)
        {
            throw e;
        }

        //INSERT INTO CFTABLEEXT
        Object[] params = new Object[8];
        sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";

        LangUsedBeanList langUsedBeanList = new LangUsedBeanList();
        LangUsedBean[] langUsedBean = langUsedBeanList.retrieveAllLanguage(1);

        for (int i = 0; i < langUsedBean.length; i++)
        {
            String lang = langUsedBean[i].getLangcode();
            params[0] = new Integer(1);
            params[1] = lang;
            params[2] = "cfdevmdl";
            params[3] = idDevMdl;
            
            String descr = xmlNode.getAttribute(DESCRIPTION);
            descr = Replacer.replace(descr, ";", "_");
            descr = Replacer.replace(descr, "\"", "_");
            descr = Replacer.replace(descr, "'", "_");
            descr = Replacer.replace(descr, "<", "_");
            
            params[4] = descr;
            params[5] = null;
            params[6] = null;
            params[7] = new Timestamp(System.currentTimeMillis());

            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
            }
            catch (DataBaseException e)
            {
                //Rollback delle insert nella cftableext e cfdevmdl
                sql = "delete from cftableext where idsite=1 and tablename=? and tableid=?";
                DatabaseMgr.getInstance().executeStatement(null, sql,
                    new Object[] { "cfdevmdl", idDevMdl });
                
                sql = "delete from cfdevmdl where iddevmdl=?";
                DatabaseMgr.getInstance().executeStatement(null, sql,
                    new Object[] { idDevMdl });
                
                throw e;
            }
        }

        //INSERT INTO CFVARMDL
        Map<Integer,String> unit = new HashMap<Integer,String>();
        sql = "select idunitmeasurement, description from unitmeasurement";

        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        Record record = null;
        Integer id = null;
        String desc = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            id = (Integer) record.get(0);
            desc = (String) record.get(1);
            unit.put(id, desc);
        }

        VarMdlImport varMdlImport = new VarMdlImport();
        String type = null;
        Integer typeInt = null;
        XMLNode xmlNodeTmp = null;
        XMLNode xmlNodeTmp2 = null;
        Map<String,String> min_rel = new HashMap<String,String>();
        Map<String,String> max_rel = new HashMap<String,String>();

        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTmp = xmlNode.getNode(i);
            type = xmlNodeTmp.getAttribute(TYPE);
            typeInt = getTypeInt(type);

            for (int j = 0; j < xmlNodeTmp.size(); j++)
            {
                try
                {
                    xmlNodeTmp2 = xmlNodeTmp.getNode(j);
                    varMdlImport.importStructure(xmlNodeTmp2, idDevMdl,
                        typeInt, langUsedBean, buttonImages, unit);

                    if (xmlNodeTmp2.getAttribute("MinRif") != null)
                    {
                        min_rel.put(xmlNodeTmp2.getAttribute("Code"),
                            xmlNodeTmp2.getAttribute("MinRif"));
                    }

                    if (xmlNodeTmp2.getAttribute("MaxRif") != null)
                    {
                        max_rel.put(xmlNodeTmp2.getAttribute("Code"),
                            xmlNodeTmp2.getAttribute("MaxRif"));
                    }
                }
                catch (Exception e)
                {
                    //Rollback delle insert nella cftableext e cfdevmdl
                    sql = "delete from cftableext where idsite=1 and tablename=? and tableid=?";
                    DatabaseMgr.getInstance().executeStatement(null, sql,
                        new Object[] { "cfdevmdl", idDevMdl });
                    sql = "delete from cfdevmdl where iddevmdl=?";
                    DatabaseMgr.getInstance().executeStatement(null, sql,
                        new Object[] { idDevMdl });
                    
                    try
                    {
                        String sCode = xmlNodeTmp2.getAttribute(CODE);
                        
                        EventMgr.getInstance().log(
                            new Integer(1),"System","Config",EventDictionary.TYPE_ERROR,"S035",new Object[]{sCode});
                    }
                    catch(Exception e1){}
                    
                    throw e;
                }
            }
        }
        // allineamento minimo e massimo relativi
        alignMinMaxRelative(idDevMdl,min_rel,max_rel);
        return 0;
    }

    public String updateStructure(XMLNode xmlNode, String language, int profile)
        throws Exception
    {
        // ##################### PREREQUISITI #####################
        // code device: chiave che distingue dispositivo da updatare
        String code = xmlNode.getAttribute(CODE);
        String return_msg = "";

        // retrieve iddevmdl che corrisponde a quel code. Mi serve per agire sulle istanze di modelli.
        String sql = "select iddevmdl from cfdevmdl where code=?";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { code });
        idDevMdl = (Integer) rs.get(0).get("iddevmdl");

        //retrive iddevice istanziati per quel modello
        sql = "select iddevice from cfdevice where idsite=? and iddevmdl=? and iscancelled=?";

        Object[] param = new Object[3];
        param[0] = new Integer(1);
        param[1] = idDevMdl;
        param[2] = "FALSE";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] iddevices = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            iddevices[i] = ((Integer) rs.get(i).get("iddevice")).intValue();
        }

        LangUsedBeanList langUsedBeanList = new LangUsedBeanList();
        LangUsedBean[] langUsedBean = langUsedBeanList.retrieveAllLanguage(1);

        Map<Integer,String> unit = new HashMap<Integer,String>();
        sql = "select idunitmeasurement, description from unitmeasurement";

        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        Record record = null;
        Integer id = null;
        String desc = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            id = (Integer) record.get(0);
            desc = (String) record.get(1);
            unit.put(id, desc);
        }

        //key delle mappe = code   sono commentate le parti che usavano come code type$address$bitposition
        VarMdlImport varMdlImport = new VarMdlImport();
        Map xml_map = getDeviceCreatorVariable(xmlNode); // mappa variabili da device creator
        Map db_map = getDbVariable(idDevMdl); // mappa variabili ora su database

        //liste operazioni da eseguire
        List<Integer> to_remove = new ArrayList<Integer>();
        List<XMLNode> to_add = new ArrayList<XMLNode>();
        List<XMLNode> to_update = new ArrayList<XMLNode>();

        List<Object> tmp_list_rem = new ArrayList<Object>();

        // ciclo su variabili xml da device creator
        Iterator iter = xml_map.keySet().iterator();
        Object o_key = null;
        XMLNode tmp_node = null;

        while (iter.hasNext())
        {
            o_key = iter.next();
            tmp_node = (XMLNode) xml_map.get(o_key);

            // se sul db è presente la variabile, update
            if (db_map.containsKey(o_key))
            {
                //varUpdate.updateVariable(tmp_node); //update su db
                to_update.add(tmp_node);
                tmp_list_rem.add(o_key); //remove da mappa db
            }
            else	// altrimenti la inserisco
            {
                to_add.add(tmp_node);
            }
            
            if (("".equals(return_msg)) && (Integer.parseInt(tmp_node.getAttribute("Address")) > 100000)) //x indirizzi modbus
            {
            	return_msg = "MODBUS_UPD";
            }
        }

        //pulizia mappa che userò x rimuovere le variabili non + necessarie sul db
        for (int i = 0; i < tmp_list_rem.size(); i++)
        {
            db_map.remove(tmp_list_rem.get(i));
        }

        iter = db_map.keySet().iterator();
        o_key = null;

        Record r = null;
        Integer idvarmdl = null;

        while (iter.hasNext())
        {
            o_key = iter.next();
            r = (Record) db_map.get(o_key);
            idvarmdl = (Integer) r.get("idvarmdl");

            // rimozione da db
            //
            to_remove.add(idvarmdl);
        }

        // ##################### CONTROLLO PRE-UPDATE #####################
        // 1 - controllo se possibile la rimozione
        if (to_remove.size() > 0)
        {
            String remState = VarUpdate.verifyVariablesDependencies(to_remove,
                    language);

            if (!remState.equals("OK"))
            {
                return remState;
            }
        }

        // ##################### INIZIO UPDATE #####################
        /* ##### GESTIONE VARIABILI #######*/
        idvarmdl = null;
        //mappe per gestione massimi e minimi relativi
        Map<String,String> min_rel = new HashMap<String,String>();
        Map<String,String> max_rel = new HashMap<String,String>();
        
        try
        {
            for (int i = 0; i < to_remove.size(); i++)
            {
                idvarmdl = (Integer) to_remove.get(i);
                VarUpdate.removeVariable(idvarmdl, language);
            }
        }
        catch(Exception e) 
        {
            throw e;
        }
        
        int type_int = -1;

        try
        {
            for (int i = 0; i < to_add.size(); i++)
            {
                tmp_node = (XMLNode) to_add.get(i);
                type_int = getTypeInt(tmp_node.getAttribute("type").toString());
                varMdlImport.addVarMdlAndVariables(tmp_node, idDevMdl, type_int,
                    langUsedBean, buttonImages, unit, profile);
                if (tmp_node.getAttribute("MinRif") != null)
                {
                    min_rel.put(tmp_node.getAttribute("Code"),
                    		tmp_node.getAttribute("MinRif"));
                }
    
                if (tmp_node.getAttribute("MaxRif") != null)
                {
                    max_rel.put(tmp_node.getAttribute("Code"),
                    		tmp_node.getAttribute("MaxRif"));
                }
            }
        }
        catch(Exception e)
        {
            try {
                String sCode = tmp_node.getAttribute(CODE);
                EventMgr.getInstance().log(
                new Integer(1),"System","Config",EventDictionary.TYPE_ERROR,"S035",new Object[]{sCode});
            }
            catch(Exception e1) {}
            
            throw e;
        }
        
        try
        {
            for (int i = 0; i < to_update.size(); i++)
            {
                tmp_node = (XMLNode) to_update.get(i);
                type_int = getTypeInt(tmp_node.getAttribute("type").toString());
                varMdlImport.updateVariable(tmp_node, idDevMdl, buttonImages, unit,
                    type_int);
                if (tmp_node.getAttribute("MinRif") != null)
                {
                    min_rel.put(tmp_node.getAttribute("Code"),
                    		tmp_node.getAttribute("MinRif"));
                }
    
                if (tmp_node.getAttribute("MaxRif") != null)
                {
                    max_rel.put(tmp_node.getAttribute("Code"),
                    		tmp_node.getAttribute("MaxRif"));
                }
            }
        }
        catch(Exception e)
        {
            try {
                String sCode = tmp_node.getAttribute(CODE);
                EventMgr.getInstance().log(
                new Integer(1),"System","Config",EventDictionary.TYPE_ERROR,"S035",new Object[]{sCode});
            }
            catch(Exception e1) {}
            
            throw e;
        }
        
        if ((min_rel.size() > 0) || (max_rel.size() > 0))
        {
	        // allineamento minimo e massimo relativi x modelli
	        alignMinMaxRelative(idDevMdl,min_rel,max_rel);
	        // allineamento minimo e massimo relativo per le istanze
	        alignMinMaxRelativeInstance(iddevices,min_rel,max_rel);
        }
        
        // ##### UPDATE CFDEVMDL  #######
        Object[] params = new Object[7];
        params[0] = xmlNode.getAttribute(MANUFACTURER);
        params[1] = xmlNode.getAttribute(HWPLATFORM);
        params[2] = xmlNode.getAttribute(SWVERSION);
        params[3] = xmlNode.getAttribute(IMAGE);
        params[4] = xmlNode.getAttribute(LITTLEENDIAN, "True").toUpperCase();
        params[5] = "TRUE";
        params[6] = code;

        sql = "update cfdevmdl set manufacturer=?, hdversion=?, swversion=?, imagepath=?,littlendian=?,ide=? where code=?";

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
        catch (DataBaseException e)
        {
            //E' esploso, significa che non è riuscito a fare l'update
            throw e;
        }

        // ##### UPDATE CFTABLEEXT -> TABLE:CFDEVMDL  #######
        params = new Object[5];
        sql = "update cftableext set description=? where idsite=? and languagecode=? and tablename=? and tableid=?";

        for (int i = 0; i < langUsedBean.length; i++)
        {
            String lang = langUsedBean[i].getLangcode();
            params[0] = xmlNode.getAttribute(DESCRIPTION);
            params[1] = new Integer(1);
            params[2] = lang;
            params[3] = "cfdevmdl";
            params[4] = idDevMdl;

            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
            }
            catch (DataBaseException e)
            {
                throw e;
            }
        }

        // ##### UPDATE CFDEVICE  #######
        // 04/03/2008 Aggiunto update dell'immagine del dispositivo
        sql = "update cfdevice set littlendian=?,imagepath=? where idsite=? and iddevmdl=?";
        params = new Object[4];
        params[0] = xmlNode.getAttribute(LITTLEENDIAN, "TRUE").toUpperCase();
        params[1] = xmlNode.getAttribute(IMAGE);
        params[2] = new Integer(1);
        params[3] = idDevMdl;
        DatabaseMgr.getInstance().executeStatement(null, sql, params);

        if ("".equals(return_msg))
        {
        	return_msg = "OK";
        }
        
        return return_msg;
    }

    public String[] getButtonImages()
    {
        if (0 < buttonImages.size())
        {
            String[] tmp = new String[buttonImages.size()];
            tmp = (String[]) buttonImages.toArray(tmp);

            return tmp;
        }

        return null;
    }

    public String getDeviceImage()
    {
        return deviceImage;
    }

    private Map getDeviceCreatorVariable(XMLNode xmlNode)
    {
        Map<String,XMLNode> xml_map = new HashMap<String,XMLNode>();
        XMLNode xmlVarType = null;
        XMLNode xmlNodeTmp = null;
        String key = null;
        String type = null;
        
        for (int i = 0; i < xmlNode.size(); i++) //ciclo sui tipi di variabili
        {
            xmlVarType = xmlNode.getNode(i);
            type = xmlVarType.getAttribute(TYPE).toString();

            //typeInt = getTypeInt(type);
            for (int j = 0; j < xmlVarType.size(); j++) //ciclo sulle variabili di ogni tipo
            {
                xmlNodeTmp = xmlVarType.getNode(j);
                key = xmlNodeTmp.getAttribute(CODE);

                /*
                typeInt + "$" + xmlNodeTmp.getAttribute(ADDRESS) + "$" +
                xmlNodeTmp.getAttribute(BITPOSITION);*/
                xmlNodeTmp.setAttribute("type", type);
                xml_map.put(key, xmlNodeTmp);
            }
        }

        return xml_map;
    }

    private Map getDbVariable(int iddevmdl) throws DataBaseException
    {
        //String sql = "select idvarmdl,type,addressin,bitposition from cfvarmdl where iddevmdl = ?";
        String sql = "select idvarmdl,code from cfvarmdl where iddevmdl = ?";
        String key = null;
        Map<String,Record> db_map = new HashMap<String,Record>();
        RecordSet rs = null;

        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(iddevmdl) });

        Record r = null;

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                r = rs.get(i);
                key = r.get("code").toString();

                /*r.get("type") + "$" + r.get("addressin") + "$" +
                r.get("bitposition");*/
                db_map.put(key, r);
            }
        }

        return db_map;
    }

    public static int getTypeInt(String type)
    {
        int typeInt = -1;

        if (type.equalsIgnoreCase(DIGITAL))
        {
            typeInt = new Integer(1);
        }
        else if (type.equalsIgnoreCase(ANALOGIC))
        {
            typeInt = new Integer(2);
        }
        else if (type.equalsIgnoreCase(INTEGER))
        {
            typeInt = new Integer(3);
        }
        else if (type.equalsIgnoreCase(ALARM))
        {
            typeInt = new Integer(4);
        }

        return typeInt;
    }

    private void alignMinMaxRelative(Integer iddevmdl, Map min_map, Map max_map)
        throws DataBaseException
    {
        Iterator iter = min_map.keySet().iterator();
        String key = "";
        String code_min = "";
        String code_max = "";
        while (iter.hasNext())
        {
            key = iter.next().toString();
            code_min = (String) min_map.get(key);
            VarMdlImport.alignRif(key, code_min, iddevmdl, true);
        }

        iter = max_map.keySet().iterator();

        while (iter.hasNext())
        {
        	key = iter.next().toString();
        	code_max = (String) max_map.get(key);
        	VarMdlImport.alignRif(key, code_max, iddevmdl, false);
        }
    }
    
    private void alignMinMaxRelativeInstance(int[] iddevices, Map min_map, Map max_map)
    throws DataBaseException
{
    Iterator iter = min_map.keySet().iterator();
    String key = "";
    String code_min = "";
    String code_max = "";
    while (iter.hasNext())
    {
        key = iter.next().toString();
        code_min = (String) min_map.get(key);
        VarMdlImport.alignRifInstance(key, code_min, iddevices, true);
    }

    iter = max_map.keySet().iterator();

    while (iter.hasNext())
    {
    	key = iter.next().toString();
    	code_max = (String) max_map.get(key);
    	VarMdlImport.alignRifInstance(key, code_max, iddevices, false);
    }
}

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        File file = new File("c:\\provadevice.xml");
        XMLNode xml = XMLNode.parse(file.toURL().openStream());
        DevMdlImport d = new DevMdlImport();
        d.importStructure(xml);
    }

	/**
	 * @return Returns the idDevmdl.
	 */
	public Integer getIdDevmdl() 
	{
		return idDevMdl;
	}
    
    
}
