package com.carel.supervisor.director.graph;

public class GraphConstant {
	
	public final static String TYPE_HACCP="haccp";
	public final static String TYPE_HISTORICAL="historical";
	
    public final static int MONTH = 0;
    public final static int WEEK = 1;
    public final static int DAY = 2;
    public final static int HOURS12 = 3;
    public final static int HOURS6 = 4;
    public final static int HOUR = 5;
    public final static int MINUTS30 = 6;
    public final static int MINUTS15 = 7;
    public final static int MINUTS5 = 8;
    public final static int MINUT = 9;


    //mese,settimana.giorno,12 ore IN milliSECONDI
    public final static long[] PERIOD = new long[]
        {
            35 * 24 * 60 * 60 * 1000L/*2592000000L*/, 604800000L, 86400000L, 43200000L, 21600000L, 3600000L,
            1800000L, 900000L, 300000L, 60000L
        };
	public static final int NUM_X_POINT = 0;
   
	public static final  String[] colors = 
    {
		"#FF0000", "#0000FF", "#00FF00", "#FF6600", "#FFFF00", "#FF00FF",
		"#00FFFF", "#CC99FF", "#339966", "#FFFF99", "#325693", "#FFFF80", 
		"#FF8080", "#80FF80", "#00FF80", "#80FFFF", "#0080FF", "#FF80C0", 
		"#C0FF00", "#00FF40", "#0080C0", "#8080C0", "#325693", "#804040",
		"#FF8040", "#FFFFFF", "#000000", "#EAEAEA", "#BBBBBB", "#000080" 
    };
	
	
	
    //Constant PRESENTATION i valori dei colori sotto DEVONO essere presenti nel vettore sopra colors
	public static final String V_FINDER_BG_COLOR="000000";
	public static final String V_FINDER_FG_COLOR="FFFFFF";
	public static final String GRID_COLOR="000000";
	public static final String GRAPH_BG_COLOR="FFFFFF";
	public static final String AXIS_COLOR="000000";
	
	
	//public final static String TIME_PERIOD="timeperiod";
	private static int incrementalCountColor=0;
	  public static synchronized String createCiclicColor(){
	    	String color=GraphConstant.colors[incrementalCountColor].substring(1);
			incrementalCountColor++;
			incrementalCountColor%=GraphConstant.colors.length;
			return color;
		}//createCiclicColor
	  
	  public static final String[]tableNames= new String[]{"hsvarhaccp","hsvarhistor"};
}//GraphConstant
