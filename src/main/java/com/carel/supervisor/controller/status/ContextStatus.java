package com.carel.supervisor.controller.status;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;


public class ContextStatus
{
    private long delay = 0;
    private boolean status = false;
    private long activationTime = 0;
    private long actualTime = 0;
    // last time of toManageStatus
    private long lastExecuteTime = 0;
    private Integer idRule = null;
    private Integer idVar = null;
    private boolean persistent = true;
    private boolean manualCancel = false;
    private boolean manualReset = false;
    //if acknowledged, no re-send alarm notification
    private boolean isAcknowledged = false;
    //flag for cylicSendNOtification
    //by default it is null: need to check if it is resendNotification
    private Boolean isResendNotification = null; 
    private boolean timebandOk = false;

    //to save the status before user click Inhibition
    protected AbstractStatus actualStatusBeforeManualReset = null;
    
    public ContextStatus()
    {
    }

    public ContextStatus(RuleStateBean ruleStateBean)
    {
        idRule = ruleStateBean.getIdRule();
        idVar = ruleStateBean.getIdVar();
        activationTime = ruleStateBean.getInserttime().getTime();
    }

    /**
     * @return: long
     */
    public long getActivationTime()
    {
        return activationTime;
    }

    /**
     * @param activationTime
     */
    public void setActivationTime(long activationTime)
    {
        this.activationTime = activationTime;
    }

    /**
     * @return: long
     */
    public long getDelay()
    {
        return delay;
    }

    /**
     * @param delay
     */
    public void setDelay(long delay)
    {
        this.delay = delay * 1000;
    }

    /**
     * @return: boolean
     */
    public boolean isStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /**
     * @return: long
     */
    public long getActualTime()
    {
        return actualTime;
    }

    /**
     * @param actualTime
     */
    public void setActualTime(long actualTime)
    {
        this.actualTime = actualTime;
    }

    /**
     * @return: Integer
     */
    public Integer getIdRule()
    {
        return idRule;
    }

    /**
     * @return: Integer
     */
    public Integer getIdVar()
    {
        return idVar;
    }

    public void setIdVar(Integer idVar)
    {
        this.idVar = idVar;
    }

    /**
     * @param idRule
     */
    public void setIdRule(Integer idRule)
    {
        this.idRule = idRule;
    }

    /**
     * @return: boolean
     */
    public boolean isPersistent()
    {
        return persistent;
    }

    /**
     * @param persistent
     */
    public void setPersistent(boolean persistent)
    {
        this.persistent = persistent;
    }

    /**
     * @return: boolean
     */
    public boolean isManualCancel()
    {
        return manualCancel;
    }

    /**
     * @param manual
     */
    public void setManualCancel(boolean manual)
    {
        this.manualCancel = manual;
    }

    /**
         * @return: boolean
         */
    public boolean isManualReset()
    {
        return manualReset;
    }

    /**
     * @param manualReset
     */
    public void setManualReset(boolean manualReset)
    {
        this.manualReset = manualReset;
    }

    /**
    * @return: boolean
    */
    public boolean isTimebandOk()
    {
        return timebandOk;
    }

    /**
     * @param timemandOk
     */
    public void setTimebandOk(boolean timemandOk)
    {
        this.timebandOk = timemandOk;
    }
    public AbstractStatus getActualStatusBeforeManualReset() {
		return actualStatusBeforeManualReset;
	}

	public void setActualStatusBeforeManualReset(
			AbstractStatus actualStatusBeforeManualReset) {
		this.actualStatusBeforeManualReset = actualStatusBeforeManualReset;
	}

	public long getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(long lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	public boolean isAcknowledged() {
		return isAcknowledged;
	}

	public void setAcknowledged(boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public Boolean isResendNotification() {
		return isResendNotification;
	}

	public void setResendNotification(Boolean isResendNotification) {
		this.isResendNotification = isResendNotification;
	}
	
	
}
