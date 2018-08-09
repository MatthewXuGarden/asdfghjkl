package supervisor;

import java.io.File;
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
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfileException;
import com.carel.supervisor.base.profiling.SectionProfile;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.assistance.UserInMgr;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBean;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.menu.MenuLoader;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.tabmenu.MenuBuilder;

public class LayedPrev extends HttpServlet
{
	private String CONTENT_TYPE = "text/html; charset=UTF-8";
    private String DEFAULT_ENCODING = "UTF-8";
    private String PATH_LOGIN = "/arch/login/Login.jsp";
    private String PATH_DESKTOP = "/arch/desktop/Desktop.jsp";
    private String PATH_DIR = "app";
    private String PATH_SUBDIR = "layed_preview";
    private String PATH_FIRSTJSP = "1.jsp";
    private String PATH_MAPS = "layed_preview/1.jsp";
    private String LAYED_USR = "layed";
    private String LAYED_PWD = "layed";
    private String LOCAL_HOST = "127.0.0.1";
    private String CHANNEL = "WEB";
    private String USERNAME_LABELFIELD = "username";
    private String USERPROFILE_LABELFIELD = "idprofile";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		doPreview(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		doPreview(request, response);
	}
	
	private void doPreview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(DEFAULT_ENCODING);
        request.setCharacterEncoding(DEFAULT_ENCODING);
        
        HttpSession session = request.getSession();
        String remoteAddr = request.getRemoteAddr();
        
        LangService lang = LangMgr.getInstance().getLangService("EN_en");
        
        String page = "";
        String user = "";
        String password = "";
        
        IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
        
