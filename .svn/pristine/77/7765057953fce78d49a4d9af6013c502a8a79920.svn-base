package com.carel.supervisor.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.director.graph.YGraphCoordinates;
import com.carel.supervisor.field.DataDynamicSaveMember;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;

public class ReportRequest
{
    //Costanti per le query e statement
	
    private static final String FREQUENCY = "frequency";
    private static final String INSERT_TIME = "inserttime";
    private static final String VALUE = "value";
    private static final int FETCH_SIZE = 100;
    
    private ReportInformation queueAndInformation = null; //coda contenente le variabili da visualizzare identificate da idsite,idvariable
    private Connection connection = null; //connessione al db
    private int idVariable = 0; //id della variabile
    private int idSite = 0; //id del sito
    private long actualRecordPeriod = 0; //attuale periodo di campionamento per il record scelto
    private int numVariables = 0; //numero delle variabili o delle curve da estrarre                   
   
    private final String[]tableNames= new String[]{"hsvarhaccp","hsvarhistor"};
    private String tableName=null;

    
    YReportCoordinates [][]ysReportSeries=null;
    private long startTime=0;
    private long endTime = 0;
    
    private long granularityTime=0;
    private int numPoint=0;
    
    private YReportCoordinates sxPoint=null;
    private YReportCoordinates dxPoint=null;
    
    private long preRowTime = -1;
    
    public ReportRequest(ReportInformation reportQueue)
        throws DataBaseException
    {
        this.queueAndInformation = reportQueue;
        connection = DatabaseMgr.getInstance().getConnection(null);
       
    } //ReportRequest

