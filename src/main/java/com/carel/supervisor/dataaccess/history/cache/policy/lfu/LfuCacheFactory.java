package com.carel.supervisor.dataaccess.history.cache.policy.lfu;



import com.carel.supervisor.dataaccess.history.cache.util.AbstractPolicyCacheFactory;
import com.carel.supervisor.dataaccess.history.cache.util.reaper.ReapableCache;



/**
 * Creates a least-frequently-used cache.
 *
 *
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.7 $
 */
public class LfuCacheFactory extends AbstractPolicyCacheFactory
{

    public ReapableCache newReapableCache(String cacheName, long timeoutMilliSeconds, int maxSize)
    {
        return new LfuCache(cacheName, timeoutMilliSeconds, maxSize);
    }


    public String toString()
    {
        return "LfuCacheFactory";
    }
}
