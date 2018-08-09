package com.carel.supervisor.base.math;

public class MathExt
{
    private MathExt()
    {
    }

    //Generico Massimo comune divisore
    public static int gcd(int m, int n)
    {
        if (m < n)
        {
            int t = m;
            m = n;
            n = t;
        }

        int r = m % n;

        if (r == 0)
        {
            return n;
        }
        else
        {

            return gcd(n, r);
        }
    }

    // vettore nel quale calcolare il massimo comune divisore
    public static int gcd(int[] x)
    {
        int d = x[0];

        for (int i = 1; i < x.length; i++)
        {
            d = gcd(d, x[i]);
        }

        return d;
    }

    //algoritmo di euclide

    /*  public static int mcd(int a, int b)
      {
          int t;

          while (b != 0)
          {
              t = b;
              b = a % b;
              a = t;
          } //while

          return a;
      } //mcd

      public static int mcd(int[] x)
      {
          int d = x[0];

          for (int i = 1; i < x.length; i++)
              d = mcd(d, x[i]);

          return d;
      }*/
} //Class MathExt
