package com.carel.supervisor.dataaccess.monitor;

public class StatementDummy implements IStatement
{
    private CounterDummy c = new CounterDummy();

    public ICounter retrieve(String sql)
    {
        return c;
    }

    public Object[][] result()
    {
        return null;
    }
    
    public void clear()
    {

    }
}
