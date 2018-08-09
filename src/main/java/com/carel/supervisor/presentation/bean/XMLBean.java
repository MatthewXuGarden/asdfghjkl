package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.db.Record;

public class XMLBean 
{
	private static final String ID_DEVICE = "iddevice";
	private static final String ID_VARIABLE = "idvariable";

	private static final String DEVICE_DESCRIPTION = "devicedescription";
	private static final String VARIABLE_DESCRIPTION = "variabledescription";
	private static final String VARIABLE_TYPE = "variabletype";
	
	private static final String DESCRIPTION = "description";
	private static final String START_TIME = "starttime";
	private static final String END_TIME = "endtime";
	private static final String ACK_USER = "ackuser";
	
	private static final String ISLOGIC = "islogic";
	private static final String TYPE = "type";
	private static final String PRIORITY = "priority";
	private static final String READWRITE = "readwrite";
	private static final String MIN = "minvalue";
	private static final String MAX = "maxvalue";
	private static final String GRPCATEGORY = "grpcategory";
	private static final String SHORTDESCR = "shortvariabledescription";
	private static final String LONGDESCR = "longvariabledescription";

	private static final String IDLINE = "idline";
	private static final String COMPORT = "comport";
	private static final String BAUDRATE = "baudrate";
	private static final String TYPEPROTOCOL = "typeprotocol";
	private static final String ADDRESS = "address";
	private static final String CODE = "code";
	private static final String ISENABLED = "isenabled";

	private static final String ID_ALARM = "idalarm";
	private static final String ALARM_PRIORITY = "alpriority";
	private static final String ACKTIME = "acktime";
	private static final String DELUSER = "deluser";
	private static final String DELTIME = "deltime";
	private static final String RESETUSER = "resetuser";
	private static final String RESETTIME = "resettime";
	private static final String DEVDESC = "devdesc";
	private static final String ALDESC = "aldesc";
	private static final String PRIODESC = "priodesc";
	
	private String devDescription = "";
	private int idDevice = -1;
    private int idVariable = -1;
    private String varDescription = "";
    private int varType = -1;
    
    private String description = "";
    private Timestamp starttime =null;
    private Timestamp endtime =null;
    private String ackuser = null;

    private String islogic;
	private String type;
	private Integer priority;
	private String readwrite;
	private String min;
	private String max;
	private Integer grpcat;
	private String shortd;
	private String longd;
	private Integer idLine;
	private String comport;
	private Integer baudrate;
	private String typeprotocol;
	private Integer address;
	private String code;
	private String isEnabled;
	private Integer idAlarm;
	private String alpriority;
	private Timestamp acktime;
	private String deluser;
	private Timestamp deltime;
	private String resetuser;
	private Timestamp resettime;
	private String aldesc;
	private String devdesc;
	private String priodesc;
    
    
    public XMLBean(Record record) 
    {
    	if(record.hasColumn(ID_DEVICE))
    		this.idDevice = ((Integer) record.get(ID_DEVICE)).intValue();
		if(record.hasColumn(DEVICE_DESCRIPTION))
    		this.devDescription = (String) record.get(DEVICE_DESCRIPTION);
    	if(record.hasColumn(ID_VARIABLE))
    		this.idVariable = ((Integer) record.get(ID_VARIABLE)).intValue();
    	if(record.hasColumn(VARIABLE_DESCRIPTION))
    		this.varDescription = (String) record.get(VARIABLE_DESCRIPTION);
    	if(record.hasColumn(VARIABLE_TYPE))
    		this.varType = ((Integer) record.get(VARIABLE_TYPE)).intValue();
    
    	if(record.hasColumn(DESCRIPTION))
    		this.description = (String) record.get(DESCRIPTION);
    	if(record.hasColumn(START_TIME))
    		this.starttime = (Timestamp) record.get(START_TIME);
    	if(record.hasColumn(END_TIME))
    		this.endtime = (Timestamp) record.get(END_TIME);
    	if(record.hasColumn(ACK_USER))
    		this.ackuser = (String) record.get(ACK_USER);
    	
    	if(record.hasColumn(ISLOGIC))
    		this.islogic = (String) record.get(ISLOGIC);
    	if(record.hasColumn(TYPE))
    		this.type = (String) record.get(TYPE);
    	if(record.hasColumn(PRIORITY))
    		this.priority = (Integer)record.get(PRIORITY);
    	if(record.hasColumn(READWRITE))
    		this.readwrite = (String) record.get(READWRITE);
    	if(record.hasColumn(MIN))
    		this.min = (String) record.get(MIN);
    	if(record.hasColumn(MAX))
    		this.max = (String) record.get(MAX);
    	if(record.hasColumn(GRPCATEGORY))
    		this.grpcat = (Integer) record.get(GRPCATEGORY);
    	if(record.hasColumn(SHORTDESCR))
    		this.shortd = (String) record.get(SHORTDESCR);
    	if(record.hasColumn(LONGDESCR))
    		this.longd = (String) record.get(LONGDESCR);
    	
    	if(record.hasColumn(IDLINE))
    		this.idLine = (Integer) record.get(IDLINE);
    	if(record.hasColumn(COMPORT))
    		this.comport = (String) record.get(COMPORT);
    	if(record.hasColumn(BAUDRATE))
    		this.baudrate = (Integer) record.get(BAUDRATE);
    	if(record.hasColumn(TYPEPROTOCOL))
    		this.typeprotocol = (String) record.get(TYPEPROTOCOL);
    	if(record.hasColumn(ADDRESS))
    		this.address = (Integer) record.get(ADDRESS);
    	if(record.hasColumn(CODE))
    		this.code = (String) record.get(CODE);
    	if(record.hasColumn(ISENABLED))
    		this.isEnabled = (String) record.get(ISENABLED);
    	
    	if(record.hasColumn(ID_ALARM))
    		this.idAlarm = (Integer) record.get(ID_ALARM);
    	if(record.hasColumn(ALARM_PRIORITY))
    		this.alpriority = (String) record.get(ALARM_PRIORITY);
    	if(record.hasColumn(ACKTIME))
    		this.acktime = (Timestamp) record.get(ACKTIME);
    	if(record.hasColumn(DELUSER))
    		this.deluser = (String) record.get(DELUSER);
    	if(record.hasColumn(DELTIME))
    		this.deltime = (Timestamp) record.get(DELTIME);
    	if(record.hasColumn(RESETUSER))
    		this.resetuser = (String) record.get(RESETUSER);
    	if(record.hasColumn(RESETTIME))
    		this.resettime = (Timestamp) record.get(RESETTIME);
    	if(record.hasColumn(ALDESC))
    		this.aldesc = (String) record.get(ALDESC);
    	if(record.hasColumn(DEVDESC))
    		this.devdesc = (String) record.get(DEVDESC);
    	if(record.hasColumn(PRIODESC))
    		this.priodesc = (String) record.get(PRIODESC);
    	
    }
    
