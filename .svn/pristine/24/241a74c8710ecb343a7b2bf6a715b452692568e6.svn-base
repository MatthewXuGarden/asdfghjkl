package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;

public class AlarmsCallBack extends BackGroundCallBack 
{
	
	public AlarmsCallBack() 
	{
		super();
	}
	
	public void executeOnOk(SetContext setContext, SetWrp wrp)
	{
		Integer idAlarm = (Integer)wrp.getObjectForCallBack();
		if (null != idAlarm)
        {
            AlarmCtrl.notifyPVPRO(idAlarm);
        }
	}
	
}
