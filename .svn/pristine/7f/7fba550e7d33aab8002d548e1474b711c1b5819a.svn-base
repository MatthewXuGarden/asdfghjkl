package com.carel.supervisor.presentation.bo;

import java.util.Properties;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.bean.SiteBookletBean;


public class BSiteBooklet extends BoMaster {
	private static final long serialVersionUID = 3986243423502487486L;
	private static final int REFRESH_TIME = -1;
	
	
	public BSiteBooklet(String l)
    {
        super(l, REFRESH_TIME);
    }
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "onPageLoad();");
		p.put("tab2name", "enableAction(1);");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "sitebooklet.js;keyboard.js;");
		p.put("tab2name", "sitebooklet.js;");
		return p;
	}
	
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		return p;
    }

	
    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		if( tabName.equalsIgnoreCase("tab1name") ) {
			String strNewPage = prop.getProperty("new_page");
			if( strNewPage != null ) {
				us.getCurrentUserTransaction().setProperty("page", strNewPage);
			}
			else {
				int idsite = Integer.parseInt(prop.getProperty("idsite"));
				SiteBookletBean bean = new SiteBookletBean(idsite);				
				int page = Integer.parseInt(prop.getProperty("page"));
				prop.put("user_name", us.getUserName());				
	    		switch( page ) {
	    		case 3:
	    			//bean.saveSiteBooklet(prop);
	    			bean.saveCover(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "4");
	    			break;
	    		case 4:
	    			bean.saveInstructions(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "5");
	    			break;
	    		case 5:
	    			bean.saveContact(SiteBookletBean.CTYPE_OWNER, prop);
	    			bean.saveContact(SiteBookletBean.CTYPE_MANAGER, prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "6");
	    			break;
	    		case 6:
	    			bean.saveContact(SiteBookletBean.CTYPE_PLANT, prop);
	    			bean.saveContact(SiteBookletBean.CTYPE_HEAD_OF_PLANT, prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "7");
	    			break;
	    		case 7:
	    			bean.saveContact(SiteBookletBean.CTYPE_HEAD_OF_AUDIT, prop);
	    			bean.saveSiteUsage(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "8");
	    			break;
	    		case 8:
	    			bean.saveSiteType(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "9");
	    			break;
	    		case 9:
	    			bean.saveSecondaryFluid(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "10");
	    			break;
	    		case 10:
	    			bean.saveSiteReference(prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "11");
	    			break;
	    		case 11:
	    			bean.saveSafetyDevices1(us, prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "12");
	    			break;
	    		case 12:
	    			bean.saveSafetyDevices2(us, prop);
	    			if( !bean.isBooklet() )
	    				us.getCurrentUserTransaction().setProperty("page", "30");
	    			break;
	    		case 26:
	    			bean.saveRefrigerantRecovery(us, prop);
	    			break;
	    		case 30:
	    			bean.savePreventionPlan(prop);
	    			if( !bean.isBooklet() ) {
	    				bean.createBooklet();
	    				us.getCurrentUserTransaction().setProperty("page", "3");
	    			}
	    			break;
	    		case 31:
	    			bean.saveLeakageVerification(us, prop);
	    			break;
	    		case 71:
	    			bean.saveNotices(us, prop);
	    			break;
	    		}
			}
		}
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	String strResponse = "<response/>";
    	return strResponse;
    }
}
