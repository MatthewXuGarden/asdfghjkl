package com.carel.supervisor.plugin.parameters.dataaccess;

import java.util.Map;
import java.util.TreeMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ParametersConfigDeviceList {
	
	 private Map<Integer, String> configDevices;
	
	public ParametersConfigDeviceList(String l){
		configDevices = new TreeMap<Integer, String>();
		
		// sql query modified to exclude 'Internal IO' device
		// Nicola Compagno 30032010
		String sql =
				"select iddevice, a_desc.description as description " +
				"from cfdevice  " +
				"inner join cftableext a_desc " +
					"on a_desc.tableid = cfdevice.iddevice " +
					"and a_desc.tablename='cfdevice' " +
					"and a_desc.languagecode='"+l+"' " +
					"and a_desc.idsite = 1 " +
					"and cfdevice.code != '-1.000'" +
					"order by a_desc.description asc";
		
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			for (int i = 0; i < rs.size(); i++) {
				int id = ((Integer) rs.get(i).get("iddevice")).intValue();
				String desc = (String) rs.get(i).get("description");
				
				configDevices.put(id, desc);
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	public Map<Integer, String> getDevicesMap(){
		return configDevices;
	}
}
