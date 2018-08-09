package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.sim.DispSimMgr;
import com.carel.supervisor.presentation.bean.rule.RelayBean;
import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import java.util.Properties;


public class BTestIo extends BoMaster
{
	private static final long serialVersionUID = -390828102075296762L;
	private static final int REFRESH_TIME = -1;

    public BTestIo(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "enableAction(1);onload_testio();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "testio.js");

        return p;
    }

    public static String createSelect(String type)
    {
        StringBuffer sb = new StringBuffer();

        try
        {
            DispatcherBook[] addrBook = DispatcherMgr.getInstance().getReceiverInfoByType(type);
            sb.append("<option value=\"nop\">---------------</option>\n");

            for (int i = 0; i < addrBook.length; i++)
            {
                sb.append("<option value=\"" + addrBook[i].getKey() + "\">" +
                    addrBook[i].getReceiver() + "</option>\n");
            }
        }
        catch (Exception e)
        {
        }

        return sb.toString();
    }

    public static String createSelectRelay(int idsite, String language)
    {
        StringBuffer sb = new StringBuffer();

        try
        {
            RelayBeanList rbl = new RelayBeanList(idsite, language, true);
            RelayBean rb = null;
            sb.append("<option value=\"nop\">---------------</option>\n");

            if (rbl != null)
            {
                for (int i = 0; i < rbl.size(); i++)
                {
                    rb = rbl.getRelayBean(i);

                    if (rb != null && rb.getShow())
                    {
                        sb.append("<option value=\"" + rb.getIdvariable() + "\">" +
                            "[" + rb.getDeviceDesc() + "] ---> " + rb.getDescription() + "</option>\n");
                    }
                }
            }
        }
        catch (Exception e)
        {
        }

        return sb.toString();
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        if (tabName.equalsIgnoreCase("tab1name"))
        {
            String pvcode = BaseConfig.getPlantId();
            String devy = prop.getProperty("tio");
            //String type = prop.getProperty("tiotype");
            String dest = "";
            String valu = prop.getProperty("trelevalue");

            //if (type.equalsIgnoreCase("libero"))
            //{
            //   dest = prop.getProperty("taddrlib");
            //}
            //else
            //{
                dest = prop.getProperty("tioaddress");
            //}

            DispSimMgr.getInstance().setTestIo(pvcode, devy, dest, valu, us.getUserName(),
                us.getLanguage());
        }
    }
}
