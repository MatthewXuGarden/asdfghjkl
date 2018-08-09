package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.alarms.AlarmMngTable;


public class AlarmLogList
{
    private int num_alarms_4_page = 50;
    private AlarmLog[] alarmLog = null;
    private Map alarmLogById = new HashMap();
    private int pageNumber = 1;
    private int pageTotal = 1;
    
    public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public AlarmLogList()
    {
    }
    
	public AlarmLogList(String language, String dbId, String pvcode, int idsite,
        boolean isActive, int[] idList, int numPage)
        throws DataBaseException
    {
		buildAlarmLogListWithIds(language, dbId, pvcode, idsite, isActive, idList, numPage);
    }
	
	public AlarmLogList(String language, String dbId, String pvcode, int idsite,
        boolean isActive, int numalarmsforpage, int[] idList, int numPage)
        throws DataBaseException
    {
		this.num_alarms_4_page = numalarmsforpage;
		buildAlarmLogListWithIds(language, dbId, pvcode, idsite, isActive, idList, numPage);
    }
	
	public AlarmLogList(String language, String dbId, String pvcode,boolean isActive,int numPage)
		throws DataBaseException
	{
		buildAlarmLogList(language, dbId, pvcode,isActive,numPage);
	}
	
	public AlarmLogList(String language, String dbId, String pvcode,boolean isActive, int numalarmsforpage, int numPage)
		throws DataBaseException
	{
		this.num_alarms_4_page = numalarmsforpage;
		buildAlarmLogList(language, dbId, pvcode,isActive,numPage);
	}
	
	public void buildAlarmLogListWithIds(String language, String dbId, String pvcode, int idsite,
	        boolean isActive, int[] idList, int numPage)
    		throws DataBaseException
    {
        if ((null != idList) && (0 != idList.length))
        {
            StringBuffer sql = new StringBuffer(
                    "select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable " +
                    "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and " +
                    "cftableext.languagecode = ? and hsalarm.pvcode = ? and hsalarm.idsite = ? ");

            Object[] params = null;

            // filtro sui dispositivi
            if ((null != idList) && (idList.length > 0))
            {
                int dim = idList.length;
                params = new Object[dim + 5];

                sql.append(" and hsalarm.iddevice in (");

                int i = 0;

                for (; i < dim; i++)
                {
                    params[i + 3] = new Integer(idList[i]);
                    sql.append("?");

                    if (i < (dim - 1))
                    {
                        sql.append(",");
                    }
                }

                sql.append(")");
                params[3 + i++] = new Integer(num_alarms_4_page);
                params[3 + i] = new Integer((numPage - 1) * num_alarms_4_page);
            }
            else
            {
                params = new Object[5];
                params[2] = new Integer(num_alarms_4_page);
                params[3] = new Integer((numPage - 1) * num_alarms_4_page);
            }

            params[0] = language;
            params[1] = pvcode;
            params[2] = new Integer(idsite);

            //allarmi attivi o no, sticky o meno...
            sql.append(activeOrRecallCondition(isActive));
            
            if (numPage == 0)
            {
                Object[] paramsTmp = new Object[params.length - 2];

                for (int i = 0; i < paramsTmp.length; i++)
                {
                    paramsTmp[i] = params[i];
                }

                String sqlTmp = "select count(1) as count " +
                    sql.toString().substring(new String("select hsalarm.*, cftableext.description ").length());
                RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                        sqlTmp, paramsTmp);
//                this.pageTotal=(recordSet.size()/NUM_ALARMS_4_PAGE)+1;
                if (recordSet.size() > 0)
                {
                    pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / num_alarms_4_page;
                }
                else
                {
                    pageNumber = 1;
                }

                params[params.length - 1] = new Integer(pageNumber * num_alarms_4_page);
                numPage = pageNumber + 1;
            } //page

            
            Object[] temp = new Object[params.length - 2];

            for (int i = 0; i < temp.length; i++)
            {
            	temp[i] = params[i];
            }

            String sqlTmp = "select count(1) as count " +sql.toString().substring(new String("select hsalarm.*, cftableext.description ").length());
            RecordSet totSet = DatabaseMgr.getInstance().executeQuery(dbId,sqlTmp, temp);
            if (totSet.size() > 0){
            	this.pageTotal = ((Integer) totSet.get(0).get("count")).intValue() / num_alarms_4_page+1;
            }else{
            	this.pageTotal = 1;
            }
            
            sql.append(" order by hsalarm.starttime desc ");
            
            //per la paginazione
            sql.append("limit ? offset ? ");

            RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                    sql.toString(), params);
            Record record = null;
            alarmLog = new AlarmLog[recordSet.size()];

