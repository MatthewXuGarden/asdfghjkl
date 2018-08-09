package com.carel.supervisor.presentation.groupmng;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;

import java.sql.Timestamp;


public class GroupManager
{
    public GroupManager()
    {
    }
    public int insertNewGroup(int idsite) throws DataBaseException
    {
        Object[] values = new Object[6];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfgroup", "idgroup");
        values[1] = new Integer(idsite);
        values[2] = new Integer(1);
        values[3] = values[0].toString();
        values[4] = "FALSE";
        values[5] = new Timestamp(System.currentTimeMillis());

        String sql = "insert into cfgroup values (?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        return Integer.parseInt(values[0].toString());
    }

    public void updateIdGroupInDevPhy(int idsite, int iddev, int newgroup)
        throws DataBaseException
    {
        Object[] values = new Object[4];
        values[0] = new Integer(newgroup);
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Integer(idsite);
        values[3] = new Integer(iddev);

        String sql = "update cfdevice set idgroup = ? , lastupdate = ? where idsite = ? and iddevice = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public void removeGroup(int idsite, int idgroup, int groupToMoveOldDevices)
        throws DataBaseException
    {
        //rimozione descrizioni variabili di gruppo e variabili di gruppo
        String sql = "delete from cftableext where cftableext.idsite = ? and cftableext.tablename = ? and cftableext.tableid in (select idgroup from cfgroupvar where idsite= ? and idgroup = ?)";
        Object[] values = new Object[4];
        values[0] = new Integer(idsite);
        values[1] = "cfgroupvar";
        values[2] = new Integer(idsite);
        values[3] = new Integer(idgroup);

        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        sql = "delete from cfgroupvar where idsite = ? and idgroup = ?";
        values = new Object[2];
        values[0] = new Integer(idsite);
        values[1] = new Integer(idgroup);

        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        //rimozione gruppo da cfgroup
        values = new Object[2];
        values[0] = new Integer(idsite);
        values[1] = new Integer(idgroup);

        sql = "delete from cfgroup where idsite = ? and idgroup = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        //rimozione da cftableext
        sql = "delete from cftableext where idsite = ? and tablename = ? and tableid = ?";
        values = new Object[3];
        values[0] = new Integer(idsite);
        values[1] = "cfgroup";
        values[2] = new Integer(idgroup);
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        //tutti i device del gruppo eliminato ora fanno parte del gruppo globale
        sql = "update cfdevice set idgroup = ? , lastupdate = ? where idsite = ? and idgroup = ?";
        values = new Object[4];
        values[0] = new Integer(groupToMoveOldDevices);
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = values[0] = new Integer(idsite);
        values[3] = new Integer(idgroup);
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public void removeEmptyGroup(int idsite, int idgroup)
        throws DataBaseException
    {
        //rimozione gruppo da cfgroup
        Object[] values = new Object[2];
        values[0] = new Integer(idsite);
        values[1] = new Integer(idgroup);

        String sql = "delete from cfgroup where idsite = ? and idgroup = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);

        //rimozione da cftableext
        sql = "delete from cftableext where idsite = ? and tablename = ? and tableid = ?";
        values = new Object[3];
        values[0] = new Integer(idsite);
        values[1] = "cfgroup";
        values[2] = new Integer(idgroup);
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public int numOfDeviceOfGroup(int idsite, int idgroup)
        throws DataBaseException
    {
        String sql = "select count(1) from cfdevice where idsite = ? and idgroup = ? and iscancelled = 'FALSE'";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idgroup);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() == 0)
        {
            return 0;
        }
        else
        {
            int num = ((Integer) rs.get(0).get("count")).intValue();

            return num;
        }
    }

    public static String getDescriptionOfGroup(int idsite, int idgroup,
        String language) throws DataBaseException
    {
        String sql = "select description from cftableext where idsite = ? and tablename='cfgroup' and tableid = ? and languagecode = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idgroup);
        param[2] = language;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() > 0)
        {
            return rs.get(0).get("description").toString();
        }
        else
        {
            return "";
        }
    }
}
