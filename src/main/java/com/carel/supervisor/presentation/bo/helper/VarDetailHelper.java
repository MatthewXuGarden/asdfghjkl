package com.carel.supervisor.presentation.bo.helper;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.varaggregator.VarAggregator;
import com.carel.supervisor.presentation.bean.UnitOfMeasurementBeanList;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;

public class VarDetailHelper
{
	// Alessandro: added virtual key support
	private static String buildKeyboardInputs = (VirtualKeyboard.getInstance().isOnScreenKey() ? "buildKeyboardInputs();" : "");
	public static final int VSCROLLBAR_WIDTH = 21;
	public static final int OFFSET_WIDTH = 80;
	

	
	private static HashMap<Integer,String> grpCat = new HashMap<Integer,String>();
	
	public static String getHTMLAlarmsTable(int idsite,int iddev,String language, int width, int height,boolean isprotected) throws Exception
	{
		StringBuffer html = new StringBuffer();
		
		LangService lan = LangMgr.getInstance().getLangService(language);
		//stringhe header
		String description = lan.getString("devdetail","description");
		String active = lan.getString("devdetail","active");
		String priority = lan.getString("devdetail","priority");
		String category = lan.getString("evnsearch", "category");
		
		//stringhe priority
		String low = lan.getString("devdetail","low");
		String mid = lan.getString("devdetail","mid");
		String high = lan.getString("devdetail","high");
		String veryhigh = lan.getString("devdetail","veryhigh");
		
		int table_height = height*45/100;
		int table_width = width - OFFSET_WIDTH;
    	
       	html.append("<div id='theader' style='overflow:hidden;width:"+(table_width+VSCROLLBAR_WIDTH)+"'>");
    	html.append("<table width='"+table_width+"' cellpadding='1' cellspacing='1' class='table'>\n");
    	html.append("<tr class='th' height='20' >\n");      
    	/*html.append("<td align='center' width='5%'><b>"+active+"</b></td>\n");
    	html.append("<td align='center' width='50%'><b>"+description+"</b></td>\n");
    	html.append("<td align='center' width='12%'><b>"+priority+"</b></td>\n");
    	html.append("<td align='center' width='*'><b>"+category+"</b></td>\n");*/
    	html.append("<th id='THalarm00' align='center' width='60px'><b>"+active+"</b></td>\n");
    	html.append("<th id='THalarm01' align='center' ><b>"+description+"</b></td>\n");
    	html.append("<th id='THalarm02' align='center' width='120px'><b>"+priority+"</b></td>\n");
    	html.append("<th id='THalarm03' align='center' width='250px'><b>"+category+"</b></td>\n");
    	html.append("</tr>\n");
    	html.append("</table>\n");
    	html.append("</div>");
    	
    	html.append("<div id='tdata' style='width:"+(table_width+VSCROLLBAR_WIDTH)+";height:"+table_height+";overflow-X:auto;overflow-Y:auto;'>");
    	html.append("<table width='"+table_width+"' id='TableData1' cellpadding='1' cellspacing='1' class='table' style='table-layout:fixed'>\n");
    	
    	VarphyBeanList alarmlist = new VarphyBeanList();
    	VarphyBean[] alarms = alarmlist.getAlarmVarPhy(language,idsite,iddev);
    	VarphyBean alarm = null;
    	int idalarm = -1;
    	String pr = "";
    	String cate = "";
    	int freqval = 30;
    	boolean isactive = false;
		for (int i=0;i<alarms.length;i++)
		{
			alarm = alarms[i];
			idalarm=alarm.getId();
			isactive = alarm.isActive();
			pr = "";
			if (isactive)
			{
				freqval = alarm.getFrequency();
				int priorityVal = alarm.getPriority().intValue(); 
				switch (priorityVal)
				{
					case 1 :
					{
						pr = veryhigh;
						break;
					}
					case 2 :
					{
						pr = high;
						break;
					}
					case 3 :
					{
						pr = mid;
						break;
					}
					case 4 :
					{
						pr = low;
						break;
					}	
					default :
						break;
				}
				
				if(priorityVal > 4)
				{
					pr = low;
				}
				
				Integer grpcat = alarm.getGrpcategory();
				cate = getGrpAlarmDesc(idsite, language, grpcat);
			}
			html.append("<input type='hidden' id='alr_id"+i+"' value='"+idalarm+"' />\n");
			html.append("<input type='hidden' id='alr_"+idalarm+"' name='alr_"+idalarm+"' value='"+(alarm.isActive()?true:false)+"' />\n");
			html.append("<input type='hidden' id='prior"+idalarm+"' name='prior"+idalarm+"' value='"+alarm.getPriority().toString()+"'/>\n");
			String rowColor= i%2==0?"Row1":"Row2";
			if (!isprotected)
			{
				html.append("<tr class='"+rowColor+"'  style='cursor:pointer' onclick='hide_set("+i+");select_row("+i+");mod_line("+i+");' title='"+lan.getString("devdetail","clickmod")+"'>\n");
			}
			else
			{
				html.append("<tr class='"+rowColor+"'  onclick='select_row("+i+");' >\n");
			}
			html.append("<td class='standardTxt' align='center' width='60px'>"+(alarm.isActive()?"X":"")+"</td>\n");
			html.append("<td class='standardTxt' align='left' style='word-break:break-word'>"+alarm.getShortDescription()+"</td>\n");
			html.append("<td class='standardTxt' align='center' width='120px'>"+pr+"</td>\n");
			html.append("<td class='standardTxt' align='center' width='250px' style='word-break:break-word'>"+cate+"</td>\n");
			html.append("</tr>\n");	
		}
				
		html.append("</table></div>\n");
		html.append("<input type='hidden' id='freqval' value='"+freqval+"'/>");
		html.append("<input type='hidden' id='veryhigh' value='"+veryhigh+"'/>");
		html.append("<input type='hidden' id='high' value='"+high+"'/>");
		html.append("<input type='hidden' id='mid' value='"+mid+"'/>");
		html.append("<input type='hidden' id='low' value='"+low+"'/>");
		return html.toString();
	}
	
