package com.carel.supervisor.action;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.rule.AlarmRule;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList;
import com.carel.supervisor.device.*;
import java.sql.Timestamp;
import java.util.Date;


public class ActionLogAlarm extends AbstractAction
{
    public void calledOffStatus(Rule rule, Date now) throws Exception
    {
        AlarmLogList alarmLogList = new AlarmLogList();
        Object[] values = ((AlarmRule) rule).toObjectAllarmFinished(now);
        alarmLogList.alarmFinished(null, BaseConfig.getPlantId(),
            (Integer) values[0], (Integer) values[1], (Timestamp) values[2]);

        Integer idDevice = ((AlarmRule) rule).getIdDevice();

        if (null != idDevice)
        {
            DeviceStatusMgr.getInstance().removeAlarm(idDevice);
        }
    }

    public void toManageStatus(Rule rule, Date now) throws Exception
    {
        AlarmLogList alarmLogList = new AlarmLogList();
        alarmLogList.insert(null, BaseConfig.getPlantId(),
            ((AlarmRule) rule).toObject(now));

        Integer idDevice = ((AlarmRule) rule).getIdDevice();

        if (null != idDevice)
        {
            DeviceStatusMgr.getInstance().addAlarm(idDevice);
        }
    }

    public void aRalreadyManagedStatus(Rule rule, Date now) throws Exception
    {
        Integer idDevice = ((AlarmRule) rule).getIdDevice();

        if (null != idDevice)
        {
            DeviceStatusMgr.getInstance().addAlarm(idDevice);
        }
    }
}
