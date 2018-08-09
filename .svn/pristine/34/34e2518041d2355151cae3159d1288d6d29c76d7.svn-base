package com.carel.supervisor.presentation.bo.helper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.varaggregator.VarAggregator;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.plugin.parameters.dataaccess.Parameter;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBean;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.GraphBean;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bo.BDevDetail;
import com.carel.supervisor.presentation.bo.helper.DeviceImport.DeviceConfigImport;
import com.carel.supervisor.presentation.bo.helper.DeviceImport.GraphConfBlockBean;
import com.carel.supervisor.presentation.helper.SpaceHistoricalHelper;


public class Propagate
{
	public static final String DEVICE = "device";
	public static final String DEVICE_CODE = "devicecode";
	public static final String ALARM = "alarm";
	public static final String ALARM_ABBREVATION = "a";
	public static final String ALARM_ISACTIVE = "active";
	public static final String ALAMR_FREQUENCY = "freq";
	public static final String PRIORITY = "prio";
	public static final String UNITMEASURE = "um";
	public static final String UNITMEASURE_ABBREVATION = "u";
	public static final String HACCP = "haccp";
	public static final String HACCP_ABBREVATION = "h";
	public static final String LOG = "log";
	public static final String LOG_ABBREVATION = "l";
	public static final String LOG_FREQUENCY = "freq";
	public static final String LOG_KEYMAX = "keymax";
	public static final String LOG_DELTA = "delta";
	public static final String DISPLAY = "display";
	public static final String DISPLAY_ABBREVATION = "disp";
	public static final String DISPLAY_TODISPLAY = "todisp";
	public static final String DESCRIPTION = "description";
	public static final String DESCRIPTION_LANG = "lang";
	public static final String DESCRIPTION_LANG_KEY = "key";
	public static final String DESCRIPTION_LANG_DES = "dsc";
	//public static final String DESCRIPTION_LANG_DESCR = "descr";
	public static final String DESCRIPTION_LANG_LONG = "long";
	public static final String DESCRIPTION_LANG_SHORT = "short";
	public static final String GRAPHCONF = "graphconf";
	public static final String GRAPHCONF_PROFILE = "gcprof";
	public static final String GRAPHCONF_ABBREVATION = "gc";
	public static final String GRAPHCONF_ISHACCP = "haccp";
	public static final String GRAPHCONF_COLOR = "color";
	public static final String GRAPHCONF_YMAX = "ymax";
	public static final String GRAPHCONF_YMIN = "ymin";
	public static final String GRAPH = "graphpage";
	public static final String GRAPH_PROFILE = "gpprof";
	public static final String GRAPH_ABBREVATION = "gp";
	public static final String GRAPH_PERIOD = "period";
	public static final String GRAPH_VIEWFINDERCHECK = "viewfck";
	public static final String GRAPH_XGRIDCHECK = "xgridck";
	public static final String GRAPH_YGRIDCHECK = "ygridcck";
	public static final String GRAPH_VIEWFINDERCOLORBG = "viewfrgb";
	public static final String GRAPH_VIEWFINDERCOLORFG = "viewfrfg";
	public static final String GRAPH_GRIDCOLOR = "gridcol";
	public static final String GRAPH_BGGRAPHCOLOR = "bggraphcol";
	public static final String GRAPH_AXISCOLOR = "axiscol";
	public static final String GRAPHCONFBLOCK = "graphblock";
	public static final String GRAPHCONFBLOCK_PROFILE = "gbprof";
	public static final String GRAPHCONFBLOCK_ABBREVATION = "gb";
	public static final String GRAPHCONFBLOCK_LABEL = "lab";
	public static final String CODE = "c";
	
	public static final String LANGUAGECODE = "languagecode";
	public static final String TRUE = "TRUE";
	public static final String PROFILENAME = "profname";
	
	public static final int FIRSTVALUE = -987654321;
    public static void propagateAlarms(int idsite, String username,
        String language, DeviceBean master, DeviceBean slave, PropagateRes res)
        throws DataBaseException, IOException
    {
        int idmaster = master.getIddevice();
        int idslave = slave.getIddevice();
        //verifica spazio su disco per effettuare propagazione allarmi
        if (!canPropagateAlarms(idsite, idmaster, idslave,res).canPropagate())
        {
            //log: propagazione allarmi non avvenuta
            /*EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W042",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
        else
        {
            //tiro su idvariable, frequency, priority, isactive delle variabili del MASTER e dello SLAVE
            String sql = "select idvariable, frequency, priority, isactive from cfvariable where idsite=? and " +
                "iddevice=? and type=4 order by idvarmdl";
            Object[] param = new Object[2];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idmaster);

            RecordSet rs_m = DatabaseMgr.getInstance().executeQuery(null, sql, param); //dati master

            param[1] = new Integer(idslave);

            RecordSet rs_s = DatabaseMgr.getInstance().executeQuery(null, sql, param); //dati slave

            //ciclo su tutte le variabili
            int num = rs_m.size();

            VarphyBeanList alarmlist = new VarphyBeanList();

            int prior_master = -1;
            int freq_master = -1;
            String isactive_master = null;

            int id_slave = -1;

            for (int i = 0; i < num; i++)
            {
                prior_master = ((Integer) (rs_m.get(i).get("priority"))).intValue();
                freq_master = ((Integer) (rs_m.get(i).get("frequency"))).intValue();
                isactive_master = rs_m.get(i).get("isactive").toString().trim();

                id_slave = ((Integer) (rs_s.get(i).get("idvariable"))).intValue();

                //MASTER ATTIVO
                if (isactive_master.equals("TRUE"))
                {
                    alarmlist.updateIsActive(idsite, id_slave, freq_master,
                        prior_master);

                    if (!VarAggregator.existInBuffer(idsite, id_slave)) //se non c'�?in buffer la inserisco
                    {
                        sql = "insert into buffer values (?,?,?,?,?)";
                        param = new Object[5];
                        param[0] = new Integer(idsite);
                        param[1] = new Integer(id_slave);
                        param[2] = new Integer(100);
                        param[3] = new Integer(-1);
                        param[4] = new Boolean(false);
                        DatabaseMgr.getInstance().executeStatement(null, sql, param);
                    }

                    if (VarAggregator.getBufferKeyActual(idsite, id_slave) != -1) //se keyactual !=-1
                    {
                        if (VarAggregator.isVaribleInReorder(idsite, id_slave))
                        {
                            sql = "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                " and idvariable = ?";
                            param = new Object[4];
                            param[0] = new Integer(freq_master);
                            param[1] = new Integer(100);
                            param[2] = new Integer(idsite);
                            param[3] = new Integer(id_slave);
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);
                        }
                        else
                        {
                            sql = "insert into reorder values (?,?,?,?)";
                            param = new Object[4];
                            param[0] = new Integer(idsite);
                            param[1] = new Integer(id_slave);
                            param[2] = new Integer(freq_master);
                            param[3] = new Integer(100);
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);
                        }
                    }
                }

                //MASTER NON ATTIVO
                else
                {
                    if (isVariableActive(idsite, id_slave))
                    {
                        alarmlist.updateIsNotActive(idsite, id_slave);

                        // Modifica per rimuovere della coda del guardiano gli allarmi che non sono pi�?attivi
                        //e che hanno generato un allarme
                        AlarmCtrl.calledOff(new Integer(id_slave));
                    }
                }
            }

            //log corretta propagazione allarmi
            //log: propagazione allarmi non avvenuta

            /*EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W043",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
    }
    public static void propagateAlarms(int idsite, String username,
            String language, DeviceConfigImport importer, DeviceBean slave, PropagateRes res)
            throws DataBaseException, IOException
        {
            int idslave = slave.getIddevice();
            //verifica spazio su disco per effettuare propagazione allarmi
            if (!canPropagateAlarms(idsite, importer, idslave,res).canPropagate())
            {
                //log: propagazione allarmi non avvenuta
                /*EventMgr.getInstance().info(new Integer(idsite), username,
                    "Config", "W042",
                    new Object[] { master.getDescription(), slave.getDescription() });*/
            }
            else
            {
                Map map = importer.getAlarmMap();
                Iterator it = map.entrySet().iterator();
                
                int prior_master = -1;
                int freq_master = -1;
                String isactive_master = null;
                VarphyBeanList alarmlist = new VarphyBeanList();
                String sql = "";
                Object[] param = null;
                while(it.hasNext())
                {
                	Map.Entry<String, VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
                	String code = entry.getKey();
                	VarphyBean var = entry.getValue();
                    prior_master = var.getPriority();
                    freq_master = var.getFrequency();
                    isactive_master = var.isActive()?"TRUE":"FALSE";
                    
                    VarphyBean var_slave = VarphyBeanList.retrieveVarByCode(idsite, code, idslave, language);
                    if(var_slave == null)
                    {
                    	continue;
                    }
                    int id_slave = var_slave.getId();

                    //MASTER ATTIVO
                    if (isactive_master.equals("TRUE"))
                    {
                    	if(var_slave.isActive() == true && var_slave.getFrequency() == freq_master
                    			&& var_slave.getPriority() == prior_master)
                    	{
                    		continue;
                    	}
                    	alarmlist.updateIsActive(idsite, id_slave, freq_master,
                                prior_master);

                        if (!VarAggregator.existInBuffer(idsite, id_slave)) //se non c'�?in buffer la inserisco
                        {
                            sql = "insert into buffer values (?,?,?,?,?)";
                            param = new Object[5];
                            param[0] = new Integer(idsite);
                            param[1] = new Integer(id_slave);
                            param[2] = new Integer(100);
                            param[3] = new Integer(-1);
                            param[4] = new Boolean(false);
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);
                        }

                        if (VarAggregator.getBufferKeyActual(idsite, id_slave) != -1) //se keyactual !=-1
                        {
                            if (VarAggregator.isVaribleInReorder(idsite, id_slave))
                            {
                                sql = "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                    " and idvariable = ?";
                                param = new Object[4];
                                param[0] = new Integer(freq_master);
                                param[1] = new Integer(100);
                                param[2] = new Integer(idsite);
                                param[3] = new Integer(id_slave);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);
                            }
                            else
                            {
                                sql = "insert into reorder values (?,?,?,?)";
                                param = new Object[4];
                                param[0] = new Integer(idsite);
                                param[1] = new Integer(id_slave);
                                param[2] = new Integer(freq_master);
                                param[3] = new Integer(100);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);
                            }
                        }
                    }

