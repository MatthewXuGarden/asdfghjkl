package com.carel.supervisor.presentation.bo.helper;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.defaultconf.DefGraphVar;
import com.carel.supervisor.presentation.defaultconf.Defaulter;


public class GraphVariable
{
    public static boolean insertVariableGraphInfo(int idsite,int idvariable,int iddevice,String ishaccp,int idprofile) 
    	throws DataBaseException
    {
    	DefGraphVar dgv = Defaulter.checkDefaultForGraphVariable(iddevice,idvariable,ishaccp);
        
        String insert = "insert into cfgraphconf values (?,?,?,?,?,?,?,?,?)";
        Object[] param = new Object[9];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idprofile);
        param[2] = null;
        param[3] = new Integer(iddevice);
        param[4] = ishaccp;
        param[5] = new Integer(idvariable);
        param[6] = GraphConstant.createCiclicColor();
        param[7] = null;
        param[8] = null;
        
        if(dgv.isDefault())
        {
            if(dgv.isVisible())
            {
                param[6] = dgv.getColor();
                param[7] = new Float(dgv.getMin());
                param[8] = new Float(dgv.getMax());
            }
            else
                return false;
        }
        
        DatabaseMgr.getInstance().executeStatement(null, insert, param);
        
        return true;
    }
    
    public static boolean insertVariableGraphInfo(int idsite,int idvariable,int iddevice,String ishaccp,int[] idprofile) 
    	throws DataBaseException
    {
        DefGraphVar dgv = Defaulter.checkDefaultForGraphVariable(iddevice,idvariable,ishaccp);
        
        String insert = "insert into cfgraphconf values (?,?,?,?,?,?,?,?,?)";
        // Default block
        String insertDefault = "insert into cfgraphconfblock values (?,?,?,?,?,?,?)";
        
        Object[] param = new Object[9];
        String randomColor = GraphConstant.createCiclicColor();
        
        for(int i=0; i<idprofile.length; i++)
        {
        	param = new Object[9];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idprofile[i]);
            param[2] = null;
            param[3] = new Integer(iddevice);
            param[4] = ishaccp;
            param[5] = new Integer(idvariable);
            param[6] = randomColor;
            param[7] = null;
            param[8] = null;
        
            if(dgv.isDefault())
            {
                if(dgv.isVisible())
                {
                    param[6] = dgv.getColor();
                    param[7] = new Float(dgv.getMin());
                    param[8] = new Float(dgv.getMax());
                }
                else
                    return false;
            }
            
            DatabaseMgr.getInstance().executeStatement(null,insert,param);
            
            /*
             * DEFAUL BLOCK VARIABLE
             */
            param = new Object[7];
            param[0] = new Integer(idsite);
            param[1] = new Integer(idprofile[i]);
            param[2] = null;
            param[3] = new Integer(iddevice);
            param[4] = ishaccp;
            param[5] = new Integer(idvariable);
            param[6] = "DEFAULT";
            DatabaseMgr.getInstance().executeStatement(null,insertDefault,param);
            // FINE
        }
        return true;
    }

    public static void removeVariableGraphInfo(int idsite,int idprofile,int iddevice,int idvariable,String ishaccp)
    	throws DataBaseException
    {
        String sql = "delete from cfgraphconf where idsite=? and idprofile=? and idvariable=? and ishaccp=?";
        
        Object[] param = new Object[4];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idprofile);
        param[2] = new Integer(idvariable);
        param[3] = ishaccp;
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }
    public static void removeVariableGraphInfo(int idsite,int iddevice,int idvariable,String ishaccp)
	throws DataBaseException
	{
	    String sql = "delete from cfgraphconf where idsite=? and idvariable=? and ishaccp=?";
	    
	    Object[] param = new Object[3];
	    param[0] = new Integer(idsite);
	    param[1] = new Integer(idvariable);
	    param[2] = ishaccp;
	    DatabaseMgr.getInstance().executeStatement(null, sql, param);
	}

    public static void reorderCfPageGrah(int idsite, int idprofile, int iddevice)
    	throws DataBaseException
    {
        //reorder haccp
        List vars_haccp = new ArrayList();
        List vars = new ArrayList();

        String sql = "select idvariable,ishaccp from cfgraphconf where idsite=? and idprofile=? and iddevice=?";
        Object[] param = new Object[3];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idprofile);
        param[2] = new Integer(iddevice);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        for (int i = 0; i < rs.size(); i++)
        {
            if (rs.get(i).get("ishaccp").toString().trim().equalsIgnoreCase("TRUE"))
                vars_haccp.add((Integer) rs.get(i).get("idvariable"));
            else
                vars.add((Integer) rs.get(i).get("idvariable"));
        }

        deleteRowsCfPageGraphOfDevice(idsite, idprofile, iddevice);
        
        insertRowCfPageGRaph(idsite, idprofile, iddevice, vars_haccp, "TRUE");
        insertRowCfPageGRaph(idsite, idprofile, iddevice, vars, "FALSE");

        deleteVarGroupCfPageGraph(idsite, idprofile, iddevice, vars);
        deleteVarGroupCfPageGraph(idsite, idprofile, iddevice, vars_haccp);
    }

    private static void insertRowCfPageGRaph(int idsite, int idprofile,
        int iddevice, List ids_variables, String ishaccp)
        throws DataBaseException
    {
        String insert = "insert into cfpagegraph values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List param = new ArrayList();
        param.add(new Integer(idsite));
        param.add(new Integer(idprofile));
        param.add(null);
        param.add(new Integer(iddevice));
        param.add(ishaccp);
        param.add(new Short((short) 8));

        if (ids_variables.size() > 0)
        {
            for (int i = 0; i < ids_variables.size(); i++)
            {
                if (i == 20)
                {
                    break;
                }

                param.add((Integer) (ids_variables.get(i)));
            }
        }

        if (ids_variables.size() < 20)
        {
            for (int i = ids_variables.size(); i < 20; i++)
            {
                param.add(null);
            }
        }
       
        param.add("TRUE");
        param.add("TRUE");
        param.add("TRUE");
        param.add(GraphConstant.V_FINDER_BG_COLOR);
        param.add(GraphConstant.V_FINDER_FG_COLOR);
        param.add(GraphConstant.GRID_COLOR);
        param.add(GraphConstant.GRAPH_BG_COLOR);
        param.add(GraphConstant.AXIS_COLOR);

        DatabaseMgr.getInstance().executeStatement(null, insert, param.toArray());
    }

    private static void deleteRowsCfPageGraphOfDevice(int idsite,int idprofile,int iddevice)
    	throws DataBaseException
    {
        String sql = "delete from cfpagegraph where idsite=? and idprofile=? and iddevice=?";
        
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[]
            {
                new Integer(idsite), new Integer(idprofile),
                new Integer(iddevice)
            });
    }
    
    public static void insertDeviceGraphVariable(int idsite,int iddevice,int idprofile) 
    	throws DataBaseException
    {
    	insertDeviceGraphVariable(idsite,iddevice,idprofile,true);
    }
    
    public static void insertDeviceGraphVariable(int idsite,int iddevice,int idprofile,boolean otherProf) 
        throws DataBaseException
    {
    	int[] profList = null;
    	if(otherProf)
    	{
    		profList = getProfileList();
            if((profList != null) && (profList.length == 0))
            {
                profList = new int[1];
                profList[0] = idprofile;
            }
    	}
    	else
    	{
    		 profList = new int[1];
             profList[0] = idprofile;
    	}
    	
        int idVariableToInsert = 0;
        String sql = "select idvariable,ishaccp,idhsvariable from cfvariable where idsite=? and iddevice=? order by priority";
        Object[] params = {new Integer(idsite),new Integer(iddevice)};
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
        Record r = null;
        List ids_haccp = new ArrayList();
        List ids_hist = new ArrayList();
        
        // Divido in HACCP e LOG
        for (int i = 0; i < rs.size(); i++)
        {
            r = rs.get(i);
            
            if (r.get("ishaccp").toString().trim().equalsIgnoreCase("TRUE"))
                ids_haccp.add(new Integer(r.get("idvariable").toString()));

            if ((new Integer(r.get("idhsvariable").toString())).intValue() == 0)
                ids_hist.add(new Integer(r.get("idvariable").toString()));
        }

        String insert = "insert into cfpagegraph values " +
                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        /**
         * Preparo parametri
         * HACCP
         */
        List param = new ArrayList();
        param.add(new Integer(idsite));
        param.add(new Integer(idprofile));
        param.add(null);
        param.add(new Integer(iddevice));
        param.add("TRUE");
        param.add(new Short((short) GraphConstant.DAY));

        if (ids_haccp.size() > 0)
        {
            for (int i = 0; i < ids_haccp.size(); i++)
            {
                if (i == 20)
                    break;
                
                idVariableToInsert = ((Integer)ids_haccp.get(i)).intValue();
                
                if(GraphVariable.insertVariableGraphInfo(idsite,idVariableToInsert,iddevice,"TRUE",profList))
                    param.add((Integer)ids_haccp.get(i));
                else
                    param.add(null);
            }
        }

        if (ids_haccp.size() < 20)
        {
            for (int i = ids_haccp.size(); i < 20; i++)
                param.add(null);
        }

        param.add("TRUE");
        param.add("TRUE");
        param.add("TRUE");
        param.add(GraphConstant.V_FINDER_BG_COLOR);
        param.add(GraphConstant.V_FINDER_FG_COLOR);
        param.add(GraphConstant.GRID_COLOR);
        param.add(GraphConstant.GRAPH_BG_COLOR);
        param.add(GraphConstant.AXIS_COLOR);
        
        params = param.toArray();
        for(int i=0; i<profList.length; i++)
        {
            params[1] = profList[i];
            DatabaseMgr.getInstance().executeStatement(null,insert,params);
        }

        
        /**
         * Preparo parametri
         * LOG
         */
        param = new ArrayList();
        param.add(new Integer(idsite));
        param.add(new Integer(idprofile));
        param.add(null);
        param.add(new Integer(iddevice));
        param.add("FALSE");
        param.add(new Short((short) GraphConstant.DAY));
        
        if (ids_hist.size() > 0)
        {
            for (int i = 0; i < ids_hist.size(); i++)
            {
                if (i == 20)
                    break;
                
                idVariableToInsert = ((Integer)ids_hist.get(i)).intValue();
                
                if(GraphVariable.insertVariableGraphInfo(idsite,idVariableToInsert,iddevice,"FALSE",profList))
                    param.add((Integer) (ids_hist.get(i)));
                else
                    param.add(null);
            }
        }

        if (ids_hist.size() < 20)
        {
            for (int i = ids_hist.size(); i < 20; i++)
                param.add(null);
        }

        param.add("TRUE");
        param.add("TRUE");
        param.add("TRUE");
        param.add(GraphConstant.V_FINDER_BG_COLOR);
        param.add(GraphConstant.V_FINDER_FG_COLOR);
        param.add(GraphConstant.GRID_COLOR);
        param.add(GraphConstant.GRAPH_BG_COLOR);
        param.add(GraphConstant.AXIS_COLOR);
        
        params = param.toArray();
        for(int i=0; i<profList.length; i++)
        {
            params[1] = profList[i];
            DatabaseMgr.getInstance().executeStatement(null,insert,params);
        }
    }

    public static void removeGraphVariableOfDevice(int idsite, int iddevice,
        String lang) throws Exception
    {
        //RIMOZIONE DA CFGRAPHCONF (rimozione x idvariabile)
        int[] ids_vars = VarphyBeanList.getIdsOfVarsOfDevice(lang, idsite,
                iddevice);

        StringBuffer sql_buff = new StringBuffer(
                "delete from cfgraphconf where idsite = ? and idvariable in (");
        
        List param = new ArrayList();
        param.add(new Integer(idsite));

        for (int i = 0; i < ids_vars.length; i++)
        {
            param.add(new Integer(ids_vars[i]));
            sql_buff.append("?,");
        }

        String sql = sql_buff.substring(0, sql_buff.length() - 1);
        sql = sql + ")";

        DatabaseMgr.getInstance().executeStatement(null, sql, param.toArray());
        
        /*
         * Remove block label by variable
         * Reuse previous query substituting just table name  
         */
        sql = sql.replaceFirst("cfgraphconf", "cfgraphconfblock");
        DatabaseMgr.getInstance().executeStatement(null, sql, param.toArray());
        // End
        
        //cerco il gruppo a cui appartiene il device
        sql = "select idgroup from cfdevice where idsite = ? and iddevice = ? and iscancelled = 'FALSE'";

        Object[] val = new Object[2];
        val[0] = new Integer(idsite);
        val[1] = new Integer(iddevice);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, val);

        //idgruppo
        int idgroup = ((Integer) rs.get(0).get("idgroup")).intValue();

        //RIMOZIONE DA CFPAGEGRAPH
        sql = "select * from cfpagegraph where idsite = ? and iddevice = ? or idgroup = ?";
        
        val = new Object[3];
        val[0] = new Integer(idsite);
        val[1] = new Integer(iddevice);
        val[2] = new Integer(idgroup);
        
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, val);

        List vars_before = new ArrayList();
        int var_before = 0;
        int var_torem = 0;
        List vars_to_rem = new ArrayList();
        Record tmp = null;

        for (int i = 0; i < rs.size(); i++) //ciclo sulle righe della cfpagegraph interessate
        {
            tmp = rs.get(i);

            for (int j = 1; j < (ConfigurationGraphBeanList.NUM_VARIABLE + 1); j++)
            {
            	if ((tmp.get("idvariable" + j) != null) && (((Integer)(tmp.get("idvariable" + j))).intValue() != 0))
                {
                    vars_before.add((Integer) tmp.get("idvariable" + j));
                }
            }

            for (int j = 0; j < vars_before.size(); j++)
            {
                var_before = ((Integer) vars_before.get(j)).intValue();

                for (int z = 0; z < ids_vars.length; z++)
                {
                    if (var_before == ids_vars[z])
                    {
                        vars_to_rem.add(new Integer(var_before));
                    }
                }
            }

            List vars_after = new ArrayList();
            //vars_after.clear();

            for (int j = 0; j < vars_before.size(); j++) //creazione nuova lista variabili
            {
                var_before = ((Integer) vars_before.get(j)).intValue();

                boolean iscontained = false;

                for (int z = 0; z < vars_to_rem.size(); z++)
                {
                    var_torem = ((Integer) vars_to_rem.get(z)).intValue();

                    if (var_torem == var_before)
                    {
                        iscontained = true;
                        break;
                    }
                }

                if (!iscontained && (var_before != 0))
                {
                    vars_after.add(new Integer(var_before));
                }
            }

            //rimozione vecchi dati
            StringBuffer sql_remove = new StringBuffer(
                    "delete from cfpagegraph where idsite=? and idprofile=? and ");

            List params = new ArrayList();
            params.add(tmp.get("idsite"));
            params.add(tmp.get("idprofile"));

            if (((Integer) tmp.get("idgroup")).intValue() == 0)
            {
                params.add(tmp.get("iddevice"));
                sql_remove.append("iddevice = ? ");
            }
            else
            {
                params.add(tmp.get("idgroup"));
                sql_remove.append("idgroup = ? ");
            }

            sql_remove.append("and ishaccp = ?");

            params.add(UtilBean.trim(tmp.get("ishaccp")));
            
            DatabaseMgr.getInstance().executeStatement(null,
                sql_remove.toString(), params.toArray());

            params.clear();
            params.add(tmp.get("idsite"));
            params.add(tmp.get("idprofile"));

            if (((Integer) tmp.get("idgroup")).intValue() == 0)
            {
                params.add(null);
            }
            else
            {
                params.add(tmp.get("idgroup"));
            }

            if (((Integer) tmp.get("iddevice")).intValue() == 0)
            {
                params.add(null);
            }
            else
            {
                params.add(tmp.get("iddevice"));
            }

            params.add(tmp.get("ishaccp"));
            
            params.add(tmp.get("periodcode"));
            
            if (vars_after.size() > 0)
            {
            	for (int j = 0; j < vars_after.size(); j++)
                {
                    params.add(vars_after.get(j));
                }

                for (int j = vars_after.size(); j < ConfigurationGraphBeanList.NUM_VARIABLE; j++)
                {
                    params.add(null);
                }

                sql = "insert into cfpagegraph values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                
                DatabaseMgr.getInstance().executeStatement(null, sql, params.toArray());
            }
            
            vars_after.clear();
            vars_before.clear();
            vars_to_rem.clear();
        }
    }

    public static void deleteVarGroupCfPageGraph(int idsite, int idprofile,
        int iddevice, List idvars) throws DataBaseException
    {
        if (idvars.size() > 0)
        {
            //cerco il gruppo a cui appartiene il device
            String sql = "select idgroup from cfdevice where idsite = ? and iddevice = ? and iscancelled = 'FALSE'";

            Object[] val = new Object[2];
            val[0] = new Integer(idsite);
            val[1] = new Integer(iddevice);

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, val);

            //idgruppo
            int group = ((Integer) rs.get(0).get("idgroup")).intValue();

            //tiro su riga del gruppo interessato, se c'�
            sql = "select * from cfpagegraph where idsite=? and idprofile=? and idgroup=?";
            
            rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[]
                    {
                        new Integer(idsite), new Integer(idprofile),
                        new Integer(group)
                    });

            List vars_before = null;
            int var_before = 0;
            int var_torem = 0;
            List vars_to_rem = null;
            Record tmp = null;

            for (int i = 0; i < rs.size(); i++) //ciclo sulle righe della cfpagegraph interessate
            {
            	vars_before = new ArrayList();
            	vars_to_rem = new ArrayList();
                tmp = rs.get(i);

                for (int j = 1;
                        j < (ConfigurationGraphBeanList.NUM_VARIABLE + 1);
                        j++)
                {
                	if ((tmp.get("idvariable" + j) != null) && (((Integer)(tmp.get("idvariable" + j))).intValue() != 0))
                    {
                        vars_before.add((Integer) tmp.get("idvariable" + j));
                    }
                }

                for (int j = 0; j < vars_before.size(); j++)
                {
                    var_before = ((Integer) vars_before.get(j)).intValue();

                    for (int z = 0; z < idvars.size(); z++)
                    {
                        if (var_before == ((Integer) idvars.get(z)).intValue())
                        {
                            vars_to_rem.add(new Integer(var_before));
                        }
                    }
                }

                List vars_after = new ArrayList();

                for (int j = 0; j < vars_before.size(); j++) //creazione nuova lista variabili
                {
                    var_before = ((Integer) vars_before.get(j)).intValue();

                    boolean iscontained = false;

                    for (int z = 0; z < vars_to_rem.size(); z++)
                    {
                        var_torem = ((Integer) vars_to_rem.get(z)).intValue();

                        if (var_torem == var_before)
                        {
                            iscontained = true;
                            break;
                        }
                    }

                    if (!iscontained && (var_before != 0))
                    {
                        vars_after.add(new Integer(var_before));
                    }
                }

                //rimozione vecchi dati
                StringBuffer sql_remove = new StringBuffer(
                        "delete from cfpagegraph where idsite=? and idprofile=? and ");

                List params = new ArrayList();
                params.add(tmp.get("idsite"));
                params.add(tmp.get("idprofile"));

                if (((Integer) tmp.get("idgroup")).intValue() == 0)
                {
                    params.add(tmp.get("iddevice"));
                    
                    sql_remove.append("iddevice = ? ");
                }
                else
                {
                    params.add(tmp.get("idgroup"));
                    
                    sql_remove.append("idgroup = ? ");
                }

                sql_remove.append("and ishaccp = ?");

                params.add(UtilBean.trim(tmp.get("ishaccp")));
                
                DatabaseMgr.getInstance().executeStatement(null,
                    sql_remove.toString(), params.toArray());

                params.clear();
                params.add(tmp.get("idsite"));
                params.add(tmp.get("idprofile"));

                if (((Integer) tmp.get("idgroup")).intValue() == 0)
                {
                    params.add(null);
                }
                else
                {
                    params.add(tmp.get("idgroup"));
                }

                if (((Integer) tmp.get("iddevice")).intValue() == 0)
                {
                    params.add(null);
                }
                else
                {
                    params.add(tmp.get("iddevice"));
                }

                params.add(tmp.get("ishaccp"));

                if (((Short) tmp.get("periodcode")).shortValue() == 0)
                {
                    params.add(new Short((short)8));
                }
                else
                {
                    params.add(tmp.get("periodcode"));
                }
                
                if (vars_after.size() > 0)
                {
                    for (int j = 0; j < vars_after.size() && j < (ConfigurationGraphBeanList.NUM_VARIABLE); j++)
                    {
                        params.add(vars_after.get(j));
                    }

                    for (int j = vars_after.size();
                            j < (ConfigurationGraphBeanList.NUM_VARIABLE);
                            j++)
                    {
                        params.add(null);
                    }

                    sql = "insert into cfpagegraph values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,null,null,null,null,null,null,null,null)";
                    
                    DatabaseMgr.getInstance().executeStatement(null, sql, params.toArray());
                }
            }
        }
    }
    
    /**
     * Loads all profile id list
     */
    private static int[] getProfileList()
    {
        int[] pList = new int[0];
        String sql = "select idprofile from profilelist where idprofile >= 0";
        
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
            Record r = null;
            if(rs != null)
            {
                pList = new int[rs.size()];
                for(int i=0; i<rs.size(); i++)
                {
                    r = rs.get(i);
                    if(r != null)
                        pList[i] = ((Integer)r.get("idprofile")).intValue();
                }
            }
        }
        catch(Exception e) {
            pList = new int[0];
        }
        return pList;
    }
    
}
