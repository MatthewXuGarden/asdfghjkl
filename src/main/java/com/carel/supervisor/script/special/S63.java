package com.carel.supervisor.script.special;

/*
 * MasterCase_HS_x5 - 63
 * 
 * 11078 - SONDA_7
 * 		11152 - ESISTE_SONDA.BAnd(2)
 * 
 * 11079 - SONDA_6
 * 		11152 - ESISTE_SONDA.BAnd(1)
 * 
 * 11099 - PST_SUPERHEAT
 * 		IF OFFLINE DEVICE 
 * 			LONGDESCRIPTION
 * 		ELSE
 * 			LONGDESCRIPTION + "/100"
 */
public class S63 extends S21 
{
	public S63() {
		super();
		this.SONDA_6 		= 11078;
		this.SONDA_7 		= 11079;
		this.ESISTE_SONDA 	= 11152;
		this.PST_SUPERHEAT 	= 11099;
	}
}
