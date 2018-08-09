package com.carel.supervisor.script.special;

import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;

/*
 * uChiller2 - 40
 * 
 * 7112 - FAN_DEFROST_STATUS
 * 		Viene quadruplicata e visualizzata a video con i seguenti code:
 * 		- Def1
 * 		- FanDef1
 * 		- Def2
 * 		- FanDef2
 * 
 * 		Def1:
 * 			7206 - DEFROST_ENABLE = 1
 * 				AND
 * 			7108 - FIRM_RELEASE >= 16
 * 			
 * 			Diplay a video AND(1)
 * 		
 * 		FanDef1:
 * 			7206 - DEFROST_ENABLE = 1
 * 				AND
 * 			7108 - FIRM_RELEASE >= 16
 * 			
 * 			Diplay a video AND(4)
 *		
 *		Def2:
 * 			7206 - DEFROST_ENABLE = 1
 * 				AND
 * 			7108 - FIRM_RELEASE >= 16
 * 				AND
 * 			7151 - LAN_CONFIGURATION > 1
 * 			
 * 			Diplay a video AND(2)
 * 		
 * 		FanDef2:
 * 			7206 - DEFROST_ENABLE = 1
 * 				AND
 * 			7108 - FIRM_RELEASE >= 16
 * 				AND
 * 			7151 - LAN_CONFIGURATION > 1
 * 			
 * 			Diplay a video AND(8)
 */
public class S40 extends Special
{
	protected final static String CD1 = "Def1";
	protected final static String CD2 = "FanDef1";
	protected final static String CD3 = "Def2";
	protected final static String CD4 = "FanDef2";
	
	protected int FAN_DEFROST_STATUS = 7112;
	protected int DEFROST_ENABLE = 7206;
	protected int FIRM_RELEASE = 7108;
	protected int LAN_CONFIGURATION = 7151;
	
	public S40() {
		super();
	}
	
	public boolean display(SContext ctx,boolean initial)
	{
		boolean ris = initial;
		float value = 0;
		int varMdlInVar = -1;
		VarphyBean var = ctx.getVariable();
		String varcode = "";
		
		if(var.getIdMdl().intValue() == FAN_DEFROST_STATUS)
		{
			varcode = var.getShortDescription();
			if(varcode != null)
			{
				if(varcode.equalsIgnoreCase(CD3) || varcode.equalsIgnoreCase(CD4))
				{
					varMdlInVar = ctx.decodeVarMdl(DEFROST_ENABLE);
				}
			}
		}
		
		return ris;
	}
	
	public VarphyBean[] check(SContext ctx) 
	{
		VarphyBean var = ctx.getVariable();
		VarphyBean[] adder = null;
		
		if(var.getIdMdl().intValue()==FAN_DEFROST_STATUS)
		{
			int idx = 0;
			adder = new VarphyBean[4];
			
			var.setShortDescription(CD1);
			adder[idx++] = var;
			
			adder[idx] = (new VarphyBean(var));
			adder[idx++].setShortDescription(CD2);
			
			adder[idx] = (new VarphyBean(var));
			adder[idx++].setShortDescription(CD3);
			
			adder[idx] = (new VarphyBean(var));
			adder[idx].setShortDescription(CD4);
		}
		else
			adder = new VarphyBean[]{var};
		
		return adder;
	}
	
	public String value(SContext ctx) 
	{
		String ret = "";
		VarphyBean var = ctx.getVariable();
		//int varMdlInVar = -1;
		int mask = 1;
		float value = 0;
		boolean ris = false;
		
		if(var.getIdMdl().intValue() == FAN_DEFROST_STATUS)
		{
			if(var.getShortDescription().equalsIgnoreCase(CD1))
				mask = 1;
			else if(var.getShortDescription().equalsIgnoreCase(CD2))
				mask = 4;
			else if(var.getShortDescription().equalsIgnoreCase(CD3))
				mask = 2;
			else if(var.getShortDescription().equalsIgnoreCase(CD4))
				mask = 8;
			
			//varMdlInVar = ctx.decodeVarMdl(FAN_DEFROST_STATUS);
			try {
				//by Kevin, FAN_DEFROST_STATUS is not in cffield's expression, so varMdlInVar is -1
				value = ControllerMgr.getInstance().getFromField(var).getCurrentValue();
				ris = BitManipulation.andMask(value,mask);
			}
			catch(Exception e){
				ris = false;
			}
			
			if(ris)
				ret = "ON";
			else
				ret = "OFF";
		}
		
		return ret;
	}
}
