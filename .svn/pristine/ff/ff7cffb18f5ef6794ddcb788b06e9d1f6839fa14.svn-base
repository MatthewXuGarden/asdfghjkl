package com.carel.supervisor.presentation.maps;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.controller.setfield.OnLineCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.comboset.ComboParam;
import com.carel.supervisor.presentation.comboset.ComboParamMap;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.script.EnumerationMgr;


public class MapData
{
    private final static String BASE_POST_FIELDS_NAME = "input";
    private final static String VARIABLE_GET_FROM_FIELD = "variableGet";
    private final static String VARIABLE_SET_IN_FIELD = "variableSet";
    private final static String VARIABLE_TEXTSET_IN_FIELD = "variableTextSet";
    private final static String DEVICE = "device";
    private final static String DEVICE_DESCRIPTION = "devdesc";
    private final static String VARIABLE_DESCRIPTION = "vardesc";
    private final static String VARIABLE_TEXT = "vartext";
    private final static String VARIABLE_TEXT_B = "vartext_b";
    private final static String VARIABLE_IMAGE = "varimage";
    
    private final static String GENERIC_COMPRESSOR = "gc";
    private final static String GENERIC_FAN = "gfan";
    
    private final static String CONDENSERS = "cond"; 
    
    private final static String SEMI_HERMETIC_COMPRESSOR = "shc";
    
    private final static String GENERAL_ALARM_STATUS = "GA";
    
    
    private final static String VARIABLE_GET_TYPE_A="TypeA";
    private final static String VARIABLE_GET_TYPE_B="TypeB";
    
    
    private final static String VARIABLE_GET_VALUE_TYPE_A="Value";
    private final static String VARIABLE_GET_VALUE_TYPE_B="CurrentValue";
    
    
    StringBuffer scriptHtmlBuffer = new StringBuffer();

    public MapData(Properties properties, UserSession userSession)
    {
        int n = properties.size();

        if (n == 0)
        {
            System.out.print("Nessun oggetto");

            return;
        } //if

        DeviceStructureList deviceStructureList = userSession.getGroup().getDeviceStructureList();

        String value = null;
        Integer id = null;

        String color_offline = "#BBBBBB";
        String color_online  = "#00FF00";
        String color_alarm   = "red";
        String color_inhibit = "#4076D2";
        String statusColour1 = "#AAAAAA";
        String statusColour2 = "#BBBBBB";
        String statusColour3 = "#CCCCCC";
        String statusColour4 = "#DDDDDD";
        String statusColour5 = "#EEEEEE";
        
/*________________________________________________________________________________
   DEPRECATED: Romain Brosse proposal - START
   
        String color_new_offline = "darkgray";
        String color_new_alarm = "red";
        
   DEPRECATED: Romain Brosse proposal - END
        
        GroupListBean listOfGroups = userSession.getGroup();
        int site = userSession.getIdSite();
    	String language = userSession.getLanguage();
       
        GroupBean[] groupsOfArea = null;
		try {
			groupsOfArea = listOfGroups.retrieveAllGroups(site,language);
		} catch (Exception e) {
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
________________________________________________________________________________*/
        
        for (int i = 0; i < n; i++)
        {
            Variable variable = null;
            Variable variableStatus = null;
            Variable variableAlarm = null;
            Variable variableAlarm_2 = null;
            Variable variableAlarm_3 = null;
            
            boolean directLogic1 = true;
            boolean directLogic2 = true;
            boolean directLogic3 = true;
            boolean directLogic4 = true;
            boolean directLogic5 = true;
            
            boolean existStatus1 = false;
        	boolean existStatus2 = false;
        	boolean existStatus3 = false;
        	boolean existStatus4 = false;
        	boolean existStatus5 = false;

            Variable status1 = null;
            Variable status2 = null;
            Variable status3 = null;
            Variable status4 = null;
            Variable status5 = null;

            Variable temperature = null;
            Variable temperature2 = null;
            String box_color = null;
            String display_mu = null;
            String display_temp = null;
            String display_temp2 = null;
            String display_temp_title = null;
            String display_descr_temp = null;

            boolean existFiller = false;
            boolean filler      = false;
            
            Integer nb_alarm_present = null;
            Integer nb_new_alarm_present = null;
            Integer alarm_present = null;
            Integer offline_present = null;
            Integer online_present = null;
            Integer inhibit_present = null;
            Integer id1 = null;
            Integer id2 = null;

            try
            {
                String[] tmp = ((String) properties.get(BASE_POST_FIELDS_NAME + i)).split("=");
                
                if (tmp[0].equals(VARIABLE_GET_FROM_FIELD))
                {
                	String []datas=tmp[1].split(";");
                    variable = VDMappingMgr.getInstance().getVariable(datas[0]); 
                    	
                    if(datas[1].equals(VARIABLE_GET_TYPE_A)){
	                    scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                        "\").SetVariable('obj."+VARIABLE_GET_VALUE_TYPE_A+"'," + ((variable.getInfo().getType()==1&&new Float(variable.getCurrentValue()).isNaN())?0:variable.getCurrentValue()) + " );");
                    }//if VARIABLE_GET_TYPE_A
                    else if(datas[1].equals(VARIABLE_GET_TYPE_B)){
                        scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
    	                        "\").SetVariable('obj."+VARIABLE_GET_VALUE_TYPE_B+"1'," + variable.getCurrentValue() + " );");
                        scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
    	                        "\").SetVariable('obj."+VARIABLE_GET_VALUE_TYPE_B+"2'," + variable.getCurrentValue() + " );");
                    }//if VARIABLE_GET_VALUE_TYPE_B

                } //if variable get from field
                
                
                /*  ****    BIOLO 11/08/2009    ****
                 *  new management of variables set  
                 *  for a better management of active objects refresh  */
                
