package com.carel.supervisor.dataaccess.history.cache.policy.lfu;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.history.cache.util.CacheNode;
import com.carel.supervisor.dataaccess.history.cache.util.LinkedListNode;







/**
 * Class LfuNode
 *
 *
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.1 $
 */
class LfuNode implements CacheNode
{

    /// private static final Logger LOG         = Logger.getLogger(LfuNode.class);
    Object         key         = null;
    Object         value       = null;
    LinkedListNode fifoNode    = null;
    LinkedListNode lfuNode     = null;
    long           timeoutTime = 0;
    int            numUsages   = 0;

    public final boolean isExpired()
    {

        long timeToGo = timeoutTime - System.currentTimeMillis();

        return (timeToGo <= 0);
    }


    public final Object getValue()
    {
        return this.value;
    }


    public final void setValue(Object value)
    {
        this.value = value;
    }

    //Ing. Gilioli Manuel customizzazione
    public void delete(){
    	try {
			((PreparedStatement)this.value).close();
		} catch (SQLException e) {
			LoggerMgr.getLogger(this.getClass()).error("Problema nella chiusura del PreparedStatements eliminato dalla cache"+e.toString());
			e.printStackTrace();
		}
    }

    public String toString()
    {
        return "(lfu-" + String.valueOf(key) + ":u=" + numUsages + ")";
    }
}
