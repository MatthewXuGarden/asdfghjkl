package com.carel.supervisor.presentation.bean;



import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;


public class VarMdlBean
{
    private final static String ID_VAR_MDL = "idvarmdl";
    private final static String ID_SITE = "idsite";
    private final static String ID_DEV_MDL = "iddevmdl";
    private final static String CODE = "code";
    private final static String TYPE = "type";
    public static final String ADDRESS_IN = "addressin";
    public static final String ADDRESS_OUT = "addressout";
    public static final String VAR_DIMENSION = "vardimension";
    public static final String VAR_LENGTH = "varlength";
    public static final String BIT_POSITION = "bitposition";
    public static final String SIGNED = "signed";
    public static final String DECIMAL = "decimal";
    private final static String TO_DISPLAY = "todisplay";
    private final static String BUTTON_PATH = "buttonpath";
    private final static String PRIORITY = "priority";
    private final static String READ_WRITE = "readwrite";
    private final static String MIN_VALUE = "minvalue";
    private final static String MAX_VALUE = "maxvalue";
    private final static String MUNIT = "measureunit";
    private final static String ID_VAR_GROUP = "idvargroup";
    private final static String ION = "imageon";
    private final static String IOFF = "imageoff";
    private final static String FREQUENCY = "frequency";
    private final static String DELTA = "delta";
    private final static String DELAY = "delay";
    private final static String ISHACCP = "ishaccp";
    private final static String ISRELAY = "isrelay";
    private final static String GRP_CATEGORY = "grpcategory";
    private final static String IS_ACTIVE = "isactive";
    private final static String HS_TIME = "hstime";
    private final static String HS_FREQUENCY = "hsfrequency";
    private final static String HS_DELTA = "hsdelta";
    private final static String DESCRIPTION = "description";
    private final static String SHORT_DESCRIPTION = "shortdescr";
    private final static String LONG_DESCRIPTION = "longdescr";
    
    private Integer idvarmdl = null;
    private Integer idsite = null;
    private Integer iddevmdl = null;
    private String code = null;
    private Integer type = null;
    private Integer addressIn = null;
    private Integer addressOut = null;
    private Integer varDimension = null;
    private Integer varLength = null;
    private Integer bitPosition = null;
    private Integer decimal = null;
    private String signed = null;
    private String todisplay = null;
    private String buttonpath = null;
    private Integer priority = null;
    private String readwrite = null;
    private String minvalue = null;
    private String maxvalue = null;
    private String defaultValue = null;
    private String measureUnit = null;
    private Integer idvargroup = null;
    private String imageOn = null;
    private String imageOff = null;
    private Integer frequency = null;
    private Double delta = null;
    private Integer delay = null;
    private String ishaccp = "";
    private String isrelay = "";
    private Integer grpcategory = null;
    private String isactive = null;
    private Integer hstime = null;
    private Integer hsfrequency = null;
    private Double hsdelta = null;

    private String description = "";
    private String shortdescription = "";
    private String longdescription = "";
    
    public VarMdlBean(Record record) throws DataBaseException
    {
        this.idvarmdl = (Integer) record.get(ID_VAR_MDL);
        this.idsite = (Integer) record.get(ID_SITE);
        this.iddevmdl = (Integer) record.get(ID_DEV_MDL);
        this.code = (String) record.get(CODE);
        this.type = (Integer) record.get(TYPE);
        this.addressIn = (Integer) record.get(ADDRESS_IN);
        this.addressOut = (Integer) record.get(ADDRESS_OUT);
        this.signed = (String) record.get(SIGNED);
        this.varDimension = (Integer) record.get(VAR_DIMENSION);
        this.varLength = (Integer) record.get(VAR_LENGTH);
        this.bitPosition = (Integer) record.get(BIT_POSITION);
        this.decimal = (Integer) record.get(DECIMAL);
        this.todisplay = (String) record.get(TO_DISPLAY);
        this.buttonpath = (String) record.get(BUTTON_PATH);
        this.priority = (Integer) record.get(PRIORITY);
        this.readwrite = (String) record.get(READ_WRITE);
        this.minvalue = (String) record.get(MIN_VALUE);
        this.maxvalue = (String) record.get(MAX_VALUE);
        this.measureUnit = (String) record.get(MUNIT);
        this.idvargroup = (Integer) record.get(ID_VAR_GROUP);
        this.imageOn = (String) record.get(ION);
        this.imageOff = (String) record.get(IOFF);
        this.frequency = (Integer) record.get(FREQUENCY);
        this.delta = (Double) record.get(DELTA);
        this.delay = (Integer) record.get(DELAY);
        this.ishaccp = UtilBean.trim(record.get(ISHACCP));
        this.isrelay = UtilBean.trim(record.get(ISRELAY));
        this.grpcategory = (Integer) record.get(GRP_CATEGORY);
        this.isactive = UtilBean.trim(record.get(IS_ACTIVE));
        this.hstime = (Integer) record.get(HS_TIME);
        this.hsfrequency = (Integer) record.get(HS_FREQUENCY);
        this.hsdelta = (Double) record.get(HS_DELTA);
        
        if (record.hasColumn(DESCRIPTION)) this.description = (String) record.get(DESCRIPTION);
        if (record.hasColumn(SHORT_DESCRIPTION)) this.shortdescription = (String) record.get(SHORT_DESCRIPTION);
        if (record.hasColumn(LONG_DESCRIPTION)) this.longdescription = (String) record.get(LONG_DESCRIPTION);
    }

