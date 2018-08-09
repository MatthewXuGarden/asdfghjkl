package com.carel.supervisor.presentation.helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.assistance.UserInMgr;
import com.carel.supervisor.presentation.session.UserSession;


public class ServletHelper
{
    //private static long SESSION_TIMEOUT = 900000L;
	private static long SESSION_TIMEOUT = BaseConfig.getSession();
	
	// used to prevent re-use of the last user/pass posted from browser's memory
	private static final int NO_OF_INVALID_SESSIONS = 50;
	private static String astrInvalidSessions[] = new String[NO_OF_INVALID_SESSIONS];
	private static int iptrInvalidatedSessions = 0;
	
    public static final String PARAM_TRXNAME = "trx";
    public static final String PARAM_USER = "txtUser";
    public static final String PARAM_PASSWORD = "txtPassword";
    public static final String PARAM_USERLANG = "txtLanguage";
    public static final String PARAM_BROWSER = "browser";
    public static final String PARAM_ERROR = "error";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_SESSION = "sessione";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_BO = "bo";
    public static final String PARAM_DESC = "desc";
    public static final String PARAM_HTMLTAB = "htmlMenuTab";
    public static final String PARAM_NAVID = "navid";
    public static final String PARAM_CMD = "cmd";

    private ServletHelper()
    {
    }

