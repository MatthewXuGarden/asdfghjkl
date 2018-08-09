package com.carel.supervisor.plugin.customview;

import com.carel.supervisor.dataaccess.datalog.impl.CustomViewBean;
import com.carel.supervisor.dataaccess.datalog.impl.CustomViewBeanList;

public class CustomViewMgr 
{
	private static CustomViewMgr cview = new CustomViewMgr();
	private CustomView[] viewList = null; 
	
	private CustomViewMgr()
	{
		viewList = new CustomView[0];
		loadCustomView(1);
	}
	
	public static CustomViewMgr getInstance() {
		return cview;
	}
	
	public CustomView hasDeviceCustomView(int mdl)
	{
		CustomView find = null;
		if(viewList != null)
		{
			for(int i=0; i<viewList.length; i++)
			{
				if(viewList[i] != null && viewList[i].getIdMdl() == mdl)
				{
					find = viewList[i];
					break;
				}
			}
		}
		return find;
	}
	
	private void loadCustomView(int idSite)
	{
		CustomViewBean[] list = CustomViewBeanList.getCustomViewList(1);
		if(list != null)
		{
			viewList = new CustomView[list.length];
			for(int i=0; i<list.length; i++)
			{
				viewList[i] = new CustomView(list[i].getMdl(),list[i].getCode(),list[i].getTarget(),
											 list[i].getJavascript(),list[i].getEvents(),list[i].getBusiness());
				
				viewList[i].loadBttAction(idSite);
			}
		}
	}
}
