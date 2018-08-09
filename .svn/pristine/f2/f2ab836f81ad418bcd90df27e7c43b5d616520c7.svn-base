package com.carel.supervisor.plugin.algorithmpro.obj;

import java.sql.Timestamp;


public interface IAlgorithmPro 
{
	/**
	 * Called once on starting thread  
	 */
	public boolean startup();
	
	/**
	 * Called once on stopping thread
	 */
	public boolean shutdown();
	
	/**
	 * Called during life cycle thread
	 */
	public void execute();
	
	public void setName(String name);
	public String getName();
	public void setWork(boolean work);
	public boolean getWork();
	public long getSleep();
	
	public void markStartTime();
	public void markStopTime();
	public Timestamp getStartTime();
	public Timestamp getStopTime();
	
	public void setStateInfo1(String value);
	public void setStateInfo2(String value);
	public void setStateInfo3(String value);
	public void setStateInfo4(String value);
	public void setStateInfo5(String value);
	
	public String getStateInfo1();
	public String getStateInfo2();
	public String getStateInfo3();
	public String getStateInfo4();
	public String getStateInfo5();
}
