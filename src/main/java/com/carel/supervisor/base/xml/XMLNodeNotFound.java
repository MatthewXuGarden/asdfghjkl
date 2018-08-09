package com.carel.supervisor.base.xml;

public class XMLNodeNotFound extends RuntimeException
{
    public XMLNodeNotFound(String message)
    {
        super(message);
    }

    public XMLNodeNotFound()
    {
        super();
    }
}
