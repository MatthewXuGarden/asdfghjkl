package com.carel.supervisor.base.util.queue;


//l'object[]  da incodare deve contenere idsito idvariable samplingPeriod historicalPeriod 
public class Queue
{
    private final static int LENGTH = 100;
    private final static int EXPONENTIAL_RESIZE_FACTOR = 2;
    private int length = 0; //lunghezza coda
    private int start = 0; //inizio della coda
    private int end = 0; //fine della coda 
    private int numElement = 0; //numero di elementi: campo derivato ma ridondanza comoda
    private Object[][] queue = null; //Coda di elementi

    public Queue(int length)
    {
        this.length = (length <= 0) ? LENGTH : length;
        queue = new Object[this.length][];
    } //QueueRecords

    public synchronized void enqueRecord(Object[] record)
    {
        queue[end] = record;
        stepEnqueue();
    } //enqueueRecord

    public synchronized Object[][] dequeAllRecords()
    {
        return dequeRecords(numElement);
    } //enqueueRecord

    public synchronized Object[][] dequeRecords(int recordsNumber)
    {
        if (numElement == 0)
        {
            return null;
        }

        int n = (recordsNumber < numElement) ? recordsNumber : numElement;
        Object[][] tmpRecords = new Object[n][];

        for (int i = 0; i < n; i++)
        {
            tmpRecords[i] = queue[start];
            queue[start] = null;
            stepDequeue();
        } //for

        return tmpRecords;
    } //enqueueRecord

    public synchronized String toString()
    {
        StringBuffer tmp = new StringBuffer();

        for (int i = start; i < (start + numElement); i++)
        {
            int index = i % length;

            for (int j = 0; j < queue[index].length; j++)
            {
                tmp.append("Field:");
                tmp.append(j + 1);
                tmp.append(" Values:");
                tmp.append(queue[index][j]);
                tmp.append(" ");
            } //for

            tmp.append("\n");
        } //for

        return tmp.toString();
    } //toString

    public int realSize()
    {
        return length;
    } //realSize

    public int elementsSize()
    {
        return numElement;
    } //realSize

    private void stepEnqueue()
    {
        numElement++;

        if (numElement == length)
        {
            resize();
        }

        //Incremento Circolare
        end++;
        end %= length;

        //Numero effettivo di elementi
    } //stepEnqueue

    private void stepDequeue()
    {
        //Decremento Circolare
        start++;
        start %= length;

        //Numero effettivo di elementi
        numElement--;
    } //stepDequeue

    //il tempo per passare da 250K elementi a 500K elementi è di 60ms 
    private void resize()
    {
    	System.out.println("ENTRO IN RESIZE fdbqueue");
    	System.out.println("lunghezza " + length);
        int tmpLength = (length * EXPONENTIAL_RESIZE_FACTOR);
        Object[][] tmpQueue = new Object[tmpLength][];

        for (int i = 0; i < length; i++)
        {
            tmpQueue[i] = queue[(start + i) % length];
        }

        start = 0;
        end = numElement - 1;
        length = tmpLength;
        queue = tmpQueue;
    } //resize
} //Class QueueRecords
