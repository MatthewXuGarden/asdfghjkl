/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author team pvpro 20090324
 *
 */
public class FSTag extends XmlDAO {

	private List<FSTagObj> tagList = new ArrayList<FSTagObj>(); 
	
	private final static String FS = "FloatingSuction";
	private final static String VALUE = "value";
	private final static String DEVICE = "Dev";
	
	
	private final static String ISRACK = "isRack";
	private final static String V1 = "var1";
	private final static String V2 = "var2";
	private final static String V3 = "var3";
	private final static String V4 = "var4";
	
	
	protected void unmarshal(Document doc, XPathFactory xfactory) 
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
					
			/*
			 <Properties>
					<FloatingSuction>
						<Dev var4="" var3="TRUE" var2="70" var1="46" isRack="FALSE" />
						<Dev var4="" var3="TRUE" var2="70" var1="46" isRack="FALSE" />
					</FloatingSuction>
				</Properties>
			 */
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+DeviceTag.PROPERTIES+"/"+
					FS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			
			FSTagObj tmp = new FSTagObj();
			if (nodes.getLength()!=0)
			{
				for (int i=0;i<nodes.getLength();i++)
				{
					Node currNode = nodes.item(i);
					tmp.setIsrack(getAttrValByName(currNode, FSTag.ISRACK));
					tmp.setVar1(getAttrValByName(currNode, FSTag.V1));
					tmp.setVar2(getAttrValByName(currNode, FSTag.V2));
					tmp.setVar3(getAttrValByName(currNode, FSTag.V3));
					tmp.setVar4(getAttrValByName(currNode, FSTag.V4));
					tagList.add(tmp);
				}
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Properties/FloatingSuction");
		}
	}
	
	/**
	 * @return the alVarCRL
	 */
	public List<FSTagObj> getFSconfig() {
		return tagList;
	}

}
