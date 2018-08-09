package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.field.FileMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.MbSlaveConfBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.dbllistbox.ListBoxElement;
import com.carel.supervisor.presentation.helper.ModbusSlaveCommander;
import com.carel.supervisor.presentation.session.UserSession;

public class BDataTransfer extends BoMaster
{
	UserSession usersession;

	public static final String  MBTYPE_COIL			= "coil";
	public static final String  MBTYPE_REGISTER		= "register";
	public BDataTransfer(String l)
	{
		super(l, 0);
	}

	@Override
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "datatransferOnLoad();");
		p.put("tab2name", "onConfLoad();");
		return p;
	}

	@Override
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
        //20091126-simon.zhang
        //append the virtual keyboard		
		p.put("tab1name", "dbllistbox.js;datatransfer.js;keyboard.js;");
		p.put("tab2name", "mbslaveconf.js;keyboard.js;");
		return p;
	}

	
	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		if( "tab2name".equals(tabName) )
			executeTab2Post(us, prop);
	}

	
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		String toReturn = "";
		if ("tab1name".equals(tabName.trim()))
		{
			if ("loadVariables".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				String sql = "SELECT cfvariable.*, cftableext.description from cfvariable inner join cftableext " + "on cftableext.tableid = cfvariable.idvariable and "
								+ "cftableext.tablename='cfvariable' and " + "cftableext.languagecode = ? and " + "cfvariable.idsite = ? and " + "cfvariable.iddevice = ? and "
								+ "cfvariable.iscancelled = ? and " + "cftableext.idsite = ? and " + "cfvariable.idhsvariable is NULL " + "order by cftableext.description";

				Object[] prm = new Object[]
				{us.getLanguage(), new Integer(us.getIdSite()), Integer.parseInt(prop.getProperty("iddev")), "FALSE", new Integer(us.getIdSite())};
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, prm);

				toReturn += "<variables>";
				for (int x = 0; x < rs.size(); x++)
				{
					toReturn += "<variable id='" + rs.get(x).get("idvariable") + "'>" + "<![CDATA[" + rs.get(x).get("code")+"  -  "+rs.get(x).get("description") + "]]>" + "</variable>";
				}
				toReturn += "</variables>";
			}
			if ("loadselected".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				String sql = "select v.idvariable as idvariabile, v.code as code, a.description as variabile, b.description as dispositivo " +
						"from cfvariable as v, cftableext as a, cftableext as b, cftransfervar " +
						"where v.idvariable = cftransfervar.idvar and " +
						"a.idsite="+us.getIdSite()+" and " +
						"a.tablename='cfvariable' and " +
						"a.tableid=v.idvariable and " +
						"a.languagecode='"+us.getLanguage()+"' and " +
						"b.idsite="+us.getIdSite()+" and " +
						"b.tablename='cfdevice' and " +
						"b.tableid=v.iddevice and " +
						"b.languagecode='"+us.getLanguage()+"' order by v.iddevice";

				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);//, prm);
				List list2 = new ArrayList();
				for(int i=0;i<rs.size();i++)
				{
					String text = rs.get(i).get("dispositivo")+" -> "+rs.get(i).get("code")+"  -  "+rs.get(i).get("variabile");
					String value = String.valueOf(rs.get(i).get("idvariabile"));
					ListBoxElement element = new ListBoxElement(text,value);
					list2.add(element);
				}
				Collections.sort(list2);
				toReturn += "<variables>";
				for (int i=0;i<list2.size();i++)
				{
					ListBoxElement element = (ListBoxElement)list2.get(i);
					String deviceDes = element.getDescription().substring(0, element.getDescription().indexOf(" -> "));
					String variableDes = element.getDescription().substring(element.getDescription().indexOf(" -> ")+4,element.getDescription().length());
					String value = element.getValue();
					toReturn += "<device>"+"<![CDATA[" + deviceDes + "]]>" + "</device>";
					toReturn += "<var>"+"<![CDATA[" + variableDes + "]]>" + "</var>";
					toReturn += "<idvar>"+"<![CDATA[" + value + "]]>" + "</idvar>";
				}
				toReturn += "</variables>";
