package com.carel.supervisor.presentation.bo.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zippa ricorsivamente una directory
 */
public class Zipper {
	
	private static final int BUFFER = 4096;
	private static ArrayList fileToesclude = new ArrayList();
	
	/**
	 * 
	 * @param dir2zip (path directory da zippare)
	 * @param zipName (nome del file (senza estensione .zip))
	 * @param dirToSaveZip (path directory dove salvare il file zip creato)
	 */
	public static void zip(String dir2zip,String zipName,String dirToSaveZip){
		try {
			FileOutputStream dest = new FileOutputStream(new File(dirToSaveZip+File.separator+zipName+".zip"));
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			ZipOutputStream out = new ZipOutputStream(checksum);
			zipDir(out,new File(dir2zip));
			out.close();
		} catch (Exception e) {
		}
	}
	
	private static void zipDir(ZipOutputStream out,File dir2zip){
	  try {	 
		String[] dirList = dir2zip.list(); 
        byte[] readBuffer = new byte[BUFFER]; 
        int bytesIn = 0;  
        for(int i=0; i<dirList.length; i++) 
        { 
            File f = new File(dir2zip,dirList[i]); 
            if(f.isDirectory()) 
            { 
            	//Se ho una directory richiamo la funzione in modo ricorsivo per aggiungere tutto il contenuto
            	//allo zip 
            	String filePath = f.getPath(); 
            	zipDir(out,new File(filePath)); 
            	
            	continue; 
            }
         if(!fileToesclude.contains(f.getName()))
         {	 
            //ho trovato un file da aggiungere allo zip
            FileInputStream fis = new FileInputStream(f); 
            ZipEntry anEntry = new ZipEntry(f.getPath());  
            out.putNextEntry(anEntry); 
            //scrivo il contenuto del file nello ZipOutputStream 
            while((bytesIn = fis.read(readBuffer)) != -1) 
            { 
            	out.write(readBuffer, 0, bytesIn); 
            } 
           //chiudo lo stream 
           fis.close();
           out.closeEntry();
         }
        }
	  }catch (Exception e) {
	    //e.printStackTrace();
	  }
	}
	/**
	 * Unzip il file (.zip) pozionando i file scompattati nella loro directory di origine
	 * @param zipped
	 * @throws IOException
	 */
	public static void unzip(File zipped) throws IOException
	{
		if(zipped.length() == 0 || !zipped.exists()) //Se il file zip è vuoto o non esiste
			return;
		
		BufferedOutputStream bos = null;
		FileInputStream fis = new FileInputStream(zipped);
		CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null)
		{
			File file=new File(entry.getName());
			 if(!file.getParentFile().exists())
				 file.getParentFile().mkdirs();
				
			int count;
			byte data[] = new byte[BUFFER];
			FileOutputStream fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos, BUFFER);
			while ((count = zis.read(data, 0, BUFFER)) != -1)
			{
				bos.write(data, 0, count);
			}
			bos.flush();
			bos.close();
		}
		zis.close();
	}
	/**
	 * Esclude il file dallo zip
	 * @param nomeFile
	 */
	public static void addFileToEscludeFromZip(String nomeFile){
		fileToesclude.add(nomeFile);
	}
		
	public static void main(String a[]) throws Exception{
		//Zipper.addFileToEscludeFromZip("UpdService.jar");
		//Zipper.zip("C:\\tmp");
		//Zipper.unzip(new File("C:\\Carel\\prova\\zip\\pappa.zip"));
		//System.out.println("Fine operazione");
	}
}
