package com.carel.supervisor.controller.function;

import java.util.Map;


public class Costant extends CalcElement
{
    private float value = 0;

    public Costant(float value)
    {
        this.value = value;
    }

    public float getValue()
    {
        return value;
    }

    public Map getIdDev()
    {
        return null;
    }

    public boolean isNull()
    {
        return false;
    }
}
