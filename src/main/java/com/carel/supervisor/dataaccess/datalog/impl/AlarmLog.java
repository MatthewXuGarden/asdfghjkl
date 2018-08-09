package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import java.sql.Timestamp;


public class AlarmLog implements IDumpable
{
    private static final String ID = "idalarm";
    private static final String PVCODE = "pvcode";
    private static final String SITE = "idsite";
    private static final String IDVARIABLE = "idvariable";
    private static final String IDDEVICE = "iddevice";
    private static final String ISLOGIC = "islogic";
    private static final String STARTTIME = "starttime";
    private static final String ENDTIME = "endtime";
    private static final String ACKUSER = "ackuser";
    private static final String ACKTIME = "acktime";
    private static final String DELACTIONUSER = "delactionuser";
    private static final String DELACTIONTIME = "delactiontime";
    private static final String RESETUSER = "resetuser";
    private static final String RESETTIME = "resettime";
    private static final String LASTUPDATE = "lastupdate";
    private static final String PRIORITY = "priority";
    private static final String DESCRIPTION = "description";

    //
    private int id = 0;
    private String pvcode = null;
    private int site = 0;
    private int idvariable = 0;
    private int iddevice = 0;
    private String islogic = null;
    private Timestamp starttime = null;
    private Timestamp endtime = null;
    private String ackuser = null;
    private Timestamp acktime = null;
    private String delactionuser = null;
    private Timestamp delactiontime = null;
    private String resetuser = null;
    private Timestamp resettime = null;
    private Timestamp lastupdate = null;
    private String priority = null;
    private String description = "";
    private String varcode = "";
    private String devicedesc = "";

    /**
     * @param record
     * @param dbId
     * AlarmLog
     */
    public AlarmLog(Record record, String dbId)
    {
        id = ((Integer) record.get(ID)).intValue();
        pvcode = UtilBean.trim(record.get(PVCODE));
        site = ((Integer) record.get(SITE)).intValue();
        idvariable = ((Integer) record.get(IDVARIABLE)).intValue();
        iddevice = ((Integer) record.get(IDDEVICE)).intValue();
        islogic = UtilBean.trim(record.get(ISLOGIC));
        starttime = (Timestamp) record.get(STARTTIME);
        endtime = (Timestamp) record.get(ENDTIME);
        ackuser = UtilBean.trim(record.get(ACKUSER));
        acktime = (Timestamp) record.get(ACKTIME);
        delactionuser = UtilBean.trim(record.get(DELACTIONUSER));
        delactiontime = (Timestamp) record.get(DELACTIONTIME);
        resetuser = UtilBean.trim(record.get(RESETUSER));
        resettime = (Timestamp) record.get(RESETTIME);
        lastupdate = (Timestamp) record.get(LASTUPDATE);
        priority = UtilBean.trim(record.get(PRIORITY));
        description = UtilBean.trim(record.get(DESCRIPTION));
        devicedesc = "";
    }
    
    public AlarmLog(Record record, String dbId,String varcode)
    {
        id = ((Integer) record.get(ID)).intValue();
        pvcode = UtilBean.trim(record.get(PVCODE));
        site = ((Integer) record.get(SITE)).intValue();
        idvariable = ((Integer) record.get(IDVARIABLE)).intValue();
        iddevice = ((Integer) record.get(IDDEVICE)).intValue();
        islogic = UtilBean.trim(record.get(ISLOGIC));
        starttime = (Timestamp) record.get(STARTTIME);
        endtime = (Timestamp) record.get(ENDTIME);
        ackuser = UtilBean.trim(record.get(ACKUSER));
        acktime = (Timestamp) record.get(ACKTIME);
        delactionuser = UtilBean.trim(record.get(DELACTIONUSER));
        delactiontime = (Timestamp) record.get(DELACTIONTIME);
        resetuser = UtilBean.trim(record.get(RESETUSER));
        resettime = (Timestamp) record.get(RESETTIME);
        lastupdate = (Timestamp) record.get(LASTUPDATE);
        priority = UtilBean.trim(record.get(PRIORITY));
        description = UtilBean.trim(record.get(DESCRIPTION));
        devicedesc = "";
        this.varcode = varcode;
    }

    public AlarmLog(String dbId)
    {
        id = Integer.parseInt(dbId);
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(PVCODE, pvcode);
        dumpWriter.print(SITE, site);
        dumpWriter.print(IDVARIABLE, idvariable);
        dumpWriter.print(IDDEVICE, iddevice);
        dumpWriter.print(ISLOGIC, islogic);
        dumpWriter.print(STARTTIME, starttime);
        dumpWriter.print(ENDTIME, endtime);
        dumpWriter.print(ACKUSER, ackuser);
        dumpWriter.print(ACKTIME, acktime);
        dumpWriter.print(DELACTIONUSER, delactionuser);
        dumpWriter.print(DELACTIONTIME, delactiontime);
        dumpWriter.print(RESETUSER, resetuser);
        dumpWriter.print(RESETTIME, resettime);
        dumpWriter.print(LASTUPDATE, lastupdate);

        return dumpWriter;
    }

