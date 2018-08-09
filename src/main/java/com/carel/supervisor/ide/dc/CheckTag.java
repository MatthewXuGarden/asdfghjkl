/**
 * 
 */
package com.carel.supervisor.ide.dc;

import java.sql.Connection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.ide.dc.xmlDAO.DeviceTag;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.ide.dc.xmlDAO.XmlDAO;

/**
 * @author Utente
 *
 */
public class CheckTag extends XmlDAO {

	private static final String CODE = "code";
	private static final String CHECK = "Check";
	private String code;
	
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {		
			XPath xpath = xfactory.newXPath();
			
			// Reads the //Check tag
			XPathExpression expr = xpath.compile("//"+CHECK);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node checkNode = nodes.item(0);		
			code = getAttrValByName(checkNode, CODE);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Check");
		}
	}


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
