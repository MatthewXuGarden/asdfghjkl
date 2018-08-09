package com.carel.supervisor.presentation.assistance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.guardian.GuardianCheck;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.presentation.io.CioFAX;
import com.carel.supervisor.presentation.io.CioMAIL;
import com.carel.supervisor.presentation.io.CioSMS;

public class GuardianConfig 
{
	private static final String C_MAILAN = "com.carel.supervisor.guardian.feedback.channels.EmailLan";
	private static final String C_MAIL   = "com.carel.supervisor.guardian.feedback.channels.Email";
	private static final String C_FAX    = "com.carel.supervisor.guardian.feedback.channels.Fax";
	private static final String C_SMS    = "com.carel.supervisor.guardian.feedback.channels.Sms";
	private static final String C_BUZ    = "com.carel.supervisor.guardian.feedback.channels.Buzzer";
	
	private static final String PREX = "GC_";
	private static final String NUM = "GuiNumSeg";
	public static final String FAX_RECIPIENT = "F";
	public static final String EMAIL_RECIPIENT = "E";
	public static final String SMS_RECIPIENT = "S";
	
	private static final String FAX = "FAX";
	private static final String EMAIL = "EMAIL";
	private static final String SMS = "SMS";
	
	private static Properties propRecipients = null;
	
	public GuardianConfig() {
	}
	
