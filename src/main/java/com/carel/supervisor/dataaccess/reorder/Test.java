package com.carel.supervisor.dataaccess.reorder;

import java.sql.Timestamp;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;


public class Test
{
    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();
        DatabaseMgr.getInstance().executeStatement(null,"update buffer set keymax=?,keyactual=?",new Object[]{new Integer(10),new Integer(5)});
        
        DatabaseMgr.getInstance().executeStatement(null,"insert into reorder values(?,?,1,1)",new Object[]{new Integer(1),new Integer(20)});
        
        
        long time=System.currentTimeMillis();
        for(int i=0;i<10;i++){
        	
        	DatabaseMgr.getInstance().executeStatement(null,"insert into hsvarhistor  values(1,20,(?+6)%10,1,1,?)",new Object[]{new Short((short)i) , new Timestamp(time+300000L*(long)i)});
        	//DatabaseMgr.getInstance().executeStatement(null,"insert into hsvarhistor  values(1,20,?,1,1,?)",new Object[]{new Short((short)i) , new Timestamp(time+300000L*(long)i)});
             	
        	
        }
        System.out.println("OK");
    }
}
