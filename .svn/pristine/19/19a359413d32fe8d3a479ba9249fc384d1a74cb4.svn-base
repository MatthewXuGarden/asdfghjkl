package com.carel.supervisor.ide.dc.DbModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import sun.misc.BASE64Decoder;

public class Utils {
	public static void decodeImage(String imageString, String fileName, String outPath) throws Exception{
		try {
			BASE64Decoder b64Decoder = new BASE64Decoder();
			File customFilePath = new File(outPath) ;
			if(!customFilePath.isDirectory()){
				customFilePath.mkdir();
			}
			OutputStream byteOS = new FileOutputStream(outPath + File.separator + fileName);
			byteOS.write(b64Decoder.decodeBuffer(imageString));
			byteOS.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String booleanToPvString(boolean var)
	{
		String result = "FALSE";
		if(var)
			result = "TRUE";
		return result;
	}
}

