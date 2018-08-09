package com.carel.supervisor.report;

import com.carel.supervisor.base.util.queue.Queue;

import java.sql.Timestamp;


public class ReportInformation extends Queue
{
    //	Object [] da accodare è cosi fatto {idSite,idVariable}
    public final static int INDEX_ID_SITE = 0;
    public final static int INDEX_ID_VARIABLE = 1;

    private Timestamp startTime = null;
    private Timestamp endTime = null; //tempo finale
    private long reportPeriod = 0; //periodo di interesse in millisecondi
    private long samplingPeriodRequest = 0; //periodo di campionamento richiesto per il report 
    private String reportType=null;
    private Integer timeValues[] = null;

    public ReportInformation(int length)
    {
        super(length);
    } //ReportInformation

    public void setStartTime(Timestamp startTime)
    {
    	this.startTime = startTime;
    }
    
    public void setEndTime(Timestamp endTime)
    {
        this.endTime = endTime;
    } //setEndTime

    public void setReportPeriod(long reportPeriod)
    {
        this.reportPeriod = reportPeriod;
    } //setPeriodGraph

    public long getReportPeriod()
    {
        return this.reportPeriod;
    } //getPeriodGraph

    public Timestamp getEndTime()
    {
        return endTime;
    } //getEndTime

    public Timestamp getStartTime()
    {
        /*if( startTime != null )
        	return startTime;
        else*/ if( endTime != null )
            return  new Timestamp(endTime.getTime() -this.reportPeriod);

        return null;
    } //getStartTime

    public long getSamplingPeriodRequest()
    {
        return samplingPeriodRequest;
    } //getSamplingPeriodRequest

    public void setSamplingPeriodRequest(long samplingPeriodRequest)
    {
        this.samplingPeriodRequest = samplingPeriodRequest;
    } //setSamplingPeriodRequest

	public String getReportType(){
		return reportType;
	}//getReportType

	public void setReportType(String reportType){
		this.reportType = reportType;
	}//setReportType
    
	public Integer[] getTimeValues() {
		return timeValues;
	}

	public void setTimeValues(Integer[] timeValues){
		this.timeValues = timeValues;
	}
} //Class ReportInformation
