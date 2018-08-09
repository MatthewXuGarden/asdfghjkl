package com.carel.supervisor.ide.dc.DbModel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.util.Log;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.io.FileSystemUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.ide.dc.DbModel.Translation.Translations;
import com.carel.supervisor.ide.dc.xmlDAO.CategoryTag;
import com.carel.supervisor.ide.dc.xmlDAO.ComboTag;
import com.carel.supervisor.ide.dc.xmlDAO.DeviceTag;
import com.carel.supervisor.ide.dc.xmlDAO.DeviceVarTag;
import com.carel.supervisor.ide.dc.xmlDAO.EnumTag;
import com.carel.supervisor.ide.dc.xmlDAO.FSTagObj;
import com.carel.supervisor.ide.dc.xmlDAO.ImagesTag;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.ide.dc.xmlDAO.KeyTag;
import com.carel.supervisor.ide.dc.xmlDAO.ModbusTag;
import com.carel.supervisor.ide.dc.xmlDAO.PVVarTag;
import com.carel.supervisor.ide.dc.xmlDAO.VarCRLTag;
import com.carel.supervisor.ide.dc.xmlDAO.VarModbusTag;
import com.carel.supervisor.presentation.bean.DevMdlExtBean;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.devices.ResetSubset;



public class Model
{
	private static final String HD_VERSION = "";
	private static final int ID_SITE = 1;
	private static final Object DESCR = "descr";
	private static final Object SHORTDESCR = "short";
	private static final Object LONGDESCR = "long";
	private static final Object EN = "EN_en";
	private static final String ID_DEVICE = "iddevmdl";
	private static final String VAR_CODE = "code";
	private static final String ID_VAR_MDL = "idvarmdl"; 
	private static final String CF_VAR_MDL = "cfvarmdl";
	private static final String CF_DEV_MDL = "cfdevmdl";
	private static final String MIN_MAX_PK_REF = "pk";
	private static final Object OFFLINE_VAR = "OFFLINE";
	private static final String IMG_PATH = BaseConfig.getAppHome() + "images" + File.separator + "devices";
	private static final String DIGITAL_BUTTON_PATH = BaseConfig.getAppHome() + "images" + File.separator + "button";
	private static final int START_BIT = 1;
	private static final int DATA_TRANSMISSION = 0; // 0 indicates RTU (1:  ASCII - not supported yet)

	private DeviceDb device;
	private HashMap<String, VariableDb> variables = new HashMap<String, VariableDb>();
	private HashMap<String, Integer> newIdVarMdl = new HashMap<String, Integer>();
	private Translations translations;
	private ArrayList<EnumDb> enums = new ArrayList<EnumDb>();
	private ArrayList<ComboDb> combos= new ArrayList<ComboDb>();
	private ArrayList<CategoryDb> categories = new ArrayList<CategoryDb>();
	private List<FSTagObj> fsinfo = new ArrayList<FSTagObj>();

	public Model()
	{
		device = new DeviceDb();
		variables = new HashMap<String, VariableDb>();
		translations = new Translations(); 
		enums = new ArrayList<EnumDb>();
		
	}

	public HashMap<String, VariableDb> getvariables()
	{
		return variables;
	}
	
	public Translations getTranslations()
	{
		return translations;
	}
	
	//insert operations
	
	public boolean insertNewModel(DeviceTag deviceTag, Connection conn) throws ImportException
	{
		
		boolean deviceExist = false;
		HashMap<String, VariableModBusInformationDb> varsModbus = new HashMap<String, VariableModBusInformationDb>();

		fillInformations(deviceTag, varsModbus);

		try
		{
			if(checkModelCorrectness(deviceTag.isModbusModel()))
			{
				deviceExist = insertModelOnDB(deviceTag.getImagesInfo(), varsModbus, conn);
			}
			
		}catch(ImportException e)
		{
			throw e;
		}
		return deviceExist;
	}

	private boolean insertModelOnDB(ImagesTag imageStore, HashMap<String, VariableModBusInformationDb> varsModbus, Connection conn) throws ImportException
	{
		try{			
			//RETRIEVE used langset
			Set<String> langSet = translations.getLanguages().keySet();
			
			//check if model exists as standard CAREl model. In this case is not possible to update model.
			String sqlExistsStandard = "select count(1) from cfdevmdl where idsite=? and code=? and ide=?";
			PreparedStatement ps = conn.prepareStatement(sqlExistsStandard);
			ps.setInt(1, ID_SITE);
			ps.setString(2, this.device.getCode());
			ps.setString(3, "FALSE");
	
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0){
				throw new ImportException("usererror","Cannot update an existing CAREL standard model.");
			}
			
			//check if model exists (and is not a standard CAREL model). In this case model is updated.
			String sqlExixsts = "select count(1) from cfdevmdl where idsite=? and code=?";
			ps = conn.prepareStatement(sqlExixsts);
			ps.setInt(1, ID_SITE);
			ps.setString(2, this.device.getCode());

			rs = ps.executeQuery();
			count = 0;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0){
				//return true to specify that device already exists. Update operation will be executed.
				return true; 
			}

			//retrieve PVPRO languages
			LangUsedBeanList lan = new LangUsedBeanList();
			LangUsedBean[] langused = lan.retrieveAllLanguage(1);
			List<String> langs = new ArrayList<String>();
			for (int i=0; i<langused.length; i++)
			{
				langs.add(langused[i].getLangcode());
			}
			
			//retrieve default language
			String defaultLan = LangUsedBeanList.getDefaultLanguage(1);
			
