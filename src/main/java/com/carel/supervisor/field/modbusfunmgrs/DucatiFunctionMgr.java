package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashSet;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettings;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettingsMap;

public class DucatiFunctionMgr implements IFunctionMgr
{
	private static IFunctionMgr me;
	private HashSet<Integer> pf = new HashSet<Integer>();
	
	private DucatiFunctionMgr() {
		pf = new HashSet<Integer>();
        try {
        	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
        			" select cfvarmdl.idvarmdl,cfvarmdl.code,cfvarmdl.iddevmdl from cfvarmdl,cfdevmdl " + 
        			" where  "+
        			" cfvarmdl.code like 'PF%' and "+
        			" cfvarmdl.iddevmdl=cfdevmdl.iddevmdl and "+
        			" cfdevmdl.iddevmdl = (select iddevmdl from cfdevmdl where code='DUCATI'); ");
        	if(rs!=null){
        		for (int i = 0; i < rs.size(); i++) {
					pf.add((Integer)rs.get(i).get(0));
				}
        	}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public static IFunctionMgr getInstance() {
		if (me == null)
			me = new DucatiFunctionMgr();
		return me;
	}
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.field.modbusfunmgrs.IFunctionMgr#applyFunction(com.carel.supervisor.field.Variable, float)
	 */
	public float applyFunction(long value, Variable v)
	{
		if(pf.contains(v.getInfo().getModel())){
			int sign= (int) ((value >> 31) & 0x1);
			value = value & 0x7fff;
			value = (long) (value*(Math.pow(-1, sign)));
		}
		ModbusSettings mbs = ModbusSettingsMap.get(v.getInfo().getModel());
		float decimalfactor = 1.0f;
		if(mbs!=null)
			decimalfactor = mbs.getAvalue();
		return new Float(value*decimalfactor);
	}

    public float applyInverseFunction(Variable v)
    {
    	float value = v.getCurrentValue();
		ModbusSettings mbs = ModbusSettingsMap.get(v.getInfo().getModel());
		float decimalfactor = 1.0f;
		if(mbs!=null)
			decimalfactor = mbs.getAvalue();
		return new Float(value/decimalfactor);
    }    
}
