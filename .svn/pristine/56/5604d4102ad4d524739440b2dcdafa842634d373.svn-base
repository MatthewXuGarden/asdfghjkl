package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;

public class FunctionBeanList 
{
	public FunctionBeanList()
	{
		
	}
	
	public int insertFunction(String pvcode,int idsite,String operation,String firstVal,String secondVal,boolean isVar)
	{
		int ret = 0;
		String sql = "insert into cffunction values(?,?,?,?,?,?,?,?)";
		Integer key = null;
		String paramField = "";
		
		try 
		{
			paramField = createParamField(firstVal,isVar,secondVal);
			key = SeqMgr.getInstance().next(null,"cffunction","idfunction");
			Object[] param = {key,pvcode,new Integer(idsite),key,operation,paramField,new Integer(1),new Timestamp(System.currentTimeMillis())};
			DatabaseMgr.getInstance().executeStatement(null,sql,param);
            
			DataHs dataHs= CreateSqlHs.getInsertData("cffunction",param);										
         	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
			
         	ret = key.intValue();
		}
		catch(Exception e)
		{
			ret = 0;
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ret;
	}
	
	public FunctionBean getFunctionByCode(int code)
	{
		FunctionBean funBean = null;
		
		String sql = "select * from cffunction where functioncode=?";
		Object[] param = {new Integer(code)};
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			Record  r = null;
			if(rs != null && rs.size() > 0)
				r = rs.get(0);
			
			if(r != null)
				funBean = new FunctionBean(r);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return funBean;
	}
		
	public boolean deleteFunctionByCode(int code)
	{
		boolean ret = false;
		
        
		
		
		String sql = "delete from cffunction where functioncode=?";
		try
		{
			DataHs dataHs= CreateSqlHs.getDeleteData("cffunction",
	        		new String[]{"idfunction","pvcode","idsite","functioncode","opertype","parameters","operorder"},
	        		new Object[]{new Integer(code)},new String[]{"="},new String[]{"functioncode"});
	        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
	        
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(code)});
			ret = true;
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ret;
	}
	
	public boolean updateFunctionByCode(int idcode,String operator,String val1,String val2,boolean isvar)
	{
		boolean ret = false;
		String sql = "update cffunction set opertype=?,parameters=? where functioncode=?";
		Object[] param = {operator,createParamField(val1,isvar,val2),new Integer(idcode)};
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,param);
			
			DataHs dataHs= CreateSqlHs.getUpdateData("cffunction",
	        		new String[]{"idfunction","pvcode","idsite","functioncode","opertype","parameters","operorder"},
	        		new Object[]{new Integer(idcode)},new String[]{"="},new String[]{"functioncode"});
	        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
			
	        ret = true;
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ret;
	}
	
	public boolean deleteFunction(int id)
	{
		boolean ret = false;
		
		String sql = "delete from cffunction where idfunction=?";
		try
		{
			DataHs dataHs= CreateSqlHs.getDeleteData("cffunction",
	        		new String[]{"idfunction","pvcode","idsite","functioncode","opertype","parameters","operorder"},
	        		new Object[]{new Integer(id)},new String[]{"="},new String[]{"idfunction"});
	        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
	        
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(id)});
			ret = true;
		}
		catch(Exception e)
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ret;
	}
	
	private String createParamField(String firstVal,boolean isvar,String secondVal)
	{
		String ret = "pk";
		if(firstVal.endsWith("D"))
			firstVal = firstVal.substring(0,firstVal.length()-1);
		if(secondVal.endsWith("D"))
			secondVal = secondVal.substring(0,secondVal.length()-1);
		ret+=firstVal+";";
		if(isvar)
			ret+="pk";
		ret+=secondVal;
		return ret;
	}
}

