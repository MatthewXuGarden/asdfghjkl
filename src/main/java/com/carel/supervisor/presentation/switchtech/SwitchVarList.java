package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bean.DeviceBean;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SwitchVarList
{
    Map config_var_map = new HashMap();
    List config_var_list = new ArrayList();
    Map config_alr_map = new HashMap();
    List config_alr_list = new ArrayList();

    public SwitchVarList(int idsite, int idswitch, String language)
        throws DataBaseException
    {
        String sql =
            "select switchvar.*, cftableext.description from switchvar inner join cftableext on switchvar.idswitch=? and switchvar.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfvarmdl' and switchvar.idsite = ? and cftableext.idsite = ? order by switchvar.idvarmdl";

        Object[] param = new Object[4];
        param[0] = new Integer(idswitch);
        param[1] = language;
        param[2] = new Integer(idsite);
        param[3] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        Record tmp = null;
        SwitchVar tmp_conf = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = rs.get(i);
            tmp_conf = new SwitchVar(tmp);

            if (tmp_conf.getIsalarm().equalsIgnoreCase("TRUE"))
            {
                config_alr_list.add(tmp_conf);
                config_alr_map.put(tmp_conf.getIdswitch(), tmp_conf);
            }
            else
            {
                config_var_list.add(tmp_conf);
                config_var_map.put(tmp_conf.getIdswitch(), tmp_conf);
            }
        }
    }

    public int size(boolean isalarm)
    {
        if (isalarm)
        {
            return config_alr_list.size();
        }
        else
        {
            return config_var_list.size();
        }
    }

    public SwitchVar getMdlById(int id, boolean isalarm)
    {
        if (isalarm)
        {
            return (SwitchVar) config_alr_map.get(new Integer(id));
        }
        else
        {
            return (SwitchVar) config_var_map.get(new Integer(id));
        }
    }

    public SwitchVar getVarMdl(int position, boolean isalarm)
    {
        if (isalarm)
        {
            return (SwitchVar) config_alr_list.get(position);
        }
        else
        {
            return (SwitchVar) config_var_list.get(position);
        }
    }

    public SwitchVar[] getList(int idsite, int id_switch, boolean isalarm)
    {
        if (isalarm)
        {
            SwitchVar[] list = new SwitchVar[config_alr_list.size()];

            for (int i = 0; i < config_alr_list.size(); i++)
            {
                list[i] = (SwitchVar) config_alr_list.get(i);
            }

            return list;
        }
        else
        {
            SwitchVar[] list = new SwitchVar[config_var_list.size()];

            for (int i = 0; i < config_var_list.size(); i++)
            {
                list[i] = (SwitchVar) config_var_list.get(i);
            }

            return list;
        }
    }

    private static void clearConfMdl(int idsite, int idswitch)
        throws DataBaseException
    {
        String sql = "delete from switchvar where idsite=? and idswitch=?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idswitch) });
    }

    public static void saveSwitchMdlConf(int idsite, int idswitch,
        String var_param, String alr_param, String language)
        throws Exception
    {
        //pulizia precedente configurazione
        clearConfMdl(idsite, idswitch);

        /*################
        ##SAVE SWITCHVAR##
        ################*/
        String sql = "insert into switchvar values(?,?,?,?,?,?,?,?);";
        Integer tmp_id = null;
        Integer tmp_dev = null;
        Float eev = null;
        Float tev = null;

        if (!var_param.equals(""))
        {
            String[] rows_var = var_param.split(";");

            //variabili normali	
            for (int i = 0; i < rows_var.length; i++)
            {
                Object[] param = new Object[8];
                String[] sub_param = rows_var[i].split(",");
                tmp_dev = new Integer(sub_param[0]);
                tmp_id = new Integer(sub_param[1]);
                eev = new Float(sub_param[2]);
                tev = new Float(sub_param[3]);
                param[0] = new Integer(idsite);
                param[1] = new Integer(idswitch);
                param[2] = tmp_dev;
                param[3] = tmp_id;
                param[4] = "FALSE";
                param[5] = eev;
                param[6] = tev;
                param[7] = new Timestamp(System.currentTimeMillis());
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
        }

        //variabili di allarme
        if (!alr_param.equals(""))
        {
            String[] rows_alr = alr_param.split(";");

            for (int i = 0; i < rows_alr.length; i++)
            {
                Object[] param = new Object[8];
                String[] sub_param = rows_alr[i].split(",");
                tmp_dev = new Integer(sub_param[0]);
                tmp_id = new Integer(sub_param[1]);
                param[0] = new Integer(idsite);
                param[1] = new Integer(idswitch);
                param[2] = tmp_dev;
                param[3] = tmp_id;
                param[4] = "TRUE";
                param[5] = null;
                param[6] = null;
                param[7] = new Timestamp(System.currentTimeMillis());
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
        }
        
        //eventuale pulizia dispositivi istanziati
        cleanSwitchDev(idsite, idswitch, language);
        //se nessuna variabile x switch o allarme pulisco configuraizioni
        if (var_param.equalsIgnoreCase("")&&alr_param.equalsIgnoreCase(""))
        {
        	Object[] param = new Object[]{new Integer(idsite),new Integer(idswitch)};
        	
        	sql = "delete from switchconfig where idsite = ? and idswitch = ?";
        	DatabaseMgr.getInstance().executeStatement(null,sql,param);
        	sql = "delete from switchstatus where idsite = ? and idswitch = ?";
        	DatabaseMgr.getInstance().executeStatement(null,sql,param);
        }
    }

    private static void cleanSwitchDev(int idsite, int idswitch, String language)
        throws Exception
    {
        //istanze di device per lo switch selezionato
        DeviceBean[] dev_list = SwitchDevList.getDevicesOfSwitch(idsite,
                idswitch, language);
        Map ids_dev = new HashMap();

        for (int i = 0; i < dev_list.length; i++)
        {
            ids_dev.put(new Integer(i), new Integer(dev_list[i].getIddevmdl()));
        }

        //modelli di device coivolti poer lo switch selezionato
        String sql = "select distinct iddevmdl from switchvar where idsite=? and idswitch=?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idswitch);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        Map ids_evdmdl = new HashMap();

        for (int i = 0; i < rs.size(); i++)
        {
            ids_evdmdl.put(new Integer(i), rs.get(i).get(0));
        }

        Map idsmdl_to_remove = new HashMap();

        for (int i = 0; i < ids_dev.size(); i++)
        {
            if (!ids_evdmdl.containsValue(ids_dev.get(new Integer(i))))
            {
                idsmdl_to_remove.put(new Integer(i), ids_dev.get(new Integer(i)));
            }
        }

        List iddev_to_rem = new ArrayList();

        for (int i = 0; i < dev_list.length; i++)
        {
            if (idsmdl_to_remove.containsValue(
                        new Integer(dev_list[i].getIddevmdl())))
            {
                iddev_to_rem.add(new Integer(dev_list[i].getIddevice()));
            }
        }

        if (iddev_to_rem.size() > 0)
        {
            StringBuffer delete = new StringBuffer(
                    "delete from switchdev where idsite=? and idswitch=? and iddevice in (");

            for (int i = 0; i < (iddev_to_rem.size() - 1); i++)
            {
                delete.append(iddev_to_rem.get(i) + ",");
            }

            delete.append(iddev_to_rem.get(iddev_to_rem.size() - 1) + ")");
            DatabaseMgr.getInstance().executeStatement(null, delete.toString(),
                new Object[] { new Integer(idsite), new Integer(idswitch) });
        }
    }

    public static void restoreDefault(int idsite, int idswitch)
        throws DataBaseException
    {
    	String sql = "delete from switchvar where idsite=? and idswitch=?";
    	Object[] par = new Object[]{new Integer(idsite), new Integer(idswitch)};
    	DatabaseMgr.getInstance().executeStatement(null,sql,par);
    	
    	sql = "select * from switchdefault";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int rows = rs.size();

        if (rows != 0)
        {
            Object[][] params = new Object[rs.size()][];
            Record def = null;

            for (int i = 0; i < rs.size(); i++)
            {
                def = rs.get(i);
                params[i] = new Object[8];
                params[i][0] = new Integer(idsite);
                params[i][1] = new Integer(idswitch);
                params[i][2] = def.get("iddevmdl");
                params[i][3] = def.get("idvarmdl");
                params[i][4] = def.get("isalarm");
                params[i][5] = def.get("eev");
                params[i][6] = def.get("tev");
                params[i][7] = new Timestamp(System.currentTimeMillis());
            }
            
            sql = "insert into switchvar values (?,?,?,?,?,?,?,?)";
            DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
        }
    }
}
