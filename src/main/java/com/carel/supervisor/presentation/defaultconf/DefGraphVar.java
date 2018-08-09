package com.carel.supervisor.presentation.defaultconf;

public class DefGraphVar {
	private int iddevmdl = 0;
	private int idvarmdl = 0;
	private boolean isDefault = false;
	private boolean isVisible = false;
	private boolean isHaccp = false;
	private String color = "";
	private float min = 0.0F;
	private float max = 0.0F;
	private int order = 0;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getIddevmdl() {
		return iddevmdl;
	}

	public void setIddevmdl(int iddevmdl) {
		this.iddevmdl = iddevmdl;
	}

	public int getIdvarmdl() {
		return idvarmdl;
	}

	public void setIdvarmdl(int idvarmdl) {
		this.idvarmdl = idvarmdl;
	}

	public boolean isHaccp() {
		return isHaccp;
	}

	public void setHaccp(boolean isHaccp) {
		this.isHaccp = isHaccp;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