                    //MASTER NON ATTIVO
                    else
                    {
                        if (var_slave.isActive() == true)
                        {
                            alarmlist.updateIsNotActive(idsite, id_slave);

                            // Modifica per rimuovere della coda del guardiano gli allarmi che non sono pi�?attivi
                            //e che hanno generato un allarme
                            AlarmCtrl.calledOff(new Integer(id_slave));
                        }
                    }
                }

                //log corretta propagazione allarmi
                //log: propagazione allarmi non avvenuta

                /*EventMgr.getInstance().info(new Integer(idsite), username,
                    "Config", "W043",
                    new Object[] { master.getDescription(), slave.getDescription() });*/
            }
        }
    public static void propagateDescriptions(int idsite, int idprofile,
        String username, DeviceBean master, DeviceBean slave, String language)
        throws DataBaseException
    {
        //retrieve descrizioni e shortdescr delle variabili dal dispositivo master
        String sql =
            "select cfvariable.idvarmdl,cftableext.description, cftableext.shortdescr , cftableext.longdescr from cfvariable,cftableext where cfvariable.idsite=1 and " +
            "cfvariable.iddevice=? and cftableext.idsite=1 and cftableext.tablename='cfvariable' and " +
            "cftableext.tableid=cfvariable.idvariable and cftableext.languagecode=? and cfvariable.idhsvariable is not null";

        Object[] params = new Object[2];
        params[0] = master.getIddevice();
        params[1] = language;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

        Integer[] idvarmdl = new Integer[rs.size()];
        String[] descr = new String[rs.size()];
        String[] shortdescr = new String[rs.size()];
        String[] longdescr = new String[rs.size()];
        
        for (int i = 0; i < rs.size(); i++)
        {
        	idvarmdl[i] = (Integer) rs.get(i).get(0);
        	descr[i] = (rs.get(i).get(1)==null)?"":rs.get(i).get(1).toString().trim();
        	shortdescr[i] = (rs.get(i).get(2)==null)?"":rs.get(i).get(2).toString().trim();
        	longdescr[i] = (rs.get(i).get(3)==null)?"":rs.get(i).get(3).toString().trim();;
        }

        sql = "update cftableext set description=?,shortdescr=?, longdescr = ? , lastupdate = ?  where idsite=1 and tablename='cfvariable' and " +
            "tableid in (select idvariable from cfvariable where idsite=1 and iddevice=? and idvarmdl=? ) and " +
            "languagecode=?";
        params = new Object[7];

        for (int j = 0; j < idvarmdl.length; j++)
        {
            params[0] = descr[j];
            params[1] = shortdescr[j];
            params[2] = longdescr[j];
            params[3] = new Timestamp(System.currentTimeMillis());
            params[4] = slave.getIddevice();
            params[5] = idvarmdl[j];
            params[6] = language;

            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
    }
    public static void propagateDescriptions(int idsite, int idprofile,
            String username, DeviceConfigImport importer, DeviceBean slave, String language)
            throws DataBaseException
        {
             Map map = importer.getNologMap();     
            Iterator it = map.entrySet().iterator();
            String sql = "update cftableext set description=?,shortdescr=?,longdescr=?,lastupdate = ? where idsite=1 and tablename='cfvariable' and " +
            "tableid in (select idvariable from cfvariable where idsite=1 and iddevice=? and code=? ) and " +
            "languagecode=?";
            String sql2 = "update cftableext set description=?,shortdescr=?,lastupdate = ? where idsite=1 and tablename='cfvariable' and " +
            "tableid in (select idvariable from cfvariable where idsite=1 and iddevice=? and code=? ) and " +
            "languagecode=?";
            
            while(it.hasNext())
            {
            	Map.Entry<String, VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
            	String key = entry.getKey();
            	VarphyBean var = entry.getValue();
            	Object[] params = null;
            	if(var.getLongDescription()==null){
            		params = new Object[6];
            	}else{
            		params = new Object[7];
            	}
                params[0] = var.getShortDescription();
                params[1] = var.getShortDesc();
                int i = 1;       // versions lower than 2.0.3 do not have longdescr attribute.
                if(var.getLongDescription()!=null){
                	i++;
                	params[i] = var.getLongDescription();
                }
                params[++i] = new Timestamp(System.currentTimeMillis());
                params[++i] = slave.getIddevice();
                params[++i] = key;
                params[++i] = language;
                DatabaseMgr.getInstance().executeStatement(null, var.getLongDescription()==null?sql2:sql,params);// versions lower than 2.0.3 do not have longdescr attribute.
            }
        }

    public static void propagateUnitMeasurement(int idsite, int idprofile,
        String username, DeviceBean master, DeviceBean slave, String language)
        throws DataBaseException
    {
        //retrieve um delle variabili dal dispositivo master
        String sql =
            "select idvarmdl,measureunit from cfvariable where cfvariable.idsite=1 and " +
            "cfvariable.iddevice=? and cfvariable.idhsvariable is not null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { (master.getIddevice()) });

        Integer[] idvarmdl = new Integer[rs.size()];
        String[] um = new String[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            idvarmdl[i] = (Integer) rs.get(i).get(0);
            um[i] = rs.get(i).get(1).toString().trim();
        }

        sql = "update cfvariable set measureunit=?,lastupdate = ? where idsite=1 and idvariable in " +
            "(select idvariable from cfvariable where idsite=1 and iddevice=? and idvarmdl=? )";//and idhsvariable is not null

        Object[] params = new Object[4];

        for (int j = 0; j < idvarmdl.length; j++)
        {
            params[0] = um[j];
            params[1] = new Timestamp(System.currentTimeMillis());
            params[2] = slave.getIddevice();
            params[3] = idvarmdl[j];
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
    }
    public static void propagateUnitMeasurement(int idsite, int idprofile,
            String username, DeviceConfigImport importer, DeviceBean slave, String language)
            throws DataBaseException
        {
            Map map = importer.getNologMap();
            Iterator it = map.entrySet().iterator();
            String sql = "update cfvariable set measureunit=?,lastupdate = ? where idsite=1 and idvariable in " +
            "(select idvariable from cfvariable where idsite=1 and iscancelled ='FALSE' and iddevice=? and code=? )";//and idhsvariable is not null
            Object[] params = new Object[4];
            while(it.hasNext())
            {
            	Map.Entry<String, VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
            	String key = entry.getKey();
            	VarphyBean var = entry.getValue();
            	if(var.getMeasureUnit() == null)
            	{
            		continue;
            	}
                params[0] = var.getMeasureUnit();
                params[1] = new Timestamp(System.currentTimeMillis());
                params[2] = slave.getIddevice();
                params[3] = key;
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
            }
        }

    public static void propagateHACCP(int idsite, int idprofile,
        String username, DeviceBean master, DeviceBean slave, String language,PropagateRes res)
        throws DataBaseException, IOException
    {
        int idmaster = master.getIddevice();
        int idslave = slave.getIddevice();

        if (!canPropagateHaccp(idsite, idmaster, idslave,res).canPropagate())
        {
            /*//log propagazione non avvenuta
            EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W044",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
        else
        {
            /*####################
            ##PROPAGAZIONE HACCP##
            ####################*/
            String sql =
                "select idvariable, ishaccp from cfvariable where idsite=? and " +
                "iddevice=? and type!=4 and iscancelled ='FALSE' and idhsvariable is not null order by idvarmdl";
            Object[] param = new Object[2];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idmaster);

            RecordSet rs_m = DatabaseMgr.getInstance().executeQuery(null, sql,
                    param); //dati master

            param[1] = new Integer(idslave);

            RecordSet rs_s = DatabaseMgr.getInstance().executeQuery(null, sql,
                    param); //dati slave

            int num = rs_m.size();

            //Campi su cui ciclo per ogni variabile
            String m_ishaccp = null; //ishaccp x master
            String s_ishaccp = null; //ishaccp x slave

            int id_var_slave = -1;
            List<Integer> var_graph_remove = new ArrayList<Integer>();

            for (int i = 0; i < num; i++)
            {
                m_ishaccp = rs_m.get(i).get("ishaccp").toString().trim();
                id_var_slave = new Integer(rs_s.get(i).get("idvariable")
                                               .toString()).intValue();
                s_ishaccp = rs_s.get(i).get("ishaccp").toString().trim();

                if (m_ishaccp.equalsIgnoreCase("FALSE") &&
                        s_ishaccp.equalsIgnoreCase("TRUE")) //RIMOZIONE HACCP
                {
                    VarAggregator.updateIsHACCP(idsite, id_var_slave, "FALSE");
                    var_graph_remove.add(new Integer(id_var_slave));
                    GraphVariable.removeVariableGraphInfo(idsite, idprofile,
                        idslave, id_var_slave, "TRUE");
                }
                else if (m_ishaccp.equalsIgnoreCase("TRUE") &&
                        s_ishaccp.equalsIgnoreCase("FALSE")) //AGGIUNTA HACCP
                {
                    VarAggregator.updateIsHACCP(idsite, id_var_slave, "TRUE");

                    if (!VarAggregator.existInBuffer(idsite, id_var_slave)) //se non �?presente in buffer, faccio l'insert
                    {
                        sql = "insert into buffer values (?,?,?,?,?)";
                        param = new Object[5];
                        param[0] = new Integer(idsite);
                        param[1] = new Integer(id_var_slave);
                        param[2] = new Integer(BDevDetail.HACCP_ROWSHYSTORICAL);
                        param[3] = new Integer(-1);
                        param[4] = new Boolean(false);
                        DatabaseMgr.getInstance().executeStatement(null, sql,
                            param);

                        GraphVariable.insertVariableGraphInfo(idsite,
                            id_var_slave, idslave, "TRUE", idprofile);
                    }
                }
            }

            //rimozione da tabelle grafici
            GraphVariable.deleteVarGroupCfPageGraph(idsite, idprofile, idslave,
                var_graph_remove);
            GraphVariable.reorderCfPageGrah(idsite, idprofile, idslave);
        }
    }
    public static void propagateHACCP(int idsite, int idprofile,
            String username, DeviceConfigImport importer, DeviceBean slave, String language,PropagateRes res)
            throws DataBaseException, IOException
        {
            int idslave = slave.getIddevice();

            if (!canPropagateHaccp(idsite, importer, idslave,res).canPropagate())
            {
                /*//log propagazione non avvenuta
                EventMgr.getInstance().info(new Integer(idsite), username,
                    "Config", "W044",
                    new Object[] { master.getDescription(), slave.getDescription() });*/
            }
            else
            {
                /*####################
                ##PROPAGAZIONE HACCP##
                ####################*/
            	Map nolog = importer.getNologMap();
                Map haccp = importer.getHaccpMap();
                String sql =
                    "select idvariable,code, ishaccp from cfvariable where idsite=? and " +
                    "iddevice=? and type!=4 and iscancelled ='FALSE' and idhsvariable is not null order by idvarmdl";
                Object[] param = new Object[2];
                param[0] = new Integer(idsite);
                param[1] = new Integer(idslave);

                RecordSet rs_slave = DatabaseMgr.getInstance().executeQuery(null, sql,
                        param); //dati master
                int num = rs_slave.size();
                
                //Campi su cui ciclo per ogni variabile
                String code = null;
                String s_ishaccp = null; //ishaccp x slave

                int id_var_slave = -1;
                List<Integer> var_graph_remove = new ArrayList<Integer>();
                
                for(int i=0;i<num;i++)
                {
                	code = rs_slave.get(i).get("code").toString().trim();
                	s_ishaccp = rs_slave.get(i).get("ishaccp").toString().trim();
                	id_var_slave = new Integer(rs_slave.get(i).get("idvariable")
                            .toString()).intValue();
                	if(nolog.get(code) != null)
                	{
                		VarphyBean var = (VarphyBean)haccp.get(code);
                		if(var != null)
                		{
                			//master yes, slave no
                			//add
                			if("FALSE".equals(s_ishaccp))
                			{
                				 VarAggregator.updateIsHACCP(idsite, id_var_slave, "TRUE");

                                 if (!VarAggregator.existInBuffer(idsite, id_var_slave)) //se non �?presente in buffer, faccio l'insert
                                 {
                                     sql = "insert into buffer values (?,?,?,?,?)";
                                     param = new Object[5];
                                     param[0] = new Integer(idsite);
                                     param[1] = new Integer(id_var_slave);
                                     param[2] = new Integer(BDevDetail.HACCP_ROWSHYSTORICAL);
                                     param[3] = new Integer(-1);
                                     param[4] = new Boolean(false);
                                     DatabaseMgr.getInstance().executeStatement(null, sql,
                                         param);

                                     GraphVariable.insertVariableGraphInfo(idsite,id_var_slave, idslave, "TRUE", idprofile);
                                 }
                			}
                		}
                		else
                		{
                			//master no, slave yes
                			//remove
                			if("TRUE".equals(s_ishaccp))
                			{
                				VarAggregator.updateIsHACCP(idsite, id_var_slave, "FALSE");
                                var_graph_remove.add(new Integer(id_var_slave));
                                GraphVariable.removeVariableGraphInfo(idsite,idslave, id_var_slave, "TRUE");
                			}
                		}
                	}
                }

                //rimozione da tabelle grafici
                GraphVariable.deleteVarGroupCfPageGraph(idsite, idprofile, idslave,
                    var_graph_remove);
                GraphVariable.reorderCfPageGrah(idsite, idprofile, idslave);
            }
        }

    public static void propagateHistorical(int idsite, int idprofile,
        String username, DeviceBean master, DeviceBean slave, String language, PropagateRes res)
        throws Exception
    {
        String sql = "";
        Object[] param = null;

        int idmaster = master.getIddevice();
        int idslave = slave.getIddevice();

        if (!canPropagateHistorical(idsite, idmaster, idslave,res).canPropagate())
        {
            /*//log propagazione non avvenuta
            EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W044",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
        else
        {
            /*######################
            ##PROPAGAZIONE STORICO##
            ######################*/
            VarAggregator varlist_master = new VarAggregator(idsite, language,
                    idmaster, true);
            VarAggregator varlist_slave = new VarAggregator(idsite, language,
                    idslave, true);
            
            VarphyBean[] vars_master = varlist_master.getVarList();
            VarphyBean[] vars_slave = varlist_slave.getVarList();

            VarphyBean master_var = null;
            VarphyBean slave_var = null;

            Map<Integer,VarphyBean> var_by_mdl = new HashMap<Integer,VarphyBean>(); //creo mappa con variabili recuperabili x idvarmdl

            for (int i = 0; i < vars_slave.length; i++)
            {
                var_by_mdl.put(vars_master[i].getIdMdl(), vars_slave[i]);
            }

            List<Integer> var_graph_remove = new ArrayList<Integer>();

            for (int i = 0; i < vars_master.length; i++) //scorro le var del master 
            {
                master_var = vars_master[i]; //variabile i del dispositivo master
                slave_var = (VarphyBean) var_by_mdl.get(master_var.getIdMdl()); //variabile i del dispositivo slave

                //SE MASTER HA STORICO
                if (master_var.getIdhsvariable().intValue() != -1)
                {
                    //SE SLAVE NON HA STORICO, LO AGGIUNGO    
                    if (slave_var.getIdhsvariable().intValue() == -1)
                    {
                        VarphyBean master_histor = master_var.getSon(); // storico master
                        VarphyBean new_hist = new VarphyBean(slave_var); //creo storico per slave

                        new_hist.setIdhsvariable(null);
                        new_hist.setFrequency(master_histor.getFrequency());

                        if (master_histor.getType() != 1)
                        {
                            new_hist.setVariation(master_histor.getVariation());
                        }
                        else
                        {
                            new_hist.setVariation(1); //variazione per variabile digitale
                        }

                        new_hist.setDelay(master_histor.getDelay());
                        new_hist.setActive(true);
                        new_hist.setGrpcategory(master_histor.getGrpcategory());
                        new_hist.setInsertTime(new Timestamp(
                                System.currentTimeMillis()));
                        new_hist.setLastUpdate(new Timestamp(
                                System.currentTimeMillis()));
                        new_hist.setHaccp(false);

                        int id_new_hist = new_hist.save();

                        //inserimento buffer e hsfrequency
                        sql = "insert into buffer values (?,?,?,?,?)";
                        param = new Object[5];
                        param[0] = new_hist.getSite();
                        param[1] = new Integer(id_new_hist);

                        param[2] = new Short(VarAggregator.getKeyMax(idsite,
                                    master_histor.getId().intValue(),
                                    master_histor.getFrequency().intValue()));

                        param[3] = new Integer(-1);
                        param[4] = new Boolean(false);
                        DatabaseMgr.getInstance().executeStatement(null, sql,
                            param);

                        LangUsedBeanList langlist = new LangUsedBeanList();
                        LangUsedBean[] langs = langlist.retrieveAllLanguage(idsite);

                        for (int j = 0; j < langs.length; j++)
                        {
                            String tempdesc = TableExtBean.retrieveDescritionFromTableById(idsite,
                                    langs[j].getLangcode(),
                                    master_histor.getId().intValue(),
                                    "cfvariable");
                            TableExtBean.insertTableExt(idsite,
                                langs[j].getLangcode(), "cfvariable", tempdesc,
                                id_new_hist);
                        }

                        //associo a madre slave nuovo figlio slave appena creato
                        VarAggregator.updateIdHsVariable(idsite,
                            slave_var.getId().intValue(), id_new_hist);

                        //insert variabile default per grafico storico
                        GraphVariable.insertVariableGraphInfo(idsite,
                            id_new_hist, idslave, "FALSE", idprofile);
                    }

                    //SE SLAVE HA STORICO FACCIO L'UPDATE   
                    else
                    {
                        VarphyBean master_histor = master_var.getSon(); //storico master
                        VarphyBean slave_histor = slave_var.getSon(); //storico slave

                        if (master_histor.getType() != 1)
                        {
                            VarAggregator.updateFreqAndVarMin(idsite,
                                slave_histor.getId().intValue(),
                                master_histor.getFrequency().intValue(),
                                master_histor.getVariation());
                        }
                        else
                        {
                            VarAggregator.updateFrequency(idsite,
                                slave_histor.getId().intValue(),
                                master_histor.getFrequency().intValue());
                        }

                        if (VarAggregator.getBufferKeyActual(idsite,
                                    slave_histor.getId().intValue()) != -1) //se keyactual !=-1
                        {
                            if (VarAggregator.isVaribleInReorder(idsite,
                                        slave_histor.getId().intValue()))
                            {
                                sql = "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                    " and idvariable = ?";
                                param = new Object[4];
                                param[0] = master_histor.getFrequency();
                                param[1] = VarAggregator.getPeriod(1,master_histor.getId(),master_histor.getFrequency());
                                param[2] = new Integer(idsite);
                                param[3] = slave_histor.getId();
                                DatabaseMgr.getInstance().executeStatement(null,
                                    sql, param);
                            }
                            else
                            {
                                sql = "insert into reorder values (?,?,?,?)";
                                param = new Object[4];
                                param[0] = new Integer(idsite);
                                param[1] = slave_histor.getId();
                                param[2] = master_histor.getFrequency();
                                /*
                                param[3] = getProfTime(master_histor.getKeymax(),
                                        master_histor.getFrequency().intValue(),
                                        master_histor.getType());*/
                                param[3] = VarAggregator.getPeriod(1,master_histor.getId(),master_histor.getFrequency());
                                DatabaseMgr.getInstance().executeStatement(null,
                                    sql, param);
                            }
                        }
                        else //update buffer e frequency
                        {
                            sql = "update buffer set keymax = ? where idsite = ? and idvariable = ?";
                            param = new Object[3];
                            param[0] = new Short(VarAggregator.getKeyMax(
                                        idsite,
                                        master_histor.getId().intValue(),
                                        master_histor.getFrequency().intValue()));
                            param[1] = new Integer(idsite);
                            param[2] = slave_histor.getId();
                            DatabaseMgr.getInstance().executeStatement(null,
                                sql, param);
                        }
                    }
                }

                //SE MASTER NON HA STORICO
                else
                {
                    //SE SLAVE HA STORICO DEVO ELIMINARLO   
                    if (slave_var.getIdhsvariable().intValue() != -1)
                    {
                        VarAggregator.cancelHsVariable(idsite,
                            slave_var.getId().intValue(),
                            slave_var.getIdhsvariable().intValue());

                        //rimozione info grafico
                        var_graph_remove.add(slave_var.getIdhsvariable());
                        GraphVariable.removeVariableGraphInfo(idsite,
                            idprofile, idslave,
                            slave_var.getIdhsvariable().intValue(), "FALSE");
                    }
                }
            }

            // rimozione tabelle grafici
            GraphVariable.deleteVarGroupCfPageGraph(idsite, idprofile, idslave,
                var_graph_remove);
            GraphVariable.reorderCfPageGrah(idsite, idprofile, idslave);

            /*// log propagazione avvenuta
            EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W045",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
    }
    
    public static void propagateHistorical(int idsite, int idprofile,
            String username, DeviceConfigImport importer, DeviceBean slave, String language, PropagateRes res)
            throws Exception
    {
        String sql = "";
        Object[] param = null;

        int idslave = slave.getIddevice();

        if (!canPropagateHistorical(idsite, importer, idslave,res).canPropagate())
        {
            /*//log propagazione non avvenuta
            EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W044",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
        else
        {
            /*######################
            ##PROPAGAZIONE STORICO##
            ######################*/
        	LangUsedBeanList langlist = new LangUsedBeanList();
            LangUsedBean[] langs = langlist.retrieveAllLanguage(idsite);
            
        	Map nolog = importer.getNologMap();
            Map log = importer.getLogMap();
            sql =
                "select * from cfvariable where idsite=? and " +
                "iddevice=? and type!=4 and iscancelled ='FALSE' and idhsvariable is not null order by idvarmdl";
            param = new Object[2];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idslave);

            RecordSet rs_slave = DatabaseMgr.getInstance().executeQuery(null, sql,
                    param); //dati master
            int num = rs_slave.size();

            List<Integer> var_graph_remove = new ArrayList<Integer>();
            String code = null;
            int s_idhsvariable = -1;
            int id_var_slave = -1;
            int type = 0;
            for (int i = 0; i < num; i++) //scorro le var del master 
            {
            	code = rs_slave.get(i).get(VarphyBean.CODE).toString().trim();
                s_idhsvariable = (Integer)rs_slave.get(i).get(VarphyBean.IDHSVARIABLE);
                id_var_slave = (Integer)rs_slave.get(i).get(VarphyBean.ID);
                type = (Integer)rs_slave.get(i).get(VarphyBean.TYPE);
                if(nolog.containsKey(code))
                {
                	VarphyBean var_master = (VarphyBean)log.get(code);
                	if(var_master != null)
                	{
                		//master yes, slave no
                		if(s_idhsvariable == -1)
                		{
                			VarphyBean slave_var = VarphyBeanList.retrieveVarById(idsite, id_var_slave, language);
                			VarphyBean new_hist = new VarphyBean(slave_var); //creo storico per slave

                            new_hist.setIdhsvariable(null);
                            new_hist.setFrequency(var_master.getFrequency());                       
                            if (type != 1)
                            {
                                new_hist.setVariation(var_master.getVariation());
                            }
                            else
                            {
                                new_hist.setVariation(1); //variazione per variabile digitale
                            }
                            new_hist.setActive(true);
                            new_hist.setInsertTime(new Timestamp(
                                    System.currentTimeMillis()));
                            new_hist.setLastUpdate(new Timestamp(
                                    System.currentTimeMillis()));
                            new_hist.setHaccp(false);

                            int id_new_hist = new_hist.save();

                            //inserimento buffer e hsfrequency
                            sql = "insert into buffer values (?,?,?,?,?)";
                            param = new Object[5];
                            param[0] = new_hist.getSite();
                            param[1] = new Integer(id_new_hist);

                            param[2] = new Short(var_master.getKeymax());

                            param[3] = new Integer(-1);
                            param[4] = new Boolean(false);
                            DatabaseMgr.getInstance().executeStatement(null, sql,
                                param);

                            for (int j = 0; j < langs.length; j++)
                            {
                                String tempdesc = TableExtBean.retrieveDescritionFromTableById(idsite,
                                        langs[j].getLangcode(),
                                        id_var_slave,
                                        "cfvariable");
                                TableExtBean.insertTableExt(idsite,
                                    langs[j].getLangcode(), "cfvariable", tempdesc,
                                    id_new_hist);
                            }

                            //associo a madre slave nuovo figlio slave appena creato
                            VarAggregator.updateIdHsVariable(idsite,
                                slave_var.getId().intValue(), id_new_hist);

                            //insert variabile default per grafico storico
                            GraphVariable.insertVariableGraphInfo(idsite,
                                id_new_hist, idslave, "FALSE", idprofile);	
                		}
                		else
                		{
                            VarphyBean slave_histor = VarphyBeanList.retrieveVarById(idsite, s_idhsvariable, language);

                            if (var_master.getType() != 1)
                            {
                                VarAggregator.updateFreqAndVarMin(idsite,
                                    slave_histor.getId().intValue(),
                                    var_master.getFrequency().intValue(),
                                    var_master.getVariation());
                            }
                            else
                            {
                                VarAggregator.updateFrequency(idsite,
                                    slave_histor.getId().intValue(),
                                    var_master.getFrequency().intValue());
                            }

                            if (VarAggregator.getBufferKeyActual(idsite,
                                        slave_histor.getId().intValue()) != -1) //se keyactual !=-1
                            {
                                if (VarAggregator.isVaribleInReorder(idsite,
                                            slave_histor.getId().intValue()))
                                {
                                    sql = "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                        " and idvariable = ?";
                                    param = new Object[4];
                                    param[0] = var_master.getFrequency();
                                    param[1] = VarAggregator.getPeriod(var_master.getKeymax(),var_master.getFrequency());
                                    param[2] = new Integer(idsite);
                                    param[3] = slave_histor.getId();
                                    DatabaseMgr.getInstance().executeStatement(null,
                                        sql, param);
                                }
                                else
                                {
                                    sql = "insert into reorder values (?,?,?,?)";
                                    param = new Object[4];
                                    param[0] = new Integer(idsite);
                                    param[1] = slave_histor.getId();
                                    param[2] = var_master.getFrequency();
                                    /*
                                    param[3] = getProfTime(master_histor.getKeymax(),
                                            master_histor.getFrequency().intValue(),
                                            master_histor.getType());*/
                                    param[3] = VarAggregator.getPeriod(var_master.getKeymax(),var_master.getFrequency());
                                    DatabaseMgr.getInstance().executeStatement(null,
                                        sql, param);
                                }
                            }
                            else //update buffer e frequency
                            {
                                sql = "update buffer set keymax = ? where idsite = ? and idvariable = ?";
                                param = new Object[3];
                                param[0] = new Short(var_master.getKeymax());
                                param[1] = new Integer(idsite);
                                param[2] = slave_histor.getId();
                                DatabaseMgr.getInstance().executeStatement(null,
                                    sql, param);
                            }
                		}
                	}
                	else
                    {
                         //MASTER no, slave yes   
                         if (s_idhsvariable != -1)
                         {
                             VarAggregator.cancelHsVariable(idsite,
                             	id_var_slave,
                             	s_idhsvariable);

                             //rimozione info grafico
                             var_graph_remove.add(s_idhsvariable);
                             GraphVariable.removeVariableGraphInfo(idsite,
                                 idprofile, idslave,
                                 s_idhsvariable, "FALSE");
                         }
                    }
                }
            }

            // rimozione tabelle grafici
            GraphVariable.deleteVarGroupCfPageGraph(idsite, idprofile, idslave,
                var_graph_remove);
            GraphVariable.reorderCfPageGrah(idsite, idprofile, idslave);

            /*// log propagazione avvenuta
            EventMgr.getInstance().info(new Integer(idsite), username,
                "Config", "W045",
                new Object[] { master.getDescription(), slave.getDescription() });*/
        }
    }
    public static void propagateVariableVisualization(int idsite,
        int idprofile, String username, DeviceBean master, DeviceBean slave,
        String language) throws DataBaseException
    {
        //	retrieve um delle variabili dal dispositivo master
        String sql =
            "select idvarmdl,priority,todisplay from cfvariable where cfvariable.idsite=1 and " +
            "cfvariable.iddevice=? and cfvariable.idhsvariable is not null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { (master.getIddevice()) });

        Integer[] idvarmdl = new Integer[rs.size()];
        Integer[] priority = new Integer[rs.size()];
        String[] todisplay = new String[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            idvarmdl[i] = (Integer) rs.get(i).get(0);
            priority[i] = (Integer) rs.get(i).get(1);
            todisplay[i] = rs.get(i).get(2).toString().trim();
        }

        sql = "update cfvariable set priority=?,todisplay=?,lastupdate = ?  where idsite=1 and idvariable in " +
            "(select idvariable from cfvariable where idsite=1 and iddevice=? and idvarmdl=?)";// and idhsvariable is not null

        Object[] params = new Object[5];

        for (int j = 0; j < idvarmdl.length; j++)
        {
            params[0] = priority[j];
            params[1] = todisplay[j];
            params[2] = new Timestamp(System.currentTimeMillis());
            params[3] = slave.getIddevice();
            params[4] = idvarmdl[j];
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
        }
    }
    public static void propagateVariableVisualization(int idsite,
            int idprofile, String username, DeviceConfigImport importer, DeviceBean slave,
            String language) throws DataBaseException
        {
            
    		Iterator it = importer.getNologMap().entrySet().iterator();
    		while(it.hasNext())
    		{
    			Map.Entry<String, VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
    			VarphyBean var = entry.getValue();
    			if(var.getDisplay() == null )//alarm
    			{
    				continue;
    			}
	            String sql = "update cfvariable set priority=?,todisplay=?,lastupdate = ? where idsite=1 and idvariable in " +
	                "(select idvariable from cfvariable where idsite=1 and iddevice=? and code=?)";// and idhsvariable is not null
	
	            Object[] params = new Object[5];
	            params[0] = var.getPriority();
	            params[1] = var.getDisplay();
	            params[2] = new Timestamp(System.currentTimeMillis());
	            params[3] = slave.getIddevice();
	            params[4] = var.getCode();
	            DatabaseMgr.getInstance().executeStatement(null, sql, params);
    		}
        }

    public static void propagateGraphConf(int idsite, int idprofile, String username, String language, DeviceBean master, DeviceBean slave) throws Exception
    {
        String msg = "";
    	
    	try
        {
			propagateIntoCfGraphConf(idsite, idprofile, language,
			    master.getIddevice(), slave.getIddevice());
		}
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(Propagate.class);
            logger.error(e);
            
            msg = msg + e.getMessage();
		}

        try
        {
			propagateIntoCfPageGraph(idsite, idprofile, language,
			    master.getIddevice(), slave.getIddevice());
		}
        catch (Exception e1)
        {
        	Logger logger = LoggerMgr.getLogger(Propagate.class);
            logger.error(e1);
            
            msg = msg + (msg.equals("")?"":" and ") + e1.getMessage();
		}

        if (!msg.equals("")) //ERRORE
        {
        	EventMgr.getInstance().info(new Integer(idsite), username, "Config", "W071",
        			new Object[] { "graph configuration", master.getDescription(), slave.getDescription() });
        	
        	throw new DataBaseException(msg);
        }
        else //OK
        {
        	EventMgr.getInstance().info(new Integer(idsite), username, "Config", "W046",
        			new Object[] { master.getDescription(), slave.getDescription() });
        }
        
    }
    public static void propagateGraphConf(int idsite, int idprofile[], String username, String language, DeviceConfigImport importer, DeviceBean slave,ProfileBeanList profiles) throws Exception
    {
        String msg = "";
    	
    	try
        {
			propagateIntoCfGraphConf(idsite, idprofile, language,
			   importer, slave.getIddevice(),profiles);
		}
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(Propagate.class);
            logger.error(e);
            
            msg = msg + e.getMessage();
		}

        try
        {
			propagateIntoCfPageGraph(idsite, idprofile, language,
			    importer, slave.getIddevice(),profiles);
		}
        catch (Exception e1)
        {
        	Logger logger = LoggerMgr.getLogger(Propagate.class);
            logger.error(e1);
            
            msg = msg + (msg.equals("")?"":" and ") + e1.getMessage();
		}

        if (!msg.equals("")) //ERRORE
        {
        	EventMgr.getInstance().info(new Integer(idsite), username, "Config", "W071",
        			new Object[] { "graph configuration", importer.getDevMdl().getCode(), slave.getDescription() });
        	
        	throw new DataBaseException(msg);
        }
        else //OK
        {
        	EventMgr.getInstance().info(new Integer(idsite), username, "Config", "W046",
        			new Object[] { importer.getDevMdl().getCode(), slave.getDescription() });
        }
        
    }

    public static void propagateImages(int idsite, int idprofile,
        String username, DeviceBean master, DeviceBean slave, String language)
        throws DataBaseException
    {
        //    	retrieve um delle variabili dal dispositivo master
        String sql = "select imagepath from cfdevice where idsite=1 and iddevice=?";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { (master.getIddevice()) });

        String imagepath = rs.get(0).get(0).toString();

        sql = "update cfdevice set imagepath=? where idsite=1 and iddevice=?";

        Object[] params = new Object[2];

        params[0] = imagepath;
        params[1] = slave.getIddevice();
        DatabaseMgr.getInstance().executeStatement(null, sql, params);
    }

    private static void propagateIntoCfGraphConf(int idsite, int idprofile,
        String language, int id_master, int id_slave) throws Exception
    {
        //pulizia variabili slave 
        String sql = "delete from cfgraphconf where idsite=? and iddevice=? and idprofile=?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_slave);
        param[2] = new Integer(idprofile);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        /*
         * Remove block label by variable
         * Reuse previous query substituting just table name  
         */
        sql = sql.replaceFirst("cfgraphconf", "cfgraphconfblock");
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        // End
        
        sql = "select * from cfgraphconf where idsite=? and iddevice=? and idprofile=?";
        String sqlBck = "select * from cfgraphconfblock where idsite=? and iddevice=? and idprofile=?";

        param[1] = new Integer(id_master);
        param[2] = new Integer(idprofile);

        //record nella cfpagegraph per il dispositivo master
        RecordSet rs_master = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        
        /*
         * Block labels
         * Preapare data
         */
        Map blockMaps = new HashMap();
        Integer idSite = null;
        Integer idVar  = null;
        String bLabel = null;
        List labelList = null;
        
        RecordSet rs_master_block = DatabaseMgr.getInstance().executeQuery(null, sqlBck, param);
        if(rs_master_block != null)
        {
        	Record rb = null;
        	for(int i=0; i<rs_master_block.size(); i++)
        	{
        		rb = rs_master_block.get(i);
        		
        		idSite = (Integer)rb.get("idsite");
        		idVar  = (Integer)rb.get("idvariable");
        		bLabel = UtilBean.trim(rb.get("blocklabel"));
        		
        		labelList = (List)blockMaps.get(idSite+"_"+idVar);
        		if(labelList == null)
        			labelList = new ArrayList();
        		
        		labelList.add(bLabel);
        		blockMaps.put(idSite+"_"+idVar, labelList);
        	}
        }
        // End

        int num_var = rs_master.size();

        int idvar = -1;
        int idvarmdl = -1;
        int idvar_slave = -1;
        String ishaccp = null;
        Record tmp = null;

        String sql_find_idvarslave = "select idvariable,idhsvariable from cfvariable where idsite=? and iddevice=? and idvarmdl=? and idhsvariable is not null";
        String sql_insert_new_row_slave = "insert into cfgraphconf values (?,?,?,?,?,?,?,?,?);";
        String sql_find_varmdl = "select idvarmdl from cfvariable where idsite=? and idvariable=?";
        String sql_insert_new_row_slave_block = "insert into cfgraphconfblock values (?,?,?,?,?,?,?);"; 
        
        Object[] values_find = new Object[3];
        Object[] values_insert = new Object[9];
        Object[] values_insert_block = new Object[7];

        for (int i = 0; i < num_var; i++)
        {
        	// Clear
        	values_insert_block = new Object[7];
        	
            tmp = rs_master.get(i);
            idvar = Integer.parseInt(tmp.get(5).toString().trim()); //idvariable master
            ishaccp = tmp.get(4).toString().trim(); //ishaccp 

            RecordSet rs_varmdl = DatabaseMgr.getInstance().executeQuery(null,
                    sql_find_varmdl,
                    new Object[] { new Integer(idsite), new Integer(idvar) });
            idvarmdl = Integer.parseInt(rs_varmdl.get(0).get(0).toString());

            values_find[0] = new Integer(idsite);
            values_find[1] = new Integer(id_slave);
            values_find[2] = new Integer(idvarmdl);
            
            RecordSet recordset_slave = DatabaseMgr.getInstance().executeQuery(null,
                    sql_find_idvarslave, values_find);

            if (ishaccp.equalsIgnoreCase("TRUE"))
            {
                idvar_slave = Integer.parseInt(recordset_slave.get(0).get(0)
                                                              .toString());
            }
            else
            {
                idvar_slave = Integer.parseInt(recordset_slave.get(0).get(1)
                                                              .toString());
            }

            values_insert[0] = new Integer(idsite);
            values_insert[1] = new Integer(idprofile);
            values_insert[2] = null;
            values_insert[3] = new Integer(id_slave);
            values_insert[4] = ishaccp;
            values_insert[5] = new Integer(idvar_slave);
            values_insert[6] = tmp.get(6).toString().trim();
            values_insert[7] = (((Float) tmp.get(7)).floatValue() == 0) ? null
                                                                        : tmp.get(7);
            values_insert[8] = (((Float) tmp.get(8)).floatValue() == 0) ? null
                                                                        : tmp.get(8);
            DatabaseMgr.getInstance().executeStatement(null,sql_insert_new_row_slave, values_insert);
            
            // Insert block configuration
            if (ishaccp.equalsIgnoreCase("FALSE"))
            {
            	 values_insert_block[0] = new Integer(idsite);
                 values_insert_block[1] = new Integer(idprofile);
                 values_insert_block[2] = null;
                 values_insert_block[3] = new Integer(id_slave);
                 values_insert_block[4] = "FALSE";
                 values_insert_block[5] = new Integer(idvar_slave);
                 
            	List l = (ArrayList)blockMaps.get(idsite+"_"+idvar);
            	if(l != null)
            	{
            		for (int j=0; j<l.size(); j++) 
            		{
            			values_insert_block[6] = (String)l.get(j);
            			DatabaseMgr.getInstance().executeStatement(null,sql_insert_new_row_slave_block, values_insert_block);
            			
					} 
            	}
            }
            // End
        }
    }
    private static void propagateIntoCfGraphConf(int idsite, int[] idprofile,
            String language, DeviceConfigImport importer, int id_slave,ProfileBeanList profiles) throws Exception
        {
            //pulizia variabili slave 
    		if (idprofile.length == 0)
    			return;
            String sql = "delete from cfgraphconf where idsite=? and iddevice=? and idprofile in (";
            for(int i=0;i<idprofile.length;i++)
            {
            	if(i<idprofile.length-1)
            	{
            		sql += "?,";
            	}
            	else
            	{
            		sql += "?)";
            	}
            }
            Object[] param = new Object[2+idprofile.length];
            param[0] = new Integer(idsite);
            param[1] = new Integer(id_slave);
            for(int i=0;i<idprofile.length;i++)
            {
            	param[2+i] = new Integer(idprofile[i]);
            }
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
            sql = sql.replaceFirst("cfgraphconf", "cfgraphconfblock");
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
            // End
            
            String sql_find_idvarslave = "select * from cfvariable where idsite=? and iddevice=? and iscancelled='FALSE' and (ishaccp='TRUE' or (idhsvariable<>-1 and idhsvariable is not null)) ";
            Object[] values_find = new Object[2];
            values_find[0] = new Integer(idsite);
            values_find[1] = new Integer(id_slave);
            RecordSet slave = DatabaseMgr.getInstance().executeQuery(null, sql_find_idvarslave,values_find);
            for(int i=0;i<idprofile.length;i++)
            {
	            Map graphconfigMap = importer.getGraphconfigMap();
	            Map graphconfblockMap = importer.GetGraphconfblockMap();
	            ProfileBean profile = profiles.getProfileById(idprofile[i]);
	            
	            Map graphconfigMap2 = (Map)graphconfigMap.get(profile.getCode());
	            Map graphconfblockMap2 = (Map)graphconfblockMap.get(profile.getCode());
	
	            String code = "";
	            int idvar = -1;
	            int idhsvariable = -1;
	            String ishaccp = null;
	
	            String sql_insert_new_row_slave = "insert into cfgraphconf values (?,?,?,?,?,?,?,?,?);";
	            String sql_insert_new_row_slave_block = "insert into cfgraphconfblock values (?,?,?,?,?,?,?);"; 
	            
	            Object[] values_insert = new Object[9];
	            Object[] values_insert_block = new Object[7];
	            for (int j = 0; j < slave.size(); j++)
	            {
	            	ishaccp = UtilBean.trim(slave.get(j).get(VariableInfo.IS_HACCP));
	            	idhsvariable = (Integer)slave.get(j).get(VarphyBean.IDHSVARIABLE);
	            	code = UtilBean.trim(slave.get(j).get(VariableInfo.CODE));
	            	GraphBean bean = null;
	            	GraphConfBlockBean block = null;

	            	bean = (GraphBean)graphconfigMap2.get("LOG"+code);
	            	block = (GraphConfBlockBean)graphconfblockMap2.get("LOG"+code);
	            	idvar = idhsvariable;

	            	if(bean != null)
	            	{
	            		
		                values_insert[0] = new Integer(idsite);
		                values_insert[1] = new Integer(idprofile[i]);
		                values_insert[2] = null;
		                values_insert[3] = new Integer(id_slave);
		                values_insert[4] = "FALSE";
		                values_insert[5] = new Integer(idvar);
		                values_insert[6] = bean.getColor();
		                if(bean.getYMin()!=null)
		                {
		                	values_insert[7] = bean.getYMin();
		                }
		                else
		                {
		                	values_insert[7] = null;
		                }
		                if(bean.getYMax()!=null)
		                {
		                	values_insert[8] = bean.getYMax();
		                }
		                else
		                {
		                	values_insert[8] = null;
		                }
		                DatabaseMgr.getInstance().executeStatement(null,sql_insert_new_row_slave, values_insert);
	            	}
	                // Insert block configuration
	                 	
	                if (block != null)
	                {
	                	 values_insert_block[0] = new Integer(idsite);
	                     values_insert_block[1] = new Integer(idprofile[i]);
	                     values_insert_block[2] = null;
	                     values_insert_block[3] = new Integer(id_slave);
	                     values_insert_block[4] = "FALSE";
	                     values_insert_block[5] = new Integer(idvar);
	                	 values_insert_block[6] = block.getLabel();
	                	 DatabaseMgr.getInstance().executeStatement(null,sql_insert_new_row_slave_block, values_insert_block);
	                }
	                // End
	                if("TRUE".equals(ishaccp))
	            	{
	            		bean = (GraphBean)graphconfigMap2.get("HACCP"+code);
	            		idvar = Integer.parseInt(slave.get(j).get(VariableInfo.ID).toString().trim());
	            		if (bean != null)
	 	                {
	            			values_insert[0] = new Integer(idsite);
			                values_insert[1] = new Integer(idprofile[i]);
			                values_insert[2] = null;
			                values_insert[3] = new Integer(id_slave);
			                values_insert[4] = "TRUE";
			                values_insert[5] = new Integer(idvar);
			                values_insert[6] = bean.getColor();
			                if(bean.getYMin()!=null)
			                {
			                	values_insert[7] = bean.getYMin();
			                }
			                else
			                {
			                	values_insert[7] = null;
			                }
			                if(bean.getYMax()!=null)
			                {
			                	values_insert[8] = bean.getYMax();
			                }
			                else
			                {
			                	values_insert[8] = null;
			                }
			                DatabaseMgr.getInstance().executeStatement(null,sql_insert_new_row_slave, values_insert);
	 	                }
	            	}
	            }
            }
        }
    
    private static void propagateIntoCfPageGraph(int idsite, int idprofile,
        String language, int id_master, int id_slave) throws Exception
    {
        //pulizia variabili slave 
        String sql = "delete from cfpagegraph where idsite=? and iddevice=? and idprofile=?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_slave);
        param[2] = new Integer(idprofile);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        sql = "select * from cfpagegraph where idsite=? and iddevice=? and idprofile=?";

        param[1] = new Integer(id_master);
        param[2] = new Integer(idprofile);

        //record nella cfpagegraph per il dispositivo master
        RecordSet rs_master = DatabaseMgr.getInstance().executeQuery(null, sql,
                param);

        List<Integer> vars_master = new ArrayList<Integer>();
        List<Integer> vars_slave = new ArrayList<Integer>();
        List<Integer> vars_mdl = new ArrayList<Integer>();
        String ishaccp = null;
        Record insert_master = null;

        for (int i = 0; i < rs_master.size(); i++)
        {
            insert_master = rs_master.get(i);

            for (int j = 6; j < 26; j++)
            {
                if (!insert_master.get(j).equals(new Integer(0)))
                {
                    vars_master.add((Integer)insert_master.get(j));
                }
            }

            //trovo i modelli delle variabili coivolte nella configuarazione
            for (int j = 0; j < vars_master.size(); j++)
            {
                String sql_find_varmdl = "select idvarmdl from cfvariable where idsite=? and idvariable=?";
                param = new Object[2];
                param[0] = new Integer(idsite);
                param[1] = vars_master.get(j);
               
                RecordSet rs_varmdl = DatabaseMgr.getInstance().executeQuery(null,
                        sql_find_varmdl, param);
                vars_mdl.add((Integer)rs_varmdl.get(0).get(0));
            }

            ishaccp = insert_master.get("ishaccp").toString().trim();

            //dalla lista modelli risalgo alla lista variabili
            for (int j = 0; j < vars_mdl.size(); j++)
            {
                String sql_find_idvarslave = "select idvariable,idhsvariable from cfvariable where idsite=? and iddevice=? and idvarmdl=? and idhsvariable is not null";
                param = new Object[3];
                param[0] = new Integer(idsite);
                param[1] = new Integer(id_slave);
                param[2] = vars_mdl.get(j);

                RecordSet recordset_slave = DatabaseMgr.getInstance()
                                                       .executeQuery(null,
                        sql_find_idvarslave, param);

                if (ishaccp.equalsIgnoreCase("TRUE"))
                {
                    vars_slave.add((Integer)recordset_slave.get(0).get(0));
                }
                else if ((Integer)recordset_slave.get(0).get(1)!=-1)
                {
                    vars_slave.add((Integer)recordset_slave.get(0).get(1));
                }
                else
                {
                	//ignora
                }
            }

            sql = "insert into cfpagegraph values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            param = new Object[34];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idprofile);
            param[2] = null;
            param[3] = new Integer(id_slave);
            param[4] = rs_master.get(i).get(4).toString().trim();
            param[5] = rs_master.get(i).get(5);

            //da param[6]a param[25] devo rimappare le variabili che sono istanziate sul master
            for (int j = 6; j < (vars_slave.size() + 6); j++)
            {
                param[j] = vars_slave.get(j - 6);
            }

            for (int j = vars_slave.size() + 6; j < 26; j++)
            {
                param[j] = null;
            }

            param[26] = rs_master.get(i).get(26).toString().trim();
            param[27] = rs_master.get(i).get(27).toString().trim();
            param[28] = rs_master.get(i).get(28).toString().trim();
            param[29] = rs_master.get(i).get(29).toString().trim();
            param[30] = rs_master.get(i).get(30).toString().trim();
            param[31] = rs_master.get(i).get(31).toString().trim();
            param[32] = rs_master.get(i).get(32).toString().trim();
            param[33] = rs_master.get(i).get(33).toString().trim();

            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            //pulizia liste
            vars_master.clear();
            vars_slave.clear();
            vars_mdl.clear();
        }
    }
    private static void propagateIntoCfPageGraph(int idsite, int[] idprofile,
            String language, DeviceConfigImport importer, int id_slave,ProfileBeanList profiles) throws Exception
        {
    		if(idprofile.length == 0)
    			return;
            //pulizia variabili slave 
            String sql = "delete from cfpagegraph where idsite=? and iddevice=? and idprofile in (";
            for(int i=0;i<idprofile.length;i++)
            {
            	if(i<idprofile.length-1)
            	{
            		sql += "?,";
            	}
            	else
            	{
            		sql += "?)";
            	}
            }
            Object[] param = new Object[2+idprofile.length];
            param[0] = new Integer(idsite);
            param[1] = new Integer(id_slave);
            for(int i=0;i<idprofile.length;i++)
            {
            	param[2+i] = new Integer(idprofile[i]);
            }
            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            sql =  "select * from cfvariable where idsite=? and iddevice=? and iscancelled='FALSE' and (ishaccp='TRUE' or (idhsvariable<>-1 and idhsvariable is not null)) ";
            param = new Object[2];
            param[0] = new Integer(idsite);
            param[1] = new Integer(id_slave);
            RecordSet slave = DatabaseMgr.getInstance().executeQuery(null, sql, param);
            for(int i=0;i<idprofile.length;i++)
            {
            	Map map = importer.GetPagegraphMap();
            	ProfileBean profile = profiles.getProfileById(idprofile[i]);
            	ArrayList list = (ArrayList)map.get(profile.getCode());
            	for(int k=0;k<list.size();k++)
            	{
            		int j = 0;
            		ConfigurationGraphBean graph = (ConfigurationGraphBean)list.get(k);
            		Integer[] idvar = new Integer[20];
            		j = checkCodeInSlave(graph.getCode1(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode2(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode3(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode4(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode5(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode6(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode7(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode8(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode9(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode10(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode11(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode12(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode13(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode14(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode15(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode16(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode17(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode18(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode19(),slave,idvar,j,graph.isHaccp());
            		j = checkCodeInSlave(graph.getCode20(),slave,idvar,j,graph.isHaccp());
            		sql = "insert into cfpagegraph values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    param = new Object[34];
                    param[0] = new Integer(idsite);
                    param[1] = new Integer(idprofile[i]);
                    param[2] = null;
                    param[3] = new Integer(id_slave);
                    param[4] = graph.isHaccp()?"TRUE":"FALSE";
                    param[5] = graph.getPeriodCode();

                    //da param[6]a param[25] devo rimappare le variabili che sono istanziate sul master
                    for (int l = 6; l < (idvar.length + 6); l++)
                    {
                        param[l] = idvar[l - 6];
                    }

                    param[26] = graph.getViewFinderCheck();
                    param[27] = graph.getXGridCheck();
                    param[28] = graph.getYGridCheck();
                    param[29] = graph.getViewFinderColorBg();
                    param[30] = graph.getViewfinderColorFg();
                    param[31] = graph.getGridColor();
                    param[32] = graph.getBgGraphColor();
                    param[33] = graph.getAxisColor();

                    DatabaseMgr.getInstance().executeStatement(null, sql, param);
            	}
            }
        }
    private static int checkCodeInSlave(String code, RecordSet slave, Integer[] idvar,int index,boolean haccpgraph)
    {
    	if(code == null || code.length()==0)
    		return index;
		for(int i=0;i<slave.size();i++)
		{
			String code_slave = UtilBean.trim(slave.get(i).get(VariableInfo.CODE));
			String haccp = UtilBean.trim(slave.get(i).get(VariableInfo.IS_HACCP));
			int idhsvariable = (Integer)slave.get(i).get(VarphyBean.IDHSVARIABLE);
			boolean slaveishaccp = "FALSE".equals(haccp)?false:true;
			if(code.equals(code_slave))
			{
				if(haccpgraph == true && slaveishaccp == true)
				{
					idvar[index++] = new Integer((Integer)slave.get(i).get(Parameter.IDVARIABLE));
				}
				if(haccpgraph == false && idhsvariable != -1)
				{
					idvar[index++] = new Integer((Integer)slave.get(i).get(VarphyBean.IDHSVARIABLE));
				}
				break;
			}
		}
		return index;
    }
    private static PropagateRes canPropagateHistorical(int idsite, int idmaster,
        int idslave, PropagateRes res) throws DataBaseException, IOException
    {
    	long delta = 0;
        delta = calculateKeyMaxOfDevice(idsite, idmaster, false, true) -
            calculateKeyMaxOfDevice(idsite, idslave, false, true);

        // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
        if (delta <= 0)
        {
        	res.setCanPropagate(true);
        }
        else
        {
            // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
            long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
            long freespace = res.getFreespace();
            //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
            if (freespace > deltaByte)
            {
            	res.setCanPropagate(true);
            	res.setFreespace(freespace-deltaByte);
            }
            else
            {
            	res.setCanPropagate(false);
            }
        }
        return res;
    }
    private static PropagateRes canPropagateHistorical(int idsite, DeviceConfigImport importer,
            int idslave, PropagateRes res) throws DataBaseException, IOException
    {
    	long delta = 0;
        delta = calculateKeyMaxOfDevice(importer, false, true) -
            calculateKeyMaxOfDevice(idsite, idslave, false, true);

        // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
        if (delta <= 0)
        {
        	res.setCanPropagate(true);
        }
        else
        {
            // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
            long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
            long freespace = res.getFreespace();
            //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
            if (freespace > deltaByte)
            {
            	res.setCanPropagate(true);
            	res.setFreespace(freespace-deltaByte);
            }
            else
            {
            	res.setCanPropagate(false);
            }
        }
        return res;
    }

    private static PropagateRes canPropagateHaccp(int idsite, int idmaster,
        int idslave, PropagateRes res) throws DataBaseException, IOException
    {
        long delta = 0;
        delta = calculateKeyMaxOfDevice(idsite, idmaster, false, false) -
            calculateKeyMaxOfDevice(idsite, idslave, false, false);

        // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
        if (delta <= 0)
        {
        	res.setCanPropagate(true);
        }
        else
        {
            // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
            long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
            long freespace = res.getFreespace();
            //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
            if (freespace > deltaByte)
            {
            	res.setCanPropagate(true);
                res.setFreespace(freespace-deltaByte);
            }
            else
            {
            	res.setCanPropagate(false);
            }
        }
        return res;
    }
    private static PropagateRes canPropagateHaccp(int idsite, DeviceConfigImport importer,
    	int idslave, PropagateRes res) throws DataBaseException, IOException
    {
        long delta = 0;
        delta = calculateKeyMaxOfDevice(importer, false, false) -
            calculateKeyMaxOfDevice(idsite, idslave, false, false);

        // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
        if (delta <= 0)
        {
        	res.setCanPropagate(true);
        }
        else
        {
            // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
            long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
            long freespace = res.getFreespace();
            //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
            if (freespace > deltaByte)
            {
            	res.setCanPropagate(true);
                res.setFreespace(freespace-deltaByte);
            }
            else
            {
            	res.setCanPropagate(false);
            }
        }
        return res;
    }

    private static PropagateRes canPropagateAlarms(int idsite, int idmaster,
        int idslave, PropagateRes res) throws DataBaseException, IOException
    {
    	long delta = 0;
        delta = calculateKeyMaxOfDevice(idsite, idmaster, true, false) - calculateKeyMaxOfDevice(idsite, idslave, true, false);

        // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
        if (delta <= 0)
        {
            res.setCanPropagate(true);
        }
        else
        {
            // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
            long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
            long freespace = res.getFreespace();
            //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
            if (freespace > deltaByte)
            {
                res.setCanPropagate(true);
                res.setFreespace(freespace-deltaByte);
            }
            else
            {
                res.setCanPropagate(false);
            }
        }
        return res;
    }
    private static PropagateRes canPropagateAlarms(int idsite, DeviceConfigImport importer,
            int idslave, PropagateRes res) throws DataBaseException, IOException
        {
        	long delta = 0;
            delta = calculateKeyMaxOfDevice(importer, true, false) - calculateKeyMaxOfDevice(idsite, idslave, true, false);

            // se la somma dei keymax del master �?minore di quella dello slave, allora di sicuro posso propagare
            if (delta <= 0)
            {
                res.setCanPropagate(true);
            }
            else
            {
                // calcolo quanto spazio serve in pi�?di prima su disco in byte e vedo se su disco ho spazio a sufficenza	
                long deltaByte = SpaceHistoricalHelper.keyMaxSumToByte(delta);
                long freespace = res.getFreespace();
                //Dallo spazio libero tolgo quello "prenotato" per la storicizzazione di allarmi,haccp e storico
                if (freespace > deltaByte)
                {
                    res.setCanPropagate(true);
                    res.setFreespace(freespace-deltaByte);
                }
                else
                {
                    res.setCanPropagate(false);
                }
            }
            return res;
        }
    private static long calculateKeyMaxOfDevice(int idsite, int iddevice,
        boolean isalarm, boolean historical) throws DataBaseException
    {
        String sql = "";

        if (isalarm)
        {
            sql = "select buffer.keymax " +
            		" from buffer,cfvariable " +
            		" where buffer.idsite=? and " +
            		" cfvariable.idsite=? and " +
            		" buffer.idvariable=cfvariable.idvariable and " +
            		" cfvariable.iddevice=? and " +
            		" cfvariable.iscancelled='FALSE' and " +
            		" cfvariable.isactive='TRUE' and " +
            		" cfvariable.type=4";
        } else if (historical) {
            sql = "select buffer.keymax " +
            		" from buffer,cfvariable " +
            		" where buffer.idsite=? and " +
            		" cfvariable.idsite=? and " +
            		" buffer.idvariable=cfvariable.idvariable and " +
            		" cfvariable.iddevice=? and " +
            		" cfvariable.iscancelled='FALSE' and " +
            		" cfvariable.isactive='TRUE' and " +
            		" cfvariable.type!=4 and " +
            		" cfvariable.idhsvariable is null";
        } else { //haccp
            sql = "select buffer.keymax " +
            		" from buffer,cfvariable " +
            		" where buffer.idsite=? and " +
            		" cfvariable.idsite=? and " +
            		" buffer.idvariable=cfvariable.idvariable and " +
            		" cfvariable.iddevice=? and " +
            		" cfvariable.iscancelled='FALSE' and " +
            		" cfvariable.isactive='TRUE' and " +
            		" cfvariable.type!=4 and " +
            		" cfvariable.ishaccp='TRUE'";
        }

        Object[] params = new Object[3];
        params[0] = new Integer(idsite);
        params[1] = new Integer(idsite);
        params[2] = new Integer(iddevice);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

        long keymax = 0;
        for (int i = 0; i < rs.size(); i++)
        {
            keymax = keymax + new Long(rs.get(i).get(0).toString()).longValue();
        }
        return keymax;
    }
    private static long calculateKeyMaxOfDevice(DeviceConfigImport importer,
            boolean isalarm, boolean historical) throws DataBaseException
    {
        long keymax = 0;
        Map map = null;
        if(isalarm)
        {
        	map = importer.getAlarmMap();
        	keymax = BDevDetail.ALARM_ROWSHYSTORICAL*map.size();   
        }
        else if(historical)
        {
        	map = importer.getLogMap();
        	Iterator it = map.entrySet().iterator();
        	while(it.hasNext())
        	{
        		Map.Entry<String,VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
        		VarphyBean var = entry.getValue();
        		keymax += var.getKeymax();
        	}
        }
        else
        {
        	map = importer.getHaccpMap();
        	keymax = BDevDetail.HACCP_ROWSHYSTORICAL*map.size();
        }
        return keymax;
    }
    private static boolean isVariableActive(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select isactive from cfvariable where idsite =? and idvariable=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idvariable) });

        if (UtilBean.trim(rs.get(0).get("isactive").toString()).equals("TRUE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

//    private static Integer getProfTime(short keymax, int hsfreq, int type)
//    {
//        Integer proftime = null;
//
//        if (type == 4)
//        {
//            proftime = new Integer(100);
//        }
//        else
//        {
//            proftime = new Integer(new Double((hsfreq * 64 * keymax) / 1.1).intValue());
//        }
//
//        return proftime;
//    }

    public static Set<Integer> canPropagateGraphConf(int idsite, String language, int id_master, int[] id_slaves) throws Exception
    {
//    	long tstart = System.currentTimeMillis();
    	StringBuffer id_slaveslist = new StringBuffer();
    	
    	for(int i=0;i<id_slaves.length-1;i++){
    		id_slaveslist.append(id_slaves[i]);
    		id_slaveslist.append(',');
    	}
		
    	id_slaveslist.append(id_slaves[id_slaves.length-1]);    	
    	
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
    			"select distinct b.iddevice " +
    			"from cfvariable as a, cfvariable as b " +
    			"where " +
    			"a.iddevice="+id_master+" " +
    			"and b.iddevice in ("+id_slaveslist.toString()+") " +
    			"and a.idvarmdl = b.idvarmdl " +
    			"and a.iscancelled = 'FALSE' " +
    			"and b.iscancelled = 'FALSE' " +
    			"and a.idsite = "+idsite+" and b.idsite = a.idsite and " +
    			"( " +
    			"(a.ishaccp = 'TRUE' and b.ishaccp = 'FALSE' and b.idhsvariable is not null) or " +
    			"(a.idhsvariable != -1 and b.idhsvariable = -1)" +
    			") " +
    			"order by b.iddevice");
    	
		HashSet<Integer> toret = new HashSet<Integer>();
    	
    	if(rs!=null)
    	{
    		for(int i=0;i<rs.size();i++)
    		{
    			toret.add((Integer)rs.get(i).get(0));
    		}
    	}
//        LoggerMgr.getLogger(Propagate.class).info("time a="+(System.currentTimeMillis()-tstart));
        return toret;
    }
    
    public static String DeviceExport(int idsite,String lan,int idmaster,int profile,String filename)
    {
        String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String fixver = BaseConfig.getProductInfo("fix");
        fixver = (fixver == null || fixver.equals(""))?"":"."+fixver;
        String projectHead = "<Project pvproversion=\""+BaseConfig.getProductInfo("version")+fixver+
        		"\" docdate=\""+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date())+
        		"\" >";
        String projectTail = "</Project>";

        BufferedWriter XMLwriter = null;
        
        //tot: return message
        String tot = "<response>";
        try
        {
	        DevMdlBeanList mdlBeanList = new DevMdlBeanList();
	        DevMdlBean master = mdlBeanList.retrieveByDeviceId(idsite, lan, idmaster);
	        if(master != null)
	        {
		       	 XMLwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
		       	 XMLwriter.write(xmlHead);
		       	 XMLwriter.write(projectHead);
		       	 XMLNode deviceXML = new XMLNode(DEVICE,"");
		       	 deviceXML.setAttribute(DEVICE_CODE, master.getCode());
				 StringBuffer sql = new StringBuffer();
				 sql.append("select cfvariable.*,cftableext.languagecode,cftableext.description,cftableext.shortdescr,cftableext.longdescr ");
				 sql.append(" from cfvariable");
				 sql.append(" inner join cftableext on cftableext.tableid=cfvariable.idvariable and cftableext.tablename='cfvariable'");
				 sql.append(" where cfvariable.iddevice= ? and");
				 sql.append(" cfvariable.idsite=? and");
				 sql.append(" cfvariable.iscancelled='FALSE' and");
				 sql.append(" cfvariable.idhsvariable is not null");
				 sql.append(" order by cfvariable.idvariable");
				 Object[] params = new Object[]{new Integer(idmaster),new Integer(idsite)};
				 RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), params);
				 XMLNode alarmSection = new XMLNode(ALARM,"");
				 deviceXML.addNode(alarmSection);
				 XMLNode unitMeasureSection = new XMLNode(UNITMEASURE,"");
				 deviceXML.addNode(unitMeasureSection);
				 XMLNode haccpSection = new XMLNode(HACCP,"");
				 deviceXML.addNode(haccpSection);
				 XMLNode logSection = new XMLNode(LOG,"");
				 deviceXML.addNode(logSection);
				 XMLNode displaySection = new XMLNode(DISPLAY,"");
				 deviceXML.addNode(displaySection);
				 XMLNode descriptionSection = new XMLNode(DESCRIPTION,"");
				 if(rs != null && rs.size()>0)
				 {
					 //strange id to identify the first one
					 int tempid = FIRSTVALUE;
					 boolean isfirstid = true;
					 Map langmap = new HashMap();
					 for(int i=0;i<rs.size();i++)
					 {
						 Record record = rs.get(i);
						 int idvariable = ((Integer) record.get(VariableInfo.ID)).intValue();
						 int type = ((Integer) record.get(VariableInfo.TYPE)).intValue();
						 String code = UtilBean.trim(record.get(VariableInfo.CODE));
						 String languagecode = UtilBean.trim(record.get(LANGUAGECODE));
						 String description = UtilBean.trim(record.get(VarphyBean.DESCRIPTION));
						 String shortdescr = UtilBean.trim(record.get(VarphyBean.SHORTDESC));
						 String longdescr = UtilBean.trim(record.get(VarphyBean.LONG_DESCR));
						 XMLNode lang = null;
						 if(tempid != idvariable && tempid != FIRSTVALUE)
						 {
							 isfirstid = false;
						 }
						 //multi-language
						 if(isfirstid == true)
						 {
							 lang = new XMLNode(DESCRIPTION_LANG,"");
							 langmap.put(languagecode, lang);
							 descriptionSection.addNode(lang);
							 lang.setAttribute(DESCRIPTION_LANG_KEY, languagecode);
						 }
						 else
						 {
							 lang = (XMLNode)langmap.get(languagecode);
						 }
						 //description
						 if(lang != null)
						 {
							 XMLNode dsc_var = new XMLNode(DESCRIPTION_LANG_DES,"");
							 lang.addNode(dsc_var);
							 dsc_var.setAttribute(CODE, code);
							 dsc_var.setTextValue("<![CDATA["+description+"]]>");
							 dsc_var.setAttribute(DESCRIPTION_LANG_SHORT,  UtilityString.replaceBadChars4XML(shortdescr));	
							 dsc_var.setAttribute(DESCRIPTION_LANG_LONG, UtilityString.replaceBadChars4XML(longdescr));
							 
						 }
						 // only need to save one time for multi-language
						 if(tempid != idvariable)
						 {
							 tempid = idvariable;
							 String priority = String.valueOf(record.get(VariableInfo.PRIORITY));
							 
							 String unitmeasure = UtilBean.trim(record.get(VariableInfo.MEASUREUNIT));
							 //unit measure
							 XMLNode um_var = new XMLNode(UNITMEASURE_ABBREVATION,"");
							 unitMeasureSection.addNode(um_var);
							 um_var.setAttribute(CODE, code);
							 um_var.setTextValue("<![CDATA["+unitmeasure+"]]>");
							 
							 //alarm
							 if(type == VariableInfo.TYPE_ALARM)
							 {
								 String isActive = UtilBean.trim(record.get(VariableInfo.IS_ACTIVE));
								 String frequency = record.get(VariableInfo.FREQUENCY).toString();
								 XMLNode alarm_var = new XMLNode(ALARM_ABBREVATION,"");
								 alarmSection.addNode(alarm_var);
								 alarm_var.setAttribute(CODE, code);
								 alarm_var.setAttribute(PRIORITY, priority);
								 alarm_var.setAttribute(ALARM_ISACTIVE, isActive);
								 alarm_var.setAttribute(ALAMR_FREQUENCY, frequency);
							 }
							 else
							 {
								 String isHaccp = UtilBean.trim(record.get(VariableInfo.IS_HACCP));
								 String todisplay = UtilBean.trim(record.get(VariableInfo.TODISPLAY));
								 
								 //haccp
								 if(isHaccp.equals(TRUE))
								 {
									 XMLNode haccp_var = new XMLNode(HACCP_ABBREVATION,"");
									 haccpSection.addNode(haccp_var);
									 haccp_var.setAttribute(CODE, code);
								 }
								 
								 //display
								 XMLNode display_var = new XMLNode(DISPLAY_ABBREVATION,"");
								 displaySection.addNode(display_var);
								 display_var.setAttribute(CODE, code);
								 display_var.setAttribute(DISPLAY_TODISPLAY, todisplay);
								 display_var.setAttribute(PRIORITY, priority);
							 }
						 }
					 }
				 }
				 //log
				 sql = new StringBuffer();
				 sql.append("select cfvariable.code,cfvariable.frequency,cfvariable.type,cfvariable.delta, buffer.keymax");
				 sql.append(" from cfvariable ");
				 sql.append(" inner join buffer on buffer.idvariable = cfvariable.idvariable");
				 sql.append(" where cfvariable.iddevice= ? and ");
				 sql.append(" cfvariable.idsite=? and ");
				 sql.append(" cfvariable.iscancelled='FALSE' and ");
				 sql.append(" cfvariable.idhsvariable is null ");
				 sql.append(" order by cfvariable.idvariable ");
				 params = new Object[]{new Integer(idmaster),new Integer(idsite)};
				 rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), params);
				 if(rs != null && rs.size()>0)
				 {
					 for(int i=0;i<rs.size();i++)
					 {
						 Record record = rs.get(i);
						 int type = ((Integer) record.get(VariableInfo.TYPE)).intValue();
						 String code = UtilBean.trim(record.get(VariableInfo.CODE));
						 XMLNode log_var = new XMLNode(LOG_ABBREVATION,"");
						 logSection.addNode(log_var);
						 log_var.setAttribute(CODE, code);
						 String frequency = String.valueOf(record.get(VariableInfo.FREQUENCY));
						 log_var.setAttribute(LOG_FREQUENCY, frequency);
						 String keymax = String.valueOf( record.get("keymax"));
						 log_var.setAttribute(LOG_KEYMAX, keymax);
						 //digital, do delta
						 if(type != VariableInfo.TYPE_DIGITAL)
						 {
							 String delta = String.valueOf(record.get(VariableInfo.VARIATION));
							 log_var.setAttribute(LOG_DELTA, delta);
						 }
					 }
				}
				 // graphconf
				 sql = new StringBuffer();
				 sql.append("select cfgraphconf.*,cfvariable.code,profilelist.code as profname ");
				 sql.append("from cfgraphconf ");
				 sql.append("inner join cfvariable on cfgraphconf.idvariable=cfvariable.idvariable ");
				 sql.append("inner join profilelist on profilelist.idprofile = cfgraphconf.idprofile ");
				 sql.append("where cfgraphconf.iddevice = ? ");
				 sql.append("order by profname ");
				 params = new Object[]{new Integer(idmaster)};
				 rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), params);
				 XMLNode graphconfigSection = new XMLNode(GRAPHCONF,"");
				 deviceXML.addNode(graphconfigSection);
				 if(rs != null && rs.size()>0)
				 {
					 XMLNode tempXML = null;
					 String tempprofile = null;
					 for(int i=0;i<rs.size();i++)
					 {
						 Record record = (Record)rs.get(i);
						 String code = UtilBean.trim(record.get(VariableInfo.CODE));
						 String isHaccp = UtilBean.trim(record.get(VariableInfo.IS_HACCP));
						 String color = UtilBean.trim(record.get(GraphBean.COLOR));
						 String ymax = String.valueOf(record.get(GraphBean.YMAX));
						 String ymin = String.valueOf(record.get(GraphBean.Y_MIN));
						 String profilename = UtilBean.trim(record.get(PROFILENAME));
						 XMLNode graphconf_profile = null;
						 if(tempprofile == null || !tempprofile.equals(profilename))
						 {
							 graphconf_profile = new XMLNode(GRAPHCONF_PROFILE,"");
							 graphconf_profile.setAttribute(PROFILENAME, profilename);
							 graphconfigSection.addNode(graphconf_profile);
							 tempXML = graphconf_profile;
							 tempprofile = profilename;
						 }
						 else
						 {
							 graphconf_profile = tempXML;
						 }
						 XMLNode graphconf_var = new XMLNode(GRAPHCONF_ABBREVATION,"");
						 graphconf_profile.addNode(graphconf_var);
						 graphconf_var.setAttribute(CODE, code);
						 graphconf_var.setAttribute(GRAPHCONF_ISHACCP, isHaccp);
						 graphconf_var.setAttribute(GRAPHCONF_COLOR,color);
						 if(!ymax.endsWith(ymin))
						 {
							 graphconf_var.setAttribute(GRAPHCONF_YMAX, ymax);
							 graphconf_var.setAttribute(GRAPHCONF_YMIN, ymin);
						 }
					 }
				 }
				 //pageconf
				 sql = new StringBuffer();
				 sql.append("select cfpagegraph.ishaccp,cfpagegraph.periodcode");
				 sql.append(",v1.code as idv1,v2.code as idv2,v3.code as idv3,v4.code as idv4,v5.code as idv5,v6.code as idv6,v7.code as idv7,v8.code as idv8,v9.code as idv9,v10.code as idv10");
				 sql.append(",v11.code as idv11,v12.code as idv12,v13.code as idv13,v14.code as idv14,v15.code as idv15,v16.code as idv16,v17.code as idv17,v18.code as idv18,v19.code as idv19,v20.code as idv20");
				 sql.append(",cfpagegraph.viewfindercheck,cfpagegraph.xgridcheck,cfpagegraph.ygridcheck,cfpagegraph.viewfindercolorbg,cfpagegraph.viewfindercolorfg,cfpagegraph.gridcolor,cfpagegraph.bggraphcolor,cfpagegraph.axiscolor");
				 sql.append(",profilelist.code as profname ");
				 sql.append(" from cfpagegraph");
				 sql.append(" left join cfvariable as v1	on v1.idvariable=cfpagegraph.idvariable1 left join cfvariable as v2	on v2.idvariable=cfpagegraph.idvariable2 left join cfvariable as v3	on v3.idvariable=cfpagegraph.idvariable3 left join cfvariable as v4	on v4.idvariable=cfpagegraph.idvariable4");
				 sql.append(" left join cfvariable as v5	on v5.idvariable=cfpagegraph.idvariable5 left join cfvariable as v6	on v6.idvariable=cfpagegraph.idvariable6 left join cfvariable as v7	on v7.idvariable=cfpagegraph.idvariable7 left join cfvariable as v8	on v8.idvariable=cfpagegraph.idvariable8");
				 sql.append(" left join cfvariable as v9	on v9.idvariable=cfpagegraph.idvariable9 left join cfvariable as v10	on v10.idvariable=cfpagegraph.idvariable10 left join cfvariable as v11	on v11.idvariable=cfpagegraph.idvariable11 left join cfvariable as v12	on v12.idvariable=cfpagegraph.idvariable12");
				 sql.append(" left join cfvariable as v13	on v13.idvariable=cfpagegraph.idvariable13 left join cfvariable as v14	on v14.idvariable=cfpagegraph.idvariable14 left join cfvariable as v15	on v15.idvariable=cfpagegraph.idvariable15 left join cfvariable as v16	on v16.idvariable=cfpagegraph.idvariable16");
				 sql.append(" left join cfvariable as v17	on v17.idvariable=cfpagegraph.idvariable17 left join cfvariable as v18	on v18.idvariable=cfpagegraph.idvariable18 left join cfvariable as v19	on v19.idvariable=cfpagegraph.idvariable19 left join cfvariable as v20	on v20.idvariable=cfpagegraph.idvariable20");
				 sql.append(" inner join profilelist on profilelist.idprofile = cfpagegraph.idprofile ");
				 sql.append(" where cfpagegraph.iddevice = ? ");
				 sql.append(" order by profname");
				 params = new Object[]{new Integer(idmaster)};
				 rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), params);
				 XMLNode graphSection = new XMLNode(GRAPH,"");
				 deviceXML.addNode(graphSection);
				 if(rs != null && rs.size()>0)
				 {
					 XMLNode tempXML = null;
					 String tempprofile = null;
					 for(int i=0;i<rs.size();i++)
					 {
						 Record record = rs.get(i);
						 String ishaccp = UtilBean.trim(record.get(VariableInfo.IS_HACCP));
						 String periodcode = String.valueOf(record.get(ConfigurationGraphBean.PERIOD_CODE));
						 String idvar1 = UtilBean.trim(record.get("idv1"));
						 String idvar2 = UtilBean.trim(record.get("idv2"));
						 String idvar3 = UtilBean.trim(record.get("idv3"));
						 String idvar4 = UtilBean.trim(record.get("idv4"));
						 String idvar5 = UtilBean.trim(record.get("idv5"));
						 String idvar6 = UtilBean.trim(record.get("idv6"));
						 String idvar7 = UtilBean.trim(record.get("idv7"));
						 String idvar8 = UtilBean.trim(record.get("idv8"));
						 String idvar9 = UtilBean.trim(record.get("idv9"));
						 String idvar10 = UtilBean.trim(record.get("idv10"));
						 String idvar11 = UtilBean.trim(record.get("idv11"));
						 String idvar12 = UtilBean.trim(record.get("idv12"));
						 String idvar13 = UtilBean.trim(record.get("idv13"));
						 String idvar14 = UtilBean.trim(record.get("idv14"));
						 String idvar15 = UtilBean.trim(record.get("idv15"));
						 String idvar16 = UtilBean.trim(record.get("idv16"));
						 String idvar17 = UtilBean.trim(record.get("idv17"));
						 String idvar18 = UtilBean.trim(record.get("idv18"));
						 String idvar19 = UtilBean.trim(record.get("idv19"));
						 String idvar20 = UtilBean.trim(record.get("idv20"));
						 String viewFinderCheck = UtilBean.trim(record.get("viewfindercheck")); 
					     String xGridCheck = UtilBean.trim(record.get("xgridcheck")); 
					     String yGridCheck = UtilBean.trim(record.get("ygridcheck")); 
					     String viewFinderColorBg = UtilBean.trim(record.get("viewfindercolorbg"));
					     String viewfinderColorFg = UtilBean.trim(record.get("viewfindercolorfg")); 
					     String gridColor = UtilBean.trim(record.get("gridcolor")); 
					     String bgGraphColor = UtilBean.trim(record.get("bggraphcolor")); 
					     String axisColor = UtilBean.trim(record.get("axiscolor"));
					     String profilename = UtilBean.trim(record.get(PROFILENAME));
					     XMLNode graph_profile = null;
						 if(tempprofile == null || !tempprofile.equals(profilename))
						 {
							 graph_profile = new XMLNode(GRAPH_PROFILE,"");
							 graph_profile.setAttribute(PROFILENAME, profilename);
							 graphSection.addNode(graph_profile);
							 tempXML = graph_profile;
							 tempprofile = profilename;
						 }
						 else
						 {
							 graph_profile = tempXML;
						 }
					     XMLNode graph_var = new XMLNode(GRAPH_ABBREVATION,"");
					     graph_profile.addNode(graph_var);
					     graph_var.setAttribute(GRAPHCONF_ISHACCP,ishaccp);
					     graph_var.setAttribute(GRAPH_PERIOD, periodcode);
					     if(idvar1 != null)
					     {
					    	 graph_var.setAttribute("idvar1", idvar1);
					     }
					     if(idvar2 != null)
					     {
					    	 graph_var.setAttribute("idvar2", idvar2);
					     }
					     if(idvar3 != null)
					     {
					    	 graph_var.setAttribute("idvar3", idvar3);
					     }
					     if(idvar4 != null)
					     {
					    	 graph_var.setAttribute("idvar4", idvar4);
					     }
					     if(idvar5 != null)
					     {
					    	 graph_var.setAttribute("idvar5", idvar5);
					     }
					     if(idvar6 != null)
					     {
					    	 graph_var.setAttribute("idvar6", idvar6);
					     }
					     if(idvar7 != null)
					     {
					    	 graph_var.setAttribute("idvar7", idvar7);
					     }
					     if(idvar8 != null)
					     {
					    	 graph_var.setAttribute("idvar8", idvar8);
					     }
					     if(idvar9 != null)
					     {
					    	 graph_var.setAttribute("idvar9", idvar9);
					     }
					     if(idvar10 != null)
					     {
					    	 graph_var.setAttribute("idvar10", idvar10);
					     }
					     if(idvar11 != null)
					     {
					    	 graph_var.setAttribute("idvar11", idvar11);
					     }
					     if(idvar12 != null)
					     {
					    	 graph_var.setAttribute("idvar12", idvar12);
					     }
					     if(idvar13 != null)
					     {
					    	 graph_var.setAttribute("idvar13", idvar13);
					     }
					     if(idvar14 != null)
					     {
					    	 graph_var.setAttribute("idvar14", idvar14);
					     }
					     if(idvar15 != null)
					     {
					    	 graph_var.setAttribute("idvar15", idvar15);
					     }
					     if(idvar16 != null)
					     {
					    	 graph_var.setAttribute("idvar16", idvar16);
					     }
					     if(idvar17 != null)
					     {
					    	 graph_var.setAttribute("idvar17", idvar17);
					     }
					     if(idvar18 != null)
					     {
					    	 graph_var.setAttribute("idvar18", idvar18);
					     }
					     if(idvar19 != null)
					     {
					    	 graph_var.setAttribute("idvar19", idvar19);
					     }
					     if(idvar20 != null)
					     {
					    	 graph_var.setAttribute("idvar20", idvar20);
					     }
					     if(viewFinderCheck != null)
					     {
					    	 graph_var.setAttribute(GRAPH_VIEWFINDERCHECK,viewFinderCheck);
					     }
					     if(xGridCheck != null)
					     {
					    	 graph_var.setAttribute(GRAPH_XGRIDCHECK, xGridCheck);
					     }
					     if(yGridCheck != null)
					     {
					    	 graph_var.setAttribute(GRAPH_YGRIDCHECK, yGridCheck);
						 }
					     if(viewFinderColorBg != null)
					     {
						     graph_var.setAttribute(GRAPH_VIEWFINDERCOLORBG, viewFinderColorBg);
					     }
					     if(viewfinderColorFg != null)
					     {
						     graph_var.setAttribute(GRAPH_VIEWFINDERCOLORFG, viewfinderColorFg);
					     }
					     if(gridColor != null)
					     { 
						     graph_var.setAttribute(GRAPH_GRIDCOLOR, gridColor);
					     }
					     if(bgGraphColor != null)
					     {
						     graph_var.setAttribute(GRAPH_BGGRAPHCOLOR, bgGraphColor);
					     }
					     if(axisColor != null)
					     {
						     graph_var.setAttribute(GRAPH_AXISCOLOR, axisColor);
						 }
					 }
				 }
				 //graphconfblock
				 sql = new StringBuffer();
				 sql.append("select cfgraphconfblock.*,cfvariable.code,profilelist.code as profname ");
				 sql.append("from cfgraphconfblock ");
				 sql.append("inner join cfvariable on cfgraphconfblock.idvariable=cfvariable.idvariable ");
				 sql.append("inner join profilelist on profilelist.idprofile = cfgraphconfblock.idprofile ");
				 sql.append("where cfgraphconfblock.iddevice = ? ");
				 sql.append("order by profname, blocklabel ");
				 params = new Object[]{new Integer(idmaster)};
				 rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), params);
				 XMLNode graphconfblockSection = new XMLNode(GRAPHCONFBLOCK,"");
				 deviceXML.addNode(graphconfblockSection);
				 if(rs != null && rs.size()>0)
				 {
					 XMLNode tempXML = null;
					 String tempprofile = null;
					 for(int i=0;i<rs.size();i++)
					 {
						 Record record = (Record)rs.get(i);
						 String code = UtilBean.trim(record.get(VariableInfo.CODE));
						 String isHaccp = UtilBean.trim(record.get(VariableInfo.IS_HACCP));
						 String label = UtilBean.trim(record.get("blocklabel"));
						 String profilename = UtilBean.trim(record.get(PROFILENAME));
						 XMLNode graphconfblock_profile = null;
						 if(tempprofile == null || !tempprofile.equals(profilename))
						 {
							 graphconfblock_profile = new XMLNode(GRAPHCONFBLOCK_PROFILE,"");
							 graphconfblock_profile.setAttribute(PROFILENAME, profilename);
							 graphconfblockSection.addNode(graphconfblock_profile);
							 tempXML = graphconfblock_profile;
							 tempprofile = profilename;
						 }
						 else
						 {
							 graphconfblock_profile = tempXML;
						 }
						 XMLNode graphconfblock_var = new XMLNode(GRAPHCONFBLOCK_ABBREVATION,"");
						 graphconfblock_profile.addNode(graphconfblock_var);
						 graphconfblock_var.setAttribute(CODE, code);
						 graphconfblock_var.setAttribute(GRAPHCONF_ISHACCP, isHaccp);
						 graphconfblock_var.setAttribute(GRAPHCONFBLOCK_LABEL, label);
					 }
				 }
				 deviceXML.addNode(descriptionSection);
				 XMLwriter.write(deviceXML.toString());
				 XMLwriter.write(projectTail);
				 XMLwriter.flush();
				 XMLwriter.close();
				 tot += "<file><![CDATA["+filename+"]]></file></response>";
	        }
        }
        catch(Exception e)
        {
        	tot += "<file><![CDATA[ERROR]]></file></response>";
        }
        return tot;
    }
}
