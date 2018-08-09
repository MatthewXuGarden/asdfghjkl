package com.carel.supervisor.dataaccess.function;

import com.carel.supervisor.base.util.Dictionary;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DeviceFunz
{
    private static DeviceFunz funz = new DeviceFunz();
    private static String FUN_APPLY = "com.carel.supervisor.dataaccess.function.CarelFunction";
    
    private Dictionary container = null;
    
    private DeviceFunz()
    {
        container = new Dictionary();
        try {
            load();
        }catch(Exception e){}
    }
    
    public static DeviceFunz getInstance() {
        return funz;
    }
    
    public long applyFunction(int idDevMdl,int idVarMdl,long value)
    {
        String obj = (String)container.get(new Integer(idDevMdl),new Integer(idVarMdl));
        if(obj != null)
        {
            try
            {
                Long ret = (Long)((Class.forName(FUN_APPLY)).getMethod(obj,new Class[]{Long.class}))
                                   .invoke(CarelFunction.class,new Object[]{new Long(value)});
                if(ret != null)
                    value = ret.longValue();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return value;
    }
        
    
    private void load()
        throws Exception
    {
        String sql = "select * from cfcarelfunz";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
        Record r = null;
        for(int i=0; i<rs.size(); i++)
        {
            r = rs.get(i);
            this.container.add((Integer)r.get("iddevmdl"),(Integer)r.get("idvarmdl"),UtilBean.trim(r.get("carelfunz")));
        }
        //this.container.add(new Integer(116),new Integer(20911),"calcYear");
    }
}
