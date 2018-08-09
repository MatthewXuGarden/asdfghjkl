package com.carel.supervisor.dispatcher.sim;

import java.util.Random;


public class Simulator
{
    private static Random random = new Random();
    private static final int F_S = 60000;
    private static final int S_S = 62000;
    private static final int D_S = 120000;
    private static final int E_S = 83000;

    public static boolean simAction(String type, String fisDev)
    {
        boolean result = getResult();
        int sleep = timeSleep(type);
        int delta = 0;

        try
        {
            if (result)
            {
                delta = random.nextInt(5) + 1;
                delta = delta * 1000;
                Thread.sleep((sleep + delta));
            }
            else
            {
                delta = random.nextInt(10) + 1;
                delta = delta * 1000;
                Thread.sleep((delta));
            }
        }
        catch (Exception e)
        {
        }

        return result;
    }

    private static boolean getResult()
    {
        if ((random.nextInt(5) + 1) >= 4)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private static int timeSleep(String type)
    {
        if (type.equalsIgnoreCase("F"))
        {
            return F_S;
        }
        else if (type.equalsIgnoreCase("S"))
        {
            return S_S;
        }
        else if (type.equalsIgnoreCase("D"))
        {
            return D_S;
        }
        else if (type.equalsIgnoreCase("E"))
        {
            return E_S;
        }
        else
        {
            return 30000;
        }
    }
}
