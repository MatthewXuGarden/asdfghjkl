package com.carel.supervisor.presentation.devices;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.presentation.session.UserSession;

public class StateDtlDevice extends AbstractDtlDevice
{
	private static final String READONLY = "1";
	private static final String STATE = "STAT";
	
	

    public StateDtlDevice(UserSession session,String lang,int idDevice)
    {
        super(session,lang,idDevice);
    }

    public String renderVariables(String tableName)
    {
    	StringBuffer table = new StringBuffer();
    	int counter=1;
    	for(int i = 0; i<(variables.size()>12?12:variables.size()); i++)
    	{
    		VarphyBean v = (VarphyBean)variables.get(i);
    		if(v.getDisplay().trim().equalsIgnoreCase(STATE))
    		{
    			if (i%2==0) table.append("<div class='statVarRow'>");
		    	table.append("<div class='statVarBox' id='var_"+idDevice+"_"+v.getId().intValue()+"' title='"+v.getShortDescription()+"'>");
		    	table.append("<div class='statVarLed'>");
				table.append(buildLedStatus((String)values.get(i),"statusLed"));
				table.append("</div>");
				table.append("<div class='statVarDesc'>");
				table.append(v.getShortDesc().equals("")?(v.getShortDescription().length()<=50?v.getShortDescription():v.getShortDescription().substring(0, 50)+"..."):v.getShortDesc());
				table.append("</div>");
				table.append("</div>");
				if ((i+1)%2==0) table.append("<div class='clr'></div></div>");
    		}
    	}  	
        return table.toString();
    }
    
    public String refreshVariables(String tableName)
    {
    	StringBuffer ris = new StringBuffer();
		String type = "";
		VarphyBean tmp = null;
		ris.append("<stat>");
		for(int i=0; i<this.variables.size(); i++)
		{
			tmp = (VarphyBean)this.variables.get(i);
			type = tmp.getType()==1?"D":"A";
			ris.append("<svar id='var_"+idDevice+"_"+tmp.getId().intValue()+"' type='"+type+"' value='"+(String)this.values.get(i)+"' valueOrig='"+(String)this.valuesOriginal.get(tmp.getId())+"'>"+(tmp.getShortDesc().equals("")?(tmp.getShortDescription().length()<=50?tmp.getShortDescription():tmp.getShortDescription().substring(0, 50)+"..."):tmp.getShortDesc())+"</svar>");
		}
		ris.append("</stat>");
        return ris.toString();
    }

    public boolean check(VarphyBean variable)
    {
		String rwStatus = null;
		String toDisplay = null;
		if (variable != null)
		{
			rwStatus = variable.getReadwrite();
			toDisplay = variable.getDisplay();
			if (rwStatus != null && toDisplay != null &&
				rwStatus.trim().equalsIgnoreCase(READONLY) && toDisplay.trim().equalsIgnoreCase(STATE))
			{
					return true;
			}
		}
		return false;
    }
}
