package com.carel.supervisor.director.vscheduler;

import com.carel.supervisor.base.xml.XMLNode;


public class CCommand {
	// sender
	private String strUserName;
	// destination
	private String strIdCat;
	private String strIdGroup;
	
	private String strType;
	private boolean bDVal;
	private float fAVal;
	
	// hold & fake command flags; used to enter autoreset commands
	private boolean bHold;
	private boolean bFake;
	
	
	public CCommand()
	{
		strType = "";
		bDVal = false;
		fAVal = 0;
		bHold = false;
		bFake = false;
	}
	
	
	public CCommand(String strType)
	{
		this.strType = strType;
		bDVal = false;
		fAVal = 0;
		bHold = false;
		bFake = false;
	}
	
	
	public void loadXML(XMLNode xmlCommand)
	{
		strIdCat = xmlCommand.getAttribute(CDataDef.strIdCatAttr);
		strIdGroup = xmlCommand.getAttribute(CDataDef.strIdGroupAttr);
		strType = xmlCommand.getAttribute(CDataDef.strTypeAttr);
		if( strType.equals(CDataDef.strDigitalVal) || strType.equals(CDataDef.strMixedVal) )
			bDVal = Integer.parseInt(xmlCommand.getAttribute(CDataDef.strDigitalValAttr)) != 0;
		if( strType.equals(CDataDef.strMixedVal) || strType.equals(CDataDef.strAnalogVal) )
			fAVal = Float.parseFloat(xmlCommand.getAttribute(CDataDef.strAnalogValAttr));
	}
	
	
	public void setUserName(String strUserName)
	{
		this.strUserName = strUserName;
	}
	
	public String getUserName()
	{
		return strUserName;
	}
	
	
	public void setIdCat(String strIdCat)
	{
		this.strIdCat = strIdCat;
	}
	
	public String getIdCat()
	{
		return strIdCat;
	}
	
	
	public void setIdGroup(String strIdGroup)
	{
		this.strIdGroup = strIdGroup;
	}
	
	public String getIdGroup()
	{
		return strIdGroup;
	}
	
	
	public void setType(String strType)
	{
		this.strType = strType;
	}
	
	public String getType()
	{
		return strType;
	}
	
	
	public void setDVal(boolean b)
	{
		bDVal = b;
	}
	
	public boolean getDVal()
	{
		return bDVal;
	}
	
	
	public void setAVal(float n)
	{
		fAVal = n;
	}
	
	public float getAVal()
	{
		return fAVal;
	}
	
	
	public void setHold()
	{
		bHold = true;
	}
	
	public boolean isHold()
	{
		return bHold;
	}
	
	
	public void setFake()
	{
		bHold = false;
		bFake = true;
	}
	
	public boolean isFake()
	{
		return bFake;
	}
	
}
