package com.carel.supervisor.presentation.comboset;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ComboParam 
{
	private Integer idvar = null;
	private List<OptionParam> list = null;
	
	public ComboParam(int idvar, int idcombo, String varcode, String language)
	{
		this.idvar = idvar;
		
		String sql = "select cfcomboset.value,cftableext.description "+
					"from cfcomboset "+
					"inner join cfcombo on cfcomboset.idcombo=cfcombo.idcombo and cfcomboset.idcombo=? and cfcombo.idcombogroup is null "+
					"inner join cfoption on cfcomboset.idoption=cfoption.idoption "+
					"inner join cftableext on cftableext.tablename='cfoption' and cftableext.languagecode=? and cftableext.tableid=cfoption.idoption order by cfoption.idoption";



		
		Object[] param = new Object[2];
		param[0] = idcombo;
		param[1] = language;
		RecordSet rs = null;
		
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
		} 
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		if (rs!=null && rs.size()>0)
		{
			this.list = new ArrayList<OptionParam>();
			Record r = null;
			OptionParam opt = null;
			
			for (int i=0;i<rs.size();i++)
			{
				r = rs.get(i);
				opt = new OptionParam(r.get("description").toString(),(Float)r.get("value"));
				this.list.add(opt);
			}
		}
	}

	public Integer getIdvar() {
		return idvar;
	}

	public void setIdvar(Integer idvar) {
		this.idvar = idvar;
	}
	
	public int getOptionNumber()
	{
		return this.list.size();
	}
	
	public OptionParam getOption(int i)
	{
		return this.list.get(i);
	}
	public boolean hasOption()
	{
		if(this.list == null || this.list.size() ==0)
			return false;
		else
			return true;
	}
	//"Add by AlexBs": restituisce la stringa corrispondente al valore scelto dalla combobox
	public String getDescFromValue(String value)
	{
		String desc = ""; //valore non ancora riconosciuto
		
		if ((value != null) && (!value.equals("")))
		{
			//caso di device off-line:
			if (value.equals("***"))
			{
				desc = value;
			}
			else
			{
				try
				{
					Float flValue = new Float(value);
					
					for (int k = 0; k < list.size(); k++)
					{
						if (list.get(k).getValue().floatValue() == flValue.floatValue())
						{
							desc = list.get(k).getDesc(); //valore riconosciuto
							break;
						}
					}
					
					//se non riconosco il valore fra le stringhe della combo, uso il valore stesso:
					if ("".equals(desc))
						desc = value;
				}
				catch (Exception e)
				{
					desc = "***"; //se il valore non � valido
				}
			}
		}
		
		return desc;
	}
}
