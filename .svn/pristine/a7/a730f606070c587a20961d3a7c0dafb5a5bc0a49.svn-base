package com.carel.supervisor.dataaccess.history.organize;

import java.util.GregorianCalendar;




public class TableHsDrop implements ITableHS{
	
	 
	
	private GregorianCalendar calendarDrop=null;
	
	
	
	private String []operations=new String[2];
	
	//Formato tablename esempio hsvarhistor_y2010m12 o hsvarhaccp_y2010m12
	public TableHsDrop(String tableName){
		String []data= tableName.split("_");
		int year=Integer.parseInt(data[1].substring(1,5));
		int month=Integer.parseInt(data[1].split("m")[1]);
		calendarDrop=new GregorianCalendar(year,month-1,1);
		
		/*operations[0]="TRUNCATE TABLE "+tableName;
		operations[1]="REINDEX TABLE "+tableName;
		operations[2]="VACUUM FULL "+tableName;
		
		
		operations[3]="TRUNCATE TABLE "+"hsvarhistor"+"_"+data[1];
		operations[4]="REINDEX TABLE "+"hsvarhistor"+"_"+data[1];
		operations[5]="VACUUM FULL " +"hsvarhistor"+"_"+data[1];*/
		operations[0]="DROP TABLE "+tableName + " CASCADE";
		operations[1]="DROP TABLE "+"hsvarhistor"+"_"+data[1]+" CASCADE";
	}
	

	
	public long getTimeInMillis(){
		return calendarDrop.getTimeInMillis();
	}
	

	public String[] getOperations() {
		
		return operations;
	}
	
	/*
	public static void main(String[] args) {
		TableHsDrop tableHsDrop=new TableHsDrop("hsvarhaccp_y2010m01");
		String []sql=tableHsDrop.getOperations();
		
		for(int i=0;i<sql.length;i++)
			System.out.println(sql[i]);
		
		
	}
*/

}
