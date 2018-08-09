package com.carel.supervisor.dataaccess.history.organize;

import java.util.GregorianCalendar;




public class TableHsCreate implements ITableHS{
	
	 
	
	private GregorianCalendar calendarCreateStart=null;
	private GregorianCalendar calendarCreateEnd=null;
	
	
	
	private String []tableType=new String[]{"hsvarhistor","hsvarhaccp"}; //hsvarhistor, hsvarhaccp
	private String []operations=new String[8];
	
	
	
	//Formato tablename esempio hsvarhistor_y2010m12 o hsvarhaccp_y2010m12
	public TableHsCreate(String tableName){
		String []data= tableName.split("_");
		int year=Integer.parseInt(data[1].substring(1,5));
		int month=Integer.parseInt(data[1].split("m")[1]);
		calendarCreateStart=new GregorianCalendar(year,month-1,1); //già +1 rispetto al month
		calendarCreateEnd=new GregorianCalendar(year,month,1);
		getSQL2Create();
	}
	
	
	

	public String getSQL2Create(){
		
		StringBuffer tmp= new StringBuffer();
		StringBuffer prefixTableName= new StringBuffer();
		
		int monthStart=calendarCreateStart.get(GregorianCalendar.MONTH)+1;
		int monthEnd=calendarCreateEnd.get(GregorianCalendar.MONTH)+1;
		
		for(int i=0,j=0;i<2;i++){
			tmp.append("CREATE TABLE ");
			tmp.append(tableType[i]);
			tmp.append("_");
			
			prefixTableName= new StringBuffer();
			prefixTableName.append("y");
			prefixTableName.append(calendarCreateStart.get(GregorianCalendar.YEAR));
			prefixTableName.append("m");
			
			
			if(monthStart<10)
				prefixTableName.append("0");
			prefixTableName.append(monthStart);
			
			tmp.append(prefixTableName.toString());
			tmp.append(" ( ");
			tmp.append("CHECK (");
		
			 //'2004-02-01' AND logdate < TIMESTAMP '2004-03-01' )
			
			tmp.append("inserttime >= ");
			tmp.append("TIMESTAMP WITH TIME ZONE '");
			tmp.append(calendarCreateStart.get(GregorianCalendar.YEAR));
			tmp.append("-");
			if(monthStart<10)
				tmp.append("0");
			tmp.append(monthStart);
			tmp.append("-");
			tmp.append("01'");
			tmp.append(" AND ");
			tmp.append("inserttime < TIMESTAMP WITH TIME ZONE '");
			
			tmp.append(calendarCreateEnd.get(GregorianCalendar.YEAR));
			tmp.append("-");
			if(monthEnd<10)
				tmp.append("0");
			tmp.append(monthEnd);
			tmp.append("-");
			tmp.append("01' )");
		
		
			tmp.append(" ) ");
			tmp.append(" INHERITS (");
			tmp.append(tableType[i]);
			tmp.append(");");
			
			operations[j++]=tmp.toString();
			tmp= new StringBuffer();
			
			
			tmp.append("CREATE RULE ");
			tmp.append(tableType[i]);
			tmp.append("_insert_");
			tmp.append(prefixTableName.toString());
			tmp.append(" AS ");
			tmp.append("ON INSERT TO ");
			tmp.append(tableType[i]);
			tmp.append(" WHERE ( ");
			tmp.append("inserttime >= TIMESTAMP WITH TIME ZONE ");
			
			tmp.append("'");
			tmp.append(calendarCreateStart.get(GregorianCalendar.YEAR));
			tmp.append("-");
			if(monthStart<10)
				tmp.append("0");
			tmp.append(monthStart);
			tmp.append("-");
			tmp.append("01'");
			
			//'2004-02-01'
			
			tmp.append(" AND inserttime < TIMESTAMP WITH TIME ZONE '");
			tmp.append(calendarCreateEnd.get(GregorianCalendar.YEAR));
			tmp.append("-");
			if(monthEnd<10)
				tmp.append("0");
			tmp.append(monthEnd);
			tmp.append("-");
			tmp.append("01' )");
		
			
			//		 '2004-03-01' 
			tmp.append(" DO INSTEAD INSERT INTO ");
			tmp.append(tableType[i]);
			tmp.append("_");
			tmp.append(prefixTableName);
			tmp.append(" VALUES (NEW.idsite,NEW.idvariable,NEW.\"key\",NEW.frequency,NEW.status,NEW.inserttime,NEW.value,NEW.n1,NEW.value1,NEW.n2,NEW.value2,NEW.n3,NEW.value3,NEW.n4,NEW.value4,NEW.n5,NEW.value5,NEW.n6,NEW.value6,NEW.n7,NEW.value7,NEW.n8,NEW.value8,NEW.n9,NEW.value9,NEW.n10,NEW.value10,NEW.n11,NEW.value11,NEW.n12,NEW.value12,NEW.n13,NEW.value13,NEW.n14,NEW.value14,NEW.n15,NEW.value15,NEW.n16,NEW.value16,NEW.n17,NEW.value17,NEW.n18,NEW.value18,NEW.n19,NEW.value19,NEW.n20,NEW.value20,NEW.n21,NEW.value21,NEW.n22,NEW.value22,NEW.n23,NEW.value23,NEW.n24,NEW.value24,NEW.n25,NEW.value25,NEW.n26,NEW.value26,NEW.n27,NEW.value27,NEW.n28,NEW.value28,NEW.n29,NEW.value29,NEW.n30,NEW.value30,NEW.n31,NEW.value31,NEW.n32,NEW.value32,NEW.n33,NEW.value33,NEW.n34,NEW.value34,NEW.n35,NEW.value35,NEW.n36,NEW.value36,NEW.n37,NEW.value37,NEW.n38,NEW.value38,NEW.n39,NEW.value39,NEW.n40,NEW.value40,NEW.n41,NEW.value41,NEW.n42,NEW.value42,NEW.n43,NEW.value43,NEW.n44,NEW.value44,NEW.n45,NEW.value45,NEW.n46,NEW.value46,NEW.n47,NEW.value47,NEW.n48,NEW.value48,NEW.n49,NEW.value49,NEW.n50,NEW.value50,NEW.n51,NEW.value51,NEW.n52,NEW.value52,NEW.n53,NEW.value53,NEW.n54,NEW.value54,NEW.n55,NEW.value55,NEW.n56,NEW.value56,NEW.n57,NEW.value57,NEW.n58,NEW.value58,NEW.n59,NEW.value59,NEW.n60,NEW.value60,NEW.n61,NEW.value61,NEW.n62,NEW.value62,NEW.n63,NEW.value63,NEW.n64  );");
		
			operations[j++]=tmp.toString();
			tmp= new StringBuffer();
			
			tmp.append("CREATE INDEX i_");
					tmp.append(tableType[i]);
			tmp.append(prefixTableName);
			tmp.append("key");
			tmp.append("  ON ");
			tmp.append(tableType[i]);
			tmp.append("_");
			tmp.append(prefixTableName);
			tmp.append(" USING btree  (idvariable, \"key\");");
			
			operations[j++]=tmp.toString();
			tmp= new StringBuffer();
			
			tmp.append("CREATE INDEX i_");
			tmp.append(tableType[i]);
			tmp.append(prefixTableName);
			tmp.append("time");
			tmp.append("  ON ");
			tmp.append(tableType[i]);
			tmp.append("_");
			tmp.append(prefixTableName);
			tmp.append(" USING btree  (idvariable, inserttime);");
			
			operations[j++]=tmp.toString();
			tmp= new StringBuffer();
		}
		
		return tmp.toString();
	}


	public static void main(String[] args) {
		TableHsCreate tableHsCreate=new TableHsCreate("hsvarhistor_y2009m12");
		String []sql=tableHsCreate.getOperations();
		
		for(int i=0;i<sql.length;i++)
			System.out.println(sql[i]);
		
		
	}


	public String[] getOperations() {
		return operations;
	}

}
