package com.carel.supervisor.presentation.https2xml;

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

public class AL2Request implements IXMLRequest {

	private StringBuffer response;
	private String username;
	
	public String getNameRequest() {
		return "AL2";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		response = new StringBuffer ();
		response.append("<rs t='AL2'>\n");
		StringBuffer response2 = new StringBuffer();
		String pvidentifier = bmsc.getPvIdentifier();
		try{
			DeviceListBean dlb = new DeviceListBean(1, bmsc.getLanguage());
			int[] iddevices = dlb.getIds();
			for (int itr = 0;itr<iddevices.length;itr++) {
				int iddev = iddevices[itr];
				DeviceBean d = dlb.getDevice(iddev);
				response2.append("<dv did='"+ pvidentifier+":"+ d.getCode()+
						"' st='"+UtilDevice.getLedColor(iddev)+
						"' en='"+(d.getIsenabled().equalsIgnoreCase("FALSE")?false:true)+
						"' >\n");
				LinkedList<AlarmLog> list = loadAlarmList(iddev);
				for (Iterator<AlarmLog> iterator = list.iterator(); iterator.hasNext();) {
					AlarmLog alarmLog = iterator.next();
					Record r = DatabaseMgr.getInstance().executeQuery(null, "select idvargroup,code from cfvariable where idvariable="+alarmLog.getIdvariable(),null).get(0);
					response2.append("<alr uuid='"+pvidentifier+":"+d.getCode()+":"+ alarmLog.getVarcode()+
							"' start='"+alarmLog.getStarttime()+ 
							"' end='"+(alarmLog.getEndtime()==null?"":alarmLog.getEndtime())+
							"' prio='"+alarmLog.getPriority()+"' >\n");
					response2.append("<desc><![CDATA["+alarmLog.getDescription()+"]]></desc>\n");
					response2.append("<ack user='"+(alarmLog.getAckuser()!=null?alarmLog.getAckuser():"")+"' time='"+(alarmLog.getAcktime()!=null?alarmLog.getAcktime():"")+"'/>\n");
					response2.append("<del user='"+(alarmLog.getDelactionuser()!=null?alarmLog.getDelactionuser():"")+"' time='"+(alarmLog.getDelactiontime()!=null?alarmLog.getDelactiontime():"")+"'/>\n");
					response2.append("<rst user='"+(alarmLog.getResetuser()!=null?alarmLog.getResetuser()!=null:"")+"' time='"+(alarmLog.getResettime()!=null?alarmLog.getResettime():"")+"'/>\n");
					response2.append("</alr>\n");
					
				}
				response2.append("</dv>\n");
			}
		}catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			response2= new StringBuffer();
		}
		response.append(response2.toString());
		response.append("</rs>\n");
	}

	private LinkedList<AlarmLog> loadAlarmList(Integer iddev) {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		LinkedList<AlarmLog> alarmlist = new LinkedList<AlarmLog>();
        StringBuilder sql = new StringBuilder();
		try {
			sql.append("select hsalarm.*,cfvariable.code as vcode, tvar.description as description, tdev.description as device  ");
			sql.append("from  "); 
			sql.append("hsalarm, cfvariable,cftableext as tvar, cftableext as tdev, cfdevice  "); 
			sql.append("where  ");
			sql.append("hsalarm.iddevice = "+iddev+" and  ");
			sql.append("hsalarm.idvariable = cfvariable.idvariable and ");
			sql.append("cfdevice.iddevice = hsalarm.iddevice and ");
			sql.append("cfdevice.iscancelled='FALSE' and  "); 
			sql.append("tvar.tableid=hsalarm.idvariable and  "); 
			sql.append("tvar.tablename='cfvariable' and  "); 
			sql.append("tvar.languagecode='"+bmsc.getLanguage()+"' and  "); 
			sql.append("tdev.tableid=hsalarm.iddevice and  "); 
			sql.append("tdev.tablename='cfdevice' and  "); 
			sql.append("tdev.languagecode='"+bmsc.getLanguage()+"' and  "); 
			sql.append("tvar.idsite=hsalarm.idsite and  "); 
			sql.append("tdev.idsite=hsalarm.idsite and  "); 
			sql.append("(hsalarm.endtime is null) and (hsalarm.resettime is null)  ");
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql.toString());
			for (int i = 0; i < rs.size(); i++)
				alarmlist.add(new AlarmLog(rs.get(i), "",rs.get(i).get("vcode").toString()));
		} catch (Exception e) {
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
