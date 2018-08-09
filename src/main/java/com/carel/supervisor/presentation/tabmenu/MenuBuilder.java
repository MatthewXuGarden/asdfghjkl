package com.carel.supervisor.presentation.tabmenu;

import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.presentation.menu.ActionObj;
import com.carel.supervisor.presentation.menu.MenuAction;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.TabObj;
import com.carel.supervisor.presentation.menu.configuration.MenuActionMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;


public class MenuBuilder
{
    private static int numCurTabAction = 0;

    public static Tab getFirstPage(UserSession userSess, String section,
        boolean enable)
    {
        MenuTab tabMenu = MenuTabMgr.getInstance().getTabMenuFor(section);
        TabObj tabobj = null;

        if (tabMenu != null)
        {
            for (int i = 0; i < tabMenu.getNumTab(); i++)
            {
                tabobj = tabMenu.getTab(i);

                if (tabobj != null)
                {
                    if (userSess.isTabActive(section, tabobj.getIdTab()))
                    {
                        Tab tab = new Tab(userSess.getLanguage(), section);
                        tab.setTabName(tabobj.getIdTab());
                        tab.setTooltip(tabobj.getProperties("tooltip"));
                        tab.setPageLink(tabobj.getProperties("resource"));
                        tab.setPage(tabobj.getProperties("resource"));

                        return tab;
                    }
                }
            }
        }

        return null; //NON DOVREBBE mai passare di qui
    }

    public static String buildTabMenu(UserSession userSess, String section,
        boolean enable)
    {
        MenuTab tabMenu = MenuTabMgr.getInstance().getTabMenuFor(section);
        TabList listaTab = new TabList(enable);
        Tab tab = null;
        TabObj tabobj = null;
        String ret = "";

        String tmp_resource = ""; //aggiunta x note

        if (tabMenu != null)
        {
            for (int i = 0; i < tabMenu.getNumTab(); i++)
            {
                tabobj = tabMenu.getTab(i);

                if (tabobj != null)
                {
                	if(PacketMgr.getInstance().checkForTabRestriction(section, tabobj.getIdTab()))
                	{
                		if(!PacketMgr.getInstance().isFunctionAllowed(section))
                			continue;
                	}
                	
                	if (userSess.isTabActive(section, tabobj.getIdTab()))
                    {
                        tab = new Tab(userSess.getLanguage(), section);
                        tab.setTabName(tabobj.getIdTab());
                        tab.setTooltip(tabobj.getProperties("tooltip"));
                        tab.setPageLink(tabobj.getProperties("resource"));
                        tab.setPage(tabobj.getProperties("resource"));
                        listaTab.addTab(tab);
                    }
                }
            }

            ret = listaTab.getHTMLTabList(userSess,section, "");
        }

        return ret;
    }

    public static int getPositionPage(UserSession userSess, String section,
        String page, boolean enable)
    {
        MenuTab tabMenu = MenuTabMgr.getInstance().getTabMenuFor(section);
        TabList listaTab = new TabList(enable);
        TabObj tabobj = null;
        int count = 0;

        if (tabMenu != null)
        {
            for (int i = 0; i < tabMenu.getNumTab(); i++)
            {
                tabobj = tabMenu.getTab(i);

                if (tabobj != null)
                {
                    if (userSess.isTabActive(section, tabobj.getIdTab()))
                    {
                        count++;

                        //if (page.equals(tabobj.getProperties("resource")))
                        //{
                        //    return count;
                        //}
                        // Intervento per gestire corretto Tab Attivo
                        if (tabobj.getProperties("resource").startsWith(page))
                        {
                            return count;
                        }
                    }
                }
            }
        }

        // Tech for Note.jsp
        if ((page != null) && page.endsWith("note.jsp"))
        {
            return count;
        }
        else
        {
            return 1;
        }
    }

    public static int getNumActionMenu()
    {
        return numCurTabAction;
    }

    public static String buildActionMenu(UserSession userSess, String section,
        String tab)
    {
        MenuAction actMenu = MenuActionMgr.getInstance().getActMenuFor(section,
                tab);
        SubTabList listaSubTab = new SubTabList();
        SubTab subTab = null;
        ActionObj actObj = null;
        String ret = "";

        if (actMenu != null)
        {
            for (int i = 0; i < actMenu.getNumAct(); i++)
            {
                actObj = actMenu.getAct(i);

                if (actObj != null)
                {
                	subTab = new SubTab(userSess.getLanguage(), section);
                    subTab.setName(actObj.getName());
                    subTab.setAction(actObj.getAction());
                    subTab.setTooltip(actObj.getTooltip());
                    subTab.setPathIconStandby(actObj.getIconStanby());
                    subTab.setPathIconOver(actObj.getIconOver());
                    subTab.setPathIconDisabled(actObj.getIconDisable());
                    subTab.setDescription(actObj.getDescription());
                    
                    if (userSess.isButtonActive(section, tab, actObj.getName()))
                    {
                    	subTab.setVisible(true);
                    }
                    
                    listaSubTab.addSubTab(subTab);
                }
            }

            ret = listaSubTab.getHTMLSubTabList();
        }

        numCurTabAction = listaSubTab.numSubTabs();

        return ret;
    }

    
    public static String buildActionNoteMenu(UserSession userSess,
        String section, String tab)
    {
        MenuAction actMenu = MenuActionMgr.getInstance().getActMenuFor(section,
                tab);
        SubTabList listaSubTab = new SubTabList();
        SubTab subTab = null;
        ActionObj actObj = null;
        String ret = "";
        boolean onlyread = true;

        if (actMenu != null)
        {
            for (int i = 0; i < actMenu.getNumAct(); i++)
            {
                actObj = actMenu.getAct(i);

                if (actObj != null)
                {
                	subTab = new SubTab(userSess.getLanguage(), section);
                    subTab.setName(actObj.getName());
                    subTab.setAction(actObj.getAction());
                    subTab.setTooltip(actObj.getTooltip());
                    subTab.setPathIconStandby(actObj.getIconStanby());
                    subTab.setPathIconOver(actObj.getIconOver());
                    subTab.setPathIconDisabled(actObj.getIconDisable());
                    subTab.setDescription(actObj.getDescription());
                    
                    if (userSess.isButtonActive(section, tab, actObj.getName()))
                    {
                    	subTab.setVisible(true);
                    }
                    
                    listaSubTab.addSubTab(subTab);
                    onlyread = false;
                }
            }

            ret = listaSubTab.getHTMLSubTabList();
        }

        numCurTabAction = listaSubTab.numSubTabs();

        if (onlyread)
        {
            userSess.setProperty("onlyreadnote", "yes");
        }
        else
        {
            userSess.setProperty("onlyreadnote", "no");
        }

        return ret;
    }
}
