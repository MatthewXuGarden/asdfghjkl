package com.carel.supervisor.dataaccess.history.cache.util;



import com.carel.supervisor.dataaccess.history.cache.decorator.sync.SyncCache;
import com.carel.supervisor.dataaccess.history.cache.util.reaper.CacheReaper;
import com.carel.supervisor.dataaccess.history.cache.util.reaper.ReapableCache;

import com.carel.supervisor.dataaccess.history.cache.Cache;
import com.carel.supervisor.dataaccess.history.cache.CacheFactory;


/**
 * @version $Revision: 1.1 $
 * @author $Author: jeffdrost $
 */
public abstract class AbstractPolicyCacheFactory implements CacheFactory
{

    private static final Log LOG    = new Log(AbstractPolicyCacheFactory.class);
    private int              period = 1000;

    public abstract ReapableCache newReapableCache(String cacheName, long timeoutMilliSeconds, int maxSize);


    public Cache newInstance(String cacheName, long timeoutMilliSeconds, int maxSize)
    {

        return CacheReaper.register(                                           //
            new SyncCache(                                                     //
                newReapableCache(cacheName, timeoutMilliSeconds, maxSize)),    //
                period);
    }


    /**
     * time in milliseconds between calls from the reaper.  Every "period"
     * milliseconds this factory's reaper will wake up and call
     * "removeExpiredElements" on the cache.  Note that changing this value will
     * only effect new caches - all existing caches will continue with the same
     * period.
     */
    public int getPeriod()
    {
        return period;
    }


    public void setPeriod(int period)
    {
        this.period = period;
    }
}
