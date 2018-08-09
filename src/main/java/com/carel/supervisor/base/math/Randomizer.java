package com.carel.supervisor.base.math;

public class Randomizer
{
    private Randomizer()
    {
    }

    public static double returnNumber(double max)
    {
        return (double) (Math.random() * max);
    }

    public static double returnNumber(double min, double max)
    {
        return (double) ((Math.random() * (max - min)) + min);
    }

    public static long returnNumber(long max)
    {
        return (long) (Math.random() * max);
    }

    public static long returnNumber(long min, long max)
    {
        return (long) ((Math.random() * (max - min)) + min);
    }

    public static int returnNumber(int max)
    {
        return (int) (Math.random() * max);
    }

    public static int returnNumber(int min, int max)
    {
        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }

    public static String returnString(long numChar)
    {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < numChar; i++)
        {
            buffer.append((char) returnNumber(32, 126));
        }

        return buffer.toString();
    }

    /*
       public static Date returnDate()
       {
      int iYear = returnNumber(1980,2020);
      int iMonth = returnNumber(1,12);
      int iDay = 0;
      int iBase = 0;
      switch (iMonth)
      {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
          iBase=31;
          break;
        case 2:
          iBase=28;
          break;
        case 4:
        case 6:
        case 9:
        case 11:
          iBase=30;
          break;
      }
      iDay = returnNumber(1,iBase);
      return DateObject.createDate(iDay,iMonth,iYear);
       }
     */
}
