package com.carel.supervisor.remote.engine.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.io.ReadFile;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.IRetriever;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DataCollector;
import com.carel.supervisor.field.dataconn.impl.DataConnCAREL;
import com.carel.supervisor.field.dataconn.impl.DataConnMODBUS;
import com.carel.supervisor.field.types.DoubleValue;

public class ExpDataMgr 
{

	private static ExpDataMgr me = new ExpDataMgr();
	
	private List<ExpData> variables = new ArrayList<ExpData>();
	private List<ExpDataModbus> varModbus = new ArrayList<ExpDataModbus>();
	private Map<Integer, Integer > map = new HashMap<Integer, Integer>();
	private boolean firstTime = true;
	
	private ExpDataMgr()
	{
		load();
	}
	
	public static ExpDataMgr getInstance()
	{
		return me;
	}
	
	public synchronized  void load()
	{
		
		map.clear();
		variables.clear();
		varModbus.clear();
		
		String descFile = BaseConfig.getCarelPath() + File.separator + "scheduler" + File.separator + "conf" + File.separator + "InitComm.conf";
        
		String s = null;
		try
		{
			s = ReadFile.readFromFile(descFile);
		}
		catch(Exception e)
		{
		//Non c'è il file, per cui non faccio niente	
			return;
		}
		
		//File vuoto
		if ((null!=s) && (!"".equals(s.trim())))
		{
			String[] lines = StringUtility.split(s, "|");
			String[] vars = null;
			if (lines.length != 0)
			{
				for(int i= 0; i < lines.length; i++)
				{
					//Struttura file
					//ModelCode1;ModelVariable1;ModelVariable2;...|.......
					//ModelCodeN;* (tutte le variabili)
					vars = StringUtility.split(lines[i], ";");
					loadCodes(vars);
				}	
				loadVariables();
			}
		}
	}
	
	public boolean idvarMdlPresent(int idvarmdl)
	{
		return map.containsKey(idvarmdl);
	}
	
