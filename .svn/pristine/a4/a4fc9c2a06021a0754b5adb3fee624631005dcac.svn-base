package com.carel.supervisor.presentation.graph;

public class UnitMeasurementConstant {
	
	private final static String SIMBOL_PER="%";
	private final static String SIMBOL_PERC="% - &degC";
	private final static String SIMBOL_PERCF="% - &degC/&degF";
	private final static String SIMBOL_PERSCF="%/&degC/&degF";
	private final static String SIMBOL_PERRH="%RH";
	private final static String SIMBOL_PERRHBAR="%RH/bar";
	private final static String SIMBOL_PERFS="%fs";
	private final static String SIMBOL_PERrH="%rH";
	private final static String SIMBOL_PERRHPER="%rH - %";
	private final static String SIMBOL_PERRHC="%rH - &degC";
	private final static String SIMBOL_PERRHCF="%rH - &degC/&degF";
	
	
	private final static String SIMBOL_C="&degC";
	private final static String SIMBOL_CBAR="&degC / bar";
	private final static String SIMBOL_C10="&degC x 10";
	private final static String SIMBOL_CF="&degC/&degF";
	private final static String SIMBOL_CFBAR="&degC/&degF bar";
	private final static String SIMBOL_CFSBAR="&degC/&degF/bar";
	private final static String SIMBOL_CSF="&degC/F";
	private final static String SIMBOL_CSFSBAR="&degC/F/Bar";
	private final static String SIMBOL_CbAR="&degC/bar";
	private final static String SIMBOL_CMIN="&degC/min";
	private final static String SIMBOL_CBARG="&degC:barg";
	private final static String SIMBOL_F="&degF";
	private final static String SIMBOL_K="&degK";
	private final static String SIMBOL_MICRO="&micro;S/cm";
	
	private final static String SIMBOL_SD="/d";
	private final static String SIMBOL_10MIN="10 min.";
	private final static String SIMBOL_10SEC="10 sec";
	private final static String SIMBOL_100HOURS="100 hours";
	
	private final static String A="A";
	private final static String BaR="Bar";
	private final static String HoUR="Hour";
	private final static String HoUR10="Hour*10";
	private final static String HoURS="Hours";
	private final static String HZ="Hz";
	private final static String K="K";
	private final static String KCALKG="Kcal/Kg";
	private final static String MW="MW";
	private final static String MWH="MWh";
	private final static String MiN="Min";
	private final static String NUM="Num";
	private final static String PA="Pa";
	private final static String RCF="R &degC/&degF";
	private final static String RPM="RPM/30";
	private final static String SeC="Sec";
	private final static String StEP="Step/;%";
	private final static String V="V";
	private final static String V100="V/100"; 
    private final static String VAC="V/A/&degC";
	private final static String VOLT="Volt";
	private final static String YeAR="Year";
	
	private final static String BAR="bar";
	
	private final static String BARX10="bar x 10";
	private final static String BARC="bar/&degC";
	
	
	private final static String BAR10="bar/10";
	private final static String BAR100="bar/100";
	private final static String BARAT="bar/@";
	private final static String BARPSI="bar/PSI";
	private final static String BARG="barg";

	private final static String D="d";
	private final static String DAY="day";
	private final static String DAYS="days";

	private final static String FLAG="flag";
	private final static String FLAGS="flags";
	
	private final static String GG="gg";
	
	private final static String H="h";
	private final static String HX1000="h (x1000)";
	private final static String HX10="h x10";
	private final static String HOUR="hour";
	private final static String HOUR10="hour*10";
	private final static String HOURMIN="hour/min";
	private final static String HOURS="hours";
	
	private final static String IMPWH="imp/Wh";

	private final static String KVA="kVA";
	private final static String KW="kW";
	private final static String KWH="kWh";
	private final static String KGH="kg/h";
	private final static String KGHLBSHR="kg/h - lbs/hr";
	private final static String KVAR="kvar";
	private final static String KVARH="kvarh";

	private final static String LUX="lux";
	
	private final static String M="m";
	private final static String MA="mA";
	private final static String MV="mV";
	private final static String MH="mh";
	private final static String MIN="min";
	private final static String MINP="min.";
	private final static String MINUTES="minutes";
	private final static String MONTH="month";
	private final static String MS="ms";
	private final static String MSEC="msec";

