package com.carel.supervisor.presentation.profile;



public class ProfileRedirectBean
{
    private boolean reDirect=false;
    private boolean reDefaultResolution=false;
    private String reFolder="";
    private String rePage="";
    private String reTabNum="";
    private String reTabId="";
    
    
	public boolean isReDirect() {
		return reDirect;
	}
	public String getRePage() {
		return rePage;
	}
	public String getReTabNum() {
		return reTabNum;
	}
	public String getReTabId() {
		return reTabId;
	}
	public void setReDirect(boolean reDirect) {
		this.reDirect = reDirect;
	}
	public void setRePage(String rePage) {
		this.rePage = rePage;
	}
	public void setReTabNum(String reTabNum) {
		this.reTabNum = reTabNum;
	}
	public void setReTabId(String reTabId) {
		this.reTabId = reTabId;
	}
	public String getReFolder() {
		return reFolder;
	}
	public void setReFolder(String reFolder) {
		this.reFolder = reFolder;
	}
	public boolean isReDefaultResolution() {
		return reDefaultResolution;
	}
	public void setReDefaultResolution(boolean reDefaultResolution) {
		this.reDefaultResolution = reDefaultResolution;
	}

 
}
