package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;

import java.util.Date;


public class VarphyBean extends VariableInfo
{
    private final static String PVCODE = "pvcode";
    private final static String IDVARMDL = "idvarmdl";
    public final static String CODE = "code";
    private final static String DISPLAY = "todisplay";
    private final static String ISBUTTON = "buttonpath";
    private final static String RW = "readwrite";
    private final static String DEFVAL = "defaultvalue";
    private final static String MUNIT = "measureunit";
    private final static String GRPCODE = "idvargroup";
    private final static String ION = "imageon";
    private final static String IOFF = "imageoff";
    public final static String DELAY = "delay";
    public final static String GRPCATEGORY = "grpcategory";
    private final static String INSTIME = "inserttime";
    private final static String UPDTIME = "lastupdate";
    private final static String ISONCHANGE = "isonchange";
    public final static String IDHSVARIABLE = "idhsvariable";
    public final static String DESCRIPTION = "description";
    public final static String LONG_DESCR = "longdescr";
    public final static String SHORTDESC = "shortdescr";
    private String pvCode = null;
    private Integer idMdl = null;
    private String codeVar = null;
    private String display = null;
    private String readwrite = null;
    private String defaultValue = null;
    private String measureUnit = null;
    private Integer grpCode = null;
    private String imageOn = null;
    private String imageOff = null;
    private Integer delay = null;
    private boolean isonchange = true;
    private Integer grpcategory = null;
    private Integer idhsvariable = null;
    private Date insertTime = null;
    private Date lastUpdate = null;
    private String longDescription = "";
    private String shortDescription = "";
    private String shortDesc = "";
    private String buttonpath = "";
    private VarphyBean son = null;

    public VarphyBean(VarphyBean varphyBean)
    {
        super((VariableInfo) varphyBean);
        this.pvCode = varphyBean.getPv();
        this.idMdl = varphyBean.getIdMdl();
        this.codeVar = varphyBean.getCodeVar();
        this.display = varphyBean.getDisplay();
        this.buttonpath = varphyBean.getButtonpath();
        this.readwrite = varphyBean.getReadwrite();
        this.defaultValue = varphyBean.getDefaultValue();
        this.measureUnit = varphyBean.getMeasureUnit();
        this.grpCode = varphyBean.getGrpCode();
        this.imageOn = varphyBean.getImageOn();
        this.imageOff = varphyBean.getImageOff();
        this.delay = varphyBean.getDelay();
        this.grpcategory = varphyBean.getGrpcategory();
        this.insertTime = varphyBean.getInsertTime();
        this.lastUpdate = varphyBean.getLastUpdate();
        this.isonchange = varphyBean.isOnChange();
        this.idhsvariable = varphyBean.getIdhsvariable();
        this.shortDescription = varphyBean.getShortDescription();
        this.longDescription = varphyBean.getLongDescription();
        this.shortDesc = varphyBean.getShortDesc();
    }
    public VarphyBean()
    {
    	
    }
    public VarphyBean(Record rec)
    {
        super(rec);
        this.pvCode = UtilBean.trim(rec.get(PVCODE));
        this.idMdl = (Integer) rec.get(IDVARMDL);
        this.codeVar = UtilBean.trim(rec.get(CODE));
        this.display = UtilBean.trim(rec.get(DISPLAY));
        this.buttonpath = UtilBean.trim(rec.get(ISBUTTON));
        this.readwrite = UtilBean.trim(rec.get(RW));
        this.defaultValue = (String) rec.get(DEFVAL);
        this.measureUnit = UtilBean.trim(rec.get(MUNIT));
        this.grpCode = (Integer) rec.get(GRPCODE);
        this.imageOn = UtilBean.trim(rec.get(ION));
        this.imageOff = UtilBean.trim(rec.get(IOFF));
        this.delay = (Integer) rec.get(DELAY);
        this.grpcategory = (Integer) rec.get(GRPCATEGORY);
        this.insertTime = (Date) rec.get(INSTIME);
        this.lastUpdate = (Date) rec.get(UPDTIME);
        this.isonchange = UtilBean.checkBoolean(rec.get(ISONCHANGE), false);
        this.idhsvariable = (Integer) rec.get(IDHSVARIABLE);
        if (rec.hasColumn(DESCRIPTION)) {
	        this.shortDescription = (rec.get(DESCRIPTION) != null)
	            ? UtilBean.trim(rec.get(DESCRIPTION)) : "";
        }
        if (rec.hasColumn(LONG_DESCR)) {
	        this.longDescription = (rec.get(LONG_DESCR) != null)
	            ? UtilBean.trim(rec.get(LONG_DESCR)) : "";
        }
        if (rec.hasColumn(SHORTDESC))
        {
            this.shortDesc = (rec.get(SHORTDESC) != null)
            ? UtilBean.trim(rec.get(SHORTDESC)) : "";
        }
    }

