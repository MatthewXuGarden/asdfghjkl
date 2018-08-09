package com.carel.supervisor.base.math;

public class LittleEndian implements IEndian
{

    public char[] concat(byte[] src, int from, char[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new char[toTmp + (amount / 2)];
        }

        for (int i = 0; i < amount; i += 2)
        {
            dest[toTmp++] = (char) (((src[fromTmp + 0] & 0xff) << 0) |
                ((src[fromTmp + 1] & 0xff) << 8));
            fromTmp += 2;
        }

        return dest;
    }

    public short[] concat(byte[] src, int from, short[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new short[toTmp + (amount / 2)];
        }

        for (int i = 0; i < amount; i += 2)
        {
            dest[toTmp++] = (short) (((src[fromTmp + 0] & 0xff) << 0) |
                ((src[fromTmp + 1] & 0xff) << 8));
            fromTmp += 2;
        }

        return dest;
    }

    public int[] concat(byte[] src, int from, int[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new int[toTmp + (amount / 4)];
        }

        for (int i = 0; i < amount; i += 4)
        {
            dest[toTmp++] = (int) (((src[fromTmp + 0] & 0xff) << 0) |
                ((src[fromTmp + 1] & 0xff) << 8) | ((src[fromTmp + 2] & 0xff) << 16) |
                ((src[fromTmp + 3] & 0xff) << 24));
            fromTmp += 4;
        }

        return dest;
    }

    public long[] concat(byte[] src, int from, long[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new long[toTmp + (amount / 8)];
        }

        for (int i = 0; i < amount; i += 8)
        {
            dest[toTmp++] = (long) (((src[fromTmp + 0] & 0xffL) << 0) |
                ((src[fromTmp + 1] & 0xffL) << 8) |
                ((src[fromTmp + 2] & 0xffL) << 16) |
                ((src[fromTmp + 3] & 0xffL) << 24) |
                ((src[fromTmp + 4] & 0xffL) << 32) |
                ((src[fromTmp + 5] & 0xffL) << 40) |
                ((src[fromTmp + 6] & 0xffL) << 48) |
                ((src[fromTmp + 7] & 0xffL) << 56));
            fromTmp += 8;
        }

        return dest;
    }

