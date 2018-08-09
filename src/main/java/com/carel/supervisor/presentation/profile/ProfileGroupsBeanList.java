package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileGroupsBeanList
{
    private List profileList = new ArrayList();
    private Map profileMap = new HashMap();

    public ProfileGroupsBeanList()
    {
    }

    public void load(int idprofile, Integer idSite) throws Exception
    {
        String sql = "select * from profilegroups where idprofile = ? and idsite = ?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idprofile), idSite });
        Record record = null;
        ProfileGroupsBean profileGroupsBean = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            profileGroupsBean = new ProfileGroupsBean(record);
            profileList.add(profileGroupsBean);
            profileMap.put((Integer) record.get("idprofile"), record);
        }
    }

    public int size()
    {
        return profileList.size();
    }

    public ProfileGroupsBean get(int pos)
    {
        return (ProfileGroupsBean) profileList.get(pos);
    }

    public ProfileGroupsBean getById(int id)
    {
        return (ProfileGroupsBean) profileMap.get(new Integer(id));
    }
    public boolean isGroupActive(int idGroup)
    {
        for(int i=0;i<profileList.size();i++)
        {
        	ProfileGroupsBean bean = get(i);
        	if(bean.getIdgroup() == idGroup)
        	{
        		return false;
        	}
        }
        return true;
    }
    public static boolean isGroupEnabled(int idsite, int idprofile, int idgroup)
        throws DataBaseException
    {
        String sql = "select count(1) from profilegroups where idsite=? and idprofile=? and idgroup=?";
        Object[] values = new Object[3];
        values[0] = new Integer(idsite);
        values[0] = new Integer(idprofile);
        values[0] = new Integer(idgroup);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);
        int count = ((Integer) rs.get(0).get("count")).intValue();

        if (count > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
