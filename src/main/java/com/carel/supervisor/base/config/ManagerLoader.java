package com.carel.supervisor.base.config;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;


public class ManagerLoader
{
    private static final String COMPONENT = "component";

    //private static final String NAME = "name";
    private ManagerLoader()
    {
    }

    public static void load(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        XMLNode xmlTmp = null;
        
        MethodInvoker methodInvoker = new MethodInvoker();
        String className = null;
        boolean blockingError = false;
        boolean startOnload = true;
        Logger logger = LoggerMgr.getLogger("CONFIG");
        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);

            if (!xmlTmp.getNodeName().equals(COMPONENT))
            {
                throw new InvalidConfigurationException("");
            }

            try
            {
            	className = xmlTmp.getAttribute("class");
            	blockingError = xmlTmp.getAttribute("blockingerror",true);
            	startOnload = xmlTmp.getAttribute("startonload",true);
            	if (startOnload)
            	{
            		methodInvoker.invoke(className, blockingError, xmlTmp);
            	}
            	logger.info("LOADED MODULE [" + className + "]");
            }
            catch (Exception ex)
            {
                throw new InvalidConfigurationException("class " +
                    xmlTmp.getAttribute("class"), ex);
            }
        }
    }
}
