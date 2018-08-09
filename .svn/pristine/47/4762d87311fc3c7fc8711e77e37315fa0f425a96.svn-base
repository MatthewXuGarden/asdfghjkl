package com.carel.supervisor.presentation.bo.helper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.ac.AcMaster;
import com.carel.supervisor.presentation.ac.AcSlave;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.GroupBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.groupmng.GroupManager;

public class VarDependencyCheck {
	
	private final static int HISTORY_CASE = 1;
	private final static int ACTION_CASE = 2;
	private final static int ALRM_CONDITION_CASE = 3;
	private final static int EVN_CONDITION_CASE = 4;
	private final static int GRAPHCONF_CASE = 5;
	private final static int REPORT_CASE = 6;
	private final static int LOGIC_VAR_CASE = 7;
	private final static int LOGIC_DEV_CASE = 8;
	private final static int GUARDIAN_CASE = 9;
	private final static int REMOTE_CASE = 10;
	private final static int BOOKLET_CASE = 11;
	private final static int PARAM_CONTROL_CASE = 12;
	private final static int LN_CONFIG_CASE = 13;
	private final static int LN_REMOTECOMMAND_CASE = 14;
	private final static int KPI_CONFIG_CASE = 15;
	private final static int DEWPOINT_CONFIG_CASE = 16;
	private final static int ENERGY_CONFIG_CASE = 17;
	private final static int ENERGY_CONFIG_Meter_Models_CASE = 18;
	
	
	public VarDependencyCheck()
	{
	}	
	