//				toReturn += "<variables>";
//				for (int x = 0; x < rs.size(); x++)
//				{
//					toReturn += "<device>"+"<![CDATA[" + rs.get(x).get("dispositivo") + "]]>" + "</device>";
//					toReturn += "<var>"+"<![CDATA[" + rs.get(x).get("variabile") + "]]>" + "</var>";
//					toReturn += "<idvar>"+"<![CDATA[" + rs.get(x).get("idvariabile") + "]]>" + "</idvar>";
//				}
//				toReturn += "</variables>";
			}
			if ("loaddevvar".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				String sql = "select v.iddevice as iddevice,v.idvariable as idvariable,v.idvarmdl as idvarmdl,v.code as code, a.shortdescr as short ,a.description as variable, b.description as dispositivo " +
				"from cfvariable as v, cftableext as a, cftableext as b, cftransfervar " +
				"where v.idvariable = cftransfervar.idvar and " +
				"a.idsite="+us.getIdSite()+" and " +
				"a.tablename='cfvariable' and " +
				"a.tableid=v.idvariable and " +
				"a.languagecode='"+us.getLanguage()+"' and " +
				"b.idsite="+us.getIdSite()+" and " +
				"b.tablename='cfdevice' and " +
				"b.tableid=v.iddevice and " +
				"b.languagecode='"+us.getLanguage()+"'";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,null);
				StringBuffer ris = new StringBuffer();
				toReturn = "<response>";
				toReturn = toReturn + "<variables>";
				ris.append("<![CDATA[");
				if(rs.size()>0){
						for (int x=0;x<rs.size();x++)
						{
							ris.append("<tr class='"+(x%2==0?"Row1":"Row2")+"' id='"+rs.get(x).get("iddevice")+"_"+rs.get(x).get("idvariable")+"'>\n");
							ris.append("<td class='standardTxt'>" + rs.get(x).get("dispositivo") + "</td>\n");
							ris.append("<td class='standardTxt'>" + rs.get(x).get("code")+ " - " + rs.get(x).get("variable") + "</td>\n");
							ris.append("<td class='standardTxt'  style='text-align: center;cursor: pointer;'>");
							ris.append("<IMG onclick='deleteItem(this);' src='images/actions/removesmall_on_black.png'>");
							ris.append("</td>\n");
							ris.append("</tr>\n");
						}			
				}	
				ris.append("]]>");
				toReturn = toReturn + ris.toString();
				toReturn = toReturn + "</variables>";
				toReturn = toReturn + "</response>";
			}
			if ("adddevvar".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				toReturn = "<response>";
				StringBuffer ris = new StringBuffer();
				ris.append(getDevVarMdl(us, prop));
				toReturn = toReturn + ris.toString()+"</response>";
			}
