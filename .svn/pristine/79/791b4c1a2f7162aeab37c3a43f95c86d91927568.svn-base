package com.carel.supervisor.dataaccess.reorder.test.insert.testmultivar;

import java.sql.Date;
import java.sql.Timestamp;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

public class InsertDB {
	
	private static final int KMAX=9;
	private static final int NEW_KMAX=6;
	private static final int KACTUAL=4; //0...KMAX-1
	private static final int NUM_VAR=2; //0...KMAX-1
	
	private static final boolean ISTURN=true;
	
//	LA CLASS REORDER E' STATA LEGGERMENTE MODIFICATA PER PRENDERE COME NEWKEYMAX DIRETTAMENTE IL SAMPLINGPERIOD NELLA TABELLA REORDER
	
	public static void main(String[] args) throws Exception {
		BaseConfig.init();


		long startTime= System.currentTimeMillis();
		StringBuffer sql= new StringBuffer();
		 Object []objects= new Object[6];
		 objects[0]=new Short((short)1);
		 objects[3]= new Integer(300);
		 objects[4]= new Integer(-1);
		 
		 
		 
		DatabaseMgr.getInstance().executeStatement(null,"truncate hsvarhistor",null);
		DatabaseMgr.getInstance().executeStatement(null,"truncate buffer",null);
		sql.append("INSERT INTO hsvarhistor VALUES(?,?,?,?,?,?)");
			
		for(int i=0;i<NUM_VAR;i++){
			objects[1]= new Integer(2+i);
			DatabaseMgr.getInstance().executeStatement(null,"INSERT INTO buffer VALUES(?,?,?,?,?)",new Object[]{new Integer(1),new Integer(2+i),new Short((short)KMAX),new Short((short)KACTUAL),new Boolean(ISTURN)});
			
			for(int j=0;j<KMAX;j++){
				objects[2]= new Short((short)((j+KACTUAL+1)%KMAX));
				objects[5]= new Timestamp(startTime+((Integer)objects[3]).longValue()*1000L*(long)j);
				DatabaseMgr.getInstance().executeStatement(null,sql.toString(),objects);
			}//for
			
			DatabaseMgr.getInstance().executeStatement(null,"INSERT INTO reorder VALUES(?,?,?,?)",new Object[]{new Integer(1),new Integer(2+i),new Integer(NEW_KMAX),new Integer(NEW_KMAX)});
		}//for
			System.out.println("Ok");	
	}//main

}//InsertDB
