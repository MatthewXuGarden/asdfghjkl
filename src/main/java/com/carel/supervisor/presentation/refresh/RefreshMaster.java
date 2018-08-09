package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.session.UserSession;


public abstract class RefreshMaster implements IRefresh
{
    protected final static int INDEX_PAGE = 2;
    protected final static int INDEX_REFRESH_TYPE = 3;
    protected final static int ISLINK=4;
    protected final static int PAGE_ROWS=5;
    //protected final static int INDEX_TYPE_PAGE_MODE=4;
    public final static String TYPE_REFRESH = "refresh";
    public final static String TYPE_PAGE = "page";
    protected String[] arData = null;

    public void setTableData(String data) throws Exception
    {
        if (data != null)
        {
            this.arData = data.split("#");
        }
    }

    public abstract void refresh(UserSession userSession)
        throws Exception;

    public abstract String getHtmlData(UserSession userSession, String idHtmlObj)
        throws Exception;
}
