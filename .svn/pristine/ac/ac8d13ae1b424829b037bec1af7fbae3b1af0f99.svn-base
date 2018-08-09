package com.carel.supervisor.base.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipperFile
{
    public static void saveFile(String file, String data, boolean append)
        throws IOException
    {
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;

        File f = new File(file);
        FileOutputStream fos = new FileOutputStream(f, append);

        try
        {
            // write UTF8 BOM mark if file is empty
            if (f.length() < 1)
            {
                final byte[] bom = new byte[]
                    {
                        (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
                    };
                fos.write(bom);
            }

            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);

            if (data != null)
            {
                bw.write(data);
            }
        }
        catch (IOException ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                bw.close();
                fos.close();
            }
            catch (Exception ex)
            {
            }
        }
    }

    public static void zip(String path, String fileName, String origExtension,
        String data) throws Exception
    {
        FileOutputStream file = new FileOutputStream(path + fileName + ".zip",
                false);
        ZipOutputStream zipOut = new ZipOutputStream(file);
        zipOut.setLevel(9);

        StringReader reader = new StringReader(data);
        BufferedReader in = new BufferedReader(reader);

        zipOut.putNextEntry(new ZipEntry(fileName + "." + origExtension));

        //final byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        //zipOut.write(bom);
        OutputStreamWriter osw = new OutputStreamWriter(zipOut, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        if (data != null)
        {
            bw.write(data);
        }

        bw.flush();
        bw.close();
        in.close();

        //zipOut.finish();
        //zipOut.close();
        file.flush();
        file.close();
    }

    public static void zip(String path, String zipname, String[] fileName,
        String origExtension, String[] data) throws Exception
    {
        FileOutputStream file = new FileOutputStream(path + "SITE" + ".zip",
                false);
        ZipOutputStream zipOut = new ZipOutputStream(file);
        zipOut.setLevel(9);

        OutputStreamWriter osw = new OutputStreamWriter(zipOut, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        if (data.length > 0)
        {
            for (int i = 0; i < data.length; i++)
            {
                StringReader reader = new StringReader(data[i]);
                BufferedReader in = new BufferedReader(reader);
                zipOut.putNextEntry(new ZipEntry(fileName[i] + "." +
                        origExtension));

                if (data[i] != null)
                {
                    bw.write(data[i]);
                }

                bw.flush();
                in.close();
            }

            bw.close();
        }

        file.flush();
        file.close();
    }

    public static String unzip(String path, String fileName, String data,
        String code) throws Exception
    {
        FileInputStream file = new FileInputStream(path + fileName + ".zip");
        ZipInputStream zipIn = new ZipInputStream(file);
        ZipEntry zipEntry = zipIn.getNextEntry();
        int iNum = (int) zipEntry.getSize();
        byte[] btUnzip = new byte[iNum];
        int i = 0;
        int c;

        while ((c = zipIn.read()) != -1)
        {
            btUnzip[i] = (byte) c;
            i++;
        }

        zipIn.close();

        String unzip = new String(btUnzip, code); //"ISO-8859-1");

        return unzip;
    }
}
