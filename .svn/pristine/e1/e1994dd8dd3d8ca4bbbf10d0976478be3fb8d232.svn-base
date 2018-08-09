package com.carel.supervisor.director.guardian;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;

import java.util.ArrayList;
import java.util.List;


public class CheckersMgr
{
    private static CheckersMgr me = new CheckersMgr();
    private ArrayList<ValueLogger> vars = new ArrayList<ValueLogger>();
    private VarCheckerThread thread = null;
    private GuardianChecker guardian = null;

    private CheckersMgr()
    {
    }

    public static CheckersMgr getInstance()
    {
        return me;
    }

    public synchronized void stop()
    {
        if (null != thread)
        {
            thread.stopCheck();
            thread = null;

            for (int i = 0; i < vars.size(); i++)
            {
                ((ValueLogger) vars.get(i)).clear();
            }
        }
        if (null != guardian)
        {
        	guardian.stopCheck();
        	guardian = null;
        }
    }

    public synchronized List<Integer> start(String user)
    {
    	List<Integer> ids = null;
        try
        {
	    	stop();
	        ids = reloadVars();
	        String gsn = ProductInfoMgr.getInstance().getProductInfo().get("gvark");
	        if(vars.size() > 0 && gsn == null){
	        	thread = new VarCheckerThread(vars);
		        thread.start();
	        }
	        guardian = new GuardianChecker();
	        guardian.start();
        }
        catch(Exception e)
        {
        	LoggerMgr.getLogger(this.getClass()).error(e);
        	EventMgr.getInstance().error(new Integer(1),user, "Action", "G004", null);
        }
        return ids;
    }

    public boolean areCostant()
    {
    	boolean costant = true;
        for (int i = 0; i < vars.size(); i++)
        {
            costant = costant && (((ValueLogger) vars.get(i)).isCostant());
        }
        if (0 == vars.size())
        {
        	return false;
        }
        return costant;
    }

    public boolean isGuardianRunning()
    {
    	if (null != guardian)
    	{
    		return guardian.isGuardianRunning();
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public void resetVariables()
    {
        for (int i = 0; i < vars.size(); i++)
        {
            ((ValueLogger) vars.get(i)).clear();
        }
    }
    
    private List<Integer> reloadVars() throws Exception
    {
    	ArrayList<Integer> ids = new ArrayList<Integer>();
    	vars = new ArrayList<ValueLogger>();
        String sql = "select idvariable from cfvarguardian";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        Integer id = null;
        ValueLogger value = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            id = (Integer) recordset.get(i).get(0);
            value = new ValueLogger(id);
            vars.add(value);
            ids.add(id);
        }

        return ids;
    }
    
    public void stopForImport()
    {
    	if(guardian != null)
        	this.guardian.setNop();
    }
    
    public void startAfterImport()
    {
    	if(guardian != null)
    		this.guardian.resetNop();
    }
}
