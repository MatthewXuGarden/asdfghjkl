package com.carel.supervisor.presentation.xmllibrary;



public class CircGauge
{
    private final static int GAUGENUMBER = 3;
    private final static String NAME = "CircGauge";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();

        String[][] attribute = new String[GAUGENUMBER][];

        attribute[0] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr"
            };

        attribute[1] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr"
            };

        attribute[2] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr"
            };

        //XMLibrary.append(Flash.CreateHML(GAUGENUMBER,attribute,NAME));
        XMLibrary.append(Flash.CreateDictionary(GAUGENUMBER, attribute, NAME, 11));

        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
