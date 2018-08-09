package com.carel.supervisor.presentation.menu;

import java.util.*;


public class Section
{
    private List caption = new ArrayList();
    private List link = new ArrayList();
    private String imgName = null;
    private String sectionName = null;
    private String name = null;

    public Section(String sectionName, String imgName,String name)
    {
        this.sectionName = sectionName;
        this.imgName = imgName;
        this.name = name;
    }

    public void add(String caption, String link)
    {
        this.caption.add(caption);
        this.link.add(link);
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public String getImg()
    {
        return imgName;
    }

    public String getCaption(int i)
    {
        return (String) caption.get(i);
    }

    public String getLink(int i)
    {
        return (String) link.get(i);
    }

    public int size()
    {
        return caption.size();
    }
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
