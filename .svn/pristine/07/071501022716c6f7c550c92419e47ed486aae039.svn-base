/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author team pvpro 20090324
 *
 */
public class CarelTag extends XmlDAO {

	//private ArrayList<VarCRLTag> alVarCRL = new ArrayList<VarCRLTag>();
	private HashMap<String, VarCRLTag> alVarCRL = new HashMap<String, VarCRLTag>();
	
	private final static String CAREL = "Carel";
	private final static String VARS = "Vars";
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) 
			throws ImportException {
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
					CAREL+"/"+VARS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			
			// for each VarCRL tag we create an instance of VarCRL
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				VarCRLTag varCRL = new VarCRLTag();
				varCRL.setCode(getAttrValByName(currNode, VarCRLTag.CODE));
				varCRL.setAddressIn(Integer.parseInt(getAttrValByName(currNode, VarCRLTag.ADDRESS_IN)));
				varCRL.setAddressOut(Integer.parseInt(getAttrValByName(currNode, VarCRLTag.ADDRESS_OUT)));
				varCRL.setVarDimension(Integer.parseInt(getAttrValByName(currNode, VarCRLTag.VAR_DIMENSION)));
				varCRL.setVarLength(Integer.parseInt(getAttrValByName(currNode, VarCRLTag.VAR_LENGTH)));
				varCRL.setBitPosition(Integer.parseInt(getAttrValByName(currNode, VarCRLTag.BIT_POSITION)));
				
				alVarCRL.put(varCRL.getCode(), varCRL);
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Protos/Carel/Vars");
		}
	}
	
	
	
	/**
	 * @return the alVarCRL
	 */
	public HashMap<String, VarCRLTag> getAlVarCRL() {
		return alVarCRL;
	}

}
