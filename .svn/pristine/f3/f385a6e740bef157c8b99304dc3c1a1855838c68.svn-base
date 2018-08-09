package com.carel.supervisor.dataaccess.hs;

import java.sql.Timestamp;


public class CreateSqlHs {

	private static String INSERT="I";
	private static String UPDATE="U";
	private static String DELETE="D";
	

	public static DataHs getInsertData(String cfTableName,Object []objects){
		StringBuffer sql= new StringBuffer();
		Object []objectsHs=new Object[objects.length+1];
	  	sql.append("INSERT INTO hs");
		sql.append(cfTableName);
		sql.append(" VALUES(?");
		for(int i=0;i<objects.length;i++){
			sql.append(",?");
		}//for
		sql.append(")");
		for(int i=0;i<objects.length;i++)
			objectsHs[i]=objects[i];
		objectsHs[objects.length]=new String(INSERT);
		return new DataHs(sql.toString(),objectsHs);
	}//getInsertData


	//Va chiamata DOPO dell'esecuzione dell'update nella tabella di configurazione
	public static DataHs getUpdateData(String cfTableName,String []fields,Object []objectsWHERE,String []conditionsWHERE,String []fieldsWHERE){
		return generalUpdateDelete(UPDATE,cfTableName,fields,objectsWHERE,conditionsWHERE,fieldsWHERE);
	}//getUpdateData

	
	//Va chiamata PRIMA dell'esecuzione del delete nella tabella di configurazione
	public static DataHs getDeleteData(String cfTableName,String []fields,Object []objectsWHERE,String []conditionsWHERE,String []fieldsWHERE){
		return generalUpdateDelete(DELETE,cfTableName,fields,objectsWHERE,conditionsWHERE,fieldsWHERE);
		
	}//

	private static DataHs generalUpdateDelete(String type,String cfTableName,String []fields,Object []objectsWHERE,String []conditionsWHERE,String []fieldsWHERE){
		StringBuffer sql= new StringBuffer();
		int n=objectsWHERE!=null?objectsWHERE.length+2:2;
		Object []objectsHs=new Object[n];
		objectsHs[0]=new Timestamp(System.currentTimeMillis());
		objectsHs[1]= new String(type);
		for(int i=2;i<n;i++)
			objectsHs[i]=objectsWHERE[i-2];
		sql.append("INSERT INTO hs");
		sql.append(cfTableName);
		sql.append("\n        (SELECT ");
        for(int i=0;i<fields.length;i++){
        	sql.append(fields[i]);
        	sql.append(",");
        }//for
        sql.append("?,? ");
        sql.append("\n         FROM  ");
        sql.append(cfTableName);
        if(objectsWHERE!=null){
        	sql.append("\n         WHERE ");
        	sql.append(fieldsWHERE[0]);
        	sql.append(conditionsWHERE[0]);
        	sql.append(" ? ");
        	for(int i=1;i<objectsWHERE.length;i++){
        		sql.append(" AND "); 
        		sql.append(fieldsWHERE[i]);
        		sql.append(conditionsWHERE[i]);
        		sql.append(" ? ");
            }//for
        }//if
        sql.append(")");
       return new DataHs(sql.toString(),objectsHs);
		
	}//generalUpdateInsert

}//Class CreateSqlHs


