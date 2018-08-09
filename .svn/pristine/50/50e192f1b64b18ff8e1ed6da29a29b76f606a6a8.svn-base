package com.carel.supervisor.base.math;

public class BitManipulation
{
    private final static byte BYTE_LENGTH = 8;
    private final static long MASK = 0xFF;

    
    public static long extractNumber(long number, int start, int length)
    {
        return extractNumberCarel(number,start,length);
    }
    
    public static long extractNumberModbus(long number, int start, int length)
    {
        long mask = 0;
        long result = 0;

        for (int i = 0; i < length; i++)
        {
            mask |= (((long) 1) << (start - i));
        }

        result = number & mask;

        return result >> (start - length + 1);
    } //extractNumber

    
    public static long extractNumberCarel(long number, int start, int length)
    {
        long mask = 0;

        for (int i = 0; i < length; i++)
        {
            mask |= (1L << i);
        }

        return (number >> start) & mask;
    } //extractNumber
    
    //fonde in un intero INT[Float[N-1]|   |Float[0]=dimesione in position[0]]
    public static int fusionNumberCarel(Float[] values, int []position)
    {
    	long mask =  0xFFFF;
    	long pos=0;
        int finalValue=0;
        long value=0;
        for(int i=0;i<values.length;i++){
        	mask =  0xFFFF;
        	value=values[i].longValue();
        	mask>>=(0x10-position[i]);
        	finalValue|=(mask & value)<<pos;
        	pos+=position[i];                         
        }//for
        return finalValue;
    } //extractNumber

    public static long fusionNumber(byte[] numbers, boolean isBigEndian, boolean signed)
        throws Exception
    {
        if (numbers.length > BYTE_LENGTH)
        {
            throw new Exception("Format number error");
        }

        long result = 0;

        if (isBigEndian)
        {
            for (int i = 0; i < numbers.length; i++)
            {
                result |= ((numbers[i] & MASK) << (BYTE_LENGTH * i));
            }
        }
        else
        {
            for (int i = numbers.length-1; i>=0; i--)
            {
                result |= ((numbers[i] & MASK) << (BYTE_LENGTH * (numbers.length-1-i)));
            }
        }

        if (signed)
        {
            if (numbers.length == 2)
            {
                result = (short) result;
            }
            else if (numbers.length == 4)
            {
                result = (int) result;
            }
        }

        return result;
    } //fusionNumber
    
    public static boolean andMask(float value,int mask) 
	{
		int a = (int)value;
		if((a & mask) == mask)
			return true;
		else
			return false;
	}
    
    /*
     * Converte un array di byte in un intero
     */
    public static int byteArrayToInt(byte[] b, int offset) 
    {
        int value = 0;
        for (int i = 0; i < 4; i++) 
        {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
    
    public static int NewFusionNumberCarel(Float[] values, int []dimension, int []offset,int valueOfDimension)
    {
        long mask = 0xFFFF;
        long value=0;
        
        for(int i=0;i<values.length;i++)
        {
            mask = 0xFFFF;
            value = values[i].longValue();
            mask >>= (0x10-dimension[i]);
            //reset the value to 0 in varlenth size first
        	for(int j=offset[i];j<offset[i]+dimension[i];j++)
        	{
        		valueOfDimension &= ~(1<<j);
        	}
        	//update the value with real value
            valueOfDimension |= (mask & value) << offset[i];
        }
        
        return valueOfDimension;
    }
}
