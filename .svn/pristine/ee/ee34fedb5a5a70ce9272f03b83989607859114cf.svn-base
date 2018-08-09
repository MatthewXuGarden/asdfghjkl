package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.parameters.ParametersMgr;


public class VarphyBeanList
{
    private static final String TB_VAR = "cfvariable";
    private static Map<Integer, VarphyBean> varMap = new HashMap<Integer, VarphyBean>();

    //private static final String DISPLAY_STATE_ON = "MAIN";
    public static VarphyBean[] getListVarWritable(String language, int site, int[] id)
        throws Exception
    {
        return getListVarWrite(language, site, id);
    }

    public static VarphyBean[] getListVar(String language, int site, int[] id)
        throws Exception
    {
        return getListVar(language, site, id, "NONE", "");
    }

    public static VarphyBean[] getListVar(String language, int site, int[] id, String orderby)
        throws Exception
    {
        return getListVar(language, site, id, "NONE", orderby);
    }

    public static VarphyBean[] getMainListVarToDisplay(String language, int site, int[] id)
        throws Exception
    {
        return getListVar(language, site, id, "HOME", "cfvariable.priority,cftableext.description", true);
    }

    public static VarphyBean[] getListVarToDisplay(String language, int site, int[] id)
        throws Exception
    {
        return getListVar(language, site, id, "MAIN", "cfvariable.priority,cftableext.description");
    }

    public static VarphyBean[] getStateListVarToDisplay(String language, int site, int[] id) throws Exception
	{
	    return getListVar(language, site, id, "STAT", "cfvariable.priority,cftableext.shortdescr");
	}

    private static VarphyBean[] getListVar(String language, int site, int[] id, String toDisplay,
        String orderBy) throws Exception
    {
        return getListVar(language, site, id, toDisplay, orderBy, false);
    }

