package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;


public class LogicDeviceBean
{
    private static final String ID_DEVICE = "iddevice";
    private static final String DESCRIPTION = "description";
    private static final String NUM_VAR = "numvar";

    private int iddevice = -1;
    private String description = null;
    private int numVars=0;
    
    public LogicDeviceBean()
    {
    }

    public LogicDeviceBean(Record record) throws DataBaseException
    {
    	if(record.hasColumn(ID_DEVICE))
    		this.iddevice = ((Integer) record.get(ID_DEVICE)).intValue();
    	if(record.hasColumn(DESCRIPTION))
    		this.description = (String) record.get(DESCRIPTION);
    	if(record.hasColumn(NUM_VAR))
    		this.numVars= ((Integer) record.get(NUM_VAR)).intValue();
    }//LogicDeviceBean

	public String getDescription() {
		return description;
	}//getDescription


	public int getIddevice() {
		return iddevice;
	}//getIddevice


	public int getNumVars() {
		return numVars;
	}//getNumVars


}//LogicDeviceBean
