package com.carel.supervisor.remote.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;
import java.util.zip.ZipFile;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.remote.bean.ImportBean;
import com.carel.supervisor.remote.bean.ImportBeanList;
import com.carel.supervisor.remote.engine.master.ImpMaster;
import com.carel.supervisor.remote.manager.RemoteMgr;

public class Importer 
{
	private ImportBeanList impo = null;
	private ZipFile zp = null;
	private String wPath = "";
	
	public Importer()
	{
		this.impo = new ImportBeanList();
		this.wPath = RemoteMgr.getInstance().getPath(); 
	}
	
	public void startImport(String db)
	{
		ImportBean[] listImpo = this.impo.loadImport(db);
		Properties p = null;
		LineNumberReader reader = null;
		String num = "";
		String tab = "";
		int iNum = 0;
		ImpMaster impMaster = null;
		String manageClass = "";
		boolean ris = false;
		
		for(int i=0; i<listImpo.length; i++)
		{
			ris = false;
			p = loadPropFile(listImpo[i].getIdfile());
			if(p != null)
			{
				num = p.getProperty("occu");
				try {
					iNum = Integer.parseInt(num);
				}
				catch(Exception e){}
				
				for(int j=0; j<iNum; j++)
				{
					tab = p.getProperty(("T"+j));
					manageClass = RemoteMgr.getInstance().getManageTable(tab);
					if(manageClass != null)
					{
						reader = openZipFile(listImpo[i].getIdfile(),tab);
						if(reader != null)
						{
							impMaster = initObject(manageClass,tab,listImpo[i].getIdsite(),null,p.getProperty("lang"));
							if(impMaster != null)
							{
								impMaster.setInpuStream(reader);
								ris = impMaster.importData();
							}
							
							closeZipFile(reader);
						}
					}
				}
			}
			
			if(ris)
			{
				try 
				{
					if(this.impo.updateImport(null,listImpo[i].getIdfile(),listImpo[i].getIdsite()))
						SiteInfoList.updateTimeLastConnection(null,listImpo[i].getIdsite(),String.valueOf(System.currentTimeMillis()));
				}
				catch(Exception e)
				{
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
			}
		}
	}
	
	private Properties loadPropFile(String idGui)
	{
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(this.wPath+idGui+".pvpro"));
		} 
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return prop;
	}
	
	private LineNumberReader openZipFile(String idGui,String name)
	{
		LineNumberReader lnr = null;
		try 
		{
			this.zp = new ZipFile(new File(this.wPath+idGui+"_"+name+".zip"));
			lnr = new LineNumberReader(new InputStreamReader(this.zp.getInputStream(this.zp.getEntry(name))));
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return lnr;
	}
	
	private void closeZipFile(LineNumberReader reader)
	{
		if(reader != null) {
			try {
				reader.close();
				this.zp.close();
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	private ImpMaster initObject(String clas,String tabName,int idsite,String db,String lang)
	{
		ImpMaster ret = null;
		Class[] cls = {int.class,String.class,String.class,String.class};
		Object[] obj = {new Integer(idsite),tabName,db,lang};
		
		try {
			ret = (ImpMaster)FactoryObject.newInstance(clas,cls,obj);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		try {
			BaseConfig.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Importer im = new Importer();
		im.startImport(null);
	}
}
