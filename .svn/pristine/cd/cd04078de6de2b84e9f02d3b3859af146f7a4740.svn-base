package com.carel.supervisor.presentation.graph;

import java.sql.Timestamp;
import java.util.Properties;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.director.graph.FlashObjParameters;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.director.graph.GraphFCTime;
import com.carel.supervisor.director.graph.GraphInformation;
import com.carel.supervisor.director.graph.GraphRequest;
import com.carel.supervisor.director.graph.NormalizeSerie;
import com.carel.supervisor.director.graph.YGraphCoordinates;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.session.UserSession;

public class LoadGraph {

	public final static int NUM_INFO_4_ROW=8; 
	
	private StringBuffer header= null;
	private StringBuffer footer= null;
	private StringBuffer seriesData= new StringBuffer();
	private int numDigitalSeries=0;
	private GraphInformation graphInformation=null;
	private GraphFCTime graphFCTime=null;
	private Long maintime = 0l;
	
	public LoadGraph(Properties properties,UserSession userSession) {
		try {
			if (properties.getProperty("mainTime") != null) {
				maintime = new Long(properties.getProperty("mainTime"));
			}
		} catch (Exception e) {
			maintime = 0l;
		}
		String variableList=properties.getProperty("variableList");
		String typeGraph=properties.getProperty("typeGraph");
		String autoscale= (null == properties.getProperty("autoscale")) ? "false" : properties.getProperty("autoscale");
		String []valueInfo=null;
		Object [][]objects=null; 
		//int []idsVar=null;
		int []types=null;
		graphInformation = new GraphInformation(40);
		if(!variableList.equals("null")){
			valueInfo=variableList.split(";");
			int n=valueInfo.length%NUM_INFO_4_ROW;
			int num=valueInfo.length/NUM_INFO_4_ROW;
			if((n==0)&&(valueInfo.length!=0)){
				objects= new Object[num][NUM_INFO_4_ROW+1];
				//idsVar= new int[num];
				types= new int[num];
				for(int i=0,j=0;i<num;i++){
					objects[i][0] =new Integer(userSession.getIdSite());
					objects[i][1] =new Integer(valueInfo[j++]);//idVar
					//idsVar[i]=((Integer)objects[i][1]).intValue();
					objects[i][2] =valueInfo[j++] ;            //Variable Description
					objects[i][3] =valueInfo[j++] ;            //Type
					int type=new Integer((String)objects[i][3]).intValue();
					if((type==VariableInfo.TYPE_ALARM)||(type==VariableInfo.TYPE_DIGITAL)){
						types[i]=NormalizeSerie.TYPE_D;
						numDigitalSeries++;	
					}
					else
						types[i]=NormalizeSerie.TYPE_A;
					objects[i][4] =valueInfo[j++];            //Unit of Measure
					objects[i][5]= valueInfo[j++];            //Color
					objects[i][6]= valueInfo[j++]; //Device Description
					objects[i][7]=(valueInfo[j++].equals("null")||autoscale.equals("true"))?null:new Float(valueInfo[j-1]); //Y Min
					objects[i][8]=(valueInfo[j++].equals("null")||autoscale.equals("true"))?null:new Float(valueInfo[j-1]);; //Y Max
					
					// 20090304 - Commented out the following block, since it caused problems with some units of measurement (totally wrong ranges)
					//scala imposta nel caso non sia data dall'utente potrebbe anche non essere assegnata dopo il seguente, in tal caso autoscale
//					if((objects[i][7]==null)&&(types[i]==NormalizeSerie.TYPE_A)){
//						// Compute the default min/max range for each unit of measure
//						Object []objectsTmp=UnitMeasurementConstant.getScale((String) objects[i][4]);
//						objects[i][7]=objectsTmp[0];
//						objects[i][8]=objectsTmp[1];
//					}//if
					
					graphInformation.enqueRecord(objects[i]);
				}//for
			}//if
			
			FlashObjParameters flashParameters=new FlashObjParameters(numDigitalSeries,num-numDigitalSeries,userSession.getScreenHeight(),userSession.getScreenWidth(),420,35);
			String graphType=(String)properties.get("typeGraph");
			
			int timePeriod= new Integer(properties.getProperty("timeperiod")).intValue();
	
			if(timePeriod<0){
				String endXZoomIn=properties.getProperty("endXZoomIn");
				String startXZoomIn=properties.getProperty("startXZoomIn");
				if(endXZoomIn.contains("."))
					endXZoomIn=StringUtility.split(endXZoomIn, ".")[0];
				if(startXZoomIn.contains("."))
					startXZoomIn=StringUtility.split(startXZoomIn,".")[0];
				
				// time fix
				Long objEndXZoomIn = String2Time(properties.getProperty("endXZoomInCSV"));
				if( objEndXZoomIn != null )
					graphInformation.setEndTime(new Timestamp(objEndXZoomIn));
				else
					graphInformation.setEndTime(new Timestamp(new Long(endXZoomIn).longValue()));
				Long objStartXZoomIn = String2Time(properties.getProperty("startXZoomInCSV"));
				if( objStartXZoomIn != null )
					graphInformation.setStartTime(new Timestamp(objStartXZoomIn));
				else
					graphInformation.setStartTime(new Timestamp(new Long(startXZoomIn).longValue()));
				
			}//if Zoom In
			else{
				String mainTime=properties.getProperty("mainTime");
				if(mainTime.contains("."))
					mainTime=StringUtility.split(mainTime, ".")[0];
					
				graphInformation.setStartTime(new Timestamp(new Long(mainTime).longValue()));
				graphInformation.setEndTime(new Timestamp(new Long(mainTime).longValue()+GraphConstant.PERIOD[timePeriod]));
			
			}//else
			
			graphInformation.setGraphPeriod(timePeriod);
			graphInformation.setGraphType(graphType);
			graphInformation.setFlashParameters(flashParameters);
			
			graphFCTime= new GraphFCTime(graphInformation);	
			graphFCTime.startFCTime();
			
			GraphRequest graphRequest = null;
			try {
				graphRequest = new GraphRequest(graphInformation);
				graphRequest.startRetrieve();
				
			}//try
			catch (Exception e) {
				e.printStackTrace();
			}//catch
			
			YGraphCoordinates [][]coordinates= graphRequest.getGraphYsSeries();
	
	        //Interpretazione dei dati: il null implica il padding dal valore precedente al successivo il null,null indica il buco del grafo
			
			for (int i = 0; i < coordinates.length; i++)
			{
				NormalizeSerie normalizeSerie= new NormalizeSerie();
				normalizeSerie.setType(types[i]);
				normalizeSerie.startNormalize(coordinates[i],graphInformation.getFlashParameters(),objects[i], graphRequest);
				normalizeSerie.setParameters(objects[i]);
				seriesData.append(normalizeSerie.getXMLNormalized());
			}//for
		
		}//if
		try {
			// ENHANCEMENT 20090213 - If the request contains the "save_data" parameter, we save the new var config.
			if(new Integer((String) properties.get("timeperiod")).intValue()>=0 
					&& null != properties.get("save_data") 
					&& properties.getProperty("save_data").trim().length() > 0) {
				new ConfigurationGraphBeanList(typeGraph).automaticSaveConfiguration(properties,userSession);
			}
		}//try
		catch (Exception e) {
			
			e.printStackTrace();
		}//catch
	}//LoadGraph


