package com.carel.supervisor.field;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;


public class VariableMgr
{
    private static VariableMgr me = new VariableMgr();
    private Map<Integer,Variable> map = new HashMap<Integer,Variable>();
    private Variable scheduler = null;
    private Variable remote = null;

    private VariableMgr()
    {
    	init();
    }

    public static VariableMgr getInstance()
    {
        return me;
    }

    public Variable getById(int id)
    {
    	return map.get(new Integer(id));
    }

    public Variable getById(Integer id)
    {
    	return map.get(id);
    }

    public void add(Variable variable)
    {
        map.put(variable.getInfo().getId(), variable);
    }
    
    public void clear()
    {
    	map.clear();
    	init();
    }
    
    private void init()
    {
    	VariableInfo v = new VariableInfo();
		v.setId(new Integer(0));
		v.setFrequency(new Integer(10));//Valutazione ogni 10 secondi
		v.setPriority(new Integer(1));
		v.setSite(new Integer(1));
		scheduler = new Variable(v);
		scheduler.setValue(new Float(1)); // vale sempre 1
		
		VariableInfo v2 = new VariableInfo();
		v2.setId(new Integer(-1));
		v2.setFrequency(new Integer(10));//Valutazione ogni 10 secondi
		v2.setPriority(new Integer(1));
		v2.setSite(new Integer(1));
		remote = new Variable(v2);
		remote.setValue(new Float(1)); // vale sempre 1
		
		map.put(scheduler.getInfo().getId(), scheduler);
		map.put(remote.getInfo().getId(), remote);
    }
}
