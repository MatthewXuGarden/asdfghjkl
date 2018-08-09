package com.carel.supervisor.presentation.bean;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class VarMdlBeanList
{
    public VarMdlBeanList()
    {
    }

    public static VarMdlBean[] retrieve(int idsite,int iddevmdl, String language)
        throws DataBaseException
    {
    	String sql =
        "select cfvarmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr " +
        "from cfvarmdl inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and " +
        "cftableext.tablename='cfvarmdl' and cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ? and cftableext.idsite = ?";
        
    	Object[] param = new Object[4];
    	param[0] = language;
    	param[1] = new Integer(idsite);
    	param[2] = new Integer(iddevmdl);
    	param[3] = new Integer(idsite);
    	
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,param);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }

        return listVar;
    }
    public static VarMdlBean[] retrieveAlarms(int idsite,int iddevmdl, String language)
    	throws DataBaseException
	{
		String sql =
	    "select cfvarmdl.*, cftableext.description,cftableext.shortdescr,cftableext.longdescr " +
	    "from cfvarmdl inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and " +
	    "cftableext.tablename='cfvarmdl' and cfvarmdl.type=4 and cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ? and cftableext.idsite = ?";
	    
		Object[] param = new Object[4];
		param[0] = language;
		param[1] = new Integer(idsite);
		param[2] = new Integer(iddevmdl);
		param[3] = new Integer(idsite);
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,param);
	   
	    VarMdlBean[] listVar = new VarMdlBean[rs.size()];
	
	    for (int i = 0; i < rs.size(); i++)
	    {
	        listVar[i] = new VarMdlBean(rs.get(i));
	    }
	
	    return listVar;
	}

    
    public static VarMdlBean[] retrieveById(int idsite, int iddevmdl) throws DataBaseException
      
    {
        
    	String sql =
            "select cfvarmdl.* from cfvarmdl where cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    new Integer(idsite), new Integer(iddevmdl)
                });

        VarMdlBean[] varlist = new VarMdlBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
           	varlist[i] = new VarMdlBean(rs.get(i));
        }

        return varlist;
    }
    
    public static VarMdlBean[] retrieveByIds(int[] idvarmdls) throws DataBaseException
    
    {
        if(idvarmdls == null || idvarmdls.length==0)
        	return null;
    	String sql =
            "select cfvarmdl.* from cfvarmdl where cfvarmdl.idvarmdl in (";
    	Object[] params = new Object[idvarmdls.length];
    	for(int i=0;i<idvarmdls.length;i++)
    	{
    		sql += "?,";
    		params[i] = new Integer(idvarmdls[i]);
    	}
    	sql = sql.substring(0,sql.length()-1)+")";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,params);

        VarMdlBean[] varlist = new VarMdlBean[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
           	varlist[i] = new VarMdlBean(rs.get(i));
        }

        return varlist;
    }

    public static VarMdlBean[] retrieveOrdered(int idsite,int iddevmdl, String language) throws DataBaseException
    {
        String sql =
        "select cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr " +
        "from cfvarmdl inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and " +
        "cftableext.tablename='cfvarmdl' and cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ? and cftableext.idsite = ? " +
        "order by cftableext.description";
        
        Object[] param = new Object[4];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevmdl);
        param[3] = new Integer(idsite);
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,param);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
    
    public static VarMdlBean[] retrieveLogOrdered(int idsite,int iddevmdl, String language) throws DataBaseException
    {
        String sql =
        "select cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr " +
        "from cfvarmdl inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and " +
        "cftableext.tablename='cfvarmdl' and cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ? and cftableext.idsite = ? " +
        "and cfvarmdl.hsfrequency is not null order by cftableext.description";
        
        Object[] param = new Object[4];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevmdl);
        param[3] = new Integer(idsite);
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,param);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
    
    //BIOLO: query da ottimizzare
    public VarMdlBean[] retrieveOrderedIfDevIsPresentLog(String language,int idsite,int iddevmdl,String isHaccp,Integer interval) throws DataBaseException
    {
    	String sql = "";
    	Object []objects=null;
    	
    	if ("TRUE".equalsIgnoreCase(isHaccp))
    	{
    		sql = "select distinct cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr " +
        	"from cfvarmdl,cftableext,cfvariable where cftableext.tableid=cfvariable.idvarmdl and " +
        	"cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and " +
        	"cfvarmdl.idsite = ? and cftableext.idsite = ? and iddevice in " +
        			"(select iddevice from cfdevice where iddevmdl=?) and	iscancelled=? and " +
        			"cfvariable.ishaccp=? and	idhsvariable is not null and cfvarmdl.idvarmdl=cfvariable.idvarmdl order by cftableext.description ";
    		objects= new Object[]{language,new Integer(idsite),new Integer(idsite),new Integer(iddevmdl),"FALSE","TRUE"};
    	}
    	else
    	{
    		/*sql = "select distinct cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr " +
        	"from cfvarmdl,cftableext,cfvariable where  " +
        	"cfvarmdl.iddevmdl=? and cfvarmdl.idvarmdl=cftableext.tableid and " +
        	"cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and " +
        	"cfvarmdl.idsite = ? and cftableext.idsite = ? and cfvarmdl.idvarmdl in " +
        		"(select distinct idvarmdl from cfvariable where iddevice in " +
        			"(select iddevice from cfdevice where iddevmdl=?) and	iscancelled=? and " +
        			"ishaccp=? and	idhsvariable is null) order by cftableext.description ";
        		*/
    		sql = "select distinct cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr " +
        	"from cfvarmdl,cftableext,cfvariable where cftableext.tableid=cfvariable.idvarmdl and " +
        	"cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and " +
        	"cfvarmdl.idsite = ? and cftableext.idsite = ? and iddevice in " +
        			"(select iddevice from cfdevice where iddevmdl=?) and	iscancelled=? and " +
        			"cfvariable.ishaccp=? and	idhsvariable is null and cfvarmdl.idvarmdl=cfvariable.idvarmdl order by cftableext.description ";
    		//objects= new Object[]{new Integer(iddevmdl),language,new Integer(idsite),new Integer(idsite),new Integer(iddevmdl),"FALSE","FALSE"};
    		objects= new Object[]{language,new Integer(idsite),new Integer(idsite),new Integer(iddevmdl),"FALSE","FALSE"};
    	}
    	
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
    
  //
    
  //Ing. Gilioli Manuel Start 4 report
    public VarMdlBean[] retrieveOrderedIfDevIsPresent(String language,int idsite,int iddevmdl,String isHaccp,Integer interval) throws DataBaseException
    {
    	// retrieve number of device for IDDEVMDL
    	String sql = "select count(*) from cfdevice where idsite=? and iddevmdl=? and iscancelled=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,new Object[]{new Integer(idsite),new Integer(iddevmdl),"FALSE"});
    	
    	Integer dev_number = 0;
    	dev_number = (Integer)rs.get(0).get(0);
    	
    	// retrieve of variable logged for all device
    	Object []objects = null;
    	if ("FALSE".equalsIgnoreCase(isHaccp))
    	{
	    	sql = "select count(*)=? as ok,cfvariable.idvarmdl from cfvariable,cfvarmdl where cfvariable.idvarmdl=cfvarmdl.idvarmdl and " +
	    			"cfvarmdl.iddevmdl=?  and cfvariable.idhsvariable is "+(interval==0?"not":"")+" NULL and iscancelled = ? group by cfvariable.idvarmdl";
	    	
	    	objects= new Object[]{dev_number,new Integer(iddevmdl),"FALSE"};
    	}
    	else
    	{
    		sql = "select count(*)=? as ok,cfvariable.idvarmdl from cfvariable,cfvarmdl where cfvariable.idvarmdl=cfvarmdl.idvarmdl and " +
			"cfvarmdl.iddevmdl=?  and cfvariable.ishaccp=? and iscancelled = ? group by cfvariable.idvarmdl";
	
    		objects= new Object[]{dev_number,new Integer(iddevmdl),"TRUE","FALSE"};
    	}
        rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
        List<Integer> idvarmdl = new ArrayList<Integer>();
        if (rs!=null & rs.size()>0)
        {
        	Record r = null;
        	
        	for (int i=0;i<rs.size();i++)
        	{
        		r = rs.get(i);
        		if ((Boolean) r.get("ok") == true)
        		{
        			idvarmdl.add((Integer)r.get("idvarmdl"));
        		}
        	}
        }
        
        // retrieve of Variable informations
        StringBuffer sql_buff = new StringBuffer("select cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr from cfvarmdl, cftableext where " +
        		"cftableext.tableid=cfvarmdl.idvarmdl and cftableext.tablename='cfvarmdl' and cftableext.languagecode=? and " +
        		"cftableext.idsite=1 and cfvarmdl.idvarmdl in (");
        for (int i=0;i<idvarmdl.size();i++)
        {
        	sql_buff.append(idvarmdl.get(i));
        	if (i < (idvarmdl.size() - 1))
        	{
        		sql_buff.append(",");
        	}
        }
        sql_buff.append(")");
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql_buff.toString(),new Object[]{language});
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
    
  //Ing. Gilioli Manuel Fine 4 report 
    
 //Matteo M. Start Controllo Parametri
    public VarMdlBean[] retrieveOrderedIfDevIsPresent(String language,int idsite,int iddevmdl,String isHaccp,Integer interval, boolean onlyParameters) throws DataBaseException
    {
    	String sql ="select cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr "+ 
        "from cfvarmdl,cftableext,(select  distinct iddevmdl  from cfdevice )as a "+
        "where  a.iddevmdl=cfvarmdl.iddevmdl "+ 
        "and    cfvarmdl.idvarmdl=cftableext.tableid "+
        "and    cftableext.languagecode = ? "+
        "and cftableext.tablename='cfvarmdl' "+
        "and cfvarmdl.idsite = ? "+
        "and cfvarmdl.iddevmdl = ? "+
        "and cftableext.idsite = ? ";
    	if (onlyParameters) sql+=" and cfvarmdl.readwrite>1 and cfvarmdl.type<>4 ";
    	sql+=
        ("FALSE".equalsIgnoreCase(isHaccp)?"":"and cfvarmdl.ishaccp = ?") +
        "order by cftableext.description ";
        
    	Object []objects=null;
		if("FALSE".equalsIgnoreCase(isHaccp))
			objects= new Object[]{language,new Integer(idsite),new Integer(iddevmdl),new Integer(idsite)};
		else
			objects= new Object[]{language,new Integer(idsite),new Integer(iddevmdl),new Integer(idsite),isHaccp};
			
		
    	
    	
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
    
  //Ing. Gilioli Manuel Fine 4 report     
 
    // retrieve no alarm, no readonly
    // used by VisualScheduler.VSCategory
    public static VarMdlBean[] retrieveVSVarMdls(int idsite,int iddevmdl, String language) throws DataBaseException
    {
        String sql = "select cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr "
        	+ "from cfvarmdl inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and "
        	+ "cftableext.tablename='cfvarmdl' and cfvarmdl.idsite = ? and cfvarmdl.iddevmdl = ? and cftableext.idsite = ? "
        	+ "where cfvarmdl.type < 4 and cfvarmdl.readwrite <> '1' order by cfvarmdl.code";
        
        Object[] param = new Object[4];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevmdl);
        param[3] = new Integer(idsite);
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,param);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
   
    public VarMdlBean[] retrieveOrderedIfDevIsPresentLog(String language,int idsite,int iddevmdl) throws DataBaseException
    {
    	String sql = "";
    	Object []objects=null;
    	
    	sql = "select distinct cfvarmdl.*, cftableext.description, cftableext.shortdescr, cftableext.longdescr "+
    		"from cfvarmdl "+
    		"inner join cftableext on cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and cftableext.idsite =?  and cftableext.tableid=cfvarmdl.idvarmdl "+
    		"inner join cfvariable on cfvariable.iscancelled ='FALSE' and cfvariable.idhsvariable is null and cfvariable.idvarmdl=cfvarmdl.idvarmdl "+
    		"where cfvarmdl.iddevmdl=? order by cftableext.description ";
		objects= new Object[]{language,new Integer(idsite),new Integer(iddevmdl)};
    	
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,objects);
       
        VarMdlBean[] listVar = new VarMdlBean[rs.size()];
    
        for (int i = 0; i < rs.size(); i++)
        {
            listVar[i] = new VarMdlBean(rs.get(i));
        }
    
        return listVar;
    }
}
