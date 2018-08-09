package com.carel.supervisor.controller.function;

import com.carel.supervisor.field.IRetriever;
import com.carel.supervisor.field.Variable;

import java.util.HashMap;
import java.util.Map;


public abstract class Function extends CalcElement implements IRetriever
{
    protected CalcElement[] element = null;
    private boolean blockingError = false;

    public Function(CalcElement[] element)
    {
        this.element = element;
    }

    public float condition(boolean b)
    {
        if (b)
            return 1;
        else
            return 0;
    }

    public void retrieve(Variable variable)
    {
        float f = Float.NaN;

        try
        {
            f = getValue();
        }
        catch (Exception e)
        {
            //Math exception are not notified
        }

        if (Float.isNaN(f))
            variable.setValue(null);
        else
            variable.setValue(new Float(f));
    }

    public boolean isNull()
    {
        boolean nullable = false;

        for (int i = 0; i < element.length; i++)
            nullable = nullable || element[i].isNull();

        return nullable;
    }

    public boolean isBlockingError()
    {
        return blockingError;
    }

    public void setBlockingError()
    {
        blockingError = true;
    }

    public void removeBlockingError()
    {
        blockingError = false;
    }

    public Map getIdDev()
    {
        Map map = new HashMap();

        for (int i = 0; i < element.length; i++)
        {
        	if(element[i].getIdDev() != null)
        	{
        		map.putAll(element[i].getIdDev());
        	}
        }
        return map;
    }
    public CalcElement[] getElements()
    {
    	return this.element;
    }
    public HashMap getVariableComposeFormula()
    {
        HashMap variables = new HashMap();

        for (int i = 0; i < element.length; i++)
            if (element[i].getClass().equals(CalcVariable.class))
            {
                variables.put(((CalcVariable) element[i]).getIdVariable(),
                    ((CalcVariable) element[i]).getIstantanValue());
            } //if
            else if (!element[i].getClass().equals(Costant.class))
            { //� una funzione

                HashMap mergingVar = ((Function) element[i]).getVariableComposeFormula();
                Object[] keys = mergingVar.keySet().toArray();

                if (keys != null)
                {
                    for (int j = 0; j < keys.length; j++)
                    {
                        variables.put(keys[j], mergingVar.get(keys[j]));
                    } //for
                } //if
            } //else

        return variables;
    } //getVariableComposeFormula
} //Function
