package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.plugin.customview.CustomView;
import com.carel.supervisor.plugin.customview.CustomViewBtt;
import com.carel.supervisor.plugin.customview.CustomViewMgr;
import com.carel.supervisor.presentation.tabmenu.SubTab;
import com.carel.supervisor.presentation.tabmenu.SubTabList;

public class CustomBeanList 
{
	public static String getCustomButton(String language,int idDevMdl)
	{
		SubTabList listaSubTab = new SubTabList();
		SubTab subTab = null;
		String ret = "";
		CustomView custom = CustomViewMgr.getInstance().hasDeviceCustomView(idDevMdl);
		if(custom != null)
		{
			CustomViewBtt[] list = custom.getBttList();
			if(list != null)
			{
				for(int i=0; i<list.length; i++) 
				{
					subTab = new SubTab(language,custom.getCode());
					subTab.setName(list[i].getName());
                    subTab.setAction(list[i].getAction());
                    subTab.setTooltip(list[i].getTooltip());
                    subTab.setPathIconStandby(list[i].getStandby());
                    subTab.setPathIconOver(list[i].getOver());
                    subTab.setPathIconDisabled(list[i].getDisable());
                    /*
                     *  Travaglin
                     *  Modifica per compatibilita Pulsante con Descrizione 
                     */
                    subTab.setDescription(list[i].getName());
                    
                    listaSubTab.addSubTab(subTab);
				}
				ret = listaSubTab.getHTMLSubTabList();
			}
		}
		return ret ;
	}
}
