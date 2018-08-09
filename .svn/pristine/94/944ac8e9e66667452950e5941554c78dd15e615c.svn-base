package com.carel.supervisor.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.action.AbstractAction;
import com.carel.supervisor.action.ActionAnomaly;
import com.carel.supervisor.action.ActionFactory;
import com.carel.supervisor.action.ActionLogAlarm;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.database.ActionBean;
import com.carel.supervisor.controller.database.ActionList;
import com.carel.supervisor.controller.database.AlarmCarelLoader;
import com.carel.supervisor.controller.database.FunctionList;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.controller.function.CalcElementData;
import com.carel.supervisor.controller.function.Function;
import com.carel.supervisor.controller.function.FunctionMgr;
import com.carel.supervisor.controller.rule.AlarmRule;
import com.carel.supervisor.controller.rule.RemoteRule;
import com.carel.supervisor.controller.rule.RemoteRuleHelper;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.controller.rule.ScheduledRule;
import com.carel.supervisor.controller.status.SavedStatus;
import com.carel.supervisor.controller.time.AlwaysValidity;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.controller.time.TimeValidityFactory;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableLogicInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.HsRelayBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;


public class ControllerMgr implements IInitializable
{
    private static boolean initialized = false;
    private static boolean loaded = false;
    private static ControllerMgr me = new ControllerMgr();
    private static final Logger logger = LoggerMgr.getLogger(ControllerMgr.class);
    private HashMap<Integer,Function> functionsById = new HashMap<Integer,Function>();
    private HashMap<Integer,Function> functionsByCode = new HashMap<Integer,Function>();
    private VariablesAccess variablesAccess = null;

    private ControllerMgr()
    {
    }

