package com.carel.supervisor.plugin.algorithmpro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.algorithmpro.exception.CreateAlgorithmException;
import com.carel.supervisor.plugin.algorithmpro.obj.IAlgorithmPro;
import com.carel.supervisor.plugin.algorithmpro.util.AlgoPro;
import com.carel.supervisor.plugin.algorithmpro.util.AlgoProLoader;

public class AlgorithmProMgr
{
	private static AlgorithmProMgr algo = new AlgorithmProMgr();
	
	private Map<String,AlgorithmThread> algoThread = null;
	private AlgoProLoader algoProLoader =  null;
	private Properties conf = null;
	private int maxNumObj = 3;
	
	private AlgorithmProMgr() 
	{
		algoThread = new HashMap<String, AlgorithmThread>();
	}
	
	public static AlgorithmProMgr getInstance() {
		return algo;
	}
	
	public boolean stopAlgoPro()
	{
		boolean ret = true;
		String key = "";
		Iterator i = algoThread.keySet().iterator();
		while(i.hasNext())
		{
			key = (String)i.next();
			this.stopAlgoProByName(key,"System",false);
		}
		return ret;
	}
	
	/**
	 * Stop Object using unique name 
	 */
	public boolean stopAlgoProByName(String name,String userName)
	{
		return stopAlgoProByName(name,userName,true);
	}
	
	/**
	 * Stop Object using unique name 
	 */
	public boolean stopAlgoProByName(String name,String userName,boolean saveState)
	{
		boolean ret = false;
		AlgorithmThread cur = this.algoThread.get(name);
		if(cur != null)
		{
			cur.stopWork(saveState);
			EventMgr.getInstance().info(1, userName, "Action", "ALG03", name);
			ret = true;
		}
		return ret;
	}
	
	public boolean startAlgoProByName(String name,String userName)
	{
		boolean ret = false;
		AlgorithmThread cur = this.algoThread.remove(name);
		if(cur != null && cur.getStatus().equals("0"))
		{
			String sCl = cur.getsClass();
			String sNa = cur.getsName();
			String sDe = cur.getsDesc();
			cur = null;
			
			// Create and restart object
			ret = startObject(sCl, sNa, sDe);
			if(ret)
				EventMgr.getInstance().info(1, userName, "Action", "ALG01", name);
			else
				EventMgr.getInstance().error(1, userName, "Action", "ALG02", name);
		}
		return ret;
	}
	
	/**
	 * Starting point.
	 * This method is called from hook class.
	 */
	public boolean startAlgoPro()
	{
		boolean ret = true;
		
		// Load user jar into context
		algoProLoader = new AlgoProLoader();
		//algoProLoader.loadJar();
			
		// Load configuration user file
		try 
		{
			conf = algoProLoader.getPropConfiguration();	
		} 
		catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).warn("*** Algorithm Pro ***: Configuration file not found");
			// stack trace hidden before is working condition and not a problem
			// LoggerMgr.getLogger(this.getClass()).error(e);
			ret = false;
			conf = null;			
			return ret;
		}
	
		try 
		{			
			// If configuration file loaded correct, It start the objects
			startObjects();
		} 
		catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * It starts objects defined into configuration user file
	 */
	private void startObjects()
	{
		String sClass = null;
		String sName = null;
		String sDesc = null;
		
		for(int i=1; i<=maxNumObj; i++)
		{
			sClass = null;
			sName = null;
			sDesc = null;
			
			sClass = this.conf.getProperty((AlgoPro.CLASS+"["+i+"]"));
			sName = this.conf.getProperty((AlgoPro.NAME+"["+i+"]"));
			sDesc = this.conf.getProperty((AlgoPro.DESC+"["+i+"]"));
			
			if(sClass != null && sName != null)
				startObject(sClass, sName, sDesc);
		}
		
		// Reload System Configuration table
		try {
			SystemConfMgr.getInstance().refreshSystemInfo();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private boolean startObject(String sClass, String sName, String sDesc)
	{
		boolean ris = false;
		
		try 
		{
			// Create custom object
			IAlgorithmPro objAlgo = null;
			try 
			{
				// Create object
				if(algoProLoader.useThisLoader())
					objAlgo = (IAlgorithmPro)Class.forName(sClass,true,algoProLoader.getJarLoader()).newInstance();
				else
					objAlgo = (IAlgorithmPro)Class.forName(sClass).newInstance();
				
				if(sName != null)
					objAlgo.setName(sName);
			} 
			catch (InstantiationException e) {
				e.printStackTrace();
				throw new CreateAlgorithmException(e.getMessage());
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new CreateAlgorithmException(e.getMessage());
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new CreateAlgorithmException(e.getMessage());
			}
			
			// Create Thread object
			AlgorithmThread aThread = new AlgorithmThread(objAlgo,sClass, sName, sDesc);
			
			// Check from database (SystemConf Table) if the object has to start or not
			String sVal = null;
			IProductInfo product = null;
			try 
			{
				product = ProductInfoMgr.getInstance().getProductInfo();
				sVal = product.get(AlgoPro.PFX_SYSCONF + sName);
				if(sVal == null)
				{
					sVal = "FALSE";
					product.store(AlgoPro.PFX_SYSCONF + sName, sVal);
				}
			}
			catch(Exception e) {
				sVal = "FALSE";
				try {
				product.store(AlgoPro.PFX_SYSCONF + sName, sVal);
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}
			
			// Start the thread
			if(sVal != null && sVal.equalsIgnoreCase("TRUE"))
			{	
				Thread t = new Thread(aThread,"AlgorithmThread");
				t.setDaemon(true);
				t.start();
			}
			else
			{
				aThread.stopWork(true);
				aThread.forceReset();
			}
			
			// Save reference
			algoThread.put(sName, aThread);
			ris = true;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ris;
	}
	
	/**
	 * Return an array list with the name of the various object
	 * loaded into the application
	 */
	public String[] retrieveAlgoListName()
	{
		Iterator i = this.algoThread.values().iterator();
		String[] names = new String[this.algoThread.size()];
		int idx = 0;
		AlgorithmThread value = null;
		while(i.hasNext())
		{
			value = (AlgorithmThread)i.next();
			if(value != null)
				names[idx] = value.getsName();
			idx++;
		}
		return names;
	}
	
	public String getAlgoDescription(String name) {
		try {
			return this.algoThread.get(name).getsDesc();
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoMainClass(String name) {
		try {
			return this.algoThread.get(name).getsClass();
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStatus(String name) {
		try {
			return this.algoThread.get(name).getStatus();
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStartTime(String name) {
		try {
			return this.algoThread.get(name).getStartTime();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStopTime(String name) {
		try {
			return this.algoThread.get(name).getStopTime();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStateInfo1(String name) {
		try {
			return this.algoThread.get(name).getStateInfo1();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStateInfo2(String name) {
		try {
			return this.algoThread.get(name).getStateInfo2();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStateInfo3(String name) {
		try {
			return this.algoThread.get(name).getStateInfo3();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStateInfo4(String name) {
		try {
			return this.algoThread.get(name).getStateInfo4();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getAlgoStateInfo5(String name) {
		try {
			return this.algoThread.get(name).getStateInfo5();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
