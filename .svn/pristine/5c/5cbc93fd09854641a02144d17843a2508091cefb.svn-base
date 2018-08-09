/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Utente
 *
 */
public class TranslationsTag extends XmlDAO {

	private static final String LANG_DEFAULT="langDefault";
	private static final String LANGS = "Langs";
	private static final String SECTION = "Section";
	private static final String ITEM = "Item";
	private static final String KEY = "Key";
	private static final String PV = "PV";
	private static final String ENUM = "ENUM";
	private static final String WIZARD = "WIZARD";
	private static final String COMBO = "COMBO";
	private String langDefault;
	
	private HashMap<String, LangTag> hmLang = new HashMap<String, LangTag>();
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.ide.dc.xmlDAO.XmlDAO#unmarshal(org.w3c.dom.Document, javax.xml.xpath.XPathFactory, java.sql.Connection)
	 */
	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Translations tag
		    //<Translations langDefault="IT">
		    //  <Langs>
		    //    <Lang key="IT">
		    //      <Sections>
		    //        <Section name="PV">
		    //          <Items>
		    //            <Item code="12345~device~descr">
		    //              <Keys>
		    //                <Key key="descr" value="" />
		    //              </Keys>
		    //            </Item>
		    //            <Item code="SYTFSX">
		    //              <Keys>
		    //                <Key key="descr" value="SIHZKRLDOREBLBWHBUWFUXQNBUPSDVPWSSLHTANKCGFPLMABDVBPZZYURUXQCTZTCHISPONBGLCOSABPTNPITZXTUQPZCXIJHMMEEIMOIAJOWKCRSHXOERADKOXYZOYJCTGGOXLKBAZSPJXKOYGUAUEPMITPKNPTBZLCDWCVWMMDRHNBRNKAHDJHPQUFDWZOLGQQXUGRVLYNIAYEURXXCOMYQDNMCXR" />
			// 				...
			
			XPathExpression expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+DeviceTag.TRANSLATIONS);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			langDefault = getAttrValByName(nodes.item(0), LANG_DEFAULT);
			
			expr = xpath.compile("//"+DeviceTag.DEVICE+"/"+DeviceTag.TRANSLATIONS+"/"+LANGS+"/*");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;			
			//PV have to read only its sections
			List<String> pv_sections = new ArrayList<String>();
			pv_sections.add(PV);
			pv_sections.add(ENUM);
			pv_sections.add(COMBO);
			pv_sections.add(WIZARD);
			
			// for each Lang tag we create an instance of LangTag
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currNode = nodes.item(i);
				LangTag lang = new LangTag();
				String OriginalLangKey = getAttrValByName(currNode, LangTag.KEY);
				
				//Language code change, to support Language code set used by Device Creator
				if(OriginalLangKey.equals("CZ"))
					OriginalLangKey = "CS";
				if(OriginalLangKey.equals("SW"))
					OriginalLangKey = "SE";
				
				String langKey = OriginalLangKey.toUpperCase() + "_" + OriginalLangKey.toLowerCase();
				lang.setKey(langKey.toUpperCase() + "_" + langKey.toLowerCase());
				
				// Parses and stores each Section tag
				Element currElement = (Element)nodes.item(i);
				NodeList sectionNodes = currElement.getElementsByTagName(SECTION);

				for (int j = 0; j < sectionNodes.getLength(); j++) {

					Node currSectionNode = sectionNodes.item(j);
					
					SectionTag section = new SectionTag();
					String sectionName = getAttrValByName(currSectionNode, SectionTag.NAME);
					
					if(pv_sections.contains(sectionName)){
						
						// Parses and stores each Item tag
						// under the assumption that 'Item' element is contained only inside 'Items' Tag
						// Here is used the method 'getElementsByTagName' to inspect inside xml document
						// this solution is used to substitute the use of XPath queries, to increase performance
						Element itemElem = (Element)sectionNodes.item(j);
						NodeList itemNodes = itemElem.getElementsByTagName(ITEM);
						
						section.setName(sectionName);

						//NodeList itemNodes = (NodeList) itemResult;				
						for (int k = 0; k < itemNodes.getLength(); k++) {
							Node currItemNode = itemNodes.item(k);
							
							ItemTag item = new ItemTag();
							String itemCode = getAttrValByName(currItemNode, ItemTag.CODE);
							item.setCode(itemCode);
							
							// Parses and stores each Key tag
							// under the assumption that 'Key' element is contained only inside 'Keys' Tag
							// Here is used the method 'getElementsByTagName' to inspect inside xml document
							// this solution is used to substitute the use of XPath queries, to increase performance
							Element currentItemElem = (Element)itemNodes.item(k);
							NodeList keyNodes = currentItemElem.getElementsByTagName(KEY);
														
							for (int l = 0; l < keyNodes.getLength(); l++) {
								Node currKeyNode = keyNodes.item(l);
								KeyTag key = new KeyTag();
								String keyKey = getAttrValByName(currKeyNode, KeyTag.KEY);
								key.setKey(keyKey);
								key.setValue(getAttrValByName(currKeyNode, KeyTag.VALUE));
								
								item.getHmKey().put(keyKey, key);
							}
							section.getHmItem().put(itemCode, item);
						}
						lang.getHmSection().put(sectionName, section);
					}
				}
				hmLang.put(langKey, lang);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device/Translations");
		}
	}

	/**
	 * @return the langDefault
	 */
	public String getLangDefault() {
		return langDefault;
	}

	/**
	 * @param langDefault the langDefault to set
	 */
	public void setLangDefault(String langDefault) {
		this.langDefault = langDefault;
	}

	/**
	 * @return the hmLang
	 */
	public HashMap<String, LangTag> getHmLang() {
		return hmLang;
	}
}
