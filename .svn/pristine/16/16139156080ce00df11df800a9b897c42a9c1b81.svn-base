package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.CalcElement;
import com.carel.supervisor.controller.function.Function;


public class TenfoldFunction extends Function
{
    public TenfoldFunction(CalcElement[] element)
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
            return value * 10;
        }
        catch (Exception e)
        {
            return Float.NaN;
        }
    }
}
