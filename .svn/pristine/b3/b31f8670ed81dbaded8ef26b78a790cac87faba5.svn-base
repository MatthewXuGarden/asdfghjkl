package com.carel.supervisor.base.util.queue;


public class TestQueue
{
    public static void main(String[] args)
    {
        Queue queue = new Queue(100);

        Integer[][] integers = new Integer[1000][1];

        for (int i = 0; i < 180; i++)
        {
            integers[i][0] = new Integer(i);
            queue.enqueRecord(integers[i]);
        } //for

        System.out.println(queue);

        for (int i = 0; i < 50; i++)
        {
            queue.dequeRecords(1);
        } //for

        for (int i = 0; i < 380; i++)
        {
            integers[i][0] = new Integer(i);
            queue.enqueRecord(integers[i]);
        } //for

        System.out.println(queue);
    }
}
