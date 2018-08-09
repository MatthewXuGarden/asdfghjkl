package com.carel.supervisor.plugin.fs;

import java.util.*;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class FSRack
{
    private static final String ID = "idrack";   // Primary Key
    private static final String IDDEVICE = "iddevice";   //iddevice
    private static final String SETPOINT = "setpoint";   //id setpoint variable
    private static final String MINSET = "minset";		//id min variable
    private static final String MAXSET = "maxset";		//id max variable
    private static final String GRADIENT = "gradient";	//id gradient variable
    private static final String TIMEWINDOW = "timewindow";
    private static final String WAITTIME = "waittime";
    private static final String MAX_OFFLINE_TIME = "maxofftime";
    private static final String MAX_OFFLINE_UTIL = "maxoffutil";
    private static final String AUX = "aux";
    private static final String DESCRIPTION = "description";
    private static final String NEW_ALG = "new_alg";

    //db object 
    private Integer id_rack = null;
    private Integer id_device = null;
    private Float id_setpoint = null;
    private Float id_minset = null;
    private Float id_maxset = null;
    private Float id_gradient = null;
    private Integer timewindow = null;
    private Integer waittime = null;
    private Integer maxofftime = null;
    private Integer maxoffutil = null;
    private String aux = null;
    private String description = null;
    
    // new algorithm
    private boolean new_alg = false;
    //private int[] anSb = null;
    //Float fSetpoint0 = null;
    Float[] afSetpoint = null;

    //cache object
    private Boolean firstSet = false;
    private Boolean ready = false;
    private Integer clock = new Integer(0);
    private FSUtil[] utils = null;
    private Date dtSamplingTime = null;
    private Object lockSamplingTime = new Object();
    private boolean solenoidNullError = false;

        
    //create rack object when engine start, with linked utils 
    public FSRack(Record r,String lang) throws DataBaseException
    {
        this.id_rack = (Integer) r.get(ID);
        this.id_device = (Integer) r.get(IDDEVICE);
        this.id_setpoint = (Float) r.get(SETPOINT);
        this.id_minset = (Float) r.get(MINSET);
        this.id_maxset = (Float) r.get(MAXSET);
        this.id_gradient = (Float) r.get(GRADIENT);
        this.timewindow = (Integer) r.get(TIMEWINDOW);
        this.waittime = (Integer) r.get(WAITTIME);
        this.maxofftime = (Integer) r.get(MAX_OFFLINE_TIME);
        this.maxoffutil = (Integer) r.get(MAX_OFFLINE_UTIL);
        this.aux =  r.get(AUX).toString();
        this.clock = new Integer(0);
        this.description = r.get(DESCRIPTION).toString();
        this.utils = FSUtil.getUtilsOfRack(this.id_rack,timewindow,lang);
        this.new_alg = Boolean.parseBoolean(r.get(NEW_ALG).toString());
    }

    //list of racks, used by FS engine 
    public static FSRack[] getRacks(String def_lang) throws DataBaseException
    {
        String sql = "select fsrack.*,cftableext.description from fsrack,cftableext where idrack <> ? and idrack in (select distinct idrack from fsutil) and cftableext.idsite=? and cftableext.tablename=? and cftableext.languagecode=? and cftableext.tableid=fsrack.iddevice";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]{new Integer(-1),new Integer(1),"cfdevice",def_lang});

        if (rs.size() > 0)
        {
            FSRack[] racks = new FSRack[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                racks[i] = new FSRack(rs.get(i),def_lang);
            }

            return racks;
        }
        else
        {
            return null;
        }
    }
    
    //number of utils linked to the rack
    public int numberOfUtils()
    {
    	if (this.utils.length>0)
    	{
    		return this.utils.length;
    	}
    	else return 0;
    }
    
    
    // GETTERS & SETTERS
    public Integer getIdDevice()
    {
    	return id_device;
    }
    
    public Integer getClock()
    {
        return clock;
    }

    public void setClock(Integer clock)
    {
        this.clock = clock;
    }

    public Float getId_gradient()
    {
        return id_gradient;
    }

    public void setId_gradient(Float gradient)
    {
        this.id_gradient = gradient;
    }

    public Integer getId_rack()
    {
        return id_rack;
    }

    public void setId_rack(Integer idrack)
    {
        this.id_rack = idrack;
    }
    
    public void setId_device(Integer iddevice)
    {
        this.id_device = iddevice;
    }

    public Integer getMaxofftime()
    {
        return maxofftime;
    }

    public void setMaxofftime(Integer maxofftime)
    {
        this.maxofftime = maxofftime;
    }

    public Integer getMaxoffutil()
    {
        return maxoffutil;
    }

    public void setMaxoffutil(Integer maxoffutil)
    {
        this.maxoffutil = maxoffutil;
    }

    public Float getId_maxset()
    {
        return id_maxset;
    }

    public void setId_maxset(Float maxset)
    {
        this.id_maxset = maxset;
    }

    public Float getId_minset()
    {
        return id_minset;
    }

    public void setMinset(Float minset)
    {
        this.id_minset = minset;
    }

    public Integer getTimewindow()
    {
        return timewindow;
    }

    public void setTimewindow(Integer timewindow)
    {
        this.timewindow = timewindow;
    }

    public FSUtil[] getUtils()
    {
        return utils;
    }

    public void setUtils(FSUtil[] utils)
    {
        this.utils = utils;
    }

    public Integer getWaittime()
    {
        return waittime;
    }

    public void setWaittime(Integer waittime)
    {
        this.waittime = waittime;
    }

	public Float getId_setpoint() {
		return id_setpoint;
	}

	public void setId_setpoint(Float id_setpoint) {
		this.id_setpoint = id_setpoint;
	}

	public void setId_minset(Float id_minset) {
		this.id_minset = id_minset;
	}

	public String getAux() {
		return aux;
	}

	public void setAux(String aux) {
		this.aux = aux;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getReady() {
		return ready;
	}

	public void setReady(Boolean ready) {
		this.ready = ready;
	}

	public Boolean getFirstSet() {
		return firstSet;
	}

	public void setFirstSet(Boolean firstSet) {
		this.firstSet = firstSet;
	}
	
	// new algorithm
	public boolean isNewAlg()
	{
		return this.new_alg;
	}
	
//	public void initSb(int n)
//	{
//		anSb = new int[n];
//	}
//	
//	public void setSb(int val)
//	{
//		for(int i = anSb.length - 1; i > 0; i--)
//			anSb[i] = anSb[i-1];
//		anSb[0] = val;
//	}
//	
//	public int getSbTotal()
//	{
//		int val = 0;
//		for(int i = 0; i < anSb.length; i++)
//			val += anSb[i];
//		return val;
//	}
//	
//	public void resetSb()
//	{
//		if( anSb != null )
//			for(int i = 0; i < anSb.length; i++)
//				anSb[i] = 0;
//		clock = FSManager.getT();
//	}
	
	
	// record setpoint changes
	public void changeSetpoint(Float setpoint)
	{
		if( afSetpoint == null )
			afSetpoint = new Float[FSManager.SB_STATUS];
		else
			for(int i = afSetpoint.length - 1; i > 0; i--)
				afSetpoint[i] = afSetpoint[i-1];
		afSetpoint[0] = setpoint;
	}

	
//	public void shiftSetpoint()
//	{
//		afSetpoint[0] = fSetpoint0;
//		if( afSetpoint == null )
//			afSetpoint = new Float[FSManager.getSb()];
//		else
//			for(int i = afSetpoint.length - 1; i > 0; i--)
//				afSetpoint[i] = afSetpoint[i-1];
//	}
	
	
	public Float getSetpoint(int i)
	{
		return afSetpoint != null ? afSetpoint[i] : null;
	}
	
	
	public void setSamplingTime()
	{
		synchronized(lockSamplingTime) {
			dtSamplingTime = new Date();
		}
	}
	
	
    public Date getSamplingTime()
    {
    	synchronized(lockSamplingTime) {
    		return dtSamplingTime;
    	}
    }

	public boolean isSolenoidNullError() {
		return solenoidNullError;
	}

	public void setSolenoidNullError(boolean solenoidNullError) {
		this.solenoidNullError = solenoidNullError;
	}
    
}
