package supervisor;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.bo.master.IMaster;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Refresh extends HttpServlet
{
    public void init() throws ServletException
    {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        UserSession userSess = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        
        if (ServletHelper.validateSessionWithoutSet(userSess))
        {
        	Properties properties = ServletHelper.retrieveParameters(request);
        	response.setCharacterEncoding(userSess.getEncoding());
        	
            UserTransaction userTrx = userSess.getCurrentUserTransaction();
            IMaster master = userTrx.getBoTrx();
            IRefresh refresh = null;

            String tabName = userTrx.getCurrentTab();
            String paramName = "";
            String paramVal = "";

            if (master != null)
            {
                try
                {
                    RefreshBean[] rb = master.getRefreshObj(tabName);

                    for (int i = 0; i < rb.length; i++)
                    {
                        refresh = createObject(rb[i].getTypeObj());

                        if (refresh != null)
                        {
                            paramName = "pd" + rb[i].getIdNumTable();
                            paramVal = properties.getProperty(paramName);
                            refresh.setTableData(paramVal+"#"+rb[i].isLink());
                            refresh.refresh(userSess);
                            out.write("<html><head> "+
                            		"<META http-equiv=\"Content-Type\" content=\"text/html; charset=");
                            out.write(userSess.getEncoding());
                            out.write("\" >\"</head>");
                            out.write("<body>");
                            out.write(refresh.getHtmlData(userSess,
                                    rb[i].getIdHtmlObj()));

                            out.write("</body></html>");
                        }
                    }
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
        }
        else {
        	// session expires; force logout
			out.write("<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=");
			out.write(userSess.getEncoding());
			out.write("\" >\"</head>");
			out.write("<body onLoad=\"top.frames['manager'].Redirect('servlet/logout')\">");
			out.write("</body></html>");
        }

        out.flush();
        out.close();
    }

    private IRefresh createObject(int type)
    {
        IRefresh oRef = null;

        try
        {
            switch (type)
            {
            case IRefresh.R_DEVICE:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_DEVICESOBJ);

                break;

            case IRefresh.R_ALARMS:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_ALARMSOBJ);

                break;

            case IRefresh.R_EVENTS:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_EVENTOBJ);

                break;

            case IRefresh.R_ALARMSCALL:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_RECALARMSOBJ);

                break;

            case IRefresh.R_ALARMSSEARCH:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_ALARMSSEARCHOBJ);

                break;

            case IRefresh.R_EVENTSSEARCH:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_EVENTSSEARCHOBJ);

                break;
                
            case IRefresh.R_DEVICEDETAIL:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_DEVICESDETAILOBJ);

                break;
//            case IRefresh.R_FLASHLED:
//                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_FLASHLEDOBJ);
//
//                break;

            case IRefresh.R_PARAMS:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_PARAMSOBJ);

                break;

            case IRefresh.R_ALARMSEVENTS:
                oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_ALARMSEVENTSOBJ);

                break;
               
            case IRefresh.R_PARAMETERS:
            	oRef = (IRefresh) FactoryObject.newInstance(IRefresh.R_PARAMETERSOBJ);
            	break;
            	
            case IRefresh.R_PRINT:
            	oRef = (IRefresh)FactoryObject.newInstance(IRefresh.R_PRINTOBJ);
            	break;
            	
            case IRefresh.R_REPORT:
            	oRef = (IRefresh)FactoryObject.newInstance(IRefresh.R_REPORTOBJ);
            	break;
            default:
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return oRef;
    }
}
