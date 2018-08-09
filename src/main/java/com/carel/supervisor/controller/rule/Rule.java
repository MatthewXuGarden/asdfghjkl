package com.carel.supervisor.controller.rule;

import java.sql.Timestamp;

import com.carel.supervisor.action.AbstractAction;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.status.AbstractStatus;
import com.carel.supervisor.controller.status.ContextStatus;
import com.carel.supervisor.controller.status.SavedStatus;
import com.carel.supervisor.controller.status.StatusRegistry;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;


public class Rule
{
    protected Variable variable = null; //DIGITAL VARIABLE
    protected TimeValidity timeValidity = null;
    protected AbstractAction action = null;
    protected Integer idRule = null;
    private Integer frequency = null;
    protected ContextStatus contextStatus = new ContextStatus();
    protected AbstractStatus actualStatus = null;
    private Integer idSite = null;
    private Integer priority = null;

    public Rule(RuleBean ruleBean, Variable variable,
        TimeValidity timeValidity, AbstractAction action)
    {
        this.idRule = ruleBean.getIdRule();

        VariableInfo variableInfo = variable.getInfo();
        this.frequency = variableInfo.getFrequency();
        this.variable = variable;
        this.timeValidity = timeValidity;
        this.action = action;
        this.idSite = ruleBean.getIdSite();
        this.priority = variableInfo.getPriority();
        this.contextStatus.setDelay(ruleBean.getDelay());
        this.contextStatus.setIdRule(idRule);
        this.contextStatus.setIdVar(variableInfo.getId());
        this.actualStatus = StatusRegistry.invalidStatus;
    }

    public Rule(Integer idRule, int delay, Variable variable,
        TimeValidity timeValidity, AbstractAction action)
    {
        this.idRule = idRule;

        VariableInfo variableInfo = variable.getInfo();
        this.frequency = variableInfo.getFrequency();
        this.variable = variable;
        this.timeValidity = timeValidity;
        this.action = action;
        this.idSite = variableInfo.getSite();
        this.priority = variableInfo.getPriority();
        this.contextStatus.setDelay(delay);
        this.contextStatus.setIdRule(idRule);
        this.contextStatus.setIdVar(variableInfo.getId());
        this.actualStatus = StatusRegistry.invalidStatus;
    }

    public Rule(Integer idRule, Integer frequency, Integer idSite,
        Integer priority, int delay, Variable variable,
        TimeValidity timeValidity, AbstractAction action)
    {
        this.idRule = idRule;
        this.frequency = frequency;
        this.variable = variable;
        this.timeValidity = timeValidity;
        this.action = action;
        this.idSite = idSite;
        this.priority = priority;
        this.contextStatus.setDelay(delay);
        this.contextStatus.setIdRule(idRule);
        this.contextStatus.setIdVar(variable.getInfo().getId());
        this.actualStatus = StatusRegistry.invalidStatus;
    }

    public synchronized void executeActions(long actualTime)
        throws Exception
    {
    	if (!Float.isNaN(variable.getCurrentValue()))
    	{
	        boolean status = ((variable.getCurrentValue() == 1) ? true : false);
	        contextStatus.setStatus(status);
	        executeActionsInner(actualTime);
    	}
    }

    private void executeActionsInner(long actualTime) throws Exception
    {
    	contextStatus.setActualTime(actualTime);
        Timestamp now = new Timestamp(actualTime);
        boolean timebandOk = timeValidity.isValid(now);
    	contextStatus.setTimebandOk(timebandOk);
    	AbstractStatus nextStatus = null;
    	while (!actualStatus.equals(nextStatus))
    	{
    		if (null != nextStatus)
    		{
    			actualStatus = nextStatus;
    		}
    		nextStatus = actualStatus.next(contextStatus);
    		if (!nextStatus.equals(actualStatus))
            {
    			nextStatus.execute(this, now);	
            }
    	}
    }
    
    public void setPersistent(boolean persistent)
    {
    	contextStatus.setPersistent(persistent);
    }
    
    public AbstractAction getAction()
    {
        return action;
    }

    /**
     * @return: int
     */
    public Integer getFrequency()
    {
        return frequency;
    }

    /**
     * @return: int
     */
    public Integer getIdRule()
    {
        return idRule;
    }

    public Integer getIdVar()
    {
        return variable.getInfo().getId();
    }
    
    public Integer getIdDevice()
    {
    	return variable.getInfo().getDevInfo().getId();
    }

    public boolean isAlarm()
    {
        return (variable.getInfo().getType() == VariableInfo.TYPE_ALARM);
    }
    
    // Aggiunta funzione per gestione var logiche come allarme su Eventi
    public boolean isAlarmForActionLog()
    {
    	boolean ris = (variable.getInfo().getType() == VariableInfo.TYPE_ALARM);
    	if(!ris)
    		ris = variable.getInfo().isLogic();
        return ris;
    }
    
    public void manualAcknowledge() throws Exception
    {
        if (isAlarm())
        {
        	//kevin, 2014-8-26
        	//acknowledged, for resendNotification
        	contextStatus.setAcknowledged(true);
        }
    }
    
    public void manualCancel() throws Exception
    {
        if (isAlarm())
        {
            contextStatus.setManualCancel(true);
            contextStatus.setStatus(true);
            executeActionsInner(System.currentTimeMillis());
            contextStatus.setManualCancel(false);
        }
    }

    public void manualReset() throws Exception
    {
        if (isAlarm())
        {
            contextStatus.setManualReset(true);
            contextStatus.setStatus(true);
            executeActionsInner(System.currentTimeMillis());
            contextStatus.setManualReset(false);
        }
    }
    
    public void refreshStatus(SavedStatus savedState) throws Exception
    {
        this.contextStatus.setActivationTime(savedState.getActivationTime());
        actualStatus = savedState.getState();
        //kevin,2014-8-26
        //load cancelled or resetted status when PC restart, for resendNotification
        if(this.getClass().equals(Rule.class) && this.variable != null)
        {
        	String sql = "select count(*) as num from hsalarm where endtime is null and idvariable=? and ackuser is not null and acktime is not null";
        	Object[] params = new Object[]{this.getIdVar()};
        	 RecordSet rs = null;
             try
             {
                 rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
                 if(rs != null && rs.size()>0)
                 {
                	 int num = Integer.valueOf(rs.get(0).get(0).toString());
                	 if(num>0)
                		 contextStatus.setAcknowledged(true);
                 }
             }
             catch(Exception ex)
             {
            	 
             }
        }
        actualStatus.executeAfterRetrieve(this, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * @return: Integer
     */
    public Integer getPriority()
    {
        return priority;
    }

    /**
     * @return: Integer
     */
    public Integer getIdSite()
    {
        return idSite;
    }

    public long getStartTime()
    {
        return contextStatus.getActivationTime();
    }
    public ContextStatus getContextStatus() {
		return contextStatus;
	}

	public void setContextStatus(ContextStatus contextStatus) {
		this.contextStatus = contextStatus;
	}
}