                else if(tmp[0].equals(VARIABLE_SET_IN_FIELD))
                {
                	boolean can_set = userSession.isButtonActive("dtlview","tab1name","subtab2name");
                	
                	String []datas=tmp[1].split(";");
                	
                	//value of variable from field
                	Float field_value = VDMappingMgr.getInstance().getVariable(datas[0]).getCurrentValue();
                	
                	// value of variable to set
                	Float setted_value = null;
                	
                	Long now = System.currentTimeMillis();
                	if (!datas[1].equals("value"))    //PARAMETER SET case
                	{
                		if (can_set)
                		{
	                		setted_value = Float.parseFloat(datas[1]);
	                		// if value are different -> SET 
	                    	if (field_value!=setted_value)
	                    	{
	                    		    variable = VDMappingMgr.getInstance().getVariable(datas[0]);
	                				SetContext setContext = new SetContext();
	                				setContext.setLanguagecode(userSession.getLanguage());
	                            	setContext.addVariable(variable.getInfo().getId(), Float.parseFloat(datas[1]));
	                            	setContext.setCallback(new OnLineCallBack());
	                        		setContext.setUser(userSession.getUserName());
	                                SetDequeuerMgr.getInstance().add(setContext);
	                                
	                                //set value in flash object
	                    			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	              	                        "\").SetVariable('obj.Value',"+setted_value+");");
	                    			
	                    			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	              	                        "\").SetVariable('obj.Decimals',"+variable.getInfo().getDecimal()+");");
	                    			
	                    			//for IE11,Chrome,FF.
	                    			//insert the timestamp of the set instant
	                    			scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
	                        		scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableSet="+datas[0]+";value;"+now+"\";\n");
	                        		scriptHtmlBuffer.append("\n}else{");
	                        		scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableSet="+datas[0]+";value;"+now+"\";\n");
	                        		scriptHtmlBuffer.append("\n}");
	                    	}
                		}
                		else  //NO SET PERMISSION
                		{
                			scriptHtmlBuffer.append("\nalert('Permission denied');");
                			//set flash object
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
          	                        "\").SetVariable('obj.Value',"+field_value+");");
                			
                			//for IE11,Chrome,FF.
                			//reset input structure (variableSet=1-1-1-1-1;value) without final timestamp
                			scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableSet="+datas[0]+";value\";\n");
                			scriptHtmlBuffer.append("\n}else{");
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableSet="+datas[0]+";value\";\n");
                			scriptHtmlBuffer.append("\n}");
                		}
                	}
                	else   // REFRESH case
                	{
                		Long set_time = 0L;
                		variable = VDMappingMgr.getInstance().getVariable(datas[0]);
                		//datas.length is > 2 when in input object there is the timestamp of the set
                		if (datas.length>2)  //during SET of variable
                		{
                			set_time = Long.parseLong(datas[2]) ;
                			if (now > (set_time + 40000))  //if timeout after set: refresh variable with value from field
                			{
                				//set flash object
                    			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
              	                        "\").SetVariable('obj.Value',"+field_value+");");
                    			
                    			//For IE11, Chrome, FF
                    			//remove timestamp; so next time enter in normal refresh case
                    			scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
                    			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableSet="+datas[0]+";value\";\n");
                    			scriptHtmlBuffer.append("\n}else{");
                    			scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableSet="+datas[0]+";value\";\n");
                    			scriptHtmlBuffer.append("\n}");
                			}
                			//else if it's in setting time, don't refresh from field and keep set time for next refresh cycle 
                			else{
                				//For IE11, Chrome, FF
                				scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
                				scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableSet="+datas[0]+";value;"+set_time+"\";\n");
                				scriptHtmlBuffer.append("\n}else{");
                				scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableSet="+datas[0]+";value\";\n");
                				scriptHtmlBuffer.append("\n}");
                			}
                		}
                		else  // STANDARD refresh from field
                		{
                			//set flash object
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
          	                        "\").SetVariable('obj.Value',"+field_value+");");
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
  	                        "\").SetVariable('obj.Decimals',"+variable.getInfo().getDecimal()+");");
                			
                			//reset input structure (variableSet=1-1-1-1-1;value) without final timestamp
                			scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableSet="+datas[0]+";value\";\n");
                			scriptHtmlBuffer.append("\n}else{");
                			scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableSet="+datas[0]+";value\";\n");
                			scriptHtmlBuffer.append("\n}");
                		}
                	}
                	
                	
                }//if variable set in field
                // BIOLO END
                
                else if(tmp[0].equals(VARIABLE_TEXTSET_IN_FIELD))
                {
                	
                	String []datas=tmp[1].split(";");
                	
                	
                	if(!datas[1].equals("value")){
                		variable = VDMappingMgr.getInstance().getVariable(datas[0]);
                		SetContext setContext = new SetContext();
                		setContext.setLanguagecode(userSession.getLanguage());
                    	setContext.addVariable(variable.getInfo().getId(), Float.parseFloat(datas[1]));
                    	setContext.setCallback(new OnLineCallBack());
                		setContext.setUser(userSession.getUserName());
                        SetDequeuerMgr.getInstance().add(setContext);
                		
                      //For IE11, Chrome, FF
                        scriptHtmlBuffer.append("\nif(myDocumentElement.getElementById(\"input"+i+"\")!=null){");
                		scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"input"+i+"\").value=\"variableTextSet="+datas[0]+";value\";\n");
                		scriptHtmlBuffer.append("\n}else{");
                		scriptHtmlBuffer.append("\nmyDocumentElement.getElementsByName(\"input"+i+"\")[0].value=\"variableTextSet="+datas[0]+";value\";\n");
                		scriptHtmlBuffer.append("\n}");
                		scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"textset"+i+"\").value=\"\";\n");
                	}//
                }//
                else if (tmp[0].equals(DEVICE))
                {
                    id = VDMappingMgr.getInstance().getDeviceId(tmp[1]);//Integer.valueOf(tmp[1]);

                    scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"imageobj" + i +
                        "\").style.backgroundImage  = \"url(images/led/L" +
                        UtilDevice.getLedColor(id) + ".gif)\";\n");
                } //if device
                else if (tmp[0].equals(DEVICE_DESCRIPTION))
                {
                    id = VDMappingMgr.getInstance().getDeviceId(tmp[1]);
                    value = deviceStructureList.get(id.intValue()).getDescription();
                    scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"devdesc" + i +"\").innerHTML  = \" " + value+ " \";\n");
                } //if device_desc_dinamic
                else if(tmp[0].equals(VARIABLE_DESCRIPTION))
                {
                	variable = VDMappingMgr.getInstance().getVariable(tmp[1]);
                	int idvar = variable.getInfo().getId();
                	value = VarphyBeanList.retrieveVarById(userSession.getIdSite(), idvar, userSession.getLanguage()).getShortDescription();
                	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vardesc" + i +"\").innerHTML  = \" " + value+ " \";\n");
                }
                else if (tmp[0].equals(VARIABLE_TEXT))
                {
                	 id = VDMappingMgr.getInstance().getVariable(tmp[1]).getInfo().getId();
                     VarphyBean varphyBean=VarphyBeanList.retrieveVarById(1,id.intValue(),userSession.getLanguage());
                     variable = VDMappingMgr.getInstance().getVariable(tmp[1]);
                     // bug id 9648 : Labels are not displayed correctly in the maps 
                     ComboParamMap cm = new ComboParamMap();
                     ComboParam c = cm.getComboByIdVar(variable.getInfo().getDevice(), userSession.getLanguage(), variable.getInfo().getId());
                     
                     value = EnumerationMgr.getInstance().getEnumCode(varphyBean.getIdMdl(), variable.getCurrentValue(), userSession.getLanguage());
                     if (!"".equals(value))
                     {		
                    	  if(c!=null){
                        	  scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
                                      "\").innerHTML  = \" " + c.getDescFromValue(variable.getFormattedValue())  + " "+(varphyBean.getMeasureUnit()!=null?varphyBean.getMeasureUnit():"")+ " \";\n");
                         }else{
	                    	  scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
	                                  "\").innerHTML  = \" " + value + " "+(varphyBean.getMeasureUnit()!=null?varphyBean.getMeasureUnit():"")+" \";\n");
                         }
                     }
                     else{
                    	 if(c!=null){
	                       	  scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
	                                     "\").innerHTML  = \" " + c.getDescFromValue(variable.getFormattedValue())  + " "+(varphyBean.getMeasureUnit()!=null?varphyBean.getMeasureUnit():"")+ " \";\n");
                        }else{
		                       scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
		                         "\").innerHTML  = \" " + variable.getFormattedValue() + " "+(varphyBean.getMeasureUnit()!=null?varphyBean.getMeasureUnit():"")+" \";\n");
                        }
                     }
                } //if variable text
                else if (tmp[0].equals(VARIABLE_TEXT_B))
                {
                	 id = VDMappingMgr.getInstance().getVariable(tmp[1]).getInfo().getId();
                     variable = VDMappingMgr.getInstance().getVariable(tmp[1]);
                     // bug id 9648 : Labels are not displayed correctly in the maps 
                     ComboParamMap cm = new ComboParamMap();
                     ComboParam c = cm.getComboByIdVar(variable.getInfo().getDevice(), userSession.getLanguage(), variable.getInfo().getId());
                     if(c!=null){
                    	  scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
                                  "\").innerHTML  = \" " + c.getDescFromValue(variable.getFormattedValue())    + " \";\n");
                     }else{
	                     scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"vartxtobj" + i +
	                         "\").innerHTML  = \" " + variable.getFormattedValue() + " \";\n");
                     }
                } //if variable text without MEASURING UNIT
                else if (tmp[0].equals(VARIABLE_IMAGE))
                {
                	String []datas=tmp[1].split(";");
                    variable = VDMappingMgr.getInstance().getVariable(datas[0]); 
                  
                    scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"varimageobj" + i +
                        "\").src  = \" " + (variable.getCurrentValue()==1?datas[1]:datas[2]) + " \";\n");
                } //if variable text
                else if (tmp[0].equals(GENERAL_ALARM_STATUS)){
                	GroupListBean groupListBean = userSession.getGroup();			
        			int[] groups = groupListBean.getIds();
        			DeviceStructureList deviceStructureListMaster = groupListBean.getDeviceStructureList(); 
        			int[] ids = deviceStructureListMaster.retrieveIdsByGroupsId(groups);
        			scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
      	                        "\").SetVariable('obj.Value',"+(DeviceStatusMgr.getInstance().existAlarm(ids)==true?1:0) +" );");

                }//genera alarm status
                else if (tmp[0].equals(GENERIC_COMPRESSOR)){
                	String []datas=tmp[1].split(";");
                    // status
                    variableStatus = VDMappingMgr.getInstance().getVariable(datas[0]); //ControllerMgr.getInstance().getFromField(new Integer(datas[0]).intValue());
                    if(variableStatus.getCurrentValue()==1){
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                        "\").SetVariable('obj.Value',1 );");
                    }
                    else{
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                        "\").SetVariable('obj.Value',0 );");
                    }
                	// alarm
                    variableAlarm = VDMappingMgr.getInstance().getVariable(datas[1]); //ControllerMgr.getInstance().getFromField(new Integer(datas[1]).intValue());
                	if(variableAlarm.getCurrentValue()==1){
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
      	                        "\").SetVariable('obj.Alarm',1);");

                    }
                    else {
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
      	                        "\").SetVariable('obj.Alarm',0);");

                    }
                }//Generic Compressor 
                
                
                if (tmp[0].equals(SEMI_HERMETIC_COMPRESSOR)){
                	String []datas=tmp[1].split(";"); 
                	//id = Integer.valueOf(datas[0]);
                	id = VDMappingMgr.getInstance().getDeviceId(datas[0]);
                	variableStatus = VDMappingMgr.getInstance().getVariable(datas[1]);
                	
                	if (!datas[2].equalsIgnoreCase(" "))
                	{
                		variableAlarm = VDMappingMgr.getInstance().getVariable(datas[2].trim());
                	}
                	if (!datas[3].equalsIgnoreCase(" "))
                	{ 
                		variableAlarm_2 = VDMappingMgr.getInstance().getVariable(datas[3].trim());
                	}
                	if (!datas[4].equalsIgnoreCase(" "))
                	{		
                		variableAlarm_3 = VDMappingMgr.getInstance().getVariable(datas[4].trim());
                	}
                	// status
                	if(DeviceStatusMgr.getInstance().isOffLineDevice(id)){
                		 scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                         "\").SetVariable('obj.Value',0 );");
                     }//if
	               	 else if (variableStatus.getCurrentValue()==1) {
	            		 scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                     "\").SetVariable('obj.Value',1 );");
	                 }
	            	 else
	            	 {
	            		 scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                     "\").SetVariable('obj.Value',0 );");
	            	 }	
                	 // alarm
                	 if((variableAlarm!=null&&variableAlarm.getCurrentValue()==1) || (variableAlarm_2!=null&&variableAlarm_2.getCurrentValue()==1) || (variableAlarm_3!=null&& variableAlarm_3.getCurrentValue()==1)){
                		 scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                         "\").SetVariable('obj.Alarm',1);");
                	 }
                	 else {
                		 scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                         "\").SetVariable('obj.Alarm',0);");
                	 }
                	 
                	 int j=5;
                	 while (datas.length>j)
                	 {
                		 variable = VDMappingMgr.getInstance().getVariable(datas[j]);//ControllerMgr.getInstance().getFromField(id.intValue());
                         scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                        "\").SetVariable('obj.Valve"+ (j-3)+"',"+variable.getCurrentValue()+");");
                		 j++;
                	 }
                }//SemiHermetic Compressor
                else if (tmp[0].equals(GENERIC_FAN)){
                	String []datas=tmp[1].split(";");
                    variableStatus = VDMappingMgr.getInstance().getVariable(datas[0]);//ControllerMgr.getInstance().getFromField(new Integer(datas[0]).intValue());
                    
                    if (datas.length>1)
                    {
                    	variableAlarm = VDMappingMgr.getInstance().getVariable(datas[1]);//ControllerMgr.getInstance().getFromField(new Integer(datas[1]).intValue());
                    }
                    if(variableAlarm!=null && variableAlarm.getCurrentValue()==1){
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
      	                        "\").SetVariable('obj.Alarm',1 );");

                    }//if 
                    else if(variableStatus.getCurrentValue()==1){
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                        "\").SetVariable('obj.Alarm',0 );");
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
	                        "\").SetVariable('obj.Value',1 );");
                    }
                    else{
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                        "\").SetVariable('obj.Alarm',0 );");
                    	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                        "\").SetVariable('obj.Value',0 );");
                    }
                }//Generic Fan
                else if (tmp[0].equals(CONDENSERS)){
                	String []datas=tmp[1].split(";");
                	if(datas.length%2==0){
                		for(int j=0,z=0;j<datas.length;j++,z++){
                			variableStatus = VDMappingMgr.getInstance().getVariable(datas[j++]);//ControllerMgr.getInstance().getFromField(new Integer(datas[j++]).intValue());
                            variableAlarm = VDMappingMgr.getInstance().getVariable(datas[j]);//ControllerMgr.getInstance().getFromField(new Integer(datas[j]).intValue());
                            
                            if(variableAlarm.getCurrentValue()==1){
                            	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
              	                        "\").SetVariable('obj.Alarm"+(z+1)+"',1 );");

                            }//if 
                            else if(variableStatus.getCurrentValue()==1){
                            	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
        	                        "\").SetVariable('obj.Alarm"+(z+1)+"',0 );");
                            	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
        	                        "\").SetVariable('obj.Value"+(z+1)+"',1 );");
                            }
                            else{
                            	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                                "\").SetVariable('obj.Alarm"+(z+1)+"',0 );");
                            	scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"flashobj" + i +
                                "\").SetVariable('obj.Value"+(z+1)+"',0 );");
                            }//else
                			
                		}//for
                		
                	}//if
                	
                }//Condensers
                
