package com.carel.supervisor.base.config;

import java.io.*;


public class ExtensionFilter implements FilenameFilter
{
    private transient String extension = null;

    public ExtensionFilter(String extension)
    {
        this.extension = extension;
    }

    public boolean accept(File dir, String name)
    {
        boolean code = false;

        if ("*".equals(extension))
        {
            code = true;
        }
        else
        {
            if (name.endsWith(this.extension))
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
