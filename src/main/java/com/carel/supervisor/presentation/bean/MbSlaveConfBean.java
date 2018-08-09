package com.carel.supervisor.presentation.bean;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.presentation.session.UserSession;


public class MbSlaveConfBean
{
	private UserSession us;
	private LangService lang;
	
	public static final String RS485 = "RS485";
	public static final String TCP_IP = "TCP/IP";
	public static final int TCP_PORT = 502;
	public static final int FLAG_READ_ONLY = 0x01;
	
	private String strConnection = RS485;
	private int nPort = 0;
	private int nSpeed = 0;
	private String strUser = "";
	private int nLogLevel = 1;
	private boolean bWriteEnable = false;
	
	private int idMbDev = 0;
	private String strDeviceName = "";
	private int nDeviceAddress = 0;
	private static String strLanguage = null;
	
	public static final int MAXADDRESS = 247;
	
	public MbSlaveConfBean(UserSession us)
	{
		this.us = us;
		lang = LangMgr.getInstance().getLangService(us.getLanguage());
		
		try {
			String sql = "select * from cfmodbusslave";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				String strParameter = r.get("parameter").toString();
				String strValue = r.get("value").toString();
				
				if( strParameter.equals("connection") )
					strConnection = strValue;
				else if( strParameter.equals("port") )
					nPort = Integer.parseInt(strValue);
				else if( strParameter.equals("speed") )
					nSpeed = Integer.parseInt(strValue);
				else if( strParameter.equals("user") )
					strUser = strValue;
				else if( strParameter.equals("writeenable") )
					bWriteEnable = strValue.equals("1");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void setConnection(String connection)
	{
		strConnection = connection;
		if( strConnection.equals(TCP_IP) ) {
			nPort = TCP_PORT;
			nSpeed = 0;
		}
	}
	
	
	public String getConnection()
	{
		return strConnection;
	}
	
	
	public void setPort(int n)
	{
		nPort = n;
	}
	
	
	public int getPort()
	{
		return nPort;
	}
	
	
	public void setSpeed(int n)
	{
		nSpeed = n;
	}
	
	
	public int getSpeed()
	{
		return nSpeed;
	}
	
	
	public void setUser(String strUser)
	{
		this.strUser = strUser;
	}
	
	
	public String getUser()
	{
		return strUser;
	}
	
	
	public void setDeviceName(String strDeviceName)
	{
		this.strDeviceName = strDeviceName;
	}
	
	
	public String getDeviceName()
	{
		return strDeviceName;
	}
	

	public void setDeviceAddress(int nDeviceAddress)
	{
		this.nDeviceAddress = nDeviceAddress;
	}
	
	
	public int getDeviceAddress()
	{
		return nDeviceAddress;
	}
	
	
	public void setWriteEnable(boolean b)
	{
		bWriteEnable = b;
	}
	
	
	public boolean getWriteEnable()
	{
		return bWriteEnable;
	}
	
	
	public void updateMbConf()
	{
		try {
			String sql = "delete from cfmodbusslave";
			DatabaseMgr.getInstance().executeStatement(null, sql, null);

			sql = "insert into cfmodbusslave values(?, ?)";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { "connection", strConnection });
			DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { "port", nPort });
			DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { "speed", nSpeed });
			DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { "user", strUser });
			DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { "writeenable", bWriteEnable ? "1" : "0" });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void updateDevice(int idMbDev)
	{
		this.idMbDev = idMbDev;
		try {
			String sql = "update mbslavedev set name=?, address=? where idmbdev=?";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { strDeviceName, nDeviceAddress, idMbDev }
			);
			sql = "delete from mbslavevar where idmbdev=?";
			DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { idMbDev }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void addDevice(String strName, int nAddress)
	{
		try {
			String sql = "insert into mbslavedev values(?, ?, ?)";
		  	idMbDev = SeqMgr.getInstance().next(null, "mbslavedev", "idmbdev");
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { idMbDev, strName, nAddress }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}


	public void addVariable(int idDevice, int idVariable, String strType, int nAddress, int nFlags)
	{
		if( idMbDev <= 0 )
			return;
		
		try {
			String sql = "insert into mbslavevar values(?, ?, ?, ?, ?, ?, ?)";
			int idMbVar = SeqMgr.getInstance().next(null, "mbslavevar", "idmbvar");
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { idMbVar, idMbDev, idDevice, idVariable, strType, nAddress, nFlags }
			);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void removeDevice(int idMbDev)
	{
		try {
			String sql = "delete from mbslavedev where idmbdev=?";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { idMbDev }
			);
			// all related variables are removed by db constraint
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public static void removeDeviceByAddress(int address)
	{
		try {
			String sql = "delete from mbslavedev where address=?";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] { address }
			);
			// all related variables are removed by db constraint
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(MbSlaveConfBean.class).error(e);
		}
	}
	
	
	public boolean loadDevice(int idMbDev)
	{
		try {
			String sql = "select * from mbslavedev where idmbdev=?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { idMbDev }
			);
			if( rs.size() == 1 ) {
				Record r = rs.get(0);
				this.idMbDev = idMbDev;
				strDeviceName = r.get("name").toString();
				nDeviceAddress = (Integer)r.get("address");
				return true;
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return false;
	}
	
	
	public String getHTMLDeviceTable()
	{
		try {
			// data
			String sql = "select * from mbslavedev order by address";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				data[i] = new HTMLElement[3];
				data[i][0] = new HTMLSimpleElement(r.get("name").toString());
				data[i][1] = new HTMLSimpleElement(r.get("address").toString());
				astrClickRowFunction[i] = r.get("idmbdev").toString();
				sql = "select count(*) from mbslavevar where idmbdev=" + astrClickRowFunction[i];
				RecordSet rs1 = DatabaseMgr.getInstance().executeQuery(null, sql);
				data[i][2] = new HTMLSimpleElement(rs1.size() == 1 ? rs1.get(0).get(0).toString() : "0");
			}
			
			// header
			String[] headerTable = new String[3];
	        headerTable[0] = lang.getString("datatransfer", "device_name");
	        headerTable[1] = lang.getString("datatransfer", "device_address");
	        headerTable[2] = lang.getString("datatransfer", "variable_num");
	        
	        // table
	        HTMLTable table = new HTMLTable("deviceTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectDevice('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyDevice('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(us.getScreenWidth());
	        table.setScreenH(us.getScreenHeight());
	        table.setHeight(160); // room for 4 devices
	        table.setColumnSize(0, 540);
	        table.setColumnSize(1, 100);
	        table.setColumnSize(2, 100);
	        table.setWidth(860);
	        table.setAlignType(new int[] { 0, 0, 1 });
	        return table.getHTMLText();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "";
		}
	}
	
	public String getHTMLSubTab1VariableTable()
	{
		try {
			// data
			String sql = "select v.idvariable as idvariabile, v.code as code, a.shortdescr as short ,a.description as variabile, b.description as dispositivo " +
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
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				data[i] = new HTMLElement[4];
				data[i][0] = new HTMLSimpleElement(r.get("dispositivo").toString());
				data[i][1] = new HTMLSimpleElement(r.get("code").toString()+" - "+r.get("variabile").toString());
				data[i][2] = new HTMLSimpleElement("<img src='images/actions/removesmall_on_black.png' style='cursor:pointer;' onclick='deleteThisRow( this );'/>");
				data[i][3] = new HTMLSimpleElement(r.get("idvariabile").toString());
			}
			// header
			String[] headerTable = new String[3];
	        headerTable[0] = lang.getString("datatransfer", "device");
	        headerTable[1] = lang.getString("datatransfer", "variable");
	        headerTable[2] = "<img src='images/dbllistbox/delete_on.png' alt='"+lang.getString("setaction", "delall")+"' onClick='deleteAllRows();'/>";
	        
	        // table
	        HTMLTable table = new HTMLTable("deviceTable", headerTable, data);
	        table.setTableId(2);
	        table.setSgClickRowAction("onSelectRMVar('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onDeleteRMVar('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(us.getScreenWidth());
	        table.setScreenH(us.getScreenHeight());
	        table.setRowHeight(20);
	        table.setHeight(200); // room for 10 variables
	        table.setColumnSize(0, 265);
	        table.setColumnSize(1, 365);
	        table.setColumnSize(2, 80);
	        table.setWidth(855);
	        table.setAlignType(new int[] { 0, 0, 1});
	        return table.getHTMLTextBufferNoWidthCalc().toString();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "";
		}
	}
	
	public String getHTMLVariableTable()
	{
		try {
			// data
			String sql = "select first.*, cftableext.description as variablename from " 
				+ "(select mbslavevar.*, cftableext.description as devicename from mbslavevar inner join "
				+ "cftableext on cftableext.tableid = mbslavevar.iddevice and cftableext.tablename='cfdevice' and cftableext.languagecode=? and cftableext.idsite=?  and mbslavevar.idmbdev=?) as first "
				+ "inner join cftableext on cftableext.tableid = first.idvariable and cftableext.tablename='cfvariable' and cftableext.languagecode=? and cftableext.idsite=? "
				+ "order by \"type\", address;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
			new Object[] { us.getLanguage(), us.getIdSite(), idMbDev, us.getLanguage(), us.getIdSite()});
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				astrClickRowFunction[i] = "" + i;
				data[i] = new HTMLElement[10];
				data[i][0] = new HTMLSimpleElement(r.get("devicename").toString());
				data[i][1] = new HTMLSimpleElement(r.get("variablename").toString());
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
						data[i][2] = new HTMLSimpleElement(lang.getString("vs", type));
						if( !bRelay ) {
							String strRW = rs2.get(0).get("readwrite").toString().trim();
							if( strRW.equals("1") )
								bRO = true;
						}
					}
				} catch(DataBaseException e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
				
				data[i][3] = new HTMLSimpleElement(r.get("type").toString());
				data[i][4] = new HTMLSimpleElement(r.get("address").toString());
				data[i][5] = new HTMLSimpleElement("<input type='checkbox' onClick='onReadOnly(" + i + ", this.checked)'"
					+ ((((Integer)r.get("flags")).intValue() & FLAG_READ_ONLY) == FLAG_READ_ONLY || bRO ? " checked" : "")
					+ (bRO ? " disabled" : "")
					+ ">");
				// hidden columns
				data[i][6] = new HTMLSimpleElement(r.get("iddevice").toString());
				data[i][7] = new HTMLSimpleElement((bRO ? "-" : "") + r.get("idvariable").toString());
				data[i][8] = new HTMLSimpleElement(r.get("flags").toString());
				data[i][9] = new HTMLSimpleElement(strPvType);
			}
			
			// header
			String[] headerTable = new String[6];
	        headerTable[0] = lang.getString("datatransfer", "device");
	        headerTable[1] = lang.getString("datatransfer", "variable");
	        headerTable[2] = lang.getString("datatransfer", "pvtype");
	        headerTable[3] = lang.getString("datatransfer", "type");
	        headerTable[4] = lang.getString("datatransfer", "variable_address");
	        headerTable[5] = lang.getString("datatransfer", "read_only");
	        
	        // table
	        HTMLTable table = new HTMLTable("deviceTable", headerTable, data);
	        table.setTableId(2);
	        table.setSgClickRowAction("onSelectMbVar('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onDeleteMbVar('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(us.getScreenWidth());
	        table.setScreenH(us.getScreenHeight());
	        table.setRowHeight(20);
	        table.setHeight(200); // room for 10 variables
	        table.setColumnSize(0, 265);
	        table.setColumnSize(1, 265);
	        table.setColumnSize(2, 50);
	        table.setColumnSize(3, 50);
	        table.setColumnSize(4, 50);
	        table.setColumnSize(5, 46);
	        table.setWidth(855);
	        table.setAlignType(new int[] { 0, 0, 0, 0, 1, 1 });
	        return table.getHTMLText();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "";
		}
	}
	
	
	public Collection<Integer> getDevAddresses()
    {
        LinkedList<Integer> addresses = new LinkedList<Integer>();

		try {
			String sql = "select address from mbslavedev order by address";
	        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	        for(int i = 0; i < rs.size(); i++)
	        	addresses.add((Integer)rs.get(i).get(0));
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

		return addresses;
    }
	
	
	public int getVariablesNo()
	{
		int n = 0;
		try {
			String sql = "select count(*) from mbslavevar where idmbdev != " + idMbDev;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			if( rs.size() == 1 )
				n = (Integer)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return n;
	}

	static public int getMbVariablesNo()
	{
		int n = 0;
		try {
			String sql = "select count(*) from mbslavevar";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			if( rs.size() == 1 )
				n = (Integer)rs.get(0).get(0);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger("MbSlaveConfBean").error(e);
		}
		return n;
	}
	
	
	public static String getLanguage()
	{
		if( strLanguage == null ) {
			try {
				String sql = "select languagecode from cflanguage where languagecode != 'EN_en'";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
				if( rs.size() == 1 )
					strLanguage = rs.get(0).get(0).toString();
				else
					strLanguage = "EN_en";
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(MbSlaveConfBean.class).error(e);
				return "EN_en";
			}
		}
		return strLanguage;
	}
	
	
	public static String[] getUsers()
	{
		String astrUsers[] = null;

		try {
			String sql = "select * from cfusers order by username";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			astrUsers = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++)
				astrUsers[i] = rs.get(i).get("username").toString().trim();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(MbSlaveConfBean.class).error(e);
		}
		
		return astrUsers;
	}
	
	
	public boolean generateXml(String strFileName)
	{
		// prepare xml data
		XMLNode xmlDevices = new XMLNode("devices", "");
		try {
			String sql = "select idmbdev, address from mbslavedev order by address";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int idMbDev = (Integer)r.get("idmbdev");
				XMLNode xmlDevice = new XMLNode("device", "");
				xmlDevice.setAttribute("addr", r.get("address").toString());
				XMLNode xmlCoils = new XMLNode("coils", "");
				XMLNode xmlRegisters = new XMLNode("registers", "");
				String sqlVars = "select mbslavevar.*, cfdevice.idline, cfdevice.address as devaddress from mbslavevar "
					+ "inner join cfdevice on mbslavevar.iddevice = cfdevice.iddevice where idmbdev=? "
					+ "order by type, mbslavevar.address";
				RecordSet rsVars = DatabaseMgr.getInstance().executeQuery(null, sqlVars,
					new Object[] { idMbDev });
				for(int j = 0; j < rsVars.size(); j++) {
					Record rVars = rsVars.get(j);
					String strAddress = rVars.get("address").toString();
					String strIdLine = rVars.get("idline").toString();
					String strDevAddress = rVars.get("devaddress").toString();
					String strIdVariable = rVars.get("idvariable").toString();
					String strType = rVars.get("type").toString();
					String strFlags = rVars.get("flags").toString();
					if( strType.equals("coil") ) {
						XMLNode xmlCoil = new XMLNode("coil", "");
						xmlCoil.setAttribute("num", strAddress);
						xmlCoil.setAttribute("lineno", strIdLine);
						xmlCoil.setAttribute("address", strDevAddress);
						xmlCoil.setAttribute("varid", strIdVariable);
						xmlCoil.setAttribute("flags", strFlags);
						xmlCoils.addNode(xmlCoil);
					}
					else if( strType.equals("register") ) {
						XMLNode xmlRegister = new XMLNode("reg", "");
						xmlRegister.setAttribute("num", strAddress);
						xmlRegister.setAttribute("lineno", strIdLine);
						xmlRegister.setAttribute("address", strDevAddress);
						xmlRegister.setAttribute("varid", strIdVariable);
						xmlRegister.setAttribute("flags", strFlags);
						xmlRegisters.addNode(xmlRegister);
					}
				}
				xmlDevice.addNode(xmlCoils);
				xmlDevice.addNode(xmlRegisters);
				xmlDevices.addNode(xmlDevice);
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return false;
		}
		
		// write xml file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(strFileName));
			bw.write(xmlDevices.getStringBuffer().toString());
			bw.close();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return false;
		}
		
		return true;
	}
	
	
	public boolean generateIni(String strFileName)
	{
		// read log level from ini file
		nLogLevel = 1;
		try	{
			InputStream in = new FileInputStream(strFileName); 
			Properties p = new Properties();
			p.load(in);
			in.close();
			String strLogLevel = p.getProperty("LogLevel");
			nLogLevel = (strLogLevel != null) ? Integer.parseInt(strLogLevel): 1;
		}
		catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		// write ini file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(strFileName));
			bw.write("[SlaveExtension]\n");
			if( strConnection.equals(RS485) ) {
				bw.write("COMM = " + nPort + "\n");
				bw.write("Baud = " + nSpeed + "\n");
				bw.write("Parity = N\n");
			}
			else if( strConnection.equals(TCP_IP) ) {
				bw.write("MB-Port = " + nPort + "\n");
				bw.write("COMM = 0\n"); // to prevent using COM1 by default
			}
			bw.write("Language = " + getLanguage() + "\n");
			bw.write("ScanRate = 60\n");
			//bw.write("user = " + strUser + "\n");
			//bw.write("password = changeme\n");
			bw.write("URL = http://127.0.0.1/PlantVisorPRO/servlet/MasterXML\n");
			bw.write("WriteEnable = " + (bWriteEnable ? 1 : 0) + "\n");
			bw.write("LogLevel = " + nLogLevel + "\n");
			bw.close();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return false;
		}
		
		return true;
	}
}
