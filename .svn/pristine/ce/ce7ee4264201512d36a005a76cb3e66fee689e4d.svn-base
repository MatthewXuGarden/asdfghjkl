package com.carel.supervisor.ide.dc.xmlDAO;

public class ImportException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4104184819438584122L;
	private String message = "";
	
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public ImportException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 */
	public ImportException(String arg0) {
		super(arg0);
	}

	/**
	 * 
	 * @param arg0
	 */
	public ImportException(Throwable arg0) {
		super(arg0);
	}
	
	public ImportException(String arg0, Throwable arg1, String msg)
	{
		super(arg0, arg1);
		message = msg;
	}
	
	public ImportException(String arg0, String msg)
	{
		super(arg0);
		message = msg;
	}
	
	public String getDescription()
	{
		return message;
	}

}
