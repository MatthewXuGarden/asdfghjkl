package com.carel.supervisor.base.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class BufferFileWriter
{
    private static final int NUM = 200;
    private String[] list = new String[NUM];
    private int count = -1;
    private PrintWriter printerWriter = null;
    private static final Logger logger = LoggerMgr.getLogger(BufferFileWriter.class);
    
    public BufferFileWriter(String fileName, boolean appendMode)
    {
        try
        {
            if (null != fileName)
            {
                printerWriter = new PrintWriter(new BufferedWriter(
                            new FileWriter(fileName, appendMode)));
            }
        }
        catch (IOException e)
        {
            logger.error(e);
        }
    }

    public void write(String line)
    {
        if (count < NUM)
        {
            count++;
            list[count] = line;
        }
        else
        {
            count = -1;

            for (int i = 0; i < NUM; i++)
            {
                print(list[i]);
                list[i] = null;
            }

            flush();
            write(line);
        }
    }

    public void close() throws Exception
    {
        for (int i = 0; i <= count; i++)
        {
            print(list[i]);
            list[i] = null;
        }

        flush();

        if (null != printerWriter)
        {
            printerWriter.close();
        }
    }

    private void print(String line)
    {
        if (null == printerWriter)
        {
            System.out.println(line);
        }
        else
        {
            printerWriter.print(line);
        }
    }

    private void flush()
    {
        if (null != printerWriter)
        {
            printerWriter.flush();
        }
    }
}
