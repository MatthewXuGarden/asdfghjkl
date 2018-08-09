package com.carel.supervisor.ide;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;
import com.carel.supervisor.presentation.bo.BDevDetail;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;


public class VarMdlImport
{
    private static final String CODE = "Code";
    private static final String ADDRESSIN = "Address";
    private static final String ADDRESSOUT = "AddressWrite";
    private static final String VARDIMENSION = "Dimension";
    private static final String VARLENGTH = "Length";
    private static final String BITPOSITION = "BitPosition";
    private static final String SIGNED = "Signed";
    private static final String DECIMAL = "Decimals";
    private static final String MEASUREUNIT = "Unit";
    private static final String PRIORITY = "Priority";
    private static final String ACCESSLEVEL = "AccessLevel";
    private static final String ISACTIVE = "IsActive";
    private static final String CATEGORY = "Category";
    private static final String HACCP = "HACCP";
    private static final String MIN = "Min";
    private static final String MAX = "Max";
    private static final String TODISPLAY = "ToDisplay";
    private static final String RELAY = "Relay";
    private static final String BUTTON = "Button";
    private static final String IMAGEON = "ImageOn";
    private static final String IMAGEOFF = "ImageOff";
    private static final String HISTORY = "History";
    private static final String HISTORY_DEPTH = "HistoryDepth";
    private static final String HISTORY_FREQUENCY = "HistoryFrequency";
    private static final String HISTORY_VARIATION = "HistoryVariation";
    private static final String DESCRIPTION = "Description";
    private static final String LONG_DESCRIPTION = "LongDescr";
    private static final String SHORT_DESCRIPTION = "ShortDescr";
    //private static final String TYPE = "type";
    private Map<Integer,Integer> readWrite = new HashMap<Integer,Integer>();

    public VarMdlImport()
    {
        readWrite.put(new Integer(1), new Integer(1));
        readWrite.put(new Integer(2), new Integer(2));
        readWrite.put(new Integer(3), new Integer(3));
        readWrite.put(new Integer(7), new Integer(7));
        readWrite.put(new Integer(11), new Integer(11));
    }

