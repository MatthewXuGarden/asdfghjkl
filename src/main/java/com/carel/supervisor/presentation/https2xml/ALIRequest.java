package com.carel.supervisor.presentation.https2xml;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.devices.UtilDevice;

public class ALIRequest implements IXMLRequest {

	private StringBuffer response;
	private String username;
	
	public String getNameRequest() {
		return "ALI";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date ts;
		String tmStr = node.getAttribute("ts");
		try{
			
			ts= sdf.parse(tmStr);
		} catch (Exception e) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(GregorianCalendar.DAY_OF_MONTH , -1);
			ts=gc.getTime();
		}

		response = new StringBuffer ();
		response.append("<rs t='ALI' ts='"+sdf.format(new Date(System.currentTimeMillis()))+"'>\n");
		StringBuffer response2 = new StringBuffer();
		try{
			DeviceListBean dlb = new DeviceListBean(1, bmsc.getLanguage());
			int[] iddevices = dlb.getIds();
			for (int itr = 0;itr<iddevices.length;itr++) {
				int iddev = iddevices[itr];
				DeviceBean d = dlb.getDevice(iddev);
				response2.append("<dv id='"+d.getIddevice()+
						"' st='"+UtilDevice.getLedColor(iddev)+
						"' devaddr='"+d.getAddress()+
						"' en='"+(d.getIsenabled().equalsIgnoreCase("FALSE")?false:true)+
						"' code='"+d.getCode()+"'>");
				LinkedList<AlarmLog> list = loadAlarmList(iddev,ts);
				for (Iterator<AlarmLog> iterator = list.iterator(); iterator.hasNext();) {
					AlarmLog alarmLog = iterator.next();
					Record r = DatabaseMgr.getInstance().executeQuery(null, "select idvargroup,code from cfvariable where idvariable= ? ",new Object[]{alarmLog.getIdvariable()}).get(0);
					response2.append("<alr id='"+alarmLog.getId()+
							"' prio='"+alarmLog.getPriority()+ 
							"' start='"+alarmLog.getStarttime()+
							"' end='"+
							(alarmLog.getEndtime()==null ? "" : alarmLog.getEndtime())+
							"' cat='"+r.get(0)+
							"' idvar='"+alarmLog.getIdvariable()+ 
							"' c='"+r.get(1)+"' >");
					response2.append("<desc><![CDATA["+alarmLog.getDescription()+"]]></desc>");
					response2.append("<ack user='"+(alarmLog.getAckuser()!=null?alarmLog.getAckuser():"")+"' time='"+(alarmLog.getAcktime()!=null?alarmLog.getAcktime():"")+"'/>");
					response2.append("<del user='"+(alarmLog.getDelactionuser()!=null?alarmLog.getDelactionuser():"")+"' time='"+(alarmLog.getDelactiontime()!=null?alarmLog.getDelactiontime():"")+"'/>");
					response2.append("<rst user='"+(alarmLog.getResetuser()!=null?alarmLog.getResetuser()!=null:"")+"' time='"+(alarmLog.getResettime()!=null?alarmLog.getResettime():"")+"'/>");
					response2.append("</alr>");	
				}
				response2.append("</dv>");
			}
		}catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			response2= new StringBuffer();
		}
		response.append(response2.toString());
		response.append("</rs>\n");
	}

	private LinkedList<AlarmLog> loadAlarmList(Integer iddev, Date from) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		LinkedList<AlarmLog> alarmlist = new LinkedList<AlarmLog>();
        StringBuilder sql = new StringBuilder();
		try {
			sql.append("select hsalarm.*, tvar.description as description, tdev.description as device  ");
			sql.append("from  "); 
			sql.append("hsalarm, cftableext as tvar, cftableext as tdev, cfdevice  "); 
			sql.append("where  ");
			sql.append("hsalarm.iddevice = ? and  ");
			sql.append("cfdevice.iddevice = hsalarm.iddevice and ");
			sql.append("cfdevice.iscancelled='FALSE' and  "); 
			sql.append("tvar.tableid=hsalarm.idvariable and  "); 
			sql.append("tvar.tablename='cfvariable' and  "); 
			sql.append("tvar.languagecode='"+bmsc.getLanguage()+"' and  "); 
			sql.append("tdev.tableid=hsalarm.iddevice and  "); 
			sql.append("tdev.tablename='cfdevice' and  "); 
			sql.append("tdev.languagecode='"+bmsc.getLanguage()+"' and  "); 
			sql.append("tvar.idsite=hsalarm.idsite and  "); 
			sql.append("tdev.idsite=hsalarm.idsite "); 
			sql.append("and hsalarm.lastupdate > ? ");
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString(), new Object[]{iddev, sdf.format(from)});
			
			for (int i = 0; i < rs.size(); i++)
				alarmlist.add(new AlarmLog(rs.get(i), ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alarmlist;
	}

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
	
	
}
