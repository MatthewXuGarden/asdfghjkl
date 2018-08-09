package com.carel.supervisor.director.graph;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.field.DataDynamicSaveMember;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;

//"Per me si va ne la citt� dolente,  
//per me si va ne l'etterno dolore,  
//per me si va tra la perduta gente.  
// Giustizia mosse il mio alto fattore:  
// fecemi la divina podestate,  
//la somma sapienza e 'l primo amore.  
//  Dinanzi a me non fuor cose create  
//se non etterne, e io etterno duro.  
//Lasciate ogne speranza, voi ch'intrate".  

public class GraphRequest
{
    //Costanti per le query e statement
    private static final String FREQUENCY = "frequency";
    private static final String INSERT_TIME = "inserttime";
    private static final String VALUE = "value";
    private static final int FETCH_SIZE = 100;
    
    private GraphInformation queueAndInformation = null; //coda contenente le variabili da visualizzare identificate da idsite,idvariable
    private Connection connection = null; //connessione al db
    private int idVariable = 0; //id della variabile
    private int idSite = 0; //id del sito
    private long actualRecordPeriod = 0; //attuale periodo di campionamento per il record scelto
    private int actualIndexCurve=-1;    //Curva in calcolo
    private int numVariables = 0; //numero delle variabili o delle curve da estrarre                
    private final String[]tableNames= new String[]{"hsvarhaccp","hsvarhistor"};
    private String tableName=null;

    YGraphCoordinates [][]ysGraphSeries=null;
    
    //variabili che mi permettono il padding dell'ultimo nelle situazioni di confine di inizio e fine asse X
    private long startTime=0;
    private long endTime=0;
    private long granularityTime=0;

    private YGraphCoordinates sxPoint=null;
    private YGraphCoordinates dxPoint=null;
    private boolean centralPoint = false;
   
    // fix BUG 4898 - start
    private double tmpForAvg = 0d;
    private Float previousSampleValue=null;
    private long elapsedTimeForAverage = 0l;
	private long previousSampleTime = 0l;
		
	private Float lastSxValue = 0f;
	private long lastSxSampleTime = 0l; 
	
	//private double firstDxValue = 0f;
	//private long firstDxSampleTime = 0l; 	
	
	//if there are two rows: row 1 and row 2
	//row 1 is the first row, row 2 is the second
	//if first row take very long time(big nx), row2's first value time is<row1's last value time
	//add this to avoid this problem
	//but now we shall insert values to ysGraphSeries by time order
	private int lastIndex = -1;
	
	private long preRowTime = -1;
 // fix BUG 4898 - end
    
    public GraphRequest(GraphInformation reportQueue)
        throws DataBaseException
    {
        this.queueAndInformation = reportQueue;
        connection = DatabaseMgr.getInstance().getConnection(null);
       
    } //ReportRequest

