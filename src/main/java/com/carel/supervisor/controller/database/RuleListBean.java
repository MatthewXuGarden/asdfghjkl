package com.carel.supervisor.controller.database;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;

import java.util.*;


public class RuleListBean
{
    private List rules = new ArrayList();
    private Map ids = new HashMap();

    public RuleListBean() 
    {
    }
    
    public void loadAllRules(Integer idSite) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and idcondition is not null order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite});
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }
    
    public void loadEnabledRules(Integer idSite) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and isenabled = ? order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite, "TRUE" });
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }
    public void loadRulesByRulecode(Integer idSite,String rulecode) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and isenabled = ? and rulecode = ? order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite, "TRUE",rulecode });
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }
    public RuleBean loadWizardRulesByRulecode(Integer idSite,String rulecode) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and rulecode = ? order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite,rulecode });
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++) {
            record = recordSet.get(i);
            ruleBean = new RuleBean(record);
        }
        return ruleBean;
    }
    public void loadNoCondRules(Integer idSite) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and idcondition is null order by rulecode";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite});
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }
    

    public void loadEnabledCondRules(Integer idSite) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and idcondition is not null and isenabled = ? order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite, "TRUE"});
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }

    public void loadEnabledNoCondRules(Integer idSite) throws DataBaseException
    {
    	String sql = "select * from cfrule where idsite = ? and idcondition is null and isenabled = ? order by idrule";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite, "TRUE"});
        Record record = null;
        RuleBean ruleBean = null;
        
        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            ids.put(record.get("idrule"),new Integer(i));
            ruleBean = new RuleBean(record);
            rules.add(ruleBean);
        }
    }
   
    public RuleBean get(int pos)
    {
        return (RuleBean) rules.get(pos);
    }

    public int size()
    {
        return rules.size();
    }

    public static void deleteRule(int idsite, int idrule)
        throws DataBaseException
    {
        String sql = "delete from cfrule where idsite = ? and idrule = ?";
        
        DataHs dataHs= CreateSqlHs.getDeleteData("cfrule",new String[]{
        		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
        		"actioncode","delay","isenabled"
        },new Object[]{new Integer(idsite), new Integer(idrule)},new String[]{"=","="},new String[]{"idsite","idrule"});										
     		
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idrule) });
    }
    public static void deleteRule(int idsite, String rulecode) throws DataBaseException{
	    String sql = "delete from cfrule where idsite = ? and rulecode = ?";
	    
	    DataHs dataHs= CreateSqlHs.getDeleteData("cfrule",new String[]{
	    		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
	    		"actioncode","delay","isenabled"
	    },new Object[]{new Integer(idsite), rulecode},new String[]{"=","="},new String[]{"idsite","rulecode"});										
	 		
	    DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
	    
	    DatabaseMgr.getInstance().executeStatement(null, sql,
	        new Object[] { new Integer(idsite),rulecode });
	}
    public static void deleteRuleByActioncode(int idsite, int actioncode) throws DataBaseException{
        String sql = "delete from cfrule where idsite = ? and actioncode = ?";
        
        DataHs dataHs= CreateSqlHs.getDeleteData("cfrule",new String[]{
        		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
        		"actioncode","delay","isenabled"
        },new Object[]{new Integer(idsite), actioncode},new String[]{"=","="},new String[]{"idsite","actioncode"});										
     		
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite),actioncode });
    }
    
    public static void deleteRuleByIdcondition(int idsite, int idcondition) throws DataBaseException{
        String sql = "delete from cfrule where idsite = ? and idcondition = ?";
        
        DataHs dataHs= CreateSqlHs.getDeleteData("cfrule",new String[]{
        		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
        		"actioncode","delay","isenabled"
        },new Object[]{new Integer(idsite), idcondition},new String[]{"=","="},new String[]{"idsite","idcondition"});										
     		
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite),idcondition });
    }

    public static void updateRule(Integer idsite, Integer idrule, String isenabled, String description, Integer condition_code, Integer timeband_code,
        Integer action_code, Integer delay, String usr) throws DataBaseException
    {
        //rilevo stato precedente regola:
    	String wasEnabled = "";
    	String sql_ctrl = "select * from cfrule where idsite = ? and idrule = ?";
        Object[] par = new Object[]{idsite,idrule};
        
        try
        {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql_ctrl,par);
			wasEnabled = rs.get(0).get("isenabled").toString().trim();
		}
        catch (Exception e)
        {
			LoggerMgr.getLogger(RuleListBean.class).error(e.getMessage());
		}
    	
    	String sql = "update cfrule set isenabled=?,rulecode=?,idcondition=?,idtimeband=?," +
        		"actioncode=?,delay=? where idsite = ? and idrule = ?";
        Object[] param = new Object[8];
        param[0]= isenabled;
        param[1]= description;
        param[2]= condition_code;
        param[3]= timeband_code;
        param[4]= action_code;
        param[5]= delay;
        param[6]= idsite;
        param[7]= idrule;
        
        DatabaseMgr.getInstance().executeStatement(null,sql,param);
        
        DataHs dataHs= CreateSqlHs.getUpdateData("cfrule",new String[]{
        		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
        		"actioncode","delay","isenabled"
        },new Object[]{idsite, idrule},new String[]{"=","="},new String[]{"idsite","idrule"});
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        
        try
        {
	        if (!isenabled.equals(wasEnabled))
	        {
	        	//log x modifica abilitazione regola
	        	EventMgr.getInstance().info(new Integer(1),usr,"Config",(isenabled.equals("TRUE")?"W075":"W076"),description);
	        }
	        else
	        {
	        	//log x modifica generica regola
	        	EventMgr.getInstance().info(new Integer(1),usr,"Config","W077",description);
	        }
        }
        catch (Exception e)
        {
        	LoggerMgr.getLogger(RuleListBean.class).error(e.getMessage());
        }
    
    }
    
    public static void updateIdeRule(Integer idsite, Integer idrule, String isenabled) throws DataBaseException
        {
            String sql = "update cfrule set isenabled=? where idsite = ? and idrule = ?";
            Object[] param = new Object[3];
            param[0]= isenabled;
            param[1]= idsite;
            param[2]= idrule;
            
            DatabaseMgr.getInstance().executeStatement(null,sql,param);
        
            DataHs dataHs= CreateSqlHs.getUpdateData("cfrule",new String[]{
            		"idrule","pvcode","idsite","rulecode","idcondition","idtimeband",
            		"actioncode","delay","isenabled"
            },new Object[]{idsite, idrule},new String[]{"=","="},new String[]{"idsite","idrule"});
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
            
        }
    
    
	/*
     * Non utilizzato.
     * Sia il metodo che l'oggetto: ids
	 */
	public Map getIds() {
		return ids;
	}
}
