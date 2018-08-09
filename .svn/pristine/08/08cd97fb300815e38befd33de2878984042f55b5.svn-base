package com.carel.supervisor.plugin.algorithmpro.obj;


public class AlgoSleep 
{
	//public static AlgoSleep SECOND_1 	= new AlgoSleep(1000L);
	//public static AlgoSleep SECOND_5 	= new AlgoSleep(5000L);
	//public static AlgoSleep SECOND_10 	= new AlgoSleep(10000L);
	//public static AlgoSleep SECOND_15 	= new AlgoSleep(15000L);
	public static AlgoSleep SECOND_30 	= new AlgoSleep(30000L);
	
	public static AlgoSleep MINUTE_1 	= new AlgoSleep(60000L);
	public static AlgoSleep MINUTE_5 	= new AlgoSleep(300000L);
	public static AlgoSleep MINUTE_10 	= new AlgoSleep(600000L);
	public static AlgoSleep MINUTE_15 	= new AlgoSleep(900000L);
	public static AlgoSleep MINUTE_30 	= new AlgoSleep(1800000L);
	public static AlgoSleep MINUTE_60 	= new AlgoSleep(3600000L);
	
	private long sleepTime = 60000L;
	
	private AlgoSleep(long lTime) {
		this.sleepTime = lTime;
	}
	
	public long getTime() {
		return this.sleepTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sleepTime ^ (sleepTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object arg0) 
	{
		boolean ris = false;
		if(arg0 instanceof AlgoSleep)
		{
			if(((AlgoSleep)arg0).getTime() == this.getTime())
				ris = true;
		}
		return ris;
	}

	@Override
	public String toString() {
		return Long.toString((this.getTime()/1000L));
	}
}
