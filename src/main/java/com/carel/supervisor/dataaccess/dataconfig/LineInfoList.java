package com.carel.supervisor.dataaccess.dataconfig;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


/**
 * @author Chiara Moretti <br>
 * Carel S.p.A. <br>
 * <br>
 * 19-dic-2005 17.32.38 <br>
 */
public class LineInfoList extends AbstractBindable
{
    protected LineInfo[] lineInfo = null;
    protected HashMap<Integer, LineInfo> lineById = new HashMap<Integer, LineInfo>();
    
    
    protected LineInfoList() 
    {	
    }
    
    public LineInfoList(String dbId, String plantId) throws DataBaseException
    {
        super();

        String sql = "select * from cfline order by code";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql);
        Record record = null;
        lineInfo = new LineInfo[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            lineInfo[i] = new LineInfo(record);
            lineById.put(new Integer(lineInfo[i].getId()), lineInfo[i]);
        }
    }

    @SuppressWarnings("unchecked")
	public void bind(Map registry) throws Exception
    {
        DeviceInfoList devInfoList = (DeviceInfoList) registry.get("cfdev");
        DeviceInfo devInfo = null;
        LineInfo lineInfo = null;
        for (int i = 0; i < devInfoList.size(); i++)
        {
        	devInfo = devInfoList.get(i);
        	lineInfo = getById(devInfo.getLine());
        	if (null != lineInfo) //NOT LOGICAL DEVICES
        	{
        		lineInfo.add(devInfo);
        	}
        }
    }
    
    public int size()
    {
    	if (null == lineInfo)
    	{
    		return 0;
    	}
    	else
    	{
    		return lineInfo.length;
    	}
    }

    public LineInfo get(int pos)
    {
        return lineInfo[pos];
    }

    public LineInfo getById(Integer idLine)
    {
        return (LineInfo) lineById.get(idLine);
    }

    public void clear()
    {
    	if (null != lineInfo)
    	{
	        for (int i = 0; i < lineInfo.length; i++)
	        {
	        	lineInfo[i].clear();
	            lineInfo[i] = null;
	        }
	
	        lineById.clear();
	        lineById = null;
	        lineInfo = null;
    	}
    }
}
