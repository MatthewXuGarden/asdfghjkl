package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class FunctionBean 
{
	private int idFunction = 0;
	private String pvcode = "";
	private int idSite = 0;
	private int funCode = 0;
	private String operator = "";
	private String parameters = "";
	private int order = 0;
	
	public FunctionBean(Record r)
	{
		try {
			this.idFunction = ((Integer)r.get("idfunction")).intValue();
			this.pvcode = UtilBean.trim(r.get("pvcode"));
			this.idSite = ((Integer)r.get("idsite")).intValue();
			this.funCode = ((Integer)r.get("functioncode")).intValue();
			this.operator = UtilBean.trim(r.get("opertype"));
			this.parameters = UtilBean.trim(r.get("parameters"));
			this.order = ((Integer)r.get("operorder")).intValue();
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	public int getFunCode() {
		return funCode;
	}

	public int getIdFunction() {
		return idFunction;
	}

	public int getIdSite() {
		return idSite;
	}

	public String getOperator() {
		return operator;
	}

	public int getOrder() {
		return order;
	}

	public String getParameters() {
		return parameters;
	}

	public String getPvcode() {
		return pvcode;
	}
	
	public Object[] decodeParamField()
	{
		String id1 = "";
		String id2 = "";
		
		int iIdVar1 = 0;
		float iIdVar2 = 0;
		int iType1 = 0;
		int iType2 = 0;
				
		if(this.parameters != null)
		{
			String[] arval = this.parameters.split(";");
			if(arval != null && arval.length == 2)
			{
				id1 = arval[0];
				id2 = arval[1];
				
				if(id1 != null && id1.startsWith("pk")) {
					try {
						iIdVar1 = Integer.parseInt(id1.substring(2));
						iType1 = 1;
					} catch (NumberFormatException e) {
						iType1 = 0;
					}
				}
				
				if(id2 != null && id2.startsWith("pk")) {
					iType2 = 1;
					id2 = id2.substring(2);
				}
					
				try {
					iIdVar2 = Float.parseFloat(id2);
				} catch (NumberFormatException e) {
					iType2 = 0;
				}
			}
		}
		return new Object[]{iIdVar1,iType1,iIdVar2,iType2};
	}
}
