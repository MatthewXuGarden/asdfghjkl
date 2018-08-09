package com.carel.supervisor.dispatcher.action;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.https2xml.XMLResponse;
import com.carel.supervisor.report.PrinterMgr2;

public class BMSGeneralAction extends DispatcherAction {

	@Override
	public boolean compareAction(DispatcherAction da) {
		return this.getTypeAction().equalsIgnoreCase(da.getTypeAction());
	}

	public BMSGeneralAction(Integer key, Integer id, Integer pri, Integer sta,
			String rec, Timestamp itime, Timestamp utime, String tmpl,
			String type, Boolean isalarm, Integer idvar, Timestamp startTime,
			Timestamp endTime) {
		super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar,
				startTime, endTime);
	}

	protected String[] initializedRecepients(String recepient) {
		return new String[0];
	}

	public int[] putActionInQueue() throws Exception {
		List<Integer> actions = this.getKeyAction();
		int keyact[] = new int[actions.size()];
		String actkey[] = new String[actions.size()];
		for (int i = 0; i < keyact.length; i++) {
			keyact[i] = actions.get(i);
			actkey[i] = String.valueOf(keyact[i]);
		}

		HSActionQBeanList actionQList = new HSActionQBeanList();
		HSActionQBean actionQ = null;
		Integer key = null;

		boolean allOk = true;
		String path_file = "";
		if (this.getPathFiles().length!=0)
		{
			path_file = this.getPathFiles()[0];
		}

		try {
			key = SeqMgr.getInstance().next(null, "hsactionqueue",
					"idhsactionqueue");
			actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
					this.getIdSite(), this.getPriority(), this.getRetryNum(),
					this.getRetryAfter(), this.getTypeAction(), this
							.getTypeAction(), 1,path_file , "",
					"", actkey[0]);

			actionQList.addAction(actionQ);
		} catch (Exception e) {
			allOk = false;

			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}

		if (allOk) {
			allOk = actionQList.insertActions();
		}

		if (allOk) {
			HSActionBeanList actionbean = new HSActionBeanList();
			actionbean.updateToSendActionList(keyact);
			return new int[0];
		} else {
			return new int[0];
		}
	}

	public void buildTemplate(String pathDir) throws Exception {
		// BMS - Alarms
		// comunicazione xml verso server

		try 
		{
			StringBuffer response = new StringBuffer();
			BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
	    	XMLNode nodeXevents;

			if (this.getTypeAction().equalsIgnoreCase("#BA")) {
				/*
				 * If action due to alarm then ... else send alive signal
				 */
				if (this.isAlarm()) {
					
					response = new StringBuffer();

					response.append(XMLResponse.getStartingTag());
					response.append("\n<response type=\"AL2\">\n");

					response.append("<rs t='AL2'>\n");
					String pvidentifier = bmsc.getPvIdentifier();
					try {
						ArrayList<Integer> ids_variables = this.getIdVariable();
						ArrayList<Timestamp> timestamps = this.getLastupdate();

						String d_code = "";
						String v_code = "";
						String priority = "";
						String ackuser = "";
						Timestamp acktime = null;
						String delactionuser = "";
						Timestamp delactiontime = null;
						String resetuser = "";
						Timestamp resettime = null;
						Timestamp starttime = null;
						Timestamp endtime = null;

						Map<Integer, ArrayList<Integer>> dev_var = getVarOfDevices(
								ids_variables, timestamps);

						Integer iddev = null;
						Integer idalr = null;
						Record r_dev = null;
						Record r_var = null;

						Object[] iddevices = dev_var.keySet().toArray();
						RecordSet devices = retrieveDeviceInfo(iddevices);
						if (devices != null) {
							for (int d = 0; d < devices.size(); d++) {
								r_dev = devices.get(d);
								iddev = (Integer) r_dev.get("iddevice");
								// retrieve device info
								d_code = r_dev.get("code").toString();

								// write device section in xml
								response
										.append("<dv did='"
												+ pvidentifier
												+ ":"
												+ d_code
												+ "' "
												+ "st='"
												+ UtilDevice.getLedColor(iddev)
												+ "' "
												+ "en='"
												+ (r_dev.get("isenabled")
														.toString()
														.equalsIgnoreCase(
																"FALSE") ? false
														: true) + "'>\n");

								ArrayList<Integer> alr_list = dev_var
										.get(iddev);
								for (int i = 0; i < alr_list.size(); i++) // variable
																			// iteration
																			// (for
																			// device
																			// "iddev")
								{
									// retrieve variable info
									idalr = alr_list.get(i);
									r_var = retrieveAlarmInfo(idalr);
									ackuser = UtilBean.trim(r_var
											.get("ackuser"));
									acktime = (Timestamp) r_var.get("acktime");
									delactionuser = UtilBean.trim(r_var
											.get("delactionuser"));
									delactiontime = (Timestamp) r_var
											.get("delactiontime");
									resetuser = UtilBean.trim(r_var
											.get("resetuser"));
									resettime = (Timestamp) r_var
											.get("resettime");
									starttime = (Timestamp) r_var
											.get("starttime");
									endtime = (Timestamp) r_var.get("endtime");
									v_code = r_var.get("v_code").toString();
									priority = r_var.get("priority").toString();

									// write variable section in xml

									response.append("<alr uuid='"
											+ pvidentifier + ":" + d_code + ":"
											+ v_code + "' start='" + starttime
											+ "' end='"
											+ (endtime == null ? "" : endtime)
											+ "' prio='" + priority + "' >\n");
									response.append("<ack user='"
											+ (ackuser != null ? ackuser : "")
											+ "' time='"
											+ (acktime != null ? acktime : "")
											+ "'/>\n");
									response
											.append("<del user='"
													+ (delactionuser != null ? delactionuser
															: "")
													+ "' time='"
													+ (delactiontime != null ? delactiontime
															: "") + "'/>\n");
									response.append("<rst user='"
											+ (resetuser != null ? resetuser
													: "")
											+ "' time='"
											+ (resettime != null ? resettime
													: "") + "'/>\n");
									response.append("</alr>\n");

								}

								response.append("</dv>");
							}
						}
					} catch (Exception e) {
						LoggerMgr.getLogger(this.getClass()).error(e);
						response = new StringBuffer();
					}

					response.append("</rs>\n");
					response.append("</response>");
					response.append(XMLResponse.getEndingTag());

					File path = new File(BaseConfig.getCarelPath()
							+ File.separator
							+ PrinterMgr2.getInstance().getSavePath());
					path.mkdirs();
					File file = new File(path.getAbsolutePath()
							+ File.separator + "AL2_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".xml");
					BufferedWriter fw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(file),
									Charset.forName("UTF-8")));

					fw.write(response.toString());
					fw.flush();
					fw.close();

					addPathFile(file.getName());

				}
			}
			
			if (this.getTypeAction().equalsIgnoreCase("#BS"))
			{	
				// #BS action is managed directly in DispatcherDQ
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private RecordSet retrieveDeviceInfo(Object[] iddevices)
			throws DataBaseException {
		String sql = "select iddevice,code,isenabled from cfdevice where iddevice in (";
		for (int i = 0; i < iddevices.length; i++) {
			sql += ((Integer) iddevices[i] + ",");
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ") and iscancelled='FALSE' order by code";

		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);

		if (rs != null && rs.size() > 0) {
			return rs;
		} else
			return null;
	}

	private Record retrieveAlarmInfo(Integer idAlr) throws DataBaseException {
		String sql = "";
		sql += ("select cfvariable.code as v_code, cfvariable.priority, "
				+ "ackuser,acktime,delactionuser,delactiontime,resetuser,resettime,starttime,endtime FROM "
				+ "cfvariable,hsalarm where "
				+ "hsalarm.idalarm = ? and cfvariable.iscancelled='FALSE' and "
				+ "hsalarm.idvariable = cfvariable.idvariable ");

		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { idAlr });
		if (rs != null && rs.size() > 0) {
			return rs.get(0);
		} else
			return null;
	}

	private Map<Integer, ArrayList<Integer>> getVarOfDevices(
			ArrayList<Integer> ids_var, ArrayList<Timestamp> timestamps)
			throws DataBaseException {
		String sql = "select iddevice,idalarm from hsalarm where idvariable=? and lastupdate=? ";

		Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> tmp = null;

		Integer iddev = null;
		Integer idalr = null;

		for (int i = 0; i < ids_var.size(); i++) {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
					new Object[] { ids_var.get(i), timestamps.get(i) });
			if (rs != null && rs.size() > 0) {
				iddev = (Integer) rs.get(0).get("iddevice");
				idalr = (Integer) rs.get(0).get("idalarm");
				if (map.get(iddev) != null) {
					tmp = map.get(iddev);
					tmp.add(idalr);
					map.put(iddev, tmp);
				} else {
					tmp = new ArrayList<Integer>();
					tmp.add(idalr);
					map.put(iddev, tmp);
				}
			}
		}
		return map;
	}
}