//			if ("confirmMaxdimension".equalsIgnoreCase(prop.getProperty("cmd")))
//			{
//				try
//				{
//					XMLNode msg = XMLNode.parse(prop.getProperty("msg"));
//					XMLNode[] ids = msg.getNodes("id");
//					String result="0";
//					boolean flag=false;
//					if(ids!=null)
//					{
//						String sql = "select round( sum(cast ( 60 as numeric )/ cast( V.frequency*3.5 as numeric)),2) as variable " +
//						"from cfvariable v ";
//						sql = sql + " where v.idvariable in (";
//						for (int i = 0; i < ids.length; i++)
//						{
//							sql = sql + "'"+ids[i].getTextValue()+"',";	
//						}
//						sql = sql.substring(0,sql.length()-1);
//						sql = sql + ")";
//						RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);//, prm);
//						float va = Float.parseFloat(rs.get(0).get("variable").toString());
////						int va1 = (int)Math.ceil(va/(float)10)*10;
//						int va1 = (int)Math.ceil(va);
//						if (va1<1){
//							va1=1;
//						}
//						result = String.valueOf(va1);
//						if(Integer.valueOf( prop.getProperty("maxdim").toString())>=va1){
//							flag=true;
//						}
//					}
//					toReturn = "<MSG>"+result+"</MSG>";
//					if(flag){
//						prop.setProperty("cmd", "submitVars");
//					}
//				} catch (Exception e)
//				{
//					System.out.println(e.getMessage());
//					toReturn = "<MSG>ERROR</MSG>";
//					LoggerMgr.getLogger(this.getClass()).error("confirmMaxdimension", e);
//				}
//			}
			if ("submitVars".equalsIgnoreCase(prop.getProperty("cmd")))
			{
				try
				{
					long kb = 512000l;
					if(prop.getProperty("maxdim")!= null && !"".equals(prop.getProperty("maxdim")))
						kb = Long.parseLong(prop.getProperty("maxdim"))*1024;

					String CarelPath = BaseConfig.getCarelPath();
			        String confpath = CarelPath + File.separator + "scheduler" + File.separator + "conf" + File.separator + "manager.properties";
			        Properties properties = new Properties();
			        properties.load(new FileInputStream(confpath));
			        properties.setProperty("chunksize",String.valueOf(kb));
			        properties.store(new FileOutputStream(confpath),"");
					
					XMLNode msg = XMLNode.parse(prop.getProperty("msg"));
					XMLNode[] ids = msg.getNodes("id");
					String sql = "INSERT INTO cftransfervar VALUES(?,?,?)";
					DatabaseMgr.getInstance().executeStatement(null, "DELETE FROM cftransfervar", new Object[]{});
					if(ids!=null)
					{
						for (int i = 0; i < ids.length; i++)
						{
							DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]
							{new Integer(ids[i].getTextValue()), new Integer(1),new Timestamp(System.currentTimeMillis())});
						}
					}
					toReturn = "<MSG>OK</MSG>";
					if(FileMgr.getInstance()!=null)
						FileMgr.getInstance().reload();
				} catch (Exception e)
				{
					toReturn = "<MSG>BAD</MSG>";
					LoggerMgr.getLogger(this.getClass()).error("submitVars", e);
				}
			}
			if (prop.getProperty("cmd")==null){
				return executeTab1Data(us, prop);
			}
			
		}
		else if( "tab2name".equals(tabName) )
			return executeTab2Data(us, prop);
		return toReturn;
	}
	
	private StringBuffer getDevVarMdl(UserSession us, Properties prop){
		StringBuffer ris = new StringBuffer();
		try {
			ris.append("<variables><![CDATA[");
			List<Record> l = VarphyBeanList.getMappingMdlInstancesForDataTransfer(us.getLanguage(), 
					prop.getProperty("devices"), 
					prop.getProperty("variables"), 
					prop.getProperty("exclude"));
			for (Iterator<Record> iterator = l.iterator(); iterator.hasNext();) {
				Record record = iterator.next();
    			String idPerRow = (Integer)record.get(0)+"_"+(Integer)record.get(2);
    			ris.append("<tr class='Row1' id ='" );
    			ris.append(idPerRow);
    			ris.append("'>");
    			ris.append("<td class='standardTxt' style='display: none;'>");
    			ris.append(idPerRow);
    			ris.append("</td>");
    			ris.append("<td class='standardTxt'>");
    			ris.append((String)record.get(3));
    			ris.append("</td>");
    			ris.append("<td class='standardTxt'>");
    			ris.append((String)record.get(5)+"_"+(String)record.get(4));
    			ris.append("</td>");
				ris.append("<td class='standardTxt'  style='text-align: center;cursor: pointer;'>");
				ris.append("<img onclick='deleteItem(this);' src='images/actions/removesmall_on_black.png'/>");
    			ris.append("</td>");
    			ris.append("</tr>");
			}
			ris.append("]]></variables><added>"+l.size()+"</added>");
		}catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
	
	public void executeTab2Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if( cmd.equals("save") ) {
			MbSlaveConfBean bean = new MbSlaveConfBean(us);
			String strConnection = prop.getProperty("connection");
			bean.setConnection(strConnection);
			if( strConnection.equals(MbSlaveConfBean.RS485) ) {
	    		int nPort = Integer.parseInt(prop.getProperty("comport"));
	    		bean.setPort(nPort);
	    		int nSpeed = Integer.parseInt(prop.getProperty("baudrate"));
	    		bean.setSpeed(nSpeed);
			}
			String strWriteEnable = prop.getProperty("writeenable"); 
			bean.setWriteEnable(strWriteEnable != null && strWriteEnable.equalsIgnoreCase("on")); 
			bean.updateMbConf();
			
			int idMbDev = Integer.parseInt(prop.getProperty("idmbdev"));
			if( idMbDev > 0 ) {
				us.removeProperty("idmbdev");
				String strDeviceName = prop.getProperty("device_name");
				bean.setDeviceName(strDeviceName);
				int nDeviceAddress = Integer.parseInt(prop.getProperty("device_address"));
				bean.setDeviceAddress(nDeviceAddress);
				bean.updateDevice(idMbDev);
				String strDefMbDev = prop.getProperty("defmbdev");
				if( strDefMbDev.length() > 0 ) {
		    		String astrDefMbDev[] = strDefMbDev.split(";");
		    		for(int i = 0; i < astrDefMbDev.length; i++) {
		    			String astrTuple[] = astrDefMbDev[i].split(",");
		    			bean.addVariable(Integer.parseInt(astrTuple[0]), Integer.parseInt(astrTuple[1]),
		    				astrTuple[2], Integer.parseInt(astrTuple[3]), Integer.parseInt(astrTuple[4]));
		    		}
				}
			}

			bean.generateIni(BaseConfig.getCarelPath() + "mdslave\\AppConfig.ini");
			bean.generateXml(BaseConfig.getCarelPath() + "mdslave\\SiteConfig.xml");
			us.getCurrentUserTransaction().setProperty("mb_restart_required", "1");
		}
		else if( cmd.equals("add") ) {
			MbSlaveConfBean bean = new MbSlaveConfBean(us);
			String strName = prop.getProperty("device_name");
			int nAddress = Integer.parseInt(prop.getProperty("device_address"));
			bean.addDevice(strName, nAddress);
			String strDefMbDev = prop.getProperty("defmbdev");
			if( strDefMbDev.length() > 0 ) {
	    		String astrDefMbDev[] = strDefMbDev.split(";");
	    		for(int i = 0; i < astrDefMbDev.length; i++) {
	    			String astrTuple[] = astrDefMbDev[i].split(",");
	    			bean.addVariable(Integer.parseInt(astrTuple[0]), Integer.parseInt(astrTuple[1]),
	    				astrTuple[2], Integer.parseInt(astrTuple[3]), Integer.parseInt(astrTuple[4]));
	    		}
			}
			bean.generateXml(BaseConfig.getCarelPath() + "mdslave\\SiteConfig.xml");
		}
		else if( cmd.equals("remove") ) {
			MbSlaveConfBean bean = new MbSlaveConfBean(us);
			int idMbDev = Integer.parseInt(prop.getProperty("idmbdev"));
			bean.removeDevice(idMbDev);
			bean.generateXml(BaseConfig.getCarelPath() + "mdslave\\SiteConfig.xml");
		}
		else if( cmd.equals("modify") ) {
			String strId = prop.getProperty("idmbdev");
			us.getCurrentUserTransaction().setProperty("idmbdev", strId);
		}
		else if("defaultdevice".equalsIgnoreCase(cmd))
		{
			int idsite = us.getIdSite();
			String varSource = prop.getProperty("varsource");
			if(varSource == null)
				return;
			boolean log = varSource.indexOf("log")>=0?true:false;
			boolean highestAlarm = varSource.indexOf("highestalarm")>=0?true:false;
			if(!log && !highestAlarm)
				return;
			String language = LangUsedBeanList.getDefaultLanguage(idsite);
			String deleteConfirm = prop.getProperty("deleteConfirm");
			int[] duplicatedAddress = hasDuplicateDevice(idsite,language);
			if("0".equals(deleteConfirm) && duplicatedAddress.length>0)
			{
				us.getCurrentUserTransaction().setProperty("needConfirm", "1");
				us.getCurrentUserTransaction().setProperty("varSource", varSource);
				return;
			}
			if(duplicatedAddress.length>0)
			{
				for(int i=0;i<duplicatedAddress.length;i++)
					MbSlaveConfBean.removeDeviceByAddress(duplicatedAddress[i]);
			}
			DeviceListBean deviceList = new DeviceListBean(idsite,language,false,true,false);
			MbSlaveConfBean beanMbSlave = new MbSlaveConfBean(us);
			Collection<Integer> addresses = beanMbSlave.getDevAddresses();	
			//int address = beanMbSlave.getDeviceAddress();
			int addressIndex = 1;
			int[] ids = deviceList.getIds();
			int mbVarNum = MbSlaveConfBean.getMbVariablesNo();
			int MAXVARNUM = 1000;
			String str = ProductInfoMgr.getInstance().getProductInfo().get("mdslave_threshold");
			if(str != null)
				MAXVARNUM= Integer.valueOf(str);
			int invalidType = 0;
			int deviceAdded = 0;
			for(int i=0;i<ids.length;i++)
			{
				for(int j=addressIndex; j <= MbSlaveConfBean.MAXADDRESS; j++) {
					addressIndex = j;
					if(!addresses.contains(j) ) {
						DeviceBean deviceBean = deviceList.getDevice(ids[i]);
						int addedNum = addMbSlaveDevice(idsite,mbVarNum,MAXVARNUM,beanMbSlave, deviceBean, addressIndex++, log, highestAlarm);
						if(addedNum>0)
							deviceAdded++;
						mbVarNum += addedNum;
						if(mbVarNum>=MAXVARNUM)
						{
							invalidType = 1;
							break;
						}
						break;
					}
				}
				if(addressIndex>MbSlaveConfBean.MAXADDRESS && i<ids.length)
					invalidType = 2;
				if(invalidType>0)
					break;
			}
			if(invalidType>0)
			{
				us.getCurrentUserTransaction().setProperty("invalidType", invalidType+"");
				us.getCurrentUserTransaction().setProperty("deviceAdded", deviceAdded+"");
			}
			else if(deviceAdded>0)
				us.getCurrentUserTransaction().setProperty("deviceAdded", deviceAdded+"");
		}
	}
	private int addMbSlaveDevice(int idsite,int currentVarNum, int MAXVARNUM,MbSlaveConfBean beanMbSlave,DeviceBean deviceBean,int address,boolean log,boolean highestAlarm)
	{
		int variableAdded = 0;
		RecordSet rs = VarphyBeanList.retrieveVarByDevice(idsite,deviceBean.getIddevice(),log, highestAlarm);
		int coilAddress = 0;
		int registerAddress = 0;
		if(rs.size()>0)
		{
			beanMbSlave.addDevice(deviceBean.getDescription(), address);
			for(int i=0;i<rs.size();i++)
			{
				if(currentVarNum+variableAdded+1>MAXVARNUM)
					return currentVarNum+variableAdded;
				Record rd = rs.get(i);
				int idvariable = (Integer)rd.get("idvariable");
				int type = (Integer)rd.get("type");
				int vardimension = (Integer)rd.get("vardimension");
				int varlength = (Integer)rd.get("varlength");
				if(type == 3 && varlength == 32 && vardimension == 32 )
					type = 2;
				int varAddress = 0;
				String varType = "";
				if(type == 1 || type == 4)
				{
					varAddress = coilAddress++;
					varType = MBTYPE_COIL;
				}
				else if(type == 2)
				{
					varAddress = registerAddress;
					registerAddress = registerAddress+2;
					varType = MBTYPE_REGISTER;
				}
				else if(type == 3)
				{
					varAddress = registerAddress++;
					varType = MBTYPE_REGISTER;
				}
				beanMbSlave.addVariable(deviceBean.getIddevice(), idvariable, varType, varAddress, 0);
				variableAdded++;
			}
		}
		return variableAdded;
	}
	private int[] hasDuplicateDevice(int idsite,String language) throws Exception
	{
		int[] result = new int[0];
		try {
			String sql = "select distinct mbslavedev.address from cfdevice "+
					"inner join cftableext on cftableext.idsite=1 and cftableext.languagecode='EN_en' and cftableext.tablename='cfdevice' and cftableext.tableid=cfdevice.iddevice "+
					"inner join mbslavedev on mbslavedev.name=cftableext.description "+
					"where cfdevice.islogic='FALSE' and cfdevice.iscancelled='FALSE' "+
					"order by mbslavedev.address";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			if( rs.size() >0)
			{
				result = new int[rs.size()];
				for(int i=0;i<rs.size();i++)
					result[i] = (Integer)rs.get(i).get(0);
			}
			return result;
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return result;
	}

	public String executeTab1Data(UserSession us, Properties prop) throws Exception
	{
		StringBuilder response= new StringBuilder();
		response.append("<response>");

		Integer action = Integer.parseInt((String) prop.get("action"));
		Integer idDevice = action < 10 ? Integer.parseInt((String) prop.get("iddevice")) : 0;
		
		switch( action.intValue() ){
			case 0: // LOAD_DEVICE_VAR
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars = varlist.getDatatransferVarOfDevice(us.getLanguage(),us.getIdSite(),idDevice);
				StringBuffer strbufTypeCSV = new StringBuffer();
				strbufTypeCSV.append("<input type='hidden' id='csvVarType' name='csvVarType' value='");
				response.append("<select onclick=\"onSelectDevVar(this.selectedIndex)\" ondblclick=\"addDevVar(this.selectedIndex);\" id='devvar' name='devvar' multiple size='10' class='selectB'>");
				
				for (int i=0;i<vars.length;i++)	{
					VarphyBean aux = vars[i];
					response.append("<option value=\"");
					response.append(String.valueOf(aux.getId()));
					response.append("\"");
					response.append(" id=\"devvar"+aux.getId()+"\"");
					response.append(" class = '"+( (i%2==0)?"Row1":"Row2" )+"'>");
					response.append(aux.getCode() + " - " + aux.getShortDescription());	
					response.append("</option>");
				}
				response.append("</select>");
				strbufTypeCSV.append("'>");
				response.append(strbufTypeCSV);
				response.append("]]>");	
			break;

			case 1: // LOAD_DEVICE
				response.append("<![CDATA[");
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage());
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
				response.append("<select onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" id=\"dev\" name='dev' size='10' class='selectB'>");
				int device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class = '"+( (i%2==0)?"Row1":"Row2" )+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			
			case 2: // LOAD_DEVICE_MDL
				response=new StringBuilder();
				response.append("<response>");
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"dev\" name='dev' onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" size='10' class='selectB'>");
				device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+
							" value='"+tmp_dev.getIddevice()+
							"' id='dev"+tmp_dev.getIddevice()+"' class = '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;
			
			case 5: // LOAD_VARMDL
				response.append("<![CDATA[");
				VarMdlBeanList mdlList = new VarMdlBeanList();
				VarMdlBean[] varMdls = mdlList.retrieveOrderedIfDevIsPresentLog(us.getLanguage(),us.getIdSite(),idDevice);
				response.append("<select onclick=\"onSelectDevVar(this.selectedIndex)\" ondblclick=\"addDevVar(this.selectedIndex);\" id='devvar' name='devvar' multiple size='10' class='selectB'>");
				
				for (int i=0;i<varMdls.length;i++)	{
					VarMdlBean mdlBean = varMdls[i];
					response.append("<option value=\"");
					response.append(String.valueOf(mdlBean.getIdvarmdl()));
					response.append("\"");
					response.append(" id=\"devvar"+mdlBean.getIdvarmdl()+"\"");
					response.append(" class = '"+( (i%2==0)?"Row1":"Row2" )+"'>");
					response.append(mdlBean.getCode() + " - " + mdlBean.getDescription());
					response.append("</option>");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			
			case 10: // RESTART_SERVICE
				ModbusSlaveCommander.stopService();
				ModbusSlaveCommander.startService();
				break;
			case 11: // Print All modbus devices and their variables;
				LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
				StringBuffer sb = new StringBuffer();
		    	sb.append("<mytitle>"+ "<![CDATA[" + lang.getString("datatransfer", "mb_devices")  + "]]>" + "</mytitle>");
		    	String pv_type = BaseConfig.getProductInfo("type");
		        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
		            ? true : false;
		        sb.append(getCombinedMdsdevAndVars(lang,us));
		        response.append(sb);
				break;
		}
		
		response.append("</response>");
		return response.toString();
	}

	
	public String executeTab2Data(UserSession us, Properties prop) throws Exception
	{
		StringBuilder response= new StringBuilder();
		response.append("<response>");

		Integer action = Integer.parseInt((String) prop.get("action"));
		Integer idDevice = action < 10 ? Integer.parseInt((String) prop.get("iddevice")) : 0;
		
		switch( action.intValue() ){
			case 0: // LOAD_DEVICE_VAR
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars = varlist.getAllVarOfDevice(us.getLanguage(),us.getIdSite(),idDevice);
				StringBuffer strbufTypeCSV = new StringBuffer();
				strbufTypeCSV.append("<input type='hidden' id='csvVarType' name='csvVarType' value='");
				response.append("<select onclick=\"onSelectDevVar(this.selectedIndex)\" ondblclick=\"addDevVar(this.selectedIndex);\" id='devvar' name='devvar' multiple size='10' class='selectB'>");
				for (int i=0;i<vars.length;i++)	{
					VarphyBean aux = vars[i];
					response.append("<option value=\"");

					String sql = "select idrelay from cfrelay where idvariable=" + aux.getId();
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
					if( rs.size() <= 0 ) { // variable it is not a relay 
						if( aux.getReadwrite().trim().equals("1") ) // negative ids for RO variables
							response.append("-");
					}
					response.append(String.valueOf(aux.getId()));
					response.append("\"");
					response.append(" class = '"+( (i%2==0)?"Row1":"Row2" )+"'>");
					response.append(aux.getShortDescription());
					response.append("</option>");
					if( i > 0 )
						strbufTypeCSV.append(",");
					strbufTypeCSV.append(aux.getType());
					if( aux.getType() == 3 && aux.getVarLength() == 32 && aux.getVarDimension() == 32 )
						strbufTypeCSV.append("2");
				}
				response.append("</select>");
				strbufTypeCSV.append("'>");
				response.append(strbufTypeCSV);
				response.append("]]>");	
			break;

			case 1: // LOAD_DEVICE
				response.append("<![CDATA[");
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage());
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
				response.append("<select onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" id=\"dev\" name='dev' size='10' class='selectB'>");
				int device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class = '"+( (i%2==0)?"Row1":"Row2" )+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			
			case 2: // LOAD_DEVICE_MDL
				response=new StringBuilder();
				response.append("<response>");
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"dev\" name='dev' onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" size='10' class='selectB'>");
				device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class = '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;
			
			case 10: // RESTART_SERVICE
				ModbusSlaveCommander.stopService();
				ModbusSlaveCommander.startService();
				break;
			case 11: // Print All modbus devices and their variables;
				LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
				StringBuffer sb = new StringBuffer();
		    	sb.append("<mytitle>"+ "<![CDATA[" + lang.getString("datatransfer", "mb_devices")  + "]]>" + "</mytitle>");
		    	String pv_type = BaseConfig.getProductInfo("type");
		        boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))
		            ? true : false;
		        sb.append(getCombinedMdsdevAndVars(lang,us));
		        response.append(sb);
				break;
		}
		
		response.append("</response>");
		return response.toString();
	}
	public String getCombinedMdsdevAndVars(LangService lang,UserSession us) throws Exception{
		
		StringBuffer combineds = new StringBuffer();
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("datatransfer", "device_name");
        headerTable[1] = lang.getString("datatransfer", "device_address");
        headerTable[2] = lang.getString("datatransfer", "variable_num");
        String sql = "select * from mbslavedev order by address";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		String[] data = new String[3];
		String[] astrClickRowFunction = new String[rs.size()];
		for(int i = 0; i < rs.size(); i++) {
			combineds.append("<combined>");
			Record r = rs.get(i);
			String idmddev = r.get("idmbdev").toString();
			data[0] = r.get("name").toString();
			data[1] = r.get("address").toString();
			astrClickRowFunction[i] = r.get("idmbdev").toString();
			sql = "select count(*) from mbslavevar where idmbdev=" + astrClickRowFunction[i];
			RecordSet rs1 = DatabaseMgr.getInstance().executeQuery(null, sql);
			data[2] = rs1.size() == 1 ? rs1.get(0).get(0).toString() : "0";
			combineds.append("<mdsdev>");
			combineds.append(getMdsInfoAsCaption(headerTable,data));
			combineds.append("</mdsdev>");
			combineds.append("<devvars>");
			combineds.append(getVarsTable(idmddev,lang,us));
			combineds.append("</devvars>");
			combineds.append("</combined>");
		}
        return combineds.toString();
	}

	private String getMdsInfoAsCaption(String[] headerTable, String[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<trh>");
		for(int i=0;i<headerTable.length ;i++){
			sb.append("<td>"+"<![CDATA[").append(headerTable[i]).append("]]>" +"</td>");
		}
		sb.append("</trh>");
		sb.append("<tr>");
		for(int i=0;i<headerTable.length ;i++){
			sb.append("<td>"+"<![CDATA[").append(data[i]).append("]]>" +"</td>");
		}
		sb.append("</tr>");
		return sb.toString();
	}

	private String getVarsTable(String idmddev ,LangService lang,UserSession us) {
		StringBuffer sb = new StringBuffer();
		sb.append(getVarHeader(lang));
		sb.append(getMbsVar4PrintByMddevId(idmddev,lang,us));
		return sb.toString();
	}
	public String getVarHeader(LangService lang){
		StringBuffer headerTable = new StringBuffer("<trh>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "device")).append("</th>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "variable")).append("</th>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "pvtype")).append("</th>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "type")).append("</th>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "variable_address")).append("</th>");
		headerTable.append("<th>").append (lang.getString("datatransfer", "read_only")).append("</th>");
        return headerTable.append("</trh>").toString();
	}
	
	
	public String getMbsVar4PrintByMddevId(String idmddev ,LangService lang,UserSession us){
		try {
			// data
			String sql = "select first.*, cftableext.description as variablename from " 
				+ "(select mbslavevar.*, cftableext.description as devicename from mbslavevar inner join "
				+ "cftableext on cftableext.tableid = mbslavevar.iddevice and cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=?  and mbslavevar.idmbdev=?) as first "
				+ "inner join cftableext on cftableext.tableid = first.idvariable and cftableext.tablename='cfvariable' and cftableext.languagecode=? and cftableext.idsite=? "
				+ "order by \"type\", address;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
			new Object[] { us.getLanguage(), us.getIdSite(), idmddev, us.getLanguage(), us.getIdSite()});
			StringBuffer data = new StringBuffer();
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				data.append("<tr>");
				data.append("<td>"+"<![CDATA[" +r.get("devicename").toString());
				data.append("]]>" +"</td>").append("<td>"+"<![CDATA[" +r.get("variablename").toString());
				boolean bRO = false;
				boolean bRelay = false;
				String strPvType = "0"; 
				try {
					String sql2 = "select idrelay from cfrelay where idvariable=" + r.get("idvariable").toString();
					RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2);
					bRelay = rs2.size() > 0; 
					sql2 = "select \"type\", readwrite, vardimension, varlength from cfvariable where idvariable=" + r.get("idvariable").toString();
					rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2);
					if( rs2.size() > 0 ) {
						strPvType = rs2.get(0).get("type").toString();
						if( strPvType.equals("3")
							&& (Integer)rs2.get(0).get("vardimension") == 32 && (Integer)rs2.get(0).get("varlength") == 32 )
							strPvType += "2";
						// same type names used by VS
						String type = "type" + strPvType;
						data.append("]]>" +"</td>").append("<td>"+"<![CDATA[" +lang.getString("vs", type));
						if( !bRelay ) {
							String strRW = rs2.get(0).get("readwrite").toString().trim();
							if( strRW.equals("1") )
								bRO = true;
						}
					}
				} catch(DataBaseException e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
				
				data.append("]]>" +"</td>").append("<td>"+"<![CDATA[" +r.get("type").toString());
				data.append("]]>" +"</td>").append("<td>"+"<![CDATA[" +r.get("address").toString());
				data.append("]]>" +"</td>").append("<td>"+"<![CDATA[" +"<input type='checkbox' "
					+ ((((Integer)r.get("flags")).intValue() & MbSlaveConfBean.FLAG_READ_ONLY) == MbSlaveConfBean.FLAG_READ_ONLY || bRO ? " checked" : "")
					+ (bRO ? " disabled" : "")
					+ ">");
				data.append("]]>" +"</td>");
				data.append("</tr>");
			}
	        return data.toString();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "";
		}
	}
	
	
	
}
