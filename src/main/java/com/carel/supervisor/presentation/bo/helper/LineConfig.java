package com.carel.supervisor.presentation.bo.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.LineBeanList;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.BDevDetail;
import com.carel.supervisor.presentation.bo.BSiteView;
import com.carel.supervisor.presentation.fs.FSStatus;
import com.carel.supervisor.presentation.helper.KeyMaxHelper;
import com.carel.supervisor.presentation.helper.SpaceHistoricalHelper;
import com.carel.supervisor.presentation.session.UserSession;


public class LineConfig
{
	public static final int ALL_CHECK = 1;
	public static final int HISTOR_CHECK = 2;
	
	public static final int DEFAULT_MAX_MIN = 10;
	public static Map<Integer,Integer> serialCOM = new HashMap<Integer,Integer>();
	
	public static String serialCOM1 = "COM3";
	public static String serialCOM2 = "COM4";
	
	private static String GETCOM_CMD ="MIO;9";
	
	public LineConfig()
    {
    }

    public static void initRS485COMPorts()
    {
		String result = "";
		int port = 1980;
    	
    	try{
    		//get COM port associated to USB port A
    		// --RS485 port n�1--
    		result = SocketComm.sendCommand("localhost", port, GETCOM_CMD+";0");
    		serialCOM.put(Integer.parseInt(result), 1);
    	}
    	catch (Exception e)
    	{
    		LoggerMgr.getLogger(LineConfig.class).error("Error on retrieve COM for serial port 1: "+e);
    	}
    	try{
    		//get COM port associated to USB port B
    		// --RS485 port n�2-- 
    		result = SocketComm.sendCommand("localhost", port, GETCOM_CMD+";1");
    		serialCOM.put(Integer.parseInt(result), 2);
    	}
    	catch (Exception e)
    	{
    		LoggerMgr.getLogger(LineConfig.class).error("Error on retrieve COM for serial port 2: "+e);
    	}
    }
    
    
    public static Map<Integer,Integer> getSerial485COM()
    {
    	return serialCOM;
    }
	
	public static void updateCfTableExt() throws DataBaseException
    {
        //String sql = "drop index ui_cftableext";
        //DatabaseMgr.getInstance().executeStatement(null, sql, null);
        String sql = "insert into cftableext SELECT * from tmp_cftableext";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "truncate tmp_cftableext";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        //sql = "create unique index ui_cftableext on cftableext (idsite, tablename, tableid,languagecode);";
        sql = "reindex table cftableext";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "reindex table cfvariable";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "reindex table buffer";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "reindex table cfdevice";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "analyze cftableext";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "analyze cfvariable";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
    }

