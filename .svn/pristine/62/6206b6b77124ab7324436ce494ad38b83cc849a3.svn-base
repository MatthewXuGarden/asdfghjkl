package com.carel.supervisor.presentation.tabmenu;

import java.util.*;


public class SubTabList
{
    private List subTabList = null;

    public SubTabList()
    {
        subTabList = new ArrayList();
    }

    public void addSubTab(SubTab a)
    {
        subTabList.add(a);
    }

    public void removeSubTab(SubTab a)
    {
        subTabList.remove(a);
    }

    public SubTab getSubTab(int i)
    {
        return ((SubTab) subTabList.get(i));
    }

    public int numSubTabs()
    {
        return subTabList.size();
    }

    public String getHTMLSubTabList()
    {
        StringBuffer html = new StringBuffer();
        html.append("\n <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        html.append("\n<tr valign=\"top\" align=\"left\">");
        int nTabs = numSubTabs();

        for (int i = 0; i < nTabs; i++)
        {
            String SubTabName = getSubTab(i).getName();
            String SubTabStandBy = getSubTab(i).getPathIconStandby();
            String SubTabAction = getSubTab(i).getAction();
            String SubTabTooltip = getSubTab(i).getTooltip();
            String SubTabOver = getSubTab(i).getPathIconOver();
            String SubTabDisa = getSubTab(i).getPathIconDisabled();
            boolean visible = getSubTab(i).getVisible();

            String subTabDesc = getSubTab(i).getDescription();
            
            html.append("\n <td valign=\"top\" align=\"center\" style=\"cursor:pointer;"+(visible?"":"visibility:hidden;display:none")+"\" title=\""+SubTabTooltip+"\" id=\""+SubTabName+"\">");
            
            html.append("<div id=\"ActBttDivEn"+(i+1)+"\" ");
            html.append("onclick=\""+SubTabAction+";\"");
            html.append("onmouseover=\"overAction("+(i+1)+",'"+SubTabOver+"')\" onmouseout=\"outAction("+(i+1)+",'"+SubTabStandBy+"')\"");
            html.append("class='ActBttDivEnabled' ");
            html.append(">");
            html.append("<img class=\"actBttImg\" border=\"0\"id=\"ActBttImgEn"+(i+1)+"\" src=\""+SubTabStandBy+"\"/>");
            html.append("<div id=\"ActBttDivTxtEn"+(i+1)+"\" class=\"actBttDivTxt\">");
            html.append(subTabDesc);
            html.append("</div>");

            html.append("</div>");
            
            html.append("<div id=\"ActBttDivDs"+(i+1)+"\" style=\"visibility:hidden;display:none;\" ");
            html.append("class='ActBttDivDisabled' ");
            html.append(">");
            html.append("<img class=\"actBttImg\" border=\"0\" id=\"ActBttImgDs"+(i+1)+"\" src=\""+SubTabDisa+"\"/>" );
            html.append("<div id=\"ActBttDivTxtDs"+(i+1)+"\" class=\"actBttDivTxt\">");
            html.append(subTabDesc);
            html.append("</div>");  

            html.append("</div>");
            
            html.append("</td>");
            
            // include the separator only if the current button and the next button (if exists) are visible
            boolean nextVisible = (i+1 < nTabs) ? getSubTab(i+1).getVisible() : false;
            if (visible && i < nTabs-1 && nextVisible)
            	html.append("<td style=\"width:2px;\"><img src=\"images/tab/tabbtnsep.png\" border=\"0\"/></td>");
        }

        html.append("\n</tr>");
        html.append("\n</table>");

        return html.toString();
    }
}
