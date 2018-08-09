package com.carel.supervisor.field;

import java.sql.Timestamp;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.history.FDBQueue;


public class  DataDynamicSaveMember
{
    public final static int VALUES = 64; //Valori da memorizzare per record

    //public final static int FORCE_SAVE_CICLE = 128; //Numero di cicli dopo i quali se vi � stato il salvataggio lo forzo
    private final static int INSERT_FIELDS = 7; //Numero di campi nel primo insert di ogni variabile
    //private final static int DELETE_FIELDS = 2; //Numero di campi nel primo insert di ogni variabile
    private final static int UPDATE_AS_INSERT_FIELDS = 7; //Numero di campi nel primo update di ogni variabile una volta riiniziato il giro del buffer
    
    private final static int BUFFER_FIELD = 4; //Numero di campi per l'update della tabella buffer
    private short idsite = 0; //indice del sito
    private int idvariable = 0; //indice della variabile fisica
    private short keyActual = 0; //indice del record
    private short keyMax = 1000; //massimo numero di record concessi per quella variabile   
    private long timeStamp = 0; //tempo di salvataggio della variabile 1 per record 
    private Status status = new Status(); //Stato delle 64 variabili
    private int n = 0; //(RICORDA 0=128) conatore parziale dei deltaT viene annullato dopo ogni scrittura del valore
    private Float newValue = null; //valore appena acquisito dal campo
    private Float oldValue = null; //valore vecchio	
    private float epsilon = 0; //delta, se salvare o no	
    private byte position = 0; //posizione nel record nella quale inserire il valore
    private boolean enqueVariable = false;
    private boolean circularModeActivate = false; //viene attivato quando il buffer viene sovrascritto in modo circolare e poi resta a true fino al reorder
    private int maxStepNumber = 0;
    private boolean isHaccp = false;
    private int frequency = 0;
    private final String[] tableNames = new String[] { "hsvarhaccp", "hsvarhistor" };
    private String tableName = null;
    
    //private String suffixTableName=null;

    
    //Aggiunte a modifiche funzionali
    private long timeStampFirstLine = 0; //tempo di salvataggio della variabile 1 per record 
    private int addN=0; //L'n cumulativo
    private int variableType=0;
    
    //Ulteriori Modifiche funzionali
    private   int LIMIT_TIME=607; //tempo oltre il quale devo scaricare lo statements
    private Integer[] timesN=new Integer[VALUES-1];
	private Float[] valuesN=new Float[VALUES-1];
	private int flushN=0;//contatore tempo                       
	private int startIndexFlush=0;
	private int endIndexFlush=0;
	private boolean firstRun=true;//avvio del motore
    
    //private boolean isFirstTime = true; //la prima volta che si chiede il salvataggio della variabile
    public DataDynamicSaveMember(VariableInfo variableInfo, int maxStepNumber)
    {
        idsite = (short) variableInfo.getSite().intValue();
        idvariable = variableInfo.getId().intValue();
        epsilon = (float) variableInfo.getVariation();
        isHaccp = variableInfo.isHaccp();
        tableName = new String(isHaccp ? tableNames[0] : tableNames[1]);
        frequency = variableInfo.getFrequency().intValue();
        this.maxStepNumber = maxStepNumber;

        //Fare Molta attenzione a questa parte
        keyMax = (short) (variableInfo.getKeymax());
        keyActual = (short) (variableInfo.getKeyactual() + 1); 
        circularModeActivate = variableInfo.getIsTurn();
        variableType=variableInfo.getType();
        if (keyMax == keyActual)
        {
            circularModeActivate = true;
            keyActual = 0;
        } //if

        //Fine Fare Molta attenzione a questa parte
    } //DataDynamicSaveMember

