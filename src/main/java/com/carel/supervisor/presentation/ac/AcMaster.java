package com.carel.supervisor.presentation.ac;

import java.util.HashMap;
import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.ac.AcProperties;
import com.carel.supervisor.presentation.bo.helper.VarDetailHelper;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class AcMaster
{
	public static final int OFFSET_WIDTH = 300;
    public static final String SLAVE = "slave";
    
    public static String[] variables = {"Tamb","rH%","Tdew","Extra1","Extra2","Extra3"};
    public static String[] extra_vars = {"Tglass","Output"};
    
    public static HashMap<Integer,Integer> getMasterVars(Integer iddevmaster)
    {
        RecordSet rs = null;
        HashMap<Integer,Integer> varlist = new HashMap<Integer,Integer>(); // Mappa: (index, idvariable)
        
        String sql = "select index, idvarmaster from ac_master where iddevmaster=? order by index";
        
        Object[] param = new Object[] {iddevmaster};
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMaster.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            Integer indice = null;
            Integer idvar = null;
            
            for (int j = 0; j < rs.size(); j++)
            {
                indice = (Integer)(rs.get(j).get("index"));
                idvar = (Integer)(rs.get(j).get("idvarmaster"));
                
                varlist.put(indice,idvar);
            }
        }
        
        return varlist;
    }
    
    public static String getAcMasterTable(String lang, int idsite, int scrHeight, int scrWidth) throws DataBaseException
    {
        StringBuffer table = new StringBuffer();
        MasterBeanList mList = new MasterBeanList();
        LangService l = LangMgr.getInstance().getLangService(lang);
        
        int table_width = scrWidth - OFFSET_WIDTH;
        int n_max_vars = (new AcProperties()).getProp("ac_maxvariable");

        // recupero devices potenzialmente master:
        String sql = "select cfdevice.iddevice,cftableext.description,cfdevmdl.code as devmdlcode from cfdevice,cftableext,cfdevmdl where " +
            "cfdevice.iddevice in (select iddevice from cfdevice where iddevmdl in (select iddevmdl from cfdevmdl where code in " + 
            "(select distinct code from ac_master_mdl)) and idsite="+idsite+" and iscancelled='FALSE') and " +
            "cftableext.tablename='cfdevice' and cftableext.languagecode='"+lang+"' and cftableext.idsite="+idsite+" and cftableext.tableid=cfdevice.iddevice " +
            "and cfdevmdl.iddevmdl=cfdevice.iddevmdl and cfdevice.idsite=cftableext.idsite order by cftableext.description";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);

        if ((rs != null) && (rs.size() > 0))
        {
            //recupero iddevice dei devs gi� settati come slave:
            HashMap<Integer,Integer> id_slaves = new HashMap<Integer,Integer>();
            
            String sql_slaves = "select distinct iddevslave from ac_slave";
            
            RecordSet rs_slaves = DatabaseMgr.getInstance().executeQuery(null, sql_slaves, null);
            
            if ((rs_slaves != null) && (rs_slaves.size() > 0))
            {
                for (int i = 0; i < rs_slaves.size(); i++)
                {
                    id_slaves.put(new Integer(i),(Integer)(rs_slaves.get(i).get(0)));
                }
            }
            
            Object[][] devices = new Object[rs.size()][2];
            String[] codes = new String[rs.size()];

            for (int i = 0; i < devices.length; i++)
            {
            	devices[i][0] = (Integer)rs.get(i).get("iddevice");
            	devices[i][1] = (String)rs.get(i).get("description");
                codes[i] = (String)rs.get(i).get("devmdlcode");
            }

            
            //costruzione table
            table.append("<div style='width:"+(table_width+VarDetailHelper.VSCROLLBAR_WIDTH)+"px'>");
            table.append("<table class='table' border='0' width='"+table_width+"px' cellspacing='1' cellpadding='1'>\n");
            table.append("<tr class='th'>");
            table.append("<td align='center' width='*'><b>"+l.getString("ac","device")+"</b></td>");
            table.append("<td align='center' style='width:100px;'><b>"+l.getString("ac","enabled")+"</b></td>");
            
            for (int i = 0; i < n_max_vars; i++)
            {
                table.append("<td align='center' style='width:100px'><b>"+AcMaster.variables[i]+"</td>");
            }

            table.append("</tr>");
            table.append("</table>\n");
            table.append("</div>");
            table.append("<div style='width:"+(table_width+VarDetailHelper.VSCROLLBAR_WIDTH)+"px;height:260pt; overflow:auto;'>" +
            		"<table class='table' border='0' width='"+table_width+"px' cellspacing='1' cellpadding='1'>\n");
            
            Object[] tmp = null;
            String ids = "";
            String grpName = "";
            boolean checked = false;
            boolean slave = false;
            MasterBean m = null;

            for (int i = 0; i < devices.length; i++)
            {
                tmp = devices[i];
                ids = ids + tmp[0] + ";";
                m = mList.getMasterById((Integer)tmp[0]);
                checked = (m != null) ? true : false;
                slave = false;
                
                //table.append("<tr class='Row1' onMouseOver='this.className=\"selectedRow\"' onMouseOut='this.className=\"Row1\"'>\n");
                table.append("<tr class='"+(i%2==0?"Row1":"Row2")+"'>\n");
                
                //table.append("<td class='standardTxt'>" + tmp.getDescription() + "</td>\n");
                table.append("<td class='standardTxt' width='*'>" + tmp[1]);
                
                if (id_slaves.containsValue((Integer)tmp[0]))
                {
                    table.append("</td>\n");
                    table.append("<td class='standardTxt' align='center' style='color:RED;height:22px;width:100px;'>"+SLAVE+"</td>\n");
                    slave = true;
                    checked = false;
                }
                else
                {
                    //se il device � settabile come master, allora mostro il corrispondente nome gruppo:
                    grpName = AcMdl.getGroupName(((Integer)(tmp[0])).intValue(), lang);
                    
                    if (checked)
                    {
                        table.append("<div>&nbsp;<b><u>"+l.getString("ac","banco")+"</u>:&nbsp;"+grpName+"</b></div>");
                    }
                    
                    table.append("<input type='hidden' name='grpname_" + tmp[0] + "' value='"+grpName+"' />\n");
                    
                    table.append("</td>\n");
                    table.append("<td class='standardTxt' style='width:100px' align='center'>");
                    table.append("<input " + (checked ? "checked" : "") + " class='standardTxt' onclick='check_dev(this," + tmp[0] + ");' type='checkbox' id='ch_" + tmp[0] + "' name='ch_" + tmp[0] + "'/>");
                    table.append("</td>\n");
                }
                
                //table.append("<td class='standardTxt'>"+getMasterVar(tmp.getIddevice(), codes[i], lang, checked, m)+"</td>\n");
                table.append(getMasterVar(((Integer)(tmp[0])).intValue(), codes[i], lang, idsite, checked, m, slave)+"\n");
                table.append("</tr>\n");
            }
            
            if ((ids != null) && !ids.equals(""))
            {
                ids = ids.substring(0, ids.length() - 1);
            }
            
            table.append("</table></div>");
            table.append("<input type='hidden' id='ids_master' name='ids_master' value='" + ids + "'/>");
            return table.toString();
        }
        else
        {
            String ritorno = "<p class='tdTitleTable'>" + l.getString("ac", "nomaster") + "</p>";
            return ritorno;
        }
    }

    private static String getMasterVar(int iddevice, String code, String lang, int idsite, boolean checked, MasterBean m, boolean slave) throws DataBaseException
    {
        
        int n_mstr_vars = (new AcProperties()).getProp("ac_maxvariable");
        
        String sql = "select idvariable,description,index,def,min,max from cfvariable,cftableext,ac_master_mdl where " +
                "cfvariable.iddevice="+iddevice+" and cftableext.tablename='cfvariable' and cftableext.tableid=cfvariable.idvariable and languagecode='"+lang+"' and " +
                "cfvariable.code=ac_master_mdl.vcode and ac_master_mdl.code='"+code+"' and cfvariable.idhsvariable is not null and " +
                "cfvariable.iscancelled='FALSE' and cftableext.idsite="+idsite+" and cfvariable.idsite=cftableext.idsite order by index";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        
        //StringBuffer html_vars = new StringBuffer("<table width='100%' border='0' cellspacing='1' cellpadding='1'>");
        StringBuffer html_vars = new StringBuffer("");
        
        Record r = null;
        String ids = "";
        String id = "";
        String def = "";
	//20091201-simon add
	//it will disable the calendar input box when the virtual Keyboard is open
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        
        int k = 0;
        int indice = 0;
        
        //for (int i = 0; i < rs.size(); i++)
        for (int i = 0; i < n_mstr_vars; i++)
        {
            if (k < rs.size())
            {
                indice = ((Integer)rs.get(k).get("index")).intValue();
            }
            else indice = 0;
            
            if (indice == (i+1))
            {
                r = rs.get(k);
                id = r.get("idvariable").toString();
                ids = ids + id + ";";
                def = checked ? Float.toString(m.getVariableDef(new Integer(id)).getDef_value()) : r.get("def").toString(); //se checked prendo da tabella master,altrimenti da modelli master
                //html_vars.append("<tr>");
                //html_vars.append("<td  class='standardTxt' width='85%'>" + r.get("description").toString() + "</td>");
                //html_vars.append("<td  class='standardTxt' width='85%'>" + variables[i] + "</td>");
                html_vars.append("<td class='standardTxt' style='width:100px' align='center'>");
                
                if (! slave)
                {
                    html_vars.append("<input disabled id='def_" + id + "' name='def_" + id + "' maxlength='30' size='5' class='"+(OnScreenKey?"keyboardInput":"standardTxt")+"' type='text' value='" + def + "' onkeydown='checkOnlyAnalog(this,event);' onclick='enableAction(1);' onchange='enableAction(1);' />\n");
                }
                else
                {
                    html_vars.append("<span style='color:RED'> &nbsp; </span>");
                }
                
                html_vars.append("<input type='hidden' value='" + r.get("index").toString() + "' id='ind_" + id + "' name='ind_" + id + "' />\n");
                html_vars.append("<input type='hidden' value='" + r.get("min").toString() + "' id='min_" + id + "' />\n");
                html_vars.append("<input type='hidden' value='" + r.get("max").toString() + "' id='max_" + id + "' />\n");
                html_vars.append("</td>\n");
                //html_vars.append("</tr>");
                k++;
            }
            else
            {
                html_vars.append("<td class='standardTxt' style='width:100px'>&nbsp;</td>\n");
            }
        }

        if ((ids != null) && !ids.equals(""))
        {
            ids = ids.substring(0, ids.length() - 1);
        }

        //html_vars.append("</table>\n");
        html_vars.append("<input type='hidden' id='vars_" + iddevice + "' name='vars_" + iddevice + "' value='" + ids + "'");

        return html_vars.toString();
    }

    public static void saveAcMasters(Properties prop)
    {
        String s_ids_dev = prop.getProperty("ids_master");

        if ((s_ids_dev != null) && (!s_ids_dev.equalsIgnoreCase("")))
        {
            String[] ids = s_ids_dev.split(";");
            int id = -1;

            for (int i = 0; i < ids.length; i++)
            {
                id = Integer.parseInt(ids[i]);

                if (prop.getProperty("ch_" + id) != null)
                {
                    try
                    {
                        addMaster(id, prop);
                    }
                    catch (Exception e)
                    {
                        // PVPro-generated catch block:
                        Logger logger = LoggerMgr.getLogger(AcMaster.class);
                        logger.error(e);
                    }
                }
                else
                {
                    //eliminazione master
                    try
                    {
                        removeMaster(id);
                    }
                    catch (Exception e)
                    {
                        // PVPro-generated catch block:
                        Logger logger = LoggerMgr.getLogger(AcMaster.class);
                        logger.error(e);
                    }
                }
            }
        }
    }

    public static void saveEnabMasters(Properties prop)
    {
        int n_master = Integer.parseInt(prop.getProperty("n_master"));
        
        int k=0;
        String params = "";
        
        for (int i = 0; i < n_master; i++)
        {
            if (prop.getProperty("mstr_"+(i+1)) != null)
            {
                params = params + prop.getProperty("mstr_"+(i+1)) + ",";
                k++;
            }
        }
        
        // disabilito tutti i master:
        String sql_del = "update ac_master set enabled=0";
        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql_del, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMaster.class);
            logger.error(e);
        }
        
        //se almeno una checkbox � stata o � rimasta selezionata, allora:
        if (!"".equals(params))
        {
            params = params.substring(0, params.length() - 1);
            
            // abilito i master selezionati nel SubTab2:
            String sql_upd = "update ac_master set enabled=1 where iddevmaster in ("+params+")";
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql_upd, null);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(AcMaster.class);
                logger.error(e);
            }
        }
    }
    
    public static void updGroupsNames(Properties prop)
    {
        int id_dev = -1;
        boolean fatto = false;
        
        try
        {
            MasterBeanList mbl = new MasterBeanList();
            
            for (int i = 0; i < mbl.list.size(); i++)
            {
                id_dev = mbl.list.get(i).getIddev();
                fatto = AcMdl.updGroupName(id_dev, prop.getProperty("grp_" + id_dev));
            }
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMaster.class);
            logger.error(e);
        }
    }
    
    private static void addMaster(int id_dev, Properties prop) throws DataBaseException
    {
        String s_ids_vars = prop.getProperty("vars_" + id_dev);

        MasterBeanList mbl = new MasterBeanList();
        Integer isdevenab = null;
        if ((mbl != null) && (mbl.getMasterById(id_dev) != null))
            isdevenab = mbl.getMasterById(id_dev).getEnabled();
        else isdevenab = new Integer(0);
        
        //eliminazione eventuali record precendenti piuttosto che fare update
        String sql = "delete from ac_master where iddevmaster=?";
        DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(id_dev)});
        
        if ((s_ids_vars != null) && !s_ids_vars.equalsIgnoreCase(""))
        {
            String[] ids = s_ids_vars.split(";");
            int id = -1;
            String s_def = "";
            float def = 0.0f;
            Integer index = null;
            Object[][] params = new Object[ids.length][5];
            Object[][] params_upd = new Object[ids.length][3];

            for (int i = 0; i < ids.length; i++)
            {
                id = Integer.parseInt(ids[i]);
                s_def = prop.getProperty("def_" + id);
                index = new Integer(prop.getProperty("ind_" + id));
                def = java.lang.Float.parseFloat(s_def);
                params[i][0] = new Integer(id_dev);
                params[i][1] = new Integer(id);
                params[i][2] = index;
                params[i][3] = def;
                params[i][4] = isdevenab;
                
                params_upd[i][0] = def;
                params_upd[i][1] = new Integer(id_dev);
                params_upd[i][2] = new Integer(id);
            }

            sql = "insert into ac_master values (?,?,?,?,?)";
            DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
            
            // quando modifico il valore di appoggio lo propago sulla tabella slave 
            String sql_update = "update ac_slave set def=? where iddevmaster=? and idvarmaster=?";
            DatabaseMgr.getInstance().executeMultiStatement(null, sql_update, params_upd);
            
            boolean fatto = AcMdl.setGroupName(id_dev, prop.getProperty("grpname_" + id_dev));
        }
    }

    public static void removeMaster(int id_dev) throws DataBaseException
    {
        String sql_slave = "delete from ac_slave where iddevmaster=?";
        DatabaseMgr.getInstance().executeStatement(null, sql_slave, new Object[]{new Integer(id_dev)});
        
        String sql_master = "delete from ac_master where iddevmaster=?";
        DatabaseMgr.getInstance().executeStatement(null, sql_master, new Object[]{new Integer(id_dev)});
        
        boolean fatto = AcMdl.delGroupName(id_dev);
    }
    
    public static void updateAllMastersOfModel(String devCode, int siteId) throws DataBaseException
    {
        Integer idSite = new Integer(siteId);
        
        //recupero device gi� master:
        String sql_1 = "select distinct iddevmaster from ac_master where iddevmaster IN (select iddevice from cfdevice where " +
            " iddevmdl=(select iddevmdl from cfdevmdl where code=? and idsite=?) and idsite=?)";
        
        Object[] params_1 = new Object[] {devCode, idSite, idSite};
        
        RecordSet rs_1 = DatabaseMgr.getInstance().executeQuery(null, sql_1, params_1);
        
        //solo se ho gi� dei master del modello appena modificato, allora:
        if ((rs_1 != null) && (rs_1.size() > 0))
        {
            //recupero nuovi valori dal modello appena aggiornato:
            String sql_2 = "select vcode, index, def from ac_master_mdl where code=? order by index";
            Object[] params_2 = new Object[] {devCode};
            RecordSet rs_2 = DatabaseMgr.getInstance().executeQuery(null, sql_2, params_2);
            
            String sql_3 = "delete from ac_master where iddevmaster=?";
            
            String sql_4 = "insert into ac_master values (?,(select idvariable from cfvariable where iddevice=? and code=? and idsite=? and idhsvariable is not null),?,?,?)";
            Object[] params_4 = new Object[7];
            
            Integer devmstr = null;
            
            //per ogni dev master:
            for (int i = 0; i < rs_1.size(); i++)
            {
                devmstr = (Integer)rs_1.get(i).get("iddevmaster");
                
                //elimino valori precedenti:
                DatabaseMgr.getInstance().executeStatement(null, sql_3, new Object[] {devmstr});
                
                //per ogni nuova var configurata nel modello:
                for (int j = 0; j < rs_2.size(); j++)
                {
                    params_4[0] = devmstr;
                    params_4[1] = devmstr;
                    params_4[2] = rs_2.get(j).get("vcode").toString();
                    params_4[3] = idSite;
                    params_4[4] = (Integer)rs_2.get(j).get("index");
                    params_4[5] = (Float)rs_2.get(j).get("def");
                    params_4[6] = new Integer(0); //disabilito il master modificato (al momento � senza slaves)
                    
                    //inserisco i nuovi val per il device in base al nuovo modello:
                    DatabaseMgr.getInstance().executeStatement(null, sql_4, params_4);
                }
            }
        }
    }
    
    public static void removeAllMastersByCode(String devCode, int siteId) throws DataBaseException
    {
        Integer idSite = new Integer(siteId);
        Object[] params = new Object[] {devCode,idSite,"FALSE",idSite};
        
        String sql_slave = "delete from ac_slave where iddevmaster IN (select iddevice from cfdevice where iddevmdl=(select iddevmdl from cfdevmdl where code=? and idsite=?) and iscancelled=? and idsite=?)";
        
        String sql_master = "delete from ac_master where iddevmaster IN (select iddevice from cfdevice where iddevmdl=(select iddevmdl from cfdevmdl where code=? and idsite=?) and iscancelled=? and idsite=?)";
        
        DatabaseMgr.getInstance().executeStatement(null, sql_slave, params);
        
        DatabaseMgr.getInstance().executeStatement(null, sql_master, params);
    }
    
    public static void removeAllSlavesOfMaster(String devCode, int siteId) throws DataBaseException
    {
        Integer idSite = new Integer(siteId);
        Object[] params = new Object[] {devCode,idSite,"FALSE",idSite};
        
        String sql_slave = "delete from ac_slave where iddevmaster IN (select iddevice from cfdevice where iddevmdl=(select iddevmdl from cfdevmdl where code=? and idsite=?) and iscancelled=? and idsite=?)";
        
        DatabaseMgr.getInstance().executeStatement(null, sql_slave, params);
    }
}
