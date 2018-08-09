package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.io.Serializable;


public class GroupBean implements Serializable
{
    private int groupId = -1;
    //private int[] groupIds = null;
    private String description = "";
    private boolean isGlobal = false;

    /**
     * @param groupId
     * @param isglobal
     * GroupBean
     */
    public GroupBean(int groupId, String isglobal)
    {
        this.groupId = groupId;
        this.isGlobal = ("TRUE".equals(isglobal));
    }

    public GroupBean(Record record, int idsite, String language)
        throws DataBaseException
    {
        this.groupId = Integer.parseInt(record.get("idgroup").toString());

        if (record.get("isglobal").toString().equals("TRUE"))
        {
            this.isGlobal = true;
        }
        else
        {
            this.isGlobal = false;
        }

        this.description = (String) record.get("description");
    }

    /**
     * @return: String
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return: boolean
     */
    public boolean isGlobal()
    {
        return isGlobal;
    }


    /**
    * @param description
    */
    public void setDescription(String description)
    {
        if (null == description)
        {
            this.description = "";
        }
        else
        {
            this.description = description;
        }
    }


    public int getGroupId()
    {
        return groupId;
    }
}
