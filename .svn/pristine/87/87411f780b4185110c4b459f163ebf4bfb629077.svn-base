package com.carel.supervisor.director.ide;

import com.carel.supervisor.dataaccess.db.Record;
import java.util.*;


public class Device
{
    private Record record = null;
    private List variables = new ArrayList();

    public Device(Record record)
    {
        this.record = record;
    }

    public Record getDevice()
    {
        return record;
    }

    public void addVariable(Record record)
    {
        variables.add(record);
    }

    public Record getVariable(int i)
    {
        return (Record) variables.get(i);
    }

    public int size()
    {
        return variables.size();
    }
}
