package com.carel.supervisor.presentation.ac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.ac.AcProperties;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;

public class MasterBeanList {
	
	Map<Integer,MasterBean> mlist = null; // < iddev, MasterBean >
	List<MasterBean> list = null;
	
	private static final String ID_DEVICE_MASTER = "iddevmaster";
	private static final String ID_VARIABLE_MASTER = "idvarmaster";
	private static final String DEFAULT_VALUE = "def";
	private static final String INDEX = "index";
    
    private static final String ENABLED = "enabled";
	

    public MasterBeanList() throws DataBaseException
    {
        mlist = new HashMap<Integer,MasterBean>();
        list = new ArrayList<MasterBean>();
        
        //controllo tabelle "ac_master" e "ac_slave" per consistenza configurazioni:
        //boolean changed = AcProcess.ctrl_ac_tables();
        
        String sql = "select * from ac_master order by iddevmaster";
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        
        if (rs != null)
        {
            
            Integer iddev = null;
            Integer enabled = null;
            
            Record r = null;
            
            for (int i = 0; i < rs.size(); i++)
            {
                r = rs.get(i);
                
                iddev = (Integer) r.get(ID_DEVICE_MASTER);
                enabled = (Integer) r.get(ENABLED);
                
                if (!mlist.containsKey(iddev))
                {
                    addMasterNode(iddev, enabled);
                }
                addVariable(mlist.get(iddev),r);
            }
        }
    }
    
	private void addMasterNode(Integer iddev, Integer enabled)
	{
		MasterBean m = new MasterBean(iddev, enabled);
		mlist.put(iddev,m);
		list.add(m);
	}
	
	private void addVariable(MasterBean m, Record r)
	{
		Integer idv = (Integer) r.get(ID_VARIABLE_MASTER);
		float def_v = (Float) r.get(DEFAULT_VALUE);
		Integer index = (Integer) r.get(INDEX);
		VarDefaultBean v = new VarDefaultBean(idv,index,def_v);
		m.addVar(idv,index,v);
	}
	
	public MasterBean getMasterById(Integer id)
	{
		return mlist.get(id);
	}
	
	/*
    public String getHTMLList(String lang) throws DataBaseException
	{
		StringBuffer html = new StringBuffer();
		html.append("<table border='0' cellpadding='2' cellspacing='0'>");
		MasterBean m = null;
		DeviceBean d = null;
		for (int i=0; i < list.size(); i++)
		{
			m = list.get(i);
			d = DeviceListBean.retrieveSingleDeviceById(1,m.getIddev(),lang);
			html.append("<tr height='20px'>");
			html.append("<td class='standardTxt'><b>M"+(i+1)+" = "+ d.getDescription()+"</b>");
            html.append(" nbsp; <input type='checkbox' ");
            if (m.getEnabled().intValue() == 1) html.append(" checked ");
            html.append(" name='M"+(i+1)+"' value='' /> </td>");
			html.append("</tr>");
		}
		html.append("</table>");
		return html.toString();
	}
	*/
    
