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
import com.carel.supervisor.presentation.bo.helper.VarDetailHelper;


public class AcSlave
{
    public static HashMap<Integer,Integer> getSlaveVars(Integer iddevslave)
    {
        RecordSet rs = null;
        HashMap<Integer,Integer> varlist = new HashMap<Integer,Integer>(); // Mappa: (index, idvariable)
        
        String sql = "select index, idvarslave from ac_slave_mdl, ac_slave where ac_slave.iddevslave=? and " +
            " ac_slave.idvarslave = (select idvariable from cfvariable where idsite=? and cfvariable.code=ac_slave_mdl.vcode and " +
            " cfvariable.iddevice=ac_slave.iddevslave and idhsvariable is not null) order by index";
        
        Object[] param = new Object[] {iddevslave, new Integer(1)};
        
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
                idvar = (Integer)(rs.get(j).get("idvarslave"));
                
                varlist.put(indice,idvar);
            }
        }
        
        return varlist;
    }
    
    public static String getAcSlaveTable(String lang, int idsite, int scrnH, int scrnW) throws DataBaseException
    {
        StringBuffer table = new StringBuffer();
        MasterBeanList mList = new MasterBeanList();
        int n_master = mList.getNumberOfMaster();
        
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String selallradio = lan.getString("ac","selectallradio");

        // recupero devices potenzialmente slave escludendo i device gi� master:
        String sql = "select cfdevice.iddevice,cftableext.description,cfdevmdl.code as devmdlcode from cfdevice,cftableext,cfdevmdl where " +
            "cfdevice.iddevice in (select iddevice from cfdevice where iddevmdl in " +
            "(select iddevmdl from cfdevmdl where code in (select distinct code from ac_slave_mdl)) and idsite="+idsite+") " +
            "and cfdevice.iscancelled='FALSE' and cftableext.tablename='cfdevice' and cftableext.languagecode='"+lang+"' " + 
            "and cftableext.tableid=cfdevice.iddevice and cftableext.idsite="+idsite+" and cfdevmdl.iddevmdl=cfdevice.iddevmdl and " + 
            "cfdevice.idsite=cftableext.idsite and cfdevice.iddevice not in (select iddevmaster from ac_master) order by cftableext.description";

        int table_width = scrnW - VarDetailHelper.OFFSET_WIDTH;
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        
        if (rs != null && rs.size() > 0)
        {
            AcSlaveMasterPres l = new AcSlaveMasterPres();
            
            Object[] tmp = null;
            String ids = "";

            table.append("<div style='width:"+(table_width+VarDetailHelper.VSCROLLBAR_WIDTH)+"px'>");
            table.append("<table class='table' width='"+table_width+"px'  cellpadding='1' cellspacing='1'>");

            // TABLE HEADER
            table.append("<tr class='th'>");
            table.append("<td width='*' class='td' align='center'><b>Slaves</b></td>");

            int i = 0;
            for (;i < n_master; i++)
            {
                table.append("<td style='width:80px' align='center'><b>G" + (i + 1) + "</b> &nbsp; <input type='radio' title='"+selallradio+"' name='Master' value='' onclick='select_all_radio("+mList.getMasterByIndex(i).getIddev()+","+(i+1)+");' />");
                table.append("<input type='hidden' id='radio_mstr"+(i+1)+"' value='false' />");
                table.append("</td>");
            }

            table.append("<td style='width:80px' align='center'><b>N/A</b> &nbsp; <input type='radio' title='"+selallradio+"' name='Master' id='radio_mstr0' value='' onclick='select_all_radio(-1,"+(i+1)+");' />");
            table.append("<input type='hidden' id='radio_mstr"+(i+1)+"' value='false' />");
            table.append("</td>");
            table.append("</tr>");
            
            table.append("</table>\n");
            table.append("</div>");
            table.append("<div style='width:"+(table_width+VarDetailHelper.VSCROLLBAR_WIDTH)+"px;height:200pt; overflow:auto;'>" +
            		"<table width='"+table_width+"px' id='ac_slaves' name='ac_slaves' class='table' cellpadding='1' cellspacing='1'>");
            
            // TABLE BODY 
            // for i: ciclo sugli slave e creo le righe
            String selected = "";
            boolean associato = false;
            int idmaster = -1;
            for (i = 0; i < rs.size(); i++)
            {
                tmp = new Object[2];
                tmp[0] = (Integer)rs.get(i).get("iddevice");
                tmp[1] = (String)rs.get(i).get("description");
                
                ids = ids + tmp[0] + ";";
                
                //table.append("<tr class='Row1' onMouseOver='this.className=\"selectedRow\"' onMouseOut='this.className=\"Row1\"'>\n");
                table.append("<tr class='"+(i%2==0?"Row1":"Row2")+"'>\n");
                table.append("<td  class='standardTxt' width='*'>" + tmp[1] + "</td>");
                
                associato = false;
                
                // for j:ciclo sul numero di master e creo n colonne
                for (int j = 0; j < n_master; j++)
                {
                    idmaster = mList.getMasterByIndex(j).getIddev();
                    if (l.getMasterId(((Integer)tmp[0]).intValue()) == idmaster)
                    {
                        selected = "checked";
                        associato = true;
                    }
                    else
                    {
                        selected = "";
                    }
                    table.append("<td class='standardTxt' style='width:80px' align='center'><input "+selected+" type='radio' name='sl_" + tmp[0] + "' value='"+ idmaster + "' /></td>");
                }

                table.append("<td class='standardTxt' style='width:80px' align='center'><input "+(!associato?"checked":"")+" type='radio' name='sl_" + tmp[0] + "' value='na'/></td>");
                table.append("</tr>");
            }

            table.append("</table></div>");

            if ((ids != null) || !ids.equals(""))
            {
                ids = ids.substring(0, ids.length() - 1);
            }
            
            //lista degli iddev degli slaves:
            table.append("<input type='hidden' id='ids_slave' name='ids_slave' value='" + ids + "'/>");
            
            //numero dei master (escluso "N/A"):
            table.append("<input type='hidden' id='n_master' name='n_master' value='" + n_master + "'/>");

            return table.toString();
        }
        else
        {
            LangService l = LangMgr.getInstance().getLangService(lang);
            return "<p class='tdTitleTable'>"+l.getString("ac","noslave")+"</p>";
        }
            
        
    }

    public static boolean saveAcSlaves(Properties prop,String lang) throws NumberFormatException, DataBaseException
    {
        String s_ids_dev = prop.getProperty("ids_slave");
        boolean stato_ok = true;
        boolean stato;

        if ((s_ids_dev != null) && !s_ids_dev.equalsIgnoreCase(""))
        {
            String[] ids = s_ids_dev.split(";");
            int id = -1;
            String master = "";

            // ciclo su tutti gli slave per vedere a che master sono associati
            for (int i = 0; i < ids.length; i++)
            {
                id = Integer.parseInt(ids[i]);
                master = prop.getProperty("sl_"+ids[i]);

                if (master.equalsIgnoreCase("na"))
                {
                    removeSlave(id);
                }
                else
                {
                    removeSlave(id);
                    stato = addSlave(id,Integer.parseInt(master));
                    if (stato_ok) stato_ok = stato;
                }
            }
        }
        return stato_ok;
    }

    public static void removeSlave(int idslave)
    {
        String sql = "delete from ac_slave where iddevslave=?";

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Integer(idslave)});
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcSlave.class);
            logger.error(e);
        }
    }
    
    public static void removeAllSlavesByCode(String devCode, int siteId) throws DataBaseException
    {
        Integer idSite = new Integer(siteId);
        
        String sql = "delete from ac_slave where iddevslave IN (select iddevice from cfdevice where iddevmdl IN (select iddevmdl from cfdevmdl where code=? and idsite=?) and iscancelled=? and idsite=?)";
        
        Object[] params = new Object[] {devCode,idSite,"FALSE",idSite};
        
        DatabaseMgr.getInstance().executeStatement(null, sql, params);
    }
    
    private static boolean addSlave(int idslave, int idmaster) throws DataBaseException
    {
                
        // RETRIEVE RIGHE DA AC_SLAVE_MDL 
        String sql = "select index,vcode from ac_slave_mdl where code = (select cfdevmdl.code from cfdevmdl,cfdevice where cfdevice.iddevice=? and cfdevice.idsite=? and cfdevmdl.iddevmdl=cfdevice.iddevmdl) order by index";
        
        Object[] param = new Object[2];
        param[0] = new Integer(idslave);
        param[1] = new Integer(1);
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
        
        // ARRAY INDICI X LINK FRA MASTER E SLAVE + ARRAY DEI CODE VARIABILI SLAVE DEL DEVICE SLAVE
        Integer[] index_slave = new Integer[rs.size()];
        String[] code_var_slave = new String[rs.size()];
        Integer[] ids_var_slave = new Integer[rs.size()];
        
        Record r = null;
        for (int i=0; i < rs.size(); i++)
        {
            r = rs.get(i);
            index_slave[i] = ((Integer)r.get("index"));
            code_var_slave[i] = ((String)r.get("vcode"));
        }
        
        // RETRIEVE IDVARIABLE SLAVE DA CFVARIABLE BY CODE
        sql = "select idvariable from cfvariable where iddevice=? and code=? and iscancelled=? and idsite=? and idhsvariable is not null ";
        
        param = new Object[4];
        for (int i=0; i < code_var_slave.length; i++)
        {
            param[0] = new Integer(idslave);
            param[1] = code_var_slave[i];
            param[2] = "FALSE";
            param[3] = new Integer(1);
            
            rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
            
            ids_var_slave[i] = (Integer) rs.get(0).get(0);
        }
        
        // RETRIEVE RIGHE UTILI DA AC_MASTER_MDL
        sql = "select index,vcode,def,min,max,alarm from ac_master_mdl where code = (select cfdevmdl.code from cfdevmdl,cfdevice where cfdevice.iddevice=? and cfdevice.idsite=? and cfdevmdl.iddevmdl=cfdevice.iddevmdl) order by index";
        
        param = new Object[2];
        param[0] = new Integer(idmaster);
        param[1] = new Integer(1);
        
        rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
        
        Integer[] index_master = new Integer[rs.size()];
        String[] code_var_master = new String[rs.size()];
        String[] code_alr_master = new String[rs.size()];
        Integer[] ids_var_master = new Integer[rs.size()];
        Integer[] ids_alr_master = new Integer[rs.size()];
        Float[] def = new Float[rs.size()];
        Float[] min = new Float[rs.size()];
        Float[] max = new Float[rs.size()];
        
        r = null;
        for (int i=0; i < rs.size(); i++)
        {
            r = rs.get(i);
            index_master[i] = ((Integer)r.get("index"));
            code_var_master[i] = r.get("vcode").toString();
            code_alr_master[i] = r.get("alarm").toString();
            def[i] = (Float)r.get("def");
            min[i] = (Float)r.get("min");
            max[i] = (Float)r.get("max");
            
        }
        
        // RETRIEVE IDVARIABLE MASTER DA CFVARIABLE BY CODE
        sql = "select idvariable from cfvariable where iddevice=? and code=? and iscancelled=? and idsite=? and idhsvariable is not null ";
        
        param = new Object[4];
        for (int i=0; i < code_var_master.length; i++)
        {
            param[0] = new Integer(idmaster);
            param[1] = code_var_master[i];
            param[2] = "FALSE";
            param[3] = new Integer(1);
            
            rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
            
            ids_var_master[i] = (Integer) rs.get(0).get(0);
        }
        
        // RETRIEVE IDVARIABLE ALARM MASTER DA CFVARIABLE BY CODE
        sql = "select idvariable from cfvariable where iddevice=? and code=? and iscancelled=? and idsite=? and idhsvariable is not null ";
        
        param = new Object[4];
        for (int i=0; i < code_alr_master.length; i++)
        {
            param[0] = new Integer(idmaster);
            param[1] = code_alr_master[i];
            param[2] = "FALSE";
            param[3] = new Integer(1);
            
            rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
            
            if ((rs != null) && (rs.size() > 0))
            {
                //se ho impostato un allarme x la var:
                ids_alr_master[i] = (Integer) rs.get(0).get(0);
            }
            else
            {
                //se non ho impostato un allarme x la var:
                ids_alr_master[i] = new Integer(-1);
            }
        }
        
        //SCORRO INDEX SLAVE E SE CORRISPONDE UN INDEX MASTER SALVO
        sql = "insert into ac_slave values (?,?,?,?,?,?,?,?)";
        
        param = new Object[8];
        
        boolean inserito = false;
        
        for (int i = 0; i < index_slave.length; i++)
        {
            for (int j = 0; j < index_master.length; j++)
            {
                if (index_slave[i].intValue() == index_master[j].intValue())
                {
                    param[0] = new Integer(idmaster);
                    param[1] = new Integer(ids_var_master[j]);
                    param[2] = new Integer(idslave);
                    param[3] = new Integer(ids_var_slave[i]);
                    param[4] = def[j];
                    param[5] = min[j];
                    param[6] = max[j];
                    param[7] = new Integer(ids_alr_master[j]);
                    
                    DatabaseMgr.getInstance().executeStatement(null,sql,param);
                    inserito = true;
                }
            }
        }
        return inserito;
    }
}
