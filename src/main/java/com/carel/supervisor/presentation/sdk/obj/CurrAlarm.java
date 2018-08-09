package com.carel.supervisor.presentation.sdk.obj;

import java.sql.Timestamp;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class CurrAlarm
{
    public int idalr = 0;
    public String date = "";
    public String desc = "";
    public String ackuser = "";
    public String acktime = "";
    public String resetuser = "";
    public String resettime = "";
    public String priority = "";
    public int prio = 1;
    
    public CurrAlarm(int idalr, String date, String desc, String ackuser, 
                     String acktime, String resetuser, String resettime, String priority)
    {
        this.idalr = idalr;
        this.date = date;
        this.desc = desc;
        this.ackuser = ackuser;
        this.acktime = acktime;
        this.resetuser = resetuser;
        this.resettime = resettime;
        this.priority = priority;
        this.prio = 1;
    }
    
    public CurrAlarm(Record r)
    {
        this.idalr = ((Integer)r.get("idalarm")).intValue();
        this.date = DateUtils.date2String((Timestamp)r.get("starttime"), "yyyy/MM/dd HH:mm:ss");
        this.desc = UtilBean.trim(r.get("description"));
        this.ackuser = UtilBean.trim(r.get("ackuser"));
        this.acktime = DateUtils.date2String((Timestamp)r.get("acktime"), "yyyy/MM/dd HH:mm:ss");;
        this.resetuser = UtilBean.trim(r.get("resetuser"));
        this.resettime = DateUtils.date2String((Timestamp)r.get("resettime"), "yyyy/MM/dd HH:mm:ss");;
        this.priority = ""+r.get("priority");
        this.prio = adjustPriority(UtilBean.trim(r.get("prio")));
    }
    
    public String getAcktime()
    {
        return acktime;
    }

    public String getAckuser()
    {
        return ackuser;
    }

    public String getDate()
    {
        return date;
    }

    public String getDesc()
    {
        return desc;
    }

    public int getIdalr()
    {
        return idalr;
    }

    public String getResettime()
    {
        return resettime;
    }

    public String getResetuser()
    {
        return resetuser;
    }
    
    public String getPriority()
    {
    	return priority;
    }
    
    public int getPrio()
    {
    	return prio;
    }
    
    private int adjustPriority(String p)
    {
    	int iP = 1;
    	try {
    		iP = Integer.parseInt(p);
    		if(iP > 4)
    			iP = 4;
    	}
    	catch(Exception e) {}
    	return iP;
    }
}
