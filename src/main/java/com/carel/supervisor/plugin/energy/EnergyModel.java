package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.language.*;
import com.carel.supervisor.presentation.session.*;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class EnergyModel {
	private int idModel			= 0;
	private String strName		= "";
	private int idDevMdl		= 0;
	private int idVarMdlKw		= 0;
	private int idVarMdlKwh		= 0;
	
	
	public static EnergyModel[] getList()
	{
		EnergyModel[] aModels = null;
		try {
			String sql = "select idmodel from energymodel order by name;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			aModels = new EnergyModel[rs.size()];
			for(int i = 0; i < rs.size(); i++)
				aModels[i] = new EnergyModel((Integer)rs.get(i).get(0));
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("EnergyModel").error(e);
		}
		return aModels;
	}

	
	public EnergyModel()
	{
	}
	
	
	public EnergyModel(int idModel)
	{
		this.idModel = idModel;
		if( idModel > 0 )
			loadModel();
	}
	
	
	public int getIdModel()
	{
		return idModel;
	}
	
	
	public void setName(String strName)
	{
		this.strName = strName;
	}
	
	public String getName()
	{
		return strName;
	}
	
	
	public void setIdDevMdl(int idDevMdl)
	{
		this.idDevMdl = idDevMdl;
	}
	
	public int getIdDevMdl()
	{
		return idDevMdl;
	}


	public void setIdVarMdlKw(int idVarMdlKw)
	{
		this.idVarMdlKw = idVarMdlKw;
	}
	
	public int getIdVarMdlKw()
	{
		return idVarMdlKw;
	}
	

	public void setIdVarMdlKwh(int idVarMdlKwh)
	{
		this.idVarMdlKwh = idVarMdlKwh;
	}
	
	public int getIdVarMdlKwh()
	{
		return idVarMdlKwh;
	}

	
	public int getIdVarKw(int idDevice)
	{
		int idVariable = 0;
		try {
			String sql = "select idvariable from cfvariable where idsite = 1 and iddevice = ? and idvarmdl = ? and idhsvariable is null and iscancelled = 'FALSE';";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice, idVarMdlKw });
			if( rs.size() > 0 )
				idVariable = (Integer)rs.get(0).get(0);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return idVariable;
	}
	

	public int getIdVarKwh(int idDevice)
	{
		int idVariable = 0;
		try {
			String sql = "select idvariable from cfvariable where idsite = 1 and iddevice = ? and idvarmdl = ? and idhsvariable is null and iscancelled = 'FALSE';";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice, idVarMdlKwh });
			if( rs.size() > 0 )
				idVariable = (Integer)rs.get(0).get(0);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return idVariable;
	}

	
	public void loadModel()
	{
		try {
			String sql = "select * from energymodel where idmodel = ?;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idModel });
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				strName = (String)r.get("name");
				idDevMdl = (Integer)r.get("iddevmdl");
				idVarMdlKw = (Integer)r.get("idvarmdlkw");
				idVarMdlKwh = (Integer)r.get("idvarmdlkwh");
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void saveModel()
	{
		try {
			if( idModel == 0 )
				idModel = SeqMgr.getInstance().next(null, "energymodel", "idmodel");
			String sql = "select * from energymodel where idmodel = ?;";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idModel });
			if( rs.size() > 0 ) {
				sql = "update energymodel set name = ?, iddevmdl = ?, idvarmdlkw = ?, idvarmdlkwh = ? where idmodel = ?;";
				DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { strName, idDevMdl, idVarMdlKw, idVarMdlKwh, idModel });
			}
			else {
				sql = "insert into energymodel values (?, ?, ?, ?, ?);";
				DatabaseMgr.getInstance().executeStatement(null, sql,
					new Object[] { idModel, strName, idDevMdl, idVarMdlKw, idVarMdlKwh });
			}
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public void deleteModel()
	{
		try {
			String sql = "delete from energymodel where idmodel = ?;";
			DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idModel });
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public static String getHtmlModelTable(UserSession session)
	{
		String language = session.getLanguage();
		LangService lang = LangMgr.getInstance().getLangService(language);

		// data
		EnergyModel[] aModels = getList();
		HTMLElement[][] data = new HTMLElement[0][];
		String[] astrClickRowFunction = new String[0];
		if( aModels != null ) {
			data = new HTMLElement[aModels.length][];
			astrClickRowFunction = new String[aModels.length];
			for(int i = 0; i < aModels.length; i++) {
				data[i] = new HTMLElement[4];
				astrClickRowFunction[i] = String.valueOf(aModels[i].idModel);
				data[i][0] = new HTMLSimpleElement(aModels[i].getName());
				data[i][1] = new HTMLSimpleElement(getDevMdlName(language, aModels[i].getIdDevMdl()));
				data[i][2] = new HTMLSimpleElement(getVarMdlName(language, aModels[i].getIdVarMdlKw()));
				data[i][3] = new HTMLSimpleElement(getVarMdlName(language, aModels[i].getIdVarMdlKwh()));
			}
		}
		
		// header
		String[] headerTable = new String[4];
        headerTable[0] = lang.getString("energy", "energy_meter_model");
        headerTable[1] = lang.getString("energy", "device_model");
        headerTable[2] = lang.getString("energy", "power");
        headerTable[3] = lang.getString("energy", "active_energy");
        
        // table
        HTMLTable table = new HTMLTable("modelTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectModel('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onModifyModel('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(150);
        table.setColumnSize(0, 155);
        table.setColumnSize(1, 155);
        table.setColumnSize(2, 155);
        table.setColumnSize(3, 155);
        table.setWidth(900);
        return table.getHTMLText();
	}
	
	
	public static String getHtmlModelOptions()
	{
		StringBuffer sbOptions = new StringBuffer();
		EnergyModel[] aModels = getList();
		if( aModels != null ) {
			for(int i = 0; i < aModels.length; i++) {
				sbOptions.append("<option value=\"");
				sbOptions.append(aModels[i].getIdModel());
				sbOptions.append("\">");
				sbOptions.append(aModels[i].getName());
				sbOptions.append("</option>\n");
			}
		}
		return sbOptions.toString();
	}
	
	
	public static String getDevMdlName(String language, int idDevMdl)
	{
		String name = "------";
		if( idDevMdl > 0 )
		try {
			String sql = "select description from cftableext where tablename = 'cfdevmdl' and idsite = 1 and languagecode = ? and tableid = ?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { language, idDevMdl });
			if( rs.size() > 0 )
				name = (String)rs.get(0).get(0);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("EnergyModel").error(e);
		}
		return name;
	}
	
	
	public static String getVarMdlName(String language, int idVarMdl)
	{
		String name = "------";
		if( idVarMdl > 0 )
		try {
			String sql = "select description from cftableext where tablename = 'cfvarmdl' and idsite = 1 and languagecode = ? and tableid = ?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { language, idVarMdl });
			if( rs.size() > 0 )
				name = (String)rs.get(0).get(0);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("EnergyModel").error(e);
		}
		return name;
	}
	

	public static String getDeviceName(String language, int idDevice)
	{
		String name = "------";
		if( idDevice > 0 )
		try {
			String sql = "select description from cftableext where tablename = 'cfdevice' and idsite = 1 and languagecode = ? and tableid = ?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { language, idDevice });
			if( rs.size() > 0 )
				name = (String)rs.get(0).get(0);
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("EnergyModel").error(e);
		}
		return name;
	}
	
	public static String getMeterModel(int idvarmdlkw,int idvarmdlkwh){
    	String meterMdl = "";
    	try {

    	String strSQL = "select energymodel.name from energymodel,cfvariable cf1,cfvariable cf2 " +
    			"where cf1.idsite=1 and cf1.iscancelled='FALSE' and cf1.idhsvariable = ? " +
    			"and cf2.idsite=1 and cf2.iscancelled='FALSE' and cf2.idhsvariable = ? " +
    			"and cf1.idvarmdl = energymodel.idvarmdlkw " +
    			"and cf2.idvarmdl = energymodel.idvarmdlkwh";
    	Object[] param = new Object[2];
		param[0] = idvarmdlkw;
		param[1] = idvarmdlkwh;
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, strSQL, param);	
		if(rs.size() > 0){
			meterMdl = " - " + (String) rs.get(0).get("name");
		}
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger("EnergyModel").error(e);
		}
    	return meterMdl;
    }
}
