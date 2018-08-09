package com.carel.supervisor.presentation.bo.helper;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;


public class VariableHelper
{
	private static int screenw = 1024;
	private static int screenh = 768;
	
	public VariableHelper()
    {
    }

    public static RemoveState isVariableInCondition(int idsite,
        int[] idsvariable, String lang) throws DataBaseException
    {
        String sql =
            "select cfvarcondition.idvariable,cfcondition.condcode from cfvarcondition,cfcondition where cfvarcondition.idsite = ? " +
            "and cfcondition.idsite = cfvarcondition.idsite and cfcondition.idcondition=cfvarcondition.idcondition " +
            //Start alarmP 2007/06/27 Fixing 
            "and cfcondition.condType!=?"; 
            //End
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite),"P" });
        boolean iscontained = false;
        int tmp_id = 0;
        String code = "";

        for (int i = 0; i < rs.size(); i++)
        {
            tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();
            code = rs.get(i).get("condcode").toString();

            for (int j = 0; j < idsvariable.length; j++)
            {
                if (tmp_id == idsvariable[j])
                {
                    iscontained = true;
                    break;
                }
            }

            if (iscontained)
            {
                break;
            }
        }

        String message = "";

        if (iscontained)
        {
            VarphyBean var = VarphyBeanList.retrieveVarById(idsite,tmp_id,lang);
            DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite,
                    var.getDevice().intValue(), lang);

            String var_desc = var.getShortDescription();
            String dev_desc = dev.getDescription();
            String line_desc = StringUtility.split(dev.getCode(), ".")[0];

            LangService lang_s = LangMgr.getInstance().getLangService(lang);
            message = lang_s.getString("control", "condition");
            message = message.replace("$1", var_desc);
            message = message.replace("$2", dev_desc);
            message = message.replace("$3", line_desc);
            message = message.replace("$4", code);
        }

        return new RemoveState(message, !iscontained);
    }

    public static RemoveState isVariableInAction(int idsite,
        int[] ids_variables, String lang) throws DataBaseException
    {
        boolean iscontained = false;

        String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and (actiontype = 'L' or actiontype = 'V')";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        String[] params = null;
        int tmp_id = 0;
        String code = "";

        for (int i = 0; i < rs.size(); i++) //ciclo sui record
        {
            params = rs.get(i).get("parameters").toString().split(";");
            code = rs.get(i).get("code").toString();

            for (int j = 0; j < params.length; j++) //ciclo sui param di ogni record
            {
                tmp_id = Integer.parseInt(params[j].split("=")[0]);

                for (int z = 0; z < ids_variables.length; z++) //ciclo array ids_variable e confronto
                {
                    if (tmp_id == ids_variables[z])
                    {
                        iscontained = true;

                        break;
                    }
                }

                if (iscontained)
                {
                    break;
                }
            }
        }

        String message = "";

        if (iscontained)
        {
            VarphyBean var = VarphyBeanList.retrieveVarById(idsite,tmp_id,lang);
            DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite,
                    var.getDevice().intValue(), lang);

            String var_desc = var.getShortDescription();
            String dev_desc = dev.getDescription();
            String line_desc = StringUtility.split(dev.getCode(), ".")[0];

            LangService lang_s = LangMgr.getInstance().getLangService(lang);
            message = lang_s.getString("control", "action");
            message = message.replace("$1", var_desc);
            message = message.replace("$2", dev_desc);
            message = message.replace("$3", line_desc);
            message = message.replace("$4", code);
        }

        return new RemoveState(message, !iscontained);
    }

    public static RemoveState isVariableInReport(int idsite,
        int[] ids_variables, String lang) throws DataBaseException
    {
        boolean iscontained = false;

        String sql = "select code,parameters from cfreport where idsite = ? ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        String[] params = null;
        int tmp_id = 0;
        String code = "";

        for (int i = 0; i < rs.size(); i++) //ciclo sui record
        {
            params = rs.get(i).get("parameters").toString().split(";");
            code = rs.get(i).get("code").toString();

            for (int j = 0; j < params.length; j++) //ciclo sui param di ogni record
            {
                tmp_id = Integer.parseInt(params[j].replace("pk", ""));

                for (int z = 0; z < ids_variables.length; z++) //ciclo array ids_variable e confronto
                {
                    if (tmp_id == ids_variables[z])
                    {
                        iscontained = true;

                        break;
                    }
                }

                if (iscontained)
                {
                    break;
                }
            }

            if (iscontained)
            {
                break;
            }
        }

        String message = "";

        if (iscontained)
        {
            VarphyBean var = VarphyBeanList.retrieveVarById(idsite,tmp_id,lang);
            DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite,
                    var.getDevice().intValue(), lang);

            String var_desc = var.getShortDescription();
            String dev_desc = dev.getDescription();
            String line_desc = StringUtility.split(dev.getCode(), ".")[0];

            LangService lang_s = LangMgr.getInstance().getLangService(lang);
            message = lang_s.getString("control", "report");
            message = message.replace("$1", var_desc);
            message = message.replace("$2", dev_desc);
            message = message.replace("$3", line_desc);
            message = message.replace("$4", code);
        }

        return new RemoveState(message, !iscontained);
    }

    public static RemoveState isVariableInLogic(int idsite, int[] ids_variable,
        String lang) throws DataBaseException
    {
        String sql =
            "select cffunction.parameters,cfvariable.idvariable from cffunction inner join cfvariable " +
            "on cfvariable.iscancelled='FALSE' and cfvariable.functioncode is not null " +
            "and cfvariable.functioncode = cffunction.functioncode";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        boolean iscontained = false;
        int tmp_id = 0;
        String idvarlogic = "";
        for (int i = 0; i < rs.size(); i++) //ciclo sui parameters e ci lavoro
        {
            String[] params = rs.get(i).get("parameters").toString().split(";");
            idvarlogic = rs.get(i).get("idvariable").toString();
            for (int j = 0; j < params.length; j++)
            {
                if (params[j].contains("pk"))
                {
                    tmp_id = Integer.parseInt(params[j].replace("pk", ""));

                    for (int z = 0; z < ids_variable.length; z++)
                    {
                        if (tmp_id == ids_variable[z])
                        {
                            iscontained = true;

                            break;
                        }
                    }

                    if (iscontained)
                    {
                        break;
                    }
                }

                if (iscontained)
                {
                    break;
                }
            }
            
            if (iscontained)
            {
                break;
            }            
        }

        String message = "";

        if (iscontained)
        {
        	VarphyBean var = VarphyBeanList.retrieveVarById(idsite,tmp_id,lang);
            DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite,
                    var.getDevice().intValue(), lang);

            String var_desc = var.getShortDescription();
            String dev_desc = dev.getDescription();
            String line_desc = StringUtility.split(dev.getCode(), ".")[0];
            String var_logic_desc = VarphyBeanList.getShortDescriptionOfVars(idsite,lang,Integer.parseInt(idvarlogic));
            if (null == var_logic_desc)
            {
            	var_logic_desc = "";
            }
            LangService lang_s = LangMgr.getInstance().getLangService(lang);
            message = lang_s.getString("control", "logic");
            message = message.replace("$1", var_desc);
            message = message.replace("$2", dev_desc);
            message = message.replace("$3", line_desc);
            message = message.replace("$4", var_logic_desc);
        }

        return new RemoveState(message, !iscontained);
    }
    
    
    
    public static RemoveState isVariableInGuardian(int idsite, int[] ids_variable,
            String lang) throws DataBaseException
        {
            String sql = "select idvariable from cfvarguardian";

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
            Map<Integer,Integer> map = new HashMap<Integer,Integer>();
            Integer idvar =  null;
            for (int i = 0; i < rs.size(); i++) 
            {
            	idvar = (Integer)rs.get(i).get(0);
                map.put(idvar,idvar);
            }

            String message = "";
            
            for(int i = 0; i < ids_variable.length; i++)
            {
            	if (map.containsKey(new Integer(ids_variable[i])))
            	{
            		VarphyBean var = VarphyBeanList.retrieveVarById(idsite,ids_variable[i],lang);
                    DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite,
                            var.getDevice().intValue(), lang);

                    String var_desc = var.getShortDescription();
                    String dev_desc = dev.getDescription();
                    String line_desc = StringUtility.split(dev.getCode(), ".")[0];
                    
            		LangService lang_s = LangMgr.getInstance().getLangService(lang);
                    message = lang_s.getString("control", "guardian");
                    message = message.replace("$1", var_desc);
                    message = message.replace("$2", dev_desc);
                    message = message.replace("$3", line_desc);
                    return new RemoveState(message, false);
            	}
            }

            return new RemoveState(message, true);
        }
    
    /*
    public static String getHTMLhistorycalTable(int idsite, int iddev,String language, boolean isProtected,int screenw,int screenh) throws Exception
    {
    	LangService lan = LangMgr.getInstance().getLangService(language);
    	 	
    	VarAggregator varlist = new VarAggregator(idsite,language,iddev);
    	VarphyBean[] vars = varlist.getVarList();
    	
    	String variable = lan.getString("devdetail","variable");
    	String haccp = lan.getString("devdetail","haccp");
    	String history = lan.getString("devdetail","history");
    	
    	String code = lan.getString("devdetail","code");
    	String freq = lan.getString("devdetail","freq");
    	//String sec = lan.getString("devdetail","sec");
    	String varmin = lan.getString("devdetail","varmin");
    	String profhistory = lan.getString("devdetail","profhistory");
    	String week = lan.getString("devdetail","week");
    	String month = lan.getString("devdetail","month");
    	String month2 = lan.getString("devdetail","month2");
    	String month6 = lan.getString("devdetail","month6");
    	String year = lan.getString("devdetail","year");
    	
       	String ishaccp = "";
    	String disabTxt = "";
    		
    	String hist = "";
    	int freqval= 0;
    	double delta = 0;
    	String s_freqval = "";
    	String s_delta = "";
    	String disab_freq = "";
    	String disab_delta = "";
    		
    	
    	//tabella
    	String[] header = new String[]{code,variable, haccp, history , profhistory, varmin, freq};
    	HTMLElement[][] tableData = new HTMLElement[vars.length][];
    		
    	for (int i=0;i<vars.length;i++)
    	{
    		int period = 15552000;
    		if (vars[i].isHaccp())
    		{
    			ishaccp = "checked";
    		}
    		else ishaccp="";
    		
    		if (vars[i].getSon()!=null)
    		{
    			hist= "checked";
    			freqval = vars[i].getSon().getFrequency().intValue();
    			s_freqval = (freqval==0)?"":String.valueOf(freqval);
    			delta = vars[i].getSon().getVariation();
    			s_delta = (delta==0)?"0":String.valueOf(delta);
    			if (vars[i].getSon().getType()!=2)
    				s_delta = s_delta.replace(".0","");
    			period = VarAggregator.getPeriod(idsite,vars[i].getSon().getId().intValue(),vars[i].getSon().getFrequency().intValue());
    			disabTxt = "";
    			
    			disab_freq = "";
    			disab_delta = "";
    			
    		}
    		else
    		{
    			disabTxt = "disabled";
    			disab_freq = "disabled";
    			disab_delta = "disabled";
    			s_delta = "0";
    			hist="";
    			s_freqval = "300";
    		}
    		VarphyBean tmpvar = vars[i];
    		int[] constvalue = new int[]{604800,2592000,5184000,15552000,31536000};
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
    		
    		
    		if (isProtected)
    		{
    			disabTxt = "disabled";
    			disab_freq = "disabled";
    			disab_delta = "disabled";
    			ishaccp = ishaccp + " disabled";
    			hist = hist + " disabled";
    		}
    		
    				
    		tableData[i]= new HTMLElement[7];
    		tableData[i][0] = new HTMLSimpleElement(tmpvar.getShortDesc());
    		//descrizione
    		tableData[i][1] = new HTMLSimpleElement(tmpvar.getShortDescription());
    		//ishaccp
    		tableData[i][2] = new HTMLSimpleElement("<INPUT type='hidden' id='val"+i+"' name='val"+i+"' value='"+tmpvar.getId()+"'/><input "+ishaccp+" type='checkbox' name='haccp"+tmpvar.getId()+"' id='haccp"+tmpvar.getId()+"'/>");
    		
    		//storico
    		if (tmpvar.getType()!=1)
    		{
    			tableData[i][3] = new HTMLSimpleElement("<input "+hist+" type='checkbox' name='hist"+tmpvar.getId()+"' id='hist"+tmpvar.getId()+"' onclick='freq_disab("+tmpvar.getId()+")'/>");
    		}
    		else
    		{
    			tableData[i][3] = new HTMLSimpleElement("<input "+hist+" type='checkbox' name='hist"+tmpvar.getId()+"' id='hist"+tmpvar.getId()+"' onclick='freq_disab_novarmin("+tmpvar.getId()+")'/>");
    		}
    		
    		//profondità storico
    		tableData[i][4] = new HTMLSimpleElement("<SELECT class='lswtype' "+disabTxt+" id='prof"+tmpvar.getId()+"' name='prof"+tmpvar.getId()+"'/><OPTION "+((index==0)?"selected":"")+" value='604800'>"+week+"</OPTION><OPTION "+((index==1)?"selected":"")+" value='2592000'>"+month+"</OPTION><OPTION "+((index==2)?"selected":"")+" value='5184000'>"+month2+"</OPTION><OPTION "+((index==3)?"selected":"")+" value='15552000'>"+month6+"</OPTION><OPTION "+((index==4)?"selected":"")+" value='31536000'>"+year+"</OPTION></SELECT>");
    				
    			
    		//variazione minima
    		
    		if (tmpvar.getType()==1) //digitale
    		{
    			tableData[i][5] = new HTMLSimpleElement("&nbsp;");
    			
    		}
    		else if (tmpvar.getType()==2)  //analogica
    		{
    			tableData[i][5] = new HTMLSimpleElement("<table width='90%' ><tr><td align='left' class='standardTxt' ><input class='lswtype' "+disab_delta+" type='textarea' size='5' maxlength='5' id='delta"+tmpvar.getId()+"' name='delta"+tmpvar.getId()+"' value='"+s_delta+"' onkeydown='checkOnlyAnalog(this,event);'/> "+((tmpvar.getMeasureUnit()==null)?"":tmpvar.getMeasureUnit())+"</td></tr></table/>");
    		}
    		else   //intera
    		{
    			tableData[i][5] = new HTMLSimpleElement("<table width='90%' ><tr><td align='left' class='standardTxt' ><input class='lswtype' "+disab_delta+" type='textarea' size='5' maxlength='5' id='delta"+tmpvar.getId()+"' name='delta"+tmpvar.getId()+"' value='"+s_delta+"' onblur='onlyNumberOnBlur(this);' onkeydown='checkOnlyNumber(this,event);'/> "+((tmpvar.getMeasureUnit()==null)?"":tmpvar.getMeasureUnit())+"</td></tr></table/>");
    		}
    		
    		//frequenza
    		//tableData[i][5] = new HTMLSimpleElement("<input class='lswtype' "+disab_freq+" type='textarea' size='6' maxlength='5' id='freq"+tmpvar.getId()+"' name='freq"+tmpvar.getId()+"' value='"+s_freqval+"' onblur='onlyNumberOnBlur(this);' onkeydown='checkOnlyNumber(this,event);'/> "+sec+"");
    		tableData[i][6] = new HTMLSimpleElement(getHTMLFreqCombo(tmpvar.getId().intValue(),Integer.parseInt(s_freqval),disab_freq));
    	}
    	
    	HTMLTable varTable = new HTMLTable("", header, tableData);
    	varTable.setScreenH(screenh);
    	varTable.setScreenW(screenw);
    	varTable.setWidth(900);
    	varTable.setHeight(310);
    	varTable.setColumnSize(0,50);
    	varTable.setColumnSize(6,125);
        varTable.setRowHeight(25);
    	varTable.setAlignType(new int[]{1,0,1,1,1,1,1});
    	varTable.setTableId(1);
    	String table = varTable.getHTMLText();
    	
    	return table;
    }
    
    private static String getHTMLFreqCombo(int varid, int secondsSelected, String disab)
    {
    	StringBuffer html = new StringBuffer();
    	//boolean sel = false;
    	
    	html.append("<select "+disab+" id='freq"+varid+"' name='freq"+varid+"' class='standardTxt'>");
    	html.append("<option "+((secondsSelected==5)?"selected":"" )+ " value='5'>5s</option>");
    	html.append("<option "+((secondsSelected==10)?"selected":"" )+ "  value='10'>10s</option>");
    	html.append("<option "+((secondsSelected==15)?"selected":"" )+ "  value='15'>15s</option>");
    	html.append("<option "+((secondsSelected==30)?"selected":"" )+ "  value='30'>30s</option>");
    	html.append("<option "+((secondsSelected==60)?"selected":"" )+ "  value='60'>1m</option>");
    	html.append("<option "+((secondsSelected==120)?"selected":"" )+ "  value='120'>2m</option>");
    	html.append("<option "+((secondsSelected==300)?"selected":"" )+ "  value='300'>5m</option>");
    	html.append("<option "+((secondsSelected==600)?"selected":"" )+ "  value='600'>10m</option>");
    	html.append("<option "+((secondsSelected==900)?"selected":"" )+ "  value='900'>15m</option>");
    	html.append("<option "+((secondsSelected==1800)?"selected":"" )+ "  value='1800'>30m</option>");
    	html.append("<option "+((secondsSelected==3600)?"selected":"" )+ "  value='3600'>1h</option>");
    	html.append("</select>");
    	
    	
    	return html.toString();
    }*/
    
}