    public synchronized void  createRecordToEnqueue(FDBQueue queue)
    {
    	/*
    	 * Trava
    	 * Modifica per alleggerire il carico sulla base dati.
    	 * Eliminazione degli allarmi dalle tabelle dello storico:
    	 * - HSVARHYSTOR;
    	 * - HSVARHACCP;
    	 */
    	if(variableType==VariableInfo.TYPE_ALARM)
    		return;
    	// FINE 
    	
    	
    	Object[] valuesHistorical = null;
		Object[] valuesBuffer = null;
		StringBuffer sqlHistorical = new StringBuffer();
		StringBuffer sqlBuffer = new StringBuffer();
		
		if(firstRun){
			timeStampFirstLine=System.currentTimeMillis();
			firstRun=false;
		}//if
		addN++;
		flushN++;
		
		// adjust counters, basing on the current timestamp (clock sinchronization can be necessary in case of system delays)
		int syncClock= synchronizeClock(addN-1,frequency,timeStampFirstLine);
		n+=syncClock;addN+=syncClock;flushN+=syncClock;
		
		// when enqueueVariable == FALSE ( --> no difference between current a previous variable value)
    	// AND it is not the first value of the table row
    	// AND the 'limit time' (607 secs) is passed
    	// then 'flush' the value (update 'n' value on hsvarhistor table)
    	if ((!enqueVariable)&&(position!=0))
        {
    		n++;
 
    		if (n < maxStepNumber) //n%=FORCE_SAVE_CICLE
            {
            	//controllo per non aggiornare le variabili d'allarme
            	if(variableType==VariableInfo.TYPE_ALARM){
            		if(newValue==null)
            			return;
            		if(newValue.floatValue()==0)
            			return;
            	}//if
            		
             	timesN[position-1]=new Integer(n-1);
            	valuesN[position-1]=(newValue==null?null:new Float(newValue.floatValue()));
            	endIndexFlush=position;
            	if(checkLimitTime()){
            		
	        		 try
	        		 {
	                 	FileMgr.getInstance().putValue(idvariable, this.newValue);
	                 }
	                 catch(Exception e){}
            		 
        			valuesHistorical = new Object[(endIndexFlush-startIndexFlush)*2+2];
                	sqlHistorical= new StringBuffer();
                	sqlHistorical.append("UPDATE ");
                    sqlHistorical.append(tableName);
                    sqlHistorical.append(" SET  ");
                    int i=startIndexFlush,j=0;
                    for(;i<endIndexFlush-1;i++){
                    	sqlHistorical.append("value" + (i+1) + "=?,n" + (i+1) + "=? ,");
                    	valuesHistorical[j++]=valuesN[i];
                    	valuesHistorical[j++]=timesN[i];
                    }//for
                	valuesHistorical[j++]=valuesN[i];
                	valuesHistorical[j++]=timesN[i];
                	sqlHistorical.append("value" + endIndexFlush + "=?,n" + endIndexFlush + "=? ");
                    sqlHistorical.append("WHERE idvariable=?  AND  key=?");
                    valuesHistorical[valuesHistorical.length-2] = new Integer(idvariable);
                    valuesHistorical[valuesHistorical.length-1] = new Short(keyActual);
                    startIndexFlush= endIndexFlush-1;
                    queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
                    endIndexFlush=startIndexFlush;
                }//if
            	
            	
               return;
            }//if
        }//if
      

        if (position == 0)
        {
            if (!circularModeActivate)
            {
            	//AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            	//crateSuffixTableName(timeStamp);
                
            	//primo insert
                valuesHistorical = new Object[INSERT_FIELDS];
                sqlHistorical.append("INSERT INTO ");
                sqlHistorical.append(tableName);
                
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                //sqlHistorical.append(suffixTableName);
                
                sqlHistorical.append(" VALUES(?,?,?,?,?,?,?)");
                valuesHistorical[0] = new Short(idsite);
                valuesHistorical[1] = new Integer(idvariable);
                valuesHistorical[2] = new Short(keyActual);
                valuesHistorical[3] = new Integer(frequency);
                valuesHistorical[4] = new Long(System.currentTimeMillis());//uso adesso lo status per il timing continuo al posto di status.getAllStatus()
                valuesHistorical[5] = new Timestamp(timeStamp);
                valuesHistorical[6] = newValue;
            } //if
            else
            {
            	//UPDATE diviene DELETE + INSERT
            	
            	/*
            	//DELETE 
            	
            	valuesHistorical = new Object[DELETE_FIELDS];
            	sqlHistorical.append("DELETE from ");
            	sqlHistorical.append(tableName);
            	sqlHistorical.append(" WHERE idvariable=?  AND  key=?");
            	valuesHistorical[0] = new Integer(idvariable);
                valuesHistorical[1] = new Short(keyActual);
            	
                queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
                
            	//AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            	crateSuffixTableName(timeStamp);

                
                //INSERT
            	
                sqlHistorical = new StringBuffer();
            	valuesHistorical = new Object[INSERT_FIELDS];
                sqlHistorical.append("INSERT INTO ");
                sqlHistorical.append(tableName);
                
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                sqlHistorical.append(suffixTableName);
               
                
                sqlHistorical.append(" VALUES(?,?,?,?,?,?,?)");
                valuesHistorical[0] = new Short(idsite);
                valuesHistorical[1] = new Integer(idvariable);
                valuesHistorical[2] = new Short(keyActual);
                valuesHistorical[3] = new Integer(frequency);
                valuesHistorical[4] = new Long(System.currentTimeMillis());//uso adesso lo status per il timing continuo al posto di status.getAllStatus()
                valuesHistorical[5] = new Timestamp(timeStamp);
                valuesHistorical[6] = newValue;
                */
            	
                //primo update in sovrascrittura come insert
                valuesHistorical = new Object[UPDATE_AS_INSERT_FIELDS];
                sqlHistorical.append("UPDATE ");
                sqlHistorical.append(tableName);
                sqlHistorical.append(
                    " SET  key=?, frequency=?, status=?, inserttime=?, value=?,n1=NULL ," +
                    "value1=NULL ,n2=NULL ,value2=NULL ,n3=NULL ,value3=NULL ,n4=NULL ," +
                    "value4=NULL ,n5=NULL ,value5=NULL ,n6=NULL ,value6=NULL ,n7=NULL ,value7=NULL ," +
                    "n8=NULL ,value8=NULL ,n9=NULL ,value9=NULL ,n10=NULL ,value10=NULL ,n11=NULL ," +
                    "value11=NULL ,n12=NULL ,value12=NULL ,n13=NULL ,value13=NULL ,n14=NULL ," +
                    "value14=NULL ,n15=NULL ,value15=NULL ,n16=NULL ,value16=NULL ,n17=NULL ," +
                    "value17=NULL ,n18=NULL ,value18=NULL ,n19=NULL ,value19=NULL ,n20=NULL ," +
                    "value20=NULL ,n21=NULL ,value21=NULL ,n22=NULL ,value22=NULL ,n23=NULL ," +
                    "value23=NULL ,n24=NULL ,value24=NULL ,n25=NULL ,value25=NULL ,n26=NULL ," +
                    "value26=NULL ,n27=NULL ,value27=NULL ,n28=NULL ,value28=NULL ,n29=NULL ," +
                    "value29=NULL ,n30=NULL ,value30=NULL ,n31=NULL ,value31=NULL ,n32=NULL ," +
                    "value32=NULL ,n33=NULL ,value33=NULL ,n34=NULL ,value34=NULL ,n35=NULL ," +
                    "value35=NULL ,n36=NULL ,value36=NULL ,n37=NULL ,value37=NULL ,n38=NULL ," +
                    "value38=NULL ,n39=NULL ,value39=NULL ,n40=NULL ,value40=NULL ,n41=NULL ," +
                    "value41=NULL ,n42=NULL ,value42=NULL ,n43=NULL ,value43=NULL ,n44=NULL ," +
                    "value44=NULL ,n45=NULL ,value45=NULL ,n46=NULL ,value46=NULL ,n47=NULL ," +
                    "value47=NULL ,n48=NULL ,value48=NULL ,n49=NULL ,value49=NULL ,n50=NULL ," +
                    "value50=NULL ,n51=NULL ,value51=NULL ,n52=NULL ,value52=NULL ,n53=NULL ," +
                    "value53=NULL ,n54=NULL ,value54=NULL ,n55=NULL ,value55=NULL ,n56=NULL ," +
                    "value56=NULL ,n57=NULL ,value57=NULL ,n58=NULL ,value58=NULL ,n59=NULL ," +
                    "value59=NULL ,n60=NULL ,value60=NULL ,n61=NULL ,value61=NULL ,n62=NULL ," +
                    "value62=NULL ,n63=NULL ,value63=NULL, n64=NULL ");
                sqlHistorical.append("WHERE idvariable=?  AND  key=?");
                valuesHistorical[0] = new Short(keyActual);
                valuesHistorical[1] = new Integer(frequency);
                valuesHistorical[2] = new Long(System.currentTimeMillis());//uso adesso lo status per il timing continuo al posto di status.getAllStatus()
                valuesHistorical[3] = new Timestamp(timeStamp);
                valuesHistorical[4] = newValue;
                valuesHistorical[5] = new Integer(idvariable);
                valuesHistorical[6] = new Short(keyActual);
                
            } //else
            queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
            
            if(endIndexFlush==VALUES-1){
            	valuesHistorical = new Object[(endIndexFlush-startIndexFlush)*2+2];
            	sqlHistorical= new StringBuffer();
            	sqlHistorical.append("UPDATE ");
                sqlHistorical.append(tableName);
                
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                //sqlHistorical.append(suffixTableName);
                
                sqlHistorical.append(" SET  ");
                int i=startIndexFlush,j=0;
                for(;i<endIndexFlush-1;i++){
                	sqlHistorical.append("value" + (i+1) + "=?,n" + (i+1) + "=? ,");
                	valuesHistorical[j++]=valuesN[i];
                	valuesHistorical[j++]=timesN[i];
                }//for
            	valuesHistorical[j++]=valuesN[i];
            	valuesHistorical[j++]=timesN[i];
            	sqlHistorical.append("value" + endIndexFlush + "=?,n" + endIndexFlush + "=? ");
                sqlHistorical.append("WHERE idvariable=?  AND  key=?");
                valuesHistorical[valuesHistorical.length-2] = new Integer(idvariable);
                valuesHistorical[valuesHistorical.length-1] = new Short((short) ((keyActual-1+keyMax)%keyMax));
                startIndexFlush= endIndexFlush%(VALUES-1);
                queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
                endIndexFlush=startIndexFlush;
                
            }//if
         
            flushN=1;
            addN=1;
            timeStampFirstLine=timeStamp;
            valuesBuffer = new Object[BUFFER_FIELD];
            sqlBuffer.append("UPDATE buffer SET keyactual=?,isturn=? ");
            sqlBuffer.append("WHERE idvariable=? AND idsite=?");
            valuesBuffer[0] = new Short(keyActual);
            valuesBuffer[1] = new Boolean(circularModeActivate);
            valuesBuffer[2] = new Integer(idvariable);
            valuesBuffer[3] = new Integer(idsite);

            queue.enqueueVariable(valuesBuffer, sqlBuffer.toString());
        } //if (position == 0)
        else
        {
        	timesN[position-1]=new Integer(n);
        	valuesN[position-1]=(newValue==null?null:new Float(newValue.floatValue()));
        	endIndexFlush=position;
        	if(checkLimitTime()){
            	valuesHistorical = new Object[(endIndexFlush-startIndexFlush)*2+2];
            	sqlHistorical= new StringBuffer();
            	sqlHistorical.append("UPDATE ");
                sqlHistorical.append(tableName);
                
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                //sqlHistorical.append(suffixTableName);
                
                sqlHistorical.append(" SET  ");
                int i=startIndexFlush,j=0;
                for(;i<endIndexFlush-1;i++){
                	sqlHistorical.append("value" + (i+1) + "=?,n" + (i+1) + "=? ,");
                	valuesHistorical[j++]=valuesN[i];
                	valuesHistorical[j++]=timesN[i];
                }//for
            	valuesHistorical[j++]=valuesN[i];
            	valuesHistorical[j++]=timesN[i];
            	sqlHistorical.append("value" + endIndexFlush + "=?,n" +endIndexFlush + "=? ");
                sqlHistorical.append("WHERE idvariable=?  AND  key=?");
                valuesHistorical[valuesHistorical.length-2] = new Integer(idvariable);
                valuesHistorical[valuesHistorical.length-1] = new Short(keyActual);
                startIndexFlush= endIndexFlush%(VALUES-1);
                queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
                endIndexFlush=startIndexFlush;
            }//if
      
        } //else

        n = 1;

        position++;

        if (position == VALUES)
        { //position%=VALUES;
            position = 0;
            status.resetStatus();
        }

        if (position == 0)
        {
            keyActual++;
        }

        if (keyActual == keyMax)
        {
            circularModeActivate = true;
            keyActual = 0;
        } //if

      
    } //createRecordToEnqueue

    
    public synchronized void createRecordToEnqueueBeforeStop(FDBQueue queue)
    {
    	/*
    	 * Trava
    	 * Modifica per alleggerire il carico sulla base dati.
    	 * Eliminazione degli allarmi dalle tabelle dello storico:
    	 * - HSVARHYSTOR;
    	 * - HSVARHACCP;
    	 */
    	if(variableType==VariableInfo.TYPE_ALARM)
    		return;
    	// FINE 
    	
    	 Object[] valuesHistorical = null;
         StringBuffer sqlHistorical = new StringBuffer();
      
         try{
	         if((endIndexFlush-startIndexFlush>0)&&(endIndexFlush-startIndexFlush<VALUES-1)){
	        	
	    	   	valuesHistorical = new Object[(endIndexFlush-startIndexFlush)*2+2*((timesN[endIndexFlush-1].intValue()>1)?1:0)];
	         	sqlHistorical= new StringBuffer();
	         	sqlHistorical.append("UPDATE ");
	            sqlHistorical.append(tableName);
	            
	            //AAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                //sqlHistorical.append(suffixTableName);
               
	            
	            sqlHistorical.append(" SET  ");
	            boolean atLeastOneVariableWrite=false;
	            int i=startIndexFlush,j=0;
	            for(;i<endIndexFlush-1;i++){
	            	atLeastOneVariableWrite=true;
	             	sqlHistorical.append("value" + (i+1) + "=?,n" + (i+1) + "=? ,");
	             	valuesHistorical[j++]=valuesN[i];
	             	valuesHistorical[j++]=timesN[i];
	             }//for
	         	if(timesN[i].intValue()>1){
	         		atLeastOneVariableWrite=true;
		            valuesHistorical[j++]=valuesN[i];
		         	valuesHistorical[j++]=new Integer(timesN[i].intValue()-1);
		         	sqlHistorical.append("value" + endIndexFlush + "=?,n" +endIndexFlush + "=? ");
	         	} else 
	         		sqlHistorical=new StringBuffer(sqlHistorical.toString().substring(0,sqlHistorical.toString().length()-2));
	         	sqlHistorical.append(" WHERE idvariable=?  AND  key=?");
	            valuesHistorical[valuesHistorical.length-2] = new Integer(idvariable);
	            valuesHistorical[valuesHistorical.length-1] = new Short(keyActual);
	            if(atLeastOneVariableWrite) //condizione equivalente if((endIndexFlush-startIndexFlush!=1)||(timesN[endIndexFlush-1].intValue()>1))
	            	queue.enqueueVariable(valuesHistorical, sqlHistorical.toString());
	         }//if
	         resetForChangeTime();
         }//try
         catch(Exception e){
        	 LoggerMgr.getLogger(this.getClass()).error("Error in Flush Cache VAR ID="+idvariable +" Exception "+e );
         }//catch
    }//createRecordToEnqueueBeforeStop
  