    public static VarphyBean[] getListVar(String language, int site, int[] id, String toDisplay,
        String orderBy, boolean onlyRead) throws Exception
    {
        List<Comparable<?>> params = new ArrayList<Comparable<?>>();

        params.add(language);
        params.add(new Integer(site));
        params.add("FALSE");
        params.add(new Integer(site));

        StringBuffer sql = new StringBuffer();

        sql.append(
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr ");
        sql.append("from " + TB_VAR +
            " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? and ");
        sql.append(
            "cfvariable.idsite=? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? ");

        if (null != toDisplay)
        {
            sql.append(" and cfvariable.todisplay=?");
            params.add(toDisplay);
        }

        if (onlyRead)
        {
            sql.append(" and cfvariable.readwrite=?");
            params.add("1 ");
        }

        sql.append(" and cfvariable.iddevice = ? ");

        if (orderBy.equalsIgnoreCase(""))
        {
            sql.append(" order by cfvariable.iddevice");
        }
        else
        {
            sql.append(" order by " + orderBy);
        }

        return commonLinkLanguageMulti(language, sql.toString(), site, params, id);
    }
    public static Map getByiddevice_devmdlcodes(String language,int site,List paramList,boolean readwrite)
    	throws Exception
    {
    	if(paramList == null ||paramList.size() == 0)
    	{
    		return null;
    	}
    	int size = 0;
    	for(int i=0;i<paramList.size();i++)
    	{
    		String[] temp = (String[])paramList.get(i);
    		size += temp.length;
    	}
    	Object[] params = new Object[4+size];
        params[0] = language;
        params[1] = new Integer(site);
        params[2] = "FALSE";
        params[3] = new Integer(site);
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from " +
            TB_VAR +
        " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.languagecode = ? and ");
        sql.append("cfvariable.idsite=? and ");
        if(readwrite == true)
        {
        	sql.append("cfvariable.readwrite <> '1' and ");
        }
        sql.append(
            "(cfvariable.buttonpath is Null  or cfvariable.buttonpath = '') and cftableext.tablename='cfvariable' and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null  and cftableext.idsite = ? and ");
        if(paramList.size()>0)
        {
        	sql.append("(");
        }
        size = 0;
        for(int i=0;i<paramList.size();i++)
        {
        	sql.append("(");
        	String[] temp = (String[])paramList.get(i);
        	if(temp.length<2)
        	{
        		continue;
        	}
        	sql.append("cfvariable.iddevice=? and (cfvariable.code=");
        	params[4+size++] = temp[0];
        	for(int j=1;j<temp.length;j++)
        	{
        		params[4+size++] = temp[j];
        		sql.append("?");
        		if(j<temp.length-1)
        		{
        			sql.append(" or cfvariable.code=");
        		}
        	}
        	if(i<paramList.size()-1)
        	{
        		sql.append(")) or ");
        	}
        	else
        	{
        		sql.append("))");
        	}
        }
        sql.append(")");
        sql.append("order by cfvariable.iddevice,cftableext.description");

        Map result = new HashMap();
        VarphyBean[] vars = commonLinkLanguage(language, sql.toString(), site, params);
        VarphyBean last = null;
        List varList = new ArrayList();
        for(int i=0;i<vars.length;i++)
        {
        	if(last == null)
        	{
        		result.put(vars[i].getDevice(), varList);
        		varList.add(vars[i]);
        		last = vars[i];
        	}
        	else
        	{
        		if(vars[i].getDevice().equals(last.getDevice()))
        		{
        			varList.add(vars[i]);
        		}
        		else
        		{
        			varList = new ArrayList();
        			result.put(vars[i].getDevice(), varList);
        			varList.add(vars[i]);
        			last = vars[i];
        		}
        	}
        }
        return result;
    }
    public static Map getByiddeviceFromWizard_paramsetting(String language,int site,int[] ids,boolean readwrite)
	throws Exception
{
	if(ids.length == 0)
	{
		return null;
	}
	Object[] params = new Object[4+ids.length];
    params[0] = language;
    params[1] = new Integer(site);
    params[2] = "FALSE";
    params[3] = new Integer(site);
    String sql = "";
    sql = " select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from " +
            TB_VAR +
        " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.languagecode = ? and "+
        " cfvariable.idsite=? and ";
    if(readwrite == true)
    {
    	sql += " cfvariable.readwrite <> '1' and ";
    }
    sql += " (cfvariable.buttonpath is Null  or cfvariable.buttonpath = '') and cftableext.tablename='cfvariable' and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null  and cftableext.idsite = ? "+
    	" inner join cfdevice on cfdevice.iddevice=cfvariable.iddevice "+
        " inner join cfdevmdl on cfdevmdl.iddevmdl=cfdevice.iddevmdl "+
        " inner join wizard_paramsetting on wizard_paramsetting.varmdlcode=cfvariable.code and cfdevmdl.code=wizard_paramsetting.devmdlcode "+
        " where ";
    int size = 0;
    for(int i=0;i<ids.length;i++)
    {
    	if(i<ids.length-1)
    	{
    		sql += " cfvariable.iddevice=? or";
    	}
    	else
    	{
    		sql += " cfvariable.iddevice=? ";
    	}
    	params[4+i] = ids[i];
    }
    sql += " order by cfvariable.iddevice,wizard_paramsetting.position,cfvariable.idvariable ";
    Map result = new HashMap();
    VarphyBean[] vars = commonLinkLanguage(language, sql.toString(), site, params);
    VarphyBean last = null;
    List varList = new ArrayList();
    for(int i=0;i<vars.length;i++)
    {
    	if(last == null)
    	{
    		result.put(vars[i].getDevice(), varList);
    		varList.add(vars[i]);
    		last = vars[i];
    	}
    	else
    	{
    		if(vars[i].getDevice().equals(last.getDevice()))
    		{
    			varList.add(vars[i]);
    		}
    		else
    		{
    			varList = new ArrayList();
    			result.put(vars[i].getDevice(), varList);
    			varList.add(vars[i]);
    			last = vars[i];
    		}
    	}
    }
    return result;
}
    public static VarphyBean[] getByiddevice_devmdlcodes(String language,int site,int iddevice,String[] varmdlcodes)
	throws Exception
	{
		Object[] params = new Object[4+1+varmdlcodes.length];
	    params[0] = language;
	    params[1] = new Integer(site);
	    params[2] = "FALSE";
	    params[3] = new Integer(site);
	    params[4] = new Integer(iddevice);
	    StringBuffer sql = new StringBuffer();
	    sql.append(
	        "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from " +
	        TB_VAR +
		" inner join cfvarmdl on cfvarmdl.idvarmdl=cfvariable.idvarmdl"+
	    " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.languagecode = ? and ");
	    sql.append("cfvariable.idsite=? and ");
	    sql.append("cfvariable.readwrite <> '1' and ");
	    sql.append(
	        "(cfvariable.buttonpath is Null  or cfvariable.buttonpath = '') and cftableext.tablename='cfvariable' and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null  and cftableext.idsite = ? and ");
	    sql.append("cfvariable.iddevice = ?");
	    sql.append(" and cfvarmdl.code in (");
	    for(int i=0;i<varmdlcodes.length;i++)
	    {
	    	params[5+i] = varmdlcodes[i];
	    	sql.append("?");
	    	if(i<varmdlcodes.length-1)
	    	{
	    		sql.append(",");
	    	}
	    }
	    sql.append(")");
	    sql.append("order by cfvarmdl.idvargroup, cfvarmdl.priority");
	
	    return commonLinkLanguage(language, sql.toString(), site, params);
	}
    public static VarphyBean[] getByIddevmdl_varcodes(String language,int site,int iddevmdl,String[] varCodes)
	throws Exception
	{
		Object[] params = new Object[4+varCodes.length];
		params[0] = new Integer(iddevmdl);
	    params[1] = language;
	    params[2] = new Integer(site);
	    params[3] = new Integer(site);
	    StringBuffer sql = new StringBuffer();
	    sql.append(
	        "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from " +
	        TB_VAR +
	        " inner join cfdevice on cfvariable.iddevice=cfdevice.iddevice and cfdevice.iscancelled='FALSE' and cfdevice.iddevmdl=? "+
	    	" inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.languagecode = ? and cftableext.idsite = ? and ");
	    sql.append("cfvariable.idsite=? and ");
	    sql.append(
	        "cftableext.tablename='cfvariable' and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null  and ");
	    sql.append(" cfvariable.code in (");
	    for(int i=0;i<varCodes.length;i++)
	    {
	    	params[4+i] = varCodes[i];
	    	sql.append("?");
	    	if(i<varCodes.length-1)
	    	{
	    		sql.append(",");
	    	}
	    }
	    sql.append(") ");
	    sql.append("order by cfvariable.iddevice");
	
	    return commonLinkLanguage(language, sql.toString(), site, params);
	}
    private static VarphyBean[] getListVarWrite(String language, int site, int[] id)
        throws Exception
    {
        Object[] params = new Object[id.length + 4];
        params[0] = language;
        params[1] = new Integer(site);
        params[2] = "FALSE";
        params[3] = new Integer(site);

        StringBuffer sql = new StringBuffer();

        sql.append(
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from " +
            TB_VAR +
            " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.languagecode = ? and ");
        sql.append("cfvariable.idsite=? and ");
        sql.append("cfvariable.readwrite <> '1' and ");
        sql.append(
            "(cfvariable.buttonpath is Null  or cfvariable.buttonpath = '') and cftableext.tablename='cfvariable' and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null  and cftableext.idsite = ? and ");
        sql.append("cfvariable.iddevice in (");

        for (int i = 0; i < id.length; i++)
        {
            params[i + 4] = new Integer(id[i]);
            sql.append("?");

            if (i < (id.length - 1))
            {
                sql.append(",");
            }
        }

        sql.append(")");
        
        //sql.append(" order by cfvariable.idvargroup, cfvariable.priority");  //order by cfvariable.priority
        
        //order by cfvarmdl.priority 
        //sql.append(" inner join cfvarmdl on cfvariable.idvarmdl = cfvarmdl.idvarmdl order by cfvarmdl.priority");
        sql.append(" left outer join  cfvariable as v2 on  cfvariable.islogic='TRUE' and cfvariable.idvarmdl=v2.idvariable and v2.idsite=1 and cfvariable.idsite=1 "+
        		   " left outer join  cfvarmdl on v2.idvarmdl = cfvarmdl.idvarmdl left outer join cfvarmdl as m2 on cfvariable.islogic='FALSE' and  cfvariable.idvarmdl = m2.idvarmdl "+
        		   " order by m2.idvargroup, m2.priority, cfvarmdl.idvargroup, cfvarmdl.priority ");

        return commonLinkLanguage(language, sql.toString(), site, params);
    }

