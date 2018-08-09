package com.carel.supervisor.script;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class EnumerationMgr 
{
	private static final EnumerationMgr me = new EnumerationMgr();
	private static Map<String,String> enumeration = null;
	
	private EnumerationMgr()
	{
	}
	
	public static EnumerationMgr getInstance()
    {
		return me;
    }
	
	public void init()
	{
		enumeration = new HashMap<String,String>();
		loadEnumeration();
	}
	
	public String getEnumCode(int idvarmdl,float value,String lang)
	{
		String desc = "";
		try
		{
			desc = (String)enumeration.get(buildKey(idvarmdl,value,lang));
			if (desc == null)
			{
				desc = "";
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			LoggerMgr.getLogger(this.getClass()).error(e);
			LoggerMgr.getLogger(this.getClass()).error("---DEBUG PARAM ---  varmdl:" + idvarmdl + "value:" + value+"lang:"  + lang);
		}
	
		return desc;
	
	}
	
	private static void loadEnumeration()
	{
		String sql = 
		"select cfvarmdl.idvarmdl as var,cfenum.index as idx,cftableext.description as desc,languagecode from cfenum,cfvarmdl,cftableext "+
		"where cfenum.idvarmdl=cfvarmdl.idvarmdl and cftableext.tablename=? and cftableext.tableid=cfenum.idenum";
		
		RecordSet rs = null;
		Record r = null;
		
		int key = -1;
		int idx = -1;
		String lang = null;
		String  des = null;
		
		try 
		{
			Object[] params = {"cfenum"};
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
			if(rs != null)
			{
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						key = ((Integer)r.get("var")).intValue();
						idx = ((Integer)r.get("idx")).intValue();
						lang = UtilBean.trim(r.get("languagecode"));
						des = UtilBean.trim(r.get("desc"));
						enumeration.put(buildKey(key,idx,lang),des);
					}
				}
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(EnumerationMgr.class);
			logger.error(e);
		}
	}
	
	private static String buildKey(int a,float b,String lang_code) {
		return a+"_"+b+"_"+lang_code;
	}
	
	public boolean isVarInEnum(int idvarmdl, String value, String lang)
	{
		boolean isIn = false;
		String val = "";
		float valore = 0;
		
		if ((value != null) && (!"".equals(value)) && (idvarmdl > 0))
		{
			try
			{
				valore = (new Float(value)).floatValue();
				val = getEnumCode(idvarmdl, valore,lang);
				isIn = (!"".equals(val));
			}
			catch (Exception e)
			{
				isIn = false;
			}
		}
		
		return isIn;
	}
}
