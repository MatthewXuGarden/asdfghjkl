package com.carel.supervisor.presentation.ac;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.ac.AcProcess;
import com.carel.supervisor.director.ac.AcProperties;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;


public class AcMdl
{
    public static String getComboDevice(int idsite, String language,int idDevMdl)
    {
        DevMdlBeanList l = new DevMdlBeanList();
        StringBuffer html = new StringBuffer();
        try
        {
            // invocation with the new parameter, to hide 'Internal IO' model
        	// Nicola Compagno 25032010
        	l.retrieve(idsite, language, true);
        }
        catch (DataBaseException e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
        }
        if (l!=null && l.size()>0)
        {
        	
        	html.append("<select class='standardTxt' id='dev_combo' name='dev_combo' onchange='loadVar(this);' >\n");
        	html.append("<option value='-1'>-------------------------</option>\n");
        	DevMdlBean tmp = null;
        	for (int i=0;i<l.size();i++)
        	{
        		tmp = l.getMdlBean(i);
        		html.append("<option value='"+tmp.getIddevmdl()+"'");
                if (idDevMdl == tmp.getIddevmdl()) html.append(" selected ");
                html.append(">"+tmp.getDescription()+"</option>\n");
                //html.append("<option value='"+tmp.getCode()+"'>"+tmp.getDescription()+"</option>\n");
        	}
        	html.append("</select>");
        }
        return html.toString();
    }

    //recupera il nome del gruppo associato al master se esiste, oppure il nome del device master:
    public static String getGroupName(int iddevmaster, String lang)
    {
        String grpName = "";
        
        String sql = "select descr_grp from ac_groups where iddevmaster=?";
        RecordSet rs = null;
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {new Integer(iddevmaster)});
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            grpName = rs.get(0).get("descr_grp").toString();
        }
        else
        {
            String sql_2 = "select description from cftableext where languagecode='"+lang+"' and tablename='cfdevice' and tableid="+iddevmaster+" and idsite=1";
            
            try
            {
                rs = DatabaseMgr.getInstance().executeQuery(null, sql_2, null);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(AcMdl.class);
                logger.error(e);
            }
            
            if ((rs != null) && (rs.size() > 0))
            {
                grpName = rs.get(0).get("description").toString();
            }
        }
        
        return grpName;
    }
    
    //recupera il nome del gruppo se esiste:
    public static String getGroupName(int iddevmaster)
    {
        String grpName = "";
        
        String sql = "select descr_grp from ac_groups where iddevmaster=?";
        RecordSet rs = null;
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {new Integer(iddevmaster)});
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            grpName = rs.get(0).get("descr_grp").toString();
        }
        
        return grpName;
    }
    
    //solo se non esiste gi� inserisce uno nuovo nome gruppo:
    public static boolean setGroupName(int iddevmaster, String groupName)
    {
        boolean fatto = false;
        
        if ("".equals(getGroupName(iddevmaster)))
        {
            String sql_ins = "insert into ac_groups values (?, ?)";
            Object[] params_ins = new Object[] {new Integer(iddevmaster), groupName};
            
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql_ins, params_ins, fatto);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(AcMdl.class);
                logger.error(e);
                
                fatto = false;
            }
            
            fatto = true;
        }
        
        return fatto;
    }
    
    //elimina un nome gruppo:
    public static boolean delGroupName(int iddevmaster)
    {
        boolean fatto = true;
        
        String sql = "delete from ac_groups where iddevmaster=?";
        Object[] param = new Object[]{new Integer(iddevmaster)};
        
        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, param, fatto);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
            
            fatto = false;
        }
        
        return fatto;
    }
    
    //aggiorna una nome gruppo:
    public static boolean updGroupName(int iddevmaster, String newGrpName)
    {
        boolean fatto = true;
        
        String sql = "update ac_groups set descr_grp=? where iddevmaster=?";
        Object[] param = new Object[]{newGrpName, new Integer(iddevmaster)};
        
        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, param, fatto);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
            
            fatto = false;
        }
        
        return fatto;
    }
    
    //recupera i codes delle vars Tglass e Output associate al device (non devmdl):
    public static String[] getExtraVarsCodes(int iddev)
    {
        String[] varcode = new String[] {"",""};
        
        int indice = -1;
        RecordSet rs = null;
        
        String sql = "select index,varcode from ac_extra_vars where devcode=(select code from cfdevmdl where " +
            " iddevmdl=(select iddevmdl from cfdevice where iddevice="+iddev+" and idsite=1) and idsite=1) order by index";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int j = 0; j < rs.size(); j++)
            {
                // 1=Tglass; 2=Output.
                if (rs.get(j).get("varcode") != null)
                {
                    indice = ((Integer)rs.get(j).get("index")).intValue();
                    varcode[indice-1] = rs.get(j).get("varcode").toString();
                }
            }
        }
        
        return varcode;
    }
    
    //recupera gli idvariable delle vars Tglass e Output associate al device:
    public static int[] getExtraVarsIds(int iddev)
    {
        int[] ids = new int[] {-1,-1};
        
        String[] varscodes = getExtraVarsCodes(iddev);
        
        RecordSet rs = null;
        String sql = "select code,idvariable from cfvariable where iddevice="+iddev+" AND (code='"+varscodes[0]+"' or code='"+varscodes[1]+"') and idsite=1 and idhsvariable is not null";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcMdl.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int i = 0; i < rs.size(); i++)
            {
                // Tglass:
                if (varscodes[0].equals(rs.get(i).get("code").toString()))
                {
                    ids[0] = ((Integer)rs.get(i).get("idvariable")).intValue();
                }
                
                // Output:
                if (varscodes[1].equals(rs.get(i).get("code").toString()))
                {
                    ids[1] = ((Integer)rs.get(i).get("idvariable")).intValue();
                }
            }
        }
        
        return ids;
    }
    
    public static String getComboCus(int idsite, String language)
    {
        StringBuffer combo = new StringBuffer();
        
        LangService lan = LangMgr.getInstance().getLangService(language);
        String abilitate = lan.getString("ac","abilitate");
        String disabilitate = lan.getString("ac","disabilitate");
        
        int n_max_vars = (new AcProperties()).getProp("ac_maxvariable");
        
        //combo.append("<input type='hidden' id='cusstatus' name='cusstatus' value='"+(n_max_vars>3?"enab":"disab")+"' />");
        combo.append("<select id='cusenab' name='cusenab' onchange='xSave();enableAction(1);'>\n");
        
        if (n_max_vars > 3)
        {
            combo.append("<option value='enab' selected>");
            combo.append(abilitate);
            combo.append("</option>\n");
            combo.append("<option value='disab'>"+disabilitate+"</option>\n");
        }
        else
        {
            combo.append("<option value='disab' selected>");
            combo.append(disabilitate);
            combo.append("</option>\n");
            combo.append("<option value='enab'>"+abilitate+"</option>\n");
        }
        
        combo.append("</select>\n");
        
        return combo.toString();
    }
    
    /*  // by Luca B.
    public static String getAcMasterMdlTable(int iddevmdl, String lang)
    {
        if (iddevmdl!=-1)
        {
        	return "TODO variabili del dev "+iddevmdl;
        }
        else
        {
        	return "Dispositivo non selezionato";
        }
    	
    }
    
    public static String getAcSlaveMdlTable(int iddevmdl, String lang)
    {
        return "";
    }
    */
}
