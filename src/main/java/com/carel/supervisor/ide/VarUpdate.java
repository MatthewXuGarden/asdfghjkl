package com.carel.supervisor.ide;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.presentation.bean.LineBeanList;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.helper.RemoveState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public class VarUpdate
{
    private static final String CODE = "Code";

    
    public static void removeVariable(Integer idvarmdl, String lang)
        throws DataBaseException
    {
        //set iscancelled=TRUE su cfvariable
        String sql = "update cfvariable set iscancelled=? where idsite=? and idvarmdl=?";
        Object[] param = new Object[3];
        param[0] = "TRUE";
        param[1] = new Integer(1);
        param[2] = idvarmdl;
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        //remove from cfvarmdl
        sql = "delete from cfvarmdl where idvarmdl=?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { idvarmdl });
        


    }

    

    public static String verifyVariablesDependencies(List ids_varmdl,
        String language) throws DataBaseException
    {
        StringBuffer sql = new StringBuffer(
                "select idvariable from cfvariable where idvarmdl in (");
        Object[] param = new Object[ids_varmdl.size()];

        for (int i = 0; i < ids_varmdl.size(); i++)
        {
            sql.append("?,");
            param[i] = (Integer) ids_varmdl.get(i);
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(") and iscancelled='FALSE' and idhsvariable is not null");
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                sql.toString(), param);

        if (rs.size() > 0)
        {
            int[] ids_var = new int[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids_var[i] = ((Integer) rs.get(i).get("idvariable")).intValue();
            }

            RemoveState rstate = LineConfig.verifyVariableDependence(1,
                    ids_var, language);

            if (rstate.getCanRemove())
            {
                return "OK";
            }
            else
            {
                return rstate.getMessage();
            }
        }
        else
        {
            return "OK";
        }
    }
}
