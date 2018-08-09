/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.HashMap;

/**
 * @author team pvpro 20090325
 *
 */
public class SectionTag {

	private HashMap<String, ItemTag> hmItem = new HashMap<String, ItemTag>();
	
	protected static final String NAME = "name";
	protected static final String ITEMS = "Items";

	protected static final String SECTION = "Section";

	private String name;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the hmItem
	 */
	public HashMap<String, ItemTag> getHmItem() {
		return hmItem;
	}
	
}
