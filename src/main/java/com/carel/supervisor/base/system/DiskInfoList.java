package com.carel.supervisor.base.system;

import java.util.ArrayList;
import java.util.List;


public class DiskInfoList
{
    private List list = new ArrayList();

    public DiskInfoList()
    {
    }

    public void add(String driver, long total, long used, long free, String serialNumber,
        String fileSystem)
    {
        DiskInfo diskInfo = new DiskInfo();
        diskInfo.setDriver(driver);
        diskInfo.setTotal(total);
        diskInfo.setUsed(used);
        diskInfo.setFree(free);
        diskInfo.setSerialNumber(serialNumber);
        diskInfo.setFileSystem(fileSystem);
        list.add(diskInfo);
    }

    public int size()
    {
        return list.size();
    }

    public DiskInfo getDiskInfo(int i)
    {
        return (DiskInfo) list.get(i);
    }

    public DiskInfo getDiskInfoByName(String name)
    {
        DiskInfo diskInfo = null;

        for (int i = 0; i < size(); i++)
        {
            diskInfo = getDiskInfo(i);

            if (diskInfo.getDriver().toUpperCase().startsWith(name.toUpperCase()))
            {
                return diskInfo;
            }
        }

        return null;
    }
}
