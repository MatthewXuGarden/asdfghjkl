package com.carel.supervisor.presentation.xmllibrary;



public class Slider
{
    private final static int GAUGENUMBER = 2;
    private final static String NAME = "Slider";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();
        String[] suffix = new String[GAUGENUMBER];
        String[][] attribute = new String[GAUGENUMBER][];

        suffix[0] = new String("V");
        suffix[1] = new String("H");

        attribute[0] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr", "BarColor"
            };

        attribute[1] = new String[]
            {
                "MinScale", "MaxScale", "Unit", "HiAlr", "LoAlr", "BarColor"
            };

        //XMLibrary.append(Flash.CreateHMLSuffix(GAUGENUMBER,attribute,NAME,suffix));
        XMLibrary.append(Flash.CreateDictionary(GAUGENUMBER, attribute, NAME, 21));
        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