	private void loadCodes(String[] vars) 
	{
		try
		{	
			
			String model = vars[0];
			
			//Capisco se è protocollo Modbus o Carel
			
			//Query per codevar *
			String sql1 = "select idvarmdl from cfvarmdl inner join cfdevmdl on cfdevmdl.iddevmdl=cfvarmdl.iddevmdl where cfdevmdl.code=? and cfvarmdl.idsite=1 and type!=4";
			
			String sql2 = "select idvarmdl from cfvarmdl inner join cfdevmdl on cfdevmdl.iddevmdl=cfvarmdl.iddevmdl where cfdevmdl.code=? and cfvarmdl.code=? and cfvarmdl.idsite=1 and type!=4";
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection con = DatabaseMgr.getInstance().getConnection(null);
			
			for(int i = 1; i < vars.length; i++)
			{
				if (vars[i].equals("*"))
				{
					ps = con.prepareStatement(sql1);
					if(ps != null)
					{
						ps.setString(1, model);
						rs = ps.executeQuery();
						if(rs != null)
						{				
							try
							{
								while(rs.next())
								{
									map.put(rs.getInt("idvarmdl"), rs.getInt("idvarmdl"));
								}
							}
							catch(Exception e) 
							{
								Logger logger = LoggerMgr.getLogger(this.getClass());
								logger.error(e);
							}
						}
						ps.close();
					}
				}
				else
				{
					ps = con.prepareStatement(sql2);
					if(ps != null)
					{
						ps.setString(1, model);
						ps.setString(2, vars[i]);
						rs = ps.executeQuery();
						if(rs != null)
						{				
							try
							{
								while(rs.next())
								{
									map.put(rs.getInt("idvarmdl"), rs.getInt("idvarmdl"));
								}
							}
							catch(Exception e) 
							{
								Logger logger = LoggerMgr.getLogger(this.getClass());
								logger.error(e);
							}
						}
						ps.close();
					}
				}
			}
			con.close();
			
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void loadVariables()
	{

		//Load variabili protocollo Carel
		String sql = "select idvarmdl, idvariable, globalindex, addressin, type, varlength,vardimension,bitposition,decimal " +
				"from cfvariable inner join cfdevice on cfdevice.iddevice=cfvariable.iddevice inner join cfline on " +
				"cfline.idline=cfdevice.idline where cfvariable.iscancelled='FALSE' and cfline.typeprotocol='CAREL' and cfvariable.type!= 4 and cfvariable.idhsvariable is not null order by idvariable";
		
		String sqlupdate = "update cfvariable set lastupdate=current_timestamp where idvariable=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ExpData data = null; 
		try
		{
			Connection con = DatabaseMgr.getInstance().getConnection(null);
			
			ps = con.prepareStatement(sql);
			if(ps != null)
			{
				rs = ps.executeQuery();
				if(rs != null)
				{
										
					try
					{
						int idvarmdl = 0; 
						while(rs.next())
						{
							idvarmdl = rs.getInt("idvarmdl");
							if (map.containsKey(idvarmdl))
							{
								data = new ExpData();
								data.idvar = rs.getInt("idvariable");
								data.globalindex = (short)rs.getInt("globalindex");
								data.address = (short)rs.getInt("addressin");
								data.type = (short)rs.getInt("type");
								data.varlength = (short)rs.getInt("varlength");
								data.vardimension = (short)rs.getInt("vardimension");
								data.bitposition = (short)rs.getInt("bitposition");
								data.decimal = (short)rs.getInt("decimal");
								variables.add(data);
							}
						}
					}
					catch(Exception e) 
					{
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
					}
				}
				ps.close();
				
				//Aggiorniamo lastupdate in cfvariable
				ps = con.prepareStatement(sqlupdate);
				
				for(int i = 0; i < variables.size(); i++)
				{
					ps.setInt(1, variables.get(i).idvar);
					ps.execute();
				}
				ps.close();
				con.commit();
				
			}
			
			
			// Load Variabili protocollo Modbus
			
			VariableInfo varInfo = null;
	        Variable variable = null;

	        sql = "select cfvariable.* from cfvariable inner join cfdevice on cfdevice.iddevice=cfvariable.iddevice inner join" +
	        		" cfline on cfline.idline=cfdevice.idline where cfline.typeprotocol='MODBUS' and cfvariable.iscancelled='FALSE' and cfvariable.idhsvariable is not null order by idvariable";

	        DataCollector dataCollector = FieldConnectorMgr.getInstance().getDataCollector();
	        
	        ps = con.prepareStatement(sql);
	        if(ps != null)
			{
				rs = ps.executeQuery();
				if(rs != null)
				{
										
					try
					{
						int idvarmdl = 0; 
						while(rs.next())
						{
							idvarmdl = rs.getInt("idvarmdl");
							if (map.containsKey(idvarmdl))
							{							
								varInfo = new VariableInfo(rs);
								
						        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
					
						        DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(varInfo.getDevice());
						        varInfo.setDeviceInfo(deviceInfo);
					
						        variable = new Variable(varInfo);
					
						        IRetriever retriever = (IRetriever) dataCollector.getDataConnector(varInfo.getProtocolType());
						        variable.setRetriever(retriever);
						        dataCollector.getFromField(variable);
						        ExpDataModbus var = new ExpDataModbus();
						        var.variable = variable;
						        varModbus.add(var);
							}
					        
						}
					}
					catch(Exception e) 
					{
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
					}
				}
				ps.close();
				
				//Aggiorniamo lastupdate in cfvariable
				ps = con.prepareStatement(sqlupdate);
				
				for(int i = 0; i < varModbus.size(); i++)
				{
					ps.setInt(1, varModbus.get(i).variable.getInfo().getId());
					ps.execute();
				}
				ps.close();
				con.commit();
				
			}
			
	        con.close();
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	
	public void retrieve(JspWriter out, boolean force) throws Exception
	{
		ExpData data = null; 
		DataConnCAREL d = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
		long l1 = System.currentTimeMillis();
//		String path=BaseConfig.getCarelPath() + File.separator + "scheduler" + File.separator + "send" + File.separator;
//		
//		String firstPart = DateUtils.date2String(new Date(l1), "yyyyMMdd");
//		String secondPart = DateUtils.date2String(new Date(l1), "HHmmss");
//		PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(path +firstPart+  "-9999" + secondPart + ".txt", false)));
		DoubleValue doubleValue = new DoubleValue();
		short returnCode = 0;
		out.println(l1);
		for(int i = 0; i < variables.size(); i++)
		{
			data = variables.get(i);
			returnCode = d.retrieve(data.globalindex,data.address,data.type, data.varlength, data.vardimension, data.bitposition, data.decimal, doubleValue);
			if (force)
			{
				data.value = Double.NaN;
			}
			if (!data.value.equals(doubleValue.getValue()) || firstTime)
			{	
				if (returnCode == 1) {
					data.value = doubleValue.getValue();
					out.println(data.idvar+"\t"+doubleValue.getValue());
				}
				else if (returnCode == 0)
					out.println(data.idvar+"\tnull");
			}
		}
		
		
		DataConnMODBUS m = (DataConnMODBUS)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("MODBUS");
		
		DoubleValue doubleValueModbus = new DoubleValue();
		ExpDataModbus dataModbus = null;
		Variable var = null;
		for(int i = 0; i < varModbus.size(); i++)
		{
			dataModbus = varModbus.get(i);
			var = dataModbus.variable;
			returnCode = m.retrieve(var, doubleValueModbus);
			if (force)
			{
				dataModbus.value = Double.NaN;
			}

			if (!dataModbus.value.equals(doubleValueModbus.getValue()) || firstTime)
			{	
				dataModbus.value = doubleValueModbus.getValue();
				if (returnCode == 1)
					out.println(var.getInfo().getId()+"\t"+doubleValueModbus.getValue());
				else if (returnCode == 0)
					out.println(var.getInfo().getId()+"\tnull");
			}
		}
		
		firstTime = false;

		out.flush();
//	 	out.close();
	 	
	}
}
