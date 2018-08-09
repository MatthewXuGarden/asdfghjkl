package com.carel.supervisor.controller.pagelinks;

public class SingleLink
{
	// singolo link da un tab:
	private String target = null;	
	private Integer perm = null;
	
	public SingleLink(String target, Integer perm)
	{
		this.target = target;
		this.perm = perm;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getPerm() {
		return perm;
	}

	public void setPerm(Integer perm) {
		this.perm = perm;
	}
}
