package com.carel.supervisor.presentation.xmllibrary;



public class LinearGauge
{
    private final static int GAUGENUMBER = 3;
    private final static String NAME = "linearGauge";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();

        String[][] attribute = new String[GAUGENUMBER][];

        attribute[0] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "BarColour", "PointerColour", "ScaleColour", "BackgroundColour",
                "TextColour", "AlarmStatusColour", "NormalStatusColour",
                "AlarmValue"
            };

        attribute[1] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "BarColour", "ScaleColour", "BackgroundColour", "TextColour",
                "AlarmColour", "AlarmValue"
            };

        attribute[2] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "ScaleColour", "BackgroundColour", "TextColour", "ZoneValue1",
                "ZoneValue2"
            };

        //XMLibrary.append(Flash.CreateHML(GAUGENUMBER,attribute,NAME));
        XMLibrary.append(Flash.CreateDictionary(GAUGENUMBER, attribute, NAME, 14));
        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
