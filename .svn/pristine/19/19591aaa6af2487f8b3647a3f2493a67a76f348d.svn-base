/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.HashMap;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.dataaccess.db.SeqMgr;

/**
 * @author team pvpro 20090323
 *
 */
public class PVVarsTag extends XmlDAO {

	private final static String CODE = "code";
	private final static String VARS = "Vars";
	
	private HashMap<String, PVVarTag> hmPVVars = new HashMap<String, PVVarTag>();
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device/Supervisors/PV/Vars tag and its children
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+PVTag.SUPERVISORS+"/"+PVTag.PV_TAG+"/"+VARS+"/*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;	
			
			int hstime = 0;
			int hsfreq = 0;
			
			
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				// <PVVar code="abc" sdkCode="def" priority="0" readWrite="11" hsTime="0" hsFrequency="0" hsDelta="2"
				// toDisplay="MAIN" HACCP="true" category="19" relay="false" imageOn="" imageOff="" defaultValue="7" />
				PVVarTag pvv = new PVVarTag();
				pvv.setCode(getAttrValByName(currNode, CODE));
				pvv.setPriority(Integer.parseInt(getAttrValByName(currNode, PVVarTag.PRIORITY)));
				pvv.setReadWrite(Integer.parseInt(getAttrValByName(currNode, PVVarTag.READ_WRITE)));
				
				hstime = Integer.parseInt(getAttrValByName(currNode, PVVarTag.HS_TIME));
				hsfreq = Integer.parseInt(getAttrValByName(currNode, PVVarTag.HS_FREQUENCY));
				
				if(!checkHistorConf(hstime, hsfreq))
				{
					throw new ImportException("modelerr","Errors on Log configuration for variable with code: '"+getAttrValByName(currNode, CODE)+"'");
				}
				
				//adjust Histor time, using the given frequency
				hstime = adjustHistorTime(hstime, hsfreq);
				
				pvv.setHsTime(hstime);
				pvv.setHsFrequency(hsfreq);
				pvv.setHsDelta(getFloatAttrValByName(currNode, PVVarTag.HS_DELTA));
				pvv.setToDisplay(getAttrValByName(currNode, PVVarTag.TO_DISPLAY));
				pvv.setHaccp("true".equals(getAttrValByName(currNode, PVVarTag.HACCP)));
				pvv.setCommand("true".equals(getAttrValByName(currNode, PVVarTag.IS_COMMAND)));
				
				//set general category (1) if attribute isn't cast-able to integer 
				int category = 0;
				try
				{
					String tmp = getAttrValByName(currNode, PVVarTag.CATEGORY);
					if (tmp.startsWith("c_"))
					{
						//String categ_code = tmp.split("_")[1];
						String categ_code = tmp;
						pvv.setCategory_code(categ_code);
						category = -1;
					}
					else
					{
						category = Integer.parseInt(getAttrValByName(currNode, PVVarTag.CATEGORY));
					}
					
				}catch (NumberFormatException nfe)
				{
					category = 1;
				}
				pvv.setCategory(category);	
				
				pvv.setRelay("true".equals(getAttrValByName(currNode, PVVarTag.RELAY)));
				pvv.setImageOn(getAttrValByName(currNode, PVVarTag.IMAGE_ON));
				
				if(pvv.getImageOn() == null)
					pvv.setImageOn("");
				pvv.setImageOff(getAttrValByName(currNode, PVVarTag.IMAGE_OFF));
				if(pvv.getImageOff() == null)
					pvv.setImageOff("");
				if(getAttrValByName(currNode, PVVarTag.DEFAULT_VALUE) != null)
					pvv.setDefaultValue(getFloatAttrValByName(currNode, PVVarTag.DEFAULT_VALUE));
				else
					pvv.setDefaultValue((float) 0.0);
				pvv.setCombo(getAttrValByName(currNode, PVVarTag.COMBO));
				pvv.setEEnum(getAttrValByName(currNode, PVVarTag.ENUM));
				// the CODE attribute is employed as key
				if(hmPVVars.containsKey(pvv.getCode()))
					throw new ImportException("modelerr","One or more variables have the same code: '"+getAttrValByName(currNode, CODE)+"'");
				else
					hmPVVars.put(pvv.getCode(), pvv);		
			}
		} catch (ImportException ie)
		{
			throw ie;
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Supervisors/PV/Vars/PVVar");
		}			
	}

	private boolean checkHistorConf(int hstime, int hsfreq) {
		
		if(hstime != 0 && hstime != 604800 && hstime != 2592000 && hstime != 5184000 && hstime != 15552000 && hstime != 31536000 && hstime != 46656000 && hstime != 62208000)
			return false;
		if(hsfreq != 0 && hsfreq != 5 && hsfreq != 10 && hsfreq != 15 && hsfreq != 30 && hsfreq != 60 && hsfreq != 120 && hsfreq != 300 && hsfreq != 900 && hsfreq != 1800 && hsfreq != 3600)
			return false;
		return true;
	}
	
	private int adjustHistorTime(int hstime, int hsfreq) {
		
		switch(hsfreq)
		{
			case 5:
				if(hstime > 604800)
					return 604800; //5 seconds - 7 days
				break;
			case 10:
				if(hstime > 604800)
					return 604800; //10 seconds - 7 days
				break;
			case 15:
				if(hstime > 604800)
					return 604800; //15 seconds - 7 days
				break;
			case 30:
				if(hstime > 5184000)
					return 5184000; //30 seconds - 60 days
				break;
			case 60:
				if(hstime > 15552000)
					return 15552000; //1 minute - 180 days
				break;
			case 120:
				if(hstime > 15552000)
					return 15552000; //2 minutes - 180 days
				break;
			case 300:
				if(hstime > 46656000)
					return 46656000; //5 minutes - 540 days
				break;
			case 900:
				if(hstime > 46656000)
					return 46656000; //15 minutes - 540 days
				break;
			case 1800:
				if(hstime > 62208000)
					return 62208000; //30 minutes - 720 days
				break;
			case 3600:
				if(hstime > 62208000)
					return 62208000; //60 minutes - 720 days
				break;
		}
		return hstime;
	}

	/**
	 * @return the hmPVVars
	 */
	public HashMap<String, PVVarTag> getHmPVVars() {
		return hmPVVars;
	}
	
	/**
	 * 
	 * @param code
	 * @return DeviceVar instance given the var code
	 */
	public PVVarTag getPVVarByCode(String code) {
		return hmPVVars.get(code);
	}
	
	/**
	 * @return the variables code set
	 */
	public Set<String> getPVVarsCode() {
		return hmPVVars.keySet();
	}
	
}
