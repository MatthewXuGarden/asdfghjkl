package com.carel.supervisor.presentation.sdk.obj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.copydevice.PageImpExp;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.sdk.util.Sfera;
import com.carel.supervisor.presentation.session.UserSession;

public class CurrUnit
{
    private UserSession sessionUser = null;
    private boolean blockError = false;
    private Map varmap = null;
    private Map alrmap = null;
    private int curIdDev = -1;
    private int curIdDevMdl = -1;
    private Map<String, CurrVar> mapcurr;
    private String assint;
    private String assintdefault;
    private Integer gblIdx = null;
    private Map<String, VarphyBean> partvarmap = null;
    private Map<String, CurrVar> partmapcurr = null;
    
    public CurrUnit() 
    {
        varmap = null;
        alrmap = null;
        blockError = false;
    }
    
	public CurrUnit(int id)
	{
		this();
		this.curIdDev = id;
	}

    private void loadDeviceVariables()
    {
        if(this.varmap == null)
        {
            this.varmap = new HashMap();
            this.mapcurr = new HashMap<String, CurrVar>();
            VarphyBean[] variables = null;
            try
            {
                variables = new VarphyBeanList().getAllVarOfDevice(this.sessionUser.getLanguage(),this.sessionUser.getIdSite(),this.curIdDev);
                
                for(int i=0; i<variables.length; i++)
                {
                    varmap.put(variables[i].getCodeVar().toUpperCase(),variables[i]);
                    mapcurr.put(variables[i].getCodeVar(),
                    	new CurrVar(variables[i].getId(), variables[i].getCodeVar(), "", variables[i].getShortDescription(),variables[i].getShortDesc(),
                    					variables[i].getLongDescription(),variables[i].getMeasureUnit(),variables[i].getType(), variables[i].getReadwrite().trim(),this.curIdDev));
                }
            }
            catch(Exception e) {
                blockError = true;
            }
            variables = null;
        }
    }
    
    private boolean loadDeviceAlarms()
    {
        boolean isAlarms = false;
        this.alrmap = new HashMap();
        
        /*
        String sql = 
        "select idalarm,starttime,description,ackuser,acktime,resetuser,resettime,priority from hsalarm,cftableext "+
        "where iddevice=? and endtime is null and resetuser is null and languagecode=? "+
        "and tablename='cfvariable' and tableid=idvariable";
        */

        String sql = 
        "select "+
        "idalarm, starttime, a.description, b.description as priority, "+
        "ackuser, acktime, "+
        "resetuser, resettime, "+
        "hsalarm.priority as prio "+
        "from hsalarm, cftableext as a, cftext as b "+
        "where iddevice=? and "+
        "endtime is null and "+
        "resetuser is null and "+
        "a.idsite = ? and "+
        "a.tablename=? and "+
        "a.tableid=idvariable AND "+
        "a.languagecode=? and "+
        "b.idsite = ? AND "+
        "b.languagecode=? and "+
        "b.code=? and "+
        "b.subcode=('alarmstate'||priority)";

        try 
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,
                           new Object[]{new Integer(this.curIdDev),this.sessionUser.getIdSite(),"cfvariable",this.sessionUser.getLanguage(),this.sessionUser.getIdSite(),this.sessionUser.getLanguage(),"alrview"});
            
            for(int i=0; i<rs.size(); i++)
                this.alrmap.put(new Integer(i),new CurrAlarm(rs.get(i)));
            
