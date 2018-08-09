package com.carel.supervisor.presentation.refresh;

public class RefreshBean
{
    private String idHtmlObj = "";
    private String jsToCall = "";
    private int idNumTable = -1;
    private int typeObj = -1;
    private boolean isLink=true;//simon add for device detail page alarms,2009-5-8

    public RefreshBean(String id, int type, int num)
    {
        this.idHtmlObj = id;
        this.typeObj = type;
        this.idNumTable = num;
    }
    public RefreshBean(String id, int type, int num,boolean link)
    {
        this.idHtmlObj = id;
        this.typeObj = type;
        this.idNumTable = num;
        this.isLink=link;
    }    

    public RefreshBean(String id, int type, int num, String jsToCall)
    {
        this(id, type, num);
        this.jsToCall = jsToCall;
    }

    public String getJsToCall()
    {
        return this.jsToCall;
    }

    public void setJsToCall(String js)
    {
        this.jsToCall = js;
    }

    public String getIdHtmlObj()
    {
        return idHtmlObj;
    }

    public void setIdHtmlObj(String idHtmlObj)
    {
        this.idHtmlObj = idHtmlObj;
    }

    public int getTypeObj()
    {
        return typeObj;
    }

    public void setTypeObj(int typeObj)
    {
        this.typeObj = typeObj;
    }

    public int getIdNumTable()
    {
        return idNumTable;
    }

    public void setIdNumTable(int idNumTable)
    {
        this.idNumTable = idNumTable;
    }

	public boolean isLink() {
		return isLink;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
}
