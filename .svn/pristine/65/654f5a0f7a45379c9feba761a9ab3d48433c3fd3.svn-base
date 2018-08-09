package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.db.Record;

public class GraphBean {

		
	public void setColor(String color) {
		this.color = color;
	}
	public void setYMin(Float min) {
		yMin = min;
	}
	public void setYMax(Float max) {
		yMax = max;
	}

	private static final String ID_DEVICE = "iddevice";
	private static final String ID_VARIABLE = "idvariable";
	private static final String DEVICE_DESCRIPTION = "devicedescription";
	private static final String VARIABLE_DESCRIPTION = "variabledescription";
	private static final String VARIABLE_TYPE = "type";
	private static final String MEASURE_UNIT = "measureunit";
	public static final String COLOR = "color";
	public static final String Y_MIN = "ymin";
	public static final String YMAX = "ymax";
	
	private int idDevice = -1;
    private String devDescription = null;
    private int idVariable = -1;
    private String varDescription = null;
    private int variableType = -1;
    private String unitOfmeasure=null;
    private String color=null;
    private Float yMin=null;
    private Float yMax=null;
    
    //add bye Kevin. for device import/export
    private boolean isHaccp = false;
    private String code = null;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public GraphBean()
    {
    	
    }
	public GraphBean(Record record) {
		if(record.hasColumn(ID_DEVICE))
    		this.idDevice = ((Integer) record.get(ID_DEVICE)).intValue();
    	
		if(record.hasColumn(DEVICE_DESCRIPTION))
		{
			/*
			this.devDescription = Replacer.replace((String) record.get(DEVICE_DESCRIPTION), ";", " "); //separatore lista parametri
			this.devDescription = Replacer.replace(this.devDescription.trim(), "<", ""); //char non accettato dal flash
			this.devDescription = Replacer.replace(this.devDescription.trim(), "\"", ""); //char non accettato dal flash
			*/
			this.devDescription = StringUtility.clrBadGfxChars((String) record.get(DEVICE_DESCRIPTION));
			this.devDescription = Replacer.replace(devDescription, "&deg", "°");
		}
    	
		if(record.hasColumn(ID_VARIABLE))
    		this.idVariable = ((Integer) record.get(ID_VARIABLE)).intValue();
    	
    	if(record.hasColumn(VARIABLE_DESCRIPTION))
    	{
    		/*
    		this.varDescription = Replacer.replace((String) record.get(VARIABLE_DESCRIPTION), ";", " "); //separatore lista parametri
    		this.varDescription = Replacer.replace(this.varDescription.trim(), "<", ""); //char non accettato dal flash
    		this.varDescription = Replacer.replace(this.varDescription.trim(), "\"", ""); //char non accettato dal flash
    		*/
    		this.varDescription = StringUtility.clrBadGfxChars((String) record.get(VARIABLE_DESCRIPTION));
    		this.varDescription = Replacer.replace(varDescription, "&deg", "°");
    		
    	}
    	
    	if(record.hasColumn(VARIABLE_TYPE))
    		this.variableType =((Integer) record.get(VARIABLE_TYPE)).intValue();
    	
    	if(record.hasColumn(MEASURE_UNIT))
    	{
    		/*
    		this.unitOfmeasure = Replacer.replace((String) record.get(MEASURE_UNIT), ";", " "); //separatore lista parametri
    		this.unitOfmeasure = Replacer.replace(this.unitOfmeasure, "<", ""); //char non accettato dal flash
    		this.unitOfmeasure = Replacer.replace(this.unitOfmeasure, "\"", ""); //char non accettato dal flash
    		*/
    		this.unitOfmeasure = StringUtility.clrBadGfxChars((String) record.get(MEASURE_UNIT));
    		this.unitOfmeasure = Replacer.replace(unitOfmeasure, "&deg", "°");
    	}
    	
    	if(record.hasColumn(COLOR))
    		this.color = (String) record.get(COLOR);
    	if(record.hasColumn(Y_MIN))
    		this.yMin = ((Float) record.get(Y_MIN));
    	if(record.hasColumn(YMAX))
    		this.yMax = ((Float) record.get(YMAX));
    	
	}//ChartBean

	public String getDeviceDescription() {
		return devDescription;
	}//getDescription

	public int getIdDevice() {
		return idDevice;
	}//getIddevice
	
	public String getVariableDescription() {
		return varDescription;
	}//getDescription

	public int getIdVariable() {
		return idVariable;
	}//getIddevice

	public boolean isHaccp() {
		return isHaccp;
	}

	public void setHaccp(boolean isHaccp) {
		this.isHaccp = isHaccp;
	}

	public int getVariableType(){
		return variableType;
	}//getVariableType

	public String getUnitOfmeasure() {
		return unitOfmeasure;
	}//getUnitOfmeasure

	public String getColor() {
		return color;
	}//getColor
	
	public Float getYMax() {
		return yMax;
	}//getYMax

	public Float getYMin() {
		return yMin;
	}//getYMin
	
}//Class ChartBean
