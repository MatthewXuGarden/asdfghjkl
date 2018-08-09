package com.carel.supervisor.presentation.bean;

import java.io.Serializable;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DeviceStructure implements Serializable
{
    private int idDevice = 0;
    private int idDevMdl = 0;
    private int idGroup = 0;
    private String description = "";
    private String groupName = "";
    private String code = "";
    private String image = "";
    private int idSite = 0;
    private VarphyBean[] varphyBean = null;
    private boolean isLogic = false;
    
    // SDK
    private int addressIn = -1;

    public DeviceStructure(int idSite, int idDevice, int idGroup, String code,
            String description, String groupName, 
            String imageDevice)
        {
        	this.idSite = idSite;
            this.idDevice = idDevice;
            this.idGroup = idGroup;
            this.description = description;
            this.groupName = groupName;
            this.code = code;
            this.image = imageDevice;
        }
    
    public void setIdDevMdl(int id){
    	this.idDevMdl = id;
    }
    
    public int getIdDevMdl() {
    	return this.idDevMdl;
    }
    
    public String getImageDevice()
    {
        return this.image;
    }

    public void setImageDevice(String imgpath)
    {
        this.image = imgpath;
    }
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public int getIdDevice()
    {
        return idDevice;
    }

    public void setIdDevice(int idDevice)
    {
        this.idDevice = idDevice;
    }

    public int getIdGroup()
    {
        return idGroup;
    }

    public void setIdGroup(int idGroup)
    {
        this.idGroup = idGroup;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void loadVariables(String languagecode, int site) throws Exception
    {
    	varphyBean = VarphyBeanList.getMainListVarToDisplay(languagecode,
                site, new int[]{idDevice});
    }
    
    public VarphyBean[] getVariables()
    {
    	return varphyBean;
    }
    
    public void setIsLogic(boolean logic) {
    	this.isLogic = logic;
    }
    
    public boolean isLogic() {
    	return this.isLogic;
    }
    
    // SDK
    public void setAddressIn(int addr) {
        this.addressIn = addr;
    }
    
    public int getAddressIn() {
        return this.addressIn;
    }
}
