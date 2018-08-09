package com.carel.supervisor.presentation.co2;

import java.beans.BeanDescriptor;
import java.util.Properties;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.presentation.bean.*;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class RackBean {
	private int idRack;
	private int idCO2devmdl;
	private String suffix;
	private DeviceBean beanRack;
	private int idVariable;
	private String varCode;
	private String varDescription;
	private float fStatusOff;
	// backup configuration
	private int idBackupDevice;
	private int idBackupVariable;
	private boolean bOffline;
	private String operation;
	private float constant;
	
	
	public RackBean(int idRack)
	{
		this.idRack			= idRack;
	}

	
	public RackBean(int idRack, int idCO2devmdl,String suffix,DeviceBean beanRack, int idVariable, String varCode, String varDescription, Float fStatusOff)
	{
		this.idRack			= idRack;
		this.idCO2devmdl	= idCO2devmdl;
		this.suffix			= suffix;
		this.beanRack		= beanRack;
		this.idVariable		= idVariable;
		this.varCode		= varCode;
		this.varDescription	= varDescription;
		this.fStatusOff		= fStatusOff;
	}
	

	public int getIdRack()
	{
		return idRack;
	}
	
	public int getIdCO2devmdl() {
		return idCO2devmdl;
	}


	public String getSuffix() {
		return suffix;
	}


	public String getDescription()
	{
		if(suffix != null)
			return beanRack.getDescription() + " (" + suffix + ")";
		else
			return beanRack.getDescription();
	}
	
	
	public String getVarCode()
	{
		return varCode;
	}
	
	
	public int getNumGroups() throws DataBaseException
	{
		String sql = "SELECT COUNT(*) FROM co2_rackgroups WHERE idrack=?;";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idRack });
		if( rs.size() > 0 )
			return (Integer)rs.get(0).get(0);
		else
			return 0;
	}
	
	
	public int getIdBackupDevice()
	{
		return idBackupDevice;
	}
	
	
	public int getIdBackupVariable()
	{
		return idBackupVariable;
	}

	
	public boolean getOffline()
	{
		return bOffline;
	}
	
	
	public String getOperation()
	{
		return operation;
	}
	
	
	public float getConstant()
	{
		return constant;
	}
	
	
	public DeviceBean getBeanRack() {
		return beanRack;
	}


	public void saveRack(Properties props) throws DataBaseException
    {
    	// update rack groups
    	String sql = "DELETE FROM co2_rackgroups WHERE idrack=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idRack });    	
    	String strGroups = props.getProperty("groups");
    	if(strGroups != null && strGroups.length()>0)
    	{
	    	String[] astrGroups = strGroups.split(";");
	    	sql = "INSERT INTO co2_rackgroups VALUES(?,?);";
	    	for(int i = 0; i < astrGroups.length; i++) {
	    		int idGroup = Integer.parseInt(astrGroups[i]);
	    		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idRack, idGroup });
	    	}
    	}
    }
	
	
    public void saveBackup(Properties props) throws DataBaseException
    {
    	idBackupDevice = Integer.parseInt(props.getProperty("device"));
    	idBackupVariable = Integer.parseInt(props.getProperty("variable"));
    	bOffline = props.getProperty("offline") != null;
    	operation = props.getProperty("operation");
    	try {
    		constant = Float.parseFloat(props.getProperty("constant"));
    	}
    	catch(NumberFormatException e) {
    		constant = 0;
    	}
    	String sql = "UPDATE co2_rack SET idbackupdevice=?, idbackupvariable=?, offline=?, operation=?, constant=? WHERE idrack=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql,
    		new Object[] {
    			idBackupDevice, idBackupVariable, bOffline, operation, constant,
    			idRack
    		}
    	);
    }
    
    
    // return racks selected to be used with co2 plugin	
    public static RackBean[] retrieveRacksSelected(String language) throws DataBaseException {
    	RackBean[] racks = retrieveRacks(language);
    	// count selected racks
    	int n = 0;
    	for(int i = 0; i < racks.length; i++)
    		if( racks[i].idRack > 0 )
    			n++;
    	RackBean[] racksSelected = new RackBean[n];
    	for(int i = 0, j = 0; i < racks.length; i++) {
    		if( racks[i].idRack > 0 ) {
    			racks[i].loadBackup();
    			racksSelected[j++] = racks[i];
    		}
    	}
    	return racksSelected;
    }
    
	
    // return configured list of devices that can be used as rack
    public static RackBean[] retrieveRacks(String language) throws DataBaseException {
		String sql = "select co2_devmdl.*,co2_rack.idrack,cfdevice.*,cfvariable.idvariable,t1.description,t2.description as vdesc "+
					"from co2_devmdl "+
					"inner join cfdevmdl on co2_devmdl.devcode=cfdevmdl.code and co2_devmdl.israck=TRUE "+
					"inner join cfdevice on cfdevice.iddevmdl=cfdevmdl.iddevmdl and cfdevice.iscancelled='FALSE' "+
					"left outer join co2_rack on co2_rack.idco2devmdl=co2_devmdl.idco2devmdl and cfdevice.iddevice=co2_rack.iddevice "+
					"inner join cftableext as t1 on t1.tablename='cfdevice' and t1.idsite=1 and t1.languagecode=? and t1.tableid=cfdevice.iddevice "+
					"inner join cfvariable on cfvariable.iddevice=cfdevice.iddevice and cfvariable.code=co2_devmdl.var1 and cfvariable.idhsvariable is not null "+
					"inner join cftableext as t2 on t2.tablename='cfvariable' and t2.tableid=cfvariable.idvariable and t2.idsite=1 and t2.languagecode=? "+
					"order by cfdevice.iddevice,co2_devmdl.idco2devmdl";        
		
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { language, language });
		RackBean[] racks = new RackBean[rs.size()];
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
			DeviceBean beanDevice = new DeviceBean(r, language);
			int idVariable = (Integer)r.get("idvariable"); 
		    racks[i] = new RackBean(
		    	r.get("idrack") == null?0:Integer.valueOf(r.get("idrack").toString()),
		    	Integer.valueOf(r.get("idco2devmdl").toString()),
		    	r.get("suffix") == null?null:r.get("suffix").toString(),
	    		beanDevice,
	    		idVariable,
		    	r.get("var1").toString(),
		    	r.get("vdesc").toString(),
		    	Float.parseFloat(r.get("var2").toString())
		    );
		}

		return racks;
    }
    
   
    // save racks
    public static void saveRacks(Properties props) throws DataBaseException
    {
    	RackBean[] racks = retrieveRacks("EN_en");
    	for(int i = 0; i < racks.length; i++) {
    		RackBean rack = racks[i];
    		if( rack.idRack > 0 ) {
    			if( !props.containsKey("rack_" +rack.idRack) ) {
    				// delete rack
    				String sql = "DELETE FROM co2_rack WHERE idRack=?;";
    				DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { rack.idRack });
    				sql = "DELETE FROM co2_rackgroups WHERE idRack=?;";
    				DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { rack.idRack });
    			}
    		}
    		else if( props.containsKey("dev_" + rack.beanRack.getIddevice() + "_" + rack.idCO2devmdl) ) {
    			// insert rack
    			String sql = "INSERT INTO co2_rack VALUES(?,?,?,?);";
    			DatabaseMgr.getInstance().executeStatement(null, sql,
    				new Object[] { SeqMgr.getInstance().next(null, "co2_rack", "idrack"),
    					rack.idCO2devmdl,
    					rack.beanRack.getIddevice(),
    					rack.idVariable
    				}
    			);
    		}
    	}
    }
    
    
    public static String getHTMLRackTable(UserSession session) throws DataBaseException
    {
    	LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());
    	
    	// data
    	RackBean[] racks = retrieveRacksSelected(session.getLanguage());
        HTMLElement[][] data = new HTMLElement[racks.length][];
		String[] astrClickRowFunction = new String[racks.length];        
		for(int i = 0; i < racks.length; i++) {
        	Integer idRack = racks[i].idRack;
        	String name = racks[i].getDescription();
        	String[] astrCode = StringUtility.split(racks[i].beanRack.getCode(),".");
        	int nGroups = racks[i].getNumGroups();
			data[i] = new HTMLElement[4];
			data[i][0] = new HTMLSimpleElement(name);
			data[i][1] = new HTMLSimpleElement(astrCode[0]);
			data[i][2] = new HTMLSimpleElement(astrCode[1]);
			data[i][3] = new HTMLSimpleElement(String.valueOf(nGroups));
			astrClickRowFunction[i] = String.valueOf(idRack);
		}
		
		// header
		String[] headerTable = new String[4];
        headerTable[0] = lang.getString("co2", "racks");
        headerTable[1] = lang.getString("co2", "line");
        headerTable[2] = lang.getString("co2", "address");
        headerTable[3] = lang.getString("co2", "num_groups");

        // table
        HTMLTable table = new HTMLTable("groupsTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectRack('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onModifyRack('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(150);
        table.setColumnSize(0, 350);
        table.setColumnSize(1, 30);
        table.setColumnSize(2, 30);
        table.setColumnSize(3, 30);
        table.setWidth(870);
        table.setAlignType(new int[] { 0, 1, 1, 1 });
        
        return table.getHTMLText();        
    }	
    
    
    public static String getHTMLBackupTable(UserSession session) throws Exception
    {
    	LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());
    	String strOffline = lang.getString("co2", "offline");
		DeviceListBean devs = new DeviceListBean(session.getIdSite(), session.getLanguage());
		VarphyBeanList varList = new VarphyBeanList();
    	
    	// data
    	RackBean[] racks = retrieveRacksSelected(session.getLanguage());
        HTMLElement[][] data = new HTMLElement[racks.length][];
		String[] astrClickRowFunction = new String[racks.length];        
		for(int i = 0; i < racks.length; i++) {
			RackBean rack = racks[i];
        	Integer idRack = rack.idRack;
        	String name = rack.getDescription();
			data[i] = new HTMLElement[4];
			data[i][0] = new HTMLSimpleElement(name);
			if( rack.idBackupDevice > 0 ) {
				DeviceBean dev = devs.getDevice(rack.idBackupDevice);
				data[i][1] = new HTMLSimpleElement(dev != null ? dev.getDescription() : "");
				if( rack.bOffline )
					data[i][2] = new HTMLSimpleElement("x");
				else
					data[i][2] = new HTMLSimpleElement("----------");
				VarphyBean[] avars = varList.getListVarByIds(session.getIdSite(), session.getLanguage(), new int[] { rack.idBackupVariable }); 
				if( avars.length > 0 )
					data[i][3] = new HTMLSimpleElement(avars[0].getShortDescription()
						+ " " + rack.operation + " " + rack.constant);
				else
					data[i][3] = new HTMLSimpleElement("----------");
			}
			else {
				data[i][1] = new HTMLSimpleElement("----------");
				data[i][2] = new HTMLSimpleElement("----------");
				data[i][3] = new HTMLSimpleElement("----------");
			}
			astrClickRowFunction[i] = String.valueOf(idRack);
		}
		
		// header
		String[] headerTable = new String[4];
        headerTable[0] = lang.getString("co2", "racks");
        headerTable[1] = lang.getString("co2", "backup_device");
        headerTable[2] = lang.getString("co2", "offline");
        headerTable[3] = lang.getString("co2", "backup_condition");

        // table
        HTMLTable table = new HTMLTable("groupsTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectBackup('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onModifyBackup('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(150);
        table.setColumnSize(0, 250);
        table.setColumnSize(1, 120);
        table.setColumnSize(2, 50);
        table.setColumnSize(3, 150);
        table.setWidth(870);
        table.setAlignType(new int[] { 0, 0, 1 ,0});
        
        return table.getHTMLText();        
    }	
    
    
    // return Rack table
    public static String getHTMLTable(RackBean[] racks, String language) throws DataBaseException
    {
		LangService lang = LangMgr.getInstance().getLangService(language);
		StringBuffer html = new StringBuffer("");
     	html.append("<table width='98%' class='table' cellpadding='1' cellspacing='1'>\n");
        html.append("<tr class='th' height='22'>");
        html.append("<td align='center'><b>" + lang.getString("co2", "rack") + "</b></td>");
        html.append("<td align='center'><b>" + lang.getString("co2", "line") + "</b></td>");
        html.append("<td align='center'><b>" + lang.getString("co2", "address") + "</b></td>");
        html.append("<td align='center'><b>" + lang.getString("co2", "enabled") + "</b></td>");
        html.append("</tr>\n");
    	
		for(int i = 0; i < racks.length; i++) {
			html.append("<tr class='"+(i%2==0?"Row1":"Row2")+"' >\n");
			html.append("<td class='standardTxt'>" + racks[i].getDescription()+"</td>\n");
			String[] astrCode = StringUtility.split(racks[i].beanRack.getCode(),".");
			html.append("<td class='standardTxt' align='center'>" + astrCode[0] + "</td>\n");
			html.append("<td class='standardTxt' align='center'>" + astrCode[1] + "</td>\n");
			String checkbox = "<input type='checkbox' ";
			if( racks[i].idRack > 0 )
			{
				checkbox += "checked ";
				checkbox += "name='rack_" + racks[i].idRack+"'/>";
			}
			else
			{
				checkbox += "name='dev_" + racks[i].beanRack.getIddevice() + "_" + racks[i].idCO2devmdl + "'/>";
			}
			html.append("<td class='standardTxt' align='center'>" + checkbox + "</td>");
			html.append("</tr>");
		}
		
		html.append("</table>");
		return html.toString();            
    }
		
    
