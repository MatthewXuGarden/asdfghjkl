package com.carel.supervisor.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bsh.This;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.function.CalcElement;
import com.carel.supervisor.controller.function.CalcVariable;
import com.carel.supervisor.controller.function.Function;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.IRetriever;
import com.carel.supervisor.field.RetrieveDummy;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DataCollector;


public class VariablesAccess
{
    private DataCollector dataCollector = null;
    private HashMap<Integer, Function> functions = null;
    public VariablesAccess(DataCollector dataCollector)
    {
        this.dataCollector = dataCollector;
        this.functions = new HashMap<Integer, Function>();
    }

    public void addFunction(Integer id, Function func)
    {
    	Iterator iter = functions.entrySet().iterator(); 
    	while (iter.hasNext()) { 
    	    Map.Entry entry = (Map.Entry) iter.next(); 
    	    Function function = (Function)entry.getValue();
    	    CalcElement[] elements = function.getElements();
    	    for(int i=0;i<elements.length;i++)
    	    {
    	    	CalcElement element = elements[i];
    	    	if(element instanceof CalcVariable)
    	    	{
    	    		CalcVariable calcV = (CalcVariable)element;
    	    		if(calcV.getInnerVariable() != null && calcV.getInnerVariable().getInfo().getId().equals(id)
    	    				&& calcV.getInnerVariable().getRetriever() instanceof RetrieveDummy)
    	    		{
    	    			calcV.getInnerVariable().setRetriever(func);
    	    		}
    	    	}
    	    }
    	} 
        functions.put(id, func);
    }

    public Variable getFromField(VariableInfo varinfo) throws Exception
	{
	    	return innerFromField(varinfo, false);
	}
    
    public void refreshValue(Variable variable) throws Exception
	{
    	if (variable.getInfo().isLogic())
        {
            variable.getRetriever().retrieve(variable);
        }
        else
        {
            dataCollector.getFromField(variable);
        }
	}
    
    private Variable innerFromField(VariableInfo varInfo, boolean retrieveData) throws Exception
	{
    	DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");

        DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(varInfo.getDevice());
        varInfo.setDeviceInfo(deviceInfo);
        Variable variable = new Variable(varInfo);
        IRetriever retriever = null;

        if (varInfo.isLogic())
        {
            retriever = (IRetriever) functions.get(varInfo.getId());
            variable.setRetriever(retriever);
            if (retrieveData)
        	{
            	retriever.retrieve(variable);
        	}
        }
        else
        {
            retriever = (IRetriever) dataCollector.getDataConnector(varInfo.getProtocolType());
            variable.setRetriever(retriever);
            if (retrieveData)
        	{
            	dataCollector.getFromField(variable);
        	}
        }

        return variable;
	}
    
    public Variable getFromField(VarphyBean varphyBean, boolean retrieveData)
        throws Exception
    {
        //Recupero i dati del dispositivo
    	VariableInfo varInfo = new VariableInfo(varphyBean);
    	return innerFromField(varInfo, retrieveData);
    }

    public Variable getFromField(int idVariable) throws Exception
    {
        return getFromField(idVariable,false);
    }
    
    // 'isValueOfDimension' flag is used to specify (in case of compressed variables) if the value has to be taken
    // from the whole 'varDimension' bit size
    public Variable getFromField(int idVariable,boolean isValueOfDimension) throws Exception
    {
        Variable variable = retrieve(idVariable);
        variable.setIsValueOfDimension(isValueOfDimension);
        variable.getRetriever().retrieve(variable);

        return variable;
    }

    public Variable[] getFromField(int[] idVariable) throws Exception
    {
    	Variable[] variable = retrieve(idVariable);

        //Aggiorno il valore della variabile condizionato al protocollo
        for(int i = 0; i < variable.length; i++)
        {
        	variable[i].getRetriever().retrieve(variable[i]);
        }
        return variable;
    }
    
    public Variable[] getFromFieldWithDuplicates(int[] idVariable) throws Exception //this method manages the idVariable array with duplicated values 
    {
    	Variable[] variable = retrieveWithDuplicates(idVariable);

        //Aggiorno il valore della variabile condizionato al protocollo
        for(int i = 0; i < variable.length; i++)
        {
        	variable[i].getRetriever().retrieve(variable[i]);
        }
        return variable;
    }
    
