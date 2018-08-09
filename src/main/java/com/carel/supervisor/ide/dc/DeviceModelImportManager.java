package com.carel.supervisor.ide.dc;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.VarCmd;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.ide.dc.DbModel.Instance;
import com.carel.supervisor.ide.dc.DbModel.Model;
import com.carel.supervisor.ide.dc.DbModel.VariableDb;
import com.carel.supervisor.ide.dc.DbModel.Translation.Translations;
import com.carel.supervisor.ide.dc.xmlDAO.DeviceTag;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.presentation.bo.helper.MdlDependencyCheck;
import com.carel.supervisor.presentation.devices.ResetSubset;
import com.carel.supervisor.presentation.session.UserSession;

public class DeviceModelImportManager {

	private String devcode;
	private String windowMsg;
	
	public DeviceModelImportManager()
	{
		devcode = "";
		windowMsg = "";
	}
	
	public int importDeviceModel(String xmlPath, String langcode, int profile,UserSession us) throws ImportException 
	{
		DeviceTag deviceXmlDAO = new DeviceTag();
		Logger log = LoggerMgr.getLogger(this.getClass());
		
		try 
		{
			// Initializes common resources
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();	
			
			Connection conn = DatabaseMgr.getInstance().getConnection(null);
			
			// Loads doc11
			File inputXML = new File(xmlPath);
			
			Document doc = parser.parse(inputXML); 

			XPathFactory xfactory = XPathFactory.newInstance();
			
			try 
			{
				CheckTag check = new CheckTag();
				check.unmarshal(doc, xfactory);
				
				// reads the document's CRC
				Long documentCRC = Long.parseLong(check.getCode(), 16);
				//System.out.println("+++++++++++++++++++++++++++++ Document CRC:"+documentCRC);
			
				// Computes the CRC Check code, and compares it with the actual file's CRC 
				DeviceXMLCRC deviceXMLCRC = new DeviceXMLCRC();
				Long computedCRC = Long.parseLong(deviceXMLCRC.computeCRC(inputXML), 16);
				//System.out.println("+++++++++++++++++++++++++++++ Computed CRC:"+computedCRC);
				
				conn.setAutoCommit(false);
			
				// Unmarshals the whole Device xml (//Device)
				deviceXmlDAO.unmarshal(doc, xfactory);
				
				devcode = deviceXmlDAO.getCode();
				
				//Device insert (or update)
				Model model = new Model();
				boolean deviceExist = model.insertNewModel(deviceXmlDAO, conn);
				// bug fix Alarm reset button disappear in device detail page.
				ResetSubset.getInstance().setInitialized(false);
				
				//if device already exists execute Update
				if(deviceExist)
				{
					//first update existing model, and check new device model correctness
					model.updateModel(deviceXmlDAO, conn);
					log.info("Device model updated.");
					
					//get all VariableDB of imported varmdl
					HashMap<String, VariableDb> importvars = model.getvariables();
					
					//get ids of NEW varmdl inserted inside device model ('cfvarmdl') table
					HashMap<String,Integer> newIdVarMdl = model.getNewIdVarMdl();
					
					//get translations (already 'filled' in Model)
					Translations translations = model.getTranslations();
					
					//get all imported varmdl codes
					Set<String> importmdlVarsCodes = deviceXmlDAO.getPvInfo().getPvVars().getPVVarsCode();
					 				
					Instance instance  = new Instance(devcode, importmdlVarsCodes, importvars, translations, newIdVarMdl, deviceXmlDAO.getImagesInfo(), deviceXmlDAO.getLittleEndian(), deviceXmlDAO.getImage());
					
					//then update existing device instances (cfdevice) and variable instances (cfvariable)
					instance.updateInstances(conn, langcode, profile,devcode);
					log.info("Device instances updated.");
					
					// check if model updated is inside modules configuration
					// and show a message window after update
					windowMsg = checkModulesDependece(devcode, langcode);
					
					if(!windowMsg.equals(""))
					{
						LangService lan = LangMgr.getInstance().getLangService(langcode);
						windowMsg = lan.getString("importctrl", "moduledepmsg") + "\n\n" +windowMsg;
					}
				}
				
				//makes last changes permanent. In case of error, catch block executes rollback to undo last changes.
				conn.commit();
				
				// refresh the map containing list of the 'command' vars
				VarCmd.getInstance().refreshVars();
				
				us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
				
				//Log operation completed
				log.info("Device model import successfully done.");
		
				if(documentCRC.longValue() != computedCRC.longValue())
				{
					if(deviceExist)
						//model update with bad CRC
						return 3;
					else
						//model insert with bad CRC
						return 2;					
				}
				
				if(deviceExist)
					//model update with good CRC
					return 1;
				else
					//model insert with good CRC
					return 0;
			} 
			catch (Exception e) 
			{
				//EN_en language to log on Carel.log
				LangService lan = LangMgr.getInstance().getLangService("EN_en");
				String msg = lan.getString("impdev", e.getMessage());
				
				String descr = "";
				if(e instanceof ImportException)
					descr = ((ImportException)e).getDescription();
				else
					descr = e.getMessage();
					
				log.error("Device model import error: " + msg + " **** " + descr);
			
				try 
				{
					conn.rollback();
				} 
				catch (Exception e2) 
				{
					log.error("** GRAVE: Device model import failed, and even rollback() FAILED **. Reason:",e2);
					throw new ImportException("error");
				}
			
				log.error("Device model import failed. Reason:",e);
			
				throw e;
			} 
			
			finally 
			{
				try 
				{
					conn.close();	
				} 
				catch (Exception e2) 
				{
					log.error("Device model import: Exception raised while closing the DB connection.",e2);
				}
			}
		} 
		catch (ImportException ie)
		{
			throw ie;
		}
		catch (SAXException se)
		{
			throw new ImportException("xmlerr",se,"XML malformed");
		}
		catch (Exception e) 
		{
			log.error("Device model import error: " + e.getMessage());
			log.error("Device model import failed. Reason:",e);
			throw new ImportException("error",e);
		} 	
	}
	
