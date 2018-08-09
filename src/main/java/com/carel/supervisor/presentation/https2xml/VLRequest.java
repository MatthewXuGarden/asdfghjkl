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

public class VLRequest implements IXMLRequest {

/*
<request>
<login userName='admin' password='admin' />
<rq t='VL' />
<dv val='<id1>;<id2>;<id3>' />
</request>
*/
	
	private StringBuilder response;
private String username;
	
	public String getNameRequest() {
		return "VL";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		BMSConfiguration bmsc = BmsMgr.getInstance().getConfig();
		response = new StringBuilder();
		response.append("<rs t='VL'>\n");
		LinkedList<String> iddevices=new LinkedList<String>();		
		if(node.getNode("dv")!=null){
			String str = node.getNode("dv").getAttribute("val");
			String[] idstr = str.split(";");
			for(int r=0;r<idstr.length;r++){
				try{
					iddevices.add(idstr[r]);
				}catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}else{
			
			iddevices = new LinkedList<String>(bmsc.getDevicemappingsmap().keySet());
		}
		StringBuilder response2 = new StringBuilder();
		try{
			for (Iterator<String> iterator = iddevices.iterator(); iterator.hasNext();) {
				String iddev = iterator.next();
				try{
					DeviceBean d = null;
					String conf = null;
					if (iddev.contains("."))   //new style uuid
					{
						d = DeviceListBean.retrieveSingleDeviceByCode(1, iddev, bmsc.getLanguage());
						conf = BmsMgr.getInstance().getConfig().getDevicemappingsmap().get(d.getCode());
					}
					else    //old style: iddevice
					{
						d = DeviceListBean.retrieveSingleDeviceById(1, new Integer(iddev), bmsc.getLanguage());
						conf = BmsMgr.getInstance().getConfig().getDevicemappingsmap().get(String.valueOf(d.getIddevice()));
					}
					response2.append("<dv id='"+d.getIddevice()+
							"' st='"+UtilDevice.getLedColor(d.getIddevmdl())+
							"' devaddr='"+d.getAddress()+
							"' en='"+(d.getIsenabled().equalsIgnoreCase("FALSE")?false:true)+
							"' code='"+d.getCode()+"'>\n");
				
					Subset ss = BmsMgr.getInstance().getConfig().getSubsetsmap().get(new SubsetKey(new Integer(d.getIddevmdl()),conf));
						
					if (ss!=null)
					{
						//getvariables
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
									VariableInfo vi = vars[k].getInfo();
									response2.append("<vr id='"+vi.getId()+"' code='"+rs.get(k).get("code")+"' type='"+vi.getType()+
											"' vaddr='"+vi.getAddressIn()+"' val='"+vars[k].getCurrentValue()+"'>\n");
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