            if(this.alrmap.size() > 0)
                isAlarms = true;
        }
        catch(Exception e) {
        	e.printStackTrace();
        	LoggerMgr.getLogger(this.getClass()).error(e);
        }
        return isAlarms;
    }
    
    /**
     * PVP 2.0
     * @param HttpServletRequest: HTTP request
     */
    public void setReq(HttpServletRequest req) {
		setCurrentSession(ServletHelper.retrieveSession(req.getRequestedSessionId(), req),false);
	}
    
    /**
     * PVP 2.0
     * @param us
     */
    public void setCurrentSession(UserSession us)
    {
    	setCurrentSession(us,false);
    }
    
    /**
     * PVP 2.0
     * @param us
     */
    public void setCurrentSession(UserSession us,boolean fromNode) 
    {
    	blockError = false;
        this.sessionUser = us;
        
        if(!fromNode)
        {
	        try 
	        {
	            curIdDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	            // DA TESTARE! La cache delle variabili viene svuotata ad ogni cambio di dispositivo
	            this.partvarmap = new HashMap();
	            this.partmapcurr = new HashMap();	
	            DeviceStructureList deviceStructureList = this.sessionUser.getGroup().getDeviceStructureList();
	            DeviceStructure deviceStructure = deviceStructureList.get(curIdDev);
	            if(deviceStructure != null)
	                curIdDevMdl = deviceStructure.getIdDevMdl();
	        }
	        catch(Exception e) {
	            blockError = true;
	        }
        }
    }
    
    /*
     * Sezione Dispositivo
     */
    public int getId()
    {
        if(!blockError)
            return this.curIdDev;
        else
            return -1;
    }
    
    public int getIdMdl()
    {
        if(!blockError)
            return this.curIdDevMdl;
        else
            return -1; 
    }
    
    public String getDescription()
    {
        String ret = "";
        if(!blockError)
        {
            DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
            DeviceStructure deviceStructure = deviceStructureList.get(curIdDev);
            if(deviceStructure != null)
                ret = deviceStructure.getDescription();
        }
        return ret;
    }
    
    public String getLine()
    {
        String ret = "";
        if(!blockError)
        {
            DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
            DeviceStructure deviceStructure = deviceStructureList.get(curIdDev);
            if(deviceStructure != null)
                ret = deviceStructure.getCode();
        }
        return ret;
    }
    public String getCode() throws DataBaseException
    {
    	String ret = "";
        if(!blockError)
        {
            ret = PageImpExp.getDeviceCode(1, curIdDev);
        }
        return ret;
    }
    public int getStatus() 
    {
        int ret = 0;
        if(!blockError)
            ret = Integer.parseInt(UtilDevice.getLedColor(new Integer(curIdDev)));
        return ret;
    }
    
    public String getRefreshableStatusAssint(String values) 
    {
    	return getRefreshableStatusAssint(values, "");
    }
    
    public String getRefreshableStatusAssint(String values, String def) 
    {
        String ret = "";
    	if(values!=null)
    	{
	    	assint=values;
	    	assintdefault = def!=null?def:"";
	        if(!blockError)
	            ret = "<span id='devsts_"+curIdDev+"' name='devsts_"+curIdDev+"'>"+Sfera.assint(Integer.parseInt(UtilDevice.getLedColor(new Integer(curIdDev))), assint, assintdefault)+"</span>"
	            + "<input id='imgvaldevsts' type='hidden' value=\"" + values + "\"/>";	            
    	}
        return ret;
    }
    
    public String getImage()
    {
        String ret = "";
        if(!blockError)
        {
            DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
            DeviceStructure deviceStructure = deviceStructureList.get(curIdDev);
            if(deviceStructure != null)
                ret = "images/devices/"+deviceStructure.getImageDevice();
        }
        return ret;
    }
    
    /*
     * Sezione Variabili
     */
    /*
    // original version:
    public CurrVar getVariable(String vCode)
    {
    	if(varmap==null||varmap.isEmpty())
    		loadDeviceVariables();
        
        CurrVar ret = null;
        if(!blockError)
        {
            VarphyBean tmp = (VarphyBean)this.varmap.get(vCode.toUpperCase());
            String val = "***";
            
            if(tmp != null)
            {
                try {
                    val = (ControllerMgr.getInstance().getFromField(tmp)).getFormattedValue();
                }
                catch(Exception e){
                    val = "***";    
                }
                
//                ret = new CurrVar(tmp.getId(), vCode, val,tmp.getShortDescription(),tmp.getShortDesc(),
//                                      tmp.getLongDescription(),tmp.getMeasureUnit(),tmp.getType());
                ret = mapcurr.get(vCode);
                ret.setValue(val);
            }
        }
        
        if(ret == null)
            ret = new CurrVar(0, vCode, "***","","","","",0,"");   
        
        return ret;
    }
    */
    
    // da versione di test x Carel-FR a versione ufficiale:
    public CurrVar getVariable(String vCode)
    {
        CurrVar ret = null;
        VarphyBean tmp = null;
        
        if(!blockError)
        {
        	if ((partmapcurr != null) && (partmapcurr.containsKey(vCode)) && (partvarmap != null))
        	{
        		ret = partmapcurr.get(vCode);
        		tmp = partvarmap.get(vCode);
        	}
        	// if variable is called first time
        	if (tmp == null)
	        {
        		tmp = VarphyBeanList.retrieveVarByCode(this.sessionUser.getIdSite(), vCode, this.curIdDev, this.sessionUser.getLanguage());
        	}
        	
        	String val = "***";
            
            if (tmp != null)
            {
                try
                {
                    val = (ControllerMgr.getInstance().getFromField(tmp)).getFormattedValue();
                }
                catch(Exception e)
                {
                    val = "***";    
                }
                
	       		if (ret == null)
	       			ret = new CurrVar(tmp.getId().intValue(), tmp.getCodeVar(), val, tmp.getShortDescription(), tmp.getShortDesc(), tmp.getLongDescription(), tmp.getMeasureUnit(), tmp.getType(), tmp.getReadwrite(),this.curIdDev);
	       		else
	       			ret.setValue(val);
	            
	            if (partmapcurr == null)
	            	partmapcurr = new HashMap<String, CurrVar>();
	            
	            partmapcurr.put(vCode, ret);
	            
	            if (partvarmap == null)
	            	partvarmap = new HashMap<String, VarphyBean>();
	            
	            partvarmap.put(vCode, tmp);
            }
        }
        
        if (ret == null)
            ret = new CurrVar(0, vCode, "***", "", "", "", "", 0, "",this.curIdDev);
        
        return ret;
    }
    
    /*
     * Sezione Allarmi
     */
    public boolean loadAlarms() {
        return loadDeviceAlarms();
    }
    
    public int getAlarmNumber() {
    	if (alrmap!=null)
    		return this.alrmap.size();
    	else
    		return 0;
    }
    
    public CurrAlarm getAlarmAt(int idx) 
    {
        CurrAlarm ca = null;
        
        try 
        {
            ca = (CurrAlarm)this.alrmap.get(new Integer(idx));
        }
        catch(Exception e) {
            ca = new CurrAlarm(-1,"","","","","","","");
        }
        return ca;
    }
    
	public String getAssint()
	{
		return assint;
	}
    
	public String getAssintDefault()
	{
		return assintdefault;
	}    
	
	public String getSyncTimeButton(int id_hh,int id_mm,int id_ack)
    {
    	StringBuffer sb = new StringBuffer("");
    	sb.append("<img src='images/custom/clock.png' onclick='syncroTime("+id_hh+","+id_mm+")'  onload='setClock("+id_ack+")' style='cursor:pointer' alt='Sincronizzazione orologio'/>");
    	sb.append("<div style='display:none' id='syncro'></div>");
    	return sb.toString();
    }
	
	public String getSyncTimeButton(String codeHour, String codeMinute)
    {
    	String str = "<img src='images/custom/clock.png' onclick=\"timeSync(" + getId() + ",'" + codeHour + "','" + codeMinute + "')\" style='cursor:pointer'>";
    	return str;
    }


	public String getSyncTimeButton(String codeHour, String codeMinute, String codeConfirmation)
    {
    	String str = "<img src='images/custom/clock.png' onclick=\"timeSync(" + getId() + ",'" + codeHour + "','" + codeMinute + "','" + codeConfirmation + "')\" style='cursor:pointer'>";
    	return str;
    }
	
	
	public Integer getGblIdx()
	{
		if (this.gblIdx == null)
		{
			String sql = "select globalindex from cfdevice where iddevice="+this.curIdDev+" and idsite=1";
			
			try
			{
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
				
				if ((rs.get(0).get("globalindex") != null) && (((Integer)rs.get(0).get("globalindex")).intValue() != 0))
				{
					this.gblIdx = (Integer) rs.get(0).get("globalindex");
				}
			}
			catch (Exception e)
			{
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		
		return this.gblIdx;
			
	}
}