    public synchronized void setNewValue(Float newValue)
    {
        this.newValue = newValue;

        if (null != newValue)
        {
            if (null != oldValue)
            {
                if (0 == epsilon)
                {
                	//se il delta sulla base dati � 0, allora la disuguaglianza deve essere stretta
                    if (Math.abs(newValue.floatValue() - oldValue.floatValue()) > epsilon)
                    {
                        enqueVariable = true;
                    }
                    else
                    {
                        enqueVariable = false;
                    }
                }
                else
                {
                	//Se il delta sulla base dati � diverso da 0, allora la disuguaglianza � debole
                    if (Math.abs(newValue.floatValue() - oldValue.floatValue()) >= epsilon)
                    {
                        enqueVariable = true;
                    }
                    else
                    {
                        enqueVariable = false;
                    }
                }
            }
            else
            {
                enqueVariable = true;
            }
        }
        else
        {
            if (null != oldValue)
            {
                enqueVariable = true;
            }
            else
            {
                enqueVariable = false;
            }
        }
        if(enqueVariable)
        {
        	this.oldValue = this.newValue;
            // modifica architettura per datatransfer - TMP OFF UNTIL NEW RELEASE
            try
            {
            	FileMgr.getInstance().putValue(idvariable, this.newValue);
            }
            catch(Exception e){}        	
        }
    } //setNewValue

