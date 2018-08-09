package com.carel.supervisor.presentation.defaultconf;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;

public class Defaulter
{
    public static DefGraphVar checkDefaultForGraphVariable(int device,int variable,String haccp)
    {
        DefGraphVar dgv = new DefGraphVar();
        RecordSet rs = null;
        Record r  =null;
        
        String sql = "select master.* " +
                     "from cfdefgraphvar as master, "+
                     "(select b.iddevmdl,b.idvarmdl "+
                     "from cfvariable as a,cfvarmdl as b where "+
                     "a.iddevice=? and a.idvariable=? and a.ishaccp=? and a.idvarmdl=b.idvarmdl) as slave where "+
                     "slave.iddevmdl=master.iddevmdl and slave.idvarmdl=master.idvarmdl and master.ishaccp=?";
        try
        {
            Object[] params = {new Integer(device),new Integer(variable),haccp,haccp};
            rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
            if(rs != null && rs.size() > 0)
            {
                r = rs.get(0);
                if(r != null)
                {
                    dgv.setHaccp((UtilBean.trim(r.get("ishaccp")).equalsIgnoreCase("TRUE"))?true:false);
                    dgv.setVisible((UtilBean.trim(r.get("isvisible")).equalsIgnoreCase("TRUE"))?true:false);
                    dgv.setColor(UtilBean.trim(r.get("colorplot")));
                    dgv.setMin(((Float)r.get("minvalue")).floatValue());
                    dgv.setMax(((Float)r.get("maxvalue")).floatValue());
                    dgv.setOrder(((Integer)(r.get("orderend"))).intValue());
                    dgv.setDefault(true);
                }
            }
        }
        catch(Exception e) {
            Logger logger = LoggerMgr.getLogger(Defaulter.class);
            logger.error(e);
        }
        return dgv;
    }
    
    public static void insertDefaultForNewProfile(int idsite,int idprofile)
    {
    	try 
    	{
    		int[] iddevs = new int[0];
    		String sql = "select iddevice from cfdevice where iscancelled='FALSE'";
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    		if(rs != null)
    		{
    			Record r = null;
    			iddevs = new int[rs.size()];
    			for(int i=0; i<rs.size(); i++)
    			{
    				r = rs.get(i);
    				if(r != null)
    				{
    					iddevs[i] = ((Integer)r.get("iddevice")).intValue();
    				}
    			}
    		}
    		
    		for(int i=0; i<iddevs.length; i++) {
    			GraphVariable.insertDeviceGraphVariable(idsite, iddevs[i], idprofile, false);
    		}
		} 
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(Defaulter.class);
            logger.error(e);
		}
    }
}
