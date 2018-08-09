package supervisor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.io.UnicodeReader;
import com.carel.supervisor.base.io.Unzipper;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.director.ide.RuleImporter;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.ide.dc.DeviceModelImportManager;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bo.BBooklet;
import com.carel.supervisor.presentation.bo.BEnergy;
import com.carel.supervisor.presentation.bo.BSiteView;
import com.carel.supervisor.presentation.bo.BSystem;
import com.carel.supervisor.presentation.bo.BVisualScheduler;
import com.carel.supervisor.presentation.bo.helper.BackupHelper;
import com.carel.supervisor.presentation.bo.helper.ProfileConfigImport;
import com.carel.supervisor.presentation.bo.helper.DeviceImport.DeviceConfigImport;
import com.carel.supervisor.presentation.helper.MultipartRequest;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.io.CioEVT;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;


public class ServUpload extends HttpServlet {
	
	private static final String TABBODY_PATH = "/arch/include/Tabbody.jsp";
	private static final String ERROR_PATH = "/arch/include/Error.jsp";
	private final static String DTLVIEW_PATH = "custom" + File.separator + "dtlview";
	private final static String DTLVIEW_SECTION_PATH = "custom" + File.separator + "dtlview_section";
	private static final String EXTRAPATH = "engine"+File.separator+"temp";
	public static final String BOOKLET_CABINET_PATH = "booklet";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		fileUpload(request, response);		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		fileUpload(request, response);
	
	}
	
	public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// stringhe per path di salvataggio file in upload:
		String confDir = "conf";
		String ruleDir = "rule"; //da config. regole
		String jspDir = "jsp";
		String svgDir = "svg";
		String svgDtlDir = "svgdtl";
		String devDir = "dev";
		String rulesDir = "xmlrules"; //da config. editor
		String impParamModule = "impParamModule"; //da config. editor
		String imgtoplogo = "toplogoimg";
		String imglogin = "loginimg";
		String imgdevice = "devimg";
		String importWav = "wav";
		String importVSCat = "VSCatImp";
		String importEnergyProfile = "EnergyProfile";
		String tipoFile = "";
		String dev_folder = "";
		//device config import
		String deviceconfigDir = "deviceconfig";
		String profconfigDir = "profconfig";
		String sitebackup = "sitebackup";
		String booklet_cabinet = "booklet_cabinet";
		request.setCharacterEncoding("UTF-8");
		// Retrive UserSession
		UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
		
		// controlla "contentType" dalla Form
		String contentType = request.getContentType();
		MultipartRequest multireq = null;
 		if( ServletHelper.validateSession(us) && contentType != null ) {
 			String filePath = "";
 			boolean bRemoveFile = false;
 			
			if( contentType.indexOf("multipart/form-data") >= 0 ) {
		 		// get uploaded file
				bRemoveFile = true;

	     		String fileName = "";
	        	String saveFile = "";
				
				// aggiungo il path al file di output (sottodir � sempre la stessa per l'upload)
				saveFile = System.getenv("PVPRO_HOME") + File.separator + EXTRAPATH + File.separator + saveFile;
				multireq = new MultipartRequest(request.getContentType(), request.getContentLength(), request.getInputStream(), saveFile, request.getContentLength());
				
				tipoFile = multireq.getURLParameter("tipofile");
				dev_folder = multireq.getURLParameter("dev_folder");
				
				Enumeration en = multireq.getFileParameterNames();
																	
				// prendo il primo file soggetto ad upload
				if(en.hasMoreElements())
				{
					fileName = (String)en.nextElement();
					fileName = multireq.getFileSystemName(fileName);		
				}
			
				if( fileName != null && fileName.length() > 0 )
					filePath = saveFile + fileName;
			} // if( contentType.indexOf("multipart/form-data") >= 0 )
			else if( contentType.equalsIgnoreCase("application/x-www-form-urlencoded") ) {
				// get local file
				bRemoveFile = false;
				tipoFile = request.getParameter("tipofile");
				dev_folder = request.getParameter("dev_folder");
				String fdField = request.getParameter("fdField");
				if( fdField != null ) {
					String fileName = request.getParameter(fdField);
					if( fileName != null )
						filePath = fileName;
				}
			}
			
			response.setContentType("application/unknown");
			response.setContentType("text/html; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");
    		
	        Properties properties = new Properties();
    		Transaction transaction = us.getTransaction();
    		    		
    		if( importWav.equalsIgnoreCase(tipoFile) )					// "wav" (wave file for alarm window)
        		ImportWav(us, properties, transaction, filePath);
    		else if( confDir.equalsIgnoreCase(tipoFile) )				// "sitemgrimp" (*.conf)
        		ImportConf(us, properties, transaction, filePath);
        	else if( ruleDir.equalsIgnoreCase(tipoFile) )				// "rulemgrimp" (*.rule)
        		ImportConfRules(us, properties, transaction, filePath);
        	else if( imgtoplogo.equalsIgnoreCase(tipoFile) )			// "changeimgtop" (picture)
        		ChangeImgTop(us, properties, transaction, filePath);
        	else if( imglogin.equalsIgnoreCase(tipoFile) )				// "changeimg" (picture)
				ChangeImgLogin(us, properties, transaction, filePath);
        	else if( tipoFile.startsWith(imgdevice) )				// "devimg" (picture)
				ChangeImgDevice(us, properties, transaction, filePath, tipoFile);
			else if( devDir.equalsIgnoreCase(tipoFile) )				// "impdevcreator" (*.xml)
        		ImpDevCreator(us, properties, transaction, filePath);
        	else if( jspDir.equalsIgnoreCase(tipoFile) )				// "impjsp" (*.zip)
    			ImportJsp(us, properties, transaction, filePath);
        	else if( svgDir.equalsIgnoreCase(tipoFile) )				// "impsvg" (*.zip)
    			ImportSvg(us, properties, transaction, filePath);
        	else if( svgDtlDir.equalsIgnoreCase(tipoFile) )				// "impsvgdtl" (*.zip)
    			ImportDtlSvg(us, properties, transaction, filePath, dev_folder);
        	else if( rulesDir.equalsIgnoreCase(tipoFile) )				// "importrules" (*.xml)
    			ImportRules(us, properties, transaction, filePath);
        	else if( impParamModule.equalsIgnoreCase(tipoFile) )				// "import device parameter module" 
        		ImpParameterModule(us, properties, transaction, filePath);
        	else if( deviceconfigDir.equalsIgnoreCase(tipoFile) ) {		// "deviceconfig"
    			String newfilePath = "";
    			if( contentType.indexOf("multipart/form-data") >= 0 )
    			{
    				newfilePath = filePath + new SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
    			}
    			else
    			{
    				newfilePath = filePath;
    			}
    			File oldfile = new File(filePath);
    			String filename = oldfile.getName();
    			if(!filePath.equals(newfilePath))
    			{
	    			File newfile = new File(newfilePath);
	    			oldfile.renameTo(newfile);
    			}
    			ImportDeviceConfig(us,properties,transaction,newfilePath,filename);
    		}
        	else if(sitebackup.equalsIgnoreCase(tipoFile))
        	{
        		UploadSiteBackup(us,properties,transaction,filePath);
        	}
        	else if(profconfigDir.equalsIgnoreCase(tipoFile)){
        		boolean overwrite = false;
        		
        		String overwriteProf2 = us.getPropertyAndRemove("overwriteProf2");
    			if(overwriteProf2!=null && overwriteProf2.equalsIgnoreCase("yes")){
    				overwrite = true;
    			}
        		String newfilePath = "";
    			if( contentType.indexOf("multipart/form-data") >= 0 )
    			{	if(overwrite){
    					newfilePath = us.getPropertyAndRemove("filename2"); 
    					filePath = newfilePath;
    				}else{
    					newfilePath = filePath + new SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
    				}
    			}
    			else
    			{
    				newfilePath = filePath;
    			}
    			File oldfile = new File(filePath);
    			String filename = oldfile.getName();
    			if(!filePath.equals(newfilePath))
    			{
	    			File newfile = new File(newfilePath);
	    			oldfile.renameTo(newfile);
    			}
    			UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
    			ProfileBeanList profile_list = new ProfileBeanList(sessionUser.getIdSite(),false);
    			
    			int result = ImportProfConfig(us,properties,transaction,newfilePath,filename,profile_list,overwrite);
    			if( result != 1 )
    				filePath = newfilePath; // remove profile
        	}
        	else if( importVSCat.equalsIgnoreCase(tipoFile) ) {
        		ImpVSCategories(us, properties, transaction, filePath);
        	}
        	else if( importEnergyProfile.equalsIgnoreCase(tipoFile) ) {
        		ImpEnergyProfile(us, properties, transaction, filePath);
        	}
        	else if(booklet_cabinet.equalsIgnoreCase(tipoFile))
        	{
        		ImpBooklet_cabinet(request,multireq,us,properties,transaction,filePath);
        	}
    		if( bRemoveFile )
    			RemoveFile(filePath);
			
 		} // if( ServletHelper.validateSession(us) && contentType != null )
		
		RequestDispatcher dispatcher = null;
		dispatcher = getServletContext().getRequestDispatcher(TABBODY_PATH);
		
		// Dispatch to Resource
        try
        {
            dispatcher.include(request, response);
        }
        catch (Throwable e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            us.setThrowable(e);
            dispatcher = getServletContext().getRequestDispatcher(ERROR_PATH);
            dispatcher.include(request, response);
        }
        
		return;
	}
	
 
	private void ImportWav(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		byte[] buffer = new byte[1024];
		
		try
		{
			String fileOutWav = "";
			
			if( filePath.length() > 0 )
			{
				int n = 0;
				File fileInput = new File(filePath);
				FileInputStream fileInputStream = new FileInputStream(fileInput);
				
				String folderOutWav = BaseConfig.getAppHome() + "sounds";
				String filename = fileInput.getName();
				fileOutWav = folderOutWav + File.separator + filename;
				FileOutputStream fileOutputStream = new FileOutputStream(fileOutWav);
		
				while ((n = fileInputStream.read(buffer)) != -1)
				{
					fileOutputStream.write(buffer, 0, n);
				} // while di copia
			
				fileOutputStream.close();
				fileInputStream.close();
			}
			
            CioEVT evt = new CioEVT(us.getIdSite());
            evt.loadConfiguration();
            boolean esy = evt.saveConfiguration(evt.getIdconf(), fileOutWav);
            if( esy )
                DispMemMgr.getInstance().storeConfiguration("W");
		}
		catch (Exception e)
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
	}

	private void ImportConf(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		try 
		{
			DirectorMgr.getInstance().mustCreateProtocolFile();
			
			stopPvEngine(us);
			
			// ripristina file di config.:
			String r = BackupHelper.restoreConf(us.getUserName(), filePath);
			properties.setProperty("sitemgrimp", r);
			
			transaction.setSystemParameter(properties);
		}
		catch (Exception e)
		{
			properties.setProperty("sitemgrimp", e.getMessage());
			transaction.setSystemParameter(properties);
		}
		finally
		{
			try
			{
				startPvEngine(us);
				us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
			}
			catch (Exception e)
			{
				properties.setProperty("sitemgrimp", e.getMessage());
				transaction.setSystemParameter(properties);
			}
		}
		
		us.setForceLogout(true);
	}
	
	private void ImportConfRules(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		try {
			DirectorMgr.getInstance().mustCreateProtocolFile();
			
			stopPvEngine(us);
	
			String r = BackupHelper.restoreRule(us.getUserName(), filePath);
			properties.setProperty("rulemgrimp", r);
	
			transaction.setSystemParameter(properties);
		}
		catch (DataBaseException e)
		{
			properties.setProperty("rulemgrimp", e.getMessage());
			transaction.setSystemParameter(properties);
		}
		catch (Exception e)
		{
			properties.setProperty("rulemgrimp", e.getMessage());
			transaction.setSystemParameter(properties);
		}
		finally
		{
			try
			{
				startPvEngine(us);
				us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
			}
			catch (Exception e)
			{
				properties.setProperty("sitemgrimp", e.getMessage());
				transaction.setSystemParameter(properties);
			}
		}
		us.setForceLogout(true);
	}

	private void ImportRules(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		try
		{
			File fileXML = new File(filePath);
			UnicodeReader r = new UnicodeReader(new FileInputStream(fileXML), null);
			XMLNode xml = XMLNode.parse(r);
	
			RuleImporter ruleImporter = new RuleImporter();
			ruleImporter.importer(xml);
			properties.setProperty("importrules", "OK");
			transaction.setSystemParameter(properties);
			DirectorMgr.getInstance().mustRestart();
		}
		catch (Exception e)
		{
			properties.setProperty("importrules", e.getMessage());
			transaction.setSystemParameter(properties);
	
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	private void ChangeImgTop(UserSession us, Properties properties, Transaction transaction, String filePath)
	{	
		IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
		
		String langcode = us.getLanguage();
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		
		// immagine di default
		if (filePath.equals(""))
		{
			String old_image = p_info.get("imgtop");
			try {
				p_info.set("imgtop", "top/left.png");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			properties.setProperty("changeimgtop", lan.getString("mgr", "defaultimg"));
			transaction.setSystemParameter(properties);
	
			if (!old_image.equals("top/left.png")) // cancellazione
													// vecchio file
													// da cartella
													// custom
			{
				String dest = BaseConfig.getAppHome() + "images" + File.separator + old_image;
				File file_to_delete = new File(dest);
				file_to_delete.delete();
			}
		}else
		{
			String old_image = p_info.get("imgtop");
	
			byte[] buffer = new byte[1024];
	
			try
			{
				int n = 0;
				String appHome = BaseConfig.getAppHome() + "images" + File.separator + "custom_login";

				File imageFile = new File(filePath);
				String newfileName = imageFile.getName();
				String copyName = newfileName;
				File fileOutputImg = new File(appHome + File.separator + newfileName);
	
				FileInputStream fileInputStream = new FileInputStream(imageFile);
				FileOutputStream fileOutputStream = new FileOutputStream(fileOutputImg);
	
				while ((n = fileInputStream.read(buffer)) != -1)
				{
					fileOutputStream.write(buffer, 0, n);
				} // while di copia
	
				fileInputStream.close();
				fileOutputStream.close();
	
				p_info.set("imgtop", "custom_login" + File.separator + newfileName);
	
				properties.setProperty("changeimgtop", lan.getString("mgr", "imgset") + copyName);
				transaction.setSystemParameter(properties);
	
				if (!old_image.equals("top/left.png") && !old_image.equals("custom_login" + File.separator + newfileName)) // cancellazione
																														// vecchio
																														// file
				{
					String dest = BaseConfig.getAppHome() + "images" + File.separator + old_image;
					File file_to_delete = new File(dest);
					file_to_delete.delete();
				}
			} catch (Exception e)
			{
				properties.setProperty("changeimgtop", e.getMessage());
				transaction.setSystemParameter(properties);
	
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
	
				// logger.er
			}
		}        				
	}

	private void ChangeImgLogin(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
		
		String langcode = us.getLanguage();
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		
		try {
			p_info.load();
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
		}
	
		// immagine di default
		if (filePath.equals(""))
		{
			String old_image = p_info.get("img");
			try {
				p_info.set("img", "login.jpg");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			properties.setProperty("changeimg", lan.getString("mgr", "defaultimg"));
			transaction.setSystemParameter(properties);
	
			if (!old_image.equals("login.jpg")) // cancellazione
												// vecchio file da
												// cartella custom
			{
				String dest = BaseConfig.getAppHome() + "images" + File.separator + old_image;
				File file_to_delete = new File(dest);
				file_to_delete.delete();
			}
		} else
		{
			String old_image = p_info.get("img");
	
			byte[] buffer = new byte[1024];
	
			try
			{
				int n = 0;
				String appHome = BaseConfig.getAppHome() + "images" + File.separator + "custom_login";

				File imageFile = new File(filePath);
				String newfileName = imageFile.getName();
				String copyName = newfileName;
				File fileOutputImg = new File(appHome + File.separator + newfileName);
	
				FileInputStream fileInputStream = new FileInputStream(imageFile);
				FileOutputStream fileOutputStream = new FileOutputStream(fileOutputImg);
	
				while ((n = fileInputStream.read(buffer)) != -1)
				{
					fileOutputStream.write(buffer, 0, n);
				} // while di copia
	
				fileInputStream.close();
				fileOutputStream.close();
	
				p_info.set("img", "custom_login" + File.separator + newfileName);
	
				properties.setProperty("changeimg", lan.getString("mgr", "imgset") + copyName);
				transaction.setSystemParameter(properties);
	
				if (!old_image.equals("login.jpg") && !old_image.equals("custom_login" + File.separator + newfileName)) // cancellazione
																													// vecchio
																													// file
				{
					String dest = BaseConfig.getAppHome() + "images" + File.separator + old_image;
					File file_to_delete = new File(dest);
					file_to_delete.delete();
				}
			} catch (Exception e)
			{
				properties.setProperty("changeimg", e.getMessage());
				transaction.setSystemParameter(properties);
	
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	private void ChangeImgDevice(UserSession us, Properties properties, Transaction transaction, String filePath, String fileType)
	{
		try {
			int idDevice = Integer.parseInt(fileType.split("_")[1]);
		
			try {
				// get previous image and remove image file if it is not default
				String sqlQuery = "select cfdevmdl.imagepath as imgmdl, cfdevice.imagepath as imgdev "
					+ " from cfdevmdl, cfdevice where cfdevmdl.iddevmdl = "
					+ "(select iddevmdl from cfdevice where iddevice = ?) "
					+ "and cfdevice.iddevice = ?;";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlQuery, new Object[] { idDevice, idDevice });
				if( rs.size() == 1 ) {
					Record r = rs.get(0);
					String strImgMdl = r.get("imgmdl").toString();
					String strImgDev = r.get("imgdev").toString();
					if( !strImgDev.equalsIgnoreCase(strImgMdl) && strImgDev.startsWith("_custom_") ) {
						// check image dependencies
						sqlQuery = "select count(*) from cfdevice where imagepath = ? and iscancelled='FALSE';";
						rs = DatabaseMgr.getInstance().executeQuery(null, sqlQuery, new Object[] { strImgDev });
						if( rs.size() == 1 ) {
							int n = (Integer)rs.get(0).get(0);
							if( n <= 1 ) {
								// there are no dependencies, file could be deleted
								String strCustomFilePath = BaseConfig.getAppHome() + "images" + File.separator + "devices"
								+ File.separator + strImgDev;
								File file = new File(strCustomFilePath);
								file.delete();
							}
						}
					}
				}
			} catch(DataBaseException e) {
				LoggerMgr.getLogger(ServUpload.class).error(e);
			} catch(Exception e) {
				LoggerMgr.getLogger(ServUpload.class).error(e);
			}
			
			if( filePath.isEmpty() ) {
				//restore default image
				try {
					String sql = "update cfdevice set imagepath = (select imagepath from cfdevmdl where iddevmdl = "
						+"(select iddevmdl from cfdevice where iddevice = ?)) where iddevice = ?;";
					DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idDevice, idDevice });
				} catch(DataBaseException e) {
					LoggerMgr.getLogger(ServUpload.class).error(e);	
				}
			}
			else {
				try {
					// copy image file to custom destination
					File imageFile = new File(filePath);
					String strDestFileName = "_custom_" + idDevice + "_" + imageFile.getName();
					String strDestPath = BaseConfig.getAppHome() + "images" + File.separator + "devices"
						+ File.separator + strDestFileName;
					File fileOutputImg = new File(strDestPath);
					FileInputStream fileInputStream = new FileInputStream(imageFile);
					FileOutputStream fileOutputStream = new FileOutputStream(fileOutputImg);
					byte[] buffer = new byte[1024];
					int n = 0;
					while( (n = fileInputStream.read(buffer)) != -1 )
						fileOutputStream.write(buffer, 0, n);
					fileInputStream.close();
					fileOutputStream.close();
					// change image
					String sql = "update cfdevice set imagepath = ? where iddevice = ?";
					DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { strDestFileName, idDevice });
				} catch(Exception e) {
					LoggerMgr.getLogger(ServUpload.class).error(e);
				}
			}
			// update device structure
			us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
		} catch(NumberFormatException e) {
			LoggerMgr.getLogger(ServUpload.class).error(e);
		} catch(Exception e) {
			LoggerMgr.getLogger(ServUpload.class).error(e);
		}
	}
	
	private void ImpParameterModule(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		File f=new File(filePath);
		String newf=BaseConfig.getCarelPath() +BaseConfig.getTemporaryFolder()+File.separator+f.getName();
		copyFile(filePath,newf);
		us.setProperty("params_values", newf);
		us.setProperty("cmd", "load_from_file");
	}
	
	private void ImpVSCategories(UserSession us, Properties prop, Transaction transaction, String filePath)
	{
		prop.setProperty("cmd", "import");
		prop.setProperty("impexp", filePath);
		try {
			BVisualScheduler bo = new BVisualScheduler(us.getLanguage());
			bo.executePostAction(us, "tab3name", prop);
		}
		catch(Exception e) {
			LoggerMgr.getLogger(ServUpload.class).error(e);
		}
	}
	
	private void ImpEnergyProfile(UserSession us, Properties prop, Transaction transaction, String filePath)
	{
		prop.setProperty("cmd", "importXML");
		prop.setProperty("impexp", filePath);
		try {
			BEnergy bo = new BEnergy(us.getLanguage());
			bo.executePostAction(us, "tab6name", prop);
		}
		catch(Exception e) {
			LoggerMgr.getLogger(ServUpload.class).error(e);
		}
	}
	
	private void ImpDevCreator(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		String langcode = us.getLanguage();
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		int profile = us.getProfile();
		
		try
		{	
			DeviceModelImportManager devImport = new DeviceModelImportManager();
			int updated = devImport.importDeviceModel(filePath, langcode, profile,us);
			
			switch (updated)
			{
				//import ok (model insert)
				case 0:
					properties.setProperty("impdevcreator", "Import OK");
					break;
				//import ok (model update)
				case 1:
					properties.setProperty("impdevcreator", lan.getString("impdev", "updmdl"));
					break;
				//import ok (model insert) + crc mismatch
				case 2:
					properties.setProperty("impdevcreator", "Import Ok"+"<br>"+lan.getString("impdev", "crcerr"));
					break;
				//import ok (model update) + crc mismatch
				case 3: 
					properties.setProperty("impdevcreator", lan.getString("impdev", "updmdl")+"<br>"+lan.getString("impdev", "crcerr"));
					break;
			}
			
			// if the insert operation is OK, then create custom directory (for customized jsp page)
			// and add custom directory name (equals to the device code) into cfdevcustom table
			
            try
            {
				String devcode = devImport.getDeviceModelCode();
	            String devFolder = BaseConfig.getAppHome() + DTLVIEW_PATH + File.separator + devcode;
	            
	            if (!(new File(devFolder).exists()))
	            {
	                new File(devFolder).mkdirs();
	            }
	            
	            devFolder = BaseConfig.getAppHome() + DTLVIEW_SECTION_PATH + File.separator + devcode;
	            
	            if (!(new File(devFolder).exists()))
	            {
	                new File(devFolder).mkdirs();
	            }
	        
	            String sql_newfolder = "insert into cfdevcustom (folder, iddevmdl) values (?, (select iddevmdl from cfdevmdl where code=? and idsite=1))";
	            DatabaseMgr.getInstance().executeStatement(null, sql_newfolder, new Object[]{devcode, devcode});
            }
            catch (Exception e)
            {
            	Logger logger = LoggerMgr.getLogger(BSystem.class);
    			logger.error(e);
            }
			
            properties.setProperty("impdevmsgvisib", "hidden");
            properties.setProperty("impdevmsgdisp", "none");
            properties.setProperty("windowmsg", devImport.getWindowMsg());
            
            DirectorMgr.getInstance().mustCreateProtocolFile();
            DirectorMgr.getInstance().mustRestart();
            transaction.setSystemParameter(properties);
		}
		catch (ImportException ie)
		{	
			String msg = lan.getString("impdev", ie.getMessage());
			if (msg.equals(""))
			{
				msg = lan.getString("impdev", "error");
			}
			if(ie.getMessage().equals("modelerr") || ie.getMessage().equals("xmlerr") || ie.getMessage().equals("usererror"))
			{
				msg = msg + " - " + ie.getDescription(); 
			}
			properties.setProperty("impdevcreator", msg);
			
			if(ie.getMessage().equals("varimporterr"))
			{
				String errorcause = lan.getString("importctrl", "varcause");
				String tmpstr = ("<b>"+errorcause +"</b><br>" + ie.getDescription());
				properties.setProperty("impdevvarmsg", tmpstr);	
				properties.setProperty("impdevmsgvisib", "visible");
				properties.setProperty("impdevmsgdisp", "block");
			}
			
			transaction.setSystemParameter(properties);
		}
		catch (Exception e)
		{			
			String msg = lan.getString("impdev", "error");
			properties.setProperty("impdevcreator", msg);	
			transaction.setSystemParameter(properties);
	
			Logger logger = LoggerMgr.getLogger(BSystem.class);
			logger.error(e);
		}	
	}


	private void ImportJsp(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		
		byte[] buffer = new byte[1024];
		
		try
		{
			//create temorary directory where file is unzipped
			// HACK: .zip file has the same name of .xml file
			String sep = File.separator;
			if (!filePath.contains(File.separator))
			{
				sep = "/";
			}
			String fileName = filePath.substring(filePath.lastIndexOf(sep)+1);
			String tmpPath = filePath.substring(0, filePath.lastIndexOf(sep));
			String fileNameNoExt = fileName.substring(0, fileName.indexOf("."));		
			String tmpBackupPath = tmpPath + File.separator + fileNameNoExt; 
			new File(tmpBackupPath).mkdir();
			
			
			//unzip file
			Unzipper.unzipDirSubDir(filePath,tmpBackupPath);
			
			String jspFilePath = tmpBackupPath + File.separator + fileNameNoExt + ".jsp";
			
			String langcode = us.getLanguage();
			LangService lan = LangMgr.getInstance().getLangService(langcode);
			String error = lan.getString("mgr", "error");
			
			int n = 0;
		
			String appHome = BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps";
			File fileInputJsp = new File(jspFilePath);
			String newfileName = fileInputJsp.getName();
			String copyName = newfileName;
			File fileOutputJsp = new File(appHome + File.separator + newfileName);
	
			FileInputStream fileInputStream = new FileInputStream(fileInputJsp);
			FileOutputStream fileOutputStream = new FileOutputStream(fileOutputJsp);
	
			while ((n = fileInputStream.read(buffer)) != -1)
			{
				fileOutputStream.write(buffer, 0, n);
			} // while di copia
	
			fileInputStream.close();
			fileOutputStream.close();
	
			// Copia Directory
			String nameDir = newfileName.substring(0, newfileName.length() - 4);
			String pathDir = fileInputJsp.getParent() + File.separator + nameDir;
			File dirInputJsp = new File(pathDir);
			File[] filesInputJsp = dirInputJsp.listFiles();
	
			for (int i = 0; i < filesInputJsp.length; i++)
			{
				fileInputStream = new FileInputStream(filesInputJsp[i].getCanonicalPath());
				fileName = filesInputJsp[i].getName();
				fileOutputJsp = new File(appHome + File.separator + nameDir);
				fileOutputJsp.mkdir();
				fileOutputJsp = new File(appHome + File.separator + nameDir + File.separator + fileName);
	
				fileOutputStream = new FileOutputStream(fileOutputJsp);
	
				while ((n = fileInputStream.read(buffer)) != -1)
				{
					fileOutputStream.write(buffer, 0, n);
				} // while di copia
	
				fileInputStream.close();
				fileOutputStream.close();
			} // for
	
			properties.setProperty("impjsp", lan.getString("mgr", "execcopy")+": " + copyName);
			transaction.setSystemParameter(properties);
			
			//se import ok, allora elimino directory di unzip
			RemoveDirectory(tmpBackupPath);
			
			// Remove cache files of maps (WORK)
			delJspCache(us, error);
		} 
	    catch (Exception e)
		{
			properties.setProperty("impjsp", e.getMessage());
			transaction.setSystemParameter(properties);
			Logger logger = LoggerMgr.getLogger(BSystem.class);
			logger.error(e);
		}
	}
	
	private void ImportSvg(UserSession us, Properties properties, Transaction transaction, String filePath)
	{
		
		try
		{
			String langcode = us.getLanguage();
			LangService lan = LangMgr.getInstance().getLangService(langcode);
			
			String sep = File.separator;
			if (!filePath.contains(File.separator))
			{
				sep = "/";
			}

			String destFolder = BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps" + File.separator + "svgmaps"; 
			//unzip file
			
			Unzipper.unzipDirSubDir(filePath,filePath+"svgmaps");
			String [] contents = new File(filePath+"svgmaps").list();
			
			//check if zip file content is correct
			if(contents.length!=1 && Arrays.asList(contents).contains("webmi.js"))
			{
				new File(destFolder).delete();
				new File(destFolder).mkdir();
				FileUtils.copyDirectory(new File(filePath+"svgmaps"),new File(destFolder));
			
				properties.setProperty("impsvg", lan.getString("mgr", "importok"));
				transaction.setSystemParameter(properties);
			}
			else
			{
				properties.setProperty("impsvg", lan.getString("mgr", "impsvgfailed"));
				transaction.setSystemParameter(properties);
			}
		} 
	    catch (Exception e)
		{
			properties.setProperty("impsvg", e.getMessage());
			transaction.setSystemParameter(properties);
			Logger logger = LoggerMgr.getLogger(BSystem.class);
			logger.error(e);
		}
	}
	
	private void ImportDtlSvg(UserSession us, Properties properties, Transaction transaction, String filePath, String dev_folder)
	{
		
		try
		{
			String langcode = us.getLanguage();
			LangService lan = LangMgr.getInstance().getLangService(langcode);
			
			String sep = File.separator;
			if (!filePath.contains(File.separator))
			{
				sep = "/";
			}

			String destFolder = BaseConfig.getAppHome() + "custom" + File.separator + "dtlview" + File.separator + dev_folder; 
			//unzip file
			
			Unzipper.unzipDirSubDir(filePath,filePath+"svgmaps");
			String [] contents = new File(filePath+"svgmaps").list();
			
			//check if zip file content is correct
			if(contents.length!=1 && Arrays.asList(contents).contains("webmi.js"))
			{
				new File(destFolder).delete();
				new File(destFolder).mkdir();
				FileUtils.copyDirectory(new File(filePath+"svgmaps"),new File(destFolder));
			
				properties.setProperty("impsvgdtl", lan.getString("mgr", "importok"));
				transaction.setSystemParameter(properties);
			}
			else
			{
				properties.setProperty("impsvgdtl", lan.getString("mgr", "impsvgfailed"));
				transaction.setSystemParameter(properties);
			}
		} 
	    catch (Exception e)
		{
			properties.setProperty("impsvgdtl", e.getMessage());
			transaction.setSystemParameter(properties);
			Logger logger = LoggerMgr.getLogger(BSystem.class);
			logger.error(e);
		}
	}


	private void stopPvEngine(UserSession us) throws Exception
	 {
	    long sleep = 1000L;
	    // Stop motore
	    if (DirectorMgr.getInstance().isStarted())
	    {
	        DirectorMgr.getInstance().stopEngine(us.getUserName());
	        sleep = 10000L;
	    }
	
	    // Rest macchina a stati
	    ControllerMgr.getInstance().reset();
	
	    // Stop dispatcher
	    if (DispatcherMgr.getInstance().isServiceRunning())
	    {
	        DispatcherMgr.getInstance().stopService();
	        sleep = 10000L;
	    }
	
	    Thread.sleep(sleep);
	 }
	
	 private void startPvEngine(UserSession us) throws Exception
	 {
	    // Start if trial period or license correct
	    if (Information.getInstance().canStartEngine())
	    {
	        if (DirectorMgr.getInstance().isStopped())
	        {
	            DirectorMgr.getInstance().reloadConfiguration(us.getUserName());
	            DirectorMgr.getInstance().startEngine(us.getUserName());
	
	            if (!DirectorMgr.getInstance().isStopped())
	            {
	                if (!DispatcherMgr.getInstance().isServiceRunning())
	                {
	                    DispatcherMgr.getInstance().startService(true);
	                }
	            }
	        }
	    } 
	    else
	        EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
	    
	    Thread.sleep(1000L);
	 }
	 
	 private void RemoveFile(String filePath)
	 {
		 if(filePath == null || filePath.equals(""))
		 {
			 return;
		 }
		 try
		 {
			 File file_to_delete = new File(filePath);
			 file_to_delete.delete();
		 }
		 catch (Exception e)
		 {
			 Logger logger = LoggerMgr.getLogger(this.getClass());
			 logger.error(e);
		 }
	 }
 
	 private void RemoveDirectory(String dirPath)
	 {
		 File path = new File(dirPath);
		 
		 if( path.exists() ) 
		 {
		     File[] files = path.listFiles();
		     for(int i=0; i<files.length; i++) {
		        if(files[i].isDirectory()) {
		          RemoveDirectory(files[i].getAbsolutePath());
		        }
		        else {
		          files[i].delete();
		        }
		     }
		     path.delete();
		 }
	 }
	 public void copyFile(String oldPath, String newPath) { 
	       try { 
//	           int bytesum = 0; 
	           int byteread = 0; 
	           File oldfile = new File(oldPath); 
	           if (oldfile.exists()) { 
	               FileInputStream inStream = new FileInputStream(oldPath); 
	               FileOutputStream fs = new FileOutputStream(newPath); 
	               byte[] buffer = new byte[1444]; 
	              while ( (byteread = inStream.read(buffer)) != -1) { 
//	                   bytesum += byteread; 
	                   fs.write(buffer, 0, byteread); 
	               } 
	               inStream.close(); 
	           } 
	       }catch (Exception e) { 
	           System.out.println("copy error"); 
	           e.printStackTrace(); 
	       } 
	   } 

	/*
	 * Metodo che elimina ogni file dalla cartella di cache delle jsp importate dall'IDE
	 */
	private boolean delJspCache(UserSession usrSess, String errore)
	{
	    boolean cancellato = false;
	    
	    //TODO: non � sempre localhost !?!
	    String host = "localhost";
	    
	    String jspcachedir = BaseConfig.getCarelPath() + "engine" + File.separator + "work" + File.separator + "Catalina" + File.separator;
	    jspcachedir += host;
	    jspcachedir += File.separator + "PlantVisorPRO" + File.separator + "org" + File.separator + "apache" + File.separator + "jsp" + File.separator + "app" + File.separator + "mstrmaps";
	    
	    try
	    {
	        File jspcachedircontent = new File(jspcachedir);
	        File[] jspcachefiles = jspcachedircontent.listFiles();
	                                            
	        if (jspcachefiles != null)
	        {
	            for (int j=0; j < jspcachefiles.length; j++)
	            {
	                cancellato = new File(jspcachefiles[j].getCanonicalPath()).delete();
	                
	                if (! cancellato)
	                {
	                    Logger logger = LoggerMgr.getLogger(this.getClass());
	                    logger.error(errore+": deleteFile() " + jspcachefiles[j].getCanonicalPath());
	                }
	            }
	        }
	    }
	    catch (Exception e)
	    {
	        //PVPro-generated catch block
	        Logger logger = LoggerMgr.getLogger(this.getClass());
	        logger.error(e);
	    }
	    
	    return cancellato;
	}
	private void ImportDeviceConfig(UserSession us,Properties prop,Transaction tran,String filePath,String filename)
	{
		UserTransaction ut = us.getCurrentUserTransaction();
		BSiteView siteview = (BSiteView)ut.getBoTrx();
		DeviceConfigImport importer = new DeviceConfigImport();
		siteview.setDeviceConfigImport(importer);
		int result = importer.loadBeanByXML(us.getIdSite(),/*prop*/us.getProperties(), filePath, true, true, true, true, true, true, true, us.getLanguage());
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		if(result == 0)
		{
			//prop.setProperty("loadxmlerror", lang.getString("propagate", "loadxmlerror"));
			us.setProperty("loadxmlerror", lang.getString("propagate", "loadxmlerror"));
		}
		//prop.setProperty("filename", filename);
		us.setProperty("filename", filename);
		importer.setFilename(filename);
		//tran.setSystemParameter(prop);
	}
	
	private int ImportProfConfig(UserSession us,Properties prop,Transaction tran,String filePath,String filename,ProfileBeanList profile_list,boolean overwrite)
	{ 
		UserTransaction ut = us.getCurrentUserTransaction();
		ProfileConfigImport importer = new ProfileConfigImport();
		
		int result = importer.loadBeanByXML(us.getIdSite(),prop, filePath, us.getLanguage(),profile_list,overwrite);
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		if(result == 4){
			us.setProperty("higherVersion", "yes");
		}
		if(result == 10){
			us.setProperty("lowerVersion", "yes");
		}
		if(result == 11){
			us.setProperty("errorVersion", "yes");
		}
		if(result == 3){
			us.setProperty("isadmin", "yes");
		}
		if(result == 2)
		{
			us.setProperty("loadxmlerror", "yes");
		}
		if(result == 1)
		{
			us.setProperty("overwriteProf", "yes");  //set for JS.
			us.setProperty("overwriteProf2", "yes"); //set for java.
			us.setProperty("filename", filePath);	// set for local.
			us.setProperty("filename2", filePath);	//set for remote
			
		}
		prop.setProperty("filename", filename);
		importer.setFilename(filename);
		tran.setSystemParameter(prop);
		
		return result;
	}
	private void UploadSiteBackup(UserSession us,Properties prop,Transaction tran,String filename)
	{
		boolean r = BackupHelper.SiteBackupUploadFile(us.getUserName(), filename);
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		if(r)
		{
			prop.setProperty("sitebacupload", lang.getString("mgr", "sitebacuploadok"));
		}
		else
		{
			prop.setProperty("sitebacupload", lang.getString("mgr", "sitebacuploaderr"));
		}
		tran.setSystemParameter(prop);
	}
	private void ImpBooklet_cabinet(HttpServletRequest request,MultipartRequest multireq,UserSession us,Properties prop,Transaction tran,String filePath)
	{		
		byte[] buffer = new byte[1024];
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		try
		{
			int n = 0;
			String bookletPath = BaseConfig.getCarelPath()+BOOKLET_CABINET_PATH;
			Properties properties = null;
			if(multireq != null)
				properties = retrieveParameters(multireq);
			else
				properties = ServletHelper.retrieveParameters(request);
			
			if("".equals(filePath))
			{
				String id = properties.getProperty("id");
				if(id != null && id.length()>0)
				{
					properties.put("filename", "");
				}
			}
			else
			{
				File file = new File(filePath);
				String newfileName = file.getName();
				
				File fileCopy = new File(bookletPath + File.separator + newfileName);
	
				FileInputStream fileInputStream = new FileInputStream(file);
				FileOutputStream fileOutputStream = new FileOutputStream(fileCopy);
	
				while ((n = fileInputStream.read(buffer)) != -1)
				{
					fileOutputStream.write(buffer, 0, n);
				} // while di copia
				prop.setProperty("booklet_cabinet_upload", lang.getString("mgr", "sitebacuploadok"));
				tran.setSystemParameter(prop);
				fileInputStream.close();
				fileOutputStream.close();
				
				properties.put("filename", newfileName);
			}
			BBooklet bo = new BBooklet(us.getLanguage());
			bo.executePostAction(us, "tab2name", properties);
		} catch (Exception e)
		{
			prop.setProperty("booklet_cabinet_upload", lang.getString("mgr", "sitebacuploaderr"));
			tran.setSystemParameter(prop);
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	private Properties retrieveParameters(MultipartRequest multireq)
    {
        Properties properties = new Properties();

        Enumeration param = multireq.getParameterNames();
        String name = null;
    	while (param.hasMoreElements())
        {
            name = param.nextElement().toString();
            properties.setProperty(name, multireq.getURLParameter(name).toString());
        }
        return properties;
    }
}