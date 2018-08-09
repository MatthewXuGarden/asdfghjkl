package com.carel.supervisor.script.special;

import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;

public class S25 extends Special
{
	public static int S1 	= 4512;
	public static int S2 	= 4513;
	public static int S3 	= 4514;
	public static int S4 	= 4515;
	public static int DL5 	= 4554;
	public static int DL6 	= 4555;
	public static int H1 	= 4520;
	public static int L1 	= 4521;
	public static int H2 	= 4527;
	public static int L2 	= 4528;
	public static int H3 	= 4534;
	public static int L3 	= 4535;
	public static int H4 	= 4543;
	public static int L4 	= 4544;
	public static int MOD 	= 4516;
	public static int SONDE = 4518;
	
	public S25() {
		super();
	}
	
	public boolean display(SContext ctx,boolean initial)
	{
		boolean ris = initial;
		
		float value1 = 0;
		float value2 = 0;
		
		VarphyBean var = ctx.getVariable();
		
		value1 = getCurrentValue(ctx.decodeVarMdl(SONDE));
		value2 = getCurrentValue(ctx.decodeVarMdl(MOD));
		
		if((var.getIdMdl().intValue() == S1) || (var.getIdMdl().intValue() == H1) || (var.getIdMdl().intValue() == L1))
		{
			ris = BitManipulation.andMask(value1,1);
			ris = (ris && (!BitManipulation.andMask(value2,1)));
		}
		else if(var.getIdMdl().intValue() == S2 || (var.getIdMdl().intValue() == H2) || (var.getIdMdl().intValue() == L2))
		{
			ris = BitManipulation.andMask(value1,2);
			ris = (ris && (!BitManipulation.andMask(value2,1)));
		}
		else if(var.getIdMdl().intValue() == S3 || (var.getIdMdl().intValue() == H3) || (var.getIdMdl().intValue() == L3))
		{
			ris = BitManipulation.andMask(value1,4);
		}
		else if(var.getIdMdl().intValue() == S4 || (var.getIdMdl().intValue() == H4) || (var.getIdMdl().intValue() == L4))
		{
			ris = BitManipulation.andMask(value1,8);
		}
		else if((var.getIdMdl().intValue() == DL5) || (var.getIdMdl().intValue() == DL6))
		{
			ris = BitManipulation.andMask(value1,1);
		}
		
		return ris;
	}
		
	private float getCurrentValue(int idVariable)
	{
		float ret = Float.NaN;
		try {
			ret = ControllerMgr.getInstance().getFromField(idVariable).getCurrentValue();
		}
		catch(Exception e){}
		return ret;
	}
}
