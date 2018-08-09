package com.carel.supervisor.presentation.xmllibrary;



public class Tanks
{
    private final static int GAUGENUMBER = 2;
    private final static String NAME = "Tank";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();

        String[][] attribute = new String[GAUGENUMBER][];

        attribute[0] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr", "BarColor"
            };

        attribute[1] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr", "BarColor"
            };

        //XMLibrary.append(Flash.CreateHMLTank(GAUGENUMBER,attribute,NAME));
        XMLibrary.append(Flash.CreateDictionary(GAUGENUMBER, attribute, NAME, 19));
        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
