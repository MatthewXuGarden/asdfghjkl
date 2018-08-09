package com.carel.supervisor.presentation.defaultconf;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.session.UserSession;

public class Propagher
{
    public static void work(UserSession us,int pFrom, int[] pTo)
    {
        if(pTo != null && pTo.length > 0)
        {
            String sqlVar = "delete from cfgraphconf where idprofile=?";
            String sqlVarBlk = "delete from cfgraphconfblock where idprofile=?";
            String sqlPag = "delete from cfpagegraph where idprofile=?";
            Object[] par = new Object[1];
            
            try
            {
                for(int i=0; i<pTo.length; i++)
                {
                    par[0] = new Integer(pTo[i]);
                    
                    DatabaseMgr.getInstance().executeStatement(null,sqlVar,par);
                    DatabaseMgr.getInstance().executeQuery(null,"select copy_profile_graph_var("+pFrom+","+pTo[i]+");");
                    
                    DatabaseMgr.getInstance().executeStatement(null,sqlVarBlk,par);
                    DatabaseMgr.getInstance().executeQuery(null,"select copy_profile_graph_var_block("+pFrom+","+pTo[i]+");");
                    
                    DatabaseMgr.getInstance().executeStatement(null,sqlPag,par);
                    DatabaseMgr.getInstance().executeQuery(null,"select copy_profile_graph_pag("+pFrom+","+pTo[i]+");");
                }
                
                String lis = getProfilesDesc(pTo);
                if(lis != null && lis.length() > 0)
                	EventMgr.getInstance().info(new Integer(1),us.getUserName(),"Config","W073",new Object[]{lis});
            }
            catch(Exception e) {
                Logger logger = LoggerMgr.getLogger(Propagher.class);
                logger.error(e);
            }
        }
    }
    
    private static String getProfilesDesc(int[] pTo)
    {
    	StringBuffer sb = new StringBuffer();
    	String sql = "select code from profilelist where idprofile in (";
    	Object[] par = new Object[pTo.length];
    	for (int i=0; i<pTo.length; i++) 
    	{
    		par[i] = new Integer(pTo[i]);
    		sql += "?,";
		}
    	sql = sql.substring(0,sql.length()-1);
    	sql += ")";
    	
    	try
    	{
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,par);
    		if(rs != null)
    		{
    			Record r = null;
    			for(int i=0; i<rs.size(); i++)
    			{
    				r = rs.get(i);
    				if(r != null)
    				{
    					if(i!=0)
    						sb.append(",");
    					sb.append(UtilBean.trim(r.get("code")));
    				}
    			}
    		}
    	}
    	catch(Exception e) {
    	}
    	return sb.toString();
    }
}