/*________________________________________________________________________________
   DEPRECATED: Romain Brosse proposal - START
                
                // Device information aggregated
                if(tmp[0].equals("input_perso_a"))
                {
                    nb_alarm_present = 0;
                    nb_new_alarm_present = 0;
                    String datas[] = tmp[1].split(";");
                    id = Integer.valueOf(VDMappingMgr.getInstance().getDeviceId(datas[0]));
                    value = deviceStructureList.get(id.intValue()).getDescription();

                    

                    
                    display_mu         = datas[5];
                    display_temp       = datas[6];
                    display_temp2      = datas[7];
                    display_temp_title = datas[8];
                    display_descr_temp = datas[9];
                    statusColour1      = "#" + datas[13].substring(2);
                    statusColour2      = "#" + datas[14].substring(2);


                    if (!datas[1].equals("")){status1 = VDMappingMgr.getInstance().getVariable(datas[1]);}
                    if (!datas[2].equals("")){status2 = VDMappingMgr.getInstance().getVariable(datas[2]);}
                    box_color = color_online;
                    String sql = "select idalarm, starttime, a.description, b.description as priority, ackuser, acktime, resetuser, resettime from hsalarm, cftableext as a, cftext as b where iddevice=? and endtime is null and resetuser is null and a.idsite = ? and a.tablename=? and a.tableid=idvariable AND a.languagecode=? and b.idsite = ? AND b.languagecode=? and b.code=? and b.subcode=('alarmstate'||priority)";
                    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(id.intValue()), Integer.valueOf(userSession.getIdSite()), "cfvariable", userSession.getLanguage(), Integer.valueOf(userSession.getIdSite()), userSession.getLanguage(), "alrview"});
                     for (int p=0; p<rs.size(); p++){
                          Record alarme = rs.get(p);
                          String ackuser = UtilBean.trim(alarme.get("ackuser"));
                          if (ackuser==null){nb_new_alarm_present++;}
                          nb_alarm_present++;
                      }
                    if((UtilDevice.getLedColor(id).equals("1")&&((!status1.equals(""))||(!status2.equals("")))))
                    {
                        if (((status2.getCurrentValue() == 1.0F)&&(datas[10].equals("1")))||((status2.getCurrentValue() == 0.0F)&&(datas[10].equals("0")))) {box_color  = statusColour2;}
                        else if (status1.getCurrentValue() == 1.0F) {box_color = statusColour1;}
                    }
                    else if(UtilDevice.getLedColor(id).equals("0")){
                      if(nb_new_alarm_present>0){box_color = color_new_offline;}
                      else{box_color = color_offline;}
                    } 
                    else if(UtilDevice.getLedColor(id).equals("2")) {
                      if(nb_new_alarm_present>0){box_color = color_new_alarm;}
                      else{box_color = color_alarm;}
                    }
                    else if(UtilDevice.getLedColor(id).equals("3")) {box_color = color_inhibit;}
                    scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").style.backgroundColor  = \"").append(box_color).append("\";\n").toString());
                    if (display_temp.equals("1")||display_temp2.equals("1")||display_temp_title.equals("1"))
                    {
                        temperature = VDMappingMgr.getInstance().getVariable(datas[3]);
                        id1 = VDMappingMgr.getInstance().getVariable(datas[3]).getInfo().getId();
                        VarphyBean varphyBean = VarphyBeanList.retrieveVarById(1, id1.intValue(), userSession.getLanguage());
                        if (display_temp.equals("1"))
                        {
                            scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"td1_obj_perso_a")).append(i).append("\").innerHTML  = \"").append(display_descr_temp.equals("0") ? "" : varphyBean.getShortDescription()).append(display_descr_temp.equals("0") ? "" : " : ").append(temperature.getFormattedValue()).append(" ").append(((varphyBean.getMeasureUnit() == null)||display_mu.equals("0")) ? "" : varphyBean.getMeasureUnit()).append("\";\n").toString());
                        }
                        if (display_temp2.equals("1"))
                        {
                            temperature2 = VDMappingMgr.getInstance().getVariable(datas[4]);
                            id2 = VDMappingMgr.getInstance().getVariable(datas[4]).getInfo().getId();
                            VarphyBean varphyBean2 = VarphyBeanList.retrieveVarById(1, id2.intValue(), userSession.getLanguage());
                            scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"td2_obj_perso_a")).append(i).append("\").innerHTML  = \"").append(display_descr_temp.equals("0") ? "" : varphyBean2.getShortDescription()).append(display_descr_temp.equals("0") ? "" : " : ").append(temperature2.getFormattedValue()).append(" ").append(((varphyBean2.getMeasureUnit() == null)||display_mu.equals("0")) ? "" : varphyBean2.getMeasureUnit()).append("\";\n").toString());
                            if (display_temp_title.equals("1"))
                            {
                                scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").title  = \"").append(value).append(" : ").append(varphyBean.getShortDescription()).append("= ").append(temperature.getFormattedValue()).append(" ").append(varphyBean.getMeasureUnit() == null ? "" : varphyBean.getMeasureUnit()).append(" - ").append(varphyBean2.getShortDescription()).append("= ").append(temperature2.getFormattedValue()).append(" ").append(varphyBean2.getMeasureUnit() == null ? "" : varphyBean2.getMeasureUnit()).append("\";\n").toString());
                            }
                        }
                        if ((display_temp_title.equals("1"))&&(display_temp2.equals("0")))
                        {
                            scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").title  = \"").append(value).append(" : ").append(varphyBean.getShortDescription()).append("= ").append(temperature.getFormattedValue()).append(" ").append(varphyBean.getMeasureUnit() == null ? "" : varphyBean.getMeasureUnit()).append("\";\n").toString());
                        }
                    }
                    if (display_temp_title.equals("0"))
                    {
                        scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").title  = \"").append(value).append("\";\n").toString());
                    }
                if (datas[12].equals("0"))
                {
                  scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").onclick  = function() {top.frames['manager'].loadTrx('mstrmaps/FramesetTab.jsp&folder=mstrmaps&bo=BMasterMaps&type=click&pathmap=device.jsp&iddev=").append(datas[0]).append("&desc=Synoptique unit&eacute;');};\n").toString());
                }
                else
                {                  
                  scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_a")).append(i).append("\").onclick  = function() {top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab").append(datas[12]).append(".jsp&curTab=tab").append(datas[12]).append("name&type=click&iddev=").append(datas[0]).append("&desc=D&eacute;tail unit&eacute;');};\n").toString());
                }
                } else
                	
   DEPRECATED: Romain Brosse proposal - END
________________________________________________________________________________*/
                	
                // Device status area
                if(tmp[0].equals("input_perso_b"))
                {
                    nb_alarm_present     = 0;
                    String datas[] = tmp[1].split(";");
                    id = Integer.valueOf(VDMappingMgr.getInstance().getDeviceId(datas[0]));
                    value = deviceStructureList.get(id.intValue()).getDescription();

                	existStatus1 = !datas[1].equals("");
                	existStatus2 = !datas[2].equals("");
                	existStatus3 = !datas[3].equals("");
                	existStatus4 = !datas[4].equals("");
                	existStatus5 = !datas[5].equals("");

                    if ( existStatus1 )
                    {
                    	status1       = VDMappingMgr.getInstance().getVariable(datas[1]);
                    	directLogic1  = datas[6].equals("1");
                    	statusColour1 = "#" + datas[11].substring(2);
                    }
                    if ( existStatus2 )
                    {
                    	status2       = VDMappingMgr.getInstance().getVariable(datas[2]);
                    	directLogic2  = datas[7].equals("1");
                    	statusColour2 = "#" + datas[12].substring(2);
                    }
                    if ( existStatus3 )
                    {
                    	status3       = VDMappingMgr.getInstance().getVariable(datas[3]);
                    	directLogic3  = datas[8].equals("1");
                    	statusColour3 = "#" + datas[13].substring(2);
                    }
                    if ( existStatus4 )
                    {
                    	status4       = VDMappingMgr.getInstance().getVariable(datas[4]);
                    	directLogic4  = datas[9].equals("1");
                    	statusColour4 = "#" + datas[14].substring(2);
                    }
                    if ( existStatus5 )
                    {
                    	status5       = VDMappingMgr.getInstance().getVariable(datas[5]);
                    	directLogic5  = datas[10].equals("1");
                    	statusColour5 = "#" + datas[15].substring(2);
                    }
                    
                    box_color = color_online;
                    
                    String sql = "select idalarm, starttime, a.description, b.description as priority, ackuser, acktime, resetuser, resettime from hsalarm, cftableext as a, cftext as b where iddevice=? and endtime is null and resetuser is null and a.idsite = ? and a.tablename=? and a.tableid=idvariable AND a.languagecode=? and b.idsite = ? AND b.languagecode=? and b.code=? and b.subcode=('alarmstate'||priority)";
                    
                    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(id.intValue()), Integer.valueOf(userSession.getIdSite()), "cfvariable", userSession.getLanguage(), Integer.valueOf(userSession.getIdSite()), userSession.getLanguage(), "alrview"});

                    for (int p=0; p<rs.size(); p++)
                    {
                    	Record alarme = rs.get(p);
					    String ackuser = UtilBean.trim(alarme.get("ackuser"));
					    nb_alarm_present++;
					 }
                    
                    // Device OK with one of status1 ... status5 (pay attention to direct or inverse logic)
                    if ( (UtilDevice.getLedColor(id).equals("1") && ( existStatus1 || existStatus2 || existStatus3 || existStatus4 || existStatus5 ) ) )
                    {
                        if ( (existStatus1) && (((status1.getCurrentValue() == 1.0F) && (directLogic1)) || ((status1.getCurrentValue() == 0.0F) && (!directLogic1))) ) 
                        	box_color = statusColour1;
                        
                        else if ( (existStatus2) && (((status2.getCurrentValue() == 1.0F) && (directLogic2)) || ((status2.getCurrentValue() == 0.0F) && (!directLogic2))) )
                        	box_color = statusColour2;

                        else if ( (existStatus3) && (((status3.getCurrentValue() == 1.0F) && (directLogic3)) || ((status3.getCurrentValue() == 0.0F) && (!directLogic3))) ) 
                        	box_color = statusColour3;

                        else if ( (existStatus4) && (((status4.getCurrentValue() == 1.0F) && (directLogic4)) || ((status4.getCurrentValue() == 0.0F) && (!directLogic4)) ) ) 
                        	box_color = statusColour4;

                        else if ( (existStatus5) && (((status5.getCurrentValue() == 1.0F) && (directLogic5)) || ((status5.getCurrentValue() == 0.0F) && (!directLogic5)) ) )
                        	box_color = statusColour5;
                    }
                    // Device offline
                    else if (UtilDevice.getLedColor(id).equals("0"))
                    {
                   		box_color = color_offline;
                    }
                    // Device on alarm
                    else if (UtilDevice.getLedColor(id).equals("2"))
                    {
                   		box_color = color_alarm;
                    }
                    // Device disabled
                    else if (UtilDevice.getLedColor(id).equals("3"))
                    {
                    	box_color = color_inhibit;
                    }
                    scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_b")).append(i).append("\").style.backgroundColor  = \"").append(box_color).append("\";\n").toString());
                } else
                	
                // Device set
                if(tmp[0].equals("input_perso_c"))
                {
                    alarm_present = 0;
                    offline_present = 0;
                    online_present = 0;
                    inhibit_present = 0;
                    nb_alarm_present = 0;
                    String datas[] = tmp[1].split(";");
                    existFiller = !datas[0].equals("");
                    if ( existFiller )
                    	filler  = datas[0].equals("1");
                    
                    Set deviceSet = new HashSet();
                    for(int m = 1; m < datas.length; m++)
                    {
                    	if (!datas[m].equals(""))
                    		deviceSet.add(datas[m]);
                    }
                    java.util.Iterator deviceIterator = deviceSet.iterator();
                    while(deviceIterator.hasNext())
                    {
                      id = Integer.valueOf(VDMappingMgr.getInstance().getDeviceId(deviceIterator.next().toString()));
                      if(UtilDevice.getLedColor(id).equals("0")){offline_present++;}
                      if(UtilDevice.getLedColor(id).equals("1")){online_present++;}
                      if(UtilDevice.getLedColor(id).equals("2")){alarm_present++;}
                      if(UtilDevice.getLedColor(id).equals("3")){inhibit_present++;}
                      String sql = "select idalarm, starttime, a.description, b.description as priority, ackuser, acktime, resetuser, resettime from hsalarm, cftableext as a, cftext as b where iddevice=? and endtime is null and resetuser is null and a.idsite = ? and a.tablename=? and a.tableid=idvariable AND a.languagecode=? and b.idsite = ? AND b.languagecode=? and b.code=? and b.subcode=('alarmstate'||priority)";
                      RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(id), Integer.valueOf(userSession.getIdSite()), "cfvariable", userSession.getLanguage(), Integer.valueOf(userSession.getIdSite()), userSession.getLanguage(), "alrview"});
                      for (int p=0; p<rs.size(); p++){
                          Record alarme = rs.get(p);
                          String ackuser = UtilBean.trim(alarme.get("ackuser"));
                          nb_alarm_present++;
                      }
                    }
                    if (alarm_present > 0)
                    {
                        box_color = color_alarm;
                        String alarmCaption = LangMgr.getInstance().getLangService(userSession.getLanguage()).getString("mstrmaps", "DevicesSetAlarmTooltip");
                        scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_c")).append(i).append("\").title  = \"").append(alarm_present == null ? "" : alarm_present.toString()).append(alarmCaption).append("\";\n").toString());
                    }else
                    if ( offline_present > 0 )
                    {
                       box_color = color_offline;
                    }else
                    if (online_present >0)
                    {
                        box_color = color_online;
                    }else
                    {
                        box_color = color_inhibit;
                    }
                     scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_c")).append(i).append("\").style.borderColor  = \"").append(box_color).append("\";\n").toString());
                     
                     if ( filler )
                    	 scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_c")).append(i).append("\").style.backgroundColor  = \"").append(box_color).append("\";\n").toString());
                 }
                	 
