package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.session.UserSession;


public class ButtonDtlDevice extends AbstractDtlDevice
{
	private List inputAvailable = new ArrayList();
	
	private Map lnkMapper = new HashMap();
	private Map lnkRemapp = new HashMap();
	private Map lnkDescr = new HashMap();
	private boolean buttonActive = false;
	
	private String devStatus;
	
    public ButtonDtlDevice(UserSession session,String lang,int idDevice)
    {
        super(session,lang,idDevice);
        this.devStatus = UtilDevice.getLedColor(new Integer(idDevice));
        buttonActive = this.session.isButtonActive("dtlview","tab1name","subtab2name");
    }

    public String renderVariables(String tableName)
    {    	

    	int[] limits=calcOffsetsForScreenSize();
        // Profilatura pulsanti
        String sOnClick = "";
        //String sStyle = "";
        String sClass = "";
        boolean canIwrite = false;
        
    	VarphyBean tmp = null;
    	StringBuffer sb = new StringBuffer("");
    	String pathImg = "";
    	int dim = this.variables.size();
    	if(dim > 12)
    		dim = 12;
    	
    	String led[] = null;
    	// Get variable link with button
    	linkVariable();
    	 	
    	// box generale
    	sb.append("<div class='buttonDtlDeviceBox'>");
    	for(int i=0; i<dim; i++)
    	{
            // Check del RW
            try {
                canIwrite = ((Boolean)this.inputAvailable.get(i)).booleanValue();
            }
            catch(Exception e){}
            
    		tmp = (VarphyBean)this.variables.get(i);
    		
    		// check if device is offline
    		if(((String)this.values.get(i)).equalsIgnoreCase("***"))
			{
				// if device is offline get the imageoff + "_off" image name
				// (it always exists for standard icons and is created - equals to the imageoff icon - for the custom devices)
				pathImg = ((tmp.getImageOff()!=null?tmp.getImageOff().substring(0,tmp.getImageOff().length()-4)+"_off.png":tmp.getButtonpath()));
				
				sClass = "commandBtnOff";
				sOnClick = "";
			}
    		else
    		{
    			if(((String)this.values.get(i)).equalsIgnoreCase("0"))
        			pathImg = ((tmp.getImageOff()!=null?tmp.getImageOff():tmp.getButtonpath()));
        		else
        			pathImg = ((tmp.getImageOn()!=null?tmp.getImageOn():tmp.getButtonpath()));
    		
    			
    			if(canIwrite && buttonActive) // checks the possibility to write on the variable, based on the MANUFACTURER / SERVICE filter flag
    										  // checks also the possibility to set variables, based on the user profile
    			{
					sClass = "commandBtnOn";
					sOnClick = "onclick='dtlBttSetVars("+i+");return false;'";
				}
				else
				{
					sClass = "commandBtnOff";
					sOnClick = "";
					pathImg = pathImg.substring(0,pathImg.length()-4)+"_off.png";
				}
    		}
    		
    		
    		// div interno cliccabile
    		sb.append("<div class='"+sClass+"' ");
    		sb.append(sOnClick);
    		sb.append(" >");
    		
    		sb.append("<form name='frmdtlbtt"+i+"' class='commandForm' id='frmdtlbtt"+i+"' action='servlet/master;' method='post'>");
    		sb.append("<input type='hidden' id='dtlst_"+tmp.getId().intValue()+"' name='dtlst_"+tmp.getId().intValue()+"' ");
            if(isGavButton(tmp))
                sb.append("value='0'/>");
            else
                sb.append("value='"+(((String)this.values.get(i)).equalsIgnoreCase("1")?"0":"1")+"'/>");
    		
            // icon
    		sb.append("<img class='commandIconPos' id='bttdtlst_"+tmp.getId().intValue()+"' src='"+pathImg+"' alt='"+tmp.getShortDescription()+"'/>");
    		// short desc.
    		String shortDescr = tmp.getShortDescription();
    		if (shortDescr != null && shortDescr.length() > limits[2]) {
    			shortDescr = shortDescr.substring(0,limits[2]);
    			shortDescr+="..";
    		}
    		if(canIwrite)
        		sb.append("<div class='commandDescPos' >"); /* style='left:"+limits[0]+"px;' */
    		else // metto la descrizione del bottone in grigetto
        		sb.append("<div class='commandDescPos' >"); /* style='left:"+limits[0]+"px;' */
    		sb.append(shortDescr);
    		sb.append("</div>");    		

    		
    		// led
    		led = buildStatusLed(tmp,(String)this.values.get(i)/*, limits[1]*/);
    		sb.append(led[0]);
    		
    		sb.append("</form>");
    		sb.append("</div>"); // end DIV commandBtn..
    		if(i%2==1)
    			sb.append("<div class='clr'></div>");
    	}
   	
    	if (ResetSubset.getInstance().hasReset(getIdDevMdl()))
    	{	
			String raBtnClass = "commandBtnOff";
			String raIconOnOff = "_off";
			String raOnClick = "";
			
	    	if(session.isButtonActive("alrview", "tab1name", "Add"))  //(check ack button)
	    	{
	        	raBtnClass = "commandBtnOn";
	        	raIconOnOff = ""; 
	        	raOnClick = "onclick='dtlBttSetVars("+13+");return false;'";
	    	}
			sb.append("<div class='"+raBtnClass+"' "+raOnClick+">");
			sb.append("<form name='frmdtlbtt"+13+"' id='frmdtlbtt"+13+"' class='commandForm' action='servlet/master;' method='post'>");
			sb.append("<input type='hidden' id='resetbttvalue' name='resetbttvalue' value='0'/>");
			sb.append("<input type='hidden' id='resetbttcmd' name='resetbttcmd' value='resetbtt'/>");
			sb.append("<img class='commandIconPos' id='bttdtlst_13"+/*tmp.getId().intValue()+*/"' src='images/button/alarmreset" + raIconOnOff + ".png' alt='"+LangMgr.getInstance().getLangService(session.getLanguage()).getString("button","reset")+"' ></img>");
			sb.append("<div class='commandDescPos'>"); //style='left:"+(limits[0]+2)+"px;'
			sb.append("ALARM");
			sb.append("</div>");
			sb.append("</form>");
			sb.append("</div><div class='clr'></div>");
    	}
    	
		sb.append("</div>"); // end DIV box generale
    	
    	return sb.toString();
    }
    
