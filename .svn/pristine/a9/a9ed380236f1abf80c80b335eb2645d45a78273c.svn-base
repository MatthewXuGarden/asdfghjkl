/**
 * 
 */
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

/**
 * @author team pvpro 20090324
 *
 */
public class ImagesTag extends XmlDAO {

	private final static String IMAGES = "Images";
	private final static String KEY = "key";
	private final static String EXT = "ext";
	private final static String FILENAME = "fileName";
	private final static String BYTES = "bytes";

	private HashMap<String, ImageTag> images;
	
	public ImagesTag()
	{
		images = new HashMap<String, ImageTag>();
	}
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Images/ImageStringed tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+IMAGES+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				ImageTag imageTag = new ImageTag();
				imageTag.setKey(getAttrValByName(nodes.item(i), KEY));
				imageTag.setExt(getAttrValByName(nodes.item(i), EXT));
				imageTag.setFileName(getAttrValByName(nodes.item(i), FILENAME));
				imageTag.setBytes(getAttrValByName(nodes.item(i), BYTES));
				images.put(imageTag.getKey(), imageTag);
			}			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Images");
		}
	}

	public HashMap<String, ImageTag> getImages() {
		return images;
	}

	public void setImages(HashMap<String, ImageTag> images) {
		this.images = images;
	}

	}
