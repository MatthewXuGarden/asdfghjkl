package com.carel.supervisor.presentation.bean;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession; 
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;


public class FileDialogBean {

	protected boolean bLocal					= false;	// true if loaded from localhost
	protected boolean bOnScreenKey				= false;	// var related to virtual keyboard	
	protected int nWidth						= 800;		// dialog width
	protected int nHeight						= 600;  	// dialog height
	protected int nTopOffset					= -140;		// header top correction
	protected int nLeftOffset					= 0;		// menu left correction
	protected int nPlaceWidth					= 128;		// dialog left places width
	protected int nBottomHeight					= 40;		// dialog controls height
	protected int nScreenWidth;
	protected int nScreenHeight;
	protected Vector<Properties> places			= new Vector<Properties>();
	protected LangService lang					= null;
	
	static public final String CMD				= "FileDialog"; // check cmd parameter on page BO

	// default icons
	static public final String FOLDER			= "images/filedialog/folder_opened.png";
	static public final String FILE				= "images/filedialog/file.png";
	static public final String USB				= "images/filedialog/usb.png";
	static public final String CFCARD			= "images/filedialog/cfcard.png";
	static public final String FTP				= "images/filedialog/ftp.png";
	static public final String NETFOLDER		= "images/filedialog/netfolder_opened.png";
	static public final String DRIVE			= "images/filedialog/drive_opened.png";
	static public final String CLOSE			= "images/filedialog/close.png";
	// page input handler buttons
	static private final String strBrowseOn		= "images/filedialog/browse_on.png";
	static private final String strBrowseOff	= "images/filedialog/browse_off.png";
	static private final String strSaveOn		= "images/filedialog/save_on.png";
	static private final String strSaveOff		= "images/filedialog/save_off.png";
	//static private final String strRefresh		= "images/filedialog/refresh_on_black.png";
//	protected String overwriteProf 				= "false";
	
	
	public FileDialogBean(String language)
	{
		lang = LangMgr.getInstance().getLangService(language);
		bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();		
	}
	
