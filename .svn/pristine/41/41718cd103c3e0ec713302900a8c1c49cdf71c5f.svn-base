package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.SystemConf;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class AlreadyManagedStatus extends AbstractStatus
{
    public AlreadyManagedStatus()
    {
        super(4);
    }
    
    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().alreadyManagedStatus(rule, now);	
    }

    public void executeAfterRetrieve(Rule rule, Date now) throws Exception
    {
    	rule.getAction().aRalreadyManagedStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        if (contextState.isManualReset())
        {
        	if(contextState.getActualStatusBeforeManualReset() == null)
        	{
        		contextState.setActualStatusBeforeManualReset(StatusRegistry.alreadyManagedStatus);
        	}
            contextState.setManualReset(false);
            RuleStateBean.update(contextState, StatusRegistry.blockedStatus);

            return StatusRegistry.blockedStatus;
        }
        else
        {
            if (!contextState.isStatus())
            {
                if (contextState.isTimebandOk())
                {
                    return StatusRegistry.calledOffStatus;
                }
                else
                {
                    RuleStateBean.remove(contextState);
                    contextState.setActivationTime(0);

//                  Rimuoviamo l'allarme dalla tabella di controllo perch� rientrato
                    AlarmCtrl.calledOff(contextState.getIdVar());
                    
                    return StatusRegistry.invalidStatus;
                }
            }
            else
            {
            	Integer idvariable  = contextState.getIdVar();
    			Integer idRule = contextState.getIdRule();
    			Rule rule = RuleMgr.getInstance().getRule(idvariable, idRule);
    			// First : There is a lot of AlarmRule , so we need let them go first.
    			if(rule.getClass().equals(Rule.class) && idRule != null && idvariable != null)
    			{
    				// step1 : frist time toManageStatus
        			if(contextState.isResendNotification()==null ){
        				SystemConf cfg = SystemConfMgr.getInstance().get("resend_enable");
        				if(cfg != null && cfg.getValue().equals("TRUE"))
		            	{	
	            			if( !contextState.isAcknowledged())
	            			{
                				String sql = "select actiontype from cfaction where actioncode in (select actioncode from cfrule where idrule = '"+rule.getIdRule()+"') ";
                        		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
                        		boolean channelOK = false ;
                        		for(int i=0;i<rs.size();i++){
                        			cfg = SystemConfMgr.getInstance().get("resend_channel_"+(String)rs.get(i).get(0));
                					if (cfg!=null && cfg.getValue().equals("TRUE")){
                							channelOK = true;
                							break;
                					}
                				} 
                        		
                        		boolean priorityOK = false;
                        		cfg = SystemConfMgr.getInstance().get("resend_priority_"+rule.getPriority());
                        		if(cfg != null && cfg.getValue().equals("TRUE"))
                        			priorityOK = true;
                        		if(channelOK && priorityOK)
                        			contextState.setResendNotification(true);
                        		else
                        			contextState.setResendNotification(false);
		            		}
		            		else
		            			contextState.setResendNotification(false);
        				}
        				else
        					contextState.setResendNotification(false);
        			}
            	    // step2:  more than one times toManageStatus
        			else if(contextState.isAcknowledged()&&contextState.isResendNotification() != null && contextState.isResendNotification())
        				contextState.setResendNotification(false);
        			if(contextState.isResendNotification() != null &&  contextState.isResendNotification()){
        				int c_freq = Integer.parseInt(SystemConfMgr.getInstance().get("resend_frequency").getValue());
        				int c_times = Integer.parseInt(SystemConfMgr.getInstance().get("resend_times").getValue());
        				int finisheEnd = c_freq*c_times*60*1000+5*60*1000;//add 5 minutes more because there is delay
            			if( ((contextState.getActualTime()-contextState.getLastExecuteTime()) >=c_freq*60*1000)  && (contextState.getActualTime() <= contextState.getActivationTime()+finisheEnd )  )
            				return StatusRegistry.toManageStatus;
            			else if(contextState.getActualTime() > contextState.getActivationTime()+finisheEnd)//not valid anymore,set resendNotification to false
            			{
            				contextState.setResendNotification(false);
            			}
        			}
    			}
    			return StatusRegistry.alreadyManagedStatus;
            }
        }
    }
}
