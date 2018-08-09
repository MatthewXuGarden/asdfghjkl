package com.carel.supervisor.dataaccess.dataconfig;

import java.sql.ResultSet;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.Record;


public class VariableInfo implements IDumpable
{
    //Costanti
    public static final String ID = "idvariable";
    public static final String DEVICE = "iddevice";
    public static final String MODEL = "idvarmdl";
    public static final String SITE = "idsite";
    public static final String PRIORITY = "priority";
    public static final String ADDRESS_IN = "addressin";
    public static final String ADDRESS_OUT = "addressout";
    public static final String FREQUENCY = "frequency";
    public static final String TYPE = "type";
    public static final String VARIATION = "delta";
    public static final String PVCODE = "pvcode";
    public static final String IS_LOGIC = "islogic";
    public static final String FUNCTIONCODE = "functioncode";
    public static final String VAR_DIMENSION = "vardimension";
    public static final String VAR_LENGTH = "varlength";
    public static final String BIT_POSITION = "bitposition";
    public static final String SIGNED = "signed";
    public static final String DECIMAL = "decimal";
    private final static String MIVAL = "minvalue";
    private final static String MAXVAL = "maxvalue";
    public static final int TYPE_DIGITAL = 1;
    public static final int TYPE_ANALOGIC = 2;
    public static final int TYPE_INTEGER = 3;
    public static final int TYPE_ALARM = 4;

    //Manuel Gilioli
    public static final String KEY_MAX = "keymax";
    public static final String KEY_ACTUAL = "keyactual";
    public static final String IS_TURN = "isturn";
    public static final String IS_ACTIVE = "isactive";
    public static final String IS_HACCP = "ishaccp";
    private DeviceInfo devInfo = null;

    //Manuel Gilioli
    private Integer id = null;
    private Integer device = null;
    private Integer model = null;
    private Integer site = null;
    private Integer priority = null;
    private Integer addressIn = null;
    private Integer addressOut = null;
    private Integer frequency = null;
    private int type = 0;
    private int varDimension = 0;
    private int varLength = 0;
    private int bitPosition = 0;
    private int decimal = 0;
    private boolean isSigned = false;
    private double variation = 0;
    private String pv = null;
    private boolean isLogic = false;
    private Integer functionCode = null;
    private short keyactual = -1; //dovrebbe essere settata anche sul db =-1 la prima volta
    private short keymax = 100;
    private boolean isturn = false;
    private boolean isActive = true;
    private boolean isHaccp = false;
    private String minValue = null;
    private String maxValue = null;

    //Matteo M
    public static final String MEASUREUNIT = "measureunit";
	public static final String CODE = "code";
	public static final String TODISPLAY = "todisplay";
    private String measureunit=null;
	private String code = null;
    
    
    public VariableInfo() {
    }
    
