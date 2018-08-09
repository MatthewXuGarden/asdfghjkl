package com.carel.supervisor.presentation.menu;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangSection;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.session.UserSession;


public class MenuLoader
{
    public static final String ML_SEZIONI = "section";
    public static final String ML_VOCI = "entry";

    public MenuLoader()
    {
    }

    public static MainMenu create(UserSession us)
    {
        return MenuLoader.create(us.getLanguage(), us);
    }

    public static MainMenu create(String language, UserSession us)
    {
        LangSection ls = LangMgr.getInstance().getLangService(language).getSection("menu");
        boolean visibilityGroups = us.localVisibility();
        
        MainMenu mainMenu = new MainMenu();
        mainMenu.setExitLabel(ls.get("section20"));
        mainMenu.setExitQuest(ls.get("entry999"));

        MenuSection[] listSection = MenuConfigMgr.getInstance().getGroupSection();
        MenuVoce[] listVoci = null;
        
        //the section that holds groups. defined in MenuConfigMgr.SECTION_GROUP
        //now is section5
        MenuSection groupSection = null;

        Group group = null;
        Section section = null;

        try
        {
            group = new Group();
            groupSection = MenuConfigMgr.getInstance().getMenuSectionBySectionName(MenuConfigMgr.SECTION_GROUP);

            listSection = MenuConfigMgr.getInstance().getMenuSection();

            for (int j = 0; j < listSection.length; j++)
            {
                section = new Section(ls.get(listSection[j].getSectionName()),
                        listSection[j].getSectionImg(),listSection[j].getSectionName());
                
                //1. add link in configuration.xml
                listVoci = listSection[j].getListVoci();
                for (int k = 0; k < listVoci.length; k++)
                {
                    if (us.isMenuActive(listVoci[k].getName()))
                    {
                    	if(PacketMgr.getInstance().checkForMenuRestriction(listVoci[k].getName()))
                    	{
                    		if(PacketMgr.getInstance().isFunctionAllowed(listVoci[k].getName()))
                    			section.add(ls.get(listVoci[k].getName()), listVoci[k].getLink());
                    	}
                    	else
                    	{
                    		section.add(ls.get(listVoci[k].getName()), listVoci[k].getLink());
                    	}
                    }
                }
                //2. add link for group
                if(visibilityGroups && listSection[j].getSectionName().equalsIgnoreCase(MenuConfigMgr.SECTION_GROUP))
                {
                	if (groupSection != null)
                    {
                        listVoci = groupSection.getListVoci();
                    }
                    else
                    {
                        listVoci = new MenuVoce[0];
                    }
//                    	for (int k = 0; k < listVoci.length; k++)
//                        {
//                    		section.add(listVoci[k].getName(), listVoci[k].getLink());
//                        }
                	//only get Global
                	if(listVoci.length>0)
                	{
                		//section.add(listVoci[0].getName(), listVoci[0].getLink());
                		section.add(ls.get("entry1"), listVoci[0].getLink());
                	}
                }
                if (0 < section.size()) //Solo se ho voci nella sezione essa viene visualizzata
                {
                    group.addSection(section);
                }
            }

            mainMenu.setGroup(group);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(MenuLoader.class);
            logger.error(e);
        }

        return mainMenu;
    }
}
