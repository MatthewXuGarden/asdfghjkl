package com.carel.supervisor.dataaccess.history.cache.policy.zero;



import com.carel.supervisor.dataaccess.history.cache.Cache;
import com.carel.supervisor.dataaccess.history.cache.CacheFactory;


/**
 * Creates a cache that never stores anything you give it.  This is a non-cache.
 *
 *
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.4 $
 */
public class ZeroCacheFactory implements CacheFactory
{

    public static final CacheFactory NULL_CACHE_FACTORY = new ZeroCacheFactory();
    public static final Cache        NULL_CACHE         = new ZeroCache();

    /**
     * Method newInstance
     */
    public Cache newInstance(String cacheName, long timeoutMilliSeconds, int maxSize)
    {
        return NULL_CACHE;
    }


    public String toString()
    {
        return "ZeroCacheFactory";
    }
}
