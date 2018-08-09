package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DevMdlBeanList
{
    private List<DevMdlBean> devMdlList = new ArrayList<DevMdlBean>();

    public DevMdlBeanList()
    {
    }

    public static String popolateCombo(int idsite, String language) throws DataBaseException
	{
    	String sql =
            "select cfdevmdl.iddevmdl, cftableext.description " +
            "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ? and cfdevmdl.ide='TRUE' order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(idsite), new Integer(
                        idsite) });
        
        Record r = null;
        StringBuffer buffer = null;
       	buffer = new StringBuffer("<SELECT style='width:260px;' name=\"remdevcreator\" id=\"remdevcreator\" onchange='verifyDev(this);'>");
       	buffer.append("<OPTION value='-1' selected >---------</OPTION>\n");
        for (int i = 0; i < rs.size(); i++)
	    {
        	r = rs.get(i);
            buffer.append("<OPTION value=\"");
            buffer.append(((Integer)r.get(0)).toString());
            buffer.append("\">");
            buffer.append((String)r.get(1));
            buffer.append("</OPTION>\n");
        }
        buffer.append("</SELECT>");
        return buffer.toString();
	}
    
    
    public static String popolateComboDevMdl(int idsite, String language) throws DataBaseException
	{
    	String sql =
            "select cfdevmdl.iddevmdl, cftableext.description " +
            "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ? order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(idsite), new Integer(
                        idsite) });
        
        Record r = null;
        StringBuffer buffer = null;
       	buffer = new StringBuffer("<SELECT style='width:260px;' name=\"expsvg_iddevmdl\" id=\"expsvg_iddevmdl\" onchange='verifyDevMdl(this);'>");
       	buffer.append("<OPTION value='-1' selected >---------</OPTION>\n");
        for (int i = 0; i < rs.size(); i++)
	    {
        	r = rs.get(i);
            buffer.append("<OPTION value=\"");
            buffer.append(((Integer)r.get(0)).toString());
            buffer.append("\">");
            buffer.append((String)r.get(1));
            buffer.append("</OPTION>\n");
        }
        buffer.append("</SELECT>");
        return buffer.toString();
	}
    
    // combobox that list the device models and as VALUE is used the "folder" od cfdevcustom  
    public static String popolateComboDevFolders(int idsite, String language) throws DataBaseException
	{
    	String sql =
            "select cfdevcustom.folder, cftableext.description from cfdevmdl,cftableext,cfdevcustom where cfdevmdl.iddevmdl=cftableext.tableid and "
            + "cftableext.languagecode = ? and cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ? and "
            + "cfdevmdl.iddevmdl = cfdevcustom.iddevmdl order by cftableext.description";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(idsite), new Integer(
                        idsite) });
        
        Record r = null;
        StringBuffer buffer = null;
       	buffer = new StringBuffer("<SELECT style='width:260px;' name=\"dev_folder\" id=\"dev_folder\" onchange='verifyDevMdl(this);'>");
       	buffer.append("<OPTION value='-1' selected >---------</OPTION>\n");
        for (int i = 0; i < rs.size(); i++)
	    {
        	r = rs.get(i);
            buffer.append("<OPTION value=\"");
            buffer.append((r.get(0)).toString());
            buffer.append("\">");
            buffer.append((String)r.get(1));
            buffer.append("</OPTION>\n");
        }
        buffer.append("</SELECT>");
        return buffer.toString();
	}
    
    public DevMdlBean[] retrieve(int idsite, String language)
        throws DataBaseException
    {
		String sql = "select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr "
				+ "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and "
				+ "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ? order by upper (cftableext.description) ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, new Integer(idsite), new Integer(
                        idsite) });
        DevMdlBean[] listDev = new DevMdlBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            listDev[i] = new DevMdlBean(rs.get(i), language);
            devMdlList.add(listDev[i]);
        }

        return listDev;
    }
    
    // new retrieve method. To allow hiding of 'Internal IO' device from devmdl list
    // Nicola Compagno 25032010
    public DevMdlBean[] retrieve(int idsite, String language, boolean hideInternalIO) throws DataBaseException
    {
    	if(!hideInternalIO)
    	{
    		return retrieve(idsite, language);
    	}
    	else
    	{
			String sql = "select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr "
					+ "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and "
					+ "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ? and cfdevmdl.code != 'Internal IO' order by cftableext.description"; 
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { language, new Integer(idsite), new Integer(
                            idsite) });
            DevMdlBean[] listDev = new DevMdlBean[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                listDev[i] = new DevMdlBean(rs.get(i), language);
                devMdlList.add(listDev[i]);
            }

            return listDev;
     
    	}
    }
    
    public DevMdlBean[] retrieveOnLine(int idsite, String language)
    throws DataBaseException
{
    String sql =
        "select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr " +
        "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and " +
        "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cftableext.idsite = ?" +
        " and cfdevmdl.iddevmdl in (select distinct iddevmdl from cfdevice where iscancelled = ? and idsite = ?) order by cftableext.description";
    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
            new Object[] { language, new Integer(idsite), new Integer(
                    idsite), "FALSE" , new Integer(idsite)});
    DevMdlBean[] listDev = new DevMdlBean[rs.size()];

    for (int i = 0; i < rs.size(); i++)
    {
        listDev[i] = new DevMdlBean(rs.get(i), language);
        devMdlList.add(listDev[i]);
    }

    return listDev;
}

    public DevMdlBean retrieveByCode(int idsite, String language, String code)
    	throws DataBaseException
    {
    	String sql =
            "select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr " +
            "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cfdevmdl.code = ? and cftableext.idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), code, new Integer(idsite)
                });
        if(rs == null || rs.size() == 0)
        {
        	return null;
        }
        return new DevMdlBean(rs.get(0), language);
    }
    public DevMdlBean retrieveById(int idsite, String language, int iddev)
        throws DataBaseException
    {
        String sql =
            "select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr " +
            "from cfdevmdl inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and " +
            "cftableext.tablename='cfdevmdl' and cfdevmdl.idsite = ? and cfdevmdl.iddevmdl = ? and cftableext.idsite = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    language, new Integer(idsite), new Integer(iddev),
                    new Integer(idsite)
                });

        return new DevMdlBean(rs.get(0), language);
    }
    
    public DevMdlBean retrieveByDeviceId(int idsite, String language, int deviceid)
    throws DataBaseException
	{
	    String sql =
	    	"select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr "+
	    	"from cfdevmdl "+ 
	    	"inner join cftableext on cfdevmdl.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and cftableext.idsite = ? and cftableext.tablename='cfdevmdl' "+
	    	"inner join cfdevice on cfdevice.iddevmdl=cfdevmdl.iddevmdl "+
	    	"where cfdevmdl.idsite = ? and cfdevice.iddevice = ? ";              
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	            new Object[]
	            {
	                language, new Integer(idsite), 
	                new Integer(idsite),new Integer(deviceid),
	            });
	
	    return new DevMdlBean(rs.get(0), language);
	}
    public int size()
    {
        return devMdlList.size();
    }
    
    public DevMdlBean getMdlBean(int i)
    {
    	return devMdlList.get(i);
    }
    public DevMdlBean getMdlBeanbyid(int iddevmdl)
    {
    	for(int i =0;i<devMdlList.size();i++)
    	{
    		DevMdlBean bean = (DevMdlBean)devMdlList.get(i);
    		if(bean.getIddevmdl() == iddevmdl)
    		{
    			return bean;
    		}
    	}
    	return null;
    }
  //Ing. Gilioli Manuel Start 4 report
    public DevMdlBean[] retrieveDevMdl(int idsite, String language)
    throws DataBaseException
    {
    	
    	String sql ="select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr "+ 
    	 "from cfdevmdl,cftableext,(select  distinct iddevmdl  from cfdevice where iscancelled=?)as a "+ 
    	 "where  a.iddevmdl=cfdevmdl.iddevmdl "+
    	 "and   cfdevmdl.iddevmdl=cftableext.tableid "+ 
    	 "and   cftableext.languagecode = ? "+
    	 "and   cftableext.tablename='cfdevmdl' "+
    	 "and   cfdevmdl.idsite = ? "+
    	 "and   cftableext.idsite = ? "+
    	 "order by cftableext.description ";
    
    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
            new Object[] { "FALSE",language, new Integer(idsite), new Integer(
                    idsite) });
    DevMdlBean[] listDev = new DevMdlBean[rs.size()];

    for (int i = 0; i < rs.size(); i++)
    {
        listDev[i] = new DevMdlBean(rs.get(i), language);
        devMdlList.add(listDev[i]);
    }

    return listDev;
	}
  //Ing. Gilioli Manuel Fine 4 report
    
    
    // method added to hide 'Internal IO' model from query
    // Nicola Compagno 25032010
    public DevMdlBean[] retrieveDevMdl(int idsite, String language, boolean hideInternalIO)
    throws DataBaseException
    {
    	if(!hideInternalIO)
    	{
    		return retrieveDevMdl(idsite,language);
    	}
    	else
    	{
    		String sql ="select cfdevmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr "+ 
	       	 "from cfdevmdl,cftableext,(select  distinct iddevmdl  from cfdevice where iscancelled=?)as a "+ 
	       	 "where  a.iddevmdl=cfdevmdl.iddevmdl "+
	       	 "and   cfdevmdl.iddevmdl=cftableext.tableid "+ 
	       	 "and   cftableext.languagecode = ? "+
	       	 "and   cftableext.tablename='cfdevmdl' "+
	       	 "and   cfdevmdl.idsite = ? "+
	       	 "and   cftableext.idsite = ? "+
	       	 "and   cfdevmdl.code != 'Internal IO'" +
	       	 "order by cftableext.description ";
       
	       RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
	               new Object[] { "FALSE",language, new Integer(idsite), new Integer(
	                       idsite) });
	       DevMdlBean[] listDev = new DevMdlBean[rs.size()];
	
	       for (int i = 0; i < rs.size(); i++)
	       {
	           listDev[i] = new DevMdlBean(rs.get(i), language);
	           devMdlList.add(listDev[i]);
	       }
	
	       return listDev;
       	}	
    }

    
}
