package com.carel.supervisor.controller.database;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class ActionBean
{
    private static final String IDACTION = "idaction";
    private static final String PVCODE = "pvcode";
    private static final String IDSITE = "idsite";
    private static final String ACTIONCODE = "actioncode";
    private static final String ACTIONTYPE = "actiontype";
    private static final String PARAMETERS = "parameters";
    private Integer idAction = null;
    private String pvcode = null;
    private int idsite = 0;
    private int actioncode = 0;
    private String actiontype = null;
    private String parameters = null;
    private ActionBean next = null;

    public ActionBean(Record record)
    {
        idAction = (Integer) record.get(IDACTION);
        pvcode = UtilBean.trim(record.get(PVCODE));
        idsite = ((Integer) record.get(IDSITE)).intValue();
        actiontype = UtilBean.trim(record.get(ACTIONTYPE));
        actioncode = ((Integer) record.get(ACTIONCODE)).intValue();
        parameters = UtilBean.trim(record.get(PARAMETERS));
    }

    /**
     * @return: String
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
    public Integer getIdAction()
    {
        return idAction;
    }

    /**
     * @param idAction
     */
    public void setIdAction(Integer idAction)
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
     * @return: ActionBean
     */
    public ActionBean getNext()
    {
        return next;
    }

    /**
     * @param next
     */
    public void setNext(ActionBean next)
    {
        this.next = next;
    }
}
