package com.carel.supervisor.presentation.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.hs.CreateSqlHs;
import com.carel.supervisor.dataaccess.hs.DataHs;
import com.carel.supervisor.presentation.session.UserSession;


public class LogicVariableBeanList
{
 
    private LogicVariableBean[] logicVariableList = null;
    private int size = 0;
    
    
    public LogicVariableBeanList(){}
    
    public void loadVariableComplete(String dbId, int idSite, String language)
        throws Exception
    {
    	StringBuffer sql = new StringBuffer();
    	Object[] objects = new Object[]
          {
      		new Integer(0),
      		"I",
      		new Integer(idSite),
      		"FALSE",
      		"TRUE",
      		"LOGIC_CONDITION",
      		"cfvariable",
      		language,
              new Integer(idSite),
              
          };
       sql.append("SELECT cfvariable.idvariable,cfvariable.type,cftableext.description,cffunction.opertype,cffunction.functioncode,cfvariable.iddevice ");
       sql.append("FROM   cfvariable,cftableext,cffunction ");
       sql.append("WHERE  cffunction.functioncode >= ? ");
       sql.append("AND    cffunction.opertype != ? ");
       sql.append("AND    cfvariable.functioncode= cffunction.functioncode ");
       sql.append("AND    cfvariable.idsite = ? "); 
       sql.append("AND    cfvariable.iscancelled = ? ");
       sql.append("AND    cfvariable.islogic= ?  ");
       sql.append("AND    cfvariable.code!=?  ");
       sql.append("AND    cfvariable.idhsvariable is not null  ");
       sql.append("AND    cfvariable.idvariable=cftableext.tableid ");
       sql.append("AND    cftableext.tablename= ? ");
       sql.append("AND    cftableext.languagecode = ? ");
       sql.append("AND    cftableext.idsite = ? ");
       sql.append("ORDER BY  cftableext.description");       

        
       RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId, sql.toString(), objects);

