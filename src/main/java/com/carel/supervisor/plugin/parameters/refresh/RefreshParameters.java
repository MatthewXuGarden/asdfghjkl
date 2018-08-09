package com.carel.supervisor.plugin.parameters.refresh;

import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEventsList;
import com.carel.supervisor.presentation.refresh.RefreshMaster;
import com.carel.supervisor.presentation.session.UserSession;
 
public class RefreshParameters extends RefreshMaster {

	private ParametersEventsList pel;
	
	public RefreshParameters(){
		
	}
	
	@Override
	public String getHtmlData(UserSession userSession, String idHtmlObj)
			throws Exception {
        if (pel != null)
        {
    		String canModifyS= userSession .getCurrentUserTransaction().getProperty("param_canmodify");
    		
    		boolean canModify;
    		if (canModifyS!=null)
    			canModify=new Boolean(canModifyS).booleanValue();
    		else
    			canModify=false;
        	
            return pel.getHTMLTableRefresh(idHtmlObj, userSession,canModify);
        }

        return "";
	}

	@Override
	public void refresh(UserSession userSession) throws Exception {
		
		String iddeviceS= userSession .getCurrentUserTransaction().getProperty("param_device");
		
		int iddevice;
		if (iddeviceS!=null)
			iddevice=new Integer(iddeviceS).intValue();
		else
			iddevice=-1;
			
		pel = new ParametersEventsList(userSession.getLanguage(), null, 1, new Integer(arData[INDEX_PAGE]).intValue() , false, iddevice);
		userSession.getCurrentUserTransaction().setProperty("PEvnPage", arData[INDEX_PAGE]);

	}

}
