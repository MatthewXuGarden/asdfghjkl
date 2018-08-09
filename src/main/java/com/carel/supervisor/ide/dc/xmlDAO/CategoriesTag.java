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
public class CategoriesTag extends XmlDAO {

	private ArrayList<CategoryTag> alCategory = new ArrayList<CategoryTag>();
	
	
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Category tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.CATEGORIES+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			// for each Combo tag we create an instance of ComboTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				CategoryTag categoryTag = new CategoryTag();
				categoryTag.setCategoryCode(getAttrValByName(currNode, CategoryTag.CATEGORY_CODE));
				
				// each Combo tag has one ComboItems tag, which in turn contains N ComboItem tags
				
				/*
				<Categories>
					<Category categoryCode="test category" />
		            <Category categoryCode="test category 2" /> 
		        </Categories>
		        */
		         
				
				//DESCRIPTION for each Category
				/*
				<Item code="category~test category">
                <Keys>
                  <Key key="descr" value="test category" />
                </Keys>
                </Item>
                */
                
				// we store each EnumItem in an ArrayList field of the CategoryTag obj
				XPathExpression currExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.CATEGORIES+
						"/"+CategoryTag.CATEGORY+"["+(i+1)+"]/*");
				Object currResult = currExpr.evaluate(doc, XPathConstants.NODESET);
				
				
				alCategory.add(categoryTag);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/Category");
		}
	}
	
	public ArrayList<CategoryTag> getAlCategory() {
		return alCategory;
	}
}
