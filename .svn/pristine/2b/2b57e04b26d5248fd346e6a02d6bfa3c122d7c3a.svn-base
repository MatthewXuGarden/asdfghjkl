package com.carel.supervisor.presentation.menu;

import java.util.ArrayList;
import java.util.List;


public class MenuTab
{
    int numTab = 0;
    String idTrx = "";
    List listTab = null;

    public MenuTab(String idTab, int numTab)
    {
        this.numTab = numTab;
        this.idTrx = idTab;
        this.listTab = new ArrayList();
    }

    public MenuTab(String idTab, List list)
    {
        this.idTrx = idTab;
        this.listTab = list;
    }

    public String getIdTab()
    {
        return this.idTrx;
    }

    public int getNumTab()
    {
        return this.numTab;
    }

    public void addTab(TabObj to)
    {
        this.listTab.add(to);
    }

    public TabObj getTab(int index)
    {
        return (TabObj) listTab.get(index);
    }
}
