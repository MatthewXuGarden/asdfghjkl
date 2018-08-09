package com.carel.supervisor.controller;

import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.Variable;



public class VDMappingMgr {
	
	private static VDMappingMgr meInstance = new VDMappingMgr();
	
	private HashMap<String,Integer> deviceMappingMap = new HashMap<String,Integer>(); // key is lineCode+"-"+deviceAddress
	private HashMap<String,Integer> variableMappingMap = new HashMap<String,Integer>(); //key is idDevice+"-"+type+"-"+addressIn+"-"+bitPosition+"-"+varLength;
	private HashMap<String,Integer> variableMappingMap2 = new HashMap<String,Integer>(); //key is iddevice+"-"+code

    private VDMappingMgr(){
    }//VDMappingMgr

    public static VDMappingMgr getInstance(){
        return meInstance;
    }//getInstance	
	
    public void clear(){
    	deviceMappingMap = new HashMap<String,Integer>();
 	    variableMappingMap = new HashMap<String,Integer>();
    }//clear	
	
	public Integer getIdDevice(Integer lineCode, Integer deviceAddress){
		Integer idDevice=null;
		String key=lineCode.toString()+"-"+deviceAddress.toString();
    	if(deviceMappingMap.containsKey(key)){
    		idDevice=deviceMappingMap.get(key);
		}//if
    	else
    	{
    		/*
    		 * Modificata query ed aggiunto: 
    		 * and cfdevice.idline=cfline.idline
    		 * Altrimenti venivano recuperati dei record sbagliati
    		 */
    		StringBuffer sql=new StringBuffer();
    		sql.append("SELECT iddevice FROM cfdevice,cfline WHERE cfdevice.idsite=1 AND cfdevice.iscancelled='FALSE' ");
    		sql.append(" AND cfline.code=? ");
    		sql.append(" AND cfdevice.address= ? and cfdevice.idline=cfline.idline");

    		RecordSet recordSet=null;
			try {
				recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
				        new Object[] { lineCode,deviceAddress });
				idDevice=(Integer) recordSet.get(0).get(0);
				deviceMappingMap.put(key, idDevice);
			}//try 
            catch (DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}//catch
    	}//else
    	return idDevice;
    }//getIdDevice
	
	 public int getDeviceId(String data) 
	 	throws Exception
	 {
    	int deviceId=0;
    	if(!data.contains("-"))
    	{
    		/*
        	 * Se non contiene il carattere '-'
        	 * significa che sta andando nel vecchio modo
        	 */
    		deviceId=Integer.valueOf(data);	
    	}
    	else
    	{
    		/*
        	 * Se contiene il carattere '-'
        	 * vuol dire che funziona con lo svincolo chiavi primarie
        	 */
    		String []datas = data.split("-");
    		/*
        	 *	Controllo se la variabile � logica.
        	 *	Se datas[0] == 0 allora vuol dire che � una logica e quindi 
        	 *	utilizzo il suo unique key.
        	 */
    		Integer idDevice = null;
    		if (datas != null && datas.length > 0)
    		{
                // When the device is Internal IO, the first character "-" means
                // "minus" and it isn't a separator ("splitter"):
                // line identifier:   -1
                // device identifier: 0
                if ( !data.startsWith("-1-0") )
                {
    	    		if(datas[0].equalsIgnoreCase("0"))
    	    			idDevice = new Integer(datas[1]);
    	    		else
    	    		{
    	    			if(datas[0].equals(""))
    	    				datas[0] = "0";
    	    			idDevice = getIdDevice( new Integer(datas[0]).intValue() , new Integer(datas[1]).intValue());
    	   			}
                }
                else
                {
    	    		if(datas[1].equalsIgnoreCase("0"))
    	    			idDevice = new Integer(datas[2]);
    	    		else
    	    		{
    	    			if(datas[1].equals(""))
    	    				datas[1] = "0";
    	    			idDevice = getIdDevice( new Integer("-" + datas[1]).intValue() , new Integer(datas[2]).intValue());
    	   			}
                }
    		}
    		
    		deviceId=idDevice!=null?idDevice.intValue():deviceId;
    	}
    	return deviceId;
	}
	 
	 public Variable getVariable(String data) 
	 	throws Exception
	 {
    	Variable var=null;
    	
    	if(!data.contains("-"))
    	{
    		/*
        	 * Se non contiene il carattere '-'
        	 * significa che sta andando nel vecchio modo
        	 */
    		var=ControllerMgr.getInstance().getFromField(new Integer(data).intValue());	
    	}
    	else
    	{
    		/*
        	 * Se contiene il carattere '-'
        	 * vuol dire che funziona con lo svincolo chiavi primarie
        	 */
    		//Integer idVar = null;
    		Integer idVar = new Integer(0);
    		String []datas=data.split("-");
    		/*
        	 *	Controllo se la variabile � logica.
        	 *	Se datas[0] == 0 allora vuol dire che � una logica e quindi 
        	 *	utilizzo il suo unique key.
        	 */
    		if (datas != null && datas.length > 0)
    		{
                // When the device is Internal IO, the first character "-" means
                // "minus" and it isn't a separator ("splitter"):
                // line identifier:   -1
                // device identifier: 0
                if ( !data.startsWith("-1-0") )
                {
    	    		if(datas[0].equalsIgnoreCase("0"))
    	    			idVar = new Integer(datas[3]);
    	    		else
    	    			idVar = getIdVariable( new Integer(datas[0]).intValue() ,new Integer(datas[1]).intValue(), new Integer(datas[2]).intValue(), new Integer(datas[3]).intValue(),new Integer(datas[4]).intValue(),new Integer(datas[5]).intValue());
                }
                else
                {
    	    		if(datas[1].equalsIgnoreCase("0"))
    	    			idVar = new Integer(datas[4]);
    	    		else
    	    			idVar = getIdVariable( new Integer("-" + datas[1]).intValue() ,new Integer(datas[2]).intValue(), new Integer(datas[3]).intValue(), new Integer(datas[4]).intValue(),new Integer(datas[5]).intValue(),new Integer(datas[6]).intValue());
                }
    		}
    		
    		var=ControllerMgr.getInstance().getFromField(idVar.intValue());
    	}
    	return var;
	 }
	
	public Integer getIdVariable(Integer lineCode, Integer deviceAddress,Integer type, Integer addressInVariable,Integer bitPosition,Integer varLength){
    	Integer idVariable=null;
		Integer idDevice=getIdDevice(lineCode,deviceAddress);
		
		String key=idDevice.toString()+"-"+type.toString()+"-"+addressInVariable.toString()+"-"+bitPosition+"-"+varLength;
    	if(variableMappingMap.containsKey(key)){
    		idVariable=variableMappingMap.get(key);
		}//if
    	else{
    		StringBuffer sql=new StringBuffer();
    		sql.append("SELECT idvariable FROM cfvariable WHERE idsite=1 AND iscancelled='FALSE' ");
    		sql.append(" AND iddevice = ? ");
    		sql.append(" AND type = ? ");
    		sql.append(" AND addressin = ? "); 
    		sql.append(" AND bitposition = ? ");
    		sql.append(" AND varlength = ? ");
    		
    		RecordSet recordSet=null;
			try {
				recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
				        new Object[] { idDevice,type,addressInVariable ,bitPosition,varLength});
				
				idVariable=(Integer) recordSet.get(0).get(0);
				variableMappingMap.put(key, idVariable);
			}//try 
            catch (DataBaseException e) {
            	LoggerMgr.getLogger(this.getClass()).error(e);
			}//catch
    	}//else
    	return idVariable;
    }//getIdVariable

	public Integer getIdVariable(Integer idDevice, String varCode){
    	Integer idVariable=null;
				
		String key=idDevice.toString()+"-"+varCode;
    	if(variableMappingMap2.containsKey(key)){
    		idVariable=variableMappingMap2.get(key);
		}//if
    	else{
    		StringBuffer sql=new StringBuffer();
    		sql.append("SELECT idvariable FROM cfvariable WHERE idsite=1 AND iscancelled='FALSE' ");
    		sql.append(" AND iddevice = ? ");
    		sql.append(" AND code = ? ");
    		
    		RecordSet recordSet=null;
			try {
				recordSet = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
				        new Object[] { idDevice,varCode});
				if(recordSet.size()>0)
				{
					idVariable=(Integer) recordSet.get(0).get(0);
					variableMappingMap2.put(key, idVariable);
				}
			}//try 
            catch (DataBaseException e) {
            	LoggerMgr.getLogger(this.getClass()).error(e);
			}//catch
    	}//else
    	return idVariable;
    }//getIdVariable

}//Variable Device Mapping Manager


