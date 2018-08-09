package com.carel.supervisor.director;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.director.graph.YGraphCoordinates;
import com.carel.supervisor.field.DataDynamicSaveMember;

public class RecoveryData {
	
	public static final int RECOVERY_NOREGULAR_STOP=0;
	public static final int RECOVERY_CHANGE_TIME_FORWARD=1;
	public static final int RECOVERY_CHANGE_TIME_BACK=2;
	
	
	 
	public RecoveryData(){
		
	}

	public  void startRecovery(int type) throws Exception{
		
		switch(type){
			case RECOVERY_NOREGULAR_STOP:
			case RECOVERY_CHANGE_TIME_FORWARD:
			case RECOVERY_CHANGE_TIME_BACK:
				recoveryNoRegularStop();
			break;
		}//switch
		
	}//startRecovery
	
	private void recoveryNoRegularStop() throws Exception{
		StringBuffer sql=null;
		Connection connection= null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		for(int i=0;i<GraphConstant.tableNames.length;i++){
			sql= new StringBuffer();
			sql.append("SELECT ");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(".idsite,");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(".idvariable,keyactual,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,");
			sql.append(" n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,");
			sql.append(" n21,n22,n23,n24,n25,n26,n27,n28,n29,n30,");
			sql.append(" n31,n32,n33,n34,n35,n36,n37,n38,n39,n40,");
			sql.append(" n41,n42,n43,n44,n45,n46,n47,n48,n49,n50,");
			sql.append(" n51,n52,n53,n54,n55,n56,n57,n58,n59,n60,");
			sql.append(" n61,n62,n63,n64 FROM ");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(",buffer WHERE ");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(".idsite=buffer.idsite ");
			sql.append(" AND ");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(".idvariable=buffer.idvariable ");
			sql.append(" AND ");
			sql.append(GraphConstant.tableNames[i]);
			sql.append(".key=buffer.keyactual ");
			sql.append(" AND buffer.keyactual<>?");
			connection= DatabaseMgr.getInstance().getConnection(null);
			preparedStatement=connection.prepareStatement(sql.toString());
			preparedStatement.setShort(1,(short) -1);
			resultSet=preparedStatement.executeQuery();
			if(resultSet!=null){
				while(resultSet.next()){
					try{
						for(int j=0;j<=DataDynamicSaveMember.VALUES;j++){
							if(resultSet.getObject(j+4)==null){
								recoveryRow((Integer)resultSet.getObject(1),(Integer)resultSet.getObject(2),(Integer)resultSet.getObject(3),GraphConstant.tableNames[i],j+1);
								break;
							}//if
							if(((Integer)resultSet.getObject(j+4)).shortValue()==0)
								break;//riga ok
						}//for
					}//catch
					catch(Exception e){
						LoggerMgr.getLogger(this.getClass()).error("RecoveryData Error idSite = "+(Integer)resultSet.getObject(1) + " idVariable = "+(Integer)resultSet.getObject(2)+" key "+(Integer)resultSet.getObject(3)+e);
					}//catch
				}//while
			}//if
			DatabaseMgr.getInstance().closeConnection(null, connection);
		}//for
		
	}//recoveryNoRegularStop

	private void recoveryRow(Integer idSite,Integer idVariable,Integer key,String tableName,int position ) throws Exception{
		StringBuffer sql= new StringBuffer();
		Connection connection= DatabaseMgr.getInstance().getConnection(null);
		PreparedStatement preparedStatement=null;
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET n");
		sql.append(position);
		sql.append("=0 ");
		sql.append(" WHERE idsite=? AND idvariable=? AND key=?");
		preparedStatement=connection.prepareStatement(sql.toString());
		preparedStatement.setInt(1,idSite.intValue());
		preparedStatement.setInt(2,idVariable.intValue());
		preparedStatement.setShort(3,key.shortValue());
		preparedStatement.execute();
		connection.commit();
		DatabaseMgr.getInstance().closeConnection(null, connection);
	}//recoveryRow

	

		
	
}//RecoveryData