    public Integer importStructure(XMLNode xmlNode, Integer idDevMdl,
        Integer type, LangUsedBean[] langUsedBean, List<String> buttonImages, Map unit)
        throws Exception
    {
        boolean isHACCP = xmlNode.getAttribute(HACCP, "False").toUpperCase()
                                 .equals("TRUE");
        Integer idVarMdl = SeqMgr.getInstance().next(null, "cfvarmdl", "idvarmdl");

        Object[] values = new Object[33];
        values[0] = idVarMdl;
        values[1] = new Integer(1);
        values[2] = idDevMdl;
        values[3] = xmlNode.getAttribute(CODE);
        values[4] = type;
        values[5] = Integer.valueOf(xmlNode.getAttribute(ADDRESSIN));
        values[6] = Integer.valueOf(xmlNode.getAttribute(ADDRESSOUT));
        values[7] = Integer.valueOf(xmlNode.getAttribute(VARDIMENSION));
        values[8] = Integer.valueOf(xmlNode.getAttribute(VARLENGTH));
        values[9] = Integer.valueOf(xmlNode.getAttribute(BITPOSITION));
        values[10] = xmlNode.getAttribute(SIGNED, "TRUE").toUpperCase();
        values[11] = Integer.valueOf(xmlNode.getAttribute(DECIMAL, 0));
        values[12] = xmlNode.getAttribute(TODISPLAY, "NONE").toUpperCase();

        String tmp = xmlNode.getAttribute(BUTTON);
        boolean buttonPresent = false;

        if ((null != tmp) && (tmp.equalsIgnoreCase("TRUE")))
        {
            buttonPresent = true;
            values[13] = "images/button/setvar.png";

            //buttonImages.add(xmlNode.getAttribute(IMAGEON));
        }

        values[14] = Integer.valueOf(xmlNode.getAttribute(PRIORITY, 1));

        Integer access = Integer.valueOf(xmlNode.getAttribute(ACCESSLEVEL, 1));
        values[15] = (Integer) readWrite.get(access);
        values[16] = xmlNode.getAttribute(MIN);
        values[17] = xmlNode.getAttribute(MAX);
        values[18] = null;

        Integer unitMeasure = Integer.valueOf(xmlNode.getAttribute(
                    MEASUREUNIT, 0));

        values[19] = unit.get(unitMeasure);
        values[20] = Integer.valueOf(xmlNode.getAttribute(CATEGORY, "1"));

        if (buttonPresent)
        {
            values[21] = xmlNode.getAttribute(IMAGEON);

            if (null != values[21])
            {
                buttonImages.add(values[21].toString());
                values[21] = "images/button/" + values[21];
            }

            values[22] = xmlNode.getAttribute(IMAGEOFF);

            if (null != values[22])
            {
                buttonImages.add(values[22].toString());
                values[22] = "images/button/" + values[22]; 
            }
        }

        if (type.intValue() == VariableInfo.TYPE_ALARM)
        {
            values[23] = new Integer(BDevDetail.ALARM_FREQ);
            values[24] = new Integer(0);
        } //if
        else
        {
            values[23] = (isHACCP ? new Integer(BDevDetail.HACCP_FREQ) : new Integer(0)); //Se è HACCP allora 900
            values[24] = (isHACCP ? new Integer(0) : new Integer(-1));
        } //else

        values[25] = new Integer(0);
        values[26] = xmlNode.getAttribute(HACCP, "False").toUpperCase();
        values[27] = xmlNode.getAttribute(ISACTIVE, "True").toUpperCase();
        values[28] = xmlNode.getAttribute(RELAY, "False").toUpperCase();
        values[29] = null;

        if ("TRUE".equalsIgnoreCase(xmlNode.getAttribute(HISTORY, "False").toUpperCase()))
        {
            values[30] = new Integer(xmlNode.getAttribute(HISTORY_DEPTH,
                        "15552000")); //profondità storico
            values[31] = new Integer(xmlNode.getAttribute(HISTORY_FREQUENCY,
                        "300")); //frequenza storico
            values[32] = new Double(xmlNode.getAttribute(HISTORY_VARIATION, "1")); //delta storico    	
        }
        else
        {
            values[30] = null; //profondità storico
            values[31] = null; //frequenza storico
            values[32] = null; //delta storico     
        }

        String sql = "insert into cfvarmdl values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //remove
        System.out.println(java.util.Arrays.deepToString(values));
        //remove
        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, values);
        }
        catch (Exception e)
        {
            //    		Rollback delle insert nella cfvarmdl
            sql = "delete from cfvarmdl where idsite=1 and iddevmdl=?";
            DatabaseMgr.getInstance().executeStatement(null, sql,
                new Object[] { idDevMdl });
            throw e;
        }

        String description = xmlNode.getAttribute(DESCRIPTION);
        description = Replacer.replace(description, ";", "_");
        description = Replacer.replace(description, "\"", "_");
        description = Replacer.replace(description, "'", "_");
        description = Replacer.replace(description, "<", "_");
        
        Object[] params = new Object[8];
        sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";

        for (int i = 0; i < langUsedBean.length; i++)
        {
            String lang = langUsedBean[i].getLangcode();
            params[0] = new Integer(1);
            params[1] = lang;
            params[2] = "cfvarmdl";
            params[3] = idVarMdl;
            params[4] = description;
            params[5] = xmlNode.getAttribute(SHORT_DESCRIPTION);
            params[6] = xmlNode.getAttribute(LONG_DESCRIPTION);
            params[7] = new Timestamp(System.currentTimeMillis());

            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
            }
            catch (Exception e)
            {
                // Rollback delle insert nella cfvarmdl
                sql = "delete from cfvarmdl where idsite=1 and iddevmdl=?";
                DatabaseMgr.getInstance().executeStatement(null, sql,
                    new Object[] { idDevMdl });
                throw e;
            }
        }

        return idVarMdl;
    }

    public void addVarMdlAndVariables(XMLNode xmlNode, Integer idDevMdl,
        Integer type, LangUsedBean[] langUsedBean, List<String> buttonImages, Map unit,
        int profile) throws Exception
    {
        //insert cfvarmdl
        //VarMdlImport varMdlImport = new VarMdlImport();
        Integer idvarmdl = importStructure(xmlNode, idDevMdl, type,
                langUsedBean, buttonImages, unit);
        String sql = "select iddevice from cfdevice where iddevmdl=? and iscancelled=? and idsite=1";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idDevMdl,"FALSE" });
        Integer[] ids_device = null;

        if (rs != null)
        {
            ids_device = new Integer[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids_device[i] = (Integer) rs.get(i).get("iddevice");
            }
        }

        if (ids_device != null)
        {
            // insert cfvariable x n istanze di dispositivi istanziati
            for (int i = 0; i < ids_device.length; i++)
            {
                Object[] var = new Object[38];
                var[0] = SeqMgr.getInstance().next(null, "cfvariable",
                        "idvariable"); //idvar
                var[1] = BaseConfig.getPlantId(); //pvcode
                var[2] = new Integer(1); //idsite
                var[3] = ids_device[i]; //iddevice
                var[4] = "FALSE"; //islogic
                var[5] = idvarmdl; //idvarmdl
                var[6] = null; //functioncode  
                var[7] = xmlNode.getAttribute(CODE); //code
                var[8] = type; //type
                var[9] = Integer.valueOf(xmlNode.getAttribute(ADDRESSIN));
                var[10] = Integer.valueOf(xmlNode.getAttribute(ADDRESSOUT));
                var[11] = Integer.valueOf(xmlNode.getAttribute(VARDIMENSION));
                var[12] = Integer.valueOf(xmlNode.getAttribute(VARLENGTH));
                var[13] = Integer.valueOf(xmlNode.getAttribute(BITPOSITION));
                var[14] = xmlNode.getAttribute(SIGNED, "TRUE").toUpperCase(); //signed
                var[15] = Integer.valueOf(xmlNode.getAttribute(DECIMAL, 0)); //decimal
                var[16] = xmlNode.getAttribute(TODISPLAY, "NONE").toUpperCase(); //todisplay

                String tmp = xmlNode.getAttribute(BUTTON);
                boolean buttonPresent = false;

                if ((null != tmp) && (tmp.equalsIgnoreCase("TRUE")))
                {
                    buttonPresent = true;
                    var[17] = "images/button/setvar.png"; //buttonpath

                    //buttonImages.add(xmlNode.getAttribute(IMAGEON));
                }

                var[18] = Integer.valueOf(xmlNode.getAttribute(PRIORITY, 1)); //priority

                Integer access = Integer.valueOf(xmlNode.getAttribute(
                            ACCESSLEVEL, 1));
                var[19] = (Integer) readWrite.get(access); //rw
                var[20] = xmlNode.getAttribute(MIN); //minvalue
                var[21] = xmlNode.getAttribute(MAX); //maxvalue
                var[22] = null; //defaultvalue

                Integer unitMeasure = Integer.valueOf(xmlNode.getAttribute(
                            MEASUREUNIT, 0));
                var[23] = unit.get(unitMeasure); //measureunit
                var[24] = Integer.valueOf(xmlNode.getAttribute(CATEGORY, "1")); //idvargroup

                if (buttonPresent)
                {
                    var[25] = xmlNode.getAttribute(IMAGEON);

                    if (null != var[25])
                    {
                        buttonImages.add(var[25].toString()); //imageon
                        var[25] = "images/button/" + var[25];
                    }

                    var[26] = xmlNode.getAttribute(IMAGEOFF);

                    if (null != var[26])
                    {
                        buttonImages.add(var[26].toString()); //imageoff
                        var[26] = "images/button/" + var[26];
                    }
                }

                boolean isHACCP = xmlNode.getAttribute(HACCP, "False")
                                         .trim().toUpperCase().equals("TRUE");

                if (type.intValue() == VariableInfo.TYPE_ALARM)
                {
                    var[27] = new Integer(BDevDetail.ALARM_FREQ); //frequency = 30
                    var[28] = new Double(0); //delta
                } //if
                else
                {
                    var[27] = (isHACCP ? new Integer(BDevDetail.HACCP_FREQ) : new Integer(0)); //Se è HACCP allora 900
                    var[28] = (isHACCP ? new Double(0) : new Double(-1));
                } //else

                var[29] = new Integer(0); //delay
                var[30] = "FALSE"; //isonchange
                var[31] = xmlNode.getAttribute(HACCP, "False").toUpperCase(); //ishaccp

                if ("TRUE".equalsIgnoreCase(xmlNode.getAttribute(HACCP, "False")
                                                       .trim().toUpperCase()))
                {
                    //	inserimento in tbl buffer x haccp:
                    sql = "insert into buffer values (?,?,?,?,?)";
                    
                    Object[] param = new Object[5];
                    param[0] = new Integer(1);
                    param[1] = var[0];
                    param[2] = BDevDetail.HACCP_ROWSHYSTORICAL;
                    param[3] = new Integer(-1);
                    param[4] = new Boolean(false);
                    
                    DatabaseMgr.getInstance().executeStatement(null, sql, param);
                    
                    GraphVariable.insertVariableGraphInfo(1,
                            ((Integer) var[0]).intValue(),
                            ids_device[i].intValue(), "TRUE", profile);
                }
                else if (type.intValue() == VariableInfo.TYPE_ALARM)
                {
                	// inserimento in tbl buffer x compatibilita' gestione precedente:
                    sql = "insert into buffer values (?,?,?,?,?)";
                    
                    Object[] param = new Object[5];
                    param[0] = new Integer(1);
                    param[1] = var[0];
                    param[2] = BDevDetail.ALARM_ROWSHYSTORICAL;
                    param[3] = new Integer(-1);
                    param[4] = new Boolean(false);
                    
                    DatabaseMgr.getInstance().executeStatement(null, sql, param);
                }

                var[32] = xmlNode.getAttribute(ISACTIVE, "True").toUpperCase(); //isactive
                var[33] = "FALSE"; //iscancelled
                var[34] = null; //grpcategory 

                if ("TRUE".equalsIgnoreCase(xmlNode.getAttribute(HISTORY,
                                "False").toUpperCase()))
                {
                    var[35] = SeqMgr.getInstance().next(null, "cfvariable",
                            "idvariable");
                }
                else
                {
                    var[35] = new Integer(-1); //var madre senza storico
                }

                var[36] = new Timestamp(System.currentTimeMillis()); //inserttime
                var[37] = new Timestamp(System.currentTimeMillis()); //lastupdate

                String insert = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                DatabaseMgr.getInstance().executeStatement(null, insert, var);

                //descrizione in lingua
                String description = xmlNode.getAttribute(DESCRIPTION);
                Object[] params = new Object[8];
                sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";

                for (int j = 0; j < langUsedBean.length; j++)
                {
                    String lang = langUsedBean[j].getLangcode();
                    params[0] = new Integer(1);
                    params[1] = lang;
                    params[2] = "cfvariable";
                    params[3] = var[0];
                    params[4] = description;
                    params[5] = xmlNode.getAttribute(SHORT_DESCRIPTION);
                    params[6] = xmlNode.getAttribute(LONG_DESCRIPTION);
                    params[7] = new Timestamp(System.currentTimeMillis());

                    DatabaseMgr.getInstance().executeStatement(null, sql, params);
                }

                // insert variabile storico
                if ("TRUE".equalsIgnoreCase(xmlNode.getAttribute(HISTORY,
                                "False").toUpperCase()))
                {
                    Integer prof = new Integer(xmlNode.getAttribute(
                                HISTORY_DEPTH));
                    Integer freq = new Integer(xmlNode.getAttribute(
                                HISTORY_FREQUENCY));
                    Double delta = new Double(xmlNode.getAttribute(
                                HISTORY_VARIATION));

                    var[0] = var[35]; //idvariabile storico
                    var[27] = freq; //frequency 
                    var[28] = delta; //delta  
                    var[31] = "FALSE"; //ishaccp
                    var[35] = null; //idhsvariable x figlio

                    DatabaseMgr.getInstance().executeStatement(null, insert, var);

                    // descrizione in lingua per la variabile storico
                    sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";

                    for (int j = 0; j < langUsedBean.length; j++)
                    {
                        String lang = langUsedBean[j].getLangcode();
                        params[0] = new Integer(1);
                        params[1] = lang;
                        params[2] = "cfvariable";
                        params[3] = var[0];
                        params[4] = description;
                        params[5] = xmlNode.getAttribute(SHORT_DESCRIPTION);
                        params[6] = xmlNode.getAttribute(LONG_DESCRIPTION);
                        params[7] = new Timestamp(System.currentTimeMillis());

                        DatabaseMgr.getInstance().executeStatement(null, sql,
                            params);
                    }

                    // inserimento buffer x figlio storicizzato
                    sql = "insert into buffer values (?,?,?,?,?)";

                    Object[] param = new Object[5];
                    param[0] = new Integer(1);
                    param[1] = var[0];

                    param[2] = new Short(ReorderInformation.calculateNewKeyMax(
                                new Short(type.shortValue()), prof, freq));

                    param[3] = new Integer(-1);
                    param[4] = new Boolean(false);
                    DatabaseMgr.getInstance().executeStatement(null, sql, param);

                    // insert variabile default per grafico storico
                    GraphVariable.insertVariableGraphInfo(1,
                        ((Integer) var[0]).intValue(),
                        ids_device[i].intValue(), "FALSE", profile);
                }

                GraphVariable.reorderCfPageGrah(1, profile,
                    ids_device[i].intValue());
            }
        }
    }

    public void updateVariable(XMLNode xmlNode, Integer iddevmdl,
        List<String> buttonImages, Map unit, Integer type) throws DataBaseException
    {
        //UPDATE CFVARMDL in base iddevmdl
        String code = xmlNode.getAttribute(CODE);
        boolean isHACCP = xmlNode.getAttribute(HACCP, "False").toUpperCase()
                                 .equals("TRUE");

        String update =
            "update cfvarmdl set type=?,addressin=?,addressout=?,vardimension=?,varlength=?,bitposition=?,signed=?,decimal=?," +
            "todisplay=?,buttonpath=?,priority=?,readwrite=?,minvalue=?,maxvalue=?,defaultvalue=?,measureunit=?,idvargroup=?," +
            "imageon=?,imageoff=?,frequency=?,delta=?,delay=?,ishaccp=?,isactive=?,isrelay=?,grpcategory=?,hstime=?,hsfrequency=?," +
            "hsdelta=? where iddevmdl=? and code=?";

        Object[] params = new Object[31];
        params[0] = type; //type
        params[1] = Integer.valueOf(xmlNode.getAttribute(ADDRESSIN)); //addressin
        params[2] = Integer.valueOf(xmlNode.getAttribute(ADDRESSOUT)); //addressout
        params[3] = Integer.valueOf(xmlNode.getAttribute(VARDIMENSION)); //vardimension
        params[4] = Integer.valueOf(xmlNode.getAttribute(VARLENGTH)); //varlenght
        params[5] = Integer.valueOf(xmlNode.getAttribute(BITPOSITION)); //bitposition
        params[6] = xmlNode.getAttribute(SIGNED, "TRUE").toUpperCase(); // signed
        params[7] = Integer.valueOf(xmlNode.getAttribute(DECIMAL, 0)); // decimal
        params[8] = xmlNode.getAttribute(TODISPLAY, "NONE").toUpperCase(); // todisplay

        String tmp = xmlNode.getAttribute(BUTTON);
        boolean buttonPresent = false;

        if ((null != tmp) && (tmp.equalsIgnoreCase("TRUE")))
        {
            buttonPresent = true;
            params[9] = "images/button/setvar.png";

            //buttonImages.add(xmlNode.getAttribute(IMAGEON));
        }
        else
        {
            params[9] = null;
        }

        params[10] = Integer.valueOf(xmlNode.getAttribute(PRIORITY, 1)); //priority

        Integer access = Integer.valueOf(xmlNode.getAttribute(ACCESSLEVEL, 1));
        params[11] = (Integer) readWrite.get(access); //readwrite
        params[12] = xmlNode.getAttribute(MIN); //minvalue
        params[13] = xmlNode.getAttribute(MAX); //maxvalue
        params[14] = null; //defaultvalue

        Integer unitMeasure = Integer.valueOf(xmlNode.getAttribute(
                    MEASUREUNIT, 0));
        params[15] = unit.get(unitMeasure); //measureunit
        params[16] = Integer.valueOf(xmlNode.getAttribute(CATEGORY, "1"));

        if (buttonPresent)
        {
            params[17] = xmlNode.getAttribute(IMAGEON);

            if (null != params[17])
            {
                buttonImages.add(params[17].toString());
                params[17] = "images/button/" + params[17];
            }

            params[18] = xmlNode.getAttribute(IMAGEOFF);

            if (null != params[18])
            {
                buttonImages.add(params[18].toString());
                params[18] = "images/button/" + params[18];
            }
        }

        if (type.intValue() == VariableInfo.TYPE_ALARM)
        {
            params[19] = new Integer(BDevDetail.ALARM_FREQ);
            params[20] = new Integer(0);
        } //if
        else
        {
            params[19] = (isHACCP ? new Integer(BDevDetail.HACCP_FREQ) : new Integer(0)); //Se è HACCP allora 900
            params[20] = (isHACCP ? new Integer(0) : new Integer(-1));
        } //else  	

        params[21] = new Integer(0);
        params[22] = xmlNode.getAttribute(HACCP, "False").trim().toUpperCase();
        params[23] = xmlNode.getAttribute(ISACTIVE, "True").toUpperCase();
        params[24] = xmlNode.getAttribute(RELAY, "False").toUpperCase();

        params[25] = null; //grpcategory

        if ("TRUE".equalsIgnoreCase(xmlNode.getAttribute(HISTORY, "False").toUpperCase()))
        {
            params[26] = new Integer(xmlNode.getAttribute(HISTORY_DEPTH,
                        "15552000")); //profondità storico
            params[27] = new Integer(xmlNode.getAttribute(HISTORY_FREQUENCY,
                        "300")); //frequenza storico
            params[28] = new Double(xmlNode.getAttribute(HISTORY_VARIATION, "1")); //delta storico    	
        }
        else
        {
            params[26] = null; //profondità storico
            params[27] = null; //frequenza storico
            params[28] = null; //delta storico     
        }

        params[29] = iddevmdl;
        params[30] = code;
        
        DatabaseMgr.getInstance().executeStatement(null, update, params);

        //UPDATE CFTABLEEXT-> DESCRIPTION PER MODELLI VARIABILI
        String var_description = xmlNode.getAttribute(DESCRIPTION);
        
        update = "update cftableext set description=?,shortdescr=?,longdescr=?,lastupdate=? where tablename=? and " +
        		" tableid in (select idvarmdl from cfvarmdl where iddevmdl=? and code=?) and idsite=?";
        
        params = new Object[8];
        params[0] = var_description;
        params[1] = xmlNode.getAttribute(SHORT_DESCRIPTION);
        params[2] = xmlNode.getAttribute(LONG_DESCRIPTION);
        params[3] = new Timestamp(System.currentTimeMillis());
        params[4] = "cfvarmdl";
        params[5] = iddevmdl;
        params[6] = code;
        params[7] = new Integer(1);
        
        DatabaseMgr.getInstance().executeStatement(null, update, params);

        //update cfvariable
        String sql =
            "select idvariable from cfvariable inner join cfvarmdl on cfvarmdl.code=? and " +
            "cfvarmdl.iddevmdl=? and cfvariable.idvarmdl=cfvarmdl.idvarmdl";
        params = new Object[2];
        params[0] = code;
        params[1] = iddevmdl;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        Integer var_to_update = null;

        //ricavati gli id delle variabili da updatare, ciclo e faccio update 
        sql = "update cfvariable set type=?,addressin=?,addressout=?,vardimension=?,varlength=?,bitposition=?,signed=?,decimal=?,buttonpath=?," +
        "readwrite=?,minvalue=?,maxvalue=?,defaultvalue=?,idvargroup=?,imageon=?,imageoff=?,delay=?,grpcategory=?" +
        " where idsite=? and idvariable=?";
        
        for (int i = 0; i < rs.size(); i++)
        {
            var_to_update = (Integer) rs.get(i).get(0);
            
            params = new Object[20];
            params[0] = type; //type
            params[1] = Integer.valueOf(xmlNode.getAttribute(ADDRESSIN)); //addressin
            params[2] = Integer.valueOf(xmlNode.getAttribute(ADDRESSOUT)); //addressout
            params[3] = Integer.valueOf(xmlNode.getAttribute(VARDIMENSION)); //vardimension
            params[4] = Integer.valueOf(xmlNode.getAttribute(VARLENGTH)); //varlenght
            params[5] = Integer.valueOf(xmlNode.getAttribute(BITPOSITION)); //bitposition
            params[6] = xmlNode.getAttribute(SIGNED, "TRUE").toUpperCase(); // signed
            params[7] = Integer.valueOf(xmlNode.getAttribute(DECIMAL, 0)); // decimal
            tmp = xmlNode.getAttribute(BUTTON);

            if ((null != tmp) && (tmp.equalsIgnoreCase("TRUE")))
            {
                buttonPresent = true;
                params[8] = "images/button/setvar.png"; //buttonpath

                //buttonImages.add(xmlNode.getAttribute(IMAGEON));
            }

            access = Integer.valueOf(xmlNode.getAttribute(ACCESSLEVEL, 1));
            params[9] = (Integer) readWrite.get(access); //rw
            params[10] = xmlNode.getAttribute(MIN); //minvalue
            params[11] = xmlNode.getAttribute(MAX); //maxvalue
            params[12] = null; //defaultvalue
            params[13] = Integer.valueOf(xmlNode.getAttribute(CATEGORY, "1")); //idvargroup

            if (buttonPresent)
            {
                params[14] = xmlNode.getAttribute(IMAGEON);

                if (null != params[14])
                {
                    buttonImages.add(params[14].toString());
                    params[14] = "images/button/" + params[14];
                }

                params[15] = xmlNode.getAttribute(IMAGEOFF);

                if (null != params[15])
                {
                    buttonImages.add(params[15].toString());
                    params[15] = "images/button/" + params[15];
                }
            }

            params[16] = new Integer(0); //delay
            params[17] = null; //grpcategory
            params[18] = new Integer(1);
            params[19] = var_to_update;

            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
    }

    public static void alignRif(String code_var, String code_rif,
        Integer iddevmdl, boolean isMin) throws DataBaseException
    {
        String sql = "";

        if (isMin)
        {
            sql = "update cfvarmdl set minvalue='pk'||(select idvarmdl from cfvarmdl where iddevmdl=? and code=? ) where iddevmdl=? and code=?";
        }
        else
        {
            sql = "update cfvarmdl set maxvalue='pk'||(select idvarmdl from cfvarmdl where iddevmdl=? and code=? ) where iddevmdl=? and code=?";
        }

        Object[] param = new Object[4];
        param[0] = iddevmdl;
        param[1] = code_rif;  //code variabile che fa da minimo o massimo
        param[2] = iddevmdl;
        param[3] = code_var;
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }
    
    public static void alignRifInstance(String code_var, String code_rif,
            int[] iddevevices, boolean isMin) throws DataBaseException
        {
            String sql = "";

            if (isMin)
            {
                sql = "update cfvariable set minvalue='pk'||(select idvariable from cfvariable where iddevice=? and code=? ) where iddevice=? and code=?";
            }
            else
            {
                sql = "update cfvariable set maxvalue='pk'||(select idvariable from cfvariable where iddevice=? and code=? ) where iddevice=? and code=?";
            }
            
            for (int i=0;i<iddevevices.length;i++)
            {
	            Object[] param = new Object[4];
	            param[0] = new Integer(iddevevices[i]);
	            param[1] = code_rif;  //code variabile che fa da minimo o massimo
	            param[2] = new Integer(iddevevices[i]);
	            param[3] = code_var;
	            DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
        }
}
