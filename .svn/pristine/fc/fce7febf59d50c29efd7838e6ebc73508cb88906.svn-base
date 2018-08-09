package com.carel.supervisor.ide.dc.DbModel;


/**
 * @author pvpro team 20090422
 */

public class VariableDb
{
	private int idVarModel; //NOT USED
	private int idSite;		
	private int idDevModel;
	private String code;
	private int type;
	private int addressIn;
	private int addressOut;
	private int varDimension;
	private int varLength;
	private int bitPosition;
	private boolean signed;
	private int decimal;
	private String toDisplay;
	private String buttonPath;
	private int priority;
	private int readWrite;
	private String minValue;
	private String maxValue;
	private String defaultValue;
	private String measureUnit;
	private int idVarGroup; 
	private String imageOn;
	private String imageOff;
	private int frequency;
	private int delta;
	private int delay;
	private boolean isHaccp;
	private boolean isActive; 
	private boolean isRelay;
	private int grpCategory;
	private int hsTime;
	private int hsFrequency;
	private float hsDelta;
	private String categoryCustomCode;
	private boolean isCommand;
	
	
	
	private void fillInformations(int idSite, int idDevModel, String code, int type, int addressIn, int addressOut, 
		int varDimension, int varLength, int bitPosition, boolean signed, int decimal, String toDisplay,
		String buttonPath, int priority, int readWrite, String minValue,  String maxValue,
		String defaultValue, String measureUnit, int idVarGroup, String imageOn, String imageOff,
		int frequency, int delta, int delay, boolean isHaccp, boolean isActive, boolean isRelay,
		int grpCategory, int hsTime, int hsFrequency, float hsDelta, String caregoryCustomCode, boolean isCommand)
	{
		this.idSite = idSite;	
		this.idDevModel = idDevModel;
		this.code = code;
		this.type =type;
		this.addressIn = addressIn;
		this.addressOut = addressOut;
		this.varDimension = varDimension;
		this.varLength = varLength;
		this.bitPosition = bitPosition;
		this.signed = signed;
		this.decimal = decimal;
		this.toDisplay = toDisplay;
		this.buttonPath = buttonPath;
		this.priority = priority;
		this.readWrite = readWrite;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.defaultValue = defaultValue;
		this.measureUnit = measureUnit;
		this.idVarGroup = idVarGroup;
		this.imageOn = imageOn;
		this.imageOff = imageOff;
		this.frequency = frequency;
		this.delta = delta;
		this.delay = delay;
		this.isHaccp = isHaccp;
		this.isActive = isActive;
		this.isRelay = isRelay;
		this.grpCategory = grpCategory; 
		this.hsTime = hsTime;
		this.hsFrequency = hsFrequency;
		this.hsDelta = hsDelta;
		this.categoryCustomCode = caregoryCustomCode;
		this.isCommand = isCommand;
	}

public void fillInformations(int idSite, int idDevModel, String code, int type, int addressIn, int addressOut, 
					int varDimension, int varLength, int bitPosition, boolean signed, int decimal, String toDisplay,
					int priority, int readWrite, String minValue,  String maxValue,
					String defaultValue, String measureUnit, int category, String imageOn, String imageOff,
					boolean isHaccp, boolean isRelay, int hsTime, int hsFrequency, float hsDelta,String categoryCustomCode,
					boolean isCommand)
					{
						int frequency = 0;
						int delta = -1;
						int delay = 0;
						if(isHaccp)
						{
							frequency = 900;
						}
	
						if(type == 4)
						{
							frequency = 30;
							delta = 0;
							
							//change category in case of alarm type and category = "general" (unknown from xml unmarshal)
							if(category == 1)
								category = 55;
								
							fillInformations(idSite, idDevModel, code, type, addressIn, addressOut, varDimension, varLength,
									bitPosition, signed, decimal, toDisplay, "", priority, readWrite, minValue,
									maxValue, defaultValue, measureUnit, 8, imageOn, imageOff, frequency, delta, delay,
									isHaccp, true, isRelay, category, hsTime, hsFrequency, hsDelta,categoryCustomCode, isCommand);
						}
						
						else
							
						fillInformations(idSite, idDevModel, code, type, addressIn, addressOut, varDimension, varLength,
							bitPosition, signed, decimal, toDisplay, "", priority, readWrite, minValue,
							maxValue, defaultValue, measureUnit, category, imageOn, imageOff, frequency, delta, delay,
							isHaccp, true, isRelay, category, hsTime, hsFrequency, hsDelta,categoryCustomCode, isCommand);
					}



