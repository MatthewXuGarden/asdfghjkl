/**
 * Manages the unmarshaling of the whole Device tag
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author pvpro team 20090319
 *
 */
public class DeviceTag extends XmlDAO {

	private static final String DESCRIPTION_KEY = "descriptionKey";
	private static final String SW_VERSION = "swVersion";
	private static final String LITTLE_ENDIAN = "littleEndian";
	private static final String MANUFACTURER = "manufacturer";
	private static final String IMAGE = "image";
	private static final String IDE= "ide";
	protected static final String PROTOS = "Protos";
	protected static final String DEVICE = "Device";
	protected static final String CODE = "code";
	public static final String TRANSLATIONS = "Translations";
	public static final String PROPERTIES = "Properties";
	public static final String VARCOMPTAG = "#@#VAR_COMP#@#";
	
	private String code;
	private String descriptionKey;
	private String swVersion;
	private boolean littleEndian;
	private String manufacturer;
	private String image;
	private int ide;
	private DeviceVarsTag varCommonInfo;
	private PVTag pvInfo;
	private CarelTag carelInfo;
	private ModbusTag modbusInfo;
	private ImagesTag imagesInfo;
	private TranslationsTag translations;
	private PropertiesTag properties;
	private boolean modbusModel;
	
	@Override
	public void unmarshal(Document doc, XPathFactory xfactory) throws ImportException {
		try {
			XPath xpath = xfactory.newXPath();
			
			// Reads the Device tag
			XPathExpression expr = xpath.compile("//"+DEVICE);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node device = nodes.item(0);
			
			code = getAttrValByName(device, CODE);
			descriptionKey = getAttrValByName(device, DESCRIPTION_KEY);
			swVersion = getAttrValByName(device, SW_VERSION);
			littleEndian = getBoolAttrValByName(device, LITTLE_ENDIAN);
			manufacturer = getAttrValByName(device, MANUFACTURER);
			ide = getIntAttrValByName(device, IDE);
			
			// Reads the Vars (common) and stores them in a hashmap (DeviceVars' instance field)
			varCommonInfo = new DeviceVarsTag();			
			varCommonInfo.unmarshal(doc, xfactory);
			// To retrieve the Device/Vars info, just call dvs.getHmVars() to get a hashMap containing all the DeviceVars
						
			// Now reads the Supervisors/PV tag and its children
			pvInfo = new PVTag();
			pvInfo.unmarshal(doc, xfactory);
			
			image = pvInfo.getImageKey();
			
			// To retrieve the /Device/Supervisors/PV/Vars info,
			// just call pv.getHmPVVars() to get a hashMap containing all the PVVars
			
			// Now reads the Protos tag. Protocols may be: Carel or Modbus. We provide a class
			// for parsing each protocol.
			carelInfo = new CarelTag();
			carelInfo.unmarshal(doc, xfactory);
			
			modbusInfo = new ModbusTag();
			modbusInfo.unmarshal(doc, xfactory);

			modbusModel = false;
			
			if((modbusInfo.getVarsModbus()).getAlVarMODBUS().size() >0)
		
				modbusModel = true;
			
			if(carelInfo.getAlVarCRL().size() > 0 && (modbusInfo.getVarsModbus()).getAlVarMODBUS().size() >0)
			{
				modbusModel = false;
				throw new ImportException("modelerr","Model contains two different protocols");
			}
			
			imagesInfo = new ImagesTag();
			imagesInfo.unmarshal(doc, xfactory);
			
			translations = new TranslationsTag();
			translations.unmarshal(doc, xfactory);
			
			properties = new PropertiesTag();
			properties.unmarshal(doc, xfactory);
			
			// patch for adjusting wrong 'compacted dig/alarm' variables: 
			// if swversion field does not contains '#@#VAR_COMP#@#' suffix (to identify explicit use of the comapcted vars)
			// then all dig/alarm variables with varlength = 1 are changed to varlength = 16
			
			if(!swVersion.endsWith(VARCOMPTAG) && !modbusModel)
				adjustCompactedDigAlrms();
						
		}catch (ImportException ie)
		{
			throw ie;
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new ImportException("xmlerr",e,"XML format error. Tag: Device");
		}
	}

	private void adjustCompactedDigAlrms() {
		HashMap<String, DeviceVarTag> commonInfoMap = varCommonInfo.getHmVars();
		HashMap<String, VarCRLTag> carelInfoMap = carelInfo.getAlVarCRL();
		Set<String> codeVariableSet = commonInfoMap.keySet();
		for(String code:codeVariableSet){
			DeviceVarTag devvartag = (DeviceVarTag)commonInfoMap.get(code);
			if(devvartag.getType()==1 || devvartag.getType()==4) // only for digital and alarm variables type
			{
				VarCRLTag varcrltag = (VarCRLTag)carelInfoMap.get(code);
				if(varcrltag.getVarDimension() == 16 && varcrltag.getVarLength() != 16)
					varcrltag.setVarLength(16);
			}
		}
	}
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescriptionKey() {
		return descriptionKey;
	}


	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}
	
	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}


	public boolean getLittleEndian() {
		return littleEndian;
	}


	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public int getIde() {
		return ide;
	}


	public void setIde(int ide) {
		this.ide = ide;
	}


	public DeviceVarsTag getVarCommonInfo() {
		return varCommonInfo;
	}


	public void setVarCommonInfo(DeviceVarsTag varCommonInfo) {
		this.varCommonInfo = varCommonInfo;
	}


	public PVTag getPvInfo() {
		return pvInfo;
	}


	public void setPvInfo(PVTag pvInfo) {
		this.pvInfo = pvInfo;
	}


	public CarelTag getCarelInfo() {
		return carelInfo;
	}


	public void setCarelInfo(CarelTag carelInfo) {
		this.carelInfo = carelInfo;
	}


	public ModbusTag getModbusInfo() {
		return modbusInfo;
	}


	public void setModbusInfo(ModbusTag modbusTag) {
		this.modbusInfo = modbusTag;
	}


	public ImagesTag getImagesInfo() {
		return imagesInfo;
	}


	public void setImagesInfo(ImagesTag imagesInfo) {
		this.imagesInfo = imagesInfo;
	}


	public TranslationsTag getTranslations() {
		return translations;
	}


	public void setTranslations(TranslationsTag translations) {
		this.translations = translations;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public boolean isModbusModel() {
		return modbusModel;
	}


	public void setModbusModel(boolean modbusModel) {
		this.modbusModel = modbusModel;
	}


	public PropertiesTag getProperties() {
		return properties;
	}

	
}
