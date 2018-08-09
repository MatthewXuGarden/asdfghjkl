package com.carel.supervisor.director.ide;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.presentation.bo.BSetAction;


public class RuleImporter
{
    private static final String NAME = "name";
    private static final String TIMEBANDS = "TimeBands";
    private static final String CONDITIONS = "Conditions";
    private static final String ACTIONS = "Actions";
    private static final String ACTION = "action";
    private static final String RULES = "Rules";
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String ID = "id";
    private static final String ORDER = "order";
    private static final String TIMEBAND = "timeband";
    private static final String CONDITION = "condition";
    private static final String PARAMETERS = "parameters";
    private static final String DELAY = "delay";
    private static final String OPR = "opr";
    private static final String PARS = "pars";
    
    
    public RuleImporter()
    {
    }

    public void importer(XMLNode xmlNode) throws Exception
    {
    	DataHs dataHs= CreateSqlHs.getDeleteData("cfrule",new String[]{"idrule","pvcode","idsite","rulecode","idcondition","idtimeband","actioncode","delay","isenabled"},new Object[]{new Integer(0)},new String[]{"<"},new String[]{"idrule"});
    	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
    	String sql = "delete from cfrule where idrule < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "delete from cftableext where tablename='cfvariable' and tableid < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        dataHs= CreateSqlHs.getDeleteData("cftimeband",new String[]{"idtimeband","pvcode","idsite","timecode","timetype","timeband","iscyclic"},new Object[]{new Integer(0)},new String[]{"<"},new String[]{"idtimeband"});
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cftimeband where idtimeband < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

          
        dataHs= CreateSqlHs.getDeleteData("cfaction",new String[]{"idaction","pvcode","idsite","code","actioncode","actiontype","isscheduled","template","parameters"},new Object[]{new Integer(0)},new String[]{"<"},new String[]{"idaction"});
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cfaction where idaction < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        dataHs= CreateSqlHs.getDeleteData("cffunction",new String[]{"idfunction","pvcode","idsite","functioncode","opertype","parameters","operorder"},new Object[]{new Integer(0)},new String[]{"<"},new String[]{"idfunction"});
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cffunction where idfunction < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        sql = "delete from cfvariable where idvariable < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        
        dataHs= CreateSqlHs.getDeleteData("cfvarcondition",new String[]{"idcondition","pvcode","idsite","idvariable"},new Integer[]{new Integer(0)},new String[]{"<"},new String[]{"idcondition"});										
     	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cfvarcondition where idcondition < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        dataHs= CreateSqlHs.getDeleteData("cfcondition",new String[]{"idcondition","pvcode","idsite","isgeneral","condcode","condtype"},new Integer[]{new Integer(0)},new String[]{"<"},new String[]{"idcondition"});										
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cfcondition where idcondition < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);

        
        dataHs= CreateSqlHs.getDeleteData("cfaddrbook",new String[]{"idaddrbook","pvcode","idsite","type","address","receiver"},new Integer[]{new Integer(0)},new String[]{"<"},new String[]{"idaddrbook"});										
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        sql = "delete from cfaddrbook where idaddrbook < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        sql = "delete from buffer where idvariable < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        sql = "delete from buffer where idvariable < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        sql = "delete from hsvarhistor where idsite=1 and idvariable < 0";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
        
        Map timeband = new HashMap();
        Map actions = new HashMap();
        Map conditions = new HashMap();
        XMLNode xmlNodeTmp = null;
        
        xmlNodeTmp = findNode(xmlNode, TIMEBANDS);
        timeband = saveTimeBand(xmlNodeTmp);

        xmlNodeTmp = findNode(xmlNode, CONDITIONS);
        conditions = saveConditions(xmlNodeTmp);

        //Devo distinguere le azioni appartenenti a regole schedulate e quelle su allarmi/eventi
        
       
        xmlNodeTmp = findNode(xmlNode, ACTIONS);
        actions = saveActions(xmlNodeTmp, conditions.isEmpty());

