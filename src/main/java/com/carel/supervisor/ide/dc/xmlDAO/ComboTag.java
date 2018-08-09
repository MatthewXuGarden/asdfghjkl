/**
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;

/**
 * 
 * @author team pvpro 200903223
 *
 */
public class ComboTag {

	protected static final String COMBO = "Combo";
	protected static final String COMBO_CODE = "comboCode";
	protected static final String USER = "user";
	protected static final String COMBO_ITEMS = "ComboItems";

	private String comboCode;
	
	private ArrayList<ComboItemTag> alComboItem = new ArrayList<ComboItemTag>();

	/**
	 * @return the comboId
	 */
	public String getComboCode() {
		return comboCode;
	}
	/**
	 * @param comboId the comboId to set
	 */
	public void setComboCode(String comboCode) {
		this.comboCode = comboCode;
	}


	public ArrayList<ComboItemTag> getAlComboItem() {
		return alComboItem;
	}

}
