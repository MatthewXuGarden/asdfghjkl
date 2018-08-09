package com.carel.supervisor.dataaccess.history.cache.util.reaper;



import com.carel.supervisor.dataaccess.history.cache.Cache;

import java.util.Timer;

import com.carel.supervisor.dataaccess.history.cache.util.Log;


/**
 * Class CacheReaper
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.4 $
 */
public class CacheReaper
{

    private static final Log   LOG   = new Log(CacheReaper.class);
    private static final Timer TIMER = new Timer(true);

    public static Cache register(ReapableCache cache, long period)
    {

        LOG.debug("register : " + cache);
        TIMER.scheduleAtFixedRate(new ReaperTask(cache), period, period);

        return cache;
    }
}
