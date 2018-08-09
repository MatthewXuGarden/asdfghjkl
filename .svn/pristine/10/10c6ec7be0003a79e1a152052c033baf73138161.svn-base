/**
 * 
 */
package com.carel.supervisor.ide.dc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.CRC32;

import com.carel.supervisor.ide.dc.xmlDAO.ImportException;

/**
 * @author team pvpro 20090327
 *
 */
public class DeviceXMLCRC {

	/**
	 * Computes the CRC of the contents of Device tag
	 * @param doc
	 * @param xfactory
	 * @return the computed Device CRC in hex format
	 */
	public String computeCRC(File inputFile) throws ImportException
	{		
		String computedCRC = null;
		CRC32 crc = new CRC32();
		try {
			
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"UTF8"));
				String str = null;
	
				boolean evaluateRow = false;
				while (null != (str = br.readLine())) 
				{
					if (str.indexOf("<Device") != -1) 
						evaluateRow = true;
					
					if (evaluateRow) 
					{
						crc.update(str.getBytes("UTF8"));
						crc.update("\r\n".getBytes("UTF8"));
					}
					if (str.indexOf("</Device>") != -1) 
						break;
				}
				
				br.close();
				computedCRC = Long.toHexString(crc.getValue());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new ImportException("error",e,"Error while calculating CRC.");
			}
		return computedCRC;
	}
}
