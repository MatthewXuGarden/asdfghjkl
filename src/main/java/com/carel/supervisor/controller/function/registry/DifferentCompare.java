package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.*;


public class DifferentCompare extends Function
{
    public DifferentCompare(CalcElement[] element)
    {
        super(element);
    }

    public float getValue()
    {
        float value1 = element[0].getValue();
        float value2 = element[1].getValue();

        if (Float.isNaN(value1))
        {
            return Float.NaN;
        }

        if (Float.isNaN(value2))
        {
            return Float.NaN;
        }

        try
        {
            return condition(value1 != value2);
        }
        catch (Exception e)
        {
            return Float.NaN;
        }
    }
}
