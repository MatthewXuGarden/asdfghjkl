package com.carel.supervisor.updater;

import java.io.File;
import java.io.FileOutputStream;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;

public class UpdateMgr implements IInitializable
{
	private static UpdateMgr mm = new UpdateMgr();
	public static final String ERRORCHECK = "ERRORCHECK";
	public static final String UNZIPCHECK = "UNZIPCHECK";
	
	private String directory = "";
	private String servicename = "";
	private String log = "";
	
	private long token = 0L;
	
	private UpdateMgr() {
	}
	
	public static UpdateMgr getInstance() {
        return mm;
    }
	
	public void init(XMLNode xmlStatic) 
		throws InvalidConfigurationException 
	{
		directory = xmlStatic.getNode("directory").getAttribute("value");
		servicename = xmlStatic.getNode("service").getAttribute("value");
		log = xmlStatic.getNode("log").getAttribute("value");
	}
	
	public String getServicename() {
		return servicename;
	}

	public String getDirectory() {
		return directory;
	}
	
	public long getToken() {
		return token;
	}
	
	public boolean startDaemon() 
	{
		boolean ris = false;
		ScriptInvoker si = new ScriptInvoker();
		try 
		{
			String dirLog = this.directory + File.separator + this.log;
			si.execute(new String[]{"net","start",this.servicename}, dirLog);
			ris = true;
		} 
		catch (Exception e) 
		{
			ris = false;
			Logger logger = LoggerMgr.getLogger(UpdateMgr.class);
			logger.error(e);
		}
		return ris;
	}
	
	/**
	 * Check authentication and send back the software version
	 */
	public String checkAndGetVersion(String sIdent, String sPassw)
	{
		String version = "***";
		try
        {
			SiteInfo si = SiteInfoList.retrieveSiteById(1);
			if (si != null) {
				if (si.getCode() != null && si.getCode().equals(sIdent)
						&& si.getPassword() != null
						&& si.getPassword().equals(sPassw)) 
				{
					version = ProductInfoMgr.getInstance().getProductInfo().get("version");
					String fix = ProductInfoMgr.getInstance().getProductInfo().get("fix");
					if(fix != null && fix.trim().length() > 0)
						version += "." + fix;
				} 
			}
        }
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(UpdateMgr.class);
            logger.error(e);
            version = "***";
        }
		return version;
	}
	
	/**
	 * Prepare directory for upload SP file
	 */
	public void initDirectoryForUpload()
	{
		File base = new File(this.directory);
		if(base.exists())
			cleanDirectory(base);
		else
			base.mkdirs();
	}
	
	/**
	 * Write the check file with information about
	 * - file name
	 * - file length
	 * - file CRC
	 */
	public String writeCheckFile(String sFileName,String sLength,String sCrc)
		throws Exception
	{
		File chkFile = new File(this.getDirectory() + File.separator + "chk_" + sFileName + ".crc");
    	FileOutputStream fos = new FileOutputStream(chkFile);
    	fos.write(("filename="+sFileName+"\n").getBytes());
    	fos.write(("filelen="+sLength+"\n").getBytes());
    	fos.write(("filecrc="+sCrc+"\n").getBytes());
    	fos.flush();
    	fos.close();
    	return "OK";
	}
	/**
	 * Clean the directory for upload SP file
	 */
	private void cleanDirectory(File d)
	{
		File[] files = d.listFiles();
		for(int i=0; i<files.length; i++) 
		{
			if(files[i].isDirectory()) 
				cleanDirectory(files[i]);
			else 
				files[i].delete();
		}
	}
	
	/**
	 * Verify client token
	 */
	public boolean checkToken(String sToken) 
	{
		long lTk = 0L;
		try {
			lTk = Long.parseLong(sToken);
		}catch(Exception e){}
		return (lTk == getToken()?true:false);
	}
	
	/**
	 * Generate token for upload task
	 */
	public long generateToken() {
		this.token = System.currentTimeMillis();
		return this.token;
	}
	
	public boolean checkCRCError()
	{
		File f = new File(this.directory + File.separator + ERRORCHECK);
		return f.exists();
	}
	
	public boolean checkUNZIPError()
	{
		File f = new File(this.directory + File.separator + UNZIPCHECK);
		return f.exists();
	}
}
