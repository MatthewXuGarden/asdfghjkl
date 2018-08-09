package com.carel.supervisor.remote.engine;

import java.io.FileOutputStream;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.remote.engine.master.ExpMaster;
import com.carel.supervisor.remote.manager.RemoteMgr;

public class Exporter 
{
	private String idLang = "";
	private long lastConn = 0;
	private long elab = 0;
	private String messageToRemote = "";
	private String wPath = "";
	
	public Exporter(String lang,long last)
	{
		this.idLang = lang;
		this.lastConn = last;
		this.messageToRemote = "";
		this.wPath = RemoteMgr.getInstance().getPath(); 
		this.elab = System.currentTimeMillis();
	}
	
	public boolean export(String db,String[] enable) 
	{
		ExpMaster master = null;
		StringBuffer sb = new StringBuffer();
		String[][] list = RemoteMgr.getInstance().getTableToElab();
		FileOutputStream fos = null;
		boolean ris = false;
		boolean found = false;
		String tab = "";
		int counter = 0;
		String defLang = checkLanguage(this.idLang);
		
		try {
			fos = new FileOutputStream(this.wPath+this.elab+".pvpro");
			sb.append("<exp>");
			tab = "time="+this.elab+"\n";
			sb.append("<id>"+this.elab+"</id>");
			fos.write(tab.getBytes());
			tab = "occu="+enable.length+"\n";
			sb.append("<oc>"+list.length+"</oc>");
			fos.write(tab.getBytes());
			tab = "lang="+idLang+"\n";
			sb.append("<ln>"+idLang+"</ln>");
			fos.write(tab.getBytes());
			sb.append("<tab>");
			for(int i=0; i<list.length; i++)
			{
				found = false;
				for(int j=0; j<enable.length; j++)
				{
					if(list[i][0].equalsIgnoreCase(enable[j]))
					{
						found = true;
						break;
					}
				}
				
				if(found)
				{
					master = initObject(list[i][1],db,defLang,list[i][0],this.lastConn);
					if(master != null)
					{
						master.setPath(this.wPath);
						if(master.export(this.elab)) 
						{
							fos.write(("T"+(counter)+"="+list[i][0]+"\n").getBytes());
							sb.append("<ele>"+list[i][0]+"</ele>");
							counter++;
						}
					}
				}
			}
			sb.append("</tab>");
			sb.append("</exp>");
			fos.flush();
			fos.close();
			ris = true;
		}
		catch(Exception e) {
			ris = false;
			e.printStackTrace();
		}
		messageToRemote = sb.toString();
		return ris;
	}
	
	public String getMessageToRemote() {
		return this.messageToRemote;
	}
	
	public long getTimeExport() {
		return this.elab;
	}
	
	private ExpMaster initObject(String clas,String db,String lang,String tab,long last)
	{
		ExpMaster ret = null;
		Class[] cls = {String.class,String.class,String.class,long.class};
		Object[] obj = {db,lang,tab,new Long(last)};
		
		try {
			ret = (ExpMaster)FactoryObject.newInstance(clas,cls,obj);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ret;
	}
	
	private String checkLanguage(String lang)
	{
		String sql = "select * from cfsiteext where idsite=?";
		String ret = lang;
		String val = "";
		boolean found = false;
		RecordSet rs = null;
		Record r = null;
		
		try 
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(1)});
			if(rs != null)
			{
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						val = UtilBean.trim(r.get("languagecode"));
						if(val != null && val.equalsIgnoreCase(lang)) {
							found = true;
							break;
						}
					}
				}
			}
		}
		catch(Exception e){
			ret = "EN_en";
		}
		
		if(!found)
			ret = "EN_en";
		
		return ret;
	}
}
