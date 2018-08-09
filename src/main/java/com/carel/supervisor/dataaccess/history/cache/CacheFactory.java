package com.carel.supervisor.dataaccess.history.cache;



/**
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.7 $
 */
public interface CacheFactory
{
    Cache newInstance(String cacheName, long timeoutMilliSeconds, int maxSize);
}