            //METTERE DEI WARNING SE NN TROVO IL LINK ADEGUATO
            for (int i = 0; i < recordSet.size(); i++)
            {
                record = recordSet.get(i);
                alarmLog[i] = new AlarmLog(record, dbId);
                alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
            }
            pageNumber = numPage;
        }
    }
    
	public void buildAlarmLogList(String language, String dbId, String pvcode,boolean isActive,int numPage)
		throws DataBaseException
    {
    	
    	StringBuffer sql = new StringBuffer();
		sql.append("select hsalarm.*,tvar.description as description,tdev.description as device ");
		sql.append("from hsalarm,cftableext as tvar,cftableext as tdev,cfdevice where ");
		sql.append("hsalarm.iddevice = cfdevice.iddevice and cfdevice.iscancelled='FALSE' and ");
		sql.append("tvar.tableid=hsalarm.idvariable and tvar.tablename='cfvariable' and ");
		sql.append("tvar.languagecode=? and tdev.tableid=hsalarm.iddevice and ");
		sql.append("tdev.tablename='cfdevice' and tdev.languagecode=? ");
		sql.append("and tvar.idsite=hsalarm.idsite and tdev.idsite=hsalarm.idsite ");
		
		Object[] params = new Object[4];
		params[0] = language;
        params[1] = language;
        params[2] = new Integer(num_alarms_4_page);
        params[3] = new Integer((numPage - 1) * num_alarms_4_page);
        
        //allarmi attivi o no, sticky o meno...
        sql.append(activeOrRecallCondition(isActive));
        
        if (numPage == 0)
        {
            Object[] paramsTmp = new Object[params.length - 2];

            for (int i = 0; i < paramsTmp.length; i++)
            {
                paramsTmp[i] = params[i];
            }

            String sqlTmp = "select count(1) as count " +
                sql.toString().substring(new String("select hsalarm.*,tvar.description as variabile,tdev.description as device ").length());
            RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                    sqlTmp, paramsTmp);
//            this.pageTotal=(recordSet.size()/NUM_ALARMS_4_PAGE)+1;
            if (recordSet.size() > 0)
            {
                pageNumber = ((Integer) recordSet.get(0).get("count")).intValue() / num_alarms_4_page;
            }
            else
            {
                pageNumber = 1;
            }

            params[params.length - 1] = new Integer(pageNumber * num_alarms_4_page);
            numPage = pageNumber + 1;
        } 

        
        Object[] temp = new Object[params.length - 2];

        for (int i = 0; i < temp.length; i++)
        {
        	temp[i] = params[i];
        }

        String sqlTmp = "select count(1) as count " +
        sql.toString().substring(new String("select hsalarm.*,tvar.description as variabile,tdev.description as device ").length());
        RecordSet totSet = DatabaseMgr.getInstance().executeQuery(dbId,sqlTmp, temp);
        if (totSet.size() > 0){
        	this.pageTotal = ((Integer) totSet.get(0).get("count")).intValue() / num_alarms_4_page+1;
        }else{
        	this.pageTotal = 1;
        }
        
        sql.append(" order by hsalarm.starttime desc ");
        
        sql.append("limit ? offset ? ");

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                sql.toString(), params);
        Record record = null;
        alarmLog = new AlarmLog[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            alarmLog[i] = new AlarmLog(record, dbId);
            alarmLog[i].setDeviceDesc(UtilBean.trim(record.get("device")));
            alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
        }

        pageNumber = numPage;
    }
    
    /*
     * Per la paginazione
     */
    public AlarmLogList(String language, String dbId, String pvcode, int idsite,
        boolean isActive, int[] idList, int numStart, int numEnd)
        throws DataBaseException
    {
        super();

        if ((null != idList) && (0 != idList.length))
        {
        	StringBuffer sql = new StringBuffer(
                    "select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable " +
                    "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and " +
                    "cftableext.languagecode = ? and hsalarm.pvcode = ? and hsalarm.idsite = ? ");

            Object[] params = null;

            // filtro sui dispositivi
            if ((null != idList) && (idList.length > 0))
            {
                int dim = idList.length;
                params = new Object[dim + 3];

                sql.append(" and hsalarm.iddevice in (");

                for (int i = 0; i < dim; i++)
                {
                    params[i + 3] = new Integer(idList[i]);
                    sql.append("?");

                    if (i < (dim - 1))
                    {
                        sql.append(",");
                    }
                }

                sql.append(")");
            }
            else
            {
                params = new Object[3];
            }

            params[0] = language;
            params[1] = pvcode;
            params[2] = new Integer(idsite);

            //allarmi attivi o no, sticky o meno...
            sql.append(activeOrRecallCondition(isActive));


            sql.append(" order by hsalarm.starttime desc");

            RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                    sql.toString(), params, numStart, numEnd);
            Record record = null;
            alarmLog = new AlarmLog[recordSet.size()];

            //METTERE DEI WARNING SE NN TROVO IL LINK ADEGUATO
            for (int i = 0; i < recordSet.size(); i++)
            {
                record = recordSet.get(i);
                alarmLog[i] = new AlarmLog(record, dbId);
                alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
            }
        }
    }
    
    public AlarmLogList(String language, String dbId, String pvcode, int idsite,
            boolean isActive, int[] idList)
            throws DataBaseException
        {
            super();

            if ((null != idList) && (0 != idList.length))
            {
            	StringBuffer sql = new StringBuffer(
                        "select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable " +
                        "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and " +
                        "cftableext.languagecode = ? and hsalarm.pvcode = ? and hsalarm.idsite = ? ");

                Object[] params = null;

                // filtro sui dispositivi
                if ((null != idList) && (idList.length > 0))
                {
                    int dim = idList.length;
                    params = new Object[dim + 3];

                    sql.append(" and hsalarm.iddevice in (");

                    for (int i = 0; i < dim; i++)
                    {
                        params[i + 3] = new Integer(idList[i]);
                        sql.append("?");

                        if (i < (dim - 1))
                        {
                            sql.append(",");
                        }
                    }

                    sql.append(")");
                }
                else
                {
                    params = new Object[3];
                }

                params[0] = language;
                params[1] = pvcode;
                params[2] = new Integer(idsite);

                //allarmi attivi o no, sticky o meno...
                sql.append(activeOrRecallCondition(isActive));


                sql.append(" order by hsalarm.starttime desc");

                RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                        sql.toString(), params);
                Record record = null;
                alarmLog = new AlarmLog[recordSet.size()];

                //METTERE DEI WARNING SE NN TROVO IL LINK ADEGUATO
                for (int i = 0; i < recordSet.size(); i++)
                {
                    record = recordSet.get(i);
                    alarmLog[i] = new AlarmLog(record, dbId);
                    alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
                }
            }
        }

    public void retriveByAlarmId(String language, String dbId, String pvcode, int idsite,
        boolean isActive, int idAlarm) throws DataBaseException
    {
        String sql = 
                "select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable " +
                "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and " +
                "cftableext.languagecode = ? and hsalarm.pvcode = ? and hsalarm.idsite = ? and hsalarm.idalarm = ?";
        Object[] params = new Object[4];
        params[0] = language;
        params[1] = pvcode;
        params[2] = new Integer(idsite);
        params[3] = new Integer(idAlarm);

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                params);
        Record record = null;
        alarmLog = new AlarmLog[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            alarmLog[i] = new AlarmLog(record, dbId);
            alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
        }
    }

    // Insert for Dispatcher
    public void retriveByAlarmsId(String language, String dbId, String pvcode, int idsite,
        int[] idAlarm) throws DataBaseException
    {
        Object[] params = null;
        StringBuffer buffer = new StringBuffer(
                "select hsalarm.*, cftableext.description from hsalarm inner join cftableext on cftableext.tableid=hsalarm.idvariable " +
                "where cftableext.tablename='cfvariable' and cftableext.idsite = hsalarm.idsite and " +
                "cftableext.languagecode = ? and hsalarm.pvcode = ? and hsalarm.idsite = ? ");

        if (idAlarm.length > 0)
        {
            buffer.append("and hsalarm.idalarm in (");
            params = new Object[idAlarm.length + 3];
            params[0] = language;
            params[1] = pvcode;
            params[2] = new Integer(idsite);

            for (int i = 0; i < idAlarm.length; i++)
            {
                if (i < (idAlarm.length - 1))
                {
                    buffer.append("?,");
                }

                params[i + 3] = new Integer(idAlarm[i]);
            }

            buffer.append(")");
        }
        else
        {
            params = new Object[3];
            params[0] = language;
            params[1] = pvcode;
            params[2] = new Integer(idsite);
        }

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                buffer.toString(), params);
        Record record = null;

        alarmLog = new AlarmLog[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            alarmLog[i] = new AlarmLog(record, dbId);
            alarmLogById.put(new Integer(alarmLog[i].getId()), alarmLog[i]);
        }
    }

    public void insert(String dbId, String pvcode, Object[] values)
        throws DataBaseException
    {
        String sInsert = "insert into hsalarm values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        values[1] = pvcode;
        DatabaseMgr.getInstance().executeStatement(null, sInsert, values);
    }

    public void alarmFinished(String dbId, String pvcode, Integer idsite,
        Integer idvariable, Timestamp endtime) throws DataBaseException
    {
        //ricerco l'Id dell'allarme + recente e ne faccio l'Update
        AllarmFinished allarmFinished = new AllarmFinished(dbId, pvcode,
                idsite, idvariable, endtime);
        DatabaseMgr.getInstance().executeCommand(dbId, allarmFinished);
    }

    public int size()
    {
        return alarmLog.length;
    }

    public AlarmLog getByPosition(int pos)
    {
        return alarmLog[pos];
    }

    public AlarmLog getById(int id)
    {
        return (AlarmLog) alarmLogById.get(new Integer(id));
    }

    public int[] getDeviceId()
    {
        if (null == alarmLog)
        {
            return null;
        }

        int[] list = new int[alarmLog.length];

        for (int i = 0; i < alarmLog.length; i++)
        {
            list[i] = alarmLog[i].getIddevice();
        }

        return list;
    }

    public AlarmLog[] getListAlarmsLoaded()
    {
        return this.alarmLog;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public int getTotActiveAlarms(String dbId, String pvcode, int idsite)
        throws DataBaseException
    {
    	//TODO controllare se questa function deve essere modificata per gli sticky alarms o no 
        String sql = "select count(idalarm) as counter from hsalarm where idsite=? "+
        			 "and endtime is null and resettime is null and iddevice in (select iddevice from cfdevice where iscancelled=?)";
        
        Object[] params = {new Integer(idsite),"FALSE"};
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId, sql, params);
        int number = 0;

        if ((rs != null) && (rs.size() > 0))
            number = ((Integer) rs.get(0).get("counter")).intValue();

        return number;
    }
    
    public int getOfflineCounter(int iddevice,Date startTime) throws DataBaseException
    {
        String sql = "select count(*) from hsalarm "+
        		"inner join cfvariable on cfvariable.idvariable=hsalarm.idvariable and hsalarm.idsite=1 and cfvariable.addressin=0 and cfvariable.addressout=0 and cfvariable.type=4 "+
        		"where hsalarm.iddevice=? and (hsalarm.endtime>=? or hsalarm.endtime is null)";

        Object[] params = new Object[]{new Integer(iddevice),new Timestamp(startTime.getTime())};

        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,sql, params);
        if(recordSet != null && recordSet.size()>0)
        {
        	return (Integer)recordSet.get(0).get(0);
        }
        return 0;
    }
    
    private String activeOrRecallCondition(boolean isActive){
    	String sql="";
        if (AlarmMngTable.stickyEnabled())
        {
        	//Modalit� sticky: gli allarmi risultano rientrati solo se sono finiti (end o  reset) e sono ackati
            if (isActive)
            {
            	//attivi se non sono terminati OPPURE se (sono terminati e) l'acktime � nullo
            	sql+=(" and (((hsalarm.endtime is null) and (hsalarm.resettime is null)) or (hsalarm.acktime is null) )");
            }
            else
            {	            	
                sql+=(" and (((hsalarm.endtime is not null) or (hsalarm.resettime is not null)) and (hsalarm.acktime is not null) )");	            	
            }
        }
        else{
            //Modalit� legacy: gli allarmi rientrano quando sono end o reset, indipendentemente da ack
            if (isActive)
            {
                sql+=(" and (hsalarm.endtime is null) and (hsalarm.resettime is null)");
            }
            else
            {
                sql+=(" and ((hsalarm.endtime is not null) or (hsalarm.resettime is not null))");
            }
        }
        return sql;
    }
    
	public void setPageRows(Integer page_rows)
	{
		this.num_alarms_4_page = page_rows;
	}

	public Integer getPageRows()
	{
		return this.num_alarms_4_page;
	}
}
