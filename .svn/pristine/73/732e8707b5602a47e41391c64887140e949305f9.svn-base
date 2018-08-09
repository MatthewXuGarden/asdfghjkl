package com.carel.supervisor.ide.dc.DbModel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;
import com.carel.supervisor.ide.dc.DbModel.Translation.Translations;
import com.carel.supervisor.ide.dc.xmlDAO.ImagesTag;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.presentation.bo.BDevDetail;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;
import com.carel.supervisor.presentation.bo.helper.VarDependencyCheck;
import com.carel.supervisor.presentation.bo.helper.VarDependencyState;

public class Instance {

	private static final Object DESCR = "descr";
	private static final Object SHORTDESCR = "short";
	private static final Object LONGDESCR = "long";
	private static final Object EN = "EN_en";
	private static final String MIN_MAX_PK_REF = "pk";
	private static final int ID_SITE = 1;
	private static final String IMG_PATH = BaseConfig.getAppHome() + "images" + File.separator + "devices";
	
	private Map<String, Record> varsOnDb;
	private VarDependencyState dependenceState;
	private HashMap<String, Integer> newIdVarMdl;
	private HashMap<String, VariableDb> variables;
	private String devcode;
	private boolean littleEndian;
	private String image;
	private Set<String> importmdlVarsCodes;
	private Translations translations;
	private ImagesTag images;

	
	public Instance(String devcode, Set<String> importmdlVarsCodes, HashMap<String, VariableDb> variables, Translations translations, HashMap<String, Integer> newIdVarMdl, ImagesTag images, boolean littleEndian, String image)
	{
		dependenceState = new VarDependencyState();
		this.devcode = devcode;
		this.importmdlVarsCodes = importmdlVarsCodes;
		this.variables = variables; // HashMap containing the variables info, from xml model
		this.newIdVarMdl = newIdVarMdl;
		this.translations = translations;
		this.images = images;
		this.image = image;
		this.littleEndian = littleEndian;
	}
	
