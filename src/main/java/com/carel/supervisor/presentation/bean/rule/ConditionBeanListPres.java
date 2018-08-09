package com.carel.supervisor.presentation.bean.rule;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBean;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.GroupBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class ConditionBeanListPres extends ConditionBeanList
{
	private int idsite = 0;
	private String language = "";
	private int screenw = 1024;
	private int screenh = 768;
	
	public ConditionBeanListPres(int idsite,String lang)
	{
		super(idsite,lang);
		this.idsite = idsite;
		this.language = lang;
	}
	
	public String getTableCondition(LangService multiLanguage,boolean permict)
	{
		HTMLTable condTable = null;
		
		String[] hCol = {multiLanguage.getString("alrsched","ide"),multiLanguage.getString("alrsched","desc"),multiLanguage.getString("alrsched","tipo")};
		condTable = new HTMLTable("TbCondition",hCol,getConditionTableData(),false,false);
		condTable.setScreenH(screenh);
		condTable.setScreenW(screenw);
		condTable.setHeight(125);
		condTable.setWidth(888);
        
		condTable.setColumnSize(0, 50);
		condTable.setColumnSize(1, 385);
		condTable.setColumnSize(2, 385);
		condTable.setAlignType(new int[]{1,0,0});
		//condTable.setTableTitle(multiLanguage.getString("alrsched","conds"));
		condTable.setTableId(1);
		
		ConditionBean[] cb = getConditionList();
		String[] condIds = new String[cb.length];
		for(int i=0; i<condIds.length; i++)
			condIds[i] = String.valueOf(cb[i].getIdCondition());
		String s=condTable.getHTMLText();
		if (permict)
		{
			condTable.setSgClickRowAction("selectCondition($1);");
			condTable.setDbClickRowAction("CondgetForUpd($1)");
		}
		
		condTable.setSnglClickRowFunction(condIds);
		condTable.setDlbClickRowFunction(condIds);
		
		return condTable.getHTMLText();
	}
	public String getAlarmVariable(UserSession us,LangService multiLanguage)
		throws Exception
	{
		String hcondid = us.getCurrentUserTransaction().remProperty("hcondid");
		HTMLElement[][] data = null;
		String[] varIds = null;
		if(hcondid != null && hcondid.length()>0)
		{
			ConditionBean cb = loadCondition(hcondid);
			String[][] datas = cb.getData();
			varIds = new String[datas.length];
			data = new HTMLElement[datas.length][2];
			for(int i=0;i<datas.length;i++)
			{
				data[i][0] = new HTMLSimpleElement(datas[i][2]+" -&gt; "+datas[i][1]);
				data[i][1] = new HTMLSimpleElement("<img src='images/actions/removesmall_on_black.png' style='cursor:pointer;' onclick='deleteAlarm("+i+")'>");
				varIds[i] = datas[i][0];
			}
		}
		HTMLTable condTable = null;
		String[] hCol = {multiLanguage.getString("alrsched","var"),multiLanguage.getString("button","del")
						};
		condTable = new HTMLTable("TbCondition",hCol,data,false,false);
		condTable.setScreenH(screenh);
		condTable.setScreenW(screenw);
		condTable.setHeight(160);
		condTable.setWidth(850);
        
		condTable.setColumnSize(0, 700);
		condTable.setColumnSize(1, 100);
		condTable.setAlignType(new int[]{0,1});
		
		condTable.setDlbClickRowFunction(varIds);
		condTable.setTableId(2);
		
		return condTable.getHTMLText();
	}
	public String getTableConditionGen(LangService multiLanguage, boolean permict)
	{
		HTMLTable condTable = null;
		
		String[] hCol = {multiLanguage.getString("alrsched","ide"),multiLanguage.getString("alrsched","desc"),multiLanguage.getString("alrsched","tipo")};
		condTable = new HTMLTable("TbConditionGeb",hCol,getConditionTableData(),false,false);
		condTable.setScreenH(screenh);
		condTable.setScreenW(screenw);
		condTable.setHeight(100);
		condTable.setWidth(900);
        
		
		condTable.setColumnSize(0, 42);
		condTable.setColumnSize(1, 493);
		condTable.setColumnSize(2, 310);
		//condTable.setTableTitle(multiLanguage.getString("alrsched","conds"));
		condTable.setTableId(1);
		condTable.setAlignType(new int[]{1,0,0});
		ConditionBean[] cb = getConditionList();
		String[] condIds = new String[cb.length];
		for(int i=0; i<condIds.length; i++)
			condIds[i] = String.valueOf(cb[i].getIdCondition());
		
		if (permict)
		{
			condTable.setSgClickRowAction("selectConditionGen($1);");
			condTable.setSnglClickRowFunction(condIds);
			condTable.setDbClickRowAction("CondgetForUpdGen($1)");
			condTable.setDlbClickRowFunction(condIds);
		}
		return condTable.getHTMLText();
	}
	
	public String getTypeSelect(String type,String value)
	{
		String[][] data = null;
		String selected = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<option value='0'>--------------------</option>");
		int iType = 1;
		try {
			iType = Integer.parseInt(type);
		}
		catch(Exception e) {}
		
		switch(iType)
		{
			case 1:
				break;
			case 2:
				data = getAllGroupsList(this.idsite,this.language);
				break;
			case 3:
				data = getAllDeviceList(this.idsite,this.language);
				break;
			case 4:
				break;
		}
		
		if(data != null)
		{
			for(int i=0; i<data.length; i++)
			{
				if(data[i][0].equalsIgnoreCase(value))
					selected = "selected";
				else
					selected = "";
				sb.append("<option value='"+data[i][0]+"' "+selected+">"+data[i][1]+"</option>");
			}
		}
		return sb.toString();
	}
	
	public String getVarList(String type,String value) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		String[][] data = null;
		
		int iType = 1;
		try {
			iType = Integer.parseInt(type);
		}
		catch(Exception e) {}
		
		switch(iType)
		{
			case 1:
				break;
			case 2:
				break;
			case 3:
				data = getAlarmVarDevice(this.idsite,this.language,value);
				break;
			case 4:
				break;
		}
		
		if(data != null)
		{
			for(int i=0; i<data.length; i++)
				sb.append("<option value='"+data[i][0]+"'>"+data[i][1]+"</option>");
		}
		
		return sb.toString();
	}
	
	
	private HTMLElement[][] getConditionTableData()
    {
		HTMLElement[][] data = null;
		ConditionBean[] cb = getConditionList();
		
		if(cb != null)
		{
			data = new HTMLElement[cb.length][4];
			for(int i=0; i<data.length; i++)
			{
				if (cb[i].getIdCondition()<0)
				{
					data[i][0] = new HTMLSimpleElement("<img src='images/ok.gif' >");
				}
				else
				{
					data[i][0] = new HTMLSimpleElement(""); 
				}
				data[i][1] = new HTMLSimpleElement(cb[i].getCodeCondition()); 
				data[i][2] = new HTMLSimpleElement(decodeConditionType(cb[i].getTypeCondition()));
				//Start Fixing 2007/06/27
                data[i][3] = new HTMLSimpleElement(""+cb[i].getPriorityCondition());
                //End
			}
		}
		
		return data;
    }
	
	private String decodeConditionType(String s)
	{
		String ret = "";
		if(s != null)
		{
			if(s.equalsIgnoreCase("V"))
				ret = "Variable";
			else if(s.equalsIgnoreCase("G"))
				ret = "Group";
			else if(s.equalsIgnoreCase("S"))
				ret = "System";
			else if(s.equalsIgnoreCase("K"))
				ret = "Costante";
			else if(s.equalsIgnoreCase("R"))
				ret = "Remote";
			//Start alarmP Fixing 2007/06/27
            else if(s.equalsIgnoreCase("P")) 
                ret= "Variable Priority";
            //End
		}
		return ret;
	}
	
	private String[][] getAllGroupsList(int idsite,String language)
	{
		String[][] ret = null;
		
		try {
			GroupListBean groupList = new GroupListBean();
			GroupBean[] list = groupList.retrieveAllGroupsNoGlobal(idsite,language);
			ret = new String[list.length][2];
			for (int i=0; i<ret.length; i++) {
				ret[i][0] = String.valueOf(list[i].getGroupId());
				ret[i][1] = list[i].getDescription();
			}
		}
		catch(Exception e) {
			ret = new String[0][0];
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ret;
	}
	
	private String[][] getAllDeviceList(int idsite,String language)
	{
		String[][] ret = null;
		int[] iddevs = null;
		
		try {
			DeviceListBean deviceList = new DeviceListBean(this.idsite,this.language);
			ret = new String[deviceList.size()][2];
			iddevs = deviceList.getIds();
			
			for (int i=0; i<ret.length; i++) {
				ret[i][0] = String.valueOf(iddevs[i]);
				ret[i][1] = deviceList.getDevice(iddevs[i]).getDescription();
			}
		}
		catch(Exception e) {
			ret = new String[0][0];
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ret;
	}
	
	private String[][] getAlarmVarDevice(int idsite,String language,String iddevice) throws Exception
	{
		String[][] ret = null;
		int idevice = 0;
		try {
			idevice = Integer.parseInt(iddevice);
		}
		catch(Exception e) {
		}
		
		VarphyBeanList varphyList = new VarphyBeanList();
		VarphyBean[] list = varphyList.getActiveAlarmVarPhy(language,idsite,idevice);
		ret = new String[list.length][2];
		for (int i=0; i<ret.length; i++) {
			ret[i][0] = String.valueOf(list[i].getId());
			ret[i][1] = list[i].getShortDescription();
		}

		return ret;
	}
	
	public String getAllNotAlarmVars(String value,String current,boolean filter,String isDigit)
	{
		StringBuffer sb = new StringBuffer();
		boolean bDigit = (isDigit != null && isDigit.equalsIgnoreCase("T"));
		sb.append("<option value='0'>--------------------</option>");
		String[][] varsList = null;
		String selected = "";
		
		try {
			varsList = getNotAlarmVarDevice(this.idsite,this.language,value,filter,bDigit);
		}
		catch(Exception e) {
			varsList = new String[0][0];
		}
		
		for(int i=0; i<varsList.length; i++)
		{
			if(varsList[i][0].equalsIgnoreCase(current) || varsList[i][0].equalsIgnoreCase(current+"D"))
				selected = "selected";
			else
				selected = "";
			
			sb.append("<option value=\""+varsList[i][0]+"\" "+selected+">"+varsList[i][1]+"</option>");
		}
		
		return sb.toString();
	}
	
	private String[][] getNotAlarmVarDevice(int idsite,String language,String iddevice,boolean filter,boolean onlyDigit) throws Exception
	{
		String[][] ret = null;
		String digit = "";
		List listaV = new ArrayList();
		VarphyBean tmp = null;
		
		int idevice = 0;
		try {
			idevice = Integer.parseInt(iddevice);
		}
		catch(Exception e) {
		}
		
		VarphyBeanList varphyList = new VarphyBeanList();
		VarphyBean[] list = varphyList.getVarNotAlarmsForCondition(language,idsite,idevice);
		
		for(int i=0; i<list.length; i++)
		{
			if(filter) 
			{
				if(onlyDigit)
				{
					if(list[i] != null && list[i].getType() == 1)
						listaV.add(list[i]);
				}
				else
				{
					if(list[i] != null && list[i].getType() != 1)
						listaV.add(list[i]);
				}
			}
			else
				listaV.add(list[i]);
		}
		
		ret = new String[listaV.size()][2];
		for (int i=0; i<ret.length; i++) 
		{
			tmp = (VarphyBean)listaV.get(i);
			if(tmp.getType() == 1)
				digit = "D";
			else
				digit = "";
			ret[i][0] = String.valueOf(tmp.getId()+digit);
			ret[i][1] = tmp.getShortDescription();
		}

		return ret;
	}
	
	public void setScreenH(int height) {
    	this.screenh = height;
    }
    
	public void setScreenW(int width) {
    	this.screenw = width;
    }
}
