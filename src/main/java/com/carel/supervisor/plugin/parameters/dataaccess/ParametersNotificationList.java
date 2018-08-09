package com.carel.supervisor.plugin.parameters.dataaccess;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.plugin.parameters.ParametersMgr;

public class ParametersNotificationList {
	

	public static void setNotifications(int[] lista){
		HashMap<Integer, String> tipi = new HashMap<Integer, String> ();
		String presql = "select idaddrbook, type from cfaddrbook;";
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null, presql);
			for (int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer id = (Integer) r.get("idaddrbook");
				String tipo = (String) r.get("type");
				tipi.put(id, tipo);
			}
		} catch (DataBaseException e1) {
			Logger logger = LoggerMgr.getLogger(ParametersNotificationList.class);
			logger.error(e1);
		}

		
		String sql="delete from parameters_notification_list;";
		String sql2="delete from cfaction where actiontype in ('"+ParametersMgr.EmailCode+"','"+ParametersMgr.FaxCode+"','"+ParametersMgr.SmsCode+"')";
		ParametersMgr.startingActionId=null;
		try {
			DatabaseMgr.getInstance().executeStatement(null, sql,null);
			DatabaseMgr.getInstance().executeStatement(null, sql2,null);
			
			sql="insert into parameters_notification_list values (?)";
			Object[] o = new Object[1];
			
			sql2="insert into cfaction values (?,?,?,?,?,?,?,?,?,?)";
			Object[] o2 = new Object[10];
			
			
			for (int i = 0; i < lista.length; i++) {
				Integer id = SeqMgr.getInstance().next(null, "cfaction", "idaction");
				
				o[0]=lista[i];
				
				o2[0]=id;
				if (ParametersMgr.startingActionId==null)
					ParametersMgr.startingActionId=id;
				o2[1]="firstPV";
				o2[2]=1;
				o2[3]="ParametersPlugin";
				o2[4]=ParametersMgr.startingActionId ;
				o2[5]=tipi.get(lista[i])+"X";
				o2[6]="TRUE";
				o2[7]=null;
				o2[8]=new Integer(lista[i]).toString();
				o2[9]=new Date(System.currentTimeMillis());
				
				DatabaseMgr.getInstance().executeStatement( sql, o);
				DatabaseMgr.getInstance().executeStatement( sql2,o2);
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(ParametersNotificationList.class);
			logger.error(e);
		}
		
	}
	
	public static String getOptionList()
	{
		StringBuffer sb = new StringBuffer();
		DispatcherBook[] list = null;
		String type = "";
		
		
		// only Email channel allowed. Other channles does not work properly. To be fixed.
		// Fax
		/*list = DispatcherBookList.getInstance().getReceiversByType("F");
		type = "FAX - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}
		
		// Sms
		list = DispatcherBookList.getInstance().getReceiversByType("S");
		type = "SMS - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}*/
		
		// Email
		list = DispatcherBookList.getInstance().getReceiversByType("E");
		type = "EMAIL - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}
		
		return sb.toString();
	}
	
	
	public static String getOptionListConf()
	{
		Set<Integer> insieme = new HashSet<Integer>();
		int[] listaConf= null;
		
		String sql = "select idaddrbook from parameters_notification_list;";
		try {
			RecordSet rs =  DatabaseMgr.getInstance().executeQuery(null, sql);
			
			listaConf = new int[rs.size()];
			
			for (int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer v = ((Integer) r.get("idaddrbook"));
				listaConf[i]=v.intValue();
				
				insieme.add(v);
			}
			
		} catch (DataBaseException e) {
			listaConf = new int[0];
			
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.plugin.parameters.dataaccess.ParametersNotificationList");
			logger.error(e);
			
		}
		
		
		StringBuffer sb = new StringBuffer();
		DispatcherBook[] list = null;
		String type = "";
		
		// only Email channel allowed. Other channles does not work properly. To be fixed.
		// Fax
		/*list = DispatcherBookList.getInstance().getReceiversByType("F");
		type = "FAX - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
			{
				if (insieme.contains(new Integer(list[i].getKey())))
					sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
			}
		}
		
		// Sms
		list = DispatcherBookList.getInstance().getReceiversByType("S");
		type = "SMS - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				if (insieme.contains(new Integer(list[i].getKey())))
					sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}*/
		
		// Email
		list = DispatcherBookList.getInstance().getReceiversByType("E");
		type = "EMAIL - ";
		if(list != null) {
			int zebraCounter = 0;
			for(int i=0; i<list.length; i++)
				if (insieme.contains(new Integer(list[i].getKey()))) {
					sb.append("<option value=\""+list[i].getKey()+"\" class='"+(zebraCounter%2==0?"Row1":"Row2")+"'>"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
					zebraCounter++;
				}
		}
		
		return sb.toString();
		
	}
	
	public static String getOptionListNotConf()
	{
		Set<Integer> insieme = new HashSet<Integer>();
		int[] listaConf= null;
		
		String sql = "select idaddrbook from parameters_notification_list;";
		try {
			RecordSet rs =  DatabaseMgr.getInstance().executeQuery(null, sql);
			
			listaConf = new int[rs.size()];
			
			for (int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				Integer v = ((Integer) r.get("idaddrbook"));
				listaConf[i]=v.intValue();
				
				insieme.add(v);
			}
			
		} catch (DataBaseException e) {
			listaConf = new int[0];
			
			Logger logger = LoggerMgr.getLogger("com.carel.supervisor.plugin.parameters.dataaccess.ParametersNotificationList");
			logger.error(e);
			
		}
		
		
		StringBuffer sb = new StringBuffer();
		DispatcherBook[] list = null;
		String type = "";
		
		// only Email channel allowed. Other channles does not work properly. To be fixed.
		// Fax
		/*list = DispatcherBookList.getInstance().getReceiversByType("F");
		type = "FAX - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
			{
				if (!insieme.contains(new Integer(list[i].getKey())))
					sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
			}
		}
		
		// Sms
		list = DispatcherBookList.getInstance().getReceiversByType("S");
		type = "SMS - ";
		if(list != null) {
			for(int i=0; i<list.length; i++)
				if (!insieme.contains(new Integer(list[i].getKey())))
					sb.append("<option value=\""+list[i].getKey()+"\">"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
		}*/
		
		// Email
		list = DispatcherBookList.getInstance().getReceiversByType("E");
		type = "EMAIL - ";
		if(list != null) {
			int zebraCounter = 0;
			for(int i=0; i<list.length; i++)
				if (!insieme.contains(new Integer(list[i].getKey()))) {
					sb.append("<option value=\""+list[i].getKey()+"\" class='"+(zebraCounter%2==0?"Row1":"Row2")+"'>"+type+list[i].getReceiver()+" ("+list[i].getAddress()+")</option>");
					zebraCounter++;
				}
		}
		
		return sb.toString();
		
	}
}
