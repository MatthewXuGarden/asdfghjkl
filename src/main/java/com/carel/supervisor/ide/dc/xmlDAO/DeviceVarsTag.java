/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.presentation.bo.helper.LineConfig;

/**
 * @author team pvpro 20090321
 *
 */
public class DeviceVarsTag extends XmlDAO {
	
	private final static String VARS = "Vars";
	private final static String CODE = "code";

	private HashMap<String, DeviceVarTag> hmVars = new HashMap<String, DeviceVarTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();		
			// Reads the Device/Vars/ children
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+VARS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				// <Var code="" type="" signed="true" decimal="" minValue="" maxValue="" measureUnit="" />
				DeviceVarTag dv = new DeviceVarTag();
				dv.setCode(getAttrValByName(currNode, CODE));
				dv.setType(Integer.parseInt(getAttrValByName(currNode, DeviceVarTag.TYPE)));
				dv.setSigned("true".equals(getAttrValByName(currNode, DeviceVarTag.SIGNED)));
				dv.setDecimal(Integer.parseInt(getAttrValByName(currNode, DeviceVarTag.DECIMAL)));
				//dv.setMinValue(getAttrValByName(currNode, DeviceVarTag.MIN_VALUE));
				String min = getAttrValByName(currNode, DeviceVarTag.MIN_VALUE);
				if(min != null && !min.equals("") && !min.contains("pk"))
				{
					min = UtilityString.substring(min, LineConfig.DEFAULT_MAX_MIN);
					if(min.endsWith("."))
						min = min.substring(0, min.length()-1);
				}
				dv.setMinValue(min);
				//dv.setMaxValue(getAttrValByName(currNode, DeviceVarTag.MAX_VALUE));
				String max = getAttrValByName(currNode, DeviceVarTag.MAX_VALUE);
				if(max != null && !max.equals("") && !max.contains("pk"))
				{
					max = UtilityString.substring(max, LineConfig.DEFAULT_MAX_MIN);
					if(max.endsWith("."))
						max = max.substring(0, max.length()-1);
				}
				dv.setMaxValue(max);
				dv.setUnitOfMeasure(getAttrValByName(currNode, DeviceVarTag.UNIT_OF_MEASURE));
				// the CODE attribute is employed as key
				hmVars.put(dv.getCode(), dv);				
			}				
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Vars");
		}		
	}
	/**
	 * @return the hmVars
	 */
	public HashMap<String, DeviceVarTag> getHmVars() {
		return hmVars;
	}
	
	/**
	 * 
	 * @param code
	 * @return DeviceVar instance given the var code
	 */
	public DeviceVarTag getDeviceVarByCode(String code) {
		return hmVars.get(code);
	}

}