    public Variable retrieve(int idVariable) throws Exception
    {
        VariableInfo varInfo = null;
        Variable variable = null;

        String sql = "select * from cfvariable where idvariable = ? and idsite=1 and iscancelled='FALSE'";

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idVariable) });
        if( recordSet.size() >= 1 ) {
        	Record record = recordSet.get(0);
        	varInfo = new VariableInfo(record);
        }
        else {
        	LoggerMgr.getLogger("VariableAccess").error("Unable to find variable id: " + idVariable);
        	return null;
        }

        //Recupero i dati del dispositivo
        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");

        DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(varInfo.getDevice());
        varInfo.setDeviceInfo(deviceInfo);

        //aggiorno lo stato delle informazioni della variabile
        //Mi creo l'astrazione dai dati della variabile, utile per aggiornamento
        variable = new Variable(varInfo);

        IRetriever retriever = null;

        if (varInfo.isLogic())
        {
            retriever = (IRetriever) functions.get(varInfo.getId());
            if(retriever != null)
            {
            	variable.setRetriever(retriever);
            	retriever.retrieve(variable);
            }
        }
        else
        {
            retriever = (IRetriever) dataCollector.getDataConnector(varInfo.getProtocolType());
            variable.setRetriever(retriever);
            dataCollector.getFromField(variable);
        }

        return variable;
    }
    
    public Variable[] retrieve(int[] idVariable) throws Exception
    {
        VariableInfo varInfo = null;
        StringBuffer sql = new StringBuffer("select * from cfvariable where idsite=1 and iscancelled='FALSE' and idvariable in (");
        Integer[] id = new Integer[idVariable.length];
        HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();
        for(int i = 0; i < idVariable.length; i++)
        {
        	sql.append("?");
        	if (i < (idVariable.length - 1))
        	{
        		sql.append(",");
        	}
        	id[i] = new Integer(idVariable[i]);
        	table.put(id[i],new Integer(i));
        }
        sql.append(")");
        
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),id );
        
        int size = recordSet.size();
        Variable[] variable = new Variable[size];
        for(int i = 0; i < size; i++)
        {
	        Record record = recordSet.get(i);
	        varInfo = new VariableInfo(record);
	
	        //Recupero i dati del dispositivo
	        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
	
	        DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(varInfo.getDevice());
	        varInfo.setDeviceInfo(deviceInfo);
	
	        //aggiorno lo stato delle informazioni della variabile
	        //Mi creo l'astrazione dai dati della variabile, utile per aggiornamento
	        int pos = ((Integer)table.get(varInfo.getId())).intValue(); 
	        variable[pos] = new Variable(varInfo);
	
	        IRetriever retriever = null;
	
	        if (varInfo.isLogic())
	        {
	            retriever = (IRetriever) functions.get(varInfo.getId());
	            variable[pos].setRetriever(retriever);
	            retriever.retrieve(variable[pos]);
	        }
	        else
	        {
	            retriever = (IRetriever) dataCollector.getDataConnector(varInfo.getProtocolType());
	            variable[pos].setRetriever(retriever);
	            dataCollector.getFromField(variable[pos]);
	        }
        }
        return variable;
    }
    
    public Variable[] retrieveWithDuplicates(int[] idVariable) throws Exception //this method manages the idVariable array with duplicated values
    {
        StringBuffer sql = new StringBuffer("select * from cfvariable where idsite=1 and iscancelled='FALSE' and idvariable in (");
        Integer[] id = new Integer[idVariable.length];
        HashMap<Integer, VariableInfo> table = new HashMap<Integer, VariableInfo>();
        for(int i = 0; i < idVariable.length; i++)
        {
        	sql.append("?");
        	if (i < (idVariable.length - 1))
        	{
        		sql.append(",");
        	}
        	id[i] = new Integer(idVariable[i]);
        	//table.put(id[i],new Integer(i));
        }
        sql.append(")");
        
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),id );
        
        for(int i = 0; i < recordSet.size(); i++)
        {
	        Record record = recordSet.get(i);
	        VariableInfo varInfo = new VariableInfo(record);
        
	        table.put(varInfo.getId(), varInfo);
        }
        
        Variable[] variable = new Variable[idVariable.length];
        
        //Recupero i dati del dispositivo
        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
        
        for(int i = 0; i < idVariable.length; i++)
        {
	        VariableInfo varInfoResult = (VariableInfo)table.get(idVariable[i]);
        	
        	DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(varInfoResult.getDevice());
        	varInfoResult.setDeviceInfo(deviceInfo);
        
        	variable[i] = new Variable(varInfoResult);
        	
        	IRetriever retriever = null;
        	
	        if (varInfoResult.isLogic())
	        {
	            retriever = (IRetriever) functions.get(varInfoResult.getId());
	            variable[i].setRetriever(retriever);
	            retriever.retrieve(variable[i]);
	        }
	        else
	        {
	            retriever = (IRetriever) dataCollector.getDataConnector(varInfoResult.getProtocolType());
	            variable[i].setRetriever(retriever);
	            dataCollector.getFromField(variable[i]);
	        }
        }
        
        return variable;
    }
    
    
    public static int getIdDevice(int idVariable)
    {
        int idDevice = 0;
    	try { 
	    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select iddevice from cfvariable where idvariable = " + idVariable);
	        idDevice = (Integer)rs.get(0).get(0);
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger("VariableAccess").error(e);
    	}
        return idDevice;
    }
    
}