	public static boolean isLocal(HttpServletRequest request)
	{
		String remoteAddr = request.getRemoteAddr();
		String remoteHost = request.getRemoteHost();
		String server = request.getServerName();
		return !BaseConfig.isDemo()
			&& ( remoteAddr.equals("127.0.0.1")
			|| remoteHost.equalsIgnoreCase(server)
			|| server.equalsIgnoreCase("localhost")
			|| server.equals("127.0.0.1") );
	}
	public FileDialogBean(HttpServletRequest request)
	{
		bLocal = FileDialogBean.isLocal(request);
		
		UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
//		overwriteProf = us.getProperty("overwriteProf2");
		nScreenWidth = us.getScreenWidth();
		nScreenHeight = us.getScreenHeight();
		if( nScreenWidth <= 1024 || nScreenHeight <= 960 ) {
			nWidth = 800;
			nHeight = 436;
			nTopOffset = -135;
			nLeftOffset = 0;
		}
		else {
			nWidth = 800;
			nHeight = 600;
			nTopOffset = -135;
			nLeftOffset = 0;
		}
		
		lang = LangMgr.getInstance().getLangService(us.getLanguage());
		bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();		
	}
	
	
	// return FileDialog page holder
	public String renderFileDialog()
	{
		StringBuffer div = new StringBuffer();
		div.append("<script language='javascript'>var fdLocal=" + (bLocal ? "true" : "false") + ";</script>\n");
		if( bLocal ) {
			if( bOnScreenKey )
				div.append("<input type='hidden' id='vkeytype' value='PVPro'/>\n");
			div.append("<input type='hidden' name='fdSetAllReq' id='fdSetAllReq' value='"
					+ lang.getString("siteview","selcaract") + "'/>\n");
			div.append("<div id='divFileDialog' name='divFileDialog'");
			int x = (nScreenWidth - nWidth) / 2 + nLeftOffset;
			int y = (nScreenHeight - nHeight) / 2 + nTopOffset;
			div.append(" style='position:absolute;left:" + x + "px;width:" + nWidth + "px;top:" + y + "px;height:" + nHeight + "px;background:#FFFFFF;visibility:hidden;'>\n");
			div.append("<table class='tableFD' width='" + nWidth + "' height='" + nHeight + "' cellpadding='1' cellspacing='0'>\n");
			div.append("<tr class='trFD'>");
			div.append("<td class='tdFD' width='" + nPlaceWidth + "' valign='top'>" + renderPlaces() + "</td>");
			div.append("<td class='tdFD' width='*' valign='top'>");
			int divWidth = nWidth - nPlaceWidth;
			int divHeight = nHeight - nBottomHeight;
			// caption
			div.append("<div style='width:" + divWidth +"px;height:" + divHeight + "px' class='fdCaptionBox'>\n");			
			div.append(renderFiles());
			div.append(renderControls());
			// end captionBox
			div.append("</div>\n");			
			div.append("</td>");
			div.append("</tr>\n");
			div.append("</table>\n");
			div.append("</div>\n");
			div.append(renderTranslation());
		}
		return div.toString();
	}
	
	
	protected String renderPlaces()
	{
		StringBuffer div = new StringBuffer();	
		div.append("<div style='width:" + nPlaceWidth +"px;height:" + (nHeight-6) + "px' class='fdPlacesBox' >\n");
		div.append("<div id='fdPlaces' name='fdPlaces' style='width:" + (nPlaceWidth-2) + "px;height:" + (nHeight-8) + "px' >\n");
		div.append("<table align='center' width='98%' cellspacing='15'>\n");
		for(int i = 0; i < places.size(); i++) {
			Properties place = places.get(i);
			div.append("<tr onClick=\"onPlaceChanged(" + i + ",'" + place.getProperty("path") + "')\">");
			div.append("<td align='center'>");
			div.append("<img src='" + place.getProperty("icon") + "'>");
			div.append("<br>" + place.getProperty("name"));
			div.append("<br><input id='place" + i + "' name='place" + i + "' type='hidden' value='" + place.getProperty("path") + "'>");
			div.append("</td></tr>\n");
		}
		div.append("</table>\n");
		div.append("</div>");
		div.append("</div>\n");
		return div.toString();
	}
	
	
	protected String renderFiles()
	{
		StringBuffer div = new StringBuffer();
		int divWidth = nWidth - nPlaceWidth;
		int divHeight = nHeight - nBottomHeight;
		div.append("<div>\n");
		div.append("<table id='fdCaption' name='fdCaption' class='table' border='0' width='100%' cellspacing='2px'><tbody>\n");
		div.append("<tr class='th'>\n");
		div.append("<td width='98%' class='bbk' >FileDialog</td>\n");
		div.append("<td width='2%' class='bbk' onclick='onFileDialogCancel()' ><img src='"+CLOSE+"' /></td>\n");
		div.append("</tr>");
		div.append("</tbody></table>");
		div.append("</div>\n");
		// file list
		div.append("<div id='fdFiles' name='fdFiles' style='width:" + (divWidth-2) + "px;height:" + (divHeight-27) + "px'>\n");
		div.append("</div>");
		return div.toString();
	}

	
	protected String renderControls()
	{
		String strClass = bOnScreenKey ? "keyboardInput" : "standardTxt";
		StringBuffer div = new StringBuffer();
		div.append("<div id='fdControls'>\n");
		div.append("<table width='100%' cellspacing='0'><tr class='trFDControls'>\n");
		// file name
		div.append("<td width='80%' align='left'>&nbsp;");
		div.append(lang.getString("filedialog", "filename"));
		div.append("&nbsp;<input id='fdFileName' name='fdFileName' type='text' class='" + strClass + "' style='width:340px' onKeyPress=\"return filterFileName(event);\""
				+ /*onKeyUp='onFileNameChanged(event);*/"'></td>\n");
		// ok
		div.append("<td>\n");
		div.append("<table id='fdOk' name='fdOk' border='0' cellpadding='0' cellspacing='0' width='110px' height='30px' onClick='onFileDialogOk()' style='cursor:pointer;'>");
		div.append("<tr class='groupCategory_small'><td>" + lang.getString("filedialog", "ok") + "</td></tr>");
		div.append("</table>");
		div.append("</td>\n");
		// cancel
		div.append("<td>\n");
		div.append("<table border='0' cellpadding='0' cellspacing='0' width='110px' height='30px' onClick='onFileDialogCancel()' style='cursor:pointer;'>");
		div.append("<tr class='groupCategory_small'><td>" + lang.getString("filedialog", "cancel") + "</td></tr>");
		div.append("</td></tr>");
		div.append("</table>\n");
		div.append("</td>");
		div.append("</tr></table>");
		div.append("</div>\n");
		// network folder
		div.append("<div id='divNetworkFolder' name='divNetworkFolder'>\n");
		div.append("<table width='100%' cellspacing='0'><tr class='trFDControls'>\n");
		div.append("<td width='80%' align='left'>&nbsp;");
		div.append(lang.getString("filedialog", "netfolder"));
		div.append("&nbsp;<input id='fdNetworkFolder' name='fdNetworkFolder' type='text' class='standardTxt fdNetworkFolder'></td>\n");
		div.append("<td width='*' align='center'>");
		div.append("<table border='0' cellpadding='0' cellspacing='0' width='110px' height='30px' onClick='javascript:onNetworkFolder()' style='cursor:pointer;'>");
		//div.append("<tr class='groupCategory_small'><td><img src='" + strRefresh + "'></td></tr>");
		div.append("<tr class='groupCategory_small'><td>" + lang.getString("filedialog", "gotofolder") + "</td></tr>");
		div.append("</table>");		
		//div.append("<img src='" + strRefresh + "' onClick='onNetworkFolder()'>");
		div.append("</td>\n");
		// cancel
		div.append("<td align='right'>\n");
		div.append("<table border='0' cellpadding='0' cellspacing='0' width='110px' height='30px' onClick='onFileDialogCancel()' style='cursor:pointer;'>");
		div.append("<tr class='groupCategory_small'><td>" + lang.getString("filedialog", "cancel") + "</td></tr>");
		div.append("</td></tr>");
		div.append("</table>\n");
		div.append("</td>");
		div.append("</tr></table>\n");
		div.append("</div>\n");
		return div.toString();
	}
	

