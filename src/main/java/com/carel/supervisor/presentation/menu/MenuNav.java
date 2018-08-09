package com.carel.supervisor.presentation.menu;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserTransaction;
import java.util.Iterator;
import java.util.Properties;


public class MenuNav
{
    private static final String NAVSEC = "navbar";
    private static final int nummaxvoci = 5;

    public static String buildNavMenu(UserTransaction ut, String language)
    {
        UserTransaction localTrx = ut;
        StringBuffer sb = new StringBuffer("");
        LangService mlang = LangMgr.getInstance().getLangService(language);
        String forUser = "";
        String sCode = "";
        String newnavmenu = "";
        int lio = 1;
        int nvoci = 1;

        do
        {
            sCode = localTrx.getTrxLabel();

            if (sCode == null)
            {
                sCode = "xxx";
            }

            if (localTrx.hasChild())
            {
                sb.append(
                    "<a href=\\\"#\\\" onclick=\\\"top.frames['manager'].loadTrx('");
                sb.append(buildLinkNav(localTrx));
                sb.append("');return false;\\\">");
                forUser = mlang.getString(NAVSEC, sCode);
                sb.append(("".equals(forUser)) ? sCode : forUser);
                sb.append("</a> > ");
            }
            else
            {
                forUser = mlang.getString(NAVSEC, sCode);
                sb.append(("".equals(forUser)) ? sCode : forUser);
            }

            localTrx = localTrx.getChild();
        }
        while (localTrx != null);

        //gestione numero max voci visualizzabili nel NavMenu:
        while ((nvoci < nummaxvoci) && (lio > 0))
        {
	        lio = sb.lastIndexOf("<a href");
	        
	        if (lio > 0)
	        {
	        	newnavmenu = sb.substring(lio) + newnavmenu;
	        	sb.delete(lio, sb.length());
	        	nvoci += 1;
	        }
	        else
	        {
	        	newnavmenu = sb.toString() + newnavmenu;
	        }
        }
        
        return newnavmenu;
    }
    public static String getBack(UserTransaction ut, String language)
    {
        UserTransaction localTrx = ut;
        UserTransaction temp = null;
        StringBuffer sb = new StringBuffer("");
        boolean canBack = false;

        do
        {
        	if(localTrx.hasChild() == true)
        	{
        		canBack = true;
        	}
        	else
        	{
        		break;
        	}
        	temp = localTrx;
            localTrx = localTrx.getChild();
            if (canBack == true && localTrx.getChild() == null)
            {
                sb.append("top.frames['manager'].loadTrx('");
                sb.append(buildLinkNav(temp));
                sb.append("');return false;");
                break;
            }
        }
        while (true); 
        return sb.toString();
    }


    private static String buildLinkNav(UserTransaction ut)
    {
        StringBuffer sb = new StringBuffer("");
        Properties prop = ut.getProperties();
        String key = "trx";
        String val = "";

        // Put firt the TRX param
        val = prop.getProperty(key);

        if ((val != null) && (val.length() > 0))
        {
            sb.append(val + "&");
        }

        Iterator i = prop.keySet().iterator();

        while (i.hasNext())
        {
            key = (String) i.next();

            if ((key != null) && (!key.equalsIgnoreCase("trx")))
            {
                val = prop.getProperty(key);
                sb.append(key + "=" + val + "&");
            }
        }

        sb.append("navid=" + ut.getTrxId());

        return sb.toString();
    }
}
