package com.carel.supervisor.dispatcher.memory;

public class FisicMemory
{
    private String type = "";
    private String modem = "";
    private String provy = "";

    public FisicMemory(String type, String modem, String provider)
    {
        this.type = type;
        this.modem = modem;

        if ((provider != null) && provider.equalsIgnoreCase("X"))
        {
            this.provy = "";
        }
        else
        {
            this.provy = provider;
        }
    }

    public FisicMemory(String type, String modem)
    {
        this(type, modem, "");
    }

    public String getTypeFisic()
    {
        return this.type;
    }

    public String getModem()
    {
        return this.modem;
    }

    public String getProvider()
    {
        return this.provy;
    }
}