	protected String renderTranslation()
	{
		StringBuffer tran = new StringBuffer();
		tran.append("<input id='fdLoad' name='fdLoad' type='hidden' value='" + lang.getString("filedialog", "load") + "'>\n");
		tran.append("<input id='fdSave' name='fdSave' type='hidden' value='" + lang.getString("filedialog", "save") + "'>\n");
		tran.append("<input id='fdError1' name='fdError1' type='hidden' value='" + lang.getString("filedialog", "error1") + "'>\n");
		return tran.toString();
	}
	
	
	public void addPlace(String path, String name, String icon, String type)
	{
		Properties place = new Properties();
		place.setProperty("path", path);
		place.setProperty("name", name);
		place.setProperty("icon", icon);
		place.setProperty("type", type);
		places.add(place);
	}
	
	
	public void addDefaultPlaces(Properties defaultPlaces)
	{
		String place;
		/*
		String place = defaultPlaces.getProperty("USB", "F:");
		addPlace(place.replaceAll("\\\\", "/"), lang.getString("filedialog", "usb"), USB);
		place = defaultPlaces.getProperty("CFCARD", "E:");
		addPlace(place.replaceAll("\\\\", "/"), lang.getString("filedialog", "cfcard"), CFCARD);
		*/
		
		
		// add drive volumes
		File roots[] = File.listRoots();
		FileSystemView fsView = FileSystemView.getFileSystemView();
		for(int i = 0; i < roots.length; i++) {
			place = roots[i].getAbsolutePath().substring(0, 2);
			String volume = "";
			try{
				volume = fsView.getSystemDisplayName(roots[i]);
			}catch(Exception e){}
			String name = volume.length() > 0 ? volume : place;
			if(Integer.parseInt(BaseConfig.getReleaseHW().replace(".",""))>=205) // if HW version is 2.0.5 or higher 
																					   // then it has SSD. Don't hide 'D' from file dialog
			{
				if( !place.startsWith("C") )
					addPlace(place, name, DRIVE, "DRIVE");
			}
			else
			{
				if( !(place.startsWith("C") || place.startsWith("D")) )
					addPlace(place, name, DRIVE, "DRIVE");
			}
			
		}
		
		place = defaultPlaces.getProperty("FTP");
		if( place != null && place.length() > 0 )
			addPlace(place.replaceAll("\\\\", "/"), lang.getString("filedialog", "ftp"), FTP, "FTP");
		/*
		place = defaultPlaces.getProperty("NETFOLDER");
		if( place != null && place.length() > 0 )
			addPlace(place.replaceAll("\\\\", "/"), lang.getString("filedialog", "netfolder"), NETFOLDER, "NETFOLDER");
		else
			addPlace("", lang.getString("filedialog", "netfolder"), NETFOLDER, "NETFOLDER");
		*/
	}

	
	public void clearPlaces()
	{
		places.clear();
	}
	

