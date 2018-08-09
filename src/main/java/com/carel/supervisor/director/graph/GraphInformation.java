package com.carel.supervisor.director.graph;

import java.sql.Timestamp;

import com.carel.supervisor.base.util.queue.Queue;
import com.carel.supervisor.director.graph.GraphConstant;


public class GraphInformation extends Queue
{
    //Object [] da accodare è cosi fatto {idSite,idVariable}
    public final static int INDEX_ID_SITE = 0;
    public final static int INDEX_ID_VARIABLE = 1;

    
    private Timestamp endTime = null; //tempo finale
    private Timestamp startTime=null; //tempo iniziale diviene utile per lo zoom in
    private int indexGraphPeriod = 0; //valori nell'insieme {MONTH,WEEK,DAY,HOURS12,etc. etc.}        
    private String graphType=null;
	FlashObjParameters flashParameters=null;
    
    
    public GraphInformation(int length)
    {
        super(length);
    } //GraphQueue

    public void setEndTime(Timestamp endTime)
    {
        this.endTime = endTime;
    } //setEndTime

    public void setFlashParameters(FlashObjParameters flashParameters)
    {
        this.flashParameters = flashParameters;
    } //setFlashParameters

    public FlashObjParameters getFlashParameters()
    {
        return flashParameters;
    } //getFlashParameters

    public void setGraphPeriod(int indexGraphPeriod)
    {
        this.indexGraphPeriod = indexGraphPeriod;
    } //setPeriodGraph

    public long getGraphPeriodInSeconds()
    {
        return GraphConstant.PERIOD[indexGraphPeriod] / 1000;
    } //getPeriodGraph

    public Timestamp getEndTime()
    {
        return endTime;
    } //getEndTime

    public Timestamp getStartTime()
    {
       
        return startTime;
    } //getStartTime
    
    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }//setStartTime
    
    public long getTimeGranularity(){
    	
    	return (endTime.getTime()-getStartTime().getTime())/(flashParameters.getXWidth()-1);
    
	}//getTimeGranularity

	public String getGraphType() {
		return graphType;
	}//getGraphType

	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}//setGraphType
    
    
} //Class GraphQueue
