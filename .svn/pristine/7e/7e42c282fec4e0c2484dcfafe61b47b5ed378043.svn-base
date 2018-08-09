package com.carel.supervisor.presentation.io;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dispatcher.comm.layer.DispLayRas;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;
import com.carel.supervisor.remote.bean.IncomingBean;
import com.carel.supervisor.remote.bean.IncomingBeanList;


public class CioRAS
{
	private int idsite = 0;
	
    public CioRAS(int idsite)
    {
        this.idsite = idsite;
    }

    public String[][] getModem()
    {
        DispLayer layer = new DispLayRas();
        return layer.getFisicChannel("R");
    }
    
    public String[] getConfigModem()
    {
    	IncomingBean[] list = IncomingBeanList.getEnableIncomingDevice(null);
    	String[] ret = new String[0];
    	
    	if(list != null)
    	{
    		ret = new String[list.length];
    		for(int i=0; i<list.length; i++)
    			ret[i] = list[i].getIdModem();
    	}
    	return ret;
    }
	public void setTestResult(int idrelay, boolean isTestSuccessful)
	{
		String sql = "update cfrelay set ioteststatus=? where idrelay=?";
		Object[] values = null;
		try
		{
			if(isTestSuccessful == true)
			{
				values = new Object[]{"OK",new Integer(idrelay)};	
			}
			else
			{
				values = new Object[]{"FAIL", new Integer(idrelay)};
			}
			DatabaseMgr.getInstance().executeStatement(null, sql,values);
		}
		catch (DataBaseException e)
		{
			e.printStackTrace();
		}
	}
}
