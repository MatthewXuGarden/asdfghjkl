package com.carel.supervisor.dataaccess.dataconfig;

import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DeviceCarelInfoList extends DeviceInfoList
{
    public DeviceCarelInfoList(String dbId, String plantId)
        throws DataBaseException
    {
        super();

        String sql = "select cfdevice.*,cfline.comport,cfdevmdl.code as devtype,cfline.code as linenum from cfdevice inner join cfline on cfline.idline=cfdevice.idline " +
        		" inner join cfdevmdl on cfdevice.iddevmdl=cfdevmdl.iddevmdl " +
        		"where cfdevice.pvcode = ? and cfdevice.iscancelled = ? and " +
        		"cfdevice.islogic = ? and cfline.typeprotocol='CAREL' order by cfdevice.globalindex";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, "FALSE", "FALSE" });
        Record record = null;
        devInfo = new DeviceInfo[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            devInfo[i] = new DeviceInfo(record);
            devByCode.put(devInfo[i].getCode(), devInfo[i]);
            devById.put(devInfo[i].getId(), devInfo[i]);
        }
    }
    
    public void bind(Map registry) throws Exception
    {
        LineCarelInfoList lineInfoList = (LineCarelInfoList) registry.get("cfline");
        LineInfo lineInfo = null;

        for (int i = 0; i < devInfo.length; i++)
        {
            lineInfo = lineInfoList.getById(devInfo[i].getLine());
            devInfo[i].setLineInfo(lineInfo);
        }
    }
}
