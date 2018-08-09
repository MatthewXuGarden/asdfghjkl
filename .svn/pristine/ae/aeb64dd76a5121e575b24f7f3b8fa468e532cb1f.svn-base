package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;

public class MP99FunctionMgr implements IFunctionMgr
{
    private static MP99FunctionMgr me;
    private HashMap<Integer, Float[]> varfun;
    
    private MP99FunctionMgr() {
        varfun = new HashMap<Integer, Float[]>();
        try {
            loadMP99Fun();
        }
        catch(Exception e){
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    public static MP99FunctionMgr getInstance() 
    {
        if(me == null)
            me = new MP99FunctionMgr();
        return me;
    }
    
    public float applyFunction(long value, Variable v)
    {
    	float retvalue = Float.NaN;
        try
        {
        	int d = v.getInfo().getDecimal();
        	float dd = (float)Math.pow(10, d);
        	float fval = value;//v.getCurrentValue();
        	Float[] fvals = varfun.get(v.getInfo().getModel());
        	retvalue = 0.0f+Math.round(((fvals[0] * fval + fvals[1])) * dd) / dd;        	
        }
        catch(Exception e){}
        
        return retvalue;
    }

    public float applyInverseFunction(Variable v)
    {
        try
        {
        	int d = v.getInfo().getDecimal();
        	float dd = (float)Math.pow(10, d);
        	float fval = v.getCurrentValue();
        	Float[] fvals = varfun.get(v.getInfo().getModel());
        	v.setValue(0.0f+Math.round((fval - fvals[1]) * dd / fvals[0]) / dd);
        }
        catch(Exception e){}
        
    	return v.getCurrentValue();
    }    
    
    public void loadMP99Fun() throws Exception
    {
        varfun.clear();
        String sql = "select * from cfmp99fun";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
        Record r = null;
        for(int i=0; i<rs.size(); i++)
        {
            r = rs.get(i);
            varfun.put((Integer)r.get("idvarmdl"), new Float[]{(Float)r.get("a"),(Float)r.get("b")});
        }
    }
    
}
