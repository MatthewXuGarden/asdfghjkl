package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.util.*;


public abstract class DBTypeBase implements IDBType
{
    private List types = new ArrayList();

    public int getType(int pos)
    {
        return ((Integer) types.get(pos)).intValue();
    }

    public int numTypes()
    {
        return types.size();
    }

    public void addType(int type)
    {
        types.add(new Integer(type));
    }
}