    public byte[] split(char[] src, int from, byte[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new byte[toTmp + (amount * 2)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (byte) (src[fromTmp] >> 0);
            dest[toTmp++] = (byte) (src[fromTmp] >> 8);
            ++fromTmp;
        }

        return dest;
    }

    public int[] concat(char[] src, int from, int[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new int[toTmp + (amount / 2)];
        }

        for (int i = 0; i < amount; i += 2)
        {
            dest[toTmp++] = (int) (((src[fromTmp + 0] & 0xffff) << 0) |
                ((src[fromTmp + 1] & 0xffff) << 16));
            fromTmp += 2;
        }

        return dest;
    }

    public long[] concat(char[] src, int from, long[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new long[toTmp + (amount / 4)];
        }

        for (int i = 0; i < amount; i += 4)
        {
            dest[toTmp++] = (long) (((src[fromTmp + 0] & 0xffffL) << 0) |
                ((src[fromTmp + 1] & 0xffffL) << 16) |
                ((src[fromTmp + 2] & 0xffffL) << 32) |
                ((src[fromTmp + 3] & 0xffffL) << 48));
            fromTmp += 4;
        }

        return dest;
    }

    public byte[] split(short[] src, int from, byte[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new byte[toTmp + (amount * 2)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (byte) (src[fromTmp] >> 0);
            dest[toTmp++] = (byte) (src[fromTmp] >> 8);
            ++fromTmp;
        }

        return dest;
    }

    public int[] concat(short[] src, int from, int[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new int[toTmp + (amount / 2)];
        }

        for (int i = 0; i < amount; i += 2)
        {
            dest[toTmp++] = (int) (((src[fromTmp + 0] & 0xffff) << 0) |
                ((src[fromTmp + 1] & 0xffff) << 16));
            fromTmp += 2;
        }

        return dest;
    }

    public long[] concat(short[] src, int from, long[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new long[toTmp + (amount / 4)];
        }

        for (int i = 0; i < amount; i += 4)
        {
            dest[toTmp++] = (long) (((src[fromTmp + 0] & 0xffffL) << 0) |
                ((src[fromTmp + 1] & 0xffffL) << 16) |
                ((src[fromTmp + 2] & 0xffffL) << 32) |
                ((src[fromTmp + 3] & 0xffffL) << 48));
            fromTmp += 4;
        }

        return dest;
    }

    public byte[] split(int[] src, int from, byte[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new byte[toTmp + (amount * 4)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (byte) (src[fromTmp] >> 0);
            dest[toTmp++] = (byte) (src[fromTmp] >> 8);
            dest[toTmp++] = (byte) (src[fromTmp] >> 16);
            dest[toTmp++] = (byte) (src[fromTmp] >> 24);
            ++fromTmp;
        }

        return dest;
    }

    public char[] split(int[] src, int from, char[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new char[toTmp + (amount * 2)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (char) (src[fromTmp] >> 0);
            dest[toTmp++] = (char) (src[fromTmp] >> 16);
            ++fromTmp;
        }

        return dest;
    }

    public short[] split(int[] src, int from, short[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new short[toTmp + (amount * 2)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (short) (src[fromTmp] >> 0);
            dest[toTmp++] = (short) (src[fromTmp] >> 16);
            ++fromTmp;
        }

        return dest;
    }

    public long[] concat(int[] src, int from, long[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new long[toTmp + (amount / 2)];
        }

        for (int i = 0; i < amount; i += 2)
        {
            dest[toTmp++] = (long) (((src[fromTmp + 0] & 0xffffffffL) << 0) |
                ((src[fromTmp + 1] & 0xffffffffL) << 32));
            fromTmp += 2;
        }

        return dest;
    }

    public byte[] split(long[] src, int from, byte[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new byte[toTmp + (amount * 8)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (byte) (src[fromTmp] >> 0);
            dest[toTmp++] = (byte) (src[fromTmp] >> 8);
            dest[toTmp++] = (byte) (src[fromTmp] >> 16);
            dest[toTmp++] = (byte) (src[fromTmp] >> 24);
            dest[toTmp++] = (byte) (src[fromTmp] >> 32);
            dest[toTmp++] = (byte) (src[fromTmp] >> 40);
            dest[toTmp++] = (byte) (src[fromTmp] >> 48);
            dest[toTmp++] = (byte) (src[fromTmp] >> 56);
            ++fromTmp;
        }

        return dest;
    }

    public char[] split(long[] src, int from, char[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new char[toTmp + (amount * 4)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (char) (src[fromTmp] >> 0);
            dest[toTmp++] = (char) (src[fromTmp] >> 16);
            dest[toTmp++] = (char) (src[fromTmp] >> 32);
            dest[toTmp++] = (char) (src[fromTmp] >> 48);
            ++fromTmp;
        }

        return dest;
    }

    public short[] split(long[] src, int from, short[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new short[toTmp + (amount * 4)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (short) (src[fromTmp] >> 0);
            dest[toTmp++] = (short) (src[fromTmp] >> 16);
            dest[toTmp++] = (short) (src[fromTmp] >> 32);
            dest[toTmp++] = (short) (src[fromTmp] >> 48);
            ++fromTmp;
        }

        return dest;
    }

    public int[] split(long[] src, int from, int[] dest, int to, int amount)
    {
    	int fromTmp = from;
    	int toTmp = to;
    	if (dest == null)
        {
            dest = new int[toTmp + (amount * 2)];
        }

        for (int i = 0; i < amount; ++i)
        {
            dest[toTmp++] = (int) (src[fromTmp] >> 0);
            dest[toTmp++] = (int) (src[fromTmp] >> 32);
            ++fromTmp;
        }

        return dest;
    }
}
