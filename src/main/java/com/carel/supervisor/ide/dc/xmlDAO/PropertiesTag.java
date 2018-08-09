package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.List;

import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;


public class PropertiesTag extends XmlDAO {

	private List<FSTagObj> fsTagObjList = null;
	
	
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		try {
			
			// Floating suction Unmarshal
			FSTag fs = new FSTag();
			fs.unmarshal(doc, xfactory);
			fsTagObjList = fs.getFSconfig();
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Properties");
		}
	}
	
	public List<FSTagObj> getFSConfig() {
		return fsTagObjList;
	}
}
