package com.carel.supervisor.director.guardian;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class VarCheckerThread extends Thread
{
	private boolean stop = false;
    private long timestamp = 0;
    private List vars = new ArrayList();
    
    public VarCheckerThread(List vars)
    {
    	setName("VarCheckerThread");
    	this.vars = vars;
    }
    
    public void run()
    {
        while (!stop)
        {
            /*
             *  Travaglin
             *  Se l'utente nella pagina di configurazione toglie la spunta dal controllo
             *  delle variabili costanti, non deve fare nulla. 
             */
            boolean doCkeckVarK = true;
            try
            {
                String gsn = ProductInfoMgr.getInstance().getProductInfo().get("gvark");
                if(gsn != null)
                    doCkeckVarK = false;
            }
            catch(Exception e){}
            
            if(doCkeckVarK)
            {
                timestamp = System.currentTimeMillis();
                
                for(int i = 0; i < vars.size(); i++)
                {
                	try
                	{
                		((ValueLogger)vars.get(i)).refresh();
                	}
                	catch(Exception e)
                	{
                		LoggerMgr.getLogger(this.getClass()).error(e);
                	}
                }
                
                if (CheckersMgr.getInstance().areCostant())
                {
                    EventMgr.getInstance().error(new Integer(1),"SYSTEM", "Action", "G005", null);
                	CheckersMgr.getInstance().resetVariables();
                }
            }
            
            try
            {
                Thread.sleep((long)SystemConfMgr.getInstance().get("varping").getValueNum() * 1000);
            }
            catch (InterruptedException e)
            {
            }
        }
    }
    
    public void stopCheck()
    {
        this.stop = true;
    }
    
    public long getLastTimestamp()
    {
    	return timestamp;
    }
}
