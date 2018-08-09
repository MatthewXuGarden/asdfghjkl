/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.ide.dc.xmlDAO.WizardTag.WizardType;

/**
 * @author team pvpro 20090324
 *
 */
public class WizardsTag extends XmlDAO {

	private ArrayList<WizardTag> alWizard = new ArrayList<WizardTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Wizards tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.WIZARDS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			// for each Wizard tag we create an instance of WizardTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				// <Wizard wizardId="TITAPXNP">
				WizardTag wizardTag = new WizardTag();
				wizardTag.setWizardId(getAttrValByName(currNode, WizardTag.WIZARD_ID));
				wizardTag.setUser(getAttrValByName(currNode, WizardTag.USER));
				String wizardType = getAttrValByName(currNode, WizardTag.WIZARD_TYPE);
				try
				{
					wizardTag.setWizartType(Enum.valueOf(WizardType.class, wizardType));
				}
				catch(Exception e)
				{
					wizardTag.setWizartType(WizardType.UNKNOWN);	
				}
				wizardTag.setUser(getAttrValByName(currNode, WizardTag.USER));
				// each Wizard tag has one Sets tag, which in turn contains N Set tags; the Set tag
				// contains one VarSets tag, which contains M VarSet tags
				// <Wizard wizardId="TITAPXNP">
	            //  <Sets>
	            //    <Set setId="74" value="LFSKT">
	            //      <VarSets>
	            //        <VarSet idVar="SYTFSX" value="UDKH" />
	            //        <VarSet idVar="GUHRSFY" value="FW" />
	            //      </VarSets>
	            //    </Set>
	            //    ...
	            //  </Sets>
	            // </Wizard>
				
				// we store each Set in an ArrayList field of the WizardTag obj
				XPathExpression currExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.WIZARDS+
						"/"+WizardTag.WIZARD+"["+(i+1)+"]/"+WizardTag.SETS+"/*");
				Object currResult = currExpr.evaluate(doc, XPathConstants.NODESET);
				NodeList currNodes = (NodeList) currResult;
				for (int j = 0; j < currNodes.getLength(); j++) {
					Node currSetNode = currNodes.item(j);
					SetTag set = new SetTag();
					set.setSetId(Long.parseLong(getAttrValByName(currSetNode, SetTag.SET_ID)));
					set.setValue(getAttrValByName(currSetNode, SetTag.VALUE));
					
					// nested nodes "VarSets/VarSet[1..M] are stored in an arrayList contained in the
					// Set obj instance
					XPathExpression vsCurrExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"
							+PVTag.PV_TAG+"/"+PVTag.WIZARDS+"/"+WizardTag.WIZARD+"["+(i+1)+"]/"
							+WizardTag.SETS+"/"+SetTag.SET+"["+(j+1)+"]/"+SetTag.VARSETS+"/*");
					Object vsCurrResult = vsCurrExpr.evaluate(doc, XPathConstants.NODESET);
					NodeList vsCurrNodes = (NodeList) vsCurrResult;
					for (int k = 0; k < vsCurrNodes.getLength(); k++) {
						Node currVarSetNode = vsCurrNodes.item(k);
						VarSetTag varSet = new VarSetTag();		
						varSet.setIdVar(getAttrValByName(currVarSetNode, VarSetTag.ID_VAR));
						varSet.setValue(getAttrValByName(currVarSetNode, VarSetTag.VALUE));
						
						set.getAlVarSet().add(varSet);
					}
					wizardTag.getAlSet().add(set);					
				}
				alWizard.add(wizardTag);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/Wizards");
		}		

	}
	/**
	 * @return the alWizard
	 */
	public ArrayList<WizardTag> getAlWizard() {
		return alWizard;
	}

}
