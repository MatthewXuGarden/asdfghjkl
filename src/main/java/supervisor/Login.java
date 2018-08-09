package supervisor;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfileException;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.assistance.UserInMgr;
import com.carel.supervisor.presentation.bean.*;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.ldap.DBProfiler;
import com.carel.supervisor.presentation.menu.MenuLoader;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.tabmenu.MenuBuilder;


public class Login extends HttpServlet
{
    private String CONTENT_TYPE = "text/html; charset=UTF-8";
    private String DEFAUL_ENCODING = "UTF-8";
    public static final String PATH_LOGIN = "/arch/login/Login.jsp";
    public static final String PATH_LOGIN_MOBILE = "/mobile/Login.jsp";
    private String PATH_DESKTOP = "/arch/desktop/Desktop.jsp";
    private String PATH_MOBILE = "/mobile/Mobile.jsp";
    private String CHANNEL = "WEB";
    
    public static final String FIRSTTIME = "firsttime";
    public static final String CHANGEPWD = "changepwd";
    public static final String PWDEXPIRED = "pwdexpired";
    public static final String NORMAL = "normal";
    public static final String PWDCHANGEDONE = "pwdchangedone";
    public static final String WIZARDDONE = "wizarddone";
    public static final String USER = "user";
    public static final String LICENSE = "license";
    public static final String PAGETYPE = "pagetype";
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserSession userSession = null;
        RequestDispatcher dispatcher = null;
        Properties properties = ServletHelper.retrieveParameters(request);
        String changepwd = properties.getProperty(CHANGEPWD);
        userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);

        if (ServletHelper.validateSession(userSession) && changepwd == null)
        {
            String language = properties.getProperty(ServletHelper.PARAM_LANGUAGE);

            if ((null != language) && (!language.equals("")))
            {
                userSession.setLanguage(language);
                session.setAttribute(ServletHelper.PARAM_LANGUAGE, language);

                try
                {
                    userSession.loadGroup();
                    MenuConfigMgr.getInstance().completeMenuConfiguration(userSession);
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(Login.class);
                    logger.error(e);
                    session.setAttribute(ServletHelper.PARAM_ERROR, e.getMessage());
                }

                dispatcher = getServletContext().getRequestDispatcher(ServletHelper.isMobileDevice(request) ? PATH_MOBILE : PATH_DESKTOP);
                dispatcher.include(request, response);
            }
            //2009-12-31, by Kevin
            else if(language == null)
            {
            	dispatcher = getServletContext().getRequestDispatcher(ServletHelper.isMobileDevice(request) ? PATH_MOBILE : PATH_DESKTOP);
                dispatcher.include(request, response);
            }

            return;
        }
        else if(ServletHelper.validateSession(userSession) && changepwd != null)
        {
        	session.setAttribute(CHANGEPWD, CHANGEPWD);
        }
        
       	dispatcher = getServletContext().getRequestDispatcher(ServletHelper.isMobileDevice(request) ? PATH_LOGIN_MOBILE : PATH_LOGIN);
        dispatcher.include(request, response);

        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		String page = null;
		HttpSession session = request.getSession();

		Properties properties = ServletHelper.retrieveParameters(request);
		String jsession = properties.getProperty("jsession");
		String user = properties.getProperty(ServletHelper.PARAM_USER);
		String password = properties.getProperty(ServletHelper.PARAM_PASSWORD);
		String npassword = properties.getProperty("npassword");
		String browser = properties.getProperty(ServletHelper.PARAM_BROWSER);
		String encoding = DEFAUL_ENCODING;
		String language = properties.getProperty(ServletHelper.PARAM_USERLANG);
		String screenH = properties.getProperty("screenh");
		String screenW = properties.getProperty("screenw");
		String cmd = properties.getProperty(ServletHelper.PARAM_CMD);
		
		// invalidate user/pass for cached posts
		if( ServletHelper.isInvalidId(jsession) ) {
			user = "";
			password = "";
		}
		
		if (language != null) {
			int idx = language.indexOf("^");

			if (idx != -1) {
				encoding = language.substring(idx + 1);
				language = language.substring(0, idx);
			}
		}

		try {
			if( cmd.equals(LICENSE) ) {
				if( properties.getProperty("license_agreement") != null )
                	ProductInfoMgr.getInstance().getProductInfo().set("license_agreement", "yes");
		        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(PATH_LOGIN);
		        dispatcher.include(request, response);
		        return;
			}
			
			boolean modifyUser = false;
			if (cmd.equals(FIRSTTIME) || cmd.equals(PWDEXPIRED)) {
				IProfiler profiler = ProfilingMgr.getInstance().getProfiler();
				Record record = DBProfiler.getUserByUsername(user);
				if (cmd.equals(PWDEXPIRED)) {
					String encryptpwd = profiler.encryptPassword(npassword);
					String oldencryptpwd = record.get(DBProfiler.PASSWORD_LABELFIELD).toString().trim();
					
					if (encryptpwd.equals(oldencryptpwd)) {
						throw new ProfileException(DBProfiler.PROFILE_EXCEPTION_SAMEPWD);
					}
				}
				if(record != null)
				{
					properties.setProperty(IProfiler.INPUT_PROFILE, record.get(DBProfiler.USERPROFILE_LABELFIELD).toString());
				}
				properties.setProperty(IProfiler.INPUT_USER_TO_MODIFY, user);
				properties.setProperty(IProfiler.INPUT_PASSWORD, npassword);
				properties.setProperty("userlogged", user);
				modifyUser = profiler.modifyUser(properties);
				password = npassword;
				if (modifyUser == false) {
					throw new Exception();
				} else {
					IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
					product.set(PWDCHANGEDONE, "1");
				}
			}
			
			
			UserProfile userProfile = checkUser(user, password);

			
			page = properties.getProperty(PAGETYPE).equals("mobile") ? PATH_MOBILE : PATH_DESKTOP;
			//page = ServletHelper.isMobileDevice(request) ? PATH_MOBILE : PATH_DESKTOP;

			// 2009-12-22, change password, by Kevin
			if (cmd.equals(CHANGEPWD) == true) {
				IProfiler profiler = ProfilingMgr.getInstance().getProfiler();
				properties.setProperty(IProfiler.INPUT_USER_TO_MODIFY, user);
				properties.setProperty(IProfiler.INPUT_PASSWORD, npassword);
				properties.setProperty("userlogged", user);
				String profile = userProfile.getSection(IProfiler.USER_SECTION)
                        .getValue(ProfilingMgr.getInstance().getProfiler().getProfileNameLabelField()).trim();
				properties.setProperty(IProfiler.INPUT_PROFILE, profile);
				modifyUser = profiler.modifyUser(properties);
				if (modifyUser == false) {
					throw new Exception();
				}
			}
			
			UserSession userSession = new UserSession(userProfile);
			userSession.setLanguage(language);
			userSession.setEncoding(encoding);
			userSession.setUserBrowser(browser);
			userSession.setScreenHeight(screenH);
			userSession.setScreenWidth(screenW);
			userSession.setSessionId(request.getSession().getId());

			// login redirect
			LoginRedirectBean.initRedirect(userSession, properties);
			
			// 2009-12-25, check account expired, by Kevin
			// I move saveSession here
			// because in Login.jsp, I need to check sessionValid
			// so I need to saveSession before loading the codes below
			// befere loading the codes below, just to make sure the performance
			// is OK.
			ServletHelper.saveSession(userSession, request);
			// Add user in list of user in
			UserInMgr.getInstance().addUseIn(user, userSession.getSessionId());
			// End
			collectSession(request,userSession);
		} catch (ProfileException pe) {
			String langused = "";
			try {
				langused = StringUtility.split(properties.getProperty("txtLanguage"), "^")[0];
			} catch (Exception e_1) {
				langused = "EN_en";
			}
			LangService lang = LangMgr.getInstance().getLangService(langused);
			String errmsg = pe.getMessage();
			if (errmsg.equals(DBProfiler.PROFILE_EXCEPTION_BLOCKED)) {
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "blockederrormsg"));
			} else if (errmsg.equals(DBProfiler.PROFILE_EXCEPTION_EXPIRED)) {
				session.setAttribute(DBProfiler.PROFILE_EXCEPTION_EXPIRED, DBProfiler.PROFILE_EXCEPTION_EXPIRED);
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "expirederrormsg"));
				session.setAttribute(USER, user);
			} else if (errmsg.equals(DBProfiler.PROFILE_EXCEPTION_SAMEPWD)) {
				session.setAttribute(DBProfiler.PROFILE_EXCEPTION_EXPIRED, DBProfiler.PROFILE_EXCEPTION_EXPIRED);
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "samepwderrormsg"));
				session.setAttribute(USER, user);
			} else if (errmsg.equals(DBProfiler.PROFILE_EXCEPTION_HIDDENNOTALLOWED)) {
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "resupnoallowed"));
			} else {
				if (cmd.equals(CHANGEPWD)) {
					session.setAttribute(CHANGEPWD, CHANGEPWD);
				}
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "loginerrormsg"));
			}
			page = ServletHelper.isMobileDevice(request) ? PATH_LOGIN_MOBILE : PATH_LOGIN;
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(Login.class);
			logger.error(e);
			String langused = "";
			try {
				langused = StringUtility.split(properties.getProperty("txtLanguage"), "^")[0];
			} catch (Exception e_1) {
				langused = "EN_en";
			}
			LangService lang = LangMgr.getInstance().getLangService(langused);
			session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("login", "loginerrormsg"));
			page = ServletHelper.isMobileDevice(request) ? PATH_LOGIN_MOBILE : PATH_LOGIN;
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.include(request, response);

        return;
    }
    
    public void collectSession(HttpServletRequest request,UserSession userSession) throws Exception{

		// remote
		if ((request.getParameter("remote") != null) && (request.getParameter("remote").equals("yes"))) {
			userSession.setIdSite(new Integer(request.getParameter("idsite")).intValue());
		} else {
			userSession.setIdSite(1);
		}

		SiteInfoList siteInfoList = new SiteInfoList(null, BaseConfig.getPlantId());
		SiteInfo siteInfo = siteInfoList.getById(userSession.getIdSite());
		userSession.setSiteName(siteInfo.getName());

		try {
			String langDef = LangUsedBeanList.getDefaultLanguage(siteInfo.getId());
			String langDefDescr = LangUsedBeanList.getDefaultLanguageDescription(siteInfo.getId());
			userSession.setDefaultLanguage(langDef);
			userSession.setDefaultLanguageDescription(langDefDescr);
			
			if (userSession.getLanguage() == null || "".equals(userSession.getLanguage())){
				userSession.setLanguage(userSession.getDefaultLanguage());
			}else{
				LangUsedBeanList langused = new LangUsedBeanList();
				LangUsedBean[] lista=langused.retrieveAllLanguage(1);
				boolean exist=false;
				for(int i=0; i<lista.length; i++){
					if(userSession.getLanguage().equals(lista[i].getLangcode())){
						exist=true;
						break;
					}
				}
				if(exist==false){
					userSession.setLanguage(userSession.getDefaultLanguage());
				}
			}
		} catch (Exception e) {
			userSession.setDefaultLanguage(userSession.getLanguage());
			userSession.setDefaultLanguageDescription("");
		}
		userSession.loadGroup();
		MenuConfigMgr.getInstance().completeMenuConfiguration(userSession);

		// Forziamo il load di alcuni componenti per evitare l'errore 500
		forceLoad();
		String user = userSession.getUserName();
		if (!"roffline".equalsIgnoreCase(user)) {
			EventMgr.getInstance().info(new Integer(userSession.getIdSite()), user, "Action", "LOG05", new Object[] { user + " " + request.getRemoteAddr() });
		}
    }
	public UserProfile checkUser(String user,String password) throws Exception{
		UserCredential userCredential = new UserCredential(user, password, CHANNEL);
		UserProfile userProfile = ProfilingMgr.getInstance().getUserProfile(userCredential);
		
		if (DBProfiler.checkAccountExpired(user)) {
			throw new ProfileException(DBProfiler.PROFILE_EXCEPTION_EXPIRED);
		}
		return userProfile;
	}
    public void forceLoad() {
		try {
			ConfigurationGraphBeanList c = new ConfigurationGraphBeanList();
		} catch (Exception e) {
		}

		try {
			ConfigurationGraphBean c2 = new ConfigurationGraphBean();
		} catch (Exception e) {
		}

		try {
			int i = GraphConstant.MONTH;
		} catch (Exception e) {
		}

		try {
			MenuLoader m = new MenuLoader();
		} catch (Exception e) {
		}

		try {
			MenuBuilder m1 = new MenuBuilder();
		} catch (Exception e) {
		}

		try {
			MenuTabMgr m2 = MenuTabMgr.getInstance();
		} catch (Exception e) {
		}
	}
}
