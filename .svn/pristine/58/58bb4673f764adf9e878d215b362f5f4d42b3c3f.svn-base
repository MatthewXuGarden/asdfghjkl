package com.carel.supervisor.presentation.co2;

import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.presentation.bean.DeviceBean;


public class UtilityBean {
	private DeviceBean beanUtility;
	private String var1Code;
	private String var2Code;
	private int idGroup;

	
	public UtilityBean(DeviceBean beanUtility, String var1Code, String var2Code, int idGroup)
	{
		this.beanUtility = beanUtility;
		this.var1Code = var1Code;
		this.var2Code = var2Code;
		this.idGroup = idGroup;
	}
	
	
	public int getIdGroup()
	{
		return idGroup;
	}
	
	
	public int getIdDevice()
	{
		return beanUtility.getIddevice();
	}
	
	
	public String getDescription()
	{
		return beanUtility.getDescription();
	}
	
	
	public static UtilityBean[] retrieveUtilities(String language) throws DataBaseException
	{
		String sql = "select cfdevice.*,cftableext.description,co2_devmdl.var1,co2_devmdl.var4 from cfdevice,cftableext,co2_devmdl"
			+ " where cfdevice.iddevmdl in (select iddevmdl from cfdevmdl where code in"
			+ " (select devcode from co2_devmdl where israck=FALSE)) and cfdevice.iscancelled='FALSE' and cftableext.tablename='cfdevice'"
			+ " and cftableext.languagecode=? and cftableext.tableid=cfdevice.iddevice and cftableext.idsite=1"
			+ " and co2_devmdl.devcode=(select code from cfdevmdl where iddevmdl=cfdevice.iddevmdl) order by cfdevice.iddevice";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { language });
        
		UtilityBean[] utilities = new UtilityBean[rs.size()];
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
			DeviceBean beanDevice = new DeviceBean(r, language);
			utilities[i] = new UtilityBean(
				beanDevice,
				r.get("var1").toString(),
				(String)r.get("var4"),
				getIdGroup(beanDevice.getIddevice()));
		}

		return utilities;
	}
	
	
    private static int getIdGroup(int idDevice) throws DataBaseException
    {
    	String sql = "SELECT idgroup FROM co2_grouputilities WHERE iddevice=?;";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice });
    	if( rs.size() > 0 )
    		return (Integer)rs.get(0).get(0);
    	else
    		return 0;
    }
}
