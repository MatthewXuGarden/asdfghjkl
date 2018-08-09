package com.carel.supervisor.base.config;

public class ResourceNotFoundException extends Exception
{
    public ResourceNotFoundException()
    {
        super();
    }

    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
