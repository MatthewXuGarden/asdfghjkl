package com.carel.supervisor.director.bms;

public class SubsetKey {

	private String idss;
	private Integer iddevmdl;

	public SubsetKey(Integer iddevmdl, String idss) {
		this.setMdlcode(iddevmdl);
		this.setIdss(idss);
	}

	public void setMdlcode(Integer mdlcode) {
		this.iddevmdl = mdlcode;
	}

	public Integer getMdlcode() {
		return iddevmdl;
	}

	public void setIdss(String idss) {
		this.idss = idss;
	}

	public String getIdss() {
		return idss;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SubsetKey){
			return ((SubsetKey)obj).getMdlcode().equals(iddevmdl)&&
			((SubsetKey)obj).getIdss().equals(idss);
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		if (iddevmdl != null)
			hash = hash * prime + iddevmdl.hashCode();
		if (idss != null)
			hash = hash * prime + idss.hashCode();
		return hash;
	}
}

