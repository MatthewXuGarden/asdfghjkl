package com.carel.supervisor.presentation.bo.helper.DeviceImport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.ide.dc.xmlDAO.XmlDAO;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBean;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.GraphBean;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bo.BDevDetail;
import com.carel.supervisor.presentation.bo.helper.Propagate;
import com.carel.supervisor.presentation.session.Transaction;

public class DeviceConfigImport extends XmlDAO
{
	private DevMdlBean devMdl = null;
	private Map logMap = null;
	private Map haccpMap = null;
	private Map alarmMap = null;
	private Map nonlogMap = null;
	private Map graphconfigMap = null;
//	private Map languageMap = null;
	private Map pagegraphMap = null;
	private Map graphconfblockMap = null;
	private Logger logger;
	
	private String filename = "";
	
	public static String PROFILE_ADMIN = "System Administrator";
	protected void unmarshal(Document doc, XPathFactory xfactory)
		throws ImportException
	{
	}
	public DeviceConfigImport()
	{
		devMdl = new DevMdlBean();
		logMap = new HashMap();
		haccpMap = new HashMap();
		alarmMap = new HashMap();
		nonlogMap = new HashMap();
		graphconfigMap = new HashMap();
//		languageMap = new HashMap();
		pagegraphMap = new HashMap();
		graphconfblockMap = new HashMap();
		logger = LoggerMgr.getLogger(this.getClass());
	}
	public int loadBeanByXML(int idsite,Properties prop,String xmlPath, boolean alarm,boolean descr,boolean um,boolean haccp,boolean hist,boolean prior, boolean graphconf,String lang) 
	{
        try
        {
        	ProfileBeanList profiles = new ProfileBeanList(idsite,false);
        	// Initializes common resources
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();	
			File inputXML = new File(xmlPath);
			Document doc = parser.parse(inputXML); 
			NodeList nodes = doc.getElementsByTagName(Propagate.DEVICE);
			Node nodedevice = nodes.item(0);
			DeviceRead(nodedevice);
			prop.setProperty("devmdlcode", this.devMdl.getCode());
	        if(alarm)
	        {
	        	AlarmRead(nodedevice);
	        }
	        if(um)
	        {
	        	this.UnitMeasureRead(nodedevice);
	        }
	        if(haccp)
	        {
	        	this.HaccpRead(nodedevice);
	        }
	        if(hist)
	        {
	        	this.LogRead(nodedevice);
	        }
	        if(prior)
	        {
	        	this.DisplayRead(nodedevice);
	        }
	        if(graphconf)
	        {
	        	this.GraphConfigRead(nodedevice,profiles);
	        	this.PageGraphRead(nodedevice,profiles);
	        	this.GraphConfBlockRead(nodedevice, profiles);
	        }
	        if(descr)
	        {
	        	this.DescriptionRead(nodedevice, lang,prop);
	        }
	        String sameprofcode = getSameProfileCode(this.graphconfigMap,profiles);
	        prop.setProperty("sameprofcode", sameprofcode);
        }
        catch(Exception ex)
        {
        	logger.error("device configuration read error: "+ex.toString());
        	return 0;
        }
        return 1;
	}
	private void DeviceRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			String devmdlcode = getAttrValByName(nodedevice, Propagate.DEVICE_CODE);
			devMdl.setCode(devmdlcode);
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.DEVICE);
		}
	}
	private void AlarmRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.ALARM);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodealarm = nodes.item(0);
			nodes = ((Element)nodealarm).getElementsByTagName(Propagate.ALARM_ABBREVATION);
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String code = getAttrValByName(node, Propagate.CODE);
				Integer priority = new Integer(getAttrValByName(node,Propagate.PRIORITY));
				boolean isActive = "FALSE".equals(getAttrValByName(node,Propagate.ALARM_ISACTIVE))?
					false:true;
				Integer frequency = new Integer(getAttrValByName(node,Propagate.ALAMR_FREQUENCY)); 
				VarphyBean var = (VarphyBean)nonlogMap.get(code);
				if(var == null)
				{
					var = new VarphyBean();
					var.setCode(code);
					nonlogMap.put(code, var);
					alarmMap.put(code, var);
				}
				if(!alarmMap.containsKey(code))
				{
					alarmMap.put(code, var);
				}
				var.setPriority(priority);
				var.setActive(isActive);
				var.setFrequency(frequency);
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.ALARM);
		}
	}
	private void UnitMeasureRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.UNITMEASURE);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodeum = nodes.item(0);
			nodes = ((Element)nodeum).getElementsByTagName(Propagate.UNITMEASURE_ABBREVATION);
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String code = getAttrValByName(node, Propagate.CODE);
				String measureUnit = node.getTextContent();//getAttrValByName(node,Propagate.UNITMEASURE);
				VarphyBean var = (VarphyBean)nonlogMap.get(code);
				if(var == null)
				{
					var = new VarphyBean();
					var.setCode(code);
					nonlogMap.put(code, var);
				}
				var.setMeasureUnit(measureUnit);
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.UNITMEASURE);
		}
	}
	private void HaccpRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.HACCP);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodehaccp = nodes.item(0);
			nodes = ((Element)nodehaccp).getElementsByTagName(Propagate.HACCP_ABBREVATION);
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String code = getAttrValByName(node, Propagate.CODE);
				VarphyBean var = (VarphyBean)nonlogMap.get(code);
				if(var == null)
				{
					var = new VarphyBean();
					var.setCode(code);
					nonlogMap.put(code, var);
					haccpMap.put(code, var);
				}
				if(!haccpMap.containsKey(code))
				{
					haccpMap.put(code, var);
				}
				var.setHaccp(true);
				var.setKeymax((short)BDevDetail.HACCP_ROWSHYSTORICAL);
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.HACCP);
		}
	}
	private void LogRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.LOG);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodelog = nodes.item(0);
			nodes = ((Element)nodelog).getElementsByTagName(Propagate.LOG_ABBREVATION);
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String code = getAttrValByName(node, Propagate.CODE);
				Integer frequency = new Integer(getAttrValByName(node,Propagate.LOG_FREQUENCY));
				VarphyBean var = new VarphyBean();
				logMap.put(code, var);
				var.setCode(code);
				var.setFrequency(frequency);
				Short keymax = Short.valueOf(getAttrValByName(node,Propagate.LOG_KEYMAX));
				var.setKeymax(keymax);
				String delta = getAttrValByName(node,Propagate.LOG_DELTA);
				if(delta != null)
				{
					double variation = Double.parseDouble(delta);
					var.setVariation(variation);
				}
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.LOG);
		}
	}
	private void DisplayRead(Node nodedevice)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.DISPLAY);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodeum = nodes.item(0);
			nodes = ((Element)nodeum).getElementsByTagName(Propagate.DISPLAY_ABBREVATION);
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String code = getAttrValByName(node, Propagate.CODE);
				Integer priority = new Integer(getAttrValByName(node,Propagate.PRIORITY));
				String display = getAttrValByName(node,Propagate.DISPLAY_TODISPLAY);
				VarphyBean var = (VarphyBean)nonlogMap.get(code);
				if(var == null)
				{
					var = new VarphyBean();
					var.setCode(code);
					nonlogMap.put(code, var);
				}
				var.setPriority(priority);
				var.setDisplay(display);
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.DISPLAY);
		}
	}
	private void GraphConfigRead(Node nodedevice,ProfileBeanList profiles)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.GRAPHCONF);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodegc = nodes.item(0);
			nodes = ((Element)nodegc).getElementsByTagName(Propagate.GRAPHCONF_PROFILE);
			for(int i=0;i<nodes.getLength();i++)
			{
				Element node = (Element)nodes.item(i);
				String profilename = getAttrValByName(node,Propagate.PROFILENAME);
				Map map = null;
				if(PROFILE_ADMIN.equals(profilename))
				{
					map = new HashMap();
					graphconfigMap.put(profilename, map);
				}
				else
				{
					for(int j=0;j<profiles.size();j++)
					{
						ProfileBean profile = profiles.getProfile(j);
						if(profile.getCode().equals(profilename))
						{
							map = new HashMap();
							graphconfigMap.put(profilename, map);
							break;
						}
					}
				}
				if(map == null)
				{
					continue;
				}
				NodeList nodes2 = node.getElementsByTagName(Propagate.GRAPHCONF_ABBREVATION);
				if(nodes2 == null || nodes2.getLength() == 0)
				{
					continue;
				}
				for(int j=0;j<nodes2.getLength();j++)
				{
					Node node2 = (Node)nodes2.item(j);
					String code = getAttrValByName(node2, Propagate.CODE);
					String haccp = getAttrValByName(node2,Propagate.GRAPHCONF_ISHACCP);
					boolean isHaccp = ("FALSE").equals(haccp)?false:true;
					String color = getAttrValByName(node2,Propagate.GRAPHCONF_COLOR);
					String ymax = getAttrValByName(node2,Propagate.GRAPHCONF_YMAX);
					String ymin = getAttrValByName(node2,Propagate.GRAPHCONF_YMIN);
					GraphBean var = new GraphBean();
					if(isHaccp == true)
					{
						map.put("HACCP"+code, var);
					}
					else
					{
						map.put("LOG"+code, var);
					}
					var.setCode(code);
				    var.setColor(color);
				    var.setHaccp(isHaccp);
					if(ymax != null)
					{
						var.setYMax(Float.valueOf(ymax));
						var.setYMin(Float.valueOf(ymin));
					}
				}
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.GRAPHCONF);
		}
	}
	private void PageGraphRead(Node nodedevice,ProfileBeanList profiles)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.GRAPH);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodegp = nodes.item(0);
			nodes = ((Element)nodegp).getElementsByTagName(Propagate.GRAPH_PROFILE);
			for(int i=0;i<nodes.getLength();i++)
			{
				Element node = (Element)nodes.item(i);
				String profilename = getAttrValByName(node,Propagate.PROFILENAME);
				ArrayList list = null;
				if(PROFILE_ADMIN.equals(profilename))
				{
					list = new ArrayList();
					pagegraphMap.put(profilename, list);
				}
				else
				{
					for(int j=0;j<profiles.size();j++)
					{
						ProfileBean profile = profiles.getProfile(j);
						if(profile.getCode().equals(profilename))
						{
							list = new ArrayList();
							pagegraphMap.put(profilename, list);
							break;
						}
					}
				}
				if(list == null)
				{
					continue;
				}
				NodeList nodes2 = node.getElementsByTagName(Propagate.GRAPH_ABBREVATION);
				if(nodes2 == null || nodes2.getLength() == 0)
				{
					continue;
				}
				for(int j=0;j<nodes2.getLength();j++)
				{
					Node node2 = (Node)nodes2.item(j);
				
					String haccp = getAttrValByName(node2,Propagate.GRAPHCONF_ISHACCP);
					boolean isHaccp = "FALSE".endsWith(haccp)?false:true;
					Short periodcode = Short.valueOf(getAttrValByName(node2,Propagate.GRAPH_PERIOD));
					String idvar1 = getAttrValByName(node2,"idvar1");
					String idvar2 = getAttrValByName(node2,"idvar2");
					String idvar3 = getAttrValByName(node2,"idvar3");
					String idvar4 = getAttrValByName(node2,"idvar4");
					String idvar5 = getAttrValByName(node2,"idvar5");
					String idvar6 = getAttrValByName(node2,"idvar6");
					String idvar7 = getAttrValByName(node2,"idvar7");
					String idvar8 = getAttrValByName(node2,"idvar8");
					String idvar9 = getAttrValByName(node2,"idvar9");
					String idvar10 = getAttrValByName(node2,"idvar10");
					String idvar11 = getAttrValByName(node2,"idvar11");
					String idvar12 = getAttrValByName(node2,"idvar12");
					String idvar13 = getAttrValByName(node2,"idvar13");
					String idvar14 = getAttrValByName(node2,"idvar14");
					String idvar15 = getAttrValByName(node2,"idvar15");
					String idvar16 = getAttrValByName(node2,"idvar16");
					String idvar17 = getAttrValByName(node2,"idvar17");
					String idvar18 = getAttrValByName(node2,"idvar18");
					String idvar19 = getAttrValByName(node2,"idvar19");
					String idvar20 = getAttrValByName(node2,"idvar20");
					String viewFinderCheck = getAttrValByName(node2,Propagate.GRAPH_VIEWFINDERCHECK); 
				    String xGridCheck = getAttrValByName(node2,Propagate.GRAPH_XGRIDCHECK);
				    String yGridCheck = getAttrValByName(node2,Propagate.GRAPH_YGRIDCHECK);
				    String viewFinderColorBg = getAttrValByName(node2,Propagate.GRAPH_VIEWFINDERCOLORBG);
				    String viewfinderColorFg = getAttrValByName(node2,Propagate.GRAPH_VIEWFINDERCOLORFG); 
				    String gridColor = getAttrValByName(node2,Propagate.GRAPH_GRIDCOLOR); 
				    String bgGraphColor = getAttrValByName(node2,Propagate.GRAPH_BGGRAPHCOLOR);
				    String axisColor = getAttrValByName(node2,Propagate.GRAPH_AXISCOLOR);
			    
				    ConfigurationGraphBean pagegraph = new ConfigurationGraphBean();
				    pagegraph.setHaccp(isHaccp);
				    pagegraph.setCode1(idvar1);
				    pagegraph.setCode2(idvar2);
				    pagegraph.setCode3(idvar3);
				    pagegraph.setCode4(idvar4);
				    pagegraph.setCode5(idvar5);
				    pagegraph.setCode6(idvar6);
				    pagegraph.setCode7(idvar7);
				    pagegraph.setCode8(idvar8);
				    pagegraph.setCode9(idvar9);
				    pagegraph.setCode10(idvar10);
				    pagegraph.setCode11(idvar11);
				    pagegraph.setCode12(idvar12);
				    pagegraph.setCode13(idvar13);
				    pagegraph.setCode14(idvar14);
				    pagegraph.setCode15(idvar15);
				    pagegraph.setCode16(idvar16);
				    pagegraph.setCode17(idvar17);
				    pagegraph.setCode18(idvar18);
				    pagegraph.setCode19(idvar19);
				    pagegraph.setCode20(idvar20);
				    pagegraph.setPeriodCode(periodcode);
				    pagegraph.setViewFinderCheck(viewFinderCheck);
				    pagegraph.setXGridCheck(xGridCheck);
				    pagegraph.setYGridCheck(yGridCheck);
				    pagegraph.setViewFinderColorBg(viewFinderColorBg);
				    pagegraph.setViewfinderColorFg(viewfinderColorFg);
				    pagegraph.setGridColor(gridColor);
				    pagegraph.setBgGraphColor(bgGraphColor);
				    pagegraph.setAxisColor(axisColor);
				    list.add(pagegraph);
				}
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.GRAPH);
		}
	}
	private void GraphConfBlockRead(Node nodedevice,ProfileBeanList profiles)
	throws ImportException
{
	try
	{
		NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.GRAPHCONFBLOCK);
		if(nodes == null || nodes.getLength() == 0)
			return;
		Node nodegb = nodes.item(0);
		nodes = ((Element)nodegb).getElementsByTagName(Propagate.GRAPHCONFBLOCK_PROFILE);
		if(nodes == null || nodes.getLength() == 0)
			return;
		for(int i=0;i<nodes.getLength();i++)
		{
			Element node = (Element)nodes.item(i);
			String profilename = getAttrValByName(node,Propagate.PROFILENAME);
			Map map = null;
			if(PROFILE_ADMIN.equals(profilename))
			{
				map = new HashMap();
				graphconfblockMap.put(profilename, map);
			}
			else
			{
				for(int j=0;j<profiles.size();j++)
				{
					ProfileBean profile = profiles.getProfile(j);
					if(profile.getCode().equals(profilename))
					{
						map = new HashMap();
						graphconfblockMap.put(profilename, map);
						break;
					}
				}
			}
			if(map == null)
			{
				continue;
			}
			NodeList nodes2 = node.getElementsByTagName(Propagate.GRAPHCONFBLOCK_ABBREVATION);
			if(nodes2 == null || nodes2.getLength() == 0)
			{
				continue;
			}
			for(int j=0;j<nodes2.getLength();j++)
			{
				Node node2 = (Node)nodes2.item(j);
				String code = getAttrValByName(node2, Propagate.CODE);
				String haccp = getAttrValByName(node2,Propagate.GRAPHCONF_ISHACCP);
				boolean isHaccp = ("FALSE").equals(haccp)?false:true;
				String label = getAttrValByName(node2,Propagate.GRAPHCONFBLOCK_LABEL);
				GraphConfBlockBean var = new GraphConfBlockBean();
				if(isHaccp == true)
				{
					map.put("HACCP"+code, var);
				}
				else
				{
					map.put("LOG"+code, var);
				}
			    var.setLabel(label);
			    var.setIshaccp(isHaccp);
			}
		}
	}
	catch(Exception ex)
	{
		throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.GRAPHCONF);
	}
}
	private void DescriptionRead(Node nodedevice,String lang,Properties prop)
		throws ImportException
	{
		try
		{
			NodeList nodes = ((Element)nodedevice).getElementsByTagName(Propagate.DESCRIPTION);
			if(nodes == null || nodes.getLength() == 0)
				return;
			Node nodedescrp = nodes.item(0);
			nodes = ((Element)nodedescrp).getElementsByTagName(Propagate.DESCRIPTION_LANG);
			if(nodes == null || nodes.getLength() == 0)
				return;
			boolean langexist = false;
			String languages = "";
			for(int i=0;i<nodes.getLength();i++)
			{
				Node node = nodes.item(i);
				String key = getAttrValByName(node, Propagate.DESCRIPTION_LANG_KEY);
				if(lang.equals(key))
				{
					langexist = true;
				}
				languages += key+";";
			}
			languages = languages.substring(0, languages.length()-1);
			prop.setProperty("langs", String.valueOf(languages));
			//if the current language is not in xml, only update English description
			if(langexist == false)
			{
				lang = "EN_en";
			}
			for(int i=0;i<nodes.getLength();i++)
			{
				Element node = (Element)nodes.item(i);
				String key = getAttrValByName(node, Propagate.DESCRIPTION_LANG_KEY);
//				Map newlang = new HashMap();
//				languageMap.put(key,newlang);
				if(!lang.equals(key))
				{
					continue;
				}
				NodeList nodes2 = node.getElementsByTagName(Propagate.DESCRIPTION_LANG_DES);
				for(int j=0;j<nodes2.getLength();j++)
				{
					Node node2 = (Node)nodes2.item(j);
					String code = getAttrValByName(node2,Propagate.CODE);
					String descr = node2.getTextContent();//getAttrValByName(node2,Propagate.DESCRIPTION_LANG_DESCR);
					String shortD = getAttrValByName(node2,Propagate.DESCRIPTION_LANG_SHORT);
					String longD = getAttrValByName(node2,Propagate.DESCRIPTION_LANG_LONG);
					VarphyBean var = (VarphyBean)nonlogMap.get(code);
					if(var == null)
					{
						var = new VarphyBean();
						var.setCode(code);
						nonlogMap.put(code, var);
					}
					var.setShortDesc(shortD);
					var.setShortDescription(descr);
					var.setLongDescription(longD);
//					String[] str = new String[2];
//					str[0] = descr;
//					str[1] = shortD;
//					newlang.put(code, str);
				}
			}
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.DESCRIPTION);
		}
	}
	private String getSameProfileCode(Map map,ProfileBeanList profiles)
    {
    	String profilecode = "";
    	Iterator it = map.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, VarphyBean> entry = (Map.Entry<String, VarphyBean>)it.next();
    		String key = entry.getKey();
    		if("System Administrator".equals(key))
    		{
    			continue;
    		}
    		for(int i=0;i<profiles.size();i++)
    		{
    			ProfileBean bean = profiles.getProfile(i);
    			if(bean.getCode().equals(key))
    			{
    				profilecode += key+"^^^";
    				break;
    			}
    		}
    	}
    	if(profilecode != "")
    	{
    		profilecode = profilecode.substring(0,profilecode.length()-3);
    	}
    	return profilecode;
    }
	public VarphyBean getlogVarByCode(String code)
	{
		return (VarphyBean)logMap.get(code);
	}
	public Map getLogMap()
	{
		return this.logMap;
	}
	public Map getHaccpMap()
	{
		return this.haccpMap;
	}
	public Map getAlarmMap()
	{
		return this.alarmMap;
	}
	public Map getNologMap()
	{
		return this.nonlogMap;
	}
	public Map getGraphconfigMap()
	{
		return this.graphconfigMap;
	}
	public Map GetPagegraphMap()
	{
		return this.pagegraphMap;
	}
	public Map GetGraphconfblockMap()
	{
		return this.graphconfblockMap;
	}
	public DevMdlBean getDevMdl()
	{
		return this.devMdl;
	}
//	public Map getLanguageMap()
//	{
//		return this.languageMap;
//	}
	public String getFilename()
	{
		return this.filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
}