    public synchronized void setStatus(boolean active)
    {
        if (active)
        {
            this.status.setStatus(position, (long) Status.ACTIVE);
        }
        else
        {
            this.status.setStatus(position, (long) Status.DISABLE);
        }
    }

    public synchronized void setTime(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }//setTime
    
    public long getTime()
    {
        return this.timeStamp;
    }//getTime
    

    //Used BeforeStop 
    public synchronized void setExternalForceSaveData()
    {
        enqueVariable = true;
        
    } //setExternalForceSaveData
    

    
    private synchronized void resetForChangeTime() {
    	   position = 0;
           status.resetStatus();
           keyActual++;

           if (keyActual == keyMax)
           {
               circularModeActivate = true;
               keyActual = 0;
           } //if

           oldValue = null;
           endIndexFlush=startIndexFlush=0;
	}//resetForChangeTime

    public synchronized void changeTime()
    {
        position = 0;
        status.resetStatus();

            keyActual++;
        

        if (keyActual == keyMax)
        {
            circularModeActivate = true;
            keyActual = 0;
        } //if

        oldValue = null;
    } //changeTime
    

    
    private synchronized int synchronizeClock(int clockCicles,int frequency,long time){
    	long reallyNow=System.currentTimeMillis();
    	long ipotheticNow=time+(long)clockCicles*(long)frequency*1000L;
    	long delta=(reallyNow-ipotheticNow)/1000L;
       	//System.out.println("Now "+new Timestamp(reallyNow)+" IpotethicNow "+new Timestamp(ipotheticNow)+" D "+(int)delta/frequency+" F "+frequency);
    	if(delta>0)
       		return (int)delta/frequency;
       	return 0;
    }//synchronizeClock
    
    
    private synchronized boolean checkLimitTime(){
    	//flussho
    	if(flushN*frequency>=LIMIT_TIME){
    		flushN=0;

    		/*try
            {
            	FileMgr.getInstance().putValue(idvariable, this.newValue);
            }
            catch(Exception e){}        	*/
    		
    		return true;
    	}//if
    	return false;
    }//checkLimitTime
    
