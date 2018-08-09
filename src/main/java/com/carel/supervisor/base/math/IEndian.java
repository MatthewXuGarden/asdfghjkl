package com.carel.supervisor.base.math;

public interface IEndian
{
    public abstract char[] concat(byte[] src, int from, char[] dest, int to,
        int amount);

    public abstract short[] concat(byte[] src, int from, short[] dest, int to,
        int amount);

    public abstract int[] concat(byte[] src, int from, int[] dest, int to,
        int amount);

    public abstract long[] concat(byte[] src, int from, long[] dest, int to,
        int amount);

    public abstract byte[] split(char[] src, int from, byte[] dest, int to,
        int amount);

    public abstract int[] concat(char[] src, int from, int[] dest, int to,
        int amount);

    public abstract long[] concat(char[] src, int from, long[] dest, int to,
        int amount);

    public abstract byte[] split(short[] src, int from, byte[] dest, int to,
        int amount);

    public abstract int[] concat(short[] src, int from, int[] dest, int to,
        int amount);

    public abstract long[] concat(short[] src, int from, long[] dest, int to,
        int amount);

    public abstract byte[] split(int[] src, int from, byte[] dest, int to,
        int amount);

    public abstract char[] split(int[] src, int from, char[] dest, int to,
        int amount);

    public abstract short[] split(int[] src, int from, short[] dest, int to,
        int amount);

    public abstract long[] concat(int[] src, int from, long[] dest, int to,
        int amount);

    public abstract byte[] split(long[] src, int from, byte[] dest, int to,
        int amount);

    public abstract char[] split(long[] src, int from, char[] dest, int to,
        int amount);

    public abstract short[] split(long[] src, int from, short[] dest, int to,
        int amount);

    public abstract int[] split(long[] src, int from, int[] dest, int to,
        int amount);
}