    public String refreshVariables(String idDev)
    {
    	//int[] limits = calcOffsetsForScreenSize();
    	linkVariable(); //Riccardo: Do we really need to re-build the led map from DB each time the page is refreshed?   	
    	boolean canIwrite = false;
    	
    	StringBuffer sb = new StringBuffer();
    	VarphyBean tmp = null;
    	String pathImg = "";
    	String[] led = null;
    	
    	sb.append("<btt>");
    	
    	for(int i=0; i<this.variables.size(); i++)
    	{
            // Check RW
            try {
                canIwrite = ((Boolean)this.inputAvailable.get(i)).booleanValue();
            }
            catch(Exception e){}
            
    		tmp = (VarphyBean)this.variables.get(i);
    		
    		led = buildStatusLed(tmp,(String)this.values.get(i)/*, limits[1]*/);
    		
    		if(tmp != null)
    		{
    			if(((String)this.values.get(i)).equalsIgnoreCase("***"))
    			{
    				// if device is offline get the imageoff + "_off" image name
    				// (it always exists for standard icons and is created - equals to the imageoff icon - for the custom devices)
    				pathImg = ((tmp.getImageOff()!=null?tmp.getImageOff().substring(0,tmp.getImageOff().length()-4)+"_off.png":tmp.getButtonpath()));
    			}
    			else
    			{
    				if(((String)this.values.get(i)).equalsIgnoreCase("0"))
        				pathImg = ((tmp.getImageOff()!=null?tmp.getImageOff():tmp.getButtonpath()));
            		else
            			pathImg = ((tmp.getImageOn()!=null?tmp.getImageOn():tmp.getButtonpath()));
    			
    				
    				if(!canIwrite || !buttonActive)
    				{
   						pathImg = pathImg.substring(0,pathImg.length()-4)+"_off.png";
    				}
    			}
    			
                 
                if(isGavButton(tmp))
                {
                    sb.append("<bar id='"+tmp.getId().intValue()+"'" +
                              " value='0'" +
                              " led='"+led[1]+"'><![CDATA["+pathImg+"]]></bar>");
                }
                else
                {
                    sb.append("<bar id='"+tmp.getId().intValue()+"'" +
                            " value='"+(((String)this.values.get(i)).equalsIgnoreCase("1")?"0":"1")+"'" +
                            " led='"+led[1]+"'><![CDATA["+pathImg+"]]></bar>");
                }
    		}
    	}
    	
    	sb.append("</btt>");
    	
    	return sb.toString();
    }
    
