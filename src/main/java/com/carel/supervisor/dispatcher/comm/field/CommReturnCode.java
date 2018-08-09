package com.carel.supervisor.dispatcher.comm.field;

public class CommReturnCode {
	private short returnCode = 0;
    private long rowNumber = 0;
    private boolean errorPresent = false;
    private Throwable exception = null;
    private String communicationName = null;
    private String message = "";
    
    public CommReturnCode(Throwable exception)
    {
    	this.exception = exception;
    }
    public CommReturnCode(short returnCode)
    {
    	this.returnCode = returnCode;
    }
    public CommReturnCode(short returnCode, boolean errorPresent)
    {
        this.returnCode = returnCode;
        this.errorPresent = errorPresent;
    }
    public CommReturnCode(short returnCode, String message,
            boolean errorPresent)
    {
        this.returnCode = returnCode;
        this.errorPresent = errorPresent;
        this.message = message;
    }
    public CommReturnCode()
    {
    }

    /**
     * @return: short
     */
    public short getReturnCode()
    {
        return returnCode;
    }

    /**
     * @param returnCode
     */
    public void setReturnCode(short returnCode)
    {
        this.returnCode = returnCode;
    }

    /**
     * @return: long
     */
    public long getRowNumber()
    {
        return rowNumber;
    }

    /**
     * @param rowNumber
     */
    public void setRowNumber(long rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    /**
     * @return: boolean
     */
    public boolean isErrorPresent()
    {
        return errorPresent;
    }

    /**
     * @param errorPresent
     */
    public void setErrorPresent(boolean errorPresent)
    {
        this.errorPresent = errorPresent;
    }

    /**
     * @return: boolean
     */
    public boolean isExceptionPresent()
    {
        return (null != exception);
    }

    /**
     * @return: Throwable
     */
    public Throwable getException()
    {
        return exception;
    }

    /**
     * @param errorPresent
     */
    public void setException(Throwable exception)
    {
        this.exception = exception;
    }

    /**
     * @return: String
     */
    public String getCommunicationName()
    {
        return communicationName;
    }

    /**
     * @param driverName
     */
    public void setCommunicationName(String comunicationName)
    {
        this.communicationName = comunicationName;
    }
    /**
     * @return: String
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}
