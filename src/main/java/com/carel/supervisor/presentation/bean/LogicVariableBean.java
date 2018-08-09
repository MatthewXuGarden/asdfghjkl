package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;


public class LogicVariableBean
{
	private static final String ID_VARIABLE = "idvariable";
	private static final String ID_DEVICE = "iddevice";
    
    private static final String DESCRIPTION = "description";
    private static final String DESCRIPTION_DEVICE = "descriptiondev";
    private static final String TYPE = "type";
    private static final String VARIABLE_LOGIC = "islogic";
    private static final String PARAMETERS = "parameters";
    private static final String OPERTYPE = "opertype";
    private static final String IDUNITMEASUREMENT = "idunitmeasurement";
    private static final String FUNCTIONCODE = "functioncode";
    private static final String DECIMAL = "decimal";
    
    
    
    
    

    private int idVariable = -1;
    private Integer idDevice = null;
    private String description = null;
    private int type=0;
    private int idunitmeasurement = 0;
    private String isDeviceLogic=null;
    private String parameters=null;
    private String opertype=null;
    private String descriptionDevice = null;
    private int functionCode=-1;
    
    
    private int masterType=0; //se la variabile � figlia nella logica della presentation rappresenta il tipo della variabile madre
    
    
    private int decimal=0;
    
    public int getMasterType() {
		return masterType;
	}//getMasterType

	public void setMasterType(int masterType) {
		this.masterType = masterType;
	}//setMasterType

	public LogicVariableBean()
    {
    }//LogicVariableBean

    public LogicVariableBean(Record record) throws DataBaseException
    {
    	if(record.hasColumn(ID_VARIABLE))
    		this.idVariable = ((Integer) record.get(ID_VARIABLE)).intValue();
    	if(record.hasColumn(ID_DEVICE))
    		this.idDevice = ((Integer) record.get(ID_DEVICE));
    	
    	if(record.hasColumn(DESCRIPTION))
    		this.description = (String) record.get(DESCRIPTION);
    	if(record.hasColumn(TYPE))
    		this.type= ((Integer) record.get(TYPE)).intValue();
    	if(record.hasColumn(VARIABLE_LOGIC))
    		this.isDeviceLogic = (String) record.get(VARIABLE_LOGIC);
    
    	if(record.hasColumn(PARAMETERS))
    		this.parameters = (String) record.get(PARAMETERS);
    	
    	if(record.hasColumn(OPERTYPE))
    		this.opertype = (String) record.get(OPERTYPE);
    	
    	if(record.hasColumn(DESCRIPTION_DEVICE))
    		this.descriptionDevice = (String) record.get(DESCRIPTION_DEVICE);
    	
    	if(record.hasColumn(IDUNITMEASUREMENT))
    		this.idunitmeasurement = ((Integer) record.get(IDUNITMEASUREMENT)).intValue();
    	
    	if(record.hasColumn(FUNCTIONCODE))
    		this.functionCode = ((Integer) record.get(FUNCTIONCODE)).intValue();
    	
    	if(record.hasColumn(DECIMAL))
    		this.decimal = ((Integer) record.get(DECIMAL)).intValue();
    	
    }//LogicVariableBean

	public String getDescription() {
		return description;
	}//getDescription

	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getIdUnitOfMeasurement() {
		return idunitmeasurement;
	}//getIdUnitOfMeasurement


	public int getIdVariable() {
		return idVariable;
	}//getIdvariable
	
	public Integer getIdDevice() {
		return idDevice;
	}//getIdvariable

	public int getType() {
		return type;
	}//getType

	public boolean getIsDeviceLogic() {
		return new String(isDeviceLogic).trim().equals("TRUE");
	}//getIsDeviceLogic
	
	public String getTypeTranslate(String language) {
		return translateVariableType(new Integer(type),language);
	}//getType
	
	public String getOpertype() {
		return opertype;
	}//getOpertype

	public String getParameters() {
		return parameters;
	}//getParameters
	
	public String getDeviceDescription() {
		return descriptionDevice;
	}//getDeviceDescription

	public int getFunctionCode() {
		return functionCode;
	}//getFunctionCode

	public void setIdVariable(int idVariable) {
		this.idVariable = idVariable;
	}//setIdVariable

	public void setOpertype(String opertype) {
		this.opertype = opertype;
	}//setOpertype
	
	public void setIdunitmeasurement(int idunitmeasurement) {
		this.idunitmeasurement = idunitmeasurement;
	}//setIdunitmeasurement

	public void setDecimal(int decimal)
	{
		this.decimal = decimal;
	}
	
	public int getDecimal()
	{
		return this.decimal;
	}
	
	private String translateVariableType(Integer variableType,String language){
    	LangService langService = LangMgr.getInstance().getLangService(language);
    	String translateType="";
    	switch(variableType.intValue()){
			case VariableInfo.TYPE_DIGITAL :
				translateType=langService.getString("logicdevicePage","digital");
				break;
			case VariableInfo.TYPE_ANALOGIC :
				translateType=langService.getString("logicdevicePage","analogic");
				break;
			case VariableInfo.TYPE_INTEGER :
				translateType=langService.getString("logicdevicePage","integer");
				break;
			case VariableInfo.TYPE_ALARM :
				translateType=langService.getString("logicdevicePage","alarms");
				break;
			}//switch
	
    	return translateType;
    	
    }//translateVariableType



	

}//LogicDeviceBean