    public boolean check(VarphyBean variable)
    {
        String rwStatus = null;

        if (variable != null)
        {
            rwStatus = variable.getReadwrite();
        }

        if (rwStatus != null)
        {
            rwStatus = rwStatus.trim();

            if((variable.getButtonpath() != null) && (!variable.getButtonpath().equalsIgnoreCase("")))
            {
            	if (!this.devStatus.equalsIgnoreCase("0")){
		            
            		// READWRITE variables with 'SERVICE' type
		            if (rwStatus.equalsIgnoreCase("3") || rwStatus.equalsIgnoreCase("7"))
		            {
		                //if (ProfileBean.PERMISSION_READ_WRITE == session.getPermission(
		                //            ProfileBean.FUNCT_SERV_PARAM))
		            	if (session.getVariableFilter()==ProfileBean.FILTER_SERVICES || session.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER)
		                {
		                    inputAvailable.add(new Boolean(true));
		                }
		                else
		                {
		                    inputAvailable.add(new Boolean(false));
		                }
		                return true;
		            }
		            // READWRITE variables with 'MANUFACTURER' type
		            else if (rwStatus.equalsIgnoreCase("11"))
		            {
		                //if (ProfileBean.PERMISSION_READ_WRITE == session.getPermission(
		                //            ProfileBean.FUNCT_CONSTR_PARAM))
		            	if (session.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER)
		                {
		                    inputAvailable.add(new Boolean(true));
		                }
	                    else
	                    {
	                        inputAvailable.add(new Boolean(false));
	                    }
	                    return true;
	                    
		            }
		            
            	}else{
		            	// device offline
		            	inputAvailable.add(new Boolean(false));
		            	return true;
            	}
            }
        }
        return false;
    }
    
