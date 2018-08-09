package com.carel.supervisor.director;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainThreadMonitor 
{
	private static MainThreadMonitor mtm = new MainThreadMonitor();
	
	// RecoveryData First Time
	public static String RDBEGIN = "RECDATABEGIN";
	// RecoveryData Main Loop
	public static String RDLOOP = "RECDATALOOP";
	// Ping All Devices
	public static String PINGALL = "PINGALL";
	// Retryve and Save Variables
	public static String RETSAVEFIELD = "RETSAVE";
	// Check alarm by GP
	public static String GPCHKVAR = "GPCHKVAR";
	// Eval alarm and event rule
	public static String EVLALR = "EVLALR";
	// Eval schedule action
	public static String EVLSCHD = "EVLSCHD";
	// Dequeue
	public static String DEQUEUE = "DEQUEUE";
	
	private Map<String,Long> timer = null;
	private long startTime = 0L;
	private long loopTime = 0L;
	private String current = "";
	private List<String> acts = null;
	public boolean isRunning = false;
	
	
	private MainThreadMonitor()
	{
		timer = new HashMap<String, Long>();
		acts = new ArrayList<String>();
	}
	
	public static MainThreadMonitor getInstance() {
		return mtm;
	}
	
	public void setLoopTime() {
		this.loopTime = System.currentTimeMillis();
	}
	
	public long getLoopTime() {
		return System.currentTimeMillis() - this.loopTime;
	}
	
	public void start(String code) 
	{
		this.current = code;
		this.startTime = System.currentTimeMillis();
		if(!this.acts.contains(code))
			this.acts.add(code);
	}
	
	public void stop(String code)
	{
		long now = System.currentTimeMillis();
		this.timer.put(code, new Long(now-this.startTime));
	}
	
	public String getCurrent() {
		return this.current;
	}
	
	public int getActivitySize() {
		return this.acts.size();
	}
	
	public String getActivityCode(int idx) {
		return this.acts.get(idx);
	}
	
	public long getActivityTime(String code) {
		return ((Long)this.timer.get(code)).longValue();
	}
}
