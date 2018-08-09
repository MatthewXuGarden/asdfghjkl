/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

/** Holds the //Device/Supervisors/PV/Wizards/Wizard/Sets/Set/VarSets/VarSet contents
 * @author team pvpro 20090324
 *
 */
public class VarSetTag {
	
	protected static final String VARSET = "VarSet";
	
	protected static final String ID_VAR = "idVar";
	protected static final String VALUE = "value";
	
	private String idVar;
	private String value;
	/**
	 * @return the idVar
	 */
	public String getIdVar() {
		return idVar;
	}
	/**
	 * @param idVar the idVar to set
	 */
	public void setIdVar(String idVar) {
		this.idVar = idVar;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
