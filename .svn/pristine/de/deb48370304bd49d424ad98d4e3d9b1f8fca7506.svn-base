package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VarsModbusTag extends XmlDAO{


	private final static String MODBUS = "Modbus";
	private final static String VARS = "Vars";

	private HashMap<String, VarModbusTag> alVarMODBUS = new HashMap<String, VarModbusTag>();

	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();

//			<Vars>
//	          <VarMDB code="Var1" addressIn="100133" funcTypeRead="1" addressOut="500133" funcTypeWrite="5" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" />
//	          <VarMDB code="Var2" addressIn="100012" funcTypeRead="1" addressOut="500012" funcTypeWrite="5" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" />
//	          <VarMDB code="Var3" addressIn="100013" funcTypeRead="1" addressOut="500013" funcTypeWrite="5" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" />
//	          <VarMDB code="Var4" addressIn="400002" funcTypeRead="4" addressOut="400002" funcTypeWrite="0" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" />
//	          <VarMDB code="Var5" addressIn="400000" funcTypeRead="4" addressOut="400000" funcTypeWrite="0" varDimension="0" varLength="0" bitPosition="0" aValue="0" bValue="0" />
//	          <VarMDB code="OFFLINE" addressIn="0" funcTypeRead="0" addressOut="0" funcTypeWrite="0" varDimension="16" varLength="16" bitPosition="0" aValue="0" bValue="0" />
//	        </Vars>	
			
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+DeviceTag.PROTOS+"/"+
					MODBUS+"/"+VARS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			// for each VarCRL tag we create an instance of VarCRL
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				VarModbusTag varMODBUS = new VarModbusTag();
				varMODBUS.setCode(getAttrValByName(currNode, VarModbusTag.CODE));
				varMODBUS.setAddressIn(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.ADDRESS_IN)));
				varMODBUS.setAddressOut(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.ADDRESS_OUT)));
				varMODBUS.setVarDimension(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.VAR_DIMENSION)));
				varMODBUS.setVarLength(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.VAR_LENGTH)));
				varMODBUS.setBitPosition(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.BIT_POSITION)));
				varMODBUS.setFuncTypeRead(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.FUNC_TYPE_READ)));
				varMODBUS.setFuncTypeWrite(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.FUNC_TYPE_WRITE)));
				varMODBUS.setAValue(getFloatAttrValByName(currNode, VarModbusTag.A_VALUE));
				varMODBUS.setBValue(getFloatAttrValByName(currNode, VarModbusTag.B_VALUE));
				varMODBUS.setVarEncoding(Integer.parseInt(getAttrValByName(currNode, VarModbusTag.VAR_ENCODING)));

				alVarMODBUS.put(varMODBUS.getCode(), varMODBUS);
			}

		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Protos/Modbus/Modbus/Vars/VarMDB");
		}
	}

	public HashMap<String, VarModbusTag> getAlVarMODBUS() {
		return alVarMODBUS;
	}

	public void setAlVarMODBUS(HashMap<String, VarModbusTag> alVarMODBUS) {
		this.alVarMODBUS = alVarMODBUS;
	}
}
