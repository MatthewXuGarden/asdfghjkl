package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.enhanced.SyncLog;
import com.carel.supervisor.dispatcher.enhanced.SyncRecNodeConfiguration;
import com.carel.supervisor.dispatcher.enhanced.SyncRecord;


public class ImpEnanchedDevice
{
    public ImpEnanchedDevice()
    {
    }

    public void importer(SyncLog syncLog, Vector Alarms, Integer idSite, String languagecode)
        throws Exception
    {
    	
    	insertGlobalsText(idSite.intValue(),languagecode);
    	
        Map nc = syncLog.GetNodeConfiguration();
        Set mappings = nc.entrySet();
        Integer idLine = null;
        Integer address = null;
        Integer globalindex = null;
        String code = null;
        String description = null;
        String sql = "";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Map iddeviceMap = new HashMap();
        Map iddevMapVar = new HashMap();
        extractDeviceSite(idSite, iddeviceMap, iddevMapVar);

        Integer iddevice = null;

        for (Iterator i = mappings.iterator(); i.hasNext();)
        {
            Map.Entry me = (Map.Entry) i.next();
            Object ov = me.getValue();
            SyncRecNodeConfiguration obj = (SyncRecNodeConfiguration) ov;
            idLine = new Integer(obj.Line);
            address = new Integer(obj.SerialIdent);
            globalindex = new Integer((int) obj.GlobalIdent);
            code = ""+ idLine + "." + new DecimalFormat("000").format(address.longValue());//obj.UnitType;
            description = obj.UnitDescription;

            if (iddeviceMap.containsKey(globalindex)) //update
            {
                sql = "update cfdevice set idline=?, address=?,code=?,lastupdate=current_timestamp where idsite=? and iddevice=?";
                iddevice = (Integer) iddeviceMap.get(globalindex);

                Object[] params = new Object[5];
                params[0] = idLine;
                params[1] = address;
                params[2] = code;
                params[3] = idSite;
                params[4] = iddevice;
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
                sql = "update cftableext set description = ?, lastupdate=? where idsite=? and languagecode=? and tablename=? and tableid=?";
                params = new Object[6];
                params[0] = description;
                params[1] = now;
                params[2] = idSite;
                params[3] = languagecode;
                params[4] = "cfdevice";
                params[5] = iddevice;
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
                iddeviceMap.remove(globalindex);
            }
            else //insert
            {
                sql = "insert into cfdevice values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                iddevice = SeqMgr.getInstance().next(null, "cfdevice", "iddevice");
                iddevMapVar.put(globalindex,iddevice);
                Object[] params = new Object[16];
                params[0] = iddevice;
                params[1] = "firstPV";
                params[2] = idSite;
                params[3] = "FALSE";
                params[4] = null;
                params[5] = idLine;
                params[6] = address;
                params[7] = "TRUE";
                params[8] = code;
                params[9] = null; //LDAC TO DO : imagepath
                params[10] = new Integer(1); //IDGROUP
                params[11] = globalindex;
                params[12] = "TRUE";
                params[13] = "FALSE";
                params[14] = now;
                params[15] = now;
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
                sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
                params = new Object[8];
                params[0] = idSite;
                params[1] = languagecode;
                params[2] = "cfdevice";
                params[3] = iddevice;
                params[4] = description;
                params[5] = null;
                params[6] = null;
                params[7] = now;
                DatabaseMgr.getInstance().executeStatement(null, sql, params);
            }

            //tento insert in cfdevice. Se va male = faccio update
            //e di conseguenza faccio insert o update in cftableext
        }

        for (Iterator i = iddeviceMap.keySet().iterator(); i.hasNext();)
        {
            globalindex = (Integer) i.next();
            iddevice = (Integer) iddeviceMap.get(globalindex);
            sql = "update cfdevice set iscancelled='TRUE' where idsite=? and iddevice=?";
            DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idSite, iddevice });
        }

        //Gestione variabili e allarmi
        Timestamp alrDate = null;
        Integer varAddress = null;
        String alrDescription = null;
        SyncRecord rec = null;
        boolean start = false;

        //Devo fare l'insert in cfvariable (se non è presente)
        //Insert o update in cftableext
        //insert o update (dell'ultimo) su hsalarm
        Map varList = extractVar(idSite); //Ho la lista  delel variabili in funzione di 

        // globalindex device e indirizzo variabile
        Map subMap = null;
        Integer idVar = null;

        for (int i = 0; i < Alarms.size(); i++)
        {
            rec = (SyncRecord) Alarms.get(i);

            // Se restart setto l'ENDTIME di tutti gli allarmi di quel sito
            if(rec.isRestart)
            {
            	alrDate = new Timestamp(rec.AlrDate.getTime());
            	try {
            		updateToEndAlarmsTime(idSite,alrDate);
            	}
            	catch(Exception e) {
            		Logger logger = LoggerMgr.getLogger(this.getClass());
            		logger.error(e);
            	}
            	continue;
            }
            
            alrDescription = rec.AlarmDescription;
            alrDate = new Timestamp(rec.AlrDate.getTime());
            globalindex = new Integer((int) rec.GlobalIdent);
            varAddress = new Integer((int) rec.VarAddress);
            iddevice = (Integer) iddevMapVar.get(globalindex);
            start = rec.StartEnd;

            //verifico se variabile già presente:
            subMap = (Map) varList.get(globalindex);
            
            if (null == subMap)
            {
                //Prima volta per tale dispositivo
                idVar = SeqMgr.getInstance().next(null, "cfvariable", "idvariable");
            }
            else
            {
                idVar = (Integer) subMap.get(varAddress);
            }

            if ((null == subMap) || (null == idVar))
            {
                //Significo che devo fare insert in cfvariable e cftableext
                idVar = SeqMgr.getInstance().next(null, "cfvariable", "idvariable");
                sql = "insert into cfvariable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                Object[] values = new Object[]
                    {
                        idVar, "firstPV", idSite, iddevice, "FALSE", null, null, "enanched",
                        new Integer(4), varAddress, varAddress, new Integer(8), new Integer(8),
                        new Integer(0), "TRUE", new Integer(0), "NONE", null, new Integer(1), "1",
                        null, null, null, "", new Integer(1), null, null, new Integer(0),
                        new Integer(-1), new Integer(0), "FALSE","FALSE", "TRUE", "FALSE", null,null,new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis())
                    };
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
                sql = "insert into cftableext values (?,?,?,?,?,?,?,?)";
                values = new Object[]
                    {
                        idSite, languagecode, "cfvariable", idVar, alrDescription, null, null,
                        new Timestamp(System.currentTimeMillis())
                    };
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
                subMap = new HashMap();
                subMap.put(varAddress, idVar);
                varList.put(globalindex, subMap);
            }
            else
            {
                //Già presente => faccio update
                sql = "update cftableext set description = ? where idsite=? and languagecode=? and tablename=? and tableid=?";

                Object[] values = new Object[]
                    {
                        alrDescription, idSite, languagecode, "cfvariable", idVar
                    };
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
            }

            if (start) //Faccio insert
            {
                //devo ricercare l'idvariable
                Integer idAlarm = SeqMgr.getInstance().next(null, "hsalarm", "idalarm");
                sql = "insert into hsalarm values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                Object[] values = new Object[]
                    {
                        idAlarm, "firstPV", idSite, iddevice, idVar, new Integer(1), "FALSE",
                        alrDate, null, null, null, null, null, null, null,
                        new Timestamp(System.currentTimeMillis())
                    };
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
            }
            else //Faccio update cercando l'ultimo compatibile
            {
                sql = "select max(idalarm) from hsalarm where pvcode = 'firstPV' and " +
                    "idsite = ? and idvariable = ? and endtime is null";

                Object[] values = new Object[] { idSite, idVar };
                RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, values);

                if ((null == recordset) || (0 == recordset.size()))
                {
                    //NON dovrebbe mai entrare qui
                }
                else
                {
                    Integer id = (Integer) recordset.get(0).get(0);
                    sql = "update hsalarm set endtime=? where idalarm=? and idsite=?";
                    values = new Object[] { alrDate, id, idSite };
                    DatabaseMgr.getInstance().executeStatement(null, sql, values);
                }
            }
        }
    }
    
    private void updateToEndAlarmsTime(Integer idSite,Timestamp endTime)
    	throws DataBaseException
    {
    	String sql = "update hsalarm set endtime=? where idsite=? and endtime is null";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{endTime,idSite});
    }
    
    private Map extractVar(Integer idSite) throws Exception
    {
        Map varList = new HashMap();
        String sql = "select cfvariable.idvariable, cfvariable.addressin, cfdevice.globalindex " +
            " from cfvariable inner join cfdevice on cfdevice.iddevice=cfvariable.iddevice where cfvariable.idsite=?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite });
        Record r = null;
        Integer idvar = null;
        Integer addressin = null;
        Integer globalindex = null;
        Map subMap = null;

        for (int i = 0; i < recordset.size(); i++)
        {
            r = recordset.get(i);
            idvar = (Integer) r.get(0);
            addressin = (Integer) r.get(1);
            globalindex = (Integer) r.get(2);
            subMap = (Map) varList.get(globalindex);

            if (null == subMap)
            {
                subMap = new HashMap();
                varList.put(globalindex, subMap);
            }

            subMap.put(addressin, idvar);
        }

        return varList;
    }

    private void extractDeviceSite(Integer idSite, Map map1, Map map2)
        throws Exception
    {
        String sql = "select globalindex,iddevice from cfdevice where idsite=?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite });
        Record record = null;
        Integer global = null;
        Integer iddevice = null;

        if (null != recordset)
        {
            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                global = (Integer) record.get(0);
                iddevice = (Integer) record.get(1);
                map1.put(global, iddevice);
                map2.put(global, iddevice);
            }
        }
    }
    
    private void insertGlobalsText(int idSite,String languagecode)
    	throws Exception
	{
		String sql = "select * from cftableext where idsite=? and languagecode=? and tablename=? and tableid=?";
		Object[] param = {new Integer(1),languagecode,"cfarea",new Integer(1)};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
		Record r  = null;
		
		String desc = "Global";
		String lang = "EN_en";
		
		if(rs != null && rs.size() > 0)
			r = rs.get(0);
		if(r != null)
		{
			desc = UtilBean.trim(r.get("description"));
			lang = UtilBean.trim(r.get("languagecode"));
		}
		
		param = new Object[]{new Integer(idSite),lang,"cfarea",new Integer(1),desc,null,null,new Timestamp(System.currentTimeMillis())};
		
		sql = "insert into cftableext (idsite,languagecode,tablename,tableid,description,shortdescr,longdescr,lastupdate) "+
			  "values (?,?,?,?,?,?,?,?)";
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,param,false);
		}
		catch(Exception e){
			
		}
		param[2] = "cfgroup";
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,param,false);
		}
		catch(Exception e){
		}
	}
    
    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();

        SyncLog sl = new SyncLog("10.0.0.108", 21, "PVRemote", "CAREL4");

        Vector Alarms = new Vector();

        if (sl.DoAlign(Alarms) && (Alarms != null))
        {
            ImpEnanchedDevice imp = new ImpEnanchedDevice();
            imp.importer(sl, Alarms, new Integer(1), "IT_it");
        }
        sl.Finalize();
    }
}
