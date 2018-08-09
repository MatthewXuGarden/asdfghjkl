package com.carel.supervisor.controller.priority;

import java.util.HashMap;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

/*
 * ----- NOTE -----
 *
 * Valori di priorita' letti dalla tabella "cfpriority" con la seguente valenza:
 *  valore < default ==> ordinamento stretto nella coda dei set (vedere classe GeneralQueue);
 *  	eventualmente usabile anche come etichetta per scodamento (vedere caso successivo).
 *  valore > default ==> no ordinamento, va' in coda; solo etichetta per eventuale scodamento allo stop del
 * 		sotto-motore del plugin/modulo.
 *  valore = default ==> no ordinamento, va' in coda; no etichetta; x compatibilita' con set standard.
 *  nessun valore ==> come x valore di default.
 *
 * ----------------
 */
public class PriorityMgr extends InitializableBase //perche' inserita in SuperVisorConfig.xml
{
	public int DEFAULT_PRIORITY = 100;
	
	private final String PRIORITY_FLD = "priorityvalue";
	private final String CLASSNAME_FLD = "classname";
	
	public static final String DEFAULT_LBL = "Default";
	
    private static PriorityMgr instance = new PriorityMgr();

	private HashMap<String,Integer> itemsPriority = null;
	
    private PriorityMgr()
    {
    	/*
    	if ((itemsPriority == null) || ((itemsPriority != null) && (itemsPriority.size() == 0)))
		{
    		load();
		}
		*/
    }
    
    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
    	load();
    }
    
    public static PriorityMgr getInstance()
    {
        return instance;
    }
    
    public void load()
    {
        RecordSet rs = null;
        String sql = "select classname,priorityvalue from cfpriority";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(PriorityMgr.class);
            logger.error(e);
        }
        
        if ((rs != null) && (rs.size() > 0))
        {
            itemsPriority = new HashMap<String,Integer>();
        	
        	for (int i = 0; i < rs.size(); i++)
        	{
        		Record rec = rs.get(i);
        		
        		String class_name = "";
				try
				{
					class_name = (String)rec.get(CLASSNAME_FLD);
				}
				catch (Exception e)
				{
					class_name = DEFAULT_LBL;
				}
        		
				Integer priority = null;
				try
				{
					priority = (Integer)rec.get(PRIORITY_FLD);
				}
				catch (Exception e)
				{
					priority = new Integer(DEFAULT_PRIORITY);
				}
        		
        		itemsPriority.put(class_name, priority);
        		
        		if (class_name.equals(DEFAULT_LBL))
        		{
        			DEFAULT_PRIORITY = priority;
        		}
        	}
        }
    }
    
	public int getPriority(String className)
	{
		int pri = DEFAULT_PRIORITY;
		String item = "";
		
		try
		{
			String[] names = className.split("\\.");
			item = names[names.length - 1];
		}
		catch (Exception e)
		{
			item = "";
		}
		
		if ((itemsPriority != null) && (itemsPriority.size() > 0) && (itemsPriority.containsKey(item)))
		{
			pri = itemsPriority.get(item).intValue();
		}
		
		return pri;
	}
	
	public int getDefaultPriority()
	{
		return getPriority(DEFAULT_LBL);
	}
}