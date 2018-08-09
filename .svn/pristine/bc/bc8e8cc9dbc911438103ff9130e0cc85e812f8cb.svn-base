package com.carel.supervisor.presentation.menu.configuration;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.TabObj;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuTabMgr implements IInitializable
{
    private static MenuTabMgr menuTabMgr = new MenuTabMgr();
    private Map trxTab = null;
    private boolean initialized = false;
    private List lock = null;
    private List canLock = null;
    
    // X gestione plugin
    private String plug_path = null;
    private String plug_name = null;
    
    private MenuTabMgr()
    {
        this.trxTab = new HashMap();
        this.lock = new ArrayList();
        this.canLock = new ArrayList();
        this.initialized = false;
    }

    public static MenuTabMgr getInstance()
    {
        return menuTabMgr;
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
                
                // Controllo nodo per plugin
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

        XMLNode xmlNode = XMLNode.parse(url.openStream());
        
        //xmlNode = MenuHelperMgr.addNodeElements(xmlNode,this.plug_path,this.plug_name);
        
        XMLNode xmlNodeTrx = null;
        XMLNode xmlNodeObj = null;
        XMLNode[] xmlNodeChl = null;
        String nameTrx = "";
        String nameObj = "";
        String lock = "";
        String canLock = "";

        int childCount = 0;

        MenuTab menuTab = null;
        TabObj tabObj = null;

        for (int i = 0; i < xmlNode.size(); i++)
        {
            xmlNodeTrx = xmlNode.getNode(i);

            if (xmlNodeTrx != null)
            {
                nameTrx = xmlNodeTrx.getAttribute("name");

                // For lock user - start
                try
                {
                    lock = xmlNodeTrx.getAttribute("lock");
                }
                catch (Exception e)
                {
                }

                try
                {
                    canLock = xmlNodeTrx.getAttribute("canlock");
                }
                catch (Exception e)
                {
                }

                if ((lock != null) && lock.equalsIgnoreCase("true"))
                {
                    this.lock.add(nameTrx);
                }

                if ((canLock != null) && canLock.equalsIgnoreCase("true"))
                {
                    this.canLock.add(nameTrx);
                }

                // For lock user - end
                childCount = xmlNodeTrx.size();
                menuTab = new MenuTab(nameTrx, childCount);

                for (int j = 0; j < childCount; j++)
                {
                    xmlNodeObj = xmlNodeTrx.getNode(j);

                    if (xmlNodeObj != null)
                    {
                        nameObj = xmlNodeObj.getAttribute("name");
                        tabObj = new TabObj(nameObj);
                        xmlNodeChl = xmlNodeObj.getNodes();

                        if (xmlNodeChl != null)
                        {
                            for (int k = 0; k < xmlNodeChl.length; k++)
                            {
                                tabObj.addProperties(xmlNodeChl[k].getAttribute(
                                        "name"), xmlNodeChl[k].getTextValue());
                            }
                        }

                        menuTab.addTab(tabObj);
                    }
                }

                this.trxTab.put(nameTrx, menuTab);
            }
        }
    }

    public MenuTab getTabMenuFor(String nameTrx)
    {
        return (MenuTab) this.trxTab.get(nameTrx);
    }

    public boolean lock(String trx)
    {
        boolean ret = false;

        if (trx != null)
        {
            if (this.lock.contains(trx))
            {
                ret = true;
            }
        }

        return ret;
    }

    public boolean canLock(String trx)
    {
        boolean ret = false;

        if (trx != null)
        {
            if (this.canLock.contains(trx))
            {
                ret = true;
            }
        }

        return ret;
    }
}
