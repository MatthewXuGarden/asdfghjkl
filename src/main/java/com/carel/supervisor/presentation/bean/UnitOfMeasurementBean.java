package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class UnitOfMeasurementBean {

	private static final String IDUNITMEASUREMENT = "idunitmeasurement";
	private static final String DESCRIPTION = "description";
	 
	private int idUnitOfMeasurement = 0;
    private String description = "";

    
    public UnitOfMeasurementBean(Record record) {
    	if(record.hasColumn(IDUNITMEASUREMENT))
    		this.idUnitOfMeasurement = ((Integer) record.get(IDUNITMEASUREMENT)).intValue();
    	if(record.hasColumn(DESCRIPTION))
    		this.description = UtilBean.trim(record.get(DESCRIPTION));
	}//UnitOfMeasurementBean


	public String getDescription() {
		return description;
	}//getDescription

	public int getIdUnitOfMeasurement() {
		return idUnitOfMeasurement;
	}//getIdUnitOfMeasurement
	
    

}//Class UnitOfMeasurementBean
