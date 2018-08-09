package com.carel.supervisor.dataaccess.dataconfig;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper.ThreadController;

public class SiteInfoList extends AbstractBindable
{
    private SiteInfo[] siteInfo = null;
    private Map siteById = new HashMap();
    private Map siteByCode = new HashMap();

    public SiteInfoList(String dbId, String plantId) throws DataBaseException
    {
        super();

        String sql = "select * from cfsite where pvcode = ? order by idsite";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId });
        Record record = null;
        siteInfo = new SiteInfo[recordSet.size()];

        //METTERE DEI WARNING SE NN TROVO IL LINK ADEGUATO
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            siteInfo[i] = new SiteInfo(record);
            siteByCode.put(siteInfo[i].getCode(), siteInfo[i]);
            siteById.put(new Integer(siteInfo[i].getId()), siteInfo[i]);
        }
    }

    public SiteInfoList() throws DataBaseException
    {
        super();

        String sql = "select * from cfsite order by idsite";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] {  });
        Record record = null;
        siteInfo = new SiteInfo[recordSet.size()];

        //METTERE DEI WARNING SE NN TROVO IL LINK ADEGUATO
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            siteInfo[i] = new SiteInfo(record);
            siteByCode.put(siteInfo[i].getCode(), siteInfo[i]);
            siteById.put(new Integer(siteInfo[i].getId()), siteInfo[i]);
        }
    }

    public int size()
    {
        return siteInfo.length;
    }

    public SiteInfo getByPosition(int pos)
    {
        return siteInfo[pos];
    }

    public SiteInfo getByCode(String code)
    {
        return (SiteInfo) siteByCode.get(code);
    }

    public SiteInfo getById(int id)
    {
        return (SiteInfo) siteById.get(new Integer(id));
    }

    public void clear()
    {
        for (int i = 0; i < siteInfo.length; i++)
            siteInfo[i] = null;

        siteById.clear();
        siteByCode.clear();
        siteInfo = null;
        siteByCode = null;
    }
    
    
    public static SiteInfo authenticateLocal(String db,String login,String pass,String type)
    {
    	String sql = "";
    	Object[] param = null;
    	RecordSet rs = null;
    	Record r = null;
    	SiteInfo site = null;
    	
    	if(type != null && type.equalsIgnoreCase("PVP"))
    	{
    		sql = "select * from cfsite where code=? and password=? and type=? and idsite not in (0,1)";
    		param = new Object[]{login,pass,type};
    	}
    	else if(type != null && type.equalsIgnoreCase("PVE"))
    	{
    		sql = "select * from cfsite where code=? and type=? and idsite not in (0,1)";
    		param = new Object[]{login,type};
    	}
    	
    	try 
    	{
    		rs = DatabaseMgr.getInstance().executeQuery(db,sql,param);
    		if(rs != null)
    		{
    			if(rs.size() == 1) {
    				r = rs.get(0);
    				site = new SiteInfo(r);
    			}
    		}
    	}
    	catch(Exception e){
    		Logger logger = LoggerMgr.getLogger(SiteInfoList.class);
    		logger.error(e);
    	}
    	
    	return site;
    }
    
    public static SiteInfo[] retriveRemoteSite() throws DataBaseException
    {
    	String sql = "select * from cfsite where idsite > ? order by idsite";
    	Object[] param = new Object[]{new Integer(1)};
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
    	
    	SiteInfo[] remote_list = new SiteInfo[rs.size()];
    	for (int i=0;i<rs.size();i++)
    	{
    		remote_list[i] = new SiteInfo(rs.get(i));
    	}
    	
    	return remote_list;
    }
    
    public static SiteInfo[] retriveSites() throws DataBaseException  //tutti tranne idsite = 0
    {
    	String sql = "select * from cfsite where idsite!=? order by idsite";
    	Object[] param = new Object[]{new Integer(0)};
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
    	
    	SiteInfo[] remote_list = new SiteInfo[rs.size()];
    	for (int i=0;i<rs.size();i++)
    	{
    		remote_list[i] = new SiteInfo(rs.get(i));
    	}
    	
    	return remote_list;
    }
    
    public static SiteInfo retrieveSiteById(int idsite) 
    	throws DataBaseException
    {
    	String sql = "select * from cfsite where idsite = ?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite)});
    	if (rs.size()>0)
    	{
    		return new SiteInfo(rs.get(0));
    	}
    	else
    		return null;
    }
    
    public static void removeSiteById(int idsite) 
    	throws DataBaseException
    {
    	Object[] param = {new Integer(idsite)};
    	String sql = "";
    	
    	sql = "delete from hsactionqueue where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from hsaction where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from cftableext where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from hsevent where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from hsnote where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from hsalarm where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from cfvariable where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from cfdevice where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from cfgroup where idsite = ?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from cfarea where idsite = ?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	    	
    	sql = "delete from cfsiteext where idsite = ?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);	
    	
    	sql = "delete from cfsite where idsite = ?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	sql = "delete from rmtableconf where idsite = ?";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    	
    	
    	if (isSiteInDatatransfer(idsite))
    	{
	    	sql = "delete from cftransfersite where idsite = ?";
	    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
	    	//comunicazione a allineamento dati di aggiornare la configurazione
	    	try{
				Socket s = new Socket(InetAddress.getLocalHost(), 10001);
				OutputStream sos = s.getOutputStream();
				String cmdd = "update";
				sos.write(cmdd.getBytes());
				sos.flush();
				sos.close();    		
	    	}catch(Exception e){}
    	}
    	
    	if (isSiteInPolling(idsite))
    	{
    		sql = "delete from rmtimesite where idsite = ?";
        	DatabaseMgr.getInstance().executeStatement(null,sql,param);
        	
        	sql = "delete from pollingstatus where idsite = ?";
        	DatabaseMgr.getInstance().executeStatement(null,sql,param);
        	
        	/*    gestire riavvio heartbeat quando si rimuove un sito che era gestito (da dataaccess non vedo ThreadController )
        	try{	
            	if(ThreadController.getInstance().getThreadStarted().isAlive())
        	{
            		ThreadController.getInstance().getStartedThread().stopStartThread();
            		ThreadController.getInstance().newThread();
            		ThreadController.getInstance().startThread();
            		
            	}
             }catch(Exception e){
            	 Logger logger = LoggerMgr.getLogger(this.getClass());
                 logger.error(e);
             }*/
    	}
        	
        	
    }
    
    private static boolean isSiteInPolling(int idsite) throws DataBaseException
    {
    	String sql = "select * from rmtimesite where idsite=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite)});
    	if (rs.size()>0)
    	{
    		return true;
    	}
    	else
    		return false;
    }
    
    private static boolean isSiteInDatatransfer(int idsite) throws DataBaseException
    {
    	String sql = "select * from cftransfersite where idsite=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite)});
    	if (rs.size()>0)
    	{
    		return true;
    	}
    	else
    		return false;
    }
    
    public static void updateSite(int idsite,String name,String type,String code, String password,String typeconnection,String phone) 
    	throws DataBaseException
    {
    	String sql = "update cfsite set name=?,type=?,code=?,password=?,typeconnection=?,phone=? where idsite=?";
    	Object[] param = new Object[7];
    	param[0]= name;
    	param[1]=type;
    	param[2]=code;
    	param[3]=password;
    	param[4]=typeconnection;
    	param[5]=phone;
    	param[6]= new Integer(idsite);
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
    }
    
    public static void updateTimeLastConnection(String db,int idSite,String lastConn)
    	throws DataBaseException
    {
    	String sql = "update cfsite set lastconnection=? where idsite=?";
    	long lTCon = System.currentTimeMillis();
    	try {
    		lTCon = Long.parseLong(lastConn);
    	}catch(Exception e){}
    	
    	Timestamp tConn = new Timestamp(lTCon);
    	DatabaseMgr.getInstance().executeStatement(db,sql,new Object[]{tConn,new Integer(idSite)});
    }
    
    public static void updateTimeLastDialup(String db,int idSite,String lastConn)
		throws DataBaseException
	{
		String sql = "update cfsite set lastdialup=? where idsite=?";
		long lTCon = System.currentTimeMillis();
		try {
			lTCon = Long.parseLong(lastConn);
		}catch(Exception e){}
		
		Timestamp tConn = new Timestamp(lTCon);
		DatabaseMgr.getInstance().executeStatement(db,sql,new Object[]{tConn,new Integer(idSite)});
	}
    
    public static void updateSiteStatus(String db,int idSite,int status)
    	throws DataBaseException
    {
    	String sql = "update cfsite set sitestatus=? where idsite=?";
    	DatabaseMgr.getInstance().executeStatement(db,sql,new Object[]{new Integer(status),new Integer(idSite)}); 
    }
}
