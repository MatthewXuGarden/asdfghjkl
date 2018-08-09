package com.carel.supervisor.plugin.parameters.dataaccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;


public class ParametersCFG implements IDumpable {
	private static final int DEF_PLIMIT=10;
	
	private static final String ENABLED= "enabled";
	private static final String CHECKINTERVAL= "checkinterval";
	private static final String PLIMIT= "plimit";
	private static final String USERTASTIERA= "usertastiera";
	private static final String ENABLENOTIFICATION= "enablenotification";
	private static final String AGGREGATENOTIFICATION= "aggregatenotification";
	
	
	private boolean enabled;
	private long checkinterval;
	private int plimit;
	private String userTastiera;
	private boolean enablenotification;
	private boolean aggregatenotification;
	
	int[] profiles = null;
	
	public ParametersCFG() throws DataBaseException{
		
		String sql="select * from parameters_cfmaincfg ";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql);
        if (recordSet.size()>0){
        	Record r = recordSet.get(0);
        	
        	Integer en = (Integer) r.get(ENABLED);
        	if (en.intValue()>0)
        		enabled=true;
        	else enabled=false;
        	
        	checkinterval = ( (Integer) r.get(CHECKINTERVAL)).intValue();
        	plimit = ( (Integer) r.get(PLIMIT)).intValue();
        	setUserTastiera(((String) r.get(USERTASTIERA)).trim());
        	enablenotification = ((Integer) r.get(ENABLENOTIFICATION)).intValue() > 0 ? true : false;
        	
        	aggregatenotification = ((Integer) r.get(AGGREGATENOTIFICATION)).intValue() > 0 ? true : false;
        }
        else
        {
        	String sqlA="select value from productinfo where key='plimit';";
            RecordSet recordSetA;
    		try {
    			recordSetA = DatabaseMgr.getInstance().executeQuery(null, sqlA);
    	        if (recordSetA.size()>0){
    	        	Record r = recordSetA.get(0);
    	        	plimit = new Integer(((String) r.get("value"))).intValue();
    	        	}
    	        else plimit=DEF_PLIMIT;
    		} catch (DataBaseException e) {
    			plimit=DEF_PLIMIT;
    		}
            
        	enabled=false;
        	checkinterval=ParametersMgr.defaultCheckIntervall;
        	enablenotification=false;
        	aggregatenotification=true;        	
        }
        sql= "select idprofile from parameters_authorized_profile ";
        recordSet = DatabaseMgr.getInstance().executeQuery(null, sql);
        int[] prof = new int[recordSet.size()];
        for (int i = 0; i < recordSet.size() ; i++) {
        	Record r = recordSet.get(i);
        	prof[i]=( (Integer) r.get("idprofile") ).intValue();
		}
        setProfiles(prof);
       
	}
	public ParametersCFG(boolean enabled, int checkinterval,String usertastiera, boolean enablenotification, boolean aggregatenotification){
		
    	String sqlA="select value from productinfo where key='plimit';";
        RecordSet recordSetA;
		try {
			recordSetA = DatabaseMgr.getInstance().executeQuery(null, sqlA);
	        if (recordSetA.size()>0){
	        	Record r = recordSetA.get(0);
	        	plimit = new Integer ((String) r.get("value") ).intValue();
	        	}
	        else plimit=DEF_PLIMIT;
		} catch (DataBaseException e) {
			plimit=DEF_PLIMIT;
		}
		
		this.enabled=enabled;
		this.checkinterval=checkinterval;   
		this.enablenotification=enablenotification;
		this.userTastiera=usertastiera;
		this.aggregatenotification=aggregatenotification;
	}
	
	public void save() throws DataBaseException{
		String sql1="delete from parameters_cfmaincfg; " ;
		String sql2 =
				"insert into parameters_cfmaincfg values(?,?,?,?,?,?);";
		
		DatabaseMgr.getInstance().executeStatement(null, sql1,  null);


		Object[] a2 = new Object[6];
		
		a2[0]=( this.enabled ? 1 : 0) ;
		a2[1]=( this.checkinterval) ;
		a2[2]=( this.plimit) ;
		a2[3]=this.userTastiera;
		a2[4]=this.enablenotification ? 1 : 0;
		a2[5]=this.aggregatenotification ? 1 : 0;

		DatabaseMgr.getInstance().executeStatement(null, sql2,  a2);
	}
	
	public boolean isProfileAuthorized(int idprofile){
		int[] prof = getProfiles();
		int t = Arrays.binarySearch(prof,idprofile);
		return t>-1 ? true : false;
	}
	
	public int[] getAuthProfiles(){
		int[] oldr = getProfiles();
		int[] r = new int[oldr.length];
		for (int i = 0; i < r.length; i++) {
			r[i]= new Integer(oldr[i]).intValue();
		}		
		return r;
	}
	
	public void setAuthProfiles(int[] prof){
		String sqlDelete = "delete from parameters_authorized_profile;";
		String sqlInsert = "insert into parameters_authorized_profile values (?);";
		
		try {
			DatabaseMgr.getInstance().executeStatement(null, sqlDelete, null);
			
			Object[] o = new Object[1];
			for (int i = 0; i < prof.length; i++) {
				o[0]=new Integer(prof[i]);
				DatabaseMgr.getInstance().executeStatement(null, sqlInsert, o);
			}
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		setProfiles(prof);
	}
	
	public List<ListBoxElement> getAllProfileListBox(){
		List<ListBoxElement> lbe= new ArrayList<ListBoxElement>();
		
		String sql = "select profilelist.idprofile as idprofile, profilelist.code as code from profilelist ";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for (int i = 0; i < rs.size(); i++) {
				String d = (String) rs.get(i).get("code");
				String v = ((Integer) rs.get(i).get("idprofile")).toString();
				ListBoxElement l  = new ListBoxElement(d,v);
				lbe.add(l);
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return lbe;
	}
	
	public String getAllProfileListBoxHTML(){
		String r = "";
		List<ListBoxElement> l = getAllProfileListBox();
		
		for (int i = 0; i < l.size(); i++) {
			ListBoxElement lbe = l.get(i);
			
			r+="<option value=\""+lbe.getValue()+"\" class='"+(i%2==0?"Row1":"Row2")+"'>"+lbe.getDescription()+"</option>";
		}
		
		return r;
	}
	
	public String getNotAuthProfileListBoxHTML(){
		String r = "";
		List<ListBoxElement> all = getAllProfileListBox();
		List<ListBoxElement> auth = getAuthProfileListBox();
		int zebraCounter=0;
		for (int i = 0; i < all.size(); i++) {
			ListBoxElement lbe = all.get(i);
			boolean isAuth=false;
			for (int j = 0; j < auth.size(); j++) {
				ListBoxElement lba = auth.get(j);
				if (lba.getValue().trim().equalsIgnoreCase(lbe.getValue().trim())){
					isAuth=true;
				}
			}
			if (!isAuth)
			{
				r+="<option value=\""+lbe.getValue()+"\" class = '"+(zebraCounter%2==0?"Row1":"Row2")+"'>"+lbe.getDescription()+"</option>";
				zebraCounter++;
			}
		}
		
		return r;
	}
	
	public String getAuthProfileListBoxHTML(){
		String r = "";
		List<ListBoxElement> l = getAuthProfileListBox();
		
		for (int i = 0; i < l.size(); i++) {
			ListBoxElement lbe = l.get(i);
			
			r+="<option value=\""+lbe.getValue()+"\" class='"+(i%2==0?"Row1":"Row2")+"'>"+lbe.getDescription()+"</option>";
		}
		
		return r;
	}
	
	public List<ListBoxElement>  getAuthProfileListBox(){
		List<ListBoxElement> lbe= new ArrayList<ListBoxElement>();
		
		String sql = "select profilelist.idprofile as idprofile, profilelist.code as code " +
				     "from profilelist inner join parameters_authorized_profile " +
				          "     on parameters_authorized_profile.idprofile=profilelist.idprofile";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

			for (int i = 0; i < rs.size(); i++) {
				String d = (String) rs.get(i).get("code");
				String v = ((Integer) rs.get(i).get("idprofile")).toString();
				ListBoxElement l  = new ListBoxElement(d,v);
				lbe.add(l);
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return lbe;
	}
	
	public DumpWriter getDumpWriter() {
		DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ENABLED, enabled);
        dumpWriter.print(CHECKINTERVAL, checkinterval);
        dumpWriter.print(PLIMIT, plimit);
        dumpWriter.print(USERTASTIERA,userTastiera);
        dumpWriter.print(ENABLENOTIFICATION, enablenotification);
        dumpWriter.print(AGGREGATENOTIFICATION, aggregatenotification);
		return dumpWriter;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public long getCheckinterval() {
		return checkinterval;
	}
	public void setCheckinterval(long checkinterval) {
		this.checkinterval = checkinterval;
	}
	public int getPlimit() {
		return plimit;
	}
	public void setPlimit(int plimit) {
		this.plimit = plimit;
	}
	public void setUserTastiera(String userTastiera) {
		this.userTastiera = userTastiera;
	}
	public String getUserTastiera() {
		return userTastiera;
	}
	public boolean getEnablenotification() {
		return enablenotification;
	}
	public void setEnablenotification(boolean enablenotification) {
		this.enablenotification = enablenotification;
	}
	
	private synchronized int[] getProfiles() {
		return profiles;
	}
	private synchronized void setProfiles(int[] profiles) {
		Arrays.sort(profiles);
		this.profiles = profiles;
	}
	public boolean getAggregatenotification() {
		return aggregatenotification;
	}
	public void setAggregatenotification(boolean aggregatenotification) {
		this.aggregatenotification = aggregatenotification;
	}
	
	

}
