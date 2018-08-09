package com.carel.supervisor.ide.dc.DbModel;

public class DeviceModbusInfoDb {

	private int stopBit = 1;
	private int parity = 0;
	private int readTimeOut = 50;
	private int writeTimeOut = 100;
	private int activityTimeOut = 0;
	private int f1 = 0;
	private int f2 = 0;
	private int f3 = 1;
	private int f4 = 0;
	private int f5 = 0;
	private int f6 = 1;
	private int f15 = 0;
	private int f16 = 0;
	
	public void fillInformations(int stopBit, int parity, int readTimeOut, int writeTimeOut, int activityTimeOut, int f1, int f2, int f3, int f4, int f5, int f6, int f15, int f16)
	{
		this.stopBit = stopBit;
		this.parity = parity;
		this.readTimeOut = readTimeOut;
		this.writeTimeOut = writeTimeOut;
		this.activityTimeOut = activityTimeOut;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
		this.f6 = f6;
		this.f15 = f15;
		this.f16 = f16;

	}
	public int getStopBit() {
		return stopBit;
	}
	public void setStopBit(int stopBit) {
		this.stopBit = stopBit;
	}
	public int getParity() {
		return parity;
	}
	public void setParity(int parity) {
		this.parity = parity;
	}
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}
	public int getWriteTimeOut() {
		return writeTimeOut;
	}
	public void setWriteTimeOut(int writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
	}
	public int getActivityTimeOut() {
		return activityTimeOut;
	}
	public void setActivityTimeOut(int activityTimeOut) {
		this.activityTimeOut = activityTimeOut;
	}
	public int getF1() {
		return f1;
	}
	public void setF1(int f1) {
		this.f1 = f1;
	}
	public int getF2() {
		return f2;
	}
	public void setF2(int f2) {
		this.f2 = f2;
	}
	public int getF3() {
		return f3;
	}
	public void setF3(int f3) {
		this.f3 = f3;
	}
	public int getF4() {
		return f4;
	}
	public void setF4(int f4) {
		this.f4 = f4;
	}
	public int getF5() {
		return f5;
	}
	public void setF5(int f5) {
		this.f5 = f5;
	}
	public int getF6() {
		return f6;
	}
	public void setF6(int f6) {
		this.f6 = f6;
	}
	public int getF15() {
		return f15;
	}
	public void setF15(int f15) {
		this.f15 = f15;
	}
	public int getF16() {
		return f16;
	}
	public void setF16(int f16) {
		this.f16 = f16;
	}

	

	
}
