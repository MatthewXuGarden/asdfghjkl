package com.carel.supervisor.plugin.fs;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class FSUtil {
	private Integer idutil = null;
	private Integer idsolenoid = null;
	private Integer idrack = null;
	private Integer maxDC = null;
	private Integer actualDC = null;
	private Integer maxDcRecorded = -1;
	private Integer minDCRecorded = 101;
	private String code = null;
	private String description = null;
	private Boolean online = true;
	private Boolean reverse_logic = false;
	//private Boolean D0 = null;
	private int abDStatus[] = null;
	private Integer idtsh = null;
	private boolean solenoidNull = false;
	
	private FSCyclicList<Boolean> status = null;
	
	private static final String IDUTIL = "idutil";
	private static final String SOLENOID = "solenoid";
	private static final String RACK = "idrack";
	private static final String DC = "maxdc";
	private static final String CODE = "code";
	private static final String DESCRIPTION = "description";
	private static final String REV_LOGIC = "var4";
	private static final String TSH = "idtsh";
	
	
	private FSUtil(Integer idutil,Integer idsolenoid, Integer idrack, Integer maxDC, Integer window_size,String code,String description, Boolean reverse_logic)
	{
		this.idutil = idutil;
		this.idsolenoid = idsolenoid;
		this.idrack = idrack;
		this.maxDC = maxDC;
		this.code = code;
		this.description = description;
		this.reverse_logic = reverse_logic;
		status = new FSCyclicList<Boolean>(window_size);
		online=true;
	}
	
	public static FSUtil[] getUtilsOfRack(Integer idRack, Integer window_size, String lang) throws DataBaseException
	{
		String sql = "select fsutil.*,cfdevice.code,cftableext.description,fsdevmdl.var4 from fsutil,cfdevice,cftableext,cfdevmdl,fsdevmdl where fsutil.idrack=? and cfdevice.iddevice=fsutil.idutil and cftableext.idsite=? and cftableext.tablename=? and cftableext.tableid=fsutil.idutil and cftableext.languagecode=? and cfdevice.iddevmdl=cfdevmdl.iddevmdl and fsdevmdl.devcode=cfdevmdl.code order by cfdevice.idline,cfdevice.address";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{idRack,new Integer(1),"cfdevice",lang});
		
		if (rs.size()>0)
		{
			FSUtil[] utils = new FSUtil[rs.size()];
			
			Record r = null;
			boolean reverse_logic;
			
			for (int i = 0;i<rs.size();i++)
			{
				r = rs.get(i);
				reverse_logic = false;
	        	if (r.get(REV_LOGIC)!=null && r.get(REV_LOGIC).toString().equalsIgnoreCase("TRUE"))
	        	{
	        		reverse_logic = true;
	        	}
				utils[i] = new FSUtil((Integer) r.get(IDUTIL),(Integer) r.get(SOLENOID),(Integer) r.get(RACK),(Integer) r.get(DC),window_size,r.get(CODE).toString(),r.get(DESCRIPTION).toString(),reverse_logic);
				utils[i].idtsh = (Integer)r.get(TSH);
			}
			
			return utils;
		}
		else
		{
			return null;
		}
	}

	public Integer getIdutil() {
		return idutil;
	}

	public Integer getIdrack() {
		return idrack;
	}

	public Integer getMaxDC() {
		return maxDC;
	}
	
	public Integer getIdTSH()
	{
		return idtsh;
	}
	
	public Integer getMaxDcRecorded() {
		return maxDcRecorded;
	}

	public void setMaxDcRecorded(Integer maxDcRecorded) {
		this.maxDcRecorded = maxDcRecorded;
	}

	public Integer getMinDCRecorded() {
		return minDCRecorded;
	}

	public void setMinDCRecorded(Integer minDCRecorded) {
		this.minDCRecorded = minDCRecorded;
	}

	public FSCyclicList<Boolean> getStatus() {
		return status;
	}

	public void setStatus(FSCyclicList<Boolean> status) {
		this.status = status;
	}

	public Integer getActualDC() {
		return actualDC;
	}

	public void setActualDC(Integer actualDC) {
		this.actualDC = actualDC;
	}

	public Integer getIdsolenoid() {
		return idsolenoid;
	}

	public void setIdsolenoid(Integer idsolenoid) {
		this.idsolenoid = idsolenoid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Boolean getReverse_logic() {
		return reverse_logic;
	}

	public void setReverse_logic(Boolean reverse_logic) {
		this.reverse_logic = reverse_logic;
	}
	
	public void setD(Boolean b)
	{
		//step 1: for D buffer
		if( abDStatus == null )
		{
			abDStatus = new int[FSManager.SB_STATUS];
			for(int i=1;i<FSManager.SB_STATUS;i++)
				abDStatus[i] = FSManager.STATUS_NULL;
			abDStatus[0] = calculateStatus(b);
		}
		else
		{
			for(int i = abDStatus.length - 1; i > 0; i--)
				abDStatus[i] = abDStatus[i-1];
			abDStatus[0] = calculateStatus(b,abDStatus[1]);
		}
	}
	
	public int getLatestLedStatus()
	{
		if(abDStatus == null)
			return FSManager.STATUS_NULL;
		else
			return abDStatus[0];
	}
	private int calculateStatus(Boolean d)
	{
		return calculateStatus(d, -1);
	}
	private int calculateStatus(Boolean d, int lastStatus)
	{
		if(solenoidNull)
			return FSManager.STATUS_SOLENOIDNULL;
		if(d == null)
		{
			//no lastStatus
			if(lastStatus == -1)
				return FSManager.STATUS_NULL;
			//have last status
			else if(lastStatus == FSManager.STATUS_NULL ||lastStatus == FSManager.STATUS_SOLENOIDNULL || lastStatus == FSManager.STATUS_GREEN)
				return FSManager.STATUS_YELLOW_OFFLINE;
			else if(lastStatus == FSManager.STATUS_YELLOW || lastStatus == FSManager.STATUS_YELLOW_OFFLINE)
				return FSManager.STATUS_ORANGE_OFFLINE;
			else
				return FSManager.STATUS_RED_OFFLINE;
		}
		//GREEN, return immediately
		if(!d)
			return FSManager.STATUS_GREEN;
		else
		{
			//no lastStatus
			if(lastStatus == -1)
				return FSManager.STATUS_YELLOW;
			//have last status
			else if(lastStatus == FSManager.STATUS_NULL ||lastStatus == FSManager.STATUS_SOLENOIDNULL || lastStatus == FSManager.STATUS_GREEN)
				return FSManager.STATUS_YELLOW;
			else if(lastStatus == FSManager.STATUS_YELLOW || lastStatus == FSManager.STATUS_YELLOW_OFFLINE)
				return FSManager.STATUS_ORANGE;
			else
				return FSManager.STATUS_RED;
		}
	}
	public int[] getDStatus()
	{
		if(abDStatus == null)
		{
			abDStatus = new int[FSManager.SB_STATUS];
			for(int i=0;i<FSManager.SB_STATUS;i++)
				abDStatus[i] = FSManager.STATUS_NULL;
		}
		return abDStatus;
	}

	
	public void resetBuff()
	{
		abDStatus = null;
	}

	public boolean isSolenoidNull() {
		return solenoidNull;
	}

	public void setSolenoidNull(boolean solenoidNull) {
		this.solenoidNull = solenoidNull;
	}
	
}
