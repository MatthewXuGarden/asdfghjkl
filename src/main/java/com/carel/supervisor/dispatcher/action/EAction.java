package com.carel.supervisor.dispatcher.action;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.dataconfig.SystemConf;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailRetryMgr;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.main.DispDocLinker;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.report.AlarmReport;


public class EAction extends PAction {
	public static final String TYPE_LAN = "L";
	public static final String TYPE_DUP = "D";
	public static final String SPLIT = ";";

	public EAction(Integer key, Integer id, Integer pri, Integer sta,
			String rec, Timestamp itime, Timestamp utime, String tmpl,
			String type, Boolean isalarm, Integer idvar, Timestamp start,
			Timestamp end) {
		super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar,
				start, end);
	}

	protected String[] initializedRecepients(String recepient) {
		String[] recRet = new String[0];

		if (recepient != null) {
			recRet = recepient.split(SPLIT);
		}

		Arrays.sort(recRet);

		return recRet;
	}

	public int[] putActionInQueue() throws Exception {
		List<Integer> keyact = this.getKeyAction();
		int[] ret = new int[0];
		String[] listFile = this.getPathFiles();
		boolean allOk = true;
		boolean byLan = false;
		String[] receivers = this.getRecepients();

		// Get ConfMail from memory
		EMemory eMem = (EMemory) DispMemMgr.getInstance().readConfiguration(
				this.getTypeAction());

		// Build File Path
		String pathFile = listFile[0];
		pathFile = DispatcherMgr.getInstance().getRepositoryPath() + pathFile;

		if (eMem != null) {
			if (eMem.getType().equalsIgnoreCase(TYPE_LAN)) {
				byLan = true;
				allOk = sendByLAN(eMem.getSmtp(), eMem.getPort(), eMem.getSender(), receivers,
						pathFile, eMem.getRetryNum(), eMem.getRetryAfter(),
						eMem.getUser(), eMem.getPass(), eMem.getEncryption());
			} else if (eMem.getType().equalsIgnoreCase(TYPE_DUP)) {
				HSActionQBeanList actionQList = new HSActionQBeanList();
				HSActionQBean actionQ = null;
				Integer key = null;

				if (receivers != null) {
					String grpactid = "";

					for (int j = 0; j < keyact.size(); j++) {
						grpactid += (((Integer) keyact.get(j)).intValue() + ",");
					}

					grpactid = grpactid.substring(0, grpactid.length() - 1);

					for (int i = 0; i < receivers.length; i++) {
						try {
							key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
							actionQ = new HSActionQBean(key.intValue(), 
									this.getNameSite(), 
									this.getIdSite(), 
									this.getPriority(), 
									this.getRetryNum(), 
									this.getRetryAfter(), 
									this.getFisicDevice(),
									this.getTypeAction(), 1, pathFile,
									this.siteName, receivers[i], grpactid);

							actionQList.addAction(actionQ);
						} catch (Exception e) {
							allOk = false;
							Logger logger = LoggerMgr.getLogger(this.getClass());
							logger.error(e);
						}
					}

					if (allOk) {
						allOk = actionQList.insertActions();
					}
				}
			}

			if (allOk) {
				ret = new int[keyact.size()];
				for (int i = 0; i < ret.length; i++) {
					ret[i] = ((Integer) keyact.get(i)).intValue();
				}
			}

			if (byLan) {
				ret = new int[keyact.size()];

				for (int i = 0; i < ret.length; i++) {
					ret[i] = ((Integer) keyact.get(i)).intValue();
				}

				HSActionBeanList actionbean = new HSActionBeanList();

				if (allOk) {
					actionbean.updateToSendActionList(ret);
				} else {
					actionbean.updateToDiscardActionList(ret);
				}

				// Clear array
				ret = new int[0];
			}
		}

		return ret;
	}

	private boolean sendByLAN(String smtp, String port, String sender, String[] idReceivers, String pathAtt, int retryn, int retrya, String user, String pass, String encryption) {
		boolean ret = true;
		DispatcherBook db = null;
		String to = "";
		String sDate = "";

		if (idReceivers != null) {
			try {
				// Load receivers's mail
				for (int i = 0; i < idReceivers.length; i++) {
					db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(idReceivers[i]));
					to += (db.getAddress() + ",");
				}

				to = to.substring(0, to.length() - 1);

				// Add Timestamp
				sDate = DateUtils.date2String(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss.SSS");
				// sDate = DateUtils.date2String(new
				// Timestamp(System.currentTimeMillis()),
				// "yyyy/MM/dd HH:mm:ss");

				String bodymail = "";

				// Biolo: leggere file HTML, e inserirlo nel body della mail
				try {
					if (getReport() instanceof AlarmReport) {
						Object[][] tab = ((AlarmReport) getReport()).getTableAlarm();
						String lang = LangUsedBeanList.getDefaultLanguage(1);
						LangService l = LangMgr.getInstance().getLangService(lang);
						SiteInfoList s = new SiteInfoList();
						String sitename = s.getById(1).getName();
						// process

						StringBuffer html_buff = new StringBuffer("");

						html_buff.append("<HTML>\n<HEAD>\n<TITLE>"
								+ l.getString("rep_body_mail", "rep_by_mail")
								+ "</TITLE>\n</HEAD>\n<BR>\n<BODY>\n");
						html_buff.append("<TABLE width='100%'>\n\n");
						html_buff.append("<TR><TD align='center'><b>"
								+ l.getString("rep_body_mail", "alarm_report")
								+ "</b></TD><TD align='center'><b>" + sitename
								+ "</b></TD><TD align='center'><b>" + sDate
								+ "</b></TD></TR></TABLE>\n");
						// Object[] tab = (Object[]) data.get("alarmtable");
						html_buff.append("<BR><TABLE width='100%'>\n");
						for (int x = 0; x < tab.length; x++) {
							Object[] temp = (Object[]) tab[x];

							html_buff.append("<TR><TD align='center'>"
									+ temp[0].toString()
									+ " - </TD><TD align='center'>"
									+ temp[1].toString()
									+ "</TD><TD align='center'>"
									+ temp[2].toString()
									+ "</TD><TD align='center'>"
									+ temp[3].toString() + "</TD></TR>\n");
						}
						// END OF MESSAGE
						html_buff.append("<TR><TD align='center'>----------</TD><TD align='center'>&nbsp;</TD><TD align='center' colspan='2'>&nbsp;</TD></TR>");
						//
						html_buff.append("\n</TABLE>\n");

						SystemConf logEnable = SystemConfMgr.getInstance().get("email_log");
						if (logEnable != null) {
							if (logEnable.getValue().equals("TRUE")) {
								Object[][] tabLog = ((AlarmReport) getReport()).getTableLog();
								if (tabLog != null && tabLog.length > 0) {
									html_buff.append("<TABLE width='100%'>\n\n");
									html_buff.append("<TR><TD align='center'></TD><TD align='center'><b>"
													+ l.getString("rep_body_mail","logValue")
													+ "</b></TD><TD align='center'>"
													+ "</TD></TR></TABLE>\n");

									html_buff.append("<BR><TABLE width='100%'>\n");
									for (int x = 0; x < tabLog.length; x++) {
										Object[] temp = tabLog[x];
										html_buff.append("<TR><TD align='center'>"
														+ temp[0].toString()
														+ "</TD><TD align='center'>"
														+ temp[1].toString()
														+ "</TD><TD align='center'>"
														+ temp[2].toString()
														+ "</TD></TR>\n");
									}
									html_buff.append("<TR><TD align='center'>----------</TD><TD align='center'>&nbsp;</TD><TD align='center' colspan='2'>&nbsp;</TD></TR>");
									html_buff.append("\n</TABLE>\n");
								}
							}
						}

						html_buff.append("</BODY>\n</HTML>");
						bodymail = html_buff.toString();

					}
					else {
						if( reportName != null ) {
							String lang = LangUsedBeanList.getDefaultLanguage(1);
							LangService l = LangMgr.getInstance().getLangService(lang);
							StringBuffer html_buff = new StringBuffer("");
							html_buff.append("<HTML><HEAD><TITLE>"
									+ l.getString("rep_body_mail", "rep_by_mail")
									+ "</TITLE></HEAD><BODY>");
							html_buff.append("<TABLE><TR><TD><B>");
							html_buff.append(l.getString("rep_body_mail", "site_name"));
							html_buff.append("</B></TD><TD>");
							html_buff.append(siteName);
							html_buff.append("</TD></TR><TR><TD><B>");
							html_buff.append(l.getString("rep_body_mail", "report_name"));
							html_buff.append("</B></TD><TD>");
							html_buff.append(reportName);
							html_buff.append("</TD></TR><TR><TD><B>");
							html_buff.append(l.getString("rep_body_mail", "date"));
							html_buff.append("</B></TD><TD>");
							html_buff.append(sDate);
							html_buff.append("</TD></TR></TABLE>");
							html_buff.append("</BODY></HTML>");
							bodymail = html_buff.toString();
						}
						else if( pathAtt.endsWith("PDF_Alive.pdf") ) {
							String aliveContent = ProductInfoMgr.getInstance().getProductInfo().get("alive_mail_content");
							if(aliveContent == null || (!aliveContent.equals("no")))
							{
								String lang = LangUsedBeanList.getDefaultLanguage(1);
								LangService l = LangMgr.getInstance().getLangService(lang);
								StringBuffer html_buff = new StringBuffer("");
								html_buff.append("<HTML><HEAD><TITLE>"
										+ l.getString("rep_body_mail", "rep_by_mail")
										+ "</TITLE></HEAD><BODY>");
								html_buff.append("<TABLE><TR><TD><B>");
								html_buff.append("Scheduled message from: ");
								html_buff.append("</B></TD><TD>");
								html_buff.append(siteName);
								html_buff.append("</TD></TR><TR><TD><B>");
								html_buff.append(l.getString("rep_body_mail", "date"));
								html_buff.append("</B></TD><TD>");
								html_buff.append(sDate);
								html_buff.append("</TD></TR></TABLE>");
								html_buff.append("</BODY></HTML>");
								bodymail = html_buff.toString();
							}
						}
					}
				} catch (Exception e) {
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e.getMessage());
				}

				String subject;
				if( reportName != null )
					subject = this.siteName + " - " + this.reportName + " - " + sDate;
				else
					subject = this.siteName + " - " + sDate;
				DispMailMessage message = new DispMailMessage(sender, to, subject, bodymail);
				message.setAttach(pathAtt);

				DispMailSender mailer = new DispMailSender(smtp, user, pass, message);
				mailer.setPort(port);
				mailer.setRetryNum(retryn);
				mailer.setRetryAfter(retrya);
				mailer.setEncryption(encryption);
				ret = mailer.sendMessage();

				// Aggiunta per retry
				if (!ret)
					DispMailRetryMgr.getInstance().addMail(mailer);
			} catch (Exception e) {
				ret = false;

				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		} else {
			ret = false;
		}

		if (ret) {
			Object[] p = null;
			String recName = "";

			try {
				for (int i = 0; i < idReceivers.length; i++) {
					db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(idReceivers[i]));
					recName = "(" + db.getReceiver() + " " + db.getAddress() + ")";

					Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
					DispDocLinker.insertDoc(1, evId, "E", pathAtt);

					p = new Object[] {
							"<a onclick=evnviewDoc("+ evId.intValue() + ",'" + this.getTypeAction() + "')><u>" + 
							DispatcherMgr.getInstance().decodeActionType(this.getTypeAction())+ "</u></a>", recName };

					EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action", EventDictionary.TYPE_INFO, "D041", p);
				}
			} catch (Exception e1) {
			}
		}

		return ret;
	}
}
