package com.carel.supervisor.presentation.svgmaps;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.Unzipper;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.presentation.sdk.util.CustomChecker;
import com.carel.supervisor.presentation.session.UserSession;



public class SvgMapsUtils
{
	
	public static String SVGMAPS_ROOT = System.getenv("PVPRO_HOME")+"/engine/webapps/PlantVisorPRO/app/mstrmaps/svgmaps";
	private static final String [] VARTYPES = new String[] {"Digitals","Analogs", "Integers", "Alarms"};
	public static final String SEPARATOR = ".";
	public static final String VARCODE_SEPARATOR = "|";
	public static int devAddrLength = 3; 
	
	public static void modifyWebMIContext(String svgMapsScriptFilePath) throws IOException
	{
		File svgMapsScriptFile = new File(svgMapsScriptFilePath);
		//String path = request.getContextPath();
		//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String content = FileUtils.readFileToString(svgMapsScriptFile, "UTF-8");
	    content = content.replaceAll("\"/webMI/\\?", "parent.document.getElementById('basePath').href+\"servlet/svgmaps?");
	    FileUtils.writeStringToFile(svgMapsScriptFile, content, "UTF-8");
	}
	
	public static void unzipMaps(String svgMapsPath) throws Exception
	{
		File svgMapsScriptFile = new File(svgMapsPath +"/webmi.js");
		RandomAccessFile raf = new RandomAccessFile(svgMapsScriptFile, "r");  
		long gzipSignature = 0x1F8B0800;
		long mask = 0xFFFFFF00;
    	long fileheader = raf.readInt();  
    	raf.close();  
    	if ((fileheader & mask) == gzipSignature) {
        	// atvise gzips all the html, js and SVG files. Not the pics (png, jpegs) and other media formats (like wav)
        	// also the .cfg files are not gzipped.
        	// We must unzip the current folder + the language folders (en, de,...)
        	String[] GZIP_FILES_FILTER = new String[] {"htm", "js", "svg"};
        	//String dir  = BaseConfig.getAppHome()+"/app/mstrmaps/svgmaps/";
        	Collection<File> filesToGunzip =  FileUtils.listFiles(new File(svgMapsPath), GZIP_FILES_FILTER, true);
	    	for (Iterator<File> iterator = filesToGunzip.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				Unzipper.gunzipSameFile(file);
			}
    	}
	}
	
