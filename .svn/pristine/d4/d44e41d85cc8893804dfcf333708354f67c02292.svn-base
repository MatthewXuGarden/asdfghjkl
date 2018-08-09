package com.carel.supervisor.presentation.lucinotte;

import java.util.HashMap;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;

public class LNField
{
    public static final int DIRECT = 0;
    public static final int INVERSE = 1;
    
    public static String getHtmlFieldConf(String lang, int idSite) throws DataBaseException
    {
        StringBuffer tabella = new StringBuffer();
        
        int n_max_groups = LNGroups.MAX_GROUPS;
        int n_groups = 0;
        int k = -1;
        Integer idgr = null;
        String man = "";
        Integer dvxgr = null;
        HashMap<Integer,String> manual = new HashMap<Integer,String>();
        HashMap<Integer,Integer> dvsxgrp = new HashMap<Integer,Integer>();
        
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String gruppi = lan.getString("lucinotte", "gruppi");
        String dispositivi = lan.getString("lucinotte", "dispositivi");
        String var_digitali = lan.getString("lucinotte", "var_dig");
        String stato_on = lan.getString("lucinotte", "stato_on");
        String active = lan.getString("lucinotte", "attivo");
        String nogroups = lan.getString("lucinotte", "nogroups");
        String gruppo = lan.getString("lucinotte", "gruppo");
        
        //visualizzo i gruppi che coinvolgono almeno 1 device:
        //String sql = "select * from ln_groups where idgroup in (select distinct idgroup from ln_devsxgroup) order by idgroup";
        String sql = "select * from ln_groups order by idgroup";
        
        RecordSet rs = null;
        Record rc = null;
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int j = 0; j < rs.size(); j++)
            {
                idgr = (Integer)rs.get(j).get("idgroup");
                man = rs.get(j).get("manuale").toString();
                
                manual.put(idgr, man);
            }
            
            String sql_1 = "select idgroup, count(iddev) as dxg from ln_devsxgroup group by idgroup order by idgroup";
            
            RecordSet rs_1 = null;
            
            rs_1 = DatabaseMgr.getInstance().executeQuery(null, sql_1);
            
            if ((rs_1 != null) && (rs_1.size() > 0))
            {
                for (int w = 0; w < rs_1.size(); w++)
                {
                    idgr = (Integer)rs_1.get(w).get("idgroup");
                    dvxgr = (Integer)rs_1.get(w).get("dxg");
                    
                    dvsxgrp.put(idgr, dvxgr);
                }
            }
            
            tabella.append("<table class='table' width='95%' align='center' cellspacing='1' cellpadding='1'>\n");
            tabella.append("<tr class='th' style='height:20px'>\n");
            
            tabella.append("<td class='standardTxt' align='center' style='width:20%'><b>"+gruppi+"</b></td>\n");
            tabella.append("<td class='standardTxt' align='center' style='width:28%'><b>"+dispositivi+"</b></td>\n");
            tabella.append("<td class='standardTxt' align='center' style='width:35%'><b>"+var_digitali+"</b></td>\n");
            tabella.append("<td class='standardTxt' align='center' style='width:12%'><b>"+stato_on+"</b></td>\n");
            tabella.append("<td class='standardTxt' align='center' style='width:5%'><b>"+active+"</b></td>\n");
            
            tabella.append("</tr>");
            
            String sql_2 = "select * from ln_fieldvars order by idgroup";
            
            RecordSet rs_2 = null;
            Record rc_2 = null;
            
            rs_2 = DatabaseMgr.getInstance().executeQuery(null, sql_2);
            
            Integer idgrp = null;
            Integer iddev = null;
            Integer idvar = null;
            int statoOn = -1;
            String checked = "";
            String disabled = "";
            String disabgrp = "";
            String attivo = "";
            Integer idgrptmp = null;
            
            if ((rs_2 != null) && (rs_2.size() > 0))
            {
                n_groups = rs_2.size();
                k = 0;
            }
                
