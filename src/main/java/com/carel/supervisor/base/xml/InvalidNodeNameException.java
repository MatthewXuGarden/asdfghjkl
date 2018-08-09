package com.carel.supervisor.base.xml;

public class InvalidNodeNameException extends RuntimeException
{
    public InvalidNodeNameException(Exception e)
    {
        super(e);
    }

    public InvalidNodeNameException()
    {
        super();
    }
}
