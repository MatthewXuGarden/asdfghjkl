package com.carel.supervisor.plugin.algorithmpro.alias;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.plugin.algorithmpro.exception.WrongDeviceIdException;


public class AliasMgr 
{
	public static String ALGO_FOLDER = "algorithmpro";
	public static String ALIAS_FOLDER = "alias";
	private static String DID = "DID";
	
	private static AliasMgr amgr = new AliasMgr();
	
	/*
	 * Decode DID -> idDevice
	 */
	private Map<String,Integer> didToId = null;
	/*
	 * Decode CODE -> idVariable
	 */
	private Map<String,Integer> varToId = null;
	/*
	 * Association between ALIAS -> ALIAS FILE 
	 */
	private Map<String,Properties> didConverter = null;
	
	private AliasMgr() {
		init();
	}
	
	public static AliasMgr getInstance() {
		return amgr;
	}
	
	/**
	 *	It takes in input a DID or an ALIAS device
	 *	and It gives in output the database id device.
	 */
	public Integer getIdDevice(String deviceAlias)
		throws WrongDeviceIdException
	{
		Integer ret = -1;
		
		String did = verifyDID(deviceAlias);
		
		// Decode DID -> database id device
		ret = didToId.get(did);
		
		// Try to retrieve from database
		if(ret == null)
			decodDeviceIdIntoId(did);
		
		// Check again. In this case 
		ret = didToId.get(did);
		if(ret == null)
			throw new WrongDeviceIdException("Wrong DID (Device ID).");
		
		return ret;
	}
	
	/**
	 *	It takes alias of device and variable and return the code of the variable 
	 */
	public String getVariableCode(String deviceAlias,String variableAlias) {
		return verifyCodeVariable(deviceAlias, variableAlias);
	}
	
	public int decodVariable(String devAlias,String varAlias)
		throws WrongDeviceIdException
	{
		String devDid = null;
		String varCode = null;
		
		devAlias = devAlias.toUpperCase();
		
		// Try to decode device alias into DID and varAlias into variable code
		try  
		{
			devDid = ((Properties)didConverter.get(devAlias)).getProperty(DID);
			varCode = ((Properties)didConverter.get(devAlias)).getProperty(varAlias);
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		/*
		 * If devDid is null, devAlias is a correct DID value yet
		 */
		if(devDid == null)
			devDid = devAlias;
		
		// Search in memory
		Integer idDevice = didToId.get(devDid);
		// Try to load from DB
		if(idDevice == null)
			decodDeviceIdIntoId(devDid);
		// Search again in memory
		idDevice = didToId.get(devDid);
		// Verify data
		if(idDevice == null)
			throw new WrongDeviceIdException("Wrong DID (Device ID)");
		
		/*
		 * If varCode is null, varAlias must be a correct varCode value
		 */
		if(varCode == null)
			varCode = varAlias;
		
		Integer idVariable = varToId.get(devDid+":"+varCode);
		if(idVariable == null)
			decodAndStoreVarCodeIntoVarId(devDid,varCode);
		idVariable = varToId.get(devDid+":"+varCode);
		
		return idVariable.intValue();
	}
	
	/**
	 * Create alias manager.
	 * It retrieves information from a properties file store into alias folder.
	 * 
	 */
	private void init()
	{
		String fileName = "";
		Properties fileContent = null;
		
		didToId = new HashMap<String,Integer>();
		varToId = new HashMap<String,Integer>();
		didConverter = new HashMap<String,Properties>();
		
		File f = new File((System.getenv("PVPRO_HOME") + File.separator + ALGO_FOLDER + File.separator + ALIAS_FOLDER));
		File[] aliasFiles = f.listFiles(new AliasFileFilter());
		
		String fileExt = AliasFileFilter.EXT.toUpperCase();
		
		for (int i = 0; i < aliasFiles.length; i++) 
		{
			// Device alias from file name
			fileName = aliasFiles[i].getName().toUpperCase();
			
			fileName = fileName.substring(0,fileName.indexOf("."+fileExt));
			
			fileContent = new Properties();
			try 
			{
				// Load alias file for device
				fileContent.load(new FileInputStream(aliasFiles[i]));
				// Save into memory
				didConverter.put(fileName, fileContent);
				
				/*
				 * Decode DID (P1:1.001) into the internal device database id.
				 */
				decodDeviceIdIntoId(fileContent.getProperty(DID));
			} 
			catch (Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	private void decodAndStoreVarCodeIntoVarId(String did,String varCode)
		throws WrongDeviceIdException
	{
		RecordSet rs = null;
		String sql = "select * from cfvariable where idsite=? and iddevice=? and code=? and iscancelled=? and idhsvariable is not null";
		
		Integer idDevice = didToId.get(did);
		Integer idVariable = null;

		try 
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(1),idDevice,varCode,"FALSE"});
			if(rs != null && rs.size() == 1)
				idVariable = (Integer)rs.get(0).get("idvariable");
					
			if(idVariable != null)
				varToId.put(did+":"+varCode, idVariable);
		} 
		catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			e.printStackTrace();
		}	
	}
	
	/**
	 *	This method decodes an alias into a DID.
	 *	In case of DID it returns the same input DID. 
	 */
	private String verifyDID(String deviceAlias) 
	{
		String did = null;
		// Verify if it is an alias
		try {
			did = ((Properties)didConverter.get(deviceAlias)).getProperty(DID);
		}
		catch(Exception e){
			did = null;
		}
		// If it is not an alias, it is a DID
		if(did == null)
			did = deviceAlias;
		
		return did;
	}
	
	private String verifyCodeVariable(String deviceAlias,String variableAlias)
	{
		String varCode = null;
		try {
			varCode = ((Properties)didConverter.get(deviceAlias)).getProperty(variableAlias);
		}
		catch(Exception e){
			varCode = null;
		}
		if(varCode == null)
			varCode = variableAlias;
		
		return varCode;
	}
	
	/**
	 * 
	 */
	private void decodDeviceIdIntoId(String did)
		throws WrongDeviceIdException
	{
		RecordSet rs = null;
		String sql = "select iddevice from cfdevice where idsite=? and code=? and iscancelled=?";
		
		if(did != null)
		{
			if(did.indexOf(":") == -1)
				throw new WrongDeviceIdException("Wrong DID (Device ID). Check Alias File");
			
			String[] chunck = did.split(":");
			String code = chunck[1];
			
			try 
			{
				Integer idDevice = null;
				rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(1),code,"FALSE"});
				
				if(rs != null && rs.size() == 1)
					idDevice = (Integer)rs.get(0).get("iddevice");
				
				if(idDevice != null)
					didToId.put(did, idDevice);
			} 
			catch (Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
				e.printStackTrace();
			}
		}
		else
		{
			throw new WrongDeviceIdException("Wrong DID (Device ID). Check Alias File");
		}
	}
	
	 
	
}
