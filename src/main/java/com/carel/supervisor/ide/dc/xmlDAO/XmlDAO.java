/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.awt.font.NumericShaper;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author pvpro team 20090319
 *
 */
public abstract class XmlDAO {

	/**
	 * Parses data contained in a XML tag and handles it
	 * @param doc
	 * @param xfactory
	 * @param conn
	 * @throws ImportException
	 */
	protected abstract void unmarshal(Document doc, XPathFactory xfactory) throws ImportException;
	
	/**
	 * Utility method. Returns an instance of the input using reflection
	 * Convention over configuration applies here: we assume the implementation
	 * class is in this_package.impl.
	 * @param className
	 * @return XmlDAO
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	protected XmlDAO getXMLDAOInstance(String className) throws ImportException {
		XmlDAO xmlDAO = null;
		try {
			Class<XmlDAO> c;
			c = (Class<XmlDAO>) Class.forName(this.getClass().getPackage()+".impl."+className);
			xmlDAO = c.newInstance();	
		} catch (Exception e) {			
			throw new ImportException("xmlerr",e);
		}
		return xmlDAO;
	} 
	
	protected String getAttrValByName(Node node, String attrName) {
		String retVal = null;
		NamedNodeMap nnm = node.getAttributes();
		Node attr = nnm.getNamedItem(attrName);
		if (attr != null) {
			retVal = attr.getNodeValue();
		}
		return retVal;	
	}
	
	protected boolean getBoolAttrValByName(Node node, String attrName) throws Exception
	{
		boolean retVal = false;
		NamedNodeMap nnm = node.getAttributes();
		Node attr = nnm.getNamedItem(attrName);
		if (attr != null) {
			try{
				retVal = Integer.parseInt(attr.getNodeValue()) != 0;
			}
			catch (Exception e)
			{
				try
				{
					retVal = Boolean.parseBoolean(attr.getNodeValue());
				}
				catch (Exception ex)
				{
					throw ex;
				}
			}
		}
		return retVal;
	}
	
	protected int getIntAttrValByName(Node node, String attrName) throws Exception 
	{
		int retVal = 0;
		NamedNodeMap nnm = node.getAttributes();
		Node attr = nnm.getNamedItem(attrName);
		if (attr != null) {
			try
			{
				retVal = Integer.parseInt(attr.getNodeValue()); 
			}
			catch (Exception e) {
				
				throw e;
			}
		}
		return retVal;	
	}
	
	protected float getFloatAttrValByName(Node node, String attrName) throws Exception
	{
		float retVal = 0;
		NamedNodeMap nnm = node.getAttributes();
		Node attr = nnm.getNamedItem(attrName);
		if (attr != null) {
			try
			{
				String floatValStr = attr.getNodeValue();
				floatValStr = floatValStr.replace(',', '.');
				retVal = Float.parseFloat(floatValStr);
			}
			catch (Exception e) {
				
				throw e;
			}
		}
		return retVal;
	}
}
