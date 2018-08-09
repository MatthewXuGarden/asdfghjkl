package supervisor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.PvproInfo;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.setfield.NotificationParam;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.director.guardian.GuardianCheck;
import com.carel.supervisor.plugin.co2.CO2SavingManager;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.presentation.bean.DeviceDetectBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.FileDialogBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.rule.RelayBean;
import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.bo.BSystem;
import com.carel.supervisor.presentation.bo.helper.GuardianHelper;
import com.carel.supervisor.presentation.bo.master.IMaster;
import com.carel.supervisor.presentation.devices.AbstractDtlDevice;
import com.carel.supervisor.presentation.devices.ButtonDtlDevice;
import com.carel.supervisor.presentation.devices.DeviceDetail;
import com.carel.supervisor.presentation.devices.HomeDtlDevice;
import com.carel.supervisor.presentation.devices.ReadOnlyDtlDevice;
import com.carel.supervisor.presentation.devices.StateDtlDevice;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.devices.WriteDtlDevice;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;
import com.carel.supervisor.remote.engine.connection.ActiveConnections;


public class SRVLRefresh extends HttpServlet 
{
	private static final long serialVersionUID = -8589092858218063502L;

	private static final String CONTENT = "text/xml; charset=UTF-8";
	
	private static final String PG_DEVICES  = "DTLRFH";
	private static final String PG_SIMPLEDEVICES  = "SMPLDTL";
//	private static final String PG_MIXEDMODE  = "MIXEDRFH";
	private static final String PG_DETAILS  = "DEVRFH";
	private static final String PG_GUARDIAN = "GUISERV";
	private static final String PG_LOGIN    = "LGNRFH";
	private static final String PG_REMOTE 	= "RINTER";
	private static final String PG_BROAD 	= "broad";
	private static final String PG_RELAY 	= "RELAYRFH";
	private static final String PG_INTERNAL = "INTERNALRFH";
	
	// Azione per refresh hh:mm in pagina
	private static final String PG_RTIME 	= "RTIME";
	
	// Azioni per snooze sound e ack della finestra del guardiano sul server
	private static final String GP_SOUND 	= "GPSND";
	private static final String GP_WINDOW 	= "GPACK";
	
	private static final String PG_PARAM_STATUS = "PARAM_STATUS";
	
	private static final String PG_ALARM = "ALARM";
	//when test IO, need to update verified channels
	private static final String PG_IOTEST = "IOTEST";
	
	private static final String RESTART_ENGINE = "RESTART_ENGINE";
	private static boolean bRestartEngine = false; // true during engine restart
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
		String cmd = request.getParameter("cmd");
		String xmlResp = "";
		
