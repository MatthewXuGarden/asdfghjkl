package com.carel.supervisor.base.io;

import java.io.*;


public class IOReader
{
    private IOReader()
    {
    }

    public static final String readFromInputStream(InputStream input)
        throws IOException
    {
        byte[] buff = new byte[10000];
        int read = 0;
        StringBuffer buffer = new StringBuffer();

        while ((read = input.read(buff)) > 0)
        {
            buffer.append(new String(buff, 0, read));
        }

        input.close();

        return buffer.toString();
    }
}
