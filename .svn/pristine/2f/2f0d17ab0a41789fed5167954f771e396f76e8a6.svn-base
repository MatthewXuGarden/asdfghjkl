package com.carel.supervisor.presentation.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.helper.VarDependencyState;
import com.carel.supervisor.presentation.groupmng.GroupManager;
import com.carel.supervisor.presentation.session.UserSession;


public class LogicDeviceBeanList
{
    private LogicDeviceBean[] logicDeviceList = null;
    private int size = 0;

    public LogicDeviceBeanList()
    {
    }

    public synchronized void loadDeviceComplete(String dbId, int site, String language)
        throws Exception
    {
        StringBuffer sql = new StringBuffer();
        Object[] objects = new Object[]
            {
                "cfdevice", language, new Integer(site), new Integer(site), new Integer(site),
                "FALSE", "TRUE", "FALSE"
            };
        sql.append(
            "SELECT cfdevice.iddevice,cftableext.description, count(cfvariable.idvariable) as numvar ");
        sql.append("FROM   cfdevice,cftableext,cfvariable ");
        sql.append("WHERE  cfdevice.iddevice=cftableext.tableid "); //primo join
        sql.append("AND    cftableext.tablename=? ");
        sql.append("AND    cftableext.languagecode = ? ");
        sql.append("AND    cftableext.idsite = ? ");
        sql.append("AND    cfdevice.iddevice=cfvariable.iddevice "); //secondo join
        sql.append("AND    cfdevice.idsite = ? ");
        sql.append("AND    cfvariable.idsite = ? ");
        sql.append("AND    cfdevice.iscancelled = ? ");
        sql.append("AND    cfdevice.islogic=?");
        sql.append("AND    cfvariable.iscancelled = ? ");
        sql.append("AND    cfvariable.idhsvariable is not null  ");
        sql.append("GROUP BY cfdevice.iddevice,cftableext.description ");
        
        sql.append("ORDER BY cftableext.description ");

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId, sql.toString(), objects);