    /**
     * @return: String
     */
    public String getButtonpath()
    {
        return buttonpath;
    }

    /**
     * @param buttonpath
     */
    public void setButtonpath(String buttonpath)
    {
        this.buttonpath = buttonpath;
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
     * @return: String
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * @return: Integer
     */
    public Integer getDelay()
    {
        return delay;
    }

    /**
     * @param delay
     */
    public void setDelay(Integer delay)
    {
        this.delay = delay;
    }

    /**
     * @return: Double
     */
    public Double getDelta()
    {
        return delta;
    }

    /**
     * @param delta
     */
    public void setDelta(Double delta)
    {
        this.delta = delta;
    }

    /**
     * @return: String
     */

    /*public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }*/

    /**
     * @return: Integer
     */
    public Integer getFrequency()
    {
        return frequency;
    }

    /**
     * @param frequency
     */
    public void setFrequency(Integer frequency)
    {
        this.frequency = frequency;
    }

    /**
     * @return: Integer
     */
    public Integer getGrpcategory()
    {
        return grpcategory;
    }

    /**
     * @param grpcategory
     */
    public void setGrpcategory(Integer grpcategory)
    {
        this.grpcategory = grpcategory;
    }

    /**
     * @return: Integer
     */
    public Integer getIddevmdl()
    {
        return iddevmdl;
    }

    /**
     * @param iddevmdl
     */
    public void setIddevmdl(Integer iddevmdl)
    {
        this.iddevmdl = iddevmdl;
    }

    /**
     * @return: Integer
     */
    public Integer getIdsite()
    {
        return idsite;
    }

    /**
     * @param idsite
     */
    public void setIdsite(Integer idsite)
    {
        this.idsite = idsite;
    }

    /**
     * @return: Integer
     */
    public Integer getIdvargroup()
    {
        return idvargroup;
    }

    /**
     * @param idvargroup
     */
    public void setIdvargroup(Integer idvargroup)
    {
        this.idvargroup = idvargroup;
    }

    /**
     * @return: Integer
     */
    public Integer getIdvarmdl()
    {
        return idvarmdl;
    }

    /**
     * @param idvarmdl
     */
    public void setIdvarmdl(Integer idvarmdl)
    {
        this.idvarmdl = idvarmdl;
    }

    /**
     * @return: String
     */
    public String getImageOff()
    {
        return imageOff;
    }

    /**
     * @param imageOff
     */
    public void setImageOff(String imageOff)
    {
        this.imageOff = imageOff;
    }

    /**
     * @return: String
     */
    public String getImageOn()
    {
        return imageOn;
    }

    /**
     * @param imageOn
     */
    public void setImageOn(String imageOn)
    {
        this.imageOn = imageOn;
    }

    /**
     * @return: String
     */
    public String getIshaccp()
    {
        return ishaccp;
    }

    /**
     * @param ishaccp
     */
    public void setIshaccp(String ishaccp)
    {
        this.ishaccp = ishaccp;
    }

    /**
     * @return: String
     */
    public String getIsrelay()
    {
        return isrelay;
    }

    /**
     * @param isrelay
     */
    public void setIsrelay(String isrelay)
    {
        this.isrelay = isrelay;
    }

    /**
     * @return: String
     */
    public String getMaxvalue()
    {
        return maxvalue;
    }

    /**
     * @param maxvalue
     */
    public void setMaxvalue(String maxvalue)
    {
        this.maxvalue = maxvalue;
    }

    /**
     * @return: String
     */
    public String getMeasureUnit()
    {
        return measureUnit;
    }

    /**
     * @param measureUnit
     */
    public void setMeasureUnit(String measureUnit)
    {
        this.measureUnit = measureUnit;
    }

    /**
     * @return: String
     */
    public String getMinvalue()
    {
        return minvalue;
    }

    /**
     * @param minvalue
     */
    public void setMinvalue(String minvalue)
    {
        this.minvalue = minvalue;
    }

    /**
     * @return: Integer
     */
    public Integer getPriority()
    {
        return priority;
    }

    /**
     * @param priority
     */
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    /**
     * @return: String
     */
    public String getReadwrite()
    {
        return readwrite;
    }

    /**
     * @param readwrite
     */
    public void setReadwrite(String readwrite)
    {
        this.readwrite = readwrite;
    }

    /**
     * @return: String
     */
    public String getTodisplay()
    {
        return todisplay;
    }

    /**
     * @param todisplay
     */
    public void setTodisplay(String todisplay)
    {
        this.todisplay = todisplay;
    }

    /**
     * @return: Integer
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type)
    {
        this.type = type;
    }

    /*
        public String getLongdescription()
        {
            return longdescription;
        }


        public void setLongdescription(String longdescription)
        {
            this.longdescription = longdescription;
        }


        public String getShortdescription()
        {
            return shortdescription;
        }


        public void setShortdescription(String shortdescription)
        {
            this.shortdescription = shortdescription;
        }
    */

    /**
     * @return: Integer
     */
    public Integer getAddressIn()
    {
        return addressIn;
    }

    /**
     * @param addressIn
     */
    public void setAddressIn(Integer addressIn)
    {
        this.addressIn = addressIn;
    }

    /**
     * @return: Integer
     */
    public Integer getAddressOut()
    {
        return addressOut;
    }

    /**
     * @param addressOut
     */
    public void setAddressOut(Integer addressOut)
    {
        this.addressOut = addressOut;
    }

    /**
     * @return: Integer
     */
    public Integer getBitPosition()
    {
        return bitPosition;
    }

    /**
     * @param bitPosition
     */
    public void setBitPosition(Integer bitPosition)
    {
        this.bitPosition = bitPosition;
    }

    /**
     * @return: Integer
     */
    public Integer getDecimal()
    {
        return decimal;
    }

    /**
     * @param decimal
     */
    public void setDecimal(Integer decimal)
    {
        this.decimal = decimal;
    }

    /**
     * @return: String
     */
    public String getSigned()
    {
        return signed;
    }

    /**
     * @param isSigned
     */
    public void setSigned(String signed)
    {
        this.signed = signed;
    }

    /**
     * @return: Integer
     */
    public Integer getVarDimension()
    {
        return varDimension;
    }

    /**
     * @param varDimension
     */
    public void setVarDimension(Integer varDimension)
    {
        this.varDimension = varDimension;
    }

    /**
     * @return: Integer
     */
    public Integer getVarLength()
    {
        return varLength;
    }

    /**
     * @param varLength
     */
    public void setVarLength(Integer varLength)
    {
        this.varLength = varLength;
    }

    /**
     * @return: String
     */
    public String getIsactive()
    {
        return isactive;
    }

    /**
     * @param isactive
     */
    public void setIsactive(String isactive)
    {
        this.isactive = isactive;
    }

	/**
	 * @return: Integer
	 */
	
	public Double getHsdelta() {
		return hsdelta;
	}

	/**
	 * @param hsdelta
	 */
	public void setHsdelta(Double hsdelta) {
		this.hsdelta = hsdelta;
	}

	/**
	 * @return: Integer
	 */
	
	public Integer getHsfrequency() {
		return hsfrequency;
	}

	/**
	 * @param hsfrequency
	 */
	public void setHsfrequency(Integer hsfrequency) {
		this.hsfrequency = hsfrequency;
	}

	/**
	 * @return: Integer
	 */
	
	public Integer getHstime() {
		return hstime;
	}

	/**
	 * @param hstime
	 */
	public void setHstime(Integer hstime) {
		this.hstime = hstime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongdescription() {
		return longdescription;
	}

	public void setLongdescription(String longdescription) {
		this.longdescription = longdescription;
	}

	public String getShortdescription() {
		return shortdescription;
	}

	public void setShortdescription(String shortdescription) {
		this.shortdescription = shortdescription;
	}

	
    
}
