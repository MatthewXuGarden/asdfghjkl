package com.carel.supervisor.field.dataconn;

import java.util.List;

import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.field.IRetriever;
import com.carel.supervisor.field.Variable;

public interface IDataConnector extends IRetriever
{
	public DriverReturnCode loadDllDriver();
    public DriverReturnCode initDriver();
    public DriverReturnCode closeDriver();
    public int setOnField(Variable variable);
    public boolean getDeviceStatus(DeviceInfo deviceInfo);
    @SuppressWarnings("unchecked")
	public void retrieve(List variables);
    //public abstract short retrieve(short globalIndex, short address, short type, ShortValue value);
    //Get e Set for Data Connector Logical Name
    public void setName(String name);
    public String getName();
    public void writeProtocol() throws Exception;
    public void writeProtocol(int[] selectedDeviceid) throws Exception;
}
