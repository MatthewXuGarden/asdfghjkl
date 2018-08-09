package com.carel.supervisor.presentation.profile;

import com.carel.supervisor.base.config.BaseConfig;
import java.util.HashMap;
import java.util.Map;


public class ProfileMaps
{
    private Map map = new HashMap();

    public ProfileMaps(ProfileMapsBeanList profileMapsBeanList)
    {
        ProfileMapsBean profileMapsBean = null;
        ProfileTab profileTab = null;
        String menu = null;

        for (int i = 0; i < profileMapsBeanList.size(); i++)
        {
            profileMapsBean = profileMapsBeanList.get(i);
            menu = profileMapsBean.getMenu();
            profileTab = (ProfileTab) map.get(menu);

            if (null == profileTab)
            {
                profileTab = new ProfileTab(profileMapsBean);
                map.put(menu, profileTab);
            }
            else
            {
                profileTab.addTab(profileMapsBean);
            }
        }
    }

    public boolean isMenuActive(String menu)
    {
        ProfileTab profileTab = (ProfileTab) map.get(menu);

        if (null == profileTab) //Logica negata
        {
            return true;
        }

        if (profileTab.hasTabs())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Per funzionare controllare prima isMenuActive(menu)
    public boolean isTabActive(String menu, String tabName)
    {
        ProfileTab profileTab = (ProfileTab) map.get(menu);
        
        if (null == profileTab)
        {
            return true;
        }

        return profileTab.isTabActive(tabName);
    }

    public boolean isTabProtected(String menu, String tabName)
    {
        ProfileTab profileTab = (ProfileTab) map.get(menu);

        if (null == profileTab)
        {
            return false;
        }

        return profileTab.isTabProtected(tabName);
    }

    //  Per funzionare controllare prima isTabActive(menu,tabName)
    public boolean isButtonActive(String menu, String tabName, String buttonName)
    {
    	ProfileTab profileTab = (ProfileTab) map.get(menu);

        if (null == profileTab)  // if "menu" String  is not on db, all section of menu are visible.
        						//(to hide a button is necessary insert: menu || menu + tabname || menu + tabname + buttonname)
        {
            return true;
        }
        
        if (!profileTab.hasTabs())  //check if the whole MENU is disabled; when MENU is disabled, SUBTAB hashmap is empty
        {
        	return false;
        }
        
        if (menu.equals("note"))
        {
        	return profileTab.isButtonNoteActive(buttonName);      	
        }
        else
        {
        	return profileTab.isButtonActive(tabName, buttonName);
        }
        
    }
    
    public boolean isWidgetActive(String menu, String tabName, String widget)
    {
        ProfileTab profileTab = (ProfileTab) map.get(menu);

        if (null == profileTab)
        {
            return true;
        }

        return profileTab.isWidgetActive(tabName, widget);
    }

    public String getWidgetStatus(String menu, String tabName, String widget)
    {
        ProfileTab profileTab = (ProfileTab) map.get(menu);

        if (null == profileTab)
        {
            return ProfileMapsBean.NORMAL;
        }

        return profileTab.getWidgetStatus(tabName, widget);
    }
    
    public boolean isNoteActive()
    {
    	ProfileTab profileTab = (ProfileTab) map.get("note");
    	if (null == profileTab)
    	{
    		return true;
    	}
    	
    	return profileTab.isTabNoteActive("note");
    }

    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        ProfileMapsBeanList profileMapsBeanList = new ProfileMapsBeanList();
        ProfileMaps profileMaps = null;

        profileMapsBeanList.load(1);

        profileMaps = new ProfileMaps(profileMapsBeanList);

        System.out.println(profileMaps.isTabActive("eee", "e"));
        System.out.println(profileMaps.isTabActive("setline", "tab1name"));

    }
}
