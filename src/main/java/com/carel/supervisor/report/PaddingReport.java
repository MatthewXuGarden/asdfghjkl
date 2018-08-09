package com.carel.supervisor.report;

import java.sql.Timestamp;

public class PaddingReport {
	
	private Float [][]ySerie=null;
	private long []timeSerie=null;
	private Timestamp startData=null;
	private long samplingPeriod=0;
	private YReportCoordinates [][]yCoordinates=null; //ixj i numero di serie j numero di punti per serie
	private ReportRequest reportRequest=null;
	private int numSeries=0;
	private int numPoint=0;
	
	public PaddingReport(ReportRequest reportRequest){
		this.reportRequest=reportRequest;
	}
	//PaddingReport
	
	public void startPadding(){
		yCoordinates=reportRequest.getReportYsSeries();
		if((yCoordinates==null)||(yCoordinates[0]==null)||(yCoordinates.length==0)||(yCoordinates[0].length==0))
			return;//new throw Exception("");
		ReportInformation information= reportRequest.getReportInformation();
		startData=information.getStartTime();
		samplingPeriod=information.getSamplingPeriodRequest();
		
		numSeries=yCoordinates.length;
		numPoint=yCoordinates[0].length;
		ySerie=new Float[numSeries][numPoint];
		timeSerie=new long[numPoint];
		
		// Find
		Float paddingValue=null;
		
		for(int i=0;i<numSeries;i++){
			paddingValue=null;
			for (int j = 0;(j < numPoint);j++)
		  		{
		  		if (yCoordinates[i][j] != null)
			  		{
			  			ySerie[i][j]=yCoordinates[i][j].getValue();
			  			paddingValue=ySerie[i][j]; 	
			  		} //if
		  		else
		  			ySerie[i][j]=paddingValue;
		  		} //for		  		
		} //for numSeries
		//creo la griglia dei tempi
		timeSerie[0]=startData.getTime();
		for(int i=1;i<numPoint;i++)
			timeSerie[i]=timeSerie[i-1]+samplingPeriod;
	}//startPadding
	
	public long[] getTimeGrid(){
		return timeSerie;
	}//getTimeGrid
	
	public Float[][] getAllSeries(){
		return ySerie;
	}//getAllSeries
	
	public Float[] getSerie(int index){
		return ySerie[index];
	}//getSerie
	
	public String toString(){
		StringBuffer str=new StringBuffer();
		for(int i=0;i<numSeries;i++){
			str.append("\n\nSERIE #");
			str.append(i+1);
			str.append("\n");
			for(int j=0;j<numPoint;j++){
				str.append("Point  = ");
				str.append(j);
				str.append(" Time = ");
				str.append(new Timestamp(timeSerie[j]));
				str.append(" Value = ");
				str.append(ySerie[i][j]!=null?ySerie[i][j].toString():"No Value");
				str.append("\n");
				}//for
		}//for
		return str.toString();
	}//toString
}//PaddingReport