	public static String getSiteDatapoint()
	{
		StringBuffer s_response = new StringBuffer();
		
		if (PacketMgr.getInstance().isFunctionAllowed("svgmaps"))
		{
			try
	    	{
				//Description exported using the default login language
				String exportLang = BaseConfig.getProductInfo("login_language");
				if (exportLang==null || exportLang.equalsIgnoreCase(""))
				{
					exportLang = "EN_en";
				}
				
				// Atvise site xml creation		
				String sql_dev = "select a.iddevice, a.iddevmdl, a.devcode, a.linecode, t.description from " +
						"(select cfdevice.iddevice as iddevice, cfdevice.iddevmdl as iddevmdl, cfdevice.code as linecode, cfdevmdl.code as devcode from cfdevice left join cfdevmdl on cfdevmdl.iddevmdl=cfdevice.iddevmdl " +
						"where cfdevice.iscancelled = 'FALSE' and (cfdevice.globalindex<>0 or islogic = 'TRUE')) as a " +
						"inner join cftableext as t on t.tableid = a.iddevice where t.idsite=1 and t.tablename = 'cfdevice' and t.languagecode = '"+exportLang+"'";
	    		
	    		String sql_vars = "select cfvarmdl.code as varcode, cfvarmdl.idvarmdl, cftableext.description as vardesc from cfvarmdl " +
	    				"inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid "+
	    				"where cfvarmdl.iddevmdl = ? and cfvarmdl.type = ? and cftableext.idsite=1 and cftableext.tablename='cfvarmdl' and languagecode = ?";
	    		
	    		String sql_vars_logic = "select cfvariable.code as varcode, cfvariable.idvarmdl, cftableext.description as vardesc from cfvariable " +
	    				"inner join cftableext on cfvariable.idvariable=cftableext.tableid "+
	    				"where cfvariable.iddevice = ? and cfvariable.type = ? and cfvariable.idhsvariable is not null and cftableext.idsite=1 and cftableext.tablename='cfvariable' and languagecode = ?";
	    		
	    		s_response.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	    		
	    		// The separator set on XML seems ignored from the builder (it uses '.' to construct the addresses also if it is set different on xml)
	    		s_response.append("<item name=\"Root\" separator=\""+SEPARATOR+"\">\n");
	    		
	    		ArrayList<String> device_models = new ArrayList<String>();
	    		Record rec = null;
	    		Integer iddev = null;
	    		String dev_code = "";
	    		String location_code = null;
	    		String dev_desc = "";
	    		boolean is_logic = false;
	    		
				RecordSet recordSetDev = DatabaseMgr.getInstance().executeQuery(null, sql_dev, new Object[] {});
				
				// DEVICES
				for (int i=0; i<recordSetDev.size();i++)
				{
					rec = recordSetDev.get(i);
					iddev = (Integer) rec.get("iddevmdl");
					dev_desc = (rec.get("description").toString()).replace(SEPARATOR, "");
					dev_desc = dev_desc.replace(VARCODE_SEPARATOR, "");
					dev_desc =  StringEscapeUtils.escapeXml10(dev_desc);
					location_code =  rec.get("linecode").toString();
					is_logic = false;
					
					if(rec.get("devcode") != null)
						dev_code = StringEscapeUtils.escapeXml10(rec.get("devcode").toString());
					else // when devcode is NULL, then the variable is LOGICAL
					{
						dev_code = dev_desc;
						is_logic = true;
						//sql_vars = sql_vars_logic;
						iddev = (Integer) rec.get("iddevice");
						location_code = "L." + location_code;
					}
					if(device_models.contains(dev_code))
					{
						// write the 'device' ITEM
						s_response.append("<item name=\""+location_code+SEPARATOR+dev_desc+"\" type=\"#"+dev_code+"\" />\n");
						continue;
					}
					
					// write the 'device' TYPE
					s_response.append("<type name=\""+dev_code+"\">\n");
					
					// write the 'vartype' ITEM
					for(int type=1; type<=4; type++)
					{
						s_response.append("<item name=\""+VARTYPES[type-1]+"\">\n");
	
						// VARIABLES
						RecordSet recordSetVar = DatabaseMgr.getInstance().executeQuery(null, (is_logic?sql_vars_logic:sql_vars), new Object[] {iddev, type, exportLang});
						Record var = null;
						String code = "";
						String vardesc = "";
						
						for (int j=0; j<recordSetVar.size();j++)
						{
							var = recordSetVar.get(j);
							code = StringEscapeUtils.escapeXml10(var.get("varcode").toString());
							vardesc = (var.get("vardesc").toString()).replace(SEPARATOR, "");
							vardesc = vardesc.replace(VARCODE_SEPARATOR, "");
							vardesc = StringEscapeUtils.escapeXml10(vardesc);
							// write the 'variable'  ITEM within the 'device' TYPE
							String typeStr = (type==1||type==4)?"boolean":"number";
							s_response.append("<item name=\""+vardesc+" "+VARCODE_SEPARATOR+" "+code+"\" type=\""+typeStr+"\" />\n");
						}
						
						s_response.append("</item>\n");
					}
					
					s_response.append("</type>\n");
					
					// write the 'vartype' ITEM within a 'device' TYPE
					s_response.append("<item name=\""+location_code+SEPARATOR+dev_desc+"\" type=\"#"+dev_code+"\" />\n");
					
					device_models.add(dev_code);
	
				}
			} catch (Exception e) {
			
				LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
				e.printStackTrace();
			} 
	    		
	    	// end Atvise xml creation
	    	s_response.append("</item>\n");
	    	
	    	// DEBUG PURPOSES
	    	/*Writer writer = null;
	
	    	try {
	    	    writer = new BufferedWriter(new OutputStreamWriter(
	    	          new FileOutputStream("C:\\outputSiteConf.txt"), "utf-8"));
	    	    writer.write(s_response.toString());
	    	} catch (IOException ex) {
	    	  // report
	    	} finally {
	    	   try {writer.close();} catch (Exception ex) {}
	    	}*/
		}
      	return s_response.toString();
	}
	
