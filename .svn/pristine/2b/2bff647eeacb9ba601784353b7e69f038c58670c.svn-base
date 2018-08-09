package com.carel.supervisor.base.config;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class ProductInfoFactory
{
    public ProductInfoFactory()
    {
    }

    public static IProductInfo createProductInfo(String className)
    {
        IProductInfo productInfo = new ProductInfoDummy();

        try
        {
            productInfo = (IProductInfo) FactoryObject.newInstance(className);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(ProductInfoFactory.class);
            logger.equals(e);
        }
        
        return productInfo;
    }
}
