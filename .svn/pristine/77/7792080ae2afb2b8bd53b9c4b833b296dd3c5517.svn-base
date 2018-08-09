package com.carel.supervisor.report;

public class YReportCoordinates {
	private Float value = null;
	private Long oldTime=null;
	
	public final static int SX_OUT_POINT=0;
	
	public YReportCoordinates(){
	}//YReportCoordinates
	
	public YReportCoordinates(Float y,long actualTime){
		setYsReportCoordinates(y,actualTime);
	}//YReportCoordinates
	
	public void setYsReportCoordinates(Float y,long actualTime){
		oldTime= new Long(actualTime);
		value=y;
	}//setYsReportCoordinates
	
	public void setSpecialPointsValue(Float y,Long index,int type){
		switch(type){
			case SX_OUT_POINT:
				oldTime= new Long(index);
				value=y;
			break;		
		}//switch
	}//setSpecialPointsValue

	public String toString()
	{
		StringBuffer tmp = new StringBuffer();
	    tmp.append(" Y=[");
	    tmp.append(value);
	    tmp.append("]");
	    return tmp.toString();
	} //toString
	   
	   public Float getValue(){
		   return value;
	   }//getValue
	          
}//Class YGraphCoordinates
