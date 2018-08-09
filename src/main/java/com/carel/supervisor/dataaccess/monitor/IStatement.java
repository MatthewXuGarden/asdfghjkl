package com.carel.supervisor.dataaccess.monitor;

public interface IStatement
{
    public abstract ICounter retrieve(String sql);

    public abstract Object[][] result();
    
    public abstract void clear();
}
