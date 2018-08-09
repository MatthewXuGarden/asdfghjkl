package com.carel.supervisor.presentation.comboset;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ComboParamMap {
	
	private Map<Integer,ComboParam> map = null;
	
	public void loadDeviceConf(int iddev, String language)
	{
		String sql = "select distinct cfcombo.idcombo,cfvariable.idvariable,cfvariable.code "+
					"from cfcombo "+
					"inner join cfcomboset on cfcombo.idcombo=cfcomboset.idcombo and cfcombo.idcombogroup is null "+
					"inner join cfdevice on cfcombo.iddevmdl=cfdevice.iddevmdl and cfdevice.iddevice=? "+
					"inner join cfvariable on cfvariable.iddevice=cfdevice.iddevice and cfvariable.idvarmdl=cfcomboset.idvarmdl "+
					"order by cfvariable.idvariable,cfcombo.idcombo ";
		
		RecordSet rs = null;
		
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{iddev});
		} 
		catch (DataBaseException e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		if (rs!=null && rs.size()>0)
		{
			this.map = new HashMap<Integer,ComboParam>();
			Record r = null;
			ComboParam combo = null;
			Integer idvar = null;
			Integer idcombo = null;
			String code = null;
			
			for (int i=0;i<rs.size();i++)
			{
				r = rs.get(i);
				idvar = (Integer) r.get("idvariable");
				idcombo = (Integer) r.get("idcombo");
				code = r.get("code").toString();
				combo = new ComboParam(idvar,idcombo,code,language);
				this.map.put(idvar,combo);
			}
		}
	}
	
	public boolean containComboForVar(Integer idvar)
	{
		if (this.map!=null && this.map.containsKey(idvar))
			return true;
		else
			return false;
	}
	
	public ComboParam getComboForVar(Integer idvar)
	{
		return this.map.get(idvar);
	}
	
	public ComboParam getComboByIdVar(int iddev, String language, Integer idVar)
	{
		String sql = "select distinct cfcombo.idcombo,cfvariable.code "+
		"from cfcombo "+
		"inner join cfcomboset on cfcombo.idcombo=cfcomboset.idcombo and cfcombo.idcombogroup is null "+
		"inner join cfdevice on cfcombo.iddevmdl=cfdevice.iddevmdl and cfdevice.iddevice=? "+
		"inner join cfvariable on cfvariable.iddevice=cfdevice.iddevice and cfvariable.idvarmdl=cfcomboset.idvarmdl and cfvariable.idvariable = ?"+
		"order by cfcombo.idcombo ";

		RecordSet rs = null;
		
		try
		{
		rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{iddev,idVar});
		} 
		catch (DataBaseException e)
		{
		LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		ComboParam combo = null;
		if (rs!=null && rs.size()>0)
		{
			Record r = rs.get(0);
			Integer idcombo = (Integer) r.get("idcombo");
			String code = r.get("code").toString();
			combo = new ComboParam(idVar,idcombo,code,language);
		}
		return combo;
	}
}
