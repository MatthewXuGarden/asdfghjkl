/**
 * 
 */
package com.carel.supervisor.ide.dc.DbModel.Translation;

import java.util.HashMap;

/**
 * @author Utente
 *
 */
public class Item {

	
	private HashMap<String, String> keys;
	private String code;
	
	public Item()
	{
		keys = new HashMap<String, String>();
	}

	public HashMap<String, String> getKeys() {
		return keys;
	}

	public void setKeys(HashMap<String, String> keys) {
		this.keys = keys;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	

}