    public VarphyBean[] getAllVarOfDevice(String lang, int site, int iddevice)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by cftableext.description";

        return commonLinkLanguage(lang, sql, site,
            new Object[] { lang, new Integer(site), new Integer(iddevice), "FALSE", new Integer(
                    site) });
    }
    
    public VarphyBean[] getDatatransferVarOfDevice(String lang, int site, int iddevice)
    throws Exception
{
    String sql =
        "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
        "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
        "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is null and cftableext.idsite = ? order by cftableext.description";

    return commonLinkLanguage(lang, sql, site,
        new Object[] { lang, new Integer(site), new Integer(iddevice), "FALSE", new Integer(
                site) });
}

    public static int[] getIdsOfVarsOfDevice(String lang, int site, int iddevice)
        throws Exception
    {
        String sql = "select idvariable from cfvariable where idsite = ? and iddevice = ? " +
            "and iscancelled=? order by idvariable";

        Object[] param = new Object[3];
        param[0] = new Integer(site);
        param[1] = new Integer(iddevice);
        param[2] = "FALSE";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        int[] ids = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            ids[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
        }

        return ids;
    }
    
    public static int[] getIdsOfVarsOfDeviceNoHist(String lang, int site, int iddevice)
    throws Exception
	{
	    String sql = "select idvariable from cfvariable where idsite = ? and iddevice = ? " +
	        "and iscancelled=? and idhsvariable is not null order by idvariable";
	
	    Object[] param = new Object[3];
	    param[0] = new Integer(site);
	    param[1] = new Integer(iddevice);
	    param[2] = "FALSE";
	
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
	    int[] ids = new int[rs.size()];
	
	    for (int i = 0; i < rs.size(); i++)
	    {
	        ids[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
	    }
	
	    return ids;
	}

    public VarphyBean[] getAlarmVarPhy(String lang, int site, int iddevice)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type = 4 and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by idvariable";

        return commonLinkLanguage(lang, sql, site,
            new Object[] { lang, new Integer(site), new Integer(iddevice), "FALSE", new Integer(
                    site) });
    }
    
    public VarphyBean[] getActiveAlarmVarPhy(String lang, int site, int iddevice)
    throws Exception
    {
    String sql =
        "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
        "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
        "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type = 4 " +
        "and cfvariable.iscancelled=? and cfvariable.isactive='TRUE' " +
        "and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by idvariable";

    return commonLinkLanguage(lang, sql, site,
        new Object[] { lang, new Integer(site), new Integer(iddevice), "FALSE", new Integer(
                site) });
    }

    public VarphyBean[] getVarNotAlarms(String lang, int site, int iddevice)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type != ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by cfvariable.type desc";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(iddevice), new Integer(4), "FALSE",
                new Integer(site)
            });
    }

    public VarphyBean[] getVarNotAlarmsRw(String lang, int site, int iddevice)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type != ? and cfvariable.readwrite !='1' " +
            " and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by cfvariable.type desc,cftableext.description";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(iddevice), new Integer(4), "FALSE",
                new Integer(site)
            });
    }

    public VarphyBean[] getListVarRwByType(String lang, int site, int iddevice, int varType)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type = ? and cfvariable.readwrite !='1' and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ?";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(iddevice), new Integer(varType), "FALSE",
                new Integer(site)
            });
    }

    public VarphyBean[] getListVarByIds(int site, String lang, int[] ids)
        throws Exception
    {
        StringBuffer sqltmp = new StringBuffer(
                "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
                "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
                "and cfvariable.idsite = ? and cfvariable.iscancelled=? and cftableext.idsite = ? and cfvariable.idvariable in (");
        ArrayList<Object> paramsTmp = new ArrayList<Object>();
        paramsTmp.add(lang);
        paramsTmp.add(new Integer(site));
        paramsTmp.add("FALSE");
        paramsTmp.add(new Integer(site));

        for (int i = 0; i < (ids.length - 1); i++)
        {
            paramsTmp.add(new Integer(ids[i]));
            sqltmp.append("?,");
        }

        paramsTmp.add(new Integer(ids[ids.length - 1]));
        sqltmp.append("?)");

        String sql = sqltmp.toString();
        Object[] params = paramsTmp.toArray();

        return commonLinkLanguage(lang, sql, site, params);
    }

    private static VarphyBean[] commonLinkLanguageMulti(String language, String sql, int site,
        List<Comparable<?>> params, int[] ids) throws Exception
    {
    	ArrayList<VarphyBean> listVar = new ArrayList<VarphyBean>();
        RecordSet rs = null;

        try
        {
            for (int j = 0; j < ids.length; j++)
            {
                params.add(new Integer(ids[j]));

                Object[] values = new Object[params.size()];
                values = (Object[]) params.toArray(values);
                rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);

                VarphyBean tmp = null;

                for (int i = 0; i < rs.size(); i++)
                {
                    tmp = new VarphyBean(rs.get(i));
                    listVar.add(tmp);
                    varMap.put(tmp.getId(), tmp);
                }

                params.remove(params.size() - 1);
            }
        }
        catch (Exception e)
        {
            EventMgr.getInstance().log(new Integer(1), "PVPro", "ACTION",
                EventDictionary.TYPE_WARNING, "PV001", null);
        }

        VarphyBean[] result = new VarphyBean[listVar.size()];
        result = (VarphyBean[]) listVar.toArray(result);

        return result;
    }

    private static VarphyBean[] commonLinkLanguage(String language, String sql, int site,
        Object[] params) throws Exception
    {
        VarphyBean[] listVar = null;
        RecordSet rs = null;

        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);

            listVar = new VarphyBean[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                listVar[i] = new VarphyBean(rs.get(i));
                varMap.put(listVar[i].getId(), listVar[i]);
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            EventMgr.getInstance().log(new Integer(1), "PVPro", "ACTION",
                EventDictionary.TYPE_WARNING, "PV001", null);
            listVar = new VarphyBean[0];
        }

        return listVar;
    }

    public void updateIsActive(int idsite, int idvar, int frequency, int priority)
		throws DataBaseException
    {
        String sql = "update cfvariable set isactive = ?,frequency = ?, priority = ?,lastupdate = ? where idsite = ? and idvariable = ? ";
        Object[] param = new Object[6];
        param[0] = "TRUE";
        param[1] = new Integer(frequency);
        param[2] = new Integer(priority);
        param[3] = new Timestamp(System.currentTimeMillis());
        param[4] = new Integer(idsite);
        param[5] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public void updateIsNotActive(int idsite, int idvar)
        throws DataBaseException
    {
        String sql = "update cfvariable set isactive = ?, frequency = ?, priority = ?,lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[6];
        param[0] = "FALSE";
        param[1] = null;
        param[2] = new Integer(1);
        param[3] = new Timestamp(System.currentTimeMillis());
        param[4] = new Integer(idsite);
        param[5] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }
	
	public void updateIsHACCP(int idsite, int idvar, String ishaccp)
	    throws DataBaseException
	{
	    String sql = "update cfvariable set ishaccp = ? where idsite = ? and idvariable = ?";
	    Object[] param = new Object[3];
	    param[0] = ishaccp;
	    param[1] = new Integer(idsite);
	    param[2] = new Integer(idvar);
	
	    DatabaseMgr.getInstance().executeStatement(null, sql, param);
	}

    public void updateFrequency(int idsite, int idvar, int freq)
        throws DataBaseException
    {
        String sql = "update cfvariable set frequency = ?,lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[4];

        if (freq == 0)
        {
            param[0] = null;
        }
        else
        {
            param[0] = new Integer(freq);
        }
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public void updateFreqAndVarMin(int idsite, int idvar, int freq, double varmin)
        throws DataBaseException
    {
        String sql = "update cfvariable set frequency = ?,delta = ?,lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[5];

        if (freq == 0)
        {
            param[0] = null;
        }
        else
        {
            param[0] = new Integer(freq);
        }

        if (varmin == 0)
        {
            param[1] = null;
        }
        else
        {
            param[1] = new Double(varmin);
        }
        param[2] = new Timestamp(System.currentTimeMillis());
        param[3] = new Integer(idsite);
        param[4] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static String getShortDescriptionOfVars(int idsite, String languagecode, int idvariable)
        throws DataBaseException
    {
        String sql = "select cftableext.description from cftableext,cfvariable where cfvariable.idsite=? and cftableext.idsite = ? and cftableext.languagecode = ? and cftableext.tablename='cfvariable' and cftableext.tableid=cfvariable.idvariable and cfvariable.idvariable = ? and cfvariable.iscancelled ='FALSE'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    new Integer(idsite), new Integer(idsite), languagecode, new Integer(idvariable)
                });

        if (rs.size() != 0)
        {
            return rs.get(0).get("description").toString();
        }
        else
        {
            return null;
        }
    }

    public int getDeviceOfVariable(int idvar, int idSite)
    {
        String sql = "select iddevice from cfvariable where idvariable=? and idsite=? and iscancelled=?";
        Object[] param = { new Integer(idvar), new Integer(idSite), "FALSE" };
        Integer deviceid = null;

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
            Record r = null;

            if ((rs != null) && (rs.size() > 0))
            {
                r = rs.get(0);
            }

            if (r != null)
            {
                deviceid = ((Integer) r.get("iddevice"));
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        if (deviceid != null)
        {
            return deviceid.intValue();
        }
        else
        {
            return 0;
        }
    }

    public VarphyBean[] getVarNotAlarmsForCondition(String lang, int site, int iddevice)
        throws Exception
    {
        String sql = "";
        
        sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' " +
            "and cftableext.languagecode = ? and cfvariable.idsite = ? and cfvariable.iddevice = ? " +
            "and cfvariable.type != ? and cfvariable.iscancelled=? and cfvariable.ishaccp=? and cftableext.idsite = ? " +
            "and cfvariable.frequency is not null and cfvariable.frequency != 0 and cfvariable.idhsvariable is null order by cfvariable.type desc";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(iddevice), new Integer(4), "FALSE", "FALSE",
                new Integer(site)
            });
    }
    
    
    public VarphyBean[] getAllVarNotAlarmsHistorical(String lang, int site)
        throws Exception
    {
        String sql = "";
        sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' " +
            "and cftableext.languagecode = ? and cfvariable.idsite = ? " +
            "and cfvariable.type != ? and cfvariable.iscancelled=? and cfvariable.ishaccp=? and cftableext.idsite = ? " +
            "and cfvariable.frequency is not null and cfvariable.frequency != 0 order by cfvariable.type desc";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(4), "FALSE", "FALSE", new Integer(site)
            });
    }
    
    
    public VarphyBean[] getAllVarNotAlarmsHACCP(String lang, int site)
        throws Exception
    {
        String sql = "";
        sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ?" +
            "inner join cftableext as deviceext on cfvariable.iddevice = deviceext.tableid and deviceext.tablename='cfdevice' and deviceext.languagecode = ?"+
            " Where cfvariable.idsite = ? " +
            "and cfvariable.type != ? and cfvariable.iscancelled=? and cfvariable.ishaccp=? and cftableext.idsite = ?  and deviceext.idsite=? " +
            "and cfvariable.frequency is not null and cfvariable.idhsvariable is not null and cfvariable.frequency != 0  order by deviceext.description,cftableext.description";

        return commonLinkLanguage(lang, sql, site,
            new Object[] { lang, lang, new Integer(site), new Integer(4), "FALSE", "TRUE", new Integer(
                    site),new Integer(site) });
    }

    public VarphyBean[] getVarNotAlarmsHACCP(String lang, int site, int iddevice)
        throws Exception
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' " +
            "and cftableext.languagecode = ? and cfvariable.idsite = ? " +
            "and cfvariable.type != ? and cfvariable.iscancelled=? and cfvariable.ishaccp=? and cftableext.idsite = ? " +
            "and cfvariable.frequency is not null and cfvariable.idhsvariable is not null " +
            "and cfvariable.frequency != 0 and cfvariable.iddevice=? order by cfvariable.type desc";

        return commonLinkLanguage(lang, sql, site,
            new Object[]
            {
                lang, new Integer(site), new Integer(4), "FALSE", "TRUE", new Integer(site),
                new Integer(iddevice)
            });
    }

    public VarphyBean getVarById(int idvar)
    {
        return varMap.get(new Integer(idvar));
    }

    public static void clearMap()
    {
        varMap.clear();
    }

    public static VarphyBean retrieveVarById(int idsite, int idvar, String lang)
        throws DataBaseException
    {
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr, cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
            "and cfvariable.idsite = ? and cfvariable.idvariable = ? and cftableext.idsite = cfvariable.idsite";
        
        Object[] param = new Object[3];
        param[0] = lang;
        param[1] = new Integer(idsite);
        param[2] = new Integer(idvar);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() > 0)
        {
            return new VarphyBean(rs.get(0));
        }
        else
        {
            return null;
        }
    }
    
    public static VarphyBean retrieveVarByUuid(int idsite, String uuid, String lang)
    throws DataBaseException
{
    String dev_code = uuid.split(":")[1];
    String var_code = uuid.split(":")[2];
    String sql =
        "select cfvariable.*, cftableext.description, cftableext.longdescr, cftableext.shortdescr from cfvariable " +
        "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
        "and cfvariable.idsite = ? and cfvariable.code = ? and cftableext.idsite = cfvariable.idsite " +
        "and cfvariable.iddevice=(select iddevice from cfdevice where code=? and iscancelled='FALSE')";
    
    Object[] param = new Object[4];
    param[0] = lang;
    param[1] = new Integer(idsite);
    param[2] = var_code;
    param[3] = dev_code;

    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

    if (rs.size() > 0)
    {
        return new VarphyBean(rs.get(0));
    }
    else
    {
        return null;
    }
}
    
    public static VarphyBean retrieveVarByCode(int idSite, String varCode, int idDev, String lang)
    {
		VarphyBean varBean = null;
		
		String sql =
	        "select cfvariable.*, cftableext.description, cftableext.longdescr, cftableext.shortdescr from cfvariable " +
	        " inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode='"+lang+"' " + 
	        " and cfvariable.idsite="+idSite+" and cfvariable.code='"+varCode+"' and cfvariable.iddevice="+idDev+" and cftableext.idsite=cfvariable.idsite " + 
	        " and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null";
	
	    try
	    {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);

			if (rs.size() > 0)
			{
				varBean = new VarphyBean(rs.get(0));
			}
		}
	    catch (Exception e)
	    {
	    	Logger logger = LoggerMgr.getLogger(VarphyBeanList.class);
			logger.error(e);
		}
	    
	    return varBean;
    }

    public static RecordSet retrieveVarByDevice(int idSite, int idDev, boolean onlyhistory) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct idvariable, type, addressin, addressout, vardimension, readwrite, ishaccp, idhsvariable ");
		sql.append(" from cfvariable ");
		sql.append(" where cfvariable.idsite=");
		sql.append(idSite);
		sql.append(" and ");
		sql.append(" cfvariable.iddevice=");
		sql.append(idDev);
		sql.append(" and cfvariable.iscancelled='FALSE' ");
		sql.append(" and (type=4 or cfvariable.idhsvariable is "+(onlyhistory?"":" not ")+" null )");

		try {
			return DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
					null);
		} catch (Exception e) {
			LoggerMgr.getLogger(VarphyBeanList.class).error(e);
		}
		return null;
	}
    
    public static RecordSet retrieveVarByDevice(int idSite, int idDev) {
		return retrieveVarByDevice(idSite, idDev, false);
	}
    //BIOLO
    
    public static RecordSet retrieveVarByDevice(int idSite, int idDev,boolean log,boolean highestAlarm) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct idvariable, type, addressin, addressout, vardimension,varlength, readwrite, ishaccp, idhsvariable ");
		sql.append(" from cfvariable ");
		sql.append(" where cfvariable.idsite=");
		sql.append(idSite);
		sql.append(" and ");
		sql.append(" cfvariable.iddevice=");
		sql.append(idDev);
		sql.append(" and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null");
		sql.append(" and (");
		if(log)
			sql.append("(idhsvariable<>-1) or ");
		if(highestAlarm)
			sql.append("(type=4 and priority=1)");
		sql.append(") order by type,addressin");
		String sqlStr = sql.toString().replace("or )", " )");

		try {
			return DatabaseMgr.getInstance().executeQuery(null, sqlStr,
					null);
		} catch (Exception e) {
			LoggerMgr.getLogger(VarphyBeanList.class).error(e);
		}
		return null;
	}
    
    public VarphyBean[] getLoggedVarsOfDevice(String lang, int site, int iddevice,
			String isHaccp,Integer interval) throws Exception 
	{
		String sql = "";
		Object []objects=null;
		
		if (isHaccp.equalsIgnoreCase("TRUE"))
		{
			sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? "
				+ "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and "
				+ "cftableext.idsite = ? and ishaccp = ? order by cftableext.description";
			objects= new Object[] { lang,
					new Integer(site), new Integer(iddevice), "FALSE",
					new Integer(site), "TRUE" };
		}
		else
		{
			sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? "
				+ "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is null and "
				+ "cftableext.idsite = ? and ishaccp = ? order by cftableext.description";
			objects= new Object[] { lang,
					new Integer(site), new Integer(iddevice), "FALSE",
					new Integer(site), "FALSE" };
		}
		
		return commonLinkLanguage(lang, sql, site,objects);
	}
    
    // versione senza longdescr:
    public VarphyBean[] getLogVarsOfDevice(String lang, int site, int iddevice,
			String isHaccp,Integer interval) throws Exception 
	{
		String sql = "";
		Object []objects=null;
		
		if (isHaccp.equalsIgnoreCase("TRUE"))
		{
			sql = "select cfvariable.*, cftableext.description, cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode=? "
				+ "and cfvariable.idsite=? and cfvariable.iddevice=? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and "
				+ "cftableext.idsite=? and ishaccp=? order by cftableext.description";
			objects= new Object[] { lang,
					new Integer(site), new Integer(iddevice), "FALSE",
					new Integer(site), "TRUE" };
		}
		else
		{
			sql = "select cfvariable.*, cftableext.description, cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode=? "
				+ "and cfvariable.idsite=? and cfvariable.iddevice=? and cfvariable.iscancelled=? and cfvariable.idhsvariable is null and "
				+ "cftableext.idsite=? and ishaccp=? order by cftableext.description";
			objects= new Object[] { lang,
					new Integer(site), new Integer(iddevice), "FALSE",
					new Integer(site), "FALSE" };
		}
		
		return commonLinkLanguage(lang, sql, site,objects);
	}
    
