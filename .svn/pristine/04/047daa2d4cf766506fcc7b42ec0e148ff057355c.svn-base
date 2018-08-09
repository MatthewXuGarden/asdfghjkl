package com.carel.supervisor.plugin.parameters.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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


public class ParametersEvent implements IDumpable {
	public static final String ID = "id";
	public static final String DATETIME = "datetime";
	public static final String IDSITE = "idsite";
	public static final String IDVARIABLE = "idvariable";
	public static final String EVENTTYPE = "eventtype";
	public static final String USERNAME = "username";
	public static final String TONOTIFY = "tonotify";
	public static final String STARTINGVALUE = "startingvalue";
	public static final String ENDINGVALUE = "endingvalue";
	public static final String CANROLLBACK = "canrollback";

	public static final String IDDEVICE = "iddevice";
	public static final String DEV_DESCR = "dev_descr";
	public static final String VAR_DESCR = "var_descr";

	private Integer id;
	private Timestamp datetime;
	private Integer idsite;
	private Integer idvariable;
	private String eventtype;
	private String username;
	private Boolean tonotify;
	private Boolean canrollback;
	private Float startingvalue;
	private Float endingvalue;
	private Integer iddevice;
	private String dev_descr;
	private String var_descr;
	
	
	public ParametersEvent(){
		
	}
	
