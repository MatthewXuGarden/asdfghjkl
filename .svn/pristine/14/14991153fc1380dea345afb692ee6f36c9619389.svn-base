package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import java.sql.Timestamp;


public class LineBean
{
    private static final String ID_LINE = "idline";
    private static final String ID_SITE = "idsite";
    private static final String CODE = "code";
    private static final String COM_PORT = "comport";
    private static final String BAUD_RATE = "baudrate";
    private static final String TYPE_PROTOCOL = "typeprotocol";
    private static final String PROTOCOL = "protocol";
    private static final String LAST_UPDATE = "lastupdate";
    private int idline = -1;
    private int idsite = -1;
    private int code = -1;
    private String comport = null;
    private int baudrate = -1;
    private String typeprotocol = null;
    private String protocol = null;
    private Timestamp lastupdate = null;
    private int numberofdevice = -1;

    public static final int FIRST_CAREL = 1;
    public static final int LAST_CAREL = 8;
    public static final int FIRST_MODBUS = 9;
    public static final int LAST_MODBUS = 99;
    public static final int FIRST_SNMP = 100;
    public static final int LAST_SNMP = Integer.MAX_VALUE;
    
    public LineBean()
    {
    }

    public LineBean(Record record) throws DataBaseException
    {
        this.idline = ((Integer) record.get(ID_LINE)).intValue();
        this.idsite = ((Integer) record.get(ID_SITE)).intValue();
        this.code = ((Integer) record.get(CODE)).intValue();
        this.comport = record.get(COM_PORT).toString().trim();
        this.baudrate = ((Integer) record.get(BAUD_RATE)).intValue();
        this.typeprotocol = record.get(TYPE_PROTOCOL).toString().trim();
        this.protocol = record.get(PROTOCOL).toString().trim();
        this.lastupdate = ((Timestamp) record.get(LAST_UPDATE));
        
        String sql = "select * from cfdevice,cfline where cfline.idline = cfdevice.idline and cfline.idsite = ? and cfline.idline= ? and cfdevice.iscancelled=?";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idline), "FALSE" });
        this.numberofdevice = rs.size();
    }

    public int getBaudrate()
    {
        return baudrate;
    }

    public void setBaudrate(int baudrate)
    {
        this.baudrate = baudrate;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getComport()
    {
        return comport;
    }

    public void setComport(String comport)
    {
        this.comport = comport;
    }

    public String getIpAddress()
    {
        return comport;
    }

    public void setIpAddress(String ip)
    {
        this.comport = ip;
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

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getTypeprotocol()
    {
        return typeprotocol;
    }

    public void setTypeprotocol(String typeprotocol)
    {
        this.typeprotocol = typeprotocol;
    }

    public int getNumberofdevice()
    {
        return numberofdevice;
    }

    public void setNumberofdevice(int numberofdevice)
    {
        this.numberofdevice = numberofdevice;
    }

   
    
    public int save() throws DataBaseException
    {
        Object[] values = new Object[8];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfline", "idline");
        values[1] = new Integer(idsite);
        values[2] = new Integer(code);
        values[3] = comport;
        values[4] = new Integer(baudrate);
        values[5] = typeprotocol;
        values[6] = protocol;
        values[7] = new Timestamp(System.currentTimeMillis());
        String insert = "insert into cfline values (?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);

        return ((Integer) values[0]).intValue();
    }
    
    public boolean isSerial()
    {
    	return code < 100 ? true : false;
    }
    
    public boolean isLan()
    {
    	return code >= 100 ? true : false;
    }
}
