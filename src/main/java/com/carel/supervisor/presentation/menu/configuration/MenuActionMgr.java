package com.carel.supervisor.presentation.menu.configuration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.menu.ActionObj;
import com.carel.supervisor.presentation.menu.MenuAction;


public class MenuActionMgr implements IInitializable
{
    private static MenuActionMgr menuActMgr = new MenuActionMgr();
    private Map trxTab = null;
    private boolean initialized = false;
    
    // X gestione plugin
    private String plug_path = null;
    private String plug_name = null;
    
    private MenuActionMgr()
    {
        trxTab = new HashMap();
        initialized = false;
    }

    public static MenuActionMgr getInstance()
    {
        return menuActMgr;
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            XMLNode xmlNodeTmp = null;

            try
            {
                xmlNodeTmp = xmlStatic.getNode(0);

                String type = xmlNodeTmp.getAttribute("type");
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
                
                init(type, path, name);
            }
            catch (Exception ex)
            {
                FatalHandler.manage(this, "...", ex);
            }

            initialized = true;
        }
    }

    private void init(String type, String path, String name)
        throws Exception
    {
        URL url = ResourceLoader.fileFromResourcePath(path, name);

        if (url == null)
        {
            throw new Exception("No configuration menu found");
        }

        XMLNode xmlNodeTrx = null;
        XMLNode xmlNodeTab = null;
        XMLNode xmlNodeAct = null;

        String trxName = "";
        String tabName = "";
        String actName = "";
        int numTab = 0;
        int numAct = 0;

        String actAction = "";
        String actToolty = "";
        String actIStan = "";
        String actIOver = "";
        String actIClick = "";
        String actIDisab = "";
        String actDesc = "";

        ActionObj actionObj = null;
        MenuAction menuAction = null;

        XMLNode xmlNode = XMLNode.parse(url.openStream());
        //xmlNode = MenuHelperMgr.addNodeElements(xmlNode,this.plug_path,this.plug_name);
        
        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTrx = xmlNode.getNode(i);

            if (xmlNodeTrx != null)
            {
                trxName = xmlNodeTrx.getAttribute("name");
                numTab = xmlNodeTrx.size();

                for (int j = 0; j < numTab; j++)
                {
                    xmlNodeTab = xmlNodeTrx.getNode(j);

                    if (xmlNodeTab != null)
                    {
                        tabName = xmlNodeTab.getAttribute("name");
                        numAct = xmlNodeTab.size();
                        menuAction = new MenuAction(tabName, numAct);

                        for (int k = 0; k < numAct; k++)
                        {
                            xmlNodeAct = xmlNodeTab.getNode(k);

                            if (xmlNodeAct != null)
                            {
                                actName = xmlNodeAct.getAttribute("name");

                                actAction = xmlNodeAct.getNode("action")
                                                      .getTextValue();
                                actToolty = xmlNodeAct.getNode("tooltip")
                                                      .getTextValue();
                                actIStan = xmlNodeAct.getNode("iconStanby")
                                                     .getTextValue();
                                actIOver = xmlNodeAct.getNode("iconOver")
                                                     .getTextValue();
                                actIClick = xmlNodeAct.getNode("iconClick")
                                                      .getTextValue();
                                actIDisab = xmlNodeAct.getNode("iconDisable")
                                                      .getTextValue();

                                if(xmlNodeAct.getNode("description")!=null)
                                	actDesc = xmlNodeAct.getNode("description").getTextValue();
                                else 
                                	actDesc="";

                                actionObj = new ActionObj(actName, actToolty,
                                        actIStan);
                                actionObj.setAction(actAction);
                                actionObj.setIconClick(actIClick);
                                actionObj.setIconDisable(actIDisab);
                                actionObj.setIconOver(actIOver);
                                
                                actionObj.setDescription(actDesc);

                                menuAction.addAct(actionObj);
                            }
                        }

                        this.trxTab.put(buildKey(trxName, tabName), menuAction);
                    }
                }
            }
        }
    }

    public MenuAction getActMenuFor(String nameTrx, String nameTab)
    {
        return (MenuAction) this.trxTab.get(buildKey(nameTrx, nameTab));
    }

    private String buildKey(String trx, String tab)
    {
        return trx + "_" + tab;
    }
}
