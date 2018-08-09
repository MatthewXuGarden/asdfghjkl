package com.carel.supervisor.presentation.bean;

import java.util.Properties;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.session.*;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.menu.*;


public class LoginRedirectBean
{
	private UserSession userSession;
	private boolean bRedirect;
	private int idDevice;
	private int idLine;
	private int nDeviceAddress;
	private int iTab;
	private boolean bMobile;
	

	public LoginRedirectBean(UserSession userSession)
	{
		this.userSession = userSession;
		String redirect = userSession.getProperty("bLoginRedirect"); 
		bRedirect = redirect != null && redirect.equalsIgnoreCase("true");
		if( bRedirect ) {
	    	userSession.setProperty("bLoginRedirect", "false"); // clear redirect request from user session
			try {
				idLine = Integer.parseInt(userSession.getProperty("nLoginRedirectLineId"));
			}
			catch(NumberFormatException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				bRedirect = false; // invalid line; unable to redirect
			}
			try {
				nDeviceAddress = Integer.parseInt(userSession.getProperty("nLoginRedirectDeviceSerialAddress"));
			}
			catch(NumberFormatException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				bRedirect = false; // invalid device address; unable to redirect
			}
			idDevice = getIdDevice();
			if( idDevice == 0 )
				bRedirect = false; // device not found; unable to redirect
			try {
				int nTab = Integer.parseInt(userSession.getProperty("nLoginRedirectTabId"));
				iTab = nTab - 1;
			}
			catch(NumberFormatException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				iTab = -1;
			}
			if( iTab < 0 || iTab > MenuTabMgr.getInstance().getTabMenuFor("dtlview").getNumTab() - 1 )
				bRedirect = false; // invalid tab index; unable to redirect
			String mobile = userSession.getProperty("bLoginMobile"); 
			bMobile = mobile != null && mobile.equalsIgnoreCase("true");
		}
	}
	
	
	public static void initRedirect(UserSession userSession, Properties properties)
	{
		String redirect = properties.getProperty("bRedirect");
		Boolean bLoginRedirect = redirect != null && redirect.equalsIgnoreCase("true");
		if( bLoginRedirect ) {
			userSession.setProperty("bLoginRedirect", bLoginRedirect.toString());
			userSession.setProperty("nLoginRedirectLineId", properties.getProperty("nLineId"));
			userSession.setProperty("nLoginRedirectDeviceSerialAddress", properties.getProperty("nDeviceSerialAddress"));
			userSession.setProperty("nLoginRedirectTabId", properties.getProperty("nTabId"));
			Boolean bLoginMobile = properties.getProperty("pagetype").equals("mobile");
			userSession.setProperty("bLoginMobile", bLoginMobile.toString());
		}
	}
	
	
	public boolean isRedirect()
	{
		return bRedirect && !bMobile;
	}
	
	
	public void setSessionProperties(Properties properties)
	{
		properties.setProperty("bo", "BDtlView");
		properties.setProperty("folder", "dtlview");
		userSession.setProperty("iddev", String.valueOf(idDevice));
	}
	

	public void setTransactionProperties()
	{
    	DeviceStructureList deviceStructureList = userSession.getGroup().getDeviceStructureList(); 
    	int[] ids = deviceStructureList.retrieveIdsByGroupsId(userSession.getGroup().getIds());
    	userSession.getTransaction().setIdDevices(ids);
    	userSession.getTransaction().setIdDevicesCombo(ids);
      	userSession.getCurrentUserTransaction().setProperty("group", "");
    	TabObj tab = MenuTabMgr.getInstance().getTabMenuFor("dtlview").getTab(iTab);
    	userSession.getCurrentUserTransaction().setProperty("curTab", tab.getIdTab());
    	String resource = tab.getProperties("resource");
    	if( resource.indexOf("note.jsp&notetable=") >= 0 ) {
    		String notetable = resource.substring(resource.indexOf("notetable=") + "notetable=".length(), resource.length());
   			userSession.getCurrentUserTransaction().setProperty("notetable", notetable);
   			resource = resource.substring(0, resource.indexOf("&notetable="));
    	}
    	userSession.getCurrentUserTransaction().setProperty("resource", resource);
	}
	
	
	private int getIdDevice()
	{
		String sql = "SELECT iddevice FROM cfdevice WHERE idline=(SELECT idline FROM cfline WHERE code=?) AND address=?;";
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { idLine, nDeviceAddress });
			if( rs.size() > 0 )
				return (Integer)rs.get(0).get(0);
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return 0;
	}
	
	
	public boolean isMobileRedirect()
	{
		return bRedirect && bMobile;
	}


	public void setMobileSessionProperties()
	{
		userSession.setProperty("iddev", String.valueOf(idDevice));
	}
	
	
	public String getMobilePage()
	{
		String page;

		switch(iTab) {
		case 0:
			page = "DeviceMain.jsp";
			break;
		case 1:
			page = "DeviceParameters.jsp";
			break;
		case 2:
			page = "DeviceAlarms.jsp";
			break;
		case 4:
			page = "DeviceGraph.jsp";
			break;
		default:
			page = "Devices.jsp";
		}

		return page;
	}
}
