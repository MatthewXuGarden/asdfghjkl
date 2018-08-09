/**
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;

/**
 * 
 * @author team pvpro 200903224
 *
 */

public class WizardTag {


	public enum WizardType{
		VAR,
		PROBE,
		REF,
		UNIT,
		UNKNOWN
	}
	
	private ArrayList<SetTag> alSet = new ArrayList<SetTag>();

	protected static final String WIZARD = "Wizard";
	protected static final String WIZARD_ID = "wizardId";
	protected static final String USER = "user";
	protected static final String WIZARD_TYPE = "type";
	protected static final String SETS = "Sets";		

	private String wizardId;
	private WizardType wizardType;
	private String code;
	
	/**
	 * @return the wizardId
	 */
	public String getWizardId() {
		return wizardId;
	}

	/**
	 * @param wizardId the wizardId to set
	 */
	public void setWizardId(String wizardId) {
		this.wizardId = wizardId;
	}

	/**
	 * @return the alSet
	 */
	public ArrayList<SetTag> getAlSet() {
		return alSet;
	}

	public void setWizartType(WizardType wizardType) {
		this.wizardType = wizardType;
	}

	public WizardType getWizardType() {
		return wizardType;
	}

	public void setUser(String user) {
		this.code = user;
	}

	public String getUser() {
		return code;
	}


}