    private void linkVariable()
    {
    	if(this.idDevMdl != -1)
    	{
    		String sql = "select * from cfvarlnk where iddevmdl=?";
    		RecordSet rs = null;
    		Record r = null;
    		int[] idVar = null;

    		try
    		{
    			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(this.idDevMdl)});
    			if(rs != null)
    			{
    				idVar = new int[rs.size()];
    				for(int i=0; i<rs.size(); i++)
    				{
    					r = rs.get(i);
    					if(r != null)
    					{
    						lnkMapper.put((Integer)r.get("idvarmdl"),(Integer)r.get("idvariable"));
    						idVar[i] = ((r.get("idvariable")!=null)?((Integer)r.get("idvariable")).intValue():-1);
    					}
    				}
    			}
    			
    			if(idVar != null && idVar.length > 0)
    			{
    				String add = "";
    				Object[] param = new Object[(4+idVar.length)];
    				int idx = 0;
    				
    				sql = "select a.idvariable,a.idvarmdl,b.description from cfvariable as a,cftableext as b "+
    				      "where a.idsite=? and a.iddevice=? and a.idhsvariable is not null and " +
    				      "b.languagecode=? and b.tablename=? and b.tableid=a.idvariable and a.idvarmdl in (";
    				
    				param[idx++] = new Integer(1);
    				param[idx++] = new Integer(this.idDevice);
    				param[idx++] = this.lang;
    				param[idx++] = "cfvariable";
    				
    				for(int i=0; i<idVar.length; i++) {
    					add += "?,";
    					param[idx++] = new Integer(idVar[i]);
    				}
    				
    				add = add.substring(0,add.length()-1);
    				sql = sql + add + ");";
    				
    				rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
    				if(rs != null)
    				{
//    					String value = "";
    					for(int i=0; i<rs.size(); i++)
    					{
    						r = rs.get(i);
    						if(r != null)
    						{
    							this.lnkRemapp.put((Integer)r.get("idvarmdl"),(Integer)r.get("idvariable"));
    							this.lnkDescr.put((Integer)r.get("idvariable"),r.get("description"));
    						}
    					}
    				}
    			}
    		}
    		catch(Exception e) {
    			Logger logger = LoggerMgr.getLogger(this.getClass());
    			logger.error(e);
    		}
    	}
    }
    
    // Alessandro : l'offset che varia in base alla risoluzione non e' piu' necessario
    private String[] buildStatusLed(VarphyBean variable,String value /*, int ledLOffset*/)
    {
    	String html = "";
		String desc = "";
		String retValue = "";
		String[] ret = {"",""};
//		int idVar = -1;
		
    	Integer lnk = (Integer)this.lnkMapper.get(variable.getIdMdl());
    	
    	if(lnk != null)
    	{
    		if(lnk.intValue() == variable.getIdMdl().intValue())
    		{
    			// Led and Button have same value
    			retValue = (value.equalsIgnoreCase("1"))?"1":"0";
    			/* style='left:"+ledLOffset+"px;' */
    			html = "<img class='commandLedPos'  id=\"rleddtlst_"+variable.getId().intValue()+"\" src=\"images/led/RectL"+retValue+".png\" alt=\""+variable.getShortDescription()+"\"/>";
    		}
    		else
    		{
    			// Led link to another variable.
    			lnk = (Integer)this.lnkRemapp.get(lnk);
    			
    			if(lnk != null)
    			{
    				desc = (String)this.lnkDescr.get(lnk);
    				value = getVariableValue(lnk.intValue());
    				
    				// Intervento per MASTERCASE
    				if(variable.getIdMdl().intValue() == 3884)
    					value =	(value.equalsIgnoreCase("1")?"0":"1");
    				
    				retValue = (value.equalsIgnoreCase("1"))?"1":"0";
    				/* style='left:"+ledLOffset+"px;' */
    				html = "<img class='commandLedPos'  id=\"rleddtlst_"+variable.getId().intValue()+"\" src=\"images/led/RectL"+retValue+".png\" alt=\""+desc+"\"/>";
    			}
    		}
    	}
    	
    	if(html != null && html.length() == 0)
    		html = "&nbsp;";
    	
    	ret[0] = html;
    	ret[1] = retValue;
    	
    	return ret;
    }
    
    /*
     * Tuonata per Gavazzi
     * - 20326: Counter reset
     * - 20822: Alarm reset
     * - 20823: Max reset
     */
    private boolean isGavButton(VarphyBean tmp)
    {
        DeviceStructureList deviceStructureList = this.session.getGroup().getDeviceStructureList();
        DeviceStructure deviceStructure = deviceStructureList.get(tmp.getDevice().intValue());
        boolean setZero = false;
        if(deviceStructure.getIdDevMdl() == 109 && 
           (tmp.getIdMdl().intValue() == 20326 || tmp.getIdMdl().intValue() == 20822 || 
            tmp.getIdMdl().intValue() == 20823))
        {
            setZero = true;
        }
        return setZero;
    }
    
    /**
     * calc the right offset and limits for the actual screen size
     * @return      
     */
    private int[] calcOffsetsForScreenSize() {
    	int screenWidth=session.getScreenWidth();
    	
    	int result[] = new int[3];
    	// default (minimum) screenres: 1024x768
    	int textLOffset = 33;
    	int ledLOffset = 33;
    	int maxchars = 18;
    	
    	if (screenWidth > 1024 && screenWidth < 1280) {
    		textLOffset=screenWidth/34;
    		ledLOffset=screenWidth/34;
    	}
    	else if (screenWidth == 1280){
    		textLOffset=35;
    		ledLOffset=35;
    		maxchars=25;
    	}
    	else if (screenWidth > 1280 && screenWidth < 1440) {
    		textLOffset=screenWidth/42;
    		ledLOffset=screenWidth/42;
    		maxchars=25;
    	}
    	else if (screenWidth == 1440) {
    		textLOffset=39;
    		ledLOffset=39;
    		maxchars=26;		
    	}
    	else if (screenWidth > 1440 && screenWidth < 1600) {
    		textLOffset=screenWidth/42;
    		ledLOffset=screenWidth/42;
    		maxchars=26;
    	}
    	else if (screenWidth == 1600) {
    		textLOffset=39;
    		ledLOffset=39;
    		maxchars=32;		
    	}
    	else if (screenWidth > 1600) {
    		textLOffset=screenWidth/45;
    		ledLOffset=screenWidth/45+1;
    		maxchars=32;		
    	}    	
    	result[0]=textLOffset;
    	result[1]=ledLOffset;
    	result[2]=maxchars;
    	
    	return result;
    }
}
