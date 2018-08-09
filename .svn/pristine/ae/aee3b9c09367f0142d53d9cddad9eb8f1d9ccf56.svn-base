package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;

public class VarlogBeanList 
{
	public int[] retriveLogicVariable(int idvar)
    {
    	String sql = "select functioncode,type from cfvariable where idvariable=? and idsite=1 and islogic=? and iscancelled=?";
    	Object[] param = {new Integer(idvar),"TRUE","FALSE"};
    	Integer tmp = null;
    	int[] ret = new int[2];
    	
    	try
    	{
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
    		Record r = null;
    		if(rs != null && rs.size() > 0)
    			r = rs.get(0);
    		
    		if(r != null)
    		{
    			tmp = ((Integer)r.get("functioncode"));
    			if(tmp != null)
    				ret[0] = tmp.intValue();
    			
    			tmp = ((Integer)r.get("type"));
    			if(tmp != null)
    				ret[1] = tmp.intValue();
    		}
    	}
    	catch(Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
    	}
    	
    	return ret;
    }
    
    public boolean deleteLogicVariable(int idvar)
    {
    	boolean ret = false;
    	String sql = "update cfvariable set iscancelled=? where idvariable=? and islogic=? and idsite=1";
    	Object[] param = {"TRUE",new Integer(idvar),"TRUE"};
    	try
    	{
    		DatabaseMgr.getInstance().executeStatement(null, sql, param);
    		ret = true;
    	}
    	catch(Exception e)
    	{
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
    	}
    	
    	return ret;
    }
    
    public int insertNewLogicVarCondition(int functionCode,int frequency,int type)
    {
    	String sql = "insert into cfvariable (idvariable,pvcode,idsite,iddevice,islogic,functioncode,code,type,"+
    				 "todisplay,frequency,delta,delay,isonchange,ishaccp,isactive,iscancelled,grpcategory,idhsvariable,"+
    				 "inserttime,lastupdate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	int ret = 0;
    	
    	try
    	{
    		Timestamp time = new Timestamp(System.currentTimeMillis());
    		Integer key = SeqMgr.getInstance().next(null,"cfvariable","idvariable");
    		Object[] param = {key,BaseConfig.getPlantId(),new Integer(1),new Integer(0),"TRUE",new Integer(functionCode),
    						  "LOGIC_CONDITION",new Integer(type),"FALSE",new Integer(frequency),new Integer(0),
    						  new Integer(0),"FALSE","FALSE","TRUE","FALSE",new Integer(0),new Integer(-1),time,time};
    		DatabaseMgr.getInstance().executeStatement(null, sql, param);
    		ret = key.intValue();
    	}
    	catch(Exception e) {
    		ret = 0;
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
    	}
    	return ret;
    }
    
    public boolean insertLogicVarInBuffer(int idLVar)
    {
    	String sql = "insert into buffer values(?,?,?,?,?)";
    	Object[] param = {new Integer(1),new Integer(idLVar),new Integer(100),new Integer(-1),new Boolean(false)};
    	boolean ris = true;
    	try {
    		DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	}
    	catch(Exception e) {
    		ris = false;
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
    	}
    	return ris;
    }
    
    public boolean updateVarlogFrequency(int idVar,int frequency,int type)
    {
    	boolean ret = false;
    	
    	String sql = "update cfvariable set frequency=?,type=? where idvariable=? and islogic=? and idsite=1";
    	Object[] param = {new Integer(frequency),new Integer(type),new Integer(idVar),"TRUE"};
    	try {
    		DatabaseMgr.getInstance().executeStatement(null,sql,param);
    		ret = true;
    	}
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
    	}
    	
    	return ret;
    }
}