        xmlNodeTmp = findNode(xmlNode, RULES);
        saveRules(xmlNodeTmp, timeband, conditions, actions);
    }
    
    private XMLNode findNode(XMLNode xmlNode, String name)
    {
        XMLNode xmlNodeTmp = null;

        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTmp = xmlNode.getNode(i);

            if (xmlNodeTmp.getAttribute(NAME).equalsIgnoreCase(name))
            {
                return xmlNodeTmp;
            }
        }

        return null;
    }

    //OK
    private void saveRules(XMLNode xmlNode, Map timeBands, Map conditions, Map actions)
        throws DataBaseException
    {
        XMLNode xmlNodeTmp = null;
        String code = null;
        Integer id = null;
        String timeband = null;
        String condition = null;
        String action = null;
        Integer timebandId = null;
        Integer conditionId = null;
        Integer actionId = null;
        Integer delay = null;
        String sql = "insert into cfrule values (?,?,?,?,?,?,?,?,?,?)";
        Object[] params = null;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTmp = xmlNode.getNode(i);
            
            code = xmlNodeTmp.getAttribute(NAME);
            code = StringUtility.clrBadOSChars(code);
            
            id = Integer.valueOf(xmlNodeTmp.getAttribute(ID, 0) * (-1));
            timeband = xmlNodeTmp.getAttribute(TIMEBAND);
            
            action = xmlNodeTmp.getAttribute(ACTION);
            action = StringUtility.clrBadOSChars(action);
            
            condition = xmlNodeTmp.getAttribute(CONDITION);
            timebandId = (Integer) timeBands.get(timeband);
            conditionId = (Integer) conditions.get(condition);
            actionId = (Integer) actions.get(action);
            delay = new Integer(xmlNodeTmp.getAttribute(DELAY, 0));
            params = new Object[]
                {
                    id, "firstPV", new Integer(1), code, conditionId, timebandId, actionId, delay,
                    "TRUE", now
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
            
            DataHs dataHs= CreateSqlHs.getInsertData("cfrule",params);         		
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
            
        }
    }

    //OK
    private Map saveTimeBand(XMLNode xmlNode) throws DataBaseException
    {
        XMLNode xmlNodeTmp = null;
        Map map = new HashMap();
        String code = null;
        Integer id = null;
        Integer type = null;
        String timeband = null;
        String sql = "insert into cftimeband values (?,?,?,?,?,?,?,?)";
        Object[] params = null;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTmp = xmlNode.getNode(i);
            
            code = xmlNodeTmp.getAttribute(NAME);
            code = StringUtility.clrBadOSChars(code);
            
            id = Integer.valueOf(xmlNodeTmp.getAttribute(ID, 0) * (-1));
            type = Integer.valueOf(xmlNodeTmp.getAttribute(TYPE, 0));
            timeband = xmlNodeTmp.getAttribute(VALUE);
            if ((4 == type.intValue()) || (5 == type.intValue())) //Eventuale normalizzazione
            {
            	if (-1 != timeband.indexOf("+"))
            	{
            		timeband = normalizza(timeband);
            	}
            }
            params = new Object[] { id, "firstPV", new Integer(1), code, type, timeband, "", now };
            DatabaseMgr.getInstance().executeStatement(null, sql, params);
            DataHs dataHs= CreateSqlHs.getInsertData("cftimeband",params);
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
          
            
            map.put(code, id);
        }

        return map;
    }

    private String normalizza(String timeband)
    {
    	String[] token = StringUtility.split(timeband,"|");
    	String[] days = StringUtility.split(token[0],"+");
    	StringBuffer buffer = new StringBuffer();
    	for(int i = 0; i < days.length; i++)
    	{
    		buffer.append(days[i]);
    		buffer.append("|");
    		buffer.append(token[1]);
    		if (i < (days.length - 1))
			{
    			buffer.append(",");
			}
    	}
    	return buffer.toString();
    }
    
    private Map saveActions(XMLNode xmlNode) throws DataBaseException
    {
    	return saveActions(xmlNode, false);
    }
    
    private Map saveActions(XMLNode xmlNode, boolean isSched) throws DataBaseException
    {
    	XMLNode xmlNodeTmp = null;
    	XMLNode xmlNodeTmp2 = null;
        Map map = new HashMap();
        String code = null;
        Integer id = null;
        String type = null;
        String sched = (isSched ? "TRUE" : "FALSE"); // se non c"e' condizione, allora deve essere schedulata
        String sql = "insert into cfaction values (?,?,?,?,?,?,?,?,?,?)";
        
        
        
        String sql1 = "insert into cfaddrbook values (?,?,?,?,?,?,?)";
        Object[] params = null;
        Object[] params1 = null;
        String parameters = null;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int count = 0;
        int idaction = 0;
        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTmp = xmlNode.getNode(i);
            
            code = xmlNodeTmp.getAttribute(NAME);
            code = StringUtility.clrBadOSChars(code);
            
            id = Integer.valueOf(xmlNodeTmp.getAttribute(ID, 0) * (-1));
            for(int j = 0; j < xmlNodeTmp.size(); j++)
            {
            	xmlNodeTmp2 = xmlNodeTmp.getNode(j);
	            type = xmlNodeTmp2.getAttribute(TYPE);
	            if (type.equals("R")) //Aspettando che si aggiorni l'IDE, metto D al posto di R per il DIAL
	            {
	            	type="D";
	            }
	            parameters = xmlNodeTmp2.getAttribute(PARAMETERS,"");
	            //insert in address
	            if (!type.equals("L") && !type.equals("V"))
	            {
		            String[] tokens = StringUtility.split(parameters, ";");
		            parameters = "";
		            for (int k = 0; k < tokens.length; k++)
		            {
		            	count--;
		            	String[] tmp = StringUtility.split(tokens[k],"=");
		            	
		            	params1 = new Object[]{new Integer(count), "firstPV", new Integer(1),type, tmp[1],tmp[1],now};
		            	DatabaseMgr.getInstance().executeStatement(null, sql1, params1);
		            	DataHs dataHs= CreateSqlHs.getInsertData("cfaddrbook",params1);										
		                DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
		                
		            	
		            	parameters=parameters+new Integer(count);
		            	if (k < (tokens.length-1))
		            	{
		            		parameters=parameters+";";
		            	}
		            }
	            }
	            idaction--;
	            String template = "";
	            if ((type.equals("E")) || (type.equals("P")))
	            {
	            	template = BSetAction.UNSCHEDULERTEMPLATE;
	            }
	            else if (type.equals("F")) 
	            {
	            	template = "PVSendFax.rtf";
	            }
	            else if(type.equals("L"))
	            {
	            	// CASO RELAY
	            	try 
	            	{
	            		parameters = remapIdVarIntoIdRelayForRelayRue(parameters);
					} 
	            	catch (Exception e) {
					}
	            }
	            
	            params = new Object[] { new Integer(idaction), "firstPV", new Integer(1), code, id, type, sched, template, parameters, now };
	            DatabaseMgr.getInstance().executeStatement(null, sql, params);
	           
	           DataHs dataHs= CreateSqlHs.getInsertData("cfaction",params);
	           DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(), dataHs.getObjects());
	    
	            
            }
            map.put(code, id);
        }

        return map;
    }
    
   
    //insert into cffunction
    //into cfvariable
    //into cfvarcondition
    //into cfcondition
    private Map saveConditions(XMLNode xmlNode) throws DataBaseException
    {
        XMLNode xmlNodeTmp = null;
        Map map = new HashMap();
        Integer code = null;
        Integer id = null;
        Integer order = null;
        String opr = null;
        String name = null;
        String pars = null;
        String sql = "insert into cffunction values (?,?,?,?,?,?,?,?)";
        Object[] params = null;
        int count = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List idFunctionCode = new ArrayList();

        for (int i = 0; i < xmlNode.size(); i++) //Ciclo a livello di Nodo Conditions
        {
            xmlNodeTmp = xmlNode.getNode(i);
            
            name = xmlNodeTmp.getAttribute(NAME);
            name = StringUtility.clrBadOSChars(name);
            
            code = Integer.valueOf(xmlNodeTmp.getAttribute(ID, 0) * (-1));

            for (int j = 0; j < xmlNodeTmp.size(); j++) //Ciclo per ogni singolo elemento della Condizione
            {
            	count--;
                id = new Integer(count);
                XMLNode func = xmlNodeTmp.getNode(j);
                order = Integer.valueOf(func.getAttribute(ORDER, 0));
                opr = func.getAttribute(OPR);
                pars = func.getAttribute(PARS);
                params = new Object[] { id, "firstPV", new Integer(1), code, opr, pars, order, now };
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
                
                DataHs dataHs= CreateSqlHs.getInsertData("cffunction",params);										
                DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
                
            }

            idFunctionCode.add(code);

            Integer idVariable = insertVariable(name, id, code);
            Integer idCondition = insertVarCondition(idVariable);
            insertCondition(idCondition, name);
            map.put(name, idCondition);
        }

        return map;
    }

    private Integer insertVariable(String name, Integer id, Integer code)
        throws DataBaseException
    {
        String sql = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] param = new Object[38];
        Integer idVariable = id;
        param[0] = idVariable;
        param[1] = "firstPV";
        param[2] = new Integer(1);
        param[3] = new Integer(0);
        param[4] = "TRUE";
        param[6] = code;
        param[7] = name;
        param[8] = new Integer(1);
        param[16] = "FALSE";
        param[27] = new Integer(300); //frequency
        param[28] = new Integer(0); //delta
        param[30] = "FALSE";
        param[31] = "FALSE";
        param[32] = "TRUE";
        param[33] = "FALSE";
        param[36] = new Timestamp(System.currentTimeMillis());
        param[37] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        sql="insert into buffer values (?,?,?,?,?)";
        param = new Object[5];
        param[0] = new Integer(1);
        param[1] = idVariable;
        param[2] = new Integer(600);//profondit� di circa  3 mesi a 300 sec
        param[3] = new Integer(-1);
        param[4] = new Boolean(false);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        
        sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
        param = new Object[8];
        
        param[0] = new Integer(1);
        param[1] = LangUsedBeanList.getDefaultLanguage(1);//Default
        param[2] = "cfvariable";
        param[3] = id;
        param[4] = name;
        param[5] = null;
        param[6] = null;
        param[7] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        return idVariable;
    }

    private Integer insertVarCondition(Integer idVariable)
        throws DataBaseException
    {
        String sql = "insert into cfvarcondition values (?,?,?,?,?)";
        Object[] param = new Object[5];
        param[0] = idVariable;
        param[1] = "firstPV";
        param[2] = new Integer(1);
        param[3] = idVariable;
        param[4] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        DataHs dataHs= CreateSqlHs.getInsertData("cfvarcondition",param);										
     	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        

        return idVariable;
    }

    private void insertCondition(Integer idCondition, String name)
        throws DataBaseException
    {
        String sql = "insert into cfcondition values (?,?,?,?,?,?,?)";
        Object[] param = new Object[7];
        param[0] = idCondition;
        param[1] = "firstPV";
        param[2] = new Integer(1);
        param[3] = "TRUE";
        param[4] = name;
        param[5] = "K";
        param[6] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        DataHs dataHs= CreateSqlHs.getInsertData("cfcondition",param);										
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

    }
    
    /*
	 * Trava
	 * Modifica per la corretta gestione dei RELAY in fase di import da RuleEditor
	 * Il RE fornisce nel campo parameters la coppia:
	 * - IDVAR=VALUE
	 * per PVPRO deve essere tradotta in
	 * - IDREL=VALUE
	 * Dove IDREL � recuperato in:
	 * CFRELAY
	 * select idrelay from cfrelay where idvariable=IDVAR
	 */
    private String remapIdVarIntoIdRelayForRelayRue(String parameters)
    	throws Exception
    {
    	String ret = parameters;
    	String[] relList = {parameters};
    	StringBuffer sb = new StringBuffer();
    	if(parameters != null)
    	{
    		if(parameters.indexOf(";") != -1)
    			relList = StringUtility.split(parameters,";");
    	}
    	
    	if(relList != null)
    	{
    		String[] tmp1 = null;
    		String idrel = null;
    		String value = null;
    		for (int i = 0; i < relList.length; i++) 
    		{
    			tmp1 = StringUtility.split(relList[i],"=");
    			idrel = getIdRelayFromIdVar(tmp1[0]);
    			value = tmp1[1];
    			if(i!=0)
    				sb.append(";");
    			sb.append(idrel+"="+value);
			}
    		ret = sb.toString();
    	}
    	return ret;
    }
    
    private String getIdRelayFromIdVar(String idVar)
    {
    	String ret = idVar;
    	String sql = "select idrelay from cfrelay where idvariable=?";
    	try 
    	{
    		RecordSet rs  = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idVar)});
    		if(rs != null && rs.size() != 0)
    			ret = ""+((Integer)rs.get(0).get("idrelay")).intValue();
		} 
    	catch (DataBaseException e) {}
    	return ret;
    }
}
