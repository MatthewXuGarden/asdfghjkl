package com.carel.supervisor.base.system;

import java.text.DecimalFormat;


public class SystemInfo
{
    private static SystemInfo me = new SystemInfo();

    private SystemInfo()
    {
        try
        {
            System.loadLibrary("systeminfo");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public native void retrieveHDInfo(DiskInfoList diskInfoList);

    public native long getTotalRam();

    public native long getFreeRam();

    public native short getCPUInfo();

    public native String getCPUID();

    public native String getOS();

    public native short[] getMacAddress();
    
    public native int getRAMUsage();
    
    public native int getCPUUsage();

    public native int serviceStatus(String serviceName, String machineName);

    public static SystemInfo getInstance()
    {
        return me;
    }

    public String getMacAddreesString()
    {
        short[] mac = getMacAddress();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < mac.length; i++)
        {
            buffer.append(zero(String.valueOf(mac[i]), 3));

            if (i < (mac.length - 1))
            {
                buffer.append("-");
            }
        }

        return buffer.toString();
    }

    private String zero(String s, int num)
    {
        String tmp = "";

        for (int i = 0; i < (num - s.length()); i++)
        {
            tmp += "0";
        }

        tmp += s;

        return tmp;
    }

    public String getTotalRamString()
    {
        long l = getTotalRam();
        DecimalFormat d = new DecimalFormat();
        d.setGroupingUsed(true);

        String s = d.format(l) + " KB";

        return s;
    }

    public String getUsedRamString()
    {
        long l = getTotalRam() - getFreeRam();
        DecimalFormat d = new DecimalFormat();
        d.setGroupingUsed(true);

        String s = d.format(l) + " KB";

        return s;
    }

    public long[] getDiskUsage(String disk)
    {
        DiskInfo diskInfo = null;
        DiskInfoList diskInfoList = new DiskInfoList();
        SystemInfo.getInstance().retrieveHDInfo(diskInfoList);
        diskInfo = diskInfoList.getDiskInfoByName(disk);
        if (null == diskInfo)
        {
            return null;
        }
        long[] usage = new long[2];
        usage[0] = diskInfo.getFree();
        usage[1] = diskInfo.getUsed();
        return usage;
    }
    
    public String[] getDiskUsageString(String disk)
    {
        long[] l = getDiskUsage(disk);
    	
        if (null == l)
        {
            return new String[] { "", "" };
        }

        String[] usage = new String[2];
        DecimalFormat d = new DecimalFormat();
        d.setGroupingUsed(true);
        usage[0] = d.format(l[0]) + " MB";
        usage[1] = d.format(l[1]) + " MB";

        return usage;
    }
    /**2010-2-2
     * simon note: it will be used by diagnose.bat. it will generate some info to export.
     * please do not remove it
     */
    public static void main(String[] argv)
    {
        DiskInfoList diskInfoList = new DiskInfoList();
        SystemInfo.getInstance().retrieveHDInfo(diskInfoList);

        DiskInfo diskInfo = null;

        for (int i = 0; i < diskInfoList.size(); i++)
        {
            diskInfo = diskInfoList.getDiskInfo(i);
            System.out.println("DRIVER : " + diskInfo.getDriver());
            System.out.println("TOTAL : " + diskInfo.getTotal()+" MB");
            System.out.println("USED : " + diskInfo.getUsed()+" MB");
            System.out.println("FREE : " + diskInfo.getFree()+" MB");
            System.out.println("SERIAL : " + diskInfo.getSerialNumber());
            System.out.println("FILE SYSTEM : " + diskInfo.getFileSystem());
        }

        System.out.println("-------------------------");

        System.out.println("TOTAL RAM :" + SystemInfo.getInstance().getTotalRam()+" MB");
        System.out.println("FREE RAM :" + SystemInfo.getInstance().getFreeRam()+" MB");

        System.out.println("NUM PROCESSOR :" + SystemInfo.getInstance().getCPUInfo());
        System.out.println("-------------------------");
//        System.out.println(SystemInfo.getInstance().getCPUID());

        //System.out.println(SystemInfo.getInstance().getCPUVendorID());
//        System.out.println("CPU Usage :" + SystemInfo.getInstance().getCPUUsage()+" %");
        System.out.println(SystemInfoExt.getInstance().getCpuInfo());
        System.out.println("CPU Per Usage :" + SystemInfoExt.getInstance().getCpuPerUsage());
        System.out.println("-------------------------");
        System.out.println(SystemInfo.getInstance().getOS());

//        short[] mac = SystemInfo.getInstance().getMacAddress();
//
//        for (int i = 0; i < mac.length; i++)
//        {
//            System.out.print(mac[i]);
//            System.out.print("-");
//        }
    }
}
