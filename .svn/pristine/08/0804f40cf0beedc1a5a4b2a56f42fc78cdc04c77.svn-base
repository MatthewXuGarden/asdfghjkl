package com.carel.supervisor.plugin.algorithmpro.field;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.plugin.algorithmpro.alias.AliasMgr;

public class FieldMgr 
{
	private static String LAN = "EN_en";
	
	public static Float getFromField(String deviceAlias,String variableAlias)
		throws Exception
	{
		Float ret = Float.NaN;
		
		Integer idDevice = AliasMgr.getInstance().getIdDevice(deviceAlias);
		String varCode = AliasMgr.getInstance().getVariableCode(deviceAlias, variableAlias);
		VarphyBean varphyBean = VarphyBeanList.retrieveVarByCode(1, varCode, idDevice.intValue(), LAN);
		
		Variable v = ControllerMgr.getInstance().getFromField(varphyBean);
		ret = v.getCurrentValue();
		
		return ret;
	}
	
	public static void setOnField(String deviceAlias,String variableAlias,float value,String userName)
		throws Exception
	{
		//Integer idDevice = AliasMgr.getInstance().getIdDevice(deviceAlias);
		Integer idVariable = AliasMgr.getInstance().decodVariable(deviceAlias, variableAlias);
		
		//String varCode = AliasMgr.getInstance().getVariableCode(deviceAlias, variableAlias);
		//VarphyBean varphyBean = VarphyBeanList.retrieveVarByCode(1, varCode, idDevice.intValue(), LAN);
		
		//DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
		//varphyBean.setDevInfo(deviceInfoList.getByIdDevice(varphyBean.getDevice()));
		
		//Variable v = new Variable(varphyBean);
		
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
		setContext.setUser(userName);
		setContext.setCallback(new FieldAlgoProCallBack());
		
		SetWrp wrp = setContext.addVariable(idVariable, value);
		wrp.setCheckChangeValue(false);
		
		SetDequeuerMgr.getInstance().add(setContext,PriorityMgr.getInstance().getPriority(FieldMgr.class.getName()));
	}
}
