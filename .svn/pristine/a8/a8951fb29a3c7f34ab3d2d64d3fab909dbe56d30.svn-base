package com.carel.supervisor.presentation.devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.script.EnumerationMgr;
import com.carel.supervisor.script.ExpressionMgr;
import com.carel.supervisor.script.special.SContext;
import com.carel.supervisor.script.special.Special;


public abstract class AbstractDtlDevice
{
	protected UserSession session = null;
	protected List<VarphyBean> variables = new ArrayList<VarphyBean>();
	protected Map<Integer,String> valuesOriginal = new HashMap<Integer,String>();
    protected List<String> values = new ArrayList<String>();
    protected String lang = "";
	protected int idDevice = 0;
	protected int idDevMdl = -1;
    
    public AbstractDtlDevice(UserSession session,String lang,int idDevice)
    {
        this.idDevice = idDevice;
        this.lang = lang;
        this.session = session;
    }
    
    public void setIdDevMdl(int id) {
    	this.idDevMdl = id;
    }
    
    public int getIdDevMdl() {
    	return this.idDevMdl;
    }
    
    public void profileVariables(VarphyBean[] listVarphy) 
    {
    	for(int i = 0; i < listVarphy.length; i++)
    	{
            if (check(listVarphy[i]))
            {
            	addVariable(listVarphy[i]);
            }
    	}
    	checkVariables();
    }
    
    public void checkVariables()
    {
    	ExpressionMgr expMgr = this.session.getCurrentUserTransaction().getBoTrx().getExpressionMgr();
    	Special sp = this.session.getCurrentUserTransaction().getBoTrx().getSpecial();
    	SContext sc = new SContext();
    	VarphyBean tmp = null;
    	List<VarphyBean> variablesTmp = new ArrayList<VarphyBean>();
        List<String> valuesTmp = new ArrayList<String>();
        boolean bExp = false;
        
    	if(expMgr != null)
    	{
    		for(int i=0; i<this.variables.size(); i++)
    		{
    			tmp = this.variables.get(i);
    			bExp = expMgr.evaluate(tmp.getId().intValue());
    			
    			if(!bExp)
    			{
    				sc.setVariable(tmp);
    				sc.setVariableMap(expMgr.getVariablesList());
    				bExp = sp.display(sc,bExp);
    			}
    			
    			if(bExp)
    			{
    				variablesTmp.add(this.variables.get(i));
    				valuesTmp.add(this.values.get(i));
    			}
    		}
    		this.variables = variablesTmp;
    		this.values = valuesTmp;
    	}
    }

    public String encodeVariables(VarphyBean variable,float value)
    {
    	String ret = "";
    	ExpressionMgr expMgr = this.session.getCurrentUserTransaction().getBoTrx().getExpressionMgr();
    	Special sp = this.session.getCurrentUserTransaction().getBoTrx().getSpecial();
    	SContext sc = new SContext();
    	sc.setVariable(variable);
		sc.setVariableMap(expMgr.getVariablesList());
		sc.setIdDevice(this.idDevice);
    	ret = sp.value(sc);
    	if(ret == null || ret.equalsIgnoreCase(""))
    	{
    		ret = EnumerationMgr.getInstance().getEnumCode(variable.getIdMdl(),value,this.session.getLanguage());
    	}
    	return ret;
    }
    
    protected void addVariable(VarphyBean variable)
    {
    	// 
    	ExpressionMgr expMgr = this.session.getCurrentUserTransaction().getBoTrx().getExpressionMgr();
    	Special sp = this.session.getCurrentUserTransaction().getBoTrx().getSpecial();
    	
    	SContext sc = new SContext();
    	sc.setVariable(variable);
		sc.setVariableMap(expMgr.getVariablesList());
		sc.setIdDevice(this.idDevice);
		
		VarphyBean[] tmp = sp.check(sc);
    	
    	
		for(int i=0; i<tmp.length; i++)
		{
			variable = tmp[i];
			
			variables.add(variable);
		
	    	String sValue = "";
	    	float varvalue = 0;
	    	
            try
            {
            	Variable variableTemp = ControllerMgr.getInstance().getFromField(variable);
            	varvalue = variableTemp.getCurrentValue();
            	sValue = encodeVariables(variable,varvalue);
            	
            	if(sValue != null && !sValue.equalsIgnoreCase(""))
            		values.add(sValue);
            	else
            		values.add(variableTemp.getFormattedValue());
            	if(variable.getType() == VariableInfo.TYPE_DIGITAL)
            		valuesOriginal.put(variable.getId(), variableTemp.getFormattedValue());
            }
            catch (Exception e)
            {
            	values.add("***");
            	if(variable.getType() == VariableInfo.TYPE_DIGITAL)
            		valuesOriginal.put(variable.getId(), "***");
            }
		}
    }
    
    protected String getVariableValue(int idVar)
    {
    	String ret = "***";    	
        try
        {
        	ret = ControllerMgr.getInstance().getFromField(idVar).getFormattedValue();
        }
        catch (Exception e) {
        }
    	return ret;
    }
    
    protected String buildLedStatus(String value, String cssClass)
    {
    	String ret = "<img src='images/led/";
    	if(value != null && value.equalsIgnoreCase("1"))
    		ret += "L1.gif";
    	else
    		ret += "L0.gif";
    	ret += "'";
    	// se passata come parametro inserisco una classe css
    	if(cssClass != null && !cssClass.equalsIgnoreCase(""))
    		ret += " class='"+cssClass+"' ";
    	ret += "/>";
    	return ret;
    }
    
    public abstract String renderVariables(String tableName);
    public abstract String refreshVariables(String tableName);
    public abstract boolean check(VarphyBean variable);
}
