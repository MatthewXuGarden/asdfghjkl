package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.plugin.algorithmpro.AlgorithmProMgr;
import com.carel.supervisor.plugin.algorithmpro.util.AlgoPro;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;

public class BAlgoPro extends BoMaster 
{
	private static final long serialVersionUID = -492764128954171108L;

	public BAlgoPro(String l) {
		super(l);
	}

	@Override
	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
        p.put("tab1name", "algoProOnLoad();");
        return p;
	}

	@Override
	protected Properties initializeJsOnLoad() {
		Properties p = new Properties();
        p.put("tab1name","algopro.js");
        return p;
	}

	@Override
	public void executePostAction(UserSession us, String tabName,Properties prop) 
		throws Exception 
	{
		String cmd = prop.getProperty("algocmd");
		boolean ris = false;
		
		if(cmd != null && cmd.equalsIgnoreCase("algoaction"))
		{
			String objSel = prop.getProperty("algoobj");
			
			String status = AlgorithmProMgr.getInstance().getAlgoStatus(objSel);
			// Running. I want to stop it
			if(status != null && status.equalsIgnoreCase("1"))
			{
				ProductInfoMgr.getInstance().getProductInfo().set(AlgoPro.PFX_SYSCONF + objSel, "FALSE");
				ris = AlgorithmProMgr.getInstance().stopAlgoProByName(objSel,us.getUserName());
			}
			// Stop. I want to run it
			else if(status != null && status.equalsIgnoreCase("0"))
			{
				ProductInfoMgr.getInstance().getProductInfo().set(AlgoPro.PFX_SYSCONF + objSel, "TRUE");
				ris = AlgorithmProMgr.getInstance().startAlgoProByName(objSel,us.getUserName());
			}
		}
		
		// Save the current object selected and send it back to the client
		String objSel = prop.getProperty("algoobj");
		UserTransaction ut = us.getCurrentUserTransaction();
		ut.setProperty("curalgoobj", objSel);
	
	}

	
	
}
