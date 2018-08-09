package com.carel.supervisor.presentation.bean.rule;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.bo.BSetAction;
import com.carel.supervisor.presentation.io.CioEVT;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class ActionBeanList
{
    private HashMap<Integer,StringBuffer> actionCodeMap = new HashMap<Integer,StringBuffer>();
    private HashMap<Integer,ActionBean> actionMap = new HashMap<Integer, ActionBean>();
    private int[] ids = null;
    private int[] actioncode = null;
    private int screenw = 1024;
    private int screenh = 768;
    
    public ActionBeanList()
    {
    }

    public ActionBeanList(int idsite, String language, boolean isScheduled)
        throws DataBaseException
    {
        this(idsite, language, isScheduled, false);
    }

    public ActionBeanList(int idsite, String language, boolean isScheduled,
        boolean techFilter) throws DataBaseException
    {
        String filTech = "";

        if (techFilter)
        {
            filTech = "and actiontype <> 'T' and idaction not in (1)";
        }
        
        // FILTER FOR PARAMETERS CONTROL PLUGIN
        String pc_filter = " and actiontype not in ('FX','EX','SX')";

		 
        String sql = "select * from cfaction where idsite = ? and isscheduled= ? " +
        	filTech + pc_filter +" order by code DESC";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    new Integer(idsite), (isScheduled ? "TRUE" : "FALSE")
                });

        ActionBean tmp = null;
        StringBuffer param = new StringBuffer();
        actioncode = new int[rs.size()];
        ids = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new ActionBean(rs.get(i), language);
            ids[i] = tmp.getIdAction();
            actionMap.put(new Integer(tmp.getIdAction()), tmp);

            actioncode[i] = tmp.getActioncode();
            param = actionCodeMap.get(new Integer(actioncode[i]));

            if (param == null)
            {
                param = new StringBuffer();
            }

            param.append(tmp.getActiontype());
            actionCodeMap.put(new Integer(tmp.getActioncode()), param);
        }
    }
    public void getEnabledAction(int idsite, String language, boolean isScheduled,
            boolean techFilter) throws DataBaseException
        {
            String filTech = "";

            if (techFilter)
            {
                filTech = "and cfaction.actiontype <> 'T' and cfaction.idaction not in (1)";
            }
            
            // FILTER FOR PARAMETERS CONTROL PLUGIN
            String pc_filter = " and cfaction.actiontype not in ('FX','EX','SX')";

    		 
            String sql = "select cfaction.* from cfaction inner join cfrule on cfaction.actioncode=cfrule.actioncode and cfrule.isenabled='TRUE' where cfaction.idsite = ? and cfaction.isscheduled= ? " +
            	filTech + pc_filter +" order by cfaction.code DESC";

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[]
                    {
                        new Integer(idsite), (isScheduled ? "TRUE" : "FALSE")
                    });

            ActionBean tmp = null;
            StringBuffer param = new StringBuffer();
            actioncode = new int[rs.size()];
            ids = new int[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                tmp = new ActionBean(rs.get(i), language);
                ids[i] = tmp.getIdAction();
                actionMap.put(new Integer(tmp.getIdAction()), tmp);

                actioncode[i] = tmp.getActioncode();
                param = actionCodeMap.get(new Integer(actioncode[i]));

                if (param == null)
                {
                    param = new StringBuffer();
                }

                param.append(tmp.getActiontype());
                actionCodeMap.put(new Integer(tmp.getActioncode()), param);
            }
        }
    
    public Map<String, ActionBean> getActionBeansByActionCode(int idsite,int actioncode,String language)throws DataBaseException
    {
    	HashMap<String, ActionBean> map = new HashMap<String, ActionBean>();
        String sql = "select * from cfaction where idsite = ? and actioncode= ?  order by code DESC";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[]
                {
                    new Integer(idsite), new Integer(actioncode)
                });

        ActionBean tmp = null;
        for (int i = 0; i < rs.size(); i++)
        {
            tmp = new ActionBean(rs.get(i), language);
            map.put(tmp.getActiontype(), tmp);
        }
        return map;
    }
    public int size()
    {
        return actionCodeMap.size();
    }

    public ActionBean getAction(int i)
    {
        return actionMap.get(new Integer(ids[i]));
    }

    public String[] getActiontypeByActionCode(int idsite, int actioncode)
        throws DataBaseException
    {
        String sql = "select actiontype from cfaction where idsite = ? and actioncode = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        String[] actiontype = new String[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            actiontype[i] = UtilBean.trim(rs.get(i).get("actiontype").toString());
        }

        return actiontype;
    }

    public String getHTMLActionTable(int idsite, String language, String title,
        int height, int width, boolean sched, boolean permict)
        throws DataBaseException
    {
        LangService lan = LangMgr.getInstance().getLangService(language);
        String[] ClickRowFunction = new String[size()];
        String[] DBLClickRowFunction = new String[size()];
        HTMLElement[][] dati = new HTMLElement[size()][];
        
        TreeMap<String,Integer> sorted = new TreeMap<String,Integer>();
        Iterator<Integer> iter = actionCodeMap.keySet().iterator();
        while(iter.hasNext())
        {
            Integer key = iter.next();    
            sorted.put(getDescription(idsite, key.intValue()),key);
        }
        
        Iterator<String> iter2 = sorted.keySet().iterator();
        String[] code = null;

        if (!sched)
        {
            code = new String[] { "F", "S", "E", "L", "V", "D", "P", "W" };
        }
        else
        {
            code = new String[] { "F", "S", "E", "L", "V", "D", "P" };
        }

        int count = -1;
        String descr = null;
        String[] rowClass = new String[actionCodeMap.size()]; //classi per ogni riga
        int col_tmp = 0;
        int cols = code.length + 2;

        while (iter2.hasNext())
        {
            col_tmp = 0;
            count++;

            dati[count] = new HTMLElement[cols];

            descr = iter2.next();

            //descr = getDescription(idsite, key.intValue());

            Integer key = sorted.get(descr);
 
            if (key.intValue() < 0)
            {
                dati[count][col_tmp++] = new HTMLSimpleElement(
                        "<img src='images/ok.gif' > ");
            }
            else
            {
                dati[count][col_tmp++] = new HTMLSimpleElement("");
            }

            dati[count][col_tmp++] = new HTMLSimpleElement(descr);

            for (int i = 2; i < cols; i++)
            {
                if (actionCodeMap.get(key).toString().contains(code[i - 2])&& !actionCodeMap.get(key).toString().equalsIgnoreCase("#BS"))
                {
                    dati[count][col_tmp++] = new HTMLSimpleElement(
                            "<img src='images/ok.gif' > ");
                }
                else
                {
                    dati[count][col_tmp++] = new HTMLSimpleElement("");
                }
            }

            if (actionCodeMap.get(key).toString().contains("X"))
            {
                rowClass[count] = "statoAllarme1_b";
            }
            else
            {
                rowClass[count] = count%2==0?"Row1":"Row2";
            }

            ClickRowFunction[count] = "" + count; //key.toString() + ";" + descr; 7353 bug fix
            DBLClickRowFunction[count] = String.valueOf(key.intValue());
        }

        //header tabella
        String[] headerTable = null;

        headerTable = new String[code.length + 2];

        int c = 0;

        headerTable[c] = lan.getString("alrsched", "ide");
        c++;

        headerTable[c] = lan.getString("action", "description");
        c++;
        headerTable[c] = lan.getString("action", "fax");
        c++;
        headerTable[c] = lan.getString("action", "sms");
        c++;
        headerTable[c] = lan.getString("action", "email");
        c++;

        headerTable[c] = lan.getString("action", "relay");
        c++;
        headerTable[c] = lan.getString("action", "variable");
        c++;
        headerTable[c] = lan.getString("action", "remote");
        c++;

        if (!sched)
        {
            headerTable[c] = lan.getString("action", "print");
            c++;
            headerTable[c] = lan.getString("action", "window");
        }
        else
        {
            headerTable[c] = lan.getString("setaction2", "tab7name");
        }

        HTMLTable table = new HTMLTable("action", headerTable, dati);

        if (permict)
        {
            table.setDbClickRowAction("modifyAction('"+sched+"','$1');");
            table.setSgClickRowAction("selectedLine('$1')");
            table.setSnglClickRowFunction(ClickRowFunction);
            table.setDlbClickRowFunction(DBLClickRowFunction);
        }

        if (!sched)
        {
            table.setAlignType(new int[] { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
        }
        else
        {
            table.setAlignType(new int[] { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 });
        }

        table.setTableTitle(title);
        table.setScreenH(screenh);
        table.setScreenW(screenw);
        table.setWidth(width);
        table.setHeight(height);
        table.setRowsClasses(rowClass);

        //sched
        if (sched) {
            table.setColumnSize(0, 45);
            table.setColumnSize(1, 270);
            table.setColumnSize(2, 63);
            table.setColumnSize(3, 63);
            table.setColumnSize(4, 63);
            table.setColumnSize(5, 64);
            table.setColumnSize(6, 80);
            table.setColumnSize(7, 65);
            table.setColumnSize(8, 65);
        } else {
            table.setColumnSize(0, 45);
            table.setColumnSize(1, 260);
            table.setColumnSize(2, 42);
            table.setColumnSize(3, 42);
            table.setColumnSize(4, 54);
            table.setColumnSize(5, 50);
            table.setColumnSize(6, 78);
            table.setColumnSize(7, 62);
            table.setColumnSize(8, 62);
            table.setColumnSize(9, 72);
        }
       
        String htmlTable = table.getHTMLText();

        return htmlTable;
    }

    public int countActionByActioncode(int idsite, int actioncode)
        throws DataBaseException
    {
        String sql = "select count(1) as number from cfaction where idsite = ? and actioncode = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        return ((Integer) rs.get(0).get("number")).intValue();
    }

    //sezione azione non settata
	//change return type from void to int, return the actionid
    public int insertActionX(int idsite, String description,
        boolean isScheduled) //nuova action da lista
        throws DataBaseException
    {
        String sql = "insert into cfaction values (?,?,?,?,?,?,?,?,?,?)";
        SeqMgr o = SeqMgr.getInstance();
        Object[] params = new Object[10];
        params[0] = o.next(null, "cfaction", "idaction");
        params[1] = BaseConfig.getPlantId();
        params[2] = new Integer(idsite);
        params[3] = description;
        params[4] = o.next(null, "cfaction", "actioncode");
        params[5] = "X";
        params[6] = UtilBean.writeBoolean(isScheduled);
        params[7] = null;
        params[8] = "";
        params[9] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, params);

        DataHs dataHs = CreateSqlHs.getInsertData("cfaction", params);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
        return Integer.parseInt(params[4].toString());
    }

    //SEZIONE INSERT, DELETE, UPDATE DI ACTION GENERICA PARAMETRIZZATA
    public void updateAction(int idsite, int actioncode, String param,
        String actiontype, String template) throws DataBaseException
    {
        String sql = "update cfaction set parameters = ?, template = ? where idsite = ? and actioncode = ? and actiontype = ?";
        Object[] params = new Object[5];
        params[0] = param;
        params[1] = template;
        params[2] = new Integer(idsite);
        params[3] = new Integer(actioncode);
        params[4] = actiontype;
        DatabaseMgr.getInstance().executeStatement(null, sql, params);

        DataHs dataHs = CreateSqlHs.getUpdateData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[]
                {
                    new Integer(idsite), new Integer(actioncode), actiontype
                }, new String[] { "=", "=", "=" },
                new String[] { "idsite", "actioncode", "actiontype" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
    }

    public void deleteAction(int idsite, int actioncode, String actiontype)
        throws DataBaseException
    {
        DataHs dataHs = CreateSqlHs.getDeleteData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[]
                {
                    new Integer(idsite), new Integer(actioncode), actiontype
                }, new String[] { "=", "=", "=" },
                new String[] { "idsite", "actioncode", "actiontype" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());

        String sql = "delete from cfaction where idsite = ? and actioncode = ? and actiontype = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);
        param[2] = actiontype;
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public void insertAction(int idsite, int actioncode, String param,
        String actiontype, String template, String description,
        boolean isScheduled) throws DataBaseException
    {
        String sql = "insert into cfaction values (?,?,?,?,?,?,?,?,?,?)";
        SeqMgr o = SeqMgr.getInstance();
        Object[] params = new Object[10];
        params[0] = o.next(null, "cfaction", "idaction");
        params[1] = BaseConfig.getPlantId();
        params[2] = new Integer(idsite);
        params[3] = description;
        params[4] = new Integer(actioncode);
        params[5] = actiontype;
        params[6] = UtilBean.writeBoolean(isScheduled);
        params[7] = template;
        params[8] = param;
        params[9] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, params);

        DataHs dataHs = CreateSqlHs.getInsertData("cfaction", params);
        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
    }

    public boolean existAction(int idsite, int actioncode, String actiontype)
        throws DataBaseException
    {
        String sql = "select idaction from cfaction where idsite = ? and actioncode = ? and actiontype = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);
        param[2] = actiontype;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getActionParameters(int idsite, int actioncode, String actiontype)
    	throws DataBaseException
    {
        String sql = "select parameters from cfaction where idsite = ? and actioncode = ? and actiontype = ?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);
        param[2] = actiontype;

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() != 0)
        {
            return rs.get(0).get("parameters").toString();
        }
        else
        {
            return "";
        }
    }

    public String getActionTemplate(int idsite, int actioncode, String actiontype)
	throws DataBaseException
	{
	    String sql = "select template from cfaction where idsite = ? and actioncode = ? and actiontype = ?";
	    Object[] param = new Object[3];
	    param[0] = new Integer(idsite);
	    param[1] = new Integer(actioncode);
	    param[2] = actiontype;
	
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
	
	    if (rs.size() > 0)
	    {
	        Object template = rs.get(0).get("template");
	        	if( template != null )
	        		return template.toString();
	        	else
	        		return "-1";
	    }
	    else
	    {
	        return "-1";
	    }
	}
    
    public static String getDescription(int idsite, int actioncode)
        throws DataBaseException
    {
        String sql = "select code from cfaction where idsite = ? and actioncode = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        String desc = rs.get(0).get("code").toString();

        return desc;
    }

    // *****
    // controllo se esiste azione del tipo actionType gi� configurata:
    // *****
    public static boolean existsActionType(int idsite, String actionType) throws DataBaseException
    {
    	boolean result = false;
    	
    	String sql = "select count(*) from cfaction where idsite = ? and actiontype = ?";
    	Object[] params = new Object[]{new Integer(idsite), actionType};
    	
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
    	
    	try
    	{
			result = (((Integer)rs.get(0).get(0)).intValue() > 0);
		}
    	catch (Exception e)
    	{
    		result = false;
		}
    	
    	return result;
    }
    
    // *****
    // controllo se azione del tipo actionType (es. "D") ha solo parametri di tipo IP:
    // *****
    public static boolean existsParamType(int idsite, String actionType) throws DataBaseException
    {
    	boolean result = false;
    	String para = "";
    	String tmp = "";
    	
    	//N.B.: metodo usato qui solo per azione tipo "D": controlla se ho parametri di tipo IP non validi.
    	//TODO: ctrl estendibile per altri casi e/o altri tipi se necessario
    	if (("D".equals(actionType)))
    	{
	    	String sql1 = "select parameters from cfaction where idsite = ? and actiontype = ?";
	    	Object[] params1 = new Object[]{new Integer(idsite), actionType};
	    	
	    	RecordSet rs1 = DatabaseMgr.getInstance().executeQuery(null, sql1, params1);
	    	Record r1 = null;
	    	
	    	if ((rs1 != null) && (rs1.size() > 0))
	    	{
	    		for (int i = 0; i < rs1.size(); i++)
	    		{
	    			try
	    			{
						r1 = rs1.get(i);
						tmp = (String)r1.get(0);
						
						if (tmp != null)
						{
							tmp = tmp.replace(";", ",");
							para = para + (!"".equals(para)?",":"") + tmp;
						}
					}
	    			catch (Exception e)
	    			{
	    				e.printStackTrace();
	    				result = false;
	    				Logger logger = LoggerMgr.getLogger(ActionBeanList.class);
						logger.error(e);
					}
	    		}
	    		
	    		//se ho rilevato parametri da controllare:
	    		if (!"".equals(para))
	    		{
		    		//ctrl parameters azioni ras/remoto x presenza tel e/o ip
		    		String sql2 = "select address from cfaddrbook where idaddrbook in (" + para + ")";
		    		
		    		RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2, null);
		    		Record r2 = null;
		    		
		    		if ((rs2 != null) && (rs2.size() > 0))
		    		{
		    			for (int k = 0; k < rs2.size(); k++)
		    			{
		    				try
		    				{
								r2 = rs2.get(k);
								tmp = (String)r2.get(0);
								
								if (!isIP(tmp)) //se fra i parametri c'� almeno un n�tel
								{
									result = true;
									break;
								}
							}
		    				catch (Exception e)
		    				{
		    					e.printStackTrace();
		    					result = false;
		    					Logger logger = LoggerMgr.getLogger(ActionBeanList.class);
		    					logger.error(e);
							}
		    			}
		    		}
	    		}
	    	}
    	}
    	
    	return result;
    }
    
    // *****
    // controllo se valore � un IP valido:
    // *****
    public static boolean isIP(String valore)
    {
    	boolean resul = true;
    	try
    	{
			//String[] valori = valore.split(".");
    		String[] vals = StringUtility.split(valore, ".");
			
    		if (vals.length == 4)
			{
				try
				{
					int[] ipnums = new int[]{-1,-1,-1,-1};
					
					for (int h = 0; h < 4; h++)
						ipnums[h] = Integer.parseInt(vals[h]);
					
					for (int g = 0; (g < 4) && (resul); g++)
						resul = resul && (ipnums[g] >= 0) && (ipnums[g] <= 255);
				}
				catch (Exception e)
				{
					resul = false;
				}
			}
    		else
    		{
    			resul = false;
    		}
		}
    	catch (Exception e)
    	{
    		resul = false;
		}
    	
    	return resul;
    }
    
    public void deleteAllActionByActioncode(int idsite, int actioncode) //rimuovo action da lista
        throws Exception
    {
        DataHs dataHs = CreateSqlHs.getDeleteData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[] { new Integer(idsite), new Integer(actioncode) },
                new String[] { "=", "=" },
                new String[] { "idsite", "actioncode" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());

        String sql = "delete from cfaction where idsite = ? and actioncode = ? ";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(actioncode);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public void replaceActionWithXaction(int idsite, int actioncode)
    	throws DataBaseException
    {
    	replaceActionWithXaction(idsite, actioncode, null, "");
    }
    
    public void replaceActionWithXaction(int idsite, int actioncode, String template, String actionparams)
        throws DataBaseException
    {
        String sql = "update cfaction set actiontype = ?, template = ?, parameters = ?, lastupdate = ? where idsite = ? and actioncode = ? ";
        Object[] param = new Object[6];
        param[0] = "X";
        param[1] = template;
        param[2] = actionparams;
        param[3] = new Timestamp(System.currentTimeMillis());
        param[4] = new Integer(idsite);
        param[5] = new Integer(actioncode);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        DataHs dataHs = CreateSqlHs.getUpdateData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[] { new Integer(idsite), new Integer(actioncode) },
                new String[] { "=", "=" },
                new String[] { "idsite", "actioncode" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
    }

    public void replaceXActionWithAction(int idsite, int actioncode,
        String actiontype, String template, String parameters)
        throws DataBaseException
    {
        String sql = "update cfaction set actiontype = ?, template = ?, parameters = ?, lastupdate = ? where idsite = ? and actioncode = ? ";
        Object[] param = new Object[6];
        param[0] = actiontype;
        param[1] = template;
        param[2] = parameters;
        param[3] = new Timestamp(System.currentTimeMillis());
        param[4] = new Integer(idsite);
        param[5] = new Integer(actioncode);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        DataHs dataHs = CreateSqlHs.getUpdateData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[] { new Integer(idsite), new Integer(actioncode) },
                new String[] { "=", "=" },
                new String[] { "idsite", "actioncode" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
    }

    /**
     * @return: Map
     */
    public Map<Integer, StringBuffer> getActionCodeMap()
    {
        return actionCodeMap;
    }

    public static boolean isActionInRule(int idsite, int actioncode)
        throws DataBaseException
    {
        String sql = "select actioncode from cfrule where idsite = ? and actioncode = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(actioncode) });

        if (rs.size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isDefaultAction(int idsite, int idactioncode)
    {
    	boolean isDef = false;
    	String defTemplate = BSetAction.UNSCHEDULERTEMPLATE; //Print on all Alarms
    	
    	isDef = (idactioncode < 3);
    	
    	if (!isDef)
    	{
	    	String sql = "select actioncode from cfaction where template in"+
	    	"(select idreport from cfreportkernel where templatefile='"+defTemplate+"')";
	    //String sql = "select idaction from cfaction where template='"+defTemplate+"'";
	    	
	    	try
	    	{
	    		RecordSet rs = null;
	    		rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	    		
				if (rs != null)
					isDef = isDef || (rs.size() > 0);
			}
	    	catch (Exception e)
	    	{
	    		e.printStackTrace();
				//PVPro-generated catch block
				Logger logger = LoggerMgr.getLogger(ActionBeanList.class);
				logger.error(e);
			}
    	}
    	
    	return isDef;
    }
    
    public static void updateCodeOfAction(int idsite, int actioncode,
        String new_description) throws DataBaseException
    {
        String sql = "update cfaction set code = ?, lastupdate = ? where idsite = ? and actioncode = ?";
        Object[] param = new Object[4];
        param[0] = new_description;
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(actioncode);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        DataHs dataHs = CreateSqlHs.getUpdateData("cfaction",
                new String[]
                {
                    "idaction", "pvcode", "idsite", "code", "actioncode",
                    "actiontype", "isscheduled", "template", "parameters"
                },
                new Object[] { new Integer(idsite), new Integer(actioncode) },
                new String[] { "=", "=" },
                new String[] { "idsite", "actioncode" });

        DatabaseMgr.getInstance().executeStatement(null, dataHs.getSql(),
            dataHs.getObjects());
    }

    public void updateParametersOfAction(int idSite, int idaction, String parameters)
    	throws DataBaseException
    {
    	DatabaseMgr.getInstance().executeStatement("update cfaction set " +
    			" parameters 	= ?"+
    			" where idsite	= ? and"+
    			" idaction=?",
    			new Object[]{parameters,idSite,idaction});
    }
    
    /*
     * Sezione Automatica
     */
    public void insertAutomaticAction(int idsite,String actiontype,String parameters,String template,String desc)
    	throws DataBaseException
    {
    	int idAction = checkActionTech(idsite,desc,actiontype);
    	if(idAction == 0)
    	{
    		Integer nACode = null;
    		
    		idAction = checkActionTechExist(idsite,desc);
    		if(idAction == 0)
    		{
    			SeqMgr o = SeqMgr.getInstance();
    			nACode = o.next(null, "cfaction", "actioncode");
    		}
    		else
    			nACode = new Integer(idAction);
    		
    		this.insertAction(idsite,nACode.intValue(),parameters,actiontype,template,desc,false);
    		
            // scrittura negli eventi x creazione nuova azione:
            EventMgr.getInstance().info(idsite,"admin","Config","W019",new Object[]{desc});
    	}
    	else
    	{
    		String sql = "update cfaction set actiontype=?,template=?,parameters=? where idaction=?";
            
    		DatabaseMgr.getInstance().executeStatement(null,sql,
    								  new Object[]{actiontype,template,parameters,new Integer(idAction)});
    	}
    }
    
    private int checkActionTech(int idsite,String desc,String type)
		throws DataBaseException
	{
		int find = 0;
		String sql = "select idaction from cfaction where idsite=? and code=? and actiontype in(?,?) and isscheduled=?";
		RecordSet rs = null;
		Record r = null;
		rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),desc,type,"X","FALSE"});
		if(rs != null && rs.size() > 0)
			r = rs.get(0);
		if(r != null)
			find = ((Integer)r.get("idaction")).intValue();
		return find;
	}
    
    private int checkActionTechExist(int idsite,String desc)
		throws DataBaseException
	{
		int find = 0;
		String sql = "select actioncode from cfaction where idsite=? and code=? and isscheduled=?";
		RecordSet rs = null;
		Record r = null;
		rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),desc,"FALSE"});
		if(rs != null && rs.size() > 0)
			r = rs.get(0);
		if(r != null)
			find = ((Integer)r.get("actioncode")).intValue();
		return find;
	}
    
    public boolean checkCodeUnic(String code,String isSched)
    	throws DataBaseException
    {
    	boolean ris = false;
    	String sql = "select code from cfaction where code=? and isscheduled=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{code,isSched});
    	ris = (rs != null && rs.size() > 0);
    	return ris;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    public int checkAlarmNotificationChannel(int idsite,String language)
    throws Exception
    {
    	int num = 0;
    	String[] addedChannels = new String[6];
    	Iterator actions = actionMap.entrySet().iterator();
    	RelayBeanList relays = new RelayBeanList(idsite,language,true);
    	while(actions.hasNext())
    	{
    		Entry entry = (Entry)actions.next();
    		ActionBean actionBean = (ActionBean)entry.getValue();
    		if("F".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "F") == false)
    			{
    				if(validateAction(actionBean) == true)
    				{
    					addedChannels[num++] = "F";
    				}
    			}
    		}
    		else if("S".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "S") == false)
    			{
    				if(validateAction(actionBean) == true)
    				{
    					addedChannels[num++] = "S";
    				}
    			}
    		}
    		else if("E".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "E") == false)
    			{
    				if(validateAction(actionBean) == true)
    				{
    					addedChannels[num++] = "E";
    				}
    			}
    		}
    		else if("D".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "D") == false)
    			{
    				if(validateAction(actionBean) == true)
    				{
    					addedChannels[num++] = "D";
    				}
    			}
    		}
    		else if("L".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "L") == false)
    			{
    				if(validRelayAction(actionBean,relays) == true)
    				{
    					addedChannels[num++] = "L";
    				}
    			}
    		}
    		else if("W".equalsIgnoreCase(actionBean.getActiontype()))
    		{
    			if(GuardianConfig.channelExist(addedChannels, "W") == false)
    			{
    				if(this.validAlarmWindowAction(actionBean, idsite) == false)
    				{
    					addedChannels[num++] = "W";
    				}
    			}
    		}
    		if(num>1)
    		{
    			break;
    		}
    	}
    	return num;
    }
    //Keep alive means if there is a EMAIl action in Scheduled management.
    // then keep_alive = true, else keep_alive = false 
    public boolean checkKeepAlive()
    throws Exception
    {
    	Iterator actions = actionMap.entrySet().iterator();
    	while(actions.hasNext())
    	{
    		Entry entry = (Entry)actions.next();
    		ActionBean actionBean = (ActionBean)entry.getValue();
    		if("E".equalsIgnoreCase(actionBean.getActiontype()))
    		{
				if(validateAction(actionBean) == true)
				{
					return true;
				}
    		}
    	}
    	return false;
    }
    private boolean validateAction(ActionBean actionBean)
    {
    	String temp = actionBean.getParameters();
		if(temp != null && !temp.equals(""))
		{	
			String[] temp_s = temp.split(";");
			int[] temp_i = new int[temp_s.length];
			for(int p=0;p<temp_i.length;p++)
			{
				temp_i[p] = Integer.parseInt(temp_s[p]);
			}
			DispatcherBook[] book = DispatcherBookList.getInstance().loadReceivers(temp_i);
			for(int p=0;p<book.length;p++)
			{
				if(book[p].getIoteststatus().equalsIgnoreCase("OK"))
				{
					return true;
				}
			}
		}
		return false;
    }
    private boolean validRelayAction(ActionBean actionBean,RelayBeanList relays)
    {
    	String temp = actionBean.getParameters();
		if(temp != null && !temp.equals(""))
		{	
			String[] temp_s = temp.split(";");
			boolean first = true;
			for(int j=0;j<temp_s.length;j++)
			{
				String[] temp_s2 = temp_s[j].split("=");
				int idrelay = Integer.parseInt(temp_s2[0]);				
				RelayBean relay = relays.getRelayBeanById(idrelay);
				if(relay.getIoteststatus().equalsIgnoreCase("OK"))
				{
					return true;
				}
			}
		}
		return false;
    }
    private boolean validAlarmWindowAction(ActionBean actionBean,int idsite)
    {
    	CioEVT ioEvt = new CioEVT(idsite);
		ioEvt.loadTestStatus();
		if(ioEvt.getIoteststatus() == null || ioEvt.getIoteststatus().equalsIgnoreCase("") 
				|| !ioEvt.getIoteststatus().equalsIgnoreCase("OK"))
		{
			return false;
		}
		else
		{
			return true;
		}
    }
}
