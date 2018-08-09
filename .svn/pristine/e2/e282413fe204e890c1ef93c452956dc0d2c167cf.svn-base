package com.carel.supervisor.presentation.https2xml;

import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.director.bms.Subset;
import com.carel.supervisor.director.bms.SubsetKey;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.devices.UtilDevice;

public class VL2Request implements IXMLRequest {

/*
<request>
<login userName='' password='' />
<rq t='VL2' />
[<dv did='<uuid1>;<uuid2>;<uuid3>' />] (if missing => all devices)
</request>

*/
	
	private StringBuilder response;
private String username;
	
	public String getNameRequest() {
		return "VL2";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		response = new StringBuilder();
		response.append("<rs t='VL2'>\n");
		LinkedList<String> dids=new LinkedList<String>();
		String pvCode = bmsc.getPvIdentifier();
		boolean allDevices = false;
		if(node.getNodes("dv")!=null){
			XMLNode[] dev_nodes = node.getNodes("dv");
			for(int r=0;r<dev_nodes.length;r++){
				try{
					dids.add(dev_nodes[r].getAttribute("did").toString());
				}catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}else{
			dids = new LinkedList<String>(bmsc.getDevicemappingsmap().keySet());
			allDevices = true;
		}
		StringBuilder response2 = new StringBuilder();
		try{
			for (Iterator<String> iterator = dids.iterator(); iterator.hasNext();) {
				String did = iterator.next();
				String code = null;
				try{
					if (allDevices ||pvCode.equalsIgnoreCase(did.split(":")[0]))  //check on site code
					{
						if (allDevices)
							code = did;
						else
							code = did.split(":")[1];
						DeviceBean d = DeviceListBean.retrieveSingleDeviceByCode(1, code, bmsc.getLanguage());
						if (d==null)
							continue;
						response2.append("<dv did='"+pvCode+":"+code +
								"' st='"+UtilDevice.getLedColor(d.getIddevice())+
								"' en='"+(d.getIsenabled().equalsIgnoreCase("FALSE")?false:true)+
								"' >\n");
						String conf = BmsMgr.getInstance().getConfig().getDevicemappingsmap().get(d.getCode());
					
						Subset ss = BmsMgr.getInstance().getConfig().getSubsetsmap().get(new SubsetKey(new Integer(d.getIddevmdl()),conf));
						
							
						//getvariables
						if (ss!=null)
						{
							StringBuilder sqlvars = new StringBuilder();
							for (Iterator<Integer> ssitr=ss.iterator();ssitr.hasNext();) {
								sqlvars.append(ssitr.next()).append(",");
							}
							sqlvars.deleteCharAt(sqlvars.length()-1);
							String sql = "select cfvariable.idvariable as idvariable, cfvariable.code as code, cftableext.description as descr " +
									" from cfvariable, cftableext where cfvariable.iddevice="+d.getIddevice()+
									" and cfvariable.idvarmdl in ("+sqlvars.toString()+") and " +
									" cfvariable.idhsvariable is not null and" +
									" cftableext.idsite=1 and cftableext.tablename='cfvariable' and cftableext.tableid=idvariable and " +
									" languagecode='"+bmsc.getLanguage()+"'";
							try{
								RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
								if(rs!=null && rs.size()>0){
									int[] v = new int[rs.size()];
									for(int i = 0;i<rs.size();i++){
										v[i] = (Integer) rs.get(i).get("idvariable");
									}
									Variable[] vars = ControllerMgr.getInstance().getFromField(v);
									for(int k = 0;k<vars.length;k++){
										//VariableInfo vi = vars[k].getInfo();
										response2.append("<vr uuid='"+pvCode+":"+code+":"+ rs.get(k).get("code")+"' v='"+vars[k].getCurrentValue()+"'>\n");
										if(node.getAttribute("ext")!=null && node.getAttribute("ext").equalsIgnoreCase("true")){
											response2.append("<![CDATA[");
											try{
												response2.append(rs.get(k).get("descr"));
											}catch (Exception e) {
												LoggerMgr.getLogger(this.getClass()).error(e);
											}
											response2.append("]]>\n");
										}
										response2.append("</vr>\n");
									}
								}
							} catch (Exception e) {
								LoggerMgr.getLogger(this.getClass()).error(e);
		//						LoggerMgr.getLogger(this.getClass()).warn("Variable error");
							}
						}
						response2.append("</dv>\n");
					}
				}catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
					e.printStackTrace();
					//LoggerMgr.getLogger(this.getClass()).warn("Device ["+iddev+"] not valid");
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
