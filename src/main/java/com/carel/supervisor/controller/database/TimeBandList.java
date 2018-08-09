package com.carel.supervisor.controller.database;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;

import java.util.*;


public class TimeBandList
{
    private Map timebndList = new HashMap();
    private ArrayList timebndArrayList = new ArrayList();

    public TimeBandList(String dbId, String plantId, Integer idSite)
        throws DataBaseException
    {
        String sql = "select * from cftimeband where pvcode = ? and idsite = ? order by timecode";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, idSite });
        Record record = null;
        TimeBandBean timeband = null;

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            timeband = new TimeBandBean(record);
            timebndList.put(timeband.getIdtimeband(), timeband);
            timebndArrayList.add(timeband);
        }
    }

    public TimeBandBean getTimeBand(int i)
    {
        return (TimeBandBean) timebndArrayList.get(i);
    }

    public TimeBandBean get(Integer id)
    {
        return (TimeBandBean) timebndList.get(id);
    }

    public int size()
    {
        return timebndList.size();
    }

    public Integer[] getIds()
    {
        int size = timebndList.size();

        return (Integer[]) timebndList.keySet().toArray(new Integer[size]);
    }

    public static synchronized void deleteRecord(Object[] objects)
        throws DataBaseException
    {
        DataHs dataHs= CreateSqlHs.getDeleteData("cftimeband",
    			new String[]{"idtimeband","pvcode","idsite","timecode","timetype","timeband","iscyclic"},
    			objects,new String[]{"=","="},new String[]{"idtimeband","idsite"}); 
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

    	String sql = "DELETE FROM cftimeband WHERE  idtimeband=? AND idsite=?";
        DatabaseMgr.getInstance().executeStatement(sql, objects);
        
    } //deleteRecord

    public static synchronized void addRecord(Object[] objects)
        throws DataBaseException
    {
        String sql = "INSERT INTO cftimeband VALUES(?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(sql, objects);
        
        DataHs dataHs= CreateSqlHs.getInsertData("cftimeband",objects);										
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
    } //addRecord
    
    public static synchronized TimeBandBean getTimebandByCode(int idsite, String code)
    	throws DataBaseException
    {
    	String sql = "SELECT * from cftimeband where idsite=? and timecode=?";
    	RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite),code });
    	if(recordSet.size()>0)
    	{
    		Record record = recordSet.get(0);
    		return new TimeBandBean(record);
    	}
    	return null;
    }
    public static synchronized void updateRecord(Object[] objects)
        throws DataBaseException
    {
        String sql = "UPDATE cftimeband SET pvcode=? ,idsite=?,timecode=?,timetype=?,timeband=?,iscyclic=?,lastupdate=? WHERE idtimeband=?";
        DatabaseMgr.getInstance().executeStatement(sql, objects);
        
        DataHs dataHs= CreateSqlHs.getUpdateData("cftimeband",
    			new String[]{"idtimeband","pvcode","idsite","timecode","timetype","timeband","iscyclic"},
    			new Object[]{objects[objects.length-1]},new String[]{"="},new String[]{"idtimeband"}); 
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

        
    } //updateRecord

    public static synchronized void updateTimebandonly(Object[] objects)
    	throws DataBaseException
    {
    	String sql = "UPDATE cftimeband SET timeband=? ,lastupdate=? WHERE idtimeband=?";
        DatabaseMgr.getInstance().executeStatement(sql, objects);
    }
    public static synchronized boolean isTimebandInRule(int idsite, Integer idtimeband)
        throws DataBaseException
    {
        String sql = "select idtimeband from cfrule where idsite = ? and idtimeband = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), idtimeband });

        if (rs.size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getDescription(int idsite, int idtimeband)
        throws DataBaseException
    {
        String sql = "select timecode from cftimeband where idsite = ? and idtimeband = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idtimeband) });

        if (rs.size() > 0)
        {
            return rs.get(0).get("timecode").toString();
        }
        else
        {
            return "";
        }
    }
} //TimeBandList