		UserSession userSess = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		
		if(cmd != null)
		{
			if(cmd.equalsIgnoreCase(PG_DEVICES))
			{
				if (ServletHelper.validateSessionWithoutSet(userSess))
					xmlResp = commDTLRFH(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_SIMPLEDEVICES))
			{
				if (ServletHelper.validateSessionWithoutSet(userSess))
					xmlResp = commSMPLDTL(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_DETAILS))
			{
				if (ServletHelper.validateSessionWithoutSet(userSess))	
					xmlResp = commDEVRFH(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_GUARDIAN))
			{
				xmlResp = commGUISERV(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_LOGIN))
			{
				xmlResp = commLGNRFH(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_REMOTE))
			{
				xmlResp = commRINTER(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_BROAD))
			{
				xmlResp = stopBroadcastAnswer(request,response);
			}
			else if(cmd.equalsIgnoreCase("sitestatus"))
			{
				xmlResp = querySiteStatus( Integer.parseInt( userSess.getProperty("iddev") ));
			}
			else if(cmd.equalsIgnoreCase(PG_RTIME))
			{
				xmlResp = "<response>";
				xmlResp += "<t>"
					+ (ServletHelper.validateSessionWithoutSet(userSess) ? refreshTime() : "")
					+ "</t>";
				xmlResp += ("<g>" + getGuardianWinStatus() + "</g>");
				xmlResp += "</response>";
			}
			else if(cmd.equalsIgnoreCase(GP_SOUND))
			{
				GuardianCheck.snoozeSound();
				String login = request.getParameter("login");
				if(null!=login && login.equalsIgnoreCase("true")){
					response.sendRedirect ("/PlantVisorPRO");
				}else{
					response.setContentType("text/html; charset=UTF-8");
					xmlResp = "<html><head><body onload='window.close();'></body</head></html>";
				}

			}
			else if(cmd.equalsIgnoreCase(GP_WINDOW))
			{
				// Chiudo al ritorno la finestra
				response.setContentType("text/html; charset=UTF-8");
				xmlResp = "<html><head><body onload='window.close();'></body</head></html>";
				GuardianHelper.writeMsgWinClose(userSess.getUserName());
				GuardianCheck.closeWindow();
			}
			else if (cmd.equalsIgnoreCase(PG_PARAM_STATUS)){
				//icona di Controllo parametri fermo	
				String s;
				if (userSess==null)
					 s = LangMgr.getInstance().getLangService("EN_en").getString("parameters", "stoppedpluginicon");
				else s = LangMgr.getInstance().getLangService(userSess.getLanguage() ).getString("parameters", "stoppedpluginicon");
				
				xmlResp = "<response>";
				xmlResp += "<pm>"+ParametersMgr.isMotorStopped()+"</pm>";
				xmlResp += "<stopmessage>"+s+"</stopmessage>";
				xmlResp += "</response>";				
			}
			else if (cmd.equalsIgnoreCase(PG_RELAY))
			{
				xmlResp = commRELAYRFH(request,response);
			}
			if(cmd.equalsIgnoreCase(PG_INTERNAL))
			{
				xmlResp = commINTERNALRFH(request,response);
			}
			else if(cmd.equalsIgnoreCase(PG_ALARM))
			{
				xmlResp = "<response>";
				String refresh = "-1";
				boolean hasalarm = false;
				if(userSess != null)
				{
					xmlResp += "<usernull>0</usernull>";
					GroupListBean groupListBean = userSess.getGroup();
					int[] groups = groupListBean.getIds();
					DeviceStructureList deviceStructureList = groupListBean.getDeviceStructureList(); 
					int[] ids = deviceStructureList.retrieveIdsByGroupsId(groups);
					if(DeviceStatusMgr.getInstance().existAlarm(ids))
					{
						hasalarm = true;
					}
					else
					{
						hasalarm = false;
					}
					UserTransaction trxUser = userSess.getCurrentUserTransaction();
					if(trxUser != null)
					{
						IMaster currentBo = trxUser.getBoTrx();
						refresh = currentBo.getRefreshTime(trxUser.getCurrentTab()).trim();
					}
				}
				else
				{
					xmlResp += "<usernull>1</usernull>";
				}
				xmlResp += "<refresh>"+refresh+"</refresh>";
				/* Multi-message page var declarations */
				int guardian_num = DirectorMgr.getInstance().getGuardian_valid_num();
				int alarm_num = DirectorMgr.getInstance().getAlarm_valid_num();
				int engine_code = ServletHelper.messageToNotify();
				boolean valid = DirectorMgr.getInstance().isPvproValid();
				int[] guardian = DirectorMgr.getInstance().getGuardian_code();
				String guardian_status = getGuardianWinStatus();
				boolean keepalive = DirectorMgr.getInstance().isKeepAlive();
				boolean param_stopped = ParametersMgr.isMotorStopped();
				boolean logging_overload = PvproInfo.getInstance().isLoggingOverload();
				boolean device_detection = DeviceDetectBean.isDetection();
				String strBTAlert = PvproInfo.getInstance().getBTAlert();
				int numSafeMode = CO2SavingManager.getInstance() != null?CO2SavingManager.getInstance().getRackNumAtSafeMode():0;
				int numBackupcondition = CO2SavingManager.getInstance() != null?CO2SavingManager.getInstance().getRackNumInBackupCondition():0;
				/* ***** Multi-message page control logic ***** */
				
				/* Alessandro V. (21/01/2011) enhancement 7754 */
				
				// Retrieve the showsafetylevel flag value 
				IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
				String showSafetyLevel = product.get(BSystem.SHOWSAFETYLEVEL);
				
				// safety level enabled -> the safety check is done
				if ("1".equals(showSafetyLevel)) {
					
					/* RED= NO guardian channel OR 
					 * 		NO alarm channel OR 
					 * 		any alarm OR 
					 * 		Guardian messages OR 
					 * 		Guardian form OR
					 * 		ParameterMgr stoped
					 * 		Logging threshold exceeded 
					 */
					if(guardian_num == 0 || alarm_num == 0 || hasalarm || !guardian_status.equals("0")|| guardian[0] != 3
							|| param_stopped || logging_overload)
					{
						xmlResp += "<status>red</status>";
						/* Only alarms and nothing else -> redirect to the alarm page */
						if(guardian_num != 0 && alarm_num !=0 && hasalarm && guardian_status.equals("0")&& guardian[0] == 3  
							&& !param_stopped && !logging_overload)//add "&& !param_stopped && !logging_overload" by Kevin, means it is pure alarms, no other message
						{
							xmlResp += "<onlyalarm>1</onlyalarm>";
						}
					}
					
					/* YELLOW = pvpro TRIAL OR 
					 * 			engine notifications OR 
					 * 			Guardian partially configured OR 
					 * 			only 1 alarm channel OR 
					 * 			only 1 guardian channel OR 
					 * 			any Backup Tool alert OR 
					 * 			not keep alive 
					 * 			safety restore: has rack in safe mode
					 * 			safety restore: has rack offline and backup condition satisfy
					 */
					else if(alarm_num == 1 || guardian_num == 1|| engine_code != 0 || valid == false|| strBTAlert.length() > 0 || !keepalive || numSafeMode>0 || numBackupcondition>0)
					{
						xmlResp += "<status>yellow</status>";
					}
					
					/* NO BALL */
					else
					{
						xmlResp += "<status>ok</status>";
					}
				
				}else { // safety level disabled -> the safety check is not done

					/* RED= any alarm OR  
					 * 		Guardian messages OR 
					 * 		engine notifications OR 
					 * 		Logging threshold exceeded 
					 */
					if(hasalarm || !guardian_status.equals("0")|| guardian[0] != 3 || param_stopped || logging_overload)
					{
						xmlResp += "<status>red</status>";
						/* Only alarms and nothing else -> redirect to the alarm page */
						if(hasalarm && guardian_status.equals("0") && guardian[0] == 3 && !param_stopped && !logging_overload)//add "&& !param_stopped && !logging_overload" by Kevin, means it is pure alarms, no other message
						{
							xmlResp += "<onlyalarm>1</onlyalarm>";
						}						
					}
					
					/* YELLOW = pvpro TRIAL OR 
					 * 			engine notifications OR
					 * 			Guardian partially configured OR 
					 * 			any Backup Tool alert 
					 * 			safety restore: has rack in safe mode
					 * 			safety restore: has rack offline and backup condition satisfy
					 */
					else if(strBTAlert.length() > 0 || engine_code != 0 || valid == false || numSafeMode>0 || numBackupcondition>0)
					{
						xmlResp += "<status>yellow</status>";
					}
					
					/* NO BALL */
					else
					{
						xmlResp += "<status>ok</status>";
					}					
					
					
					
					
				}
				
				// restart engine
				xmlResp += "<restart_engine>" +  (!bRestartEngine && DirectorMgr.getInstance().isMustRestart()) + "</restart_engine>";
				// logging limits
				if( logging_overload )
					xmlResp += "<logging_overload>" +  PvproInfo.getInstance().getLoggingOverload() + "</logging_overload>";
				// device detection
				if( device_detection )
					xmlResp += "<device_detection>" +  device_detection + "</device_detection>";
				// notifications
				if( userSess != null ) {
					String strIdMsg = NotificationParam.getInstance().retrieve(userSess.getUserName());
					if( strIdMsg != null ) {
						LangService lang = LangMgr.getInstance().getLangService(userSess.getLanguage());
						String strMessage = lang.getString("broadcast", strIdMsg);
						xmlResp += "<notify>" + strMessage + "</notify>";
					}
				}
				// backup tool alert
				if( strBTAlert.length() > 0 )
					xmlResp += "<backup_tool>" + strBTAlert.split(",")[1] + "</backup_tool>"; 
				xmlResp += "</response>";
			}
			else if(cmd.equalsIgnoreCase(PG_IOTEST))
			{
				try
				{
					
					//add 10 seconds delay here to wait I/O test finished. 
					Thread.sleep(10000);
				}
				catch(Exception ex)
				{
				}
				DirectorMgr.getInstance().updateKeepAlive();
				DirectorMgr.getInstance().updateVaidGuardianNum();
				DirectorMgr.getInstance().updateValidAlarmNum();
			}
			else if(cmd.equalsIgnoreCase(RESTART_ENGINE))
			{
				try  {
					setRestartEngine(true);
					BSystem.stopPvEngine(userSess);
					BSystem.startPvEngine(userSess);
					setRestartEngine(false);
				}
				catch(Exception e) {
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
					setRestartEngine(false);
				}
			}
			
			else if( FileDialogBean.CMD.equalsIgnoreCase(cmd) ) {
				String path = request.getParameter("path");
				String filter = request.getParameter("filter");
				xmlResp = (new FileDialogBean(userSess.getLanguage())).cmdResponse(path, filter);
			}
		}
		
