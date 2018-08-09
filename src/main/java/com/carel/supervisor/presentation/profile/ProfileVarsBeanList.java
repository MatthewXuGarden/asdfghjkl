package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.util.ArrayList;
import java.util.List;


public class ProfileVarsBeanList
{
    private List profileList = new ArrayList();

    public ProfileVarsBeanList()
    {
    }

    public void load(String profile, Integer idSite) throws Exception
    {
        String sql = "select * from profilevars where profile = ? and idsite = ?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null,
                sql, new Object[] { profile, idSite });
        Record record = null;
        ProfileVarsBean profileVarsBean = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            profileVarsBean = new ProfileVarsBean(record);
            profileList.add(profileVarsBean);
        }
    }

    public int size()
    {
        return profileList.size();
    }

    public ProfileVarsBean get(int pos)
    {
        return (ProfileVarsBean) profileList.get(pos);
    }
}