	public void removeDeviceModel(int idDevMdl) throws ImportException
	{	
		Logger log = LoggerMgr.getLogger(this.getClass());
		
		try
		{	
			Connection conn = DatabaseMgr.getInstance().getConnection(null);
			conn.setAutoCommit(false);
			
			try	
			{
				Model model = new Model();
				model.removeModel(Integer.toString(idDevMdl), conn);
					
				//commit modifications
				conn.commit();
					
				// refresh the map containing list of the 'command' vars
				VarCmd.getInstance().refreshVars();
				
				//Log operation completed
				log.info("Device model remove successfully done.");
				log.info("Device model id: "+idDevMdl);
			
			}
			catch (Exception e)
			{
				//EN_en language to log on Carel.log
				LangService lan = LangMgr.getInstance().getLangService("EN_en");
				String msg = lan.getString("impdev", e.getMessage());
				
				String descr = "";
				if(e instanceof ImportException)
					descr = ((ImportException)e).getDescription();
				else
					descr = e.getMessage();
				
				log.error("Device model remove error: " + msg + " **** " + descr);
				
				try 
				{
					conn.rollback();
				} 
				catch (Exception e2) 
				{
					log.error("** GRAVE: Device model remove failed, and even rollback() FAILED **. Reason:",e2);
					throw new ImportException("error", e2);
				}
				
				log.error("Device model remove failed. Reason:",e);
				
				throw e;
			} 
			finally 
			{
				try 
				{
					conn.close();	
				} 
				catch (Exception e2) 
				{
					log.error("Device model remove: Exception raised while closing the DB connection.",e2);
					throw new ImportException("error");
				}
			}
		}
		catch (ImportException ie)
		{
			throw ie;
		}
		catch (Exception e) 
		{
			log.error("Device model remove error: " + e.getMessage());
			log.error("Device model remove failed. Reason:",e);
			throw new ImportException("error",e);
		} 	
	}
	
	public String getDeviceModelCode()
	{
		return devcode;
	}
	
	public String getWindowMsg()
	{
		return windowMsg;
	}

	private String checkModulesDependece(String devcode, String langcode) throws Exception
	{
		String msg = "";
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		
		// retrieve iddevmdl from code 
        String sql = "select iddevmdl from cfdevmdl where code=?";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { devcode });
        int idDevMdl = (Integer) rs.get(0).get("iddevmdl");
		
		if(MdlDependencyCheck.checkMdlInKPI(idDevMdl))
		{
			msg += lan.getString("menu", "kpi") + "\n";
		}
		if(MdlDependencyCheck.checMdlInkLN(idDevMdl))
		{
			msg += lan.getString("menu", "lucinotte") + "\n";
		}
        if(MdlDependencyCheck.checkMdlInDP(idDevMdl))
        {
        	msg += lan.getString("menu", "ac") + "\n";
        }
		return msg;
	}
	
	
}
