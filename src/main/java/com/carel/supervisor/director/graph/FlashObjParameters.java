package com.carel.supervisor.director.graph;

public class FlashObjParameters {
	
	// default values
	public  int HEIGHT=295; //altezza dell'oggetto flash
	public  int WIDTH=730;  //larghezza dell'oggetto flash
	
	public final int W1=25;     //Larghezza grafo=WIDTH-W1

	private int numDigitalSeries=0;
	private int numAnalogicSeries=0;
	
	private FlashObjParameters(int ScreenHeight,int ScreenWidth){
	    // new settings
		HEIGHT = ScreenHeight; //473 orig.
	    WIDTH = ScreenWidth; //294 orig.
	}

    public  FlashObjParameters(int numDigitalSeries,int numAnalogicSeries,int ScreenHeight,int ScreenWidth){
    	this(ScreenHeight,ScreenWidth);
    	this.numDigitalSeries=numDigitalSeries;
		this.numAnalogicSeries=numAnalogicSeries;
	}
    
    public  FlashObjParameters(int numDigitalSeries,int numAnalogicSeries,int ScreenHeight,int ScreenWidth,int offsetHeight,int offsetWidth){
    	this(ScreenHeight-offsetHeight,ScreenWidth-offsetWidth);
    	this.numDigitalSeries=numDigitalSeries;
		this.numAnalogicSeries=numAnalogicSeries;
	}    

	public int getXWidth(){
		return WIDTH-2*W1;
	}//getXWidth

	public int getNumDigitalSeries() {
		return numDigitalSeries;
	}//getNumDigitalSeries
	
	public int getNumAnalogicSeries() {
		return numAnalogicSeries;
	}//getAnalogicSeries

	public int getHEIGHT() {
		return HEIGHT;
	}//getHEIGHT

	public void setHEIGHT(int height) {
		HEIGHT = height;
	}//setHEIGHT

	public int getWIDTH() {
		return WIDTH;
	}//getWIDTH

	public void setWIDTH(int width) {
		WIDTH = width;
	}//setWIDTH
	
}
