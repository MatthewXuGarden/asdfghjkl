/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.HashMap;

/**
 * @author team pvpro 20090325
 *
 */
public class ItemTag {

	private HashMap<String, KeyTag> hmKey = new HashMap<String, KeyTag>();
	
	protected static final String ITEM = "Item";
	protected static final String CODE = "code";

	public static final String KEYS = "Keys";
	
	private String code;

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
	 * @return the hmKey
	 */
	public HashMap<String, KeyTag> getHmKey() {
		return hmKey;
	}

}
