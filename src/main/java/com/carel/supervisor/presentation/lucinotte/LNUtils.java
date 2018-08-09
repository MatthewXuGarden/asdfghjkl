package com.carel.supervisor.presentation.lucinotte;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.LogicDeviceBean;
import com.carel.supervisor.presentation.bean.LogicDeviceBeanList;

public class LNUtils
{
    public static final int hpos = 10000; //posizione ore
    public static final int mpos = 100; // posizione minuti
    private static final int lasttime = 240000;
    public static final String TIMEFORMAT = "timeformat";
    
    public static int getTimeFormat()
    {
    	int timeF = 0; //default time format: 24/hours
    	
    	String sql_get = "select value from ln_clockconfig where key=?";
    	Object[] param = new Object[]{TIMEFORMAT};
    	RecordSet rs = null;
    	
    	try
    	{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql_get, param);
			
			if (rs != null)
			{
				if (rs.size() > 0)
				{
					timeF = Integer.parseInt(rs.get(0).get("value").toString());
				}
				else
				{
		            String sql_ins = "insert into ln_clockconfig values (?,?)";
		            param = new Object[]{TIMEFORMAT,new Integer(timeF)};
		            
		            try
		            {
						DatabaseMgr.getInstance().executeStatement(sql_ins, param);
					}
		            catch (DataBaseException e1)
		            {
		                // PVPro-generated catch block:
		            	Logger logger = LoggerMgr.getLogger(LNUtils.class);
		                logger.error(e1);
					}
				}
			}
    	}
    	catch (DataBaseException e1)
        {
            // PVPro-generated catch block:
    		Logger logger = LoggerMgr.getLogger(LNUtils.class);
            logger.error(e1);
		}
    	
