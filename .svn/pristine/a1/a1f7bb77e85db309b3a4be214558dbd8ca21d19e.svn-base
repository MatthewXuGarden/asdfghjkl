package com.carel.supervisor.controller.function.registry;

import com.carel.supervisor.controller.function.CalcElement;
import com.carel.supervisor.controller.function.Function;


public class ProdFunction extends Function
{
    public ProdFunction(CalcElement[] element)
    {
        super(element);
    }

    
    public float getValue()
    {
    	float value = 0;
    	
    	for(int i = 0; i < element.length; i++) {
    		float elementValue = element[i].getValue();
    		
    		if( Float.isNaN(elementValue) )
    			return Float.NaN;
    		
    		if( i == 0 )
    			value = elementValue;
    		else
    			value *= elementValue;
    	}
    	
    	return value;
    }
}
