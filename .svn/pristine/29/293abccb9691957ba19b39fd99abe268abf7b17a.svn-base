package com.carel.supervisor.presentation.lucinotte;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;

public class LNScheduler
{
    public static String getHtmlScheduler(String lang, int idSite, int timetype)
    {
    	LangService lan = LangMgr.getInstance().getLangService(lang);
    	
    	String gruppo = lan.getString("lucinotte", "gruppo");
    	String active = lan.getString("lucinotte", "attivo");
    	String copysched = lan.getString("lucinotte", "copysched");
    	String resetsched = lan.getString("lucinotte", "resetsched");
    	
    	String[] weekdays = new String[7];
    	weekdays[0] = lan.getString("cal","mon");
    	weekdays[1] = lan.getString("cal","tue");
    	weekdays[2] = lan.getString("cal","wed");
    	weekdays[3] = lan.getString("cal","thu");
    	weekdays[4] = lan.getString("cal","fri");
    	weekdays[5] = lan.getString("cal","sat");
    	weekdays[6] = lan.getString("cal","sun");
    	
    	//String combo24hours = LNUtils.combo24Hours();
    	//String comboAPhours = LNUtils.comboAPHours();
    	
    	String combo_hours = "";
    	
    	if (timetype == 0)
    	{
    		combo_hours = LNUtils.combo24Hours();
    	}
    	else
    	{
    		combo_hours = LNUtils.comboAPHours();
    	}
    	
    	String[] grps_names = new String[LNGroups.MAX_GROUPS];
    	
    	String sql = "select nome_grp from ln_groups order by idgroup";
        RecordSet rs = null;
        
        int p = 0;
        try
        {
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			if ((rs != null) && (rs.size() > 0))
			{
				for ( ; p < rs.size(); p++)
				{
					grps_names[p] = rs.get(p).get("nome_grp").toString();
				}
			}
		}
        catch (DataBaseException e)
        {
			for (int c = p; c < LNGroups.MAX_GROUPS; c++)
			{
				grps_names[c] = "";
			}
		}
    	
    	StringBuffer tabella = new StringBuffer();
        
        tabella.append("<table width='98%' align='center' border=0 cellpadding=2 cellspacing=1>\n");
        
        for (int ngr = 1; ngr <= LNGroups.MAX_GROUPS; ngr++)
        {
        	if ((ngr == 1) || (ngr == (LNGroups.MAX_GROUPS/2)+1))
        	{
        		tabella.append("<tr>\n");
        	}
        	
        	tabella.append("<td>\n");
        	tabella.append("<table class='table' style='background-color:WHITE' width='33%' height='50%' align='center' border=0 cellpadding=1 cellspacing=0 >\n");
        	
        	tabella.append("<tr class='th' style='height:20px'>\n");
        	tabella.append("<td class='standardTxt' align='left' colspan=3>");
        	tabella.append("&nbsp;"+gruppo+"&nbsp;"+ngr+" - <b>"+grps_names[ngr-1]+"</b>");
        	tabella.append("</td>\n");
        	tabella.append("<td align='right' cellpadding=0 valign='middle' colspan=2><b>"+active+"</b><input type='checkbox' id='sched_grp"+ngr+"' name='sched_grp"+ngr+"'/>");
        	tabella.append("<span>");
        	tabella.append("<img style='height:20px;' valign='middle' src='images/actions/paramscopy2_on.png' title='"+copysched+"' onclick='copySched("+ngr+");'/>");
        	tabella.append("&nbsp;");
        	tabella.append("<img style='height:20px;' valign='middle' src='images/actions/rst2_on.png' title='"+resetsched+"' onclick='resetSched("+ngr+");'/>");
        	tabella.append("</span>");
        	tabella.append("</td>\n");
        	tabella.append("</tr>\n");
        	
    		tabella.append("<tr class='standardTxt' style='height:10px'>\n");
    		tabella.append("<td class='standardTxt' align='center' width='14%'>\n");
    		tabella.append("&nbsp;");
    		tabella.append("</td>\n");
    		
        	tabella.append("<td align='center' width='18%' style='color:GREEN'><b>ON</b></td>\n");
        	tabella.append("<td align='center' width='18%' style='color:RED'><b>OFF</b></td>\n");
        	tabella.append("<td align='center' width='18%' style='color:GREEN'><b>ON</b></td>\n");
        	tabella.append("<td align='center' width='18%' style='color:RED'><b>OFF</b></td>\n");
        	
        	tabella.append("</tr>\n");
        	
        	for (int day = 1; day <= 7; day++)
        	{
        		tabella.append("<tr class='standardTxt'>\n");
        		
        		/*
        		if ((ngr == 1) || (ngr == (LNGroups.MAX_GROUPS/2)+1))
        		{
        			tabella.append("<td class='standardTxt' align='center' id='g"+ngr+"_day"+day+"' colspan=1 style='color:RED'><b>"+weekdays[day-1]+"</b></td>\n");
        		}
        		else
        		{
        			tabella.append("<td class='standardTxt' align='center' id='g"+ngr+"_day"+day+"' colspan=1 style='color:RED'><b>"+weekdays[day-1].charAt(0)+"</b></td>\n");
        		}
        		*/
        		
        		tabella.append("<td class='standardTxt' align='center' id='g"+ngr+"_day"+day+"' colspan=1 style='color:RED'><b>"+weekdays[day-1].substring(0, 2)+"</b></td>\n");
        		
        		tabella.append("<td class='standardTxt' align='center' colspan=1>\n");
        		tabella.append("<select style='color:GREEN' id='g"+ngr+"_day"+day+"_on1' name='g"+ngr+"_day"+day+"_on1' onchange='modGrp();'>"+combo_hours+"</select>\n");
        		tabella.append("</td>\n");
        		tabella.append("<td class='standardTxt' align='center' colspan=1>\n");
        		tabella.append("<select style='color:RED' id='g"+ngr+"_day"+day+"_off1' name='g"+ngr+"_day"+day+"_off1' onchange='modGrp();'>"+combo_hours+"</select>\n");
        		tabella.append("</td>\n");
        		tabella.append("<td class='standardTxt' align='center' colspan=1>\n");
        		tabella.append("<select style='color:GREEN' id='g"+ngr+"_day"+day+"_on2' name='g"+ngr+"_day"+day+"_on2' onchange='modGrp();'>"+combo_hours+"</select>\n");
        		tabella.append("</td>\n");
        		tabella.append("<td class='standardTxt' align='center' colspan=1>\n");
        		tabella.append("<select style='color:RED' id='g"+ngr+"_day"+day+"_off2' name='g"+ngr+"_day"+day+"_off2' onchange='modGrp();'>"+combo_hours+"</select>\n");
        		tabella.append("</td>\n");
        		tabella.append("</tr>\n");
        	}
        	
        	tabella.append("</table>\n");
        	tabella.append("</td>\n");
        	
        	/*
        	if (ngr == (LNGroups.MAX_GROUPS/2))
        	{
        		tabella.append("</tr>\n");
        		tabella.append("<tr style='height:10px'>\n");
        		tabella.append("<td style='height:10px' colspan=3>&nbsp;</td>\n");
        		tabella.append("</tr>\n");
        	}
        	*/
        	
        }
        
        tabella.append("</tr>\n");
        tabella.append("</table>\n");
        
        tabella.append("<input type='hidden' id='maxgroups' value='"+LNGroups.MAX_GROUPS+"'>\n");
        
        return tabella.toString();
    }
}