        if (rs.size() > 0)
        {
            size = rs.size();

            logicDeviceList = new LogicDeviceBean[size];

            Record record = null;

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                logicDeviceList[i] = new LogicDeviceBean(record);
            } //for
        } //if
    } //LogicDeviceBeanList

    public synchronized void loadDeviceDescription(String dbId, int site, String language)
	    throws Exception {
	    loadDeviceDescription(dbId, site, language, true);	
    }
    
    public synchronized void loadDeviceDescription(String dbId, int site, String language,boolean filter)
        throws Exception
    {
        StringBuffer sql = new StringBuffer();
        Object[] objects;
        if(filter){
        	objects = new Object[]{"cfdevice", language, new Integer(site), new Integer(site), "FALSE", "FALSE"};
        }else{
        	objects = new Object[]{"cfdevice", language, new Integer(site), new Integer(site), "FALSE"};
        }
        sql.append("SELECT DISTINCT cfdevice.iddevice,cftableext.description ");
        sql.append("FROM   cfdevice,cftableext ");
        sql.append("WHERE  cfdevice.iddevice=cftableext.tableid ");
        sql.append("AND    cftableext.tablename = ? ");
        sql.append("AND    cftableext.languagecode = ? ");
        sql.append("AND    cftableext.idsite =? ");
        sql.append("AND    cfdevice.idsite = ? ");
        sql.append("AND    cfdevice.iscancelled = ? ");
        if(filter){
        	sql.append("AND    cfdevice.islogic=? ");
        }
        sql.append("ORDER BY cftableext.description");

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId, sql.toString(), objects);

        if (rs.size() > 0)
        {
            size = rs.size();

            logicDeviceList = new LogicDeviceBean[size];

            Record record = null;

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                logicDeviceList[i] = new LogicDeviceBean(record);
            } //for
        } //if
    } //LogicDeviceBeanList

    public static synchronized boolean deleteDevice(UserSession us,int idDevice)
        throws Exception
    {
    	int idsite = us.getIdSite();
    	String language = us.getLanguage();
    	VarDependencyState state = LineConfig.checkDeviceDependence(idsite, idDevice, LineConfig.ALL_CHECK, language,true);
    	LangService lang_s = LangMgr.getInstance().getLangService(language);
        if (state.dependsOn() == false)
        {
	        StringBuffer sql = new StringBuffer();
	        Object[] objects = new Object[] { "TRUE", new Integer(idDevice) };
	        sql.append("UPDATE cfdevice set iscancelled=? where iddevice=? and idsite=1");
	        DatabaseMgr.getInstance().executeStatement(null, sql.toString(), objects);
	        sql = new StringBuffer();
	        sql.append("UPDATE cfvariable set iscancelled=? where iddevice=? and idvarmdl is not null and idsite=1");
	        DatabaseMgr.getInstance().executeStatement(null, sql.toString(), objects);
	        //2010-2-9, will not delete logic variable
	        sql = new StringBuffer();
	        sql.append("UPDATE cfvariable set iddevice = NULL,idhsvariable=-1,ishaccp='FALSE' where iddevice=? and idvarmdl is  null and idsite=1");
	        objects = new Object[]{new Integer(idDevice)};
	        DatabaseMgr.getInstance().executeStatement(null, sql.toString(), objects);
	        
	        GroupManager groupMg = new GroupManager();
            GroupBean[] groups = new GroupListBean().retrieveAllGroupsNoGlobal(idsite,language);
            for (int i = 0; i < groups.length; i++)
            {
                if (groupMg.numOfDeviceOfGroup(idsite, groups[i].getGroupId()) == 0)
                {
                    groupMg.removeEmptyGroup(idsite, groups[i].getGroupId());
                }
            }
            
	        return true;
        }
        else
        {
        	String str =  lang_s.getString("importctrl", "nooperation")+"\n";
        	str += state.getMessagesAsString();
        	us.setProperty("control", str);

            return false;
        }
    } //deleteDevice

    public static synchronized void addDevice(UserSession userSession, Properties properties)
        throws Exception
    {
        Connection connection = DatabaseMgr.getInstance().getConnection(null);

        try
        {
            PreparedStatement preparedStatement = null;
            SeqMgr seqMgr = SeqMgr.getInstance();
            Integer idDevice = seqMgr.next(null, "cfdevice", "iddevice");
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            String pvCode = BaseConfig.getPlantId();
            Integer idSite = new Integer(userSession.getIdSite());

            //****************************** CFDEVICE ******************************//
            //"iddevice","pvcode","idsite","islogic","iddevmdl","idline", "address" // 
            // "littlendian", "code", "imagepath", "idgroup", "globalindex"         //
            // "isenabled" , "iscancelled", "inserttime", "lastupdate"              //
            //**********************************************************************//
            Object[] objects = new Object[]
                {
                    idDevice, pvCode, idSite, "TRUE", null, null, null, "TRUE", idDevice, null,
                    new Integer(1), null, "TRUE", "FALSE", currentTime, currentTime
                };
            String sql = "insert into cfdevice values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < objects.length; i++)
            {
                preparedStatement.setObject(i + 1, objects[i]);
            }

            preparedStatement.execute();

            //**************************** CFTABLEXT *******************************//
            // "idsite" , "languagecode DA SELECT","tablename", "tableid"           //  			
            // "description" ,"shortdescr" ,"longdescr","lastupdate"                //
            //**********************************************************************//
            objects = new Object[]
                {
                    idSite, "cfdevice", idDevice, (String) properties.get("input0"), null, null,
                    currentTime
                };

            sql = "INSERT INTO cftableext SELECT ?,languagecode,?,?,?,?,?,? from cfsiteext;";

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < objects.length; i++)
            {
                preparedStatement.setObject(i + 1, objects[i]);
            }

            preparedStatement.execute();

            String[] variablesInformations = ((String) properties.get("variableTable")).split("@");
            Integer idVariableMaster = null;
            String descriptionVariable = null;
            boolean isLogic = false;

            for (int i = 0; i < variablesInformations.length; i += 6)
            {
                idVariableMaster = new Integer(variablesInformations[i]);
                descriptionVariable = variablesInformations[i + 1];
                isLogic = (new Integer(variablesInformations[i + 5]).intValue() == 1) ? true : false;
                addVariableInLogicDevice(connection, pvCode, idSite, idDevice, idVariableMaster,
                    descriptionVariable, isLogic, currentTime);
            } //for

            connection.commit();
            DatabaseMgr.getInstance().closeConnection(null, connection);
        } //try
        catch (Exception e)
        {
            e.printStackTrace();
            DatabaseMgr.getInstance().closeConnection(null, connection);
        } //catch
    } //addDevice

    public static synchronized boolean modifyDevice(UserSession userSession, Properties properties)
        throws Exception
    {
        String[] deleteList = new String[0];
        String deleteStr = (String) properties.get("variableToDeleteInModify");
        if(deleteStr.trim().length()>0)
        	deleteList = deleteStr.split("@");
        String[] addList = ((String) properties.get("variableToAddInModify")).split("@");
        Integer idDevice = new Integer((String) properties.getProperty("idDevice"));
        String deviceDesc = (String) properties.get("input0");
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String lang = userSession.getLanguage();
        LangService lang_s = LangMgr.getInstance().getLangService(lang);

        //String sql=null;
        Connection connection = DatabaseMgr.getInstance().getConnection(null);

        //2010-12-13,Kevin Ge, check variable dependency before remove variable
        if(deleteList.length>0)
        {
        	int[] id_delete = new int[deleteList.length];
        	for(int i=0;i<id_delete.length;i++)
        	{
        		if(!deleteList[i].equals(""))
        			id_delete[i] = Integer.valueOf(deleteList[i]);
        	}
        	VarDependencyState state = LineConfig.checkVariableDependence(userSession.getIdSite(), id_delete, 0, LineConfig.ALL_CHECK, lang, true);
        	if(state.dependsOn())
        	{
        		String str =  lang_s.getString("importctrl", "nooperation")+"\n";
            	str += state.getMessagesAsString();
            	userSession.setProperty("control", str);
        		return false;
        	}
        }
        try
        {
            PreparedStatement preparedStatement = null;
            PreparedStatement ps2 = null;
			PreparedStatement ps3 = null;

            //Aggiorno la descrizione del dispositivo
            preparedStatement = connection.prepareStatement(
                    "UPDATE cftableext SET description=? WHERE tablename=? AND tableid=? and idsite=1");
            preparedStatement.setObject(1, deviceDesc);
            preparedStatement.setObject(2, "cfdevice");
            preparedStatement.setObject(3, idDevice);
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement(
                    "UPDATE cfvariable SET iscancelled=? WHERE idvariable=? and idvarmdl is not null and idsite=1");
            preparedStatement.setObject(1, "TRUE");
            
            ps2 = connection.prepareStatement(
            		"UPDATE cfvariable SET iscancelled=? WHERE idsite=1 and idvariable=(select idhsvariable from cfvariable where idvariable=? and idsite=1)");
            ps2.setObject(1, "TRUE");
            
            ps3 = connection.prepareStatement(
            		"UPDATE cfvariable SET iddevice=NULL,idhsvariable=-1 WHERE idvariable=? and idvarmdl is null and idsite=1");

            // remove variable from graph conf, bug 7369
            PreparedStatement ps0_1 = connection.prepareStatement(
    			"DELETE FROM cfgraphconf WHERE idvariable=(SELECT idhsvariable FROM cfvariable WHERE idvariable=?)");
            PreparedStatement ps0_2 = connection.prepareStatement("DELETE FROM cfgraphconf WHERE idvariable=?");
            
            for (int i = 0; ((i < deleteList.length) && (!deleteList[i].equals(""))); i++)
            {
                ps0_1.setObject(1, deleteList[i]);
                ps0_1.execute();
                ps0_2.setObject(1, deleteList[i]);
                ps0_2.execute();

            	preparedStatement.setObject(2, new Integer(deleteList[i]));
                ps2.setObject(2, new Integer(deleteList[i]));
                preparedStatement.execute();
                ps2.execute();
                
                ps3.setObject(1, new Integer(deleteList[i]));
                ps3.execute();
            } //for

            for (int i = 0; ((i < addList.length) && (!addList[i].equals(""))); i += 3)
            {
                addVariableInLogicDevice(connection, BaseConfig.getPlantId(),
                    new Integer(userSession.getIdSite()), idDevice, new Integer(addList[0 + i]),
                    addList[2 + i], (new Integer(addList[1 + i]).intValue() == 1) ? true : false,
                    currentTime);
            } //for

            connection.commit();
            DatabaseMgr.getInstance().closeConnection(null, connection);
        } //try 
        catch (Exception e)
        {
            e.printStackTrace();
            DatabaseMgr.getInstance().closeConnection(null, connection);
        } //catch
        return true;
    }

    public int size()
    {
        return size;
    } //size

    public LogicDeviceBean getLogicDevice(int i)
    {
        return logicDeviceList[i];
    } //getLogicDevice

    private static void addVariableInLogicDevice(Connection connection, String pvCode,
        Integer idSite, Integer idDevice, Integer idMVariable, String descVariable,
        boolean isLogic, Timestamp currentTime) throws Exception
    {
        PreparedStatement preparedStatement = null;
        SeqMgr seqMgr = SeqMgr.getInstance();

        Object[] objects = null;
        String sql = null;

        if (isLogic)
        {
            objects = new Object[] { idDevice, idMVariable };
            sql = "UPDATE cfvariable SET iddevice=? where idvariable=? and idsite=1";

            preparedStatement = connection.prepareStatement(sql);

            for (int j = 0; j < objects.length; j++)
            {
                preparedStatement.setObject(j + 1, objects[j]);
            }

            preparedStatement.execute();
        } //if
        else
        {
            //**************************** CFFUNCTION ******************************//
            //"idfunction","pvcode","idsite","functioncode","opertype","parameters" //
            //"operorder","lastupdate"                                              // 
            //**********************************************************************//
            Integer idFunction = seqMgr.next(null, "cffunction", "idfunction");
            objects = new Object[]
                {
                    idFunction, pvCode, idSite, idFunction, "I", "pk" + idMVariable, new Integer(1),
                    currentTime
                };
            sql = "INSERT INTO cffunction VALUES(?,?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);

            for (int j = 0; j < objects.length; j++)
            {
                preparedStatement.setObject(j + 1, objects[j]);
            }

            preparedStatement.execute();
            
            DataHs dataHs= CreateSqlHs.getInsertData("cffunction",objects);										
            DatabaseMgr.getInstance().executeStatement(null,dataHs.getSql(),dataHs.getObjects());

            //**************************** CFVARIABLE ******************************//
            // "idvariable","pvcode","idsite","iddevice","islogic","idvarmdl"       // 
            // "functioncode","code","type","addressin","addressout","vardimension" //
            // "varlength","bitposition","signed","decimal","todisplay","buttonpath"//
            // "priority","readwrite","minvalue","maxvalue","defaultvalue"          //
            // "measureunit","idvargroup","imageon","imageoff","frequency","delta"  //
            // "delay","isonchange","ishaccp","isactive","iscancelled","grpcategory"//
            // "idhsvariable","inserttime","lastupdate"                             //  
            //**********************************************************************//
            Integer idVariable = seqMgr.next(null, "cfvariable", "idvariable");
            objects = new Object[]
                {//idvarmdl -- > visto che il campo non ?utile per le variabili logiche inserisco, per le soli variabili logiche identit? l'id della variabile fisica che rappresenta in modo da agevolare la fase di set di queste logiche speciali le uniche settabili tra le logiche... il fu "una variabile fisica"
                    idVariable, pvCode, idSite, idDevice, "TRUE", idMVariable, idFunction, idVariable, null,
                    null, null, null, null, "NONE", null,  null, null, null, null, null, null, "FALSE", "FALSE",
                    "TRUE", "FALSE", new Integer(-1), currentTime, currentTime, idMVariable
                };

            sql = "INSERT INTO cfvariable SELECT ?,?,?,?,?,?,?,?,type,?,?,?,?,?,signed,decimal,?,?,priority,readwrite,?,?,?, measureunit,idvargroup,?,?,frequency,delta,?,?,?,?,?,grpcategory,?,?,? FROM cfvariable WHERE idvariable=? and idsite=1";

            preparedStatement = connection.prepareStatement(sql);

            for (int j = 0; j < objects.length; j++)
            {
                preparedStatement.setObject(j + 1, objects[j]);
            }

            preparedStatement.execute();
            
            /**
             *	Se la variabile ?di tipo allarme allora la devo inserire nella
             *	tabella buffer per la corretta gestione dell'allarme. 
             */
            try {
            	checkAndInsertBuffer(connection,idSite,idVariable.intValue());
            }
            catch(Exception e) {
            	
            }
            
            //**************************** CFTABLEXT *******************************//
            // "idsite" , "languagecode DA SELECT","tablename", "tableid"           //  			
            // "description" ,"shortdescr" ,"longdescr","lastupdate"                //
            //**********************************************************************//
            /**
             * In questo modo le variabili logiche provenienti da variabili fisiche vengono
             * inserite nella CFTABLEEXT con un'unica descrizione sia per la lingua primaria 
             * che per quella secondaria.
             * Segnalazione CLIMASET
             * MODIFICATO PER PERMETTERE LA DOPPIA DESCRIZIONE DELLE VARIABILI LOGICHE
             * Trava - 15/02/2008
             */
            /*
            objects = new Object[]
                {
                    idSite, "cfvariable", idVariable, descVariable, null, null, currentTime
                };

            sql = "INSERT INTO cftableext SELECT ?,languagecode,?,?,?,?,?,? from cfsiteext ";
            */
            
            sql = "insert into cftableext select idsite,languagecode,tablename,?,description, " +
            	  "shortdescr,longdescr,? from cftableext where idsite=? and tablename=? and tableid=?";
            
            objects = new Object[]{idVariable,currentTime,idSite,"cfvariable",idMVariable};
            
            
            
            preparedStatement = connection.prepareStatement(sql);
            for (int j = 0; j < objects.length; j++)
                preparedStatement.setObject(j + 1, objects[j]);
            preparedStatement.execute();
        }
    }
    
    private static void checkAndInsertBuffer(Connection con,int idSite,int idNewVar)
    	throws Exception
    {
    	String sql = "select type from cfvariable where idvariable=?";
    	PreparedStatement ps = con.prepareStatement(sql);
    	ps.setObject(1,new Integer(idNewVar));
    	
    	int iType = 0;
    	ResultSet rs = ps.executeQuery();
    	while(rs.next())
    		iType = rs.getInt("type");
    	
    	rs.close();
    	ps.close();
    	
    	if(iType == 4)
    	{
    		sql = "insert into buffer values(?,?,?,?,?)";
    		ps = con.prepareStatement(sql);
    		ps.setObject(1,new Integer(idSite));
        	ps.setObject(2,new Integer(idNewVar));
        	ps.setObject(3,new Integer(100));
        	ps.setObject(4,new Integer(-1));
        	ps.setObject(5,new Boolean(false));
        	ps.execute();
        	ps.close();
    	}
    }
}
