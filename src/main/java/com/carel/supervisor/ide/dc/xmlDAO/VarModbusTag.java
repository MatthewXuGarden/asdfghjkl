package com.carel.supervisor.ide.dc.xmlDAO;

public class VarModbusTag {

	//<VarMDB code="Var1" addressIn="200003" funcTypeRead="2" addressOut="200000" funcTypeWrite="0" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" varEncoding="0"/>
	
	protected final static String CODE = "code";
	protected final static String ADDRESS_IN = "addressIn";
	protected final static String ADDRESS_OUT = "addressOut";
	protected final static String VAR_DIMENSION = "varDimension";
	protected final static String VAR_LENGTH = "varLength";
	protected final static String BIT_POSITION = "bitPosition";
	protected final static String FUNC_TYPE_READ = "funcTypeRead";
	protected final static String FUNC_TYPE_WRITE = "funcTypeWrite";
	protected final static String A_VALUE = "aValue";
	protected final static String B_VALUE = "bValue";
	protected final static String VAR_ENCODING = "varEncoding";

	private String code;
	private int addressIn;
	private int addressOut;
	private int varDimension;
	private int varLength;
	private int bitPosition;
	private int funcTypeRead;
	private int funcTypeWrite;
	private float aValue;
	private float bValue;
	private int varEncoding;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public int getVarLength() {
		return varLength;
	}
	public void setVarLength(int varLength) {
		this.varLength = varLength;
	}
	public int getBitPosition() {
		return bitPosition;
	}
	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}
	public int getFuncTypeRead() {
		return funcTypeRead;
	}
	public void setFuncTypeRead(int funcTypeRead) {
		this.funcTypeRead = funcTypeRead;
	}
	public int getFuncTypeWrite() {
		return funcTypeWrite;
	}
	public void setFuncTypeWrite(int funcTypeWrite) {
		this.funcTypeWrite = funcTypeWrite;
	}
	public float getAValue() {
		return aValue;
	}
	public void setAValue(float value) {
		aValue = value;
	}
	public float getBValue() {
		return bValue;
	}
	public void setBValue(float value) {
		bValue = value;
	}

	public int getVarEncoding() {
		return varEncoding;
	}
	
	public void setVarEncoding(int value) {
		varEncoding = value;
	}
}
