package com.carel.supervisor.presentation.profile;

import java.util.HashMap;
import java.util.Map;


public class ProfileButtonWidgt
{
    private Map button = new HashMap();
    private Map widget = new HashMap();
    private boolean tabProtectd = false;

    public ProfileButtonWidgt(ProfileMapsBean profileMapsBean)
    {
        addButWid(profileMapsBean);
    }

    public void addButWid(ProfileMapsBean profileMapsBean)
    {
        String buttonName = profileMapsBean.getButton();

        if ((null != buttonName) && (!buttonName.trim().equals("")))
        {
            button.put(buttonName, buttonName);
        }

        String widgetName = profileMapsBean.getWidget();

        if ((null != widgetName) && (!widgetName.trim().equals("")))
        {
            widget.put(widgetName, profileMapsBean.getStatus());
        }

        if (((null == buttonName) || (buttonName.trim().equals(""))) &&
                ((null != widgetName) || (widgetName.trim().equals(""))))
        {
            tabProtectd = ProfileMapsBean.PROTECTED.equals(profileMapsBean.getStatus());
        }
    }

    public boolean hasButWid()
    {
        return ((button.size() > 0) || (widget.size() > 0));
    }

    public boolean isTabProtected()
    {
        return tabProtectd;
    }

    public boolean isButtonActive(String buttonName)
    {
        if (button.containsKey(buttonName))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getWidgetStatus(String widgetName)
    {
        if (widget.containsKey(widgetName))
        {
            return (String) widget.get(widgetName);
        }
        else
        {
            return ProfileMapsBean.NORMAL;
        }
    }
}