    public static Properties retrieveParameters(HttpServletRequest request)
    {
        Properties properties = new Properties();
        Enumeration param = request.getParameterNames();
        String name = null;
    	while (param.hasMoreElements())
        {
            name = param.nextElement().toString();
            properties.setProperty(name, request.getParameter(name).toString());
        }
        
        //Svincolo variabili
        String idDev = properties.getProperty("iddev");
        
        if(idDev != null)
        {
        	
        	try {
				properties.setProperty("iddev", Integer.toString(VDMappingMgr.getInstance().getDeviceId(idDev)));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return properties;
    }

    public static final UserSession retrieveSession(String token, HttpServletRequest request)
    {
        UserSession session = null;

        try
        {
            HttpSession httpSession = request.getSession();
            session = (UserSession) httpSession.getAttribute(PARAM_SESSION);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }

        return session;
    }

    public static final void saveSession(UserSession session, HttpServletRequest request)
    {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(PARAM_SESSION, session);
    }

    public static final boolean validateSession(UserSession session)
    {
        if (null == session)
        {
            return false;
        }
        else
        {
            if(!UserInMgr.getInstance().validUserIn(session.getSessionId()))
                return false;
            
            if (0 == session.getLastTime())
            {
                session.setLastTime(System.currentTimeMillis());

                return true;
            }

            if ((Math.abs(session.getLastTime() - System.currentTimeMillis())) > SESSION_TIMEOUT)
            {
                return false;
            }
            else
            {
                session.setLastTime(System.currentTimeMillis());

                return true;
            }
        }
    }
    
    /*
     * Controlla la validit� della sessione, senza aggiornarla.
     * Utilizzato da:
     * - Refresh.java
     */
    public static final boolean validateSessionWithoutSet(UserSession session)
    {
    	if (null == session)
            return false;
        else
        {
        	if (0 == session.getLastTime())
                return true;
        	
        	if ((Math.abs(session.getLastTime() - System.currentTimeMillis())) > SESSION_TIMEOUT)
                return false;
        	else
        		return true;
        }
    }
    
    
    public static final void invalidateSession(String token, HttpServletRequest request)
    {
        HttpSession httpSession = request.getSession();
        // keep tracks of invalid session id
        if( !isInvalidId(httpSession.getId()) ) {
	        astrInvalidSessions[iptrInvalidatedSessions++] = httpSession.getId();
	        if(iptrInvalidatedSessions >= NO_OF_INVALID_SESSIONS)
	        	iptrInvalidatedSessions = 0;
        }
        httpSession.invalidate();
    }

    
    public static boolean isInvalidId(String strSessionId)
    {
    	for(int i = 0; i < NO_OF_INVALID_SESSIONS; i++)
    		if( strSessionId.equals(astrInvalidSessions[i]) )
    			return true;
    	return false;
    }
    
    
    public static final void saveParameters(HttpServletRequest request, Properties properties)
    {
        UserSession userSession = retrieveSession(null, request);
        Object oVal = null;
        String key = "";
        String val = "";

        if (properties != null)
        {
            Iterator i = properties.keySet().iterator();

            while (i.hasNext())
            {
                key = (String) i.next();
                oVal = properties.get(key);

                if (oVal instanceof String)
                {
                    val = (String) oVal;
                    userSession.setProperty(key, val);
                }
            }
        }
    }

    public static final String[] retrieveVector(Properties properties, String tag)
    {
        Map map = new HashMap();
        String key = null;
        Iterator iterator = properties.keySet().iterator();
        List keys = new ArrayList();

        while (iterator.hasNext())
        {
            key = (String) iterator.next();

            if (key.startsWith(tag))
            {
                map.put(key.substring(tag.length()), properties.getProperty(key));
                keys.add(key);
            }
        }

        for (int i = 0; i < keys.size(); i++)
        {
            properties.remove((String) keys.get(i));
        }

        String[] values = new String[map.size()];

        for (int i = 0; i < map.size(); i++)
        {
            values[i] = (String) map.get(String.valueOf(i));
        }

        return values;
    }

    public static String messageToNotify(String language)
    {
        String message = "";

        if(DirectorMgr.getInstance().isDebugSessionOn())
        {
        	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
            message = multiLanguage.getString("top", "message4");
        }
        else if (DirectorMgr.getInstance().isStopped())
        {
            LangService multiLanguage = LangMgr.getInstance().getLangService(language);
            message = multiLanguage.getString("top", "message2");
        }
        else if (DirectorMgr.getInstance().isMustRestart())
        {
            LangService multiLanguage = LangMgr.getInstance().getLangService(language);
            message = multiLanguage.getString("top", "message1");
        }

        return message;
    }
    public static int messageToNotify()
    {
        int message = 0;

        if(DirectorMgr.getInstance().isDebugSessionOn())
        {
            message = 1;
        }
        else if (DirectorMgr.getInstance().isStopped())
        {
            message = 2;
        }
        else if (DirectorMgr.getInstance().isMustRestart())
        {
            message = 3;
        }

        return message;
    }

    /**
     * @param language
     * @return
     */
    public static String notifyGuardian(String language)
    {
        String message = "";

        if (!GuardianConfig.userConfGuiVariable())
        { //locale con variabili non configurate
            int topGlbLinkGui = 5;
            LangService multiLanguage = LangMgr.getInstance().getLangService(language);
            message = topGlbLinkGui + multiLanguage.getString("top", "message3");
        }
        else if (!GuardianConfig.userConfGuiChannel())
        { //remoto, oppure locale con variabili configurate
            int topGlbLinkGui = 6;
            LangService multiLanguage = LangMgr.getInstance().getLangService(language);
            message = topGlbLinkGui + multiLanguage.getString("top", "message3");
        }

        return message;
    }

	public static long getSESSION_TIMEOUT() {
		return SESSION_TIMEOUT;
	}

	public static void setSESSION_TIMEOUT(long session_timeout) {
		SESSION_TIMEOUT = session_timeout;
	}
	
	public static boolean isMobileDevice(HttpServletRequest request)
	{
		// Windows Mobile
		// Windows Mobile 5: Mozilla/4.0 (compatible; MSIE 4.01; Windows CE; PPC; 240x320)
		// Windows Mobile 6: Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 8.12; MSIEMobile 6.0)
		String userAgent = request.getHeader("User-Agent");
	    String httpAccept = request.getHeader("Accept");
		UserAgentHelper detector = new UserAgentHelper(userAgent, httpAccept);
		
		return detector.detectTierTablet() || detector.detectTierIphone();
	}
}