    public void startRetrieve() throws Exception
    {
    	try{
    		Object[][] objects = queueAndInformation.dequeAllRecords();
	        tableName= new String(queueAndInformation.getGraphType().equals(GraphConstant.TYPE_HACCP)?tableNames[0]:tableNames[1]);
	        startTime= queueAndInformation.getStartTime().getTime();
	        endTime= queueAndInformation.getEndTime().getTime();
	        granularityTime=queueAndInformation.getTimeGranularity();
	       
			if (objects != null)
	        {
	            numVariables = objects.length;
	            if (numVariables != 0)
	            	ysGraphSeries= new YGraphCoordinates[numVariables][queueAndInformation.getFlashParameters().getXWidth()];
	         
	        	} //if

			for (actualIndexCurve = 0; actualIndexCurve < numVariables; actualIndexCurve++)
				{
            
        	//Init
        	idSite = ((Integer) objects[actualIndexCurve][GraphInformation.INDEX_ID_SITE]).intValue();
            idVariable = ((Integer) objects[actualIndexCurve][GraphInformation.INDEX_ID_VARIABLE]).intValue();
            sxPoint=null;
            dxPoint=null;
            centralPoint=false;
            preRowTime = -1;
            lastIndex = -1;
            
            try{
            	//2 ZONA A
            	retrieveDataAZone(ysGraphSeries[actualIndexCurve]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("RetriveDataAZone "+e);
            }//catch
            try{
            	//3 ZONE B
            	retrieveDataBZone(ysGraphSeries[actualIndexCurve]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("RetriveDataBZone "+e);
            }//catch
            try{
	            //Point From cache
	            pointFromCache(ysGraphSeries[actualIndexCurve]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("(potrebbero esserci problemi nella cache dati)pointFromCache "+e);
            }//catch
            try{
            //point Now
            pointNow(ysGraphSeries[actualIndexCurve]);
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("(il driver potrebbe non funzionare correttamente)pointFromCache "+e);
            }//catch
            try{
            //padding 
            paddingPoint(ysGraphSeries[actualIndexCurve]);
       
            }//try
            catch (Exception e) {
            	LoggerMgr.getLogger(this.getClass()).error("paddingPoint "+e);
            }//catch
        }//for in numVariable

    	DatabaseMgr.getInstance().closeConnection(null, connection);
       	}//try
       	catch(Exception e){
       		DatabaseMgr.getInstance().closeConnection(null, connection);
       		LoggerMgr.getLogger(this.getClass()).error(e);
       	}//catch
    }//startRetrive

    private void retrieveDataAZone(YGraphCoordinates [] ysGraphSerie)
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
            	setGraphValue(insertTime.getTime(),nextTime,(Float) resultSet.getObject(VALUE),ysGraphSerie);
            }
            else
            	setGraphValue(insertTime.getTime(),0,(Float) resultSet.getObject(VALUE),ysGraphSerie);
            	
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
                if ((tmpN == null && keyActual != lastKey) || tmpN.intValue()==0){
                	initRetriveTime = insertTime.getTime() + ((distanceOfFirstInsertTime+1) * actualRecordPeriod * 1000L);
                	preRowTime = initRetriveTime;
                	setGraphValue(initRetriveTime,0,null,ysGraphSerie);
                	break;
                }//if
               
                distanceOfFirstInsertTime += tmpN.longValue();
                initRetriveTime = insertTime.getTime() + (distanceOfFirstInsertTime * actualRecordPeriod * 1000L);
                preRowTime = initRetriveTime;
                nextTmpN = (Integer) resultSet.getObject("n"+(i+1));
                if(nextTmpN != null)
                {
                    long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
                    preRowTime = nextTime;
                	setGraphValue(initRetriveTime,nextTime,(Float) resultSet.getObject(VALUE+i),ysGraphSerie);
                }
                else
                	setGraphValue(initRetriveTime,0,(Float) resultSet.getObject(VALUE+i),ysGraphSerie);
                	
            } //for
        } //while
        preparedStatement.close();
    } //RetriveDataAZone

    //------------------------Query ZONE B-----------------------//
    private void retrieveDataBZone(YGraphCoordinates [] ysGraphSerie)
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
        sql.append("inserttime BETWEEN ? AND ? ORDER BY inserttime");
        
        preparedStatement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        
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
        		setGraphValue(preRowTime,0,null,ysGraphSerie);
        	preRowTime = insertTime.getTime();
            actualRecordPeriod = resultSet.getLong(FREQUENCY);
            Integer nextTmpN = (Integer) resultSet.getObject("n1");
            if(nextTmpN != null)
            {
                long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
                preRowTime = nextTime;
            	setGraphValue(insertTime.getTime(),nextTime,(Float) resultSet.getObject(VALUE),ysGraphSerie);
            }
            else
            	setGraphValue(insertTime.getTime(),0,(Float) resultSet.getObject(VALUE),ysGraphSerie);
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
	                if ((tmpN == null && keyActual != lastKey)|| tmpN.intValue()==0){
	                	initRetriveTime = insertTime.getTime() + ((distanceOfFirstInsertTime+1) * actualRecordPeriod * 1000L);
	                	preRowTime = initRetriveTime;
	                	setGraphValue(initRetriveTime,0,null,ysGraphSerie);
	                	break;
	                }//if
	               
	                distanceOfFirstInsertTime += tmpN.longValue();
	                initRetriveTime =insertTime.getTime() +(distanceOfFirstInsertTime * actualRecordPeriod * 1000L);
	                preRowTime = initRetriveTime;
	                nextTmpN = (Integer) resultSet.getObject("n"+(i+1));
	                if(nextTmpN != null)
	                {
	                    long nextTime = insertTime.getTime() + ((distanceOfFirstInsertTime+nextTmpN) * actualRecordPeriod * 1000L);
	                    preRowTime = nextTime;
	                	setGraphValue(initRetriveTime,nextTime,(Float) resultSet.getObject(VALUE+i),ysGraphSerie);
	                }
	                else
	                	setGraphValue(initRetriveTime,0,(Float) resultSet.getObject(VALUE+i),ysGraphSerie);
	                	
	            }//for   
	        }//while di superProcessing
   
    }//RetriveDataBZone
   
    
    private int setGraphValue(long time,long nextTime,Float value,YGraphCoordinates [] ysGraphSerie)
    {
    	long index=time-startTime;
    	Long indexPadd=new Long(index);					
		// fix BUG 4898 start
		// Average = sum (y-i * deltaT-i) / sum (deltaT-i)
    	if (value != null) {
    		long elapsedFromLastSample = 0;
    		if (time > startTime && time < endTime) {
    			elapsedFromLastSample = time - Math.max(previousSampleTime, startTime);
    		}
    		if (time > endTime) {
    			elapsedFromLastSample = endTime - Math.min(endTime, previousSampleTime);
    		}    		
    		if (previousSampleValue != null) {
    			tmpForAvg += (previousSampleValue * elapsedFromLastSample);    				
    			elapsedTimeForAverage += elapsedFromLastSample;
    		}
    	}
    	previousSampleTime = time;          				
    	previousSampleValue = value;     	
    	// fix BUG 4898 end
    	if(index>=0){
    		float indexTmp=((float)index)/granularityTime;
    		index/=granularityTime;
    		if(indexTmp<=queueAndInformation.getFlashParameters().getXWidth()-1){   
    			if(lastIndex == -1 || (int)index>=lastIndex)
    			{
	    			if(ysGraphSerie[(int)index]==null)
	    				ysGraphSerie[(int)index]= new YGraphCoordinates();
	    			ysGraphSerie[(int)index].setYsGraphCoordinates(value,indexPadd);
	    			centralPoint=true;
	    			lastIndex = (int)index;
    			}
    			else if((int)index<lastIndex)
    			{
    				if(ysGraphSerie[lastIndex]==null)
	    				ysGraphSerie[lastIndex]= new YGraphCoordinates();
	    			ysGraphSerie[lastIndex].setYsGraphCoordinates(value,indexPadd);
	    			centralPoint=true;
    			}
    				
    		}//if
    	}//if
    	else{ // Left of the central interval
			if (value != null) {
		    	lastSxSampleTime = time;
		    	lastSxValue = value;
			}
    		centralPoint=true;
    		if(nextTime != 0 && nextTime-startTime>0)
    		{
	    		if(sxPoint==null)
	    			sxPoint=new YGraphCoordinates(value,indexPadd);
    		}
    	
    	}//else
    	if(nextTime != 0 && time<endTime && nextTime>=endTime)
    		if(dxPoint==null)
    			dxPoint=new YGraphCoordinates(value,indexPadd);
    	return 0;
    }//setGraphValue
    
    
    private void pointNow(YGraphCoordinates [] ysGraphSerie){
    	Variable  variable=VariableMgr.getInstance().getById(idVariable);
        long time=System.currentTimeMillis();
    
        //scarico l'ultimo null � un set fittizio infatti lo ripeto alla fine
        Float value=new Float(variable.getCurrentValue());
        value=value.isNaN()?null:value;
        
        setGraphValue(time,0,value,ysGraphSerie);
   }//pointNow
    

    private void paddingPoint(YGraphCoordinates [] ysGraphSerie){
    	if(ysGraphSerie[0]==null)
    		ysGraphSerie[0]=sxPoint;
    	if((ysGraphSerie[ysGraphSerie.length-1]==null)&&(centralPoint))
    		ysGraphSerie[ysGraphSerie.length-1]=dxPoint;
    	
    }//paddingLastPoint
   
    public YGraphCoordinates [][] getGraphYsSeries(){
    	return ysGraphSeries;
    }//getGraphYsSeries
    

	 private void pointFromCache(YGraphCoordinates [] ysGraphSerie){
		 Variable  variable=VariableMgr.getInstance().getById(idVariable);
	     DataDynamicSaveMember saverDatas= variable.getSaver();
	     Object[][]objects=saverDatas.getGraphReportCacheData();
	     if(objects==null)
	       	 return;
	  
	    for(int i=0;i<objects[0].length;i++)
	    	setGraphValue(((Long)objects[0][i]).longValue(),0,(Float)objects[1][i],ysGraphSerie);
	  
	}//pointFromCache
	 
	// fix BUG 4898 - start 
	public double getAvg() {
		double avg = Double.NaN;
		if (elapsedTimeForAverage != 0) {			
			avg = tmpForAvg / elapsedTimeForAverage;
		}
		else if (lastSxSampleTime != 0){
			// there is only one sample, coming from the sx time window.
			avg = lastSxValue;
		}
		return avg;
	}
	
	// fix BUG 4898 - end

}//Class ReportRequest