    public static ControllerMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            initialized = true;
        }
    }

    public void clear()
    {
        functionsById.clear();
        functionsByCode.clear();
    }

    public void reset() throws Exception
    {
        RuleStateBean.removeAll();

        //Reset degli allarmi nella tabella di controllo
        AlarmCtrl.reset();

        String sql = "update hsalarm set endtime=current_timestamp,lastupdate=current_timestamp where idsite=1 and endtime is null";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        try
        {
        	HsRelayBean.getInstance().reset();
        }
        catch(Exception e){}
    }

    /*Funzione per il caricamento del Controller, richiamato dal Director.
      Vengono effettuati:
      1. dei controlli sul tempo con cui la macchina � rimasta spenta. Se superiore a 15 minuti (parametrizzabile)
         allora vengono resettati tutti gli allarmi
      2. Vengono caricate le funzioni
      3. Vengono caricate le regole con relative azioni e fasce temporali (effettuando operazioni diverse nel caso di remoto)
    */
    public synchronized void load() throws Exception
    {
        //Verifico da quanto tempo il motore � rimasto spento. Se meno di 15 minuti => ok, altrimenti tolgo le "strisciate rosse"
        //e pulisco la rulestate
        Timestamp time = LiveStatus.retrieve();


        if ((null == time) ||
                    ((System.currentTimeMillis() - time.getTime()) > ((BaseConfig.getLiveTime() * 1000L))))
            {
                try
                {
                    if (null != time)
                    {
                        //Segnalazione dell'avvenuto spegnimento prolungato della macchina
                        //in minuti
                        long timeShoutdown = ((System.currentTimeMillis() - time.getTime()) / 60000L);
                        EventMgr.getInstance().error(new Integer(1), "Controller", "Start", "S027",
                            new Long(timeShoutdown));
                    }

                    reset();
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
        }
        

        //Il Controller agisce principalmente sul locale => idsite sempre 1
        Integer idSite = new Integer(1);

        //-1-	    We load Physical and Logical  Variables that are polled from the field
        VariableInfo varInfo = null;
        variablesAccess = new VariablesAccess(FieldConnectorMgr.getInstance().getDataCollector());

        //Al remoto non servono le variabili logiche
        VariableLogicInfoList varLogicInfoList = new VariableLogicInfoList(null,
                BaseConfig.getPlantId());
        Function func = null;
        CalcElementData calcElementData = null;

        //-2-	    We load Functions used to evaluate Logical Variables
        FunctionList functionList = new FunctionList(null, BaseConfig.getPlantId(), idSite);

        FunctionMgr.getInstance().clearCache();

        for (int i = 0; i < varLogicInfoList.size(); i++)
        {
            try
            {
                varInfo = varLogicInfoList.get(i);
                calcElementData = functionList.get(varInfo.getFunctionCode());
                func = FunctionMgr.getInstance().create(calcElementData);
                functionsById.put(varInfo.getId(), func);
                variablesAccess.addFunction(varInfo.getId(), func);
                functionsByCode.put(varInfo.getFunctionCode(), func);
            }
            catch (Exception e) //Errore nella definizioen della funzione. Non deve essere bloccante!!
            {
                logger.error(e);
            }
        }

        //-3-	    We load Rules        
        RuleListBean ruleBeanList = new RuleListBean();
        ruleBeanList.loadEnabledRules(idSite);

        RuleBean ruleBean = null;

        //-4-	    Each Rule is connected with a Timeband
        TimeBandList timebandList = new TimeBandList(null, BaseConfig.getPlantId(), idSite);
        TimeBandBean timeBandBean = null;
        TimeValidity timeValidity = null;

        //-5-	    Each Rule is connected with an Action        
        ActionList actionList = new ActionList(null, BaseConfig.getPlantId(), idSite);
        ActionBean actionBean = null;
        AbstractAction action = null;

        RuleMgr ruleMgr = RuleMgr.getInstance();
        Rule rule = null;

        VariableMgr vrbMgr = VariableMgr.getInstance();
        Variable variable = null;

        Integer idVariable = null;

        for (int i = 0; i < ruleBeanList.size(); i++)
        {
            ruleBean = ruleBeanList.get(i);

            // Linking timeband
            if (0 == ruleBean.getIdTimeband().intValue())
            {
                timeValidity = new AlwaysValidity();
            }
            else
            {
                timeBandBean = timebandList.get(ruleBean.getIdTimeband());

                if (null == timeBandBean)
                {
                    timeValidity = new AlwaysValidity(); //Using default
                }
                else
                {
                    timeValidity = TimeValidityFactory.createTime(timeBandBean);
                }
            }

            // Linking actions
            actionBean = actionList.get(ruleBean.getActionCode());

            //If actionBean is null we log an error and we use a Special actionLogger
            if (null == actionBean)
            {
                EventMgr.getInstance().error(idSite, "Controller", "Config", "C002",
                    new Object[] { ruleBean.getActionCode() });
                action = new ActionAnomaly();
            }
            else
            {
                action = ActionFactory.createAction(actionBean);
            }

            //Each rule is connected with a variable
            //If the rule has no variable (means no condition) and only a timeband, we are considering a scheduled action
            Integer idCondition = ruleBean.getIdCondition();

            if (null == idCondition) //TimeScheduled
            {
                idVariable = new Integer(0);
                rule = new ScheduledRule(ruleBean, timeValidity, action);
                ruleMgr.addRule(rule);
            }
            else
            {
                ConditionBeanList conditionBeanList = new ConditionBeanList(1, null);

                //ConditionBean conditionBean = conditionBeanList.loadConditionController(idCondition.toString());
                Integer[] listaIdVar = conditionBeanList.loadConditionController(idCondition.toString());

                if (listaIdVar == null)
                {
                    if (0 != ruleBean.getIdTimeband().intValue())
                    {
                        idVariable = new Integer(0);
                    }

                    if (idVariable == null)
                    {
                    	rule = new ScheduledRule(ruleBean, timeValidity, action);
                        ruleMgr.addRule(rule);
                    }
                    else
                    {
                        variable = vrbMgr.getById(idVariable);

                        if (null == variable) //Come idCondition == 0
                        {
                            EventMgr.getInstance().error(idSite, "Controller", "Config", "C001",
                                new Object[] { idVariable, ruleBean.getIdRule() });
                        }
                        else
                        {
                            rule = new ScheduledRule(ruleBean, timeValidity, action);
                            ruleMgr.addRule(rule);
                        }
                    }
                }
                else
                {
                    for (int k = 0; k < listaIdVar.length; k++)
                    {
                        idVariable = listaIdVar[k];

                        variable = vrbMgr.getById(idVariable);
                        //Start alarmP 2007/06/28 Andrea 
                        String sql = "select idcondition,pvcode,idsite,condcode,condtype from cfcondition where idcondition=?";
                        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{idCondition});
                        String type="";
                        if(rs.size()>0)
                            type=(String)rs.get(0).get("condtype");
                        //End
                        
                        if (null == variable)
                        {
                            if(type.equals("P")){} //Start alarmP 2007/06/27 Andrea End
                            else   
                                EventMgr.getInstance().error(idSite, "Controller", "Config",
                                "C001", new Object[] { idVariable, ruleBean.getIdRule() });
                        }
                        else
                        {
                            rule = new Rule(ruleBean, variable, timeValidity, action);
                            ruleMgr.addRule(rule);
                        }
                    }
                }
            }
        }

        //Adding allarms to notify (LOG ON DB)
        List<Variable> allarms = AlarmCarelLoader.loadAllarm(null, BaseConfig.getPlantId(), idSite);

        timeValidity = new AlwaysValidity();
        action = new ActionLogAlarm();

        for (int i = 0; i < allarms.size(); i++)
        {
            //LDAC TO DO :Delay 0 for CAREL Protocol (ONLY) (valorizzare delay da cfvariable) 
            //In this case IdRule is equal to 0
            rule = new AlarmRule(new Integer(0), 0, (Variable) allarms.get(i), timeValidity,
                    action);

            //rule.setPersistent(false);
            ruleMgr.addRule(rule);
        }

        loaded = true;
    }

    //La macchina a stati viene refreshata dopo il restart della macchina
    public synchronized void refreshStatus() throws Exception
    {
        //      Load previous saved states
        List<SavedStatus> listSavedState = RuleStateBean.retrieveStatus();
        SavedStatus savedState = null;
        Integer idRule = null;
        Integer idVar = null;
        RuleMgr ruleMgr = RuleMgr.getInstance();
        Rule rule = null;

        for (int i = 0; i < listSavedState.size(); i++)
        {
            try
            {
                savedState = (SavedStatus) listSavedState.get(i);
                idRule = savedState.getIdRule();
                idVar = savedState.getIdVar();
                rule = ruleMgr.getRule(idVar, idRule);

                if (null != rule)
                {
                    rule.refreshStatus(savedState);
                }
                else
                {
                    RuleStateBean.remove(idVar, idRule);
                }
            }
            catch (Exception e) //Cerco di caricare tutto quello che posso
            {
                logger.error(e);
            }
        }
    }

    //Metodo di accesso per reperire il valore della variabile dal campo
    public Variable getFromField(VarphyBean varphyBean)
        throws Exception
    {
        return variablesAccess.getFromField(varphyBean, true);
    }

    public Variable getFromField(int idVariable) throws Exception
    {
        return variablesAccess.getFromField(idVariable);
    }
    
    public Variable getFromField(int idVariable,boolean isValueOfDimension) throws Exception
    {
        return variablesAccess.getFromField(idVariable,isValueOfDimension);
    }
    
    public Variable[] getFromField(int[] idVariable) throws Exception
    {
        return variablesAccess.getFromField(idVariable);
    }
    
    public Variable[] getFromFieldWithDuplicates(int[] idVariable) throws Exception
    {
        return variablesAccess.getFromFieldWithDuplicates(idVariable);
    }
    
    public Variable retrieve(int idVariable) throws Exception
    {
        return variablesAccess.retrieve(idVariable);
    }

    public Variable retrieve(VariableInfo varInfo) throws Exception
    {
    	return variablesAccess.getFromField(varInfo);
    }

	public void refreshValue(Variable variable) throws Exception
    {
    	variablesAccess.refreshValue(variable);
    }
    
    public Variable[] retrieve(int[] idVariable) throws Exception
    {
        return variablesAccess.retrieve(idVariable);
    }
    
    public Map<Integer,Function> getFunctions()
    {
        return functionsById;
    }

    public synchronized boolean isLoaded()
    {
        return loaded;
    }

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
        ControllerMgr.getInstance().load();
    }
   
}