    public void setDeviceDesc(String d) {
    	this.devicedesc = d;
    }
    
    public String getDeviceDesc() {
    	return this.devicedesc;
    }
    
    public Timestamp getEndtime()
    {
        return endtime;
    }

    /**
     * @param endtime
     */
    public void setEndtime(Timestamp endtime)
    {
        this.endtime = endtime;
    }

    /**
     * @return: int
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return: int
     */
    public int getIdvariable()
    {
        return idvariable;
    }

    /**
     * @param idvariable
     */
    public void setIdvar(int idvariable)
    {
        this.idvariable = idvariable;
    }

    /**
     * @return: String
     */
    public String getIslogic()
    {
        return islogic;
    }

    /**
     * @param islogic
     */
    public void setIslogic(String islogic)
    {
        this.islogic = islogic;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    /**
     * @param lastupdate
     */
    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    /**
     * @return: String
     */
    public String getPvcode()
    {
        return pvcode;
    }

    /**
     * @param pvcode
     */
    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    /**
     * @return: int
     */
    public int getSite()
    {
        return site;
    }

    /**
     * @param site
     */
    public void setSite(int site)
    {
        this.site = site;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getStarttime()
    {
        return starttime;
    }

    /**
     * @param starttime
     */
    public void setStarttime(Timestamp starttime)
    {
        this.starttime = starttime;
    }

    /**
     * @return: int
     */
    public int getIddevice()
    {
        return iddevice;
    }

    /**
     * @param iddevice
     */
    public void setIddevice(int iddevice)
    {
        this.iddevice = iddevice;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getAcktime()
    {
        return acktime;
    }

    /**
     * @param acktime
     */
    public void setAcktime(Timestamp acktime)
    {
        this.acktime = acktime;
    }

    /**
     * @return: String
     */
    public String getAckuser()
    {
        return ackuser;
    }

    /**
     * @param ackuser
     */
    public void setAckuser(String ackuser)
    {
        this.ackuser = ackuser;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getDelactiontime()
    {
        return delactiontime;
    }

    /**
     * @param delactiontime
     */
    public void setDelactiontime(Timestamp delactiontime)
    {
        this.delactiontime = delactiontime;
    }

    /**
     * @return: String
     */
    public String getDelactionuser()
    {
        return delactionuser;
    }

    /**
     * @param delactionuser
     */
    public void setDelactionuser(String delactionuser)
    {
        this.delactionuser = delactionuser;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getResettime()
    {
        return resettime;
    }

    /**
     * @param resettime
     */
    public void setResettime(Timestamp resettime)
    {
        this.resettime = resettime;
    }

    /**
     * @return: String
     */
    public String getResetuser()
    {
        return resetuser;
    }

    /**
     * @param resetuser
     */
    public void setResetuser(String resetuser)
    {
        this.resetuser = resetuser;
    }

    /**
     * @param idvariable
     */
    public void setIdvariable(int idvariable)
    {
        this.idvariable = idvariable;
    }

    /**
     * @return: String
     */
    public String getPriority()
    {
        if (Integer.parseInt(priority)>4)
        	return "4";
        else
        	return priority;
    }

    /**
     * @param priority
     */
    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    /**
         * @return: String
         */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    public void updateAck(String user, Integer id) throws Exception
    {
        Object[] values = new Object[4];
        values[0] = user;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Timestamp(System.currentTimeMillis());
        values[3] = id;
        String update = "update hsalarm set ackuser = ? , acktime = ?, lastupdate = ? where idsite=1 and idalarm = ? and ackuser is null";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
    }

    public void updateCancel(String user, Integer id) throws Exception
    {
        Object[] values = new Object[4];
        values[0] = user;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Timestamp(System.currentTimeMillis());
        values[3] = id;
        String update = "update hsalarm set delactionuser = ? , delactiontime = ?, lastupdate = ? where idsite=1 and idalarm = ? and delactionuser is null";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
    }

    public void updateReset(String user, Integer id) throws Exception
    {
        Object[] values = new Object[4];
        values[0] = user;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Timestamp(System.currentTimeMillis());
        values[3] = id;
        String update = "update hsalarm set resetuser = ? , resettime = ?, lastupdate = ? where idsite=1 and idalarm = ? and resetuser is null";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
    }

	public void setVarcode(String varcode) {
		this.varcode = varcode;
	}

	public String getVarcode() {
		return varcode;
	}
    
}