//    private static int getIdRack(int idDevice, int idVariable) throws DataBaseException
//    {
//    	String sql = "SELECT idrack FROM co2_rack WHERE iddevice=? AND idvariable=?;";
//    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice, idVariable });
//    	if( rs.size() > 0 )
//    		return (Integer)rs.get(0).get(0);
//    	else
//    		return 0;
//    }
    
    
    private void loadBackup() throws DataBaseException
    {
    	String sql = "SELECT * FROM co2_rack WHERE idrack=?;";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idRack });
    	if( rs.size() > 0 ) {
    		Record r = rs.get(0);
    		idBackupDevice = (Integer)r.get("idbackupdevice");
    		idBackupVariable = (Integer)r.get("idbackupvariable");
    		bOffline = (Boolean)r.get("offline");
    		operation = (String)r.get("operation");
    		constant = (Float)r.get("constant");
    	}
    }
    
    
    public void updateVarCode(String varCode)
    {
    	try {
        	this.varCode = varCode;
        	String sql = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code=? ORDER BY idvariable;";
    		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { beanRack.getIddevice(), varCode });
    		if( rs.size() > 0 )
    			idVariable = (Integer)rs.get(0).get(0);
    		sql = "UPDATE co2_rack SET idvariable=? WHERE idrack=?;";
    		DatabaseMgr.getInstance().executeStatement(null, sql,
    			new Object[] { idVariable, idRack });
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
}
