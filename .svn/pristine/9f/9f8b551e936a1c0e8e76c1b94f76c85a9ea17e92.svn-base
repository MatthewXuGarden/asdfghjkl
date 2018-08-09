package com.carel.supervisor.presentation.tabmenu;

import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;


public class SubTab
{
    private String name = null;
    private String action = null;
    private String tooltip = null;
    private String pathIconStandby = null;
    private String pathIconClick = null;
    private String pathIconOver = null;
    private String pathIconDisabled = null;
    private String languageUsed = null;
    private String section = null;
    private boolean visible = false;

    private String description = null;

    public SubTab(String languageUsed, String section)
    {
        this.languageUsed = languageUsed;
        this.section = section;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAction()
    {
        return this.action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getPathIconClick()
    {
        return pathIconClick;
    }

    public void setPathIconClick(String pathIconClick)
    {
        this.pathIconClick = pathIconClick;
    }

    public String getPathIconOver()
    {
        return pathIconOver;
    }

    public void setPathIconOver(String pathIconOver)
    {
        this.pathIconOver = pathIconOver;
    }

    public String getPathIconStandby()
    {
        return pathIconStandby;
    }

    public void setPathIconStandby(String pathIconStandby)
    {
        this.pathIconStandby = pathIconStandby;
    }

    public String getPathIconDisabled()
    {
        return pathIconDisabled;
    }

    public void setPathIconDisabled(String pathIconDisabled)
    {
        this.pathIconDisabled = pathIconDisabled;
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

	public String getDescription()
	{
        LangService multiLanguage = LangMgr.getInstance().getLangService(languageUsed);

        //return multiLanguage.getString(section, description);
        return multiLanguage.getString("button", description);
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public boolean getVisible()
	{
		return this.visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
}