	private StringBuffer pageHeader(){
		header= new StringBuffer();
		header.append("<?xml version='1.0' ?>");
		// 20090126 Changed old tag "curve" (italian) to "graphs"
		header.append("<graphs");
		header.append(graphFCTime==null?" ":graphFCTime.getXMLFCTime());
		header.append("anaNum=\"");
		header.append(graphInformation.getFlashParameters()!=null?graphInformation.getFlashParameters().getNumAnalogicSeries():0);
		header.append("\" digNum=\"");
		header.append(graphInformation.getFlashParameters()!=null?graphInformation.getFlashParameters().getNumDigitalSeries():0);
		
		// use a neutral time format; to be able to recreate time on the remote flash time zone
		header.append("\" maintimeCSV=\"");
		header.append(Time2String(maintime));
		
		header.append("\" maintime=\"" + maintime + "\">");
		return header;
	}//pageHeader
		
	private String pageFooter(){
		footer= new StringBuffer();
		footer.append("</graphs>");
		return footer.toString();
	}//pageFooter

	
	public String getHTML(){
		return (pageHeader().append(seriesData).append(pageFooter())).toString();
	}//getHTML
	
	
	// time fix
	private Long String2Time(String strTime)
	{
		if( strTime == null || strTime.length() <= 0 )
			return null;
		
		String astrTime[] = strTime.split(",");
		if( astrTime.length == 6 ) {
			Timestamp objTime = new Timestamp(Integer.parseInt(astrTime[0], 10) - 1900, Integer.parseInt(astrTime[1], 10), Integer.parseInt(astrTime[2], 10),
				Integer.parseInt(astrTime[3], 10), Integer.parseInt(astrTime[4], 10), Integer.parseInt(astrTime[5], 10), 0);
			return new Long(objTime.getTime());
		}
		else {
			return null;
		}
	}

	
	private String Time2String(long nTime)
	{
		Timestamp objTime = new Timestamp(nTime);
		return "" + (objTime.getYear() + 1900) + "," + objTime.getMonth() + "," + objTime.getDate() 
			+ "," + objTime.getHours() + "," + objTime.getMinutes() + "," + objTime.getSeconds();
	}
	
}//Class LoadGraph
