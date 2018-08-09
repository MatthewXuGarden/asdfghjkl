/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

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
public class PVTag extends XmlDAO {
		
	protected static final String SUPERVISORS = "Supervisors";
	protected static final String PV_TAG = "PV";
	protected static final String IMAGE_KEY = "imageKey";
	protected static final String ENUMS = "Enums";
	protected static final String COMBOS = "Combos";
	protected static final String CATEGORIES = "Categories";
	protected static final String LINKS = "Links";
	protected static final String WIZARDS = "Wizards";
	
	private String imageKey = null;
	private PVVarsTag pvVars = null;
	private EnumsTag enums;
	private CombosTag combos;
	private LinksTag links;
	private CategoriesTag categories;
	private WizardsTag wizards;
	

	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+SUPERVISORS+"/"+PV_TAG);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node pvNode = nodes.item(0);
			
			this.imageKey = getAttrValByName(pvNode, IMAGE_KEY);
			
			// Now reads the Device/Supervisors/PV/Vars children
			pvVars = new PVVarsTag();			
			pvVars.unmarshal(doc, xfactory);
			
			// Now reads the Device/Supervisors/PV/Enums children
			enums = new EnumsTag();	
			enums.unmarshal(doc, xfactory);
			
			// Now reads the Device/Supervisors/PV/Combos children
			combos = new CombosTag();
			combos.unmarshal(doc, xfactory);
			
			// Now reads the Device/Supervisors/PV/Links children
			links = new LinksTag();
			links.unmarshal(doc, xfactory);
			
			// Now reads the Device/Supervisors/PV/Categories children
			categories = new CategoriesTag();
			categories.unmarshal(doc, xfactory);
			
			// Now reads the Device/Supervisors/PV/Wizards children
			wizards = new WizardsTag();
			wizards.unmarshal(doc, xfactory);
			
		} catch (ImportException ie)
		{
			throw ie;
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/PV");
		}
	}
	
	/**
	 * @return the imageKey
	 */
	public String getImageKey() {
		return imageKey;
	}

	/**
	 * @return the pvVars
	 */
	public PVVarsTag getPvVars() {
		return pvVars;
	}

	/**
	 * @return the enums
	 */
	public EnumsTag getEnums() {
		return enums;
	}

	/**
	 * @return the combos
	 */
	public CombosTag getCombos() {
		return combos;
	}

	/**
	 * @return the links
	 */
	public LinksTag getLinks() {
		return links;
	}
	
	public CategoriesTag getCategories() {
		return categories;
	}

	/**
	 * @return the wizards
	 */
	public WizardsTag getWizards() {
		return wizards;
	}
}
