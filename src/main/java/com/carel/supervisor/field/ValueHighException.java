package com.carel.supervisor.field;

public class ValueHighException extends Exception
{
    private String value = null;

    public ValueHighException(String message)
    {
        super(message);
        value = message;
    }

    public String getValue()
    {
        return value;
    }
}
