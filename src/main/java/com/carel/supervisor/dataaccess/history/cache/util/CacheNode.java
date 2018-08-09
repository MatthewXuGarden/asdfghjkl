package com.carel.supervisor.dataaccess.history.cache.util;



/**
 * Class CacheNode
 *
 *
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.6 $
 */
public interface CacheNode
{

    void setValue(Object value);


    Object getValue();


    boolean isExpired();
}
