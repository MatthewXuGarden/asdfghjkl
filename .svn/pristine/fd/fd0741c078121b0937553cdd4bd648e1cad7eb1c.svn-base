package com.carel.supervisor.plugin.energy;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class EGUtils {
	public static final int HPOS = 10000; // posizione ore
	public static final int MPOS = 100; // posizione minuti
	private static final int LASTTIME = 240000;

	private static DecimalFormat kwformat = new DecimalFormat();
	private static DecimalFormat kwhformat = new DecimalFormat();
	private static DecimalFormat costformat = new DecimalFormat();
	private static DecimalFormat percformat = new DecimalFormat();
	private static DecimalFormat kgco2format = new DecimalFormat();

	static{
		kwformat.setMinimumFractionDigits(1);
		kwformat.setMaximumFractionDigits(1);
		kwhformat.setMinimumFractionDigits(0);
		kwhformat.setMaximumFractionDigits(0);
		costformat.setMinimumFractionDigits(0);
		costformat.setMaximumFractionDigits(2);
		percformat.setMinimumFractionDigits(1);
		percformat.setMaximumFractionDigits(1);
		kgco2format.setMinimumFractionDigits(0);
		kgco2format.setMaximumFractionDigits(0);
	}

	public static String combo24Hours(Integer selectedhour) {
		StringBuffer orario = new StringBuffer();
		String selected = "";
		int time = -1;
		if (selectedhour != null)
			time = selectedhour.intValue();
		if (time == -1)
			selected = "selected";
		orario.append("<option " + selected + " value='-1'> --- </option>\n");
		for (int i = 0; i < 24; i++) {
			if (time == (i * HPOS))
				selected = "selected";
			else
				selected = "";
			orario.append("<option " + selected + " value='" + (i * HPOS)
					+ "'>" + (i < 10 ? "0" : "") + i + ".00</option>\n");
			if (time == (i * HPOS + 30 * MPOS))
				selected = "selected";
			else
				selected = "";
			orario.append("<option " + selected + " value='"
					+ (i * HPOS + 30 * MPOS) + "'>" + (i < 10 ? "0" : "") + i
					+ ".30</option>\n");
		}
		if (time == LASTTIME)
			selected = "selected";
		else
			selected = "";
		orario.append("<option " + selected + " value='" + (LASTTIME)
				+ "'>24.00</option>\n");
		return orario.toString();
	}
	
	public static String formatkw(Float num){
		return kwformat.format(num);
	}

	public static String formatkwh(Float num){
		return kwhformat.format(num);
	}

	public static String formatkwh(Double num){
		return kwhformat.format(num);
	}
	
	public static String formatperc(Float num){
		return percformat.format(num);
	}
	
	public static String formatperc(Double num){
		return percformat.format(num);
	}

	public static String formatcost(Float num){
		return costformat.format(num);
	}

	public static String formatcost(Double num){
		return costformat.format(num);
	}
	
	public static String formatkgco2(Float num){
		return kgco2format.format(num);
	}

	public static void setMidnight(GregorianCalendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}
	
	
	public static String getTimeTableHtmlRow(EnergyProfile ep, int iRow)
	{
		StringBuffer strbuff = new StringBuffer();
		
		for(int iCol = 0; iCol < 24; iCol++) {
			if( iRow != 7 )
				strbuff.append("<th><select id=\"slot_" + iRow + "_" + iCol + "\" onChange=\"onSlotChanged(" + iRow + "," + iCol + ", this.value)\">");
			else
				strbuff.append("<th><select id=\"slot_col_" + iCol + "\" onChange=\"onSlotColChanged(" + iCol + ", this.value)\"><option value='-1'>--</option>");
			for(int iSlot = 0; iSlot < EnergyProfile.TIMESLOT_NO; iSlot++) {
				strbuff.append("<option value='" + iSlot + "'>");
				strbuff.append(ep.getTimeSlot(iSlot).getName());
				strbuff.append("</option>");
			}
			strbuff.append("</select></th>");
		}
		if( iRow != 7 )
			strbuff.append("<td></td><th><select id=\"slot_row_" + iRow + "\" onChange=\"onSlotRowChanged(" + iRow + ", this.value)\"><option value='-1'>--</option>");
		else
			strbuff.append("<td></td><th><select id=\"slot_table\" onChange=\"onSlotTableChanged(this.value)\"><option value='-1'>--</option>");			
		for(int iSlot = 0; iSlot < EnergyProfile.TIMESLOT_NO; iSlot++) {
			strbuff.append("<option value='" + iSlot + "'>");
			strbuff.append(ep.getTimeSlot(iSlot).getName());
			strbuff.append("</option>");
		}
		strbuff.append("</select></th>");
		return strbuff.toString();
	}
}
