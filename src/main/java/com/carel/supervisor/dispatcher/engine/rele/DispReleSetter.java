package com.carel.supervisor.dispatcher.engine.rele;

import java.util.TimerTask;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.setfield.BackGroundCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;


public class DispReleSetter extends TimerTask
{
    int idVariable = 0;
    String value = "";
    private String language = "";

    public DispReleSetter(int idVar, String val, String language)
    {
        this.idVariable = idVar;
        this.value = val;
        this.language = language;
    }

    public void run()
    {
        try
        {
            if ((this.value != null) && this.value.equalsIgnoreCase("1"))
            {
                this.value = "0";
            }
            else
            {
                this.value = "1";
            }

            int[] idvars = new int[] { this.idVariable };
            String[] values = new String[] { this.value };
            
            SetContext setContext = new SetContext();
            String lang = "EN_en";
    		try
    		{
    			lang = LangUsedBeanList.getDefaultLanguage(1);
    		}
    		catch (Exception e)
    		{
    		}
    		setContext.setLanguagecode(lang);
        	setContext.addVariable(idvars, values);
        	setContext.setCallback(new BackGroundCallBack());
    		setContext.setUser("Dispatcher");
            SetDequeuerMgr.getInstance().add(setContext);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
}