    public VariableInfo(ResultSet rs)
    	throws Exception
    {
    	id = (Integer)rs.getInt(ID);
        site = (Integer) rs.getInt(SITE);
        device = (Integer) rs.getInt(DEVICE);
        type = ((Integer) rs.getInt(TYPE)).intValue();
        priority = (Integer) rs.getInt(PRIORITY);
        addressIn = (Integer) rs.getInt(ADDRESS_IN);
        addressOut = (Integer) rs.getInt(ADDRESS_OUT);
        frequency = (Integer) rs.getInt(FREQUENCY);
        functionCode = (Integer) rs.getInt(FUNCTIONCODE);
        isLogic = UtilBean.checkBoolean(rs.getString(IS_LOGIC), false);
        isSigned = UtilBean.checkBoolean(rs.getString(SIGNED), false);
        varDimension = ((Integer) rs.getInt(VAR_DIMENSION)).intValue();
        varLength = ((Integer) rs.getInt(VAR_LENGTH)).intValue();
        bitPosition = ((Integer) rs.getInt(BIT_POSITION)).intValue();
        decimal = ((Integer) rs.getInt(DECIMAL)).intValue();
        minValue = (String) rs.getString(MIVAL);
        maxValue = (String) rs.getString(MAXVAL);
        variation = ((Double) rs.getDouble(VARIATION)).doubleValue();
        
        try {
        	measureunit=(String) rs.getString(MEASUREUNIT);
        } catch(Exception e){
        	measureunit="";
        }
        
        try {
        	isHaccp = UtilBean.checkBoolean(rs.getString(IS_HACCP), false);
        } catch(Exception e){}
        
        try {
        	isActive = UtilBean.checkBoolean(rs.getString(IS_ACTIVE), true);
        } catch(Exception e){}

        try {
        	keyactual = ((Short)rs.getShort(KEY_ACTUAL)).shortValue();
        } catch(Exception e){}

        try {
        	keymax = ((Short) rs.getShort(KEY_MAX)).shortValue();
        } catch(Exception e){}

        try {
        	isturn = ((Boolean) rs.getBoolean(IS_TURN)).booleanValue();
        } catch(Exception e){}
        
        try {
        	model = ((Integer) rs.getInt(MODEL));
        } catch(Exception e){}

        try {
        	setCode(UtilBean.trim(rs.getString(CODE)));
        } catch(Exception e){}
    }
    
    
    public VariableInfo(Record record)
    {
        id = (Integer) record.get(ID);
        site = (Integer) record.get(SITE);
        device = (Integer) record.get(DEVICE);
        type = ((Integer) record.get(TYPE)).intValue();
        priority = (Integer) record.get(PRIORITY);
        addressIn = (Integer) record.get(ADDRESS_IN);
        addressOut = (Integer) record.get(ADDRESS_OUT);
        frequency = (Integer) record.get(FREQUENCY);
        functionCode = (Integer) record.get(FUNCTIONCODE);
        isLogic = UtilBean.checkBoolean(record.get(IS_LOGIC), false);
        isSigned = UtilBean.checkBoolean(record.get(SIGNED), false);
        varDimension = ((Integer) record.get(VAR_DIMENSION)).intValue();
        varLength = ((Integer) record.get(VAR_LENGTH)).intValue();
        bitPosition = ((Integer) record.get(BIT_POSITION)).intValue();
        decimal = ((Integer) record.get(DECIMAL)).intValue();
        minValue = (String) record.get(MIVAL);
        maxValue = (String) record.get(MAXVAL);

        if (record.hasColumn(MEASUREUNIT)){
        	measureunit=(String) record.get(MEASUREUNIT);
        }else{
        	measureunit="";
        }
        
        if (record.hasColumn(IS_HACCP))
        {
            isHaccp = UtilBean.checkBoolean(record.get(IS_HACCP), false);
        }

        if (record.hasColumn(IS_ACTIVE))
        {
            isActive = UtilBean.checkBoolean(record.get(IS_ACTIVE), true);
        }

        variation = ((Double) record.get(VARIATION)).doubleValue();

        if (record.hasColumn(KEY_ACTUAL))
        {
            keyactual = ((Short) record.get(KEY_ACTUAL)).shortValue();
        }

        if (record.hasColumn(KEY_MAX))
        {
            keymax = ((Short) record.get(KEY_MAX)).shortValue();
        }

        if (record.hasColumn(IS_TURN))
        {
            isturn = ((Boolean) record.get(IS_TURN)).booleanValue();
        }
        if (record.hasColumn(MODEL))
        {
            model = ((Integer) record.get(MODEL));
        }
        try {
        	if (record.hasColumn(CODE))
        		code = UtilBean.trim(record.get(CODE));
        } catch(Exception e){}
    }

    public VariableInfo(VariableInfo variableInfo)
    {
        id = variableInfo.getId();
        site = variableInfo.getSite();
        device = variableInfo.getDevice();
        type = variableInfo.getType();
        priority = variableInfo.getPriority();
        addressIn = variableInfo.getAddressIn();
        addressOut = variableInfo.getAddressOut();
        frequency = variableInfo.getFrequency();
        functionCode = variableInfo.getFunctionCode();
        isLogic = variableInfo.isLogic();
        variation = variableInfo.getVariation();
        isSigned = variableInfo.isSigned();
        varDimension = variableInfo.getVarDimension();
        varLength = variableInfo.getVarLength();
        bitPosition = variableInfo.getBitPosition();
        decimal = variableInfo.getDecimal();
        model = variableInfo.getModel();
        measureunit=variableInfo.getMeasureunit();
    }

    public Integer getDevice()
    {
        return device;
    } //getDevice

    public Integer getId()
    {
        return id;
    } //getId

    public Integer getModel()
    {
        return model;
    } //getModel

    public DeviceInfo getDeviceInfo()
    {
        return devInfo;
    } //getDevPhyInfo

    public void setDeviceInfo(DeviceInfo devInfo)
    {
        this.devInfo = devInfo;
    } //setDevPhyInfo