/*________________________________________________________________________________
   DEPRECATED: Romain Brosse proposal - START

				else
                // Device group
                if(tmp[0].equals("input_perso_d"))
                {
                    alarm_present = 0;
                    offline_present = 0;
                    online_present = 0;
                    inhibit_present = 0;
                    nb_alarm_present = 0;
                    for(int q=0; q<groupsOfArea.length; q++){
                      if (groupsOfArea[q].getDescription().equals(tmp[1])){
                        id = groupsOfArea[q].getGroupId();
                        }
                      }
                    int[] idsdevices = listOfGroups.getDeviceStructureList().retrieveIdsByGroupId(id);
                    for (int o=0; o<idsdevices.length; o++){
                      if(UtilDevice.getLedColor(idsdevices[o]).equals("0")){offline_present++;}
                      if(UtilDevice.getLedColor(idsdevices[o]).equals("1")){online_present++;}
                      if(UtilDevice.getLedColor(idsdevices[o]).equals("2")){alarm_present++;}
                      if(UtilDevice.getLedColor(idsdevices[o]).equals("3")){inhibit_present++;}
                      String sql = "select idalarm, starttime, a.description, b.description as priority, ackuser, acktime, resetuser, resettime from hsalarm, cftableext as a, cftext as b where iddevice=? and endtime is null and resetuser is null and a.idsite = ? and a.tablename=? and a.tableid=idvariable AND a.languagecode=? and b.idsite = ? AND b.languagecode=? and b.code=? and b.subcode=('alarmstate'||priority)";
                      RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(idsdevices[o]), Integer.valueOf(userSession.getIdSite()), "cfvariable", userSession.getLanguage(), Integer.valueOf(userSession.getIdSite()), userSession.getLanguage(), "alrview"});
                      for (int p=0; p<rs.size(); p++){
                          Record alarme = rs.get(p);
                          String ackuser = UtilBean.trim(alarme.get("ackuser"));
                          nb_alarm_present++;
                      }
                    }
                    if ((alarm_present == 0) && (online_present == 0) && (offline_present > 0))
                    {
                       box_color = color_offline;
                    }else
                    if (alarm_present > 0)
                    {
                    	box_color = color_alarm;
                        scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_d")).append(i).append("\").title  = \"").append(alarm_present == null ? "" : alarm_present.toString()).append(" unite(s) en alarme").append("\";\n").toString());
                    }else
                    if(online_present >0)
                    {
                        box_color = color_online;
                    }else
                    {
                        box_color = color_inhibit;
                    }
                     scriptHtmlBuffer.append((new StringBuilder("\nmyDocumentElement.getElementById(\"obj_perso_d")).append(i).append("\").style.borderColor  = \"").append(box_color).append("\";\n").toString());
                 }else
                	 
				// y=ax+b: you have to use logical variables
                if(tmp[0].equals("input_perso_e"))
                {
                	String []datas=tmp[1].split(";");
                	String str = null;
                	String MeasuringUnit = null;
                	try	{MeasuringUnit = datas[4];}
                	catch (Exception e)	{MeasuringUnit = "";}
                	int nb_fraction = 0;
                	try	{nb_fraction = Integer.valueOf(datas[3]);}
                	catch (Exception e)	{nb_fraction = 0;}
                	try
                	{                		
                		float a = new Float(VDMappingMgr.getInstance().getVariable(datas[0]).getCurrentValue());
                		float b = new Float(VDMappingMgr.getInstance().getVariable(datas[1]).getCurrentValue());
                		float x = Float.parseFloat(datas[2]);
                		float y = a*x+b;
                		NumberFormat Myformat = NumberFormat.getInstance();
                		Myformat.setMinimumFractionDigits(nb_fraction);       //Nb de Digit mini
                		Myformat.setMaximumFractionDigits(nb_fraction);       //Nb de Digit Maxi
                		str = Myformat.format(y);
                	}
                	catch (Exception e)
                	{
                		str = "***";
                	}
                    scriptHtmlBuffer.append("\nmyDocumentElement.getElementById(\"obj_perso_e" + i +"\").innerHTML  = \" " + str+ " " +MeasuringUnit+ " \";\n");
                 }
                
   DEPRECATED: Romain Brosse proposal - END
________________________________________________________________________________*/

            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(MapData.class);
                logger.error(e);
            }
        } //for
    } //MapData

    public String getUpdateScript()
    {
        StringBuffer script = new StringBuffer();
        script.append("<script>");
        script.append("myDocumentElement=top.frames['body'].frames['bodytab'].document;");
        script.append(scriptHtmlBuffer.toString());
        script.append("top.frames['body'].frames['bodytab'].MTstopServerComm();");
        //Sblocco in scrittura
        script.append("top.frames['body'].frames['bodytab'].lock=false;");
        script.append("</script>");

        return script.toString();
    } //getUpdateScript
    
    
    
    
} //Class MapData