    //N.B.: max 6 master (+ checkbox) in una matrice 3x2
    public String getHTMLList(String lang) throws DataBaseException
    {
        StringBuffer html = new StringBuffer();
        String grpName = "";
        String masters = "";
	//20091201-simon add
	//it will disable the calendar input box when the virtual Keyboard is open        
        boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
        
        if (list.size() > 0)
        {
            LangService l = LangMgr.getInstance().getLangService(lang);
            String masterDev = l.getString("ac","master_dev");
            
            html.append("<table border='0' cellpadding='5' cellspacing='2'>");
            html.append("<tr><td>");
            
            MasterBean m = null;
            DeviceBean d = null;
            
            int maxrow = (list.size() <= 3 ? list.size() : 3);
            
            html.append("<table border='0' cellpadding='1' cellspacing='0'>"); //inizio colonna a sinistra
            
            int i;
            for (i = 0; i < maxrow; i++)
            {
                m = list.get(i);
                d = DeviceListBean.retrieveSingleDeviceById(1,m.getIddev(),lang);
                
                html.append("<tr height='18px'>");
                
                html.append("<td class='standardTxt'><b> &nbsp; G"+(i+1)+"&nbsp;</td>");
                
                html.append("<td><input type='checkbox' ");
                if (m.getEnabled().intValue() == 1) html.append(" checked ");
                html.append(" name='mstr_"+(i+1)+"' id='mstr_"+(i+1)+"' value='"+m.getIddev()+"'/> - </td>");
                grpName = AcMdl.getGroupName(m.getIddev());
                
                html.append("<td class='standardTxt' style='cursor:pointer'>&nbsp;");
                html.append("<input type='text' size='50' id='grp_"+m.getIddev()+"' name='grp_"+m.getIddev()+"' class='"+(OnScreenKey?"keyboardInput":"standardTxt")+"' value='"+grpName+"' onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);' />");
                html.append("</td>");
                
                html.append("</tr>");
                
                
                html.append("<tr height='18px'>");
    
                html.append("<td colspan=2 class='standardTxt'>&nbsp;</td>");
                
                html.append("<td class='standardTxt'>&nbsp;"+masterDev+": &nbsp;<i>"+d.getDescription()+"</i></td>");
                
                html.append("</tr>");
                
                masters += ""+m.getIddev()+";";
            }
            html.append("</table>"); //fine colonna a sinistra
            html.append("</td>");
            
            if (list.size() > 3)
            {
                html.append("<td valign='top'>");
                html.append("<table border='0' cellpadding='1' cellspacing='0'>"); //inizio colonna a destra
                
                int j;
                for (j = i; j < list.size(); j++)
                {
                    m = list.get(j);
                    d = DeviceListBean.retrieveSingleDeviceById(1,m.getIddev(),lang);
                    
                    html.append("<tr class='table' height='18 px'>");
                    
                    html.append("<td class='standardTxt'><b> &nbsp; G"+(j+1)+"&nbsp;</td>");
                    
                    html.append("<td><input type='checkbox' ");
                    if (m.getEnabled().intValue() == 1) html.append(" checked ");
                    html.append(" name='mstr_"+(j+1)+"' id='mstr_"+(j+1)+"' value='"+m.getIddev()+"'/> - </td>");
                    grpName = AcMdl.getGroupName(m.getIddev());
                    
                    html.append("<td class='standardTxt' style='cursor:pointer'>&nbsp;");
                    html.append("<input type='text' size=38 id='grp_"+m.getIddev()+"' name='grp_"+m.getIddev()+"' class='"+(OnScreenKey?"keyboardInput":"standardTxt")+"' value='"+grpName+"' />");
                    html.append("</td>");
                    
                    html.append("</tr>");
                    
                    
                    html.append("<tr height='18px'>");
    
                    html.append("<td colspan=2 class='standardTxt'>&nbsp;</td>");
                    
                    html.append("<td class='standardTxt'>&nbsp;"+masterDev+": &nbsp;<i>"+d.getDescription()+"</i></td>");
                    
                    html.append("</tr>");
                    
                    masters += ""+m.getIddev()+";";
                }
                
                // per allineare le righe in caso abbia pi� di 3, ma meno di 6 master configurati:
                for (int w = j; w < 6; w++)
                {
                    html.append("<tr height='20px'>");
                    html.append("<td class='standardTxt'> &nbsp; </td>");
                    html.append("</tr>");
                }
                
                html.append("</table>"); //fine colonna a destra
                html.append("</td>");
            }
            
            html.append("</tr></table>");
            html.append("<input type='hidden' id='n_master' name='n_master' value='"+list.size()+"' />");
            
            if ((masters != null) || !masters.equals(""))
            {
                masters = masters.substring(0, masters.length() - 1);
            }
    
            html.append("<input type='hidden' id='masters' name='masters' value='"+masters+"' />");
        }
        
        return html.toString();
    }
    
	public int getNumberOfMaster()
	{
		return list.size();
	}
	
	public MasterBean getMasterByIndex(int i)
	{
		return list.get(i);
	}
	
