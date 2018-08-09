package com.carel.supervisor.base.config;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;


public class ProductInfoMgr extends InitializableBase
{
    private static ProductInfoMgr me = new ProductInfoMgr();
    private static final String CLASS = "class";
    private IProductInfo productInfo = null;

    private ProductInfoMgr()
    {
        super();
    }

    public static ProductInfoMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        String className = retrieveAttribute(xmlStatic.getNode(0), CLASS, "BSSE0003");
        productInfo = ProductInfoFactory.createProductInfo(className);

        try
        {
            productInfo.load();
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new InvalidConfigurationException("");
        }
    }

    public IProductInfo getProductInfo()
    {
        return productInfo;
    }
}
