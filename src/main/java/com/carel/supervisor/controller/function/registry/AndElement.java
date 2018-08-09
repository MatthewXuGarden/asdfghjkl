package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.*;


public class AndElement extends Function
{
    public AndElement(CalcElement[] element)
    {
        super(element);
    }

    //1 TRUE  0   FALSE
    public float getValue()
    {
        float b = 1;
        float t = 0;
		boolean  isNaN = true;  // kevin asked : if all of CalcVariable are NaN , we should return NaN (even contain constant).
		boolean fullConstant = true;   // kevin asked : if all of elements are constant, let it go.
        for (int i = 0; i < element.length; i++)
        {
            t = element[i].getValue();
            if(element[i] instanceof CalcVariable){
				fullConstant = false;
			}
            if (Float.isNaN(t))
            {
            	t =  1;
            }else{
				if(element[i] instanceof CalcVariable){
					isNaN = false;
				}
			}
            
            b = b * t;
        }
		if (!fullConstant && isNaN) {
			return Float.NaN;
		}
        return b;
    }
}
