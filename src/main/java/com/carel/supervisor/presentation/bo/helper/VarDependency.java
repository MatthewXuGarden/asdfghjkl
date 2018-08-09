package com.carel.supervisor.presentation.bo.helper;

import java.util.ArrayList;
import java.util.List;

public class VarDependency {
	
	int lineId;
	int devId;
	int varId;
	String deviceCode;
	String varDescription;
	String devDescription;
	String line;
	String actionType;
	String actionName;
	
	
	
	public VarDependency() {
	}



	public VarDependency(int lineId, int devId, int varId, String deviceCode,
			String varDescription, String devDescription, String line,
			String actionType, String actionName) {
		super();
		this.lineId = lineId;
		this.devId = devId;
		this.varId = varId;
		this.deviceCode = deviceCode;
		this.varDescription = varDescription;
		this.devDescription = devDescription;
		this.line = line;
		this.actionType = actionType;
		this.actionName = actionName;
	}



	public VarDependency(Integer iddevice, String devcode, String varDesc, String actType,String actName, List dlist ,int idvar) {
		String[] devInfos = getDevInfoBydevid(iddevice , dlist);
		if(devInfos[2]!=null)
			this.lineId =Integer.parseInt(devInfos[2]);
		this.devId = iddevice;
		this.varId = idvar;
		this.deviceCode=devInfos[0];
		this.varDescription = varDesc;
		this.devDescription = devInfos[1];
		this.actionType = actType;
		this.actionName = actName;
		this.line=devInfos[3];
	}
	
	public static ArrayList getVardpList(int devid2, String devCode,String varDesc, List<VarDpdHelper> vdhlist, List devList, int idvar) {
		ArrayList <VarDependency> list = new ArrayList<VarDependency>();
		for (VarDpdHelper obj : vdhlist) {
			if(obj.getVarId()==idvar){
				// some dependency just use device and not use variable such as booklet-cabinet(use -1 for idvar) .
				if(idvar == -1){
					if(devid2==obj.getDevId()){
						list.add(new VarDependency(devid2,devCode,varDesc,obj.getDpdType(),obj.getDpdName(),devList,idvar ));
					}
				}else{
					list.add(new VarDependency(devid2,devCode,varDesc,obj.getDpdType(),obj.getDpdName(),devList,idvar ));
				}
			}
		}
		return list;
	}


/*
	private String[] varDpInfosByvarId(int idvar, List<VarDpdHelper> vdhlist) {
		String[] varDPinfo = new String[2];
		for (VarDpdHelper obj : vdhlist) {
			if(obj.getVarId()==idvar){
				varDPinfo[0]=obj.getDpdType();
				varDPinfo[1]=obj.getDpdName();
				
			}
		}
		return varDPinfo;
	}

*/

	private String[] getDevInfoBydevid(Integer iddevice, List<DevDependency> dlist) {
		String[] devCodeDesc = new String[4];
		for (DevDependency obj : dlist) {
			if(obj.getDevid()==iddevice){
				devCodeDesc[0]=obj.getDevCode();
				devCodeDesc[1]=obj.getDevDesc();
				devCodeDesc[2]=obj.getIdline();
				devCodeDesc[3]=obj.getLinecode();
			}
		}
		
		return devCodeDesc;
	}



	public int getLineId() {
		return lineId;
	}



	public void setLineId(int lineId) {
		this.lineId = lineId;
	}



	public int getDevId() {
		return devId;
	}



	public void setDevId(int devId) {
		this.devId = devId;
	}



	public int getVarId() {
		return varId;
	}



	public void setVarId(int varId) {
		this.varId = varId;
	}



	public String getDeviceCode() {
		return deviceCode;
	}



	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}



	public String getVarDescription() {
		if(varDescription==null){
			varDescription = "";
		}
		return varDescription;
	}



	public void setVarDescription(String varDescription) {
		this.varDescription = varDescription;
	}



	public String getDevDescription() {
		return devDescription;
	}



	public void setDevDescription(String devDescription) {
		this.devDescription = devDescription;
	}



	public String getLine() {
		return line;
	}



	public void setLine(String line) {
		this.line = line;
	}



	public String getActionType() {
		return actionType;
	}



	public void setActionType(String actionType) {
		this.actionType = actionType;
	}



	public String getActionName() {
		return actionName;
	}



	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	

}
