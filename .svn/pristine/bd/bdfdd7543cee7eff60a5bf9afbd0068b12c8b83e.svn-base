package supervisor;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.base.util.Base64;
import com.carel.supervisor.presentation.assistance.UserInMgr;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class ExternalAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String CONTENT_TYPE = "text/html; charset=UTF-8";
	private String DEFAUL_ENCODING = "UTF-8";
	public static final String PATH_LOGIN = "/arch/login/Login.jsp";
	private String PATH_DESKTOP = "/arch/desktop/Desktop.jsp";
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	public static final String DEFAULT_LANG = "EN_en";
	public static final String DEFAULT_BROWSER = "IE";
	public static final String DEFAULT_H = "768";
	public static final String DEFAULT_W = "1024";
	public static final String FOLDER = "folder";
	public static final String PAGE = "page";
	public static final String TAB = "tab";
	public static final String ACTION = "action";
	public static final String LANGUAGE = "language";
	public static final String BROWSER = "browser";
	public static final String SCREENH = "screenh";
	public static final String SCREENW = "screenw";

	public ExternalAccess() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkLogin(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		checkLogin(request, response);
	}

	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			response.setContentType(CONTENT_TYPE);
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			RequestDispatcher dispatcher = null;
			Properties properties = ServletHelper.retrieveParameters(request);

			String resolution = properties.getProperty("cmd");
			if ((resolution != null) && ("manager_initresolution".equals(resolution))) {
				UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
				ServletHelper.saveSession(us, request);
				String h = properties.getProperty(SCREENH);
				if( h != null && h.length() > 0 )
					us.setScreenHeight(h);
				String w = properties.getProperty(SCREENW);
				if( w != null && w.length() > 0 )
					us.setScreenWidth(w);
			} else {
				String language = properties.getProperty(LANGUAGE);
				String browser = properties.getProperty(BROWSER);
				String screenH = properties.getProperty(SCREENH);
				String screenW = properties.getProperty(SCREENW);
				String user = properties.getProperty(USER);
				String password = properties.getProperty(PASSWORD);
				String folder = properties.getProperty(FOLDER);
				String page = properties.getProperty(PAGE);
				String tab = properties.getProperty(TAB);
				String encoding = DEFAUL_ENCODING;

				if (request.getHeader("Authorization") != null) {
					String encoded = (request.getHeader("Authorization"));
					String tmp = encoded.substring(6);
					String up = Base64.decode(tmp);
					if (up != null) {
						user = up.substring(0, up.indexOf(":"));
						password = up.substring(up.indexOf(":") + 1);
					}
				}
				Login login = new Login();
				UserProfile userProfile = login.checkUser(user, password);
				UserSession userSession = new UserSession(userProfile);

				if (browser == null || browser.equals(""))
					browser = DEFAULT_BROWSER;
				if (screenH == null || screenH.equals("")) {
					screenH = DEFAULT_H;
					userSession.getProfileRedirect().setReDefaultResolution(true);
				}
				if (screenW == null || screenW.equals("")) {
					screenW = DEFAULT_W;
					userSession.getProfileRedirect().setReDefaultResolution(true);
				}

				userSession.setLanguage(language);
				userSession.setEncoding(encoding);
				userSession.setUserBrowser(browser);
				userSession.setScreenHeight(screenH);
				userSession.setScreenWidth(screenW);

				userSession.getProfileRedirect().setReDirect(true);
				userSession.getProfileRedirect().setReFolder(folder);
				userSession.getProfileRedirect().setRePage(page);
				userSession.getProfileRedirect().setReTabNum(tab);

				login.collectSession(request, userSession);
				// simon note: if some params will be used in another place in
				// the future.
				// it can be insert to userSession.Properties
				// Enumeration p=properties.keys();
				// while(p.hasMoreElements()){
				// String k=String.valueOf((p.nextElement()));
				// userSession.setProperty(k, properties.getProperty(k));
				// System.out.println(k+"="+properties.getProperty(k));
				// }
				userSession.setSessionId(request.getSession().getId());
				ServletHelper.saveSession(userSession, request);
				// Add user in list of user in
				UserInMgr.getInstance().addUseIn(user, userSession.getSessionId());

				dispatcher = getServletContext().getRequestDispatcher(PATH_DESKTOP);
				dispatcher.include(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerMgr.getLogger(this.getClass()).error(e);
			response.setStatus(401);
			response.setHeader("WWW-authenticate", "Basic realm=\"carel.com\"");
		}
	}


}
