package com.carel.supervisor.presentation.devices;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;

public class DeviceDetail 
{
	private int idDevice = -1;
	private int idDevMdl = -1;
	private String title = "";
	private String image = "";
	private VarphyBean[] listVarphy = null;
	
	public DeviceDetail()
	{
		this.title = "";
		this.image = "";
		this.listVarphy = new VarphyBean[0];
	}
	
	public void setIdDevice(int id) {
		this.idDevice = id;
	}
	
	public void setIdDevMdl(int id) {
		this.idDevMdl = id;
	}
	
	public void setTitle(String val) {
		this.title = val;
	}
	
	public void setImage(String val) {
		this.image = val;
	}
	
	public int getIdDevice() {
		return this.idDevice;
	}
	
	public int getIdDevMdl() {
		return this.idDevMdl;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void loadDeviceVariable(String lang,int idSite)
		throws Exception
	{
		int i = 0;
		int[] iArDevice = {getIdDevice()};
        
		VarphyBean[] listVarphy1 = VarphyBeanList.getListVarToDisplay(lang,idSite,iArDevice);
        VarphyBean[] listVarphy2 = VarphyBeanList.getMainListVarToDisplay(lang,idSite,iArDevice);
        VarphyBean[] listVarphy3 = VarphyBeanList.getStateListVarToDisplay(lang,idSite,iArDevice);
        
        this.listVarphy = new VarphyBean[listVarphy1.length + listVarphy2.length + listVarphy3.length];
        

        /*
         * Load HOME variables
         */ 
        for (i = 0; i < listVarphy1.length; i++)
        	this.listVarphy[i] = listVarphy1[i];

        /*
         * Load MAIN variables+
         */
        for (int j = 0; j < listVarphy2.length; j++)
        {
        	this.listVarphy[i] = listVarphy2[j];
            i++;
        }

        /*
         * Load STATE variables
         */
        for (int k = 0; k < listVarphy3.length; k++)
        {
        	this.listVarphy[i] = listVarphy3[k];
            i++;
        }
	}
	
	public VarphyBean[] getVariablesList() {
		return this.listVarphy;
	}
}
