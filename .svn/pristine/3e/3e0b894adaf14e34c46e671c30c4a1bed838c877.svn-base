package com.carel.supervisor.presentation.bo;

import java.util.Properties;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.bean.SiteBookletBean;


public class BSiteBookletCat extends BoMaster {
	private static final long serialVersionUID = -2303321623350528321L;
	private static final int REFRESH_TIME = -1;
	
	
	public BSiteBookletCat(String l)
    {
        super(l, REFRESH_TIME);
    }
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "onCatalogLoad();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "sitebooklet_cat.js;keyboard.js;");
		return p;
	}
	
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		return p;
    }

	
    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		String cmd = prop.getProperty("cmd");
		if( "add".equals(cmd) ) {
			prop.put("user_name", us.getUserName());
			SiteBookletBean bean = new SiteBookletBean();
			bean.saveCatalog(prop);
			us.getCurrentUserTransaction().setAttribute("idNewBooklet", new Integer(bean.getId()));
		}
		else if( "reset".equals(cmd) ) {
			int idsite = Integer.parseInt(prop.getProperty("idsite"));
			SiteBookletBean bean = new SiteBookletBean(idsite);
			bean.resetBooklet();
		}
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	String strResponse = "<response/>";
    	return strResponse;
    }
}
