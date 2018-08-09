package com.carel.supervisor.base.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class FileSystemUtils
{
    private FileSystemUtils()
    {
    }

    public static void copyFile(File in, File out) throws Exception
    {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        copyFile(fis, fos);
    }

    public static void copyFile(FileInputStream fis, FileOutputStream fos)
        throws Exception
    {
        byte[] buf = new byte[1024];
        int i = 0;

        while ((i = fis.read(buf)) != -1)
        {
            fos.write(buf, 0, i);
        }

        fis.close();
        fos.close();
    }

    public static void deleteDir(String path, String fileToExclude)
    {
        File f = new File(path);
        if(!f.exists())
        	return;
        File[] list = f.listFiles();

        for (int i = 0; i < list.length; i++)
        {
            if (null == fileToExclude)
            {
            	list[i].delete();
            }
            else if (!list[i].getName().endsWith(fileToExclude))
            {
                list[i].delete();
            }
        }
    }
}
