package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.*;


public class NotElement extends Function
{
    public NotElement(CalcElement[] element)
    {
        super(element);
    }

    public float getValue()
    {
        float value = element[0].getValue();

        if (Float.isNaN(value))
        {
            return Float.NaN;
        }

        try
        {
            if (0 == value)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            return Float.NaN;
        }
    }
}
