package com.carel.supervisor.presentation.xmllibrary;



public class Led
{
    private final static int GAUGENUMBER = 2;
    private final static String NAME = "LED";

    public static void main(String[] args)
    {
        StringBuffer XMLibrary = new StringBuffer();
        String[] suffix = new String[GAUGENUMBER];
        String[][] attribute = new String[GAUGENUMBER][];

        suffix[0] = new String("Round");
        suffix[1] = new String("Rect");

        attribute[0] = new String[] { "ONColor", "OFFColor" };

        attribute[1] = new String[] { "ONColor", "OFFColor" };

        XMLibrary.append(Flash.CreateHMLSuffix(GAUGENUMBER, attribute, NAME,
                suffix));

        System.out.println(XMLibrary.toString());
    } //main
} //Class Gauge
