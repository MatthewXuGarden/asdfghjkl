package com.carel.supervisor.presentation.menu;

import java.util.ArrayList;
import java.util.List;


public class MainMenu
{
    private Group group = null;
    private String exitLabel = "";
    private String exitQuest = "";

    public static int MENU_HEIGHT = 400;
    public static int  MENU_WIDTH = 340;
    public static int  HEAD_HEIGHT = 40;
    public static int  ITEM_HEIGHT = 30;
    public MainMenu()
    {
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
    	this.group = group;
    }
    
    public void setExitLabel(String l)
    {
        exitLabel = l;
    }

    public String getExitLabel()
    {
        return exitLabel;
    }

    public void setExitQuest(String l)
    {
        exitQuest = l;
    }

    public String getExitQuest()
    {
        return exitQuest;
    }

    public String createJavaScript()
    {
        Group group = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("function init()\n\t{\n\t\tvar o;\n");

        group = getGroup();
        if(group != null)
        	buffer.append(innerCreateJavascript(group));

        buffer.append("\t}");
        return buffer.toString();
    }
    
    
    private String innerCreateJavascript(Group group)
    {
    	Section section = null;
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("\t\t\tmenuMain = new MenuMain(");
    	buffer.append(group.size());
    	buffer.append(",'" + getExitLabel() + "'");
    	buffer.append(",'" + getExitQuest() + "'");
    	buffer.append(");\n");
    	
    	for (int j = 0; j < group.size(); j++)
    	{
    		section = group.getSection(j);
    		buffer.append("\t\t\to = addMenu(\"");
    		buffer.append(section.getSectionName());
    		buffer.append("\", ");
    		buffer.append(section.size());
    		buffer.append(", \"");
    		buffer.append(section.getImg());
    		buffer.append("\", \"");
    		buffer.append(getBgImgColor(section.getName()));
    		buffer.append("\");\n");

    		for (int l = 0; l < section.size(); l++)
    		{
    			buffer.append("\t\t\taddItem(o,\"");
    			buffer.append(section.getCaption(l));
    			buffer.append("\",\"");
    			buffer.append(section.getLink(l));
    			buffer.append("\");\n");
    		}
    	}
    	return buffer.toString();
    }
    private String getBgImgColor(String section)
    {
    	String result = "";
    	if(section.equals("section5"))
    	{
    		result = "violet\",\"773281";
    	}
    	else if(section.equals("section2"))
    	{
    		result = "red\",\"a91421";
    	}
    	else if(section.equals("section3"))
    	{
    		result = "green\",\"91ab2d";
    	}
    	else if(section.equals("section4"))
    	{
    		result = "yellow\",\"efc92f";
    	}
    	else if(section.equals("section7"))
    	{
    		result = "orange\",\"de9523";
    	}
    	else if(section.equals("section8"))
    	{
    		result = "blue\",\"589bbe";
    	}
    	else if(section.equals("section9"))
    	{
    		result = "white\",\"ffffff";
    	}
    	return result;
    }
}
