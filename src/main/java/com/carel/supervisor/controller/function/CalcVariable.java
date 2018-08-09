package com.carel.supervisor.controller.function;

import com.carel.supervisor.field.Variable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


public class CalcVariable extends CalcElement
{
    private Variable variable = null;
    private String idVariable = "";
    private String istantanValue = "";

    public CalcVariable(Variable variable)
    {
        this.variable = variable;
    }

    public boolean isNull()
    {
        return Float.isNaN(variable.getCurrentValue());
    }

    public Variable getInnerVariable()
    {
        return variable;
    }

    public float getValue()
    {
        //prima load del dato
        variable.retrieveAndSaveValue(0, null, false); //Sono tutte volatili
        idVariable = String.valueOf(variable.getInfo().getId());

        NumberFormat formatter = new DecimalFormat(".##");
        istantanValue = String.valueOf(formatter.format(variable.getCurrentValue()).replace(",", "."));

        return variable.getCurrentValue();
    } //getValue

    public boolean isOffLine()
    {
        return variable.isDeviceOffLine();
    }

    public Map getIdDev()
    {
        Map map = new HashMap();

        //Variabili logiche di variabili logiche
        if (variable.getInfo().isLogic())
        {
            Function function = (Function) variable.getRetriever();
            map.putAll(function.getIdDev());
        }
        else
        {
            map.put(variable.getInfo().getDevice(), variable.getInfo().getDevice());
        }

        return map;
    } //getIdDev

    public String getIdVariable()
    {
        return idVariable;
    } //getIdVariable

    public String getIstantanValue()
    {
        return istantanValue;
    } //getIstantanValue
} //Class CalcVariable
