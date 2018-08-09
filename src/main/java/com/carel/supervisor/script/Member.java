package com.carel.supervisor.script;

import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.script.comparator.IComparator;

public class Member 
{
	private IComparator comparator = null;
	private boolean error = false;
	private float costant  = 0;
	private int idvarmdl   = 0;
	private int idvariable = 0;
	
	public Member(String member)
	{
		String operator = "";
		int idxOper = -1;
		
		if(member != null)
		{
			if((idxOper = member.indexOf(">")) != -1)
			{
				operator = ">";
				if((member.indexOf("=")) != -1)
					operator = ">=";
			}
			else if((idxOper = member.indexOf("<")) != -1)
			{
				operator = "<";
				if((member.indexOf("=")) != -1)
					operator = "<=";
			}
			else if((idxOper = member.indexOf("=")) != -1)
				operator = "=";
			else if((idxOper = member.indexOf("!")) != -1)
				operator = "!";
			else
			{
				this.error = true;
				try {
					this.idvarmdl = Integer.parseInt(member);
				}
				catch(Exception e){
					this.idvarmdl = -1;
				}
				this.idvariable = -1;
				this.comparator = null;
			}
			
			if(!this.error)
				getNumeric(member,idxOper,operator);
		}
	}
	
	public boolean isValid() {
		return !this.error;
	}
	
	public int getIdVarmdl() {
		return idvarmdl;
	}
	
	public void retrieveIdVariable(Map varMap) {
		try {
			idvariable = ((Integer)varMap.get(new Integer(idvarmdl))).intValue();
		}
		catch(Exception e){error = true;}
	}
	
	public boolean evaluate()
	{
		boolean ris = false;
		
		if(isValid())
		{
			float fieldValue = 0;
			try {
				fieldValue = (ControllerMgr.getInstance().getFromField(this.idvariable)).getCurrentValue();
				ris = comparator.compare(fieldValue,costant);
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
			
		}
		
		return ris; 
	}
	
	private void getNumeric(String exp,int idx,String compa)
	{
		String n1 = "";
		String n2 = "";
		
		try {
			n1 = exp.substring(0,idx);
			n2 = exp.substring(idx+compa.length());
			this.idvarmdl = Integer.parseInt(n1);
			this.costant  = Float.parseFloat(n2);
			this.comparator = FactoryComparator.createComparator(compa);
		}
		catch(Exception e) {error = true;}
	}
}