	public void updateInstances(Connection conn, String langcode, int profile,String devcode) throws Exception
	{
		try
		{   
	        // retrieve iddevmdl from code 
	        String sql = "select iddevmdl from cfdevmdl where code=?";

	        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { devcode });
	        int idDevMdl = (Integer) rs.get(0).get("iddevmdl");
	        
	        //update of all instantiated devices ('littleendian' and 'imagepath' info)
	        updateDevices(idDevMdl, conn);
	        
	        
	        //map containing all varmdl of the existing model
	        varsOnDb = getDbVarmdl(idDevMdl); 
	        
	        List<String> varsToAdd = new ArrayList<String>();
			List<String> varsToUpdate = new ArrayList<String>();
			List<String> varsToRemove = new ArrayList<String>();
	        
			//find variables which have to be added or updated
			for(String varCode: importmdlVarsCodes){
				if(varsOnDb.containsKey(varCode)){
					if((Integer) varsOnDb.get(varCode).get("type") != variables.get(varCode).getType() ){
						varsToRemove.add(varCode);
						varsToAdd.add(varCode);
						newIdVarMdl.put((String)(varsOnDb.get(varCode).get("code")),(Integer)varsOnDb.get(varCode).get("idvarmdl") );
					}else{
						varsToUpdate.add(varCode);
					}
				}
				else
					varsToAdd.add(varCode);				
			}

			//find variables which have to be removed
			for(String modelVarCode:varsOnDb.keySet()){
				if(!varsToUpdate.contains(modelVarCode))
					varsToRemove.add(modelVarCode);
			}
			
			//check dependencies before Deletion
			checkDeleteDependencies(varsToRemove, idDevMdl, langcode, 1);
			
			//check dependencies before Update
			checkUpdateDependencies(varsToUpdate, idDevMdl, langcode, 2);
			
			//only if all checks are passed, then update variable instances
			if(!dependenceState.dependsOn())
			{
				HashMap<Integer, MinMaxManagement>  minMaxrefs = new HashMap<Integer, MinMaxManagement>();
				
				// add variables
				addVariables(varsToAdd, idDevMdl, profile, minMaxrefs, conn , devcode);
				
				// update variables
				updateVariables(varsToUpdate, idDevMdl, minMaxrefs, conn);
				
				// remove variables
				removeVariables(varsToRemove, profile, conn);
				
				// adjust MIN and MAX references on variable instances
				setMinMaxRefInstance(minMaxrefs, conn);
			
			}
			else
			{
				throw new ImportException("varimporterr",dependenceState.getMessagesAsHTMLText());
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	private void checkDeleteDependencies(List<String> vars_codes, int iddevmdl, String langcode, int msgSectNumber) throws Exception
	{
		if(vars_codes.size() > 0)
		{
			LangService lang_s = LangMgr.getInstance().getLangService(langcode);
			String sectMsgTitle = lang_s.getString("importctrl", "cancelerror");
			dependenceState.addMsgSection(1, sectMsgTitle);
			
			//get all ids of variables which have to be checked for dependencies
			int[] ids_variables = getVariablesIds(vars_codes);

	        if(ids_variables != null)    
	        {
	        	dependenceState = VarDependencyCheck.checkVariablesInAction(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInAlarmCondition(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInEventCondition(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInLogicVar(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInLogicDev(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInInstantReport(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInHistorReport(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInBooklet(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInGuardian(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInRemote(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInParametersControl(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInEnergy(1, ids_variables, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInLightsOnOff(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInKPI(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
	        	dependenceState = VarDependencyCheck.checkVariablesInDewPoint(1, ids_variables, iddevmdl, langcode, dependenceState, 1);
	        	
	        	//check dependencies of variables inside FloatingSuction configuration
	            // TODO: implementare il metodo (dopo modifica del plugin per supportare modelli custom?)
	        }
		}		        
	}
	
	private void checkUpdateDependencies(List<String> vars_codes, int iddevmdl, String langcode, int msgSectNumber) throws Exception
	{
		if(vars_codes.size() > 0)
		{	
			LangService lang_s = LangMgr.getInstance().getLangService(langcode);
			
			List<String> varsToCheckAllCond = new ArrayList<String>();
			//List<Integer> varsToCheckHistorCond = new ArrayList<Integer>();
			
			int[] checkallids = null;
			//int[] checkhistorids = null;
			
			// get map with varmdl main fields FROM DATABASE 
			//(which determine variable 'type' and 'readwrite access')
			Map<String,List<Integer>> map = getVarmdlMainFields(iddevmdl);
			
			// check if there are changes on fields "TYPE" and "READWRITE"
			// this check is at MODEL level
			for(int i = 0; i<vars_codes.size(); i++)
			{	
				VariableDb varImportMdl = variables.get(vars_codes.get(i));
				
				List<Integer> tmplist = map.get(vars_codes.get(i));
				int varmdltype  = tmplist.get(0);
				int varmdlrw = tmplist.get(1);
				
				if(varImportMdl!= null)
				{
					// if 'TYPE' or 'READWRITE' are changed then check all condition 
					// (the same conditions used before cancelling variable from an existing model)
					if(varmdltype != varImportMdl.getType() || varmdlrw != varImportMdl.getReadWrite())
					{
						varsToCheckAllCond.add(vars_codes.get(i));
					}
				}
			}	
			
			if(varsToCheckAllCond.size() > 0)
			{
				// add msg 'title' on section 2
				String sectMsgTitle = lang_s.getString("importctrl", "updtypeerror");
				dependenceState.addMsgSection(2, sectMsgTitle);
				
				checkallids = getVariablesIds(varsToCheckAllCond);
				if (checkallids != null)
				{
					dependenceState = VarDependencyCheck.checkVariablesInAction(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInAlarmCondition(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInEventCondition(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInLogicVar(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInLogicDev(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInInstantReport(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInHistorReport(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInBooklet(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInGuardian(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInRemote(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInParametersControl(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInEnergy(1, checkallids, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInLightsOnOff(1, checkallids, iddevmdl, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInKPI(1, checkallids, iddevmdl, langcode, dependenceState, 2);
		        	dependenceState = VarDependencyCheck.checkVariablesInDewPoint(1, checkallids, iddevmdl, langcode, dependenceState, 2);
		            //check dependencies of variables inside FloatingSuction configuration
		            // TODO: implementare il metodo (dopo modifica del plugin per supportare modelli custom?)
				}
			}
		}
	}
	

	private void updateDevices(int iddevmdl, Connection conn) throws Exception
	{
		try
		{
			String deviceImage = "";
			if(images.getImages().containsKey(image))
				deviceImage = images.getImages().get(image).getFileName();
		
			if(images.getImages().containsKey(image))
				Utils.decodeImage(images.getImages().get(image).getBytes(), images.getImages().get(image).getFileName(), IMG_PATH );
		
			//update 'littleendian' e 'imagepath' info
			String sql = "update cfdevice set littlendian=?, imagepath=? where iddevmdl=? and iscancelled=?";
			
			PreparedStatement updateHistStatement = conn.prepareStatement(sql);
			updateHistStatement.setString(1, Utils.booleanToPvString(this.littleEndian));
			updateHistStatement.setString(2, deviceImage);
			updateHistStatement.setInt(3, iddevmdl);
			updateHistStatement.setString(4, "FALSE");
			updateHistStatement.executeUpdate();
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	
	private void addVariables(List<String> varsToAdd, int idDevMdl, int profile, HashMap<Integer, MinMaxManagement> minMaxRefs, Connection conn,String devcode) throws Exception
	{
		if(varsToAdd.size() == 0)
			return;
		
		//RETRIEVE default language
		String defaultLan = LangUsedBeanList.getDefaultLanguage(1);
		
		//retrieve PVPRO languages
		LangUsedBeanList lan = new LangUsedBeanList();
		LangUsedBean[] langused = lan.retrieveAllLanguage(1);
		
		//RETRIEVE langset used inside xml model
		Set<String> langSet = translations.getLanguages().keySet();
		
		String sql = "select iddevice from cfdevice where iddevmdl=? and iscancelled=? and idsite=1";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevMdl,"FALSE" });
        
        // array with ids of all instantiated devices related to idDevMdl
        Integer[] ids_device = null;

        if (rs != null)
        {
            ids_device = new Integer[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids_device[i] = (Integer) rs.get(i).get("iddevice");
            }
        }        
        
        //cycle on variables to be added on existing device instances
        for (int i=0; i<varsToAdd.size(); i++)
		{
        	VariableDb varImportMdl = variables.get(varsToAdd.get(i));
        	
        	String insert = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement variableStatement = conn.prepareStatement(insert);
        	
        	//cycle on existing device instances
        	for(int j=0; j<ids_device.length; j++)
	        {
                int idvarmdl = newIdVarMdl.get(varsToAdd.get(i));
				
                int idvariable = SeqMgr.getInstance().next(null, "cfvariable", "idvariable");
                
                String imageOn = "";
                String imageOff = "";
                String imageButton = "";
                
                if(images.getImages().containsKey(varImportMdl.getImageOn()))
                	imageOn = "images/button/" + devcode +"/"+ images.getImages().get(varImportMdl.getImageOn()).getFileName();
				if(images.getImages().containsKey(varImportMdl.getImageOff()))
					imageOff =  "images/button/" + devcode+"/"+ images.getImages().get(varImportMdl.getImageOff()).getFileName();
		
				if(imageOn!=null && imageOff!=null && !imageOn.equals("") && !imageOff.equals(""))
				{
					imageButton="images/button/setvar.png";
				}
                
                variableStatement.setInt(1, idvariable); //idvar
                variableStatement.setString(2, BaseConfig.getPlantId()); //pvcode
                variableStatement.setInt(3, new Integer(1)); //idsite
                variableStatement.setInt(4, ids_device[j]); //iddevice
                variableStatement.setString(5, "FALSE"); //islogic
                variableStatement.setInt(6, idvarmdl); //idvarmdl
                variableStatement.setNull(7, java.sql.Types.INTEGER); //functioncode
                variableStatement.setString(8, varImportMdl.getCode()); //code
                variableStatement.setInt(9, varImportMdl.getType()); //type
                variableStatement.setInt(10, varImportMdl.getAddressIn()); //addressin
                variableStatement.setInt(11, varImportMdl.getAddressOut()); //addressout
                variableStatement.setInt(12, varImportMdl.getVarDimension()); //vardimension
                variableStatement.setInt(13, varImportMdl.getVarLength()); //varlength
                variableStatement.setInt(14, varImportMdl.getBitPosition()); //bitposition
                variableStatement.setString(15, Utils.booleanToPvString(varImportMdl.isSigned())); //signed
                variableStatement.setInt(16, varImportMdl.getDecimal()); //decimal
                variableStatement.setString(17, varImportMdl.getToDisplay().toUpperCase()); //todisplay
                
                if( imageButton != "")
                	variableStatement.setString(18, imageButton); //buttonpath
                else
                	variableStatement.setNull(18, java.sql.Types.VARCHAR);
                
                variableStatement.setInt(19, varImportMdl.getPriority()); //priority
                variableStatement.setInt(20, varImportMdl.getReadWrite()); //rw
                
                if(varImportMdl.getMinValue() != "" && !(varImportMdl.getMinValue().contains("pk")))
                	variableStatement.setString(21, varImportMdl.getMinValue()); //minvalue
                else
                	variableStatement.setNull(21, java.sql.Types.VARCHAR); //minvalue
                
                if(varImportMdl.getMaxValue() != "" && !(varImportMdl.getMaxValue().contains("pk")))
                	variableStatement.setString(22, varImportMdl.getMaxValue()); //maxvalue
                else
                	variableStatement.setNull(22, java.sql.Types.VARCHAR); //maxvalue
                
                variableStatement.setNull(23, java.sql.Types.VARCHAR); // defaultvalue
                variableStatement.setString(24, varImportMdl.getMeasureUnit()); //measureunit
                //variableStatement.setInt(25, varImportMdl.getIdVarGroup()); // idvargroup
	            if (varImportMdl.getIdVarGroup()!=-1){
	            	variableStatement.setInt(25, varImportMdl.getIdVarGroup());
	            }else{
	            	Model mdl = new Model();
	                Map<String,Integer> categoryIdByCode = mdl.getIdCustomCategoryByCode(conn);
	            	variableStatement.setInt(25, categoryIdByCode.get(varImportMdl.getCategoryCustomCode()));
	            }
                variableStatement.setString(26, imageOn); //imageon
                variableStatement.setString(27, imageOff); //imageoff

                if(varImportMdl.getFrequency() != 0)
                	variableStatement.setInt(28, varImportMdl.getFrequency()); // frequency is reported into buffer table (keymax value)
                else
                	variableStatement.setNull(28, java.sql.Types.INTEGER);
                
                if(varImportMdl.getDelta() != 0)
                	variableStatement.setInt(29, varImportMdl.getDelta()); //delta
                else
                	variableStatement.setNull(29, java.sql.Types.INTEGER);
                
                if(varImportMdl.getDelay() != 0)
                	variableStatement.setInt(30, varImportMdl.getDelay()); //delay
                else
                	variableStatement.setNull(30, java.sql.Types.INTEGER);
                
                variableStatement.setString(31, "FALSE"); //isonchange
                variableStatement.setString(32, Utils.booleanToPvString(varImportMdl.isHaccp())); //ishaccp
                
	            // insert record into 'buffer' table, to manage HACCP historical data
	            if(varImportMdl.isHaccp())
	            {	 
                    sql = "insert into buffer values (?,?,?,?,?)";
                    
                    PreparedStatement bufferStatement = conn.prepareStatement(sql);
                    
                    bufferStatement.setInt(1, new Integer(1));
                    bufferStatement.setInt(2, idvariable);
                    bufferStatement.setInt(3, BDevDetail.HACCP_ROWSHYSTORICAL);
                    bufferStatement.setInt(4, new Integer(-1));
                    bufferStatement.setBoolean(5, new Boolean(false));
                    
                    bufferStatement.executeUpdate();
                    
                    GraphVariable.insertVariableGraphInfo(1, idvariable, ids_device[j].intValue(), "TRUE", profile);
	            }
	            //insert record into 'buffer' table, also with ALARM TYPE variable
	            else if (varImportMdl.getType() == VariableInfo.TYPE_ALARM)
                {
                    sql = "insert into buffer values (?,?,?,?,?)";
                    
                    PreparedStatement bufferStatement = conn.prepareStatement(sql);
                    
                    bufferStatement.setInt(1, new Integer(1));
                    bufferStatement.setInt(2, idvariable);
                    bufferStatement.setInt(3, BDevDetail.ALARM_ROWSHYSTORICAL);
                    bufferStatement.setInt(4, new Integer(-1));
                    bufferStatement.setBoolean(5, new Boolean(false));
                    
                    bufferStatement.executeUpdate();
                }

	            variableStatement.setString(33, Utils.booleanToPvString(varImportMdl.isActive())); //isactive
	            variableStatement.setString(34, "FALSE"); //iscancelled
	            variableStatement.setInt(35, varImportMdl.getGrpCategory());
	            
	            int idhsvariable = -1;
	            
	            if(varImportMdl.getHsFrequency() != 0)
	            	idhsvariable = SeqMgr.getInstance().next(null, "cfvariable", "idvariable");
	            else
	            	idhsvariable = new Integer(-1);
	            
	            variableStatement.setInt(36, idhsvariable); //idhsvariable 
	            variableStatement.setTimestamp(37,  new Timestamp(System.currentTimeMillis()));
	            variableStatement.setTimestamp(38,  new Timestamp(System.currentTimeMillis()));

	            variableStatement.executeUpdate();
	            
                //insert description into cftableext
                insertVarDescr(idvariable, translations, langSet, langused, defaultLan, varImportMdl.getCode(), conn);
                
                //if variable has historical frequency != 0, then insert child var and its description
                if(varImportMdl.getHsFrequency() != 0)
                {
                	Integer prof = new Integer(varImportMdl.getHsTime());
	                Integer freq = new Integer(varImportMdl.getHsFrequency());
	                Float delta = new Float(varImportMdl.getHsDelta());
	
	                variableStatement.setInt(1, idhsvariable); //idvariabile storico
	                variableStatement.setInt(28, freq); //frequency 
	                variableStatement.setFloat(29, delta); //delta  
	                variableStatement.setString(32, "FALSE"); //ishaccp
	                variableStatement.setNull(36, java.sql.Types.INTEGER); //idhsvariable - child var

	                variableStatement.executeUpdate();
	                
	                //insert description into cftableext
	                insertVarDescr(idhsvariable, translations, langSet, langused, defaultLan, varImportMdl.getCode(), conn);
	                
                    // insert into buffer - for child variable
                    sql = "insert into buffer values (?,?,?,?,?)";

                    PreparedStatement bufferTranslation = conn.prepareStatement(sql);
                    
                    bufferTranslation.setInt(1, new Integer(1));
                    bufferTranslation.setInt(2, idhsvariable);
                    
                    Short type = new Integer (varImportMdl.getType()).shortValue();
                    	
                    bufferTranslation.setShort(3, new Short(ReorderInformation.calculateNewKeyMax(type, prof, freq)));
                    bufferTranslation.setInt(4, new Integer(-1));
                    bufferTranslation.setBoolean(5, new Boolean(false));
                    
                    bufferTranslation.executeUpdate();

                    // insert variabile for log graph
                    GraphVariable.insertVariableGraphInfo(1,idhsvariable,ids_device[j].intValue(), "FALSE", profile);
                }
	        
                addToMinMaxRefList(minMaxRefs, idvariable, varImportMdl.getMaxValue(), varImportMdl.getMinValue());
	        }
		}  
	}
	
	private void updateVariables(List<String> varsToUpdate, int idDevMdl, HashMap<Integer, MinMaxManagement> minMaxRefs, Connection conn) throws Exception
	{
		if(varsToUpdate.size() == 0)
			return;
		
		String select = "select iddevice from cfdevice where iddevmdl=? and iscancelled=? and idsite=1";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, select, new Object[] { idDevMdl,"FALSE" });
        
        // array with ids of all instantiated devices related to idDevMdl
        Integer[] ids_device = null;

        if (rs != null)
        {
            ids_device = new Integer[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids_device[i] = (Integer) rs.get(i).get("iddevice");
            }
        }
		
        //get map of all hsvarids ('key' is varid and 'val' is hsvarid)
        HashMap<Integer,Integer> hsvarsids =  getInstanceWithHistor(varsToUpdate);
        
        
        
        String sql =  "update cfvariable set type=?,addressin=?,addressout=?,vardimension=?,varlength=?,bitposition=?,signed=?,decimal=?,buttonpath=?," +
        "readwrite=?,minvalue=?,maxvalue=?,defaultvalue=?,idvargroup=?,imageon=?,imageoff=?,delay=?,grpcategory=?" +
        " where idsite=? and idvariable=?";
        
        PreparedStatement variableStatement = conn.prepareStatement(sql);
        
        String updPriortiry = "update cfvariable set priority=? where idsite=? and idvariable=?";
        
        PreparedStatement varPriorityStatement = conn.prepareStatement(updPriortiry);
        
        Model mdl = new Model();
        Map<String,Integer> categoryIdByCode = mdl.getIdCustomCategoryByCode(conn);
        
        
        for (int i=0; i<varsToUpdate.size(); i++)
		{
			VariableDb varImportMdl = variables.get(varsToUpdate.get(i));
		
			List<String> varmdlcodes = new ArrayList<String>();
			varmdlcodes.add(varsToUpdate.get(i));
			int[] varIds = getVariablesIds(varmdlcodes);
			
			if(varIds!=null)
			{
				for(int j=0; j<varIds.length; j++)
		        {
					String imageOn = "";
	                String imageOff = "";
	                String imageButton = "";
	                
	                if(images.getImages().containsKey(varImportMdl.getImageOn()))
	                	imageOn = "images/button/" + devcode + File.separator + images.getImages().get(varImportMdl.getImageOn()).getFileName();
					if(images.getImages().containsKey(varImportMdl.getImageOff()))
						imageOff = "images/button/" + devcode + File.separator + images.getImages().get(varImportMdl.getImageOff()).getFileName();
			
					if(imageOn!=null && imageOff!=null && !imageOn.equals("") && !imageOff.equals(""))
					{
						imageButton="images/button/setvar.png";
					}
					
					variableStatement.setInt(1,varImportMdl.getType()); //type
					variableStatement.setInt(2, varImportMdl.getAddressIn()); //addressin
					variableStatement.setInt(3, varImportMdl.getAddressOut()); //addressout
					variableStatement.setInt(4, varImportMdl.getVarDimension()); //vardimension
					variableStatement.setInt(5, varImportMdl.getVarLength()); //varlength
					variableStatement.setInt(6, varImportMdl.getBitPosition()); //bitposition
					variableStatement.setString(7, Utils.booleanToPvString(varImportMdl.isSigned())); //signed
					variableStatement.setInt(8, varImportMdl.getDecimal());
					
					// 'todisplay' is customizable by user. Don't update
					//variableStatement.setString(9, varImportMdl.getToDisplay().toUpperCase());
					
					if(imageButton != "")
						variableStatement.setString(9, imageButton); //buttonpath
					else
						variableStatement.setNull(9, java.sql.Types.VARCHAR);
					
					// priority is customizable by user. Don't update
					variableStatement.setInt(10, varImportMdl.getReadWrite()); //rw
					
					if(varImportMdl.getMinValue() != "" && !(varImportMdl.getMinValue().contains("pk")))
						variableStatement.setString(11, varImportMdl.getMinValue()); //minvalue
					else
						variableStatement.setNull(11, java.sql.Types.VARCHAR);
					
					if(varImportMdl.getMaxValue() != "" && !(varImportMdl.getMaxValue().contains("pk")))
						variableStatement.setString(12, varImportMdl.getMaxValue()); //maxvalue
					else
						variableStatement.setNull(12, java.sql.Types.VARCHAR);
						
	                variableStatement.setNull(13, java.sql.Types.VARCHAR); // defaultvalue
	                // unitmeasure is customizable by user. Don't update
	                
	                //variableStatement.setInt(15, varImportMdl.getIdVarGroup()); // idvargroup
	                
	                if (varImportMdl.getIdVarGroup()!=-1)
	                	variableStatement.setInt(14, varImportMdl.getIdVarGroup());
					else
						variableStatement.setInt(14, categoryIdByCode.get(varImportMdl.getCategoryCustomCode()));
	                
	                
	                
	                variableStatement.setString(15, imageOn); //imageon
	                variableStatement.setString(16, imageOff); //imageoff
	                
	                // frequency and delta are customizable by user. Don't update
	                variableStatement.setInt(17, varImportMdl.getDelay()); //delay
	                
	                variableStatement.setInt(18, varImportMdl.getGrpCategory());
	                
	                variableStatement.setInt(19, new Integer(1));
	                variableStatement.setInt(20, varIds[j]);
	                variableStatement.executeUpdate();
		        
	                addToMinMaxRefList(minMaxRefs, varIds[j], varImportMdl.getMaxValue(), varImportMdl.getMinValue());
		        
	                //if var has historical 'child' then update it
	                if(hsvarsids.containsKey(varIds[j]))
	                {
	                	Integer idhsvariable = (Integer)hsvarsids.get(varIds[j]);
	                	variableStatement.setInt(19, new Integer(1)); 
	                	variableStatement.setInt(20, idhsvariable);
	                	variableStatement.executeUpdate();
	                }
	                
	                //if type is not 4, then update priority
	                // NOTE: priority is customizable by PVPRO pages only for 'alarm' variable type
	                if(varImportMdl.getType() != 4)
	                {
	                	varPriorityStatement.setInt(1, varImportMdl.getPriority());
	                	varPriorityStatement.setInt(2, new Integer(1));
	                	varPriorityStatement.setInt(3, varIds[j]);
	                	varPriorityStatement.executeUpdate();
	                
	                	//also for historical 'child'
	                	if(hsvarsids.containsKey(varIds[j]))
	                	{
	                		Integer idhsvariable = (Integer)hsvarsids.get(varIds[j]);
	                		varPriorityStatement.setInt(3, idhsvariable);
	                		varPriorityStatement.executeUpdate();
	                	}
	                }
		        }
			}
		}
	}
	
	private void removeVariables(List<String> varsToRemove, int profile,  Connection conn) throws Exception
	{
		if(varsToRemove.size() == 0)
			return;
		
		int[] ids = getVariablesIds(varsToRemove);
		ArrayList<Integer> histVarIds = new ArrayList<Integer>();
		ArrayList<Integer> haccpVarIds = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<Integer>> varIdsByDev = getHistVariablesIdsByDev(varsToRemove);
		
		for(int i = 0; i < varsToRemove.size(); i++)
			histVarIds.addAll(getHistorInstances(varsToRemove.get(i)));
		
		for(int i = 0; i < varsToRemove.size(); i++)
			haccpVarIds.addAll(getHACCPInstances(varsToRemove.get(i)));
		
		if(ids == null)
			return;
		
		//remove 'child' historical variables
		String sql = "update cfvariable set iscancelled='TRUE' where idsite=? and idvariable in (select idhsvariable from cfvariable where idvariable in(";
		for(int i=0; i<ids.length; i++)
		{
			sql += "?,";
		}
		sql = sql.substring(0, sql.length() -1);
		sql += ") and idhsvariable != -1 and idhsvariable is not null)";
		
		PreparedStatement updateHistStatement = conn.prepareStatement(sql);
		updateHistStatement.setInt(1, new Integer(1));
		
		for(int i=0; i<ids.length; i++)
		{
			updateHistStatement.setInt(i+2, ids[i]);
		}
		
		updateHistStatement.executeUpdate();
	
		//remove 'main' historical variables
		sql = "update cfvariable set iscancelled='TRUE', idhsvariable=? where idsite=? and idvariable in (";
		
		for(int i=0; i<ids.length; i++)
		{
			sql += "?,";
		}
		sql = sql.substring(0, sql.length() -1);
		sql += ")";
		
		PreparedStatement removeStatement = conn.prepareStatement(sql);
		
		removeStatement.setInt(1, new Integer(-1));
		removeStatement.setInt(2, new Integer(1));
		
		for(int i=0; i<ids.length; i++)
		{
			removeStatement.setInt(i+3, ids[i]);
		}
		
		removeStatement.executeUpdate();

		for(int i = 0; i < histVarIds.size(); i++)
		{
			GraphVariable.removeVariableGraphInfo(1,profile,-1,histVarIds.get(i),"FALSE");
		}
		
		for(int i = 0; i < haccpVarIds.size(); i++)
		{
			GraphVariable.removeVariableGraphInfo(1,profile,-1,haccpVarIds.get(i),"TRUE");
		}
		
		Set<Integer> devIds = varIdsByDev.keySet();
		Iterator<Integer> it = devIds.iterator();
		
		int iddev = -1;
		while(it.hasNext())
		{
			iddev = it.next();
			GraphVariable.deleteVarGroupCfPageGraph(1,profile,iddev,varIdsByDev.get(iddev));
			GraphVariable.reorderCfPageGrah(1,profile,iddev);
		}
	}
	
	
	//private utility methods
	
	// get instantiated variables ids for the model that is updating
	// 'varsondb' has 'idvarmdl's of the current updating model
	private int[] getVariablesIds(List<String> vars_codes)throws DataBaseException
	{
		int[] ids_variables = null;
		
		if (vars_codes.size() > 0)
		{
			StringBuffer sql = new StringBuffer("select idvariable from cfvariable where idvarmdl in (");
			
			for (int i = 0; i < vars_codes.size(); i++)
			{
				Record r = varsOnDb.get(vars_codes.get(i));
				sql.append(r.get("idvarmdl")+",");
			}
			
			sql.delete(sql.length() - 1, sql.length());
	        sql.append(") and iscancelled='FALSE' and idhsvariable is not null");
	        
	        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), null);
			
	        if (rs.size() > 0)
	        {
	            ids_variables = new int[rs.size()];

	            for (int i = 0; i < rs.size(); i++)
	            {
	            	ids_variables[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
	            }
	        }
		}
		return ids_variables;
	}
	
	// get all instance variable ids, of variables that have historical configuration active. 
	// all ids are grouped by device ID
	private HashMap<Integer, ArrayList<Integer>> getHistVariablesIdsByDev(List<String> vars_codes) throws Exception
	{
		HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
		
		String sql = "select iddevice from cfvariable where code in (";
		for(int i = 0; i < vars_codes.size(); i++)
		{
			 sql += "'"+vars_codes.get(i)+"',";
		}
		sql = sql.substring(0, sql.length()-1);
		
		sql += ") and iscancelled = 'FALSE' group by iddevice";
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql); 
		
		for(int i = 0; i < rs.size(); i++)
		{
			sql = "select idvariable from cfvariable where code in (";
		
			for(int j = 0; j < vars_codes.size(); j++)
			{
				 sql += "'"+vars_codes.get(j)+"',";
			}
			sql = sql.substring(0, sql.length()-1);
			
			sql += ") and iscancelled = 'FALSE' and idhsvariable is not null and idhsvariable != -1 and iddevice = "+rs.get(i).get(0);
			
			RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int j = 0; j < rs2.size(); j++)
			{
				list.add((Integer)rs2.get(j).get(0));
			}	
			map.put((Integer)rs.get(i).get(0), list);
		}
		
		return map;
	}
	
	// gat all variable instances that have historical configuration active (id of the main variable)
	// 'key' is idvar and 'val' is idhsvar
	private HashMap<Integer,Integer> getInstanceWithHistor(List<String> varcodes) throws DataBaseException
	{
		 String sql = "select idvariable, idhsvariable from cfvariable where idvarmdl in ( ";
		 for(int i = 0; i < varcodes.size(); i++)
		 {
			 Record r = varsOnDb.get(varcodes.get(i));
			 sql += r.get("idvarmdl")+",";
		 }
		 sql = sql.substring(0, sql.length()-1);
		 sql += ") and iscancelled='FALSE' and idhsvariable is not null and idhsvariable != -1";
	     RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	     HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>(); 
	     
	     for (int i=0; i<rs.size(); i++)
	     {
	    	 int varid = ((Integer) rs.get(i).get("idvariable")).intValue();
	    	 int varhsid = ((Integer) rs.get(i).get("idhsvariable")).intValue();
	    	 ids.put(varid, varhsid);
	     } 
	     
	     return ids;
	}

	// get ids of all 'child' historical variable
	private ArrayList<Integer> getHistorInstances(String varmdlcode) throws DataBaseException
	{
		 StringBuffer sql = new StringBuffer("select idhsvariable, frequency from cfvariable where idvarmdl = ");
		 Record r = varsOnDb.get(varmdlcode);
	     sql.append(r.get("idvarmdl")+" and iscancelled='FALSE' and idhsvariable is not null and idhsvariable != -1");
	     RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), null);
	     ArrayList<Integer> ids = new ArrayList<Integer>(); 
	     
	     for (int i=0; i<rs.size(); i++)
	     {
	    	 int varid = ((Integer) rs.get(i).get("idhsvariable")).intValue();
	    	 ids.add(varid);
	     }
	     
	     return ids;
	}
	
	private ArrayList<Integer> getHACCPInstances(String varmdlcode) throws DataBaseException
	{
		StringBuffer sql = new StringBuffer("select idvariable from cfvariable where idvarmdl=");
		Record r = varsOnDb.get(varmdlcode);
		sql.append(r.get("idvarmdl")+" and iscancelled='FALSE' and ishaccp='TRUE'");
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), null);
	     ArrayList<Integer> ids = new ArrayList<Integer>(); 
	     
	     for (int i=0; i<rs.size(); i++)
	     {
	    	 int varid = ((Integer) rs.get(i).get("idvariable")).intValue();
	    	 ids.add(varid);
	     }
	     
	     return ids;
	}
	
	private Map<String,Record> getDbVarmdl(int idDevMdl) throws DataBaseException 
	{	
		String sql = "select idvarmdl,code,type from cfvarmdl where iddevmdl = ?";
		String key = null;
		Map<String,Record> db_map = new HashMap<String,Record>();
		RecordSet rs = null;
		
		rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { new Integer(idDevMdl) });
		
		Record r = null;
		
		if (rs != null)
		{
			for (int i = 0; i < rs.size(); i++)
			{
				r = rs.get(i);
				key = r.get("code").toString();
				db_map.put(key, r);
			}
		}
		
		return db_map;
	}
	
	// get all variable instance id and the fields 'type' , 'readwrite'
	private Map<String,List<Integer>> getVarmdlMainFields(int iddevmdl) throws DataBaseException
	{
		String sql = "select code, type, readwrite from cfvarmdl where iddevmdl = ?";
		RecordSet record = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(iddevmdl)});
		Map<String,List<Integer>> map = new HashMap<String,List<Integer>>();
		
		for(int j=0; j<record.size(); j++)
		{
			String code = (String)record.get(j).get("code");
			List<Integer> list = new ArrayList<Integer>();
			list.add((Integer)record.get(j).get("type"));
			list.add(Integer.parseInt(((String)record.get(j).get("readwrite")).trim()));
			map.put(code, list);
		}
	
		return map;
	}
	
	// insert variable description in both main and secondary (if exists) PVPRO languages
	private void insertVarDescr(int idvariable, Translations translations, Set<String> langSet, LangUsedBean[] langused, String defaultLan, String varcode, Connection conn) throws Exception
	{
        String descr = "";
        String shortdescr = "";
        String longdescr = "";
        String translLan = "";

        if(langSet.contains(defaultLan))
        	translLan = defaultLan;
        else
        	translLan = EN.toString();
       
        descr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(DESCR);
    	shortdescr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(SHORTDESCR);
    	longdescr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(LONGDESCR);
        
        String sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
        
        PreparedStatement tableext = conn.prepareStatement(sql);
        
        tableext.setInt(1, new Integer(1));
        tableext.setString(2, defaultLan);
        tableext.setString(3, "cfvariable");
        tableext.setInt(4, idvariable);
        tableext.setString(5, descr);
        tableext.setString(6, shortdescr);
        tableext.setString(7, longdescr);
        tableext.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        
        tableext.executeUpdate();

        if(langused.length > 1)
		{
			if(langSet.contains("EN_en"))
				translLan = EN.toString();
			else
				translLan = defaultLan;
			
			descr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(DESCR);
        	shortdescr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(SHORTDESCR);
        	longdescr = translations.getLanguages().get(translLan).getSections().get("PV").getItems().get(varcode).getKeys().get(LONGDESCR);
        	
        	tableext.setString(2, EN.toString());
        	tableext.setString(5, descr);
        	tableext.setString(6, shortdescr);
        	tableext.setString(7, longdescr);
            
            tableext.executeUpdate();
		}
	}

	// set MIN-MAX refs on variables instances
	private boolean setMinMaxRefInstance(HashMap<Integer, MinMaxManagement>  minMaxrefs, Connection conn)throws Exception
	{
		int idvariable = -1;
		
		try
		{
			boolean minMaxExit = true;
			Set<Integer> keySet = minMaxrefs.keySet();
			for(int key:keySet){
				idvariable = key;
				MinMaxManagement value = minMaxrefs.get(idvariable);
				if(value.getCodeMax() != ""){
					minMaxExit = setMaxRefInstance(idvariable, value.getCodeMax(), conn);
				}
				if(value.getCodeMin() != ""){
					minMaxExit = setMinRefInstance(idvariable, value.getCodeMin(), conn);	
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
			throw new ImportException("error",e,"Error while setting min-max variable references - idvariable: " + idvariable);
		}
	}

	private boolean setMinRefInstance(int idvariable, String minVarCode, Connection conn)throws Exception
	{	
		try
		{
			String sql = "SELECT idvariable FROM cfvariable WHERE idsite=? AND iddevice=(select iddevice FROM cfvariable where idvariable = ?) AND code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ID_SITE);
			ps.setInt(2, idvariable );
			ps.setString(3, minVarCode);
			ResultSet rs = ps.executeQuery();
			int minKey = 0;
			while(rs.next()) {
				minKey = rs.getInt(1);
			}
			if (minKey == 0)
				return false;
	
			sql = "UPDATE cfvariable SET minvalue = ? WHERE idvariable =?";
				
			PreparedStatement variableMin = conn.prepareStatement(sql);
			variableMin.setString(1, "pk" + minKey);
			variableMin.setInt(2, idvariable );
			variableMin.executeUpdate();
	
			return true;
		}
		catch (Exception e)
		{
			throw new ImportException("error",e,"Error while setting MIN variable reference on device instance - iddevice: " + idvariable + ", code: " + minVarCode);
		}
	}

	private boolean setMaxRefInstance(int idvariable, String maxVarCode, Connection conn)throws Exception
	{
		try
		{
			String sql = "SELECT idvariable FROM cfvariable WHERE idsite=? AND iddevice=(select iddevice FROM cfvariable where idvariable = ?) AND code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ID_SITE);
			ps.setInt(2, idvariable );
			ps.setString(3, maxVarCode);
			ResultSet rs = ps.executeQuery();
			int maxKey = 0;
			while(rs.next()) {
				maxKey = rs.getInt(1);
			}
			if(maxKey == 0)
				return false;
			sql = "UPDATE cfvariable SET maxvalue = ? WHERE idvariable =?";
				
			PreparedStatement variableMax = conn.prepareStatement(sql);
			variableMax.setString(1, "pk" + maxKey);
			variableMax.setInt(2, idvariable );
			variableMax.executeUpdate();
	
			return true;
		}
		catch (Exception e)
		{
			throw new ImportException("error",e,"Error while setting MAX variable reference on device instance - iddevice: " + idvariable + ", code: " + maxVarCode);
		}
	}
	
	private void addToMinMaxRefList(HashMap<Integer, MinMaxManagement> minMaxRefs, int idVariable, String max, String min)
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
			minMaxRefs.put(idVariable, minMax);				
	}
}