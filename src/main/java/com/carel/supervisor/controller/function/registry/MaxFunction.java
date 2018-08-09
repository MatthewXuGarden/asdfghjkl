package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.CalcElement;
import com.carel.supervisor.controller.function.Function;


public class MaxFunction extends Function
{
    public MaxFunction(CalcElement[] element)
    {
        super(element);
    }

    public float getValue()
    {
        float max = 0;
        float t = 0;

        for (int i = 0; i < element.length; i++)
        {
            t = element[i].getValue();

            if (Float.isNaN(t))
            {
                return Float.NaN;
            }

            max = Math.max(max, t);
        }

        return max;
    }
}
