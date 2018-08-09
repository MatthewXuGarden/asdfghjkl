package com.carel.supervisor.dataaccess.dataconfig;

import java.sql.Timestamp;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class SiteInfo implements IDumpable
{
    private static final String ID = "idsite";
    private static final String PVCODE = "pvcode";
    private static final String CODE = "code";
    private static final String ADDRESS = "address";
    private static final String ZIP = "zip";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String FAX = "fax";
    private static final String MANAGER = "manager";
    private static final String MNGEMAIL = "mngemail";
    private static final String MNGPHONE = "mngphone";
    private static final String MNGFAX = "mngfax";
    private static final String MNGMOBILE = "mngmobile";
    private static final String LASTUPDATE = "lastupdate";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String TYPE = "type";
    private static final String LASTCONNECTION = "lastconnection";
    private static final String CONNECTIONTYPE = "typeconnection";
    private static final String SITESTATUS = "sitestatus";
    private static final String LASTDIALUP = "lastdialup";
    
    private int id = 0;
    private String pvcode = null;
    private String code = null;
    private String address = null;
    private String zip = null;
    private String city = null;
    private String state = null;
    private String country = null;
    private String email = null;
    private String phone = null;
    private String fax = null;
    private String manager = null;
    private String mngemail = null;
    private String mngphone = null;
    private String mngfax = null;
    private String mngmobile = null;
    private Timestamp lastupdate = null;
    private String name = null;
    private String password = null;
    private String type = null;
    private Timestamp lastconnection = null;
    private String typeconnection = null;
    private int siteStatus = 0;
    private Timestamp lastdialup = null;

    public SiteInfo()
    {
    }

    public SiteInfo(Record record)
    {
        id = ((Integer) record.get(ID)).intValue();
        pvcode = UtilBean.trim(record.get(PVCODE));
        code = UtilBean.trim(record.get(CODE));
        address = UtilBean.trim(record.get(ADDRESS));
        zip = UtilBean.trim(record.get(ZIP));
        city = UtilBean.trim(record.get(CITY));
        state = UtilBean.trim(record.get(STATE));
        country = UtilBean.trim(record.get(COUNTRY));
        email = UtilBean.trim(record.get(EMAIL));
        phone = UtilBean.trim(record.get(PHONE));
        fax = UtilBean.trim(record.get(FAX));
        manager = UtilBean.trim(record.get(MANAGER));
        mngemail = UtilBean.trim(record.get(MNGEMAIL));
        mngphone = UtilBean.trim(record.get(MNGPHONE));
        mngfax = UtilBean.trim(record.get(MNGFAX));
        mngmobile = UtilBean.trim(record.get(MNGMOBILE));
        lastupdate = ((Timestamp) record.get(LASTUPDATE));
        name = UtilBean.trim(record.get(NAME));
        password = UtilBean.trim(record.get(PASSWORD));
        type = UtilBean.trim(record.get(TYPE));
        lastconnection = ((Timestamp) record.get(LASTCONNECTION));
        typeconnection = UtilBean.trim(record.get(CONNECTIONTYPE));
        siteStatus = ((Integer)record.get(SITESTATUS)).intValue();
        lastdialup = ((Timestamp) record.get(LASTDIALUP));
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(PVCODE, pvcode);
        dumpWriter.print(CODE, code);
        dumpWriter.print(ADDRESS, address);
        dumpWriter.print(ZIP, zip);
        dumpWriter.print(CITY, city);
        dumpWriter.print(STATE, state);
        dumpWriter.print(COUNTRY, country);
        dumpWriter.print(EMAIL, email);
        dumpWriter.print(PHONE, phone);
        dumpWriter.print(FAX, fax);
        dumpWriter.print(MANAGER, manager);
        dumpWriter.print(MNGEMAIL, mngemail);
        dumpWriter.print(MNGPHONE, mngphone);
        dumpWriter.print(MNGFAX, mngfax);
        dumpWriter.print(MNGMOBILE, mngmobile);
        dumpWriter.print(LASTUPDATE, lastupdate);
        dumpWriter.print(NAME, name);
        dumpWriter.print(PASSWORD, password);
        dumpWriter.print(TYPE, type);

        return dumpWriter;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public String getManager()
    {
        return manager;
    }

    public void setManager(String manager)
    {
        this.manager = manager;
    }

    public String getMngemail()
    {
        return mngemail;
    }

    public void setMngemail(String mngemail)
    {
        this.mngemail = mngemail;
    }

    public String getMngfax()
    {
        return mngfax;
    }

    public void setMngfax(String mngfax)
    {
        this.mngfax = mngfax;
    }

    public String getMngmobile()
    {
        return mngmobile;
    }

    public void setMngmobile(String mngmobile)
    {
        this.mngmobile = mngmobile;
    }

    public String getMngphone()
    {
        return mngphone;
    }

    public void setMngphone(String mngphone)
    {
        this.mngphone = mngphone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Timestamp getLastconnection()
    {
        return lastconnection;
    }

    public void setLastconnection(Timestamp lastconnection)
    {
        this.lastconnection = lastconnection;
    }

    public String getConnectiontype()
    {
        return typeconnection;
    }

    public void setConnectiontype(String connectiontype)
    {
        this.typeconnection = connectiontype;
    }
    
    public int getSiteStatus() {
    	return this.siteStatus;
    }
    
    public Timestamp getLastDialup()
    {
        return this.lastdialup;
    }
    
    public int save(String default_language) throws DataBaseException
    {
        Object[] values = new Object[23];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfsite", "idsite");
        values[1] = BaseConfig.getPlantId();
        values[2] = code;
        values[3] = name;
        values[4] = password;
        values[5] = type;
        values[6] = typeconnection;
        values[7] = address;
        values[8] = zip;
        values[9] = city;
        values[10] = state;
        values[11] = country;
        values[12] = email;
        values[13] = phone;
        values[14] = fax;
        values[15] = manager;
        values[16] = mngemail;
        values[17] = mngphone;
        values[18] = mngfax;
        values[19] = mngmobile;
        values[20] = null;
        values[21] = new Timestamp(System.currentTimeMillis());
        values[22] = new Integer(0);

        String insert = "insert into cfsite values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
        
        Integer id_new_site = (Integer) values[0];
        
        // insert area globale globale
        insert = "insert into cfarea values (?,?,?,?,?)";
        values = new Object[5];
        values[0]= new Integer(1);
        values[1]= id_new_site;
        values[2]= "Globale";
        values[3]= "TRUE";
        values[4]= new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
        
        //insert gruppo globale globale
        insert = "insert into cfgroup values (?,?,?,?,?,?)";
        values = new Object[6];
        values[0]= new Integer(1);
        values[1]= id_new_site;
        values[2]= new Integer(1);
        values[3]= "Globale";
        values[4]= "TRUE";
        values[5]= new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
        
        insert = "insert into cfsiteext values (?,?,?,?)";
        values = new Object[4];
        values[0] = id_new_site;
        values[1] = default_language;
        values[2] = "TRUE";
        values[3] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null,insert,values);
        
        return id_new_site.intValue();
    }
    
    public void updateSite(String name, String id, String phone, String password)
	    throws DataBaseException
	{
	    String sql = "update cfsite set name = ?,code = ?, phone = ?, password = ? where idsite = ?";
	    Object[] param = new Object[5];
	    param[0] = name;
	    param[1] = id;
	    param[2] = phone;
	    param[3] = password;
	    param[4] = new Integer(this.id);
	
	    DatabaseMgr.getInstance().executeStatement(null, sql, param);
	}
}