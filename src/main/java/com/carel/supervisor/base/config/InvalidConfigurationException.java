package com.carel.supervisor.base.config;

public class InvalidConfigurationException extends Exception
{
    public InvalidConfigurationException(String message)
    {
        super(message);
    }

    public InvalidConfigurationException(String message, Exception exception)
    {
        super(message, exception);
    }
}
