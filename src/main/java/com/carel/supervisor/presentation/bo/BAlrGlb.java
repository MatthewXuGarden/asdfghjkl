package com.carel.supervisor.presentation.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.alarms.AlarmMngTable;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.note.Note;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;


public class BAlrGlb extends BoMaster
{
    private static final int REFRESH_TIME = 30000;

    public BAlrGlb(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Map initializeRefresh()
    {
        Map map = new HashMap();
        
        RefreshBean[] rb1 = {new RefreshBean("TA", IRefresh.R_ALARMS, 1)};
        RefreshBean[] rb2 = {new RefreshBean("TalarmCalledOf", IRefresh.R_ALARMSCALL, 2)};
        
        RefreshBeanList rbl1 = new RefreshBeanList(REFRESH_TIME, 1);
        RefreshBeanList rbl2 = new RefreshBeanList(REFRESH_TIME, 1);
        
        rbl1.setRefreshObj(rb1);
        rbl2.setRefreshObj(rb2);

        map.put("tab1name", rbl1);
        map.put("tab2name", rbl2);

        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","enableAction(1);enableAction(2);enableAction(3);enableAction(4);resizeAlarmTableAct();ackOnload();");
        p.put("tab2name","enableAction(1);resizeAlarmTableRcl();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","alrgbl.js;keyboard.js;");
        p.put("tab2name","alrgbl.js;keyboard.js;");
        return p;
    }

	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		String toReturn = "<message>ok</message>";
		String cmd = prop.getProperty("cmd");
		
		try
		{
			if ("ack_all".equalsIgnoreCase(cmd))
			{
				//correzione delle query per gli allarmi da ackare per modalit� sticky o legacy
				String clauseForStickyLegacy="";
				if (!AlarmMngTable.stickyEnabled()){
					clauseForStickyLegacy="endtime is null and ";
				}

				
				List<Integer> allarmiSenzaNote  = new ArrayList<Integer>();
				List<Integer> sitiSenzaNote  = new ArrayList<Integer>();
				List<Integer> prioritaSenzaNote  = new ArrayList<Integer>();

				//se almeno una priorit� ha la nota obbligatoria 
				if (AlarmMngTable.mandatoryNote(1) || AlarmMngTable.mandatoryNote(2) || AlarmMngTable.mandatoryNote(3) || AlarmMngTable.mandatoryNote(4))
				{
					//... recupero tutti gli allarmi da ackare ... 
					//query per recuperare tutti gli allarmi da ackare! 
					String sql = "select idsite, idalarm, priority " +
								 "from hsalarm " +
								 "where "+
								clauseForStickyLegacy +
								"ackuser is null and acktime is null";

					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
					
					NoteLogList listaNote;Integer idsite; Integer idalarm; String priority; Integer pr;
					if (rs!=null && rs.size()>0)
					{
						for (int i=0;i<rs.size();i++)
						{
							idsite = (Integer) rs.get(i).get(0);
							idalarm = (Integer) rs.get(i).get(1);
							priority = (String) rs.get(i).get(2);
							pr = new Integer(priority.trim());
							
							if (AlarmMngTable.mandatoryNote(pr)){
								//... se l'allarme deve avere la nota...
								listaNote = new NoteLogList();
								listaNote.retrieve(idsite, "hsalarm", idalarm);
								if (listaNote.size()==0){
									//...ma non ne ha nessuna, lo metto in lista per aggiungerne una!
									allarmiSenzaNote.add(idalarm);
									sitiSenzaNote.add(idsite);
									prioritaSenzaNote.add(pr);
								}
							}
						}
					}
				}

				String clauseToExcludeAlarmsWithoutNote="";
				boolean priority1woNote=false;
				boolean priority2woNote=false;
				boolean priority3woNote=false;
				boolean priority4woNote=false;
				
				try{
				
				if (allarmiSenzaNote.size()>0){
						for (int i = 0; i < allarmiSenzaNote.size(); i++) {
							// ... e c'� la nota di default...
							if (AlarmMngTable.multiAckNoteEnabled()) 
							{
								Note.executeNoteAction(null, sitiSenzaNote.get(i), us.getUserName(), "save", AlarmMngTable.multiAckNote(), null, "hsalarm", allarmiSenzaNote.get(i));											
							}
							else
							{
								//se ci dovrebbe essere la nota ma manca, li escludo dall'ack e segnalo all'utente
								if (clauseToExcludeAlarmsWithoutNote.trim().length()==0)
								{
									clauseToExcludeAlarmsWithoutNote = " and idalarm not in ( "+ allarmiSenzaNote.get(i);
								}
								else
								{
									clauseToExcludeAlarmsWithoutNote += " , "+ allarmiSenzaNote.get(i);
								}
								
								switch (prioritaSenzaNote.get(i)) {
								case 1:
									priority1woNote=true;
									break;
								case 2:
									priority2woNote=true;
									break;
								case 3:
									priority3woNote=true;
									break;

								default:
									priority4woNote=true;
									break;
								}
							}
						}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (priority1woNote) us.setProperty("priority1woNote", "TRUE");
				if (priority2woNote) us.setProperty("priority2woNote", "TRUE");
				if (priority3woNote) us.setProperty("priority3woNote", "TRUE");
				if (priority4woNote) us.setProperty("priority4woNote", "TRUE");
				
				if (clauseToExcludeAlarmsWithoutNote.trim().length()>0){clauseToExcludeAlarmsWithoutNote +=" )";}
				
				String condition = " where " +
						clauseForStickyLegacy +
						"ackuser is null and acktime is null and priority in " +
						"(select substring(KEY from 10 for 1) from systemconf where key like 'priority_%' and value='TRUE') "
						+clauseToExcludeAlarmsWithoutNote;
				String selectSql = "select hsalarm.idvariable from hsalarm "+ condition;
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, selectSql);
				if(rs != null && rs.size()>0)
				{
					for (int i=0;i<rs.size();i++)
					{
						RuleMgr.getInstance().manualAcknowledge((Integer)(rs.get(i).get(0)));
					}
				}
				DatabaseMgr.getInstance().executeStatement(null, "update hsalarm set ackuser='"+us.getUserName()+"', acktime=now(),lastupdate=now() "+condition, null);
				
				//acknowledge of all alarms with priority > 4 (if "low priority" is selected from "alarms safety" page)
				String sql  = "(select substring(KEY from 10 for 1) from systemconf where key like 'priority_%' and value='TRUE')";
				rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				
				if (rs!=null && rs.size()>0)
				{
					for (int i=0;i<rs.size();i++)
					{
						if(Integer.parseInt((String)rs.get(i).get(0)) == 4)
						{
							condition = " where " +
									clauseForStickyLegacy +
									"ackuser is null and acktime is null and ( CAST(priority as Integer) > 4)"+clauseToExcludeAlarmsWithoutNote;
							selectSql = "select hsalarm.idvariable from hsalarm "+ condition;
							RecordSet rsNew = DatabaseMgr.getInstance().executeQuery(null, selectSql);
							if(rsNew != null && rsNew.size()>0)
							{
								for (int j=0;j<rsNew.size();j++)
								{
									RuleMgr.getInstance().manualAcknowledge((Integer)(rsNew.get(j).get(0)));
								}
							}
							DatabaseMgr.getInstance().executeStatement(null, "update hsalarm set ackuser='"+us.getUserName()+"', acktime=now() "+condition, null);
						}
					}
				}
				EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Action", "W078", null);
			}
			else if ("delete_all".equalsIgnoreCase(cmd))
			{
				//retrieve idalarms to delete
				String sql  = "select hsalarm.idvariable from hsalarm,cfvariable where endtime is null and " +
						"hsalarm.idvariable=cfvariable.idvariable and " +
						"ackuser is not null and delactionuser is null and " +
						"delactiontime is null and cfvariable.iscancelled='FALSE'";
       
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				
				if (rs!=null && rs.size()>0)
				{
					for (int i=0;i<rs.size();i++)
					{
						RuleMgr.getInstance().manualCancel((Integer)(rs.get(i).get(0)));
					}
				}
				
				DatabaseMgr.getInstance().executeStatement(null, "update hsalarm set delactionuser='"+us.getUserName()+"', delactiontime=now(),lastupdate=now() where acktime is not null and ackuser is not null and delactionuser is null", null);
				
				EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Action", "W079", null);
			}
			else if ("reset_all".equalsIgnoreCase(cmd))
			{
				//retrieve id alarms to reset, and delaction
				String sql  = "select hsalarm.idvariable,cfvariable.iddevice,delactionuser from hsalarm,cfvariable where endtime is null and " +
				"resettime is null and " +
				"hsalarm.idvariable=cfvariable.idvariable and " +
				"ackuser is not null and cfvariable.iscancelled='FALSE'";

				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				Integer id = null;
				Integer iddevice = null;
				Boolean deleted = false;
				
				if (rs!=null && rs.size()>0)
				{
					for (int i=0;i<rs.size();i++)
					{
						id = (Integer)(rs.get(i).get(0));
						iddevice = (Integer)(rs.get(i).get(1));
						deleted = rs.get(i).get(2)==null?false:true;
						
						if (!deleted)  //if not deleted -> delete action
						{
							DatabaseMgr.getInstance().executeStatement(null, "update hsalarm set delactionuser='"+us.getUserName()+"', delactiontime=now() where idvariable=?", new Object[]{id});
							RuleMgr.getInstance().manualCancel(id);
						}
						
						RuleMgr.getInstance().manualReset(id);
			            DeviceStatusMgr.getInstance().removeAlarm(iddevice);
					}
				}
				
				DatabaseMgr.getInstance().executeStatement(null, "update hsalarm set resetuser='"+us.getUserName()+"', resettime=now(),lastupdate=now() where acktime is not null and ackuser is not null and resetuser is null", null);
				EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Action", "W080", null);
			}else if("print_allAlarms".equalsIgnoreCase(cmd )) {
				StringBuffer response = new StringBuffer("<response>"); 
	    		String language = us.getLanguage();
	    		String sessionName="grpview";
	    		LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	    		String title = multiLanguage.getString(sessionName,"alarms");
	    		AlarmList alarmList = new AlarmList(title);
	    		alarmList.setLink(false);
	    		alarmList.loadAllFromDataBase(us);
	    		response.append(alarmList.getAllAlarms4Print(language,multiLanguage,us));
	    		toReturn = response.append("</response>").toString();
	    	}
		}
		catch (Exception e)
		{
			return "<message>ko</message>";
		}
		
		return toReturn;
	}

}
