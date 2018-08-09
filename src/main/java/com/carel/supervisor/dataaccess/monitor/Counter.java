/**
 *
 */
package com.carel.supervisor.dataaccess.monitor;

class Counter implements ICounter
{
    private long counter = 0;
    private long total = 0;
    private long current = 0;
    private long max = 0;
    private long min = 0;

    public void start()
    {
        current = System.currentTimeMillis();
        counter++;
    }

    public void stop()
    {
        long delta = (System.currentTimeMillis() - current);

        if (max < delta)
        {
            max = delta;
        }

        if (min > delta)
        {
            min = delta;
        }

        total += delta;
    }

    /**
     * @return: long
     */
    public long getCounter()
    {
        return counter;
    }

    /**
     * @return: long
     */
    public long getCurrent()
    {
        return current;
    }

    /**
     * @return: long
     */
    public long getMax()
    {
        return max;
    }

    /**
     * @return: long
     */
    public long getMin()
    {
        return min;
    }

    /**
     * @return: long
     */
    public long getTotal()
    {
        return total;
    }
}
