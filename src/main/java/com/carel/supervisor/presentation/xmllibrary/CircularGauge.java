package com.carel.supervisor.presentation.xmllibrary;



public class CircularGauge
{
    private final static int GAUGENUMBER = 10;
    private final static String NAME = "circularGauge";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();

        String[][] attribute = new String[GAUGENUMBER][];

        attribute[0] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour",
                "StartAlarmValue", "StartAlarmColour", "EndAlarmColour"
            };

        attribute[1] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "PointerColour", "ScaleColour", "BackgroundColour", "TextColour"
            };

        attribute[2] = new String[]
            {
                "MinScale1", "MinScale2", "MaxScale1", "MaxScale2",
                "CurrentValue1", "CurrentValue2", "PointerColour",
                "ScaleColour1", "ScaleColour2", "BackgroundColour", "TextColour"
            };
        attribute[3] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "PointerColour", "ScaleColour", "BackgroundColour", "TextColour",
                "StartZoneValue1", "EndZoneValue1", "StartZoneValue2",
                "EndZoneValue2", "ZoneColour1", "ZoneColour2"
            };
        attribute[4] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour", "Decimals",
                "StartAlarmValue", "StartAlarmColour", "EndAlarmColour"
            };
        attribute[5] = new String[]
            {
                "MinScale", "MaxScale", "UnitMeasurement", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour", "AlarmValue",
                "AlarmColour"
            };
        attribute[6] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour",
                "StartAlarmValue", "StartAlarmColour", "EndAlarmColour"
            };
        attribute[7] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "UnitMeasurement",
                "PointerColour", "ScaleColour", "AlarmValue", "BarColour",
                "BackgroundColour", "TextColour"
            };
        attribute[8] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour",
                "StartScaleColour", "EndScaleColour"
            };
        attribute[9] = new String[]
            {
                "MinScale", "MaxScale", "CurrentValue", "PointerColour",
                "ScaleColour", "BackgroundColour", "TextColour", "StartValue1",
                "EndValue1", "StartValue2", "EndValue2", "StartValue3",
                "EndValue3", "ScaleColour1", "ScaleColour2", "ScaleColour3"
            };

        //	XMLibrary.append(Flash.CreateHML(GAUGENUMBER,attribute,NAME));
        XMLibrary.append(Flash.CreateDictionary(GAUGENUMBER, attribute, NAME, 1));

        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