	private final static String OHM="ohm";
	private final static String ORE="ore";
	
	private final static String PPM="ppM";
	private final static String PPm="ppm";
	
	private final static String S="s";
	private final static String SAMPLES="samples";
	private final static String SEC="sec";
	private final static String SEC02="sec-0.2";
	private final static String SECP="sec.";
	private final static String SEC10="sec/10";
	private final static String SECONDS="seconds";
	private final static String SEC100="secx100";
	private final static String STEP="step";
	private final static String STEPS="steps";
	
	private final static String USCM="uS/cm";
	
	private final static String VAR="var";
	
	private final static String Y="y";
	private final static String YEAR="year";
	private final static String YEARS="years";
	
	
	//Attenzione è corretto &deg and &micro senza ; 
	
	public static synchronized Object [] getScale(String unitMeasure){

		Object []objects= new Object[2];
		objects[0]=null;
		objects[1]=null;
		
		// ENHANCEMENT 20090202 - code refactoring to improve performances  
		// 1) if the unit of measure is null, method ends immediately.
		// 2) some units of measurement are more common than others (a DB query has been performed to get those infos).
		//    It makes sense to put the most common units the upper part of "OR" chains in order to improve performances
		if (unitMeasure != null && unitMeasure.trim().length() > 0) {
	
			if(unitMeasure.equals(SIMBOL_CF) || // moved up - enhancement 20090202
					unitMeasure.equals(SIMBOL_C) ||    
					unitMeasure.equals(SIMBOL_C10) ||
					unitMeasure.equals(SIMBOL_CSF) ||
					unitMeasure.equals(SIMBOL_CFBAR) ||
					unitMeasure.equals(SIMBOL_CFSBAR) ||
					unitMeasure.equals(SIMBOL_CBAR) ||					
					unitMeasure.equals(SIMBOL_CSFSBAR) ||
					unitMeasure.equals(SIMBOL_CbAR) ||
					unitMeasure.equals(SIMBOL_CMIN) ||
					unitMeasure.equals(SIMBOL_CBARG) ||
					unitMeasure.equals(SIMBOL_F) ||
					unitMeasure.equals(SIMBOL_K) ||
					unitMeasure.equals(K) ||
					unitMeasure.equals(RCF)){
				objects[0]=new Float(-75);
				objects[1]=new Float(100);
			}//if
			else			
 			if(unitMeasure.equals(SIMBOL_PER) ||
			   unitMeasure.equals(SIMBOL_PERC) ||    
			   unitMeasure.equals(SIMBOL_PERCF) ||    
			   unitMeasure.equals(SIMBOL_PERSCF) ||    
			   unitMeasure.equals(SIMBOL_PERRH) ||    
			   unitMeasure.equals(SIMBOL_PERRHBAR) ||    
			   unitMeasure.equals(SIMBOL_PERFS) ||    
			   unitMeasure.equals(SIMBOL_PERrH) ||    
			   unitMeasure.equals(SIMBOL_PERRHPER) ||    
			   unitMeasure.equals(SIMBOL_PERRHC) ||  
			   unitMeasure.equals(StEP) ||
			   unitMeasure.equals(SIMBOL_PERRHCF)){
					objects[0]=new Float(0);
					objects[1]=new Float(100);
			}//if
			else	
			if(unitMeasure.equals(MIN)  ||
			   unitMeasure.equals(HOUR)	||
			   unitMeasure.equals(DAY)	||
			   unitMeasure.equals(SEC)	||
			   unitMeasure.equals(MONTH)||
					
			   unitMeasure.equals(SIMBOL_MICRO) ||
			   unitMeasure.equals(SIMBOL_SD) ||
	 		   unitMeasure.equals(SIMBOL_10MIN) ||
	 		   unitMeasure.equals(SIMBOL_10SEC) ||
	 		   unitMeasure.equals(SIMBOL_100HOURS) ||
	 		   unitMeasure.equals(A) ||
	 		   unitMeasure.equals(BaR) ||
	 		   unitMeasure.equals(HoUR) ||
	 		   unitMeasure.equals(HoUR10) ||
	 		   unitMeasure.equals(HoURS) ||
	 		   unitMeasure.equals(HZ) ||
	 		   unitMeasure.equals(KCALKG) ||
	 		   unitMeasure.equals(MW) ||
	 		   unitMeasure.equals(MWH) ||
	 		   unitMeasure.equals(MiN) ||
	 		   unitMeasure.equals(NUM) ||
	 		   unitMeasure.equals(PA) ||
	 		   unitMeasure.equals(RPM) ||
	 		   unitMeasure.equals(SeC) ||
	 		   unitMeasure.equals(V) ||
	 		   unitMeasure.equals(V100) ||
	 		   unitMeasure.equals(VAC) ||
	 		   unitMeasure.equals(VOLT) ||
	 		   unitMeasure.equals(YeAR) ||
	 		   unitMeasure.equals(BAR) ||
	 		   unitMeasure.equals(BARX10) ||
	 		   unitMeasure.equals(BARC) ||
	 		   unitMeasure.equals(BAR10) ||
	 		   unitMeasure.equals(BAR100) ||
	 		   unitMeasure.equals(BARAT) ||
	 		   unitMeasure.equals(BARPSI) ||
	 		   unitMeasure.equals(BARG) ||
	 		   unitMeasure.equals(D) ||
	 		   //unitMeasure.equals(DAY) || moved up - enhancement 20090202
	 		   unitMeasure.equals(DAYS) ||
	 		   unitMeasure.equals(GG) ||
	 		   unitMeasure.equals(H) ||
	 		   unitMeasure.equals(HX1000) ||
	 		   unitMeasure.equals(HX10) ||
	 		   //unitMeasure.equals(HOUR) || moved up - enhancement 20090202
	 		   unitMeasure.equals(HOUR10) ||
	 		   unitMeasure.equals(HOURMIN) ||
	 		   unitMeasure.equals(HOURS) ||
	 		   unitMeasure.equals(IMPWH) ||
	 		   unitMeasure.equals(KVA) ||
	 		   unitMeasure.equals(KW) ||
	 		   unitMeasure.equals(KWH) ||
	 		   unitMeasure.equals(KGH) ||
	 		   unitMeasure.equals(KGHLBSHR) ||
	 		   unitMeasure.equals(KVAR) ||
	 		   unitMeasure.equals(KVARH) ||
	 		   unitMeasure.equals(LUX) ||
	 		   unitMeasure.equals(M) ||
	 		   unitMeasure.equals(MA) ||
	 		   unitMeasure.equals(MV) ||
	 		   unitMeasure.equals(MH) ||
	 		   //unitMeasure.equals(MIN) || moved up - enhancement 20090202
	 		   unitMeasure.equals(MINP) ||
	 		   unitMeasure.equals(MINUTES) ||
	 		   //unitMeasure.equals(MONTH) || moved up - enhancement 20090202
	 		   unitMeasure.equals(MS) ||
	 		   unitMeasure.equals(MSEC) ||
	 		   unitMeasure.equals(OHM) ||
	 		   unitMeasure.equals(ORE) ||
	 		   unitMeasure.equals(PPM) ||
	 		   unitMeasure.equals(PPm) ||
	 		   unitMeasure.equals(S) ||
	 		   unitMeasure.equals(SAMPLES) ||
	 		   // unitMeasure.equals(SEC) || moved up - enhancement 20090202
	 		   unitMeasure.equals(SEC02) ||
	 		   unitMeasure.equals(SECP) ||
	 		   unitMeasure.equals(SEC10) ||
	 		   unitMeasure.equals(SECONDS) ||
	 		   unitMeasure.equals(SEC100) ||
	 		   unitMeasure.equals(USCM) ||
	 		   unitMeasure.equals(VAR) ||
	 		   unitMeasure.equals(Y) ||
	 		   unitMeasure.equals(YEAR) ||
	 		   unitMeasure.equals(YEARS)){
					objects[0]=new Float(0);
					objects[1]=new Float(50);
					
		   	  }//if
			else
			if(unitMeasure.equals(STEP) ||
					unitMeasure.equals(STEPS)){
				objects[0]=new Float(-750);
				objects[1]=new Float(500);
			}//if
		
		}
		
		return objects;
	}//getScale
}

















