package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.Record;


public class ProfileGroupsBean
{
    private static final String PROFILE = "idprofile";
    private static final String IDGROUP = "idgroup";
    private Integer profile = null;
    private Integer idgroup = null;

    public ProfileGroupsBean(Record record)
    {
        profile = (Integer) record.get(PROFILE);
        idgroup = (Integer) record.get(IDGROUP);

        if (idgroup.intValue() == 0)
        {
            idgroup = null;
        }
    }

    /**
     * @return: Integer
     */
    public Integer getIdgroup()
    {
        return idgroup;
    }

    /**
     * @param idgroup
     */
    public void setIdgroup(Integer idgroup)
    {
        this.idgroup = idgroup;
    }

    /**
     * @return: String
     */
    public Integer getProfile()
    {
        return profile;
    }

    /**
     * @param profile
     */
    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }
}
