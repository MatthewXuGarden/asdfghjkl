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
public class CombosTag extends XmlDAO {

	private ArrayList<ComboTag> alCombo = new ArrayList<ComboTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Combo tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.COMBOS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			// for each Combo tag we create an instance of ComboTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				ComboTag comboTag = new ComboTag();
				comboTag.setComboCode(getAttrValByName(currNode, ComboTag.COMBO_CODE));
				
				// each Combo tag has one ComboItems tag, which in turn contains N ComboItem tags
				
				//<Comboss>
				//<Combo comboCode="Var1">
		        //    <ComboItems>
		        //        <ComboItem value="0" />
		        //        <ComboItem value="1" />
				//		<ComboItem value="2" />
		        //    </ComboItems>
		        //</Combo>
			    //</Combos>
				
				//DESCRIPTION for each ComboItem
				
				//<Item code="Var1~combo~0">
                //<Keys>
                //  <Key key="descr" value="combo 0" />
                //</Keys>
                //</Item>
                
				// we store each EnumItem in an ArrayList field of the ComboTag obj
				XPathExpression currExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.COMBOS+
						"/"+ComboTag.COMBO+"["+(i+1)+"]/"+ComboTag.COMBO_ITEMS+"/*");
				Object currResult = currExpr.evaluate(doc, XPathConstants.NODESET);
				
				NodeList currNodes = (NodeList) currResult;
				for (int j = 0; j < currNodes.getLength(); j++) {
					Node currComboItemNode = currNodes.item(j);
					ComboItemTag comboItem = new ComboItemTag();
					comboItem.setValue(getIntAttrValByName(currComboItemNode, ComboItemTag.VALUE));
					comboTag.getAlComboItem().add(comboItem);		
				}
				alCombo.add(comboTag);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/Combo");
		}
	}
	
	public ArrayList<ComboTag> getAlCombo() {
		return alCombo;
	}
}
