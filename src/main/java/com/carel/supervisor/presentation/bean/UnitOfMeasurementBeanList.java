package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class UnitOfMeasurementBeanList
{
    private UnitOfMeasurementBean[] unitOfMeasurementList = null;
    private int size = 0;

    public UnitOfMeasurementBeanList()
    {
    }

    public void loadAllUnitOfMeasurement() throws DataBaseException
    {
        //String sql = "SELECT idunitmeasurement,description FROM unitmeasurement ORDER BY idunitmeasurement";
    	String sql = "SELECT idunitmeasurement,description FROM unitmeasurement ORDER BY description";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql);
        size = recordSet.size();
        if (size > 0){
            unitOfMeasurementList = new UnitOfMeasurementBean[size];
            for (int i = 0; i < size; i++)
                unitOfMeasurementList[i] = new UnitOfMeasurementBean(recordSet.get(i));
        } //if
    } //loadAllUnitOfMeasurement

	public int size() {
		return size;
	}//getSize

	public UnitOfMeasurementBean getUnitOfMeasurement(int i) {
		return unitOfMeasurementList[i];
	}//getUnitOfMeasurementList
    
    public String ComboWithSelectedMeasurement() throws DataBaseException{
    	loadAllUnitOfMeasurement();
    	StringBuffer combo = new StringBuffer();
    	combo.append("<select style='width:100%;' class='standardTxt' id='set_um'>");
    	for(int i=0; i<size; i++)
    	{
    		combo.append("<option value='");
    		combo.append(getUnitOfMeasurement(i).getDescription()+"'>");
    		combo.append(getUnitOfMeasurement(i).getDescription());
    		combo.append("</option>");
    	}
    	combo.append("</select>");
    	return combo.toString();
    }
	
} //Class UnitOfMeasurementBeanList
