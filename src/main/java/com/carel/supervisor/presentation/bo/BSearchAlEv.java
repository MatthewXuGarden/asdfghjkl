package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLog;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.AlEvExport.AlEvExportFactory;
import com.carel.supervisor.presentation.AlEvExport.IAlEvExport;
import com.carel.supervisor.presentation.alarmsevents.AlarmEvent;
import com.carel.supervisor.presentation.alarmsevents.AlarmEventList;
import com.carel.supervisor.presentation.bean.FileDialogBean;
import com.carel.supervisor.presentation.bean.search.SearchAlarmEvent;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.events.HsParam;
import com.carel.supervisor.presentation.events.ParamsList;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;

public class BSearchAlEv extends BoMaster {
	private static final long serialVersionUID = 1L;
	private static final int REFRESH_TIME = -1;
	private static final String EXPORT_PDF_CMD = "exportpdf";

	public BSearchAlEv(String l) {
		super(l, REFRESH_TIME);
	}

	protected Map initializeRefresh() {
		Map map = new HashMap();
		RefreshBean[] rb = { new RefreshBean("AlarmEventFound",
				IRefresh.R_ALARMSEVENTS, 9, "") };
		RefreshBeanList rbl = new RefreshBeanList(REFRESH_TIME, rb.length);
		rbl.setRefreshObj(rb);

		map.put("tab1name", rbl);

		return map;
	}

	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
//		p.put("tab1name",
//				"resizeTableAlarmFound();check_limit();");
		p.put("tab1name",
				"resizeTableAlarmFound();");

