/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

/** Holds the //Device/Supervisors/PV/Enums/Enum/EnumItems/EnumItem contents
 * @author team pvpro 20090323
 *
 */
public class EnumItemTag {
	
	protected static final String VALUE = "value";
	protected static final String DESCR = "descr";
	
	private int value;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

}
