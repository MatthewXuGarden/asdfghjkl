package supervisor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.plugin.customview.CustomView;
import com.carel.supervisor.plugin.customview.CustomViewMgr;
import com.carel.supervisor.presentation.bean.*;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.bo.master.IMaster;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class Master extends HttpServlet
{
    private static final String CONTENT = "text/html; charset=UTF-8";
    private static final String TYPE_MENU = "menu";
    private static final String TYPE_TAB = "tab";
    private static final String TYPE_CLICK = "click";
    private static final String TYPE_REDIRECT = "redirect";
    private static final String TABTOLOAD = "archtabtoload";
    private static final String GOBACK = "archgoback";
    private static final String FRAMESET_PATH = "/arch/include/FramesetTab.jsp";
    private static final String TABBODY_PATH = "/arch/include/Tabbody.jsp";
    private static final String ERROR_PATH = "/arch/include/Error.jsp";
    private static final String SESSI_PATH = "/arch/include/TabSession.jsp";
    private static final String BO_PACKAGE = "com.carel.supervisor.presentation.bo.";
    private String transactionFolder = "";

    public void init() throws ServletException
    {
        transactionFolder = "/app/";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        controller(request, response, "GET");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        controller(request, response, "POST");
    }

    public void controller(HttpServletRequest request,
        HttpServletResponse response, String method)
        throws ServletException, IOException
    {
    	RequestDispatcher dispatcher = null;
        IMaster oMaster = null;
        UserSession userSession = null;

        response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Retrive UserSession
        userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
        
        //when tomcat is OFF, and PVPRO page is still open, userSession is null
        if(userSession == null)
        	return;
        
        // login redirect
        LoginRedirectBean loginRedirect = new LoginRedirectBean(userSession);

        if (ServletHelper.validateSession(userSession))
        {
            // Retrive parameters from querystring
            Properties properties = ServletHelper.retrieveParameters(request);

            ServletHelper.saveParameters(request, properties);

            String curTab = properties.getProperty("curTab");

            if ((null != curTab) && (!curTab.equals("")))
            {
                //controllo x gestione chiamata da mappa by LayoutEditor a SubTabx.jsp (x>1): 
            	if ((userSession.getProperty("maps") != null) && ("y".equals(userSession.getProperty("maps"))))
                	curTab = "";
            	//Kevin comment because some one comment the else below, so my "else if" is not necessary any more
//            	else if(userSession.getProperty("type") != null && userSession.getProperty("type").equals("click")
//            			&& userSession.getProperty("folder") != null && (userSession.getProperty("folder").equals("setaction")||userSession.getProperty("folder").equals("setaction2")))
//            	{
//            		curTab = "";
//            	}
                //else
                	//userSession.getCurrentUserTransaction().setProperty("curTab", curTab);
            }

            String type = properties.getProperty(ServletHelper.PARAM_TYPE);
            String navid = properties.getProperty(ServletHelper.PARAM_NAVID);

            // GET METHOD
            if (method.equalsIgnoreCase("GET"))
            {
                if ((type != null) &&
                        ((type.equalsIgnoreCase(TYPE_MENU)) ||
                        (type.equalsIgnoreCase(TYPE_CLICK)) ||
                        (type.equalsIgnoreCase(TYPE_REDIRECT))))
                {
                    try
                    {
                        // login redirect
                        if( loginRedirect.isRedirect() )
                        	loginRedirect.setSessionProperties(properties);
                    	
                    	String className = properties.getProperty(ServletHelper.PARAM_BO);

                        if (className.equals("nop"))
                        {
                            className = "master.BoDummy";
                        }
                        
                        oMaster = createBusinessObject(userSession.getLanguage(), className);
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(Master.class);
                        logger.error(e);
                    }

                   
                    UserTransaction ut = null;

                    // Remove transaction and its childs
                    if ((navid != null) && (navid.length() > 0))
                    {
                    	int nId = Integer.parseInt(navid);
                    	
                    	if (nId > 0)
                        {
	                    	ut = userSession.getRootUserTransaction();

	                        while ((ut.hasChild()) && (ut.getTrxId() != nId))
	                        {
	                            ut = ut.getChild();
	                        }
	                        
	                        ut.removeChild();
                        }
                        else
                        {
                        	while (nId < 0)
	                        {
	                            goBackTrx(userSession);
	                            nId++;
	                        }
                        }
                    }
                    else
                    {
                        //userSession.removeProperty("curTab");
                        if (type.equalsIgnoreCase(TYPE_MENU))
                        {
                            // Build new UserTransaction - START
                            ut = new UserTransaction(properties.getProperty(ServletHelper.PARAM_BO),
                                 properties.getProperty(ServletHelper.PARAM_DESC));
                            ut.setProperties(properties);
                            ut.setBoTrx(oMaster);
                            
                            // Controllo nel caso in cui la transazione necessiti di un CustomBO
                            if(properties.getProperty("folder") != null && 
                               properties.getProperty("folder").equalsIgnoreCase("dtlview"))
                            {
                            	IMaster custom = branchForCustomPost(userSession);
                            	if(custom != null)
                            		ut.setBoCustom(custom,properties.getProperty("folder"),"SubTab1.jsp");
                            }
                            
                            userSession.addNewUserTransaction(ut);
                            // END

                            // login redirect
                            if( loginRedirect.isRedirect() )
                            	loginRedirect.setTransactionProperties();
                        }
                        else if (type.equalsIgnoreCase(TYPE_CLICK))
                        {
                        	boolean permission = true;
                        	
                        	// Add UserTransaction - START	
                            ut = new UserTransaction(properties.getProperty(
                                        ServletHelper.PARAM_BO),
                                    properties.getProperty(
                                        ServletHelper.PARAM_DESC));
                            ut.setProperties(properties);
                            ut.setBoTrx(oMaster);
                            
                            // Controllo nel caso in cui la transazione necessiti di un CustomBO
                            if(properties.getProperty("folder") != null && 
                               properties.getProperty("folder").equalsIgnoreCase("dtlview"))
                            {
                            	int iddev = Integer.parseInt(properties.getProperty("iddev"));
                            	permission = userSession.isDeviceVisible(iddev);
                            	if (!permission)
                            	{
                            		userSession.setProperty("deviceaccess", "no");
                            	}
                            	
                            	IMaster custom = branchForCustomPost(userSession);
                             	if(custom != null)
                             		ut.setBoCustom(custom,properties.getProperty("folder"),"SubTab1.jsp");
                            }
                            
                            if (permission)
                            	userSession.addUserTransaction(ut);

                            // END
                        }
                        else if (type.equalsIgnoreCase(TYPE_REDIRECT))
                        {
                        	ut = userSession.getCurrentUserTransaction();
                        	
                            if(properties.getProperty("folder") != null && 
                               properties.getProperty("folder").equalsIgnoreCase("dtlview"))
                            {
                            	IMaster custom = branchForCustomPost(userSession);
                             	if(custom != null)
                             		ut.setBoCustom(custom,properties.getProperty("folder"),"SubTab1.jsp");
                                
                                // Open Table
                                try 
                                {
                                    String newID = properties.getProperty("iddev");
                                    String oldID = ut.getProperty("iddev");
                                    if(newID != null && oldID != null && !newID.trim().equalsIgnoreCase(oldID.trim()))
                                        ut.setProperty("openwt","N");
                                }
                                catch(Exception e){
                                }
                                // Fine
                            }
                            
                            if (properties.getProperty("iddev") != null)
                            	ut.setProperty("iddev", properties.getProperty("iddev"));
                            
                        	dispatcher = getServletContext().getRequestDispatcher(TABBODY_PATH);
                        }
                    }

                    // Dispatch to Frameset Arch
                    dispatcher = getServletContext().getRequestDispatcher(FRAMESET_PATH);
                }
                else if ((type != null) && (type.equalsIgnoreCase(TYPE_TAB)))
                {
                    UserTransaction ut = userSession.getCurrentUserTransaction();
                    String key = "";
                    String val = "";

                    Iterator i = properties.keySet().iterator();

                    while (i.hasNext())
                    {
                        key = (String) i.next();
                        val = (String) properties.get(key);

                        if ((key != null) &&
                                !(key.equalsIgnoreCase(
                                    ServletHelper.PARAM_TRXNAME)) &&
                                !(key.equalsIgnoreCase(ServletHelper.PARAM_TYPE)))
                        {
                            ut.setProperty(key, val);
                        }
                    }

                    dispatcher = getServletContext().getRequestDispatcher(TABBODY_PATH);
                }
                else
                {
                    dispatcher = getServletContext().getRequestDispatcher(getRedirectPath(
                                properties));
                }
            }

            // POST METHOD
            else if (method.equalsIgnoreCase("POST"))
            {
                // Get Tab to load
                String tab2load = "-1";

                try
                {
                    tab2load = properties.getProperty(TABTOLOAD);

                    if (tab2load == null)
                    {
                        tab2load = "-1";
                    }
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(Master.class);
                    logger.error(e);
                }

                // Go Back
                boolean goBack = false;

                try
                {
                    goBack = Boolean.parseBoolean(properties.getProperty(GOBACK));
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(Master.class);
                    logger.error(e);
                }
                
                UserTransaction ut = null;
                
                try
                {
                    ut = userSession.getCurrentUserTransaction();

                    if (ut != null)
                    {
                        String resource = ut.getProperties().getProperty("resource");

                    	lockUserFlow(userSession, ut.getProperty("folder"));
                        ut.setTabToLoad(tab2load);

                        if ((null == resource) ||
                                (-1 == resource.indexOf("note.jsp")))
                        {
                            if (ut.hasBoTrx())
                            {
                                IMaster boXpost = ut.getBoTrx();
                                
                                
                                if(ut.canUseCustomBo() && checkCustomBranch(userSession))
                                	boXpost = ut.getCustomBo();
                                
                                String sCurTab = ut.getCurrentTab();
                                boXpost.executePostAction(userSession,sCurTab,properties);

                                // break into mobile
                                if( resource!=null && resource.startsWith("/mobile/") ) {
                                	dispatcher = getServletContext().getRequestDispatcher(resource);
                                	dispatcher.include(request, response);
                                	return;
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(Master.class);
                    logger.error(e);
                }
                
                
                /*
                 * SDK
                 * Set di parametri non solo dal dettaglio dispositivo 
                 */
                /*removed by Kevin Ge, because all the SDK work already be done in 
                 * line 340: boXpost.executePostAction(userSession,sCurTab,properties);
                try
                {
                    String cmdk = properties.getProperty("cmdk");
                    if((cmdk != null) && (cmdk.equalsIgnoreCase("sdks")))
                    {
                        if(ut != null)
                        {
                            String sCurTab = ut.getCurrentTab();
                            String resource = ut.getProperty("resource");
                            if(resource != null && !resource.equalsIgnoreCase("dtlview"))
                            {
                                BDtlView tmpBxSet = new BDtlView(userSession.getLanguage());
                                tmpBxSet.executePostAction(userSession,sCurTab,properties);
                                tmpBxSet = null;
                            }
                        }
                    }
                }
                catch(Exception e) 
                {
                    Logger logger = LoggerMgr.getLogger(Master.class);
                    logger.error(e);
                }
                */
                // Fine
                
                if (goBack)
                {
                    try
                    {
                        this.goBackTrx(userSession);
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(Master.class);
                        logger.error(e);
                    }

                    dispatcher = getServletContext().getRequestDispatcher(FRAMESET_PATH);
                }
                else
                {
                    dispatcher = getServletContext().getRequestDispatcher(TABBODY_PATH);
                }
            }
        }
        else
        {
        	ServletHelper.invalidateSession(request.getRequestedSessionId(), request);
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(ServletHelper.PARAM_ERROR,"Session timeout");
            dispatcher = getServletContext().getRequestDispatcher(SESSI_PATH);
        }

        // Dispatch to Resource
        try
        {
            dispatcher.include(request, response);
        }
        catch (Throwable e)
        {
            Logger logger = LoggerMgr.getLogger(Master.class);
            logger.error(e);
            userSession.setThrowable(e);
            dispatcher = getServletContext().getRequestDispatcher(ERROR_PATH);
            dispatcher.include(request, response);
        }

        return;
    }

    private BoMaster createBusinessObject(String lang, String sName)
        throws InstantiationException, IllegalAccessException, 
            ClassNotFoundException, Exception
    {
        String className = BO_PACKAGE + sName;
        BoMaster obj = (BoMaster) FactoryObject.newInstance(className,
                new Class[] { String.class }, new Object[] { lang });

        return obj;
    }

    private String getRedirectPath(Properties properties)
    {
        return transactionFolder +
        properties.getProperty(ServletHelper.PARAM_TRXNAME);
    }

    private void lockUserFlow(UserSession userSession, String folder)
    {
        if (MenuTabMgr.getInstance().canLock(folder))
        {
            userSession.setForceLogout(true);
        }
    }

    private void goBackTrx(UserSession sessionUser)
    {
        UserTransaction utCurrent = sessionUser.getRootUserTransaction();
        UserTransaction utPrevious = null;

        while (utCurrent.hasChild())
        {
            utPrevious = utCurrent;
            utCurrent = utCurrent.getChild();
        }

        if (utPrevious != null)
        {
            utPrevious.removeChild();
        }
    }
    
    /*
     * Questo metodo � stato introdotto per la gestione 
     * del post dei dati nella pagine custom di dettaglio dispositivo.
     */
    private IMaster branchForCustomPost(UserSession sessionUser)
    {
    	IMaster customBo = null;
    	
    	int idDev = -1;
		try {
			idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
		}catch(NumberFormatException e){}
		
    	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
		DeviceStructure deviceStructure = deviceStructureList.get(idDev);
		
		CustomView view = CustomViewMgr.getInstance().hasDeviceCustomView(deviceStructure.getIdDevMdl());
		
		if(view != null)
		{
			String forFactory = view.getBusiness();	
			
			Class[] oType = new Class[3];
			oType[0] = int.class;
			oType[1] = int.class;
			oType[2] = String.class;
			
			Object[] oValue = new Object[3];
			oValue[0] = new Integer(sessionUser.getIdSite());
			oValue[1] = new Integer(idDev);
			oValue[2] = sessionUser.getLanguage();
			
			try {
				customBo = (IMaster)FactoryObject.newInstance(forFactory,oType,oValue);
			} 
			catch (Exception e) 
			{
				Logger logger = LoggerMgr.getLogger(Master.class);
		        logger.error(e);
			}
		}
		
		return customBo;
    }
    
    private boolean checkCustomBranch(UserSession sessionUser)
    {
    	int idDev = -1;
		try {
			idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
		}catch(NumberFormatException e){}
		
    	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
		DeviceStructure deviceStructure = deviceStructureList.get(idDev);
		
		CustomView view = CustomViewMgr.getInstance().hasDeviceCustomView(deviceStructure.getIdDevMdl());
		
		if(view != null)
			return true;
		else
			return false;
    }
}
