package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class ProfileBean
{
    private static final String ID_PROFILE = "idprofile";
    private static final String SITE_CODE = "code";
    private static final String STATUS = "status";
    private static final String NOMENU = "nomenu";
    
    /*public static final int FUNCT_SITE_CONF = 0;
    public static final int FUNCT_CONSTR_PARAM = 1;
    public static final int FUNCT_SERV_PARAM = 2;
    public static final int FUNCT_ALARM_ACK = 3;
    public static final int FUNCT_ALARM_CANC = 4;
    public static final int FUNCT_ALARM_RESET = 5;
    public static final int FUNCT_REPORT_CONF = 6;
    public static final int FUNCT_REPORT_PRINT = 7;
    public static final int FUNCT_SYSTEM_PAGE = 8;*/
    public static final int FUNCT_HACCP = 9;
    public static final int FUNCT_HISTORICAL = 10;/*
    public static final int FUNCT_USER_MNG = 11;
    public static final int FUNCT_NOTE = 12;
    public static final int FUNCT_IO_MGR = 13;
   //lasciare sempre per ultimo - Intervento tecnico per skip
    public static final int FUNCT_CONF_GRAPH = 14;
    public static final int FUNCT_CONF_KPI = 15;
    public static final int FUNCT_CONF_AC = 16;
    public static final int FUNCT_CONF_LIGHTNIGHT = 17;
    public static final int FUNCT_CONF_REBOOT = 18;
    public static final int FUNCT_BOOKLET = 19;*/
    
    
    /*
    public static final int PERMISSION_NONE = 0;
    public static final int PERMISSION_READ_ONLY = 1;
    public static final int PERMISSION_READ_WRITE = 2;*/
    
    public static final int FILTER_NONE = 0;
    public static final int FILTER_SERVICES = 1;
    public static final int FILTER_MANUFACTURER = 2;
    
    private static final String INSERT_TIME = "inserttime";
    private Integer idprofile = null;
    private String code = null;
    private String status = null;
    private Timestamp inserttime = null;
    
    //2010-5-21, By Kevin
    private Boolean nomenu = false;

    public ProfileBean(Record r)
    {
        this.idprofile = (Integer) r.get(ID_PROFILE);
        this.code = UtilBean.trim(r.get(SITE_CODE));
        this.status = UtilBean.trim(r.get(STATUS));
        this.inserttime = (Timestamp) r.get(INSERT_TIME);
        this.nomenu = (Boolean)r.get(NOMENU);
    }


	/**
     * @return: String
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return: Integer
     */
    public Integer getIdprofile()
    {
        return idprofile;
    }

    /**
     * @param idprofile
     */
    public void setIdprofile(Integer idprofile)
    {
        this.idprofile = idprofile;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getInserttime()
    {
        return inserttime;
    }

    /**
     * @param inserttime
     */
    public void setInserttime(Timestamp inserttime)
    {
        this.inserttime = inserttime;
    }

    /**
     * @return: String
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return: Timestamp
     */
    /*
    public int getFunction(int idfunction)
    {
    	IProductInfo productInfo = ProductInfoMgr.getInstance().getProductInfo();
    	String ver = productInfo.get("cp");
    	
    	if(idfunction>14)
    	{	
    		idfunction--;
    		
    		// Software version check. In case of BE and LE versions, kpi module doesn't exist
    		if(!ver.equalsIgnoreCase("LE"))
    		{
    			idfunction--;
    		}
    	}
    	
    	
        String param = status.split(";")[idfunction];
        String value = param.split("=")[1];
        if (value.contains("-"))
        {
        	value = value.split("-")[0];
        }
        if (!value.equals("null"))
        {
        	return Integer.parseInt(value);
        }
        else
        	return PERMISSION_NONE;
    }*/
    
    public List<Integer> getForbiddenGroups()
    {
    	String tmp;
    	String[] s_groups = null;
    	List<Integer> groups = null;
    	if (this.status!=null && status.contains("-"))
    	{
    		if (this.status.split("-").length >1)
    		{
    			
    			tmp = this.status.split("-")[2];
	    		if (!tmp.equalsIgnoreCase(""))
	    		{
	    			s_groups = tmp.split(";");
	    			groups = new ArrayList<Integer>();
	    			for (int i=0;i<s_groups.length;i++)
	    			{
	    				groups.add(Integer.parseInt(s_groups[i]));
	    			}
	    		}
    		}
    	}
    	
    	return groups;
    }
    
    
    
    public int getVariableFilter()
    {
    	int permission = FILTER_NONE;
    	String tmp = "";
    	
    	if (this.status!=null && status.contains("-"))
    	{
    		tmp = this.status.split("-")[0];
    		if (!tmp.equalsIgnoreCase(""))
    		{
    			if (tmp.contains("="))
    			{
    				return permission;
    			}
    			permission = Integer.parseInt(tmp);
    		}
    	}
    	return permission;
    }
    
    public Boolean getNomenu() {
		return nomenu;
	}

	public void setNomenu(Boolean nomenu) {
		this.nomenu = nomenu;
	}
}
