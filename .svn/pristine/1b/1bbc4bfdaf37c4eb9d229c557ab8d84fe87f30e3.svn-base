package com.carel.supervisor.presentation.co2;

import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;


public class ModelBean {
	private Integer idCO2devmdl;
	private boolean bRack;
	private String devCode;
	private String devDescription;
	private String varCode;
	private String varCodeRack;
	private String varDescription;
	private String var2Code;
	private String var2Description;
	private Float fStatusOff;
	private Float fStatusOn;
	private Float fStatus2Off;
	private Float fStatus2On;
	
	
	public Integer getIdCO2devmdl() {
		return idCO2devmdl;
	}


	public void setIdCO2devmdl(Integer idCO2devmdl) {
		this.idCO2devmdl = idCO2devmdl;
	}


	public String getDevCode()
	{
		return devCode;
	}
	
	
	public String getDevDescription()
	{
		return devDescription;
	}
	
	
	public boolean isRack()
	{
		return bRack;
	}
	
	
	public String getVarCode()
	{
		return varCode;	
	}
	
	
	public String getVarDescription()
	{
		return varDescription;
	}
	

	public boolean isVar2()
	{
		return var2Code != null;
	}
	
	
	public String getVar2Code()
	{
		return var2Code != null ? var2Code : "";
	}
	
	
	public String getVar2Description()
	{
		return var2Description != null ? var2Description : "";
	}
	
	
	public String getStatusOff()
	{
		return fStatusOff != null ? fStatusOff.toString() : "";
	}
	

	public String getStatusOn()
	{
		return fStatusOn != null ? fStatusOn.toString() : "";
	}

	
	public String getStatus2Off()
	{
		return fStatus2Off != null ? fStatus2Off.toString() : "";
	}
	