        if (rs.size() > 0)
        {
            size = rs.size();

            logicVariableList = new LogicVariableBean[size];

            Record record = null;

            for (int i = 0; i < rs.size(); i++)
            {
                record = rs.get(i);
                logicVariableList[i] = new LogicVariableBean(record);
            } //for
        } //if
    } //LogicDeviceBeanList
    
    
    

    public void loadVariableForDevice(String dbId,int idDevice, int idSite, String language)
    throws Exception
{
    StringBuffer sql = new StringBuffer();
    Object[] objects = new Object[]
        {
            "cfvariable", 
            language, 
            new Integer(idSite), 
            new Integer(idSite),
            new Integer(idDevice),
            "FALSE",
            "cfvariable", 
            language, 
            new Integer(idSite), 
            new Integer(idSite),
            new Integer(idDevice),
            "FALSE"
        };
    
    //var logiche senza nome device
    sql.append("SELECT cfvar.idvariable,cfvar.type,cftableext.description as description,cfdevice.islogic ");
    sql.append("FROM   cfvariable as cfvar,cftableext,cfdevice ");
    sql.append("WHERE  cfvar.idvariable = cftableext.tableid ");
    sql.append("AND    cfvar.iddevice = cfdevice.iddevice ");
    sql.append("AND    cftableext.tablename = ? ");
    sql.append("AND    cftableext.languagecode = ? ");
    sql.append("AND    cftableext.idsite = ? ");
    sql.append("AND    cfvar.idsite = ? "); 
    sql.append("AND    cfvar.iddevice = ?  ");
    sql.append("AND    cfvar.iscancelled = ? ");
    sql.append("AND    cfvar.idvarmdl is null ");
    sql.append("AND    cfvar.idhsvariable is not null  ");
    sql.append(" UNION ");
    //var fisiche con nome device
    sql.append("SELECT cfvar.idvariable,cfvar.type, '[' || (select description as devdescr from cftableext where languagecode='"+language+"' and tablename='cfdevice' and tableid=(select iddevice from cfvariable where idvariable=cfvar.idvarmdl)) || '] --> ' || cftableext.description as description,cfdevice.islogic ");
    sql.append("FROM   cfvariable as cfvar,cftableext,cfdevice ");
    sql.append("WHERE  cfvar.idvariable = cftableext.tableid ");//primo join
    sql.append("AND    cfvar.iddevice = cfdevice.iddevice ");   //secondo join
    sql.append("AND    cftableext.tablename = ? ");
    sql.append("AND    cftableext.languagecode = ? ");
    sql.append("AND    cftableext.idsite = ? ");
    sql.append("AND    cfvar.idsite = ? "); 
    sql.append("AND    cfvar.iddevice = ?  ");
    sql.append("AND    cfvar.iscancelled = ? ");
    sql.append("AND    cfvar.idvarmdl is not null ");
    sql.append("AND    cfvar.idhsvariable is not null  ");
    sql.append("ORDER by description"); 

   RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
   sql.toString(), objects);

    if (rs.size() > 0)
    {
        size = rs.size();

        logicVariableList = new LogicVariableBean[size];

        Record record = null;

        for (int i = 0; i < rs.size(); i++)
        {
            record = rs.get(i);
            logicVariableList[i] = new LogicVariableBean(record);
        } //for
    } //if
} //loadVariableForDevice


    public void loadLogicVariablesModifiable(String dbId,int idVariable, int idSite, String language) throws DataBaseException{
        StringBuffer sql = new StringBuffer();
        Object[] objects = new Object[]
            {
        		new Integer(idVariable),new Integer(idSite),new Integer(idVariable),new Integer(idSite)
            };
        sql.append("SELECT cfvariable.idvariable,cfvariable.type,cffunction.opertype,cffunction.parameters,unitmeasurement.idunitmeasurement,cfvariable.decimal ");
        sql.append("FROM   cfvariable,cffunction,unitmeasurement ");
        sql.append("WHERE    cfvariable.functioncode= cffunction.functioncode "); 
    	
        sql.append("AND cfvariable.measureunit IN (select distinct(description) from unitmeasurement) ");
        
        sql.append("AND    cfvariable.measureunit = unitmeasurement.description ");
        sql.append("AND    cfvariable.idvariable = ?  ");
        sql.append("AND    cfvariable.idsite = ?  ");
        sql.append("AND    cfvariable.idhsvariable is not null  ");
        
        sql.append("UNION ");
        sql.append("SELECT cfvariable.idvariable,cfvariable.type,cffunction.opertype,cffunction.parameters,unitmeasurement.idunitmeasurement,cfvariable.decimal ");
        sql.append("FROM   cfvariable,cffunction,unitmeasurement ");
        sql.append("WHERE    cfvariable.functioncode= cffunction.functioncode ");
        sql.append("AND cfvariable.measureunit not IN (select distinct(description) from unitmeasurement) ");
        sql.append("AND    unitmeasurement.description='' ");
        sql.append("AND    cfvariable.idvariable = ?  ");
        sql.append("AND    cfvariable.idsite = ?  ");
        sql.append("AND    cfvariable.idhsvariable is not null ");
        
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);

        if (rs!=null && rs.size() > 0)
        {
	        size = rs.size();

	        LogicVariableBean logicVariableListTmp =null;
	        Record record = null;
            record = rs.get(0);
            logicVariableListTmp = new LogicVariableBean(record);
            String []parameters=logicVariableListTmp.getParameters().split(";");
            String operType=logicVariableListTmp.getOpertype();
            int unitMeasure=logicVariableListTmp.getIdUnitOfMeasurement();
            int decimal=logicVariableListTmp.getDecimal();
            if((parameters!=null)&&(parameters.length>0)){
            	size=parameters.length;	
            	logicVariableList= new LogicVariableBean[parameters.length];
	            Integer idVar=null;
            	for(int i=0;i<size;i++){
            		if(parameters[i].startsWith("pk"))
            		{
            			sql=new StringBuffer();
                		idVar= new Integer(parameters[i].substring(2));	
    	            	objects = new Object[]{idVar,"cfvariable","cfdevice",new Integer(idSite),new Integer(idSite),new Integer(idSite),language,language};
    	            	sql.append("SELECT cfvariable.idvariable,cfvariable.type,cftableextvar.description,cftableextdev.description as descriptiondev ");
    	            	sql.append("FROM   cfvariable,cftableext as cftableextvar,cftableext as cftableextdev ");
    	            	sql.append("WHERE  cfvariable.idvariable= cftableextvar.tableid "); 
    	            	sql.append("AND    cfvariable.iddevice = cftableextdev.tableid  ");
    	            	sql.append("AND    cfvariable.idvariable = ?  ");
    	            	sql.append("AND    cftableextvar.tablename = ?  ");
    	            	sql.append("AND    cftableextdev.tablename = ?  ");
    	            	sql.append("AND    cftableextdev.idsite = ?  ");
    	            	sql.append("AND    cftableextvar.idsite = ?  ");
    	            	sql.append("AND    cfvariable.idsite = ?  ");
    	            	sql.append("AND    cftableextvar.languagecode = ?  ");
    	            	sql.append("AND    cftableextdev.languagecode = ?  ");
    	            	sql.append("ORDER BY  cftableextvar.description");
    	            	
    	            	rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);
                        logicVariableList[i] = new LogicVariableBean(rs.get(0));
            		}
            		else
            		{
            			logicVariableList[i] = new LogicVariableBean();
            			logicVariableList[i].setDescription(parameters[i]);
            		}
	            }//for
            	logicVariableList[0].setOpertype(operType);            //nella prima variabile che puï¿½ essere anche la sola metto l'operatore
            	logicVariableList[0].setIdunitmeasurement(unitMeasure);//nella prima variabili si trova anche l'unitï¿½ di misura
            	logicVariableList[0].setDecimal(decimal);
            	logicVariableList[0].setMasterType(logicVariableListTmp.getType());
          	
            }//if
        }//if
    }//loadLogicVariableModifiable
    
    public void loadPhyisicalVariablesForDevice(String dbId,int idDevice, int idSite, String language,int []type)
    throws Exception
{
    StringBuffer sql = new StringBuffer();
  
    Object []objects= new Object[7+type.length];
    objects[0]=new String("cfvariable");
    objects[1]=new String(language);
    objects[2]=new Integer(idSite);
    objects[3]=new Integer(idSite);
    objects[4]=new Integer(idDevice);
    objects[5]=new String("FALSE");
    objects[6]=new String("FALSE");
   
    
    
    sql.append("SELECT cfvariable.idvariable,cfvariable.type,cfvariable.islogic,cftableext.description ");
    sql.append("FROM   cfvariable,cftableext,cfdevice ");
    sql.append("WHERE  cfvariable.idvariable=cftableext.tableid "); //primo join
    sql.append("AND    cfvariable.iddevice= cfdevice.iddevice ");   //secondo join
    sql.append("AND    cftableext.tablename= ? ");
    sql.append("AND    cftableext.languagecode = ? ");
    sql.append("AND    cftableext.idsite = ? ");
    sql.append("AND    cfvariable.idsite = ? "); 
    sql.append("AND    cfvariable.iddevice= ?  ");
    sql.append("AND    cfvariable.islogic = ? ");
    sql.append("AND    cfvariable.iscancelled = ? ");
    sql.append("AND    cfvariable.idhsvariable is not null ");
    
    sql.append("AND (");
    for(int i=7;i<type.length+7;i++){
    	objects[i]=new Integer(type[i-7]);
    	sql.append(" cfvariable.type = ? ");
    	if(i-7!=type.length-1)
    		sql.append(" OR ");
        else
        	sql.append(" ) ");
    }//for
    
    sql.append("ORDER BY  cftableext.description");

   RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
   sql.toString(), objects);

    if (rs.size() > 0)
    {
        size = rs.size();

        logicVariableList = new LogicVariableBean[size];

        Record record = null;

        for (int i = 0; i < rs.size(); i++)
        {
            record = rs.get(i);
            logicVariableList[i] = new LogicVariableBean(record);
        } //for
    } //if
} //loadFisicVariableForDevice

    public void loadVariablesListForDevice(String dbId,int idDevice, int idSite, String language,int []type) throws Exception {
	    StringBuffer sql = new StringBuffer();
	    
	    Object []objects= new Object[6+type.length];
	    objects[0]=new String("cfvariable");
	    objects[1]=new String(language);
	    objects[2]=new Integer(idSite);
	    objects[3]=new Integer(idSite);
	    objects[4]=new Integer(idDevice);
	    objects[5]=new String("FALSE");
	    
	    sql.append("SELECT cfvariable.idvariable,cfvariable.type,cfvariable.islogic,cftableext.description ");
	    sql.append("FROM   cfvariable,cftableext,cfdevice ");
	    sql.append("WHERE  cfvariable.idvariable=cftableext.tableid "); //primo join
	    sql.append("AND    cfvariable.iddevice= cfdevice.iddevice ");   //secondo join
	    sql.append("AND    cftableext.tablename= ? ");
	    sql.append("AND    cftableext.languagecode = ? ");
	    sql.append("AND    cftableext.idsite = ? ");
	    sql.append("AND    cfvariable.idsite = ? "); 
	    sql.append("AND    cfvariable.iddevice= ?  ");
	    sql.append("AND    cfvariable.iscancelled = ? ");
	    sql.append("AND    cfvariable.idhsvariable is not null ");
	    sql.append("AND (");
	    for(int i=6;i<type.length+6;i++){
	    	objects[i]=new Integer(type[i-6]);
	    	sql.append(" cfvariable.type = ? ");
	    	if(i-6!=type.length-1)
	    		sql.append(" OR ");
	        else
	        	sql.append(" ) ");
	    }//for
	    
	    sql.append("ORDER BY  cftableext.description");
	    
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,sql.toString(), objects);
	    if (rs.size() > 0) {
			size = rs.size();
			logicVariableList = new LogicVariableBean[size];
			Record record = null;
			for (int i = 0; i < rs.size(); i++) {
				record = rs.get(i);
				logicVariableList[i] = new LogicVariableBean(record);
			} // for
		} // if
    } //loadFisicVariableForDevice
    
    public void loadLogicalVariables(String dbId,int idSite, String language,int type)
    throws Exception
{
    StringBuffer sql = new StringBuffer();
    Object[] objects = new Object[]
        {
            "cfvariable", 
            language, 
            new Integer(idSite), 
            new Integer(idSite),
            new Integer(type),
            "TRUE","FALSE"
           
        };
    sql.append("SELECT cfvariable.idvariable,cfvariable.type,cfvariable.islogic,cftableext.description ");
    sql.append("FROM   cfvariable,cftableext ");
    sql.append("WHERE  cfvariable.idvariable=cftableext.tableid ");//primo join
    sql.append("AND    cftableext.tablename= ? ");
    sql.append("AND    cftableext.languagecode = ? ");
    sql.append("AND    cftableext.idsite = ? ");
    sql.append("AND    cfvariable.idsite = ? "); 
    sql.append("AND    cfvariable.type = ? ");
    sql.append("AND    cfvariable.islogic = ? ");
    sql.append("AND    cfvariable.iscancelled = ? ");
    sql.append("AND    cfvariable.iddevice is null ");
    sql.append("AND    cfvariable.idhsvariable is not null  ");
    sql.append("ORDER BY  cftableext.description");       
    
   RecordSet rs = DatabaseMgr.getInstance().executeQuery(dbId,
   sql.toString(), objects);

    if (rs.size() > 0)
    {
        size = rs.size();

        logicVariableList = new LogicVariableBean[size];

        Record record = null;

        for (int i = 0; i < rs.size(); i++)
        {
            record = rs.get(i);
            logicVariableList[i] = new LogicVariableBean(record);
        } //for
    } //if
} //loadFisicVariableForDevice
    
    

    public static synchronized void addVariable(UserSession userSession,Properties properties) throws Exception{
    
    	Connection connection= DatabaseMgr.getInstance().getConnection(null);
    	try{
	    	PreparedStatement preparedStatement=null;
	    	SeqMgr seqMgr=SeqMgr.getInstance();
	    	String pvCode=BaseConfig.getPlantId();
	    	Integer idSite=new Integer(userSession.getIdSite());
	    	String []variablesInformations=((String)properties.get("variableTable")).split("@",-1);
	    	String opertype=replace((String)properties.get("operationSimbol"));
	    	StringBuffer parameters=new StringBuffer();
	    	int i=0;
	    	for(;i<variablesInformations.length-5;i+=5)
	    	{
	    		//constant
	    		if(variablesInformations[i] == null || variablesInformations[i].equals(""))
	        	{
	        		parameters.append(variablesInformations[i+2]+";"); 
	        	}
	        	else
	        	{
	        		parameters.append("pk"+variablesInformations[i]+";");
	        	}
	       	}//for
	    	if(variablesInformations[i] == null || variablesInformations[i].equals(""))
        	{
        		parameters.append(variablesInformations[i+2]); 
        	}
        	else
        	{
        		parameters.append("pk"+variablesInformations[i]);
        	}
    	    Timestamp currentTime= new Timestamp(System.currentTimeMillis());	
	    	
	    	Object []objects= null;
	    	String sql=null;
	    	//**************************** CFFUNCTION ******************************//
			//"idfunction","pvcode","idsite","functioncode","opertype","parameters" //
			//"operorder","lastupdate"                                              // 
			//**********************************************************************//
			Integer idFunction=seqMgr.next(null,"cffunction","idfunction");
			objects= new Object[]{idFunction,pvCode,idSite,idFunction,opertype,parameters.toString(),new Integer(1),currentTime};
			sql="INSERT INTO cffunction VALUES(?,?,?,?,?,?,?,?)";
			
			preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
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
			Integer idVariable=seqMgr.next(null,"cfvariable","idvariable");	
			String typevar=(String)properties.get("typevar");
			Integer type=null; 
			for(;;){
				if(typevar.equals("inputRadioInteger")){
					type= new Integer(VariableInfo.TYPE_INTEGER);
					break;
				}
				if(typevar.equals("inputRadioAnalogic")){
					type= new Integer(VariableInfo.TYPE_ANALOGIC);
					break;
				}
				if(typevar.equals("inputRadioDigital")){
					type= new Integer(VariableInfo.TYPE_DIGITAL);
					break;
				}
				if(typevar.equals("inputRadioAlarm")){
					type= new Integer(VariableInfo.TYPE_ALARM);
					break;
				}
			break;
			}//forswitch
			String unitMeasurementDescription=(String)properties.get("unitMeasurementDescription");
			int decimal = properties.containsKey("decimals") ? Integer.parseInt(properties.get("decimals").toString()) : 0;
			objects= new Object[]{idVariable,pvCode,idSite,null,"TRUE",null,
					              idFunction,idVariable,type,null,null,null,
					              null,null,null,decimal,"NONE",null,
					              new Integer(0),"1",null,null,null,
					              unitMeasurementDescription,null,null,null,null,new Integer(0),
					              null,"FALSE","FALSE","FALSE","FALSE",55,
					              new Integer(-1),currentTime,currentTime};
							
			sql="INSERT INTO cfvariable VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();

	    	//**************************** CFTABLEXT *******************************//
	    	// "idsite" , "languagecode DA SELECT","tablename", "tableid"           //  			
	    	// "description" ,"shortdescr" ,"longdescr","lastupdate"                //
	    	//**********************************************************************//
	    	String descVariable=(String)properties.get("description");
	    	objects= new Object[]{idSite,"cfvariable",idVariable,
	    			descVariable,null,null,currentTime   		
	    	};
	    		    	    	
	    	sql="INSERT INTO cftableext SELECT ?,languagecode,?,?,?,?,?,? from cfsiteext ";
	    	
	    	preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();
	    	
	    	connection.commit();
	    	DatabaseMgr.getInstance().closeConnection(null,connection);
	    	
	    	// activate alarm variable
	    	if( type == VariableInfo.TYPE_ALARM ) {
	    		VarphyBeanList alarmlist = new VarphyBeanList();
	    		alarmlist.updateIsActive(idSite, idVariable, 5, 1);
	    		
                sql = "insert into buffer values (?,?,?,?,?)";
                Object[] param = new Object[5];
                param[0] = new Integer(idSite);
                param[1] = new Integer(idVariable);
                param[2] = new Integer(100);
                param[3] = new Integer(-1);
                param[4] = new Boolean(false);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
	    	}
		}//try
		catch(Exception e){
			e.printStackTrace();
			DatabaseMgr.getInstance().closeConnection(null,connection);
	    	}//catch
	    	
	

   	
    }//addVariable
    
    
    public static synchronized void deleteVariable(UserSession userSession,Properties properties) throws Exception{
    	Connection connection= DatabaseMgr.getInstance().getConnection(null);
    	try{
    		PreparedStatement preparedStatement=connection.prepareStatement("UPDATE cfvariable SET iscancelled=? WHERE idvariable=? and idsite=1");
    		preparedStatement.setObject(1,"TRUE");
    		preparedStatement.setObject(2,new Integer((String)properties.get("idVariable")));
    		preparedStatement.execute();
    		preparedStatement=connection.prepareStatement("UPDATE cfvariable SET iscancelled=? WHERE idvariable=(SELECT idhsvariable from cfvariable WHERE idvariable =? and idsite=1)");
    		preparedStatement.setObject(1,"TRUE");
    		preparedStatement.setObject(2,new Integer((String)properties.get("idVariable")));
    		preparedStatement.execute();
      		connection.commit();
        	DatabaseMgr.getInstance().closeConnection(null,connection);
    	}//try
    	catch(Exception e){
    		e.printStackTrace();
    		DatabaseMgr.getInstance().closeConnection(null,connection);
        }//catch
    	
    }//deleteVariable
  
    
    

    
    public static synchronized void modifyVariable(UserSession userSession,Properties properties) throws Exception{
    
    	Connection connection= DatabaseMgr.getInstance().getConnection(null);
    	try{
	    	PreparedStatement preparedStatement=null;
	    	Integer functionCode= new Integer((String)properties.get("functionCode"));
	    	String []variablesInformations=((String)properties.get("variableTable")).split("@");
	    	String opertype=replace((String)properties.get("operationSimbol"));
	    	StringBuffer parameters=new StringBuffer();
	    	int i=0;
	    	for(;i<variablesInformations.length-5;i+=5)
	    	{
	    		//constant
	    		if(variablesInformations[i] == null || variablesInformations[i].equals(""))
	        	{
	        		parameters.append(variablesInformations[i+2]+";"); 
	        	}
	        	else
	        	{
	        		parameters.append("pk"+variablesInformations[i]+";");
	        	}
	       	}//for
	    	if(variablesInformations[i] == null || variablesInformations[i].equals(""))
        	{
        		parameters.append(variablesInformations[i+2]); 
        	}
        	else
        	{
        		parameters.append("pk"+variablesInformations[i]);
        	}
    	    Timestamp currentTime= new Timestamp(System.currentTimeMillis());	
	    	
	    	Object []objects= null;
	    	String sql=null;
	    	//**************************** CFFUNCTION ******************************//
			//"idfunction","pvcode","idsite","functioncode","opertype","parameters" //
			//"operorder","lastupdate"                                              // 
			//**********************************************************************//
	
			objects= new Object[]{opertype,parameters.toString(),currentTime,functionCode};
			sql="UPDATE cffunction SET opertype=?,parameters=?,lastupdate=?  WHERE functioncode=?";
			
			preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();
	    	
	    	DataHs dataHs= CreateSqlHs.getUpdateData("cffunction",
	        		new String[]{"idfunction","pvcode","idsite","functioncode","opertype","parameters","operorder"},
	        		new Object[]{objects[objects.length-1]},new String[]{"="},new String[]{"functioncode"});
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
			
			String typevar=(String)properties.get("typevar");
			Integer type=null; 
			for(;;){
				if(typevar.equals("inputRadioInteger")){
					type= new Integer(VariableInfo.TYPE_INTEGER);
					break;
				}
				if(typevar.equals("inputRadioAnalogic")){
					type= new Integer(VariableInfo.TYPE_ANALOGIC);
					break;
				}
				if(typevar.equals("inputRadioDigital")){
					type= new Integer(VariableInfo.TYPE_DIGITAL);
					break;
				}
				if(typevar.equals("inputRadioAlarm")){
					type= new Integer(VariableInfo.TYPE_ALARM);
					break;
				}
			break;
			}//forswitch
			String unitMeasurementDescription=(String)properties.get("unitMeasurementDescription");
			int decimal = properties.containsKey("decimals") ? Integer.parseInt(properties.get("decimals").toString()) : 0;
			Integer idVariable=new Integer((String)properties.get("idVariable"));
			objects= new Object[]{type,unitMeasurementDescription,decimal,currentTime,idVariable};
							
			sql="UPDATE cfvariable SET type=? , measureunit=?, decimal=?, lastupdate=? WHERE idvariable=? and idsite=1";
			
			preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();

	    	//**************************** CFTABLEXT *******************************//
	    	// "idsite" , "languagecode DA SELECT","tablename", "tableid"           //  			
	    	// "description" ,"shortdescr" ,"longdescr","lastupdate"                //
	    	//**********************************************************************//
	    	String descVariable=(String)properties.get("description");
	    	objects= new Object[]{descVariable,currentTime,idVariable,"cfvariable"};
	    		    	    	
	    	sql="UPDATE cftableext SET description=?,lastupdate=? WHERE tableid=? AND tablename=? ";
	    	
	    	preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();
	    	
			// description correction for hist variable, bug 7363
	    	sql="UPDATE cftableext SET description=?,lastupdate=? WHERE tableid=(SELECT idhsvariable FROM cfvariable where idvariable=?) AND tablename=?;";
	    	preparedStatement=connection.prepareStatement(sql);
			for(int j=0;j<objects.length;j++)
	    		preparedStatement.setObject(j+1,objects[j]);
	    	preparedStatement.execute();
	    	
		  	connection.commit();
	    	DatabaseMgr.getInstance().closeConnection(null,connection);
	    	
	    	// activate alarm variable
	    	if( type == VariableInfo.TYPE_ALARM ) {
	    		VarphyBeanList alarmlist = new VarphyBeanList();
	    		alarmlist.updateIsActive(1, idVariable, 5, 1);
	    		
                sql = "insert into buffer values (?,?,?,?,?)";
                Object[] param = new Object[5];
                param[0] = new Integer(1);
                param[1] = new Integer(idVariable);
                param[2] = new Integer(100);
                param[3] = new Integer(-1);
                param[4] = new Boolean(false);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
	    	}
		}//try
		catch(Exception e){
			e.printStackTrace();
			DatabaseMgr.getInstance().closeConnection(null,connection);
		}//catch
	    	
	

   	
    }//addVariable
  
    
    private static String replace(String s)
    {
        String sTmp = Replacer.replace(s,  "&lt;","<");
        sTmp = Replacer.replace(sTmp,  "&amp;","&");
        sTmp = Replacer.replace(sTmp,  "&gt;",">");
        sTmp = Replacer.replace(sTmp, "\"", "&quot;");
        sTmp = Replacer.replace(sTmp, "'", "&apos;");

        return sTmp;
    }
    
    public int size()
    {
        return size;
    } //size

    public LogicVariableBean getLogicVariable(int i)
    {
        return logicVariableList[i];
    } //getLogicDevice
    
    
} //LogicDeviceBeanList
