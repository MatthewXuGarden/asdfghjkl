package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Chiara Moretti <br>
 * Carel S.p.A. <br>
 * <br>
 * 19-dic-2005 17.32.34 <br>
 */
public class LineInfo implements IDumpable
{
    private static final String ID = "idline";
    private static final String SITE = "idsite";
    private static final String CODE = "code"; // seconda colonna del file .cct
    private static final String COMPORT = "comport";
    private static final String BAUDRATE = "baudrate";
    private static final String TYPE_PROTOCOL = "typeprotocol";
    private static final String PROTOCOL = "protocol";
    private static final String LASTUPDATE = "lastupdate";

    //
    private int id = 0;
    private int site = -1;
    private int code = 0;
    private String comport = null;
    private int baudrate = 0;
    private String protocol = null;
    private String typeProtocol = null;
    private Timestamp lastupdate = null;
    private List arrayDevice = new ArrayList();

    public LineInfo(Record record)
    {
        id = ((Integer) record.get(ID)).intValue();
        site = ((Integer) record.get(SITE)).intValue();
        code = ((Integer) record.get(CODE)).intValue();
        comport = UtilBean.trim(record.get(COMPORT));
        baudrate = ((Integer) record.get(BAUDRATE)).intValue();
        protocol = UtilBean.trim(record.get(PROTOCOL));
        typeProtocol = UtilBean.trim(record.get(TYPE_PROTOCOL));
        lastupdate = ((Timestamp) record.get(LASTUPDATE));
    }

    /**
     * @return: int
     */
    public int getBaudrate()
    {
        return baudrate;
    }

    /**
     * @param baudrate
     */
    public void setBaudrate(int baudrate)
    {
        this.baudrate = baudrate;
    }

    /**
     * @return: String
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(int code)
    {
        this.code = code;
    }

    /**
     * @return: String
     */
    public String getComport()
    {
        return comport;
    }

    /**
     * @param comport
     */
    public void setComport(String comport)
    {
        this.comport = comport;
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
    public String getProtocol()
    {
        return protocol;
    }

    /**
     * @param protocol
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    /**
         * @return: String
         */
    public String getTypeProtocol()
    {
        return typeProtocol;
    }

    /**
     * @param typeProtocol
     */
    public void setTypeProtocol(String typeProtocol)
    {
        this.typeProtocol = typeProtocol;
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

    public boolean add(DeviceInfo deviceInfo)
    {
        return arrayDevice.add(deviceInfo);
    }


    public DeviceInfo get(int index)
    {
        return (DeviceInfo)arrayDevice.get(index);
    }

    public void clear()
    {
    	arrayDevice.clear();
    }
    
    public int size()
    {
        return arrayDevice.size();
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(SITE, site);
        dumpWriter.print(CODE, code);
        dumpWriter.print(COMPORT, comport);
        dumpWriter.print(BAUDRATE, baudrate);
        dumpWriter.print(PROTOCOL, protocol);
        dumpWriter.print(TYPE_PROTOCOL, typeProtocol);
        dumpWriter.print(LASTUPDATE, lastupdate);

        return dumpWriter;
    }
}
