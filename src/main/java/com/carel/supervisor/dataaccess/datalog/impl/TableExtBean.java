package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class TableExtBean
{
    private static final String IDSITE = "idsite";
    private static final String LANGUAGECODE = "languagecode";
    private static final String TABLENAME = "tablename";
    private static final String TABLEID = "tableid";
    private static final String DESCRIPTION = "description";
    private static final String SHORT_DESCRIPTION = "shortdescr";
    private static final String LONG_DESCRIPTION = "longdescr";
    private int idSite = -1;
    private String languageCode = null;
    private String tableName = null;
    private int tableId = -1;
    private String description = null;
    private String longDescription = null;
    private String shortdescription = null;
    private Timestamp lastUpdate = null;

    public TableExtBean()
    {
    }

    public TableExtBean(Record record)
    {
        idSite = ((Integer) record.get(IDSITE)).intValue();
        languageCode = UtilBean.trim(record.get(LANGUAGECODE));
        tableName = UtilBean.trim(record.get(TABLENAME));
        tableId = ((Integer) record.get(TABLEID)).intValue();
        description = UtilBean.trim(record.get(DESCRIPTION));
        shortdescription = UtilBean.trim(record.get(SHORT_DESCRIPTION));
        longDescription = UtilBean.trim(record.get(LONG_DESCRIPTION));
    }

    /**
     * @return: String
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return: int
     */
    public int getIdSite()
    {
        return idSite;
    }

    /**
     * @param idSite
     */
    public void setIdSite(int idSite)
    {
        this.idSite = idSite;
    }

    /**
     * @return: String
     */
    public String getLanguageCode()
    {
        return languageCode;
    }

    /**
     * @param languageCode
     */
    public void setLanguageCode(String languageCode)
    {
        this.languageCode = languageCode;
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate
     */
    public void setLastUpdate(Timestamp lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return: String
     */
    public String getLongDescription()
    {
        return longDescription;
    }

    /**
     * @param longDescription
     */
    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }

    /**
     * @return: int
     */
    public int getTableId()
    {
        return tableId;
    }

    /**
     * @param tableId
     */
    public void setTableId(int tableId)
    {
        this.tableId = tableId;
    }

    /**
     * @return: String
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @param tableName
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public void save() throws Exception
    {
        Object[] values = new Object[8];
        values[0] = new Integer(idSite);
        values[1] = languageCode;
        values[2] = tableName;
        values[3] = new Integer(tableId);
        values[4] = description;
        values[5] = shortdescription;
        values[6] = longDescription;
        values[7] = new Timestamp(System.currentTimeMillis());

        String insert = "insert into cftableext values (?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
    }

    public void delete() throws Exception
    {
        Object[] values = new Object[4];
        values[0] = new Integer(idSite);
        values[1] = languageCode;
        values[2] = tableName;
        values[3] = new Integer(tableId);

        String sql = "delete from cftableext where idsite = ?, languagecode = ?, tablename = ?, tableid = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public void update() throws Exception
    {
        Object[] values = new Object[8];
        values[0] = description;
        values[1] = longDescription;
        values[2] = shortdescription;
        values[3] = new Timestamp(System.currentTimeMillis());
        values[4] = new Integer(idSite);
        values[5] = languageCode;
        values[6] = tableName;
        values[7] = new Integer(tableId);

        String update = "update cftableext set description = ? , longdescr = ?, shortdescr = ?,  lastupdate = ? where idsite = ? and languagecode = ? and tablename = ? and tableid = ?";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
    }

    public static void removeTableExt(int idsite, String lang, String tablename,
        int tableid) throws Exception
    {
        Object[] values = new Object[4];
        values[0] = new Integer(idsite);
        values[1] = lang;
        values[2] = tablename;
        values[3] = new Integer(tableid);

        String sDelete = "delete from cftableext where idsite=? and languagecode=? and tablename=? and tableid=?";
        DatabaseMgr.getInstance().executeStatement(null, sDelete, values);
    }

    public static void insertTableExt(int idsite, String languageCode,
        String tableName, String description, int tableId)
        throws DataBaseException
    {
        Object[] values = new Object[8];

        values[0] = new Integer(idsite);
        values[1] = languageCode;
        values[2] = tableName;
        values[3] = new Integer(tableId);
        values[4] = description;
        values[5] = null;
        values[6] = null;
        values[7] = new Timestamp(System.currentTimeMillis());

        String sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public static void updateTableExt(int idsite, String languagecode,
        String tablename, int tableid, String newdescr)
        throws DataBaseException
    {
        String sql = "update cftableext set description = ?, lastupdate = ? where idsite = ? and languagecode = ? and tablename = ? and tableid = ?";
        Object[] values = new Object[6];

        values[0] = newdescr;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Integer(idsite);
        values[3] = languagecode;
        values[4] = tablename;
        values[5] = new Integer(tableid);

        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public static String retrieveDescritionFromTableById(int idsite, String lang,
        int tableid, String tablename) throws DataBaseException
    {
        String sql = "select description from cftableext where idsite = ? and languagecode = ? and tablename = ? and tableid = ?";
        Object[] values = new Object[4];
        values[0] = new Integer(idsite);
        values[1] = lang;
        values[2] = tablename;
        values[3] = new Integer(tableid);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, values);

        if (rs.size() == 0)
        {
            return "";
        }
        else
        {
            return rs.get(0).get("description").toString().trim();
        }
    }

    /**
     * @return: String
     */
    public String getShortdescription()
    {
        return shortdescription;
    }

    /**
     * @param shortdescription
     */
    public void setShortdescription(String shortdescription)
    {
        this.shortdescription = shortdescription;
    }
}
