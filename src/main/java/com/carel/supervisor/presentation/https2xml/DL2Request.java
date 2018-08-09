package com.carel.supervisor.presentation.https2xml;

import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.devices.UtilDevice;

public class DL2Request implements IXMLRequest {

/*
<request>
       <login userName='' password='' />
       <rq t='DL2' ext='{true/false}' />
</request>


 */
	private StringBuilder response;
	private String username;
	
	public String getNameRequest() {
		return "DL2";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		response = new StringBuilder();
		response.append("<rs t='DL2'>\n");
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		StringBuilder response2 = new StringBuilder();
		String pvCode = bmsc.getPvIdentifier();
		try{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from cfline order by code");
			if(rs!= null &&rs.size()>0){
				for (int i = 0; i < rs.size(); i++) {
					Record r = rs.get(i);
					response2.append("<ln id='"+r.get("idline")+
							"' lineaddr='"+r.get("comport")+
							"' baud='"+r.get("baudrate")+
							"' protocol='"+r.get("typeprotocol")+"."+r.get("protocol")+"'>\n");
					LinkedList<DeviceBean> dblist = DeviceListBean.getDevicesByLine(1, (Integer)r.get("idline"));
					for (Iterator<DeviceBean> iterator = dblist.iterator(); iterator.hasNext();) {
						DeviceBean d = iterator.next();
						response2.append("<dv did='"+pvCode+":"+ d.getCode()+
								"' st='"+UtilDevice.getLedColor(d.getIddevice())+
								"' en='"+(d.getIsenabled().equalsIgnoreCase("FALSE")?false:true)+
								"'>\n");
						if(node.getAttribute("ext")!=null &&
								node.getAttribute("ext").equalsIgnoreCase("true")){
							response2.append("<![CDATA[");
							try{
								response2.append(DatabaseMgr.getInstance().executeQuery(null, "select description from cftableext where idsite=1 and tablename='cfdevice' and tableid="+d.getIddevice()+" and languagecode='"+bmsc.getLanguage()+"'").get(0).get("description"));
							}catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
							}
							response2.append("]]>\n");
						}
						response2.append("</dv>\n");
						
					}
					response2.append("</ln>\n");
					
				}
			}
		}catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			response2= new StringBuilder();
		}
		response.append(response2.toString());
		response.append("</rs>\n");
	}
	

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
