package com.carel.supervisor.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ExpressionMgr 
{
	private int idDevice = -1;
	private int idDevMdl = -1;
	
	private Map expressionList = new HashMap();
	private Map vars = new HashMap();
	
	public ExpressionMgr(int idDevice, int idDevMdl)
	{
		this.idDevice = idDevice;
		this.idDevMdl = idDevMdl;
		loadRules();
		remapIdVarMdl();
		setIdVariables();
	}
	
	public boolean evaluate(int idVariable)
	{
		Expression e = (Expression)expressionList.get(new Integer(idVariable));
		if(e != null)
			return e.evaluate();
		else
			return true; 
	}
	
	public Map getVariablesList() {
		return this.vars;
	}
	
	private void loadRules()
	{
		String sql = "select cffield.*,cfvariable.idvariable from cffield,cfvariable where cfvariable.idsite=1 and " +
					 " cffield.iddevmdl=? and cfvariable.iddevice=? and cffield.idvarmdl=cfvariable.idvarmdl and " +
					 " cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null";
		
		RecordSet rs = null;
		Record r = null;
		Integer key = null;
		String  exp = null;
		
		Expression tmpExp = null;
		
		try 
		{
			Object[] params = {new Integer(this.idDevMdl),new Integer(this.idDevice)};
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
			if(rs != null)
			{
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						key = (Integer)r.get("idvariable");
						exp = UtilBean.trim(r.get("expression"));
						
						if(key != null && exp != null)
						{
							tmpExp = new Expression(exp);
							vars.putAll(tmpExp.getIdVarmdl());
							this.expressionList.put(key,tmpExp);
						}
					}
				}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void remapIdVarMdl()
	{
		String sql = "select idvariable,idvarmdl from cfvariable where iddevice=? and idvarmdl in (";
		
		RecordSet rs = null;
		Record r = null;
		int idx = 0;
		
		Integer key = null;
		Integer val = null;
		
		if(this.vars.size() > 0)
		{
			Object[] param = new Object[this.vars.size()+1];
			param[idx++] = new Integer(this.idDevice);
			Iterator it = this.vars.keySet().iterator();
			while(it.hasNext())
			{
				param[idx++] = it.next(); 
				sql += "?,";
			}
			sql = sql.substring(0,sql.length()-1);
			sql += ") and iscancelled='FALSE'  and idhsvariable is not null";
			
			try {
				rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			}
			catch(Exception e) {}
			
			if(rs != null)
			{
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						key = (Integer)r.get("idvarmdl");
						val = (Integer)r.get("idvariable");
						this.vars.put(key,val);
					}
				}
			}
		}
	}
	
	private void setIdVariables()
	{
		Iterator i = this.expressionList.keySet().iterator();
		Integer key = null;
		Expression tmp = null;
		
		while(i.hasNext())
		{
			key = (Integer)i.next();
			tmp = (Expression)this.expressionList.get(key);
			tmp.retrieveIdVariable(this.vars);
		}
	}
}