	public String cmdResponse(String path, String filter)
	{
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" ?>\n");
		if( path.length() > 0 ) {
			xml.append("<" + FileDialogBean.CMD + " type=\"files\">");
			String filters[];
			if( filter.length() == 0 || filter.equals("*") )
				filters = new String[0];
			else
				filters = filter.split(",");
			File folder = new File(path);
			try {
				String cpath = folder.getCanonicalPath().replaceAll("\\\\", "/");
				if( cpath.endsWith("/") )
					cpath = cpath.substring(0, cpath.length()-1);
				xml.append("<path name=\"" + cpath + "\"/>");
			} catch(IOException e) {
				LoggerMgr.getLogger(FileDialogBean.class).error(e);
			}
			
			File files[] = folder.listFiles();
			if( files != null ) {
				for(int i = 0; i < files.length; i++) {
					if( files[i].isFile() ) {
						if( filters.length == 0 )
							xml.append("<file name=\"" + files[i].getName() + "\"/>");
						else for(int k = 0; k < filters.length; k++) {
							String name = files[i].getName().toLowerCase();
							if( name.endsWith("." + filters[k]) ) {
								xml.append("<file name=\"" + files[i].getName() + "\"/>");
								break;
							}
						}
							
					}
					else if( files[i].isDirectory() ) {
						xml.append("<folder name=\"" + files[i].getName() + "\"");
						xml.append("/>");
					}
				}
			}
			else {
				xml.append("<error code=\"1\"/>");
			}
		}
		else {
			addDefaultPlaces(BaseConfig.getFilePlaces());
			xml.append("<" + FileDialogBean.CMD + " type=\"places\">");
			for(int i = 0; i < places.size(); i++) {
				Properties place = places.get(i);
				xml.append("<place name=\"" + place.getProperty("name"));
				xml.append("\" path=\"" + place.getProperty("path"));
				xml.append("\" icon=\"" + place.getProperty("icon"));
				xml.append("\" type=\"" + place.getProperty("type"));
				xml.append("\"/>");
			}
			
		}
		xml.append("</" + FileDialogBean.CMD + ">");
		return xml.toString();
	}
	
 
	public static String[] getFiles(String strPath, String strExt)
	{
		File folder = new File(strPath);
		File files[] = folder.listFiles();
		String astrFiles[] = null;
		if( files != null ) {
			StringBuffer strbufFiles = new StringBuffer();
			for(int i = 0, j = 0; i < files.length; i++) {
				if( files[i].isFile() ) {
					if( files[i].getName().indexOf(strExt) == (files[i].getName().length() - strExt.length()) ) {
						if( j > 0 )
							strbufFiles.append(",");
						strbufFiles.append(files[i].getName());
						j++;
					}
				}
			}
			astrFiles = strbufFiles.toString().split(",");
		}
		return astrFiles;
	}

	
	// page input handlers
	public String inputLoadFile(String id, String filter)
	{
		return inputFile(true, id, filter, "", false);
	}

	public String inputLoadFile(String id, String filter, boolean bDisabled)
	{
		return inputFile(true, id, filter, "", bDisabled);
	}
	
	public String inputLoadFile(String id, String filter, String extra)
	{
		return inputFile(true, id, filter, extra, false);
	}
	
	public String inputLoadFile(String id, String filter, String extra, boolean bDisabled)
	{
		return inputFile(true, id, filter, extra, bDisabled);
	}
	
	public String inputLoadFileForProfile()
	{
		String result ="";
		if(! bLocal){
			String type = "file" ;
/*          bugfix for UI , testcase id 7303 : import profile which is not 'system Administrator' profile  
			if(overwriteProf!=null && overwriteProf.equalsIgnoreCase("yes") ){
				type = "hidden";
			}
*/
			result =" <input type='"+type+"' size='30%' class='mybutton' id='importconf' name='importconf' value='' />" +
					" <input type='hidden' id='overwrite' name='overwrite' value='' />"+
					" <input type='hidden' name='tipofile' value='profconfig' /> ";
		}else{
			result = " <input type='hidden' name='tipofile' value='profconfig' /> "+
					 " <input type='hidden' id='importconf' name='importconf' onchange='profupload();' /> "+
					 " <input type='hidden' id='fdField' name='fdField' value='importconf' /> "+
					 " <input type='hidden' id='overwrite' name='overwrite' value=''  />";
		}
		return result;
	}
	
	public String inputSaveFile(String id, String filter)
	{
		return inputFile(false, id, filter, "", false);
	}
	
	public String inputSaveFile(String id, String filter, boolean bDisabled)
	{
		return inputFile(false, id, filter, "", bDisabled);
	}
	
	public String inputSaveFile(String id, String filter, String extra)
	{
		return inputFile(false, id, filter, extra, false);
	}
	
	public String inputSaveFile(String id, String filter, String extra, boolean bDisabled)
	{
		return inputFile(false, id, filter, extra, bDisabled,null);
	}
	public String inputSaveFile(String id, String filter, String extra, boolean bDisabled,String defaultName)
	{
		return inputFile(false, id, filter, extra, bDisabled,defaultName);
	}
	protected String inputFile(boolean bLoad, String id, String filter, String extra, boolean bDisabled)
	{
		return inputFile(bLoad, id, filter, extra, bDisabled,null);
	}
	protected String inputFile(boolean bLoad, String id, String filter, String extra, boolean bDisabled,String defaultName)
	{
		StringBuffer inputField = new StringBuffer();
		
		if( bLocal ) {
			inputField.append("<table cellspacing='0' cellpading='0'><tr valign='middle'><td align='right'>");
			inputField.append("<input type=\"text\" readonly class=\"standardTxt\"");
			inputField.append(" id=\"" + id + "\"");
			inputField.append(" name=\"" + id + "\"");
			if( extra.length() > 0 )
				inputField.append(" " + extra);
			if( bDisabled )
				inputField.append(" disabled");
			inputField.append("><input type=\"hidden\" id=\"fdField\" name=\"fdField\" value=\"" + id + "\">");
			inputField.append("</td><td align='left'>");
			if( bDisabled ) {
				inputField.append("<img id=\""+id+"img\" src=\"" + (bLoad ? strBrowseOff : strSaveOff) + "\"");
				inputField.append(">");
			}
			else {
				inputField.append("<img id=\""+id+"img\" src=\"" + (bLoad ? strBrowseOn : strSaveOn) + "\"");
				if(defaultName != null && defaultName.length()>0)
				{
					inputField.append(" onClick=\"onFileDialog(" + bLoad + ",'" + id + "','" + filter + "');fdSetFile('"+defaultName+"');\"");
				}
				else
				{
					inputField.append(" onClick=\"onFileDialog(" + bLoad + ",'" + id + "','" + filter + "')\"");
				}
				inputField.append(">");
			}
			inputField.append("</td></tr></table>");
		}
		else if( bLoad ) {
			inputField.append("<input type=\"file\" class=\"mybutton\"");
			inputField.append(" id=\"" + id + "\"");
			inputField.append(" name=\"" + id + "\"");
			if( extra.length() > 0 )
				inputField.append(" " + extra);
			if( bDisabled )
				inputField.append(" disabled");
			inputField.append(">");
		}
		
		return inputField.toString();
	}
	
	
	// form encoding type
	public String enctype()
	{
		if( bLocal )
			return "application/x-www-form-urlencoded";
		else
			return "multipart/form-data";
	}
}
