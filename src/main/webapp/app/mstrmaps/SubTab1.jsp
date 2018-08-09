<%@page import="com.carel.supervisor.presentation.svgmaps.SvgMapsUtils"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.director.packet.PacketMgr"%>
<%@ page language="java" pageEncoding="UTF-8"
import="java.io.*"
import="java.util.Collection"
import="java.util.Iterator"
import="org.apache.commons.io.FileUtils"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.bo.master.IMaster"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
import="com.carel.supervisor.presentation.menu.configuration.MenuTabMgr"
import="com.carel.supervisor.presentation.devices.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.base.io.Unzipper"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.svgmaps.SvgMapsTranslator"
%>
<%
		
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUser = sessionUser.getCurrentUserTransaction();
	String jsession = request.getSession().getId();
	String pathMap = trxUser.getProperty("pathmap"); //pagina IDE custom
	String session_lang = sessionUser.getLanguage();
	String lang_code = session_lang.split("_")[1];
	String lang_request = "en";
	
	LangService lan = LangMgr.getInstance().getLangService(session_lang); 
	
	
	if((pathMap == null) || (pathMap == ""))
		pathMap = sessionUser.getProperty("layedmap"); //pagina da LayoutEditor x preview
	else
		pathMap = "mstrmaps/" + pathMap; //chiamata a pagina custom
	
	if((pathMap==null)||(pathMap==""))
		pathMap="mstrmaps/1.jsp"; //pagina iniziale di default

	String pageLoad= "../../app/"+pathMap; //path finale da caricare
	
	String pageFileCheck = BaseConfig.getAppHome()+File.separator+"app"+File.separator+pathMap;	
	String device_access = sessionUser.getPropertyAndRemove("deviceaccess");
	
	
	boolean exist = new File(pageFileCheck).exists();  //check if file exist
	boolean svgMapInstalled = false;
	boolean svgmaps_activated = PacketMgr.getInstance().isFunctionAllowed("svgmaps");
	
	// Per generazione combo in dettaglio
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	sessionUser.getTransaction().setIdDevicesCombo(ids);
%>
 
<head>
    <base id="basePath" href="<%=basePath%>">
</head> 

<script type="text/javascript" src="scripts/app/keyboard.js"></script> 
<input type='hidden' id='dev_access' value='<%=device_access%>' />

<%if (exist) { 
%>
<jsp:include page="<%=pageLoad%>" flush="true" />
<% }  
else
{
	try {
		
		//File svgMapsScriptFile = new File(SvgMapsUtils.getSvgMapsRoot() +"/webmi.js");
		// Are there the SVG maps into mstrmaps?
		if (SvgMapsUtils.checkSvgMaps()) {
			
			// check if the files have been compressed (gzipped) by atvise builder. If so, decompress them before loading
			SvgMapsUtils.unzipMaps(SvgMapsUtils.getSvgMapsRoot());
			/*
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
	        	String dir  = BaseConfig.getAppHome()+"/app/mstrmaps/svgmaps/";
	        	Collection<File> filesToGunzip = FileUtils.listFiles(new File(dir), GZIP_FILES_FILTER, true);
		    	for (Iterator iterator = filesToGunzip.iterator(); iterator.hasNext();) {
					File file = (File) iterator.next();
					Unzipper.gunzipSameFile(file);
				}
	    	}
	    	
	    	*/
	    	/* else   // debug only
	        	System.out.println("Not a gzip file");  
	    	 */
			// we edit the JS file to be 100% sure that it doesn't contain the ATVISE context root "webMI/?". May be a redundant operation
			// but better be on the safe side.
		    
			/*
			String content = FileUtils.readFileToString(svgMapsScriptFile, "UTF-8");
		    content = content.replaceAll("\"/webMI/\\?", "parent.document.getElementById('basePath').href+\"servlet/svgmaps?");
		    File tempFile = new File(SvgMapsUtils.getSvgMapsRoot()+"/webmi.js");
		    FileUtils.writeStringToFile(tempFile, content, "UTF-8");
		    */
		    SvgMapsUtils.modifyWebMIContext(SvgMapsUtils.getSvgMapsRoot() +"/webmi.js");
		    
		    svgMapInstalled = true;
		    
		    // verify if Atvise pages are translated in the language on session. If not, use EN by default.
		    if (SvgMapsUtils.checkLanguageTranslation(lang_code))
		    		lang_request =  lang_code;		
		}
	 } catch (Exception e) {
		 e.printStackTrace();

	 }
	
	if (svgMapInstalled)
    {
		if (svgmaps_activated)
	 	{ // we include the SVG maps only if they're actually there. %> 
	    	<iframe id="map" src="./app/mstrmaps/svgmaps/index.htm?language=<%=lang_request%>&autofit=true&displaytype=svg&scrolling=no" width="100%" height="100%" frameborder="0" ></iframe>
   	  <%}
		else{%>
	 		<div><p class="standardTxt" style="text-align:center;"><%=lan.getString("mstrmaps","pluginrequired")%></p></div>
  	  <%}
	}
}
%>
 