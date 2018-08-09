package com.carel.supervisor.presentation.ac;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.ac.AcProperties;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.session.UserSession;

public class MasterBean
{
    private Integer iddev;
    private String descr;
    private Map<Integer,VarDefaultBean> m_var;
    private Map<Integer,VarDefaultBean> m_ind;
    private int slave_number = 0;

    //indica se il master � abilitato alla propagazione dei parametri:
    private Integer enabled; // 0=disabilitato, 1=abilitato
    

    public MasterBean(Integer iddev, Integer enabled)
    {
    	this.iddev = iddev;
        this.enabled = enabled;
        this.descr = AcMdl.getGroupName(iddev);
    	this.m_var = new HashMap<Integer,VarDefaultBean>();
    	this.m_ind = new HashMap<Integer,VarDefaultBean>();
    	
    	String sql = "select count(1) from (select iddevslave from ac_slave where iddevmaster=? group by iddevslave) as a";
    	
        try
    	{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{iddev});
			this.slave_number = (Integer) rs.get(0).get(0);
    	}
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
    }
    
    public MasterBean(Integer iddev)
    {
        // di default, un master � disabilitato (va' abilitato dall'utente nel SubTab2):
        this(iddev, 0);
    }
    
    public Integer getIddev()
    {
        return iddev;
    }

    public void setIddev(Integer iddev)
    {
        this.iddev = iddev;
    }

    public void addVar(Integer idvar,Integer index,VarDefaultBean v)
    {
    	m_var.put(idvar,v);
    	m_ind.put(index,v);
    }
    
    public VarDefaultBean getVariableDef(Integer idv)
    {
    	return m_var.get(idv);
    }
    
    public VarDefaultBean getVariableDefByIndex(Integer index)
    {
    	if (m_ind.containsKey(index))
    		return m_ind.get(index);
    	else
    		return null;
    }

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getSlave_number() {
		return slave_number;
	}

	public void setSlave_number(int slave_number) {
		this.slave_number = slave_number;
	}
    
    public void setEnabled(Integer enab_val)
    {
        this.enabled = enab_val;
    }
    
    public Integer getEnabled()
    {
        return this.enabled;
    }
    
    public String getMasterDtlTable(UserSession us, int scrHeight, int scrWidth)
    {
        String lang = us.getLanguage();
        boolean dtlviewEnabled = us.isMenuActive("dtlview");
        LangService l = LangMgr.getInstance().getLangService(lang);
        String click2goback = l.getString("ac","click2goback");
        String click2godtldev = l.getString("ac","click2godtldev");
        String banco = l.getString("ac","banco");
        
        String stato = l.getString("ac","stato");
        String devdescr = l.getString("ac","devdescr");
        String infofromser = l.getString("ac","infofromser");
        String localinfo = l.getString("ac","localinfo");
        
        //int n_cols = (new AcProperties()).getProp("ac_maxvariable");
        int n_cols = 3;
        String var_origin = "";
        
        int idsite = us.getIdSite();
        
        DeviceBean devmstrbean = null;
        
        try
        {
            devmstrbean = DeviceListBean.retrieveSingleDeviceById(1, this.getIddev(), lang);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        String running = "";
        String ok_run = l.getString("ac","ok_run");
        String no_run = l.getString("ac","no_run");

        if (this.getEnabled().intValue() == 1)
        {
            running = ok_run;
        }
        else
        {
            running = no_run;
        }
        
        StringBuffer tabella = new StringBuffer("");
        
        /*
        tabella.append("<fieldset class='field'>\n");
        tabella.append("<legend class='standardTxt'><div style='font-weight:bold'>"+banco+"</div></legend>\n");
        tabella.append("<table width='100%' height='15%'>\n");
        tabella.append("<tr class='standardTxt'>\n");
        tabella.append("<td><div style='color:GREEN'><b>&nbsp;"+AcMdl.getGroupName(this.getIddev())+"</b></div></td>\n");
        tabella.append("</tr>\n");
        tabella.append("</table>\n");
        tabella.append("</fieldset>\n");
        */
        tabella.append("<span class='standardTxt'><b>"+banco+":&nbsp;"+AcMdl.getGroupName(this.getIddev())+"</b></span>");        
        tabella.append("<br/>\n");
        //tabella.append("<br/>\n");
        
        //tabella.append("<div style='overflow-X:auto;overflow-Y:scroll;height:95%'>\n");
        tabella.append("<table style='cursor:pointer' class='table' border='0' width='"+(scrWidth*0.87)+"px' cellpadding='1' cellspacing='1'>\n");
        
        //riga intestazione 1:
        tabella.append("<tr class='th' onclick='goBackToAC();return false;'>\n");
        tabella.append("<td rowspan=2 width='8%' align='center'><b>Addr.</b></td>\n");
        tabella.append("<td rowspan=2 width='7%' align='center'><b>Link</b></td>\n");
        tabella.append("<td rowspan=2 width='6%' align='center'><b>"+stato+"</b></td>\n");
        tabella.append("<td rowspan=2 width='33%' align='center'><b>"+devdescr+"</b></td>\n");
        tabella.append("<td colspan=3 align='center'><b>"+infofromser+"</b></td>\n");
        tabella.append("<td colspan=2 align='center'><b>"+localinfo+"</b></td>\n");
        tabella.append("</tr>\n");
        
        //riga intestazione 2:
        tabella.append("<tr class='th' onclick='goBackToAC();return false;'>\n");
        /*
        tabella.append("<td>&nbsp;</td>\n");
        tabella.append("<td>&nbsp;</td>\n");
        tabella.append("<td>&nbsp;</td>\n");
        tabella.append("<td &nbsp;</td>\n");
        */
        tabella.append("<td width='8%' align='center'><b>Tamb</b></td>\n");
        tabella.append("<td width='8%' align='center'><b>rH%</b></td>\n");
        tabella.append("<td width='8%' align='center'><b>Tdew</b></td>\n");
        tabella.append("<td width='8%' align='center'><b>Tglass</b></td>\n");
        tabella.append("<td width='8%' align='center'><b>Output</b></td>\n");
        tabella.append("</tr>\n");
        
        tabella.append("</table>\n");
        tabella.append("<div style='overflow-X:auto;overflow-Y:auto;height:65%'>\n");
        tabella.append("<table class='table' border='0' width='"+(scrWidth*0.87)+"px' cellpadding='1' cellspacing='1'>\n");
        
        tabella.append("<tr class='Row1'>\n");
        tabella.append("<td width='8%' height='20px' class='standardTxt' align='center'><b>M("+devmstrbean.getAddress()+")</b></td>\n");
        tabella.append("<td width='7%' height='20px' class='standardTxt' align='center'><a onclick='go2DtlDev("+this.iddev+");return false;' title='"+click2godtldev+"' href='javascript:void(0)'><img src='images/system/right.png' border='0' complete='complete'/></a></td>\n");
        tabella.append("<td width='6%' height='20px' class='standardTxt' align='center'>"+getLed(this.getIddev())+"</td>\n");
        if(dtlviewEnabled)
        	tabella.append("<td width='33%' height='20px' class='standardTxt'><b><a href=\"javascript:void(0)\" onclick=\"top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+devmstrbean.getIddevice()+"&desc=ncode01')\">" + devmstrbean.getDescription() + "</a></b></td>\n");
        else
        	
        	tabella.append("<td width='33%' height='20px' class='standardTxt'><b>"+devmstrbean.getDescription()+"</b></td>\n");

        HashMap<Integer,Integer> master_vars = AcMaster.getMasterVars(this.iddev);
        
        // per ogni var del master:
        for (int j = 0; j < n_cols; j++)
        {
            tabella.append("<td width='8%' height='20px' align='center' class='standardTxt'>");
            var_origin = "";
            
            if (master_vars.get(new Integer(j+1)) != null)
            {
                try
                {
                    var_origin = ControllerMgr.getInstance().getFromField(master_vars.get(new Integer(j+1)).intValue()).getFormattedValue();
                    tabella.append(var_origin);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                    
                    tabella.append("<div style='color:RED'> err </div>");
                }
            }
            else
            {
                tabella.append("---");
            }
            
            tabella.append("</td>\n");
        }
        
        //gestione 2 var extra x master:
        int[] extraVars = AcMdl.getExtraVarsIds(this.getIddev());
        
        for (int k = 0; k < 2; k++)
        {
            var_origin = "";
            tabella.append("<td width='8%' height='20px' align='center' class='standardTxt'>");
            
            if (extraVars[k] == -1)
            {
                tabella.append(" --- ");
            }
            else
            {
                try
                {
                    var_origin = ControllerMgr.getInstance().getFromField(extraVars[k]).getFormattedValue();
                    tabella.append(var_origin);
                }
                catch (Exception e)
                {
                    // generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                    
                    tabella.append("<div style='color:RED'> err </div>");
                }
            }
            
            tabella.append("</td>\n");
        }
        
        tabella.append("</tr>\n");
        
        
        /*   // vecchia versione:
        tabella.append("<tr class='th'  title='"+click2goback+"' onclick='goBackToAC();return false;'>\n");
        tabella.append("<td height='20px' width='15px'>"+getLed(this.getIddev())+"</td>\n");
        
        //tabella.append("<td height='20px' align='center' class='standardTxt'><b>"+devmstrbean.getDescription()+"</b>&nbsp;("+running+")</td>\n");
        tabella.append("<td height='20px' align='center' class='standardTxt'><b>"+AcMdl.getGroupName(devmstrbean.getIddevice())+"</b>&nbsp;("+running+")</td>\n");
        
        for (int i = 0; i < n_cols; i++)
        {
            tabella.append("<td height='20px' width='70px' align='center' class='standardTxt'><b>"+AcMaster.variables[i]+"</b></td>\n");
        }
        tabella.append("<td height='20px' width='70px' align='center' class='standardTxt'><b>"+AcMaster.extra_vars[0]+"</b></td>\n");
        tabella.append("<td height='20px' width='70px' align='center' class='standardTxt'><b>"+AcMaster.extra_vars[1]+"</b></td>\n");
        
        tabella.append("</tr>\n");
        */
        
        //recupero gli slaves del master:
        Integer iddevslave = null;
        RecordSet rs = null;
        Record r = null;
        
        String sql = "select distinct iddevslave, cfdevice.address, cftableext.description as devdesc from ac_slave, cftableext, cfdevice where ac_slave.iddevmaster="+this.getIddev()+" and " +
            " cftableext.tablename='cfdevice' and cftableext.tableid=ac_slave.iddevslave and cftableext.languagecode='"+lang+"' and cftableext.idsite="+idsite+" and cfdevice.iddevice=ac_slave.iddevslave " +
            " order by cfdevice.address,description,iddevslave";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            // per ogni salve:
            for (int i = 0; i < rs.size(); i++)
            {
                r = rs.get(i);
                iddevslave = (Integer)r.get("iddevslave");
                HashMap<Integer,Integer> slaveVars = AcSlave.getSlaveVars(iddevslave);
                
                tabella.append("<tr class='Row1'>\n");
                tabella.append("<td height='20px' class='standardTxt' align='center'>S("+r.get("address")+")</td>\n");
                tabella.append("<td height='20px' class='standardTxt' align='center'><a onclick='go2DtlDev("+iddevslave.intValue()+");return false;' title='"+click2godtldev+"' href='javascript:void(0)'><img src='images/system/right.png' border='0' complete='complete'/></a></td>\n");
                tabella.append("<td height='20px' class='standardTxt' align='center'>"+this.getLed(iddevslave)+"</td>\n");
                if(dtlviewEnabled)
                	tabella.append("<td height='20px' class='standardTxt'><a href=\"javascript:void(0)\" style=\"font-weight:normal\" onclick=\"top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+r.get("iddevslave")+"&desc=ncode01')\">" + r.get("devdesc") + "</a></td>\n");
                else
                	tabella.append("<td height='20px' class='standardTxt'>"+r.get("devdesc")+"</td>\n");
                
                // per ogni var dello slave:
                for (int j = 0; j < n_cols; j++)
                {
                    tabella.append("<td height='20px' align='center' class='standardTxt'>");
                    var_origin = "";
                    
                    if (slaveVars.get(new Integer(j+1)) != null)
                    {
                        try
                        {
                            var_origin = ControllerMgr.getInstance().getFromField(slaveVars.get(new Integer(j+1)).intValue()).getFormattedValue();
                            tabella.append(var_origin);
                        }
                        catch (Exception e)
                        {
                            // PVPro-generated catch block:
                            Logger logger = LoggerMgr.getLogger(this.getClass());
                            logger.error(e);
                            
                            tabella.append("<div style='color:RED'> err </div>");
                        }
                    }
                    else
                    {
                        tabella.append("---");
                    }
                    
                    tabella.append("</td>\n");
                }
                
                //gestione 2 var extra x slave:
                extraVars = AcMdl.getExtraVarsIds(iddevslave);
                
                for (int k = 0; k < 2; k++)
                {
                    var_origin = "";
                    tabella.append("<td height='20px' align='center' class='standardTxt'>");
                    
                    if (extraVars[k] == -1)
                    {
                        tabella.append(" --- ");
                    }
                    else
                    {
                        try
                        {
                            var_origin = ControllerMgr.getInstance().getFromField(extraVars[k]).getFormattedValue();
                            tabella.append(var_origin);
                        }
                        catch (Exception e)
                        {
                            // generated catch block:
                            Logger logger = LoggerMgr.getLogger(this.getClass());
                            logger.error(e);
                            
                            tabella.append("<div style='color:RED'> err </div>");
                        }
                    }
                    
                    tabella.append("</td>\n");
                }
                
                tabella.append("</tr>\n");
            }
        }
        
        tabella.append("</table>\n");
        tabella.append("</div>\n");
        
        return tabella.toString();
    }
    
	public String getMasterDtlTable2(UserSession us)
    {
        String lang = us.getLanguage();
        LangService l = LangMgr.getInstance().getLangService(lang);
        String alarmdesc = l.getString("ac","alarmdesc");
        String click2goback = l.getString("ac","click2goback");
        
        int idsite = us.getIdSite();
        
        DeviceBean devmstrbean = null;
        
        try
        {
            devmstrbean = DeviceListBean.retrieveSingleDeviceById(1, this.getIddev(), lang);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        String running = "";
        String ok_run = l.getString("ac","ok_run");
        String no_run = l.getString("ac","no_run");

        if (this.getEnabled().intValue() == 1)
        {
            running = ok_run;
        }
        else
        {
            running = no_run;
        }
        
        StringBuffer tabella = new StringBuffer("");
        
        tabella.append("<div style='overflow-X:auto;overflow-Y:auto;height:590px' onclick='goBackToAC();return false;'>\n");
        tabella.append("<table style='cursor:pointer' title='"+click2goback+"' class='table' border='0' width='50%' cellpadding='1' cellspacing='1'>\n");
        tabella.append("<tr class='th'>\n");
        tabella.append("<td height='20px'>"+getLed(this.getIddev())+"</td>\n");
        
        //tabella.append("<td height='20px' align='center' class='standardTxt'><b>"+devmstrbean.getDescription()+"</b>&nbsp;("+running+")</td>\n");
        tabella.append("<td height='20px' align='center' class='standardTxt'><b>"+AcMdl.getGroupName(devmstrbean.getIddevice())+"</b>&nbsp;("+running+")</td>\n");
        
        tabella.append("<td height='20px' align='center' class='standardTxt'><b>"+alarmdesc+"</b></td>\n");
        tabella.append("</tr>\n");
        
        String sql_1 = "select ac_slave.iddevslave, " +
            " var1.description as devdesc, " +
            " ac_slave.idvaralarm, " +
            " var2.description as adesc " +
            " from ac_slave, cftableext as var1, cftableext as var2 " +
            " where ac_slave.iddevmaster="+this.getIddev()+" and " +
            " var1.idsite="+idsite+" and var1.tablename='cfdevice' and var1.tableid=ac_slave.iddevslave and var1.languagecode='"+lang+"' and " +
            " var2.idsite="+idsite+" and var2.tablename='cfvariable' and var2.tableid=ac_slave.idvaralarm and var2.languagecode='"+lang+"' ";

        String sql_2 = "select ac_slave.iddevslave, " +
            " var1.description as devdesc, " +
            " ac_slave.idvaralarm, " +
            " '***' as adesc " +
            " from ac_slave, cftableext as var1 " +
            " where ac_slave.idvaralarm=-1 and ac_slave.iddevmaster="+this.getIddev()+" and " +
            " var1.idsite="+idsite+" and var1.tablename='cfdevice' and var1.tableid=ac_slave.iddevslave and var1.languagecode='"+lang+"' " +
            " order by devdesc, adesc";
        
        String sql = sql_1 + " UNION " + sql_2;
        
        RecordSet rs = null;
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        int prev_iddevslave = -1;
        int actual_iddevslave = -1;
        
        if ((rs != null) && (rs.size() > 0))
        {
            for (int i=0; i < rs.size(); i++)
            {
                //var di allarme GROUP BY iddevslave:
                actual_iddevslave = ((Integer)rs.get(i).get("iddevslave")).intValue();
                if (actual_iddevslave != prev_iddevslave)
                {
                    if (prev_iddevslave > 0)
                    {
                        tabella.append("</table>\n");
                        tabella.append("</tr>\n");
                    }
                    tabella.append("<tr class='Row1'>\n");
                    tabella.append("<td height='20px' class='standardTxt'>"+this.getLed(actual_iddevslave)+"</td>\n");
                    tabella.append("<td height='20px' class='standardTxt'>"+rs.get(i).get("devdesc")+"</td>\n");
                    tabella.append("<td>\n");
                    //subtable delle var di allarme associate a questo slave:
                    tabella.append("<table border='0' cellpadding='1' cellspacing='1'>\n");
                    tabella.append("<tr class='Row1'>\n");
                    prev_iddevslave = actual_iddevslave;
                }
                else
                {
                    tabella.append("</tr>\n");
                    tabella.append("<tr class='Row1'>\n");
                }
                
                if (rs.get(i).get("adesc").equals("***"))
                {
                    tabella.append("<td height='20px' class='standardTxt'><div style='text-align:center'>&nbsp;"+rs.get(i).get("adesc")+"</div></td>\n");
                }
                else
                {
                    tabella.append("<td height='20px' class='standardTxt'>"+rs.get(i).get("adesc")+"</td>\n");
                }
                
                tabella.append("</tr>\n");
            }
        }
        
        //chiudo tabella lista di allarmi x ultimo slave
        tabella.append("</table>\n");
        
        tabella.append("</tr>\n");
        tabella.append("</table>\n");
        
        tabella.append("</div>\n");
        
        return tabella.toString();
    }
    
    private String getLed(Integer idDev)
    {
        StringBuffer buffer = new StringBuffer();
        String imgDecod = UtilDevice.getLedColor(idDev);

        buffer.append("<div id='DLed" + idDev + "' style='");
        buffer.append("background-image:url(images/led/L" + imgDecod + ".gif);background-repeat:no-repeat;background-position:center'>\n");
        buffer.append("<div style='visibility:hidden;'>" + imgDecod + "</div>\n");
        buffer.append("</div>");

        return buffer.toString();
    }
    
}
