package com.carel.supervisor.field.modbusfunmgrs;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;

public class GavFunctionMgr implements IFunctionMgr
{
    private static GavFunctionMgr me;
    private HashMap<Integer, Integer[]> varfun;
    
    private GavFunctionMgr() {
        varfun = new HashMap<Integer, Integer[]>();
        try {
            loadGavFun();
        }
        catch(Exception e){
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    public static GavFunctionMgr getInstance() 
    {
        if(me == null)
            me = new GavFunctionMgr();
        return me;
    }
    
    public float applyFunction(long value, Variable v) {
    	if(v.getInfo().getModel().equals(20310) || v.getInfo().getModel().equals(20282)){
    		try {
				return functionValue(v.getInfo().getModel(), value, 1, 1);
			} catch (Exception e) {
				e.printStackTrace();
				return Float.NaN;
			}
    	}
		if (!hasFunction(v.getInfo().getModel())) {
			return IdentityFunctionMgr.getInstance().applyFunction(value, v);
		}
		/* Little hammering */
		Integer varmdl = v.getInfo().getModel();
		if(varmdl.equals(20325) || varmdl.equals(20328) || varmdl.equals(20820) || varmdl.equals(20821)) {
			byte b = (byte) (value & 0x7f);
			byte s = (byte) ((value & 0x80)>>7);
//			value = (long) (b+100*s);
			value = (long) (b*Math.pow(-1, s));			
		}
		/* Little hammering */
		float cv = Float.NaN;
		float ct = Float.NaN;
		float vt = Float.NaN;
		int iddevice = v.getInfo().getDevice().intValue();
		try {
			// Get CT RATIO
			Variable var = getVariable(20282, iddevice);
			// ModbusMgr.getInstance().getModVariable(20282,iddevice);

			if (var != null) {
				ct = var.getCurrentValue();
			}

			// Get VT RATIO
			var = getVariable(20310, iddevice);
			if (var != null) {
				vt = var.getCurrentValue();
			}

			// Calc function value
			if (vt != Float.NaN && ct != Float.NaN) {
				cv = functionValue(v.getInfo().getModel(), (float)value, ct, vt);
//				v.setValue(new Float(cv));
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return cv;
	}

    private Variable getVariable(int i, int iddevice) {
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(
				null,
				"select idvariable from cfvariable where idvarmdl= ? and iddevice=? ",
				new Object[] { new Integer(i),new Integer(iddevice) });
			return ControllerMgr.getInstance().getFromField((Integer)rs.get(0).get(0));
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}

	public float applyInverseFunction(Variable v)
    {
		float val = v.getCurrentValue();
		Integer[] prms = varfun.get(v.getInfo().getModel());
		if(prms==null || !prms[0].equals(0))
			return val;
		val = (float) (val/Math.pow(10, prms[1]));
    	return val;
    }    

    public boolean hasFunction(int idvarmdl) {
        return this.varfun.containsKey(new Integer(idvarmdl));
    }
    
    public float functionValue(int idvarmdl,float value,float ct,float vt) throws Exception
    {
        Integer[] values = varfun.get(new Integer(idvarmdl));
        if(values != null && values.length == 2)
        {
            int typefun = values[0].intValue();
            switch(typefun)
            {
                case 1:
                    value = (value * vt);
                    break;
                case 2:
                    value = (value * ct);
                    break;
                case 3:
                    value = (value * ct * vt);
                    break;
            }
			int scal = values[1].intValue();
			value = (value * (float)Math.pow(10,scal));
        }
        return value;
    }
    
    public void loadGavFun() throws Exception
    {
        varfun.clear();
//        String sql = "select * from cfgavfun";
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
//        Record r = null;
//        for(int i=0; i<rs.size(); i++)
//        {
//            r = rs.get(i);
//            varfun.put((Integer)r.get("idvarmdl"), new Integer[]{(Integer)r.get("functype"),(Integer)r.get("unit")});
//        }
//        varfun.put(20229,new Integer[]{3,10});
        varfun.put(20275,new Integer[]{2,-3});
        varfun.put(20276,new Integer[]{2,-3});
        varfun.put(20277,new Integer[]{2,-3});
        varfun.put(20278,new Integer[]{2,-3});
        varfun.put(20279,new Integer[]{2,-3});
        varfun.put(20280,new Integer[]{2,-3});
        varfun.put(20293,new Integer[]{0,-2});
        varfun.put(20294,new Integer[]{0,-1});
        varfun.put(20295,new Integer[]{0,-1});
        varfun.put(20296,new Integer[]{1,-1});
        varfun.put(20297,new Integer[]{1,-1});
        varfun.put(20298,new Integer[]{1,-1});
        varfun.put(20299,new Integer[]{3,-4});
        varfun.put(20300,new Integer[]{3,-4});
        varfun.put(20301,new Integer[]{3,-4});
        varfun.put(20302,new Integer[]{3,-3});
        varfun.put(20303,new Integer[]{3,-4});
        varfun.put(20304,new Integer[]{3,-4});
        varfun.put(20305,new Integer[]{3,-4});
        varfun.put(20306,new Integer[]{3,-3});
        varfun.put(20307,new Integer[]{3,-3});
        varfun.put(20308,new Integer[]{3,-3});
        varfun.put(20309,new Integer[]{0,-2});
        varfun.put(20310,new Integer[]{0,-1});
        varfun.put(20313,new Integer[]{2,-3});
        varfun.put(20314,new Integer[]{1,0});
        varfun.put(20315,new Integer[]{3,-4});
        varfun.put(20316,new Integer[]{2,-3});
        varfun.put(20317,new Integer[]{1,0});
        varfun.put(20318,new Integer[]{3,-4});
        varfun.put(20319,new Integer[]{2,-3});
        varfun.put(20320,new Integer[]{1,0});
        varfun.put(20321,new Integer[]{3,-4});
        varfun.put(20322,new Integer[]{1,0});
        varfun.put(20323,new Integer[]{3,-3});
        varfun.put(20324,new Integer[]{0,-1});
        varfun.put(20325,new Integer[]{0,-2});
        varfun.put(20327,new Integer[]{0,-2});
        varfun.put(20328,new Integer[]{0,-2});
        varfun.put(20820,new Integer[]{0,-2});
        varfun.put(20821,new Integer[]{0,-2});
    }
    
}
