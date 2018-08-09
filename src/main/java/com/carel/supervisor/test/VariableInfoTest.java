package com.carel.supervisor.test;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;

public class VariableInfoTest extends VariableInfo
{
    public VariableInfoTest(Integer site, Integer id, Integer frequency, Integer priority, int type)
    {
        super();
        
        super.setFrequency(frequency);
        super.setSite(site);
        super.setPriority(priority);
        super.setId(id);
        super.setType(type);
    }
    
}