//Ing. Gilioli Manuel Start 4 report
    
    public VarphyBean[] getAllVarOfDevice(String lang, int site, int iddevice,
			String isHaccp,Integer interval) throws Exception {
		String sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? "
				+ "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and "
				+ "cftableext.idsite = ? " +("FALSE".equalsIgnoreCase(isHaccp)?"":"and ishaccp = ?")+" order by cftableext.description";

		Object []objects=null;
		if("FALSE".equalsIgnoreCase(isHaccp))
			objects= new Object[] { lang,
				new Integer(site), new Integer(iddevice), "FALSE",
				new Integer(site) };
		else
			objects= new Object[] { lang,
				new Integer(site), new Integer(iddevice), "FALSE",
				new Integer(site), isHaccp };
			
		
		return commonLinkLanguage(lang, sql, site,objects);
	}

    // versione senza longdescr:
    public VarphyBean[] getAllVarsOfDevice(String lang, int site, int iddevice,
			String isHaccp,Integer interval) throws Exception {
		String sql = "select cfvariable.*, cftableext.description, cftableext.shortdescr from cfvariable "
				+ "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? "
				+ "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and "
				+ "cftableext.idsite = ? " +("FALSE".equalsIgnoreCase(isHaccp)?"":"and ishaccp = ?")+" order by cftableext.description";

		Object []objects=null;
		if("FALSE".equalsIgnoreCase(isHaccp))
			objects= new Object[] { lang,
				new Integer(site), new Integer(iddevice), "FALSE",
				new Integer(site) };
		else
			objects= new Object[] { lang,
				new Integer(site), new Integer(iddevice), "FALSE",
				new Integer(site), isHaccp };
			
		
		return commonLinkLanguage(lang, sql, site,objects);
	}   
    
