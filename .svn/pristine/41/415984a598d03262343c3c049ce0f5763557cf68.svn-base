package com.carel.supervisor.presentation.polling;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.LineInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class TimeInfoList {
	
	protected TimeInfo[] timeInfo = null;
    protected Map timeById = new HashMap();

    protected TimeInfoList() 
    {	
    }
    
    public TimeInfoList(String dbId, String plantId) throws DataBaseException
    {
        super();

        String sql = "select * from rmtimetable order by idrmtimetable";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql);
        Record record = null;
        timeInfo = new TimeInfo[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            timeInfo[i] = new TimeInfo(record);
            timeById.put(new Integer(timeInfo[i].getIdrmtimetable()), timeInfo[i]);
        }
    }
    
    public static TimeInfo[] retriveTimeTable() throws DataBaseException
    {
    	String sql = "select * from rmtimetable order by idrmtimetable";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    	
    	TimeInfo[] fasce = new TimeInfo[rs.size()];
    	for (int i=0;i<rs.size();i++)
    	{
    		fasce[i] = new TimeInfo(rs.get(i));
    	}
    	
    	return fasce;
    }
    
    public static TimeInfo[] retriveTimeTableJoin() throws DataBaseException
    {
    	String sql = "select rmtimetable.*,hour_from,hour_to,minute_from,minute_to from rmtimetable inner join rmtime on rmtime.idrmtimetable=rmtimetable.idrmtimetable order by idrmtimetable,nslot";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    /*	
    	TimeInfo[] fasce = new TimeInfo[rs.size()];
    	for (int i=0;i<rs.size();i++)
    	{
    		fasce[i] = new TimeInfo(rs.get(i));
    	}
    */
    	ArrayList fasce_list = new ArrayList();
    	for (int i=0;i<rs.size();i++)
    	{
    		fasce_list.add(new TimeInfo(rs.get(i),true));
    	}
    	
    	TimeInfo tmp = null;
    	TimeInfo tmpNext = null;
    	ArrayList final_list = new ArrayList();
    	int nextInd = 0;
    	//for(int z=0; z < fasce_list.size(); z++){	//Determino la dimensione dell'array da restituire con
    	int z = 0;
    	while(z < fasce_list.size()){  
    	  tmp = (TimeInfo) fasce_list.get(z);
    		nextInd = z+1;
    		while(nextInd < fasce_list.size()){
    			tmpNext = (TimeInfo) fasce_list.get(nextInd);
    			if(tmp.getIdrmtimetable() == tmpNext.getIdrmtimetable()){
    				tmp.addValue(tmpNext.getFrom_time());
    				tmp.addValue(tmpNext.getTo_time());
    			}
    			else
    				break;
    		nextInd++;
    		}
    		z = nextInd;
    		final_list.add(tmp);
    	}
    	
    	TimeInfo[] fasce = new TimeInfo[final_list.size()];
    	
    	for(int i=0; i < final_list.size(); i++)
    		fasce[i] = (TimeInfo) final_list.get(i);
    	
    /*	
    	for(int h=0; h < fasce.length; h++){
    		System.out.println("--Fascia Name -> "+fasce[h].getName());
    		for(int g =0; g < (fasce[h]).getSlot().size(); g++){
    			System.out.print("Slot: "+(g+1)+" "+(fasce[h]).getSlot().get(g)+" ");
    		}
    		System.out.println();
    	}
    */	
    	
    	return fasce;
    }

}