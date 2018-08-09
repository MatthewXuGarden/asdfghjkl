package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.util.ArrayList;
import java.util.List;


public class ProfileMapsBeanList
{
    private List profileList = new ArrayList();

    public ProfileMapsBeanList()
    {
    }

    public void load(int idprofile) throws Exception
    {
        String sql = "select * from profilemaps where idprofile = ? order by code";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idprofile) });
        Record record = null;
        ProfileMapsBean profileMapsBean = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            profileMapsBean = new ProfileMapsBean(record);
            profileList.add(profileMapsBean);
        }
    }
    public  boolean isButtonEnable(int idprofile ,String menu, String tab, String but)throws Exception{
    	String sql = "select * from profilemaps where idprofile = ? and menu = ? and tab = ? and button = ?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idprofile),menu,tab,but });
        if(recordset.size()>0){
        	return false;
        }
    	return true ;
    }

    public int size()
    {
        return profileList.size();
    }

    public ProfileMapsBean get(int pos)
    {
        return (ProfileMapsBean) profileList.get(pos);
    }
}
