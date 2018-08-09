package com.carel.supervisor.dispatcher.action;

import java.sql.Timestamp;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.tech.ITech;

public class TAction extends DispatcherAction 
{
	private static final String TPATH = "com.carel.supervisor.dispatcher.tech";
	private static final String SPLIT = ";";
	
	public TAction(Integer key,Integer id,Integer pri,Integer sta,String rec,Timestamp itime,Timestamp utime,
				   String tmpl,String type,Boolean isalarm,Integer idvar,Timestamp startTime,Timestamp endTime) 
	{
		super(key,id,pri,sta,rec,itime,utime,tmpl,type,isalarm,idvar,startTime,endTime);
	}

	protected String[] initializedRecepients(String recepient) 
	{
		String[] recRet = new String[0];
        if (recepient != null)
            recRet = recepient.split(SPLIT);
        return recRet;
	}
	
	public int[] putActionInQueue() 
		throws Exception
    {
		HSActionBeanList actionbean = new HSActionBeanList();
		String[] techAction = this.getRecepients();
		ITech tech = null;
		int[] ret = new int[0];
		int idAct = -1;
		
		try
        {
            idAct = ((Integer) this.getKeyAction().get(0)).intValue();
            actionbean.updateToSendActionList(new int[] { idAct }, true);
            
            if(techAction != null)
            {
            	for(int i=0; i<techAction.length; i++)
            	{
            		tech = initializeObject(techAction[i]);
            		if(tech != null)
            			tech.doTechAction();
            	}
            }
        }
        catch (Exception e){
        	Logger logger = LoggerMgr.getLogger(this.getClass());
        	logger.error(e);
        }
        
		return ret;
    }
	
	private ITech initializeObject(String sClass)
	{
		ITech it = null;
		sClass = TPATH+"."+sClass;
		try
        {
			it = (ITech) FactoryObject.newInstance(sClass);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

		return it;
	}
}