    	return timeF;
    }
    
    public static void setTimeFormat(int valore)
    {
    	if ((valore > 1) || (valore < 0))
    	{
    		valore = 0;
    	}
    	
    	String sql_set = "update ln_clockconfig set value=? where key=?";
    	Object params[] = new Object[]{new Integer(valore),TIMEFORMAT};
    	
    	try
    	{
			DatabaseMgr.getInstance().executeStatement(sql_set, params);
		}
    	catch (DataBaseException e)
    	{
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(LNUtils.class);
            logger.error(e);
		}
    }
    
    public static String combo24Hours(Integer ora)
    {
        StringBuffer orario = new StringBuffer();
        String selected = "";
        
        int time = -1;
        
        if (ora != null)
            time = ora.intValue();
        
        //orario.append("<select id='' name=''>\n");
        
        if (time == -1)
        	selected = "selected";
        
        orario.append("<option "+selected+" value='-1'> --- </option>\n");
        
        for (int i = 0; i < 24; i++)
        {
            if (time == (i*hpos))
                selected = "selected";
            else
                selected = "";
            
            orario.append("<option "+selected+" value='"+(i*hpos)+"'>"+(i<10?"0":"")+i+".00</option>\n");
            
            if (time == (i*hpos + 30*mpos))
                selected = "selected";
            else
                selected = "";
            
            orario.append("<option "+selected+" value='"+(i*hpos + 30*mpos)+"'>"+(i<10?"0":"")+i+".30</option>\n");
        }
        
        if (time == lasttime)
            selected = "selected";
        else
            selected = "";
        
        orario.append("<option "+selected+" value='"+(lasttime)+"'>24.00</option>\n");
        
        //orario.append("</select>\n");
        
        return orario.toString();
    }
    
    public static String combo24Hours()
    {
        return combo24Hours(null);
    }
    
    public static String comboAPHours(Integer ora)
    {
    	StringBuffer orario = new StringBuffer();
        String selected = "";
        
        int time = -1;
        
        if (ora != null)
            time = ora.intValue();
        
        //orario.append("<select id='' name=''>\n");
        
        if (time == -1)
        	selected = "selected";
        
        orario.append("<option "+selected+" value='-1'> --- </option>\n");
        
        for (int i = 0; i < 12; i++)
        {
        	if (time == (i*hpos))
                selected = "selected";
            else
                selected = "";
        	
        	orario.append("<option "+selected+" value='"+(i*hpos)+"'>"+(i<10?"0":"")+i+".00a</option>\n");
            
        	if (time == (i*hpos)+30*mpos)
                selected = "selected";
            else
                selected = "";
        	
        	orario.append("<option "+selected+" value='"+(i*hpos + 30*mpos)+"'>"+(i<10?"0":"")+i+".30a</option>\n");
        }
        
        if (time == lasttime/2)
            selected = "selected";
        else
            selected = "";
        
        orario.append("<option "+selected+" value='"+lasttime/2+"'>12.00a</option>\n");
        
        if (time == (lasttime/2 + 30*mpos))
            selected = "selected";
        else
            selected = "";
        
        orario.append("<option "+selected+" value='"+(lasttime/2 + 30*mpos)+"'>00.30p</option>\n");
        
        for (int j = 1; j < 12; j++)
        {
        	if (time == (j+12)*hpos)
                selected = "selected";
            else
                selected = "";
        	
        	orario.append("<option "+selected+" value='"+((j+12)*hpos)+"'>"+(j<10?"0":"")+j+".00p</option>\n");
            
        	if (time == ((j+12)*hpos + 30*mpos))
                selected = "selected";
            else
                selected = "";
            
            orario.append("<option "+selected+" value='"+((j+12)*hpos + 30*mpos)+"'>"+(j<10?"0":"")+j+".30p</option>\n");
        }
        
        if (time == lasttime)
            selected = "selected";
        else
            selected = "";
        
        orario.append("<option "+selected+" value='"+lasttime+"'>12.00p</option>\n");
        //orario.append("</select>\n");
        
        return orario.toString();
    }
    
    public static String comboAPHours()
    {
        return comboAPHours(null);
    }
    
    public static String getComboDevice(int idsite, String language, int iddevmdl)
    {
        DevMdlBeanList l = new DevMdlBeanList();
        StringBuffer html = new StringBuffer();
        
        try
        {
            l.retrieve(idsite, language);
        }
        catch (DataBaseException e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(LNUtils.class);
            logger.error(e);
        }
        
        html.append("<select class='standardTxt' id='cmb_devmdl' name='cmb_devmdl' onchange='loadMdlVars(this);' >\n");
        
        // 1. sezione devices fisici:
        html.append("<option value='-1'> ------------------------- </option>\n");
        
        if ((l != null) && (l.size() > 0))
        {
            DevMdlBean tmp = null;
            
            for (int i = 0; i < l.size(); i++)
            {
                tmp = l.getMdlBean(i);
                html.append("<option value='"+tmp.getIddevmdl()+"'");
                
                if (iddevmdl == tmp.getIddevmdl())
                    html.append(" selected ");
                
                html.append(">"+tmp.getDescription()+"</option>\n");
            }
            
        }
        
        // 2. sezione devices logici:
        html.append("<option value='-1'> ------------------------- </option>\n");
        
        LogicDeviceBeanList ldbl = new LogicDeviceBeanList();
        
        try
        {
            ldbl.loadDeviceComplete(null, idsite, language);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(LNUtils.class);
            logger.error(e);
        }
        
        if ((ldbl != null) && (ldbl.size() > 0))
        {
            LogicDeviceBean temp = null;
            
            for (int i = 0; i < ldbl.size(); i++)
            {
                temp = ldbl.getLogicDevice(i);
                html.append("<option value='"+(-temp.getIddevice())+"'");
                
                if (iddevmdl == (-temp.getIddevice()))
                    html.append(" selected ");
                
                html.append(">"+temp.getDescription()+"</option>\n");
            }
            
        }
        
        html.append("</select>");
        
        return html.toString();
    }
}
