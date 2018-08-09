package com.carel.supervisor.controller.rule;

import com.carel.supervisor.action.AbstractAction;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.field.Variable;
import java.util.Date;


public class AlarmRule extends Rule
{
    public AlarmRule(Integer idRule, int delay, Variable variable,
        TimeValidity timeValidity, AbstractAction action)
    {
        super(idRule, delay, variable, timeValidity, action);
    }

    public Object[] toObject(Date now) throws DataBaseException
    {
        VariableInfo variableInfo = variable.getInfo();
        Object[] values = new Object[16];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "hsalarm", "idalarm");
        values[2] = variableInfo.getSite();
        values[3] = variableInfo.getDevice();
        values[4] = variableInfo.getId(); //;condition.getConditionCode(); //condition
        values[5] = variableInfo.getPriority();
        values[6] = "FALSE";
        values[7] = now; //start time
        values[8] = null;
        values[9] = null;
        values[10] = null;
        values[11] = null;
        values[12] = null;
        values[13] = null;
        values[14] = null;
        values[15] = now;

        return values;
    }

    public Object[] toObjectAllarmFinished(Date now) throws DataBaseException
    {
        VariableInfo variableInfo = variable.getInfo();
        Object[] values = new Object[3];
        values[0] = variableInfo.getSite();
        values[1] = variableInfo.getId(); //condition.getConditionCode();
        values[2] = now;

        return values;
    }

    public Integer getIdDevice()
    {
        return variable.getInfo().getDevice();
    }
}
