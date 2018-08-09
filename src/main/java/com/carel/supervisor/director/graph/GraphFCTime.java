package com.carel.supervisor.director.graph;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.TimeControl;

//class Graph Finde Change Time

public class GraphFCTime {
	
	private static final String CHANGE_TIME_POINT="ctp";
	private static final String CHANGE_TIME_WHO="ctw";
	
	private static final String OLDTIME="oldtime";
	private static final String NEWTIME="newtime";
	
	private static final String CHANGE_TIME_TYPE="changetimetype";
	
	private StringBuffer points=new StringBuffer();
	private StringBuffer whos=new StringBuffer();
	private GraphInformation graphInformation=null;
	
	public GraphFCTime(){
	}//GraphFCTime
	
	public GraphFCTime(GraphInformation graphInformation) {
		this.graphInformation=graphInformation;
	}//GraphFCTime
	
	
public void startFCTime(){
	StringBuffer sql=new StringBuffer();
	sql.append("SELECT ");
	sql.append(OLDTIME);
	sql.append(",");
	sql.append(CHANGE_TIME_TYPE);
	sql.append(" FROM hstimecontrol WHERE ");
	sql.append(OLDTIME);
	sql.append(" BETWEEN ? AND ? ");
	sql.append(" AND changetimetype != ?AND ");
	sql.append(OLDTIME);
	sql.append(" <=  ");
	sql.append(NEWTIME);
	Object []objects= new Object[]{graphInformation.getStartTime(),graphInformation.getEndTime(),TimeControl.OTHER};
	
	//trova i cambi all'indietro e prepara al disegno del rettangolo
	StringBuffer sql1=new StringBuffer();
	sql1.append("SELECT ");
	sql1.append(NEWTIME);
	sql1.append(",");
	sql1.append(OLDTIME);
	sql1.append(",");
	sql1.append(CHANGE_TIME_TYPE);
	sql1.append(" FROM hstimecontrol WHERE ");
	sql1.append(NEWTIME);
	sql1.append(" <= ? AND ");
	sql1.append(OLDTIME);
	sql1.append(" >= ? ");
	sql1.append(" AND changetimetype != ? AND ");
	sql1.append(NEWTIME);
	sql1.append(" <=  ");
	sql1.append(OLDTIME);
	
	
	Object []objects1= new Object[]{graphInformation.getEndTime(),graphInformation.getStartTime(),TimeControl.DST};
	
	
	try{
		RecordSet recordSet=DatabaseMgr.getInstance().executeQuery(null,sql.toString(),objects);
		for(int i=0;i<recordSet.size();i++){
			points.append(calculateXCoordinate((Timestamp) recordSet.get(i).get(0)));
			points.append(";");
			whos.append(((String)recordSet.get(i).get(1)).trim());
			whos.append(";");
		}//for
	
		
		recordSet=DatabaseMgr.getInstance().executeQuery(null,sql1.toString(),objects1);
		
		for(int i=0;i<recordSet.size();i++){
			String pointsTime[]=calculateXRectangleCoordinate((Timestamp) recordSet.get(i).get(0),(Timestamp) recordSet.get(i).get(1));
			if(pointsTime!=null){
				points.append(pointsTime[0]);
				points.append("-");
				points.append(pointsTime[1]);
				points.append(";");
				whos.append(((String)recordSet.get(i).get(2)).trim());
				whos.append(";");
			}//if
		}//for
	
	
	}//try
	catch(Exception e){
		LoggerMgr.getLogger(this.getClass()).error(e);
	}//catch
	
}

	private String calculateXCoordinate(Timestamp timestamp){
		long granularityTime=graphInformation.getTimeGranularity();
		long index=timestamp.getTime()-graphInformation.getStartTime().getTime();
    	
		return String.valueOf((int)(index/granularityTime)+graphInformation.flashParameters.W1);
		
	}//calculateXCoordinate
	
	private String[] calculateXRectangleCoordinate(Timestamp newTime,Timestamp oldTime){
		String []tmp=new String[2];
		long granularityTime=graphInformation.getTimeGranularity();
		long indexA=Math.max((newTime.getTime()-graphInformation.getStartTime().getTime())/granularityTime, 0);
		long indexB=Math.min((oldTime.getTime()-graphInformation.getStartTime().getTime())/granularityTime, graphInformation.flashParameters.getXWidth());
		tmp[0]= String.valueOf(indexA+graphInformation.flashParameters.W1);
		tmp[1]= String.valueOf(indexB+graphInformation.flashParameters.W1);
		return tmp;
	}//calculateXCoordinate
	
	

	public String getXMLFCTime(){
		StringBuffer xml= new StringBuffer();
		xml.append(" ");
		xml.append(CHANGE_TIME_POINT);
		xml.append("=\"");
		xml.append(points.toString());
		xml.append("\" ");
		xml.append(CHANGE_TIME_WHO);
		xml.append("=\"");
		xml.append(whos.toString());
		xml.append("\" ");
		return xml.toString();
	}//getXMLFCTime

	
}//Class GraphFCTime