    public static boolean insertDevice(int idsite, String codeLine, int idline,
        int address, int baseGlobalIndex, String langDef, String language,
        DevMdlBean tmpDevMdl, long actualSpaceReq, long freeDiskSpace,
        KeyMaxHelper totkeymax,int idprofile ) throws Exception
    {
        String sql = null;
        VarMdlBean[] tmp_varMdl_list = null;
        tmp_varMdl_list = VarMdlBeanList.retrieveById(idsite,
                tmpDevMdl.getIddevmdl());

        Integer frequency = null;
        Integer period = null;
        Short type = null;

        //hystorical: somma keymax sul buffer delle variabili da inserire
        long hystorical = 0;

        for (int i = 0; i < tmp_varMdl_list.length; i++)
        {
            frequency = tmp_varMdl_list[i].getFrequency(); //frequenza haccp o allarme (necessaria ?)
            type = new Short((short) tmp_varMdl_list[i].getType().intValue());
            
            if (tmp_varMdl_list[i].getIshaccp().trim().equalsIgnoreCase("TRUE"))  //variabile haccp
            {
            	hystorical = hystorical + BDevDetail.HACCP_ROWSHYSTORICAL;
            }
            else if (tmp_varMdl_list[i].getType().intValue() == VariableInfo.TYPE_ALARM)    //variabile allarme
            {
            	hystorical = hystorical + BDevDetail.ALARM_ROWSHYSTORICAL;
            }
            else if ((null != frequency) && (frequency.intValue() > 0))	//errore nel modello
            {
            	Logger logger = LoggerMgr.getLogger(LineConfig.class);
                logger.error(tmpDevMdl.getCode() + " - " + tmp_varMdl_list[i].getCode() + " - varmodel malformed");
            }

            frequency = tmp_varMdl_list[i].getHsfrequency(); //frequenza storico

            if ((null != frequency) && (frequency.intValue() > 0))  //variabile storico
            {
                //type = new Short((short) tmp_varMdl_list[i].getType().intValue());
                
                if (type.intValue() != VariableInfo.TYPE_ALARM)
                {
	                period = tmp_varMdl_list[i].getHstime();
	                hystorical = hystorical +
	                    ReorderInformation.calculateNewKeyMax(type, period,
	                        frequency);
                }
                else	//errore nel modello
                {
                	Logger logger = LoggerMgr.getLogger(LineConfig.class);
                    logger.error(tmpDevMdl.getCode() + " - " + tmp_varMdl_list[i].getCode() + " - alarm varmodel malformed");
                }
            }
        }

        //sommo nuovo storico al totale dei precedenti da salvare
        totkeymax.setKeymax(totkeymax.getKeymax() + hystorical);

        if (SpaceHistoricalHelper.confirmHistoricalCiclic(actualSpaceReq,
                    freeDiskSpace, totkeymax.getKeymax()))
        {
            DeviceBean tmp = new DeviceBean();
            String code = codeLine + "." + format(address);
            tmp.setPvcode(BaseConfig.getPlantId());
            tmp.setIdsite(idsite);
            tmp.setIslogic(false);
            tmp.setIddevmdl(tmpDevMdl.getIddevmdl());
            tmp.setIdline(idline);
            tmp.setAddress(address);
            tmp.setCode(code);
            tmp.setImagepath(tmpDevMdl.getImagepath());
            tmp.setLittleEndian(tmpDevMdl.isLittleEndian());
            tmp.setIdgroup(1);
            tmp.setGlobalindex(baseGlobalIndex);
            tmp.setIsenabled("TRUE");
            tmp.setIscancelled("FALSE");

            int idtmp = tmp.save();

            String temp = null;
            LangUsedBeanList langUsed = new LangUsedBeanList();
            LangUsedBean[] allLangs = langUsed.retrieveAllLanguage(idsite);
            String[] langsCode = new String[allLangs.length];

            for (int i = 0; i < allLangs.length; i++)
            {
                langsCode[i] = allLangs[i].getLangcode();
            }

            for (int i = 0; i < langsCode.length; i++)
            {
                temp = TableExtBean.retrieveDescritionFromTableById(idsite,
                        langsCode[i], tmpDevMdl.getIddevmdl(), "cfdevmdl") +
                    " - " + tmp.getAddress();
                TableExtBean.insertTableExt(idsite, langsCode[i], "cfdevice",
                    temp, idtmp);
            }

            //      inserimento di variabili in cfvariable da cfvarmdl
            VarMdlBean tmp_varMdl = null;
            Timestamp now = new Timestamp(System.currentTimeMillis());

            Object[][] vars = new Object[tmp_varMdl_list.length][38];
            ArrayList<Object[]> tmp_array = new ArrayList<Object[]>();

            //List hsfreq = new ArrayList();
            ArrayList<Object[]> buffer = new ArrayList<Object[]>();
            ArrayList<Object[]> relay = new ArrayList<Object[]>();

            ArrayList<MinMax> min = new ArrayList<MinMax>();
            ArrayList<MinMax> max = new ArrayList<MinMax>();
            HashMap<Integer, Integer> varmdl = new HashMap<Integer, Integer>();

            for (int i = 0; i < tmp_varMdl_list.length; i++)
            {
                tmp_varMdl = tmp_varMdl_list[i];
                vars[i][0] = SeqMgr.getInstance().next(null, "cfvariable",
                        "idvariable");
                vars[i][1] = BaseConfig.getPlantId();
                vars[i][2] = tmp_varMdl.getIdsite();
                vars[i][3] = new Integer(idtmp);
                vars[i][4] = "FALSE";
                vars[i][5] = tmp_varMdl.getIdvarmdl();

                //Relazione idvarmdl <- idvariable
                varmdl.put(tmp_varMdl.getIdvarmdl(), (Integer)vars[i][0]);

                vars[i][6] = null;
                vars[i][7] = tmp_varMdl.getCode();
                vars[i][8] = tmp_varMdl.getType();
                vars[i][9] = tmp_varMdl.getAddressIn();
                vars[i][10] = tmp_varMdl.getAddressOut();
                vars[i][11] = tmp_varMdl.getVarDimension();
                vars[i][12] = tmp_varMdl.getVarLength();
                vars[i][13] = tmp_varMdl.getBitPosition();
                vars[i][14] = tmp_varMdl.getSigned();
                vars[i][15] = tmp_varMdl.getDecimal();
                vars[i][16] = tmp_varMdl.getTodisplay();
                vars[i][17] = tmp_varMdl.getButtonpath();
                vars[i][18] = tmp_varMdl.getPriority();
                vars[i][19] = tmp_varMdl.getReadwrite();
                vars[i][20] = UtilityString.substring(tmp_varMdl.getMinvalue(),DEFAULT_MAX_MIN);

                if (null != tmp_varMdl.getMinvalue())
                {
                    if (tmp_varMdl.getMinvalue().startsWith("pk"))
                    {
                        MinMax minmax = new MinMax(tmp_varMdl.getMinvalue(), i);
                        min.add(minmax);
                    }
                }

                vars[i][21] = UtilityString.substring(tmp_varMdl.getMaxvalue(),DEFAULT_MAX_MIN);

                if (null != tmp_varMdl.getMaxvalue())
                {
                    if (tmp_varMdl.getMaxvalue().startsWith("pk"))
                    {
                        MinMax minmax = new MinMax(tmp_varMdl.getMaxvalue(), i);
                        max.add(minmax);
                    }
                }

                vars[i][22] = UtilityString.substring(tmp_varMdl.getDefaultValue(),DEFAULT_MAX_MIN);
                vars[i][23] = tmp_varMdl.getMeasureUnit();
                vars[i][24] = tmp_varMdl.getIdvargroup();
                vars[i][25] = tmp_varMdl.getImageOn();
                vars[i][26] = tmp_varMdl.getImageOff();
                
                // gestione frequenza:
                if (tmp_varMdl.getIshaccp().trim().toUpperCase().equals("TRUE"))
                {
                	vars[i][27] = new Integer(BDevDetail.HACCP_FREQ);
                }
                else if (tmp_varMdl.getType().intValue() == VariableInfo.TYPE_ALARM)
                {
                	vars[i][27] = new Integer(BDevDetail.ALARM_FREQ);
                }
                else
                {
	                vars[i][27] = tmp_varMdl.getFrequency();
                }

                vars[i][28] = tmp_varMdl.getDelta();
                vars[i][29] = tmp_varMdl.getDelay();
                vars[i][30] = "FALSE"; //CAmpo mai letto, non eliminato perch� troppo impattante
                vars[i][31] = tmp_varMdl.getIshaccp();
                vars[i][32] = tmp_varMdl.getIsactive();
                vars[i][33] = "FALSE";
                vars[i][34] = tmp_varMdl.getGrpcategory();
                vars[i][35] = new Integer(-1);
                vars[i][36] = now;
                vars[i][37] = now;

                int id_tmp_varphy = ((Integer) vars[i][0]).intValue(); //tmp_varphy.save();

                //insert cfrelay  
                if (tmp_varMdl.getIsrelay().equals("TRUE"))
                {
                    Object[] param = new Object[10];
                    relay.add(param);

                    SeqMgr o = SeqMgr.getInstance();
                    param[0] = o.next(null, "cfrelay", "idrelay");
                    param[1] = new Integer(1); //SITO
                    param[2] = new Integer(id_tmp_varphy);
                    param[3] = "FALSE";
                    param[4] = "M";
                    param[5] = new Integer(-1);
                    param[6] = new Integer(1);
                    param[7] = now;
                    param[8] = "NOTEST";
                    param[9] = false;
                }

                // insert su buffer. var = "tmp_varphy" e id = "id_tmp_varphy"
                if ((tmp_varMdl.getIsactive().equals("TRUE")) &&
                        ((tmp_varMdl.getIshaccp().trim().toUpperCase().equals("TRUE")) || 
                        		(tmp_varMdl.getType().intValue() == VariableInfo.TYPE_ALARM)))
                {
                    Object[] param = new Object[5];
                    //buffer.add(param);
                    param[0] = new Integer(1); //SITO
                    param[1] = new Integer(id_tmp_varphy);
                    if (tmp_varMdl.getIshaccp().trim().equalsIgnoreCase("TRUE"))
                    {
                    	param[2] = new Short((short) BDevDetail.HACCP_ROWSHYSTORICAL);  //haccp
                    	//buffer.add(param);
                    }
                    else if (tmp_varMdl.getType().intValue() == VariableInfo.TYPE_ALARM)
                    {
                    	param[2] = new Short((short) BDevDetail.ALARM_ROWSHYSTORICAL);  //allarme
                    	//buffer.add(param);
                    }
                    param[3] = new Integer(-1);
                    param[4] = new Boolean(false);
                    
                    buffer.add(param);

                    /*
                    param = new Object[4];
                    hsfreq.add(param);
                    param[0] = new Integer(1); //SITO
                    param[1] = new Integer(id_tmp_varphy);
                    param[2] = tmp_varMdl.getFrequency();
                    param[3] = now;*/
                }

                //se la variabile contiene di default lo storico, insert variabile slave x storico
                //if (tmp_varMdl.getHstime().intValue() > 0)
                if ((tmp_varMdl.getHsfrequency() != null) && (tmp_varMdl.getHsfrequency().intValue() > 0) 
                		&& tmp_varMdl.getHstime() != null && tmp_varMdl.getHstime().intValue()>0)//add Hstime>0 by Kevin, 2011-9-20
                {
                    Object[] vars_hist = new Object[38];
                    vars_hist[0] = SeqMgr.getInstance().next(null,
                            "cfvariable", "idvariable");
                    vars_hist[1] = BaseConfig.getPlantId();
                    vars_hist[2] = tmp_varMdl.getIdsite();
                    vars_hist[3] = new Integer(idtmp);
                    vars_hist[4] = "FALSE";
                    vars_hist[5] = tmp_varMdl.getIdvarmdl();
                    vars_hist[6] = null;
                    vars_hist[7] = tmp_varMdl.getCode();
                    vars_hist[8] = tmp_varMdl.getType();
                    vars_hist[9] = tmp_varMdl.getAddressIn();
                    vars_hist[10] = tmp_varMdl.getAddressOut();
                    vars_hist[11] = tmp_varMdl.getVarDimension();
                    vars_hist[12] = tmp_varMdl.getVarLength();
                    vars_hist[13] = tmp_varMdl.getBitPosition();
                    vars_hist[14] = tmp_varMdl.getSigned();
                    vars_hist[15] = tmp_varMdl.getDecimal();
                    vars_hist[16] = tmp_varMdl.getTodisplay();
                    vars_hist[17] = tmp_varMdl.getButtonpath();
                    vars_hist[18] = tmp_varMdl.getPriority();
                    vars_hist[19] = tmp_varMdl.getReadwrite();
                    // min e  max inutili x storico
                    vars_hist[20] = null;
                    vars_hist[21] = null;
                   
                    vars_hist[22] = UtilityString.substring(tmp_varMdl.getDefaultValue(),DEFAULT_MAX_MIN);
                    vars_hist[23] = tmp_varMdl.getMeasureUnit();
                    vars_hist[24] = tmp_varMdl.getIdvargroup();
                    vars_hist[25] = tmp_varMdl.getImageOn();
                    vars_hist[26] = tmp_varMdl.getImageOff();
                    vars_hist[27] = tmp_varMdl.getHsfrequency();
                    vars_hist[28] = tmp_varMdl.getHsdelta();
                    vars_hist[29] = tmp_varMdl.getDelay();
                    vars_hist[30] = "FALSE"; //Campo mai letto, non eliminato perch� troppo impattante
                    vars_hist[31] = "FALSE";
                    vars_hist[32] = tmp_varMdl.getIsactive();
                    vars_hist[33] = "FALSE";
                    vars_hist[34] = tmp_varMdl.getGrpcategory();
                    vars_hist[35] = null;
                    vars_hist[36] = now;
                    vars_hist[37] = now;
                    tmp_array.add(vars_hist);

                    Integer id_hist_var = (Integer) vars_hist[0];
                    vars[i][35] = id_hist_var;

                    Object[] param = new Object[5];
                    param[0] = new Integer(1); //SITO
                    param[1] = id_hist_var;
                    param[2] = new Short(ReorderInformation.calculateNewKeyMax(
                                new Short(tmp_varMdl.getType().shortValue()),
                                tmp_varMdl.getHstime(),
                                tmp_varMdl.getHsfrequency()));
                    param[3] = new Integer(-1);
                    param[4] = new Boolean(false);
                    buffer.add(param);

                    /*
                    param = new Object[4];
                    param[0] = new Integer(1); //SITO
                    param[1] = id_hist_var;
                    param[2] = tmp_varMdl.getHsfrequency();
                    param[3] = now;
                    hsfreq.add(param);*/
                }
            }

            //Verifico la presenza di max/min da ricodificare
            if (0 < min.size())
            {
                Integer idVarMdl = null;
                Integer idVarMin = null;
                MinMax minmax = null;

                for (int j = 0; j < min.size(); j++)
                {
                    minmax = (MinMax) min.get(j);
                    idVarMdl = minmax.getIdVarmdl();
                    idVarMin = varmdl.get(idVarMdl);
                    vars[minmax.getPos()][20] = "pk" +
                        String.valueOf(idVarMin);
                }
            }

            if (0 < max.size())
            {
                Integer idVarMdl = null;
                Integer idVarMax = null;
                MinMax minmax = null;

                for (int j = 0; j < max.size(); j++)
                {
                    minmax = (MinMax) max.get(j);
                    idVarMdl = minmax.getIdVarmdl();
                    idVarMax = varmdl.get(idVarMdl);
                    vars[minmax.getPos()][21] = "pk" +
                        String.valueOf(idVarMax);
                }
            }

            ArrayList<Object[]> lista = new ArrayList<Object[]>();

            for (int i = 0; i < vars.length; i++)
            {
                lista.add(vars[i]);
            }

            for (int i = 0; i < tmp_array.size(); i++)
            {
                lista.add(tmp_array.get(i));
            }

            String insert = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            DatabaseMgr.getInstance().executeMultiStatement(null, insert, lista);

            //update minvalue e maxvalue per sistemare i valori dove c'� il riferimento pk
            //updateVarMinMaxValue(idsite, idtmp);
            sql = "insert into tmp_cftableext SELECT cftableext.idsite, cftableext.languagecode, 'cfvariable', " +
                "cfvariable.idvariable, cftableext.description, cftableext.shortdescr, cftableext.longdescr, current_timestamp " +
                "FROM (cfvariable INNER JOIN cfvarmdl ON cfvariable.idvarmdl = cfvarmdl.idvarmdl) INNER JOIN cftableext ON " +
                "cfvarmdl.idvarmdl = cftableext.tableid WHERE cfvariable.idsite=1 and cfvariable.iscancelled='FALSE' AND cftableext.idsite = 1 AND cftableext.tablename='cfvarmdl' and cfvariable.iddevice = ?";
            DatabaseMgr.getInstance().executeStatement(null, sql,
                new Object[] { new Integer(idtmp) });

            if (buffer.size() > 0)
            {
                sql = "insert into buffer values (?,?,?,?,?)";
                DatabaseMgr.getInstance().executeMultiStatement(null, sql,
                    buffer);
            }

            /*
                        if (hsfreq.size() != 0)
                        {
                            sql = "insert into hsfrequency values (?,?,?,?)";
                            DatabaseMgr.getInstance().executeMultiStatement(null, sql,
                                hsfreq);
                        }*/
            if (relay.size() > 0)
            {
                sql = "insert into cfrelay values (?,?,?,?,?,?,?,?,?,?)";
                DatabaseMgr.getInstance().executeMultiStatement(null, sql, relay);
            }

            //preconfigurazione grafico//
            
            //#####################################################
            GraphVariable.insertDeviceGraphVariable(idsite, idtmp, idprofile);
            //#####################################################

            return true;
        }
        else
        {
            //TODO: W080 = add new msg x Eventi x not enough HD free space x historical data
        	EventMgr.getInstance().error(idsite, "Config", "Action", "W080", null);
        	
        	return false;
        }
    }

