package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;


public class DevMdlBean
{
    private static final String ID_DEV_MDL = "iddevmdl";
    private static final String ID_SITE = "idsite";
    private static final String CODE = "code";
    private static final String MANUFACTURER = "manufacturer";
    private static final String HD_VERSION = "hdversion";
    private static final String SW_VERSION = "swversion";
    private static final String IMAGE_PATH = "imagepath";
    private static final String LITTLE_ENDIAN = "littlendian";
    private final static String DESCRIPTION = "description";
    private final static String SHORT_DESCRIPTION = "shortdescr";
    private final static String LONG_DESCRIPTION = "longdescr";
    private int iddevmdl = -1;
    private int idsite = -1;
    private String code = null;
    private String manufacturer = null;
    private String hdversion = null;
    private String swversion = null;
    private String imagepath = null;
    private String description = "";
    private String shortdescription = "";
    private String longdescription = "";
    private boolean isLittleEndian = false;

    public DevMdlBean()
    {
    }

    public DevMdlBean(Record record, String language) throws DataBaseException
    {
        this.iddevmdl = ((Integer) record.get(ID_DEV_MDL)).intValue();
        this.idsite = ((Integer) record.get(ID_SITE)).intValue();
        this.code = record.get(CODE).toString().trim();
        this.isLittleEndian = UtilBean.checkBoolean((String) record.get(
                    LITTLE_ENDIAN), true);
        this.manufacturer = (record.get(MANUFACTURER) != null)
            ? record.get(MANUFACTURER).toString().trim() : "Null";
        this.hdversion = (record.get(HD_VERSION) != null)
            ? record.get(HD_VERSION).toString().trim() : "Null";
        this.swversion = (record.get(SW_VERSION) != null)
            ? record.get(SW_VERSION).toString().trim() : "Null";
        this.imagepath = (record.get(IMAGE_PATH) != null)
            ? record.get(IMAGE_PATH).toString().trim() : "Null";
        this.description = (String) record.get(DESCRIPTION);
        this.shortdescription = (String) record.get(SHORT_DESCRIPTION);
        this.longdescription = (String) record.get(LONG_DESCRIPTION);
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getHdversion()
    {
        return hdversion;
    }

    public void setHdversion(String hdversion)
    {
        this.hdversion = hdversion;
    }

    public int getIddevmdl()
    {
        return iddevmdl;
    }

    public void setIddevmdl(int iddevmdl)
    {
        this.iddevmdl = iddevmdl;
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

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getSwversion()
    {
        return swversion;
    }

    public void setSwversion(String swversion)
    {
        this.swversion = swversion;
    }

    /**
     * @return: String
     */
    public String getLongdescription()
    {
        return longdescription;
    }

    /**
     * @param longdescription
     */
    public void setLongdescription(String longdescription)
    {
        this.longdescription = longdescription;
    }

    /**
     * @return: String
     */
    public String getShortdescription()
    {
        return shortdescription;
    }

    /**
     * @param shortdescription
     */
    public void setShortdescription(String shortdescription)
    {
        this.shortdescription = shortdescription;
    }

    /**
     * @return: boolean
     */
    public boolean isLittleEndian()
    {
        return isLittleEndian;
    }

    /**
     * @param isLittleEndian
     */
    public void setLittleEndian(boolean isLittleEndian)
    {
        this.isLittleEndian = isLittleEndian;
    }
}