			//check if model translations match PVPRO languages (at least one language)
			if(checkModelTranslations(langs, langSet))
			{
				//align correct format version with database DataVersion
				//boolean -> CHAR(5)	        
				String littleEndian = Utils.booleanToPvString(this.device.isLittleEndian());
				
				// IDE flag always at "TRUE" when device is added to the system 
				// String ide = Utils.booleanToPvString(this.device.isIde());
				String ide = "TRUE";
				String deviceImage = "";
				if(imageStore.getImages().containsKey(this.device.getImage()))
					deviceImage = imageStore.getImages().get(this.device.getImage()).getFileName();
				
				//INSERT cfdevmdl
				String sql = "insert into cfdevmdl values ((SELECT max(iddevmdl)+1 FROM cfdevmdl),?,?,?,?,?,?,?,?)";
				PreparedStatement insertDevice = conn.prepareStatement(sql);
				insertDevice.setInt(1,this.device.getIdSite());
				insertDevice.setString(2, this.device.getCode());
				insertDevice.setString(3, this.device.getManufacturer());
				insertDevice.setString(4, this.device.getHdVersion());
				insertDevice.setString(5, this.device.getSwVersion());
				insertDevice.setString(6, deviceImage);
				insertDevice.setString(7, littleEndian);
				insertDevice.setString(8, ide);
				
				if(imageStore.getImages().containsKey(this.device.getImage()))
					Utils.decodeImage(imageStore.getImages().get(this.device.getImage()).getBytes(), imageStore.getImages().get(this.device.getImage()).getFileName(), IMG_PATH );

				//RETRIEVE iddevmdl of the just inserted device to fill the table cftableext
				insertDevice.executeUpdate();	 
				PreparedStatement deviceInserted = conn.prepareStatement("SELECT max(iddevmdl) FROM cfdevmdl");
				rs = deviceInserted.executeQuery();

				int idDevMdl = -1;
				if (rs.next())
					idDevMdl=rs.getInt(1);

				//insert device model translations
				if(langSet.contains(defaultLan))
				{
					//INSERT default language translations (using default language strings from xml)
					this.insertTranslations(this.device.getIdSite(), defaultLan, CF_DEV_MDL, idDevMdl, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(device.getDescriptionKey()).getKeys().get(DESCR),"", "", conn);
				}
				else
				{
					//INSERT default language translations (using EN_en language, which exists.. just checked)
					this.insertTranslations(this.device.getIdSite(), defaultLan, CF_DEV_MDL, idDevMdl, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(device.getDescriptionKey()).getKeys().get(DESCR),"", "", conn);
				}
				if(langused.length > 1)
				{
					if(langSet.contains("EN_en"))
					{
						//INSERT EN_en language translations (using EN_en language strings from xml)
						this.insertTranslations(this.device.getIdSite(), "EN_en", CF_DEV_MDL, idDevMdl, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(device.getDescriptionKey()).getKeys().get(DESCR),"", "", conn);
					}
					else
					{
						//INSERT EN_en language translations (using default language strings from xml)
						this.insertTranslations(this.device.getIdSite(), "EN_en", CF_DEV_MDL, idDevMdl, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(device.getDescriptionKey()).getKeys().get(DESCR),"", "", conn);
					}
				}
				
				//modbus Device Section
				if(this.device.getModbusInfo() != null)
				{
					insertModbusDevice(idDevMdl, this.device.getModbusInfo(), conn);
					insertModbusFilter(idDevMdl,conn);
				}
				
				// -- NEW CATEGORY ENTRY
				this.insertCategory(conn, defaultLan, langSet, langused);
				Map<String,Integer> categoryIdByCode = getIdCustomCategoryByCode(conn);
				
				
				// Floating Suction Entry
				this.insertFSInfo(conn);
								
				HashMap<Integer, MinMaxManagement>  minMaxrefs = new HashMap<Integer, MinMaxManagement> ();
				Set<String> codeVariableSet = variables.keySet();
				for(String code:codeVariableSet){
					int idVarMdl = -1;
					if(this.device.getModbusInfo() != null)
						idVarMdl = this.insertModbusVariable(idDevMdl, this.variables.get(code), varsModbus.get(code).getAValue(), varsModbus.get(code).getBValue(), varsModbus.get(code).getVarEncoding(),  minMaxrefs, imageStore, conn,categoryIdByCode);
					else
						idVarMdl = this.insertVariable(idDevMdl, this.variables.get(code), minMaxrefs, imageStore, conn,categoryIdByCode);
					if(idVarMdl >0){
						updateIdvarmdlOfComboDb(code,idVarMdl);
						//insert variable translations
						if(langSet.contains(defaultLan))
						{
							try {
								//INSERT default language translations (using default language strings from xml)
								this.insertTranslations(this.device.getIdSite(), defaultLan, "cfvarmdl", idVarMdl, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
										translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
										translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
										conn);
								}
								catch (Exception e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
									e.printStackTrace();
									throw new ImportException("modelerr","Missing description for variable with code:"+code);
								}
						}
						else
						{
							try {
								//INSERT default language translations (using EN_en language, which exists.. just checked)
								this.insertTranslations(this.device.getIdSite(), defaultLan, "cfvarmdl", idVarMdl, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
										translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
										translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
										conn);
								}
								catch (Exception e) {
									LoggerMgr.getLogger(this.getClass()).error(e);
									e.printStackTrace();
									throw new ImportException("modelerr","Missing description for variable with code:"+code);
								}
						}
						if(langused.length > 1)
						{
							if(langSet.contains("EN_en"))
							{
								try {
									//INSERT EN_en language translations (using EN_en language strings from xml)
									this.insertTranslations(this.device.getIdSite(), "EN_en", "cfvarmdl", idVarMdl, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
											translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
											translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
											conn);
									}
									catch (Exception e) {
										LoggerMgr.getLogger(this.getClass()).error(e);
										e.printStackTrace();
										throw new ImportException("modelerr","Missing description for variable with code:"+code);
									}
							}
							else
							{
								try {
									//INSERT EN_en language translations (using default language strings from xml)
									this.insertTranslations(this.device.getIdSite(), "EN_en", "cfvarmdl", idVarMdl, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
											translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
											translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
											conn);
									}
									catch (Exception e) {
										LoggerMgr.getLogger(this.getClass()).error(e);
										e.printStackTrace();
										throw new ImportException("modelerr","Missing description for variable with code:"+code);
									}
							}
						}
					}
					//exception while inserting variable
				}
				
				// INSERT ENUM 
				this.insertEnum(idDevMdl, conn, defaultLan, langSet, langused);
				
				// -- NEW COMBO ENTRY
				this.insertCombo(idDevMdl, this.device.getCode(), conn, defaultLan, langSet, langused);
				
				
				
				//Set MinMax Ref
				boolean minMaxExit = setMinMaxRef(idDevMdl, minMaxrefs, conn);

				if(!minMaxExit)
				{
					throw new ImportException("modelerr","Wrong Min and Max variable references for the device model.");
				}
			}
			else
			{
				throw new ImportException("modelerr","Description language mismatch.");
			}
			
		}catch (ImportException ie)
		{
			throw ie;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new ImportException("error",e);
		}
		return false;
	}
	private void updateIdvarmdlOfComboDb(String varCode,int idvarmdl)
	{
		for(ComboDb combo:combos)
		{
			if(combo.getCode().equals(varCode))
			{
				combo.setIdvarmdl(idvarmdl);
				return;
			}
		}
	}
	private void insertModbusDevice(int idDevMdl,
			DeviceModbusInfoDb modbusInfo, Connection conn) throws ImportException {
		try
		{
			String sql = "insert INTO cfmodbusmodels VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement insertModbusDevice = conn.prepareStatement(sql);
			insertModbusDevice.setInt(1,idDevMdl);
			insertModbusDevice.setInt(2,START_BIT);
			insertModbusDevice.setInt(3, modbusInfo.getStopBit());
			insertModbusDevice.setInt(4, modbusInfo.getParity());
			insertModbusDevice.setInt(5, modbusInfo.getReadTimeOut());
			insertModbusDevice.setInt(6, modbusInfo.getWriteTimeOut());
			insertModbusDevice.setInt(7, modbusInfo.getActivityTimeOut());
			insertModbusDevice.setInt(8, DATA_TRANSMISSION);
			
			ArrayList<Integer> functVal = new ArrayList<Integer>();
			functVal.add(modbusInfo.getF1());
			functVal.add(modbusInfo.getF2());
			functVal.add(modbusInfo.getF3());
			functVal.add(modbusInfo.getF4());
			functVal.add(modbusInfo.getF5());
			functVal.add(modbusInfo.getF6());
			functVal.add(modbusInfo.getF15());
			functVal.add(modbusInfo.getF16());
			
			boolean setDefault = false;
			
			for(int i = 0; i<functVal.size(); i++)
			{
				if(((i==0 || i== 1)&&(functVal.get(i)<0 || functVal.get(i) > 2000 )) ||
				   ((i==2 || i== 3)&&(functVal.get(i)<0 || functVal.get(i) > 125 )) ||
				   ((i==4 || i== 5)&& functVal.get(i)>1) ||
				   (i==6 && (functVal.get(i)<0 || functVal.get(i) > 1968)) ||
				   (i==7 && (functVal.get(i)<0 || functVal.get(i) > 123)))
				{
					setDefault = true;
					break;
				}
			}
			
			if(setDefault)
			{
				insertModbusDevice.setInt(9, 1);
				insertModbusDevice.setInt(10, 1);
				insertModbusDevice.setInt(11, 1);
				insertModbusDevice.setInt(12, 1);
				insertModbusDevice.setInt(13, 1);
				insertModbusDevice.setInt(14, 1);
				insertModbusDevice.setInt(15, 0);
				insertModbusDevice.setInt(16, 0);
			}
			else
			{
				for(int i = 0; i<functVal.size(); i++)
				{
					insertModbusDevice.setInt(i+9, functVal.get(i));
				}
			}
	
			insertModbusDevice.executeUpdate();	 

		}
		catch (Exception e)
		{
			throw new ImportException("error",e,"Error while inserting modbus device - iddevmdl: " + idDevMdl);
		}
	}
	private void insertModbusFilter(int idDevMdl,Connection conn) throws ImportException {
		try
		{
			String sql = "insert INTO cfdevmdlext VALUES (?,?)";
			PreparedStatement insertModbusExt = conn.prepareStatement(sql);
			insertModbusExt.setInt(1,idDevMdl);
			insertModbusExt.setString(2,DevMdlExtBean.MODBUS);
	
			insertModbusExt.executeUpdate();	 

		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while inserting modbus device - iddevmdl: " + idDevMdl);
		}
	}
	private int insertVariable(int idDevMdl, VariableDb var, HashMap<Integer, MinMaxManagement> minMaxRefs, ImagesTag imageStore, Connection conn, Map<String,Integer> categoryIdByCode)throws ImportException
	{
		try
		{
			//change the way to select max(idvarmdl+1)
			//and set new idvarmdl and code inside HashMap structure
			int idVarMdl = SeqMgr.getInstance().next(null, "cfvarmdl", "idvarmdl");
			newIdVarMdl.put(var.getCode(), idVarMdl);
			
			int result = -1;
			//String sql = "insert into cfvarmdl values ((SELECT max(idvarmdl)+1 FROM cfvarmdl),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String sql = "insert into cfvarmdl values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String signed = Utils.booleanToPvString(var.isSigned());
			String ishaccp = Utils.booleanToPvString(var.isHaccp());
			String isActive = Utils.booleanToPvString(var.isActive());
			String isRelay = Utils.booleanToPvString(var.isRelay());
	
			String imageOn = null;
			String imageOff = null;
			String buttonPath = null;
			if(imageStore.getImages().containsKey(var.getImageOn()))
				imageOn = "images/button/"+ device.getCode()+"/"+ imageStore.getImages().get(var.getImageOn()).getFileName();
			if(imageStore.getImages().containsKey(var.getImageOff()))
				imageOff = "images/button/"+ device.getCode()+"/"+ imageStore.getImages().get(var.getImageOff()).getFileName();
	
			if(imageOn!=null && imageOff!=null && !imageOn.equals("") && !(imageOff.equals("")))
			{
				buttonPath = "images/button/setvar.png";
			}
			
			PreparedStatement insertVariable = conn.prepareStatement(sql);
			insertVariable.setInt(1,idVarMdl);
			insertVariable.setInt(2, var.getIdSite());
			insertVariable.setInt(3, idDevMdl);
			insertVariable.setString(4, var.getCode());
			insertVariable.setInt(5, var.getType());
			insertVariable.setInt(6, var.getAddressIn());
			insertVariable.setInt(7, var.getAddressOut());
			insertVariable.setInt(8, var.getVarDimension());
			insertVariable.setInt(9, var.getVarLength());
			insertVariable.setInt(10, var.getBitPosition());
			insertVariable.setString(11,signed);
			insertVariable.setInt(12, var.getDecimal());
			insertVariable.setString(13, var.getToDisplay().toUpperCase());
			insertVariable.setString(14, buttonPath);
			insertVariable.setInt(15, var.getPriority());
			insertVariable.setInt(16, var.getReadWrite());
			if(!var.getMinValue().equals("") && !(var.getMinValue().contains("pk")))
				insertVariable.setString(17, var.getMinValue());
			else
				insertVariable.setNull(17, java.sql.Types.VARCHAR);
			if(!var.getMaxValue().equals("") && !(var.getMaxValue().contains("pk")))
				insertVariable.setString(18, var.getMaxValue());
			else
				insertVariable.setNull(18, java.sql.Types.VARCHAR);
			if(!var.getDefaultValue().equals(""))
				if(var.getDefaultValue().equals("0.0"))
					insertVariable.setString(19, "0");
				else	
					insertVariable.setString(19, var.getDefaultValue());
			else
				insertVariable.setNull(19, java.sql.Types.VARCHAR);
			insertVariable.setString(20, var.getMeasureUnit());
			
			if (var.getIdVarGroup()!=-1)
				insertVariable.setInt(21, var.getIdVarGroup());
			else
				insertVariable.setInt(21, categoryIdByCode.get(var.getCategoryCustomCode()));
			insertVariable.setString(22, imageOn);
			insertVariable.setString(23, imageOff);
			if(var.getFrequency() != 0)
				insertVariable.setInt(24, var.getFrequency());
			else
				insertVariable.setNull(24, java.sql.Types.INTEGER);
			if(var.getDelta() != 0)
				insertVariable.setInt(25, var.getDelta());
			else
				insertVariable.setNull(25, java.sql.Types.NUMERIC);
			if(var.getDelay() != 0)
				insertVariable.setInt(26, var.getDelay());
			else
				insertVariable.setNull(26, java.sql.Types.INTEGER);
			insertVariable.setString(27, ishaccp);
			insertVariable.setString(28,isActive);
			insertVariable.setString(29, isRelay);
			insertVariable.setInt(30, var.getGrpCategory());
			if(var.getHsTime() != 0)
				insertVariable.setInt(31, var.getHsTime());
			else
				insertVariable.setNull(31, java.sql.Types.INTEGER);
			if(var.getHsFrequency() != 0)
				insertVariable.setInt(32, var.getHsFrequency());
			else
				insertVariable.setNull(32, java.sql.Types.INTEGER);
			if(var.getHsDelta() != 0 && var.getHsDelta() != -1)
				insertVariable.setFloat(33, var.getHsDelta());
			else
				insertVariable.setNull(33, java.sql.Types.NUMERIC);
			insertVariable.executeUpdate();
			//RETRIEVE idvarmdl of the just inserted variable to fill the table cftableext
			PreparedStatement variableInserted = conn.prepareStatement("SELECT max(idvarmdl) FROM cfvarmdl");
			ResultSet rs = variableInserted.executeQuery();
	
			if (rs.next())
				result=rs.getInt(1);
			
			if(imageOn != null && !imageOn.equals(""))
			{
				Utils.decodeImage(imageStore.getImages().get(var.getImageOn()).getBytes(), imageStore.getImages().get(var.getImageOn()).getFileName(), DIGITAL_BUTTON_PATH+File.separator + device.getCode());
			
				//create the on_off button icon (shows the variable value in case of 'not accessible' button - depending on user profile)
				String imageon_off = imageStore.getImages().get(var.getImageOn()).getFileName();
			    imageon_off=	imageon_off.substring(0,imageon_off.length()-4)+"_off.png";
				Utils.decodeImage(imageStore.getImages().get(var.getImageOn()).getBytes(),imageon_off , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
			}
			if(imageOff != null && !imageOff.equals(""))
			{
				Utils.decodeImage(imageStore.getImages().get(var.getImageOff()).getBytes(),imageStore.getImages().get(var.getImageOff()).getFileName() , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
				
				//create the off_off button icon (shows the variable value in case of 'not accessible' button - depending on user profile)
				//it is also the 'offline' button icon (equal to the 'on' button icon)
				String imageoff_off = imageStore.getImages().get(var.getImageOff()).getFileName();
			    imageoff_off=	imageoff_off.substring(0,imageoff_off.length()-4)+"_off.png";
				Utils.decodeImage(imageStore.getImages().get(var.getImageOff()).getBytes(),imageoff_off , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
			}
	
			if(result >=0)
			{
				this.addToMinMaxRefList(minMaxRefs, result, var.getMaxValue(), var.getMinValue());
			}
			
			if( var.isCommand() ) {
				PreparedStatement insertCommand = conn.prepareStatement("insert into cfvarcmd values(?, ?);");
				insertCommand.setInt(1, idDevMdl);
				insertCommand.setInt(2, idVarMdl);
				insertCommand.executeUpdate();
			}
			
			return result;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while inserting variable with code " + var.getCode());
		}
	}
	
	private int insertModbusVariable(int idDevMdl, VariableDb var, float aValue, float bValue, int varEncoding, HashMap<Integer, MinMaxManagement> minMaxRefs, ImagesTag imageStore,  Connection conn, Map<String,Integer> categoryIdByCode)throws ImportException
	{
		try
		{
			int idVarMdl = insertVariable(idDevMdl, var, minMaxRefs, imageStore, conn,categoryIdByCode);
	
			//result must be 0, check for robustness
			if(idVarMdl >=0)
			{
				String sql = "insert INTO cfmodbussettings VALUES (?,?,?,?)";
				PreparedStatement insertLinearization = conn.prepareStatement(sql);
				
				insertLinearization.setInt(1, idVarMdl);
				insertLinearization.setDouble(2, aValue);
				insertLinearization.setDouble(3, bValue);
				insertLinearization.setDouble(4, varEncoding);
	
				insertLinearization.executeUpdate();
			}
			return idVarMdl;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while inserting variable with code " + var.getCode());
		}	
	}
	
	
	//update operations
	
	public void updateModel(DeviceTag deviceTag, Connection conn) throws ImportException
	{
		try{
			
			String idDevMdl = getDeviceKey(deviceTag.getCode(), conn);

			//retrieve PVPRO languages
			LangUsedBeanList lan = new LangUsedBeanList();
			LangUsedBean[] langused = lan.retrieveAllLanguage(1);
			List<String> langs = new ArrayList<String>();
			for (int i=0; i<langused.length; i++)
			{
				langs.add(langused[i].getLangcode());
			}
			String defaultLan = LangUsedBeanList.getDefaultLanguage(1);
			
			
			// CHECK Modbus info correlation
			// check if device information refer to a modbus device and device to update has modbus protocol
			// or
			// device info doesn't belong to a modbus device and device to update is not 'modbus' type.
			if(!(deviceTag.isModbusModel() ^ this.isModbusDevice(idDevMdl, conn)))
			{				
				//RETRIEVE used langset
				Set<String> langSet = deviceTag.getTranslations().getHmLang().keySet();
				
				//check if model translations match PVPRO languages (at least one language)
				if(checkModelTranslations(langs, langSet)){
					
					//check if there are instantiated devices
					//if(devicesKey.size()==0){
						//Device model can contain only device and variables code. 
						//It mustn't contain the dB primary key
						Set<String> modelsVariables = deviceTag.getPvInfo().getPvVars().getPVVarsCode();
						//variablesCode [CODE, PRIMARYKEY]
						Map<String, String> variablesCode = getVariablesCode_IdVar(idDevMdl, conn);

						List<String> varsToAdd = new ArrayList<String>();
						List<String> varsToUpdate = new ArrayList<String>();
						List<String> varsToRemove = new ArrayList<String>();

						//find variables have to add or update
						for(String varCode: modelsVariables){
							if(variablesCode.containsKey(varCode))
								varsToUpdate.add(varCode);
							else
								varsToAdd.add(varCode);				
						}

						//find variables have to remove
						for(String modelVarCode:variablesCode.keySet()){
							if(!varsToUpdate.contains(modelVarCode))
								//varsToRemove.add(variablesCode.get(modelVarCode));
								varsToRemove.add(modelVarCode);
						}
						
						//Add, update and remove variables

						HashMap<Integer, MinMaxManagement>  minMaxrefs = new HashMap<Integer, MinMaxManagement> ();
						
						this.insertCategory(conn, defaultLan, langSet, langused);
						Map<String,Integer> categoryIdByCode = getIdCustomCategoryByCode(conn);
						
						//SECTION: add new variables 
						for(String varCode : varsToAdd){

							VariableDb var = new VariableDb();						
							PVVarTag pvVar = deviceTag.getPvInfo().getPvVars().getHmPVVars().get(varCode);
							DeviceVarTag commonVar = deviceTag.getVarCommonInfo().getHmVars().get(varCode);

							int addressIn;
							int addressOut;
							int varDimension;
							int varLength;
							int bitPosition;
							float aValue = 0;
							float bValue = 0;
							int varEncoding = 0;

							if(deviceTag.isModbusModel())
							{
								VarModbusTag modbusInfo = deviceTag.getModbusInfo().getVarsModbus().getAlVarMODBUS().get(varCode);
								addressIn = modbusInfo.getAddressIn();
								addressOut = modbusInfo.getAddressOut();
								varDimension = modbusInfo.getVarDimension();
								varLength = modbusInfo.getVarLength();
								bitPosition = modbusInfo.getBitPosition();
								aValue = modbusInfo.getAValue();
								bValue = modbusInfo.getBValue();
								varEncoding = modbusInfo.getVarEncoding();
							}
							else
							{
								VarCRLTag carelInfo = deviceTag.getCarelInfo().getAlVarCRL().get(varCode);
								addressIn = carelInfo.getAddressIn();
								addressOut = carelInfo.getAddressOut();
								varDimension = carelInfo.getVarDimension();
								varLength = carelInfo.getVarLength();
								bitPosition = carelInfo.getBitPosition();
							}

							String imageOn = null;
							String imageOff = null;
							if(deviceTag.getImagesInfo().getImages().containsKey(pvVar.getImageOn()))
							{	
								// Bugid 9152:Device model updating: digital buttons not visible fixed by longbow 2012/2/20
								//imageOn = "images" + File.separator + "button" + File.separator +  deviceTag.getImagesInfo().getImages().get(pvVar.getImageOn()).getFileName();
								imageOn = pvVar.getImageOn();
							}
							if(deviceTag.getImagesInfo().getImages().containsKey(pvVar.getImageOff()))
							{	
								//imageOff = "images" + File.separator + "button" + File.separator + deviceTag.getImagesInfo().getImages().get(pvVar.getImageOff()).getFileName();
								imageOff = pvVar.getImageOff();
							}
							DecimalFormat f = new DecimalFormat("#.#");
							String defaultvalue = UtilityString.substring(f.format(pvVar.getDefaultValue()),LineConfig.DEFAULT_MAX_MIN);
							var.fillInformations(this.device.getIdSite(), -1, varCode, commonVar.getType(), addressIn, addressOut,
									varDimension, varLength, bitPosition, commonVar.isSigned(), commonVar.getDecimal(),
									pvVar.getToDisplay(), pvVar.getPriority(),pvVar.getReadWrite(), commonVar.getMinValue(),
									commonVar.getMaxValue(), defaultvalue, commonVar.getUnitOfMeasure(), pvVar.getCategory(),
									imageOn, imageOff, pvVar.isHaccp(), pvVar.isRelay(), pvVar.getHsTime(), pvVar.getHsFrequency(),pvVar.getHsDelta(),pvVar.getCategory_code(), pvVar.isCommand());
							
							
							
							int idVarMdl = -1;
							if(deviceTag.isModbusModel())
								idVarMdl = this.insertModbusVariable(Integer.parseInt(idDevMdl), var, aValue, bValue, varEncoding, minMaxrefs, deviceTag.getImagesInfo(), conn,categoryIdByCode);
							else
								idVarMdl = this.insertVariable(Integer.parseInt(idDevMdl), var, minMaxrefs, deviceTag.getImagesInfo(), conn,categoryIdByCode);
							if(idVarMdl >0){
								this.updateIdvarmdlOfComboDb(varCode, idVarMdl);
								if(langSet.contains(defaultLan))
								{
									//INSERT default language translations (using default language strings from xml)
									this.insertTranslations(this.device.getIdSite(), defaultLan, CF_VAR_MDL , idVarMdl, 
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
								}
								else
								{
									//INSERT default language translations (using EN_en language strings from xml)
									this.insertTranslations(this.device.getIdSite(), defaultLan, CF_VAR_MDL , idVarMdl, 
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
								}
								if(langused.length > 1)
								{
									if(langSet.contains("EN_en"))
									{
										//INSERT EN_en language translations (using EN_en language strings from xml)
										this.insertTranslations(this.device.getIdSite(), "EN_en", CF_VAR_MDL , idVarMdl, 
												deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
												deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
												deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
												conn);
									}
									else
									{
										//INSERT EN_en language translations (using default language strings from xml)
										this.insertTranslations(this.device.getIdSite(), "EN_en", CF_VAR_MDL , idVarMdl, 
												deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
												deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
												deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
												conn);
									}
								}
							}
							else{
									throw new ImportException("error","Variable " + var.getCode() + " - " + this.device.getCode() + " not updated");
							}	
						}
						
						//SECTION: update variables
						for(String varCode : varsToUpdate){

							PVVarTag pvVar = deviceTag.getPvInfo().getPvVars().getHmPVVars().get(varCode);
							DeviceVarTag commonVar = deviceTag.getVarCommonInfo().getHmVars().get(varCode);

							VariableDb varToUpd = this.variables.get(varCode);
							updateIdvarmdlOfComboDb(varCode,Integer.parseInt(variablesCode.get(varCode)));
							//UPDATE variable: set correct values for idvargrp - grpcategory
						
							
							int idvargroup = pvVar.getCategory();
							int type = varToUpd.getType();
							if(type == 4)
							{
								if(pvVar.getCategory() == 1)
								{	
									pvVar.setCategory(55);
									idvargroup = 8;
								}
							}
							
							//UPDATE variable: set correct values for frequency - delta - delay in case of HACCP flag set to TRUE
							int frequency = 0;
							int delta = 0;
							int delay = 0;
							if(pvVar.isHaccp())
							{
								frequency = 900;
							}
							
							if(deviceTag.isModbusModel())
							{
								VarModbusTag modbusInfo = deviceTag.getModbusInfo().getVarsModbus().getAlVarMODBUS().get(varCode);
								DecimalFormat f = new DecimalFormat("#.#");
								String defaultvalue = UtilityString.substring(f.format(pvVar.getDefaultValue()),LineConfig.DEFAULT_MAX_MIN);
								this.updateModbusVariable(Integer.parseInt(variablesCode.get(varCode)), this.device.getIdSite(), 
										Integer.parseInt(idDevMdl), varCode, commonVar.getType(), modbusInfo.getAddressIn(), 
										modbusInfo.getAddressOut(), modbusInfo.getVarDimension(), modbusInfo.getVarLength(),
										modbusInfo.getBitPosition(), commonVar.isSigned(), commonVar.getDecimal(), 
										pvVar.getToDisplay(), pvVar.getPriority(), pvVar.getReadWrite(), commonVar.getMinValue(),
										commonVar.getMaxValue(), defaultvalue, commonVar.getUnitOfMeasure(),
										pvVar.getImageOn(), pvVar.getImageOff(), frequency, delta, delay, pvVar.isHaccp(), pvVar.isRelay(), idvargroup, pvVar.getCategory(),
										pvVar.getHsTime(), pvVar.getHsFrequency(), pvVar.getHsDelta(), modbusInfo.getAValue(),modbusInfo.getBValue(), modbusInfo.getVarEncoding(), minMaxrefs, deviceTag.getImagesInfo(), conn,categoryIdByCode, pvVar.getCategory_code(),
										pvVar.isCommand());
							}
							else
							{	
								VarCRLTag carelInfo = deviceTag.getCarelInfo().getAlVarCRL().get(varCode);
								DecimalFormat f = new DecimalFormat("#.#");
								String defaultvalue = UtilityString.substring(f.format(pvVar.getDefaultValue()),LineConfig.DEFAULT_MAX_MIN);
								this.updateVariable(Integer.parseInt(variablesCode.get(varCode)), this.device.getIdSite(), 
										Integer.parseInt(idDevMdl), varCode, commonVar.getType(), carelInfo.getAddressIn(), 
										carelInfo.getAddressOut(), carelInfo.getVarDimension(), carelInfo.getVarLength(),
										carelInfo.getBitPosition(),	commonVar.isSigned(), commonVar.getDecimal(), 
										pvVar.getToDisplay(), pvVar.getPriority(), pvVar.getReadWrite(), commonVar.getMinValue(),
										commonVar.getMaxValue(), defaultvalue, commonVar.getUnitOfMeasure(), 
										pvVar.getImageOn(), pvVar.getImageOff(), frequency, delta , delay, pvVar.isHaccp(), pvVar.isRelay(), idvargroup, pvVar.getCategory(),
										pvVar.getHsTime(), pvVar.getHsFrequency(), pvVar.getHsDelta(), minMaxrefs, deviceTag.getImagesInfo(), conn, categoryIdByCode, pvVar.getCategory_code(),
										pvVar.isCommand());

							}

							if(langSet.contains(defaultLan))
							{
								//UPDATE default language translations (using default language strings from xml)
								this.updateTranslations(this.device.getIdSite(), defaultLan, CF_VAR_MDL, Integer.parseInt(variablesCode.get(varCode)),
										deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
										deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
										deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
										conn);
								String devcodeSql = "select idvariable from cfvariable  where idvarmdl = " + Integer.parseInt(variablesCode.get(varCode)) ;
						    	RecordSet rsdevcode = DatabaseMgr.getInstance().executeQuery(null, devcodeSql, null);
						        for(int i=0;i<rsdevcode.size();i++){
						        	this.updateTranslations(this.device.getIdSite(), defaultLan, "cfvariable", Integer.parseInt( rsdevcode.get(i).get(0).toString() ),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
						        }
							}
							else
							{
								//UPDATE default language translations (using EN_en language strings from xml)
								this.updateTranslations(this.device.getIdSite(), defaultLan, CF_VAR_MDL, Integer.parseInt(variablesCode.get(varCode)),
										deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
										deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
										deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
										conn);
								String devcodeSql = "select idvariable from cfvariable  where idvarmdl = " + Integer.parseInt(variablesCode.get(varCode)) ;
						    	RecordSet rsdevcode = DatabaseMgr.getInstance().executeQuery(null, devcodeSql, null);
						    	for(int i=0;i<rsdevcode.size();i++){
						        	this.updateTranslations(this.device.getIdSite(), defaultLan, "cfvariable", Integer.parseInt( rsdevcode.get(i).get(0).toString() ),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
						        }
							}
							if(langused.length > 1)
							{
								if(langSet.contains("EN_en"))
								{
									//UPDATE EN_en language translations (using EN_en language strings from xml)
									this.updateTranslations(this.device.getIdSite(), "EN_en", CF_VAR_MDL, Integer.parseInt(variablesCode.get(varCode)),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
									String devcodeSql = "select idvariable from cfvariable  where idvarmdl = " + Integer.parseInt(variablesCode.get(varCode)) ;
							    	RecordSet rsdevcode = DatabaseMgr.getInstance().executeQuery(null, devcodeSql, null);
							    	for(int i=0;i<rsdevcode.size();i++){
							        	this.updateTranslations(this.device.getIdSite(), "EN_en", "cfvariable", Integer.parseInt( rsdevcode.get(i).get(0).toString() ),
												deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
												conn);
							        }
								}
								else
								{
									//UPDATE EN_en language translations (using default language strings from xml)
									this.updateTranslations(this.device.getIdSite(), "EN_en", CF_VAR_MDL, Integer.parseInt(variablesCode.get(varCode)),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(DESCR),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(SHORTDESCR),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
											conn);
									String devcodeSql = "select idvariable from cfvariable  where idvarmdl = " + Integer.parseInt(variablesCode.get(varCode)) ;
							    	RecordSet rsdevcode = DatabaseMgr.getInstance().executeQuery(null, devcodeSql, null);
							    	for(int i=0;i<rsdevcode.size();i++){
							        	this.updateTranslations(this.device.getIdSite(), "EN_en", "cfvariable", Integer.parseInt( rsdevcode.get(i).get(0).toString() ),
												deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(varCode).getHmKey().get(LONGDESCR),
												conn);
							        }
								}
							}
						}
						
						//Kevin Ge, 2012-12-4
						//remove combo before remove cfvarmdl because remove combo need to inner join cfvarmdl
						this.deletedComboAndOption(idDevMdl,conn);
						
						//SECTION: remove variables
						for(String varCode : varsToRemove){
							this.removeVariable(Integer.parseInt(variablesCode.get(varCode)), conn);
							if(deviceTag.isModbusModel())
								this.removeModbusVariable(Integer.parseInt(variablesCode.get(varCode)), conn);
						}
						
						//ENUM & COMBO SECTION
						//  -- DELETE OLD enums entry
						String sql = "DELETE FROM cftableext WHERE tablename = 'cfenum' AND tableid in " +
								"(SELECT idenum from cfenum WHERE iddevmdl = ?) AND idsite=?";
						PreparedStatement deleteEnumsTranslations = conn.prepareStatement(sql);
						deleteEnumsTranslations.setInt(1, Integer.parseInt(idDevMdl));
						deleteEnumsTranslations.setInt(2, ID_SITE);
						deleteEnumsTranslations.executeUpdate();
						
						sql = "DELETE FROM cfenum where iddevmdl = ?";
						PreparedStatement deleteEnums = conn.prepareStatement(sql);
						deleteEnums.setInt(1, Integer.parseInt(idDevMdl));
						deleteEnums.executeUpdate();
						
						// -- NEW ENUMS ENTRY
						this.insertEnum(Integer.parseInt(idDevMdl), conn, defaultLan, langSet, langused);
						
						// -- NEW COMBO ENTRY
						this.insertCombo(Integer.parseInt(idDevMdl),deviceTag.getCode() , conn, defaultLan, langSet, langused);
					
						// Floating Suction UPDATE
						this.insertFSInfo(conn);
						
						//set MinMax ref
						boolean minMaxExit = setMinMaxRef(Integer.parseInt(idDevMdl), minMaxrefs, conn);

						if(minMaxExit)
						{
							//SECTION: update device
							this.updateDeviceInfo(this.device.getIdSite(), idDevMdl, deviceTag, conn);

							if(deviceTag.isModbusModel())
								this.updateModbusDevice(idDevMdl, deviceTag.getModbusInfo(), conn);

							//SECTION: update device translations
							if(langSet.contains(defaultLan))
							{
								//UPDATE default language translations (using default language strings from xml)
								this.updateTranslations(this.device.getIdSite(), defaultLan, CF_DEV_MDL, Integer.parseInt(idDevMdl),
										deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(deviceTag.getDescriptionKey()).getHmKey().get(DESCR),
										"","",conn);
							}
							else
							{
								//UPDATE default language translations (using EN_en language strings from xml)
								this.updateTranslations(this.device.getIdSite(), defaultLan, CF_DEV_MDL, Integer.parseInt(idDevMdl),
										deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(deviceTag.getDescriptionKey()).getHmKey().get(DESCR),
										"","",conn);
							}
							if(langused.length > 1)
							{
								if(langSet.contains("EN_en"))
								{
									//UPDATE EN_en language translations (using EN_en language strings from xml)
									this.updateTranslations(this.device.getIdSite(), "EN_en", CF_DEV_MDL, Integer.parseInt(idDevMdl),
											deviceTag.getTranslations().getHmLang().get(EN).getHmSection().get("PV").getHmItem().get(deviceTag.getDescriptionKey()).getHmKey().get(DESCR),
											"","",conn);
								}
								else
								{
									//UPDATE EN_en language translations (using default language strings from xml)
									this.updateTranslations(this.device.getIdSite(), "EN_en", CF_DEV_MDL, Integer.parseInt(idDevMdl),
											deviceTag.getTranslations().getHmLang().get(defaultLan).getHmSection().get("PV").getHmItem().get(deviceTag.getDescriptionKey()).getHmKey().get(DESCR),
											"","",conn);
								}
							}
						}
						else
						{
							throw new ImportException("modelerr","Wrong Min and Max variable references for the device.");
						}
					/*}
					else{
						//device is instantiated
						throw new ImportException("upddevused"," Device model with code '" + this.device.getCode() + "' is used in ore or more lines.");
					}*/
				}
				else{
					//missing translations
					throw new ImportException("modelerr","Description language mismatch.");
				}
			}else
			{
				//protocol mismatch
				throw new ImportException("wrongprot","Protocol mismatch while updating the device model.");
			}

		}
		catch(ImportException ie)
		{
			LoggerMgr.getLogger(this.getClass()).error(ie);
			ie.printStackTrace();
			throw ie;
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while updating device.");
		}
	}
	private void deletedComboAndOption(String idDevMdl,Connection conn) throws SQLException
	{
		String sql = "DELETE FROM cftableext WHERE tablename = 'cfoption' AND tableid in " +
			  "(SELECT idoption from cfcomboset "+
			  "INNER JOIN cfvarmdl on cfvarmdl.idvarmdl=cfcomboset.idvarmdl and cfvarmdl.iddevmdl=?)";
		PreparedStatement deleteOptionTranslations = conn.prepareStatement(sql);
		deleteOptionTranslations.setInt(1, Integer.parseInt(idDevMdl));
		deleteOptionTranslations.executeUpdate();

		sql = "DELETE FROM cfoption WHERE idoption in "+
			  "(SELECT idoption from cfcomboset "+
			  "INNER JOIN cfvarmdl on cfvarmdl.idvarmdl=cfcomboset.idvarmdl and cfvarmdl.iddevmdl=?)";
		PreparedStatement deleteOption = conn.prepareStatement(sql);
		deleteOption.setInt(1, Integer.parseInt(idDevMdl));
		deleteOption.executeUpdate();

		sql = "DELETE FROM cfcomboset where idvarmdl in "+
				"(SELECT idvarmdl FROM cfvarmdl where cfvarmdl.iddevmdl=?)";
		PreparedStatement deleteComboset = conn.prepareStatement(sql);
		deleteComboset.setInt(1, Integer.parseInt(idDevMdl));
		deleteComboset.executeUpdate();
		
		sql = "DELETE FROM cfcombo where iddevmdl=? ";
		PreparedStatement deleteCombo = conn.prepareStatement(sql);
		deleteCombo.setInt(1, Integer.parseInt(idDevMdl));
		deleteCombo.executeUpdate();
	}
	public HashMap<String, Integer> getNewIdVarMdl()
	{
		return newIdVarMdl;
	}

	private int updateVariable(int idVarMdl, int idSite, int idDevMdl, String code, int type, int addressIn,
			int addressOut, int varDimension, int varLength, int bitPosition, boolean isSigned, int decimal,
			String toDisplay, int priority, int readW, String min, String max, String defaultValue,
			String measureUnit, String imageOn, String imageOff, int frequency, int delta, int delay, boolean isHaccp,
			boolean isRelay, int idVarGroup, int grpCategory, int hsTime, int hsFreq, float hsDelta, 
			HashMap<Integer, MinMaxManagement>  minMaxRefs,  ImagesTag imageStore, Connection conn, Map<String,Integer> categoryIdByCode, String custom_categ,
			boolean isCommand)throws ImportException
			{
				try
				{
					int result = -1;
					
					String sql = "UPDATE cfvarmdl SET  type = ?, addressin = ?," +
					"addressout = ?, vardimension = ?, varlength = ?, bitposition = ?, signed = ?, decimal = ?," +
					"todisplay = ?, buttonpath = ?, priority = ?, readwrite = ?, minvalue = ?, maxvalue = ?, defaultvalue = ?," +
					"measureunit = ?,idvargroup = ?, imageon = ?, imageoff = ?, frequency = ?, delta = ?, " +
					"delay = ?, ishaccp = ?, isactive = ?, isrelay = ?, grpcategory = ?,hstime = ?, hsfrequency =?," +
					"hsdelta = ? WHERE idsite = ? AND iddevmdl = ? AND code = ? ";
			
					String signed = Utils.booleanToPvString(isSigned);
					String haccp = Utils.booleanToPvString(isHaccp);
					String relay = Utils.booleanToPvString(isRelay);
			
					String imageOnToUpdate = null;
					String imageOffToUpdate = null;
					String imageButtonToUpdate = null;
					if(imageStore.getImages().containsKey(imageOn))
						imageOnToUpdate = "images/button/"+ device.getCode()+"/"+ imageStore.getImages().get(imageOn).getFileName();
					if(imageStore.getImages().containsKey(imageOff))
						imageOffToUpdate = "images/button/"+ device.getCode()+"/"+ imageStore.getImages().get(imageOff).getFileName();
			
					if(imageOnToUpdate!=null && imageOffToUpdate!=null && !imageOnToUpdate.equals("") && !imageOffToUpdate.equals(""))
					{
						imageButtonToUpdate="images/button/setvar.png";
					}
					
					PreparedStatement updateVariable = conn.prepareStatement(sql);
					updateVariable.setInt(1, type);
					updateVariable.setInt(2, addressIn);
					updateVariable.setInt(3, addressOut);
					updateVariable.setInt(4, varDimension);
					updateVariable.setInt(5, varLength);
					updateVariable.setInt(6, bitPosition);
					updateVariable.setString(7, signed);
					updateVariable.setInt(8, decimal);
					updateVariable.setString(9, toDisplay.toUpperCase());
					updateVariable.setString(10, imageButtonToUpdate);
					updateVariable.setInt(11, priority);
					updateVariable.setInt(12, readW);
					if(!min.equals("") && !(min.contains("pk")))
						updateVariable.setString(13, min);
					else
						updateVariable.setNull(13, java.sql.Types.VARCHAR);
					if(!max.equals("") && !(max.contains("pk")))
						updateVariable.setString(14, max);
					else
						updateVariable.setNull(14, java.sql.Types.VARCHAR);
					if(!defaultValue.equals(""))
						if(defaultValue.equals("0.0"))
							updateVariable.setString(15, "0");
						else
							updateVariable.setString(15, defaultValue);
					else
						updateVariable.setNull(15, java.sql.Types.VARCHAR);
					
					updateVariable.setString(16, measureUnit);
					
					
					if (idVarGroup!=-1)
						updateVariable.setInt(17, idVarGroup);
					else
						updateVariable.setInt(17, categoryIdByCode.get(custom_categ));
					
					
					//updateVariable.setInt(17, idVarGroup);
					updateVariable.setString(18, imageOnToUpdate);
					updateVariable.setString(19, imageOffToUpdate);
					if(frequency != 0)
						updateVariable.setInt(20, frequency);
					else
						updateVariable.setNull(20, java.sql.Types.INTEGER);
					if(delta != 0)
						updateVariable.setInt(21, delta);
					else
						updateVariable.setNull(21, java.sql.Types.NUMERIC);
					if(delay != 0)
						updateVariable.setInt(22, delay);
					else
						updateVariable.setNull(22, java.sql.Types.INTEGER);
					updateVariable.setString(23, haccp);
					updateVariable.setString(24, "TRUE");
					updateVariable.setString(25, relay);
					updateVariable.setInt(26, grpCategory);
					if(hsTime != 0)
						updateVariable.setInt(27, hsTime);
					else
						updateVariable.setNull(27, java.sql.Types.INTEGER);
					if(hsFreq != 0)
						updateVariable.setInt(28, hsFreq);
					else
						updateVariable.setNull(28, java.sql.Types.INTEGER);
					if(hsDelta != 0 && hsDelta != -1)
						updateVariable.setFloat(29, hsDelta);
					else
						updateVariable.setNull(29, java.sql.Types.NUMERIC);
			
					updateVariable.setInt(30, idSite);
					updateVariable.setInt(31, idDevMdl);
					updateVariable.setString(32, code);
					
					result = updateVariable.executeUpdate();
			
					if(result >0)
					{
						if(imageOnToUpdate!=null && !imageOnToUpdate.equals(""))
						{
							Utils.decodeImage(imageStore.getImages().get(imageOn).getBytes(), imageStore.getImages().get(imageOn).getFileName(), DIGITAL_BUTTON_PATH+File.separator + device.getCode());
							
							//create the 'on_off' button icon (equal to the 'on' button icon) 
							String imageoon_off = imageStore.getImages().get(imageOn).getFileName();
						    imageoon_off=	imageoon_off.substring(0,imageoon_off.length()-4)+"_off.png";
							Utils.decodeImage(imageStore.getImages().get(imageOn).getBytes(),imageoon_off , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
						
						}
						if(imageOffToUpdate!=null && !imageOffToUpdate.equals(""))
						{
							Utils.decodeImage(imageStore.getImages().get(imageOff).getBytes(), imageStore.getImages().get(imageOff).getFileName() , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
							
							//create the 'off_off' button icon (equal to the 'off' button icon) 
							String imageoff_off = imageStore.getImages().get(imageOff).getFileName();
						    imageoff_off=	imageoff_off.substring(0,imageoff_off.length()-4)+"_off.png";
							Utils.decodeImage(imageStore.getImages().get(imageOff).getBytes(),imageoff_off , DIGITAL_BUTTON_PATH+File.separator + device.getCode());
						}
			
						this.addToMinMaxRefList(minMaxRefs, idVarMdl, max, min);
					}
					
					// update command
					PreparedStatement updateCommand;
					if( isCommand ) {
						updateCommand = conn.prepareStatement("insert into cfvarcmd values(?, ?);");
						updateCommand.setInt(1, idDevMdl);
						updateCommand.setInt(2, idVarMdl);
					}
					else {
						updateCommand = conn.prepareStatement("delete from cfvarcmd where idvarmdl=?;");
						updateCommand.setInt(1, idVarMdl);						
					}
					updateCommand.executeUpdate();
					
					return result;
				}
				catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e);
					e.printStackTrace();
					throw new ImportException("error",e,"Error while updating variable with code " + code);
				}
			}
	
	private void updateModbusVariable(int idVarMdl, int idSite, int idDevMdl, String code, int type, int addressIn,
			int addressOut, int varDimension, int varLength, int bitPosition, boolean isSigned, int decimal,
			String toDisplay, int priority, int readW, String min, String max, String defaultValue,
			String measureUnit, String imageOn, String imageOff, int frequency, int delta, int delay, boolean isHaccp,
			boolean isRelay, int idVarGroup, int grpCategory, int hsTime, int hsFreq, float hsDelta, 
			float aValue, float bValue, int varEncoding, HashMap<Integer, MinMaxManagement>  minMaxRefs,  
			ImagesTag imageStore, Connection conn, Map<String,Integer> categoryIdByCode,String custom_categ,
			boolean isCommand)throws ImportException
			{
				try
				{
					this.updateVariable(idVarMdl, idSite, idDevMdl, code, type, addressIn, addressOut, varDimension, varLength, bitPosition, isSigned, decimal, toDisplay, priority, readW, min, max, defaultValue, measureUnit, imageOn, imageOff, frequency, delta, delay, isHaccp, isRelay, idVarGroup, grpCategory, hsTime, hsFreq, hsDelta, minMaxRefs, imageStore, conn, categoryIdByCode,custom_categ,isCommand);
			
					String sql = "UPDATE cfmodbussettings SET idvarmdl = ?, avalue = ?, bvalue = ?, encoding = ? WHERE idvarmdl = ?";
					PreparedStatement updateLinearization = conn.prepareStatement(sql);
					updateLinearization.setInt(1, idVarMdl);
					updateLinearization.setDouble(2, aValue);
					updateLinearization.setDouble(3, bValue);
					updateLinearization.setInt(4, varEncoding);
					updateLinearization.setInt(5, idVarMdl);
			
					updateLinearization.executeUpdate();
				}
				catch (Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e);
					e.printStackTrace();
					throw new ImportException("error",e,"Error while updating modbus variable with code: " + code);
				}
			}

	private int updateDeviceInfo(int idSite, String deviceKey, DeviceTag deviceTag, Connection conn)throws ImportException
	{
		try
		{
			String sql = "UPDATE cfdevmdl SET idsite = ?, code = ?, manufacturer = ?, hdversion = ?," +
			"swversion = ?, imagepath = ?, littlendian = ?, ide = ? WHERE iddevmdl = ?";
	
			PreparedStatement deviceToUpdate = conn.prepareStatement(sql);
			String deviceImage = "";
			if(deviceTag.getImagesInfo().getImages().containsKey(this.device.getImage()))
				deviceImage = deviceTag.getImagesInfo().getImages().get(deviceTag.getImage()).getFileName();
	
			boolean ide = false;
			if(deviceTag.getIde() == 1)
				ide= true;
	
			deviceToUpdate.setInt(1, idSite);
			deviceToUpdate.setString(2, deviceTag.getCode());
			deviceToUpdate.setString(3, deviceTag.getManufacturer());
			deviceToUpdate.setString(4, HD_VERSION);
			deviceToUpdate.setString(5, deviceTag.getSwVersion());
			deviceToUpdate.setString(6, deviceImage);
			deviceToUpdate.setString(7, Utils.booleanToPvString(deviceTag.getLittleEndian()));
			deviceToUpdate.setString(8, Utils.booleanToPvString(ide));
	
			deviceToUpdate.setInt(9, Integer.parseInt(deviceKey));
	
			int res = deviceToUpdate.executeUpdate();
			
			if(deviceTag.getImagesInfo().getImages().containsKey(this.device.getImage()))
				Utils.decodeImage( deviceTag.getImagesInfo().getImages().get(this.device.getImage()).getBytes(), deviceTag.getImagesInfo().getImages().get(this.device.getImage()).getFileName(), IMG_PATH );
	
	
			return res;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while updating device info - code: " + deviceTag.getCode());
		}
	}
	
	private void updateModbusDevice(String idDevMdl,
			ModbusTag modbusInfo, Connection conn) throws ImportException{

		try
		{
			String sql = "UPDATE cfmodbusmodels SET iddevmdl = ?, startbit = ?, stopbit = ?, parity = ?, readtimeout = ?, writetimeout = ?," +
			"activitytimeout = ?, datatransmission = ?, f1 = ?, f2 = ?, f3 = ?, f4 = ?, f5 = ?, f6 = ?," +
			"f15 = ?, f16 = ? WHERE iddevmdl = ?";
			PreparedStatement insertModbusDevice = conn.prepareStatement(sql);
			insertModbusDevice.setInt(1,Integer.parseInt(idDevMdl));
			insertModbusDevice.setInt(2,START_BIT);
			insertModbusDevice.setInt(3, modbusInfo.getStopBit());
			insertModbusDevice.setInt(4, modbusInfo.getParity());
			insertModbusDevice.setInt(5, modbusInfo.getReadTimeOut());
			insertModbusDevice.setInt(6, modbusInfo.getWriteTimeOut());
			insertModbusDevice.setInt(7, modbusInfo.getActivityTimeOut());
			insertModbusDevice.setInt(8, DATA_TRANSMISSION);
			
			ArrayList<Integer> functVal = new ArrayList<Integer>();
			functVal.add(modbusInfo.getF1());
			functVal.add(modbusInfo.getF2());
			functVal.add(modbusInfo.getF3());
			functVal.add(modbusInfo.getF4());
			functVal.add(modbusInfo.getF5());
			functVal.add(modbusInfo.getF6());
			functVal.add(modbusInfo.getF15());
			functVal.add(modbusInfo.getF16());
			
			boolean setDefault = false;
			
			for(int i = 0; i<functVal.size(); i++)
			{
				if(functVal.get(i) < 0 || functVal.get(i) > 255 || (i==4 && functVal.get(i)>1) || (i==5 && functVal.get(i)>1))
				{
					setDefault = true;
					break;
				}
			}
			
			if(setDefault)
			{
				insertModbusDevice.setInt(9, 1);
				insertModbusDevice.setInt(10, 1);
				insertModbusDevice.setInt(11, 1);
				insertModbusDevice.setInt(12, 1);
				insertModbusDevice.setInt(13, 1);
				insertModbusDevice.setInt(14, 1);
				insertModbusDevice.setInt(15, 0);
				insertModbusDevice.setInt(16, 0);
			}
			else
			{
				for(int i = 0; i<functVal.size(); i++)
				{
					insertModbusDevice.setInt(i+9, functVal.get(i));
				}
			}
			
			insertModbusDevice.setInt(17,Integer.parseInt(idDevMdl));
	
			insertModbusDevice.executeUpdate();	 
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while updating modbus device - iddevmdl: " + idDevMdl);
		}
	}

	//remove operations
	
	public boolean removeModel(String idDevMdl, Connection conn) throws ImportException
	{
		try{
			String sql;

			if(idDevMdl == "")
				return true;
			if(instancedDevice(idDevMdl, conn).size()==0)
			{
				ResultSet variables = this.getVariables(idDevMdl, conn);

				//DELETE device
				sql = "DELETE FROM cfdevmdl WHERE idsite=1 AND iddevmdl=?";
				PreparedStatement deleteDevice = conn.prepareStatement(sql);
				deleteDevice.setInt(1, Integer.parseInt(idDevMdl));
				int res = deleteDevice.executeUpdate();
				
				//DELETE references of the 'command' variables
				sql = "DELETE FROM cfvarcmd WHERE iddevmdl=?";
				PreparedStatement deleteVarCmd = conn.prepareStatement(sql);
				deleteVarCmd.setInt(1, Integer.parseInt(idDevMdl));
				deleteVarCmd.executeUpdate();

				sql = "DELETE FROM cfmodbusmodels WHERE iddevmdl = ?";
				//DELETE device modbus info
				PreparedStatement deleteModbusDevice = conn.prepareStatement(sql);
				deleteModbusDevice.setInt(1, Integer.parseInt(idDevMdl));
				res = deleteModbusDevice.executeUpdate();
				
				if(res > 0)//ModbusDevice
				{
					sql = "DELETE FROM cfmodbussettings WHERE idvarmdl in (SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl = ? AND idsite = ?)";
					PreparedStatement deleteModbusVariable = conn.prepareStatement(sql);
					deleteModbusVariable.setInt(1, Integer.parseInt(idDevMdl));
					deleteModbusVariable.setString(2, "1");
					res = deleteModbusVariable.executeUpdate();
					
					sql = "DELETE FROM  cfdevmdlext WHERE iddevmdl=?";
					PreparedStatement deleteModbusExt = conn.prepareStatement(sql);
					deleteModbusExt.setInt(1, Integer.parseInt(idDevMdl));
					res = deleteModbusExt.executeUpdate();
				}
				
				//DELETE device translations
				sql = "DELETE FROM cftableext WHERE tablename = 'cfdevmdl' AND tableid=? AND idsite = ?";
				PreparedStatement deleteDevTranslation = conn.prepareStatement(sql);
				deleteDevTranslation.setInt(1, Integer.parseInt(idDevMdl));
				deleteDevTranslation.setInt(2, ID_SITE);
				res = deleteDevTranslation.executeUpdate();

				//DELETE variable translations
				if(variables != null){
					sql = "DELETE FROM cftableext WHERE tablename = 'cfvarmdl' AND tableid in (SELECT idvarmdl from cfvarmdl WHERE iddevmdl = ? AND idsite=?) AND idsite=?";
					PreparedStatement deleteVarTranslation = conn.prepareStatement(sql);
					deleteVarTranslation.setInt(1, Integer.parseInt(idDevMdl));
					deleteVarTranslation.setInt(2, 1);
					deleteVarTranslation.setInt(3, 1);
					res = deleteVarTranslation.executeUpdate();
				}
							
				//DELETE button image folder and content
				String devcodeSql = "select code from cfdevmdl  where idsite=? and iddevmdl=? ";
		    	RecordSet rsdevcode = DatabaseMgr.getInstance().executeQuery(null, devcodeSql, new Object[]{ID_SITE,Integer.parseInt(idDevMdl)});
		        if (rsdevcode.size()>0)   
		        {
		        	String path = BaseConfig.getAppHome()+"images"+File.separator+"button"+File.separator+(String)(rsdevcode.get(0).get(0));
		        	
		        	//delete directory content
		        	FileSystemUtils.deleteDir(path, null);
		        	
		        	//delete directory
		        	File imageFolder = new File(path);
					if(imageFolder.exists()){
						imageFolder.delete();
					}
		        }
		        //delete combo first
		        this.deletedComboAndOption(idDevMdl,conn);
		        
				//DELETE variables
				sql = "DELETE FROM cfvarmdl WHERE iddevmdl = ? AND idsite =?";
				PreparedStatement deleteVariable = conn.prepareStatement(sql);
				deleteVariable.setInt(1, Integer.parseInt(idDevMdl));
				deleteVariable.setInt(2, ID_SITE);
				res = deleteVariable.executeUpdate();
				
				// DELETE commands
				sql = "DELETE FROM cfvarcmd WHERE iddevmdl = ?";
				PreparedStatement deleteCommand = conn.prepareStatement(sql);
				deleteCommand.setInt(1, Integer.parseInt(idDevMdl));
				res = deleteCommand.executeUpdate();
				
				//DELETE enums entry
				sql = "DELETE FROM cftableext WHERE tablename = 'cfenum' AND tableid in " +
						"(SELECT idenum from cfenum WHERE iddevmdl = ?) AND idsite=?";
				PreparedStatement deleteEnumsTranslations = conn.prepareStatement(sql);
				deleteEnumsTranslations.setInt(1, Integer.parseInt(idDevMdl));
				deleteEnumsTranslations.setInt(2, ID_SITE);
				res = deleteEnumsTranslations.executeUpdate();
				
				sql = "DELETE FROM cfenum where iddevmdl = ?";
				PreparedStatement deleteEnums = conn.prepareStatement(sql);
				deleteEnums.setInt(1, Integer.parseInt(idDevMdl));
				res = deleteEnums.executeUpdate();
			}
			else
			{	
				throw new ImportException("remdevused"," Impossible to remove device with id: " + idDevMdl);
			}
		}
		catch(ImportException ie)
		{
			Log.error("ImportException:"+ie.getMessage(),ie);
			throw ie;
		}
		catch(Exception e)
		{
			Log.error("Exception while deleting device:"+e.getMessage(),e);
			throw new ImportException("error",e,"Error while removing device - iddevmdl: " + idDevMdl);
		}

		return true;
	}
	
	private void removeVariable(int idvarMdl, Connection conn)throws ImportException
	{
		try
		{
			String sql = "DELETE FROM cfvarmdl WHERE idvarmdl = ?";
			PreparedStatement variableToRemove = conn.prepareStatement(sql);
			variableToRemove.setInt(1, idvarMdl);
			variableToRemove.executeUpdate();
			this.removeTranslations(CF_VAR_MDL, idvarMdl, conn);
			// remove command variable
			sql = "DELETE FROM cfvarcmd WHERE idvarmdl = ?";
			PreparedStatement commandToRemove = conn.prepareStatement(sql);
			commandToRemove.setInt(1, idvarMdl);
			commandToRemove.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while deleting variable - idvarmdl: " + idvarMdl);
		}
	}

	private void removeModbusVariable(int idVarMdl, Connection conn)throws ImportException {

		try
		{
			String sql = "DELETE FROM cfmodbussettings WHERE idvarmdl = ?";
			PreparedStatement variableToRemove = conn.prepareStatement(sql);
			variableToRemove.setInt(1, idVarMdl);
			variableToRemove.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException ("error",e,"Error while deleting modbus variable - idvarmld: " + idVarMdl);
		}
	}

	//min MAX management
	
	/**
	 * Set the correct value for min MAX ref for each variable. On the fields min MAX is stored
	 * the variable codes. When all variable is inserted import operation can store the variable
	 * primary key 
	 *  
	 * @param idDevmdl
	 * @param minMaxrefs
	 * @param conn
	 * @throws ImportException
	 */
	private boolean setMinMaxRef(int idDevMdl,HashMap<Integer, MinMaxManagement>  minMaxrefs, Connection conn)throws ImportException
	{
		try
		{
			boolean minMaxExit = true;
			Set<Integer> keySet = minMaxrefs.keySet();
			for(int key:keySet){
				MinMaxManagement value = minMaxrefs.get(key);
				if(value.getCodeMax() != ""){
					minMaxExit = setMaxRef(key, value.getCodeMax(), idDevMdl, conn);
				}
				if(value.getCodeMin() != ""){
					minMaxExit = setMinRef(key, value.getCodeMin(), idDevMdl, conn);	
				}
			}
			return minMaxExit;
		}
		catch (ImportException ie)
		{
			throw ie;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while setting min-max variable references - iddevmdl: " + idDevMdl);
		}
	}

	private boolean setMinRef(int varKey, String minVarCode, int idDevMdl, Connection conn)throws ImportException
	{
		try
		{
			String sql = "SELECT idvarmdl FROM cfvarmdl WHERE idsite=? AND iddevmdl=? AND code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ID_SITE);
			ps.setInt(2, idDevMdl );
			ps.setString(3, minVarCode);
			ResultSet rs = ps.executeQuery();
			int minKey = 0;
			while(rs.next()) {
				minKey = rs.getInt(1);
			}
			if (minKey == 0)
				return false;
	
			sql = "UPDATE cfvarmdl SET minvalue = ? WHERE idvarmdl =?";
				
			PreparedStatement variableMin = conn.prepareStatement(sql);
			variableMin.setString(1, "pk" + minKey);
			variableMin.setInt(2, varKey );
			variableMin.executeUpdate();
	
			return true;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while setting MIN variable reference - iddevmdl: " + idDevMdl + ", code: " + minVarCode);
		}
	}

	private boolean setMaxRef(int varKey, String maxVarCode, int idDevMdl, Connection conn)throws ImportException
	{
		try
		{
			String sql = "SELECT idvarmdl FROM cfvarmdl WHERE idsite=? AND iddevmdl=? AND code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ID_SITE);
			ps.setInt(2, idDevMdl );
			ps.setString(3, maxVarCode);
			ResultSet rs = ps.executeQuery();
			int maxKey = 0;
			while(rs.next()) {
				maxKey = rs.getInt(1);
			}
			if(maxKey == 0)
				return false;
			sql = "UPDATE cfvarmdl SET maxvalue = ? WHERE idvarmdl =?";
				
			PreparedStatement variableMax = conn.prepareStatement(sql);
			variableMax.setString(1, "pk" + maxKey);
			variableMax.setInt(2, varKey );
			variableMax.executeUpdate();
	
			return true;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while setting MAX variable reference - iddevmdl: " + idDevMdl + ", code: " + maxVarCode);
		}
	}
	
	private void addToMinMaxRefList(HashMap<Integer, MinMaxManagement> minMaxRefs, int idVarMdl, String max, String min)
	{

		MinMaxManagement minMax = new MinMaxManagement();
		boolean modified = false;

		if (max.length() > 2 && max.substring(0, 2).toLowerCase().equals(MIN_MAX_PK_REF))
		{
			minMax.setCodeMax(max.substring(2));
			modified = true;
		}
		if(min.length() > 2 && min.substring(0, 2).toLowerCase().equals(MIN_MAX_PK_REF))
		{
			minMax.setCodeMin(min.substring(2));
			modified = true;
		}

		if(modified)
			minMaxRefs.put(idVarMdl, minMax);				
	}

	//Translations Utilities

	private void insertTranslations(int idSite, String langKey, String tableModel, int tableId, KeyTag descr, KeyTag shortD, KeyTag longD, Connection conn ) throws ImportException
	{
		insertTranslations(idSite, langKey, tableModel, tableId, descr.getValue(), shortD!=null?shortD.getValue():null, longD!=null?longD.getValue():null, conn );
	}
	private void insertTranslations(int idSite, String langKey, String tableModel, int tableId, String descr, String shortD, String longD, Connection conn ) throws ImportException{

		try
		{
			String sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
			PreparedStatement variableTranslation = conn.prepareStatement(sql);
			variableTranslation.setInt(1, this.device.getIdSite());
			variableTranslation.setString(2, langKey);
			variableTranslation.setString(3, tableModel);
			variableTranslation.setInt(4, tableId);
			variableTranslation.setString(5, descr);
			if(descr == "")
				throw new Exception("Device import: descr is empty");
			variableTranslation.setString(6, shortD);
			variableTranslation.setString(7, longD);
			variableTranslation.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			variableTranslation.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("usererror",e,"Error while inserting translations, language: " + langKey);
		}
	}
	private void updateTranslations(int idSite, String langKey, String tableModel, int tableId, KeyTag descr, KeyTag shortD, KeyTag longD, Connection conn ) throws ImportException
	{
		updateTranslations(idSite, langKey, tableModel, tableId, descr.getValue(), shortD!=null?shortD.getValue():null, longD!=null?longD.getValue():null, conn );
	}
	private void updateTranslations(int idSite, String langKey, String tableModel, int tableId, KeyTag descr, String shortD, String longD, Connection conn ) throws ImportException
	{
		updateTranslations(idSite, langKey, tableModel, tableId, descr.getValue(), shortD, longD, conn );
	}
	private void updateTranslations(int idSite, String langKey, String tableModel, int tableId, String descr, String shortD, String longD, Connection conn ) throws ImportException{

		try
		{
			String sql = "UPDATE cftableext SET description = ?, shortdescr = ?, longdescr = ?, lastupdate = ? WHERE idsite = ? AND tablename = ? AND tableid = ? AND languagecode = ?";
			PreparedStatement variableTranslation = conn.prepareStatement(sql);
			variableTranslation.setString(1, descr);
			variableTranslation.setString(2, shortD);
			variableTranslation.setString(3, longD);
			variableTranslation.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			variableTranslation.setInt(5, this.device.getIdSite());
			variableTranslation.setString(6, tableModel);
			variableTranslation.setInt(7, tableId);
			variableTranslation.setString(8, langKey);
	
			variableTranslation.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("usererror",e,"Error while updating translations, language: " + langKey);
		}
	}
	private void updateTranslations(int idSite, String langKey, String tableModel, int tableId,KeyTag longD, Connection conn ) throws ImportException{
		updateTranslations(idSite, langKey, tableModel, tableId,longD!=null?longD.getValue():null, conn );
	}
	private void updateTranslations(int idSite, String langKey, String tableModel, int tableId,String longD, Connection conn ) throws ImportException{

		try
		{
			String sql = "UPDATE cftableext SET longdescr = ?, lastupdate = ? WHERE idsite = ? AND tablename = ? AND tableid = ? AND languagecode = ?";
			PreparedStatement variableTranslation = conn.prepareStatement(sql);
			variableTranslation.setString(1, longD);
			variableTranslation.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			variableTranslation.setInt(3, this.device.getIdSite());
			variableTranslation.setString(4, tableModel);
			variableTranslation.setInt(5, tableId);
			variableTranslation.setString(6, langKey);
	
			variableTranslation.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("usererror",e,"Error while updating translations, language: " + langKey);
		}
	}

	private void removeTranslations(String tableName, int tableId, Connection conn) throws ImportException{

		try
		{
			String sql = "DELETE FROM cftableext WHERE tablename = ? AND tableid = ? AND idsite=?";
			PreparedStatement translationToRemove = conn.prepareStatement(sql);
			translationToRemove.setString(1, tableName);
			translationToRemove.setInt(2, tableId);
			translationToRemove.setInt(3, ID_SITE);
			translationToRemove.executeUpdate();
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e,"Error while removing translations - " + tableName + " " + tableId);
		}
	}

	//Common utilities

	private void fillInformations(DeviceTag deviceTag, HashMap<String, VariableModBusInformationDb> varsModbus) throws ImportException
	{
		try
		{
			boolean ide = false;
			DeviceModbusInfoDb modbusDevice = null;
			if(deviceTag.getIde() == 1)
				ide= true;
			if(deviceTag.isModbusModel())
			{
				modbusDevice = new DeviceModbusInfoDb();
				modbusDevice.fillInformations(deviceTag.getModbusInfo().getStopBit(), deviceTag.getModbusInfo().getParity(), 
						deviceTag.getModbusInfo().getReadTimeOut(), deviceTag.getModbusInfo().getWriteTimeOut(), 
						deviceTag.getModbusInfo().getActivityTimeOut(), deviceTag.getModbusInfo().getF1(), 
						deviceTag.getModbusInfo().getF2(), deviceTag.getModbusInfo().getF3(), 
						deviceTag.getModbusInfo().getF4(), deviceTag.getModbusInfo().getF5(), 
						deviceTag.getModbusInfo().getF6(), deviceTag.getModbusInfo().getF15(), 
						deviceTag.getModbusInfo().getF16());
			}
			
			device.fillInformations(ID_SITE, deviceTag.getCode(), deviceTag.getManufacturer() , HD_VERSION , deviceTag.getSwVersion(), deviceTag.getImage(), deviceTag.getLittleEndian(), ide, deviceTag.getDescriptionKey(), modbusDevice);
	
			Set<String> codeVariableSet = deviceTag.getPvInfo().getPvVars().getHmPVVars().keySet();
			for(String code:codeVariableSet){
				VariableDb var = new VariableDb();
				PVVarTag pvVar = deviceTag.getPvInfo().getPvVars().getHmPVVars().get(code);
				DeviceVarTag commonVar = deviceTag.getVarCommonInfo().getHmVars().get(code);
	
				int addressIn;
				int addressOut;
				int varDimension;
				int varLength;
				int bitPosition;
	
				if(deviceTag.isModbusModel())
				{
					VarModbusTag modbusInfo = deviceTag.getModbusInfo().getVarsModbus().getAlVarMODBUS().get(code);
					addressIn = modbusInfo.getAddressIn();
					addressOut = modbusInfo.getAddressOut();
					varDimension = modbusInfo.getVarDimension();
					varLength = modbusInfo.getVarLength();
					bitPosition = modbusInfo.getBitPosition();
	
					VariableModBusInformationDb varModbus = new VariableModBusInformationDb();
					varModbus.fillInformations(modbusInfo.getAValue(), modbusInfo.getBValue(), modbusInfo.getVarEncoding());
					varsModbus.put(code, varModbus);
				}
				else
				{
					VarCRLTag carelInfo = deviceTag.getCarelInfo().getAlVarCRL().get(code);
					addressIn = carelInfo.getAddressIn();
					addressOut = carelInfo.getAddressOut();
					varDimension = carelInfo.getVarDimension();
					varLength = carelInfo.getVarLength();
					bitPosition = carelInfo.getBitPosition();
				}
				if(pvVar.getHsFrequency()>0 && pvVar.getHsTime()<=0)
				{
					throw new ImportException("modelerr","code:"+pvVar.getCode()+"'s HsFrequency="+pvVar.getHsFrequency()+", but HsTime="+pvVar.getHsTime());
				}
				if(pvVar.getHsTime()>0 && pvVar.getHsFrequency()<=0)
				{
					throw new ImportException("modelerr","code:"+pvVar.getCode()+"'s HsTime="+pvVar.getHsTime()+", but HsFrequency="+pvVar.getHsFrequency());
				}
				DecimalFormat f = new DecimalFormat("#.#");
				String defaultvalue = UtilityString.substring(f.format(pvVar.getDefaultValue()),LineConfig.DEFAULT_MAX_MIN);
				var.fillInformations(ID_SITE, -1, code, commonVar.getType(), addressIn, addressOut,
						varDimension, varLength, bitPosition, commonVar.isSigned(), commonVar.getDecimal(),
						pvVar.getToDisplay(), pvVar.getPriority(),pvVar.getReadWrite(), commonVar.getMinValue(),
						commonVar.getMaxValue(), defaultvalue, commonVar.getUnitOfMeasure(), pvVar.getCategory(),
						pvVar.getImageOn(), pvVar.getImageOff(), pvVar.isHaccp(), pvVar.isRelay(), pvVar.getHsTime(), pvVar.getHsFrequency(),pvVar.getHsDelta(),pvVar.getCategory_code(), pvVar.isCommand());
				
				variables.put(var.getCode(), var);
	
			}
			
			//ENUMS MGMT
			ArrayList<EnumTag> enumList = deviceTag.getPvInfo().getEnums().getAlEnum();
			EnumDb enumDb = null;
			EnumTag enumTag = null;
			for(int i=0;i<enumList.size();i++)
			{
				enumTag = enumList.get(i);
				enumDb = new EnumDb(enumTag.getEnumCode(),enumTag.getAlEnumItem());
				enums.add(enumDb);
			}
			
			//COMBO MGMT
			ArrayList<ComboTag> comboList = deviceTag.getPvInfo().getCombos().getAlCombo();
			ComboDb comboDb = null;
			ComboTag comboTag = null;
			for(int i=0;i<comboList.size();i++)
			{
				comboTag = comboList.get(i);
				comboDb = new ComboDb(comboTag.getComboCode(),comboTag.getAlComboItem());
				combos.add(comboDb);
			}
			
			//CATEOGRIES MGMT
			ArrayList<CategoryTag> categoryList = deviceTag.getPvInfo().getCategories().getAlCategory();
			CategoryDb categoryDb = null;
			CategoryTag categoryTag = null;
			for(int i=0;i<categoryList.size();i++)
			{
				categoryTag = categoryList.get(i);
				categoryDb = new CategoryDb(categoryTag.getCategoryCode());
				categories.add(categoryDb);
			}
			
			// TRANSLATIONS
			translations.fillTranslations(deviceTag.getTranslations());
			
			fsinfo = deviceTag.getProperties().getFSConfig();
		}
		catch(ImportException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("xmlerr",e,"Inconsistent information in the xml file");
		}
	}

	private boolean isModbusDevice(String idDevMdl, Connection conn) throws ImportException
	{
		try
		{
			boolean result = false;
	
			String sql = "SELECT count(1) from cfmodbusmodels WHERE iddevmdl=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,idDevMdl);
			ResultSet rs = ps.executeQuery();
	
			int count=0 ;
			//If device exists delete it. Otherwise device was already deleted!
			while(rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0)
				result = true;
	
			return result;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e);
		}
	}
	
	
	private String getDeviceKey(String devCode, Connection conn) throws ImportException
	{
		
		try
		{
			String deviceKey = "";
	
			//RETRIEVE device primary key. Used in cfvarmdl and in cftableext
			String sql = "select iddevmdl from cfdevmdl where idsite=? and code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,ID_SITE);
			ps.setString(2,devCode);
			ResultSet rs = ps.executeQuery();
	
			//If device exists delete it. Otherwise device was already deleted!
			while(rs.next()) {
				deviceKey = rs.getString(1);
			}
			return deviceKey;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e);
		}
	}
	

	private ResultSet getVariables(String idDevMdl, Connection conn)throws ImportException
	{
		ResultSet rs = null;
		String sql;
		try
		{
			//RETRIEVE variables primary keys. Used in cftableext
			sql = "SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl = ? AND idsite = ? ";
			PreparedStatement variables = conn.prepareStatement(sql);
			variables.setInt(1, Integer.parseInt(idDevMdl));
			variables.setInt(2,ID_SITE);
			rs = variables.executeQuery();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ImportException("error",e);
		}
		return rs;
	}

	private Map<String, String> getVariablesCode_IdVar(String deviceKey, Connection conn)throws ImportException
	{
		try
		{
			ResultSet rs = null;
			String sql;
			try
			{
				//RETRIEVE variables primary keys. Used in cftableext
				sql = "SELECT code, idvarmdl FROM cfvarmdl WHERE iddevmdl = ? AND idsite = ?";
				PreparedStatement variables = conn.prepareStatement(sql);
				variables.setString(1, deviceKey);
				variables.setInt(2, ID_SITE);
				rs = variables.executeQuery();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return new HashMap<String, String>();
			}
			Map<String, String> map = new HashMap<String, String>();
			List<String> result = new ArrayList<String>();
			while(rs.next()){
				result.add(rs.getString(VAR_CODE));
				map.put(rs.getString(VAR_CODE), rs.getString(ID_VAR_MDL));
			}
			return map;	
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e);
		}
	}

	private List<String> instancedDevice(String deviceKey, Connection conn) throws ImportException
	{
		List<String> devices = new ArrayList<String>();
		try{
			String sql = "SELECT * FROM cfdevice WHERE  idsite=? AND iddevmdl = ? AND iscancelled=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ID_SITE );
			ps.setInt(2, Integer.parseInt(deviceKey));
			ps.setString(3, "FALSE");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				devices.add(rs.getString(ID_DEVICE));	
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			throw new ImportException("error",e);

		}
		return devices;
	}

	/*private List<String> getLanguages(String tableName, int tableId, Connection conn)
	throws ImportException {
		try
		{
			String sql;
	
			sql = "SELECT languagecode FROM cftableext WHERE tablename = ? AND tableid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, tableName);
			ps.setInt(2, tableId);
			ResultSet rs = ps.executeQuery();
			List<String> usedLanguages = new ArrayList<String>();
			while (rs.next())
				usedLanguages.add(rs.getString(1));
			return usedLanguages;
		}
		catch (Exception e)
		{
			throw new ImportException("error",e);
		}
	}*/

	private boolean checkModelCorrectness(boolean isModbusModel) throws ImportException
	{
		if(this.variables.size() != 0){
			//controllo variabile "OFFLINE" esistente, o equivalente variabile con indirizzo 0
			if(this.variables.containsKey(OFFLINE_VAR) || this.ExistOfflineVariable())
			{
				//check overlapping for AddressIn addresses set
				String overlappedCodes = ExistsOverlappedVariables(isModbusModel, true);				
				
				if(!overlappedCodes.equals(""))
					throw new ImportException("modelerr","Device model contains overlapped variables: <br>" + overlappedCodes  + "<br> (Address Read)");
				
				//check overlapping for AddressOut addresses set
				overlappedCodes = ExistsOverlappedVariables(isModbusModel, false);
				
				if(!overlappedCodes.equals(""))
					throw new ImportException("modelerr","Device model contains overlapped variables: <br>" + overlappedCodes + "<br> (Address Write)");
								
				// no overlapping
				return true;
			}
			else
			{
				throw new ImportException("modelerr","Offline variable doesn't exists");
			}
					
		}
		else
		{
			throw new ImportException("modelerr","No variables in the device model");
		}	
	}

	private String ExistsOverlappedVariables(boolean isModbusModel, boolean addressIn) 
	{
		Iterator<String> iter = (Iterator<String>) this.variables.keySet().iterator();
		
		Hashtable<String,Hashtable<Integer,VariableDb>> addrHash = new Hashtable<String,Hashtable<Integer,VariableDb>>();
		
		while(iter.hasNext())
		{
			VariableDb var = (VariableDb)(this.variables.get(iter.next()));
			
			String varAddr= "";

			// ignore 'addressOut' check in case of readonly variable
			if(addressIn || var.getReadWrite()!=1)
			{
				if(isModbusModel)
					// 	MODBUS device: hashtable key is the string composed by ADDR 
					varAddr= Integer.toString(addressIn?var.getAddressIn():var.getAddressOut());
				else
				{
					int type = var.getType();	
					if(var.getVarLength() != var.getVarDimension() && var.getType()!=VariableInfo.TYPE_ANALOGIC) // check if it is a 'compressed' variable (and it is not 'ANALOG')
						type = VariableInfo.TYPE_INTEGER; // 'compressed' INT, ALARM and DIGITAL variables are stored in the 'INT' address space
					
					// CAREL device: hashtable key is the string composed by ADDR + '_' + TYPE
					varAddr= Integer.toString(addressIn?var.getAddressIn():var.getAddressOut())+"_"+Integer.toString(type);
				}
								
				if(addrHash.containsKey(varAddr)) 
				{
					//hashtable key is 'bitposition' value
					Hashtable<Integer,VariableDb> bitposHash = addrHash.get(varAddr);
					
					if(bitposHash.containsKey(var.getBitPosition())) //overlapping if some variables have the same bitposition
						return bitposHash.get(var.getBitPosition()).getCode() + " - " + var.getCode();
					
					Iterator<Integer> bitposIter = (Iterator<Integer>) bitposHash.keySet().iterator();
					while(bitposIter.hasNext()) 
					{
						int bitposH = bitposIter.next();
						int varlengthH= ((VariableDb)bitposHash.get(bitposH)).getVarLength();
						
						if((var.getBitPosition() > bitposH && var.getBitPosition() <= (bitposH + varlengthH-1)) || (bitposH > var.getBitPosition() && bitposH <= (var.getBitPosition() + var.getVarLength()-1)))
							return ((VariableDb)bitposHash.get(bitposH)).getCode() + " - " + var.getCode();
					}
					
					//put var into the hastable if there is not overlapping
					bitposHash.put(var.getBitPosition(), var);
				}
				else
				{
					Hashtable <Integer, VariableDb> posLenHash = new Hashtable<Integer, VariableDb>();
					posLenHash.put(var.getBitPosition(), var);
					addrHash.put(varAddr, posLenHash);
				}
			}
		}		
		return "";
	}

	private boolean ExistOfflineVariable()
	{
		Iterator<String> iter = (Iterator<String>) this.variables.keySet().iterator();
		
		while(iter.hasNext())
		{
			VariableDb var = (VariableDb)(this.variables.get(iter.next()));
			if(var.getAddressIn() == 0)
				return true;
		}
		return false;
	}
	
	private boolean checkModelTranslations(List<String> pvproLangs, Set<String> langSet)
	{
		for(int i=0; i<pvproLangs.size(); i++)
		{
			if(langSet.contains(pvproLangs.get(i)))
				return true;	
		}
		return false;
	}
	
	private void insertEnum(Integer idDevMdl, Connection conn, String defaultLan,Set<String> langSet,LangUsedBean[] langused) throws ImportException, DataBaseException, SQLException
	{
		// INSERT ENUM 
		EnumDb en = null;
		String var_code = "";
		Integer idenum = null;
		String code = "";
		Integer val = null;
		String sql = "";
		
		
		sql = "insert into cfenum values (?,"+idDevMdl+"," +
		"(select idvarmdl from cfvarmdl where iddevmdl=? and code=?),?)";
		for (int i=0; i<enums.size();i++)
		{
			en = enums.get(i);
			var_code = en.getCode();
			
			
			
			
			for (int j=0;j<en.getItemValues().size();j++)
			{
				idenum = SeqMgr.getInstance().next(null, "cfenum", "idenum"); 
				val = en.getItemValues().get(j);
				
				PreparedStatement stat_enum = conn.prepareStatement(sql);
				stat_enum.setInt(1, idenum);
				stat_enum.setInt(2, idDevMdl);
				stat_enum.setString(3, var_code);
				stat_enum.setInt(4, val);
				stat_enum.execute();
				
				code=var_code+"~enum~"+val.toString();
				
				//insert variable translations
				if(langSet.contains(defaultLan))
				{
					try {
						//INSERT default language translations (using default language strings from xml)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfenum", idenum, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for enum with code:"+code);
						}
				}
				else
				{
					try {
						//INSERT default language translations (using EN_en language, which exists.. just checked)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfenum", idenum, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for enum with code:"+code);
						}
				}
				if(langused.length > 1)
				{
					if(langSet.contains("EN_en"))
					{
						try {
							//INSERT EN_en language translations (using EN_en language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfenum", idenum, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for enum with code:"+code);
							}
					}
					else
					{
						try {
							//INSERT EN_en language translations (using default language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfenum", idenum, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for enum with code:"+code);
							}
					}
				}
			}
		}
	}
	
	private void insertCombo(int idDevMdl,String devMdlCode, Connection conn, String defaultLan,Set<String> langSet,LangUsedBean[] langused) throws ImportException, DataBaseException, SQLException
	{
		// INSERT Combo 
		ComboDb cm = null;
		String var_code = "";
		Integer idcombo = null;
		Integer idoption = null;
		String code = "";
		Integer val = null;
			
		
		String sql_combo = "insert into cfcombo(idcombo,combocode,iddevmdl) values (?,?,?)";
		PreparedStatement stat_combo = null;
		String sql_option = "insert into cfoption values (?,?,?)";
		PreparedStatement stat_option = null;
		String sql_comboset = "insert into cfcomboset values (?,?,?,?)";
		PreparedStatement stat_comboset = null;
		
		for (int i=0; i<combos.size();i++)
		{
			cm = combos.get(i);
			var_code = cm.getCode();
			idcombo = SeqMgr.getInstance().next(null, "cfcombo", "idcombo"); 
			
			stat_combo = conn.prepareStatement(sql_combo);
			stat_combo.setInt(1, idcombo);
			stat_combo.setString(2, devMdlCode+"_"+var_code);
			stat_combo.setInt(3, idDevMdl);
			stat_combo.execute();
			
			for (int j=0;j<cm.getItemValues().size();j++)
			{
				stat_option = conn.prepareStatement(sql_option);
				idoption = SeqMgr.getInstance().next(null, "cfoption", "idoption");
				val = cm.getItemValues().get(j);
				stat_option.setInt(1, idoption);
				stat_option.setString(2, devMdlCode+"_"+var_code+"_"+(j+1));
				stat_option.setDate(3, new java.sql.Date(System.currentTimeMillis()));
				stat_option.execute();
				
				stat_comboset = conn.prepareStatement(sql_comboset);
				val = cm.getItemValues().get(j);
				stat_comboset.setInt(1, idcombo);
				stat_comboset.setInt(2, idoption);
				stat_comboset.setInt(3, cm.getIdvarmdl());
				stat_comboset.setFloat(4, val);
				stat_comboset.execute();
				
				code=var_code+"~combo~"+val.toString();
				
				//insert variable translations
				if(langSet.contains(defaultLan))
				{
					try {
						//INSERT default language translations (using default language strings from xml)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfoption", idoption, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for combo with code:"+code);
						}
				}
				else
				{
					try {
						//INSERT default language translations (using EN_en language, which exists.. just checked)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfoption", idoption, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for combo with code:"+code);
						}
				}
				if(langused.length > 1)
				{
					if(langSet.contains("EN_en"))
					{
						try {
							//INSERT EN_en language translations (using EN_en language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfoption", idoption, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for combo with code:"+code);
							}
					}
					else
					{
						try {
							//INSERT EN_en language translations (using default language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfoption", idoption, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(DESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for combo with code:"+code);
							}
					}
				}
			}
		}
	}
	
	private void insertCategory(Connection conn, String defaultLan,Set<String> langSet,LangUsedBean[] langused) throws ImportException, DataBaseException, SQLException
	{
		// INSERT Category
		//DB Cfvarmdlgrp
		//idvargroup :  int
		//idsite:		int
		//code:			varchar(128)
		//isalarm:		varchar(5)
	    //lastupdate:	timestamp
		
		
		CategoryDb categ = null;
		Integer idcategory = null;
		String code = "";
		String descr_code = "";
		
		String sql_category = "insert into cfvarmdlgrp values (?,?,?,?,?)";
		PreparedStatement stat_category = null;
		
		idcategory = getIdNewCategory(conn);
		// check: custom categories start from 1000; if is the first custom insert -> id=1000
		if (idcategory<1000)
		{
			idcategory = 1000;
		}
		
		for (int i=0; i<categories.size();i++)
		{
			categ = categories.get(i);
			code = categ.getCode();
		
			
			stat_category = conn.prepareStatement(sql_category);
			stat_category.setInt(1, idcategory);
			stat_category.setInt(2, 1);
			//stat_category.setString(3, code);
			stat_category.setString(3, code+"~"+idcategory);
			stat_category.setString(4, "FALSE");
			stat_category.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
			stat_category.execute();
			
			descr_code= "category~"+code;
				
				//insert translations
				if(langSet.contains(defaultLan))
				{
					try {
						//INSERT default language translations (using default language strings from xml)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfvarmdlgrp", idcategory, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(DESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for category with code:"+descr_code);
						}
				}
				else
				{
					try {
						//INSERT default language translations (using EN_en language, which exists.. just checked)
						this.insertTranslations(this.device.getIdSite(), defaultLan, "cfvarmdlgrp", idcategory, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(DESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(SHORTDESCR),
								translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(LONGDESCR),
								conn);
						}
						catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
							e.printStackTrace();
							throw new ImportException("modelerr","Missing description for category with code:"+descr_code);
						}
				}
				if(langused.length > 1)
				{
					if(langSet.contains("EN_en"))
					{
						try {
							//INSERT EN_en language translations (using EN_en language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfvarmdlgrp", idcategory, translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(DESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(EN).getSections().get("PV").getItems().get(descr_code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for category with code:"+descr_code);
							}
					}
					else
					{
						try {
							//INSERT EN_en language translations (using default language strings from xml)
							this.insertTranslations(this.device.getIdSite(), "EN_en", "cfvarmdlgrp", idcategory, translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(DESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(SHORTDESCR),
									translations.getLanguages().get(defaultLan).getSections().get("PV").getItems().get(descr_code).getKeys().get(LONGDESCR),
									conn);
							}
							catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
								e.printStackTrace();
								throw new ImportException("modelerr","Missing description for category with code:"+descr_code);
							}
					}
				}
				idcategory++;
			}
		}
	
	public Map<String,Integer> getIdCustomCategoryByCode(Connection conn) throws SQLException
	{
		Map<String,Integer> m = new HashMap<String,Integer>();
		String sql = "select distinct code,idvargroup from cfvarmdlgrp order by idvargroup";
		
		Statement select = conn.createStatement();
	    ResultSet result = select.executeQuery(sql);
	
		while (result.next())
		{
			int id = result.getInt(2);
	        String code = "";
	        if (result.getString(1).split("c_").length>1)
	        	code = result.getString(1).split("~")[0];
	        else
	        	code = result.getString(1);
     		m.put(code,id);
		}
		return m;
	}
	
	private int getIdNewCategory(Connection conn) throws SQLException
	{
		int id = -1;
		String sql = "select max(idvargroup) from cfvarmdlgrp";
		
		Statement select = conn.createStatement();
	    ResultSet result = select.executeQuery(sql);
	
		while (result.next())
		{
			id = result.getInt(1);
	       
		}
		id++;
		return id;
	}
	
	private void insertFSInfo(Connection conn) throws ImportException, DataBaseException, SQLException
	{
		//DELETE
		String sql = "delete from fsdevmdl where devcode = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1,device.getCode());
		ps.execute();
		
		//INSERT
		sql = "insert into fsdevmdl values (?,?,?,?,?,?)";
		FSTagObj tmp;
		for (int i=0;i<fsinfo.size();i++)
		{
			tmp = fsinfo.get(i);
			ps = conn.prepareStatement(sql);
			ps.setString(1, device.getCode());
			ps.setString(2, tmp.isIsrack() );
			ps.setString(3, tmp.getVar1());
			ps.setString(4, tmp.getVar2());
			ps.setString(5, tmp.getVar3());
			ps.setString(6, tmp.getVar4());
			ps.execute();
		}
	}
		

}