package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class BookletCabinetList {
	
	public static List<BookletCabinetBean> retrieveCabinet() throws Exception {
		List<BookletCabinetBean> cabinets = new ArrayList<BookletCabinetBean>();
		
		String sql = "select * from booklet_cabinet order by cabinet";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		BookletCabinetBean tmp = null;
		for (int i = 0; i < rs.size(); i++) {
			tmp = new BookletCabinetBean(rs.get(i));
			cabinets.add(tmp);
		}
		return cabinets;
	}
	public static List<BookletCabinetBean> retrieveCabinetWithDeviceNumber() throws Exception {
		List<BookletCabinetBean> cabinets = retrieveCabinet();
		for(BookletCabinetBean bean: cabinets)
		{
			int id = bean.getId();
			bean.setDeviceNumber(BookletCabinetDeviceList.getDeviceNumber(id));
		}
		return cabinets;
	}
	public static boolean isFileInUse(String fileName) throws Exception
	{
		String sql = "select * from booklet_cabinet where file_name=?";
		Object[] objects = {fileName};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
		if(rs.size()>0)
			return true;
		else
			return false;
	}
	public static BookletCabinetBean retrieveById(int id) throws Exception {
		String sql = "select * from booklet_cabinet where id=?";
		Object[] objects = {id};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
		BookletCabinetBean tmp = null;
		if(rs.size()>0)
			tmp = new BookletCabinetBean(rs.get(0));
		return tmp;
	}
	public static String[] getCabinetByDeviceId(int deviceId) throws Exception
	{
		String sql = "select cabinet from booklet_cabinet "+
					"inner join booklet_cabinet_dev on booklet_cabinet_dev.idcabinet=booklet_cabinet.id and iddevice=?";
		Object[] objects = {deviceId};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
		String[] result = new String[rs.size()];
		for(int i=0;i<rs.size();i++)
		{
			result[i] = rs.get(i).get(0).toString();
		}
		return result;
	}
	public static void save(BookletCabinetBean bean) throws DataBaseException
    {
		int idcabinet = -1;
		String sql = "";
		RecordSet rs = null;
		//insert
        if(bean.getId() == -1)
        {
        	Object[] values = new Object[4];
			SeqMgr o = SeqMgr.getInstance();
			idcabinet = o.next(null, "booklet_cabinet", "id");
			values[0] = idcabinet;
			values[1] = bean.getCabinet();
			values[2] = bean.getFileName();
			values[3] = new Timestamp(System.currentTimeMillis());
			String exeSql = "insert into booklet_cabinet(id,cabinet,file_name,lastupdate) values(?,?,?,?) ";
			DatabaseMgr.getInstance().executeStatement(null, exeSql, values);

			sql = "select * from booklet_cabinet where id=?";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idcabinet) });
			if(rs.size()>0)
				bean.setId(idcabinet);
        }
        //update
        else
        {
        	sql = "update booklet_cabinet set cabinet = ?, file_name = ? , lastupdate = ? where id = ?";
        	Object[] values = new Object[4];
        	values[0] = bean.getCabinet();
        	values[1] = bean.getFileName();
        	values[2] = new Timestamp(System.currentTimeMillis());
        	values[3] = bean.getId();
        	DatabaseMgr.getInstance().executeStatement(null,sql,values);
        	BookletCabinetDeviceList.deleteByCabinetId(bean.getId());
        }
    }
	public static void delete(int id)throws DataBaseException
	{
		String sql = "delete from booklet_cabinet where id=?";
    	Object[] values = new Object[1];
    	values[0] = id;
    	DatabaseMgr.getInstance().executeStatement(null,sql,values);
	}
}