    public synchronized Object[][] getGraphReportCacheData(){
      	Object[][] objects=null;
        long time=timeStampFirstLine;
    	if((endIndexFlush-startIndexFlush>0)&&(endIndexFlush-startIndexFlush<VALUES-1)){
    		objects=new Object[2][];
        	objects[0]= new Object[endIndexFlush-startIndexFlush-1];
        	objects[1]= new Object[endIndexFlush-startIndexFlush-1];
        	for(int i=0;i<startIndexFlush;i++)
        		time+=(timesN[i].longValue()*frequency*1000L);
        	
        	for(int i=startIndexFlush;i<endIndexFlush-1;i++){
        		time+=(timesN[i].longValue()*frequency*1000L);
        		objects[0][i-startIndexFlush]= new Long(time);
        		objects[1][i-startIndexFlush]= valuesN[i];
        	}//for
        	return objects;
        }//if
         return null;   
    	
    }//getGraphData

	public short getKeyActual() {
		return keyActual;
	}
    
    /*
    private void crateSuffixTableName(long timemillisecons){
    	Calendar calendar= new GregorianCalendar();
		calendar.setTimeInMillis(timemillisecons);
		int month=calendar.get(GregorianCalendar.MONTH)+1;
		suffixTableName="_"+"y"+calendar.get(GregorianCalendar.YEAR)+"m"+((month<10)?("0"+month):month);
    }//getSuffixTableName
    */
    
} //Class DataDynamicSaveMember
