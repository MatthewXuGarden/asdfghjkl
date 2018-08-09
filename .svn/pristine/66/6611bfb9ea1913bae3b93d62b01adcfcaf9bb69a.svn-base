package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.*;

public class OrElement extends Function
{
    public OrElement(CalcElement[] element)
    {
        super(element);
    }

    public float getValue()
    {
        float b = 0;
        float t = 0;
		boolean isNaN = true;  // kevin asked : if all of CalcVariable are NaN , we should return NaN (even contain constant).
		boolean fullConstant = true;   // kevin asked : if all of elements are constant, let it go.
        for (int i = 0; i < element.length; i++)
        {
            t = element[i].getValue();
			if(element[i] instanceof CalcVariable){
				fullConstant = false;
			}    
            if (Float.isNaN(t))
            {
				t = 0;
	        }else{
				if(element[i] instanceof CalcVariable){
					isNaN = false;
				}
			}
			b += t;
		}
		if (!fullConstant && isNaN) {
			return Float.NaN;
		}

        if (b > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
