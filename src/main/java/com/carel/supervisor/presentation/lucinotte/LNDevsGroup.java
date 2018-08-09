package com.carel.supervisor.presentation.lucinotte;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.lightnight.LightNightMgr;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;

public class LNDevsGroup
{
    public static final String img_undef = "images/event/alert.gif";
    public static final String img_error = "images/event/error.gif";
    
    public static HashMap<Integer,Object[]> devsStatus; // valori caricati dal metodo "loadDevsDtl" di questa classe!
    
    // x SubTab2:
    public static String getLNDevsxGroup(String lang, int idSite, int scrnH, int scrnW) throws DataBaseException
    {
        StringBuffer tabella = new StringBuffer();
        String checked = "";
        String lc_checked = "";
        String nt_checked = "";
        boolean in_gruppo = false;
        String ids_devs = "";
        
        int n_groups = LNGroups.MAX_GROUPS;
        
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String selallradio = lan.getString("ac","selectallradio");
        String selallluci = lan.getString("lucinotte","selectallluci");
        String selallnotte = lan.getString("lucinotte","selectallnotte");
        String luci = lan.getString("lucinotte", "luci");
        String notte = lan.getString("lucinotte", "notte");
        String devices = lan.getString("lucinotte", "dispositivi");
        String nodevs = lan.getString("lucinotte", "nodevs");

        // recupero i devices del sito:
        String sql = "select cfdevice.iddevice,cftableext.description,cfdevice.islogic from cfdevice,cftableext where " +
                     " cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=? and " +
                     " cftableext.tableid=cfdevice.iddevice and cfdevice.iscancelled='FALSE' " +
                     " order by cftableext.description";
        
        Object[] params = new Object[]{lang, new Integer(idSite)};
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        
        if ((rs != null) && (rs.size() > 0))
        {
            int n_devs = rs.size();
            
            HashMap<Integer,String> devsinfos = new HashMap<Integer,String>(); //<iddev,devdescr>
            HashMap<Integer,Integer> devsrowslist = new HashMap<Integer,Integer>(); //<iddev,posriga>
            HashMap<Integer,Integer> devslist = new HashMap<Integer,Integer>(); //<posriga,iddev>
            HashMap<Integer,String> devslcnt = new HashMap<Integer,String>(); //<iddev,luci/notte>
            
            Integer iddev = null;
            String devdescr = "";
            
            for (int i = 0; i < n_devs; i++)
            {
                if (! "FALSE".equals(rs.get(i).get("islogic")))
                {
                    iddev = new Integer(-1*((Integer)(rs.get(i).get("iddevice"))).intValue());
                }
                else
                {
                    iddev = (Integer)(rs.get(i).get("iddevice"));
                }
                
                devdescr = rs.get(i).get("description").toString();
                
                devsinfos.put(iddev, devdescr);
                
                devsrowslist.put(iddev, new Integer(i));
                
                devslist.put(new Integer(i), iddev);
                
                ids_devs += iddev + ";";
            }
            
            if ((ids_devs != null) && (!"".equals(ids_devs)))
            {
                ids_devs = ids_devs.substring(0, ids_devs.length() - 1);
            }
            
            tabella.append("<table class='table' width='"+(scrnW-125)+"px' cellpadding='1' cellspacing='1'>");

            tabella.append("<tr class='th'>");
            tabella.append("<td align='center' width='30%'><b>"+devices+"</b></td>");
            //tabella.append("<td class='th' align='center' width='1%'></td>");
            
            for (int i = 0; i < n_groups; i++)
            {
                tabella.append("<td align='center' width='7%'><b>G."+(i+1));
                tabella.append("</b>&nbsp;<input type='radio' title='"+selallradio+"' id='grp_"+(i+1)+"' name='grp_"+(i+1)+"' onclick='select_all_radio("+(i+1)+",this);' />");
                tabella.append("<input type='hidden' id='st_grp_"+(i+1)+"' value='false' /></td>");
            }

            tabella.append("<td align='center' width='7%'><b>N/A</b><input type='radio' title='"+selallradio+"' name='grp_NA' value='' onclick='allradio2na(this);' /></td>");
            
            //tabella.append("<td class='th' align='center' width='1%'></td>");
            
            tabella.append("<td align='center' width='8%'><b>"+luci+"</b><input type='radio' title='"+selallluci+"' value='' onclick='allradio2luci(this);' /></td>");
            tabella.append("<td align='center' width='8%'><b>"+notte+"</b><input type='radio' title='"+selallnotte+"' value='' onclick='allradio2notte(this);' /></td>");
            
            tabella.append("</tr>");
            tabella.append("</table>");
            
            tabella.append("<div style='height:"+(scrnH-530)+"px;overflow-X:auto;overflow-Y:auto'>");
            tabella.append("<table class='table' width='"+(scrnW-125)+"px' cellpadding='1' cellspacing='1'>");
            /*
            tabella.append("<tr class='th'>");
            tabella.append("<td align='center' width='35%'><b>"+devices+"</b></td>");
            tabella.append("<td class='Row1' align='center' width='1%'>&nbsp;</td>");
            
            for (int i = 0; i < n_groups; i++)
            {
                tabella.append("<td align='center' width='7%'><b>Grp." + (i + 1));
                tabella.append("</b> &nbsp; <input type='radio' title='"+selallradio+"' id='grp_"+(i+1)+"' name='grp_"+(i+1)+"' onclick='select_all_radio("+(i+1)+",this);' />");
                tabella.append("<input type='hidden' id='st_grp_"+(i+1)+"' value='false' /> </td>");
            }

            tabella.append("<td align='center' width='7%'><b>N/A</b> &nbsp; <input type='radio' title='"+selallradio+"' name='grp_NA' value='' onclick='allradio2na();' /></td>");
            
            tabella.append("<td class='Row1' align='center' width='1%'>&nbsp;</td>");
            
            tabella.append("<td align='center' width='8%'><b>"+luci+"</b> &nbsp; <input type='radio' title='"+selallluci+"' value='' onclick='allradio2luci();' /></td>");
            tabella.append("<td align='center' width='8%'><b>"+notte+"</b> &nbsp; <input type='radio' title='"+selallnotte+"' value='' onclick='allradio2notte();' /></td>");
            
            tabella.append("</tr>");
            */
            String sql_lcnt = "select * from ln_devlcnt";
            
            RecordSet rs_lcnt = DatabaseMgr.getInstance().executeQuery(null, sql_lcnt);
            
            if ((rs_lcnt != null) && (rs_lcnt.size() > 0))
            {
                for (int i = 0; i < rs_lcnt.size(); i++)
                {
                    devslcnt.put((Integer)rs_lcnt.get(i).get("iddev"), rs_lcnt.get(i).get("lcnt").toString());
                }
            }
            
            int[][] dxg = new int[n_devs][n_groups]; //righe=dev ; cols=grp.
            
            //recupero le associazioni dei devs ai gruppi:
            String sql_2 = "select * from ln_devsxgroup order by iddev,idgroup";
            
            RecordSet rs_2 = DatabaseMgr.getInstance().executeQuery(null, sql_2, null);
            
            if ((rs_2 != null) && (rs_2.size() > 0))
            {
                int curr_grp = -1;
                Integer curr_dev = null;
                int d = -1;
                
                for (int i = 0; i < rs_2.size(); i++)
                {
                    curr_grp = Integer.parseInt(rs_2.get(i).get("idgroup").toString());
                    curr_dev = (Integer)(rs_2.get(i).get("iddev"));
                    
                    d = devsrowslist.get(curr_dev).intValue();
                    
                    dxg[d][curr_grp - 1] = 1;
                }
            }
            
            Integer iddevice = null;
            
            // per ogni dev del sito:
            for (int i = 0; i < n_devs; i++)
            {
                tabella.append("<tr class='Row1'>\n");
                
                iddevice = devslist.get(new Integer(i));
                
                tabella.append("<td class='standardTxt' width='30%'>&nbsp;" + devsinfos.get(iddevice) + "</td>\n");
                //tabella.append("<td class='standardTxt' width='1%' align='center'>-</td>\n");
                
                lc_checked = "";
                nt_checked = "";
                
                in_gruppo = false;
                
                // per ogni gruppo:
                for (int g = 0; g < n_groups; g++)
                {
                    if (dxg[i][g] == 1)
                    {
                        checked = "checked";
                        in_gruppo = true;
                    }
                    else
                    {
                        checked = "";
                    }
                    
                    tabella.append("<td width='7%' align='center'><input "+checked+" type='checkbox' name='dev_"+iddevice.toString()+"__grp_"+(g+1)+"' id='dev_"+iddevice.toString()+"__grp_"+(g+1)+"' onclick='uncheck_na("+iddevice.toString()+");ctrl_na("+iddevice.toString()+");' value='"+i+"' /></td>");
                }
                
                tabella.append("<td width='7%' align='center'><input "+(in_gruppo?"":"checked")+" type='checkbox' name='dev_"+iddevice.toString()+"__grp_-1' id='dev_"+iddevice.toString()+"__grp_na' onclick='check_na("+iddevice.toString()+");' value='na' /></td>");
                
                //tabella.append("<td width='1%' class='standardTxt' align='center'>-</td>\n");
                
                if (devslcnt.get(iddevice) != null)
                    if ("luci".equals(devslcnt.get(iddevice)))
                    {
                        lc_checked = "checked";
                    }
                    else
                    {
                        nt_checked = "checked";
                    }
                
                tabella.append("<td width='8%' class='standardTxt' align='center'><input type='radio' "+lc_checked+" id='lcnt_dev_"+iddevice.toString()+"' name='lcnt_dev_"+iddevice.toString()+"' value='luci'></td>\n");
                tabella.append("<td width='8%' class='standardTxt' align='center'><input type='radio' "+nt_checked+" id='lcnt_dev_"+iddevice.toString()+"' name='lcnt_dev_"+iddevice.toString()+"' value='notte'></td>\n");
                
                tabella.append("</tr>\n");
            }
            
            tabella.append("</table>\n");
            tabella.append("</div>\n");
            tabella.append("<input type='hidden' id='ids_devs' name='ids_devs' value='"+ids_devs+"' />\n");
            tabella.append("<input type='hidden' id='n_groups' name='n_groups' value='"+n_groups+"' />\n");
        }
        else
        {
            tabella.append("<div><b>"+nodevs+"</b></div>\n");
        }
        
        return tabella.toString();
    }
    
