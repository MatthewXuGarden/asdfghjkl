package com.carel.supervisor.presentation.bo;

import java.util.*;
import java.text.*;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.fs.*;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.fs.*;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;


public class BFSDetail extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BFSDetail(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "onLoadDetail();hideNewAlgorithmColumn();");
        p.put("tab2name","enableAction(1);");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = "keyboard.js;";
        }
        
        Properties p = new Properties();
        p.put("tab1name","fsdtl.js");
        p.put("tab2name",virtkey+"fsdtl_write.js");
        
        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    	String cmd = prop.getProperty("cmd");
    	String stridrack = us.getProperty("idrack");
    	String username = us.getUserName();
    	//String stridrack = prop.getProperty("idrack");
    	
    	if ("tab1name".equalsIgnoreCase(tabName))
    	{
    		if ("reset_all".equalsIgnoreCase(cmd))
    		{
    			int idrack = Integer.parseInt(prop.remove("idrack").toString());
    			FSRack rack = FSManager.getInstance().getRackByIdRack(idrack);
    			FSUtil[] utils = rack.getUtils();
    			for (int i=0;i<utils.length;i++)
    			{
    				if( rack.isNewAlg() )
    					utils[i].resetBuff();
    				utils[i].setMaxDcRecorded(-1);
    				utils[i].setMinDCRecorded(101);
    			}
    			EventMgr.getInstance().info(1,username,"fs","FS08",FSManager.getInstance().getRackDescription(rack));
    		}
    		else if ("reset_one".equalsIgnoreCase(cmd))
    		{
    			int idrack = Integer.parseInt(prop.remove("idrack").toString());
    			int idutil = Integer.parseInt(prop.remove("idutil").toString());
    			FSRack rack = FSManager.getInstance().getRackByIdRack(idrack);
    			FSUtil[] utils = rack.getUtils();
    			for (int i=0;i<utils.length;i++)
    			{
    				if (utils[i].getIdutil().intValue()==idutil)
    				{
    					utils[i].setMaxDcRecorded(-1);
    					utils[i].setMinDCRecorded(101);
    					EventMgr.getInstance().info(1,username,"fs","FS08",utils[i].getDescription(),FSManager.getInstance().getRackDescription(rack));
    					break;
    				}
    				
    			}
    		}
    	}
    	else if ("tab2name".equalsIgnoreCase(tabName))
    	{
    		String cmdCombo = prop.getProperty("cmd_combo");
    		if ("change".equalsIgnoreCase(cmdCombo))
    		{
    			String newidRack = prop.getProperty("idrack");
    			us.setProperty("idrack",newidRack);
    		}
    		else
    		{
    			// recupero idrack e corrispondente oggetto rack:
    			int actualRack = Integer.parseInt(stridrack);
				//FSRack rack = FSManager.getInstance().getRackByIdRack(actualRack);
    			FSRack rack = FSRackBean.getActualRackFromDB(actualRack,us.getLanguage());
    			
    			boolean bNewAlg = prop.getProperty("new_alg").equals("true");
    			if( bNewAlg ) {
    				FSManager.updateFSProperty("Sb", Integer.parseInt(prop.getProperty("Sb")));
    				FSManager.updateFSProperty("t", Integer.parseInt(prop.getProperty("t")) * 60);
    				//FSManager.updateFSProperty("Q", Integer.parseInt(prop.getProperty("Q")));
    				FSManager.updateFSProperty("YELLOW_Q", Integer.parseInt(prop.getProperty("YELLOW_Q")));
    				FSManager.updateFSProperty("ORANAGE_Q", Integer.parseInt(prop.getProperty("ORANGE_Q")));
    				FSManager.updateFSProperty("RED_Q", Integer.parseInt(prop.getProperty("RED_Q")));
    				FSManager.loadFSProperties();
    			}
    			
    			String sql = "";
    			if("save_all".equalsIgnoreCase(cmd))
    			{ //salvo anche dati centrale su dispositivo fisico:
    				
    				Float maxsetpt = prop.getProperty("maxsetpt").equalsIgnoreCase("")?null:Float.parseFloat(prop.getProperty("maxsetpt"));
    				Float minsetpt = prop.getProperty("minsetpt").equalsIgnoreCase("")?null:Float.parseFloat(prop.getProperty("minsetpt"));
    				Float grad = prop.getProperty("gradval").equalsIgnoreCase("")?null:Float.parseFloat(prop.getProperty("gradval"));
    				
    				Integer windc = bNewAlg ? null : Integer.parseInt(prop.getProperty("timewinval"))*60;
    				Integer freqdc = bNewAlg ? null : Integer.parseInt(prop.getProperty("waittimeval"))*60;
    				Integer timeoff = bNewAlg ? null : Integer.parseInt(prop.getProperty("maxofftimeval"));
    				Integer banchioff = bNewAlg ? null : Integer.parseInt(prop.getProperty("maxoffutilval"));
    				
    				boolean nuovo = "new".equalsIgnoreCase(rack.getAux())?true:false;
    				
    				// se tipo aux "new" salvo nel dispositivo:
    				if (nuovo)
    				{
    					if (maxsetpt!=null||minsetpt!=null||grad!=null)
    					{
		    				SetContext rackValues = new SetContext();
		    				rackValues.setUser(us.getUserName());
		    				rackValues.setLanguagecode(us.getLanguage());
		    				rackValues.setCallback(new FSSetCallback());
		    				
		    				if (maxsetpt!=null)
		    				{
		    					rackValues.addVariable(rack.getId_maxset().intValue(),maxsetpt);	
		    				}
		    				if (minsetpt!=null)
		    				{
		    					rackValues.addVariable(rack.getId_minset().intValue(),minsetpt);
		    				}
		    				if (grad!=null)
		    				{
		    					rackValues.addVariable(rack.getId_gradient().intValue(),grad);
		    				}
		    				SetDequeuerMgr.getInstance().add(rackValues, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
    					}
						
	    				// salvo anche i valori diretti di "timewindow","waittime","maxofftime" e "maxoffutil" nella tabella 'fsrack':
    					if(!bNewAlg)
    					{
		    				sql = "update fsrack set timewindow=?, waittime=?, maxofftime=?, maxoffutil=? where (idrack = ?) and (setpoint = ?) and (aux = ?)";
		    				
		    				Object[] params = new Object[7];
	    					int iParam = 0;
	    					params[iParam++]= new Integer((int)(windc));
	    					params[iParam++]= new Integer((int)(freqdc));
	    					params[iParam++]= new Integer((int)(timeoff));
	    					params[iParam++]= new Integer((int)(banchioff));
	    					params[iParam++]= new Integer((int)(actualRack));
	    					params[iParam++]= rack.getId_setpoint();
	    					params[iParam++]= rack.getAux();
	    					
	    					boolean logs = true;
	    					DatabaseMgr.getInstance().executeStatement(null, sql, params, logs);
    					}
    				}
    				else //se tipo aux "old" salvo nel db:
    				{
    					sql = bNewAlg
    						? "update fsrack set maxset=?, minset=?, gradient=? where (idrack = ?) and (setpoint = ?) and (aux = ?)" 
    						: "update fsrack set maxset=?, minset=?, gradient=?, timewindow=?, waittime=?, maxofftime=?, maxoffutil=? where (idrack = ?) and (setpoint = ?) and (aux = ?)";
    					
    					Object[] params = new Object[bNewAlg ? 6 : 10];
    					int iParam = 0;
    					params[iParam++]= new Float(maxsetpt);
    					params[iParam++]= new Float(minsetpt);
    					params[iParam++]= new Float(grad);
    					if( !bNewAlg ) {
    						params[iParam++]= new Integer((int)(windc));
    						params[iParam++]= new Integer((int)(freqdc));
    						params[iParam++]= new Integer((int)(timeoff));
        					params[iParam++]= new Integer((int)(banchioff));
    					}
    					params[iParam++]= new Integer((int)(actualRack));
    					params[iParam++]= rack.getId_setpoint();
    					params[iParam++]= rack.getAux();
    					
    					boolean logs = true;
    					DatabaseMgr.getInstance().executeStatement(null, sql, params, logs);
    				}
    			}

    			//if ("save_utils".equalsIgnoreCase(cmd))
    			//{ //salvo solo i banchi su DB:
    			// recupero centrale selezionata:
    			
    			int numdc = Integer.parseInt(prop.getProperty("numdc"));
    			
    			if (numdc > 0)
    			{
	    			// recupero lista banchi associati da db:
	    			FSUtil[] rackUtils = rack.getUtils();
	    			if( bNewAlg ) {
	    				SetContext objContext = new SetContext();
	    				objContext.setLanguagecode(us.getLanguage());
	    				objContext.setUser(us.getUserName());
	    				objContext.setCallback(new FSSetCallback());
		    			for(int i = 0; i < numdc; i++) {
		    				SetWrp sw = objContext.addVariable(rackUtils[i].getIdTSH(), new Float(prop.getProperty("maxdcid" + i)));
		    				sw.setCheckChangeValue(true);
		    			}
	    				SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	    			}
	    			else {
		    			// preparo query per multistatement:
		    			sql = "update fsutil set maxdc = ? where (idutil = ?) and (idrack = ?) and (solenoid = ?)";
		    			
		    			Object[][] params = new Object[numdc][4];
		    			
		    			for (int i=0; i < numdc; i++)
		    			{
		    				params[i][0] = new Integer(prop.getProperty("maxdcid" + i));
		    				params[i][1] = rackUtils[i].getIdutil();
		    				params[i][2] = new Integer(actualRack);
		    				params[i][3] = rackUtils[i].getIdsolenoid();
		    			}
		    			
		    			DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
	    			}
    			}
    			
    			//}
    			EventMgr.getInstance().info(1,us.getUserName(),"fs","FS10",FSManager.getInstance().getRackDescription(rack));
    			FSManager.getInstance().configurationChanged(username);
    		}
    	}
    }

    
    public String executeDataAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    	XMLNode xmlResponse = new XMLNode("response", "");

    	int idrack = Integer.parseInt(us.getProperty("idrack"));
    	FSRack rack = FSRackBean.getActualRackFromDB(idrack, us.getLanguage());
    	boolean old = "old".equalsIgnoreCase(rack.getAux()); 
    	xmlResponse.setAttribute("bNewAlg", rack.isNewAlg() ? "true" : "false");

    	// Information area
    	XMLNode xmlInfo = null;
    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "last_sampling");
    	String strSamplingTime = "***";
    	Date dtSamplingTime = FSManager.getInstance().getRackByIdRack(rack.getId_rack()).getSamplingTime(); 
    	if( dtSamplingTime != null ) {
    		SimpleDateFormat dtFormat = new SimpleDateFormat("HH:mm");
    		strSamplingTime = dtFormat.format(dtSamplingTime);
    	}
    	xmlInfo.setAttribute("value", strSamplingTime);
    	xmlResponse.addNode(xmlInfo);

    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "current_setpoint");
    	xmlInfo.setAttribute("value", ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getFormattedValue());
    	xmlResponse.addNode(xmlInfo);
    	
    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "minimum_setpoint");
    	xmlInfo.setAttribute("value", old ? rack.getId_minset().toString() : ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getFormattedValue());
    	xmlResponse.addNode(xmlInfo);
    	
    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "maximum_setpoint");
    	xmlInfo.setAttribute("value", old ? rack.getId_maxset().toString() : ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getFormattedValue());
    	xmlResponse.addNode(xmlInfo);

    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "gradient");
    	xmlInfo.setAttribute("value", old ? rack.getId_gradient().toString() : ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getFormattedValue());
    	xmlResponse.addNode(xmlInfo);

    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "number_of_utilities");
    	xmlInfo.setAttribute("value", String.valueOf(rack.getUtils().length));
    	xmlResponse.addNode(xmlInfo);
    	
    	if( rack.isNewAlg() ) {
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "samples_number");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getSb()));
        	xmlResponse.addNode(xmlInfo);

        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "sample_period");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getT() / 60));
        	xmlResponse.addNode(xmlInfo);
        	
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "time_window");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getT() / 60 * FSManager.getSb()));
        	xmlResponse.addNode(xmlInfo);