    public void startRetrieve() throws Exception
    {
       try{
    	Object[][] objects = queueAndInformation.dequeAllRecords();
        tableName = new String(queueAndInformation.getReportType().equals(GraphConstant.TYPE_HACCP)?tableNames[0]:tableNames[1]);
        startTime = queueAndInformation.getStartTime().getTime();
        endTime = queueAndInformation.getEndTime().getTime();
        granularityTime = queueAndInformation.getSamplingPeriodRequest();
       
        if (objects != null)
        {
           	numVariables = objects.length;
           	numPoint = (int)(queueAndInformation.getReportPeriod() / granularityTime);
           	
        	if ((numVariables != 0)&&(numPoint++>0))
        		ysReportSeries= new YReportCoordinates[numVariables][numPoint];
        	else
        		return;
        } //if
            
        for (int i = 0; i < numVariables; i++)
        {
            idSite = ((Integer) objects[i][ReportInformation.INDEX_ID_SITE]).intValue();
            idVariable = ((Integer) objects[i][ReportInformation.INDEX_ID_VARIABLE]).intValue();
            sxPoint=null;
            dxPoint=null;
            preRowTime = -1;

            //2 ZONA A
            // Get data from DB: get the last record with 'insertTime' < 'report startTime'
            try{
            	RetriveDataAZone(ysReportSeries[i]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("RetriveDataAZone "+e);
            }//catch
            
            //3 ZONE B
            // Get data from DB: get all records with 'insertTime' between 'report startTime' and 'reportEndTime'
            try{
            	RetriveDataBZone(ysReportSeries[i]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("RetriveDataBZone "+e);
            }//catch
            
            //Point From cache
            // Get data from cache
            try{
            	pointFromCache(ysReportSeries[i]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("(potrebbero esserci problemi nella cache dati)pointFromCache "+e);
            }//catch
            //point Now
            //Get the instantaneous value from device
            try{
            	pointNow(ysReportSeries[i]);
            }
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("(il driver potrebbe non funzionare correttamente)pointFromCache "+e);
            }//catch
            //put the SX padding value
            try{
            paddingPoint(ysReportSeries[i]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("paddingPoint "+e);
            }//catch
        } //for in numVariable

        DatabaseMgr.getInstance().closeConnection(null, connection);
       }//try
       catch(Exception e){
    	   DatabaseMgr.getInstance().closeConnection(null, connection);
       }//catch
       
       } //startRetrive

    private void RetriveDataAZone(YReportCoordinates [] ysReportSerie)
        throws SQLException
    {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        long distanceOfFirstInsertTime = 0;
        long initRetriveTime = 0;

        StringBuffer sql = new StringBuffer();
        sql.append(
            "SELECT key,frequency,inserttime,value,n1,value1,n2,value2,n3,value3,n4,value4,n5,value5,n6,value6,n7,value7,n8,value8,n9,value9,n10,value10,n11,value11,n12,value12,n13,value13,n14,value14,n15,value15,n16,value16,n17,value17,n18,value18,n19,value19,n20,value20,n21,value21,n22,value22,n23,value23,n24,value24,n25,value25,n26,value26,n27,value27,n28,value28,n29,value29,n30,value30,n31,value31,n32,value32,n33,value33,n34,value34,n35,value35,n36,value36,n37,value37,n38,value38,n39,value39,n40,value40,n41,value41,n42,value42,n43,value43,n44,value44,n45,value45,n46,value46,n47,value47,n48,value48,n49,value49,n50,value50,n51,value51,n52,value52,n53,value53,n54,value54,n55,value55,n56,value56,n57,value57,n58,value58,n59,value59,n60,value60,n61,value61,n62,value62,n63,value63,n64 ");
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" WHERE ");
        sql.append("idsite = ? AND ");
        sql.append("idvariable = ? AND ");
        sql.append("inserttime ");
        sql.append(" < ? ORDER BY inserttime DESC LIMIT 1 OFFSET 0");

        preparedStatement = connection.prepareStatement(sql.toString());
        preparedStatement.setShort(1, (short) idSite);
        preparedStatement.setInt(2, idVariable);
        preparedStatement.setTimestamp(3, queueAndInformation.getStartTime());

        resultSet = preparedStatement.executeQuery();

        int keyActual = -1;
        Variable  variable = VariableMgr.getInstance().getById(idVariable);
        if(variable != null)
        	keyActual = variable.getSaver().getKeyActual();
        while (resultSet.next())
        {
        	int lastKey = (Integer)resultSet.getObject("key");
            Timestamp insertTime = resultSet.getTimestamp(INSERT_TIME);
            preRowTime = insertTime.getTime();
            actualRecordPeriod = resultSet.getLong(FREQUENCY);
            Integer nextTmpN = (Integer) resultSet.getObject("n1");
            if(nextTmpN != null)
            {
                long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
                preRowTime = nextTime;
                setReportValue(insertTime.getTime(),nextTime,(Float) resultSet.getObject(VALUE),ysReportSerie);
            }
            else
            	setReportValue(insertTime.getTime(),0,(Float) resultSet.getObject(VALUE),ysReportSerie);
            	
            for (int i = 1; i < DataDynamicSaveMember.VALUES; i++)
            {
                Integer tmpN = (Integer) resultSet.getObject("n" + i);
               
                //in the middle, tmpN is null, don't know how this happen
                //we just jump corrupt column
                if (tmpN == null && (i+1)<DataDynamicSaveMember.VALUES && resultSet.getObject("n" + (i+1)) != null){
                	continue;
                }
                if(tmpN == null && keyActual == lastKey){
                	continue;
                }
                //tmpN == null: engine abnormally stop
                //tmpN == 0:    engine normally stop
                if (tmpN == null || tmpN.intValue()==0)
                {
	                if ((tmpN == null && keyActual != lastKey) || tmpN.intValue()==0){
	                	initRetriveTime = insertTime.getTime() + ((distanceOfFirstInsertTime+1) * actualRecordPeriod * 1000L);
	                	preRowTime = initRetriveTime;
	                	setReportValue(initRetriveTime,0,null,ysReportSerie);
	                }
	                break;
                }
               
                distanceOfFirstInsertTime += tmpN.longValue();
                initRetriveTime = insertTime.getTime() + (distanceOfFirstInsertTime * actualRecordPeriod * 1000L);
                preRowTime = initRetriveTime;
                nextTmpN = (Integer) resultSet.getObject("n"+(i+1));
                if(nextTmpN != null)
                {
                    long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
                    preRowTime = nextTime;
                    setReportValue(initRetriveTime,nextTime,(Float) resultSet.getObject(VALUE+i),ysReportSerie);
                }
                else
                	setReportValue(initRetriveTime,0,(Float) resultSet.getObject(VALUE+i),ysReportSerie);
                	
            } //for
        } //while
    } //RetriveDataAZone

    //------------------------Query ZONE B-----------------------//
    private void RetriveDataBZone(YReportCoordinates [] ysReportSerie)
        throws SQLException
    {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        long distanceOfFirstInsertTime = 0;
        long initRetriveTime = 0;

        StringBuffer sql = new StringBuffer();
        sql.append(
            "SELECT key,frequency,inserttime,value,n1,value1,n2,value2,n3,value3,n4,value4,n5,value5,n6,value6,n7,value7,n8,value8,n9,value9,n10,value10,n11,value11,n12,value12,n13,value13,n14,value14,n15,value15,n16,value16,n17,value17,n18,value18,n19,value19,n20,value20,n21,value21,n22,value22,n23,value23,n24,value24,n25,value25,n26,value26,n27,value27,n28,value28,n29,value29,n30,value30,n31,value31,n32,value32,n33,value33,n34,value34,n35,value35,n36,value36,n37,value37,n38,value38,n39,value39,n40,value40,n41,value41,n42,value42,n43,value43,n44,value44,n45,value45,n46,value46,n47,value47,n48,value48,n49,value49,n50,value50,n51,value51,n52,value52,n53,value53,n54,value54,n55,value55,n56,value56,n57,value57,n58,value58,n59,value59,n60,value60,n61,value61,n62,value62,n63,value63,n64 ");
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" WHERE ");
        sql.append("idsite = ? AND ");
        sql.append("idvariable = ? AND ");
        sql.append("inserttime BETWEEN  ? AND ? ORDER BY inserttime");

        preparedStatement = connection.prepareStatement(sql.toString());

        preparedStatement.setShort(1, (short) idSite);
        preparedStatement.setInt(2, idVariable);
        preparedStatement.setTimestamp(3, queueAndInformation.getStartTime());
        preparedStatement.setTimestamp(4, queueAndInformation.getEndTime());

        preparedStatement.setFetchSize(FETCH_SIZE);
        resultSet = preparedStatement.executeQuery();

        int keyActual = -1;
        Variable  variable = VariableMgr.getInstance().getById(idVariable);
        if(variable != null)
        	keyActual = variable.getSaver().getKeyActual();
        while (resultSet.next())
        {
        	distanceOfFirstInsertTime = 0;
	        initRetriveTime =0;
	        int lastKey = (Integer)resultSet.getObject("key");
        	Timestamp insertTime = new Timestamp(((Timestamp) resultSet.getTimestamp(
                    INSERT_TIME)).getTime());
        	//if row by row, the time diff(last column of the first row VS first column of second row) > 5 frequency time, we add a null point
        	if(preRowTime != -1 && (preRowTime+actualRecordPeriod*5*1000L)<insertTime.getTime())
        		setReportValue(preRowTime,0,null,ysReportSerie);
        	preRowTime = insertTime.getTime();
	        actualRecordPeriod = resultSet.getLong(FREQUENCY);
	        Integer nextTmpN = (Integer) resultSet.getObject("n1");
	        if(nextTmpN != null)
	        {
	            long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
	            preRowTime = nextTime;
	        	setReportValue(insertTime.getTime(),nextTime,(Float) resultSet.getObject(VALUE),ysReportSerie);
	        }
	        else
	        	setReportValue(insertTime.getTime(),0,(Float) resultSet.getObject(VALUE),ysReportSerie);
	        for (int i = 1; i < DataDynamicSaveMember.VALUES; i++)
            {
                Integer tmpN = (Integer) resultSet.getObject("n" + i);

                //in the middle, tmpN is null, don't know how this happen
                //we just jump corrupt column
                if (tmpN == null && (i+1)<DataDynamicSaveMember.VALUES && resultSet.getObject("n" + (i+1)) != null){
                	continue;
                }
                if(tmpN == null && keyActual == lastKey){
                	continue;
                }
                if (tmpN == null || tmpN.intValue()==0)
                {
	                if ((tmpN == null && keyActual != lastKey) || tmpN.intValue()==0){
	                	initRetriveTime = insertTime.getTime() + ((distanceOfFirstInsertTime+1) * actualRecordPeriod * 1000L);
	                	preRowTime = initRetriveTime;
	                	setReportValue(initRetriveTime,0,null,ysReportSerie);
	                }
	                break;
                }
               
                distanceOfFirstInsertTime += tmpN.longValue();
                initRetriveTime =insertTime.getTime() +(distanceOfFirstInsertTime * actualRecordPeriod * 1000L);
                preRowTime = initRetriveTime;
                nextTmpN = (Integer) resultSet.getObject("n"+(i+1));
                if(nextTmpN != null)
                {
                    long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
                    preRowTime = nextTime;
                	setReportValue(initRetriveTime,nextTime,(Float) resultSet.getObject(VALUE+i),ysReportSerie);
                }
                else
                	setReportValue(initRetriveTime,0,(Float) resultSet.getObject(VALUE+i),ysReportSerie);
                	
            }//for 
	        
	    }//while di superProcessing
 
    }//RetriveDataBZone

    private int setReportValue(long time,long nextTime,Float value,YReportCoordinates [] ysReportSerie)
    {
    	
    	// for debug purposes
    	//    	String timeString = DateUtils.date2String(new Timestamp(time), "dd/MM/yyyy HH:mm:ss");
    	
    	// index is calculated from the difference:
    	// [sample time (from DB)]-[report start time]
    	// and then divided by [report frequency time]
    	
    	long timediff=time-startTime;
    	Long indexPadd=new Long(timediff);
    	
    	if(timediff>=0){
    		int index=(int)(timediff/granularityTime)+1;
    		
    		if(index<=numPoint){
    			if(ysReportSerie[(int)index]==null)
    				ysReportSerie[(int)index]= new YReportCoordinates();
    			
    			// calculate the time interval between sample time and actual time
    			long timetonow=System.currentTimeMillis()-time;
    			
    			// calculate the time interval between the last 'report sample' time (preceding the actual time)
    			// and the actual time
    			long lastinterval = System.currentTimeMillis()%granularityTime;
    			  			
    			// insert value into report only if the sampling time is located before the 'lastinterval' time slice
    			// this is to avoid the incorrect appearance into the report of a value related to a 'future' time
    			// (this case happens only if the user print a report that includes the actual instant)
    			if(timetonow>=lastinterval)
    				ysReportSerie[(int)index].setYsReportCoordinates(value,indexPadd.longValue());
    			else
    				ysReportSerie[(int)index].setYsReportCoordinates(null,indexPadd.longValue());
    				
    		}//if
    		
    	}
    	else
    	{
    		if(nextTime != 0 && nextTime-startTime>0)
    		{
	    		if(sxPoint==null)
	    			sxPoint=new YReportCoordinates(value,indexPadd);
    		}
    	}
    	if(nextTime != 0 && time<endTime && nextTime>=endTime)
    		if(dxPoint==null)
    			dxPoint=new YReportCoordinates(value,indexPadd);
    	return 0;
    }//setReportValue
    
    // get the actual value from device
    private void pointNow(YReportCoordinates [] ysReportSerie){
    	Variable  variable=VariableMgr.getInstance().getById(idVariable);
        long time=System.currentTimeMillis();
    
        Float value=new Float(variable.getCurrentValue());
        value=value.isNaN()?null:value;
        
    	setReportValue(time,0,value,ysReportSerie);
    	
    	// this setting is used to correctly close the sequence of values
    	setReportValue(time+granularityTime,0,null,ysReportSerie);   	
     
   }//pointNow
   
   
    // put the padding values (sx only) into the first and the last location
    // of the array. Only if it is necessary
    private void paddingPoint(YReportCoordinates [] ysReportSerie){
    	if(ysReportSerie[0]==null)
    		ysReportSerie[0]=sxPoint;
    	if(ysReportSerie[ysReportSerie.length-1]==null)
    		ysReportSerie[ysReportSerie.length-1]=dxPoint;
    }//paddingLastPoint
    
    
    // get values stored in the Cache
    private void pointFromCache(YReportCoordinates [] ysReportSerie){
		 Variable  variable=VariableMgr.getInstance().getById(idVariable);
	     DataDynamicSaveMember saverDatas= variable.getSaver();
	     Object[][]objects=saverDatas.getGraphReportCacheData();
	     if(objects==null)
	       	 return;
	  
	    for(int i=0;i<objects[0].length;i++)
	    	setReportValue(((Long)objects[0][i]).longValue(),0,(Float)objects[1][i],ysReportSerie);
	  
	}//pointFromCache

    
    public YReportCoordinates [][] getReportYsSeries(){
    	return ysReportSeries;
    }//getReportYsSeries
    
    public ReportInformation getReportInformation(){
    	return queueAndInformation;
    }
    
} //Class ReportRequest
