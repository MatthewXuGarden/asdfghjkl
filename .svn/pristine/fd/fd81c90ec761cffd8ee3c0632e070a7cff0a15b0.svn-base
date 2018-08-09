package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;

public class BookletCabinetDeviceList {
	
	public static void save(BookletCabinetBean bean,int[] iddevices) throws DataBaseException
    {
		//insert
        for(int deviceId:iddevices)
        {
        	Object[] values = new Object[3];
			SeqMgr o = SeqMgr.getInstance();
			int id = o.next(null, "booklet_cabinet_dev", "id");
			values[0] = id;
			values[1] = bean.getId();
			values[2] = deviceId;
			String exeSql = "insert into booklet_cabinet_dev(id,idcabinet,iddevice) values(?,?,?) ";
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);
        }
    }
	public static int getDeviceNumber(int cabinetId) throws DataBaseException
	{
		String sql = "select count(*) from booklet_cabinet_dev where idcabinet=?";
		Object[] objects = {cabinetId};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
		if(rs.size()>0)
			return Integer.valueOf(rs.get(0).get(0).toString());
		return 0;
	}
	public static void deleteByCabinetId(int cabinetId)throws DataBaseException
	{
		String sql = "delete from booklet_cabinet_dev where idcabinet=?";
    	Object[] values = new Object[1];
    	values[0] = cabinetId;
    	DatabaseMgr.getInstance().executeStatement(null,sql,values);
	}
}
