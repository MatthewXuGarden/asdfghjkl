package com.carel.supervisor.plugin.parameters.action;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.action.DispatcherAction;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;


public class SXAction extends DispatcherAction
{
    public static final String SPLIT = ";";
    private String message = "";

    public SXAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
    }

    protected String[] initializedRecepients(String recepient)
    {
        String[] recRet = new String[0];

        if (recepient != null)
        {
            recRet = recepient.split(SPLIT);
        }

        Arrays.sort(recRet);

        return recRet;
    }

    public void buildTemplate(String pathDir) throws Exception
    {
    	
    	String siteName="";
		String[] infoSender = this.getInfoSender(this.getIdSite());
		if ((infoSender != null) && (infoSender.length > 0)) {
			siteName = infoSender[0];
		}
		
		System.out.println("");
		for (int i = 0; i < this.getIdVariable().size(); i++) {
			String lang = LangUsedBeanList.getDefaultLanguage(1);
			ParametersEvent pe = new ParametersEvent(lang, this.getIdVariable().get(i));
			message +=siteName+" "+pe.getDatetime().toString()+":"+ pe.getEventtype()+" "+
	           pe.getUsername() + " "+ 
	           pe.getDev_descr() + " - " + pe.getVar_descr() +" "+
	           pe.getStartingvalue() + " - > "+pe.getEndingvalue()+
	           "";
		}

    }

    public int[] putActionInQueue() throws Exception
    {
        Integer keyact = ((Integer) this.getKeyAction().get(0));
        String actkey = String.valueOf(keyact.intValue());

        HSActionQBeanList actionQList = new HSActionQBeanList();

        HSActionQBean actionQ = null;
        Integer key = null;

        boolean allOk = true;

        String[] receiver = this.getRecepients();
        String path = DispatcherMgr.getInstance().getProviderPath() +
            DispatcherMgr.getInstance().getProviderName();

        if (receiver != null)
        {
            for (int i = 0; i < receiver.length; i++)
            {
                try
                {
                    key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                    actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
                            this.getIdSite(), this.getPriority(), this.getRetryNum(),
                            this.getRetryAfter(), this.getFisicDevice(), this.getTypeAction(), 1,
                            path, this.message, receiver[i], actkey);

                    actionQList.addAction(actionQ);
                }
                catch (Exception e)
                {
                    allOk = false;

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }

            if (allOk)
            {
                allOk = actionQList.insertActions();
            }
        }

        if (allOk)
        {
            return new int[] { keyact.intValue() };
        }
        else
        {
            return new int[0];
        }
    }
    
	@Override
	public boolean compareAction(DispatcherAction da) {
		//gli SMS non si possono accorpare
		return false;
	}
}
