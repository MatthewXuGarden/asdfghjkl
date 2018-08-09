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

/**
 * @author team pvpro 20090324
 *
 */
public class LinksTag extends XmlDAO {

	private ArrayList<LinkTag> alLink = new ArrayList<LinkTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Links tag
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.LINKS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			// for each Link tag we create an instance of LinkTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				// <Link linkId="NPUMHLAAH" varMaster="GUHRSFY">
				LinkTag linkTag = new LinkTag();
				linkTag.setLinkId(getAttrValByName(currNode, LinkTag.LINK_ID));
				linkTag.setUser(getAttrValByName(currNode, LinkTag.USER));
				linkTag.setCheckSet(getBoolAttrValByName(currNode, LinkTag.CHECK_SET));
				linkTag.setVarMaster(getAttrValByName(currNode, LinkTag.VAR_MASTER));
				// each Link tag has one Slaves tag, which in turn contains N Slave tags
		        //  <Link linkId="NPUMHLAAH" varMaster="GUHRSFY">
		        //    <Slaves>
		        //      <Slave var="GUHRSFY" />
		        //      <Slave var="SYTFSX" />
		        //    </Slaves>
		        //  </Link>
				
				// we store each Slave in an ArrayList field of the LinkTag obj
				XPathExpression currExpr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+PVTag.LINKS+
						"/"+LinkTag.LINK+"["+(i+1)+"]/"+LinkTag.SLAVES+"/*");
				Object currResult = currExpr.evaluate(doc, XPathConstants.NODESET);
				NodeList currNodes = (NodeList) currResult;
				for (int j = 0; j < currNodes.getLength(); j++) {
					Node currSlaveNode = currNodes.item(j);
					SlaveTag slave = new SlaveTag();
					slave.setVar(getAttrValByName(currSlaveNode, SlaveTag.VAR));
					linkTag.getAlSlave().add(slave);					
				}
				alLink.add(linkTag);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/Links");
		}
	}
	/**
	 * @return the alLink
	 */
	public ArrayList<LinkTag> getAlLink() {
		return alLink;
	}

}
