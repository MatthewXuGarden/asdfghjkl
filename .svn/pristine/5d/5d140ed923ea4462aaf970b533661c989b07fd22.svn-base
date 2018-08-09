package com.carel.supervisor.presentation.lucinotte;

import java.util.HashMap;
import java.util.Iterator;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class LNDays
{
    private static final String[] mesi = {"january","february","march","april","may","june","july","august","september","october","november","december"};
    private static final int hour24 = 0;
//    private static final int hourAP = 1;
    
    public static String getHtmlExceptionDays(String lang, int idSite, int scrnH, int scrnW, HashMap<Integer, String> grpmap) throws DataBaseException
    {
        StringBuffer tabella = new StringBuffer();
        
        LangService lan = LangMgr.getInstance().getLangService(lang);
        String group = lan.getString("lucinotte", "gruppo");
        String month = lan.getString("lucinotte", "mese");
        String day = lan.getString("lucinotte", "giorno");
        String descriz = lan.getString("lucinotte", "descrizione");
        String elim = lan.getString("lucinotte", "elimina");
        
        int timef = LNUtils.getTimeFormat();
        
        //tabella.append("<div style='overflow-X:auto;overflow-Y:scroll;height:"+(scrnH-575)+"px'>\n");
        tabella.append("<table style='width:"+(scrnW-150)+"px;' class='table' id='tbl_date' border='0' cellspacing='1' cellpadding='1'>\n");
        
        //tabella.append("<tr class='th' style='height:25px' onclick='goBackToLN();return false;'>\n");
        tabella.append("<tr class='th' style='height:25px'>\n");
        
        tabella.append("<td class='standardTxt' align='center' onclick='goBackToLN();return false;'><b>"+group+"</b></td>\n");

        tabella.append("<td class='standardTxt' align='center' style='width:10%' onclick='goBackToLN();return false;'><b>"+month+"</b></td>\n");
        tabella.append("<td class='standardTxt' align='center' style='width:7%' onclick='goBackToLN();return false;'><b>"+day+"</b></td>\n");
        tabella.append("<td class='standardTxt' align='center' style='width:33%' onclick='goBackToLN();return false;'><b>"+descriz+"</b></td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%' onclick='goBackToLN();return false;'><b>ON 1</b></td>\n");
        tabella.append("<td class='standardTxt' align='center' style='width:10%' onclick='goBackToLN();return false;'><b>OFF 1</b></td>\n");
        tabella.append("<td class='standardTxt' align='center' style='width:10%' onclick='goBackToLN();return false;'><b>ON 2</b></td>\n");
        tabella.append("<td class='standardTxt' align='center' style='width:10%' onclick='goBackToLN();return false;'><b>OFF 2</b></td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'><b>"+elim+"</b>&nbsp;");
        tabella.append("<input type='radio' onclick='selectAll();return false;' />");
        tabella.append("</td>\n");
        
        tabella.append("</tr>\n");
        
//        tabella.append("</table>\n");
        
        //tabella.append("<div style='height:2px'></div>\n");
        
//        tabella.append("<div style='overflow-X:auto;overflow-Y:scroll;height:"+(scrnH-415)+"px'>\n");
//        tabella.append("<table style='width:"+(scrnW-150)+"px;' class='table' id='tbl_date' border='0' cellspacing='1' cellpadding='1'>\n");
        
        //visualizzo i giorni di eccezione x lo scheduler:
        String sql = "select ln_exceptdays.*, ln_groups.nome_grp " +
        		"from ln_exceptdays left join ln_groups " +
        		"on ln_exceptdays.idgrp=ln_groups.idgroup " +
        		"order by idmonth, idday";
        
        RecordSet rs = null;
        Record rc = null;

        rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int i = 0;
        int idxd = -1;
        int idxm = -1;
        int idxgrp = -1;
        String descr = "";
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (i = 0; i < rs.size(); i++)
            {
                rc = rs.get(i);
                
                tabella.append("<tr class='Row1' style='height:20px'>\n");
                
                idxgrp = (Integer) rc.get("idgrp");
                tabella.append("<td class='standardTxt' align='center' >\n");
                tabella.append("<select id='grp_"+i+"' name='grp_"+i+"' onchange='enableAction(2);'>\n");
                tabella.append(getOptionsGroups(lang, idSite, idxgrp, grpmap));
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                idxm = Integer.parseInt(rc.get("idmonth").toString());
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<select id='month_"+i+"' name='month_"+i+"' onchange='enableAction(2);'>\n");
                tabella.append(getOptionsMonths(lang, idSite, idxm));
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                idxd = Integer.parseInt(rc.get("idday").toString());
                tabella.append("<td class='standardTxt' align='center' style='width:7%'>\n");
                tabella.append("<select style='width:75%' id='day_"+i+"' name='day_"+i+"' onchange='enableAction(2);'>\n");
                tabella.append(getOptionsDays(idxd));
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                descr = rc.get("description").toString();
                tabella.append("<td class='standardTxt' align='center' style='width:33%'>\n");
                tabella.append("<input type='text' class='"+ VirtualKeyboard.getInstance().getCssClass()+"' style='width:100%' id='descr_"+i+"' name='descr_"+i+"' value='"+descr+"' onmousedown='enableAction(2);'/>\n");
                tabella.append("</td>\n");
                
                idxd = (Integer)rc.get("on_1");
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<select id='on1_day_"+i+"' name='on1_day_"+i+"' onchange='enableAction(2);'>\n");
                if (timef == hour24)
                {
                	tabella.append(LNUtils.combo24Hours(idxd));
                }
                else
                {
                	tabella.append(LNUtils.comboAPHours(idxd));
                }
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                idxd = (Integer)rc.get("off_1");
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<select id='off1_day_"+i+"' name='off1_day_"+i+"' onchange='enableAction(1);'>\n");
                if (timef == hour24)
                {
                	tabella.append(LNUtils.combo24Hours(idxd));
                }
                else
                {
                	tabella.append(LNUtils.comboAPHours(idxd));
                }
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                idxd = (Integer)rc.get("on_2");
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<select id='on2_day_"+i+"' name='on2_day_"+i+"' onchange='enableAction(1);'>\n");
                if (timef == hour24)
                {
                	tabella.append(LNUtils.combo24Hours(idxd));
                }
                else
                {
                	tabella.append(LNUtils.comboAPHours(idxd));
                }
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                idxd = (Integer)rc.get("off_2");
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<select id='off2_day_"+i+"' name='off2_day_"+i+"' onchange='enableAction(1);'>\n");
                if (timef == hour24)
                {
                	tabella.append(LNUtils.combo24Hours(idxd));
                }
                else
                {
                	tabella.append(LNUtils.comboAPHours(idxd));
                }
                tabella.append("</select>\n");
                tabella.append("</td>\n");
                
                tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
                tabella.append("<input type='checkbox' id='del_"+i+"' name='del_"+i+"' onclick='enableAction(2);'>\n");
                tabella.append("</td>\n");
                    
                tabella.append("</tr>\n");
            }
        }
            
        tabella.append("<tr class='Row1'>\n");
        
        tabella.append("<td class='standardTxt' align='center' >\n");
        tabella.append("<select id='grp_"+i+"' name='grp_"+i+"' onchange='enableAction(2);'>\n");
        tabella.append(getOptionsGroups(lang, idSite, -1, grpmap ));
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<select id='month_"+i+"' name='month_"+i+"' onchange='enableAction(2);'>\n");
        tabella.append(getOptionsMonths(lang, idSite, -1));
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:7%'>\n");
        tabella.append("<select style='width:75%' id='day_"+i+"' name='day_"+i+"' onchange='enableAction(2);'>\n");
        tabella.append(getOptionsDays(-1));
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:33%'>\n");
        // Alessandro : inserito il codice per la classe CSS della tastiera virtuale
        tabella.append("<input type='text' class='"+ VirtualKeyboard.getInstance().getCssClass()+"' style='width:100%' id='descr_"+i+"' name='descr_"+i+"' value='' onmousedown='enableAction(2);'/>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<select id='on1_day_"+i+"' name='on1_day_"+i+"' onchange='enableAction(2);'>\n");
        if (timef == hour24)
        {
        	tabella.append(LNUtils.combo24Hours(-1));
        }
        else
        {
        	tabella.append(LNUtils.comboAPHours(-1));
        }
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<select id='off1_day_"+i+"' name='off1_day_"+i+"' onchange='enableAction(2);'>\n");
        if (timef == hour24)
        {
        	tabella.append(LNUtils.combo24Hours(-1));
        }
        else
        {
        	tabella.append(LNUtils.comboAPHours(-1));
        }
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<select id='on2_day_"+i+"' name='on2_day_"+i+"' onchange='enableAction(2);'>\n");
        if (timef == hour24)
        {
        	tabella.append(LNUtils.combo24Hours(-1));
        }
        else
        {
        	tabella.append(LNUtils.comboAPHours(-1));
        }
        tabella.append("</select>\n");
        tabella.append("</td>\n");
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<select id='off2_day_"+i+"' name='off2_day_"+i+"' onchange='enableAction(2);'>\n");
        if (timef == hour24)
        {
        	tabella.append(LNUtils.combo24Hours(-1));
        }
        else
        {
        	tabella.append(LNUtils.comboAPHours(-1));
        }
        tabella.append("</select>\n");
        tabella.append("</td>\n");        
        
        tabella.append("<td class='standardTxt' align='center' style='width:10%'>\n");
        tabella.append("<input type='checkbox' id='del_"+i+"' name='del_"+i+"' onclick='enableAction(2);'>\n");
        tabella.append("</td>\n");
            
        tabella.append("</tr>\n");
        
        tabella.append("</table>\n");
//        tabella.append("</div>\n");
        tabella.append("<input type='hidden' id='max_id' name='max_id' value='"+i+"' />\n");
        tabella.append("<input type='hidden' id='timeformat' value='"+timef+"' />\n");
        
        return tabella.toString();
    }
    
    public static HashMap<Integer, String> getGroupsMap() {
        String sql2 = "select ln_groups.idgroup, ln_groups.nome_grp from ln_groups order by idgroup";
        RecordSet rs2 = null;
        try {
			rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
        HashMap<Integer, String> grpmap = new HashMap<Integer, String>();
        if(rs2!=null && rs2.size()>0){
        	for (int idg = 0; idg < rs2.size(); idg++) {
				grpmap.put((Integer)rs2.get(idg).get("idgroup"), (String)rs2.get(idg).get("nome_grp"));
			}
        }
        return grpmap;
	}
    
    public static String getOptionsGroups(String lang, int idSite, int idxgrp, HashMap<Integer, String> grps) {
    	LangService lan = LangMgr.getInstance().getLangService(lang);
        StringBuffer combo = new StringBuffer();
        String selected = "";
        if (idxgrp == -1)
        	selected = "selected";
        
        combo.append("<option "+selected+" value='0'> "+lan.getString("lucinotte", "all")+" </option>\n");
        for (Iterator<Integer> itr = grps.keySet().iterator(); itr.hasNext();) {
        	Integer curgrp = itr.next();
            selected = "";
            if (curgrp == idxgrp)
                selected = "selected";
            combo.append("<option "+selected+" value='"+curgrp+"'>");
            combo.append(grps.get(curgrp));
            combo.append("</option>\n");
        }
        return combo.toString();
	}

	public static String getOptionsDays(int idx)
    {
        StringBuffer combo = new StringBuffer();
        String selected = "";
        
        //combo.append("<select id='' name=''>\n");
        
        if (idx == -1)
        	selected = "selected";
        
        combo.append("<option "+selected+" value='-1'> --- </option>\n");
        
        for (int i = 1; i <= 31; i++)
        {
            selected = "";
            if (i == idx)
                selected = "selected";
            
            combo.append("<option "+selected+" value='"+i+"'>");
            
            if (i > 9)
            {
            	combo.append(""+i+"");
            }
            else
            {
            	combo.append("0"+i+"");
            }
            
            combo.append("</option>\n");
        }
        
        //combo.append("</select>\n");
        
        return combo.toString();
    }
    
    public static String getOptionsMonths(String lang, int idSite, int idx)
    {
        LangService lan = LangMgr.getInstance().getLangService(lang);
        
        StringBuffer combo = new StringBuffer();
        String selected = "";
        
        //combo.append("<select id='' name=''>\n");
        
        if (idx == -1)
        	selected = "selected";
        
        combo.append("<option "+selected+" value='-1'> --- </option>\n");
        
        for (int i = 1; i <= 12; i++)
        {
            selected = "";
            if (i == idx)
                selected = "selected";
            
            combo.append("<option "+selected+" value='"+i+"'>");
            combo.append(lan.getString("cal", mesi[i-1]));
            
            combo.append("</option>\n");
        }
        
        //combo.append("</select>\n");
        
        return combo.toString();
    }
}
