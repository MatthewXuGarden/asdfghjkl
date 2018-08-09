package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DeviceCanUtil {

	public static LineInfo[] getLines(String dbId, String plantId)
			throws DataBaseException {

		String sql = "select * from cfline where typeprotocol = ? order by code";
		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
				new Object[] { "CAN" });
		Record record = null;
		LineInfo[] lineInfo = new LineInfo[recordSet.size()];

		for (int i = 0; i < recordSet.size(); i++) {
			record = recordSet.get(i);
			lineInfo[i] = new LineInfo(record);
		}
		return lineInfo;
	}

	public static DeviceInfo[] getCanDevicesOfLine(String dbId, String plantId,
			Integer idline) throws DataBaseException 
	{
		DeviceInfo[] devlist= null;
		
		String sql = "select cfdevice.* from cfdevice inner join cfline on cfline.idline=cfdevice.idline " +
		"where cfdevice.pvcode = ? and cfdevice.iscancelled = ? and " +
		"cfdevice.islogic = ? and cfline.idline=? and cfline.typeprotocol='CAN' order by cfdevice.globalindex";
		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
        new Object[] { plantId, "FALSE", "FALSE", idline});
		Record record = null;
		
		devlist = new DeviceInfo[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            devlist[i] = new DeviceInfo(record);
        }
		
		
		return devlist;
		
	}
	
	public static int getCanLinesNumber() throws DataBaseException
	{
		String sql = "select count(1) from cfline where typeprotocol = ? ";
		RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { "CAN" });
		int count = 0;
		if (recordSet.size()!=0)
		{
			count = ((Integer) recordSet.get(0).get(0)).intValue();
		}
		return count;
	}

}