//        	xmlInfo = new XMLNode("info", "");
//        	xmlInfo.setAttribute("id", "utilities_blocking_fs");
//        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getQ()));
//        	xmlResponse.addNode(xmlInfo);
        	
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "yellow_q");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getYELLOW_Q()));
        	xmlResponse.addNode(xmlInfo);
        	
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "orange_q");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getORANGE_Q()));
        	xmlResponse.addNode(xmlInfo);
        	
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "red_q");
        	xmlInfo.setAttribute("value", String.valueOf(FSManager.getRED_Q()));
        	xmlResponse.addNode(xmlInfo);
    	}
    	else {
        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "time_window_dc");
        	xmlInfo.setAttribute("value", String.valueOf(rack.getTimewindow() / 60));
        	xmlResponse.addNode(xmlInfo);

        	xmlInfo = new XMLNode("info", "");
        	xmlInfo.setAttribute("id", "frequency_dc");
        	xmlInfo.setAttribute("value", String.valueOf(rack.getWaittime() / 60));
        	xmlResponse.addNode(xmlInfo);
    	}
    	
    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "maximum_offline_time");
    	xmlInfo.setAttribute("value", String.valueOf(rack.getMaxofftime()));
    	xmlResponse.addNode(xmlInfo);
    	
    	xmlInfo = new XMLNode("info", "");
    	xmlInfo.setAttribute("id", "maximum_offline_utilities");
    	xmlInfo.setAttribute("value", String.valueOf(rack.getMaxoffutil()));
    	xmlResponse.addNode(xmlInfo);

    	// Rack area
    	if( rack.isNewAlg() ) {
	    	String rack_table = FSStatus.getRackStatusTable(1, FSManager.getInstance().getRackByIdRack(rack.getId_rack()), 
	    		us.getLanguage(), us.getScreenHeight(), us.getScreenWidth(), us);
	    	int iStart = rack_table.indexOf("<script>") + "<script>".length();
	    	int iStop = rack_table.indexOf("var aSize");
	    	String strRack = "<![CDATA[" + rack_table.substring(iStart, iStop) + "]]>";
	    	XMLNode xmlRack = new XMLNode("rack", strRack);
	    	xmlResponse.addNode(xmlRack);
    	}
    	
    	// Utility area
    	int[] anCounters = new int[FSManager.SB_STATUS];
    	String util_table = FSStatus.getStatusTable(rack, us.getLanguage(), us.getScreenHeight(), us.getScreenWidth(), us, anCounters);
    	int iStart = util_table.indexOf("<script>") + "<script>".length();
    	int iStop = util_table.indexOf("var aSize");
    	String strUtil = "<![CDATA[" + util_table.substring(iStart, iStop) + "]]>";
    	XMLNode xmlUtil = new XMLNode("util", strUtil);
    	xmlResponse.addNode(xmlUtil);
    	
    	
    	// Counters area
    	if( rack.isNewAlg() ) {
    		int[] leds = FSManager.getInstance().getRackSatus(rack);
	    	XMLNode xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "yellow_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.YELLOW]));
	    	xmlResponse.addNode(xmlCount);
	    	xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "yellow_offline_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.YELLOW_OFFLINE]));
	    	xmlResponse.addNode(xmlCount);
	    	xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "orange_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.ORANGE]));
	    	xmlResponse.addNode(xmlCount);
	    	xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "orange_offline_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.ORANGE_OFFLINE]));
	    	xmlResponse.addNode(xmlCount);
	    	xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "red_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.RED]));
	    	xmlResponse.addNode(xmlCount);
	    	xmlCount = new XMLNode("count", "");
	    	xmlCount.setAttribute("id", "red_offline_count");
	    	xmlCount.setAttribute("value", String.valueOf(leds[FSManager.RED_OFFLINE]));
	    	xmlResponse.addNode(xmlCount);
    	}
    	
    	return xmlResponse.toString();
    }
}