		// Response to client
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(xmlResp);
        response.getWriter().flush();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		response.setContentType(CONTENT);
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
		String xmlResp = "";
		
		// Retrive UserSession and check
        UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
        if (ServletHelper.validateSessionWithoutSet(userSession))
        {
			// Retrive parameters from querystring
	        Properties properties = ServletHelper.retrieveParameters(request);
			    

	        UserTransaction ut = userSession.getCurrentUserTransaction();
	        IMaster curBoUsed = ut.getBoTrx();
	        
	        if(curBoUsed != null)
	        {
	        	try
	        	{
	        		xmlResp = curBoUsed.executeDataAction(userSession,ut.getCurrentTab(),properties);
	        	}
	        	catch(Exception e) {
	        		Logger logger = LoggerMgr.getLogger(this.getClass());
	        		logger.error(e);
	        	}
	        }
        }
		
		// Response
        response.setHeader("Cache-Control", "no-cache");
        OutputStream outputStream = response.getOutputStream();
        Writer outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");
        Writer printWriter = new PrintWriter(outputStreamWriter);
        printWriter.write(xmlResp);
        printWriter.flush();
	}
	
	private String commGUISERV(HttpServletRequest request, HttpServletResponse response)
	{
		String ris = "OK";
		try {
			GuardianCheck.restartGuardian();
		}
		catch(Exception e) {
			ris = "KO";
		}
		return "<response>"+ris+"</response>";
	}
	
	private String commDTLRFH(HttpServletRequest request, HttpServletResponse response)
	{
		UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		String language = sessionUser.getLanguage();
		int idsite = sessionUser.getIdSite();
		String ris = "";
		GroupListBean groupListBean = sessionUser.getGroup();
		int[] groups = groupListBean.getIds();
		String limit = request.getParameter("limit");
		String offst = request.getParameter("offset");
		
		try 
		{
			DeviceListBean deviceList =  new DeviceListBean(idsite,language,groups,true,limit,offst);
			if(deviceList != null)
			{
				ris = deviceList.getXmlRefreshDevice();
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ris;
	}
	
	private String commSMPLDTL(HttpServletRequest request, HttpServletResponse response)
	{
		UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		String language = sessionUser.getLanguage();
		int idsite = sessionUser.getIdSite();
		String ris = "";
		GroupListBean groupListBean = sessionUser.getGroup();
		int[] groups = groupListBean.getIds();
		
		try 
		{
			DeviceListBean deviceList =  new DeviceListBean(idsite,language,groups);
			if(deviceList != null)
			{
				ris = "<response>"+ deviceList.getXmlRefreshDevice();
				ris = ris + "</response>";
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return ris;
	}
	
	private String commDEVRFH(HttpServletRequest request, HttpServletResponse response)
	{
		StringBuffer ris = new StringBuffer("<device ");
		String status = "";
		int idDev = -1;
		
		UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		
		try 
		{
			idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
			
			DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
			DeviceStructure deviceStructure = deviceStructureList.get(idDev);
			
			status = UtilDevice.getLedColor(new Integer(idDev));
			ris.append("id='"+idDev+"' status='"+status+"'>");
			
			DeviceDetail dtldevice = new DeviceDetail();
			dtldevice.setIdDevice(idDev);
			dtldevice.setIdDevMdl(deviceStructure.getIdDevMdl());
			dtldevice.loadDeviceVariable(sessionUser.getLanguage(),sessionUser.getIdSite());
			AbstractDtlDevice filterVariable = new HomeDtlDevice(sessionUser,sessionUser.getLanguage(),dtldevice.getIdDevice());
			filterVariable.profileVariables(dtldevice.getVariablesList());
			ris.append(filterVariable.refreshVariables(""+idDev));
			
			filterVariable = new ButtonDtlDevice(sessionUser,sessionUser.getLanguage(),dtldevice.getIdDevice());
			filterVariable.setIdDevMdl(dtldevice.getIdDevMdl());
			filterVariable.profileVariables(dtldevice.getVariablesList());
			ris.append(filterVariable.refreshVariables(""+idDev));
			
			filterVariable = new StateDtlDevice(sessionUser,sessionUser.getLanguage(),dtldevice.getIdDevice());
			filterVariable.setIdDevMdl(dtldevice.getIdDevMdl());
			filterVariable.profileVariables(dtldevice.getVariablesList());
			ris.append(filterVariable.refreshVariables(""+idDev));
			
			filterVariable = new ReadOnlyDtlDevice(sessionUser,sessionUser.getLanguage(),dtldevice.getIdDevice());
			filterVariable.setIdDevMdl(dtldevice.getIdDevMdl());
			filterVariable.profileVariables(dtldevice.getVariablesList());
			ris.append(filterVariable.refreshVariables(""+idDev));

			filterVariable = new WriteDtlDevice(sessionUser,sessionUser.getLanguage(),dtldevice.getIdDevice());
			filterVariable.setIdDevMdl(dtldevice.getIdDevMdl());
			filterVariable.profileVariables(dtldevice.getVariablesList());
			ris.append(filterVariable.refreshVariables(""+idDev));
			ris.append("</device>");
		}
		catch(Exception e){
			ris = new StringBuffer("<device id='-1' status='1'></device>");
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris.toString();
	}
	
	// this method is used to refresh status of Internal IO device on 'device detail page'
	private String commINTERNALRFH(HttpServletRequest request, HttpServletResponse response)
	{
		UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		String language = sessionUser.getLanguage();
		int idsite = sessionUser.getIdSite();
		
		StringBuffer ris = new StringBuffer("<device ");
		String status = "";
		int idDev = -1;
		RelayBean relay = null;
		
		try 
		{
			idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
			
			DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
			DeviceStructure deviceStructure = deviceStructureList.get(idDev);
			
			status = UtilDevice.getLedColor(new Integer(idDev));
			ris.append("id='"+idDev+"' status='"+status+"'>\n");
			
			VarphyBean[] vars = deviceStructure.getVariables();
			for(int i = 0; i<vars.length; i++)
			{
				int idvar = ((VarphyBean)vars[i]).getId();
				String value_from_field = "";
				try
				{
					value_from_field = ControllerMgr.getInstance().getFromField(idvar).getFormattedValue();
				}
				catch (Exception e)
				{
					Logger logger = LoggerMgr.getLogger(this.getClass());
	    			logger.error(e);
	            	value_from_field  = "***";
				}
				

				//detect Relays (variable code is "DOxx"
				if(((VarphyBean)vars[i]).getCodeVar().startsWith("DO"))
				{
					 relay = RelayBeanList.getRelayBeanByVariableid(idsite, language, idvar);
					 ris.append("<var id='var_").append(idvar).append("' value='").append(value_from_field).append("' active='").append(relay.getActivestate()).append("' />\n");
				}
				else
				{
					ris.append("<var id='var_").append(idvar).append("' value='").append(value_from_field).append("' active='' />\n");
				}
			}
			
			ris.append("</device>");
		}
		catch(Exception e){
			ris = new StringBuffer("<device id='-1' status='1'></device>");
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris.toString();
	}
	
	/**
	 * Introdotto controllo sulla finestra di allarme del GuardianPRO
	 */
	private String commLGNRFH(HttpServletRequest request, HttpServletResponse response)
	{
		String ret = "<response>";
		
		if(DeviceStatusMgr.getInstance().existAlarm())
			ret += "<a>Y</a>";
		else
			ret += "<a>N</a>";
		
		ret += ("<g>" + getGuardianWinStatus() + "</g>");
		
		ret += "</response>";
		return ret;
	}

	/**
	 * Periodical relay table refresh (on relaymgr page)
	 */
	private String commRELAYRFH(HttpServletRequest request, HttpServletResponse response)
	{
		StringBuffer ret = new StringBuffer("<response>\n");
		
		try 
		{
			UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
			String language = sessionUser.getLanguage();
			int idsite = sessionUser.getIdSite();
			
			RelayBeanList relayList = new RelayBeanList(idsite, language, true, false);
			String value_from_field = "";
			RelayBean relay = null;
			
			int numrelay = relayList.size();
			
			for(int i=0; i<numrelay; i++)
			{
				relay = relayList.getRelayBean(i);
				if(relay.getShow())
				{
					try
		            {
		                value_from_field = ControllerMgr.getInstance().getFromField(relay.getIdvariable()).getFormattedValue();
		            }
		            catch(Exception e)
		            {
		            	Logger logger = LoggerMgr.getLogger(this.getClass());
		    			logger.error(e);
		            	value_from_field  = "***";	
		            }
		            
		            ret.append("<relay id='").append(Integer.toString(relay.getIdrelay())).append("' val='").append(value_from_field).append("' act='").append(Integer.toString(relay.getActivestate())).append("' /> \n");
				}
			}
		}
		catch (Exception e)
		{
			ret = new StringBuffer("<response><device id='-1' status='1'></device>");
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		ret.append("</response>");
		return ret.toString();
	}
	
	private String commRINTER(HttpServletRequest request, HttpServletResponse response)
	{
		StringBuffer sb = new StringBuffer("<response ");
		UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		
		if(ActiveConnections.getInstance().isConnections())
		{
			sb.append("cmd='mark'>");
			sb.append(ActiveConnections.getInstance().getXmlConnClient());
			if(sessionUser != null)
				sessionUser.setIncomingConnections(true);
		}
		else
		{
			if(sessionUser != null && sessionUser.isIncomingConnections())
			{
				sb.append("cmd='load'>");
				sessionUser.setIncomingConnections(false);
			}
			else
			{
				sb.append("cmd='nop'>");
			}
		}
		
		sb.append("</response>");
		
		return sb.toString();
	}
	
	private String stopBroadcastAnswer(HttpServletRequest request, HttpServletResponse response)
	{
		String toReturn = "";
		String funct =request.getParameter("funct");
		String params = request.getParameter("params");
		String working = (SetDequeuerMgr.getInstance().isWorking())?"on":"off";
			
		toReturn = "<response>";
		toReturn += "<broad><![CDATA["+working+"]]>";
		toReturn += "<![CDATA["+funct+"]]><![CDATA["+params+"]]></broad>\n";
		toReturn +="</response>";

		return toReturn;
	}
	
	private String querySiteStatus(int id)
	{
		String toReturn = "";
		toReturn = "<response>\n";
		try
		{
		if(DeviceStatusMgr.getInstance().existAlarm( new int[]{id} ))
			toReturn += "<sitestatus>alarm</sitestatus>\n";
		else
			toReturn += "<sitestatus>ok</sitestatus>\n";
		}
		catch(Exception e)
		{
			toReturn += "<sitestatus>error</sitestatus>\n";
		}
		toReturn +="</response>";
		return toReturn;
	}
	
	private String refreshTime()
	{
		String ret = "";
		try 
		{
			NumberFormat formatter = new DecimalFormat("##00");
			Calendar c = Calendar.getInstance();
			ret = ""+formatter.format(c.get(Calendar.HOUR_OF_DAY));
			ret += ":"+formatter.format(c.get(Calendar.MINUTE));
			formatter = null;
		}
		catch(Exception e) {
			ret = "00:00";
            Logger logger = LoggerMgr.getLogger(this.getClass());
        	logger.error(e);
		}
		
		return ret;
	}
	
	private String getGuardianWinStatus()
	{
		try {
			return GuardianCheck.isEnableWin();
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "0";
		}
	}
	
	public static synchronized void setRestartEngine(boolean b)
	{
		bRestartEngine = b;
	}
}
