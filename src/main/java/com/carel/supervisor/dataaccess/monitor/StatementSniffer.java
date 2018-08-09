package com.carel.supervisor.dataaccess.monitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class StatementSniffer implements IStatement
{
    private Map map = new HashMap();

    public ICounter retrieve(String sql)
    {
        Counter counter = (Counter) map.get(sql);

        if (null == counter)
        {
            counter = new Counter();
            map.put(sql, counter);
        }

        return counter;
    }

    public Object[][] result()
    {
        String[] sql = null;
        Long[] count = new Long[map.size()];
        Long[] time = new Long[map.size()];
        Long[] max = new Long[map.size()];
        Long[] min = new Long[map.size()];
        sql = (String[]) map.keySet().toArray(new String[map.size()]);
        Arrays.sort(sql);

        Counter c = null;

        for (int i = 0; i < sql.length; i++)
        {
            c = (Counter) map.get(sql[i]);
            count[i] = new Long(c.getCounter());
            time[i] = new Long(c.getTotal());
            max[i] = new Long(c.getMax());
            min[i] = new Long(c.getMin());
        }

        Object[][] values = new Object[][] { sql, count, time, max, min };

        return values;
    }
    
    public void clear()
    {
    	map.clear();
    }
}
