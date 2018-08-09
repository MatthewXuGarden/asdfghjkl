package com.carel.supervisor.presentation.menu;

import java.util.Vector;


public class MenuSection
{
    private Vector listVoci = null;
    private String name = "";
    private String img = "";

    public MenuSection(String name, String img)
    {
        this.name = name;
        this.img = img;
        this.listVoci = new Vector();
    }

    public String getSectionName()
    {
        return this.name;
    }

    public String getSectionImg()
    {
        return this.img;
    }

    public void setSectionName(String name)
    {
        this.name = name;
    }

    public void setSectionImg(String img)
    {
        this.img = img;
    }

    public void addVoce(MenuVoce mv)
    {
        if (listVoci == null)
        {
            listVoci = new Vector();
        }

        listVoci.addElement(mv);
    }

    public MenuVoce[] getListVoci()
    {
        MenuVoce[] list = new MenuVoce[listVoci.size()];

        for (int i = 0; i < list.length; i++)
        {
            list[i] = (MenuVoce) listVoci.elementAt(i);
        }

        return list;
    }
}
