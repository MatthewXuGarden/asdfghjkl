package com.carel.supervisor.dispatcher.tech.clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.Dictionary;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.controller.setfield.TCCallBack;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.ClockBean;

public class TechClock 
{
    
	protected ClockBean clockBean = null;
	protected Dictionary  mapperContainer  = null;
	protected Map  mapper  = null;
	protected String lang = "EN_en";
	
	private HashMap devError = null;
	private static final int nRetryOnError = 3; 
	public boolean bSetOnField = false;
	
	
	public TechClock(ClockBean clockBean,String lang) 
	{
		this.clockBean = clockBean;
		this.lang = lang;
		this.mapperContainer = new Dictionary();
		this.devError = new HashMap();
	}
	
	private String[] getValidVariable()
	{
		List list = new ArrayList();
		for(String str: clockBean.getVarCode())
		{
			if(str != null && str.length()>0)
				list.add(str);
		}
		return (String[])list.toArray(new String[list.size()]);
	}
	public boolean load()
	{
		boolean ris = false;
		int idx = 0;
		String token = "";
		String sql = "";
		sql = "select cfdevice.iddevice as device,cfvariable.code as varcode,cfvariable.idvariable as variable "+
			"from cfdevice "+
			"inner join cfdevmdl on cfdevice.iddevmdl=cfdevmdl.iddevmdl "+
			"inner join cfvariable on cfvariable.iddevice = cfdevice.iddevice "+
			"where cfdevmdl.code=? and cfdevice.iscancelled=? "+
			"and cfvariable.idhsvariable is not null and cfvariable.code in (";
		
		String[] variables = getValidVariable();
		Object[] param = new Object[(2+variables.length)];
		
		param[idx++] = clockBean.getDevCode();
		param[idx++] = "FALSE";
		
		for(int i=0; i<variables.length; i++)
		{
			param[idx++] = variables[i];
			token += "?,";
		}
		
		token = token.substring(0,token.length()-1);
		sql = sql + token + ")";
		
		RecordSet rs = null;
		Record r = null;
		
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			if(rs != null && rs.size() > 0)
			{
				ris = true;
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						Integer idDevice = ((Integer)r.get("device")).intValue();
						this.mapperContainer.add(idDevice, r.get("varcode").toString(),((Integer)r.get("variable")));
					}
				}
			}
		}
		catch(Exception e) {
			ris = false;
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ris;
	}
	
	public void setOnField(int[] idVariable, String[] values)
    {
    	SetContext setContext = new SetContext();
    	String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		setContext.setLanguagecode(lang);
    	for(int i = 0; i < idVariable.length; i++) {
    		SetWrp sw = setContext.addVariable(idVariable[i], Float.parseFloat(values[i]));
    		sw.setCheckChangeValue(true);
    	}
    	setContext.setCallback(new TCCallBack(this, SetClock.clockMaster()));
		setContext.setUser("Dispatcher");
        SetDequeuerMgr.getInstance().add(setContext);
        bSetOnField = true;
    }
	
	public void set()
	{
		String[] varCode = clockBean.getVarCode();
		
    	Calendar cal = new GregorianCalendar();
        int year = cal.get(1);
        year %= 100;
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        int daywe = cal.get(7) - 1;
        if(daywe <= 0)
            daywe = 1;
        int hour = cal.get(11);
        int minute = cal.get(12);
        
        List idList = new ArrayList();
        List valueList = new ArrayList();
        
        for(int i=0;i<varCode.length;i++)
        {
        	String str = varCode[i];
        	if(str == null || str.length() == 0)
        		continue;
        	Integer id = (Integer)this.mapper.get(str);
        	if(id == null)
        		continue;
        	if(i == ClockBean.MASTER)
        	{
        		try{
	        		Variable variable = ControllerMgr.getInstance().retrieve(id.intValue());
					try {
						float fValue = ControllerMgr.getInstance().getFromField(id.intValue()).getCurrentValue();
						//Master variable type: digital. when it's value==1 means it is master else it is slave(mpxprostep2)
						//Master variable type: un-digital. when it's value=0 means it is master else if value>0 it is slave
						if((variable.getInfo().getType() == VariableInfo.TYPE_DIGITAL && fValue != 1) ||
						   (variable.getInfo().getType() != VariableInfo.TYPE_DIGITAL && fValue>0))
								return; // Only Master has clock
					}
					catch(Exception e){
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
						return;
					}
        		}
        		catch(Exception ex)
        		{
        			return;
        		}
        	}
        	else
        	{
				if(id != null) {
					if(id.intValue() == -1)
						return;
					idList.add(id.intValue());
					switch(i)
					{
						case ClockBean.YEAR:
							valueList.add(year);
							break;
						case ClockBean.MONTH:
							valueList.add(month);
							break;
						case ClockBean.DAY:
							valueList.add(day);
							break;
						case ClockBean.WEEKDAY:
							valueList.add(daywe);
							break;
						case ClockBean.HOUR:
							valueList.add(hour);
							break;
						case ClockBean.MINUTE:
							valueList.add(minute);
							break;
					}
					
				}
        	}
        }
        if(idList.size()>0)
        {
        	int[] ids = new int[idList.size()];
        	String[] values = new String[idList.size()];
        	for(int i=0;i<idList.size();i++)
        	{
        		ids[i] = ((Integer)idList.get(i)).intValue();
        		values[i] = String.valueOf(valueList.get(i));
        	}
        	setOnField(ids,values);
        }
    }
	
	public Iterator iterator()
	{
		return mapperContainer.iterator();
	}
	
	public void next(Integer idDevice)
	{
		mapper = mapperContainer.get(idDevice);
	}
	
	public void onSetError(int idDevice)
	{
		if( devError.containsKey(idDevice) ) {
			int nRetry = (Integer)devError.get(idDevice);
			if( --nRetry <= 0 )
				devError.remove(idDevice);
			else
				devError.put(idDevice, nRetry);
		}
		else {
			devError.put(idDevice, nRetryOnError);
		}
	}
	
	public boolean isError()
	{
		return !devError.isEmpty();
	}
	
	public Iterator iteratorError()
	{
		return devError.keySet().iterator();
	}
	
}
