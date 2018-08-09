package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.device.DeviceStatusMgr;

public class BookletDevVarBean
{
//	private static final String IDBooklet = "idbooklet";
//	private static final String IdSite = "idsite";
//	private static final String Iduser = "iduser";
//	private static final String Idsite = "idsite";
	private static final String Iddevice = "iddevice";
	private static final String Idvariable = "idvariable";
//	private static final String Lastupdate = "lastupdate";
	private static final String Device = "device";
	private static final String Variable = "variable";
	private static final String VariableCode = "varcode";
	private static final String Cabinet = "cabinet";
//	private static final String GlobalIndex = "globalindex";
	private int idBooklet = -1;
	private int idDev = -1;
	private String devDesc = "";
	private int idVar = -1;
	private String varDesc = "";
	private String varValue = "";
	private String varCode = "";
	private int idSite=-1;
	private String cabinet = "";

    public BookletDevVarBean()
    {
    }

    public BookletDevVarBean(int idSite,int idDev,String devDesc,int idVar,String varDesc,String varCode) throws DataBaseException
    {
    	this.idSite=idSite;
        this.idDev=idDev;
        this.devDesc=devDesc;
        this.idVar=idVar;
        this.varDesc=varDesc;
        this.varCode=varCode;
    }
    public BookletDevVarBean( Record record, int site, String language)throws Exception{
//	    this.idBooklet=((Integer) record.get(IDBooklet)).intValue();
//	    this.idSite=((Integer) record.get(IdSite)).intValue();
	    this.idDev=((Integer) record.get(Iddevice)).intValue();
	    this.devDesc=(String) record.get(Device);
	    this.idVar=((Integer) record.get(Idvariable)).intValue();
	    this.varDesc=(String) record.get(Variable);
	    this.varCode=(String) record.get(VariableCode);
//	    String lang=(String) record.get("languagecode");
//	    int idSite=((Integer) record.get("idsite")).intValue();
	    this.varValue=getPhyValue(this.idVar);
	}
    
    private String getPhyValue(int idVar){
    	String ret="***";
    	try{
	        //VarphyBean varphyBean = VarphyBeanList.retrieveVarById(idSite, idVar,lang);
	        if (DeviceStatusMgr.getInstance().isOffLineDevice(idDev)){
	            ret="***";
	        }else{
	            ret=ControllerMgr.getInstance().getFromField(idVar).getFormattedValue();
	        }
    	}catch (Exception e) {
        	ret="***";
        }
    	return ret;
    }
    
    public int getIdDev() {
		return idDev;
	}

	public void setIdDev(int idDev) {
		this.idDev = idDev;
	}

	public String getDevDesc() {
		return devDesc;
	}

	public void setDevDesc(String devDesc) {
		this.devDesc = devDesc;
	}

	public int getIdVar() {
		return idVar;
	}

	public void setIdVar(int idVar) {
		this.idVar = idVar;
	}

	public String getVarDesc() {
		return varDesc;
	}

	public void setVarDesc(String varDesc) {
		this.varDesc = varDesc;
	}
    public int getIdBooklet() {
		return idBooklet;
	}

	public void setIdBooklet(int idBooklet) {
		this.idBooklet = idBooklet;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public int getIdSite() {
		return idSite;
	}

	public void setIdSite(int idSite) {
		this.idSite = idSite;
	}

	public String getVarCode() {
		return varCode;
	}

	public void setVarCode(String vCode) {
		this.varCode = vCode;
	}

	public String getCabinet() {
		return cabinet;
	}

	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}
	
}
