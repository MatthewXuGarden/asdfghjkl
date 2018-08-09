package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.Record;


public class ProfileVarsBean
{
    private static final String PROFILE = "idprofile";
    private static final String IDVARIABLE = "idvariable";
    private static final String MINVALUE = "minvalue";
    private static final String MAXVALUE = "maxvalue";
    private Integer profile = null;
    private Integer idvariable = null;
    private Double minvalue = null;
    private Double maxvalue = null;

    public ProfileVarsBean(Record record)
    {
        profile = (Integer) record.get(PROFILE);
        idvariable = (Integer) record.get(IDVARIABLE);
        minvalue = (Double) record.get(MINVALUE);
        maxvalue = (Double) record.get(MAXVALUE);
    }

    /**
     * @return: Integer
     */
    public Integer getIdvariable()
    {
        return idvariable;
    }

    /**
     * @return: Double
     */
    public Double getMaxvalue()
    {
        return maxvalue;
    }

    /**
     * @return: Double
     */
    public Double getMinvalue()
    {
        return minvalue;
    }

    /**
     * @return: String
     */
    public Integer getProfile()
    {
        return profile;
    }
}
