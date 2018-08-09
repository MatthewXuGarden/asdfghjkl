package com.carel.supervisor.field.dataconn.impl.modbus;

import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ModbusSettingsMap {

	private static HashMap<Integer, ModbusSettings> extensionmap;

	public static ModbusSettings get(int idvarmdl) {
		if (extensionmap != null) {
			return extensionmap.get(idvarmdl);
		} else {
			return null;
		}
	}
	
	public static void loadExtension() {
		extensionmap = new HashMap<Integer, ModbusSettings>();
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select distinct cfmodbussettings.* from cfmodbussettings, cfvariable where cfmodbussettings.idvarmdl = cfvariable.idvarmdl");
			if(rs!=null){
				for(int i=0; i<rs.size();i++){
					Record r = rs.get(i);
					extensionmap.put((Integer)r.get(0), new ModbusSettings((Float)r.get(1),(Float)r.get(2),(Integer) r.get(3)));
				}
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(ModbusSettingsMap.class).error(e);
			e.printStackTrace();
		}
	}
}
