package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.dataaccess.db.*;
import java.util.*;


public class DeviceInfoList extends AbstractBindable
{
    protected DeviceInfo[] devInfo = null;
    protected Map devByCode = new HashMap();
    protected Map devById = new HashMap();

    protected DeviceInfoList()
    {
    }

    public DeviceInfoList(String dbId, String plantId)
        throws DataBaseException
    {
        super();

        String sql = "select a.*,b.code as desc from cfdevice as a left outer join cfdevmdl as b on a.iddevmdl = b.iddevmdl " +
		 " where a.pvcode = ? and a.iscancelled = ? order by islogic ";
        
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, "FALSE" });
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
    
    public DeviceInfoList(String dbId, String plantId, int[] idLinesExcept)
    throws DataBaseException
	{
	    super();
	
	    String ids ="";
	    for(int i =0;i<idLinesExcept.length;i++){
	    	ids += idLinesExcept[i] + ",";
	    }
	    String  sql = "select cfdevice.*,cfline.comport,cfdevmdl.code as devtype,cfline.code as linenum from cfdevice inner join cfline on cfline.idline=cfdevice.idline " +
		" inner join cfdevmdl on cfdevice.iddevmdl=cfdevmdl.iddevmdl " +
		"where cfdevice.pvcode = ? and cfdevice.iscancelled = ? and " +
		"cfdevice.islogic = ? and cfdevice.idline not in ("+ids.substring(0,ids.length()-1)+") order by cfdevice.globalindex";
	    RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
	            new Object[] { plantId, "FALSE" ,"FALSE"});
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
        LineInfoList lineInfoList = (LineInfoList) registry.get("cfline");
        LineInfo lineInfo = null;

        for (int i = 0; i < devInfo.length; i++)
        {
            lineInfo = lineInfoList.getById(devInfo[i].getLine());
            devInfo[i].setLineInfo(lineInfo);
        }
    }

    public int size()
    {
        return devInfo.length;
    }

    public DeviceInfo get(int pos) // get per posizione
    {
        return devInfo[pos];
    }

    // get per code
    public DeviceInfo getByCode(String code)
    {
        return (DeviceInfo) devByCode.get(code);
    }

    // get per idDevice
    public DeviceInfo getByIdDevice(Integer idDevice)
    {
        return (DeviceInfo) devById.get(idDevice);
    }

    public void clear()
    {
        for (int i = 0; i < devInfo.length; i++)
        {
            devInfo[i] = null;
        }

        devByCode.clear();
        devById.clear();
        devByCode = null;
        devById = null;
        devInfo = null;
    }
}
