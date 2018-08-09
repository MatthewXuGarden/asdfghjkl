package com.carel.supervisor.base.factory;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2005
 * Company: CAREL S.P.A.
 * @author Loris D'Acunto
 * @version 1.0
 */
public class ImplementationNotFoundException extends RuntimeException
{
    public ImplementationNotFoundException(String message)
    {
        super(message);
    }

    public ImplementationNotFoundException()
    {
        super();
    }
}