    public void setDeviceDescription(String s) {
        this.devDescription = s;
    }
    
    public String getDeviceDescription() {
		return devDescription;
	}

	public String getVariableDescription() {
		return varDescription;
	}

	public int getIdVariable() {
		return idVariable;
	}
	
	public String getAckUser() {
		return ackuser;
	}

    public String getDescription() {
		return description;
	}
	
	public Timestamp getEndtime() {
		return endtime;
	}
	
	public Timestamp getStarttime() {
		return starttime;
	}
	
	public int getTypeVariable() {
		return this.varType;
	}
	
	public int getIdDevice() {
		return this.idDevice;
	}

	public Integer getGrpcat()
	{
		return grpcat;
	}

	public void setGrpcat(Integer grpcat)
	{
		this.grpcat = grpcat;
	}

	public String getIslogic()
	{
		return islogic;
	}

	public void setIslogic(String islogic)
	{
		this.islogic = islogic;
	}

	public String getLongd()
	{
		return longd;
	}

	public void setLongd(String longd)
	{
		this.longd = longd;
	}

	public String getMax()
	{
		return max;
	}

	public void setMax(String max)
	{
		this.max = max;
	}

	public String getMin()
	{
		return min;
	}

	public void setMin(String min)
	{
		this.min = min;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public String getReadwrite()
	{
		return readwrite;
	}

	public void setReadwrite(String readwrite)
	{
		this.readwrite = readwrite;
	}

	public String getShortd()
	{
		return shortd;
	}

	public void setShortd(String shortd)
	{
		this.shortd = shortd;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Integer getAddress()
	{
		return address;
	}

	public void setAddress(Integer address)
	{
		this.address = address;
	}

	public Integer getBaudrate()
	{
		return baudrate;
	}

	public void setBaudrate(Integer baudrate)
	{
		this.baudrate = baudrate;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
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

	public Integer getIdLine()
	{
		return idLine;
	}

	public void setIdLine(Integer idline)
	{
		this.idLine = idLine;
	}

	public String getIsEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public String getTypeProtocol()
	{
		return typeprotocol;
	}

	public void setTypeProtocol(String typeprotocol)
	{
		this.typeprotocol = typeprotocol;
	}

	public Timestamp getAckTime()
	{
		return acktime;
	}

	public void setAckTime(Timestamp acktime)
	{
		this.acktime = acktime;
	}

	public String getAlDesc()
	{
		return aldesc;
	}

	public void setAlDesc(String aldesc)
	{
		this.aldesc = aldesc;
	}

	public String getAlPriority()
	{
		return alpriority;
	}

	public void setAlPriority(String alpriority)
	{
		this.alpriority = alpriority;
	}

	public Timestamp getDelTime()
	{
		return deltime;
	}

	public void setDelTime(Timestamp deltime)
	{
		this.deltime = deltime;
	}

	public String getDelUser()
	{
		return deluser;
	}

	public void setDelUser(String deluser)
	{
		this.deluser = deluser;
	}

	public String getDevDesc()
	{
		return devdesc;
	}

	public void setDevDesc(String devdesc)
	{
		this.devdesc = devdesc;
	}

	public Integer getIdAlarm()
	{
		return idAlarm;
	}

	public void setIdAlarm(Integer idAlarm)
	{
		this.idAlarm = idAlarm;
	}

	public String getPrioDesc()
	{
		return priodesc;
	}

	public void setPrioDesc(String priodesc)
	{
		this.priodesc = priodesc;
	}

	public Timestamp getResetTime()
	{
		return resettime;
	}

	public void setResetTime(Timestamp resettime)
	{
		this.resettime = resettime;
	}

	public String getResetUser()
	{
		return resetuser;
	}

	public void setResetUser(String resetuser)
	{
		this.resetuser = resetuser;
	}
}