	public ParametersEvent(Record record){
		id = (Integer) record.get(ID);
		datetime = (Timestamp) record.get(DATETIME);
		idsite = (Integer) record.get(IDSITE);
		idvariable = (Integer) record.get(IDVARIABLE);
		eventtype = ((String) record.get(EVENTTYPE)).trim();
		username = ((String) record.get(USERNAME)).trim();
		tonotify = ((Integer) record.get(TONOTIFY))>0 ?true : false;
		canrollback=((Integer) record.get(CANROLLBACK))>0 ?true : false;
		startingvalue = (Float) record.get(STARTINGVALUE);
		endingvalue = (Float) record.get(ENDINGVALUE);
		if(!eventtype.equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE )){
			iddevice = (Integer) record.get(IDDEVICE);
			dev_descr = ((String) record.get(DEV_DESCR)).trim();
			var_descr = ((String) record.get(VAR_DESCR)).trim();
		}
		else
		{
			iddevice=-1;
			dev_descr="";
			var_descr="";
		}
	}
	
	public ParametersEvent(ResultSet rs) throws SQLException{
		id = (Integer) rs.getInt(ID);
		datetime = (Timestamp) rs.getTimestamp(DATETIME); 
		idsite = (Integer) rs.getInt(IDSITE);
		idvariable = (Integer) rs.getInt(IDVARIABLE);
		eventtype = ((String) rs.getString(EVENTTYPE)).trim();
		username = ((String) rs.getString(USERNAME)).trim();
		tonotify = ((Integer) rs.getInt(TONOTIFY))>0 ?true : false;
		canrollback=((Integer) rs.getInt(CANROLLBACK))>0 ?true : false;
		startingvalue = (Float) rs.getFloat(STARTINGVALUE);
		endingvalue = (Float) rs.getFloat(ENDINGVALUE);
		
		if(!eventtype.equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE )){
			iddevice = (Integer) rs.getInt(IDDEVICE);
			dev_descr = ((String) rs.getString(DEV_DESCR)).trim();
			var_descr = ((String) rs.getString(VAR_DESCR)).trim();
		}
		else
		{
			iddevice=-1;
			dev_descr="";
			var_descr="";
		}
	}
	
	public ParametersEvent(String language, int id){
		String sql= "select parameters_events.*,cfdevice.iddevice, a_desc.description as dev_descr,b_desc.description as var_descr " +
		" from parameters_events " +
		" inner join cfvariable on parameters_events.idvariable = cfvariable.idvariable " +
		" inner join cfdevice on cfvariable.iddevice = cfdevice.iddevice " +
		
		" inner join cftableext a_desc on a_desc.tableid = cfdevice.iddevice "+
	    "   and a_desc.tablename='cfdevice' "+
	    "   and a_desc.languagecode='"+language+"' "+
	       
		" inner join cftableext b_desc on b_desc.tableid = cfvariable.idvariable "+
	    "   and b_desc.tablename='cfvariable' "+
	    "   and b_desc.languagecode='"+language+"' " +
	    " where id = ? "; 
		Object[] o = new Object[1];
		o[0]= new Integer(id);
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,o);
			if (rs.size()>0){
				Record record = rs.get(0);
				this.id = (Integer) record.get(ID);
				datetime = (Timestamp) record.get(DATETIME);
				idsite = (Integer) record.get(IDSITE);
				idvariable = (Integer) record.get(IDVARIABLE);
				eventtype = ((String) record.get(EVENTTYPE)).trim();
				username = ((String) record.get(USERNAME)).trim();
				tonotify = ((Integer) record.get(TONOTIFY))>0 ?true : false;
				canrollback=((Integer) record.get(CANROLLBACK))>0 ?true : false;
				startingvalue = (Float) record.get(STARTINGVALUE);
				endingvalue = (Float) record.get(ENDINGVALUE);
				if(!eventtype.equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE )){	
				iddevice = (Integer) record.get(IDDEVICE);
				dev_descr = ((String) record.get(DEV_DESCR)).trim();
				var_descr = ((String) record.get(VAR_DESCR)).trim();
				}
				else
				{
					iddevice=-1;
					dev_descr="";
					var_descr="";
				}
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
	}

	public ParametersEvent(Integer id, Timestamp datetime,	Integer idsite,	Integer idvariable, String eventtype,
			String username, Boolean tonotify,Boolean canrollback, Float startingvalue, Float endingvalue){
		
		this.id = id;
		this.datetime=datetime;
		this.idsite=idsite;
		this.idvariable=idvariable;
		this.eventtype=eventtype;
		this.username=username;
		this.tonotify=tonotify;
		this.canrollback=canrollback;
		this.startingvalue=startingvalue;
		this.endingvalue=endingvalue;
	}
	
	public void save() throws DataBaseException{
		String sql="insert into parameters_events values (?,?,?,?,?,?,?,?,?,?);";
		
		DatabaseMgr.getInstance().executeStatement(
				   sql, new Object[] {this.id, this.datetime, this.idsite, this.idvariable, this.eventtype, 
						              this.username,  this.startingvalue, this.endingvalue, 
						              this.tonotify?1:0, this.canrollback?1:0 });
	}

	public void notified(){
		if (this.tonotify)
		{
			String sql = "update parameters_events set tonotify=0 where " +
					        " id=? ";
			Object[] o = new Object[1];
			o[0]=this.id;
			
			try {
				DatabaseMgr.getInstance().executeStatement(null, sql, o);
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	public void setRollbackable(Boolean willberollbackable){
			String sql = "update parameters_events set canrollback=? where " +
					        " id=? ";
			Object[] o = new Object[2];
			o[0]=willberollbackable?1:0;
			o[1]=this.id;
			
			try {
				DatabaseMgr.getInstance().executeStatement(null, sql, o);
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
	}
	
	public DumpWriter getDumpWriter() {
		DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(DATETIME, datetime);
        dumpWriter.print(IDSITE, idsite);
        dumpWriter.print(IDVARIABLE,idvariable);
        dumpWriter.print(EVENTTYPE, eventtype);
        dumpWriter.print(USERNAME, username);
        dumpWriter.print(TONOTIFY, tonotify);
        dumpWriter.print(CANROLLBACK, canrollback);
        dumpWriter.print(STARTINGVALUE, startingvalue);
        dumpWriter.print(ENDINGVALUE, endingvalue);
		return dumpWriter;
	}

	public Timestamp getDatetime() {
		return datetime;
	}


	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}


	public Integer getIdsite() {
		return idsite;
	}


	public void setIdsite(Integer idsite) {
		this.idsite = idsite;
	}


	public Integer getIdvariable() {
		return idvariable;
	}


	public void setIdvariable(Integer idvariable) {
		this.idvariable = idvariable;
	}


	public String getEventtype() {
		return eventtype;
	}


	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Boolean getTonotify() {
		return tonotify;
	}


	public void setTonotify(Boolean tonotify) {
		this.tonotify = tonotify;
	}


	public Float getStartingvalue() {
		return startingvalue;
	}


	public void setStartingvalue(Float startingvalue) {
		this.startingvalue = startingvalue;
	}


	public Float getEndingvalue() {
		return endingvalue;
	}


	public void setEndingvalue(Float endingvalue) {
		this.endingvalue = endingvalue;
	}

	public Integer getIddevice() {
		return iddevice;
	}

	public void setIddevice(Integer iddevice) {
		this.iddevice = iddevice;
	}

	public String getDev_descr() {
		return dev_descr;
	}

	public void setDev_descr(String dev_descr) {
		this.dev_descr = dev_descr;
	}

	public String getVar_descr() {
		return var_descr;
	}

	public void setVar_descr(String var_descr) {
		this.var_descr = var_descr;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setCanrollback(Boolean canrollback) {
		this.canrollback = canrollback;
	}

	public Boolean getCanrollback() {
		return canrollback;
	}

}
