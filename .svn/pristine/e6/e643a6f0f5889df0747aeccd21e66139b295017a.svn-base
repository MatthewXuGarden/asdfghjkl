package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.CalcElement;
import com.carel.supervisor.controller.function.Function;


public class AddFunction extends Function
{
    public AddFunction(CalcElement[] element)
    {
        super(element);
    }

    public float getValue()
    {
        float sum = 0;
        float t = 0;

        for (int i = 0; i < element.length; i++)
        {
            t = element[i].getValue();

            if (Float.isNaN(t))
            {
                return Float.NaN;
            }

            sum += t;
        }

        return sum;
    }
}
