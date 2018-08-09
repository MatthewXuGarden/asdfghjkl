package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;

public class Nemod4 implements IFunctionMgr {

	private static Nemod4 me;
	private HashMap<Integer, Integer[]> varfun;

	private Nemod4() {
		varfun = new HashMap<Integer, Integer[]>();
		try {
			load();
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	private void load() {
		varfun = new HashMap<Integer, Integer[]>();
		varfun.clear();
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'VTR(KTV)'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P1PV'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P2PV'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P3PV'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P1C'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P2C'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'P3C'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'ntcurr'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3PAP'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3PRP'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3PApP'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3ppae'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'CVL1-L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'CVL2-L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'CVL3-L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3Pppae'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'freq'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3PPF'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}		
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3Ppre'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3Psoap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3Psorp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3Pap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= '3ppmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1ap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2ap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3ap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1soap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2soap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3soap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1rp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2rp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3rp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1sorp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2sorp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3sorp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1ac'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2ac'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3ac'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p1cmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p2cmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemod4') and code= 'p3cmd'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0,-3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public static Nemod4 getInstance() {
		if (me == null)
			me = new Nemod4();
		return me;
	}

	public float applyFunction(long value, Variable v) {
		Variable kta = null;
		Variable ktv = null;
		Integer idvarmdl = v.getInfo().getModel();
		Integer[] prms = varfun.get(idvarmdl);
		if(prms==null)
			return value;
		switch (prms[0]) {
		case 0:
			return (float) (value * Math.pow(10, prms[1]));
		case 3:
			kta = getVariable(v.getInfo().getDevice(), "CTR(KTA)");
			ktv = getVariable(v.getInfo().getDevice(), "VTR(KTV)");
			if (kta != null && ktv != null
					&& kta.getCurrentValue() * ktv.getCurrentValue() < 6000) {
				return (float) ((value / 100f)*Math.pow(10, prms[1]));
			} else {
				return (float) (value * Math.pow(10, prms[1]));
			}
			// break;
		case 4:
			kta = getVariable(v.getInfo().getDevice(), "CTR(KTA)");
			ktv = getVariable(v.getInfo().getDevice(), "VTR(KTV)");
			float multiplier = kta.getCurrentValue() * ktv.getCurrentValue();
			if (multiplier >= 1000) {
				return (float) ((value*10f)*Math.pow(10, prms[1]));
			} else if (multiplier >= 100) {
				return (float) (value*Math.pow(10, prms[1]));
			} else if (multiplier >= 10) {
				return (float) ((value/10f)*Math.pow(10, prms[1]));
			} else if (multiplier >= 1) {
				return (float) ((value/100f)*Math.pow(10, prms[1]));
			} else {
				return Float.NaN;
			}
			// break;
		case 5: // sign
			if (value == 0) {
				return 1;
			} else if (value == 1) {
				return -1;
			} else {
				return Float.NaN;
			}
			// break;
		default:
			return Float.NaN;
			// break;
		}
	}

	public float applyInverseFunction(Variable v) {
		float retval = v.getCurrentValue();
		int addressout = v.getInfo().getAddressOut();
		switch (addressout) {
		case 4608:
			return retval;
		case 4610:
			return retval*10;
		default: 
			return retval;
		}
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
