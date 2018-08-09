package com.carel.supervisor.controller.time;

import java.util.Date;


public abstract class TimeValidity
{
    public abstract boolean isValid(Date now);

    public abstract StringBuffer getData();

    public String toString()
    {
        return getData().toString();
    }
}