    public static void alignDeviceCode() throws DataBaseException
    {
        String sql = "select cfdevice.iddevice as iddevice, cfline.code as code, " +
            "cfdevice.address as address from cfdevice inner join cfline on " +
            " cfline.idline=cfdevice.idline where cfdevice.iscancelled='FALSE' and cfdevice.idsite=1";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        Record record = null;
        Integer iddevice = null;
        Integer code = null;
        String newCode = null;
        Integer address = null;
        sql = "update cfdevice set code = ? where idsite=1 and iddevice = ?";

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            iddevice = (Integer) record.get("iddevice");
            code = (Integer) record.get("code");
            address = (Integer) record.get("address");
            newCode = code.intValue() + "." + format(address.intValue());
            DatabaseMgr.getInstance().executeStatement(null, sql,
                new Object[] { newCode, iddevice });
        }
    }

    private static String format(int i)
    {
        if (i < 10)
        {
            return "00" + String.valueOf(i);
        }
        else if (i < 100)
        {
            return "0" + String.valueOf(i);
        }
        else
        {
            return String.valueOf(i);
        }
    }

    public static void reorderGlobalIndexOfDevice(int idsite)
        throws DataBaseException
    {
        String sql = "select iddevice from cfdevice where idsite = ? and iscancelled = 'FALSE' order by idline,address";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        int[] ids = new int[rs.size()];

        for (int i = 0; i < ids.length; i++)
        {
            ids[i] = ((Integer) rs.get(i).get("iddevice")).intValue();
        }

        int globalIndex = 0;
        sql = "update cfdevice set globalindex = ? where idsite = ? and iddevice = ?";

        ArrayList<Object[]> values = new ArrayList<Object[]>();

        for (int i = 0; i < ids.length; i++)
        {
            globalIndex++;

            Object[] param = new Object[3];
            param[0] = new Integer(globalIndex);
            param[1] = new Integer(idsite);
            param[2] = new Integer(ids[i]);
            values.add(param);
        }

        if (0 < ids.length)
        {
            DatabaseMgr.getInstance().executeMultiStatement(null, sql, values);
        }
    }

    /*
    private static void updateVarMinMaxValue(int idsite, int iddevice)
        throws DataBaseException
    {
        //update per settare min value correttamente dove ho pk
        String sql = "select idvariable,minvalue from cfvariable where idsite = ? and iddevice = ? and minvalue like 'pk%'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(iddevice) });

        Integer idvar = null;
        Integer idvarmdl = null;
        String tmp_sqlmin1 = "select idvariable from cfvariable where idsite=? and idvarmdl=? and iddevice = ?";
        String tmp_sqlmin2 = "update cfvariable set minvalue = ? where idsite = ? and idvariable = ? ";
        String new_pk = "";

        for (int i = 0; i < rs.size(); i++)
        {
            idvar = (Integer) rs.get(i).get("idvariable");
            idvarmdl = Integer.valueOf(rs.get(i).get("minvalue").toString()
                                         .substring(2));

            RecordSet tmp_rec = DatabaseMgr.getInstance().executeQuery(null,
                    tmp_sqlmin1,
                    new Object[]
                    {
                        new Integer(idsite), idvarmdl, new Integer(iddevice)
                    });

            new_pk = "pk" + tmp_rec.get(0).get("idvariable").toString();
            DatabaseMgr.getInstance().executeStatement(null, tmp_sqlmin2,
                new Object[] { new_pk, new Integer(idsite), idvar });
        }

        //update per settare max value correttamente dove ho pk
        sql = "select idvariable,maxvalue from cfvariable where idsite = ? and iddevice = ? and maxvalue like 'pk%'";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(iddevice) });

        idvar = null;
        idvarmdl = null;

        String tmp_sqlmax1 = "select idvariable from cfvariable where idsite=? and idvarmdl=? and iddevice = ?";
        String tmp_sqlmax2 = "update cfvariable set maxvalue = ? where idsite = ? and idvariable = ? ";
        new_pk = "";

        for (int i = 0; i < rs.size(); i++)
        {
            idvar = (Integer) rs.get(i).get("idvariable");
            idvarmdl = Integer.valueOf(rs.get(i).get("maxvalue").toString()
                                         .substring(2));

            RecordSet tmp_rec = DatabaseMgr.getInstance().executeQuery(null,
                    tmp_sqlmax1,
                    new Object[]
                    {
                        new Integer(idsite), idvarmdl, new Integer(iddevice)
                    });

            new_pk = "pk" + tmp_rec.get(0).get("idvariable").toString();
            DatabaseMgr.getInstance().executeStatement(null, tmp_sqlmax2,
                new Object[] { new_pk, new Integer(idsite), idvar });
        }
    }
    */
    public static RemoveState verifyVariableDependence(int idsite,
        int[] ids_variables, String lang) throws DataBaseException
    {
        //Ricerco se la variabile � coninvolta in qualche ambito, e in caso ritorno un messaggio
        RemoveState state = new RemoveState("", true);

        //serie di controlli
        state = VariableHelper.isVariableInAction(idsite, ids_variables, lang);

        if (state.getCanRemove() == true)
        {
            state = VariableHelper.isVariableInCondition(idsite, ids_variables,
                    lang);
        }

        if (state.getCanRemove() == true)
        {
            state = VariableHelper.isVariableInReport(idsite, ids_variables,
                    lang);
        }

        if (state.getCanRemove() == true)
        {
            state = VariableHelper.isVariableInLogic(idsite, ids_variables, lang);
        }

        if (state.getCanRemove() == true)
        {
            state = VariableHelper.isVariableInGuardian(idsite, ids_variables,
                    lang);
        }

        return state;
    }
    
    public static RemoveState verifyDeviceDependence(int idsite, int iddevice,
        String lang) throws Exception
    {
        //Radice verifica:Device -> verifica variabili 	DeviceBean device = null;
        int[] ids_vars = VarphyBeanList.getIdsOfVarsOfDevice(lang, idsite,
                iddevice);

        RemoveState state = new RemoveState("", true);

        state = verifyVariableDependence(idsite, ids_vars, lang);

        return state;
    }

    public static RemoveState verifyLineDependence(int idsite, int idline,
        String lang) throws Exception
    {
        //Radice verifica:Linea -> verifica devices -> verifica variabili
        LineBeanList lines = new LineBeanList();
        DeviceBean[] devices = lines.getDeviceOfLine(idsite, idline, lang);

        RemoveState state = new RemoveState("", true);

        for (int i = 0; i < devices.length; i++)
        {
            state = verifyDeviceDependence(idsite, devices[i].getIddevice(),
                    lang);

            if (state.getCanRemove() == false)
            {
                break;
            }
        }

        return state;
    }
    
    
    // NEW METHODS FOR VARIABLE DEPENDENCE CHECK. Nicola Compagno 10/9/09
    
    public static VarDependencyState checkVariableDependence(int idsite, int[] ids_variables, int iddevmdl, int checkType, String langcode)
    throws Exception
    {
    	return checkVariableDependence(idsite,ids_variables,iddevmdl,checkType,langcode,false);
    }
    
    
    public static VarDependencyState checkVariableDependence(int idsite, int[] ids_variables, int iddevmdl, int checkType, String langcode, boolean islogical) throws Exception
    {
    	VarDependencyState dependenceState = new VarDependencyState();
    	dependenceState.addMsgSection(1, "");
    	
    	switch (checkType)
    	{
    		//ALL_CHECK when device/s or line/s are removed
    		case ALL_CHECK:
    			dependenceState = VarDependencyCheck.checkVariablesInAction(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInAlarmCondition(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInEventCondition(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInLogicVar(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInLogicDev(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInInstantReport(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInHistorReport(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInBooklet(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInGuardian(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInRemote(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInParametersControl(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInEnergy(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInOpt(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInTechSwitch(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInFSP(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInCO2(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInModbusSlave(1, ids_variables, langcode, dependenceState, 1);
	        	if(islogical == false)
	        	{
		        	dependenceState = VarDependencyCheck.checkVariablesInLightsOnOff(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
		        	dependenceState = VarDependencyCheck.checkVariablesInKPI(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
		        	dependenceState = VarDependencyCheck.checkVariablesInDewPoint(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
	        	}
    			break;
    		//HISTOR_CHECK when historical is REMOVED from variable settings
    		case HISTOR_CHECK:
    			dependenceState = VarDependencyCheck.checkVariablesInEventCondition(1, ids_variables, langcode, dependenceState, 1);
    			dependenceState = VarDependencyCheck.checkVariablesInHistorReport(1, ids_variables, langcode, dependenceState, 1);
    			dependenceState = VarDependencyCheck.checkVariablesInRemote(1, ids_variables, langcode, dependenceState, 1);
				dependenceState = VarDependencyCheck.checkVariablesInEnergy(1, ids_variables, langcode, dependenceState, 1);
    			break;
    	}
    	
    	return dependenceState;
    }
    
    private static void dltVariableDependence(int idsite, int[] ids_variables,int iddevmdl, int checkType, String langcode, boolean islogical) throws Exception {
    	switch (checkType)
    	{
    		case ALL_CHECK:
    			VarDependencyCheck.dltVariablesInAction(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInAlarmCondition(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInEventCondition(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInLogicVar(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInLogicDev(1, ids_variables, langcode,  1);
	        	VarDependencyCheck.dltVariablesInInstantReport(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInHistorReport(1, ids_variables, langcode,  1);
	        	VarDependencyCheck.dltVariablesInBooklet(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInGuardian(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInRemote(1, ids_variables, langcode,  1);
	        	VarDependencyCheck.dltVariablesInParametersControl(1, ids_variables, langcode, 1);
	        	VarDependencyCheck.dltVariablesInEnergy(1, ids_variables, langcode, 1);
	        	if(islogical == false)
	        	{
		        	VarDependencyCheck.dltVariablesInLightsOnOff(1, ids_variables, iddevmdl, langcode,  1);
		        	VarDependencyCheck.dltVariablesInKPI(1, ids_variables, iddevmdl, langcode,  1);
		        	VarDependencyCheck.dltVariablesInDewPoint(1, ids_variables, iddevmdl, langcode, 1);
	        	}
    			break;
    	}
	}
    
    
    public static VarDependencyState checkDeviceDependence(int idsite, int iddevice, int checkType, String lang) throws Exception
    {
    	return checkDeviceDependence(idsite,iddevice,checkType,lang,false);
    }
    
    
    public static VarDependencyState checkDeviceDependence(int idsite, int iddevice, int checkType, String lang, boolean islogical) throws Exception
    {
        //Radice verifica:Device -> verifica variabili 	DeviceBean device = null;
        
    	DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite, iddevice, lang);
    	int iddevmdl = dev.getIddevmdl();
    	
    	int[] ids_vars = VarphyBeanList.getIdsOfVarsOfDeviceNoHist(lang, idsite, iddevice);

    	VarDependencyState state = new VarDependencyState();

        state = checkVariableDependence(idsite, ids_vars, iddevmdl, checkType, lang, islogical);
        //booklet cabinet
        String sql = "select iddevice  from booklet_cabinet_dev , booklet_cabinet where booklet_cabinet_dev.idcabinet = booklet_cabinet.id";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++) 
        {
        	int iddev = (Integer)rs.get(i).get(0);
        	if(iddev==iddevice){
        		state.setDependence(true);
        	}
        }
        return state;
    }
    
    public static void dltDeviceDependence(int idsite, int iddevice,int checkType, String lang, boolean islogical) throws Exception {
    	
    	DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite, iddevice, lang);
    	int iddevmdl = dev.getIddevmdl();
    	
    	int[] ids_vars = VarphyBeanList.getIdsOfVarsOfDeviceNoHist(lang, idsite, iddevice);
    	dltVariableDependence(idsite, ids_vars, iddevmdl, checkType, lang, islogical);

	}
    
    
    public static boolean haveLineDependence(int idsite, int idline, int checkType, String lang ,UserSession us) throws Exception
    {
        //Radice verifica:Linea -> verifica devices -> verifica variabili
        LineBeanList lines = new LineBeanList();
        DeviceBean[] devices = lines.getDeviceOfLine(idsite, idline, lang);

        VarDependencyState state = new VarDependencyState();
        
        StringBuffer iddev2del = new StringBuffer();
        boolean vardpt = false;
        
        for (int i = 0; i < devices.length; i++)
        {
        	state = checkDeviceDependence(idsite, devices[i].getIddevice(), checkType, lang);
        	String s[] = LineBeanList.getDevInfosOfLineByAddress(idsite, idline,devices[i].getAddress() ,lang);
            if (state.dependsOn() == true)
            {	vardpt = true;
            	iddev2del.append(s[0]+";");
            }
        }
        if(vardpt){
        	LangService lang_s = LangMgr.getInstance().getLangService(lang);
        	String devs2DltStr = lang_s.getString("vardpd", "dev2dlt");
        	String dltconfirmStr  = lang_s.getString("vardpd", "dltconfirm");
        	String result = devs2DltStr+"\n"+dltconfirmStr; 
        	us.setProperty("DevIds2dlt", iddev2del.toString());
        	us.setProperty("control", result);
        }
        return vardpt;
    }   
    
    public static  ArrayList<DeviceBean> getDevDependencesByLine(int idsite, int idline, int checkType, String lang) throws Exception
    {
        LineBeanList lines = new LineBeanList();
        DeviceBean[] devices = lines.getDeviceOfLine(idsite, idline, lang);
        ArrayList<DeviceBean> depdDevList = new ArrayList<DeviceBean>();
        VarDependencyState state = new VarDependencyState();
        for (int i = 0; i < devices.length; i++)
        {
        	state = checkDeviceDependence(idsite, devices[i].getIddevice(), checkType, lang);
            if (state.dependsOn() == true)
            {	
            	depdDevList.add(devices[i]);
            }
        }
        return depdDevList;
    } 
    
    /**
     * General method to remove the device and variable dependences from rule, settings and plugins 
     * @param idsite
     * @param idline
     * @param checkType
     * @param lang
     * @throws Exception
     */
    public static void removeDeviceDependence(int idsite, int iddevice, int checkType, String lang) throws Exception
    {
    	removeDeviceDependence(idsite,iddevice,checkType,lang,false);
    }
    
    /**
     * Specific method to remove the device dependences from rule, settings and plugins 
     * based on the condition the device is logical or not
     * @param idsite
     * @param iddevice
     * @param checkType
     * @param lang
     * @param isLogical
     * @throws Exception
     */
    public static void removeDeviceDependence(int idsite, int iddevice, int checkType, String lang, boolean isLogical) throws Exception
    {  	
    	// TODO : Removing DIRECT DEVICE dependences    	
    	
    	// *** from Floating Suction plugin
    	FSRack[] racks = FSRack.getRacks(lang);
    	if (racks!=null&&racks.length!=0){        		
    		for(int i=0;i<racks.length;i++){
    			// se il rack corrente è nella linea da cancellare devo eliminarlo
    			if (FSManager.isDeviceInRack(iddevice,racks[i].getId_rack())){
    				FSStatus.removeDeviceFromRack(iddevice,racks[i].getId_rack());
    			}
    		}
    	}     	
    	
    	// TODO : removing variable dependences
    }
}
