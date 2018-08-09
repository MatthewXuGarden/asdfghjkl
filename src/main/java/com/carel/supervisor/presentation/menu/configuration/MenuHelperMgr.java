package com.carel.supervisor.presentation.menu.configuration;

import java.net.URL;

import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;

public class MenuHelperMgr
{
    public static XMLNode addNodeElements(XMLNode xmlNode,String path,String prefix)
    {
        try
        {
            if(path != null && prefix != null)
            {
                URL[] fileNameList = ResourceLoader.fromResourcePathFiltered(path,"xml",prefix);
                if(fileNameList != null)
                {
                    for (int i = 0; i < fileNameList.length; i++)
                    {
                        if(fileNameList[i] != null)
                        {
                            try
                            {
                                XMLNode xmlNodeTmp = XMLNode.parse(fileNameList[i].openStream());
                                for(int j=0; j<xmlNodeTmp.size(); j++)
                                    xmlNode.addNode(xmlNodeTmp.getNode(j));
                            }
                            catch(Exception e)
                            {
                                Logger logger = LoggerMgr.getLogger(MenuHelperMgr.class);
                                if(fileNameList[i] != null)
                                    logger.error("Enable to load: " + fileNameList[i].getFile());
                                else
                                    logger.error(e);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            Logger logger = LoggerMgr.getLogger(MenuHelperMgr.class);
            logger.error(e);
        }
        
        return xmlNode;
    }
}
