package com.carel.supervisor.dispatcher.engine.rele;

import java.util.Timer;


public class DispReleTimer extends Timer
{
    private int idVariable = 0;
    private long delay = 0;
    private String value = "";
    private String language = "";

    public DispReleTimer(int idVar, String val, long delay, String language)
    {
        this.idVariable = idVar;
        this.value = val;
        this.delay = delay;
        this.language = language;
    }

    public void resetRele()
    {
        this.schedule(new DispReleSetter(this.idVariable, this.value, language), this.delay);
    }
}
