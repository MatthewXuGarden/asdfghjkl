package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.math.MathExt;
import com.carel.supervisor.controller.database.AlarmCarelLoader;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.field.Variable;


public class ConditionBeanList
{
    private int idsite = 0;
    private String language = "";
    private ConditionBean[] conditionList = null;

    public ConditionBeanList(int idsite, String lang)
    {
        this.idsite = idsite;
        this.language = lang;
    }

    public ConditionBean[] getConditionList()
    {
        return this.conditionList;
    }

    public void loadAlarmConditions() throws Exception
    {
        this.loadConditions(false);
    }

    public void loadGeneralConditions() throws Exception
    {
        this.loadConditions(true);
    }

    public void loadAllConditions(int idSite) throws Exception
    {
        String sql = "select * from cfcondition where idsite=? order by condcode";
        Object[] p = { new Integer(idSite) };
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, p);

        if (rs != null)
        {
            this.conditionList = new ConditionBean[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                this.conditionList[i] = new ConditionBean(rs.get(i));
            }
        }
    }

    public void insertAlarmCondition(String pvcode, String condcode, String condtype, String data, String priority)
        throws Exception
    {
        String sql = "insert into cfcondition values(?,?,?,?,?,?,?)";
        Integer key = SeqMgr.getInstance().next(null, "cfcondition", "idcondition");
        Object[] parm = 
            {
                key, pvcode, new Integer(this.idsite), "FALSE", condcode, decodeType(condtype),
                new Timestamp(System.currentTimeMillis())
            };
        DatabaseMgr.getInstance().executeStatement(null, sql, parm);

        DataHs dataHs= CreateSqlHs.getInsertData("cfcondition",parm);
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

        int[] ids = null;

        if (condtype.equalsIgnoreCase("1"))
        {
            ids = new int[] { 0 };
        }
		//Start Fixing
        else if(condtype.equalsIgnoreCase("6")) 
        {
        	//20091203--simon modify for insert varcondition list
        	if(priority.indexOf(",")>0){
        		String temp[]=priority.split(",");
        		ids=new int[temp.length];
        		for(int i=0;i<temp.length;i++){
        			ids[i]=new Integer(temp[i]).intValue();
        		}
        	}else{
        		ids=new int[]{(new Integer(priority)).intValue()};
        	}
        }
        //End
        else
        {
            ids = decodeData(data);
        }

        insertIntoVarCondition(pvcode, this.idsite, key, ids);
    }

    public int insertGlobalCondition(String pvcode, String condcode, String condtype,
        String condFunc, String idVar, String sVal, boolean isVar, int type)
        throws Exception
    {
        //int idCondition = 0;
        int idFunction = 0;
        int idVariable = 0;
        int[] idVars = null;
        int ret = 0;
        String sql = "insert into cfcondition values(?,?,?,?,?,?,?)";

        try
        {
            Integer key = SeqMgr.getInstance().next(null, "cfcondition", "idcondition");
            Object[] parm = 
                {
                    key, pvcode, new Integer(this.idsite), "TRUE", condcode, decodeType(condtype),
                    new Timestamp(System.currentTimeMillis())
                };
            DatabaseMgr.getInstance().executeStatement(null, sql, parm);

            DataHs dataHs= CreateSqlHs.getInsertData("cfcondition",parm);
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
            //idCondition = key.intValue();
            idFunction = insertNewFunction(pvcode, this.idsite, condFunc, idVar, sVal, isVar);
            idVariable = insertNewVariable(idFunction, idVar, sVal, isVar, type);
            insertDescriptionForConditionEvent(idVariable,condcode);
            idVars = new int[] { idVariable };
            insertIntoVarCondition(pvcode, this.idsite, key, idVars);
        }
        catch (Exception e)
        {
            ret = 0;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return ret;
    }

    private int insertNewFunction(String pvcode, int site, String operation, String fval,
        String sval, boolean isvar)
    {
        FunctionBeanList funBean = new FunctionBeanList();

        return funBean.insertFunction(pvcode, site, operation, fval, sval, isvar);
    }

    private int insertNewVariable(int functioncode, String idVar, String sVal, boolean isVar,
        int type)
    {
        VarlogBeanList logList = new VarlogBeanList();

        int idVariable = 0;
        int var1 = 0;
        int var2 = 0;
        int freq = 0;

        try
        {
            var1 = Integer.parseInt(idVar);
        }
        catch (Exception e)
        {
            var1 = 0;
        }

        if (isVar)
        {
            try
            {
                var2 = Integer.parseInt(sVal);
            }
            catch (Exception e)
            {
                var2 = 0;
            }
        }

        try
        {
            freq = getMcdFrequency(var1, var2);
            idVariable = logList.insertNewLogicVarCondition(functioncode, freq, type);
            logList.insertLogicVarInBuffer(idVariable);
        }
        catch (Exception e)
        {
            idVariable = 0;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return idVariable;
    }

    private int getMcdFrequency(int idVar1, int idVar2)
    {
        VarphyBeanList varList = new VarphyBeanList();
        int[] ids = { idVar1, idVar2 };
        int freq = 0;
        int var1 = 0;
        int var2 = 0;

        try
        {
            VarphyBean[] variables = varList.getListVarByIds(this.idsite, this.language, ids);

            if (variables != null)
            {
                if (variables.length == 1)
                {
                    if (variables[0].getFrequency() != null)
                    {
                        freq = variables[0].getFrequency().intValue();
                    }
                }
                else if (variables.length == 2)
                {
                    if (variables[0].getFrequency() != null)
                    {
                        var1 = variables[0].getFrequency().intValue();
                    }

                    if (variables[1].getFrequency() != null)
                    {
                        var2 = variables[1].getFrequency().intValue();
                    }

                    freq = MathExt.gcd(var1, var2);
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return freq;
    }

    public boolean deleteAlarmCondition(String id) throws Exception
    {
        boolean ret = deleteCondition(id, false);

        if (ret)
        {
            ret = deleteVarCondition(id);
        }

        return ret;
    }

    public boolean deleteGeneralCondition(int iid) throws Exception
    {
        String id = String.valueOf(iid);
        boolean bret = deleteCondition(id, true);
        int[] idconvar = null;
        int idlogicvar = 0;
        int idfunction = 0;

        if (bret)
        {
            idconvar = retriveConditionVariables(iid);

            if ((idconvar != null) && (idconvar.length > 0))
            {
                idlogicvar = idconvar[0];

                if (idlogicvar > 0)
                {
                    deleteVarCondition(id);

                    String sql = "select functioncode from cfvariable where idvariable=? and idsite=1";
                    Object[] param = new Object[] { new Integer(idlogicvar) };
                    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

                    if ((rs != null) && (rs.size() > 0))
                    {
                        Record r = rs.get(0);
                        idfunction = ((Integer) r.get("functioncode")).intValue();

                        FunctionBeanList funList = new FunctionBeanList();
                        funList.deleteFunctionByCode(idfunction);

                        VarlogBeanList varList = new VarlogBeanList();
                        varList.deleteLogicVariable(idlogicvar);
                    }
                }
            }
        }

        return bret;
    }

    public int[] retriveConditionVariables(int idCond)
    {
        String sql = "select idvariable from cfvarcondition where idcondition=?";
        Object[] param = { new Integer(idCond) };
        int[] idsVar = null;
        RecordSet rs = null;
        Record r = null;

        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if (rs != null)
            {
                idsVar = new int[rs.size()];

                for (int i = 0; i < rs.size(); i++)
                {
                    r = rs.get(i);
                    idsVar[i] = ((Integer) r.get("idvariable")).intValue();
                }
            }
        }
        catch (Exception e)
        {
            idsVar = new int[0];

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return idsVar;
    }

    private boolean deleteVarCondition(String id) throws Exception
    {
    	
    	Object[] param = { Integer.valueOf(id) };
    	        
        DataHs dataHs= CreateSqlHs.getDeleteData("cfvarcondition",new String[]{"idcondition","pvcode","idsite","idvariable"},param,new String[]{"="},new String[]{"idcondition"});										
     	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        
        String sql = "delete from cfvarcondition where idcondition=?";
        
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        return true;
    }

    public ConditionBean loadGeneralCondition(int idCond)
        throws Exception
    {
        String sql = "select a.idcondition,a.pvcode,a.idsite,a.condcode,a.condtype,b.idvariable from " +
            "cfcondition as a,cfvarcondition as b where a.idcondition=? and a.idcondition=b.idcondition";

        Object[] prm = { new Integer(idCond) };
        ConditionBean condBean = null;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                condBean = new ConditionBean(rs.get(i));
            }
        }

        return condBean;
    }

    public ConditionBean loadCondition(String idcondition)
        throws Exception
    {
        String sql = "select idcondition,pvcode,idsite,condcode,condtype,isgeneral from cfcondition where idcondition=?";
        Object[] prm = { Integer.valueOf(idcondition) };
        ConditionBean condBean = null;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);
        Record r = null;

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                condBean = new ConditionBean(rs.get(i));
            }

            if (condBean != null)
            {
                sql = 
                "select a.idcondition,a.idvariable,b.description as descvar,c.description as descdev "+
                "from cfvarcondition as a,cftableext as b,cftableext as c,cfvariable as d "+
                "where a.idcondition=? and a.idvariable=d.idvariable and " +
                "b.idsite=? and b.languagecode=? and b.tablename=? and b.tableid=a.idvariable and " +
                "c.idsite = ? and c.languagecode=? and c.tablename=? and c.tableid=d.iddevice";

                prm = new Object[]
                    {
                        Integer.valueOf(idcondition),new Integer(idsite),this.language,"cfvariable",
                        new Integer(idsite),this.language,"cfdevice"
                    };
                
                rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

                int idv = 0;
                String lbv = "";
                String lbd = "";

                if ((rs != null) && (rs.size() > 0)&&(!condBean.getTypeCondition().equalsIgnoreCase("P"))) //aggiunto controllo per rilevare la condizione di priority
                {
                    for (int j = 0; j < rs.size(); j++)
                    {
                        r = rs.get(j);
                        idv = ((Integer) r.get("idvariable")).intValue();
                        lbv = (String) r.get("descvar");
                        lbd = (String) r.get("descdev");
                        condBean.addVariable(idv, lbv, lbd);
                    }
                }
            }
        }

        return condBean;
    }

    public Integer[] loadConditionController(String idcondition)
        throws Exception
    {
        String sql = "select idcondition,pvcode,idsite,condcode,condtype from cfcondition where idcondition=?";
        Object[] prm = { Integer.valueOf(idcondition) };
        ConditionBean condBean = null;
        Integer[] ret = null;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);
        Record r = null;

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                condBean = new ConditionBean(rs.get(i));
            }

            if (condBean != null)
            {
				String type=condBean.getTypeCondition();
                sql = "select a.idcondition,a.idvariable from cfvarcondition as a where a.idcondition=?";

                prm = new Object[] { Integer.valueOf(idcondition) };
                rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

                int firstVal = 0;

                if ((rs != null) && (rs.size() > 0))
                {
                    ret = new Integer[rs.size()];
                    r = rs.get(0);
                    firstVal = ((Integer) r.get("idvariable")).intValue();

                    //Start alarmP 2007/06/27 Fixing
                    if(type.equals("P"))
                    {
                    	//2011-6-27, Kevin Ge, if alarm priority GT 4, deem it to 4 in alarm scheduler
                    	if(firstVal != 4)
                    		sql = "select idvariable from cfvariable where iscancelled=? and type=? and idsite=1 and isactive=? and priority=?";
                    	else
                    		sql = "select idvariable from cfvariable where iscancelled=? and type=? and idsite=1 and isactive=? and priority>=?";
                        prm = new Object[] { "FALSE", new Integer(VariableInfo.TYPE_ALARM), "TRUE", firstVal };
                        rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

                        if ((rs != null) && (rs.size() > 0))
                        {
                            ret = new Integer[rs.size()];

                            for (int j = 0; j < rs.size(); j++)
                            {
                                r = rs.get(j);
                                ret[j] = (Integer) r.get("idvariable");
                            }
                        }
                    }
                    //End
                    else if(firstVal != 0)
                    {
                        for (int j = 0; j < rs.size(); j++)
                        {
                            r = rs.get(j);
                            ret[j] = (Integer) r.get("idvariable");
                        }
                    }
                    else
                    {
                        sql = "select idvariable from cfvariable where iscancelled=? and type=? and idsite=1 and isactive=?";
                        prm = new Object[] { "FALSE", new Integer(VariableInfo.TYPE_ALARM), "TRUE" };
                        rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

                        if ((rs != null) && (rs.size() > 0))
                        {
                            ret = new Integer[rs.size()];

                            for (int j = 0; j < rs.size(); j++)
                            {
                                r = rs.get(j);
                                ret[j] = (Integer) r.get("idvariable");
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    public void updateAlarmCondition(String id, String data, String type, String desc, String priority)
        throws Exception
    {
        String sql = "update cfcondition set condcode=?,condtype=?,lastupdate=? where idcondition=?";
        Object[] param = 
            {
                desc, decodeType(type), new Timestamp(System.currentTimeMillis()),
                Integer.valueOf(id)
            };
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
        
        DataHs dataHs= CreateSqlHs.getUpdateData("cfcondition",
        		new String[]{"idcondition", 
        		  "pvcode" , 
        		  "idsite" , 
        		  "isgeneral", 
        		  "condcode" , 
        		  "condtype"},new Object[]{param[3]},new String[]{"="},new String[]{"idcondition"});										
        		
        		
        DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

        if (deleteVarCondition(id))
        {
            int[] ids = null;

            if (type.equalsIgnoreCase("1"))
            {
                ids = new int[] { 0 };
            }
			//Start alarmP 2007/06/27 Fixing
            else if (type.equalsIgnoreCase("6")) 
            {
            	//20091203--simon modify for insert varcondition list
            	if(priority.indexOf(",")>0){
            		String temp[]=priority.split(",");
            		ids=new int[temp.length];
            		for(int i=0;i<temp.length;i++){
            			ids[i]=new Integer(temp[i]).intValue();
            		}
            	}else{
            		ids=new int[]{(new Integer(priority)).intValue()};
            	}
            }
            //End
            else
            {
                ids = decodeData(data);
            }

            insertIntoVarCondition(BaseConfig.getPlantId(), this.idsite, Integer.valueOf(id), ids);
        }
    }
    public void updateAlarmCondition(String id, String data, String type, String priority)
    throws Exception
{
    String sql = "update cfcondition set condtype=?,lastupdate=? where idcondition=?";
    Object[] param = 
        {
             decodeType(type), new Timestamp(System.currentTimeMillis()),
            Integer.valueOf(id)
        };
    DatabaseMgr.getInstance().executeStatement(null, sql, param);
    
    DataHs dataHs= CreateSqlHs.getUpdateData("cfcondition",
    		new String[]{"idcondition", 
    		  "pvcode" , 
    		  "idsite" , 
    		  "isgeneral", 
    		  "condcode" , 
    		  "condtype"},new Object[]{param[2]},new String[]{"="},new String[]{"idcondition"});										
    		
    		
    DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
    if (deleteVarCondition(id))
    {
        int[] ids = null;
        if (type.equalsIgnoreCase("1"))
        {
            ids = new int[] { 0 };
        }
		//Start alarmP 2007/06/27 Fixing
        else if (type.equalsIgnoreCase("6")) 
        {
        	//20091203--simon modify for insert varcondition list
        	if(priority.indexOf(",")>0){
        		String temp[]=priority.split(",");
        		ids=new int[temp.length];
        		for(int i=0;i<temp.length;i++){
        			ids[i]=new Integer(temp[i]).intValue();
        		}
        	}else{
        		ids=new int[]{(new Integer(priority)).intValue()};
        	}
        }
        //End
        else
        {
            ids = decodeData(data);
        }
        insertIntoVarCondition(BaseConfig.getPlantId(), this.idsite, Integer.valueOf(id), ids);
    }
}
    private boolean checkConditionUsed(int idcond) throws Exception
    {
        String sql = "select * from cfrule where idcondition=?";

        Object[] param = { Integer.valueOf(idcond) };
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if ((rs != null) && (rs.size() > 0))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean deleteCondition(String id, boolean isgeneral)
        throws Exception
    {
       
  
    	
    	String sql = "delete from cfcondition where idcondition=? and isgeneral=?";
        Object[] param = { Integer.valueOf(id), (String.valueOf(isgeneral)).toUpperCase() };
        boolean ret = false;

        if (checkConditionUsed(Integer.parseInt(id)))
        {
            DataHs dataHs= CreateSqlHs.getDeleteData("cfcondition",
            		new String[]{"idcondition", 
            		  "pvcode" , 
            		  "idsite" , 
            		  "isgeneral", 
            		  "condcode" , 
            		  "condtype"},param,new String[]{"=","="},new String[]{"idcondition","isgeneral"});										
         		
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

        	DatabaseMgr.getInstance().executeStatement(null, sql, param);
            ret = true;
        }

        return ret;
    }

    private void insertIntoVarCondition(String pvcode, int idsite, Integer key, int[] idvars)
        throws Exception
    {
        String sql = "insert into cfvarcondition values(?,?,?,?,?)";
        Object[][] param = new Object[idvars.length][5];
        Timestamp time = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < param.length; i++)
        {
            param[i][0] = key;
            param[i][1] = pvcode;
            param[i][2] = new Integer(idsite);
            param[i][3] = new Integer(idvars[i]);
            param[i][4] = time;
        }

        DatabaseMgr.getInstance().executeMultiStatement(null, sql, param);
        for(int i=0;i<param.length;i++){
        	DataHs dataHs= CreateSqlHs.getInsertData("cfvarcondition",param[i]);										
        	DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());
        }//for
    }

    private void loadConditions(boolean isgeneral) throws Exception
    {
        String sql = "select idcondition,pvcode,idsite,condcode,condtype from cfcondition where isgeneral=? Order by condcode";
        Object[] prm = { (String.valueOf(isgeneral)).toUpperCase() };
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

        if (rs != null)
        {
            this.conditionList = new ConditionBean[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                this.conditionList[i] = new ConditionBean(rs.get(i));
            }
        }
    }

    private String decodeType(String type)
    {
        String ret = "";

        if (type != null)
        {
            if (type.equalsIgnoreCase("1"))
            {
                ret = "V";
            }
            else if (type.equalsIgnoreCase("2"))
            {
                ret = "G";
            }
            else if (type.equalsIgnoreCase("3"))
            {
                ret = "V";
            }
            else if (type.equalsIgnoreCase("4"))
            {
                ret = "S";
            }
            else if (type.equalsIgnoreCase("5"))
            {
                ret = "K";
            }
			//Start alarmP 2007/06/27 Fixing
            else if (type.equalsIgnoreCase("6"))
            {
                ret="P";
            }
            //End
        }

        return ret;
    }

    private int[] decodeData(String data)
    {
        String[] ret = new String[0];
        String[] rows = null;
        int[] ids = null;
        
        List<Variable> alarms = AlarmCarelLoader.loadAllarm(null,null, null);
        if ((data != null) && (data.length() > 3))
        {
            if (data.indexOf("@@") != -1)
            {
                rows = data.split("@@");
            }
            else
            {
                rows = new String[] { data };
            }

            if (rows != null)
            {
                ret = new String[rows.length];

                for (int i = 0; i < ret.length; i++)
                {
                    String id = rows[i].substring(0, rows[i].indexOf("$?"));
                    if(id.indexOf("-")>0)
                    {
                    	String[] str = id.split("-"); 
                    	for(Variable v:alarms)
                    	{
                    		if(v.getInfo().getDevice().equals(Integer.valueOf(str[0])) &&
                    			v.getInfo().getModel().equals(Integer.valueOf(str[1])))
                    		{
                    			ret[i] = v.getInfo().getId().toString();
                    			break;
                    		}
                    	}
                    }
                    else
                    	ret[i] = id;
                }
            }
        }

        ids = new int[ret.length];
        List list = new ArrayList();
        for (int i = 0; i < ids.length; i++)
        {
        	Integer v = Integer.parseInt(ret[i]);
        	boolean exist = false;
        	for(int j=0;j<i;j++)
        	{
        		if(ids[j] == v)
        		{
        			exist = true;
        			break;
        		}
        	}
        	if(!exist)
        	{
        		ids[i] = Integer.parseInt(ret[i]);
        		list.add(ids[i]);
        	}
        }
        ids = new int[list.size()];
        for(int i=0;i<list.size();i++)
        {
        	ids[i] = (Integer)list.get(i);
        }
        return ids;
    }

    public void updateGeneralCondition(String id, String desc, String type, String operation,
        String idval1, String idval2, boolean isVar, boolean isDigit)
    {
        VarlogBeanList logList = new VarlogBeanList();
        FunctionBeanList funList = new FunctionBeanList();
        int idCond = 0;
        int idLVar = 0;
        int idFunc = 0;
        int idType = 0;
        int idVar1 = 0;
        int idVar2 = 0;
        int freqy = 0;

        String sql = "update cfcondition set condcode=?,condtype=?,lastupdate=? where idcondition=?";

        try
        {
            idVar1 = Integer.parseInt(idval1);
        }
        catch (Exception e)
        {
            idVar1 = 0;
        }

        if (isVar)
        {
            try
            {
                idVar2 = Integer.parseInt(idval2);
            }
            catch (Exception e)
            {
                idVar2 = 0;
            }
        }

        int[] datasLog = null;

        try
        {
            idCond = Integer.parseInt(id);
        }
        catch (Exception e)
        {
        }

        Object[] param = 
            {
                desc, decodeType(type), new Timestamp(System.currentTimeMillis()),
                new Integer(idCond)
            };

        try
        {
            DatabaseMgr.getInstance().executeStatement(null, sql, param);
            
            DataHs dataHs= CreateSqlHs.getUpdateData("cfcondition",
            		new String[]{"idcondition", 
            		  "pvcode" , 
            		  "idsite" , 
            		  "isgeneral", 
            		  "condcode" , 
            		  "condtype"},new Object[]{param[param.length-1]},new String[]{"="},new String[]{"idcondition"});										
            		
            		
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());


            int[] idLogicVar = this.retriveConditionVariables(idCond);

            if ((idLogicVar != null) && (idLogicVar.length > 0))
            {
                idLVar = idLogicVar[0];
            }
            
            updateDescriptionForConditionEvent(idLVar,desc);
            
            idLogicVar = null;

            datasLog = logList.retriveLogicVariable(idLVar);

            if (datasLog != null)
            {
                idFunc = datasLog[0];
                idType = datasLog[1];
                idType = (isDigit) ? 1 : 0;
                funList.updateFunctionByCode(idFunc, operation, idval1, idval2, isVar);
                freqy = getMcdFrequency(idVar1, idVar2);

                if (freqy != 0)
                {
                    logList.updateVarlogFrequency(idLVar, freqy, idType);
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
    
    private void insertDescriptionForConditionEvent(int idVariable,String desc)
    {
    	String sql = "select languagecode from cfsiteext where idsite=1 and isdefault='TRUE'";
    	RecordSet rs = null;
    	Record r = null;
    	String defLang = "EN_en";
    	try
    	{
    		rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    		if(rs != null && rs.size() == 1)
    			r = rs.get(0);
    		if(r != null)
    			defLang = UtilBean.trim(r.get("languagecode"));
    		
    		sql = "insert into cftableext (idsite,languagecode,tablename,tableid,description,lastupdate) values(?,?,?,?,?,?)";
    		DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(1),defLang,
    												   "cfvariable",new Integer(idVariable),desc,
    												   new Timestamp(System.currentTimeMillis())});
    	}
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
    	}
    }
    
    private void updateDescriptionForConditionEvent(int idVariable,String desc)
    {
    	String sql = "select languagecode from cfsiteext where idsite=1 and isdefault='TRUE'";
    	RecordSet rs = null;
    	Record r = null;
    	String defLang = "EN_en";
    	try
    	{
    		rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    		if(rs != null && rs.size() == 1)
    			r = rs.get(0);
    		if(r != null)
    			defLang = UtilBean.trim(r.get("languagecode"));
    		
    		sql = "update cftableext set description=?, lastupdate=? where idsite=? and languagecode=? and tablename=? and tableid=?";
    		DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{desc,new Timestamp(System.currentTimeMillis()),
    												   new Integer(1),defLang,"cfvariable",new Integer(idVariable)});
    	}
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
    	}
    }
}
