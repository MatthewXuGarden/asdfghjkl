package com.carel.supervisor.presentation.bean.rule;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;
import java.sql.Timestamp;


public class ActionBean
{
    private static final String IDACTION = "idaction";
    private static final String PVCODE = "pvcode";
    private static final String IDSITE = "idsite";
    private static final String CODE = "code";
    private static final String ACTIONCODE = "actioncode";
    private static final String ISSCHEDULED = "isscheduled";
    private static final String ACTIONTYPE = "actiontype";
    private static final String TEMPLATE = "template";
    private static final String PARAMETERS = "parameters";
    private static final String LASTUPDATE = "lastupdate";
    private int idAction = -1;
    private String pvcode = null;
    private boolean isScheduled = false;
    private int idsite = -1;
    private int actioncode = -1;
    private String actiontype = null;
    private String template = null;
    private String parameters = null;
    private Timestamp lastupdate = null;
    private String description = null;

    public ActionBean(Record record, String language) throws DataBaseException
    {
        idAction = ((Integer) record.get(IDACTION)).intValue();
        pvcode = UtilBean.trim(record.get(PVCODE));
        idsite = ((Integer) record.get(IDSITE)).intValue();
        description = UtilBean.trim(record.get(CODE));
        actiontype = UtilBean.trim(record.get(ACTIONTYPE));
        actioncode = ((Integer) record.get(ACTIONCODE)).intValue();
        template = UtilBean.trim(record.get(TEMPLATE));
        parameters = UtilBean.trim(record.get(PARAMETERS));
        lastupdate = ((Timestamp) record.get(LASTUPDATE));
        isScheduled = UtilBean.checkBoolean(record.get(ISSCHEDULED), false);
    }

    /**
     * @return: int
     */
    public int getActioncode()
    {
        return actioncode;
    }

    /**
     * @param actioncode
     */
    public void setActioncode(int actioncode)
    {
        this.actioncode = actioncode;
    }

    /**
     * @return: String
     */
    public String getActiontype()
    {
        return actiontype;
    }

    /**
     * @param actiontype
     */
    public void setActiontype(String actiontype)
    {
        this.actiontype = actiontype;
    }

    /**
     * @return: Integer
     */
    public int getIdAction()
    {
        return idAction;
    }

    /**
     * @param idAction
     */
    public void setIdAction(int idAction)
    {
        this.idAction = idAction;
    }

    /**
     * @return: int
     */
    public int getIdsite()
    {
        return idsite;
    }

    /**
     * @param idsite
     */
    public void setIdsite(int idsite)
    {
        this.idsite = idsite;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    /**
     * @param lastupdate
     */
    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    /**
     * @return: String
     */
    public String getParameters()
    {
        return parameters;
    }

    /**
     * @param parameters
     */
    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    /**
     * @return: String
     */
    public String getPvcode()
    {
        return pvcode;
    }

    /**
     * @param pvcode
     */
    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    /**
     * @return: String
     */
    public String getTemplate()
    {
        return template;
    }

    /**
     * @param template
     */
    public void setTemplate(String template)
    {
        this.template = template;
    }

    /**
     * @return: String
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return: boolean
     */
    public boolean isScheduled()
    {
        return isScheduled;
    }

    /**
     * @param isScheduled
     */
    public void setScheduled(boolean isScheduled)
    {
        this.isScheduled = isScheduled;
    }
}
