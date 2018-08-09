package com.carel.supervisor.base.config;

import java.io.*;


public class FileNameFilter implements FilenameFilter
{
    private transient String fileName = null;

    public FileNameFilter(String fileName)
    {
        this.fileName = fileName;
    }

    public boolean accept(File dir, String name)
    {
        boolean code = false;

        if (this.fileName.equals("*"))
        {
            code = true;
        }
        else
        {
            if (name.equals(this.fileName))
            {
                code = true;
            }
            else
            {
                code = false;
            }
        }

        return code;
    }
}
