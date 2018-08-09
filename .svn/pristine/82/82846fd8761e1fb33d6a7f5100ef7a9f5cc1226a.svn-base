package com.carel.supervisor.controller.database.zipped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class VariableZippedManager
{
	private static VariableZippedManager me = new VariableZippedManager();

	Map<Integer,VariableZippedDevice> deviceMap = new HashMap<Integer,VariableZippedDevice>(); 
        
	 /*
     * Variabile presente come compressa
     */
    public boolean isZipVariable(int idDevice,int idVariable) 
    {
    	VariableZippedDevice zippedDevice = deviceMap.get(idDevice);
    	if(zippedDevice != null)
    	{
    		if(zippedDevice.getAddressInMap().containsKey(idVariable))
    			return true;
    		else
    			return false;
    	}
    	else
    		return false;
    }
    
    /*
     * Set the value on the zipped var 
     */
    public void setValue(int idDevice,int idVariable,String value)
    {
    	VariableZippedDevice deviceZippedVars = deviceMap.get(idDevice);
    	if(deviceZippedVars != null)
    	{
    		String type_address = deviceZippedVars.getAddressInMap().get(idVariable);
    		ArrayList<VariableZip> list = (ArrayList<VariableZip>)deviceZippedVars.getVarsIdsMap().get(type_address);
    		if(list != null)
    		{
    			for(int i=0; i<list.size(); i++)
                {
    				VariableZip vZip = (VariableZip)list.get(i);
    				vZip.loadValue();
                    if(vZip.getId().intValue() == idVariable)
                    {
                        vZip.setNewValue(value);
                    }
                }
    		}
    	}
    }
    
    public int getValueOfDimension(int idDevice,int idVariable)
    {
    	VariableZippedDevice deviceZippedVars = deviceMap.get(idDevice);
    	int valueOfDimension = 0;
    	if(deviceZippedVars != null)
    	{
    		String type_address = deviceZippedVars.getAddressInMap().get(idVariable);
    		ArrayList<VariableZip> list = (ArrayList<VariableZip>)deviceZippedVars.getVarsIdsMap().get(type_address);
    		if(list != null)
    		{
   				VariableZip vZip = (VariableZip)list.get(0);
    			vZip.loadValueOfDimension();
    			valueOfDimension = new Float(vZip.getRawValue()).intValue();
    		}
    	}
    	return valueOfDimension;
    }
    
    /*
     * Get the value of the whole variable (composed by 2 or more compressed variables)
     *  
     */
    public String getValue(int idDevice,int idVariable,int valueOfDimension)
    {
        String value = null;
        //Map variablesMap = deviceMap.get(idDevice);
        VariableZippedDevice deviceZippedVars = deviceMap.get(idDevice);
        
    	if(deviceZippedVars != null)
    	{
    		String type_address = deviceZippedVars.getAddressInMap().get(idVariable);
    		ArrayList<VariableZip> list = (ArrayList<VariableZip>)deviceZippedVars.getVarsIdsMap().get(type_address);
    		if(list != null && list.size()>0)
    		{
    			value = ""+callBitManipulation(list,valueOfDimension);
    		}
    	}
        return value;
    }
    
    
    public void load()
    	throws NoZippedVariableException
	{
    	deviceMap.clear();
	    VariableZip[] list = new VariableZip[0];
	    RecordSet rs = null;
	    Record r = null;
	    
	    String sql = "select * "+
	    	"from cfvariable "+
	    	"where iscancelled='FALSE' and idsite=1 and readwrite<>'1' and idhsvariable is not null and varlength<>vardimension "+
	    	"order by iddevice,addressin,type";
	    
	    try
	    {
	        rs = DatabaseMgr.getInstance().executeQuery(null,sql);
	        if (rs != null)
	        {
	            if(rs.size() == 0)
	                throw new NoZippedVariableException();
	            
	            list = new VariableZip[rs.size()];
	            for (int i=0; i<rs.size(); i++)
	            {
	                r = rs.get(i);
	                if (r != null)
	                {
	                    list[i] = new VariableZip(r);
	                }
	            }
	        }
	    }
	    catch(NoZippedVariableException nze)
	    {
	        return;
	    }
	    catch (Exception e) {
	        Logger logger = LoggerMgr.getLogger(this.getClass());
	        logger.error(e);
	    }
	    buildMap(list);
	}
    

    private void buildMap(VariableZip[] zippedvarslist) 
    {
    	VariableZip tmp = null;
    	ArrayList<VariableZip> variableZipList = null;
    	VariableZippedDevice deviceZippedVars = null;
    	
        if(zippedvarslist != null)
        {
            for(int i=0; i<zippedvarslist.length; i++) 
            {
            	VariableZip vZip = zippedvarslist[i];
            	String type_addressIn = vZip.getType()+"_"+vZip.getAddressIn();
            	
            	// if iddevice of the current var is different from the one associated to the previous one
            	// NOTE: the variables in the list are ordered by iddevice (from the query on 'load' method)
            	if(tmp == null || !tmp.getDevice().equals(vZip.getDevice()))
            	{
            		variableZipList = new ArrayList<VariableZip>();
            		variableZipList.add(vZip);
            		
            		deviceZippedVars = new VariableZippedDevice();
            		deviceZippedVars.getAddressInMap().put(vZip.getId(), type_addressIn);
            		deviceZippedVars.getVarsIdsMap().put(type_addressIn,variableZipList);
            		
            		deviceMap.put(vZip.getDevice(), deviceZippedVars);

            	}
            	// otherwise, if 'type' and 'addressin' are identical
            	else if(typeCompare(tmp.getType(),vZip.getType()) &&
            			tmp.getAddressIn().equals(vZip.getAddressIn()))
            	{
            		deviceZippedVars.getAddressInMap().put(vZip.getId(), type_addressIn);
            		variableZipList.add(vZip);            		
            	}
            	// if 'type' is different,'addressin' is identical 
            	else if(!typeCompare(tmp.getType(),vZip.getType()) && tmp.getAddressIn().equals(vZip.getAddressIn()))
            	{
            		//make sure digital,Integer, alarm from the same integer address are in the same List
            		if(deviceZippedVars.getVarsIdsMap().containsKey(type_addressIn))
            		{
            			deviceZippedVars.getAddressInMap().put(vZip.getId(), type_addressIn);
                		variableZipList.add(vZip); 
            		}
            		else
            		{
	            		variableZipList = new ArrayList<VariableZip>();
	            		variableZipList.add(vZip);
	            		deviceZippedVars.getAddressInMap().put(vZip.getId(), type_addressIn);
	            		deviceZippedVars.getVarsIdsMap().put(type_addressIn,variableZipList);
            		}
            	}
            	//'addressin' is different
            	else if(!tmp.getAddressIn().equals(vZip.getAddressIn()))
            	{
            		variableZipList = new ArrayList<VariableZip>();
	            	variableZipList.add(vZip);
	            	deviceZippedVars.getAddressInMap().put(vZip.getId(), type_addressIn);
	            	deviceZippedVars.getVarsIdsMap().put(type_addressIn,variableZipList);
            	}
            	tmp = vZip;
            }
        }
    }
    private boolean typeCompare(int type1, int type2)
    {
    	if(type1 == type2)
    		return true;
    	else if(type1 == 2 || type2 == 2)
    		return false;
    	else
    		return true;
    }
    public int getType(int type)
    {
    	int result = 2;
		if(type != 2)
			result = 3;
		return result;
    }
    private int callBitManipulation(ArrayList<VariableZip> variables,int valueOfDimension)
    {
        Float values[] = new Float[variables.size()];
        int offset[] = new int[variables.size()];
        int dimension[] = new int[variables.size()];
        
        VariableZip vz = null;
        for(int i=0; i<variables.size(); i++)
        {
            vz = variables.get(i);
            if(vz.getNewValue() == null)
                values[i] = new Float(vz.getRawValue()*Math.pow(10.0, vz.getDecimal()));
            else
                values[i] = new Float(Float.parseFloat(vz.getNewValue())*Math.pow(10.0, vz.getDecimal()));
            
            offset[i] = vz.getBitPosition();
            dimension[i] = vz.getVarLength();
        }
        
        return BitManipulation.NewFusionNumberCarel(values,dimension,offset,valueOfDimension);
    }
    
    public static VariableZippedManager getInstance()
    {
    	return me;
    }
}