	public int getIdVarModel() {
		return idVarModel;
	}



	public void setIdVarModel(int idVarModel) {
		this.idVarModel = idVarModel;
	}



	public int getIdSite() {
		return idSite;
	}



	public void setIdSite(int idSite) {
		this.idSite = idSite;
	}



	public int getIdDevModel() {
		return idDevModel;
	}



	public void setIdDevModel(int idDevModel) {
		this.idDevModel = idDevModel;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public int getAddressIn() {
		return addressIn;
	}



	public void setAddressIn(int addressIn) {
		this.addressIn = addressIn;
	}



	public int getAddressOut() {
		return addressOut;
	}



	public void setAddressOut(int addressOut) {
		this.addressOut = addressOut;
	}



	public int getVarDimension() {
		return varDimension;
	}



	public void setVarDimension(int varDimension) {
		this.varDimension = varDimension;
	}



	public int getBitPosition() {
		return bitPosition;
	}



	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}



	public boolean isSigned() {
		return signed;
	}



	public void setSigned(boolean signed) {
		this.signed = signed;
	}



	public int getDecimal() {
		return decimal;
	}



	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}



	public String getToDisplay() {
		return toDisplay;
	}



	public void setToDisplay(String toDisplay) {
		this.toDisplay = toDisplay;
	}



	public String getButtonPath() {
		return buttonPath;
	}



	public void setButtonPath(String buttonPath) {
		this.buttonPath = buttonPath;
	}



	public int getPriority() {
		return priority;
	}



	public void setPriority(int priority) {
		this.priority = priority;
	}



	public int getReadWrite() {
		return readWrite;
	}



	public void setReadWrite(int readWrite) {
		this.readWrite = readWrite;
	}



	public String getMinValue() {
		return minValue;
	}



	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}



	public String getMaxValue() {
		return maxValue;
	}



	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}



	public String getDefaultValue() {
		return defaultValue;
	}



	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}



	public String getMeasureUnit() {
		return measureUnit;
	}



	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}



	public int getIdVarGroup() {
		return idVarGroup;
	}



	public void setIdVarGroup(int idVarGroup) {
		this.idVarGroup = idVarGroup;
	}



	public String getImageOn() {
		return imageOn;
	}



	public void setImageOn(String imageOn) {
		this.imageOn = imageOn;
	}



	public String getImageOff() {
		return imageOff;
	}



	public void setImageOff(String imageOff) {
		this.imageOff = imageOff;
	}



	public int getFrequency() {
		return frequency;
	}



	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}



	public int getDelta() {
		return delta;
	}



	public void setDelta(int delta) {
		this.delta = delta;
	}



	public int getDelay() {
		return delay;
	}



	public void setDelay(int delay) {
		this.delay = delay;
	}



	public boolean isHaccp() {
		return isHaccp;
	}



	public void setHaccp(boolean isHaccp) {
		this.isHaccp = isHaccp;
	}



	public boolean isActive() {
		return isActive;
	}



	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	public boolean isRelay() {
		return isRelay;
	}



	public void setRelay(boolean isRelay) {
		this.isRelay = isRelay;
	}



	public int getGrpCategory() {
		return grpCategory;
	}



	public void setGrpCategory(int grpCategory) {
		this.grpCategory = grpCategory;
	}



	public int getHsTime() {
		return hsTime;
	}



	public void setHsTime(int hsTime) {
		this.hsTime = hsTime;
	}



	public int getHsFrequency() {
		return hsFrequency;
	}



	public void setHsFrequency(int hsFrequency) {
		this.hsFrequency = hsFrequency;
	}



	public float getHsDelta() {
		return hsDelta;
	}



	public void setHsDelta(int hsDelta) {
		this.hsDelta = hsDelta;
	}

	public int getVarLength() {
		return varLength;
	}

	public void setVarLength(int varLength) {
		this.varLength = varLength;
	}

	public String getCategoryCustomCode() {
		return categoryCustomCode;
	}

	public void setCategoryCustomCode(String categoryCustomCode) {
		this.categoryCustomCode = categoryCustomCode;
	}
	
	public boolean isCommand() {
		return isCommand;
	}
}