package com.carel.supervisor.presentation.svgmaps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.controller.setfield.DefaultCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class SvgMapsTranslator
{
	private static ConcurrentHashMap<String,String[]> subscribedVarMap = new ConcurrentHashMap<String, String[]>(); // maps containing the subscribed variables. KEY: SESSIONID-SUBSCRIPTIONID | VALUE: ADDRESSES_ARRAY
	private static ConcurrentHashMap<String,String[]> postponedPublishMap = new ConcurrentHashMap<String, String[]>(); //maps containing the postponed publish values (due to the 'maxresults' excess on 'publish' method). KEY: SESSIONID | VALUE: ADDRESSES_ARRAY
	private static ConcurrentHashMap<String, Float> readValuesMap = new ConcurrentHashMap<String, Float>(); // maps containing the values read (of the subscribed variables). KEY: SESSIONID-SUBSCRIPTIONID-ADDRESS | VALUE: VAR_VALUE
	private static ConcurrentHashMap<String, Long> aliveSessionsMap = new ConcurrentHashMap<String, Long>(); // maps containing the alive sessions. KEY: SESSIONID | KEY: timestamps (long - milliseconds)
		
	private static List<String> newSessionSubscrList = new ArrayList<String>(); // list containing the sessionIDs refered to the new subscriptions 
																				// it is used to interrupt the 'longpoll' mechanism in case of new subscriptions (to enhance the page reactivity)
	private static int nextSubscriptionID = 0;
	private static int nextSessionID = 0;
	
	
	private static String AUTHORIZE_REQUEST = "authorize";
	private static String INFO_REQUEST = "info";
	private static String READ_REQUEST = "read";
	private static String WRITE_REQUEST = "write";
	private static String CREATESESSION_REQUEST = "createsession";
	private static String DELETESESSION_REQUEST = "deletesession";
	private static String CREATESUBSCRIPTION_REQUEST = "createsubscription";
	private static String DELETESUBSCRIPTION_REQUEST = "deletesubscription";
	private static String SUBSCRIBEDATA_REQUEST = "subscribedata";
	private static String UNSUBSCRIBEDATA_REQUEST = "unsubscribedata";
	private static String PUBLISH_REQUEST = "publish";
	
	
	
	
	// ***************  ALGORITHM PARAMETERS *****************

	// SLEEP time used to check if a new subscription has come; every "SLEEPTIME" are executed N SLEEP_TIME_SLICE
	private static int SLEEP_TIME_SLICE = BaseConfig.getProductInfo("svgmaps_sleep_timeslice")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmaps_sleep_timeslice")):500; //milliseconds
	// MIN time used to give a response to 'publish' and 'read' requests when there are not subscribed variables (or PVPRO is running in DEMO mode)
	private static int MIN_PUBLISH_DELAY =  BaseConfig.getProductInfo("svgmaps_min_publish_delay")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmaps_min_publish_delay")):2000; //milliseconds -- 2 sec
	// TIMEOUT used for longpolling
	private static int LONGPOLLTIMEOUT = BaseConfig.getProductInfo("svgmaps_longpoll_timeout")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmaps_longpoll_timeout")):60000; // milliseconds -- 60 sec
	// SLEEP time used to check if there are changed data from field
	private static int SLEEPTIME = BaseConfig.getProductInfo("svgmaps_longpoll_sleep")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmaps_longpoll_sleep")):10000; // milliseconds -- 10 sec
	// TIMEOUT used to check if the session is still alive
	private static int SESSIONTIMEOUT = BaseConfig.getProductInfo("svgmaps_session_timeout")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmaps_session_timeout")):300000; //milliseconds -- 5 min
	// FREQUENCY used to check if session is alive or not
	private static int SESSIONFREQCHECK = BaseConfig.getProductInfo("svgmapg_session_checkfreq")!=null?Integer.parseInt(BaseConfig.getProductInfo("svgmapg_session_checkfreq")):60000; //milliseconds -- 60 sec
	// FLAG that indicates if session check mechanism is enabled or not 
	private static boolean SESSIONCHECK_ENABLED = BaseConfig.getProductInfo("svgmaps_session_enabcheck")!=null?(BaseConfig.getProductInfo("svgmaps_session_enabcheck").equals("yes")?true:false):false;
	// ********************************************************
	
	
	private static SessionCheckerThread sessionChecker = null; // thread that cleans up the subscribedVarMap in case of the client interrupts the communication (e.g. browser closed)
	
	public static String manageJsonRequest(HttpServletRequest request) throws IOException, ServletException
	{
		
		UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		
		Integer iddev = null;
		
		if (userSession!=null)
			iddev = userSession.getProperty("iddev")==null?null:Integer.valueOf(userSession.getProperty("iddev"));
				
		Gson gson = new Gson();
		
		Map request_params = request.getParameterMap();
		String jsonresp = "";
		
		// retrive SessionID from the request
		String xwebmiheader = request.getHeader("x-webmi");
		
		Properties p = new Properties();
		String currSessionID = "";
		
		if(xwebmiheader!=null)
		{
			String newstr = xwebmiheader.replace(",","\n");
			p.load(new StringReader(newstr));
	    	currSessionID = p.getProperty("sessionid").replace("\"", "");
		}
	    
		/*************************************************************
 		 * 				BASIC LEVEL (METHODS ABOVE)
 		 *************************************************************/
		
		if(request_params.containsKey(AUTHORIZE_REQUEST))
    	{
         	Map<String,String> responsemap = new HashMap<String,String>();
    		
         	//verify plugin SVG Maps registration
         	if (PacketMgr.getInstance().isFunctionAllowed("svgmaps"))
         	{
	    		String param = request.getParameter("data");
	    		// FIRST time, Authorize is without DATA, and server return the license.cfg content; the second Authorize contains "data", and server
	    		//    return the string modified using the encripted algorithm
	    		if(param!=null)    
	    		{
	    			String result=webmi_authorize(param);
	    			responsemap.put("result", result);
	    		}
	    		else
	    		{
	    			BufferedReader br = null;
		     		String licensecfg="";
		     		 
		    		try 
		    		{
		    			br = new BufferedReader(new FileReader(SvgMapsUtils.getLicensePath(userSession)));
		    			
		    			licensecfg = br.readLine();
		    		} 
		    		catch (IOException e) {
		    			e.printStackTrace();
		    		} 
		    		finally
		    		{
		    			try {
		    				if (br != null)br.close();
		    			} catch (IOException ex) {
		    				ex.printStackTrace();
		    			}
		    		}
		    		
		    		responsemap.put("result", licensecfg);
	    		}
         	}
         	else
         		responsemap.put("result", "");
			
         	jsonresp = gson.toJson(responsemap);
         	
         	return jsonresp;
    	}
		 
		if(request_params.containsKey(INFO_REQUEST))
        {
			Map<String,Object> responsemap = new HashMap<String,Object>();
			responsemap.put("certificationlevel", "advanced");      	
			responsemap.put("maxtimeout", LONGPOLLTIMEOUT*2);    //according webmi specification
       	 	responsemap.put("vendorinfo", "CAREL Industries S.p.A.");
       	 	
        	// OPTIONAL FIELDS
       	 	responsemap.put("loginmethod", "none");
       	 	responsemap.put("longpollsupport", true); // TODO: longpoll is always enabled; insert parameter on productinfo table
       	 	/*
        	responsemap.put("securitysupport", "false");
			responsemap.put("encryptionmodulus", "E5DFF083AE5027DB708F399AAAE932E31FAE86125614C31F562D166BDD3033D7565658DC0627968FD1B112A296208ABD5F0109C737ADACFBC597D45C962E05B2E2E278A51D411562619947F95AE4414BFD37C7E9B28C936BF28B5908E39D9D646A29EDA117A562D7F8851EE85BC95688202FC6569D530329EC2DB85EDE24475D");
        	responsemap.put("encryptionexponent", "010001");
        	*/
        	
        	jsonresp = gson.toJson(responsemap);
        	
        	return jsonresp;
        }
		
		if(request_params.containsKey(READ_REQUEST))
    	{
    		String [] tmp_array = request.getParameterValues("address[]");
 	 	      	
    		JsonArray results = new JsonArray();
    		Map<String,Object> result = null;
    		 
    		if (tmp_array!=null)
    		{
	    		try
	    		{
	    			ArrayList <String>tmp_list  = new ArrayList<String>((Arrays.asList(request.getParameterValues("address[]"))));
	    			Variable[] vars = getVariable(tmp_list,iddev);
	    			
	    			for (int i=0; i<tmp_list.size();i++)
		    		{	
		    			// variable is given as: LINENUM.DEVADDR.DEVDESCR.VARTYPE.VARDESCR | VARCODE
						// e.g. 5.001.MPXPRO - 1.Analogs.Backup sonda seriale 11 | s_BackupSerProbe11
						// or could be given as: LINENUM.DEVADDR.DEVDESCR.VARDESCR | VARCODE
						// e.g.5.001.MPXPRO - 1.Backup sonda seriale 11 | s_BackupSerProbe11
		    				
	    				result = new HashMap<String,Object>();
	    				
	    				Variable var = vars[i];
	       				float val = var.getCurrentValue();
	
	       				if(Float.isNaN(val))
		            		 result.put("value", "");
		            	 else
		            	 {
		       				//type DIGITAL
			            	if (var.getInfo().getType()==1)
			            	 	result.put("value", val==1?true:false);
			            	//type ANALOG or INTEGER
			            	else  
			            		result.put("value", val);
		            	 }
	       				
		        		 result.put("timestamp", Long.toString(System.currentTimeMillis())); //String?? (defined as 'Number' on documentation)
		        		 result.put("status", 0);
			        		
		        		 // OPTIONAL FIELDS
		        		 /*result.put("description", "DESC"); // description of the data variable
		        		 result.put("username", ""); // the name of the user who set data variable
		         		 result.put("errorstring", ""); // a description of the error
		        		 */
			        		
		        		result.put("error", 0);
			 
		        		results.add(gson.toJsonTree(result));
		    		}
	    		}catch (Exception e)
				{
					LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
				}
    			
    		}
    		    			
    		Map<String,JsonArray> output = new HashMap<String,JsonArray>();
	      	output.put("result", results);
	      	
	      	jsonresp = gson.toJson(output);
	      	
	      	return jsonresp;
	    }
    	
		if(request_params.containsKey(WRITE_REQUEST))
    	{
			ServletHelper.validateSession(userSession); //updates session timestamp; in case of write operation performed by the user, the session is kept alive
			
			boolean write_permission = false;
			if(userSession!=null)
				write_permission = userSession.isButtonActive("dtlview","tab1name","subtab2name");
			
			if(request_params.containsKey("address[]") && request_params.containsKey("value[]"))
    		{
    			//String address = null;
    			String s_value = null;
    			ArrayList <String>address_list  = new ArrayList<String>((Arrays.asList(request.getParameterValues("address[]"))));
    			String [] value_array = request.getParameterValues("value[]");
			 
    			JsonArray results = new JsonArray();
    			Map<String,Object> result = new HashMap<String,Object>();
    			
    			if (write_permission)  //write variable only if user have the write permission (same logic of Layout Editor management)
    			{
	    			if(address_list.size()!=0)
	    			{
	    				try
	    				{
		    				Variable [] vars = getVariable(address_list,iddev);
		    				
		    				for (int i=0; i<vars.length;i++)
		    				{
		    					//address = address_list.get(i);  
		    					s_value = value_array[i];
		    					
		    					// variable is given as: LINENUM.DEVADDR.DEVDESCR.VARTYPE.VARDESCR | VARCODE
		       				 	// e.g. 5.001.MPXPRO - 1.Analogs.Backup sonda seriale 11 | s_BackupSerProbe11
		       				 	// or could be given as: LINENUM.DEVADDR.DEVDESCR.VARDESCR | VARCODE
		       				 	// e.g.5.001.MPXPRO - 1.Backup sonda seriale 11 | s_BackupSerProbe11

	    						Variable var = vars[i];
	    					
	    					 	float val = Float.parseFloat(s_value);
	   						 	  		        			 	
	   		    				SetContext setContext = new SetContext();
	   		    				String lang = userSession.getLanguage();
	   		    				
	   		    				setContext.setLanguagecode(lang);
	   		    			    setContext.setCallback(new DefaultCallBack());
	   		    			    setContext.setUser(userSession.getUserName());
	   		    					
	   		    				setContext.addVariable(var, val);
	   		    				SetDequeuerMgr.getInstance().add(setContext);

		    					
		    					//result = new HashMap<String,Object>();    	
		    					result.put("error", 0);
		    					
		    					// TODO: manage 'errorstring' in case of error
		    					// result.put("errorstring", "error");
		    					
		    					results.add(gson.toJsonTree(result));
		    				}
		    			}
	    				catch (Exception e)			    	
    					{
    						LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
    					}
	    			}
    			}
    			else
    			{
    				result.put("error", 503);
    				result.put("errorstring", "R/W Permission Missing");
    				results.add(gson.toJsonTree(result));
    			}	
    				
    			Map<String,JsonArray> output = new HashMap<String,JsonArray>();
    			output.put("result", results);
    			jsonresp = gson.toJson(output);
    		}
    		
    		return jsonresp;
    	}
    	 
    	/*************************************************************
 		 * 				ADVANCED LEVEL (METHODS ABOVE)
 		 *************************************************************/
    	
		if(request_params.containsKey(CREATESESSION_REQUEST))
		{
			// DEBUG
			//LoggerMgr.getRootLogger().info("CREATESSION sessionID: " + currSessionID);
			
			// Sessions are used to give the correct answers to the corresponding client (e.g. 'publish' coming from different clients)
    		// TODO: (OPTIONAL) manage the timeout parameter. Server has to delete the session after the 'timeout'
    		Map responsemap = new HashMap();
	        responsemap.put("sessionid", nextSessionID);
	        jsonresp = gson.toJson(responsemap);
	        
	        nextSessionID++;
		
	        return jsonresp;
		}
    	
		if(request_params.containsKey(DELETESESSION_REQUEST))
		{
			// DEBUG
			// LoggerMgr.getRootLogger().info("DELETESESSION sessionID: " + currSessionID);
			
			// remove items from the subscribedVarMap
			Set<String> keys = subscribedVarMap.keySet();
    		Object[] keysArray = keys.toArray();
    		for(int i=0; i<keysArray.length; i++)
			{
    			if(((String)keysArray[i]).startsWith(currSessionID+"-"))
    				subscribedVarMap.remove((String)keysArray[i]);
    		}
    		
    		// stop alive session checker when there are not subscribed variables
    		if(SESSIONCHECK_ENABLED && subscribedVarMap.keySet().size()==0 && sessionChecker!=null)
    			sessionChecker.stopChecker();
    		
    		// remove items (related to the sessionID) from the readValuesMap
    		removeCachedValsBySessionID(currSessionID);
    		    		
    		// TODO: manage errors and error codes return
    		Map<String,JsonElement> output = new HashMap<String,JsonElement>();
          	output.put("error", gson.toJsonTree(0));
          	jsonresp = gson.toJson(output);
          	
          	return jsonresp;
		}
    	
		if(request_params.containsKey(CREATESUBSCRIPTION_REQUEST))
    	{
			// DEBUG
			//LoggerMgr.getRootLogger().info("CREATESUBSCRIPTION sessionID: " + currSessionID);
			
			// subscription IDs are given in a unique sequential series
    		// even if it is required from different sessions
			// NOTE: subscriptionid=0 is used for the user access rights ('publish' response)
    		nextSubscriptionID ++;
    		Map<String,String> responsemap = new HashMap<String,String>();
          	responsemap.put("subscriptionid", String.valueOf(nextSubscriptionID));
          	jsonresp = gson.toJson(responsemap);
     	
          	return jsonresp;
    	}
    	 
		if(request_params.containsKey(DELETESUBSCRIPTION_REQUEST))
    	{
			// DEBUG
			//LoggerMgr.getRootLogger().info("DELETESUBSCRIPTION sessionID: " + currSessionID);
			
			String subID = request.getParameter("subscriptionid");
    		String currSessIDsubID = currSessionID+"-"+subID;
    		
    		// remove items from the subscribedVarMap
    		subscribedVarMap.remove(currSessIDsubID);
    		
    		// stop alive session checker when there are not subscribed variables
    		if(SESSIONCHECK_ENABLED && subscribedVarMap.keySet().size()==0 && sessionChecker!=null)
    			sessionChecker.stopChecker();
    		
    		// remove items from the readValuesMap
    		Set<String> keys = readValuesMap.keySet();
    		Object[] keysArray = keys.toArray();

    		for(int i=0; i<keysArray.length; i++)
    		{
    			if(((String)keysArray[i]).startsWith(currSessIDsubID+"-"))
    				readValuesMap.remove(((String)keysArray[i]));
    		}
    		
    		Map<String,JsonElement> output = new HashMap<String,JsonElement>();
          	output.put("error", gson.toJsonTree(0));
          	jsonresp = gson.toJson(output);
    		
          	return jsonresp;
    	}
		
		if(request_params.containsKey(SUBSCRIBEDATA_REQUEST))
    	{
			
			// DEBUG
			//LoggerMgr.getRootLogger().info("SUBSCRIBEDATA sessionID: " + currSessionID); 
			
			String subID = request.getParameter("subscriptionid");
    		String [] addresses = request.getParameterValues("address[]");
    		 
    		String currSessIDsubID = currSessionID+"-"+subID;
    		 
    		// notify that there is a new SUBSCRIPTION for the current session (currSessionID)
    		newSessionSubscrList.add(currSessionID);
    		 
	      	if(!subscribedVarMap.containsKey(currSessIDsubID))
	      		subscribedVarMap.put(currSessIDsubID, addresses);
	      	else   //opening a pop-up may happen that is used the same sessionId-subscrId, adding some addresses to the existing ones
	      		   // in this case, is performed a MERGE between old and new addresses	
	      	{
	      		 
	      		String [] savedAddresses = subscribedVarMap.get(currSessIDsubID);
	      		ArrayList <String>savedList = new ArrayList<String>(Arrays.asList(savedAddresses));
	      		ArrayList <String>newList = new ArrayList<String>(Arrays.asList(addresses));
	      		
	      		for(int i = 0; i<newList.size(); i++)
	      		{
	      			String addr = (String)newList.get(i);
	      			if(!savedList.contains(addr))
	      				savedList.add(addr);
	      		}
	      		
	      		
	      		String [] newAddresses = new String [savedList.size()];
	      		newAddresses = savedList.toArray(newAddresses);
	      		subscribedVarMap.put(currSessIDsubID, newAddresses);
	      		
	      		LoggerMgr.getLogger(SvgMapsTranslator.class).info("MANAGED MORE THAN ONE SUBSCRIBEDATA WITH THE SAME SESSIONID-SUBSCRIPTIONID");
	      	}
	      	 
	      	if(SESSIONCHECK_ENABLED)
	      	{
	      		// start sessionChecker if it is not running
	      		aliveSessionsMap.put(currSessionID, System.currentTimeMillis());
		      	
		      	if(sessionChecker == null || !sessionChecker.isAlive())
		      	{
		      		sessionChecker = new SessionCheckerThread();
		      		sessionChecker.setName("SVGMaps_sessionChecker");
		      		sessionChecker.start();
		      	}
	      	}
	      	
	      	Map<String,JsonElement> output = new HashMap<String,JsonElement>();
      		JsonArray jsarray = new JsonArray();
	      	Map<String,JsonElement> errors = new HashMap<String,JsonElement>();
	      	errors.put("error", gson.toJsonTree(0));
	      	for(int i=0; i<addresses.length; i++)
      			jsarray.add(gson.toJsonTree(errors));
      		
      		output.put("result", gson.toJsonTree(jsarray));
      		jsonresp = gson.toJson(output);
      		return jsonresp;
    	}

		if(request_params.containsKey(UNSUBSCRIBEDATA_REQUEST))
    	{ 
    		 String subID = request.getParameter("subscriptionid");
    		 String [] addrToRemove =  request.getParameterValues("address[]");
    		 
    		 String currSessIDsubID = currSessionID+"-"+subID;
    		 
    		 if(subscribedVarMap.containsKey(currSessIDsubID))
    		 {
    			 List<String> subscribedAddrListr = new LinkedList<String>(Arrays.asList(subscribedVarMap.get(currSessIDsubID)));
    			 List<String> addrToRemoveList = Arrays.asList(addrToRemove);
        		 
        		 subscribedAddrListr.removeAll(addrToRemoveList);
        		 
        		 // remove from the 'values cache' the unsubscribed variables
        		 for(int i=0; i<addrToRemoveList.size(); i++)
        			 readValuesMap.remove(currSessIDsubID+"-"+addrToRemoveList.get(i));
        		 
        		 Object [] objArrays = subscribedAddrListr.toArray();
        		 String [] remainingAddr = Arrays.copyOf(objArrays,objArrays.length,String[].class);
        		 
        		 if(remainingAddr.length!=0)
        			 subscribedVarMap.put(currSessIDsubID, remainingAddr);
        		 else
        			 subscribedVarMap.remove(currSessIDsubID);
    		}
    		 
    		// stop alive session checker when there are not subscribed variables
    		 if(SESSIONCHECK_ENABLED && subscribedVarMap.keySet().size()==0 && sessionChecker!=null)
     			sessionChecker.stopChecker();

    		JsonArray results = new JsonArray();
    		Map<String,Object> result = null;

    		// TODO: manage errors and error codes return
    		for(int i=0; i<addrToRemove.length; i++)
    		{
    			result = new HashMap<String, Object>();
    			result.put("error", 0);
    			results.add(gson.toJsonTree(result));
    		}
    		
	      	Map<String,JsonElement> output = new HashMap<String,JsonElement>();
          	output.put("result", results);      
          	output.put("error", gson.toJsonTree(0));
          	jsonresp = gson.toJson(output);
          	
          	return jsonresp;
    	}
    	 
    	 if(request_params.containsKey(PUBLISH_REQUEST))
    	 {   		 
    		 
    		// DEBUG
 			// LoggerMgr.getRootLogger().info("PUBLISH sessionID: " + currSessionID);
    		 
    		 JsonArray resultsArray = new JsonArray();
 	      	 Map<String,Object> result = new HashMap<String, Object>();
    		 
 	      	 long startTime = System.currentTimeMillis();
 	      	 long passedTime = 0;   		 
    		 
    		 int maxResults = 0; // "maxresults" parameter comes from the 'publish' request (but it isn't implemented from client side yet)
    		 					 // maxresults==0 --> the limit is not considered and every publish contains any number of variables
    		 
    		 int givenResults = 0;
    		 int remaining = 0;
    		 
    		 boolean emptySubscription = true;
    		 
    		 String tmpStr = request.getParameter("maxresults");
    		 ArrayList<String> remainingVarList = new ArrayList<String>();
    		 
    		 if(tmpStr!=null && !tmpStr.equals(""))
    			 maxResults = Integer.parseInt(tmpStr);
    		
    		 if(SESSIONCHECK_ENABLED) //update timestamp in the aliveSessionMap if the sessionChecker is enabled
    			 aliveSessionsMap.put(currSessionID, startTime);
    		 
    		 if(subscribedVarMap.keySet().size()!=0) // skip all operations if there are not subscribed variables
    			 									 // NOTE: 'PUBLISH' requests are used from the client also to verify the connection 
    			 									 // (so PUBLISH are sent also if there are not subscribed variables)
    		 {
    			 do  // repeat read of the subscribed variables 
        			 // when the values remains unchanged
        			 // the loop runs for max MAXTIMEOUT/2 milliseconds (longpolling, see atvise documentation)
        		 {
    				 Iterator<String> it = subscribedVarMap.keySet().iterator();
    	    		 
    	    		 //If postponedPublishMap is not empty, it's used the iterator on this map instead of subscribedVarMap.
    				 //When postponedPublishMap is empty, restart the standard iteration on subscribedVarMap
    				 if(postponedPublishMap.containsKey(currSessionID))
    	    			 it = postponedPublishMap.keySet().iterator();
    	    		 
        			 while (it.hasNext())   //cicle on subscription of required session
    	    		 {
    	    			 String subscribedVarKey = it.next();
    	    			 String subID = subscribedVarKey.substring(subscribedVarKey.indexOf("-")+1);
    	    			 
    	    			 if(!subscribedVarKey.startsWith(currSessionID)) //consider only the subscriptions belonging to the current session
    	    				 continue;						// it works for both maps: subscribedVarMap and postPublishMap (for both maps the 'key' content starts with the sessionID value)
    	    			 else
    	    				 emptySubscription = false;  //used to skip delay(empty publish management)
    	    			 
    	    			 ArrayList <String>addresses = null;
    	    			 
    	    			 //keep addresses from the right map (postponed or subscribed)
    	    			 if(postponedPublishMap.containsKey(currSessionID))
    	    				 addresses =  new ArrayList<String>(Arrays.asList(postponedPublishMap.get(subscribedVarKey)));
    	    			 else
    	    				 addresses  = new ArrayList<String>(Arrays.asList(subscribedVarMap.get(subscribedVarKey)));
    	    			 		 
    	    			 String address = null;
    	    			 
    	    			 int i = 0;
    	    			 int addresses_size = 0;
    	    			 try
    	    			 {
    	    				 addresses_size = addresses.size();
	    	    			 Variable[] vars = getVariable(addresses,iddev); // This method translates the "addresses" array into a "Variables" array
	    	    			 										   // All wrong addresses are ingored and REMOVED from the "addresses" array
	    	    			 										   // so "addresses" array could result MODFIED after the method execution
	    	    			 
	    	    			 if (addresses_size!=addresses.size())   // if some addresses have been removed, reset subscribedVarMap with the existing address
	    	    			 {
	    	    				 subscribedVarMap.put(subscribedVarKey, (addresses.toArray(new String[addresses.size()])));
	    	    			 
	    	    				 if(addresses.size()==0)
	    	    				 {
	    	    					 newSessionSubscrList.remove(currSessionID); // remove the 'new SUBSCRIBEDATA' notification from the list
	    	    				 }	
	    	    			 }
	    	    			 
	    	    			 for (;i<addresses.size() && (maxResults==0 || givenResults<maxResults);i++) // maxresults==0 --> the limit is not considered and every publish contains any number of variables
	    	    			 {
	    	    				 
	    	    				 // variable is given as: LINENUM.DEVADDR.DEVDESCR.VARTYPE.VARDESCR | VARCODE
	    	    				 // e.g. 5.001.MPXPRO - 1.Analogs.Backup sonda seriale 11 | s_BackupSerProbe11
	    	    				 // or could be given as: LINENUM.DEVADDR.DEVDESCR.VARDESCR | VARCODE
	    	    				 // e.g.5.001.MPXPRO - 1.Backup sonda seriale 11 | s_BackupSerProbe11

    	    					 Variable var = vars[i];
    	    					 address=addresses.get(i);
    	    					 float val = var.getCurrentValue();
    	    				 	 String chachedVarKey = currSessionID + "-" + subID + "-" + address;
    	    					 // check variable value variation
    	    					 // put value in the response only if the value has changed
    	    				 	 
    	    				 	 if(newSessionSubscrList.contains(currSessionID) ||  // force the response when a new SUBSCRIBEDATA request comes from the client (even if the variable values are unchanged)
    	    				 	    readValuesMap.get(chachedVarKey)== null ||	
    	    				 	    ((readValuesMap.get(chachedVarKey)!= null && val!=readValuesMap.get(chachedVarKey))) &&  !(Float.isNaN(val) && readValuesMap.get(chachedVarKey).isNaN())) 
    	    					 {
   	    				 			 readValuesMap.put(chachedVarKey, val); //cache the new value
    	    						 
    	    						 result.put("type", new Integer(1));  //1 means variable,  2 alarm
    				            	 result.put("subscriptionid", new Integer[]{Integer.parseInt(subID)});
    				            	 result.put("address", address);
    					
    				            	 if(Float.isNaN(val))
    				            		 result.put("value", "");
    				            	 else
    				            	 {
    				            		//type DIGITAL
    					            	 if (var.getInfo().getType()==1) 
    					            	 	 result.put("value", val==1?true:false);
    					            	//type ANALOG or INTEGER
    					            	 else  
    					            		 result.put("value", val);
    				            	 }
    				            	  
    				            	 result.put("timestamp", System.currentTimeMillis()); // the timestamp of the last data change
    				            	 result.put("status", new Integer(0)); // the status code of the data change (0=good)
    				            		 
    				            	 // OPTIONAL FIELDS
    				             	 /*
    				            	 result.put("description",""); //the description of the data variable
    				            	 result.put("username",""); // the name of the user who set the data variable (?)
    				            	 */
    				            		 
    				            	 resultsArray.add(gson.toJsonTree(result));
    				            	 givenResults++;
    				            	 
    				            	 newSessionSubscrList.remove(currSessionID); // remove the 'new SUBSCRIBEDATA' notification from the list
    	    					 }
	    	    			}
	    	    			 
	    	    			 for (;i<addresses.size();i++)
	    	    				 remainingVarList.add(addresses.get(i));
	    	    			 
	    	    			 remaining = remainingVarList.size();
    	    		 	}
	    				catch (Exception e)
	    				{
	    					LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
	    				}
    	    		 }
        			 
    	    		 passedTime = System.currentTimeMillis()-startTime;
    	    		 
    	    		 if(resultsArray.size()==0 && passedTime < LONGPOLLTIMEOUT)
     	    		 {
    	    			int iterations = SLEEPTIME/SLEEP_TIME_SLICE;
    	    			for(int i=0; i<iterations; i++)
    	    			{
    	    				if(newSessionSubscrList.contains(currSessionID)) //interrupt the sleep (of 'SLEEPTIME' secs) when a new subscribedata comes from the client
    	    					break;
    	    				
    	    				try
         	    			{
         	    				Thread.sleep(SLEEP_TIME_SLICE);
         	    			}
         	    			catch(Exception e)
         	    			{
         	    				LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
         	    				break;
         	    			}
    	    			}
     	    		 }
    	    		
    	    		 // exit from loop and give response in the following cases:
    	    		 // 1. a new SUBSCRIBEDATA comes from the client
    	    		 // 2. resultArray is not empty
    	    		 // 3. longpoll timeout is expired
    	    			
        		 } while(resultsArray.size()==0 && passedTime < LONGPOLLTIMEOUT);
    		 }
    		 
    		 //EMPTY PUBLISH AND DEMO SLEEP MANAGEMENT 
    		 if(emptySubscription || BaseConfig.isDemo()) // delay of 2 seconds (to avoid very frequent publish) when PUBLISH request comes with NO SUBSCRIBED DATA
    			 									      // or when data variation is high (e.g. with STRESS protocol on PVPRO DEMO)
    		 {
    			int iterations = MIN_PUBLISH_DELAY/SLEEP_TIME_SLICE;
    			
    			for(int i=0; i<iterations; i++)
	    		{
	    			try
  	    			{
  	    				Thread.sleep(SLEEP_TIME_SLICE);
  	    			}
  	    			catch(Exception e)
  	    			{
  	    				LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
  	    				break;
  	    			}
	    				
	    			if(newSessionSubscrList.contains(currSessionID)) //interrupt the sleep (of 'MIN_PUBLISH_DELAY' secs) when a new subscribedata comes from the client
	    				break;
	    		}
    		 }
    		 // END EMPTY PUBLISH AND DEMO SLEEP MANAGEMENT 
    		 
    		 //
    		 if(remainingVarList.size()!=0)
    		 {
    			 String [] tmpArray = new String[remainingVarList.size()]; 
    			 tmpArray = remainingVarList.toArray(tmpArray);
    			 postponedPublishMap.put(currSessionID, tmpArray);
    		 }
    		 else
    			 postponedPublishMap.remove(currSessionID);
    		    		 
    		 Map<String,JsonElement> output = new HashMap<String,JsonElement>();
          	 output.put("result", resultsArray);
          	 output.put("remaining", gson.toJsonTree(remaining));
          	 jsonresp = gson.toJson(output);
          	
          	
          	// DEBUG PURPOSES
          	// passedTime = System.currentTimeMillis()-startTime;
          	// LoggerMgr.getRootLogger().info("Overall time: "+ passedTime +" SessID: " +currSessionID);
          	 
          	return jsonresp;
    	}

     	//  ------------ SCADA AND MULTIPOINT NOT IMPLEMENTED ----------
    	 
    	/*************************************************************
  		* 				SCADA LEVEL (METHODS ABOVE)
  		*************************************************************/
    	 
    	if(request_params.containsKey("subscribefilter"))
    	{ 	// response content given by Certec (to manage 'subscribefiler' requests coming from client even if SCADA level is not implemented) 
    		/*Map<String,Object> responsemap = new HashMap<String,Object>();
          	responsemap.put("error", "unknown method");
          	jsonresp = gson.toJson(responsemap);*/
     	
    		// response copied from DemoServer response (traffic sniffing)
          	return "{\"error\":300,\"errorstring\":\"Unknown method\"}";
    	}
    	/*************************************************************
   		* 				MULTIPOINT LEVEL (METHODS ABOVE)
   		*************************************************************/
    	 
    	return jsonresp;
	}
	
	static private String webmi_authorize(String code)
	{
		try {
			Method[] meth = Class.forName("a").getMethods();
			String[] params = new String[1];
			params[0] = new String(code);
			return (String)meth[0].invoke(null, (Object)params);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
	
	public static Variable[] getVariable(ArrayList<String> addresses, Integer id_dev_dtl) throws Exception
	{	
		Variable [] vars = new Variable[addresses.size()];
		ArrayList<Integer> idvarsList = new ArrayList<Integer>();
		ArrayList<String> returnAddresses = new ArrayList<String>();
		
		for(int i=0; i<addresses.size(); i++)
		{
			try
			{
				String varcode;
				//if short format, without description (c.pco format)  ->  $dev$.var_type.var_code
				if (!addresses.get(i).contains(SvgMapsUtils.VARCODE_SEPARATOR))
					varcode = addresses.get(i).substring(addresses.get(i).indexOf(SvgMapsUtils.SEPARATOR,6)+1); //skip $dev$ and variable type
				else
					varcode = addresses.get(i).substring(addresses.get(i).indexOf(SvgMapsUtils.VARCODE_SEPARATOR)+2);  // VARCODE_SEPARATOR is the unique separator that identifies the variable code
																									 // (NOTE: it is removed from every description when the SITE DATAPOINT is exported)
				String [] vals = (addresses.get(i).split("\\"+SvgMapsUtils.VARCODE_SEPARATOR))[0].split("\\"+SvgMapsUtils.SEPARATOR);
				
				int idDevice;
				if (vals[0].equalsIgnoreCase("$dev$"))   //if device model
				{
					idDevice = id_dev_dtl.intValue();
				}
				else
				// "L" stand for Logic Devices
					idDevice = vals[0].equals("L")?Integer.parseInt(vals[1]):VDMappingMgr.getInstance().getIdDevice(Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
					
				int	idvar = VDMappingMgr.getInstance().getIdVariable(idDevice, varcode);
				idvarsList.add(idvar);
				returnAddresses.add(addresses.get(i));
			}
			catch (Exception e)
			{
				LoggerMgr.getLogger(SvgMapsTranslator.class).error(e);
			}
			
		}
		
		int[] idvars = ArrayUtils.toPrimitive(idvarsList.toArray(new Integer[0]));
		
		if (idvars.length!=0)   
			vars = ControllerMgr.getInstance().getFromFieldWithDuplicates(idvars);
		addresses.removeAll(addresses);
		
		for(String o:returnAddresses)
			addresses.add(o);
		
		return vars;
	}
	
	
	
	private static void removeCachedValsBySessionID(String sessionID)
	{
		Set<String> keys = readValuesMap.keySet();
		Object[] keysArray = keys.toArray();
		for(int i=0; i<keysArray.length; i++)
		{
			if(((String)keysArray[i]).startsWith(sessionID+"-"))
				readValuesMap.remove((String)keysArray[i]);
		}
	}
	
	//thread used for cleaning the MAP used as cache for Longpolling 
	private static class SessionCheckerThread extends Thread
	{
		private boolean stop = true;

	    public void run()
	    {
	    	stop = false;
	    	
	    	while (!stop)
	        {
	        	Set<String> keys = aliveSessionsMap.keySet();
	        	Object[] keysArray = keys.toArray();
	        	for(int i=0; i<keys.size(); i++)
	        	{
	        		Long lastAliveTimestamp = aliveSessionsMap.get(keysArray[i]);
	        		if(System.currentTimeMillis()-lastAliveTimestamp > SESSIONTIMEOUT)
	        		{
	        			removeCachedValsBySessionID((String)keysArray[i]);
	        			aliveSessionsMap.remove(keysArray[i]);
	        		}
	        	}
	        	
	        	if(aliveSessionsMap.keySet().size()==0) //stop thread when there are no more alive sessions 
	        		stop = true;
	        	else
	        	{
		        	try
		            {
		                Thread.sleep(SESSIONFREQCHECK);
		            }
		            catch (InterruptedException e)
		            {
		            }
	        	}
	        }
	    	
	    	// clear data structures, when thread is stopped because the subscribedvarmap hashmap is empty
	    	aliveSessionsMap.clear();
	    	readValuesMap.clear();
	    }
	    
	    public void stopChecker()
	    {
	    	stop = true;
	    }
	}
}