	public static String getDevMdlDatapoint(int iddevmdl, String dev_mdl_code)
	{
		StringBuffer s_response = new StringBuffer();
		
		
		if (PacketMgr.getInstance().isFunctionAllowed("svgmaps"))
		{
			try
	    	{
				//Description exported using the default login language
				String exportLang = BaseConfig.getProductInfo("login_language");
				if (exportLang==null || exportLang.equalsIgnoreCase(""))
				{
					exportLang = "EN_en";
				}
				
				
				
				String sql_vars = "select cfvarmdl.code as varcode, cfvarmdl.idvarmdl, cftableext.description as vardesc from cfvarmdl " +
	    				"inner join cftableext on cfvarmdl.idvarmdl=cftableext.tableid "+
	    				"where cfvarmdl.iddevmdl = ? and cfvarmdl.type = ? and cftableext.idsite=1 and cftableext.tablename='cfvarmdl' and languagecode = ?";
	    		
	    		
	    		s_response.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	    		
	    		// The separator set on XML seems ignored from the builder (it uses '.' to construct the addresses also if it is set different on xml)
	    		s_response.append("<item name=\"Root\" separator=\""+SEPARATOR+"\">\n");
	  					
				// write the 'device' TYPE
				s_response.append("<type name=\""+dev_mdl_code+"\">\n");
				
				// write the 'vartype' ITEM
				for(int type=1; type<=4; type++)
				{
					s_response.append("<item name=\""+VARTYPES[type-1]+"\">\n");

					// VARIABLES
					RecordSet recordSetVar = DatabaseMgr.getInstance().executeQuery(null, sql_vars, new Object[] {iddevmdl, type, exportLang});
					Record var = null;
					String code = "";
					String vardesc = "";
					
					for (int j=0; j<recordSetVar.size();j++)
					{
						var = recordSetVar.get(j);
						code = StringEscapeUtils.escapeXml10(var.get("varcode").toString());
						vardesc = (var.get("vardesc").toString()).replace(SEPARATOR, "");
						vardesc = vardesc.replace(VARCODE_SEPARATOR, "");
						vardesc = StringEscapeUtils.escapeXml10(vardesc);
						// write the 'variable'  ITEM within the 'device' TYPE
						String typeStr = (type==1||type==4)?"boolean":"number";
						s_response.append("<item name=\""+vardesc+" "+VARCODE_SEPARATOR+" "+code+"\" type=\""+typeStr+"\" />\n");
					}
					
					s_response.append("</item>\n");
				}
				
				s_response.append("</type>\n");
				
				// write the 'vartype' ITEM within a 'device' TYPE
				s_response.append("<item name=\"$dev$\" type=\"#"+dev_mdl_code+"\" />\n");
	
			} catch (Exception e) {
			
				LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
				e.printStackTrace();
			} 
	    		
	    	s_response.append("</item>\n");

		}
      	return s_response.toString();
	}
	
	public static boolean checkSvgMaps()
	{
		File f = new File(SVGMAPS_ROOT+"/index.htm");
		if(f.exists() && !f.isDirectory())
		{
			return true;
		}
		else return false;
	}
	
	public static boolean checkLanguageTranslation(String lang)
	{
		File f = new File(SVGMAPS_ROOT+"/"+lang);
		if(f.exists() && f.isDirectory())
		{
			return true;
		}
		else return false;
	}
	
	public static String getSvgMapsRoot()
	{
		return SVGMAPS_ROOT;
	}
	
	public static String getLicensePath(UserSession session)
	{
		String path = "";
		if (!"dtlview".equalsIgnoreCase(session.getProperty("folder")))
			path = SVGMAPS_ROOT+File.separator+"license.cfg";
		else
		{
			String sql = "select folder from cfdevcustom where iddevmdl = (select iddevmdl from cfdevice where iddevice="+session.getProperty("iddev")+")";
			RecordSet recordSet;
			try {
				recordSet = DatabaseMgr.getInstance().executeQuery(null, sql);
				String dev_folder = recordSet.get(0).get("folder").toString();
				path = BaseConfig.getAppHome()+"custom"+
		                File.separator+"dtlview"+File.separator+dev_folder+File.separator+"license.cfg";
			} catch (DataBaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return path;
	}
}