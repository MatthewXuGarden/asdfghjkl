package com.carel.supervisor.controller.function;

import com.carel.supervisor.field.IFunction;


public abstract class CalcElement implements IFunction
{
    public abstract float getValue();

    public abstract boolean isNull();
}
