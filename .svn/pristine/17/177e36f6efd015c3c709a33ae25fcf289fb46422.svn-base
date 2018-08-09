package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.alarms.AlarmMngTable;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.note.Note;
import com.carel.supervisor.presentation.session.UserSession;



public class BAlrView extends BoMaster
{
	private static final long serialVersionUID = 1L;
	private static final int REFRESH_TIME = -1;

    public BAlrView(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "setActionButton();");
        p.put("tab2name", "setdefault();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        //determino se è abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = ";keyboard.js;";
        }
    	
    	Properties p = new Properties();
        p.put("tab1name", "alrview.js");
        p.put("tab2name", "note.js"+virtkey);

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        String cmd = us.getProperty("cmd");
        String user = us.getUserName();
        String id = us.getProperty("id");
        int idsite = us.getIdSite();

        String[] datas = null;

        if (id != null)
        {
            datas = StringUtility.split(id, "_");
        }

        if (datas != null)
        {
            id = datas[0];
        }

        AlarmList alarmList = new AlarmList();
        AlarmLog alarmLog = null;
        AlarmLogList alarmLogList = alarmList.loadFromDataBaseByIdAlarm(us, Integer.parseInt(id));
        alarmLog = alarmLogList.getByPosition(0);
        alarmLog.setDeviceDesc(alarmList.getAlarm(0).getAlarmDevice());

        NoteLogList listaNote = new NoteLogList();

        if (AlarmMngTable.multiAckNoteEnabled()){
        	Note.executeNoteAction(null, idsite, us.getUserName(), "save", AlarmMngTable.multiAckNote(), null, "hsalarm", alarmLog.getId());
		}
		
		listaNote.retrieve(alarmLog.getSite(), "hsalarm", alarmLog.getId());
		

		
		if (
				AlarmMngTable.mandatoryNote(new Integer(alarmLog.getPriority()))
				&& listaNote.size()==0
				
			) {
			//nota obbligatoria mancante per l'allarme
			us.setProperty("mandatoryNoteRequired", "TRUE");
		}
		else if (cmd.equals("ack"))
        {
            alarmLog.updateAck(user, Integer.valueOf(id));
            RuleMgr.getInstance().manualAcknowledge(new Integer(alarmLog.getIdvariable()));

            //Log ack 
            EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Action", "W016",
                new Object[] { new Integer(alarmLog.getId()), alarmLog.getDescription(),alarmLog.getDeviceDesc()});
        }
        else if (cmd.equals("cancel"))
        {
            alarmLog.updateCancel(user, Integer.valueOf(id));
            RuleMgr.getInstance().manualCancel(new Integer(alarmLog.getIdvariable()));

            // Log cancel 
            EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Action", "W017",
                new Object[] { new Integer(alarmLog.getId()), alarmLog.getDescription(), alarmLog.getDeviceDesc() });
        }
        else if (cmd.equals("reset"))
        {
        	//LIKE CMD==ACK
        	if(alarmLog.getAcktime()==null)
        	{
                alarmLog.updateAck(user, Integer.valueOf(id));

                //Log ack 
                EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Action", "W016",
                    new Object[] { new Integer(alarmLog.getId()), alarmLog.getDescription(),alarmLog.getDeviceDesc()});
        	}
        	//LIKE CMD==CANCEL
        	if(alarmLog.getDelactiontime()==null)
        	{
                alarmLog.updateCancel(user, Integer.valueOf(id));
                RuleMgr.getInstance().manualCancel(new Integer(alarmLog.getIdvariable()));

                // Log cancel 
                EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Action", "W017",
                    new Object[] { new Integer(alarmLog.getId()), alarmLog.getDescription(), alarmLog.getDeviceDesc() });
        	}
        	
        	//CMD RESET PART
            alarmLog.updateReset(user, Integer.valueOf(id));

            Integer idVar = new Integer(alarmLog.getIdvariable());

            RuleMgr.getInstance().manualReset(idVar);
            DeviceStatusMgr.getInstance().removeAlarm(new Integer(alarmLog.getIddevice()));

            //Log reset
            EventMgr.getInstance().info(new Integer(idsite), us.getUserName(), "Action", "W018",
                new Object[] { new Integer(alarmLog.getId()), alarmLog.getDescription(), alarmLog.getDeviceDesc() });
        }
    }
}
