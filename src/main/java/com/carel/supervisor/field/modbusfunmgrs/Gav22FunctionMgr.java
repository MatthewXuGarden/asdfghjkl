package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;

public class Gav22FunctionMgr implements IFunctionMgr {
	private static final String V_INFO = "Vunit";
	private static final String A_INFO = "Aunit";
	private static final String P_INFO = "Punit";
	private static final String[] INFOVAR = {V_INFO, A_INFO, P_INFO};
	private static Gav22FunctionMgr me;
	private HashMap<Integer, Integer[]> varfun;
	private HashMap<Integer,HashMap<String,Integer>> helpermap;

	private Gav22FunctionMgr() {
		varfun = new HashMap<Integer, Integer[]>();
		try {
			load();
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	private void load() {
		varfun = new HashMap<Integer, Integer[]>();
		varfun.clear();
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'V_L1-N'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{1, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'V_L2-N'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{1, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'V_L3-N'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{1, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'A_L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{2, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'A_L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{2, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'A_L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{2, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'W_L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'W_L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'W_L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'var_L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'var_L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'var_L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'VA_L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'VA_L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'VA_L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'PF_L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'PF_L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'PF_L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'V_sys'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{1, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'W_sys'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'var_sys'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'VA_sys'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'PF_sys'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'VA_dmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'W_dmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'Hz'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'KWh_tot'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'KVarh_tot'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'KWh_par'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'KVarh_par'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'vt_ratio'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Gavazzi_WM22') and code= 'pt_ratio'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public static Gav22FunctionMgr getInstance() {
		if (me == null)
			me = new Gav22FunctionMgr();
		return me;
	}

	public float applyFunction(long value, Variable v) {
		Integer varmdl = v.getInfo().getModel();
		Integer[] params = varfun.get(varmdl);
		if(params!=null){
			switch (params[0]) {
			case 0:
				return (float) (value*Math.pow(10, params[1]));
			case 1:
			case 2:
			case 3:
				try {
					int power = (int) getVariable(v.getInfo().getDevice(), INFOVAR[params[0]-1]).getCurrentValue() -6;
					if(power>=-3 && power<=6){ //(*unit between 3 and 12)
						return value * (float) Math.pow(10, power);
					} else {
						return Float.NaN;
					}
				} catch (Exception e) {
					return Float.NaN;
				}
			case 4:
				return (float) (value*Math.pow(10, params[1]));
			default:
				return Float.NaN;
			}
		}
		return value;
	}

	private Variable getVariable(Integer device, String varcode) {
		if (helpermap == null) {
			helpermap = new HashMap<Integer, HashMap<String, Integer>>();
		}
		HashMap<String, Integer> devmap = helpermap.get(device);
		if(devmap==null){
			devmap = new HashMap<String, Integer>();
			helpermap.put(device, devmap);
		}
		Integer var = devmap.get(varcode);
		if(var==null){
			RecordSet rs;
			try {
				rs = DatabaseMgr.getInstance().executeQuery(null, "select idvariable from cfvariable where iddevice=? and code=?",
						new Object[]{device,varcode});
				var = (Integer) rs.get(0).get(0);
				devmap.put(varcode, var);
			} catch (DataBaseException e) {
			}
		}
		try {
			return ControllerMgr.getInstance().getFromField(var);
		} catch (Exception e) {
		}
		return null;
	}

	public float applyInverseFunction(Variable v) {
		float value = v.getCurrentValue();
		Integer varmdl = v.getInfo().getModel();
		Integer[] params = varfun.get(varmdl);
		if(params!=null){
			switch (params[0]) {
			case 0:
				return (float) (value/Math.pow(10, params[1]));
			case 1:
				try {
					switch ( (int) getVariable(v.getInfo().getDevice(), "V_info").getCurrentValue()) {
						case 3:
							return value*1000f;
						case 4:
						case 7:
							return value*100f;
						case 5:
							return value*10f;
						case 6:
							return value; 
						default:
							return Float.NaN;
					}
				} catch (Exception e) {
					return Float.NaN;
				}
			case 2:
				return (float) (value/Math.pow(10, params[1]));
			case 3:
				return (float) (value/Math.pow(10, params[1]));
			case 4:
				return (float) (value/Math.pow(10, params[1]));
			default:
				return Float.NaN;
			}
		}
		return v.getCurrentValue();
	}
}
