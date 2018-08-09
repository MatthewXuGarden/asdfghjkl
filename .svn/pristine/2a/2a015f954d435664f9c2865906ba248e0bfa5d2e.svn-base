package com.carel.supervisor.dispatcher.plantwatch1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.xml.XMLNode;

public class LoadXMLFile {
	
	public static void LoadHistoryVarFromFile(  String dirpath, 
												   String datafile,
												   PlantWatchHistoryVarList pwuvt)
	{
		try {
			
			byte[] fileBuffer = null;
			long l1 = System.currentTimeMillis(); 
			File file = new File(dirpath+"/"+datafile);
			FileInputStream isfile = new FileInputStream(dirpath+"/"+datafile);
			fileBuffer = new byte[(int)file.length()];
			if(isfile.read( fileBuffer)!=-1)
			{
				String stringFileBuffer = new String(fileBuffer);
				String[] arrayString = StringUtility.split( stringFileBuffer, ";");
				//nella prima c'é il valore nel secondo la data
				//SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
				 
				for(int i = 0; i<arrayString.length-1;  i=i+2)
				{
					Date parsed =  new Date(Long.valueOf( arrayString[i+1])*1000);
					pwuvt.Add( parsed, Double.valueOf( arrayString[i]));
				}
				//df= null;
			}
			l1 = System.currentTimeMillis()-l1;
			System.out.println("Time elapsed to process file "+datafile+ " sec = "+l1/1000); 
			
			//pwuvt.Add( parsed, Double.valueOf( value));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void LoadUnitVarTypeFromXMLFile( String dirpath, 
												   String xmlfile,
												   PlantWatchVarUnitType pwuvt)
	{
		try {
			//mi carico in una mappa le variabili del file xml e poi da pwuvt mi ricavo solo le storicizzate
			File file = new File(dirpath+"/"+xmlfile);
			XMLNode xmlNode = XMLNode.parse( file.toURL().openStream());
			XMLNode xmlNodePtr_1 = null;
			XMLNode xmlNodePtr_2 = null;
			XMLNode xmlNodePtr_3 = null;
			if(xmlNode!=null)
			{
				for(int i= 0; i<xmlNode.size(); i++)
				{
					xmlNodePtr_1 = xmlNode.getNode(i);
					if(xmlNodePtr_1.getNodeName().equals("Variable"))
					{
						String name = xmlNodePtr_1.getAttribute("Name");
						boolean datalogging = false;
						if(name.indexOf("DataLogging")>0)
							datalogging = true;
						try
						{
							pwuvt.Add( 	name,
										xmlNodePtr_1.getAttribute("Description"),
										Integer.parseInt(xmlNodePtr_1.getAttribute("Type")),
										Integer.parseInt(xmlNodePtr_1.getAttribute("Address")),
										datalogging);
						}catch(NumberFormatException ne)
				        {
				        	ne.printStackTrace(); 
				        }
					}
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void LoadConfFromXMLFile(	String dirpath, 
											String xmlfile, 
											PlantWatchSiteConfig pwsc)
	{
		try {
			File file = new File(dirpath+"/"+xmlfile);
			XMLNode xmlNode = XMLNode.parse( file.toURL().openStream());
			XMLNode xmlNodePtr_1 = null;
			XMLNode xmlNodePtr_2 = null;
			XMLNode xmlNodePtr_3 = null;
			if(xmlNode!=null)
			{
				for(int i= 0; i<xmlNode.size(); i++)
				{
					xmlNodePtr_1 = xmlNode.getNode(i);
					if(xmlNodePtr_1.getNodeName().equals("Unit"))
					{
						try
						{
							pwsc.Add( Integer.parseInt(xmlNodePtr_1.getAttribute("Id")),
									  xmlNodePtr_1.getAttribute("Type"),
									  xmlNodePtr_1.getAttribute("Description"),
									  Boolean.parseBoolean( xmlNodePtr_1.getAttribute("Disabled"))
									);
						}catch(NumberFormatException ne)
				        {
				        	ne.printStackTrace(); 
				        }
					}
					
				}
			}
			file = null;
							
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static void LoadVarFromXMLFile(String dirpath, 
										  String xmlfile, 
										  int unit, 
										  int type, 
										  PlantWatchUnitVars pwvs)
	{   
		
		try {
			File file = new File(dirpath+"/"+xmlfile);
			XMLNode xmlNode = XMLNode.parse( file.toURL().openStream());
			XMLNode xmlNodePtr_1 = null;
			XMLNode xmlNodePtr_2 = null;
			XMLNode xmlNodePtr_3 = null;

			
			if(xmlNode!=null)
			{
				for(int i= 0; i<xmlNode.size(); i++)
				{
					xmlNodePtr_1 = xmlNode.getNode(i);
					if(xmlNodePtr_1.getAttribute("A").equals(Integer.toString(unit)))
					{
						xmlNodePtr_2 = null;
						for(int j= 0; i<xmlNodePtr_1.size(); j++)
						{
							xmlNodePtr_2 = xmlNodePtr_1.getNode(j);
							//System.out.println( xmlNodePtr_2.getAttribute("Address"));
							xmlNodePtr_3 = xmlNodePtr_2.getNode(0);
							//System.out.println( xmlNodePtr_3.getTextValue());
							try
							{
								Double val = new Double(xmlNodePtr_3.getTextValue());
								pwvs.Add(type,  
										Integer.parseInt(xmlNodePtr_2.getAttribute("A")),
										val	
										);
							}catch(NumberFormatException ne)
					        {
					        	ne.printStackTrace(); 
					        }
							
						}
						break;
					}
				}
			}
			file = null;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static void LoadVarFromXMLFile(String dirpath, 
			  String xmlfile, 
			  int type, 
			  PlantWatchUnitVars pwvs)
	{
		try {
			File file = new File(dirpath+"/"+xmlfile);
			XMLNode xmlNode = XMLNode.parse( file.toURL().openStream());
			XMLNode xmlNodePtr_1 = null;
			XMLNode xmlNodePtr_2 = null;
			XMLNode xmlNodePtr_3 = null;

			
			if(xmlNode!=null)
			{
				
				xmlNodePtr_1 = xmlNode.getNode(0);
				xmlNodePtr_2 = null;
				for(int j= 0; j<xmlNodePtr_1.size(); j++)
				{
					xmlNodePtr_2 = xmlNodePtr_1.getNode(j);
					//System.out.println( xmlNodePtr_2.getAttribute("Address"));
					xmlNodePtr_3 = xmlNodePtr_2.getNode(0);
					//System.out.println( xmlNodePtr_3.getTextValue());
					try
					{
						Double val = new Double(xmlNodePtr_3.getTextValue());
						pwvs.Add(type,  
								Integer.parseInt(xmlNodePtr_2.getAttribute("A")),
								val	
								);
					}catch(NumberFormatException ne)
			        {
			        	ne.printStackTrace(); 
			        }
					
				}
					
			
			}
			file = null;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void LoadAlarmsFormXMLFile(	
												String dirpath, 
												String xmlfile, 
												Date   fromDate,
												PlantWatchAlarmList pwal
											)
	{
		try {
			File file = new File(dirpath+"/"+xmlfile);
			XMLNode xmlNode = XMLNode.parse( file.toURL().openStream());
			XMLNode xmlNodePtr_1 = null;
			XMLNode xmlNodePtr_2 = null;
			XMLNode xmlNodePtr_3 = null;
			if(xmlNode!=null)
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				
				for(int i= 0; i<xmlNode.size(); i++)
				{
					xmlNodePtr_1 = xmlNode.getNode(i);
				
					try
					{
						Date parsed = df.parse( xmlNodePtr_1.getAttribute("DT"));
						if(parsed.before(fromDate))
							continue;
						
						pwal.Add( xmlNodePtr_1.getAttribute("T"),
								  Integer.parseInt(xmlNodePtr_1.getAttribute("A")),
								  Integer.parseInt(xmlNodePtr_1.getAttribute("U")),
								  xmlNodePtr_1.getAttribute("D"),
								  parsed
								);
					}catch(NumberFormatException ne)
			        {
			        	ne.printStackTrace(); 
			        }
					
				}
				df = null;
			}
			file = null;
							
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
	
	
	
}
