package com.carel.supervisor.base.io;

import java.io.*;


public class FileUtil
{
    private PrintWriter printerWriter = null;

    public FileUtil(File file, boolean appendMode) throws Exception
    {
        printerWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(file, appendMode)));
    }

    public FileUtil(String file, boolean appendMode) throws Exception
    {
        printerWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(file, appendMode)));
    }

    public static void replaceFile(File file, String text)
        throws Exception
    {
        PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(file, false)));
        out.print(text);
        out.flush();
        out.close();
    }

    public void appendFile(String text)
    {
        printerWriter.print(text);
        printerWriter.flush();
    }

    public void close() throws Exception
    {
        printerWriter.flush();
        printerWriter.close();
    }
}
