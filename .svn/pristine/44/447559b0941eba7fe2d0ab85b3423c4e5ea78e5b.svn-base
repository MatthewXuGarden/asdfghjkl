package com.carel.supervisor.plugin.energy;
import java.util.GregorianCalendar;


public class EPTimePeriod {
	private GregorianCalendar calBegin = null;
	private GregorianCalendar calEnd = null;
	
	private int aaiTimeSlots[][];
	
	
	public EPTimePeriod()
	{
		// init time slots table
		aaiTimeSlots = new int[7][]; // 7 week days
		for(int i = 0; i < 7; i++) {
			aaiTimeSlots[i] = new int[24]; // 24 hours
			for(int j = 0; j < 24; j++)
				aaiTimeSlots[i][j] = 0; // init with index of the 1st time slot
		}
	}

	
	public GregorianCalendar getBeginPeriod()						{ return calBegin; }
	public void setBeginPeriod(GregorianCalendar calBegin)			{ this.calBegin = calBegin; }
	public GregorianCalendar getEndPeriod()							{ return calEnd; }
	public void setEndPeriod(GregorianCalendar calEnd)				{ this.calEnd = calEnd; }
	
	public boolean isTimePeriod(GregorianCalendar cal)
	{
		if( calBegin == null || calEnd == null )
			return false;
		
		return calBegin.compareTo(cal) <= 0 && calEnd.compareTo(cal) >= 0; 
	}
	
	
	public int getTimeSlot(GregorianCalendar cal)
	{
		int nWeekDay = cal.get(GregorianCalendar.DAY_OF_WEEK);
		if( nWeekDay == 1 )
			nWeekDay = 6; // Sunday
		else
			nWeekDay -= 2; // Monday has ndx 0 on aaiTimeSlots table
		int nHour = cal.get(GregorianCalendar.HOUR_OF_DAY);
		return getTimeSlot(nWeekDay, nHour); 
	}
	public int getTimeSlot(int nWeekDay, int nHour)					{ return aaiTimeSlots[nWeekDay][nHour]; }
	public void setTimeSlot(int nWeekDay, int nHour, int iTimeSlot)	{ aaiTimeSlots[nWeekDay][nHour] = iTimeSlot; }
	
	
	public String getTimePeriodJS()
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("\t{\n");
		if( calBegin != null && calEnd != null ) {
			strbuff.append("\tmonthBegin:" + calBegin.get(GregorianCalendar.MONTH) + ", dayBegin:" + calBegin.get(GregorianCalendar.DAY_OF_MONTH));
			strbuff.append(", monthEnd:" + calEnd.get(GregorianCalendar.MONTH) + ", dayEnd:" + calEnd.get(GregorianCalendar.DAY_OF_MONTH) + ",\n");
		}
		strbuff.append("\taaiTimeSlots:new Array(\n");
		for(int i = 0; i < aaiTimeSlots.length; i++) {
			if( i > 0 )
				strbuff.append(",\n");
			strbuff.append("\t\tnew Array(");
			for(int j = 0; j < aaiTimeSlots[i].length; j++) {
				if( j > 0 )
					strbuff.append(",");
				strbuff.append("" + aaiTimeSlots[i][j]);
			}
			strbuff.append(")");
		}
		strbuff.append(")\n\t}\n");
		return strbuff.toString();
	}
}
