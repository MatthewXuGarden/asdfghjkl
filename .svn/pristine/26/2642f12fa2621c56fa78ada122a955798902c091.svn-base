package com.carel.supervisor.presentation.bo.helper;

public class PortInfo {

	public native String[] listPortNames();
	public native int[] listPortNumbers();
	
	public String getDetectString(int list[])
	{
	    StringBuffer sb = new StringBuffer();
	    for(int i = 0; i < list.length; i++) {
	        if( i != 0 )
	        	sb.append(',');
	  	    sb.append(list[i]);
	  	}
	  	return sb.toString();
	}
	
	public int getPort(String detect)
	{
		int port = 0;
		int list[] = listPortNumbers();
		String adetect[] = detect.split(",");
		for(int i = 0; i < list.length; i++) {
			boolean b = true;
			for(int j = 0; j < adetect.length; j++) {
				if( list[i] == Integer.parseInt(adetect[j]) ) {
					b = false;
					break;
				}
			}
			if( b ) {
				port = list[i];
				break;
			}
		}
		return port;
	}
	
	static
	{
		System.loadLibrary("PortInfo");
	}
}