	public static String getGrpAlarmDesc(int idsite, String language, Integer idgrpcat)
	{
		String categor = "";
		Integer idvargroup = null;
		String descr = "";
		RecordSet rs = null;
		
		if (grpCat.size() == 0)
		{

			String sql = "select cfvarmdlgrp.idvargroup, cftableext.description from " +
				"cfvarmdlgrp, cftableext where cfvarmdlgrp.idsite=? and cfvarmdlgrp.isalarm='TRUE' and cftableext.idsite=" +
				"cfvarmdlgrp.idsite and cftableext.tablename='cfvarmdlgrp' " +
				"and cftableext.languagecode=? and cftableext.tableid = cfvarmdlgrp.idvargroup";

			try
			{
				rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(idsite), language});
				
				for (int i = 0; i < rs.size(); i++)
		    	{
		    		idvargroup = (Integer) rs.get(i).get("idvargroup");
		    		descr = (String) rs.get(i).get("description");
		    		
		    		grpCat.put(idvargroup, descr);
		    	}
			}
			catch (Exception e)
			{
				Logger logger = LoggerMgr.getLogger(VarDetailHelper.class);
                logger.error(e);
			}
		}
		
		categor = grpCat.get(idgrpcat);
		
		if (categor == null)
		{
			categor = "--- unknown ---";
		}
		
		return categor;
	}
	
	public static String getHTMLDescriptionsTable(int idsite,int iddev,String language, int width, int height) throws Exception
	{
		StringBuffer html = new StringBuffer();
		
		LangService lan = LangMgr.getInstance().getLangService(language);
		String varcode = lan.getString("devdetail","varcode");
		String descvars = lan.getString("devdetail","descvars");
		String shortdesc = lan.getString("devdetail","code");
		String descmisure = lan.getString("devdetail","descrmeasure");
		
		int table_height = height*47/100;
		int table_width = width - OFFSET_WIDTH;
		   	
    	html.append("<div id='theader' style='overflow:hidden;width:"+(table_width+VSCROLLBAR_WIDTH)+"'>\n");
    	html.append("<table width='"+table_width+"' cellpadding='1' cellspacing='1' class='table'>\n");
    	html.append("<tr class='th' height='20' >\n");      
    	html.append("<th id='THdescr00' align='center' width='200px'><b>"+varcode+"</b></td>\n");
    	html.append("<th id='THdescr01' align='center' width='100px'><b>"+shortdesc+"</b></td>\n");
    	html.append("<th id='THdescr02' align='center'><b>"+descvars+"</b></td>\n");
    	html.append("<th id='THdescr03' align='center' width='90px'><b>"+descmisure+"</b></td>\n");
    	html.append("</tr>\n");
    	html.append("</table>\n");
    	html.append("</div>\n");
    	
    	html.append("<div id='tdata' style='width:"+(table_width+VSCROLLBAR_WIDTH)+";height:"+table_height+";overflow-X:auto;overflow-Y:auto;'>\n");
    	html.append("<table width='"+table_width+"' id='TableData1' cellpadding='1' cellspacing='1' class='table' style='table-layout:fixed'>\n");
    	
    	VarphyBeanList varlist = new VarphyBeanList();
		VarphyBean[] vars = varlist.getAllVarOfDevice(language,idsite,iddev);
		
		for (int i=0;i<vars.length;i++)
		{
			String rowColor= i%2==0?"Row1":"Row2";
			html.append("<input type='hidden' id='shortDesc_"+i+"' name='shortDesc_"+i+"' value=\""+vars[i].getShortDesc()+"\" />");
			html.append("<input type='hidden' id='description_"+i+"' name='description_"+i+"' value=\""+vars[i].getShortDescription()+"\" />");
			html.append("<input type='hidden' id='misura_"+i+"' name='misura_"+i+"' value='"+vars[i].getMeasureUnit()+"'/>");
			html.append("<input type='hidden' id='id_"+i+"' name='id_"+i+"' value='"+String.valueOf(vars[i].getId())+"'/>");
			html.append("<tr class='"+rowColor+"'  style='cursor:pointer' onclick='select_row("+i+");hide_set("+i+");mod_line("+i+");"+buildKeyboardInputs+"' title='"+lan.getString("devdetail","clickmod")+"'>\n");
			html.append("<td class='standardTxt' width='200px' style='word-break:break-all'>"+vars[i].getCodeVar()+"</td>\n");
			html.append("<td class='standardTxt' width='100px' style='word-break:break-all'>"+vars[i].getShortDesc()+"</td>\n");
			html.append("<td class='standardTxt' style='word-break:break-word'>"+vars[i].getShortDescription()+" ["+vars[i].getId().intValue()+"]</td>\n");
			html.append("<td class='standardTxt' width='90px' style='word-break:break-all'>"+vars[i].getMeasureUnit()+"</td>\n");
			html.append("</tr>\n");	
		}
		html.append("</table>\n</div>\n");

		UnitOfMeasurementBeanList umbl = new UnitOfMeasurementBeanList();
		String combo_um = umbl.ComboWithSelectedMeasurement();
		html.append("<INPUT type=\"hidden\" id=\"num\" name=\"num\" value=\""+vars.length+"\"/>");
		html.append("<INPUT type=\"hidden\" id=\"combo_um\" value=\""+combo_um+"\"/>");
		
		return html.toString();
	}
	
	public static String gethistorycalTable(int idsite, int iddev,String language, boolean isProtected,int scr_width,int scr_height) throws Exception
    {
    	StringBuffer html = new StringBuffer();
    	
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	VarAggregator varlist = new VarAggregator(idsite,language,iddev);
    	VarphyBean[] vars = varlist.getVarList();
    	
    	String code = lan.getString("devdetail","varcode");
    	String variable = lan.getString("devdetail","variable");
    	String um = lan.getString("dtlview","col3");
    	String haccp = lan.getString("devdetail","haccp");
    	String history = lan.getString("devdetail","history");
    	
    	String freq = lan.getString("devdetail","freq");
    	//String sec = lan.getString("devdetail","sec");
    	String varmin = lan.getString("devdetail","varmin");
    	String profhistory = lan.getString("devdetail","profhistory");
    	
    	String[] prof_desc = new String[] {
			lan.getString("devdetail","week"),
			lan.getString("devdetail","month"),
			lan.getString("devdetail","month2"),
			lan.getString("devdetail","month6"),
			lan.getString("devdetail","year"),
			lan.getString("devdetail","year15"),
			lan.getString("devdetail","year2")
    	};
    	
    	Map<Integer,String> desc_freq = new HashMap<Integer,String>();
    	desc_freq.put(5,"5s");
    	desc_freq.put(10,"10s");
    	desc_freq.put(15,"15s");
    	desc_freq.put(30,"30s");
    	desc_freq.put(60,"1m");
    	desc_freq.put(120,"2m");
    	desc_freq.put(300,"5m");
    	desc_freq.put(600,"10m");
    	desc_freq.put(900,"15m");
    	desc_freq.put(1800,"30m");
    	desc_freq.put(3600,"1h");
    	
    	Boolean ishaccp = false;    		
    	Boolean hist = false;
    	int freqval= 0;
    	double delta = 0;
    	String s_freqval = "";
    	String s_delta = "";
    	 	
    	int table_height = scr_height*51/100;
    	int table_width = scr_width - OFFSET_WIDTH;
    	    	  	
       	html.append("<div id='theader' width:"+(table_width+VSCROLLBAR_WIDTH)+"' style='overflow:hidden;'>");
    	html.append("<table width='"+table_width+"' cellpadding='1' cellspacing='1' class='table'>\n");
    	html.append("<tr class='th'>\n");      
    	html.append("<th id='THhistorical00' align='center' width='130px'><b>"+code+"</b></td>\n");
    	html.append("<th id='THhistorical01' align='center' width='*'><b>"+variable+"</b></td>\n");
    	html.append("<th id='THhistorical02' align='center' width='90px'><b>"+um+"</b></td>\n");
    	html.append("<th id='THhistorical03' align='center' width='55px'><b>"+haccp+"</b></td>\n");
    	html.append("<th id='THhistorical04' align='center' width='55px'><b>"+history+"</b></td>\n");
    	html.append("<th id='THhistorical05' align='center' width='100px'><b>"+profhistory+"</b></td>\n");
    	html.append("<th id='THhistorical06' align='center' width='75px'><b>"+varmin+"</b></td>\n");
    	html.append("<th id='THhistorical07' align='center' width='100px'><b>"+freq+"</b></td>\n");
    	html.append("</tr>\n");
    	
    	html.append("</table>\n");
    	html.append("</div>\n");
    	html.append("<div id='tdata' onscroll='HScroll();' style='width:"+(table_width+VSCROLLBAR_WIDTH)+";height:"+table_height+";overflow-X:auto;overflow-Y:auto;'>");
    	html.append("<table width='"
			+ table_width
			+ "' id='TableData1' cellpadding='1' cellspacing='1' class='table'>\n");
    	
    	for (int i=0;i<vars.length;i++)
    	{
    		int period = 15552000;
    		if (vars[i].isHaccp())
    		{
    			ishaccp = true;
    		}
    		else ishaccp=false;
    		
    		if (vars[i].getSon()!=null)
    		{
    			hist= true;
    			freqval = vars[i].getSon().getFrequency().intValue();
    			s_freqval = (freqval==0)?"":String.valueOf(freqval);
    			delta = vars[i].getSon().getVariation();
    			s_delta = (delta==0)?"0":String.valueOf(delta);
    			if (vars[i].getSon().getType()!=2)
    				s_delta = s_delta.replace(".0","");
    			
    			try
    			{
    				int idvariable = vars[i].getSon().getId().intValue();
    				if( VarAggregator.isVaribleInReorder(idsite, idvariable) )
    					period = VarAggregator.getNewPeriod(idsite, idvariable);
    				else
    					period = VarAggregator.getPeriod(idsite, idvariable, vars[i].getSon().getFrequency().intValue());
				}
    			catch (Exception e)
    			{
    				// in caso di errore fornisco una config. di default:
    				period = 15552000; // 6 mesi
    				int fr = 300; // 5 min.
    				short keymax = 891; // (short)((period * 1.1) / (fr * 64));
    				
    				String sql = "insert into buffer values (1, "+vars[i].getSon().getId().intValue()+", "+keymax+", -1, false)";
    				try
    				{
						DatabaseMgr.getInstance().executeStatement(null, sql, null);
					}
    				catch (Exception e1)
    				{
						e = e1;
					}
    				
    				Logger logger = LoggerMgr.getLogger(VarDetailHelper.class);
                    logger.error(e);
				}
    		}
    		else
    		{
    			s_delta = "0";
    			hist=false;
    			s_freqval = "300";
    		}
    		VarphyBean tmpvar = vars[i];
    		int[] constvalue = new int[]{604800,2592000,5184000,15552000,31536000,46656000,62208000};
    		int min = Math.abs(period-604800);
    		int index = 0;
    		for (int j=1;j<constvalue.length;j++)
    		{
    			if (Math.abs(period-constvalue[j])<min)
    			{
    				min = Math.abs(period-constvalue[j]);
    				index = j;
    			}
    		}
    		    		
    		int id_var = tmpvar.getId();	
    		String rowColor= i%2==0?"Row1":"Row2";
    		if(!isProtected)
    		{
    			// Alessandro : added function buildKeyboardInputs() to enable Virtual Keyboard
    			html.append("<TR style='cursor:pointer'  title='"+lan.getString("devdetail","clickmod")+"' class='"+rowColor+"'  onclick='hide_set("+i+");select_row("+i+");mod_hist("+i+");"+buildKeyboardInputs+"'>\n");
    		}
    		else
    		{
    			html.append("<TR class='"+rowColor+"'>\n");
    		}
    			
    		String t = tmpvar.getCodeVar();
    		//campi hidden coi valori da postare
    		html.append("<input type='hidden' id='val"+i+"' name='val"+i+"' value='"+id_var+"'/>\n");
    		html.append("<input type='hidden' name='haccp"+id_var+"' id='haccp"+id_var+"'  value='"+(ishaccp?"true":"false")+"'/>\n");
    		html.append("<input type='hidden' name='hist"+id_var+"' id='hist"+id_var+"' value='"+(hist?"true":"false")+"'/>\n");
    		html.append("<input type='hidden' id='prof"+id_var+"' name='prof"+id_var+"' value='"+constvalue[index]+"'/>");
    		html.append("<input type='hidden' id='delta"+id_var+"' name='delta"+id_var+"' value='"+s_delta+"' />\n");
    		html.append("<input type='hidden' id='freq"+id_var+"' name='freq"+id_var+"' value='"+s_freqval+"'/>");
    		html.append("<input type='hidden' id='type"+id_var+"' value='"+tmpvar.getType()+"'/>");
    		html.append("<input type='hidden' id='dec"+id_var+"' value='"+tmpvar.getDecimal()+"'/>");
    		   		
    		html.append("<TD class='standardTxt' width='130px' style='word-break:break-all'>"+((null != t)?t:"")+"</TD>\n");  
    		//descrizione
    		html.append("<TD class='standardTxt' style='word-break:break-word' width='*'>"+tmpvar.getShortDescription()+"</TD>\n"); 
    		//unit� di misura
    		html.append("<TD align='center' class='standardTxt' width='90px' style='word-break:break-all'>"+tmpvar.getMeasureUnit()+"</TD>\n");
    		//ishaccp
    		html.append("<TD align='center' class='standardTxt'width='55px'>"+(ishaccp?"X":"")+"</TD>\n");
    		//storico
    		html.append("<TD align='center' class='standardTxt'width='55px'>"+(hist?"X":"")+"</TD>\n");
    		//profondit� storico
    		html.append("<TD align='center' class='standardTxt'width='100px'>"+(hist?prof_desc[index]:"")+"</TD>\n");
    		//variazione minima
    		html.append("<TD align='center' class='standardTxt'width='75px'>"+(hist?s_delta:"")+"</TD>\n");
    		//frequenza
    		html.append("<TD align='center' class='standardTxt'width='100px'>"+(hist?desc_freq.get(Integer.parseInt(s_freqval)):"")+"</TD>\n");
    		
    		html.append("</TR>\n");
    	}
    	
    	html.append("</table>");
    	html.append("</div>");

    	return html.toString();
    }

}
