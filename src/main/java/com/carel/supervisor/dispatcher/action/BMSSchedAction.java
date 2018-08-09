package com.carel.supervisor.dispatcher.action;

import java.sql.Timestamp;


public class BMSSchedAction extends BMSGeneralAction
{

	public BMSSchedAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar,
        Timestamp startTime, Timestamp endTime)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, startTime, endTime);
    }

 
    
}
