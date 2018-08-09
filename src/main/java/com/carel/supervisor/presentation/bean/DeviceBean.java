package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class DeviceBean
{
    private static final String ID_DEVICE = "iddevice";
    private static final String PV_CODE = "pvcode";
    private static final String ID_SITE = "idsite";
    private static final String IS_LOGIC = "islogic";
    private static final String ID_DEV_MDL = "iddevmdl";
    private static final String ID_LINE = "idline";
    private static final String ADDRESS = "address";
    private static final String CODE = "code";
    private static final String IMAGEPATH = "imagepath";
    private static final String ID_GROUP = "idgroup";
    private static final String GLOBAL_INDEX = "globalindex";
    private static final String IS_ENABLED = "isenabled";
    private static final String IS_CANCELLED = "iscancelled";
    private static final String INSERT_TIME = "inserttime";
    private static final String LAST_UPDATE = "lastupdate";
    private static final String IMAGE_NOT_AV = "notavailable.jpg";
    private static final String DESCRIPTION = "description";
    private static final String LITTLE_ENDIAN = "littlendian";
    private boolean isLittleEndian = false;
    private int iddevice = -1;
    private String pvcode = null;
    private int idsite = -1;
    private boolean islogic = false;
    private int iddevmdl = -1;
    private int idline = -1;
    private int address = -1;
    private String code = null;
    private String imagepath = null;
    private int idgroup = -1;
    private int globalindex = -1;
    private String isenabled = null;
    private String iscancelled = null;
    private Timestamp inserttime = null;
    private Timestamp lastupdate = null;
    private String description = null;

    public DeviceBean()
    {
    }

    public DeviceBean(Record record, String language) throws DataBaseException
    {
        this.iddevice = ((Integer) record.get(ID_DEVICE)).intValue();
        this.pvcode = record.get(PV_CODE).toString();
        this.idsite = ((Integer) record.get(ID_SITE)).intValue();
        this.iscancelled = record.get(IS_LOGIC).toString();
        this.iddevmdl = ((Integer) record.get(ID_DEV_MDL)).intValue();
        this.idline = ((Integer) record.get(ID_LINE)).intValue();
        this.address = ((Integer) record.get(ADDRESS)).intValue();
        this.code = record.get(CODE).toString();
        this.imagepath = (record.get(IMAGEPATH) != null)
            ? record.get(IMAGEPATH).toString() : "";
        this.idgroup = ((Integer) record.get(ID_GROUP)).intValue();
        this.globalindex = ((Integer) record.get(GLOBAL_INDEX)).intValue();
        this.isenabled = record.get(IS_ENABLED).toString();
        this.iscancelled = record.get(IS_CANCELLED).toString();
        this.inserttime = ((Timestamp) record.get(INSERT_TIME));
        this.lastupdate = ((Timestamp) record.get(LAST_UPDATE));
        this.description = (String) record.get(DESCRIPTION);
        this.isLittleEndian = UtilBean.checkBoolean((String) record.get(
                    LITTLE_ENDIAN), true);
        this.islogic = UtilBean.checkBoolean((String) record.get(IS_LOGIC), false);
    }

    public DeviceBean(int deviceId, Record record, int site, String language)
        throws Exception
    {
        this.iddevice = deviceId;
        this.iddevmdl = ((Integer) record.get(ID_DEV_MDL)).intValue();
        this.idsite = site;
        this.isenabled = UtilBean.trim(record.get(IS_ENABLED));
        this.description = (String) record.get(DESCRIPTION);
        code = UtilBean.trim(record.get(CODE));
        this.isLittleEndian = UtilBean.checkBoolean((String) record.get(
                    LITTLE_ENDIAN), true);
        this.idline = ((Integer) record.get(ID_LINE)).intValue();
        Object gr = record.get(ID_GROUP);

        if (null == gr)
        {
            idgroup = -1;
        }
        else
        {
            idgroup = ((Integer) gr).intValue();
        }

        imagepath = (String) record.get(IMAGEPATH);
        this.islogic = UtilBean.checkBoolean((String) record.get(IS_LOGIC), false);
        
        // Add per SDK
        if(record.hasColumn(ADDRESS))
            this.address = ((Integer) record.get(ADDRESS)).intValue();
        
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getGroupId()
    {
        return idgroup;
    }

    public void setGroupId(int groupId)
    {
        this.idgroup = groupId;
    }

    public String getImageDevice()
    {
        if (this.imagepath == null)
        {
            return IMAGE_NOT_AV;
        }
        else
        {
            return this.imagepath;
        }
    }

    public void setImageDevice(String path)
    {
        this.imagepath = path;
    }

    public int getAddress()
    {
        return address;
    }

    public void setAddress(int address)
    {
        this.address = address;
    }

    public int getGlobalindex()
    {
        return globalindex;
    }

    public void setGlobalindex(int globalindex)
    {
        this.globalindex = globalindex;
    }

    public int getIddevice()
    {
        return iddevice;
    }

    public void setIddevice(int iddevice)
    {
        this.iddevice = iddevice;
    }

    public int getIddevmdl()
    {
        return iddevmdl;
    }

    public void setIddevmdl(int iddevmdl)
    {
        this.iddevmdl = iddevmdl;
    }

    public int getIdgroup()
    {
        return idgroup;
    }

    public void setIdgroup(int idgroup)
    {
        this.idgroup = idgroup;
    }

    public int getIdline()
    {
        return idline;
    }

    public void setIdline(int idline)
    {
        this.idline = idline;
    }

    public int getIdsite()
    {
        return idsite;
    }

    public void setIdsite(int idsite)
    {
        this.idsite = idsite;
    }

    public String getImagepath()
    {
        return imagepath;
    }

    public void setImagepath(String imagepath)
    {
        this.imagepath = imagepath;
    }

    public Timestamp getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Timestamp inserttime)
    {
        this.inserttime = inserttime;
    }

    public String getIscancelled()
    {
        return iscancelled;
    }

    public void setIscancelled(String iscancelled)
    {
        this.iscancelled = iscancelled;
    }

    public String getIsenabled()
    {
        return isenabled;
    }

    public void setIsenabled(String isenabled)
    {
        this.isenabled = isenabled;
    }

    public boolean islogic()
    {
        return islogic;
    }

    public void setIslogic(boolean islogic)
    {
        this.islogic = islogic;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    public boolean isLittleEndian()
    {
        return isLittleEndian;
    }

    public void setLittleEndian(boolean isLittleEndian)
    {
        this.isLittleEndian = isLittleEndian;
    }

    public int save() throws DataBaseException
    {
        Object[] values = new Object[16];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfdevice", "iddevice");
        values[1] = pvcode;
        values[2] = new Integer(idsite);
        values[3] = UtilBean.writeBoolean(islogic);
        values[4] = new Integer(iddevmdl);
        values[5] = new Integer(idline);
        values[6] = new Integer(address);
        values[7] = UtilBean.writeBoolean(isLittleEndian);
        values[8] = code;
        values[9] = imagepath;
        values[10] = new Integer(idgroup);
        values[11] = new Integer(globalindex);
        values[12] = isenabled;
        values[13] = iscancelled;
        values[14] = new Timestamp(System.currentTimeMillis());
        values[15] = new Timestamp(System.currentTimeMillis());

        String insert = "insert into cfdevice values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);

        return ((Integer) values[0]).intValue();
    }

    public static void updateIsEnabled(int idsite, int iddevice,String isenabled) throws DataBaseException
    {
        String sql = "update cfdevice set isenabled = ?,lastupdate = ? where idsite = ? and iddevice = ? ";
        Object[] param = new Object[4];
        param[0] = isenabled;
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(iddevice);
        
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }
}