    //LDAC : must be implemented
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);

        return dumpWriter;
    } //getDumpWriter

    public Integer getSite()
    {
        return site;
    } //getSite

    public Integer getAddressIn()
    {
        return addressIn;
    } //getAddress

    public Integer getPriority()
    {
        return priority;
    } //getPriority

    public Integer getFrequency()
    {
        return frequency;
    } //getFrequency

    public int getType()
    {
        return type;
    } //getType

    public int getVarTypeForAcquisition()
    {
    	// 20100531 - modding for reading multiplexed digital vars / alarms.
    	// It should be OK for any digital value (alarm or not). There are some devmdls (110,111,112,113,114,115) 
    	// which have a standard (not multiplexed) digital var of length == 1. Probably a bad design of the model?
    	if (varLength==1 && type != TYPE_ANALOGIC) {
    		return TYPE_INTEGER;
    	}
        switch (type)
        {
        case TYPE_ALARM:
        case TYPE_DIGITAL:
            return TYPE_DIGITAL;

        case TYPE_ANALOGIC:
            return TYPE_ANALOGIC;

        case TYPE_INTEGER:
            return TYPE_INTEGER;

        default:
            return type;
        }
    } //getVarTypeForAcquisition

    /**
         * @return: int
         */
    public Integer getFunctionCode()
    {
        return functionCode;
    }

    public double getVariation()
    {
        return variation;
    } //getVariation

    public String getPv()
    {
        return pv;
    } //getPv

    public short getPeriphIndex()
    {
        return (short) devInfo.getAddress().intValue();
    }

    public short getGlobalIndex()
    {
        return (short) devInfo.getGlobalindex().intValue();
    }

    public String getProtocolType()
    {
        return devInfo.getLineInfo().getTypeProtocol();
    }

    public boolean isLogic()
    {
        return this.isLogic;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ID: ");
        buffer.append(id);

        return buffer.toString();
    }

    //Manuel Gilioli
    public short getKeyactual()
    {
        return keyactual;
    }

    public short getKeymax()
    {
        return keymax;
    }

    public boolean getIsTurn()
    {
        return isturn;
    }

    /**
     * @return: boolean
     */
    public boolean isActive()
    {
        return isActive;
    }

    public boolean isHaccp()
    {
        return isHaccp;
    }

    /**
     * @return: Integer
     */
    public Integer getAddressOut()
    {
        return addressOut;
    }

    /**
     * @return: int
     */
    public int getBitPosition()
    {
        return bitPosition;
    }

    /**
     * @return: int
     */
    public int getDecimal()
    {
        return decimal;
    }

    /**
     * @return: boolean
     */
    public boolean isSigned()
    {
        return isSigned;
    }

    /**
     * @return: int
     */
    public int getVarDimension()
    {
        return varDimension;
    }

    /**
     * @return: int
     */
    public int getVarLength()
    {
        return varLength;
    }

    /**
     * @return: DeviceInfo
     */
    public DeviceInfo getDevInfo()
    {
        return devInfo;
    }

    /**
     * @param devInfo
     */
    public void setDevInfo(DeviceInfo devInfo)
    {
        this.devInfo = devInfo;
    }

    /**
     * @return: boolean
     */
    public boolean isIsturn()
    {
        return isturn;
    }

    /**
     * @param isturn
     */
    public void setIsturn(boolean isturn)
    {
        this.isturn = isturn;
    }

    /**
     * @param addressIn
     */
    public void setAddressIn(Integer addressIn)
    {
        this.addressIn = addressIn;
    }

    /**
     * @param addressOut
     */
    public void setAddressOut(Integer addressOut)
    {
        this.addressOut = addressOut;
    }

    /**
     * @param bitPosition
     */
    public void setBitPosition(int bitPosition)
    {
        this.bitPosition = bitPosition;
    }

    /**
     * @param decimal
     */
    public void setDecimal(int decimal)
    {
        this.decimal = decimal;
    }

    /**
     * @param device
     */
    public void setDevice(Integer device)
    {
        this.device = device;
    }

    /**
     * @param frequency
     */
    public void setFrequency(Integer frequency)
    {
        this.frequency = frequency;
    }

    /**
     * @param functionCode
     */
    public void setFunctionCode(Integer functionCode)
    {
        this.functionCode = functionCode;
    }

    /**
     * @param id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @param isActive
     */
    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    /**
     * @param isHaccp
     */
    public void setHaccp(boolean isHaccp)
    {
        this.isHaccp = isHaccp;
    }

    /**
     * @param isLogic
     */
    public void setLogic(boolean isLogic)
    {
        this.isLogic = isLogic;
    }

    /**
     * @param isSigned
     */
    public void setSigned(boolean isSigned)
    {
        this.isSigned = isSigned;
    }

    /**
     * @param keyactual
     */
    public void setKeyactual(short keyactual)
    {
        this.keyactual = keyactual;
    }

    /**
     * @param keymax
     */
    public void setKeymax(short keymax)
    {
        this.keymax = keymax;
    }

    /**
     * @param model
     */
    public void setModel(Integer model)
    {
        this.model = model;
    }

    /**
     * @param priority
     */
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    /**
     * @param site
     */
    public void setSite(Integer site)
    {
        this.site = site;
    }

    /**
     * @param type
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @param varDimension
     */
    public void setVarDimension(int varDimension)
    {
        this.varDimension = varDimension;
    }

    /**
     * @param variation
     */
    public void setVariation(double variation)
    {
        this.variation = variation;
    }

    /**
     * @param varLength
     */
    public void setVarLength(int varLength)
    {
        this.varLength = varLength;
    }

    /**
     * @return: String
     */
    public String getMaxValue()
    {
        return maxValue;
    }

    /**
     * @param maxValue
     */
    public void setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
    }

    /**
     * @return: String
     */
    public String getMinValue()
    {
        return minValue;
    }

    /**
     * @param minValue
     */
    public void setMinValue(String minValue)
    {
        this.minValue = minValue;
    }

	public void setMeasureunit(String measureunit) {
		this.measureunit = measureunit;
	}

	public String getMeasureunit() {
		return measureunit;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

    //Manuel Gilioli
} //Class VarPhyInfo
