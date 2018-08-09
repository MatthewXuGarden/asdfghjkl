package com.carel.supervisor.base.factory;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2005
 * Company: CAREL S.P.A.
 * @author Loris D'Acunto
 * @version 1.0
 */
public class FactoryObject
{
	private static final Logger logger = LoggerMgr.getLogger(FactoryObject.class);
    private FactoryObject()
    {
    }

    public static Object newInstance(String className, Class[] classes,
        Object[] values) throws Exception
    {
        try
        {
            return Class.forName(className).getConstructor(classes).newInstance(values);
        }
        catch (Exception e)
        {
            logger.fatal("TRY TO CREATE OBJECT: " + className + " " +
                createErrorMessage(classes, values), e);
            throw new Exception("TRY TO CREATE OBJECT: " + className);
        }
    }

    public static Object newInstance(String className)
        throws Exception
    {
        try
        {
            return Class.forName(className).newInstance();
        }
        catch (Exception e)
        {
            logger.fatal("TRY TO CREATE OBJECT: " + className, e);
            throw new Exception("TRY TO CREATE OBJECT: " + className);
        }
    }

    private static String createErrorMessage(Class[] classes, Object[] values)
    {
        StringBuffer buffer = new StringBuffer();

        if (null != classes)
        {
            for (int i = 0; i < classes.length; i++)
            {
                buffer.append("[CLASS: ");
                buffer.append(classes[i].toString());
                buffer.append(" - VALUE: ");
                buffer.append((null == values[i]) ? "" : values[i].toString());
                buffer.append("]");
            }

            return buffer.toString();
        }

        return "";
    }
}
