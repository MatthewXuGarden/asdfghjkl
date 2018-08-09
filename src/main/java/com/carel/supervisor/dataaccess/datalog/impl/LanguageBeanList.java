package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.util.*;


public class LanguageBeanList
{
    private static final String TB = "cftableext";
    private String table = "";
    private String language = "";
    private int idSite = -1;

    public LanguageBeanList(String language, int idSite, String tableName)
    {
        this.table = tableName;
        this.language = language;
        this.idSite = idSite;
    }

    public LanguageBean[] getListDescription(int id) throws Exception
    {
        return getListDescription(new int[] { id });
    }

    public LanguageBean[] getListDescription(int[] ids)
        throws Exception
    {
        if ((null == ids) || (0 == ids.length))
        {
            return null;
        }

        List tmp = new ArrayList();
        LanguageBean[] rows = null;
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                createQuery(ids.length), createParamList(ids));

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                tmp.add(new LanguageBean(rs.get(i)));
            }

            rows = new LanguageBean[tmp.size()];

            for (int i = 0; i < rows.length; i++)
            {
                rows[i] = (LanguageBean) tmp.get(i);
            }
        }

        return rows;
    }

    private Object[] createParamList(int[] id)
    {
        List values = new ArrayList();
        values.add(new Integer(this.idSite));
        values.add(this.language);
        values.add(this.table);

        for (int i = 0; i < id.length; i++)
        {
            values.add(new Integer(id[i]));
        }

        Object[] param = new Object[values.size()];

        for (int i = 0; i < param.length; i++)
        {
            param[i] = values.get(i);
        }

        return param;
    }

    private String createQuery(int counter)
    {
        StringBuffer sql = new StringBuffer("");

        sql.append(
            "select tableid,description,shortdescr,longdescr from"+
            " " + TB + " where"+
        	" idsite = ? and"+
        	" languagecode = ? and"+
        	" tablename = ? and"+
        	" tableid in (");

        for (int i = 0; i < counter; i++)
        {
            if (i == 0)
            {
                sql.append("?");
            }
            else
            {
                sql.append(",?");
            }
        }

        sql.append(")");

        return sql.toString();
    }

    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        LanguageBeanList lb = new LanguageBeanList("it", 1, "cfvariable");
        LanguageBean[] lbr = lb.getListDescription(new int[] { 560, 561 });

        for (int i = 0; i < lbr.length; i++)
        {
            System.out.println(lbr[i].getId() + " - " +
                lbr[i].getLongDescription());
        }
    }
}
