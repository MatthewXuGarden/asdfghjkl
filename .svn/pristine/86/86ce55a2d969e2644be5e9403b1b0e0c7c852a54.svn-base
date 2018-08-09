/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author team pvpro 20090323
 *
 */
public class EnumsTag extends XmlDAO {

	private ArrayList<EnumTag> alEnum = new ArrayList<EnumTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Enums tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.ENUMS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			// for each Enum tag we create an instance of EnumTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				EnumTag enumTag = new EnumTag();
				enumTag.setEnumCode(getAttrValByName(currNode, EnumTag.ENUM_CODE));
				
				// each Enum tag has one EnumItems tag, which in turn contains N EnumItem tags
				
				//<Enums>
				//<Enum enumCode="Var1">
		        //    <EnumItems>
		        //        <EnumItem value="0" />
		        //        <EnumItem value="1" />
				//		<EnumItem value="2" />
		        //    </EnumItems>
		        //</Enum>
			    //</Enums>
				
				//DESCRIPTION for each EnumItem
				
				//<Item code="Var1~enum~0">
                //<Keys>
                //  <Key key="descr" value="enum 0" />
                //</Keys>
                //</Item>
                
				// we store each EnumItem in an ArrayList field of the EnumTag obj
				XPathExpression currExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.ENUMS+
						"/"+EnumTag.ENUM+"["+(i+1)+"]/"+EnumTag.ENUM_ITEMS+"/*");
				Object currResult = currExpr.evaluate(doc, XPathConstants.NODESET);
				
				NodeList currNodes = (NodeList) currResult;
				for (int j = 0; j < currNodes.getLength(); j++) {
					Node currEnumItemNode = currNodes.item(j);
					EnumItemTag enumItem = new EnumItemTag();
					enumItem.setValue(getIntAttrValByName(currEnumItemNode, EnumItemTag.VALUE));
					enumTag.getAlEnumItem().add(enumItem);		
				}
				alEnum.add(enumTag);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/Enum");
		}
	}
	
	public ArrayList<EnumTag> getAlEnum() {
		return alEnum;
	}
}
