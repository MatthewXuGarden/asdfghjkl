package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModbusTag extends XmlDAO {


	//<Modbus stopBit="0" parity="0" readTimeOut="0" writeTimeOut="0" activityTimeOut="0" f1="0" f2="0" f3="0" f4="0" f5="0" f6="0" f15="0" f16="0">

	protected static final String MODBUS_TAG = "Modbus";
	protected static final String VARS = "Vars";
	protected static final String STOP_BIT = "stopBit";
	protected static final String PARITY = "parity";
	protected static final String READ_TIME_OUT = "readTimeOut";
	protected static final String WRITE_TIME_OUT = "writeTimeOut";
	protected static final String ACTIVITY_TIME_OUT = "activityTimeOut";
	protected static final String F1 = "f1";
	protected static final String F2 = "f2";
	protected static final String F3 = "f3";
	protected static final String F4 = "f4";
	protected static final String F5 = "f5";
	protected static final String F6 = "f6";
	protected static final String F15 = "f15";
	protected static final String F16 = "f16";


	private int stopBit = 1;
	private int parity = 0;
	private int readTimeOut = 50;
	private int writeTimeOut = 100;
	private int activityTimeOut = 0;
	private int f1 = 1;
	private int f2 = 1;
	private int f3 = 1;
	private int f4 = 1;
	private int f5 = 1;
	private int f6 = 1;
	private int f15 = 0;
	private int f16 = 0;
	private VarsModbusTag varsModbus;

	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();

			// Reads the Device tag
			//  <Carel>
			//    <Vars>
			//      <VarCRL code="SYTFSX" addressIn="7939" addressOut="8466" varDimension="13" varLength="2" bitPosition="6" />
			//  	  ..
			//      <VarCRL code="DAIZGLS" addressIn="46101" addressOut="10650" varDimension="1" varLength="8" bitPosition="10" />
			//    </Vars>
			//  </Carel>			
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+DeviceTag.PROTOS+"/"+
					MODBUS_TAG);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node modbusNode = nodes.item(0);
			
			varsModbus = new VarsModbusTag();
			
			if (modbusNode == null)
				return;
			
			this.stopBit =  Integer.parseInt(getAttrValByName(modbusNode, STOP_BIT));
			this.parity =  Integer.parseInt(getAttrValByName(modbusNode, PARITY));
			this.readTimeOut =  Integer.parseInt(getAttrValByName(modbusNode, READ_TIME_OUT));
			this.writeTimeOut =  Integer.parseInt(getAttrValByName(modbusNode, WRITE_TIME_OUT));
			this.activityTimeOut =  Integer.parseInt(getAttrValByName(modbusNode, ACTIVITY_TIME_OUT));
			this.f1 =  Integer.parseInt(getAttrValByName(modbusNode, F1));
			this.f2 =  Integer.parseInt(getAttrValByName(modbusNode, F2));
			this.f3 =  Integer.parseInt(getAttrValByName(modbusNode, F3));
			this.f4 =  Integer.parseInt(getAttrValByName(modbusNode, F4));
			this.f5 =  Integer.parseInt(getAttrValByName(modbusNode, F5));
			this.f6 =  Integer.parseInt(getAttrValByName(modbusNode, F6));
			this.f15 =  Integer.parseInt(getAttrValByName(modbusNode, F15));
			this.f16 =  Integer.parseInt(getAttrValByName(modbusNode, F16));
			
			varsModbus.unmarshal(doc, xfactory); 
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Protos/Modbus");
		}	
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

	public VarsModbusTag getVarsModbus() {
		return varsModbus;
	}

	public void setVarsModbus(VarsModbusTag varsModbus) {
		this.varsModbus = varsModbus;
	}

	


}

