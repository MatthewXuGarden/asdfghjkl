package supervisor;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;


public class Logout extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3909580252579331929L;
	private static final String CONTENT_TYPE = "text/html";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // Retrive UserSession
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),
                request);
        checkRemoteLocalConnection(userSession);

        if (null != userSession)
        {
            if (!"roffline".equalsIgnoreCase(userSession.getUserName()))
            {
                EventMgr.getInstance().info(new Integer(userSession.getIdSite()),
                    userSession.getUserName(), "Action", "LOG06",
                    new Object[] { userSession.getUserName() });
            }
        }

        String login = "/servlet/login";

        response.setContentType(CONTENT_TYPE);
        ServletHelper.invalidateSession(request.getRequestedSessionId(), request);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(login);
        dispatcher.forward(request, response);

        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.doGet(request, response);
    }

    private void checkRemoteLocalConnection(UserSession sessionUser)
    {
    }
}
