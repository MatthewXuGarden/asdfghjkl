package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class VariableLogicInfoList extends VariableInfoList
{

    public VariableLogicInfoList(String dbId, String plantId)
        throws DataBaseException
    {
        super();

        String sql =
            "SELECT cfvariable.* FROM cfvariable WHERE " +
            " pvcode = ? and iscancelled = ? and islogic = ? and idsite=1 and idvariable>0 order by idvariable, islogic";

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                sql, new Object[] { plantId, "FALSE", "TRUE" });
        
        sql = "SELECT cfvariable.* FROM cfvariable WHERE " +
            " pvcode = ? and iscancelled = ? and islogic = ? and idsite=1 and idvariable<0 order by idvariable, islogic";

        RecordSet recordSet2 = DatabaseMgr.getInstance().executeQuery(dbId,
                sql, new Object[] { plantId, "FALSE", "TRUE" });
        
        Record record = null;
        variableInfo = new VariableInfo[recordSet.size() + recordSet2.size()];

        int i = 0;
        int num = recordSet.size();
        for (i = 0; i < num; i++)
        {
            record = recordSet.get(i);
            variableInfo[i] = new VariableInfo(record);
            variableInfoById.put(variableInfo[i].getId(), variableInfo[i]);
        }
        
        
        for (i = 0; i < recordSet2.size(); i++)
        {
            record = recordSet2.get(i);
            variableInfo[i + num] = new VariableInfo(record);
            variableInfoById.put(variableInfo[i + num].getId(), variableInfo[i + num]);
        }
        
        DeviceInfoList devInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
        DeviceInfo devInfo = null;

        for (i = 0; i < variableInfo.length; i++)
        {
            devInfo = devInfoList.getByIdDevice(variableInfo[i].getDevice());
            variableInfo[i].setDeviceInfo(devInfo);
        }
    }
}
