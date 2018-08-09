package com.carel.supervisor.dataaccess.history;

public class FDBQueue
{
    private final static int LENGTH = 10000;
    private final static int EXPONENTIAL_RESIZE_FACTOR = 2;
    private int length = 0; //lunghezza coda
    private int start = 0; //inizio della coda
    private int end = 0; //fine della coda 
    private int numElement = 0; //numero di elementi: campo derivato ma ridondanza comoda
    private Object[][] queue = null; //Coda di elementi
    private String[] sqlStatement = null; //Coda gestita parallelamente agli elementi da storicizzare con il testo della query da storicizzare

    public FDBQueue(int length)
    {
        this.length = (length <= 0) ? LENGTH : length;
        queue = new Object[this.length][];
        sqlStatement = new String[this.length];
    } //QueueRecords

    public void enqueueVariable(Object[] values, String sql)
    {
        queue[end] = values;
        sqlStatement[end] = sql;
        stepEnqueue();
    } //enqueueRecord

    public DataToWriteDb dequeueAllVariables()
    {
        return dequeueVariables(numElement);
    }

    public DataToWriteDb dequeueVariables(int numVariables)
    {
        if (numElement == 0)
        {
            return null;
        }

        int n = (numVariables < numElement) ? numVariables : numElement;
        Object[][] tmpValues = new Object[n][];
        String[] tmpStatements = new String[n];

        for (int i = 0; i < n; i++)
        {
            tmpValues[i] = queue[start];
            tmpStatements[i] = sqlStatement[start];
            queue[start] = null;
            sqlStatement[start] = null;
            stepDequeue();
        } //for

        return new DataToWriteDb(tmpValues, tmpStatements);
    } //enqueueRecord

    public String toString()
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
        String[] tmpSqls = new String[tmpLength];

        for (int i = 0; i < length; i++)
        {
            tmpQueue[i] = queue[(start + i) % length];
            tmpSqls[i] = sqlStatement[(start + i) % length];
        } //for

        start = 0;
        end = numElement - 1;
        length = tmpLength;
        queue = tmpQueue;
        sqlStatement = tmpSqls;
    } //resize
} //Class QueueRecords
