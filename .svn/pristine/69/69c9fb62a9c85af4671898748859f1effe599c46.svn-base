package com.carel.supervisor.base.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.carel.supervisor.base.log.LoggerMgr;


public class Zipper
{
    public static void main(String[] argv)
    {
        if (argv.length != 2)
        {
            System.err.println("Usage: Zipper path zipfile");

            return;
        }

        String dir2zip = argv[0];
        String file = argv[1];

        try
        {
            zipDir(dir2zip, file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }

    public static void zipDir(String dir2zip, String file)
        throws Exception
    {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        
        if (dirList.length > 0)
        {
	        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
	        
	        zos.setLevel(Deflater.BEST_SPEED);
	        
	        byte[] readBuffer = new byte[2156];
	        int bytesIn = 0;
	
	        for (int i = 0; i < dirList.length; i++)
	        {
	            File f = new File(zipDir, dirList[i]);
	
	            FileInputStream fis = new FileInputStream(f);
	
	            ZipEntry anEntry = new ZipEntry(f.getName());
	
	            zos.putNextEntry(anEntry);
	
	            while ((bytesIn = fis.read(readBuffer)) != -1)
	            {
	                zos.write(readBuffer, 0, bytesIn);
	            }
	
	            fis.close();
	        }
	        zos.close();
        }
    }
    
    public static void zipFiles(boolean deleteFile, String baseZipPath, String[] fileNames) throws FileNotFoundException
    {
    	String fileName = baseZipPath + ".zip";
    	File file = new File(fileName);
    	ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
        zos.setLevel(Deflater.BEST_SPEED);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
    	try{
	        for (int i = 0; i < fileNames.length; i++)
	        {
	            File f = new File(fileNames[i]);
	            FileInputStream fis = new FileInputStream(f);
	        	
	            ZipEntry anEntry = new ZipEntry(f.getName());
	
	            zos.putNextEntry(anEntry);
	
	            while ((bytesIn = fis.read(readBuffer)) != -1)
	            {
	                zos.write(readBuffer, 0, bytesIn);
	            }
	
	            fis.close();
	        }
	        zos.close();
	    	if(deleteFile)
	    	{
	    		for (int i = 0; i < fileNames.length; i++)
	            {
	            	(new File(fileNames[i])).delete();
	            }
	    	}
    	}
    	catch(Exception ex)
    	{
    		LoggerMgr.getLogger(Zipper.class).error("Error during file zip process: "+ex.getMessage());
    	}
    }
    //subFileNames[0] is the file name
    //subFileNames[...] is the path
    public static void zipFiles(boolean deleteFile, String baseZipPath, String[] fileNames,String[][] subFileNames) throws FileNotFoundException
    {
    	String fileName = baseZipPath + ".zip";
    	File file = new File(fileName);
    	ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
        zos.setLevel(Deflater.BEST_SPEED);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
    	try{
	        for (int i = 0; i < fileNames.length; i++)
	        {
	            File f = new File(fileNames[i]);
	            FileInputStream fis = new FileInputStream(f);
	        	
	            ZipEntry anEntry = new ZipEntry(f.getName());
	
	            zos.putNextEntry(anEntry);
	
	            while ((bytesIn = fis.read(readBuffer)) != -1)
	            {
	                zos.write(readBuffer, 0, bytesIn);
	            }
	
	            fis.close();
	        }
	        for (int i = 0; i < subFileNames.length; i++)
	        {
	            File f = new File(subFileNames[i][0]);
	            FileInputStream fis = new FileInputStream(f);
	        	
	            String folderStr = "";
	            for(int j=1;j<subFileNames[i].length;j++)
	            {
	            	folderStr += subFileNames[i][j]+File.separator;
	            }
	            ZipEntry anEntry = new ZipEntry(folderStr+f.getName());
	
	            zos.putNextEntry(anEntry);
	
	            while ((bytesIn = fis.read(readBuffer)) != -1)
	            {
	                zos.write(readBuffer, 0, bytesIn);
	            }
	
	            fis.close();
	        }
	        zos.close();
	    	if(deleteFile)
	    	{
	    		for (int i = 0; i < fileNames.length; i++)
	            {
	            	(new File(fileNames[i])).delete();
	            }
	    		for (int i = 0; i < subFileNames.length; i++)
		        {
	    			(new File(subFileNames[i][0])).delete();
		        }
	    	}
    	}
    	catch(Exception ex)
    	{
    		LoggerMgr.getLogger(Zipper.class).error("Error during file zip process: "+ex.getMessage());
    	}
    }
}
