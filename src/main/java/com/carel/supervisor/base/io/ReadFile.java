package com.carel.supervisor.base.io;

import java.io.*;
import java.net.*;


public class ReadFile
{
    private ReadFile()
    {
    }

    public static final String readFromFile(String fileName)
        throws IOException
    {
        InputStreamReader input;
        input = new InputStreamReader(new FileInputStream(fileName));

        StringBuffer buffer = new StringBuffer();
        char[] c = new char[10000];
        int read = 0;

        while ((read = input.read(c)) > 0)
        {
            buffer.append(new String(c, 0, read));
        }

        input.close();

        return buffer.toString();
    }

    public static final String readFromFile(URL url) throws IOException
    {
        InputStreamReader input;
        input = new InputStreamReader(url.openStream());

        StringBuffer buffer = new StringBuffer();
        char[] c = new char[10000];
        int read = 0;

        while ((read = input.read(c)) > 0)
        {
            buffer.append(new String(c, 0, read));
        }

        input.close();

        return buffer.toString();
    }
}
