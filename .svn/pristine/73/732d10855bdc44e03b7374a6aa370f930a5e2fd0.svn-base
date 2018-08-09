package com.carel.supervisor.presentation.tabmenu;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;


public class Tab
{
    private static final String TYPE_TAB = "&type=tab";
    private static final String TYPE_RESOURCE = "nop&resource=";
    private static final String CURRENT = "&curTab=";
    private String tabName = null;
    private String tooltip = null;
    private String languageUsed = null;
    private String section = null;
    private String pageLink = null;
    private String page = null;

    public Tab(String languageUsed, String section)
    {
        this.languageUsed = languageUsed;
        this.section = section;
    }

    public String getTabName()
    {
        LangService multiLanguage = LangMgr.getInstance().getLangService(languageUsed);

        return multiLanguage.getString(section, tabName);
    }

    /**
         * @return: String
         */
    public String getPage()
    {
        return page;
    }

    /**
     * @param page
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    public String getPageLink()
    {
        return pageLink;
    }

    public void setPageLink(String pageLink)
    {
        if ((pageLink != null) &&
                ((pageLink.indexOf("GraphHaccp.jsp") != -1) ||
                (pageLink.indexOf("GraphHistorical.jsp") != -1)))
        {
            this.pageLink = pageLink + CURRENT + this.tabName;
        }
        else
        {
            this.pageLink = TYPE_RESOURCE + pageLink + TYPE_TAB + CURRENT +
                this.tabName;
        }
    }

    public void setTabName(String tabName)
    {
        this.tabName = tabName;
    }

    public String getTabId()
    {
        return tabName;
    }

    public String getTooltip()
    {
        LangService multiLanguage = LangMgr.getInstance().getLangService(languageUsed);

        return multiLanguage.getString(section, tooltip);
    }

    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }
}