		return p;
	}

	protected Properties initializeJsOnLoad() {
		String virtkey = VirtualKeyboard.getInstance().isOnScreenKey() ? ";keyboard.js;" : "";
		
		Properties p = new Properties();
		p.put("tab1name", "alev.js;resizeTable.js;evnview.js;../arch/FileDialog.js" + virtkey);
		return p;
	}
	
	// DOCTYPE STRICT is necessary to have FileDialog correctly functioning
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		return p;
    }

	public void executePostAction(UserSession us, String tabName,
			Properties prop) throws Exception {
	}

	public String executeDataAction(UserSession us, String tabName,
			Properties prop) throws Exception {

		String filepath = "";
		
		String cmd = prop.getProperty("cmd");
		boolean event_chk = false;
		boolean alarm_chk = false;

		String fileN = null;
		
		if(cmd!= null && cmd.equalsIgnoreCase(EXPORT_PDF_CMD))
		{
			//get export filename
			fileN = prop.getProperty("filename");
			// get export folder
			filepath = (prop.getProperty("path")).replace(fileN, "");
		}
		
		event_chk = prop.getProperty("event_chk").equalsIgnoreCase("true") ? true : false;
		alarm_chk = prop.getProperty("alarm_chk").equalsIgnoreCase("true") ? true : false;
		
		String format = prop.getProperty("format");
		
		String response = "<response>";
		String langcode = us.getLanguage();
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		

		// get correct exporter (depending on format - csv, pdf -)
		IAlEvExport exporter = AlEvExportFactory.getExporter(format);
		
		// if exists a filepath definition than set it (else the default is used)
		if(filepath != null && !filepath.equalsIgnoreCase("") && !filepath.equalsIgnoreCase("undefined"))
			exporter.setFilePath(filepath);
		else
			exporter.setFilePath(BaseConfig.getCarelPath()+File.separator+BaseConfig.getTemporaryFolder());
		
		AlarmEventList alevList = new SearchAlarmEvent().find(us, -1, alarm_chk,event_chk, event_chk);

		String noteTemplate = lan.getString("alevsearch", "expNoteAllMsg");
		String paramTemplate = lan.getString("alevsearch", "expParamModMsg");

		
		try {

			// if filename is not defined, set a default name
			if(fileN == null || fileN.equals("")  || fileN.equals("undefined"))
				fileN = "Alarmevents_" + DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
			
			// 'startexport' passing filename
			exporter.startExport(fileN, langcode);
			
			exporter.setTitle(lan.getString("report", "alevtitle"));
			exporter.setSiteName(us.getSiteName());

			String[] row = new String[6];

			// header
			row[0] = (lan.getString("report", "i18ntimelabel"));
			row[1] = (lan.getString("alarmCalledOf", "description"));
			row[2] = (lan.getString("alarmCalledOf", "device"));
			row[3] = (lan.getString("evnview", "user"));
			row[4] = (lan.getString("report", "i18nendalarm"));
			row[5] = (lan.getString("devdetail", "priority"));

			exporter.setColumnName(row);

			// end header
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			AlarmEvent tmp = null;
			String priority = "";
			for (int i = 0; i < alevList.getSize(); i++) {
				tmp = alevList.getAlarmEvent(i);
				if (alarm_chk && tmp.getT() != null && tmp.getT().equals("A")
						&& tmp.getPriority() > 0) // ALARM
				{
					row[0] = (sdf.format(tmp.getStarttime()));
					row[1] = (tmp.getAlarmVariable());
					row[2] = (tmp.getAlarmDevice());
					row[3] = ("");
					row[4] = ((tmp.getEndtime() != null ? sdf.format(tmp.getEndtime())
							.toString() : ""));
					switch (tmp.getPriority()) {
					case (1):
						priority = lan.getString("alrview", "alarmstate1");

						break;

					case (2):
						priority = lan.getString("alrview", "alarmstate2");

						break;

					case (3):
						priority = lan.getString("alrview", "alarmstate3");

						break;

					case (4):
						priority = lan.getString("alrview", "alarmstate4");

						break;
					}
					row[5] = (priority);
					exporter.writeRow(row);

					NoteLogList nll = new NoteLogList();

					Integer site = tmp.getSite();
					if (site == null)
						site = tmp.getIdSite();
					nll.retrieve(site, "hsalarm", tmp.getId());

					String msg;
					for (int j = 0; j < nll.size(); j++) {
						NoteLog n = nll.getNote(j);
						msg = MessageFormat.format(noteTemplate, n
								.getLastTime().toString(), n.getNote()
								.replaceAll(";", ","));

						row[0] = ("");
						row[1] = (msg);
						row[2] = ("");
						row[3] = (n.getUserNote());
						row[4] = ("");
						row[5] = ("");
						exporter.writeRow(row);
					}

				} else if (event_chk && tmp.getT() != null
						&& (tmp.getT().equals("E") || tmp.getT().equals("P"))
						&& tmp.getPriority() == -1)// EVENT
				{
					row[0] = (sdf.format(tmp.getLastupdate()));
					row[1] = (tmp.getMessageWithParams());
					row[2] = (tmp.getCategory());
					row[3] = (tmp.getUser());
					row[4] = ("");
					row[5] = ("");
					exporter.writeRow(row);

					if (tmp.getT().equals("E")
							&& tmp.getMessagecode().equalsIgnoreCase("W054")) {
						try {
							Integer id = new Integer(tmp.getParameters().split(
									";")[0]);

							ParamsList pl = new ParamsList();
							pl.loadFromDataBase(us, -1, id);
							HsParam[] ps = pl.getParamList();

							String msg;
							for (int j = 0; j < ps.length; j++) {
								HsParam p = ps[j];
								msg = MessageFormat.format(paramTemplate, p
										.getVariableDesc(), p.getOldValue()
										+ "", p.getNewValue() + "", p
										.getCodeDesc());
								row[0] = ("");
								row[1] = (msg);
								row[2] = (p.getDeviceDesc());
								row[3] = ("");
								row[4] = ("");
								row[5] = ("");
								exporter.writeRow(row);
							}

						} catch (Exception e) {
						}

					}
				}

			}
			
			exporter.endExport();

			response += "<limitSerach><![CDATA[" + us.getPropertyAndRemove("limit_search")
				+ "]]></limitSerach>"; 
			response += "<file><![CDATA[" + exporter.getFileName()
					+ "]]></file></response>";
			
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "<response><file><![CDATA[ERROR]]></file></response>";
		}

		return response;
	}
}
