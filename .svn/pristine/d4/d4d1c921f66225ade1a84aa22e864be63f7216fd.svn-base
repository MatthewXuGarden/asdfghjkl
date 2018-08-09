package com.carel.supervisor.dataaccess.event;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

import java.util.HashMap;
import java.util.Map;


public class VariableHelper
{
    private VariableHelper()
    {
    }

    public static Map<Integer, String[]> getDescriptions(String language, int idSite, int[] idvar)
    {
        HashMap<Integer, String[]> result = new HashMap<Integer, String[]>();
        RecordSet rs = null;
        Record r = null;
        String sql = "";
        Integer s1 = null;
        String s2 = "";
        String s3 = "";

        sql = "select var.idvariable as uno,tvar.description as due,tdev.description as tre from " +
            "cfvariable as var,cftableext as tvar,cftableext as tdev where " +
            "var.idvariable in (";

        StringBuffer sb = new StringBuffer();
        Object[] param = new Object[idvar.length + 6];
        int idx = 0;

        for (int i = 0; i < idvar.length; i++)
        {
            sb.append("?");

            if (i != (idvar.length - 1))
            {
                sb.append(",");
            }

            param[idx] = new Integer(idvar[i]);
            idx++;
        }

        param[idx++] = new Integer(idSite);
        param[idx++] = language;
        param[idx++] = "cfvariable";
        param[idx++] = new Integer(idSite);
        param[idx++] = language;
        param[idx++] = "cfdevice";

        sql += sb.toString();
        sql += (") and tvar.idsite=? and tvar.languagecode=? and tvar.tablename=? and tvar.tableid=var.idvariable and " +
        "tdev.idsite=? and tdev.languagecode=? and tdev.tablename=? and tdev.tableid=var.iddevice");

        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if (rs != null)
            {
                for (int i = 0; i < rs.size(); i++)
                {
                    r = rs.get(i);

                    if (r != null)
                    {
                        s1 = (Integer) r.get("uno");
                        s2 = UtilBean.trim(r.get("due"));
                        s3 = UtilBean.trim(r.get("tre"));
                        result.put(s1, new String[] { s2, s3 });
                    }
                }
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(VariableHelper.class);
            logger.error(e);
        }

        return result;
    }

    public static String getDeviceAndVariableDesc(String lang, int idsite,
        int id_var) throws DataBaseException
    {
        String sql =
            "select var.idvariable as uno,tvar.description as due,tdev.description as tre from " +
            "cfvariable as var,cftableext as tvar,cftableext as tdev where " +
            "var.idvariable = ? and tvar.idsite=? and tvar.languagecode=? and tvar.tablename=? and tvar.tableid=var.idvariable and " +
            "tdev.idsite=? and tdev.languagecode=? and tdev.tablename=? and tdev.tableid=var.iddevice";

        Object[] param = new Object[7];
        param[0] = new Integer(id_var);
        param[1] = new Integer(idsite);
        param[2] = lang;
        param[3] = "cfvariable";
        param[4] = new Integer(idsite);
        param[5] = lang;
        param[6] = "cfdevice";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        String result = "";
        Record r = null;
        if (rs!=null)
        {
        	r = rs.get(0);
        	result = r.get(2).toString();
        	result = result + " - ";
        	result = result + r.get(1).toString();
        	
        }
        	
        return result;
    }
}
