/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

/**
 * @author team pvpro 20090324
 *
 */
public class VarCRLTag {

	protected final static String CODE = "code";
	protected final static String ADDRESS_IN = "addressIn";
	protected final static String ADDRESS_OUT = "addressOut";
	protected final static String VAR_DIMENSION = "varDimension";
	protected final static String VAR_LENGTH = "varLength";
	protected final static String BIT_POSITION = "bitPosition";

	private String code;
	private int addressIn;
	private int addressOut;
	private int varDimension;
	private int varLength;
	private int bitPosition;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the addressIn
	 */
	public int getAddressIn() {
		return addressIn;
	}
	/**
	 * @param addressIn the addressIn to set
	 */
	public void setAddressIn(int addressIn) {
		this.addressIn = addressIn;
	}
	/**
	 * @return the addressOut
	 */
	public int getAddressOut() {
		return addressOut;
	}
	/**
	 * @param addressOut the addressOut to set
	 */
	public void setAddressOut(int addressOut) {
		this.addressOut = addressOut;
	}
	/**
	 * @return the varDimension
	 */
	public int getVarDimension() {
		return varDimension;
	}
	/**
	 * @param varDimension the varDimension to set
	 */
	public void setVarDimension(int varDimension) {
		this.varDimension = varDimension;
	}
	/**
	 * @return the varLength
	 */
	public int getVarLength() {
		return varLength;
	}
	/**
	 * @param varLength the varLength to set
	 */
	public void setVarLength(int varLength) {
		this.varLength = varLength;
	}
	/**
	 * @return the bitPosition
	 */
	public int getBitPosition() {
		return bitPosition;
	}
	/**
	 * @param bitPosition the bitPosition to set
	 */
	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}
}
