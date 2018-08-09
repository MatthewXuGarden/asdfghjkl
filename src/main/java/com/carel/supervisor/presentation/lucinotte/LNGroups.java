package com.carel.supervisor.presentation.lucinotte;

import java.util.HashMap;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class LNGroups
{

    public static final int MAX_GROUPS = 6;
    
    // x SubTab1:
    public static String getLNGroupsTable(String lang) throws DataBaseException
    {
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String nogroups = lan.getString("lucinotte", "nogroups");
        String numdevs = lan.getString("lucinotte", "numdevs");
        String forceman = lan.getString("lucinotte", "forceman");
        String dtlview = lan.getString("lucinotte", "dtlview");
        String activate = lan.getString("lucinotte", "activate");
        String disactivate = lan.getString("lucinotte", "disactivate");
        String gruppo = lan.getString("lucinotte","gruppo");
        
        //controllo consistenza dati modulo con stato attuale linee PVPro:
        LNDevsGroup.ctrlDevices();
        
        StringBuffer table = new StringBuffer();
        
        String num_grp = "";
        String checked = "";
        String width = "";
        String allinea = "";
        String colore = "";
        int ndxg = 0;
        String manuale = "";
        
        int buttonwidth = 85;
        
        //visualizzo i gruppi che coinvolgono almeno 1 device:
        //String sql = "select * from ln_groups where idgroup in (select distinct idgroup from ln_devsxgroup) order by idgroup";
        String sql = "select * from ln_groups order by idgroup";
        RecordSet rs = null;
        Record rc = null;
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        if ((rs != null) && (rs.size() > 0))
        {
            
            // recupero num devs x ogni gruppo:
            HashMap<String,Integer> ndxgrp = new HashMap<String,Integer>();
            String sql_2 = "select idgroup,count(idgroup) as totdevs from ln_devsxgroup group by idgroup order by idgroup";
            RecordSet rs_2 = DatabaseMgr.getInstance().executeQuery(null, sql_2);
            
            if ((rs_2 != null) && (rs_2.size() > 0))
            {
                for (int i = 0; i < rs_2.size(); i++)
                {
                    ndxgrp.put(rs_2.get(i).get("idgroup").toString(), (Integer)rs_2.get(i).get("totdevs"));
                }
            }
            
            table.append("<table width='98%' align='center' cellspacing='1' cellpadding='1'>\n");
            
            for (int i = 0; i < MAX_GROUPS; i++)
            {
                
                if (i == 3)
                {
                    table.append("</tr>\n"); //fine prima riga
                    table.append("<tr style='height:50px'><td colspan=6>&nbsp</td></tr>\n");
                }
                
                if ((i == 0) || (i == 3))
                {
                    table.append("<tr>\n");
                }
                
                /*
                if (i < 3)
                {
                    table.append("<td colspan=2>\n"); //3 cols nella prima riga
                    width = "80%";
                }
                else
                {
                    if (i == 3)
                    {
                        allinea = "right";
                    }
                    else
                    {
                        allinea = "left";
                    }
                    
                    table.append("<td colspan=3 align='"+allinea+"'>\n"); //2 cols nella seconda riga
                    width = "53%";
                }
                */
                
                table.append("<td colspan=2>\n"); //3 cols nella prima riga
                width = "80%";
                
                if (i < rs.size())
                {
                    rc = rs.get(i);
                    num_grp = rc.get("idgroup").toString();
                    manuale = rc.get("manuale").toString();
                    
                    //table.append("<div style='width:33%'>\n");
                    table.append("<table class='table' border='0' align='center' width='"+width+"' cellspacing='0' cellpadding='1'>\n");
                    table.append("<tr class='standardTxt' style='height:20px'>\n");
                    
                    table.append("<td colspan=1 style='width:33%' class='th' align='center'><b>"+gruppo+" "+num_grp+":&nbsp;</b></td>\n");
                    table.append("<td colspan=2 style='background-color:WHITE' align='center'>\n");
                    table.append("<input type='hidden' id='name_grp_"+num_grp+"' name='name_grp_"+num_grp+"' value='"+rc.get("nome_grp").toString()+"' />\n");
                    table.append("<b>"+rc.get("nome_grp").toString()+"</b>");
                    table.append("</td>\n");
                    table.append("</tr>\n");
                    
                    table.append("<tr class='Row1' style='height:20px'>\n");
                    table.append("<td class='standardTxt' colspan=3>\n");
                    //table.append("&nbsp;");
                    table.append("<input type='hidden' id='scheduler_grp"+num_grp+"' name='scheduler_grp_"+num_grp+"' value='"+rc.get("scheduler").toString()+"' />\n");
                    table.append("<input type='hidden' id='campo_grp"+num_grp+"' name='campo_grp_"+num_grp+"' value='"+rc.get("campo").toString()+"' />\n");
                    table.append("<input type='hidden' id='lcnt_grp_"+num_grp+"' name='lcnt_grp_"+num_grp+"' value='"+rc.get("lcnt").toString()+"' />\n");
                    table.append("<input type='hidden' id='onoff_grp_"+num_grp+"' name='onoff_grp_"+num_grp+"' value='"+rc.get("onoff").toString()+"' />\n");
                    table.append("</td>\n");
                    table.append("</tr>\n");
                    
                    table.append("<tr class='Row1'>\n");
                    table.append("<td colspan=3 align='left' valign='top'>\n");
                    
                    table.append("&nbsp;<input type='button' name='onbtn_grp_"+num_grp+"' id='onbtn_grp_"+num_grp+"' ");
                	table.append(" style='width:"+buttonwidth+";height:25px;color:GREEN;font-weight:bold;' ");
                    table.append(" value='"+activate+"' onclick='exeOnOff(\""+activate+"\",\"go_On\","+num_grp+");' />");
                    table.append("&nbsp;&nbsp;");
                    table.append("<input type='button' value='"+disactivate+"' onclick='exeOnOff(\""+disactivate+"\",\"go_Off\","+num_grp+");' name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' ");
                    table.append(" style='width:"+buttonwidth+";height:25px;color:RED;font-weight:bold;' />");

                    
                    /*
                    table.append("&nbsp;<input type='button' name='onbtn_grp_"+num_grp+"' id='onbtn_grp_"+num_grp+"' ");
                	table.append(" class='mybutton' style='width:"+buttonwidth+";height:25px;border-style:ridge;font-size:14px;text-transform:uppercase;' ");
                    table.append(" value='"+activate+"' onclick='exeOnOff(\""+activate+"\","+num_grp+");' />");
                    table.append("&nbsp;&nbsp;");
                    table.append("<input type='button' value='"+disactivate+"' onclick='exeOnOff(\""+disactivate+"\","+num_grp+");' name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' ");
                    table.append(" class='mybutton' style='width:"+buttonwidth+";height:25px;border-style:ridge;font-size:14px;text-transform:uppercase;' />");
					*/
                    
                    // pulsanti Abilita/Disabilita gruppo:
                	/*
                    table.append("&nbsp;<input type='button' name='onbtn_grp_"+num_grp+"' id='onbtn_grp_"+num_grp+"' ");
                	table.append(" style='width:"+buttonwidth+";background-color:#4F6D9E;font-weight:bold;text-transform:uppercase;text-align:center;color:WHITE;cursor:pointer;border-color:DARKGRAY;border-spacing:1px;border-style:solid;border-width:1px;' ");
                    table.append(" value='"+enable+"' onclick='exeOnOff(\""+enable+"\","+num_grp+");' />");
                    table.append("&nbsp;&nbsp;");
                    table.append("<input type='button' value='"+disable+"' onclick='exeOnOff(\""+disable+"\","+num_grp+");' name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' ");
                    table.append(" style='width:"+buttonwidth+";border:0;background-color:#4F6D9E;font-weight:bold;text-transform:uppercase;text-align:center;color:WHITE;cursor:pointer' />");
                    */
                    
                    /*
                    if ("on".equals(manuale))
                    {
                    */
                        /*
                    	table.append("&nbsp;<img name='onbtn_grp_"+num_grp+"' id='onbtn_grp_"+num_grp+"' style='cursor:pointer' ");
                        table.append(" src='images/button/on.png' onclick='exeOnOff(\""+enable+"\","+num_grp+");' />");
                        table.append("&nbsp;&nbsp;<img onclick='exeOnOff(\""+disable+"\","+num_grp+");' name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' style='cursor:pointer' src='images/button/off.png' />\n");
                        */
                    /*
                    }
                    else
                    {
                        table.append("&nbsp;<img name='onbtn_grp_"+num_grp+"' id='onbtn_grp_"+num_grp+"' style='cursor:pointer' ");
                        table.append(" src='images/button/on_off.png' "+num_grp+");' />");
                        table.append("&nbsp;&nbsp;<img name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' style='cursor:pointer' src='images/button/off_off.png' />\n");
                    }
                    */
                        
                    /*
                    enabled = rc.get("enabled").toString();
                    
                    if ("on".equalsIgnoreCase(enabled))
                    {
                        table.append(" src='images/button/on_off.png' />");
                        table.append("&nbsp;&nbsp;<img onclick='go_off("+num_grp+");' name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' style='cursor:pointer' src='images/button/off.png' />\n");
                    }
                    else
                    if ("off".equalsIgnoreCase(enabled))
                    {
                        table.append(" src='images/button/on.png' onclick='go_on("+num_grp+");' />");
                        table.append("&nbsp;&nbsp;<img name='offbtn_grp_"+num_grp+"' id='offbtn_grp_"+num_grp+"' style='cursor:pointer' src='images/button/off_off.png' />\n");
                    }
                    
                    table.append("<input type='hidden' id='enable_grp_"+num_grp+"' name='enable_grp_"+num_grp+"' value='"+enabled+"' />\n");
                    */
                    
                    table.append("</td>\n");
                    table.append("</tr>\n");
                    
                    table.append("<tr class='Row1'>\n");
                    table.append("<td colspan=3 align='left' class='standardTxt'>\n");
                    checked = ("on".equalsIgnoreCase(manuale) ? "checked" : "");
                    //table.append("&nbsp;<input type='checkbox' name='manual_grp_"+num_grp+"' id='manual_grp_"+num_grp+"' " + checked + " onclick='checkManual("+num_grp+")'/>\n");
                    table.append("&nbsp;<input type='checkbox' name='manual_grp_"+num_grp+"' id='manual_grp_"+num_grp+"' " + checked + " />\n");
                    table.append("&nbsp;<b>"+forceman+"</b>");
                    table.append("</td>\n");
                    table.append("</tr>\n");
                    
                    table.append("<tr class='Row1'>\n");
                    table.append("<td colspan=3 style='width:75%' align='right' class='standardTxt'>&nbsp;"+numdevs+": ");
                    //table.append("</td>\n");
                    
                    if (ndxgrp.get(num_grp) != null)
                        ndxg = ndxgrp.get(num_grp).intValue();
                    else
                        ndxg = 0;
                    
                    if (ndxg > 0)
                        colore = "GREEN";
                    else
                        colore = "RED";
                    
                    //table.append("<td style='width:35%' align='left' class='standardTxt'>");
                    table.append("<span style='color:"+colore+"'>&nbsp;<b>"+ndxg+"</b></span>&nbsp;");
                    table.append("</td>\n");
                    
                    table.append("</tr>\n");
                    
                    table.append("<tr class='Row1'>\n");
                    table.append("<td colspan=3 align='right' class='standardTxt'>");
                    table.append("<input type='button' style='cursor:pointer' value='STATUS' onclick='go2GrpDtl("+num_grp+");return false;' title='"+dtlview+"' />\n");
                    table.append("</td>\n");
                    table.append("</tr>\n");

                    table.append("</table>\n");
                    //table.append("</div>");
                }
                else
                {
                    //se mancano uno o più gruppi:
                    table.append("<div style='width:"+width+";height:75%'>&nbsp;</div>\n");
                }
                table.append("</td>\n");
            }
            table.append("</tr>\n");
            table.append("</table>\n");
        }
        else
        {
            table.append("<div><b>"+nogroups+"</b></div>");
        }
        
        return table.toString();
    }
    
    // x SubTab2:
    public static String getLNGroupsLegend(String lang) throws DataBaseException
    {
        // Alessandro : added Virtual Keyboard support
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        String cssVirtualKeyboardClass = (OnScreenKey ? VirtualKeyboard.getInstance().getCssClass() : "");    	
    	
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String nogroups = lan.getString("lucinotte", "nogroups");
        
        StringBuffer legenda = new StringBuffer();
        
        String num_grp = "";
        String name_grp = "";
        String width = "";
        String allinea = "";
        String onoff = "";
        String lcnt = "";
        String onoff_check = "";
        String lcnt_check = "";
        
        //visualizzo i gruppi che coinvolgono almeno 1 device:
        //String sql = "select * from ln_groups where idgroup in (select distinct idgroup from ln_devsxgroup) order by idgroup";
        String sql = "select * from ln_groups order by idgroup";
        RecordSet rs = null;
        Record rc = null;
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        if ((rs != null) && (rs.size() > 0))
        {
            legenda.append("<table width='97%' align='center' cellspacing='1' cellpadding='1'>\n");
            
            for (int i = 0; i < MAX_GROUPS; i++)
            {
                if (i == 3)
                {
                    legenda.append("</tr>\n"); //fine prima riga
                    legenda.append("<tr style='height:5px'><td style='height:5px' colspan=6></td></tr>");
                }
                
                if ((i == 0) || (i == 3))
                {
                    legenda.append("<tr>\n");
                }
                
                /*
                if (i < 3)
                {
                    legenda.append("<td colspan=2>\n"); //3 cols nella prima riga
                    width = "80%";
                }
                else
                {
                    if (i == 3)
                    {
                        allinea = "right";
                    }
                    else
                    {
                        allinea = "left";
                    }
                    
                    legenda.append("<td colspan=3 align='"+allinea+"'>\n"); //2 cols nella seconda riga
                    width = "53%";
                }
                */
                
                legenda.append("<td colspan=2>\n"); //3 cols nella prima riga
                width = "80%";
                
                if (i < rs.size())
                {
                    rc = rs.get(i);
                    num_grp = rc.get("idgroup").toString();
                    name_grp = rc.get("nome_grp").toString();
                    //name_grp = Replacer.replace(name_grp,"\"","'");
                    
                    legenda.append("<table class='table' border='0' align='center' width='"+width+"' cellspacing='1' cellpadding='1'>\n");
                    
                    /*
                    legenda.append("<tr class='standardTxt'>\n");
                    
                    legenda.append("<td class='standardTxt' align='center'><b>Grp."+num_grp+"&nbsp;</b></td>\n");
                    legenda.append("<td class='standardTxt' align='center'>L/N</td>\n");
                    legenda.append("<td class='standardTxt' align='center'>ON</td>\n");
                    
                    legenda.append("</tr>");
                    */
                    
                    legenda.append("<tr class='standardTxt' style='height:15px'>\n");
                    
                    legenda.append("<td class='th' align='center'>\n");
                    legenda.append("<span><b>G."+num_grp+"&nbsp;</b></span></td>\n");
                    legenda.append("<td class='Row1' align='center'>\n");
                    legenda.append("<input class='"+cssVirtualKeyboardClass+"' type='text' size=17 id='name_grp_"+num_grp+"' name='name_grp_"+num_grp+"' value='"+name_grp+"' onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'>\n");
                    legenda.append("</td>\n");
                    
                    lcnt = rc.get("lcnt").toString();
                    
                    if ("on".equalsIgnoreCase(lcnt))
                    {
                        lcnt_check = "checked";
                    }
                    else lcnt_check = "";
                    
                    legenda.append("<td class='Row1' align='center'>\n");
                    legenda.append("<input type='checkbox' name='lcnt_grp_"+num_grp+"' id='lcnt_grp_"+num_grp+"' " + lcnt_check + "/>");
                    legenda.append("L/N</td>\n");
                    
                    onoff = rc.get("onoff").toString();
                    
                    if ("on".equalsIgnoreCase(onoff))
                    {
                        onoff_check = "checked";
                    }
                    else onoff_check = "";
                    
                    legenda.append("<td class='Row1' align='center'>\n");
                    legenda.append("<input type='checkbox' name='onoff_grp_"+num_grp+"' id='onoff_grp_"+num_grp+"' " + onoff_check + "/>");
                    legenda.append("ON/OFF</td>\n");
                    
                    legenda.append("</tr>\n");

                    
                    legenda.append("</table>\n");
                }
                else
                {
                    //se mancano uno o più gruppi:
                    legenda.append("<div style='width:"+width+";height:75%'>&nbsp;</div>\n");
                }
                legenda.append("</td>\n");
            }
            legenda.append("</tr>\n");
            legenda.append("</table>");
        }
        else
        {
            legenda.append("<div><b>"+nogroups+"</b></div>");
        }
        
        return legenda.toString();
    }
    
    public static String getComboGroups(String language, int idgrp) throws DataBaseException
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
        String gruppo = lan.getString("lucinotte","gruppo");
    	
    	StringBuffer combo = new StringBuffer();
        
        combo.append("<select id='cmb_grps' name='cmb_grps' onchange='loadScheduling(this);' style='width:75%'>\n");
        combo.append("<option value='-1'> ----------   ---------- </option>\n");
        
        //TODO: da verificare!
        //String sql = "select idgroup, nome_grp from ln_groups where idgroup in (select distinct idgroup from ln_devsxgroup) order by idgroup";
        String sql = "select idgroup, nome_grp from ln_groups order by idgroup";
        
        RecordSet rs = null;
        Record rc = null;
        
        Integer idgroup = null;
        String grpdescr = "";
        String checked = "";
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        if ((rs != null) && (rs.size() > 0))
        {
            for(int i = 0; i < rs.size(); i++)
            {
                checked = "";
                rc = rs.get(i);
                idgroup = (Integer)rc.get("idgroup");
                grpdescr = rc.get("nome_grp").toString();
                
                if (idgroup.intValue() == idgrp)
                    checked = "checked";
                
                combo.append("<option "+checked+" value='"+idgroup.toString()+"'>"+gruppo+" "+idgroup.toString()+" - ");
                combo.append("<span style='font-weight:bold'>"+grpdescr+"</span></option>\n");
            }
        }
        
        combo.append("</select>\n");
        
        return combo.toString();
    }
}
