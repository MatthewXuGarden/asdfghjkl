package com.carel.supervisor.controller.database;

import com.carel.supervisor.controller.function.*;
import com.carel.supervisor.dataaccess.db.*;
import java.util.*;


public class FunctionList
{
    private Map calcElementData = new HashMap();

    public FunctionList(String dbId, String plantId, Integer idSite)
        throws DataBaseException
    {
        String sql = "SELECT * from cffunction where pvcode = ? and idsite = ? and functioncode>0 order by functioncode asc, operorder desc";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, idSite });
        load(recordSet);
        sql = "SELECT * from cffunction where pvcode = ? and idsite = ? and functioncode<0 order by functioncode asc, operorder desc";
        recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, idSite });
        load(recordSet);
    }

    private void load(RecordSet recordSet)
    {
    	Record record = null;
        CalcElementData calc = null;
        CalcElementData calcPrec = null;
        int actualFuncId = 0;

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            calc = new CalcElementData(record);

            if (actualFuncId != calc.getFunctionId())
            {
                calcPrec = calc;
                actualFuncId = calc.getFunctionId();
                calcElementData.put(new Integer(actualFuncId), calc);
            }
            else
            {
                calcPrec.setNext(calc);
                calcPrec = calc;
            }
        }
    }
    
    public CalcElementData get(Integer idFuncCode)
    {
        return (CalcElementData) calcElementData.get(idFuncCode);
    }
}
