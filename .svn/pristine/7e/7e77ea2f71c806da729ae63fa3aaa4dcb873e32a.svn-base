package com.carel.supervisor.field.dataconn;

public class DriverReturnCode
{
    private short returnCode = 0;
    private String message = "";
    private long rowNumber = 0;
    private boolean errorPresent = false;
    private Throwable exception = null;

    public DriverReturnCode(Throwable exception)
    {
        this.exception = exception;
    }

    public DriverReturnCode(short returnCode)
    {
        this.returnCode = returnCode;
    }

    public DriverReturnCode(short returnCode, String message,
        boolean errorPresent)
    {
        this.returnCode = returnCode;
        this.errorPresent = errorPresent;
        this.message = message;
    }

    public DriverReturnCode()
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
