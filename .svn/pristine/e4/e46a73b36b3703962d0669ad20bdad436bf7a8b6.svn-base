package com.carel.supervisor.dataaccess.history.organize;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.history.HistoryMgr;

public class DBSchemaChecker {
	private final static int NUM_TABLE=26;
	
	public DBSchemaChecker(){}
	
	public void checkTablesSchema() throws Exception {
		double t1=System.currentTimeMillis();
		DatabaseMgr databaseMgr=DatabaseMgr.getInstance();
		HashMap<String, Integer> mapTableDB= new HashMap<String, Integer>();
		
		RecordSet recordSet=null;
		
			recordSet = databaseMgr.executeQuery(null, "select tablename from pg_tables where tablename like 'hsvarhaccp_%' order by tablename");
			for(int i=0;i<recordSet.size();i++)
				mapTableDB.put((String)recordSet.get(i).get("tablename"), new Integer(1));
			
			Calendar calendar= new GregorianCalendar();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(GregorianCalendar.MONTH, -24);
			int month=0;
			String []tableToHave= new String[NUM_TABLE];
			for(int i=0;i<NUM_TABLE;i++){
				month=calendar.get(GregorianCalendar.MONTH)+1;
				tableToHave[i]="hsvarhaccp_"+"y"+calendar.get(GregorianCalendar.YEAR)+"m"+((month<10)?("0"+month):month);
				calendar.add(GregorianCalendar.MONTH,1);
			}
			
		
			/*
			for(int i=0;i<NUM_TABLE;i++){
				if(mapTableDB.containsKey(tableToHave[i]))
					mapTableDB.remove(tableToHave[i]);
				else{
					HistoryMgr.getInstance().executeMultiCreation(null, new TableHsCreate(tableToHave[i]).getOperations());
				}
			}
			
			Set<String> keySet = mapTableDB.keySet();
			for(String key:keySet)
				HistoryMgr.getInstance().executeMultiCreation(null, new TableHsDrop(key).getOperations());
			*/
			
			//double t2=System.currentTimeMillis();
			//LoggerMgr.getRootLogger().info("checkTablesSchema: "+(t2-t1)/1000+"s");
			//System.out.println("checkTablesSchema: "+(t2-t1)/1000+"s");
	}
}
