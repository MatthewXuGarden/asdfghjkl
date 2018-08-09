package com.carel.supervisor.presentation.ldap;

import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import java.util.ArrayList;


public class UsersList
{
    private static final int columns = 2;
    private int rows = 0;
    private int width = 0;
    private int height = 0;
    private String title = "";
    private int pageNumber = 1;
    private ArrayList user = null;
    private ArrayList profile = null;
    private ArrayList blocked = null;
    private int screenw = 1024;
    private int screenh = 768;

    // private ArrayList password=null;
    public UsersList()
    {
    }

    public UsersList(String title)
    {
        this.title = title;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void setTitle(String tit)
    {
        this.title = tit;
    }

    public int getHeight()
    {
        return height;
    }

    /**
         * @return: int
         */
    public int getPageNumber()
    {
        return pageNumber;
    }

    /**
     * @return: String
     */
    public String getTitle()
    {
        return title;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void loadData() throws Exception
    {
        ArrayList[] arrayLists = ProfilingMgr.getInstance().getProfiler().getUserInformation();
        user = arrayLists[IProfiler.INDEX_USER_INFORMATION];
        profile = arrayLists[IProfiler.INDEX_PROFILE_INFORMATION];
        blocked = arrayLists[IProfiler.INDEX_USER_BLOCKED];
    } //loadData

    private HTMLElement[][] getUsersTable(int idsite) throws DataBaseException
    {
        HTMLElement[][] tableData = null;

        rows = user.size();

        ProfileBeanList db_profiles = new ProfileBeanList(idsite,true);
        String idprofile = null;
        String desc = "";
        String s_user = "";
        
        if (rows != 0)
        {
            tableData = new HTMLElement[rows][];

            for (int i = 0; i < rows; i++)
            {
                idprofile = profile.get(i).toString();
                
                s_user = (String) user.get(i);
                if (idprofile.equals("-5"))
                {
                	s_user = s_user.substring(DBProfiler.CAREL_PREFIX.length(),s_user.length());
                }

                tableData[i] = new HTMLElement[columns];
                
               	tableData[i][0] = new HTMLSimpleElement(s_user);
                desc = db_profiles.getProfileById(Integer.parseInt(idprofile)).getCode();
                tableData[i][1] = new HTMLSimpleElement(desc);
            }
        }

        return tableData;
    }

    private HTMLTable getHTMLUsersTablePrv(String tableName, String language, int idsite)
        throws DataBaseException
    {
        HTMLElement[][] data = getUsersTable(idsite);
        HTMLTable usersTable = new HTMLTable(tableName, getHeaderTable(language), data, true, false);

        //usersTable.setPage(true);
        //usersTable.setPageNumber(pageNumber);
        usersTable.setScreenH(screenh);
        usersTable.setScreenW(screenw);
        usersTable.setColumnSize(0, 410);
        usersTable.setColumnSize(1, 410);
        usersTable.setHeight(height);
        usersTable.setWidth(width);
        usersTable.setTableId(1);

        //usersTable.setTableTitle(this.title);
        String[] dblclick = new String[user.size()];

        for (int i = 0; i < dblclick.length; i++)
        {
			//add blocked info, by Kevin
            dblclick[i] = new String((String) user.get(i) + ";" + (String) profile.get(i))+";"+
            	(String)blocked.get(i); //+";"+(String)password.get(i));
        }

        if (dblclick.length == 0)
        {
            dblclick = new String[] { "nop" };
        }

        usersTable.setDbClickRowAction("load('$1');");
        usersTable.setDlbClickRowFunction(dblclick);

        usersTable.setSgClickRowAction("reset_table('$1');");
        usersTable.setSnglClickRowFunction(dblclick);

        return usersTable;
    }

    private String[] getHeaderTable(String language)
    {
        LangService langService = LangMgr.getInstance().getLangService(language);
        String[] header = new String[]
            {
                langService.getString("ldapTables", "user"),
                langService.getString("ldapTables", "profile")
            };

        return header;
    }

    public String getHTMLUsersTable(String tableName, String language, int idsite)
        throws DataBaseException
    {
        return getHTMLUsersTablePrv(tableName, language, idsite).getHTMLText().toString();
    }

    public String getHTMLUsertTableRefresh(String tableName, String language, int idsite)
        throws DataBaseException
    {
        return getHTMLUsersTablePrv(tableName, language, idsite).getHTMLTextBufferRefresh()
                   .toString();
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }

}
