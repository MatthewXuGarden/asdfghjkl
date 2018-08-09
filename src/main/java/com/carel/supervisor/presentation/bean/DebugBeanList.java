package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DebugBeanList 
{
	private static DebugBeanList me = null;
	private Map memory = null;
	
	private DebugBeanList()
	{
		memory = new HashMap();
	}
	public static DebugBeanList getInstance()
	{
		if(me == null)
		{
			me = new DebugBeanList();
			me.reload();
		}
		return me;
	}
	
	public void reload()
	{
		try {
			this.memory.clear();
			String sql = "select * from debug_variable order by devmdlcode";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			DebugBean[] params = build(rs);
			String temp = "";
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					memory.put( params[i].getDevmdlcode(),  params[i]);
				}
			}
		} catch (DataBaseException ex) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(ex);
		}
	}
	public DebugBean getDebugBean(String devmdlcode)
	{
		return (DebugBean)this.memory.get(devmdlcode);
	}
	public List getVarmdlcodesByides(int[] ids)
	{
		DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
		List result = new ArrayList();
		for(int i=0;i<ids.length;i++)
		{
			DeviceInfo device = deviceInfoList.getByIdDevice(ids[i]);
			DebugBean varBean = (DebugBean)this.memory.get(device.getDescription());
			
			//define the variable sequence in String[]
			if(varBean!=null){
				String[] params = new String[26];
				params[0] = String.valueOf(ids[i]);
				params[1]=varBean.getStcode();
				params[2]=varBean.getTregcode();
				params[3]=varBean.getToffcode();
				params[4]=varBean.getToncode();
				params[5]=varBean.getTdefcode();
				params[6]=varBean.getTsatcode();
				params[7]=varBean.getTaspcode();
				params[8]=varBean.getShcode();
				params[9]=varBean.getShsetcode();
				params[10]=varBean.getValvcode();
				params[11]=varBean.getReqcode();
				params[12]=varBean.getDefrcode();
				params[13]=varBean.getFacode();
				params[14]=varBean.getFbcode();
				params[15]=varBean.getFccode();
				params[16]=varBean.getP1code();
				params[17]=varBean.getCopcode();
				params[18]=varBean.getCoolingcode();
				params[19]=varBean.getConsumptioncode();
				params[20]=varBean.getTh2o_incode();
				params[21]=varBean.getTh2o_outcode();
				params[22]=varBean.getH2o_diffcode();
				params[23]=varBean.getComp_speedcode();
				params[24]=varBean.getLiq_injcode();
				params[25]=varBean.getEnvelopecode();
				result.add(params);
			}
		}
		return result;
	}
	private DebugBean[] build(RecordSet rs)
	{
		DebugBean[] temp = null;
		try
        {
			if(rs != null && rs.size()>0)
			{
				temp = new DebugBean[rs.size()];
				for(int i=0;i<rs.size();i++)
				{
					temp[i] = new DebugBean(rs.get(i));
				}
			}
			return temp;
        }
		catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
		return temp;
	}
}
