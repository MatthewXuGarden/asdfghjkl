package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import java.sql.Timestamp;


public class GroupVarBean
{
    private static final String ID_GROUP_VAR = "idgroupvar";
    private static final String PV_CODE = "pvcode";
    private static final String ID_SITE = "idsite";
    private static final String ID_GROUP = "idgroup";
    private static final String CODE = "code";
    private static final String TYPE = "type";
    private static final String PARAMETERS = "parameters";
    private static final String MEASUREUNIT= "measureunit";
    private static final String LAST_UPDATE = "lastupdate";
    private final static String DESCRIPTION = "description";
    private int idGroupVar = -1;
    private String pvCode = null;
    private int idSite = -1;
    private int idGroup = -1;
    private String code = null;
    private int type = -1;
    private String parameters = null;
    private String measureunit = null;
    private Timestamp lastUpdate = null;
    private String description = null;

    public GroupVarBean()
    {
    }

    public GroupVarBean(Record record, int idsite, String language)
        throws DataBaseException
    {
        this.idGroupVar = ((Integer) record.get(ID_GROUP_VAR)).intValue();
        this.pvCode = UtilBean.trim(record.get(PV_CODE).toString());
        this.idSite = ((Integer) record.get(ID_SITE)).intValue();
        this.idGroup = ((Integer) record.get(ID_GROUP)).intValue();
        this.code = UtilBean.trim(record.get(CODE).toString());
        this.type = ((Integer) record.get(TYPE)).intValue();
        this.parameters = UtilBean.trim(record.get(PARAMETERS).toString());
        this.measureunit = UtilBean.trim(record.get(MEASUREUNIT).toString());
        this.lastUpdate = (Timestamp) record.get(LAST_UPDATE);
        this.description = (String) record.get(DESCRIPTION);
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getIdGroupVar()
    {
        return idGroupVar;
    }

    public void setIdGroupVar(int idGroupVar)
    {
        this.idGroupVar = idGroupVar;
    }

    public int getIdSite()
    {
        return idSite;
    }

    public void setIdSite(int idSite)
    {
        this.idSite = idSite;
    }

    public Timestamp getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getParameters()
    {
        return parameters;
    }

    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    public String getPvCode()
    {
        return pvCode;
    }

    public void setPvCode(String pvCode)
    {
        this.pvCode = pvCode;
    }

    public int getIdGroup()
    {
        return idGroup;
    }

    public void setIdGroup(int idGroup)
    {
        this.idGroup = idGroup;
    }
    
    public String getMeasureunit() {
		return measureunit;
	}

	public void setMeasureunit(String measureunit) {
		this.measureunit = measureunit;
	}

	public int save() throws DataBaseException
    {
        Object[] values = new Object[9];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfgroupvar", "idgroupvar");
        values[1] = BaseConfig.getPlantId();
        values[2] = new Integer(idSite);
        values[3] = new Integer(idGroup);
        values[4] = String.valueOf(values[0]);
        values[5] = new Integer(type);
        values[6] = parameters;
        values[7] = measureunit;
        values[8] = new Timestamp(System.currentTimeMillis());

        String insert = "insert into cfgroupvar values (?,?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);

        return ((Integer) values[0]).intValue();
    }
}