    /**
         * @return: String
         */
    public boolean isOnChange()
    {
        return isonchange;
    }

    /**
     * @param isonchange
     */
    public void setIsOnChange(boolean isOnChange)
    {
        this.isonchange = isOnChange;
    }

    /**
     * @return: Integer
     */
    public Integer getIdhsvariable()
    {
        return idhsvariable;
    }

    /**
     * @param idhsvariable
     */
    public void setIdhsvariable(Integer idhsvariable)
    {
        this.idhsvariable = idhsvariable;
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
    public String getCodeVar()
    {
        return codeVar;
    }

    /**
     * @param codeVar
     */
    public void setCodeVar(String codeVar)
    {
        this.codeVar = codeVar;
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
     * @return: String
     */
    public String getDisplay()
    {
        return display;
    }

    /**
     * @param display
     */
    public void setDisplay(String display)
    {
        this.display = display;
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
    public Integer getGrpCode()
    {
        return grpCode;
    }

    /**
     * @param grpCode
     */
    public void setGrpCode(Integer grpCode)
    {
        this.grpCode = grpCode;
    }

    /**
     * @return: Integer
     */
    public Integer getIdMdl()
    {
        return idMdl;
    }

    /**
     * @param idMdl
     */
    public void setIdMdl(Integer idMdl)
    {
        this.idMdl = idMdl;
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
     * @return: Date
     */
    public Date getInsertTime()
    {
        return insertTime;
    }

    /**
     * @param insertTime
     */
    public void setInsertTime(Date insertTime)
    {
        this.insertTime = insertTime;
    }

    /**
     * @return: Date
     */
    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate
     */
    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return: String
     */
    public String getLongDescription()
    {
        return longDescription;
    }

    /**
     * @param longDescription
     */
    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }

    /**
         * @return: String
         */
    public String getShortDesc()
    {
        return shortDesc;
    }

    /**
     * @param shortDesc
     */
    public void setShortDesc(String shortDesc)
    {
        this.shortDesc = shortDesc;
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
    public String getPvCode()
    {
        return pvCode;
    }

    /**
     * @param pvCode
     */
    public void setPvCode(String pvCode)
    {
        this.pvCode = pvCode;
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
    public String getShortDescription()
    {
        return shortDescription;
    }

    /**
     * @param shortDescription
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public int save() throws DataBaseException
    {
        Object[] values = new Object[38];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "cfvariable", "idvariable");
        values[1] = BaseConfig.getPlantId();
        values[2] = getSite();
        values[3] = getDevice();
        values[4] = UtilBean.writeBoolean(isLogic());
        values[5] = idMdl;
        values[6] = getFunctionCode();
        values[7] = codeVar;
        values[8] = new Integer(getType());
        values[9] = getAddressIn();
        values[10] = getAddressOut();
        values[11] = new Integer(getVarDimension());
        values[12] = new Integer(getVarLength());
        values[13] = new Integer(getBitPosition());
        values[14] = UtilBean.writeBoolean(isSigned());
        values[15] = new Integer(getDecimal());
        values[16] = display;
        values[17] = buttonpath;
        values[18] = getPriority();
        values[19] = readwrite;
        values[20] = getMinValue();
        values[21] = getMaxValue();
        values[22] = defaultValue;
        values[23] = measureUnit;
        values[24] = grpCode;
        values[25] = imageOn;
        values[26] = imageOff;
        values[27] = getFrequency();
        values[28] = new Double(getVariation());
        values[29] = delay;
        values[30] = UtilBean.writeBoolean(isonchange);
        values[31] = UtilBean.writeBoolean(isHaccp());
        values[32] = UtilBean.writeBoolean(isActive());
        values[33] = UtilBean.writeBoolean(false);
        values[34] = grpcategory;
        values[35] = idhsvariable;
        values[36] = insertTime;
        values[37] = lastUpdate;

        String insert = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);

        return ((Integer) values[0]).intValue();
    }

    /**
     * @return: VarphyBean
     */
    public VarphyBean getSon()
    {
        return son;
    }

    /**
     * @param son
     */
    public void setSon(VarphyBean son)
    {
        this.son = son;
    }
}
