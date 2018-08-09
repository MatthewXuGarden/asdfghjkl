package com.carel.supervisor.presentation.profile;

import java.util.HashMap;
import java.util.Map;


public class ProfileTab
{
    private Map tabs = new HashMap();

    public ProfileTab(ProfileMapsBean profileMapsBean)
    {
        addTab(profileMapsBean);
    }

    public void addTab(ProfileMapsBean profileMapsBean)
    {
        String tab = profileMapsBean.getTab();
        ProfileButtonWidgt profileButtonWidgt = null;

        if ((null != tab) && (!tab.trim().equals("")))
        {
            profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tab);

            if (null == profileButtonWidgt)
            {
                profileButtonWidgt = new ProfileButtonWidgt(profileMapsBean);
                tabs.put(tab, profileButtonWidgt);
            }
            else
            {
                profileButtonWidgt.addButWid(profileMapsBean);
            }
        }
    }

    public boolean hasTabs()
    {
        return (tabs.size() > 0);
    }

    public boolean isTabActive(String tabName)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tabName);

        if (null == profileButtonWidgt) //Logica negata
        {
            return true;
        }

        if (profileButtonWidgt.isTabProtected())
        {
            return true;
        }

        if (profileButtonWidgt.hasButWid())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isTabNoteActive(String tabName)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tabName);

        if (null == profileButtonWidgt) //Logica negata
        {
            return false;
        }

        if (profileButtonWidgt.isTabProtected())
        {
            return true;
        }

        if (profileButtonWidgt.hasButWid())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isTabProtected(String tabName)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tabName);

        if (null == profileButtonWidgt) //Logica negata
        {
            return false; //E' attivo non protetto
        }

        return profileButtonWidgt.isTabProtected();
    }

    public boolean isButtonActive(String tabName, String buttonName)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tabName);

               
        if (null == profileButtonWidgt)  //exception: in DB the case "menu" "" "button" don't exist
        								//(to hide a button is necessary insert: menu || menu + tabname || menu + tabname + buttonname)
        {
            return true;
        }
        
        if (!profileButtonWidgt.hasButWid())  //check if the whole SubTab is disabled; when subtab is disabled, button hashmap is empty
        {
        	return false;
        }

        if (profileButtonWidgt.isButtonActive(buttonName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isButtonNoteActive(String buttonName)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get("note");

        if (null == profileButtonWidgt) //Logica negata
        {
            return false;
        }

        if (profileButtonWidgt.isButtonActive(buttonName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isWidgetActive(String tabName, String widget)
    {
        String s = getWidgetStatus(tabName, widget);

        if (s.equals(ProfileMapsBean.NORMAL))
        {
            return true;
        }
        else if (s.equals(ProfileMapsBean.HIDDEN))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getWidgetStatus(String tabName, String widget)
    {
        ProfileButtonWidgt profileButtonWidgt = (ProfileButtonWidgt) tabs.get(tabName);

        if (null == profileButtonWidgt) //Logica negata
        {
            return ProfileMapsBean.NORMAL;
        }

        return profileButtonWidgt.getWidgetStatus(widget);
    }
}
