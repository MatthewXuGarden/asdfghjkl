package com.carel.supervisor.base.config;
import com.carel.supervisor.base.system.SanKitW;


public class Supervisor {
	private static Supervisor instance = null;

	// supervisor registers
	// SSD related registers
	public static final int PowerOn_Hours_Count				= 1;
	public static final int Spare_Blocks_Remaining			= 2;
	public static final int Total_LBAs_Written				= 3;
	public static final int ALARM_Spare_Blocks_Remaining	= 4;
	
	// emulate supervisor memory
	// all types of variables are stored on a single memory pool too keep model management simple
	private Float mem[] = null; 

	
	private Supervisor()
	{
	}
	
	
	public static Supervisor getInstance()
	{
		if( instance == null )
			instance = new Supervisor();
		return instance;
	}
	
	
	public synchronized void init(int nMemSize)
	{
        mem = new Float[nMemSize];
        // load persistent registers
        //checkSSD();
	}
	
	
	public synchronized boolean write(int i, Float value)
	{
		if( mem != null && i < mem.length ) {
			mem[i] = value;
			return true;
		}
		return false;
	}
	
	
	public synchronized Float read(int i)
	{
		if( mem != null && i < mem.length ) {
			return mem[i];
		}
		return null;
	}
	
	
	// check solid state drive
	public void checkSSD()
	{
        SanKitW.getInstance().execute();
        Integer value = SanKitW.getInstance().getSpareBlocksRemaining();
       	if( value != null ) {
       		write(Spare_Blocks_Remaining, new Float(value));
   			write(ALARM_Spare_Blocks_Remaining, value <= 5 ? 1f : 0f);
       	}
       	else {
       		write(Spare_Blocks_Remaining, null);
       		write(ALARM_Spare_Blocks_Remaining, 0f);
       	}
        value = SanKitW.getInstance().getPowerOnHoursCount();
       	write(PowerOn_Hours_Count, value != null ? new Float(value) : null);
        value = SanKitW.getInstance().getTotalLBAsWritten();
       	write(Total_LBAs_Written, value != null ? new Float(value) : null);
	}
}
