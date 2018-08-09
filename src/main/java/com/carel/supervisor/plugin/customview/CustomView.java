package com.carel.supervisor.plugin.customview;

import com.carel.supervisor.dataaccess.datalog.impl.CustomViewBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.CustomViewBttBean;

public class CustomView 
{
	private int idmdl = 0;
	private String code = "";
	private String target = "";
	private String javascript = "";
	private String events = "";
	private String business = "";
	private CustomViewBtt[] bttList = null;
	
	public CustomView(int c,String d,String t,String j,String e,String b)
	{
		this.idmdl = c;
		this.code = d;
		this.target = t;
		this.javascript = j;
		this.events = e;
		this.business = b;
		this.bttList = new CustomViewBtt[0]; 
	}

	public int getIdMdl() {
		return idmdl;
	}

	public String getEvents() {
		return events;
	}

	public String[] getJavascript() 
	{
		String[] arJs = null;
		if (javascript != null)
            arJs = javascript.split(";");
        else
            arJs = new String[0];
		return arJs;
	}

	public String getTarget() {
		return target;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getBusiness() {
		return business;
	}
	
	public void loadBttAction(int idSite)
	{
		CustomViewBttBean[] list = CustomViewBeanList.getCustomViewBttList(idSite);
		if(list != null)
		{
			this.bttList = new CustomViewBtt[list.length];
			for(int i=0; i<list.length; i++) 
			{
				this.bttList[i] = new CustomViewBtt(list[i].getName(),list[i].getAction(),list[i].getTooltip(),
								  list[i].getStandby(),list[i].getOver(),list[i].getClick(),list[i].getDisable());
			}
		}
	}
	
	public CustomViewBtt[] getBttList() {
		return this.bttList;
	}
}