package com.carel.supervisor.director.graph;

import java.util.ArrayList;

public class SpecialPoints {
	
	public final static int SX_OUT_POINT=0;
	public final static int CENTER_POINT=1;
	public final static int DX_OUT_POINT=2;
	
	public final static int ACTUAL_NUMBER_OF_SPECIAL_POINT=3;
	
	
	private ArrayList values =  new ArrayList();
	private boolean firstPointOfRecord=false;
	//svuota l'eventuale ricerca del non buco perchè il buco adesso è sicuro
	
	
	private Long indexPadd=null;

	public SpecialPoints(){
	}//YGraphCoordinates
	
	public void setYsGraphCoordinates(Float y,boolean empty){
		if(empty)
			values= new ArrayList();
		values.add(y);
	}//setYsGraphCoordinates
	
	
	public void setCentralPoint(long index,boolean first){
		if(indexPadd==null){
			firstPointOfRecord=first;
			indexPadd= new Long(index);
		}//if
		else{
			if(indexPadd.longValue()>=index){
				firstPointOfRecord=first;
				indexPadd= new Long(index);
			}//if
		
				
		}//else
	}//setCentralPoint
	
	
	public boolean isFirstPointOfRecord(){
		return  firstPointOfRecord;
	}//setYsGraphCoordinates

	public Float getLastValueNotNull() {
		
		for(int i=values.size()-1;i>=0;i--){
			if(values.get(i)!=null)
				return (Float)values.get(i);
		}
		return null;
	}//getLastValueNotNull
	

	   
}//Class YGraphCoordinates