	public static void saveConfiguration(Properties prop,int idSite,String siteName)
	{
		StringBuffer sb = new StringBuffer();
		
		DispatcherBook tmp = null;
		String val = "";
		int idx = -1;
		int num = 0;
		
		
		try {
			val = prop.getProperty(NUM);	
			num = Integer.parseInt(val);
		}
		catch(Exception e){
			num = 0;
		}
		
		for(int i=0; i<num; i++)
		{
			try {
				val = prop.getProperty((PREX+i));
				idx = Integer.parseInt(val);
			}
			catch(Exception e){
				idx = -1;
			}
			
			tmp = DispatcherBookList.getInstance().getReceiver(idx);
			if(tmp != null)
			{
				if(tmp.getType().equalsIgnoreCase("F"))
					sb.append(createXmlFax(tmp,idSite));
				else if(tmp.getType().equalsIgnoreCase("S"))
					sb.append(createXmlSms(tmp,idSite));
				else if(tmp.getType().equalsIgnoreCase("E"))
					sb.append(createXmlEmail(tmp,idSite));
			}
		}
		sb.append(createXmlBuzzer());
		
		String gsignal = prop.getProperty("gch");
		writeXmlGuardian(sb.toString(),siteName,gsignal);
		
		// restart guardian to force new configuration loading
		GuardianRestart();
	}
	public static String getGardianSignal()
	{
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
		try 
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			
			if(url != null)
			{
				XMLNode xmlNodeRoot = XMLNode.parse(url.openStream());
				XMLNode xmlNode = xmlNodeRoot.getNode("notifylist");
				String notify = xmlNode.getAttribute("enabled");
				if(notify == null)
					notify = "TRUE";
				if("TRUE".equalsIgnoreCase(notify))
					return null;
				else
					return "F";
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
		return "F";
	}
	
	public static void saveConfiguration(int idSite,String siteName)
	{
		getOptionListConf(); // get recipients
		saveConfiguration(propRecipients, idSite, siteName);
	}

	
	public static boolean isRecipient(String strRecipient)
	{
		getOptionListConf(); // get recipients
		int n = Integer.parseInt(propRecipients.getProperty(NUM));	
		for(int i = 0; i < n; i++) {
			int idx = Integer.parseInt(propRecipients.getProperty((PREX+i)));
			DispatcherBook dispBook = DispatcherBookList.getInstance().getReceiver(idx);
			if( dispBook != null && dispBook.getType().equalsIgnoreCase(strRecipient) )
				return true;
		}
		return false;
	}
	
	
	public static String getOptionListConf()
	{
		StringBuffer sb = new StringBuffer();
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
		
		// cache recipients
		propRecipients = new Properties();
		int nRecipients = 0;
		
		try
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			XMLNode xmlNode = null;
			XMLNode device  = null;
			XMLNode xmlNodeRoot = null;
			DispatcherBook tmp = null;
			String att = "";
			String val = "";
			int idx = -1;
			String decode = "";
			
			if(url != null)
			{
				xmlNodeRoot = XMLNode.parse(url.openStream());
				xmlNode = xmlNodeRoot.getNode("notifylist");
				String notify = xmlNode.getAttribute("enabled");
				if(notify == null)
					notify = "TRUE";
				if("TRUE".equalsIgnoreCase(notify))
					propRecipients.setProperty("gch", "0");
				for(int i=0; i<xmlNode.size(); i++)
				{
					device = xmlNode.getNode(i);
					for(int j=0; j<device.size(); j++)
					{
						att = device.getNode(j).getAttribute("name");
						if(att != null && att.equalsIgnoreCase("pvpro"))
						{
							val = device.getNode(j).getAttribute("value");
							try {
								idx = Integer.parseInt(val);
								tmp = DispatcherBookList.getInstance().getReceiver(idx);
								if(tmp != null)
								{
									if(tmp.getType().equalsIgnoreCase("F"))
										decode = "FAX - ";
									else if(tmp.getType().equalsIgnoreCase("S"))
										decode = "SMS - ";
									else if(tmp.getType().equalsIgnoreCase("E"))
										decode = "EMAIL - ";
									sb.append("<option value=\""+val+"\">"+decode+tmp.getReceiver()+" ("+tmp.getAddress()+") </option>");
									propRecipients.setProperty(PREX + nRecipients++, val);
								}
							}
							catch(Exception e) {
								Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
								logger.error(e);
							}
							
							val = "";
							att = "";
							break;
						}
					}
				}
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
		
		propRecipients.setProperty(NUM, "" + nRecipients);
		return sb.toString();
	}
	public static int checkNotificationChannelNumber()
	{
		int num = 0;
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
		
		try
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			XMLNode xmlNode = null;
			XMLNode device  = null;
			XMLNode xmlNodeRoot = null;
			String type = "";
			String att = "";
			String val = "";
			int idx = -1;
			DispatcherBook tmp = null;
			String[] addedChannels = new String[6];
			if(url != null)
			{
				xmlNodeRoot = XMLNode.parse(url.openStream());
				xmlNode = xmlNodeRoot.getNode("notifylist");
				for(int i=0; i<xmlNode.size(); i++)
				{
					device = xmlNode.getNode(i);
					type = device.getAttribute("type");
					//if(EMAIL.equals(type))
					{
						if(channelExist(addedChannels,type) == false)
						{
							
							for(int j=0; j<device.size(); j++)
							{
								att = device.getNode(j).getAttribute("name");
								if(att != null && att.equalsIgnoreCase("pvpro"))
								{
									val = device.getNode(j).getAttribute("value");
									try {
										idx = Integer.parseInt(val);
										tmp = DispatcherBookList.getInstance().getReceiver(idx);
										if(tmp != null)
										{
											if("OK".equalsIgnoreCase(tmp.getIoteststatus()))
											{
												addedChannels[num++] = type;
												if(num>1)
												{
													return num;
												}
											}
										}
									}
									catch(Exception e) {
										Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
										logger.error(e);
									}
									
									val = "";
									att = "";
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
		return num;
	}
	public static boolean channelExist(String[] addedChannels, String newChannel)
	{
		for(int i=0;i<addedChannels.length;i++)
		{
			if(addedChannels[i] == null)
			{
				return false;
			}
			else if(addedChannels[i].equals(newChannel))
			{
				return true;
			}
		}
		return false;
	}
	public static String getOptionList()
	{
		StringBuffer sb = new StringBuffer();
		DispatcherBook[] list = null;
		String type = "";
		
		// Fax
		list = DispatcherBookList.getInstance().getReceiversByType("F");
		type = "FAX - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\" class='"+(i%2==0?"Row1":"Row2")+"' >"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}
		
		// Sms
		list = DispatcherBookList.getInstance().getReceiversByType("S");
		type = "SMS - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\" class='"+(i%2==0?"Row1":"Row2")+"' >"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}
		
		// Email
		list = DispatcherBookList.getInstance().getReceiversByType("E");
		type = "EMAIL - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\" class='"+(i%2==0?"Row1":"Row2")+"' >"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}
		
		return sb.toString();
	}
	
	private static String createXmlFax(DispatcherBook db,int idSite)
	{
		CioFAX cio = new CioFAX(idSite);
		cio.loadConfiguration();
		
		StringBuffer xml = new StringBuffer();
		
		xml.append("<notify name=\"FaxPvproGuardian\" type=\""+FAX+"\" class=\""+C_FAX+"\">");
		xml.append("<object name=\"sender\" value=\"PvPro Guardian\"/>");
		xml.append("<object name=\"receiver\" value=\""+db.getReceiver()+"\"/>");
		xml.append("<object name=\"pathFile\" value=\""+BaseConfig.getCarelPath()+"guardian"+"\"/>");
		xml.append("<object name=\"nameFile\" value=\"PVGuardian.rtf\"/>");
		xml.append("<object name=\"centralino\" value=\""+cio.getCentralino()+"\"/>");
		xml.append("<object name=\"phoneReceiver\" value=\""+db.getAddress()+"\"/>");
		xml.append("<object name=\"phoneSender\" value=\"00\"/>");
		xml.append("<object name=\"idModem\" value=\""+cio.getModemId()+"\"/>");
		xml.append("<object name=\"servicepath\" value=\""+DispatcherMgr.getInstance().getServicesPath()+"\"/>");
		xml.append("<object name=\"pvpro\" value=\""+db.getKey()+"\"/>");
		xml.append("</notify>");
		
		return xml.toString();
	}
	
	private static String createXmlSms(DispatcherBook db,int idSite)
	{
		CioSMS cio = new CioSMS(idSite);
		cio.loadConfiguration();
		String device = cio.getLabelModem();
		StringBuffer xml = new StringBuffer();
		
		xml.append("<notify name=\"SmsPvproGuardian\" type=\""+SMS+"\" class=\""+C_SMS+"\">");
		xml.append("<object name=\"prefix\" value=\""+cio.getCentralino()+"\"/>");
		xml.append("<object name=\"phoneNumber\" value=\""+db.getAddress()+"\"/>");
		xml.append("<object name=\"iniPath\" value=\""+DispatcherMgr.getInstance().getProviderPath()+DispatcherMgr.getInstance().getProviderName()+"\"/>");
		xml.append("<object name=\"providerName\" value=\""+cio.getProviderLb()+"\"/>");
		xml.append("<object name=\"typeCall\" value=\""+cio.getCall()+"\"/>");
		xml.append("<object name=\"idModem\" value=\""+device+"\"/>");
		xml.append("<object name=\"servicepath\" value=\""+DispatcherMgr.getInstance().getServicesPath()+"\"/>");
		xml.append("<object name=\"pvpro\" value=\""+db.getKey()+"\"/>");
		xml.append("</notify>");
		
		return xml.toString();
	}
	
	private static String createXmlEmail(DispatcherBook db,int idSite)
	{
		CioMAIL cio = new CioMAIL(idSite);
		cio.loadConfiguration();
		
		StringBuffer xml = new StringBuffer();
		String sClass = C_MAILAN;
		
		if(cio.getProviderId() != null && !cio.getProviderId().equalsIgnoreCase(""))
			sClass = C_MAIL;
		
		xml.append("<notify name=\"EmailPvproGuardian\" type=\""+EMAIL+"\" class=\""+sClass+"\">");
		xml.append("<object name=\"addressReceiver\" value=\""+db.getAddress()+"\"/>");
		xml.append("<object name=\"addressSender\" value=\""+cio.getSender()+"\"/>");
		xml.append("<object name=\"text\" value=\"PvPro Mail\"/>");
		xml.append("<object name=\"subject\" value=\"PvPro Mail\"/>");
		xml.append("<object name=\"pathFile\" value=\"\"/>");
		xml.append("<object name=\"smtp\" value=\""+cio.getSmtp()+"\"/>");
		// Uset e Pass per SMTP authentication
		xml.append("<object name=\"smtpuser\" value=\""+cio.getUser()+"\"/>");
		xml.append("<object name=\"smtppass\" value=\""+cio.getPass()+"\"/>");
		xml.append("<object name=\"encryption\" value=\""+cio.getEncryption()+"\"/>");
		
		if(cio.getProviderId() != null && !cio.getProviderId().equalsIgnoreCase(""))
		{
			xml.append("<object name=\"provider\" value=\""+cio.getProviderId()+"\"/>");
			xml.append("<object name=\"user\" value=\"\"/>");
			xml.append("<object name=\"password\" value=\"\"/>");
			xml.append("<object name=\"servicepath\" value=\""+DispatcherMgr.getInstance().getServicesPath()+"\"/>");
		}
		xml.append("<object name=\"pvpro\" value=\""+db.getKey()+"\"/>");
		xml.append("</notify>");

		return xml.toString();
	}
	
	private static String createXmlBuzzer() {
		return "<notify name=\"BuzzerPvproGuardian\" type=\"BUZZER\" class=\""+C_BUZ+"\"></notify>";
	}
	
	private static void writeXmlGuardian(String conf,String siteName,String gsignal)
	{
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
		try 
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			
			if(url != null)
			{
				XMLNode xmlNodeRoot = XMLNode.parse(url.openStream());
				XMLNode xmlNode = xmlNodeRoot.getNode("checklist");
				xmlNode.setAttribute("site",siteName);
				
				// Rename Old File
				Calendar cal = new GregorianCalendar();
				String sData = "_"+cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH)+cal.get(Calendar.DAY_OF_WEEK)+
							   "_"+System.currentTimeMillis();
				
				file = new File(pathFile);
				file.renameTo(new File((pathFile+sData)));
				
				FileOutputStream fos = new FileOutputStream(pathFile);
				Writer out = new OutputStreamWriter(fos, "UTF8");
				out.write("<guardian>");
				if(gsignal == null)
				{
					out.write("<notifylist enabled=\"FALSE\">");
				}
				else
				{
					out.write("<notifylist enabled=\"TRUE\">");
				}
				out.write(conf);
				out.write("</notifylist>");
				out.write(xmlNodeRoot.getNode("feedbacklist").toString());
				out.write(xmlNode.toString());
				out.write("</guardian>");
				out.flush();
				out.close();
				fos.close();
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
	}
	
	public static boolean userConfGuiVariable()
	{
        String gsn = ProductInfoMgr.getInstance().getProductInfo().get("gvark");
        if(gsn != null)
            return true;
        
		boolean ris = false;
		String sql = "select * from cfvarguardian";
		RecordSet rs = null;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			if(rs != null && rs.size() > 0)
				ris = true;
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
		return ris;
	}
	
	public static boolean userConfGuiChannel()
	{
	    String gsn = getGardianSignal();
        if(gsn != null)
            return true;
        
		boolean ris = false;
		
		String pathFile = BaseConfig.getCarelPath()+"guardian"+File.separator+"Guardian.xml";
			
		try
		{
			File file = new File(pathFile);
			URL url = file.toURL();
			XMLNode xmlNode = null;
			XMLNode xmlNodeRoot = null;
			
			if(url != null)
			{
				xmlNodeRoot = XMLNode.parse(url.openStream());
				xmlNode = xmlNodeRoot.getNode("notifylist");
				ris = xmlNode.size()>1;
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(GuardianConfig.class);
			logger.error(e);
		}
		return ris;
	}
    
    /*
     * Metodo richiamato dalla pagine di configurazione del guardiano 
     * per lo stop e la disabilitazione del servizio del guardiano
     */
    public static boolean GuardianStopAndDisable()
    {
        boolean ris = false;
        GuardianCheck.stopGuardian();
        GuardianCheck.enableGuardian(false);
        return ris;
    }
    
    /*
     * Metodo richiamato dalla pagina di configurazione del guardiano
     * per la riabilitazione e lo start del servizio del guardiano
     */
    public static boolean GuardianStartAndEnable()
    {
        boolean ris = false;
        GuardianCheck.enableGuardian(true);
        GuardianCheck.startGuardian();
        return ris;
    }
    
    
    public static void GuardianRestart()
    {
    	if( GuardianCheck.isLive() )
    		GuardianCheck.restartGuardian();
    	else
    		GuardianCheck.startGuardian();
    }
}