//Funzione che mappa da iddevice-idvarmdl a iddevice-idvarmdl-idvariable
    
    public static String getMappingIdDevIdVarMdlIdDevIdVar(String language, String devices, String varmodels, String exclude) throws Exception {
    	
    	//"exclude" contiene le variabili gi� in tabella
    	//   quindi so quante variabili ci sono (per il limite MaxVar)
    	//   e quali non inserire (perch� sarebbero doppie)
    	ArrayList<Integer> varToExclude = new ArrayList<Integer>();
    	String [] excludeList = exclude.split("-");
    	for (int i = 0; i < excludeList.length; i++) {
    		try{
    			Integer varEx = new Integer(excludeList[i]);
    			varToExclude.add(varEx);
    		} catch (NumberFormatException e){
    			//non era un numero.. vado avanti!!!
    		}
    	}
    	int parametriInControllo = varToExclude.size();
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	String dd = devices.replace('-', ',');
    	String vv = varmodels.replace('-', ',');
    	String sql=" select "+ 
    	" iddevice, idvarmdl, idvariable, "+ 
    	" d.description as devdesc, v.description as vardesc "+  
    	" from cfvariable, cftableext as d, cftableext as v "+
    	" where iddevice in ( " + dd.substring(0, dd.length()-1) + " ) " +
    	" and idvarmdl in (" + vv.substring(0, vv.length()-1) + " ) "+
    	" and "+ 
    	" d.idsite=1 and d.tablename='cfdevice' and d.tableid=cfvariable.iddevice and d.languagecode='"+language+"' "+
    	" and "+ 
    	" v.idsite=1 and v.tablename='cfvariable' and v.tableid=cfvariable.idvariable and v.languagecode='"+language+"' "+
    	" and "+ 
    	" cfvariable.idhsvariable is not null"+
    	" and "+ 
    	" cfvariable.iscancelled = 'FALSE' "+ 
    	" order by devdesc, vardesc limit "+(ParametersMgr.getParametersCFG().getPlimit()+1);
    	RecordSet rs=null;
    	rs=DatabaseMgr.getInstance().executeQuery(null,sql,null);

    	//fich� ci sono record dalla query e finch� non supero il limite...
    	for(int i=0;i<rs.size() && parametriInControllo<ParametersMgr.getParametersCFG().getPlimit();i++){
    		Integer idvar= (Integer)rs.get(i).get(2);
    		//...se non sto inserendo una variabile gi� presente...
    		if (!varToExclude.contains(idvar))
    		{
    			String idPerRow = 	  (Integer)rs.get(i).get(0)+"-"+(Integer)rs.get(i).get(2);
    			parametriInControllo++;
	    		stringBuilder.append("<tr class= "+(i%2==0?"Row1":"'Row2'")+" id ='" );
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("'>");
	    		stringBuilder.append("<td class='standardTxt' style='display: none;'>");
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(3));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(4));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append("<img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"setModUser();deleteRow('table_global_vars','"+idPerRow+"');\"/>");
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("</tr>");
	    	}
    	}
    	
		return stringBuilder.toString();
    }
    //Ing. Gilioli Manuel Fine 4 report - patacca!

    
    public static List<Record> getMappingMdlInstances(String language, String devices, String varmodels, String exclude, int max) throws Exception {
    	ArrayList<Integer> varToExclude = new ArrayList<Integer>();
    	String [] excludeList = exclude.split("-");
    	for (int i = 0; i < excludeList.length; i++) {
    		try{
    			Integer varEx = new Integer(excludeList[i]);
    			varToExclude.add(varEx);
    		} catch (NumberFormatException e){
    			//non era un numero.. vado avanti!!!
    		}
    	}
    	int parametriInControllo = varToExclude.size();
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	String dd = devices.replace('-', ',');
    	String vv = varmodels.replace('-', ',');
    	String sql=" select "+ 
    	" iddevice, idvarmdl, idvariable, "+ 
    	" d.description as devdesc, v.description as vardesc "+  
    	" from cfvariable, cftableext as d, cftableext as v "+
    	" where iddevice in ( " + dd.substring(0, dd.length()-1) + " ) " +
    	" and idvarmdl in (" + vv.substring(0, vv.length()-1) + " ) "+
    	" and "+ 
    	" d.idsite=1 and d.tablename='cfdevice' and d.tableid=cfvariable.iddevice and d.languagecode='"+language+"' "+
    	" and "+ 
    	" v.idsite=1 and v.tablename='cfvariable' and v.tableid=cfvariable.idvariable and v.languagecode='"+language+"' "+
    	" order by devdesc, vardesc limit "+(max+1);
    	RecordSet rs=null;
    	rs=DatabaseMgr.getInstance().executeQuery(null,sql,null);

    	LinkedList<Record> retlist = new LinkedList<Record>();
    	//fich� ci sono record dalla query e finch� non supero il limite...
    	for(int i=0;i<rs.size() && parametriInControllo<max;i++){
    		Integer idvar= (Integer)rs.get(i).get(2);
    		//...se non sto inserendo una variabile gi� presente...
    		if (!varToExclude.contains(idvar)) {
    			retlist.add(rs.get(i));
    			String idPerRow = 	  (Integer)rs.get(i).get(0)+"-"+(Integer)rs.get(i).get(2);
    			parametriInControllo++;
	    		stringBuilder.append("<tr class='Row1' id ='" );
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("'>");
	    		stringBuilder.append("<td class='standardTxt' style='display: none;'>");
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(3));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(4));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append("<img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"setModUser();deleteRow('table_global_vars','"+idPerRow+"');\"/>");
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("</tr>");
	    	}
    	}
    	
		return retlist;
    }
    
    public static List<Record> getMappingMdlInstancesForDataTransfer(String language, String devices, String varmodels, String exclude) throws Exception {
    	ArrayList<Integer> varToExclude = new ArrayList<Integer>();
    	String [] excludeList = exclude.split("-");
    	for (int i = 0; i < excludeList.length; i++) {
    		try{
    			Integer varEx = new Integer(excludeList[i]);
    			varToExclude.add(varEx);
    		} catch (NumberFormatException e){
    			//non era un numero.. vado avanti!!!
    		}
    	}
    	int parametriInControllo = varToExclude.size();
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	String dd = devices.replace('-', ',');
    	String vv = varmodels.replace('-', ',');
    	String sql=" select "+ 
    	" iddevice, idvarmdl,idvariable, "+ 
    	" d.description as devdesc, v.description as vardesc,code "+  
    	" from cfvariable, cftableext as d, cftableext as v "+
    	" where iddevice in ( " + dd.substring(0, dd.length()-1) + " ) " +
    	" and idvarmdl in ( " + vv.substring(0, vv.length()-1) + " ) "+
    	" and idhsvariable is null and iscancelled = 'FALSE' "+ 
    	" and d.idsite=1 and d.tablename='cfdevice' and d.tableid=cfvariable.iddevice and d.languagecode='"+language+"' "+
    	" and "+ 
    	" v.idsite=1 and v.tablename='cfvariable' and v.tableid=cfvariable.idvariable and v.languagecode='"+language+"' "+
    	" order by devdesc, vardesc ";
    	RecordSet rs=null;
    	rs=DatabaseMgr.getInstance().executeQuery(null,sql,null);

    	LinkedList<Record> retlist = new LinkedList<Record>();
    	//fich� ci sono record dalla query e finch� non supero il limite...
    	for(int i=0;i<rs.size();i++){
    		Integer idvar= (Integer)rs.get(i).get(2);
    		//...se non sto inserendo una variabile gi� presente...
    		if (!varToExclude.contains(idvar)) {
    			retlist.add(rs.get(i));
    			String idPerRow = 	  (Integer)rs.get(i).get(0)+"-"+(Integer)rs.get(i).get(2);
    			parametriInControllo++;
	    		stringBuilder.append("<tr class='Row1' id ='" );
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("'>");
	    		stringBuilder.append("<td class='standardTxt' style='display: none;'>");
	    		stringBuilder.append(idPerRow);
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(3));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append((String)rs.get(i).get(4));
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("<td class='standardTxt'>");
	    		stringBuilder.append("<img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"setModUser();deleteRow('table_global_vars','"+idPerRow+"');\"/>");
	    		stringBuilder.append("</td>");
	    		stringBuilder.append("</tr>");
	    	}
    	}
    	
		return retlist;
    }

