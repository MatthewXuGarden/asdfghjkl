package com.carel.supervisor.presentation.bean;

import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ModbusExtensionModelBeanList {

	public static HashMap<Integer,ModbusExtensionModelBean> getModbusExtension(){
		HashMap<Integer, ModbusExtensionModelBean> ret = new HashMap<Integer, ModbusExtensionModelBean>();
		ret.put(0, new ModbusExtensionModelBean());
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from cfmodbusmodels;");
			if(rs!=null){
				for(int i=0; i<rs.size();i++){
					ret.put((Integer)rs.get(i).get("iddevmdl"), new ModbusExtensionModelBean(rs.get(i)));
				}
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(ModbusExtensionModelBeanList.class).error(e);
		}
		return ret;
	}
}
