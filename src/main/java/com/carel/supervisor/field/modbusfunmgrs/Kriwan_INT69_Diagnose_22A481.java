package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;
import java.util.HashSet;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettings;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettingsMap;

public class Kriwan_INT69_Diagnose_22A481 implements IFunctionMgr
{
	private static IFunctionMgr me;
	private HashMap<Integer, Integer> varfun = new HashMap<Integer, Integer>();
	private Logger logger = LoggerMgr.getLogger(this.getClass());
	
	private Kriwan_INT69_Diagnose_22A481() {
		varfun = new HashMap<Integer, Integer>();
		try {
			load();
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}


	public static IFunctionMgr getInstance() {
		if (me == null)
			me = new Kriwan_INT69_Diagnose_22A481();
		return me;
	}
	
	private void load() {
		varfun = new HashMap<Integer, Integer>();
		varfun.clear();
		
		String[] codes = {"Time_since_last_err_m/h","Err_mem_t_diff_last","Err_mem_t_diff_1","Err_mem_t_diff_2","Err_mem_t_diff_3","Err_mem_t_diff_4",
				"Err_mem_t_diff_5","Err_mem_t_diff_6","Err_mem_t_diff_7","Err_mem_t_diff_8","Err_mem_t_diff_9","Err_mem_t_diff_10",
				"Err_mem_t_diff_11","Err_mem_t_diff_12","Err_mem_t_diff_13","Err_mem_t_diff_14","Err_mem_t_diff_15","Err_mem_t_diff_16",
				"Err_mem_t_diff_17","Err_mem_t_diff_18","Err_mem_t_diff_19"};
		for(int i=0;i<codes.length ;i++)
		{
			String code = codes[i];
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '"+code+"'");
				if(rs!=null && rs.size()>0){
					varfun.put((Integer) rs.get(0).get(0), 0);
				}
			} catch (DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'Delay_active'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), 1);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.field.modbusfunmgrs.IFunctionMgr#applyFunction(com.carel.supervisor.field.Variable, float)
	 */
	public float applyFunction(long value, Variable v)
	{
		Integer idvarmdl = v.getInfo().getModel();
		Integer prms = varfun.get(idvarmdl);
		if(prms==null)
			return (float)value;
		switch (prms) {
		case 0:
			
			value = value<=120?value:value-118;
			String mu = value<=120?"min":"hour";
			v.getInfo().setMeasureunit(mu);
			return (float) value;
		case 1:
			logger.info("Z_TotalOK");
			Variable v1 = getVariable(v.getInfo().getDevice(), "MTPDA");
			Variable v2 = getVariable(v.getInfo().getDevice(),"DTPDA");
			Variable v3 = getVariable(v.getInfo().getDevice(),"HMTISPTA");
			Variable v4 = getVariable(v.getInfo().getDevice(),"HSFMA");
			
			float v1_v = v1.getCurrentValue();
			float v2_v = v2.getCurrentValue();
			float v3_v = v3.getCurrentValue();
			float v4_v = v4.getCurrentValue();
			
			float real_v = (v1_v+v2_v+v3_v+v4_v)>=1?1:0;
			int real_i = (int)real_v;
			return (float)real_i;
		default:
			return Float.NaN;
		}
	}

	public float applyInverseFunction(Variable v) {
		return v.getCurrentValue();
	}  
	private Variable getVariable(int iddevice, String code) {
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select idvariable from cfvariable where iddevice=? and code=?",
					new Object[] { new Integer(iddevice), code });
			return ControllerMgr.getInstance().getFromField((Integer) rs.get(0).get(0));
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}
}
