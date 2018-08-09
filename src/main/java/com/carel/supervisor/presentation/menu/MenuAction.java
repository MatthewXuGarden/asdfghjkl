package com.carel.supervisor.presentation.menu;

import java.util.ArrayList;
import java.util.List;


public class MenuAction
{
    int numAct = 0;
    String idTab = "";
    List listAct = null;

    public MenuAction(String idTab, int numAct)
    {
        this.idTab = idTab;
        this.numAct = numAct;
        this.listAct = new ArrayList();
    }

    public String getIdAct()
    {
        return this.idTab;
    }

    public int getNumAct()
    {
        return this.numAct;
    }

    public void addAct(ActionObj ao)
    {
        this.listAct.add(ao);
    }

    public ActionObj getAct(int index)
    {
        return (ActionObj) listAct.get(index);
    }
}
