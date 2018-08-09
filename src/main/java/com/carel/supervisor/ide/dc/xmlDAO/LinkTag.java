/**
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;

/**
 * 
 * @author team pvpro 200903224
 *
 */
public class LinkTag {

	protected static final String LINK = "Link";
	protected static final String LINK_ID = "linkId";
	protected static final String USER = "user";
	protected static final String CHECK_SET = "checkSet";
	protected static final String VAR_MASTER = "varMaster";	
	protected static final String SLAVES = "Slaves";

	private String linkId;
	private String varMaster;
	private String user;
	private boolean checkSet;
	
	
	private ArrayList<SlaveTag> alSlave = new ArrayList<SlaveTag>();


	public ArrayList<SlaveTag> getAlSlave() {
		return alSlave;
	}
	/**
	 * @return the linkId
	 */
	public String getLinkId() {
		return linkId;
	}
	/**
	 * @param linkId the linkId to set
	 */
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	/**
	 * @return the varMaster
	 */
	public String getVarMaster() {
		return varMaster;
	}
	/**
	 * @param varMaster the varMaster to set
	 */
	public void setVarMaster(String varMaster) {
		this.varMaster = varMaster;
	}
	public void setUser(String code) {
		this.user = code;
	}
	public String getUser() {
		return user;
	}
	public void setCheckSet(boolean checkSet) {
		this.checkSet = checkSet;
	}
	public boolean getCheckSet() {
		return checkSet;
	}

}
