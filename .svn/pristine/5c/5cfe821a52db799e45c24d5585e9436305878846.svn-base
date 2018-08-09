package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;

public class Nemo96HD implements IFunctionMgr {

	private static Nemo96HD me;
	private HashMap<Integer, Integer[]> varfun;

	private Nemo96HD() {
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
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'votrra(KTV)'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1pv'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2pv'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3pv'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1c'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2c'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
				"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3c'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3prp'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3ppap'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3ppae'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'cv:_L1-L2'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'cv:_L2-L3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'cv:_L3-L1'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pnae'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'Frequency'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -1});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3ppf'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pporeen'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3psiofacpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pnereen'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3psiofrepo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pavpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3ppemade'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'Neutral_current'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1acpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2acpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3acpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1siofacpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2siofacpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3siofacpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1repo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2repo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3repo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1siofrepo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2siofrepo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3siofrepo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{5, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1appo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2appo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3appo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1pofa'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2pofa'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3pofa'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -2});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1I1av'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2I2av'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3I3av'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1I1pemax'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2I2pemax'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3I3pemax'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'avI1I2I3'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1V1min'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2V2min'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3V3min'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p1V1max'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p2V2max'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= 'p3V3max'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{0, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pacpaen'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3prepaen'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{4, 0});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pacavpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3preavpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3papavpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3pacPMDpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3prePMDpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
			"select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code='Nemo96HD') and code= '3papPMDpo'");
			if(rs!=null && rs.size()>0){
				varfun.put((Integer) rs.get(0).get(0), new Integer[]{3, -3});
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public static Nemo96HD getInstance() {
		if (me == null)
			me = new Nemo96HD();
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
			return (float) (value*Math.pow(10, prms[1]));
		case 1:
			return (float) (value*Math.pow(10, prms[1]));
		case 2:
			return (float) (value*Math.pow(10, prms[1]));
		case 3:
			kta = getVariable(v.getInfo().getDevice(), "cutrra(KTA)");
			ktv = getVariable(v.getInfo().getDevice(), "votrra(KTV)");
			if (kta != null && ktv != null
					&& kta.getCurrentValue() * ktv.getCurrentValue() < 5000) {
				return (float) ((value/100f)*Math.pow(10, prms[1]));
			}else{
				return (float) (value*Math.pow(10, prms[1]));
			}
//			break;
		case 4:
			kta = getVariable(v.getInfo().getDevice(), "cutrra(KTA)");
			ktv = getVariable(v.getInfo().getDevice(), "votrra(KTV)");
			float multiplier = kta.getCurrentValue()*ktv.getCurrentValue();
			if(multiplier>=10000){
				return (float) ((value*100f)*Math.pow(10, prms[1]));
			}else if(multiplier>=1000){
				return (float) ((value*10f)*Math.pow(10, prms[1]));
			}else if(multiplier>=100){
				return (float) (value*Math.pow(10, prms[1]));
			}else if(multiplier>=10){
				return (float) ((value/10f)*Math.pow(10, prms[1]));
			}else if(multiplier>=1){
				return (float) ((value/100f)*Math.pow(10, prms[1]));
			}else{
				return Float.NaN;
			}
//			break;
		case 5: //sign
			if (value == 0) {
				return 1;
			} else if (value == 1) {
				return -1;
			} else {
				return Float.NaN;
			}
//			break;
		default:
			return Float.NaN;
//			break;
		}
	}

	public float applyInverseFunction(Variable v) {
		float retval = v.getCurrentValue();
		int addressout = v.getInfo().getAddressOut();
		switch (addressout) {
		case 600258:
			return retval*10;
//			break;
		case 609216:
		case 609728:
		case 609984:
		case 610240:
			return retval;
//			break;
		default:
			return retval;
//			break;
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