	public String getStatus2On()
	{
		return fStatus2On != null ? fStatus2On.toString() : "";
	}

	
	public void saveModel() throws DataBaseException	
	{
		if( bRack ) {
			String sql = "UPDATE co2_devmdl SET var1=?, var2=? WHERE idco2devmdl=?;";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] {
					varCode,
					String.valueOf(fStatusOff),
					this.idCO2devmdl
				}
			);
		}
		else {
			String sql = "UPDATE co2_devmdl SET var1=?, var2=?, var3=?, var4=?, var5=?, var6=? WHERE idco2devmdl=?;";
			DatabaseMgr.getInstance().executeStatement(null, sql,
				new Object[] {
					varCode, String.valueOf(fStatusOff), String.valueOf(fStatusOn),
					var2Code != null ? var2Code : null,
					var2Code != null ? String.valueOf(fStatus2Off) : null,
					var2Code != null ? String.valueOf(fStatus2On) : null,
					idCO2devmdl
				}
			);
		}
	}
	
	
	public static void saveModels(Properties props)
	{
		RackBean[] racks = new RackBean[0];
		try {
			racks = RackBean.retrieveRacksSelected("EN_en");
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("ModelBean").error(e);
		}
		
		int i = 0;
		while( true ) {
			String isRack = props.getProperty("isRack" + i);
			if( isRack == null )
				break;
			boolean bSave = true;
			ModelBean model = new ModelBean();
			model.bRack = isRack.equalsIgnoreCase("true");
			model.idCO2devmdl = Integer.valueOf(props.getProperty("idCO2devmdl" + i));
			model.varCode = props.getProperty("varCode" + i);
			try {
				model.fStatusOff = Float.parseFloat(props.getProperty("statusOff" + i));
			} catch(NumberFormatException e) {
				bSave = false;
			}
			if( model.bRack ) {
				model.varCodeRack = props.getProperty("varCodeRack" + i);
				// update rack instances with new model variable
				if( !model.varCode.equals(model.varCodeRack) ) {
					for(int j = 0; j < racks.length; j++) {
						RackBean rack = racks[j];
						if( rack.getVarCode().equals(model.varCodeRack) )
							rack.updateVarCode(model.varCode);
					}
				}
			}
			else {
				try {
					model.fStatusOn = Float.parseFloat(props.getProperty("statusOn" + i));
				} catch(NumberFormatException e) {
					bSave = false;
				}
				model.var2Code = props.getProperty("var2Code" + i);
				if( !model.var2Code.isEmpty() ) {
					try {
						model.fStatus2Off = Float.parseFloat(props.getProperty("status2Off" + i));
						model.fStatus2On = Float.parseFloat(props.getProperty("status2On" + i));
					} catch(NumberFormatException e) {
						bSave = false;
					}
				}
				else {
					model.var2Code = null;
				}
			}
			try {
				if( bSave )
					model.saveModel();
			} catch(DataBaseException e) {
				LoggerMgr.getLogger("ModelBean").error(e);
			}
			i++;
		}
	}

	
	public static ModelBean[] retrieveModels(String language) throws DataBaseException
	{
		String sql = "select * from "+
			"( "+
			"SELECT distinct(co2_devmdl.*),  "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfdevmdl' AND tableid=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode)) AS devdesc, "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=(SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode) AND code=co2_devmdl.var1)) AS var1desc, "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=(SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode) AND code=co2_devmdl.var4)) AS var2desc "+
			"FROM co2_devmdl "+
			"inner join co2_rack on co2_rack.idco2devmdl=co2_devmdl.idco2devmdl and co2_devmdl.israck=true "+
			"inner join cfdevice on co2_rack.iddevice=cfdevice.iddevice and cfdevice.iscancelled='FALSE' "+
			"inner join co2_rackgroups on co2_rackgroups.idrack=co2_rack.idrack "+
			"union "+
			"SELECT distinct(co2_devmdl.*),  "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfdevmdl' AND tableid=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode)) AS devdesc, "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=(SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode) AND code=co2_devmdl.var1)) AS var1desc, "+
			"(SELECT Description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=(SELECT idvarmdl FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE code=co2_devmdl.devcode) AND code=co2_devmdl.var4)) AS var2desc "+
			"FROM co2_devmdl "+
			"inner join cfdevmdl on co2_devmdl.devcode = cfdevmdl.code and co2_devmdl.israck=false "+
			"inner join cfdevice on cfdevmdl.iddevmdl=cfdevice.iddevmdl and cfdevice.iscancelled='FALSE' "+
			"inner join co2_grouputilities on co2_grouputilities.iddevice=cfdevice.iddevice "+
			"inner join co2_rackgroups on co2_grouputilities.idgroup=co2_grouputilities.idgroup "+
			"inner join co2_rack on co2_rack.idrack=co2_rackgroups.idrack "+
			") as ModelBean "+
			"order by ModelBean.devdesc";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { language, language, language,language, language, language });
		ModelBean[] models = new ModelBean[rs.size()];
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
			ModelBean model = new ModelBean();
			model.idCO2devmdl = Integer.valueOf(r.get("idco2devmdl").toString());
			model.devCode = (String)r.get("devcode");
			model.devDescription = (String)r.get("devdesc");
			if( model.devDescription == null )
				model.devDescription = model.devCode; 
			if(r.get("suffix") != null && Boolean.valueOf(r.get("israck").toString()))
				model.devDescription += " ("+r.get("suffix").toString()+")";
			model.varCode = (String)r.get("var1");
			model.varDescription = (String)r.get("var1desc");
			if( model.varDescription == null )
				model.varDescription = model.varCode;
			model.bRack = (Boolean)r.get("israck");
			model.fStatusOff = Float.parseFloat(r.get("var2").toString());
			if( !model.bRack ) {
				model.fStatusOn = Float.parseFloat(r.get("var3").toString());
				model.var2Code = (String)r.get("var4");
				if( model.var2Code != null ) {
					model.var2Description = (String)r.get("var2desc");
					model.fStatus2Off = Float.parseFloat(r.get("var5").toString());
					model.fStatus2On = Float.parseFloat(r.get("var6").toString());
				}
			}
			models[i] = model;
		}
		return models;
	}
	
	
	public static String getVarOptions(String language, String devcode, String varcode, boolean bWriteOnly) throws DataBaseException
	{
		StringBuffer sbOptions = new StringBuffer();
		String sql = bWriteOnly
			? "SELECT idvarmdl,code,(SELECT description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=cfvarmdl.idvarmdl) AS description"
				+ " FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE idsite=1 AND code=?) AND type != 4 AND readwrite != '1 ' ORDER BY description;"
			: "SELECT idvarmdl,code,(SELECT description FROM cftableext WHERE idsite=1 AND languagecode=? AND tablename='cfvarmdl' AND tableid=cfvarmdl.idvarmdl) AS description"
				+ " FROM cfvarmdl WHERE iddevmdl=(SELECT iddevmdl FROM cfdevmdl WHERE idsite=1 AND code=?) AND (type = 1 OR type = 4) ORDER BY description;";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { language, devcode });
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
			sbOptions.append("<option value=\"");
			String code = r.get("code").toString();
			sbOptions.append(code);
			if( code.equals(varcode) )
				sbOptions.append("\" selected>");
			else
				sbOptions.append("\">");
			sbOptions.append(r.get("description").toString());
			sbOptions.append("</option>");
		}
		return sbOptions.toString();
	}
}
