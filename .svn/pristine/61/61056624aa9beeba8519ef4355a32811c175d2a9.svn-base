package com.carel.supervisor.director.graph;

public class YGraphCoordinates {
	private Float maxValue = null;
	private Float minValue = null;
	private Float pointToPadd=null;

	private Long indexPadd=null;


	public final static int SX_OUT_POINT=0;
	public final static int DX_OUT_POINT=1;
	
	
	public YGraphCoordinates(){
	}//YGraphCoordinates
	
	public YGraphCoordinates(Float y,Long indexPadd){
		setYsGraphCoordinates(y,indexPadd);
	}//YGraphCoordinates
	
	public void setYsGraphCoordinates(Float y,Long indexPadd){
		
		if(y!=null){
			if(maxValue!=null){
				maxValue=maxValue.floatValue()<y.floatValue()?new Float(y.floatValue()):maxValue;
				minValue=minValue.floatValue()>y.floatValue()?new Float(y.floatValue()):minValue;
			}//if
			else{
				maxValue=new Float(y.floatValue()); 
				minValue=new Float(y.floatValue());
			}//else
		}//if
		
		if(this.indexPadd==null){
			this.indexPadd=indexPadd;
			pointToPadd=y;
		}//if
		else{
			if(this.indexPadd.longValue()<=indexPadd.longValue())
				this.indexPadd=indexPadd;
				pointToPadd=y;
		}//else

	}//setYsGraphCoordinates
	


	public Long getIndexPadd(){
		return this.indexPadd;
	}
	
	public String toString()
	{
		StringBuffer tmp = new StringBuffer();
	    tmp.append(" Y=[");
	    tmp.append(minValue);
	    tmp.append(";");
	    tmp.append(maxValue);
	    tmp.append("]");
	    return tmp.toString();
	} //toString
	   
	 public Float getMinValue(){
		   return minValue;
	   }//getMinValue
	   
	   public Float getMaxValue(){
		   return maxValue;
	   }//getMaxValue

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}//setMaxValue

	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}//setMinValue
	
	public Float getPointToPadd(){
		return pointToPadd;
	}// getPointToPadd
	
	public void setPointToPadd(Float paddingValue){
		this.pointToPadd=paddingValue;
	}// getPointToPadd
	   
	public void setSpecialPointsValue(Float y,Long index,int type){
		switch(type){
			case SX_OUT_POINT:
				if(this.indexPadd==null){
					this.indexPadd=index;
					maxValue=minValue=y;
				}//if
				else{
					if(this.indexPadd.longValue()<=index.longValue())
						this.indexPadd=index;
					maxValue=minValue=pointToPadd=y;
				}//else
			break;
			case DX_OUT_POINT:
				if(this.indexPadd==null){
					this.indexPadd=index;
					maxValue=minValue=y;
				}//if
				else{
					if(this.indexPadd.longValue()>=index.longValue())
						this.indexPadd=index;
						maxValue=minValue=pointToPadd=y;
				}//else
			break;
		}//switch
	}//setSpecialPointsValue

	   
}//Class YGraphCoordinates
