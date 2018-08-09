package com.carel.supervisor.director;

import java.util.Properties;


public class PolicyDequeue
{
    private final static String NORMAL_NUM_DEQUEUE = "normaleNumDequeue";
    private final static String NORMAL_NUM_GCDWRITER = "normalNumGCDwriter";
    private final static String EMERGENCY_NUM_DEQUEUE = "emergencyNumDequeue";
    private final static String EMERGENCY_NUM_GCDWRITER = "emergencyNumGCDwriter";
    private final static String EMERGENCY_THREASHOLD = "threasholdEmergency";
    private final static int DEFAULT_NORMAL_NUM_DEQUEUE = 20;
    private final static int DEFAULT_NORMAL_NUM_GCDWRITER = 1;
    private final static int DEFAULT_EMERGENCY_NUM_DEQUEUE = 100;
    private final static int DEFAULT_EMERGENCY_NUM_GCDWRITER = 1;
    private final static int DEFAULT_EMERGENCY_THREASHOLD = 6000;
    private int normalNumDequeue = 0;
    private int normalNumGCDWriter = 0;
    private int emergencyNumDequeue = 0;
    private int emergencyNumGCDWriter = 0;
    private int threasholdEmergency = 0;

    public PolicyDequeue(Properties prop)
    {
        String num = prop.getProperty(NORMAL_NUM_DEQUEUE);
        String num1 = prop.getProperty(NORMAL_NUM_GCDWRITER);

        normalNumDequeue = parse(num, DEFAULT_NORMAL_NUM_DEQUEUE);
        normalNumGCDWriter = parse(num1, DEFAULT_NORMAL_NUM_GCDWRITER);

        num = prop.getProperty(EMERGENCY_NUM_DEQUEUE);
        num1 = prop.getProperty(EMERGENCY_NUM_GCDWRITER);

        emergencyNumDequeue = parse(num, DEFAULT_EMERGENCY_NUM_DEQUEUE);
        emergencyNumGCDWriter = parse(num1, DEFAULT_EMERGENCY_NUM_GCDWRITER);

        num = prop.getProperty(EMERGENCY_THREASHOLD);
        threasholdEmergency = parse(num, DEFAULT_EMERGENCY_THREASHOLD);
    }

    private int parse(String num, int defaultValue)
    {
        try
        {
            return Integer.parseInt(num);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    /**
     * @return: int
     */
    public int getEmergencyNumDequeue()
    {
        return emergencyNumDequeue;
    }

    /**
     * @param emergencyNumDequeue
     */
    public void setEmergencyNumDequeue(int emergencyNumDequeue)
    {
        this.emergencyNumDequeue = emergencyNumDequeue;
    }

    /**
     * @return: int
     */
    public int getEmergencyNumGCDWriter()
    {
        return emergencyNumGCDWriter;
    }

    /**
     * @param emergencyNumGCDWriter
     */
    public void setEmergencyNumGCDWriter(int emergencyNumGCDWriter)
    {
        this.emergencyNumGCDWriter = emergencyNumGCDWriter;
    }

    /**
     * @return: int
     */
    public int getNormalNumDequeue()
    {
        return normalNumDequeue;
    }

    /**
     * @param normalNumDequeue
     */
    public void setNormalNumDequeue(int normalNumDequeue)
    {
        this.normalNumDequeue = normalNumDequeue;
    }

    /**
     * @return: int
     */
    public int getNormalNumGCDWriter()
    {
        return normalNumGCDWriter;
    }

    /**
     * @param normalNumGCDWriter
     */
    public void setNormalNumGCDWriter(int normalNumGCDWriter)
    {
        this.normalNumGCDWriter = normalNumGCDWriter;
    }

    /**
     * @return: int
     */
    public int getThreasholdEmergency()
    {
        return threasholdEmergency;
    }

    /**
     * @param threasholdEmergency
     */
    public void setThreasholdEmergency(int threasholdEmergency)
    {
        this.threasholdEmergency = threasholdEmergency;
    }
}
