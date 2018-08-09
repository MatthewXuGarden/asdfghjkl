package com.carel.supervisor.script.special;

import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.device.DeviceStatusMgr;

/*
 * MasterCase - 21
 * 
 * 3924 - SONDA_7
 * 		3998 - ESISTE_SONDA.BAnd(2)
 * 
 * 3925 - SONDA_6
 * 		3998 - ESISTE_SONDA.BAnd(1)
 * 
 * 3945 - PST_SUPERHEAT
 * 		IF OFFLINE DEVICE 
 * 			LONGDESCRIPTION
 * 		ELSE
 * 			LONGDESCRIPTION + "/100"
 */
public class S21 extends Special 
{
	protected int SONDA_6 		= 3925;
	protected int SONDA_7 		= 3924;
	protected int ESISTE_SONDA 	= 3998;
	protected int PST_SUPERHEAT = 3945;
	
	public S21() {
		super();
	}
	
	public boolean display(SContext ctx,boolean initial)
	{
		boolean ris = initial;
		float value = 0;
		VarphyBean var = ctx.getVariable();
		
		if((var.getIdMdl().intValue()==SONDA_7) || (var.getIdMdl().intValue()==SONDA_6))
		{
			int varMdlInVar = ctx.decodeVarMdl(ESISTE_SONDA);
			
			int mask = 1;
			if(var.getIdMdl().intValue()==SONDA_7)
				mask = 2;
			
			try {
				value = ControllerMgr.getInstance().getFromField(varMdlInVar).getCurrentValue();
				ris = BitManipulation.andMask(value,mask);
			}
			catch(Exception e){
				ris = false;
			}
		}
		return ris;
	}
	
	public VarphyBean[] check(SContext ctx) 
	{
		VarphyBean var = ctx.getVariable();
		if(var.getIdMdl().intValue()==PST_SUPERHEAT)
		{
			if(!DeviceStatusMgr.getInstance().isOffLineDevice(new Integer(ctx.getIdDevice())))
				var.setShortDescription(var.getShortDescription()+"/100");
		}
		return new VarphyBean[]{var};
	}
}
