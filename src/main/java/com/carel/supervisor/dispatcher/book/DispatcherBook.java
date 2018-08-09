package com.carel.supervisor.dispatcher.book;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class DispatcherBook
{
    private final static String B_1 = "idaddrbook";
    private final static String B_2 = "pvcode";
    private final static String B_3 = "idsite";
    private final static String B_4 = "type";
    private final static String B_5 = "address";
    private final static String B_6 = "receiver";
    private final static String B_7 = "ioteststatus";
    private int key = 0;
    private String pvcode = "";
    private int idsite = 0;
    private String type = "";
    private String address = "";
    private String receiver = "";
    private String ioteststatus = "";

    // Used only in event of client modify.
    private int state = 0;

    public DispatcherBook(Record r)
    {
        this.key = ((Integer) r.get(B_1)).intValue();
        this.pvcode = UtilBean.trim(r.get(B_2));
        this.idsite = ((Integer) r.get(B_3)).intValue();
        this.type = UtilBean.trim(r.get(B_4));
        this.address = UtilBean.trim(r.get(B_5));
        this.receiver = UtilBean.trim(r.get(B_6));
        this.ioteststatus = UtilBean.trim(r.get(B_7));
    }

    public DispatcherBook(int key, String pvcode, int site, String type, String rec, String addr,
        int state)
    {
        this.key = key;
        this.pvcode = pvcode;
        this.idsite = site;
        this.type = type;
        this.address = addr;
        this.receiver = rec;
        this.state = state;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getIdsite()
    {
        return idsite;
    }

    public void setIdsite(int idsite)
    {
        this.idsite = idsite;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getState()
    {
        return this.state;
    }
    
    public void setIoteststatus(String ioteststatus)
    {
    	this.ioteststatus = ioteststatus;
    }
    public String getIoteststatus()
    {
    	return this.ioteststatus;
    }
}
