package com.carel.supervisor.controller.database;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfoList;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;


public class AlarmCarelLoader
{
    private AlarmCarelLoader()
    {
    }

    //LDAC: a prescindere dal protocollo
    public static List<Variable> loadAllarm(String dbId, String plantId, Integer idSite)
    {
        List<Variable> allarmList = new ArrayList<Variable>();
        VariableInfoList varInfoList = (VariableInfoList) DataConfigMgr.getInstance()
                                                                       .getConfig("cfvars");
        VariableInfo varInfo = null;
        VariableMgr vrbMgr = VariableMgr.getInstance();

        for (int i = 0; i < varInfoList.size(); i++)
        {
            varInfo = varInfoList.get(i);

            //Se la variabile è di allarme ed è da notificare
            if ((VariableInfo.TYPE_ALARM == varInfo.getType()) &&
                    (varInfo.isActive()))
            {
                allarmList.add(vrbMgr.getById(varInfo.getId()));
            }
        }

        return allarmList;
    }
}