        try
        {
			String cp = p_info.get("cp");
			
			if (cp != null)
			{
			    //accesso consentito solo dalla macchina locale (non da remoto):
			    if (LOCAL_HOST.equals(remoteAddr))
			    {
			        //File previewPage = new File(BaseConfig.getAppHome() + "\\app\\layed_preview\\1.jsp");
			    	File previewPage = new File(BaseConfig.getAppHome() + File.separator + PATH_DIR + File.separator + PATH_SUBDIR + File.separator + PATH_FIRSTJSP);
			    	
			        //accesso consentito solo se esiste jsp x preview:
			        if ((previewPage != null) && (previewPage.exists()))
			        {
			        	UserCredential userCredential = null;
				        UserProfile userProfile = null;
				        page = PATH_DESKTOP;
				        
				        /*
				        //utente Fantasma:
				        user = LAYED_USR;
				        password = LAYED_PWD;
				        */
				        
				        //valori di default:
				        String browser = "IE";
				        String encoding = DEFAULT_ENCODING;
				        String language = "EN_en";
				        String screenH = "768";
				        String screenW = "1024";
				        
				        //prima controllo eventuali credenziali di un possibile utente reale:
				        Properties properties = ServletHelper.retrieveParameters(request);
				        
				        if (properties != null)
				        {
					        //user-name:
				        	if (properties.getProperty("u") != null)
					        	user = properties.getProperty("u");
					        
				        	//password:
					        if (properties.getProperty("p") != null)
					        	password = properties.getProperty("p");
					        
					        //language-code:
					        if (properties.getProperty("l") != null)
					        	language = properties.getProperty("l");
					        
					        //screen-high:
					        if (properties.getProperty("h") != null)
					        	screenH = properties.getProperty("h");
					        
					        //screen-width:
					        if (properties.getProperty("w") != null)
					        	screenW = properties.getProperty("w");
				        }
				        
				        UserSession userSession = null;
				        
				        try
				        {
				            if (user.equals(LAYED_USR))
				            {
				            	userProfile = new UserProfile();
				            	
				            	SectionProfile sectionProfile = new SectionProfile();
				            	
				            	userProfile.addSection(IProfiler.USER_SECTION, sectionProfile);
				            	
				            	sectionProfile.setValue("username", user);
				                sectionProfile.setValue("idprofile", "0"); //=admin (le mappe devono poter leggere e scrivere parametri)
				            }
				            else //user passato in GET-mode
				            {
				            	userCredential = new UserCredential(user, password, CHANNEL);
				            	userProfile = getUserProfileCrypted(userCredential); //x gestire password gi� cryptata
				            }
				
				            userSession = new UserSession(userProfile);
				            userSession.setLanguage(language);
				            userSession.setEncoding(encoding);
				            userSession.setUserBrowser(browser);
				            userSession.setScreenHeight(screenH);
				            userSession.setScreenWidth(screenW);
				            userSession.setSessionId(request.getSession().getId());
				            
				            UserInMgr.getInstance().addUseIn(user,userSession.getSessionId());
				            
				        	p_info.set("home", "2"); //setto pagina iniziale automatica su pagina mappa
				        	
				        	userSession.setProperty("layedmap", PATH_MAPS); //salvo in sessione-utente path mappa x preview
				        	
				            SiteInfoList siteInfoList = new SiteInfoList(null, BaseConfig.getPlantId());
				            userSession.setIdSite(1);
				            
				            SiteInfo siteInfo = siteInfoList.getById(userSession.getIdSite());
				            userSession.setSiteName(siteInfo.getName());
				            
				            try
				            {
				                String langDef = LangUsedBeanList.getDefaultLanguage(siteInfo.getId());
				                String langDefDescr = LangUsedBeanList.getDefaultLanguageDescription(siteInfo.getId());
				                userSession.setDefaultLanguage(langDef);
				                userSession.setDefaultLanguageDescription(langDefDescr);
				            }
				            catch (Exception e)
				            {
				                userSession.setDefaultLanguage(userSession.getLanguage());
				                userSession.setDefaultLanguageDescription("");
				            }
				            
				            userSession.loadGroup();
				            
				            MenuConfigMgr.getInstance().completeMenuConfiguration(userSession);
				            
				            ServletHelper.saveSession(userSession, request);
				            
				            //come da servlet di Login:
				            forceLoad();
				            
				            user = userSession.getUserName();
				            
				            //segnalo nuovo login negli Eventi:
				            EventMgr.getInstance().info(new Integer(userSession.getIdSite()), user, "Action", 
				            		"LOG05", new Object[] { user + " (Layout Editor) " + request.getRemoteAddr() });
				        }
				        catch (Exception e)
				        {
				            //se utente non riconosciuto:
				        	Logger logger = LoggerMgr.getLogger(this.getClass());
				            logger.error(e);
				            
				            String langua = "";
				            
				            if (userSession != null)
				            {
				            	langua = userSession.getDefaultLanguage();
				            }
				            else
				            {
				            	langua = language;
				            }
				            
				            lang = LangMgr.getInstance().getLangService(langua);
				            page = PATH_LOGIN;
				            session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("layedprev", "error_login"));
				        }
			        }
			        else
			        {
			        	page = PATH_LOGIN;
			        	session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("layedprev", "no_jsp"));
			        }
			    }
			    else
			    {
			    	page = PATH_LOGIN;
			    	session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("layedprev", "no_localhost"));
			    }
			}
			else
			{
				page = PATH_LOGIN;
				session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("layedprev", "error_cp"));
			}
		}
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            
            page = PATH_LOGIN;
			session.setAttribute(ServletHelper.PARAM_ERROR, lang.getString("layedprev", "error_cp"));
		}
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.include(request, response);

        return;
	}
	
    //la password da ctrl viene passata gi� cryptata:
    private UserProfile getUserProfileCrypted(UserCredential userCredential) throws ProfileException, Exception
	{
	    Object[] objects = new Object[]
	    {
	    		userCredential.getUserName(),
	    		userCredential.getUserPassword()
	    };
	    
	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT username,idprofile FROM cfusers ");
	    sql.append("WHERE username=? ");
	    sql.append("AND password=? ");
	
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), objects);
	
	    if ((rs == null) || (rs.size() != 1))
	    {
	        throw new ProfileException(LangMgr.getInstance().getLangService("EN_en").getString("layedprev", "error_login"));
	    }
	
	    UserProfile userProfile = new UserProfile();
	    
	    SectionProfile sectionProfile = new SectionProfile();
	    
	    userProfile.addSection(IProfiler.USER_SECTION, sectionProfile);
	
	    Record record = rs.get(0);
	    sectionProfile.setValue(USERNAME_LABELFIELD,(String) record.get(USERNAME_LABELFIELD));
	    sectionProfile.setValue(USERPROFILE_LABELFIELD,((Integer) record.get(USERPROFILE_LABELFIELD)).toString());
	
	    return userProfile;
	}
	
	private void forceLoad()
    {
    	try
        {
            ConfigurationGraphBeanList c = new ConfigurationGraphBeanList();
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        try
        {
            ConfigurationGraphBean c2 = new ConfigurationGraphBean();
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        try
        {
            int i = GraphConstant.MONTH;
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        try
        {
            MenuLoader m = new MenuLoader();
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        try
        {
            MenuBuilder m1 = new MenuBuilder();
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        try
        {
            MenuTabMgr m2 = MenuTabMgr.getInstance();
        }
        catch (Exception e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
	
}
