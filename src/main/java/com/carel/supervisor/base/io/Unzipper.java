package com.carel.supervisor.base.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;


public class Unzipper
{
    public static final void copyInputStream(InputStream in, OutputStream out)
        throws IOException
    {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
        {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    public static final void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Usage: Unzipper zipfile path");
            System.exit(1);
        }

        String zipFileName = args[0];
        String path = args[1];

        try
        {
            unzipDir(zipFileName, path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }

    public static void unzipDir(String zipFileName, String path)
        throws Exception
    {
        Enumeration entries = null;
        ZipFile zipFile = null;

        zipFile = new ZipFile(zipFileName);

        entries = zipFile.entries();

        File dir = new File(path);

        if (!dir.exists())
        {
            dir.mkdir();
        }

        if (!path.endsWith(File.separator))
        {
            path = path + File.separator;
        }
 	
	    while (entries.hasMoreElements())
        {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            
            copyInputStream(zipFile.getInputStream(entry),
                new BufferedOutputStream(new FileOutputStream(path + entry.getName())));
        }

        zipFile.close();
    }
    
    public static void unzipDirSubDir(String zipFileName, String path)
    throws Exception
    {
    	Enumeration entries = null;
        ZipFile zipFile = null;

        zipFile = new ZipFile(zipFileName);

        entries = zipFile.entries();

        File dir = new File(path);

        if (!dir.exists())
        {
            dir.mkdir();
        }

        if (!path.endsWith(File.separator))
        {
            path = path + File.separator;
        }
                
        while (entries.hasMoreElements())
        {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            if (entry.isDirectory())
                continue;//ignore directories

            String fname = entry.getName();
            fname = fname.replace("/", "\\");
            int index = fname.lastIndexOf("\\");
            
            String filePath = path;
            
            if(index != -1)
            	filePath = path + fname.substring(0, fname.lastIndexOf("\\")) + File.separator;
            
            if(!new File(filePath).exists())
                new File(filePath).mkdirs();
            
            copyInputStream(zipFile.getInputStream(entry),
                new BufferedOutputStream(new FileOutputStream(path + entry.getName())));
        }

        zipFile.close();
    }
    
    
    /**
     * Runs a gunzip with inputfile=outputfile
     * I wrote it for gunzipping the Atvise files 
     * @param gzipInputFile
     * @throws Exception
     */
    public static void gunzipSameFile(File gzipInputFile) throws Exception{
   	 
    	
		byte[] buffer = new byte[1024];
		FileInputStream fis;
		GZIPInputStream gzis;
		FileOutputStream fos;
		final String ZIPTMP = ".ziptmp";
		
		try {
			
			if (gzipInputFile.length() == 0) {
				//Empty file. return without any further action
				return;
			}
			fis = new FileInputStream(gzipInputFile);
			// creates an output file with the same name as the input file but with .ziptmp extension
			File tmpFile = new File(gzipInputFile.getAbsolutePath()+ZIPTMP);
			//gzipInputFile.renameTo(tmpFile);
			gzis = new GZIPInputStream(fis);
		
			fos = new FileOutputStream(tmpFile);
		
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fis.close();
			gzis.close();
			fos.close();
			
			// we've the gunzipped content in the tmp file. Now let's drop the original
			// file and rename the tmp to the same name of the original file
			String originalName = gzipInputFile.getAbsolutePath();
			if(gzipInputFile.delete()) {				
				File f = new File(originalName);
				tmpFile.renameTo(f);
			}
			//else {
				// error while deleting the original file. Do nothing
			//}
		}
		catch (IOException ioe) {
			// something went wrong
			ioe.printStackTrace();
		}
		catch (Exception e) {
			throw e;
		}
       
    }
    
}
