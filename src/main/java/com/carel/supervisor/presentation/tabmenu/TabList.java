package com.carel.supervisor.presentation.tabmenu;

import java.util.*;

import com.carel.supervisor.presentation.session.UserSession;


public class TabList
{
	public static final String TECH_TAB = "grpview";
    private List tabList = null;
    private boolean enable = true;

    public TabList()
    {
        tabList = new ArrayList();
        this.enable = true;
    }

    public TabList(boolean enable)
    {
        tabList = new ArrayList();
        this.enable = enable;
    }

    public void addTab(Tab a)
    {
        tabList.add(a);
    }

    public void removeTab(Tab a)
    {
        tabList.remove(a);
    }

    public int numTab()
    {
        return tabList.size();
    }

    public Tab getTab(int i)
    {
        return ((Tab) tabList.get(i));
    }

    public String getHTMLTabList(UserSession userSess,String path, String param)
    {
        StringBuffer html = new StringBuffer();
        html.append("<table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">");
        //Kevin: the old is valign="baseline", but in Firefox some tabs are abnormal, so change it to valign="top"
        html.append("<tr valign=\"top\" align=\"left\">");
        boolean setAction = true;
        String tdWidth = "15%";
        
        for (int i = 0; i < numTab(); i++)
        {
            String tabName = getTab(i).getTabName();
            String tooltip = getTab(i).getTooltip();
            String pageLink = getTab(i).getPageLink();
            setAction = true;
            
            if((path != null) && (path.equalsIgnoreCase(TECH_TAB)) && (i==1))
            	setAction = false;
            	
            if (numTab() > 6)
            { // serve per evitare che il separatore fra un tab e l'altro venga nascosto
                tdWidth = "14%";
            }
            html.append("<td width=" + tdWidth + " title=\"" + tooltip + "\" align=\"center\">");

            html.append("<div class=\"tabDivStyle\" id=\"tab_" + i +
                "\" style=\"height:26px;background-image:url(images/tab/taboff.png)\"");
            //simon add in 2010-1-27 for the function of redirect jump
            if(getTab(i).getPage().equals(userSess.getProfileRedirect().getRePage())){
            	userSess.getProfileRedirect().setReTabId("tab_" + i);
            }
            if(String.valueOf(i+1).equals(userSess.getProfileRedirect().getReTabNum())){
            	userSess.getProfileRedirect().setReTabId("tab_" + i);
            }
            
            if (enable)
            {
            	
            	/*if(setAction)
            	{*/
	                html.append("onclick =\"MT_MouseClick('','" + path + "/" + pageLink + param +
	                    "',this);\"");
	                html.append("onmouseover=\"MT_onMouseOver(this)\"");
	                html.append("onmouseout=\"MT_onMouseOut(this)\"");
            	//}
            }

            html.append(">");
            html.append(tabName);
            html.append("</div>");
            html.append("</td>");
            html.append("<td valign=\"top\" style=\"width:2px;\">");
            html.append(
                "<div style=\"height:26px;background-image:url(images/tab/tabsep.png)\"></div>");
            html.append("</td>");
        }

        html.append("<td width=\"*\">");
        html.append("<div valign=\"top\" style=\"height:26px;\">&nbsp;</div>");
        html.append("</td>");
        html.append("</tr>");
        html.append("</table>");

        return html.toString();
    }
}
