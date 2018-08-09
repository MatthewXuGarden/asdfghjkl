package com.carel.supervisor.dataaccess.history.cache.decorator.sync;



import com.carel.supervisor.dataaccess.history.cache.util.AbstractDecoratorCacheFactory;

import com.carel.supervisor.dataaccess.history.cache.Cache;


/**
 * @version $Revision: 1.5 $
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 */
public class SyncCacheFactory extends AbstractDecoratorCacheFactory
{

    protected Cache wrapDelegate(String cacheName, Cache delegateCache)
    {
        return new SyncCache(delegateCache);
    }


    public String toString()
    {
        return "SyncCacheFactory->" + getDelegate();
    }
}