	public String getHTMLOverviewTable(String lang) 
	{
		LangService l = LangMgr.getInstance().getLangService(lang);
		
		if (list.size()>0)
		{
			String nslave = l.getString("ac","nslave");
			//String variable = l.getString("ac","variable");
			String def = l.getString("ac","default");
            String noslaves = l.getString("ac","noslaves");
            String click4dtl = l.getString("ac","click4dtl");
            
            String ok_run = l.getString("ac","ok_run");
            String no_run = l.getString("ac","no_run");
			
            String msg = "";
            String running = "";
		    
            int num_of_var = (new AcProperties()).getProp("ac_maxvariable");
            int num_slaves = 0;
			
			StringBuffer html = new StringBuffer();
            html.append("<table width='98%' cellpadding='1' cellspacing='1' align='center'>\n");
            html.append("<tr>\n");
            html.append("<td>\n");
			
            MasterBean m = null;
			DeviceBean d = null;
            String var_origin = "";
			
            int index = -1;
			int i;
            for (i = 0; i < list.size(); i++)
			{
				m = list.get(i);

                HashMap<Integer,Integer> varlist = AcMaster.getMasterVars(m.getIddev());
                
                running = "<span align='right' ";
                
                if (m.getEnabled().intValue() == 1)
                {
                    running += "style='color:GREEN'><b>" + ok_run;
                }
                else
                {
                    running += "style='color:RED'><b>" + no_run;
                }
				
                running += "</b></span>";
                
                try
                {
					d = DeviceListBean.retrieveSingleDeviceById(1,m.getIddev(),lang);
				}
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
				}
                
                if (i == 3)
                {
                    html.append("</table>\n");
                    html.append("</td>\n");
                    html.append("<td valign='top'>\n");
                }
                
                if ((i == 0)||(i == 3))
                {
                    html.append("<table width='98%' cellpadding='1' cellspacing='1' align='center'>\n");
                }
                
				html.append("<tr>\n");
				
                if (i < 3)
                {
                    html.append("<td align='center'>\n");
                }
                else
                {
                    html.append("<td align='left'>\n");
                }
                
                if (m.getSlave_number() > 0)
                {
                    html.append("<div style='width:355px' onclick='openMasterDtl("+m.getIddev()+");'>\n");
                    msg = click4dtl;
                }
                else
                {
                    html.append("<div style='width:355px' onclick='noslaves();'>\n");
                    msg = noslaves;
                }
                
                html.append("<table style='cursor:pointer' title='"+msg+"' cellpadding='1' cellspacing='0' border='0' class='table' width='350px'>\n");
                html.append("<tr class='th'>\n");
				html.append("<td width='15px' align='center'>"+getLed(m.getIddev())+"</td>\n");
                
				//html.append("<td class='standardTxt'><b>M"+(i+1)+" :&nbsp; "+d.getDescription()+"</b></td>\n");
                html.append("<td class='standardTxt'><b>G"+(i+1)+":&nbsp; "+AcMdl.getGroupName(d.getIddevice())+"</b></td>\n");
                
                html.append("<td width='85px' style='background-color:WHITE' align='center'>"+running+"</td>\n");
				html.append("</tr>\n");
				
                for (int j = 0; j < num_of_var; j++)
				{
					index = j+1;
					//html.append("<tr class='Row1'><td></td><td colspan='2' class='standardTxt'>"+(m.getVariableDefByIndex(index)!=null?"<b>"+AcMaster.variables[j]+"</b> "+def+": "+ m.getVariableDefByIndex(index).getDef_value():"&nbsp;")+"</td></tr>\n");
                    html.append("<tr class='Row1'>\n");
                    html.append("<td></td>\n");
                    
                    if (m.getVariableDefByIndex(index) != null)
                    {
                        var_origin = "";
                        html.append("<td colspan=1 class='standardTxt'>&nbsp;&nbsp;<b>"+AcMaster.variables[j]+"</b></td>\n");
                        
                        //recupero valori dal campo:
                        try
                        {
                            var_origin = ControllerMgr.getInstance().getFromField(varlist.get(new Integer(j+1)).intValue()).getFormattedValue();
                            
                            html.append("<td class='standardTxt'>&nbsp;"+var_origin+"</td>\n");
                        }
                        catch (Exception e)
                        {
                            // PVPro-generated catch block:
                            Logger logger = LoggerMgr.getLogger(this.getClass());
                            logger.error(e);
                            
                            html.append("<td class='standardTxt'>&nbsp; *** &nbsp;</td>\n");
                        }
                    }
                    else
                    {
                        html.append("<td colspan=2 class='standardTxt'>&nbsp;</td>\n");
                    }
                    
                    html.append("</tr>\n");
				}
				
                num_slaves = m.getSlave_number();
                html.append("<tr class='Row1'>\n");
                html.append("<td></td>\n");
                html.append("<td class='standardTxt'>&nbsp;"+nslave+"</td>\n");
                html.append("<td class='standardTxt'");
                
                if (num_slaves == 0)
                    html.append(" style='color:RED;'");
                else
                    html.append(" style='color:GREEN;'");
                
                html.append("><b>"+num_slaves+"</b></td></tr>\n");
				html.append("</table>\n");
                html.append("</div>\n");
				html.append("</td>\n");
				html.append("</tr>\n");
				html.append("<tr height='20px'><td>&nbsp;</td></tr>\n");
			}
            html.append("</table>\n");
            
            if (i <= 3)
            {//completo colonna sinistra:
                html.append("</table>\n");
                html.append("</td>\n");
                //creo colonna destra vuota:
                html.append("<td>&nbsp;");
            }
            
            /*
            else if (i < 6)
            {//completo colonna destra:
                for (int w = i; w < 6; w++)
                {
                    html.append("<tr>\n");
                    html.append("<td class='standardTxt'> &nbsp; </td>\n");
                    html.append("</tr>\n");
                    html.append("<tr height='20px'><td>&nbsp;</td></tr>\n");
                }
                html.append("</table>\n");
            }
            */
            
            html.append("</td>\n");
            html.append("</tr>\n");
            html.append("</table>\n");
			
            return html.toString();
		}
		else
			return "<p class='tdTitleTable'>"+l.getString("ac","nomasteravailable")+"</p>\n";
	}
	
	private String getLed(Integer idDev)
    {
        StringBuffer buffer = new StringBuffer();
        String imgDecod = UtilDevice.getLedColor(idDev);

        buffer.append("<div id='DLed" + idDev + "' style='");
        buffer.append("background-image:url(images/led/L" + imgDecod +
            ".gif);background-repeat:no-repeat;background-position:center' ");
        buffer.append("><div style='visibility:hidden;'>" + imgDecod +
            "</div></div>");

        return buffer.toString();
    }
    
}