    public static String getDevsxGroupCombo(Integer ngrp, Integer iddev, String lang, Integer idSite) throws DataBaseException
    {
        StringBuffer combo = new StringBuffer();
        String selected = "";
        
        combo.append("<select id='devs_grp_"+ngrp.toString()+"' name='devs_grp_"+ngrp.toString()+"' style='width:100%' onchange='loadVars(this,"+ngrp.toString()+");'>\n");
        
        if (iddev.intValue() == -1)
            selected = "selected";
        
        combo.append("<option "+selected+" value='-1'> ---------- </option>\n");
        
        /*
        String sql_0 = "select ln_devsxgroup.iddev, cftableext.description from ln_devsxgroup, cftableext where " +
                    " ln_devsxgroup.idgroup=? and cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=? and " +
                    " cftableext.tableid=ln_devsxgroup.iddev and ln_devsxgroup.iddev > 0 order by cftableext.description";
        */
        
        // recupero i devices fisici del sito:
        String sql = "select cfdevice.iddevice AS iddev,cftableext.description from cfdevice,cftableext where " +
                     " cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=? and " +
                     " cftableext.tableid=cfdevice.iddevice and cfdevice.iscancelled='FALSE' and " +
                     " cfdevice.islogic='FALSE' order by cftableext.description";
        
        //Object[] params_0 = new Object[]{ngrp, lang, idSite};
        
        Object[] params = new Object[]{lang, idSite};
        
        RecordSet rs = null;
        Record re = null;
        
        Integer iddevice = null;
        String devdescr = "";
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int i = 0; i < rs.size(); i++)
            {
                selected = "";
                re = rs.get(i);
                iddevice = (Integer)re.get("iddev");
                devdescr = re.get("description").toString();
                
                if (iddevice.equals(iddev))
                    selected = "selected";
                
                combo.append("<option "+selected+" value='"+iddevice.toString()+"'>"+devdescr+"</option>\n");
            }
        }
        
        combo.append("</select>\n");
        return combo.toString();
    }
    
    public static String getGroupDtl(String lang, int idsite, int idgrp, int scrnH, int scrnW) throws DataBaseException
    {
        //carico HashMap con dettagli dei devs del gruppo:
        loadDevsDtl(idgrp);
        
        String touchscr = "";
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        
        StringBuffer tabella = new StringBuffer();
        
        double tablew = scrnW * 0.75;
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String lcnt_grp = lan.getString("lucinotte","gruppo");
        String click2godtldev = lan.getString("ac","click2godtldev");
//        String luci = lan.getString("lucinotte","luci");
//        String notte = lan.getString("lucinotte","notte");
        String devices = lan.getString("lucinotte","dispositivi");
        String status = lan.getString("lucinotte","status");
        String groupnodevs = lan.getString("lucinotte", "groupnodevs");
        String onoff = lan.getString("lucinotte","onoff");
        
//        String[] icons_onoff = new String[2];
//        String icon_onoff = "";
        
        //carico info del gruppo:
        String sql_grp = "select * from ln_groups where idgroup=?";
        
        Object[] par = new Object[]{new Integer(idgrp)};
        
        RecordSet recset = DatabaseMgr.getInstance().executeQuery(null, sql_grp, par);
        
        String grp_name = recset.get(0).get("nome_grp").toString();
        
        tabella.append("<div class='standardTxt' align='left'>"+lcnt_grp+" "+idgrp+" - <b> "+grp_name+" </b></div>\n");
        tabella.append("<br />\n");
        
        //carico relazioni devs del gruppo:
        String sql_devs = "select ln_devsxgroup.iddev, cfdevice.address, cftableext.description from ln_devsxgroup, cfdevice, cftableext " +
        " where cfdevice.iddevice=ABS(ln_devsxgroup.iddev) and ln_devsxgroup.idgroup=? and cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=? and " +
        " cftableext.tableid=ABS(ln_devsxgroup.iddev) order by cfdevice.idline, cfdevice.address, cftableext.description";
        
        Object[] params = new Object[]{idgrp, lang, idsite};
        
        RecordSet rs = null;
        Record re = null;
        
        Integer iddevice = null;
        Integer devAddr = null;
        String devdescr = "";
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql_devs, params);
        
        if ((rs != null) && (rs.size() > 0))
        {
            tabella.append("<table class='table' width='"+tablew+"' cellpadding='1' cellspacing='1'>\n");

            if (OnScreenKey) touchscr = "onclick='goBackToLN();return false;'";
            
            tabella.append("<tr class='th' style='height:20px' "+touchscr+">\n");

            tabella.append("<td align='center' width='5%'><b>Id.</b></td>\n"); // rowspan=2
            tabella.append("<td align='center' width='8%'><b>Link</b></td>\n");// rowspan=2
            tabella.append("<td align='center' width='55%'><b>"+devices+"</b></td>\n");// rowspan=2
            tabella.append("<td align='center' width='10%'><b>"+status+"</b></td>\n");// rowspan=2
            
            //TODO: da verificare se tenere "status" oppure "full status" !?!
//            tabella.append("<td align='center' colspan=3 width='20%'><b>Full "+status+"</b></td>\n");
//            tabella.append("</tr>\n");
            
            //tabella.append("<tr class='th' style='height:20px' onclick='goBackToLN();return false;'>\n");
            
            tabella.append("<td align='center' style='width:10%'><b>"+onoff+"</b></td>\n");
//            tabella.append("<td align='center' style='width:6%'><b>"+luci+"</b></td>\n");
//            tabella.append("<td align='center' style='width:6%'><b>"+notte+"</b></td>\n");
            
            tabella.append("</tr>\n");
            tabella.append("</table>\n");
            
            tabella.append("<div style='overflow-X:auto;overflow-Y:auto;height:66%'>\n");
            tabella.append("<table class='table' width='"+tablew+"' cellpadding='1' cellspacing='1'>\n");
            
            for (int i = 0; i < rs.size(); i++)
            {
                re = rs.get(i);
                iddevice = (Integer)re.get("iddev");
                devAddr = (Integer)re.get("address");
                
                tabella.append("<tr class='Row1' style='height:20px'>\n");
                
                tabella.append("<td class='standardTxt' align='center' width='5%'>(" + devAddr.intValue() + ")</td>\n");
                
                //gestione dev fisici e logici (logici con id < 0):
                tabella.append("<td height='20px' width='8%' class='standardTxt' align='center'><input type='button' value='Link' onclick='go2DtlDev("+Math.abs(iddevice.intValue())+");return false;' title='"+click2godtldev+"'></td>\n");
                
                devdescr = re.get("description").toString();
                
                // evidenzio nome dev logico in blu:
                if (iddevice.intValue() < 0)
                {
                    devdescr = "<span style='color:BLUE;'>&nbsp;" + devdescr + "</span>";
                }
                
                tabella.append("<td width='55%' class='standardTxt'>&nbsp;" + devdescr + "</td>\n");
                
                //tabella.append("<td class='standardTxt'>" + LNUtils.getLed(iddevice) + "</td>\n");
                
                tabella.append("<td class='standardTxt' width='10%' align='center'><img src="+getDevStatus(iddevice)+" border=0 /></td>\n");
                
                tabella.append("<td class='standardTxt' width='10%' align='center'>");
                /*
                icons_onoff = getOnOff(iddevice);
                tabella.append("<img src="+icons_onoff[0]+" border=0 />");
                tabella.append("&nbsp;");
                tabella.append("<img src="+icons_onoff[1]+" border=0 />");
                */
//                icon_onoff = getOnOff(iddevice);
                tabella.append("<img src="+getOnOff(iddevice)+" border=0 />");
                
                tabella.append("</td>\n");
                
//                tabella.append("<td width='6%' class='standardTxt' align='center'><img src="+getLuci(iddevice)+" border=0 /></td>\n");
//                tabella.append("<td width='6%' class='standardTxt' align='center'><img src="+getNotte(iddevice)+" border=0 /></td>\n");
                
                tabella.append("</tr>\n");
            }
            
            tabella.append("</table>\n");
            tabella.append("</div>\n");
        }
        else
        {
            tabella.append("<div><b>"+groupnodevs+"</b></div>");
        }
        
        return tabella.toString();
    }
    
    //controllo consistenza dati modulo con stato attuale linee PVPro:
    public static boolean ctrlDevices() throws DataBaseException
    {
        boolean changed = false;
        
        //1. associazione devs ai gruppi:
        String sql_sel_dxg = "select iddev from ln_devsxgroup where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql_sel_dxg);
        
        changed = ((rs != null) && (rs.size() > 0));
        
        if (changed)
        {
            String sql_del_dxg = "delete from ln_devsxgroup where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
            DatabaseMgr.getInstance().executeStatement(sql_del_dxg, null);
        }
        
        //2. associazione vars dei devs ai gruppi:
        String sql_grp_field = "select idgroup from ln_fieldvars where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
        
        rs = null;
        rs = DatabaseMgr.getInstance().executeQuery(null, sql_grp_field);
        
        if ((rs != null) && (rs.size() > 0))
        {
            changed = true;
            
            String sql_upd_grp = "update ln_groups set campo='off' where idgroup=?";
            
            Object[][] par = new Object[rs.size()][1];
            
            for (int i = 0; i < rs.size(); i++)
            {
                par[i][0] = (Integer)rs.get(i).get("idgroup");
            }
            
            DatabaseMgr.getInstance().executeMultiStatement(null, sql_upd_grp, par);
        }
        
        //2a. associazione vars del campo ai devices:
        if (! changed)
        {
            String sql_sel_field = "select iddev from ln_fieldvars where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
            rs = null;
            rs = DatabaseMgr.getInstance().executeQuery(null, sql_sel_field);
            changed = ((rs != null) && (rs.size() > 0));
        }
        
        if (changed)
        {
            String sql_del_field = "delete from ln_fieldvars where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
            DatabaseMgr.getInstance().executeStatement(sql_del_field, null);
        }
        
        //3. associazione della funzionalità L/N ai devices:
        if (! changed)
        {
            String sql_sel_lcnt = "select iddev from ln_devlcnt where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
            rs = null;
            rs = DatabaseMgr.getInstance().executeQuery(null, sql_sel_lcnt);
            changed = ((rs != null) && (rs.size() > 0));
        }
        
        if (changed)
        {
            String sql_del_lcnt = "delete from ln_devlcnt where ABS(iddev) not in (select iddevice from cfdevice where iscancelled='FALSE')";
            DatabaseMgr.getInstance().executeStatement(sql_del_lcnt, null);
        }
        
        
        //se ho modificato la config del modulo, lo segnalo negli eventi:
        if (changed)
        {
            // Info modifica conf modulo secondo attuali linee seriali sito:
            EventMgr.getInstance().info(1,"Light&Night","lucinotte","LN03",null);
            
            //reloadConfig();
            LightNightMgr.getInstance().reload();
        }
        
        return changed;
    }
    
    public static String getDevStatus(Integer iddev) throws DataBaseException
    {
        String icona = img_undef;
        
        Object[] values = devsStatus.get(iddev);
        
        if (values != null)
        {
            if ("luci".equals(values[1]))
            {
                icona = getLuci(iddev);
            }
            else
            {
                icona = getNotte(iddev);
            }
        }
        
        return icona;
    }
    
    //public static String[] getOnOff(Integer iddev) throws DataBaseException
    public static String getOnOff(Integer iddev) throws DataBaseException
    {
        float stato = 0f;
        float on = 1f;
        String icona = img_undef;
        String icons[] = new String[]{img_undef,img_undef};
        String icon_on_on = "images/button/on.png";
        String icon_on_off = "images/button/on_off.png";
        String icon_off_on = "images/button/off.png";
        String icon_off_off = "images/button/off_off.png";
        
        Object[] values = devsStatus.get(iddev);
        
        if (values != null)
        {
            if (values[2] != null)
            {
                //idvariable:
                Integer idvar = (Integer)values[2];
                
                //logica x la var:
                //if ("inv".equals(values[5]))
                if (((Integer)(values[5])).intValue() == LNField.INVERSE)
                    on = 0f;
                else
                    on = 1f;
                
                //se "idvar" != Null:
                if ((idvar.intValue() != 0) && (idvar.intValue() != -1))
                {
                    try
                    {
                        stato = ControllerMgr.getInstance().getFromField(idvar).getCurrentValue();
                        
                        if ((new Float(stato)).equals(Float.NaN))
                        {
                            icons[0] = img_error;
                            icons[1] = img_error;
                            icona = img_error;
                        }
                        else
                        if (stato == on)
                        {
                            icons[0] = icon_on_on;
                            icons[1] = icon_off_off;
                            icona = icon_on_on;
                        }
                        else
                        {
                            icons[0] = icon_on_off;
                            icons[1] = icon_off_on;
                            icona = icon_off_on;
                        }
                    }
                    catch (Exception e)
                    {
                        // PVPro catch block:
                        Logger logger = LoggerMgr.getLogger(LNDevsGroup.class);
                        logger.error(e);
                        
                        icons[0] = img_error;
                        icons[1] = img_error;
                        icona = img_error;
                    }
                }
            }
        }
        
        return icona;
    }
    
    public static String getLuci(Integer iddev) throws DataBaseException
    {
        float stato = 0f;
        float on = 1f;
        String icon_luci = img_undef;
        String icon_lc_on = "images/button/light.png";
        String icon_lc_off = "images/button/light_off.png";
        
        Object[] values = devsStatus.get(iddev);
        
        if (values != null)
        {
            if (values[3] != null)
            {
                //idvariable:
                Integer idvar = (Integer)values[3];
                
                //logica x la var:
                //if ("inv".equals(values[6]))
                if (((Integer)(values[6])).intValue() == LNField.INVERSE)
                    on = 0f;
                else
                    on = 1f;
                
                //se "idvar" != Null:
                if ((idvar.intValue() != 0) && (idvar.intValue() != -1))
                {
                    try
                    {
                        stato = ControllerMgr.getInstance().getFromField(idvar).getCurrentValue();
                        
                        if ((new Float(stato)).equals(Float.NaN))
                        {
                            icon_luci = img_error;
                        }
                        else
                        if (stato == on)
                        {
                            icon_luci = icon_lc_on;
                        }
                        else icon_luci = icon_lc_off;
                    }
                    catch (Exception e)
                    {
                        // PVPro catch block:
                        Logger logger = LoggerMgr.getLogger(LNDevsGroup.class);
                        logger.error(e);
                        
                        icon_luci = img_error;
                    }
                }
            }
        }
        
        return icon_luci;
    }
    
    public static String getNotte(Integer iddev) throws DataBaseException
    {
        float stato = 0f;
        float on = 1f;
        String icon_notte = img_undef;
        String icon_nt_on = "images/button/night.png";
        String icon_nt_off = "images/button/night_off.png";
        
        Object[] values = devsStatus.get(iddev);
        
        if (values != null)
        {
            if (values[4] != null)
            {
                //idvariable:
                Integer idvar = (Integer)values[4];
                
                //logica x la var:
                //if ("inv".equals(values[7]))
                if (((Integer)(values[7])).intValue() == LNField.INVERSE)
                    on = 0f;
                else
                    on = 1f;
                
                //se "idvar" != Null:
                if ((idvar.intValue() != 0) && (idvar.intValue() != -1))
                {
                    try
                    {
                        stato = ControllerMgr.getInstance().getFromField(idvar).getCurrentValue();
                        
                        if ((new Float(stato)).equals(Float.NaN))
                        {
                            icon_notte = img_error;
                        }
                        else
                        if (stato == on)
                        {
                            icon_notte = icon_nt_on;
                        }
                        else icon_notte = icon_nt_off;
                    }
                    catch (Exception e)
                    {
                        // PVPro catch block:
                        Logger logger = LoggerMgr.getLogger(LNDevsGroup.class);
                        logger.error(e);
                        
                        icon_notte = img_error;
                    }
                }
            }
        }
        
        return icon_notte;
    }
    
    public static void loadDevsDtl(int idgroup)
    {
        
        devsStatus = new HashMap<Integer, Object[]>();
        
        Integer iddev = new Integer(-1);
        Integer tmpdev = null;
        Object[] valori = null;
        
        //recupero idvariable per var di stato dei devs fisici del gruppo:
        String sql_a = "select ln_d.iddev, ln_d.lcnt, ln_v.iddevmdl, cfv.idvariable as idvar, ln_v.tipo, ln_v.logica " +
                        " from ln_devlcnt as ln_d, cfdevice as cfd, cfvariable as cfv, ln_varmdl as ln_v where " +
                        " ln_d.iddev=cfd.iddevice AND ln_v.iddevmdl=cfd.iddevmdl AND ln_v.tipo > 2 AND " +
                        " ln_d.iddev in (select iddev from ln_devsxgroup where idgroup=? and iddev > 0) AND " +
                        " cfv.idvariable=(select idvariable from cfvariable where cfvariable.idvarmdl=ln_v.idvarmdl AND " +
                        " cfvariable.iddevice=ln_d.iddev AND cfvariable.iscancelled='FALSE' AND cfvariable.idhsvariable is not null) AND " +
                        " (cfv.idvarmdl is not null) AND (cfv.idhsvariable is not null)";
        
        //recupero idvariable per var di stato dei devs logici del gruppo:
        String sql_b = "select ln_d.iddev, ln_d.lcnt, ln_v.iddevmdl, cfv.idvarmdl as idvar, ln_v.tipo, ln_v.logica " +
                        " from ln_devlcnt as ln_d, cfdevice as cfd, cfvariable as cfv, ln_varmdl as ln_v where " +
                        " ABS(ln_d.iddev)=cfd.iddevice AND ln_v.tipo > 2 AND " +
                        " ln_d.iddev in (select iddev from ln_devsxgroup where idgroup=? and iddev < 0) AND " +
                        " cfv.idvarmdl=(select idvarmdl from cfvariable where cfvariable.idvarmdl=ln_v.idvarmdl AND " +
                        " cfvariable.iddevice=ABS(ln_d.iddev) AND cfvariable.iscancelled='FALSE' AND " +
                        " cfvariable.idvarmdl is not null AND cfvariable.idhsvariable is not null) AND " +
                        " (cfv.idvarmdl is not null) AND (cfv.idhsvariable is not null) order by iddev, idvar";
        
        String sql = sql_a + " UNION " + sql_b;
        
        Object[] par = new Object[]{new Integer(idgroup), new Integer(idgroup)};
        RecordSet rs = null;
        Record rc = null;
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, par);
        }
        catch (DataBaseException e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(LNDevsGroup.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            
            for (int i = 0; i < rs.size(); i++)
            {
                rc = rs.get(i);
                tmpdev = (Integer)rc.get("iddev");
                
                if (tmpdev.intValue() != iddev.intValue())
                {
                    if (iddev.intValue() != -1)
                    {
                        devsStatus.put(iddev, valori);
                    }
                    
                    iddev = tmpdev;
                    valori = new Object[8];
                    
                    valori[0] = (Integer)rc.get("iddevmdl");
                    valori[1] = rc.get("lcnt").toString();
                }
                
                valori[Integer.parseInt(rc.get("tipo").toString()) - 1] = (Integer)rc.get("idvar");
                valori[Integer.parseInt(rc.get("tipo").toString()) + 2] = (Integer)rc.get("logica");
            }
            
            devsStatus.put(iddev, valori);
        }
    }
}