	public static VarDependencyState  checkVariablesInHistory(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;

		//query on 'cfvariable' table
		String sql = "select idvariable from cfvariable where idsite = ? and ((idhsvariable != ? and idhsvariable is not null) or (ishaccp = ?))";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(-1), "TRUE"});
		
		Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
		        if (hashset.contains(new Integer(ids_variables[i])))
		    	{
		    		depState.setDependence(true);
		    		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, HISTORY_CASE));
		    	}
        }
        
		return depState; 
	}
	
	public static VarDependencyState checkVariablesInAction(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;

        // query on 'cfaction' table
		// get all actions with type v (Variable)
		//String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and (actiontype = 'L' or actiontype = 'V')";
		String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and actiontype = 'V'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        String[] params = null;
        int tmp_id_1 = 0;
        int tmp_id_2 = 0;
        String actcode = "";

        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            actcode = rs.get(i).get("code").toString();

            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                
                tmp_id_1 = Integer.parseInt(parameters[0]);
                
                // if action involves that the variable value to set is taken from another variable
                // there are two parameters to consider
                if(parameters[1].contains("id"))
                {
                	tmp_id_2 = Integer.parseInt(params[j].split("=")[1].replace("id", ""));
                }
                // find match with action parameters and ids_variables
                for (int z = 0; z < ids_variables.length; z++)
                {
                    if (tmp_id_1 == ids_variables[z])
                    {
                        depState.setDependence(true);
                        depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id_1, langcode, new Object[] {actcode}, ACTION_CASE));
                    }
                    if (tmp_id_2 != 0 && tmp_id_2 == ids_variables[z])
                    {
                        depState.setDependence(true);
                        depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id_2, langcode, new Object[] {actcode}, ACTION_CASE));
                    }
                }
            }
        }
        
        // get all actions with type L (Relay)
		sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and actiontype = 'L'";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                tmp_id_1 = Integer.parseInt(parameters[0]);
                tmp_id_2 = getVarIdFromRelayId(idsite, tmp_id_1);
                
                for (int z = 0; z < ids_variables.length; z++)
                {
                    if (tmp_id_2 == ids_variables[z])
                    {
                        depState.setDependence(true);
                        depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id_2, langcode, new Object[] {actcode}, ACTION_CASE));
                    }
                }
            }
            
        }
        
        return depState; 
	}
	
	public static void dltVariablesInAction(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws DataBaseException {
		
		if(ids_variables.length == 0)
			return;

        // query on 'cfaction' table
		// get all actions with type v (Variable)
		//String sql = "select code,parameters from cfaction where idsite = ? and idaction <> 1 and (actiontype = 'L' or actiontype = 'V')";
		String sql = "select idaction,actioncode,parameters from cfaction where idsite = ? and idaction <> 1 and actiontype = 'V'";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        String[] params = null;
        String params4Update = "";
        int tmp_id_1 = 0;
        int tmp_id_2 = 0;
        int actid;
        int actcode;

        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            
            
            actid = ( (Integer)rs.get(i).get("idaction")).intValue();
            actcode = ( (Integer)rs.get(i).get("actioncode")).intValue();
            boolean dtl = false;
            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                
                tmp_id_1 = Integer.parseInt(parameters[0]);
                
                // if action involves that the variable value to set is taken from another variable
                // there are two parameters to consider
                if(parameters[1].contains("id"))
                {
                	tmp_id_2 = Integer.parseInt(params[j].split("=")[1].replace("id", ""));
                }
                // find match with action parameters and ids_variables
                for (int z = 0; z < ids_variables.length; z++)
                {
                    if (tmp_id_1 == ids_variables[z]  || (tmp_id_2 != 0 && tmp_id_2 == ids_variables[z])  )
                    {
                    	dtl = true;
                    	params[j] = "";
                    }
                }
            }
            for (int j = 0; j < params.length; j++){
            	if(params[j].trim() != ""){
            		params4Update += params[j]+";";
            	}
            }
            if(dtl){
            	ActionBeanList abl = new ActionBeanList();
            	try {
            		if(params4Update.trim() == ""){
	            		RuleListBean.deleteRuleByActioncode(1,actcode);
	            		abl.replaceActionWithXaction(1, actcode);
            		}
            		else{
            			abl.updateParametersOfAction(1, actid, params4Update);
            		}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        
        // get all actions with type L (Relay)
		sql = "select idaction,parameters,actioncode from cfaction where idsite = ? and idaction <> 1 and actiontype = 'L'";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite)});
        //cycle on records
        for (int i = 0; i < rs.size(); i++)
        {
            params = rs.get(i).get("parameters").toString().split(";");
            actid = ( (Integer)rs.get(i).get("idaction")).intValue();
            actcode = ( (Integer)rs.get(i).get("actioncode")).intValue();
            params4Update = "";
            boolean dtl = false;
            //cycle on params
            for (int j = 0; j < params.length; j++)
            {
                String[] parameters = params[j].split("=");
                tmp_id_1 = Integer.parseInt(parameters[0]);
                tmp_id_2 = getVarIdFromRelayId(idsite, tmp_id_1);
                
                for (int z = 0; z < ids_variables.length; z++)
                {
                    if (tmp_id_2 == ids_variables[z])
                    {
                    	dtl =true;
                    	params[j] = "";
                    }
                }
            }
            for (int j = 0; j < params.length; j++){
            	if(params[j].trim() != ""){
            		params4Update += params[j]+";";
            	}
            }
            if(dtl){
            	ActionBeanList abl = new ActionBeanList();
            	try {
            		if(params4Update.trim() == ""){
	            		RuleListBean.deleteRuleByActioncode(1,actcode);
	            		abl.replaceActionWithXaction(1, actcode);
            		}
            		else{
            			abl.updateParametersOfAction(1, actid, params4Update);
            		}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
        }
	}

	private static int getVarIdFromRelayId(int idsite, int idrelay) throws DataBaseException
	{
		String sql = "select idvariable from cfrelay where idsite = ? and idrelay = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(idrelay)});
        if( rs.size() == 1 )
        	return Integer.parseInt(rs.get(0).get("idvariable").toString());
        else
        	return -1;
	}
	
	public static VarDependencyState checkVariablesInAlarmCondition(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		 
		//query on 'cfvarcondition' and 'cfcondition' tables
		String sql = "select cfvarcondition.idvariable,cfcondition.condcode from cfvarcondition,cfcondition where cfvarcondition.idsite = ? " +
	    	"and cfcondition.idsite = cfvarcondition.idsite and cfcondition.idcondition=cfvarcondition.idcondition and cfcondition.condType!=?";

        // action type P is excluded from search
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite),"P" });
        
		int tmp_id = 0;
        String condcode = "";

        for (int i = 0; i < rs.size(); i++)
        {
            tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();
            condcode = rs.get(i).get("condcode").toString();

            for (int j = 0; j < ids_variables.length; j++)
            {
                if (tmp_id == ids_variables[j])
                {
                	 depState.setDependence(true);
                     depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {condcode}, ALRM_CONDITION_CASE));
                }
            }
        }
        
        return depState;
	}
	
	public static void dltVariablesInAlarmCondition(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		 
		//query on 'cfvarcondition' and 'cfcondition' tables
		String sql = "select cfvarcondition.idvariable,cfcondition.idcondition from cfvarcondition,cfcondition where cfvarcondition.idsite = ? " +
	    	"and cfcondition.idsite = cfvarcondition.idsite and cfcondition.idcondition=cfvarcondition.idcondition and cfcondition.condType!=?";

		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite),"P" });
        
		int tmp_id = 0;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();

            for (int j = 0; j < ids_variables.length; j++)
            {
                if (tmp_id == ids_variables[j])
                {
                	String sql2 = "delete from cfvarcondition where idvariable=" +tmp_id;
                    DatabaseMgr.getInstance().executeStatement(null, sql2, null);
                }
            }
            
        }
        
	}
	
	public static VarDependencyState checkVariablesInEventCondition(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		
		//query on 'cfvariable' and 'cffunction'tables
		String sql = "select cfvariable.idvariable, cfvariable.iddevice, cfvariable.functioncode, cfvariable.code, cffunction.parameters from cfvariable, cffunction " + 
		"where cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.code = ? and cfvariable.iscancelled = ? and cfvariable.functioncode = cffunction.functioncode";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(0), "LOGIC_CONDITION", "FALSE"});
		
		int tmp_id = 0;
		String tmpstring = "";
		String condcode = "";
		String[] tmpparams;
		
		for (int i = 0; i < rs.size(); i++)
		{
			tmpstring = (String) rs.get(i).get("parameters");
			tmpparams = tmpstring.split(";");
			
			for(int j=0; j<tmpparams.length; j++)
			{
				if(tmpparams[j].contains("pk"))
				{
					
					tmp_id = getMainVariableID(Integer.parseInt((tmpparams[j].replace("pk", ""))));
					
					for (int z = 0; z < ids_variables.length; z++)
					{
						if (tmp_id == ids_variables[z])
						{
							//get the condition description
							int varid = ((Integer)rs.get(i).get("idvariable"));
							sql = "select cfcondition.condcode from cfcondition, cfvarcondition where idvariable = ? and cfcondition.idcondition = cfvarcondition.idcondition and cfcondition.idsite = ?";
							RecordSet record = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(varid), new Integer(1)});
							
							if(record.size() > 0)
							{
								condcode = (String)record.get(0).get("condcode");
							}
							
							depState.setDependence(true);
							depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {condcode}, EVN_CONDITION_CASE));
						}
					}
				}	
			}	
		}
		
		return depState;
	}
	
	public static void dltVariablesInEventCondition(int idsite, int[] ids_variables, String langcode,  int msgSectNumber) throws Exception
	{
		
		if(ids_variables.length == 0)
			return ;
		
		
		//query on 'cfvariable' and 'cffunction'tables
		String sql = "select cfvariable.idvariable, cfvariable.iddevice, cfvariable.functioncode, cfvariable.code, cffunction.parameters from cfvariable, cffunction " + 
		"where cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.code = ? and cfvariable.iscancelled = ? and cfvariable.functioncode = cffunction.functioncode";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite), new Integer(0), "LOGIC_CONDITION", "FALSE"});
		
		int tmp_id = 0;
		String tmpstring = "";
		int idcondition ;
		String[] tmpparams;
		ConditionBeanList condBean = new ConditionBeanList(idsite, langcode);
		for (int i = 0; i < rs.size(); i++)
		{
			tmpstring = (String) rs.get(i).get("parameters");
			tmpparams = tmpstring.split(";");
			
			for(int j=0; j<tmpparams.length; j++)
			{
				if(tmpparams[j].contains("pk"))
				{
					
					tmp_id = getMainVariableID(Integer.parseInt((tmpparams[j].replace("pk", ""))));
					
					for (int z = 0; z < ids_variables.length; z++)
					{
						if (tmp_id == ids_variables[z])
						{
							//get the condition description
							int varid = ((Integer)rs.get(i).get("idvariable")).intValue();
							sql = "select cfcondition.idcondition from cfcondition, cfvarcondition where idvariable = ? and cfcondition.idcondition = cfvarcondition.idcondition and cfcondition.idsite = ?";
							RecordSet records = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(varid), new Integer(1)});
							
							if(records.size() > 0)
							{
								idcondition = ((Integer)records.get(i).get("idcondition")).intValue();
								RuleListBean.deleteRuleByIdcondition(1,idcondition);
								condBean.deleteGeneralCondition(idcondition);
							}
						}
					}
				}	
			}	
		}
		
	}
	
	public static VarDependencyState checkVariablesInGraph(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		String sql = "select idvariable1, idvariable2, idvariable3, idvariable4, idvariable5, idvariable6, idvariable7, idvariable8, idvariable9, idvariable10, idvariable11, idvariable12, idvariable13, idvariable14, idvariable15, idvariable16, idvariable17, idvariable18, idvariable19, idvariable20 from cfpagegraph";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		Set<Integer> hashset = new HashSet<Integer>();
		Integer idvar =  null;
		for (int i = 0; i < rs.size(); i++) 
        {
			for (int j = 0; j < 20; j++)
			{
				
				if((Integer)rs.get(i).get(j)==0)
					break;
				else
					idvar = (Integer)rs.get(i).get(j);
				
				hashset.add(idvar);
			}
        }
		
		Iterator<Integer> iter = hashset.iterator();
		sql = "select idvariable from cfvariable where idhsvariable in (";
		
		while (iter.hasNext())
		{
			sql += iter.next().toString() + ",";
		}
		
		sql = sql.substring(0, sql.length() -1);
		sql += ")";
		
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		hashset = new HashSet<Integer>();
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, GRAPHCONF_CASE));
        	}
        }
        
		return depState;
		
	}
	
	public static VarDependencyState checkVariablesInInstantReport(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;

		if(ids_variables.length == 0)
			return depState;
		
		//query on 'cfreportdetail' and 'cfreportkernel' tables
		String sql = "select cfreportdetail.idreport,cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel where cfreportkernel.idreport = cfreportdetail.idreport";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        String repcode = "";
        
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = (Integer) rs.get(i).get("idvariable");
        	repcode = rs.get(i).get("code").toString();
        	
        	//cycle on all variables
        	for (int j = 0; j < ids_variables.length; j++) 
            {
            	if(tmp_id == ids_variables[j])
            	{
            		depState.setDependence(true);
            		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {repcode}, REPORT_CASE));
            	}
            }
        }
        
        return depState;
	}
	public static void dltVariablesInInstantReport(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws Exception
	{

		if(ids_variables.length == 0)
			return;
		String sql = "select cfreportdetail.idreport,cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel where cfreportkernel.idreport = cfreportdetail.idreport";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        int idreport;
        ArrayList<String> idvarrpts = new ArrayList<String>();
        HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();
        	idreport = ((Integer)rs.get(i).get("idreport")).intValue();
        	if(!map.containsKey(idreport)){
        		ArrayList<Integer> tal = new ArrayList<Integer>();
        		tal.add(tmp_id);
        		map.put(idreport,tal);
        	}else{
        		ArrayList<Integer> temp =map.get(idreport);
        		temp.add(tmp_id);
        		map.put(idreport, temp);
        	}
        	for (int j = 0; j < ids_variables.length; j++) 
            {
            	if(tmp_id == ids_variables[j])
            	{
            		idvarrpts.add(tmp_id+";"+idreport);
            	}
            }
        }
        ArrayList<Integer> idvarlist = new ArrayList<Integer>();
        for (String idvarrpt : idvarrpts) {
			String sqldlt = "delete from cfreportdetail where idreport = ? and idvariable = ?";
			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] { idvarrpt.split(";")[1], idvarrpt.split(";")[0] });
			idvarlist.add(Integer.parseInt(idvarrpt.split(";")[0]));
		}
        Iterator ite = map.keySet().iterator();
        while(ite.hasNext()){
        	idreport = (Integer)ite.next();
        	ArrayList<Integer> t = map.get(idreport);
        	if(t.size()<=idvarlist.size() && idvarlist.size()>0){
        		boolean b = true;
        		for (int idvar : t) {
					if(!idvarlist.contains(idvar)){
						b = false;
					}
				}
        		if(b){
        			String sqlactionid = "select actioncode from cfaction where actiontype=?  and template=?";
        			RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sqlactionid, new Object[] { "P",  "" + idreport });
                	int actcode;
                	for(int j=0;j<rs2.size();j++){
                		actcode =( (Integer) rs2.get(j).get("actioncode") ).intValue();
                		RuleListBean.deleteRuleByActioncode(1,actcode);
                    	ActionBeanList abl = new ActionBeanList();
        				abl.deleteAllActionByActioncode(1,actcode);
                	}
                	String sqldltrpt = "delete from cfreportkernel where idreport = ?";
                	DatabaseMgr.getInstance().executeStatement(null,sqldltrpt,new Object[] { idreport });
            	}
        	}
        }
	}
	
	public static VarDependencyState checkVariablesInHistorReport(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;

		if(ids_variables.length == 0)
			return depState;
		
		String sql = "select idhsvariable from cfvariable where idvariable in (";
		for(int i = 0; i < ids_variables.length; i++)
		{
			sql += ids_variables[i]+",";
		}
		
		sql = sql.substring(0,sql.length() -1);
		
		sql += ") and idhsvariable != -1 and idhsvariable is not null";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		int [] ids_hist = new  int[rs.size()];
		for(int i = 0; i < rs.size(); i++)
		{
			ids_hist[i] = (Integer)rs.get(i).get(0);
		}
		
		//query on 'cfreportdetail' and 'cfreportkernel' tables
		sql = "select cfreportdetail.idreport,cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel where cfreportkernel.idreport = cfreportdetail.idreport";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        String repcode = "";
        
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = (Integer) rs.get(i).get("idvariable");
        	repcode = rs.get(i).get("code").toString();
        	
        	//cycle on all variables
        	for (int j = 0; j < ids_hist.length; j++) 
            {
            	if(tmp_id == ids_hist[j])
            	{
            		depState.setDependence(true);
            		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {repcode}, REPORT_CASE));
            	}
            }
        }
        
        return depState;
	}
	
	public static void dltVariablesInHistorReport(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws Exception
	{

		if(ids_variables.length == 0)
			return ;
		
		String sql = "select idhsvariable from cfvariable where idvariable in (";
		for(int i = 0; i < ids_variables.length; i++)
		{
			sql += ids_variables[i]+",";
		}
		
		sql = sql.substring(0,sql.length() -1);
		
		sql += ") and idhsvariable != -1 and idhsvariable is not null";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		int [] ids_hist = new  int[rs.size()];
		for(int i = 0; i < rs.size(); i++)
		{
			ids_hist[i] = (Integer)rs.get(i).get(0);
		}
		
		//query on 'cfreportdetail' and 'cfreportkernel' tables
		sql = "select cfreportdetail.idreport,cfreportdetail.idvariable, cfreportkernel.code from cfreportdetail, cfreportkernel where cfreportkernel.idreport = cfreportdetail.idreport";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);

        int tmp_id = 0;
        int idreport;
        ArrayList<String> idvarrpts = new ArrayList<String>();
        HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	tmp_id = ((Integer) rs.get(i).get("idvariable")).intValue();
        	idreport = ((Integer)rs.get(i).get("idreport")).intValue();
        	for (int j = 0; j < ids_hist.length; j++) 
            {
            	if(tmp_id == ids_hist[j])
            	{
            		idvarrpts.add(tmp_id+";"+idreport);
            	}
            }
        	if(!map.containsKey(idreport)){
        		ArrayList<Integer> tal = new ArrayList<Integer>();
        		tal.add(tmp_id);
        		map.put(idreport,tal);
        	}else{
        		ArrayList<Integer> temp =map.get(idreport);
        		temp.add(tmp_id);
        		map.put(idreport, temp);
        	}
        	ArrayList<Integer> idvarlist = new ArrayList<Integer>();
            for (String idvarrpt : idvarrpts) {
    			String sqldlt = "delete from cfreportdetail where idreport = ? and idvariable = ?";
    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] { idvarrpt.split(";")[1], idvarrpt.split(";")[0] });
    			idvarlist.add(Integer.parseInt(idvarrpt.split(";")[0]));
    		}
            Iterator ite = map.keySet().iterator();
            while(ite.hasNext()){
            	idreport = (Integer)ite.next();
            	ArrayList<Integer> t = map.get(idreport);
            	if(t.size()<=idvarlist.size() && idvarlist.size()>0){
            		boolean b = true;
            		for (int idvar : t) {
    					if(!idvarlist.contains(idvar)){
    						b = false;
    					}
    				}
            		if(b){
            			String sqlactionid = "select actioncode from cfaction where actiontype=?  and template=?";
            			RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sqlactionid, new Object[] { "P",  "" + idreport });
                    	int actcode;
                    	for(int j=0;j<rs2.size();j++){
                    		actcode =( (Integer) rs2.get(j).get("actioncode") ).intValue();
                    		RuleListBean.deleteRuleByActioncode(1,actcode);
                        	ActionBeanList abl = new ActionBeanList();
            				abl.deleteAllActionByActioncode(1,actcode);
                    	}
                    	String sqldltrpt = "delete from cfreportkernel where idreport = ?";
                    	DatabaseMgr.getInstance().executeStatement(null,sqldltrpt,new Object[] { idreport });
                	}
            	}
            }
        }
        
	}
	
	public static VarDependencyState checkVariablesInLogicVar(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;

		//query on 'cfvariable' and 'cffunction' tables
		String sql =  "select cffunction.parameters, cfvariable.code from cffunction inner join cfvariable on cfvariable.iscancelled = ? " + 
			"and cfvariable.functioncode is not null and cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", "LOGIC_CONDITION", new Integer(idsite)});
     
        int tmp_id = 0;
        String logicId = "";
        
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	logicId = (String)rs.get(i).get("code");
        	String[] params = ((String)rs.get(i).get("parameters")).split(";");
        	
        	for(int j=0; j<params.length; j++)
        	{
        		if(((String)params[j]).startsWith("pk"))
        		{
        			tmp_id = Integer.parseInt(params[j].replace("pk", ""));
            		
            		for (int z = 0; z < ids_variables.length; z++)
                    {
                        if (tmp_id == ids_variables[z])
                        {
                        	depState.setDependence(true);
                    		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {logicId}, LOGIC_VAR_CASE));
                        }
                    }
        		}
        	}
        }
        
        return depState;
	}
	
	public static void dltVariablesInLogicVar(int idsite, int[] ids_variables, String langcode,  int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;

		//query on 'cfvariable' and 'cffunction' tables
		String sql =  "select cffunction.parameters, cfvariable.idvariable , cfvariable.iddevice ,cfvariable.functioncode from cffunction inner join cfvariable on cfvariable.iscancelled = ? " + 
			"and cfvariable.functioncode is not null and cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", "LOGIC_CONDITION", new Integer(idsite)});
        int tmp_id = 0;
        int iddevice ;
        int idvariable ;
        int functioncode;
        
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	iddevice = ((Integer)rs.get(i).get("iddevice")).intValue();
        	idvariable = ((Integer)rs.get(i).get("idvariable")).intValue();
        	functioncode = ((Integer)rs.get(i).get("functioncode")).intValue();
        	String[] params = ((String)rs.get(i).get("parameters")).split(";");
        	for(int j=0; j<params.length; j++)
        	{
        		if(((String)params[j]).startsWith("pk"))
        		{
        			tmp_id = Integer.parseInt(params[j].replace("pk", ""));
            		
            		for (int z = 0; z < ids_variables.length; z++)
                    {
                        if (tmp_id == ids_variables[z])
                        {
                        	if(iddevice!=0){  // if it belongs to logic device, remove it  from logic device.
                        		StringBuffer sqldltdev = new StringBuffer();
                     	        sqldltdev.append("UPDATE cfvariable set iddevice = NULL where iddevice=? and idvarmdl is  null and idsite=1");
                     	        Object[] objects = new Object[]{new Integer(iddevice)};
                     	        DatabaseMgr.getInstance().executeStatement(null, sqldltdev.toString(), objects);
                        	}
                        	String sqldltvar ="UPDATE cfvariable SET iscancelled=? WHERE idvariable=? and idsite=1";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{"TRUE",idvariable});
                        	sqldltvar = "UPDATE cfvariable SET iscancelled=? WHERE idvariable=(SELECT idhsvariable from cfvariable WHERE idvariable =? and idsite=1)";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{"TRUE",idvariable});
                        	sqldltvar = "delete from cffunction where functioncode = ?";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{functioncode});
                        }
                    }
        		}
        		
        	}
        	
        	// if the device and logic group exit and are empty , 'remove' them
        	String sql2 = "select idvariable from cfvariable where iddevice = ? and iscancelled = 'FALSE' and idsite = 1 "; 
 	        RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2, new Object[]{new Integer(iddevice)});
 	        if(rs2.size()<1){
 	        	sql2 = "update cfdevice set iscancelled = 'TRUE' where iddevice = ? and idsite = 1";
 	        	DatabaseMgr.getInstance().executeStatement(null, sql2, new Object[]{new Integer(iddevice)});
 	        }
 	        GroupManager groupMg = new GroupManager();
            GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,langcode);
            for (int k = 0; k < groups.length; k++)
            {
                if (groupMg.numOfDeviceOfGroup(idsite, groups[k].getGroupId()) == 0)
                {
                    groupMg.removeEmptyGroup(idsite, groups[k].getGroupId());
                }
            }
        }
        
	}
	
	public static VarDependencyState checkVariablesInLogicDev(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//query on 'cfvariable' and 'cffunction' tables
		String sql =  "select cffunction.parameters, cfvariable.code, cfvariable.iddevice from cffunction inner join cfvariable on cfvariable.iscancelled = ? " + 
			"and cfvariable.functioncode is not null and cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is not null";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { "FALSE", "LOGIC_CONDITION", new Integer(idsite)});
     
        int tmp_id = 0;
        String logicDevId = "";
        
        //cycle on records 
        for (int i = 0; i < rs.size(); i++)
        {
        	logicDevId = ((Integer)rs.get(i).get("iddevice")).toString();
        	String[] params = ((String)rs.get(i).get("parameters")).split(";");
        	
        	for(int j=0; j<params.length; j++)
        	{
        		if(((String)params[j]).startsWith("pk")){
        			tmp_id = Integer.parseInt(params[j].replace("pk", ""));
            		
            		for (int z = 0; z < ids_variables.length; z++)
                    {
                        if (tmp_id == ids_variables[z])
                        {
                        	depState.setDependence(true);
                    		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, tmp_id, langcode, new Object[] {logicDevId}, LOGIC_DEV_CASE));
                        }
                    }
        		}
        		
        	}
        }
		
		return depState;
	}
	public static void dltVariablesInLogicDev(int idsite, int[] ids_variables,
			String langcode, int msgSectNumber) throws DataBaseException {
		if (ids_variables.length == 0)
			return;
		// query on 'cfvariable' and 'cffunction' tables
		String sql = "select cffunction.parameters, cfvariable.idvariable, cfvariable.functioncode,cfvariable.iddevice from cffunction inner join cfvariable on cfvariable.iscancelled = ? "
				+ "and cfvariable.functioncode is not null and cfvariable.functioncode = cffunction.functioncode and cfvariable.code != ? and cfvariable.idsite = ? and cfvariable.idvarmdl is not null";

		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[] { "FALSE", "LOGIC_CONDITION",new Integer(idsite) });

		int tmp_id = 0;
		int functioncode;
		int idvariable;
		int iddevice;

		// cycle on records
		for (int i = 0; i < rs.size(); i++) {
			functioncode = ((Integer) rs.get(i).get("functioncode")).intValue();
			idvariable = ((Integer) rs.get(i).get("idvariable")).intValue();
			iddevice = ((Integer) rs.get(i).get("iddevice")).intValue();
			String[] params = ((String) rs.get(i).get("parameters")).split(";");
			for (int j = 0; j < params.length; j++) {
				if (((String) params[j]).startsWith("pk")) {
					tmp_id = Integer.parseInt(params[j].replace("pk", ""));
					for (int z = 0; z < ids_variables.length; z++) {
						if (tmp_id == ids_variables[z]) {
							String sqldltvar = "delete from cffunction where functioncode = ?";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{functioncode});
                        	sqldltvar ="UPDATE cfvariable SET iscancelled=? WHERE idvariable=? and idsite=1";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{"TRUE",idvariable});
                        	sqldltvar = "UPDATE cfvariable SET iscancelled=? WHERE idvariable=(SELECT idhsvariable from cfvariable WHERE idvariable =? and idsite=1)";
                        	DatabaseMgr.getInstance().executeStatement(null, sqldltvar, new Object[]{"TRUE",idvariable});
						}
					}
				}
			}
			// if the device and logic group exit and are empty , 'remove' them
        	String sql2 = "select idvariable from cfvariable where iddevice = ? and iscancelled = 'FALSE' and idsite = 1 "; 
 	        RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2, new Object[]{new Integer(iddevice)});
 	        if(rs2.size()  <1){
 	        	sql2 = "update cfdevice set iscancelled = 'TRUE' where iddevice = ? and idsite = 1";
 	        	DatabaseMgr.getInstance().executeStatement(null, sql2, new Object[]{new Integer(iddevice)});
 	        }
 	        GroupManager groupMg = new GroupManager();
            GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,langcode);
            for (int k = 0; k < groups.length; k++)
            {
                if (groupMg.numOfDeviceOfGroup(idsite, groups[k].getGroupId()) == 0)
                {
                    groupMg.removeEmptyGroup(idsite, groups[k].getGroupId());
                }
            }
		}
	}
		
	
	public static VarDependencyState checkVariablesInGuardian(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//query on 'cfguardian' table
		String sql = "select idvariable from cfvarguardian";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, GUARDIAN_CASE));
        	}
        }

		return depState;
	}
	
	public static void dltVariablesInGuardian(int idsite, int[] ids_variables, String langcode,  int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		//query on 'cfguardian' table
		String sql = "select idvariable ,iddevice from cfvarguardian";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        String idvar;
        String iddev;
        HashMap<String,String> hashMap = new HashMap<String,String>();
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = rs.get(i).get("idvariable").toString();
        	iddev = rs.get(i).get("iddevice").toString();
        	hashMap.put(idvar, iddev);
        }
        
        for(int j = 0; j < ids_variables.length; j++)
        {
        	iddev = hashMap.get(""+ids_variables[j]);
    		if (iddev != null)
        	{
    			String sqldlt = "delete from cfvarguardian where idvariable =? and iddevice = ? ";
    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {""+ids_variables[j],iddev });
        	}
        }
    	

	}
	
	public static VarDependencyState checkVariablesInRemote(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//query on 'cfvariable' and 'cftransfervar' tables
		String sql = "select idvariable from cfvariable where idhsvariable in (select idvar from cftransfervar)";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, REMOTE_CASE));
        	}
        }

		return depState;
				
	}
	
	public static void dltVariablesInRemote(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		//query on 'cfvariable' and 'cftransfervar' tables
		String sql = "select idhsvariable,idvariable from cfvariable where idhsvariable in (select idvar from cftransfervar)";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        HashMap<String,String> hashMap = new HashMap<String,String>();
        String idvar =  null;
        String idhsvar = null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idhsvar = rs.get(i).get(0).toString();
        	idvar = rs.get(i).get(1).toString();
        	hashMap.put(idvar, idhsvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	idhsvar = hashMap.get(""+ids_variables[i]);
        	if (idhsvar!=null )
        	{
        		String sqldlt = "delete from cftransfervar where idvar =? and idsite = ? ";
    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {idhsvar ,idsite});
        	}
        }

        
        
	}
	
	public static VarDependencyState checkVariablesInBooklet(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//query on 'bookletdevvar' table
		String sql = "select idvariable from bookletdevvar";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, BOOKLET_CASE));
        	}
        }
		
		return depState;
	}
	public static void dltVariablesInBooklet(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		//query on 'bookletdevvar' table
		String sql = "select iddevice ,idvariable from bookletdevvar";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        String idvar;
        String iddev;
        HashMap<String,String> hashMap = new HashMap<String,String>();
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = rs.get(i).get("idvariable").toString();
        	iddev = rs.get(i).get("iddevice").toString();
        	hashMap.put(idvar, iddev);
        }
        
        for(int j = 0; j < ids_variables.length; j++)
        {
        	iddev = hashMap.get(""+ids_variables[j]);
    		if (iddev != null)
        	{
    			String sqldlt = "delete from bookletdevvar where idvariable =? and iddevice = ? and idsite =?";
    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {ids_variables[j],iddev, new Integer(idsite) });
        	}
        }
        
		
	}
	
	public static VarDependencyState checkVariablesInParametersControl(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//query on 'parameters_variable' table
		String sql = "select idvariable from parameters_variable";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null, PARAM_CONTROL_CASE));
        	}
        }
		
		return depState;
	}
	
	public static void dltVariablesInParametersControl(int idsite, int[] ids_variables, String langcode,  int msgSectNumber) throws DataBaseException
	{
		if(ids_variables.length == 0)
			return ;
		
		//query on 'parameters_variable' table
		String sql = "select idvariable from parameters_variable";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        Set<Integer> hashset = new HashSet<Integer>();
        Integer idvar =  null;
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
        
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		String sqldlt = "delete from parameters_variable where idvariable =?  ";
    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {ids_variables[i]});
        	}
        }
		
	}
	
	public static VarDependencyState checkVariablesInOpt(int idsite,int[] ids_variables, String langcode ,VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException {
		VarDependencyState depState = varsDependsOn;
		if(ids_variables.length == 0)
			return depState;
		
		String sql = "select name , value from opt_startstop_settings where name like 'Var%' ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        int tmp_id = 0;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	for(int j =0;j<ids_variables.length ;j++){
        		if(tmp_id ==ids_variables[j] ){
        			depState.setDependence(true);
            	}
        	}
        }
        // opt_nightfreecooling_settings
        sql = "select name , value from opt_nightfreecooling_settings where name like 'Var%' ";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	for(int j =0;j<ids_variables.length ;j++){
        		if(tmp_id ==ids_variables[j] ){
        			depState.setDependence(true);
            	}
        	}
        }
        // opt_lights_settings
        sql = "select name , value from opt_lights_settings where name like 'Var%' ";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer) Integer.parseInt((String)rs.get(i).get("value") );
        	for(int j =0;j<ids_variables.length ;j++){
        		if(tmp_id ==ids_variables[j] ){
        			depState.setDependence(true);
            	}
        	}
        }
        return depState;
	}
	
	
	public static VarDependencyState checkVariablesInTechSwitch(int idsite,int[] ids_variables, String langcode ,VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException {
		VarDependencyState depState = varsDependsOn;
		if(ids_variables.length == 0)
			return depState;
		
		String sql = "select idvariable from cfvariable where "
			+ "iddevice in (select iddevice from switchdev) and idvarmdl in("
			+ "select distinct idvarmdl from switchvar where iddevmdl in "
			+ "(select distinct iddevmdl from cfdevice where iddevice in (select iddevice from switchdev))"
			+ ") order by priority;";			
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        int tmp_id = 0;
        ArrayList<VarDpdHelper> list = new ArrayList<VarDpdHelper>(); 
        for (int i = 0; i < rs.size(); i++)
        {	
        	tmp_id = (Integer)rs.get(i).get("idvariable");
        	for(int j =0;j<ids_variables.length ;j++){
        		if(tmp_id ==ids_variables[j] ){
        			depState.setDependence(true);
            	}
        	}
        }
        return depState;
	}

	
	public static VarDependencyState checkVariablesInFSP(int idsite,int[] ids_variables, String langcode ,VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException {
		VarDependencyState depState = varsDependsOn;
		if(ids_variables.length == 0)
			return depState;
		
        // racks
        String sql = "select setpoint from fsrack where idrack > 0;";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {	
        	Integer tmp_id = ((Float)rs.get(i).get("setpoint")).intValue();
        	for(int j = 0; j<ids_variables.length; j++) {
        		if( tmp_id == ids_variables[j] ) {
        			depState.setDependence(true);
        			break;
        		}
        	}
        }
        
        // utilities
        sql = "select solenoid from fsutil;";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Integer tmp_id = Integer.parseInt(rs.get(i).get("solenoid").toString());
        	for(int j = 0; j<ids_variables.length; j++) {
        		if( tmp_id == ids_variables[j] ) {
        			depState.setDependence(true);
        			break;
        		}
        	}
        }
        
        return depState;
	}

	public static VarDependencyState checkVariablesInModbusSlave(int idsite,int[] ids_variables, String langcode,VarDependencyState depState, int msgSectNumber) throws DataBaseException  {
		
		if(ids_variables.length == 0){
			return depState;
		}
		
		StringBuffer bs = new StringBuffer();
		for(int i =0;i<ids_variables.length;i++){
			bs.append(ids_variables[i]).append(",");
		}
		
		String sql = "select * from mbslavevar where mbslavevar.idvariable in ("+bs.toString().substring(0, bs.length()-1)+")";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,null);
		if(rs.size()>0){
			depState.setDependence(true);
		}
		return depState;
	}
	
	
	public static VarDependencyState checkVariablesInCO2(int idsite,int[] ids_variables, String langcode ,VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException {
		VarDependencyState depState = varsDependsOn;
		if(ids_variables.length == 0)
			return depState;
		
        // racks
        String sql = "select idvariable, idbackupdevice, idbackupvariable, offline, (select idvariable from cfvariable where cfvariable.iddevice = co2_rack.idbackupdevice and cfvariable.code='OFFLINE') AS idoffline from co2_rack;";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	int idVariable = (Integer)r.get("idvariable");
        	for(int j = 0; j < ids_variables.length && !depState.dependsOn() ; j++)
        		if( idVariable == ids_variables[j] )
        			depState.setDependence(true);
        	int idBackupDevice = (Integer)r.get("idbackupdevice");
        	if( idBackupDevice > 0 ) {
        		boolean bOffline = (Boolean)r.get("offline");
        		if( bOffline ) {
        			int idOfflineVariable = (Integer)r.get("idoffline");
                	for(int j = 0; j < ids_variables.length && !depState.dependsOn() ; j++)
                		if( idOfflineVariable == ids_variables[j] )
                			depState.setDependence(true);
        		}
        		else {
        			int idBackupVariable = (Integer)r.get("idbackupvariable");
                	for(int j = 0; j < ids_variables.length && !depState.dependsOn() ; j++)
                		if( idBackupVariable == ids_variables[j] )
                			depState.setDependence(true);
        		}
        	}
        }
        
        // utilities
        sql = "select iddevice from co2_grouputilities;";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	int idDevice = (Integer)r.get("iddevice");
        	String sqlMdl = "SELECT * FROM co2_devmdl WHERE devcode="
    			+ "(SELECT code FROM cfdevmdl WHERE iddevmdl = (SELECT iddevmdl FROM cfdevice WHERE iddevice=?))"
    			+ " AND israck=FALSE;";
    		String sqlVar = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code=?"
    			+ " ORDER BY idvariable;";
			RecordSet rsMdl = DatabaseMgr.getInstance().executeQuery(null, sqlMdl, new Object[] { idDevice });
			if( rsMdl.size() > 0 ) {
				Record rMdl = rsMdl.get(0);
				// 1st variable
				String varCode = rMdl.get("var1").toString();
				RecordSet rsVar = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
				if( rsVar.size() > 0 ) {
					int idVariable1 = (Integer)rsVar.get(0).get(0);
                	for(int j = 0; j < ids_variables.length && !depState.dependsOn() ; j++)
                		if( idVariable1 == ids_variables[j] )
                			depState.setDependence(true);
				}
				// 2nd variable
				varCode = (String)rMdl.get("var4");
				if( varCode != null ) {
					rsVar = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
					if( rs.size() > 0 ) {
						int idVariable2 = (Integer)rsVar.get(0).get(0);
	                	for(int j = 0; j < ids_variables.length && !depState.dependsOn() ; j++)
	                		if( idVariable2 == ids_variables[j] )
	                			depState.setDependence(true);
					}
				}
			}
        }
        
        return depState;
	}

	
	public static VarDependencyState checkVariablesInEnergy(int idsite, int[] ids_variables, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		// query on 'energyactive' table
		String sql = "select idgroup, idvariable from energyactive";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		List<Integer> list = null; 
		Integer idvar = null;
		
		for (int i = 0; i < rs.size(); i++)
		{
			idvar = (Integer)rs.get(i).get(1);
			
			for(int j = 0; j < ids_variables.length; j++)
	        {
				
				if(ids_variables[j] == idvar)
				{
					int groupid = (Integer)rs.get(i).get(0);
					String groupname = ""; 
					if(groupid != -1)
					{
						String query = "select name from energygroup where idgroup = ?";
						RecordSet result = DatabaseMgr.getInstance().executeQuery(null, query, new Object[] { new Integer(groupid)});
					
						if(result.size() > 0)
						{
							groupname = (String)result.get(0).get(0);
						}
					}
					
					depState.setDependence(true);
		    		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[j], langcode, new Object[] {groupname} , ENERGY_CONFIG_CASE));
				}
	        }
		}
		
		sql = "select idgroup, idkw, idkwh from energyconsumer";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		for (int i = 0; i < rs.size(); i++)
		{
			list = new ArrayList<Integer>();
			
			if((Integer)rs.get(i).get(1) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(1));
				
				if(idvar != -1)
					list.add(idvar);
			}
			
			if((Integer)rs.get(i).get(2) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(2));
				
				if(idvar != -1)
					list.add(idvar);
			}
		
			for(int j = 0; j < ids_variables.length; j++)
	        {
				if(list.contains(ids_variables[j]))
				{
					int groupid = (Integer)rs.get(i).get(0);
					String groupname = ""; 
					if(groupid != -1)
					{
						String query = "select name from energygroup where idgroup = ?";
						RecordSet result = DatabaseMgr.getInstance().executeQuery(null, query, new Object[] { new Integer(groupid)});
					
						if(result.size() > 0)
						{
							groupname = (String)result.get(0).get(0);
						}
					}
					
					depState.setDependence(true);
		    		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[j], langcode, new Object[] {groupname} , ENERGY_CONFIG_CASE));
				}
	        }
		}		
		
		return depState;
	}
	
	public static void dltVariablesInEnergy(int idsite, int[] ids_variables, String langcode, int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		// query on 'energyactive' table
		String sql = "select idgroup, idvariable from energyactive";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		List<Integer> list = null; 
		Integer idvar = null;
		
		for (int i = 0; i < rs.size(); i++)
		{
			idvar = (Integer)rs.get(i).get(1);
			
			for(int j = 0; j < ids_variables.length; j++)
	        {
				
				if(ids_variables[j] == idvar)
				{
					int groupid = (Integer)rs.get(i).get(0);
					String groupname = ""; 
					if(groupid != -1)
					{
						String sqldlt = "delete from energyactive where idgroup =? and idvariable = ? ";
						DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {groupid,idvar});
					}
				}
	        }
		}
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer> ();
		sql = "select idgroup, idkw, idkwh from energyconsumer";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		Integer idkw = null;
		for (int i = 0; i < rs.size(); i++)
		{
			
			if((Integer)rs.get(i).get(1) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(1));
				idkw = (Integer)rs.get(i).get(1);
				
				if(idvar != -1)
					map.put(idvar,idkw);
			}
			
			if((Integer)rs.get(i).get(2) != null)
			{
				idvar = getMainVariableID((Integer)rs.get(i).get(1));
				idkw = (Integer)rs.get(i).get(2);
				
				if(idvar != -1)
					map.put(idvar,idkw);
			}
		
			for(int j = 0; j < ids_variables.length; j++)
	        {
				idkw = (Integer)map.get(ids_variables[j]);
				if(idkw!=null)
				{
					int groupid = (Integer)rs.get(i).get(0);
					if(groupid != -1)
					{
						String sqldlt = "delete from energyconsumer where idgroup =? and ( idkw = ? or idkwh = ? ) ";
		    			DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {groupid,idkw,idkw});
					}
					
				}
	        }
		}	
		
		sql="delete from energygroup where name = ''";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql=" delete from energyconsumer where idgroup not in ( "
			+" select energyconsumer.idgroup from energyconsumer, energygroup "
			+" where energyconsumer.idgroup = energygroup.idgroup "
			+" )";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql=" delete from energyactive where idgroup not in ( "
			+" select energyactive.idgroup from energyactive, energygroup "
			+" where energyactive.idgroup = energygroup.idgroup "
			+" )";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql = "delete from energyconsumer where (name = '' or enabled is null or idkw is null or idkwh is null)  and idgroup <> -1 and idconsumer <> -1";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql = "delete from energyactive where idvariable < 0";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql = "delete from energylog where idvariable not in (select idkwh from energyconsumer);";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		sql = "delete from energylogsupport where idvariable not in (select idkwh from energyconsumer);";
		DatabaseMgr.getInstance().executeStatement(sql, null);
		
	}
	
	public static VarDependencyState checkVariablesInLightsOnOff(int idsite, int[] ids_variables, int iddevmdl, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//check if the model is used in lightsOnOff configuration
		//query on ln_varmdl' table
		
		String sql = "select idvariable from cfvariable where iddevice in (select iddev from ln_devlcnt where iddev in (select iddevice from cfdevice where iddevmdl = ?)) and idvarmdl in (select idvarmdl from ln_varmdl where iddevmdl = ?) and idhsvariable is not null";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {new Integer(iddevmdl), new Integer(iddevmdl)});
		Set<Integer> hashset = new HashSet<Integer>();
		Integer idvar =  null;
		for (int i = 0; i < rs.size(); i++) 
        {
			idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
		
		for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null , LN_CONFIG_CASE));
        	}
        }
		
        //check if an instantiated variable is used in 'remote commands' section
        //query on 'ln_fieldvars' table
        sql = "select idvardig from ln_fieldvars";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        hashset = new HashSet<Integer>();
        
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
		
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null , LN_REMOTECOMMAND_CASE));
        	}
        }

		return depState;
	}
	
	public static void dltVariablesInLightsOnOff(int idsite, int[] ids_variables, int iddevmdl, String langcode, int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		//check if the model is used in lightsOnOff configuration
		//query on ln_varmdl' table
		
		String sql = "select idvariable,iddevice from cfvariable where iddevice in (select iddev from ln_devlcnt where iddev in (select iddevice from cfdevice where iddevmdl = ?)) and idvarmdl in (select idvarmdl from ln_varmdl where iddevmdl = ?) and idhsvariable is not null";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {new Integer(iddevmdl), new Integer(iddevmdl)});
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		Integer idvar =  null;
		Integer iddev = null;
		for (int i = 0; i < rs.size(); i++) 
        {
			idvar = (Integer)rs.get(i).get(0);
			iddev = (Integer)rs.get(i).get(1);
            map.put(idvar,iddev);
        }
		
		for(int i = 0; i < ids_variables.length; i++)
        {
			iddev = (Integer)map.get(ids_variables[i]);
        	if (iddev!=null)
        	{
        		String sqldlt = "delete from ln_devlcnt where iddev = "+iddev;
        		DatabaseMgr.getInstance().executeQuery(null, sqldlt);
        	}
        }
		
        //check if an instantiated variable is used in 'remote commands' section
        //query on 'ln_fieldvars' table
        sql = "select idvardig from ln_fieldvars";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        HashSet<Integer> hashset = new HashSet<Integer>();
        
        for (int i = 0; i < rs.size(); i++) 
        {
        	idvar = (Integer)rs.get(i).get(0);
            hashset.add(idvar);
        }
		
        for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		sql ="delete from ln_fieldvars where idvardig = "+ids_variables[i]; 
        		DatabaseMgr.getInstance().executeQuery(null, sql);
        	}
        }

	}
	
	public static VarDependencyState checkVariablesInKPI(int idsite, int[] ids_variables, int iddevmdl, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		//get all idvarmdls related to ids_variables
		//List<Integer> idvarmdlList = getVarmdlList(ids_variables);
		List<Integer> varconfids = new ArrayList<Integer>();
		
		String sql = "select mastervarmdl, defvarmdl, solenoidvarmdl from kpiconf where iddevmdl = "+iddevmdl;
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		for (int i = 0; i < rs.size(); i++) 
        {
			Integer idvarmdl1 = (Integer)rs.get(i).get("mastervarmdl");
			if(idvarmdl1 != null && !idvarmdl1.equals("") && !varconfids.contains(idvarmdl1))
				varconfids.add(idvarmdl1);
			
			Integer idvarmdl2 = (Integer)rs.get(i).get("defvarmdl"); 
			if(idvarmdl2 != null && !idvarmdl2.equals("") && !varconfids.contains(idvarmdl2))
				varconfids.add(idvarmdl2);
			
			Integer idvarmdl3 = (Integer)rs.get(i).get("solenoidvarmdl");
			if(idvarmdl3 != null && !idvarmdl3.equals("") && !varconfids.contains(idvarmdl3))
				varconfids.add(idvarmdl3);
        }
		
		
		sql = "select iddevice from kpidevices where iddevice in (select iddevice from cfdevice where iddevmdl = "+iddevmdl+")";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		if( varconfids.size() > 0 && rs.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for (int i = 0; i < rs.size(); i++) 
	        {
				Integer iddevice = ((Integer)rs.get(0).get(0));
				sql += iddevice+",";
	        }
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < varconfids.size(); i++)
			{
				sql += varconfids.get(i)+",";
			}
			sql = sql.substring(0, sql.length() -1);
			sql += ") and idhsvariable is not null";
			
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			Set<Integer> hashset = new HashSet<Integer>();
			Integer idvar =  null;
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
	            hashset.add(idvar);
	        }
			
			for(int i = 0; i < ids_variables.length; i++)
	        {
	        	if (hashset.contains(new Integer(ids_variables[i])))
	        	{
	        		depState.setDependence(true);
	        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null , KPI_CONFIG_CASE));
	        	}
	        }
		}

		return depState;
	}
	
	
	public static void dltVariablesInKPI(int idsite, int[] ids_variables, int iddevmdl, String langcode,  int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		//get all idvarmdls related to ids_variables
		//List<Integer> idvarmdlList = getVarmdlList(ids_variables);
		List<Integer> varconfids = new ArrayList<Integer>();
		
		String sql = "select mastervarmdl, defvarmdl, solenoidvarmdl ,idgrp from kpiconf where iddevmdl = "+iddevmdl;
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		for (int i = 0; i < rs.size(); i++) 
        {
			Integer idvarmdl1 = (Integer)rs.get(i).get("mastervarmdl");
			if(idvarmdl1 != null && !idvarmdl1.equals("") && !varconfids.contains(idvarmdl1))
				varconfids.add(idvarmdl1);
			
			Integer idvarmdl2 = (Integer)rs.get(i).get("defvarmdl"); 
			if(idvarmdl2 != null && !idvarmdl2.equals("") && !varconfids.contains(idvarmdl2))
				varconfids.add(idvarmdl2);
			
			Integer idvarmdl3 = (Integer)rs.get(i).get("solenoidvarmdl");
			if(idvarmdl3 != null && !idvarmdl3.equals("") && !varconfids.contains(idvarmdl3))
				varconfids.add(idvarmdl3);
        }
		
		
		sql = "select iddevice from kpidevices where iddevice in (select iddevice from cfdevice where iddevmdl = "+iddevmdl+")";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		if( varconfids.size() > 0 && rs.size() > 0)
		{
			sql = "select idvariable , iddevice from cfvariable where iddevice in (";
			for (int i = 0; i < rs.size(); i++) 
	        {
				Integer iddevice = ((Integer)rs.get(0).get(0));
				sql += iddevice+",";
	        }
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < varconfids.size(); i++)
			{
				sql += varconfids.get(i)+",";
			}
			sql = sql.substring(0, sql.length() -1);
			sql += ") and idhsvariable is not null";
			
			RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql);
			HashMap <Integer,Integer>  map = new HashMap <Integer,Integer> ();
			Integer idvar =  null;
			Integer iddev = null;
			for (int i = 0; i < rs2.size(); i++) 
	        {
				idvar = (Integer)rs2.get(i).get(0);
				iddev = (Integer)rs2.get(i).get(1);
				map.put(idvar, iddev);
	        }
			
			for(int i = 0; i < ids_variables.length; i++)
	        {
				iddev = map.get(new Integer(ids_variables[i]));
	        	if (iddev!=null )
	        	{
	        		String sqldlt = "delete from kpidevices where iddevice = ? ";
	        		DatabaseMgr.getInstance().executeStatement(null,sqldlt,new Object[] {iddev});
	        	}
	        }
		}
		
		DatabaseMgr.getInstance().executeStatement("delete "+
		"from kpiconf "+
		"where kpiconf.iddevmdl not in "+
		"( "+
		"select distinct iddevmdl from cfdevice where iddevice in "+
		"( "+
		"select iddevice from kpidevices "+
		") "+
		") " +
		"or kpiconf.idgrp not in (select idgrp from kpigroups)",
		null);

	}
	
	public static VarDependencyState checkVariablesInDewPoint(int idsite, int[] ids_variables, int iddevmdl, String langcode, VarDependencyState varsDependsOn, int msgSectNumber) throws DataBaseException
	{
		VarDependencyState depState = varsDependsOn;
		
		if(ids_variables.length == 0)
			return depState;
		
		DevMdlBeanList devmdlbean = new DevMdlBeanList();
		DevMdlBean devmdl = devmdlbean.retrieveById(idsite, langcode, iddevmdl);
		String devmdlcode =  devmdl.getCode();
		
		//query on 'ac_master_mdl', 'ac_extra_vars', 'ac_slave_mdl', 'ac_heartbit_mdl' tables
		//get codes of all varmdl involved on DewPoint configuration 
		
		String sql = "select vcode from ac_master_mdl where code = ? group by vcode";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {devmdlcode});
		ArrayList<String> slaveVarCodeList = new ArrayList<String>();
		
		ArrayList<String> masterVarCodeList = new ArrayList<String>();
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
		}
		
		sql = "select varcode from ac_extra_vars where devcode = ? group by varcode";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
				
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
		sql = "select vcode from ac_slave_mdl where code = ? group by vcode";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
		
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
				
		sql = "select digvar, vcode from ac_heartbit_mdl where code = ?";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
		
		if(rs != null && rs.size() > 0)
		{
			String devcode = (String)rs.get(0).get(0);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
			devcode = (String)rs.get(0).get(1);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
		}
		
		ArrayList<Integer> masterVarmdlIdsList = getVarmdlIds(masterVarCodeList, iddevmdl);
		ArrayList<Integer> slaveVarmdlIdsList = getVarmdlIds(slaveVarCodeList, iddevmdl);
		
		sql = "select iddevmaster from ac_master where iddevmaster in (select iddevice from cfdevice where iddevmdl = "+ iddevmdl +") group by iddevmaster";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> masterDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			masterDevId.add((Integer)rs.get(i).get(0));
		}
		
		sql = "select iddevslave from ac_slave where iddevslave in (select iddevice from cfdevice where iddevmdl = "+ iddevmdl +") group by iddevslave";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> slaveDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			slaveDevId.add((Integer)rs.get(i).get(0));
		}
		
		Set<Integer> hashset = new HashSet<Integer>();
		Integer idvar =  null;
		
		if(masterDevId.size() > 0 && masterVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < masterDevId.size(); i++)
			{
				sql += masterDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < masterVarmdlIdsList.size(); i++)
			{
				sql += masterVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
	            hashset.add(idvar);
	        }
		}
		
		if(slaveDevId.size() > 0 && slaveVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < slaveDevId.size(); i++)
			{
				sql += slaveDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < slaveVarmdlIdsList.size(); i++)
			{
				sql += slaveVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
	            hashset.add(idvar);
	        }
		}
		
		for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		depState.setDependence(true);
        		depState.addMessage(msgSectNumber, getDependenceMessage(idsite, ids_variables[i], langcode, null , DEWPOINT_CONFIG_CASE));
        	}
        }
		
		return depState;
	}
	
	public static void dltVariablesInDewPoint(int idsite, int[] ids_variables, int iddevmdl, String langcode,  int msgSectNumber) throws DataBaseException
	{
		
		if(ids_variables.length == 0)
			return ;
		
		DevMdlBeanList devmdlbean = new DevMdlBeanList();
		DevMdlBean devmdl = devmdlbean.retrieveById(idsite, langcode, iddevmdl);
		String devmdlcode =  devmdl.getCode();
		
		//query on 'ac_master_mdl', 'ac_extra_vars', 'ac_slave_mdl', 'ac_heartbit_mdl' tables
		//get codes of all varmdl involved on DewPoint configuration 
		
		String sql = "select vcode from ac_master_mdl where code = ? group by vcode";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {devmdlcode});
		ArrayList<String> slaveVarCodeList = new ArrayList<String>();
		
		ArrayList<String> masterVarCodeList = new ArrayList<String>();
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
		}
		
		sql = "select varcode from ac_extra_vars where devcode = ? group by varcode";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
				
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
		sql = "select vcode from ac_slave_mdl where code = ? group by vcode";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
		
		for (int i = 0; i < rs.size(); i++)
		{
			String devcode = (String)rs.get(i).get(0);
			if(!slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
		}
		
				
		sql = "select digvar, vcode from ac_heartbit_mdl where code = ?";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,  new Object[] {devmdlcode});
		
		if(rs != null && rs.size() > 0)
		{
			String devcode = (String)rs.get(0).get(0);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
			devcode = (String)rs.get(0).get(1);
			if(devcode != null && !masterVarCodeList.contains(devcode))
				masterVarCodeList.add(devcode);
			if(devcode != null && !slaveVarCodeList.contains(devcode))
				slaveVarCodeList.add(devcode);
			
		}
		
		ArrayList<Integer> masterVarmdlIdsList = getVarmdlIds(masterVarCodeList, iddevmdl);
		ArrayList<Integer> slaveVarmdlIdsList = getVarmdlIds(slaveVarCodeList, iddevmdl);
		
		sql = "select iddevmaster from ac_master where iddevmaster in (select iddevice from cfdevice where iddevmdl = "+ iddevmdl +") group by iddevmaster";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> masterDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			masterDevId.add((Integer)rs.get(i).get(0));
		}
		
		sql = "select iddevslave from ac_slave where iddevslave in (select iddevice from cfdevice where iddevmdl = "+ iddevmdl +") group by iddevslave";
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		ArrayList<Integer> slaveDevId = new ArrayList<Integer>();
		for(int i = 0; i < rs.size(); i++)
		{
			slaveDevId.add((Integer)rs.get(i).get(0));
		}
		
		Set<Integer> hashset = new HashSet<Integer>();
		Integer idvar =  null;
		
		if(masterDevId.size() > 0 && masterVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < masterDevId.size(); i++)
			{
				sql += masterDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < masterVarmdlIdsList.size(); i++)
			{
				sql += masterVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
	            hashset.add(idvar);
	        }
		}
		
		if(slaveDevId.size() > 0 && slaveVarmdlIdsList.size() > 0)
		{
			sql = "select idvariable from cfvariable where iddevice in (";
			for(int i = 0; i < slaveDevId.size(); i++)
			{
				sql += slaveDevId.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idvarmdl in (";
			
			for(int i = 0; i < slaveVarmdlIdsList.size(); i++)
			{
				sql += slaveVarmdlIdsList.get(i)+",";
			}
			
			sql = sql.substring(0,sql.length()-1);
			sql += ") and idhsvariable is not null";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			for (int i = 0; i < rs.size(); i++) 
	        {
				idvar = (Integer)rs.get(i).get(0);
	            hashset.add(idvar);
	        }
		}
		
		for(int i = 0; i < ids_variables.length; i++)
        {
        	if (hashset.contains(new Integer(ids_variables[i])))
        	{
        		String sqldlt = "select idvarmdl , iddevice from cfvariable where idvariable = " +ids_variables[i] ;
        		rs = DatabaseMgr.getInstance().executeQuery(null, sqldlt);
//        		String idvarmdl = null;
        		String iddevice = null;
        		for(int j = 0; j < rs.size(); j++)
        		{
        			iddevice = rs.get(j).get(1).toString();
        			AcSlave.removeSlave(Integer.parseInt(iddevice));
        			AcMaster.removeMaster(Integer.parseInt(iddevice));
        			
        		}
        	}
        }
	}
	
	private static String getDependenceMessage(int idsite, int var_id, String langcode, Object[] params, int dependCase)throws DataBaseException
	{                  //getDependenceMessage(idsite, tmp_id_2, langcode, new Object[] {actcode}, ACTION_CASE)
		 String message = "";
		
		 VarphyBean var = null;
		 DeviceBean dev = null;
		 String var_desc = "";
		 String dev_desc = "";
		 String line_desc = "";
		 String LDVids = "";
		 if(var_id != -1)
		 {
			var = VarphyBeanList.retrieveVarById(idsite,var_id,langcode);  
			dev = DeviceListBean.retrieveSingleDeviceById(idsite, var.getDevice().intValue(), langcode); 
			var_desc = var.getShortDescription(); 
			dev_desc = dev.getDescription();
			int iddev = dev.getIddevice();
			int idline = dev.getIdline();
			LDVids = idline+"-"+iddev+"-"+var_id+"-"+dev.getCode();
			int length = StringUtility.split(dev.getCode(), ".").length;
			//Kevin: only for non-logical device
			if(length>1)
			{
				line_desc = StringUtility.split(dev.getCode(), ".")[0];
			}
		 }
	
	     LangService lang_s = LangMgr.getInstance().getLangService(langcode);
	     
	     switch(dependCase)
	     {
	     	case HISTORY_CASE:
	     		message = lang_s.getString("importctrl", "history");
		   	    message = message.replace("$1", var_desc);
		   	    message = message.replace("$2", dev_desc);
		   	    message = message.replace("$3", line_desc);
	     		break;
	     	case ACTION_CASE:
	     		message = lang_s.getString("importctrl", "action");
		   	    message = message.replace("$1", var_desc);
		   	    message = message.replace("$2", dev_desc);
		   	    message = message.replace("$3", line_desc);
		   	    message = message.replace("$4", (String)params[0]);
	     		break;
	     	case ALRM_CONDITION_CASE:
	     		message = lang_s.getString("importctrl", "alrcondition");
		   	    message = message.replace("$1", var_desc);
		   	    message = message.replace("$2", dev_desc);
		   	    message = message.replace("$3", line_desc);
		   	    message = message.replace("$4", (String)params[0]);
	     		break;
	     	case EVN_CONDITION_CASE:
	     		message = lang_s.getString("importctrl", "evncondition");
		   	    message = message.replace("$1", var_desc);
		   	    message = message.replace("$2", dev_desc);
		   	    message = message.replace("$3", line_desc);
		   	    message = message.replace("$4", (String)params[0]);
	     		break;
	     	case GRAPHCONF_CASE:
	             message = lang_s.getString("importctrl", "graphconf");
	             message = message.replace("$1", var_desc);
	             message = message.replace("$2", dev_desc);
	             message = message.replace("$3", line_desc);
	     		break;
	     	case REPORT_CASE:
	             message = lang_s.getString("importctrl", "report");
	             message = message.replace("$1", var_desc);
	             message = message.replace("$2", dev_desc);
	             message = message.replace("$3", line_desc);
	             message = message.replace("$4", (String)params[0]);
	     		break;
	     	case LOGIC_VAR_CASE:
	     		String var_logic_desc = VarphyBeanList.getShortDescriptionOfVars(idsite,langcode,Integer.parseInt((String)params[0]));
	     		message = lang_s.getString("importctrl", "logicvar");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	            message = message.replace("$4", var_logic_desc);
	     		break;
	     	case LOGIC_DEV_CASE:
	     		DeviceBean logicDev = DeviceListBean.retrieveSingleDeviceById(idsite, Integer.parseInt((String)params[0]), langcode);
	     		String logicDev_descr = logicDev.getDescription();
	     		message = lang_s.getString("importctrl", "logicdev");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	            message = message.replace("$4", logicDev_descr);
	     		break;
	     	case GUARDIAN_CASE:
	     		message = lang_s.getString("importctrl", "guardian");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case REMOTE_CASE:
	     		message = lang_s.getString("importctrl", "remote");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case BOOKLET_CASE:
	     		message = lang_s.getString("importctrl", "booklet");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case PARAM_CONTROL_CASE:
	     		message = lang_s.getString("importctrl", "paramcontrol");
	            message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case LN_CONFIG_CASE:
	     		message = lang_s.getString("importctrl", "lnconfig");
	     		message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case LN_REMOTECOMMAND_CASE:
	     		message = lang_s.getString("importctrl", "lnremcommand");
	     		message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case KPI_CONFIG_CASE:
	     		message = lang_s.getString("importctrl", "kpiconfig");
	     		message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case DEWPOINT_CONFIG_CASE:
	     		message = lang_s.getString("importctrl", "dewpointconfig");
	     		message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	     		break;
	     	case ENERGY_CONFIG_CASE:
	     		message = lang_s.getString("importctrl", "energyconfig");
	     		message = message.replace("$1", var_desc);
	            message = message.replace("$2", dev_desc);
	            message = message.replace("$3", line_desc);
	            String site = "";
	            if(((String)params[0]).equals(""))
	            	site = lang_s.getString("importctrl", "site"); 
	            else
	            	site = (String)params[0];
	            message = message.replace("$4", site);
	     		break;
	     	case ENERGY_CONFIG_Meter_Models_CASE:
	     		message = lang_s.getString("importctrl", "energyconfigmetermodel");
	     		message = message.replace("$1", var_desc);
	     		break;	
	     }
	     return message;
	}
	
	private static int getMainVariableID(int idhistorvaiable) throws DataBaseException
	{
		//this query to get the id of the main variable (now we have the id of the logged variable)
		String sql = "select idvariable from cfvariable where idhsvariable = ?";
		int tmp_id = -1;
		RecordSet record = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idhistorvaiable)});
		
		if(record.size() > 0)
		{
			tmp_id = ((Integer)record.get(0).get("idvariable"));
		}
		
		return tmp_id;
	}
	
	private static ArrayList<Integer> getVarmdlList(int[] ids_variables) throws DataBaseException
	{
		String sql = "select idvarmdl from cfvariable where idvariable in(";
        for(int i=0 ; i<ids_variables.length; i++)
        {
        	sql +=ids_variables[i]+",";
        }
        sql = sql.substring(0, sql.length()-1);
        sql += ") group by idvarmdl";
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        
        ArrayList<Integer> idvarmdlList = new ArrayList<Integer>();
        
        Integer idvarmdl =  null;
        for(int i = 0; i < rs.size(); i++)
        {
        	idvarmdl = (Integer)rs.get(i).get(0);
        	idvarmdlList.add(idvarmdl);
        }
        
        return idvarmdlList;
	}
	
	private static ArrayList<String> getVarsCodes(int[] ids_variables) throws DataBaseException
	{
		String sql = "select code from cfvariable where idvariable in (";
	
		for(int i = 0; i < ids_variables.length; i++)
		{
			sql += ids_variables[i]+",";
		}
		
		sql = sql.substring(0, sql.length() - 1);
		sql += ") group by code";
		
		ArrayList<String> codeslist = new ArrayList<String>();
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
	
		for(int i = 0; i < rs.size(); i++)
		{
			codeslist.add((String)rs.get(i).get(0));
		}
		
		return codeslist;
	}
	
	private static ArrayList<Integer> getVarmdlIds(ArrayList<String> varmdlcodes, int iddevmdl) throws DataBaseException
	{
		ArrayList<Integer> varmdlids = new ArrayList<Integer>();
		
		if (varmdlcodes.size() == 0)
			return varmdlids;
		
		String sql = "select idvarmdl from cfvarmdl where iddevmdl = "+iddevmdl+" and code in ('";
		
		for(int i = 0; i < varmdlcodes.size(); i++)
		{
			sql += varmdlcodes.get(i)+"','";
		}
		
		sql = sql.substring(0, sql.length() -2);
		sql = sql +")";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		for(int i = 0; i < rs.size(); i ++)
		{
			varmdlids.add((Integer)rs.get(i).get(0));
		}
		return varmdlids;
		
	}
	
}
