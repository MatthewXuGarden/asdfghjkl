package com.carel.supervisor.presentation.menu.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.GroupBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.menu.MenuSection;
import com.carel.supervisor.presentation.menu.MenuVoce;
import com.carel.supervisor.presentation.session.UserSession;


public class MenuConfigMgr implements IInitializable
{
    private static MenuConfigMgr menuConfMgr = new MenuConfigMgr();
    public static final String SECTION_GROUP = "section5";
    private List config = null;
    private List menu = null;
    private boolean initialized = false;
    
    // X gestione plugin
    private String plug_path = null;
    private String plug_name = null;
    
    private MenuConfigMgr()
    {
        config = new ArrayList();
        menu = new ArrayList();
        initialized = false;
    }

    public static MenuConfigMgr getInstance()
    {
        return menuConfMgr;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            XMLNode xmlNodeTmp = null;

            try
            {
                xmlNodeTmp = xmlStatic.getNode(0);

                String path = xmlNodeTmp.getAttribute("path");
                String name = xmlNodeTmp.getAttribute("name");
                
                try
                {
                    xmlNodeTmp = xmlStatic.getNode(1);
                    this.plug_path = xmlNodeTmp.getAttribute("path");
                    this.plug_name = xmlNodeTmp.getAttribute("name");
                }
                catch(Exception e){
                    // NOP
                }
                
                init(path, name);
            }
            catch (Exception ex)
            {
                FatalHandler.manage(this, "...", ex);
            }

            initialized = true;
        }
    }

    private void init(String path, String name) throws Exception
    {
        URL url = ResourceLoader.fileFromResourcePath(path, name);
        XMLNode[] section = null;
        MenuSection tmp = null;
        MenuConfig mcTmp = null;
        String confName = "";
        String sect = "";
        String imgn = "";

        // Controllo esistenza file di configurazione
        if (url == null)
        {
            throw new Exception("No configuration menu found");
        }

        XMLNode xmlNode = XMLNode.parse(url.openStream());
        //xmlNode = MenuHelperMgr.addNodeElements(xmlNode,this.plug_path,this.plug_name);

        for (int i = 0; i < xmlNode.size(); i++)
        {
            confName = xmlNode.getNode(i).getAttribute("id");
            config.add(new MenuConfig(confName));

            // Sezione della configurazione
            section = xmlNode.getNode(i).getNodes();

            for (int a = 0; a < section.length; a++)
            {
                if (section[a] != null)
                {
                    sect = section[a].getAttribute("id");
                    imgn = section[a].getAttribute("img");

                    mcTmp = getConfiguration(confName);

                    if (mcTmp != null)
                    {
                        mcTmp.addSection(new MenuSection(sect, imgn));
                    }

                    XMLNode[] voce = section[a].getNodes();

                    if (voce != null)
                    {
                        for (int b = 0; b < voce.length; b++)
                        {
                            if (voce[b] != null)
                            {
                                mcTmp = getConfiguration(confName);

                                if (mcTmp != null)
                                {
                                    tmp = mcTmp.getSection(sect);
                                }

                                if (tmp != null)
                                {
                                    tmp.addVoce(new MenuVoce(voce[b].getAttribute(
                                                "id"), voce[b].getTextValue()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public MenuSection[] getGroupSection()
    {
    	if(menu == null)
    	{
    		return new MenuSection[0];
    	}
    	MenuSection[] section = new MenuSection[menu.size()];
    	for(int i=0;i<menu.size();i++)
    	{
    		section[i] = (MenuSection)menu.get(i);
    	}
    	return section;
    }
    public MenuSection getMenuSectionBySectionName(String sectionName)
    {
    	if(menu == null)
    	{
    		return null;
    	}
    	MenuSection temp = null;
    	for(int i=0;i<menu.size();i++)
    	{
    		temp = (MenuSection)menu.get(i);
    		if(temp.getSectionName().equals(sectionName))
    		{
    			return temp;
    		}
    	}
    	return null;
    }
    public void completeMenuConfiguration(UserSession us)
        throws Exception
    {
        menu.clear();

        GroupListBean groups = us.getGroup();
        GroupBean gb = null;
        MenuSection menuSection = null;

        int[] groupId = groups.getIds();
        
        menuSection = new MenuSection(SECTION_GROUP, "");
        if (groupId != null)
        {
            for (int j = 0; j < groupId.length; j++)
            {
                gb = groups.get(groupId[j]);

                if (gb.isGlobal())
                {
                	menuSection.addVoce(new MenuVoce(
                            gb.getDescription(),
                            buildDinamikLink(1),
                            1));
                }
                else
                {
                    menuSection.addVoce(new MenuVoce(gb.getDescription(),
                            buildDinamikLink(groupId[j]),
                            groupId[j]));
                }
            }

            menu.add(menuSection);
        }
    }


    public MenuSection[] getMenuSection()
    {
        MenuConfig mc = getConfiguration("conf1");
        String[] section = mc.getSectionName();
        MenuSection[] mSection = new MenuSection[section.length];

        for (int i = 0; i < mSection.length; i++)
        {
            mSection[i] = mc.getSection(section[i]);
        }

        return mSection;
    }
    
    public MenuVoce getMenuVoce(String sectionid,String voceid)
    {
    	MenuConfig mc = getConfiguration("conf1");
        String[] section = mc.getSectionName();
        MenuSection[] mSection = new MenuSection[section.length];
        for (int i = 0; i < mSection.length; i++)
        {
            mSection[i] = mc.getSection(section[i]);
            if(mSection[i].getSectionName().equals(sectionid))
            {
            	MenuVoce[] voce = mSection[i].getListVoci();
            	for(int j=0;j<voce.length;j++)
            	{
            		if(voce[j].getName().equals(voceid))
            		{
            			return voce[j];
            		}
            	}
            }
        }

        return null;
    }
    private MenuConfig getConfiguration(String name)
    {
        MenuConfig mc = null;

        if (config != null)
        {
            for (int i = 0; i < config.size(); i++)
            {
                mc = (MenuConfig) config.get(i);

                if ((mc != null) && (mc.getConfName().equalsIgnoreCase(name)))
                {
                    break;
                }
            }
        }

        return mc;
    }

    private String buildDinamikLink(int idGroup)
    {
    	return buildDinamikLink(idGroup,"menu");
    }
    private String buildDinamikLink(int idGroup,String type)
    {
        StringBuffer sb = new StringBuffer("nop");
        sb.append("&group=" + idGroup);
        sb.append("&folder=grpview");
        sb.append("&bo=BGrpView");
        sb.append("&type="+type);

        return sb.toString();
    }

    public String getGroupCombox(int idgroup,UserSession us)
    {
    	String global_desc = LangMgr.getInstance().getLangService(us.getLanguage()).getString("menu", "entry1");
    	String result = "";
    	GroupListBean groups = us.getGroup();
    	int[] gids = groups.getIds();
    	for(int i=0;i<gids.length;i++)
    	{
    		if(i == 0)
    		{
    			result += "<select class='group_combo' id='combo_group' onchange=\"document.getElementById('combo_group').disabled=true;go_to_group();\">";
    		}
    		GroupBean gb = groups.get(gids[i]);
    		String selected = idgroup==gids[i]?" selected ":"";
    		result += "<option value='"+buildDinamikLink(gids[i],"tab")+"'"+selected+">"+(gids[i]==1?global_desc:gb.getDescription())+"</option>\n";
    		if(i == gids.length-1)
    		{
    			result += "</select>";
    		}
    	}
    	return result;
    }
}
