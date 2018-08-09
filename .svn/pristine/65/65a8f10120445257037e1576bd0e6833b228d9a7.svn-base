package com.carel.supervisor.plugin.algorithmpro;

import java.sql.Timestamp;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.plugin.algorithmpro.obj.IAlgorithmPro;
import com.carel.supervisor.plugin.algorithmpro.util.AlgoPro;

public class AlgorithmThread implements Runnable 
{
	private IAlgorithmPro algoObj = null;
	
	private boolean work = false;
	private boolean reset = false;
	private boolean saveState = true;
	
	private String sClass = "";
	private String sName = "";
	private String sDesc = "";
	
	public AlgorithmThread(IAlgorithmPro obj,String sCl,String sNm,String sDs)
	{
		this.algoObj = obj;
		this.work = true;
		this.reset = false;
		saveState = true;
		this.sClass = sCl;
		this.sName = sNm;
		this.sDesc = sDs;
	}
	
	public void stopWork(boolean saveState) 
	{
		this.algoObj.setWork(false);
		this.work = false;
		this.saveState = saveState;
	}
	
	public void run() 
	{
		// Call startup method
		this.work = this.algoObj.startup();
		
		this.algoObj.markStartTime();
		
		while(this.work)
		{
			try
			{
				// Execute main customer logic
				this.algoObj.execute();
				
				// Check if it has to continue work
				if(this.work)
					this.work = this.algoObj.getWork();
			}
			catch(Throwable e) {
				e.printStackTrace();
			}
			
			if(this.work)
			{
				try {
					Thread.sleep(this.algoObj.getSleep());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// Call shutdown method
		this.algoObj.shutdown();
		
		this.algoObj.markStopTime();
		
		// Force
		this.work = false;
		this.reset = true;
		
		// In case of user stop, it remove state from database
		if(this.saveState)
		{
			try {
				ProductInfoMgr.getInstance().getProductInfo().set(AlgoPro.PFX_SYSCONF + sName, "FALSE");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getStateInfo1() 
	{
		String ret = "---";
		ret = this.algoObj.getStateInfo1();
		if(ret == null || ret.trim().equals(""))
			ret = "---";
		return ret;
	}
	
	public String getStateInfo2() 
	{
		String ret = "---";
		ret = this.algoObj.getStateInfo2();
		if(ret == null || ret.trim().equals(""))
			ret = "---";
		return ret;
	}
	
	public String getStateInfo3() 
	{
		String ret = "---";
		ret = this.algoObj.getStateInfo3();
		if(ret == null || ret.trim().equals(""))
			ret = "---";
		return ret;
	}
	
	public String getStateInfo4() 
	{
		String ret = "---";
		ret = this.algoObj.getStateInfo4();
		if(ret == null || ret.trim().equals(""))
			ret = "---";
		return ret;
	}
	
	public String getStateInfo5() 
	{
		String ret = "---";
		ret = this.algoObj.getStateInfo5();
		if(ret == null || ret.trim().equals(""))
			ret = "---";
		return ret;
	}
	
	public String getStartTime()
	{
		String ret = "---";
		Timestamp t = this.algoObj.getStartTime();
		if(t != null)
			ret = t.toString();
		return ret;
	}
	
	public String getStopTime()
	{
		String ret = "---";
		Timestamp t = this.algoObj.getStopTime();
		if(t != null)
			ret = t.toString();
		return ret;
	}
	
	public String getsClass() {
		return sClass;
	}

	public String getsName() {
		return sName;
	}

	public String getsDesc() {
		return sDesc;
	}
	
	/**
	 *	1 -> Running;
	 *	2 -> Pending (Ask to stop but it is still in execute method);
	 *	0 -> Stop; 
	 */
	public String getStatus() 
	{
		if((this.work))
			return "1";
		else
		{
			if((this.reset))
				return "0";
			else
				return "2";
		}
	}

	public boolean isWork() {
		return work;
	}

	public boolean isReset() {
		return reset;
	}
	
	public void forceReset() {
		this.reset = true;
	}
}