            for (int i = 1; i <= n_max_groups; i++)
            {
                tabella.append("<tr class='Row1' style='height:10px'><td class='standardTxt' colspan=5></td></tr>\n");
                
                //tabella.append("<tr class='Row1'>\n");    
                
                rc = rs.get(i-1);
                
                checked = "";
                disabled = "";
                disabgrp = "";
                idgrp = new Integer(i);
                iddev = new Integer(-1);
                idvar = new Integer(-1);
                statoOn = -1;
                attivo = "off";
                
                if ((k > -1) && (k < rs_2.size()))
                {
                    rc_2 = rs_2.get(k);
                    idgrptmp = (Integer)rc_2.get("idgroup");
                    
                    if (idgrp.equals(idgrptmp))
                    {
                        iddev = (Integer)rc_2.get("iddev");
                        statoOn = Integer.parseInt(rc_2.get("stato_on").toString());
                        attivo = rc_2.get("attivo").toString();
                        idvar = (Integer)rc_2.get("idvardig");
                        
                        k++;
                    }
                }
                
                /*
                //disabilito i settaggi x i gruppi senza devs associati:
                if ((dvsxgrp.get(idgrp) != null) && (dvsxgrp.get(idgrp).intValue() > 0))
                    disabgrp = "";
                else
                    disabgrp = "disabled";
                */
                tabella.append("<tr class='Row1' "+disabgrp+">\n");
                
                tabella.append("<td class='standardTxt'><div class='standardTxt'>&nbsp;"+gruppo+" "+idgrp.toString()+" - <span style='color:#476AB0;'><b>"+rc.get("nome_grp").toString()+"</b></span></div></td>\n");
                tabella.append("<td class='standardTxt' align='center'>"+LNDevsGroup.getDevsxGroupCombo(idgrp, iddev, lang, idSite)+"</td>\n");
                
                tabella.append("<td align='center' id='vars_grp_"+idgrp.toString()+"' class='standardTxt'>"+getDigVarsCombo(lang, idgrp, iddev, idvar, idSite)+"</td>\n");
                tabella.append("<td align='center' class='standardTxt'>"+getStatusCombo(lang, idgrp, statoOn)+"</td>\n");
                
                if ("on".equals(attivo))
                {
                    checked = "checked";
                }
                
                /*
                if ("on".equals(manual.get(idgrp)))
                {
                    disabled = "disabled";
                }
                */
                
                tabella.append("<td align='center' class='standardTxt'><input type='checkbox' "+checked+" name='active_grp_"+idgrp.toString()+"' "+disabled+"/></td>\n");
                tabella.append("</tr>\n");
            }
            
            tabella.append("<tr class='Row1' style='height:10px'><td class='standardTxt' colspan=5></td></tr>");
            tabella.append("</table>\n");
        }
        else
        {
            tabella.append("<div><b>"+nogroups+"</b></div>");
        }
        
        return tabella.toString();
    }
    
    public static String getStatusCombo(String lang, Integer ngrp, int statoOn)
    {
        StringBuffer combobox = new StringBuffer();
        
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String diretta = lan.getString("lucinotte", "diretta");
        String inversa = lan.getString("lucinotte", "inversa");
        
        String selected_d = "";
        String selected_i = "";
        String disabled = "";
        
        if ((statoOn != DIRECT) && (statoOn != INVERSE))
        {
            disabled = "disabled";
        }
        
        combobox.append("<select "+disabled+" id='statoon_grp_"+ngrp.toString()+"' name='statoon_grp_"+ngrp.toString()+"' style='width:100%'>\n");
        
        if (statoOn == DIRECT)
            selected_d = "selected";
        
        combobox.append("<option "+selected_d+" value='"+DIRECT+"'>"+diretta+"</option>\n");
        
        if (statoOn == INVERSE)
            selected_i = "selected";
        
        combobox.append("<option "+selected_i+" value='"+INVERSE+"'>"+inversa+"</option>\n");
        
        combobox.append("</select>\n");
        
        return combobox.toString();
    }
    
    public static String getDigVarsCombo(String lang, Integer idgroup, Integer iddevice, Integer idvar, Integer idSite) throws DataBaseException
    {
        StringBuffer comboVars = new StringBuffer();
        String checked = "";
        
        comboVars.append("<select id='digvar_grp_"+idgroup.toString()+"' name='digvar_grp_"+idgroup.toString()+"' style='width:100%' onchange='enable_stato("+idgroup.toString()+");'>\n");
        
        if (idvar.intValue() == -1)
            checked = "selected";
        
        comboVars.append("<option "+checked+" value='-1'> ---------- </option>\n");
        
        if (iddevice.intValue() != -1)
        {
            Integer idvariable = null;
            
            String sql = "select cfvariable.idvariable, cftableext.description from cfvariable, cftableext where " +
                          " cfvariable.iddevice=? and cfvariable.type in (1) and cftableext.tablename='cfvariable' and " +
                          " cftableext.languagecode=? and cftableext.idsite=? and cftableext.tableid=cfvariable.idvariable " +
                          " and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null order by cftableext.description";

            Object[] params = new Object[]{iddevice, lang, idSite};
            
            RecordSet rs = null;
            Record rc = null;
            
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
            
            if ((rs != null) && (rs.size() > 0))
            {
                for (int i = 0; i < rs.size(); i++)
                {
                    checked = "";
                    rc = rs.get(i);
                    idvariable = (Integer)rc.get("idvariable");
                    
                    if (idvar.equals(idvariable))
                        checked = "selected";
                    
                    comboVars.append("<option "+checked+" value='"+idvariable.toString()+"'>");
                    comboVars.append(rc.get("description").toString()+"</option>\n");
                }
            }
        }
        
        comboVars.append("</select>\n");
        
        return comboVars.toString();
    }
}
