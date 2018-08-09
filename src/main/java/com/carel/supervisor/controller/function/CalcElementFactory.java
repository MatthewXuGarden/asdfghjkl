package com.carel.supervisor.controller.function;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.field.Variable;
import java.util.HashMap;
import java.util.Map;


public class CalcElementFactory
{
    private Map registry = null;
    private Map varCal = new HashMap();

    public CalcElementFactory(Map registry)
    {
        this.registry = registry;
    }

    public void clearCache()
    {
        varCal.clear();
    }

    public CalcElement create(CalcElementData elementData)
        throws Exception
    {
        String[] var = elementData.getVariables();
        CalcElement[] element = new CalcElement[var.length];

        for (int i = 0; i < var.length; i++)
        {
            if (var[i].startsWith("$"))
            {
                int value = Integer.parseInt(var[i].substring(1));
                element[i] = create(elementData.getById(value));
            }
            else if (var[i].startsWith("pk"))
            {
                int value = Integer.parseInt(var[i].substring(2));
                element[i] = (CalcVariable) varCal.get(new Integer(value));

                if (null == element[i])
                {
                    Variable variable = ControllerMgr.getInstance().retrieve(value);
                    element[i] = new CalcVariable(variable);
                    varCal.put(new Integer(value), element[i]);
                }
            }
            else
            {
                float value = Float.parseFloat(var[i]);
                element[i] = new Costant(value);
            }
        }

        if (null != elementData.getOperator())
        {
            Function function = (Function) FactoryObject.newInstance(((String) registry.get(
                        elementData.getOperator())), new Class[] { CalcElement[].class },
                    new Object[] { element });

            return function;
        }
        else
        {
            return element[0];
        }
    }
}
