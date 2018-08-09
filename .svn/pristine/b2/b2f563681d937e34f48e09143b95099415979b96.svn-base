package com.carel.supervisor.presentation.bo.master;

import java.util.Properties;

import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.script.EnumerationMgr;
import com.carel.supervisor.script.ExpressionMgr;
import com.carel.supervisor.script.special.Special;


public interface IMaster 
{
	public RefreshBean[] getRefreshObj(String tabname) throws Exception;
	public String getRefreshTime(String tabname);
	public String getEventOnLoad(String tabName) throws Exception;
	public String[] getJavascript(String tabName) throws Exception;
	public String getDefaulResource() throws Exception;
	public String getDefaulTabName() throws Exception;
	public void executePostAction(UserSession userSess, String tabName, Properties prop) throws Exception;
	public String getCommitAction(String tabName) throws Exception;
	public String getDocType(String tabName);
	
	public void loadFilter(int idDevice,int idDevMdl,String lang) throws Exception;
	public ExpressionMgr getExpressionMgr();
	public EnumerationMgr getEnumerationMgr();
	public Special getSpecial();
	
	public String executeDataAction(UserSession userSess, String tabName, Properties prop) throws Exception;
}
