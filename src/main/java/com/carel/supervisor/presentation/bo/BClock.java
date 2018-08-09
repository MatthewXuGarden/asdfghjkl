package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.presentation.bean.ClockBean;
import com.carel.supervisor.presentation.bean.ClockBeanList;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;

public class BClock extends BoMaster {

	public BClock(String lang) {
		super(lang);
	}

	private static final long serialVersionUID = -3103595264902203797L;

	@Override
	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "clock.onPageLoad();");
		return p;
	}

	@Override
	protected Properties initializeJsOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "clock.js;");
		return p;
	}
	
	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception {
		if( tabName.equalsIgnoreCase("tab1name") )
			executeTab1Post(us, prop);
	}
	public void executeTab1Post(UserSession us,Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		ClockBeanList clockBeanList = new ClockBeanList();
		if("add".equalsIgnoreCase(cmd) || "edit".equalsIgnoreCase(cmd))
		{
			int iddevmdl = Integer.valueOf(prop.getProperty("iddevmdl"));
			clockBeanList.removeClock(iddevmdl);
			DevMdlBean devBean = (new DevMdlBeanList()).retrieveById(us.getIdSite(), us.getLanguage(), iddevmdl);
			if(devBean != null)
			{
				String devCode = devBean.getCode();
				String[] varCode = new String[7];
				varCode[ClockBean.MASTER] = prop.getProperty("master");
				varCode[ClockBean.YEAR] = prop.getProperty("year");
				varCode[ClockBean.MONTH] = prop.getProperty("month");
				varCode[ClockBean.DAY] = prop.getProperty("day");
				varCode[ClockBean.WEEKDAY] = prop.getProperty("weekday");
				varCode[ClockBean.HOUR] = prop.getProperty("hour");
				varCode[ClockBean.MINUTE] = prop.getProperty("minute");
				for(int i=0;i<varCode.length;i++)
				{
					if(varCode[i].equals(""))
						varCode[i] = null;
				}
				ClockBean bean = new ClockBean(devCode,varCode);
				clockBeanList.save(bean);
			}
		}
		if("remove".equalsIgnoreCase(cmd))
		{
			int iddevmdl = Integer.valueOf(prop.getProperty("iddevmdl"));
			clockBeanList.removeClock(iddevmdl);
		}
	}
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        StringBuffer response = new StringBuffer("<response>");
        response.append(executeTab1Data(us,prop));
        response.append("</response>");
        return response.toString();
    }
	public String executeTab1Data(UserSession us, Properties prop) throws Exception
	{
		StringBuffer response = new StringBuffer();
		String cmd = prop.getProperty("cmd");
        String lang = us.getLanguage();
        int idsite = us.getIdSite();
        ClockBeanList clockBeanList = new ClockBeanList();
        if("modifyModel".equalsIgnoreCase(cmd))
        {
        	int iddevmdl = Integer.parseInt(prop.getProperty("iddevmdl")) ;
        	ClockBean clock = clockBeanList.getClockByIddevmdl(iddevmdl,lang);
        	if(clock != null)
        	{
        		String[] varCode = clock.getVarCode();
        		String[] varDescription = clock.getVarDescription();
        		response.append("<clock>");
        		response.append("<devDescription><![CDATA["+clock.getDevDescription()+"]]></devDescription>");
        		response.append("<varCode>");
        		response.append("<master><![CDATA["+(varCode[ClockBean.MASTER]!=null?varCode[ClockBean.MASTER]:"")+"]]></master>");
        		response.append("<year><![CDATA["+(varCode[ClockBean.YEAR]!=null?varCode[ClockBean.YEAR]:"")+"]]></year>");
        		response.append("<month><![CDATA["+(varCode[ClockBean.MONTH]!=null?varCode[ClockBean.MONTH]:"")+"]]></month>");
        		response.append("<day><![CDATA["+(varCode[ClockBean.DAY]!=null?varCode[ClockBean.DAY]:"")+"]]></day>");
        		response.append("<weekday><![CDATA["+(varCode[ClockBean.WEEKDAY]!=null?varCode[ClockBean.WEEKDAY]:"")+"]]></weekday>");
        		response.append("<hour><![CDATA["+(varCode[ClockBean.HOUR]!=null?varCode[ClockBean.HOUR]:"")+"]]></hour>");
        		response.append("<minute><![CDATA["+(varCode[ClockBean.MINUTE]!=null?varCode[ClockBean.MINUTE]:"")+"]]></minute>");
        		response.append("</varCode>");
        		response.append("<varDescription>");
        		response.append("<master><![CDATA["+(varDescription[ClockBean.MASTER]!=null?varDescription[ClockBean.MASTER]:"")+"]]></master>");
        		response.append("<year><![CDATA["+(varDescription[ClockBean.YEAR]!=null?varDescription[ClockBean.YEAR]:"")+"]]></year>");
        		response.append("<month><![CDATA["+(varDescription[ClockBean.MONTH]!=null?varDescription[ClockBean.MONTH]:"")+"]]></month>");
        		response.append("<day><![CDATA["+(varDescription[ClockBean.DAY]!=null?varDescription[ClockBean.DAY]:"")+"]]></day>");
        		response.append("<weekday><![CDATA["+(varDescription[ClockBean.WEEKDAY]!=null?varDescription[ClockBean.WEEKDAY]:"")+"]]></weekday>");
        		response.append("<hour><![CDATA["+(varDescription[ClockBean.HOUR]!=null?varDescription[ClockBean.HOUR]:"")+"]]></hour>");
        		response.append("<minute><![CDATA["+(varDescription[ClockBean.MINUTE]!=null?varDescription[ClockBean.MINUTE]:"")+"]]></minute>");
        		response.append("</varDescription>");
        		response.append("</clock>");
        		cmd = "loadvars";
        	}
        }
        if ("loadvars".equalsIgnoreCase(cmd))
        {
        	int iddevmdl = Integer.parseInt(prop.getProperty("iddevmdl")) ;
        	VarMdlBean[] list = VarMdlBeanList.retrieveOrdered(idsite,iddevmdl,lang);
        	VarMdlBean tmp = null;
        	for (int i = 0; i < list.length; i++)
        	{
        		tmp = list[i];
        		if(tmp.getType() == VariableInfo.TYPE_ALARM || tmp.getType() == VariableInfo.TYPE_ANALOGIC)
        			continue;
        		response.append("<var id='" + tmp.getIdvarmdl()+"'>");
        		response.append("<descr><![CDATA[" + tmp.getDescription()+"]]></descr>");
        		response.append("<type><![CDATA[" + tmp.getType()+"]]></type>");
                response.append("<rw><![CDATA[" + tmp.getReadwrite()+ "]]></rw>");
                response.append("<varcode><![CDATA[" + tmp.getCode()+"]]></varcode>");
                response.append("</var>");
        	}
        }
        return response.toString();
	}
}
