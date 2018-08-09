package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.dataaccess.db.Record;


public class ProfileMapsBean
{
    public static final String PROTECTED = "P";
    public static final String HIDDEN = "H";
    public static final String NORMAL = "N";
    private static final String PROFILE = "idprofile";
    private static final String CODE = "code";
    private static final String MENU = "menu";
    private static final String TAB = "tab";
    private static final String BUTTON = "button";
    private static final String WIDGET = "widget";
    private static final String STATUS = "status";
    private Integer profile = null;
    private Integer code = null;
    private String menu = null;
    private String tab = null;
    private String button = null;
    private String widget = null;
    private String status = null;

    public ProfileMapsBean(Record record)
    {
        profile = (Integer) record.get(PROFILE);
        code = (Integer) record.get(CODE);
        menu = (String) record.get(MENU);
        tab = (String) record.get(TAB);
        button = (String) record.get(BUTTON);
        widget = (String) record.get(WIDGET);
        status = (String) record.get(STATUS);

        if ((null == status) && (null != widget))
        {
            status = HIDDEN;
        }
    }

    /**
     * @return: String
     */
    public String getButton()
    {
        return button;
    }

    /**
     * @param button
     */
    public void setButton(String button)
    {
        this.button = button;
    }

    /**
     * @return: String
     */
    public Integer getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code)
    {
        this.code = code;
    }

    /**
     * @return: String
     */
    public String getMenu()
    {
        return menu;
    }

    /**
     * @param menu
     */
    public void setMenu(String menu)
    {
        this.menu = menu;
    }

    /**
     * @return: String
     */
    public Integer getProfile()
    {
        return profile;
    }

    /**
     * @param profile
     */
    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }

    /**
     * @return: String
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return: String
     */
    public String getTab()
    {
        return tab;
    }

    /**
     * @param tab
     */
    public void setTab(String tab)
    {
        this.tab = tab;
    }

    /**
     * @return: String
     */
    public String getWidget()
    {
        return widget;
    }

    /**
     * @param widget
     */
    public void setWidget(String widget)
    {
        this.widget = widget;
    }
}
