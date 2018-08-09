package com.carel.supervisor.controller.database;

import java.sql.Timestamp;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;



public class RuleBean
{
    private static final String IDRULE = "idrule";
    private static final String VARIABLE = "idcondition";
    private static final String IDTIMEBAND = "idtimeband";
    private static final String ACTIONCODE = "actioncode";
    private static final String RULECODE = "rulecode";
    private static final String DELAY = "delay";
    private static final String IDSITE = "idsite";
    private static final String ISENABLED = "isenabled";
    private Integer idRule = null;
    private Integer idVariable = null;
    private Integer idTimeband = null;
    private Integer actionCode = null;
    private String ruleCode = null;
    private int delay = 0;
    private Integer idsite = null;
    private String isenabled = null;

    public RuleBean()
    {
    	
    }
    
    public RuleBean(Record record)
    {
        idRule = (Integer) record.get(IDRULE);
        idsite = (Integer) record.get(IDSITE);
        idVariable = (Integer) record.get(VARIABLE);
        idTimeband = (Integer) record.get(IDTIMEBAND);
        actionCode = (Integer) record.get(ACTIONCODE);
        delay = ((Integer) record.get(DELAY)).intValue();
        ruleCode = UtilBean.trim(record.get(RULECODE));
        isenabled = UtilBean.trim(record.get(ISENABLED));
    }

    /**
	 * @return: String
	 */
	
	public String getIsenabled() {
		return isenabled;
	}

	/**
	 * @param isenabled
	 */
	public void setIsenabled(String isenabled) {
		this.isenabled = isenabled;
	}

	/**
     * @return: int
     */
    public Integer getIdCondition()
    {
        return idVariable;
    }

    /**
     * @param conditionCode
     */
    public void setIdCondition(Integer idVariable)
    {
        this.idVariable = idVariable;
    }

    /**
     * @return: int
     */
    public int getDelay()
    {
        return delay;
    }

    /**
     * @param delay
     */
    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    /**
     * @return: int
     */
    public Integer getActionCode()
    {
        return actionCode;
    }

    /**
     * @param idAction
     */
    public void setActionCode(Integer actionCode)
    {
        this.actionCode = actionCode;
    }

    /**
     * @return: int
     */
    public Integer getIdRule()
    {
        return idRule;
    }

    /**
     * @param idRule
     */
    public void setIdRule(Integer idRule)
    {
        this.idRule = idRule;
    }

    /**
     * @return: int
     */
    public Integer getIdTimeband()
    {
        return idTimeband;
    }

    /**
     * @param idTimeband
     */
    public void setIdTimeband(Integer idTimeband)
    {
        this.idTimeband = idTimeband;
    }

    /**
     * @return: String
     */
    public String getRuleCode()
    {
        return ruleCode;
    }

    /**
     * @param ruleCode
     */
    public void setRuleCode(String ruleCode)
    {
        this.ruleCode = ruleCode;
    }

    /**
     * @return: int
     */
    public Integer getIdSite()
    {
        return idsite;
    }
    
    
    
    /**
	 * @param idsite
	 */
	public void setIdsite(Integer idsite) {
		this.idsite = idsite;
	}

	

	public void saveRule() throws DataBaseException
    {
    	String sql = "insert into cfrule values (?,?,?,?,?,?,?,?,?,?)";
    	Object[] param = new Object[10];
    	SeqMgr o = SeqMgr.getInstance();
    	param[0] = o.next(null, "cfrule", "idrule");
    	param[1] = BaseConfig.getPlantId();
    	param[2] = idsite;
    	param[3] = ruleCode;
    	param[4] = idVariable;
    	param[5] = idTimeband;
    	param[6] = actionCode;
    	param[7] = new Integer(delay);
    	param[8] = isenabled;
    	param[9] = new Timestamp(System.currentTimeMillis());
    	
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    	
        DataHs dataHs= CreateSqlHs.getInsertData("cfrule",param);									
     		
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
    	
    }
}
