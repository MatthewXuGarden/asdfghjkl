package com.carel.supervisor.presentation.bo;

import java.util.*;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.plugin.co2.*;
import com.carel.supervisor.presentation.co2.*;


public class BCO2Saving extends BoMaster {
	private static final long serialVersionUID = 3066896753879494510L;
	private static final int REFRESH_TIME = -1;
	
	
	public BCO2Saving(String l)
    {
        super(l, REFRESH_TIME);
    }
	

	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "onLoadDashboard();");
		p.put("tab2name", "onLoadConfiguration();");
		p.put("tab3name", "onLoadBackup();");
		p.put("tab4name", "onLoadRackGroups();");
		p.put("tab5name", "onLoadGroups();");
		p.put("tab6name", "onLoadRacks();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "co2saving.js;keyboard.js;");
		p.put("tab2name", "co2saving.js;keyboard.js;");
		p.put("tab3name", "co2saving.js;keyboard.js;");
		p.put("tab4name", "dbllistbox.js;co2saving.js;keyboard.js;");
		p.put("tab5name", "dbllistbox.js;co2saving.js;keyboard.js;");
		p.put("tab6name", "co2saving.js;keyboard.js;");
		return p;
	}
	

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	if( tabName.equals("tab1name") ) {
    		String cmd = prop.getProperty("cmd");
    		if( "start".equals(cmd) ) {
    			if(!CO2SavingManager.getInstance().isRunning())
    			{
    				CO2SavingManager.getInstance().enable(true);
    				CO2SavingManager.getInstance().start(us.getUserName());
    			}
    		}
    		else if( "stop".equals(cmd) ) {
    			CO2SavingManager.getInstance().stop(us.getUserName());
    			CO2SavingManager.getInstance().enable(false);
    		}
    		else if( "restart".equals(cmd) ) {
    			CO2SavingManager.getInstance().stop(us.getUserName());
    			CO2SavingManager.getInstance().start(us.getUserName());
    		}
    		else if( "quick_run".equals(cmd) ) {
    			CO2SavingManager.getInstance().quickRunning();
    		}
    	}
    	else if( tabName.equals("tab2name") ) {
    		CO2SavingManager.getInstance().setMaxMasterOfflineTime(Integer.parseInt(prop.getProperty("offlineTime")));
    		CO2SavingManager.getInstance().setMasterOfflineAction(Integer.parseInt(prop.getProperty("offlineAction")));
    		CO2SavingManager.getInstance().setGroupOnSwitchDelay(Integer.parseInt(prop.getProperty("switchDelay")));
    		ModelBean.saveModels(prop);
    		CO2SavingManager.getInstance().configurationChanged(us.getUserName());
    	}
    	else if( tabName.equals("tab3name") ) {
    		String cmd = prop.getProperty("cmd");
			int idRack = Integer.parseInt(prop.getProperty("idRack"));
			RackBean rack = new RackBean(idRack);
			if( "save".equals(cmd) )
			{
				rack.saveBackup(prop);
				CO2SavingManager.getInstance().configurationChanged(us.getUserName());
			}
			else if( "modify".equals(cmd) )
				us.getCurrentUserTransaction().setAttribute("idRack", idRack);
    	}
    	else if( tabName.equals("tab4name") ) {
    		String cmd = prop.getProperty("cmd");
			int idRack = Integer.parseInt(prop.getProperty("idRack"));
			RackBean rack = new RackBean(idRack);
			if( "save".equals(cmd) )
			{
				rack.saveRack(prop);
				CO2SavingManager.getInstance().configurationChanged(us.getUserName());
			}
			else if( "modify".equals(cmd) )
				us.getCurrentUserTransaction().setAttribute("idRack", idRack);
    	}
    	else if( tabName.equals("tab5name") ) {
    		String cmd = prop.getProperty("cmd");
			int idGroup = Integer.parseInt(prop.getProperty("idGroup"));
			GroupBean group = new GroupBean(idGroup);
    		if( "add".equals(cmd) )
    			group.addGroup(prop);
			else if( "save".equals(cmd) )
			{
				group.saveGroup(prop);
				CO2SavingManager.getInstance().configurationChanged(us.getUserName());
			}
			else if( "modify".equals(cmd) )
				us.getCurrentUserTransaction().setAttribute("idGroup", idGroup);
			else if( "remove".equals(cmd) )
			{
    			group.deleteGroup();
    			CO2SavingManager.getInstance().configurationChanged(us.getUserName());
			}
    	}
    	else if( tabName.equals("tab6name") ) {
    		RackBean.saveRacks(prop);
    		CO2SavingManager.getInstance().configurationChanged(us.getUserName());
    	}
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	StringBuffer sbResponse = new StringBuffer(); 
    	sbResponse.append("<response>");
    	
    	if( tabName.equals("tab1name") ) {
    		Vector<Vector<Status>> statusTable = CO2SavingManager.getInstance().getStatusTable();
    		for(Iterator<Vector<Status>> it = statusTable.iterator(); it.hasNext();) {
    			Vector<Status> rackStatusTable = it.next();
    			for(int i = 0; i < rackStatusTable.size(); i++) {
    				Status status = rackStatusTable.get(i);
    				if( i == 0 ) {
    					sbResponse.append("<rack id=\"");
    					sbResponse.append(status.id);
    					sbResponse.append("\" st=\"");
    					sbResponse.append(status.st);
    					sbResponse.append("\">");
    				}
    				else {
    					sbResponse.append("<group id=\"");
    					sbResponse.append(status.id);
    					sbResponse.append("\" st=\"");
    					sbResponse.append(status.st);
    					sbResponse.append("\"/>");
    				}
    			}
    			sbResponse.append("</rack>");
    		}
    	}
    	else if( tabName.equals("tab3name") ) {
    		Integer idDevice = Integer.parseInt((String)prop.get("action"));
			VarphyBeanList varList = new VarphyBeanList();
			VarphyBean[] vars = varList.getAllVarOfDevice(us.getLanguage(), us.getIdSite(), idDevice);
			for(int i = 0; i < vars.length; i++) {
				VarphyBean var = vars[i];
				if( var.getType() != 4 ) {
					sbResponse.append("<var id=\"");
					sbResponse.append(var.getId());
					sbResponse.append("\" desc=\"");
					sbResponse.append(var.getShortDescription());
					sbResponse.append("\" />");
				}
			}
    	}

    	sbResponse.append("</response>");
    	return sbResponse.toString();
    }
}
