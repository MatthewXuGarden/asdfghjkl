package com.carel.supervisor.presentation.devices;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.presentation.session.UserSession;

public class HomeDtlDevice extends AbstractDtlDevice 
{
	private static final String READONLY = "1";
	private static final String HOME = "HOME";
	
	public HomeDtlDevice(UserSession session,String lang,int idDevice)
    {
        super(session,lang,idDevice);
    }
	
	public boolean check(VarphyBean variable)
    {
    	String rwStatus = null;
    	String toDisplay = null;
    	
        if (variable != null)
        {
        	rwStatus = variable.getReadwrite();
        	toDisplay = variable.getDisplay();
        }
            
        if (rwStatus != null && toDisplay != null)
        {
            rwStatus = rwStatus.trim();
            toDisplay = toDisplay.trim();

            if (rwStatus.equalsIgnoreCase(READONLY) && toDisplay.equalsIgnoreCase(HOME))
            	return true;
        }
        return false;
    }
	
	public String renderVariables(String tableName)
    {

		StringBuffer sb = new StringBuffer("<div class='DevHomeVarsTable'>");	
		VarphyBean tmp = null;
		String sDesc = "";
		String sMeasure = "";
		int idDevice = -1;
		int idVariable = -1;
		for(int i=0; i<(this.variables.size()>6?6:this.variables.size()); i++)
		{
			tmp = (VarphyBean)this.variables.get(i);
			
			try {
				idDevice = tmp.getDevice().intValue();
			}
			catch(Exception e){
				Logger logger = LoggerMgr.getLogger(DeviceList.class);
				logger.error("*** Black Dashboard -> Home Variables *** : unable to get the device ID");
        		logger.error(e);
			}
			
			try {
				idVariable = tmp.getId().intValue();
			}
			catch(Exception e){
				Logger logger = LoggerMgr.getLogger(DeviceList.class);
				logger.error("*** Black Dashboard -> Home Variables *** : unable to get the variable ID #"+i+" [IDDevice="+idDevice+"]");
        		logger.error(e);				
			}
			
			sDesc = tmp.getShortDescription();
			sMeasure = tmp.getMeasureUnit();
			if(sMeasure != null && sMeasure.length() > 0)
				sMeasure = "["+sMeasure+"]";
			else
				sMeasure = "";

			sb.append("<div class='tdfisa' style='text-align:left;' title='"+tmp.getShortDescription()+"'>"+sDesc+"</div>");
			
			sb.append("<div class='lcd'>");
			if(tmp.getType() == 1)
				sb.append("<div id='var_"+idDevice+"_"+idVariable+"' class='lcdValue'>"+buildLedStatus((String)this.values.get(i),"")+"</div><div class='lcdMeasure'>"+sMeasure+"</div><div class='clr'></div>");
			else
				sb.append("<div id='var_"+idDevice+"_"+idVariable+"' class='lcdValue'><b>"+(String)this.values.get(i)+"</b></div><div class='lcdMeasure'>"+sMeasure+"</div><div class='clr'></div>");
			sb.append("</div>");
		}	
		sb.append("</div>"); // end div DevHomeVarsTable
		return sb.toString();

    }
	
	public String refreshVariables(String idDev)
    {
		StringBuffer ris = new StringBuffer();
		String type = "";
		VarphyBean tmp = null;
		for(int i=0; i<this.variables.size(); i++)
		{
			tmp = (VarphyBean)this.variables.get(i);
			type = tmp.getType()==1?"D":"A";
			ris.append("<var id='var_"+idDev+"_"+tmp.getId().intValue()+"' type='"+type+"'>"+(String)this.values.get(i)+"</var>");
		}
		return ris.toString();
    }
}
