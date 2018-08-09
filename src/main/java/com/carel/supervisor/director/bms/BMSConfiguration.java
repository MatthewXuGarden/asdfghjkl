package com.carel.supervisor.director.bms;

import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

public class BMSConfiguration {

	private boolean loaded;
	private HashMap<SubsetKey, Subset> subsetsmap;
	// private HashMap<Integer, LinkedList<String>> devicemappingsmap;
	private HashMap<String, String> devicemappingsmap;
	private String host=null;
	private int port=0;
	private boolean viaSocket=false;
	private boolean viaHttpPost=false;
	private String language=null;
	private String pvidentifier="";
	private FailureVar[] failure_var=null;
	
	private String alarmsXML=null;
	private String eventsXML=null;
	private String devicesXML=null;
	private String variablesXML=null;
	private HashMap<String, LinkedList<String>> actmap;
	private HashMap<String, String> packetsmap;

	public BMSConfiguration() {
		setLoaded(true);

		// load Extra conf
		try {
			URL url = ResourceLoader.fileFromResourcePath("xmlconf","xmlextraconf.xml");
			if (url != null) {
				XMLNode config = XMLNode.parse(new FileInputStream(url.getFile()));
				if (config != null) {
					
					XMLNode[] pvtags = config.getNodes("pvidentifier");
					if (pvtags!=null)
						pvidentifier = pvtags[0].getTextValue().trim(); 
					
					XMLNode[] comm_fail = config.getNodes("comm_failure");
					if (comm_fail!=null)
					{
						XMLNode[] vars = comm_fail[0].getNodes("var");
						if (vars!=null)
						{
							failure_var = new FailureVar[vars.length];
							String uuid = "";
							String value = "";
							for (int i=0;i<vars.length;i++)
							{
								uuid = vars[i].getAttribute("uuid");
								value = vars[i].getAttribute("value");
								failure_var[i] = new FailureVar(uuid,value);
							}
						}
					}
					
					XMLNode[] bmstags = config.getNodes("bms");
					
					if (bmstags!= null && bmstags.length>0)
					{
						XMLNode[] servertags = bmstags[0].getNodes("server");
						
						if (servertags.length>0)
						{
							XMLNode[] commtypetags = servertags[0].getNodes("commtype");
							
							if (commtypetags.length>0)
							{
								String type=commtypetags[0].getTextValue().trim();
								if (type.equalsIgnoreCase("socket")){
									viaSocket=true;
								}
								else if (type.equalsIgnoreCase("http")){
									viaHttpPost=true;
								}
							}
							
							XMLNode[] hosttags = servertags[0].getNodes("host");
							
							if (hosttags.length>0)
							{
								String tmp =hosttags[0].getTextValue().trim();
								
								if (viaSocket)
								{
									if (tmp.indexOf(":")>0){
										host=tmp.substring(0,tmp.indexOf(":") );
										port = new Integer(tmp.substring(tmp.indexOf(":")+1)).intValue();
									}
									else
									{
										port=0;
									}
								}
								else if (viaHttpPost)
								{
									host=tmp;
									port=0;
								}
									
							}
							
							XMLNode[] actionstag = bmstags[0].getNodes("actions");
							
							if (actionstag!= null && actionstag.length>0)
							{
								setActmap(new HashMap<String, LinkedList<String>>());
								XMLNode[] act = actionstag[0].getNodes();
								for(int ll=0;ll<act.length;ll++){
									LinkedList<String> list = new LinkedList<String>();
									StringTokenizer strtok = new StringTokenizer(act[ll].getTextValue(),",");
									while (strtok.hasMoreTokens()) {
										list.add(strtok.nextToken());
									}
									getActmap().put("#"+act[ll].getNodeName(), list);
								}
							}
						}
						XMLNode[] languagetags = bmstags[0].getNodes("language");
						
						if (languagetags!=null && languagetags.length>0)
						{
							String lang=languagetags[0].getTextValue().trim();
							if (lang!="")
								language=lang;
							else language=LangUsedBeanList.getDefaultLanguage(1);
						}
					}
					
					XMLNode[] xmlstags = config.getNodes("xmls");
					setPacketsmap(new HashMap<String, String>());
					
					if (xmlstags!=null && xmlstags.length>0)
					{
						XMLNode[] alarmslisttags = xmlstags[0].getNodes("alarmslist");
						if (alarmslisttags.length>0)
						{
							alarmsXML = alarmslisttags[0].getTextValue().trim();
							getPacketsmap().put(alarmslisttags[0].getNodeName(), alarmsXML);
						}
						XMLNode[] eventslisttags = xmlstags[0].getNodes("eventslist");
						if (eventslisttags.length>0)
						{
							eventsXML = eventslisttags[0].getTextValue().trim();
							getPacketsmap().put(eventslisttags[0].getNodeName(), eventsXML);
						}
						XMLNode[] deviceslisttags = xmlstags[0].getNodes("deviceslist");
						if (deviceslisttags.length>0)
						{
							devicesXML = deviceslisttags[0].getTextValue().trim();
							getPacketsmap().put(deviceslisttags[0].getNodeName(), devicesXML);
						}
						XMLNode[] variableslisttags = xmlstags[0].getNodes("variableslist");
						if (variableslisttags.length>0)
						{
							variablesXML = variableslisttags[0].getTextValue().trim();
							getPacketsmap().put(variableslisttags[0].getNodeName(), variablesXML);
						}
					}
				}
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error("File xmlmdlconfig.xml malformed", e);
			setLoaded(false);
		}
		// subset definition file
		subsetsmap = new HashMap<SubsetKey, Subset>();
		try {
			URL url = ResourceLoader.fileFromResourcePath("xmlconf","xmlmdlconfig.xml");
			if (url != null) {
				XMLNode config = XMLNode.parse(new FileInputStream(url.getFile()));
				if (config != null) {
					XMLNode[] devmdl = config.getNodes("device");
					for (int i = 0; i < devmdl.length; i++) {
						String mdlcode = devmdl[i].getAttribute("mdlcode");
						XMLNode[] subsets = devmdl[i].getNodes("subset");
						for (int k = 0; k < subsets.length; k++) {
							String idss = subsets[k].getAttribute("id");
							Integer iddevmdl = getIdDevMdl(mdlcode);
							if (iddevmdl != null) {
								SubsetKey key = new SubsetKey(iddevmdl, idss);
								Subset s = subsetsmap.get(key);
								if (s == null) {
									s = new Subset();
									subsetsmap.put(key, s);
								}
								XMLNode[] vars = subsets[k].getNodes("var");
								for (int j = 0; j < vars.length; j++) {
									if (vars[j].getAttribute("code") != null) {
										Integer idvarmdl = getIdVarMdl(iddevmdl, vars[j].getAttribute("code"));
										s.add(idvarmdl);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(
					"File xmlmdlconfig.xml malformed", e);
			setLoaded(false);
		}

		// site configuration definition file
		devicemappingsmap = new HashMap<String, String>();
		try {
			URL url = ResourceLoader.fileFromResourcePath("xmlconf","xmldevice.xml");
			if (url != null) {
				XMLNode config = XMLNode.parse(new FileInputStream(url.getFile()));
				if (config != null) {
					XMLNode[] subset = config.getNodes("subset");
					if(subset!=null){
						for (int i = 0; i < subset.length; i++) {
							String idss = subset[i].getAttribute("id");
							XMLNode[] devices = subset[i].getNodes("device");
							for (int k = 0; k < devices.length; k++) {
								try {
									// if(devices[k].getAttribute("id")!=null){
									// if(devicemappingsmap.get(new
									// Integer(devices[k].getAttribute("id")))==null){
									// devicemappingsmap.put(new
									// Integer(devices[k].getAttribute("id")),
									// new LinkedList<String>());
									// }
									// devicemappingsmap.get(new
									// Integer(devices[k].getAttribute("id"))).add(idss);
									// }
									if (devices[k].getAttribute("did")!=null)   //new style uuid
										devicemappingsmap.put(new String(devices[k].getAttribute("did")), idss);
									else    // old style: iddevice
										devicemappingsmap.put(new String(devices[k].getAttribute("id")), idss);
								} catch (Exception e) {
									if (devices[k].getAttribute("did")!=null)
										LoggerMgr.getLogger(this.getClass()).error("Wrong iddevice: "+ devices[k].getAttribute("did"),e);
									else
										LoggerMgr.getLogger(this.getClass()).error("Wrong iddevice: "+ devices[k].getAttribute("id"),e);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(
					"File xmlmdlconfig.xml malformed", e);
			setLoaded(false);
		}
		
	}

	private Integer getIdDevMdl(String mdlcode) {
		try {
			return (Integer) DatabaseMgr.getInstance().executeQuery(
					null,
					"select iddevmdl from cfdevmdl where code='" + mdlcode
							+ "'").get(0).get("iddevmdl");
		} catch (DataBaseException e) {
			return null;
		}
	}

	private Integer getIdVarMdl(Integer iddevmdl, String attribute) {
		try {
			return (Integer) DatabaseMgr.getInstance().executeQuery(
					null,
					"select idvarmdl from cfvarmdl where iddevmdl=" + iddevmdl
							+ " and code='" + attribute + "'").get(0).get(
					"idvarmdl");
		} catch (DataBaseException e) {
			return null;
		}
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public HashMap<SubsetKey, Subset> getSubsetsmap() {
		return subsetsmap;
	}

	public HashMap<String, String> getDevicemappingsmap() {
		return devicemappingsmap;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean getViaSocket() {
		return viaSocket;
	}

	public void setViaSocket(boolean viaSocket) {
		this.viaSocket = viaSocket;
	}

	public boolean getViaHttpPost() {
		return viaHttpPost;
	}

	public void setViaHttpPost(boolean viaHttpPost) {
		this.viaHttpPost = viaHttpPost;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public String getAlarmsXML() {
		return alarmsXML;
	}

	public void setAlarmsXML(String alarmsXML) {
		this.alarmsXML = alarmsXML;
	}

	public String getEventsXML() {
		return eventsXML;
	}

	public void setEventsXML(String eventsXML) {
		this.eventsXML = eventsXML;
	}

	public String getDevicesXML() {
		return devicesXML;
	}

	public void setDevicesXML(String devicesXML) {
		this.devicesXML = devicesXML;
	}

	public String getVariablesXML() {
		return variablesXML;
	}

	public void setVariablesXML(String variablesXML) {
		this.variablesXML = variablesXML;
	}

	private void setActmap(HashMap<String, LinkedList<String>> actmap) {
		this.actmap = actmap;
	}

	public HashMap<String, LinkedList<String>> getActmap() {
		return actmap;
	}

	private void setPacketsmap(HashMap<String, String> packetsmap) {
		this.packetsmap = packetsmap;
	}

	public HashMap<String, String> getPacketsmap() {
		return packetsmap;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public String getPvIdentifier() {
		return pvidentifier;
	}
	
	public FailureVar[] getFailureVars()
	{
		return failure_var;
	}
}
