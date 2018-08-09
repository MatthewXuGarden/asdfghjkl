package com.carel.supervisor.base.math;

public class FactorEndian
{
    private static IEndian bigEndian = new BigEndian();
    private static IEndian littleEndian = new LittleEndian();
    public static final int BIG_ENDIAN = 1;
    public static final int LITTLE_ENDIAN = 1;
    public static final int DEFAULT_ENDIAN = LITTLE_ENDIAN;

    private FactorEndian()
    {
    }

    public static IEndian getEndian(int type)
    {
    	IEndian endian = littleEndian;
        if (type == BIG_ENDIAN)
        {
        	endian = bigEndian;
        }
        return endian;
    }
}