//Funzione che mappa da iddevice-idvarmdl a iddevice-idvarmdl-idvariable
    
    public static String getIdsVarsHistOrHaccp(String values, boolean ishaccp,int timelenght) throws Exception {
    	String data[]=values.split("-");
    	StringBuilder stringBuilder= new StringBuilder();
    	String sql = "";
    	
    	if (timelenght==0)
    	{
    		if (ishaccp)
    		{
    			sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? and ishaccp='TRUE' and iscancelled='FALSE'";
    		}
    		else
    		{
    			sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? and ishaccp='FALSE' and iscancelled='FALSE'";
    		}
    	}
    	else
    	{
	    	if (ishaccp)
	    		sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? and ishaccp='TRUE' and iscancelled='FALSE'";
	    	else
	    	{
	       		sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? and idhsvariable is null and iscancelled='FALSE'";
	    	}
    	}
    	RecordSet rs=null;
    	for(int i=0;i<data.length-1;i++){
    		rs=DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(data[i]),new Integer(data[i+1])});
    		if (rs.size()!=0)   //BIOLO: check if there a record to process
    		{
	    		stringBuilder.append(data[i]);
	      		stringBuilder.append("-");
	      		stringBuilder.append(data[i+1]);
	      		stringBuilder.append("-");
	      		stringBuilder.append(rs.get(0).get(0));
	      		stringBuilder.append("-");
    		}
      		i++;
    	}
		return stringBuilder.toString();
    }
    //Ing. Gilioli Manuel Fine 4 report
    
    public VarphyBean[] getParameterOfDevice(String lang, int site, int iddevice)
    throws Exception
	{
	    String sql =
	        "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
	        "inner join cftableext on cfvariable.idvariable=cftableext.tableid and cftableext.tablename='cfvariable' and cftableext.languagecode = ? " +
	        "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.readwrite>'1' and cfvariable.type<>4 and cfvariable.iscancelled=? and cfvariable.idhsvariable is not null and cftableext.idsite = ? order by cftableext.description";
	
	    return commonLinkLanguage(lang, sql, site,
	        new Object[] { lang, new Integer(site), new Integer(iddevice), "FALSE", new Integer(
	                site) });
	}
    
    public VarphyBean[] getParamsOfDevice(String lang, int site, int iddevice)
    throws Exception
	{
	    String sql = " select cfvariable.*, cftableext.description, cftableext.shortdescr " +
	        " from cfvariable, cftableext where " +
	        " cfvariable.iddevice = "+iddevice+" and cfvariable.readwrite>'1' and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null and " +
	        " cftableext.idsite=1 and cftableext.tablename='cfvariable' and cftableext.tableid=cfvariable.idvariable and cftableext.languagecode = '"+lang+"' " +
	        " order by cftableext.description ";
	
	    return commonLinkLanguage(lang, sql, site, null);
	}
    
    public static RecordSet getLogBydevice(int idsite,int iddevice)
    	throws DataBaseException
    {
    	String sql = "select * from cfvariable where idsite=? and iddevice=? and iscancelled='FALSE' and idhsvariable is null";
    	Object[] object = new Object[]{new Integer(idsite),new Integer(iddevice)};
    	return DatabaseMgr.getInstance().executeQuery(null, sql, object);
    }
}
