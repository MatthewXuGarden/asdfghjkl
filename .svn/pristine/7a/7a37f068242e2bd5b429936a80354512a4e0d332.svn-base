package com.carel.supervisor.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ProfileMigrationTest {

	public static final int FUNCT_CONSTR_PARAM = 1;
    public static final int FUNCT_SERV_PARAM = 2;
	/**
	 * @param args
	 * @throws Exception 
	 */
    
    /*
    METHOD to USE in SP 2.0 
    */
    
	public static void main(String[] args) throws Exception {

		String sql_select = "select * from profilelist";
		String sql_update = "update profilelist set status=? where idprofile=?";
		String old_entry = "";
		String new_entry = "";
		String[] split_1st = null;
		String[] split_2nd = null;
		Integer idprofile = null;
		Integer service_right = 0;
		Integer manufacturer_right = 0;
		Integer filter = 0;
				
		try {
			
			BaseConfig.init();
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql_select);
			if (rs!=null && rs.size()>0)
			{
				for (int i=0;i<rs.size();i++)
				{
					idprofile = (Integer) rs.get(i).get("idprofile");
					old_entry = (String) rs.get(i).get("status");
					
					if (old_entry.contains("-"))
					{
						split_1st = old_entry.split("-");
						split_2nd = split_1st[0].split(";");
					}
					else
					{
						split_2nd = old_entry.split(";");
					}
					
					service_right = Integer.parseInt(split_2nd[FUNCT_SERV_PARAM].split("=")[1]);
					manufacturer_right = Integer.parseInt(split_2nd[FUNCT_CONSTR_PARAM].split("=")[1]);
					
					if (service_right!=2)  //read only services
					{
						filter = 0;
					}
					else if (manufacturer_right !=2) // r/w services
					{
						filter = 1;
					}
					else  // r/w services & manufacturer
					{
						filter = 2;
					}
					
					new_entry = filter+"-"+(split_1st!=null?split_1st[1]:"")+"-"+(split_1st!=null?split_1st[2]:"");
					DatabaseMgr.getInstance().executeStatement(null,sql_update,new Object[]{new_entry,idprofile});
				}
				
			}
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		

	}

}
