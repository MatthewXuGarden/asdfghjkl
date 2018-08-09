package com.carel.supervisor.presentation.co2;

import java.util.Properties;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;


public class GroupBean {
	private int idRack = 0;
	private int idGroup;
	private String name = "";
	private boolean bEnabled = true;
	
	
	public GroupBean(int idGroup)
	{
		this.idGroup = idGroup;
	}
	

	public int getIdGroup()
	{
		return idGroup;
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public int getIdRack()
	{
		return idRack;
	}
	
	
    public static GroupBean[] retrieveGroups() throws DataBaseException
    {
    	String sql = "SELECT *,(SELECT idrack from co2_rackgroups WHERE co2_rackgroups.idgroup=co2_group.idgroup) AS idrack FROM co2_group ORDER BY name;";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
    	GroupBean[] groups = new GroupBean[rs.size()];
    	for(int i = 0; i < rs.size(); i++) {
    		Record r = rs.get(i);
    		GroupBean group = new GroupBean((Integer)r.get("idgroup"));
    		group.name = (String)r.get("name");
    		group.bEnabled = (Boolean)r.get("enabled");
    		Integer rack = (Integer)r.get("idrack");
    		if( rack != null )
    			group.idRack = rack.intValue();
    		groups[i] = group;
    	}
    	return groups;
    }
	
	
	public void addGroup(Properties props) throws DataBaseException
    {
   		// add group
    	idGroup = SeqMgr.getInstance().next(null, "co2_rack", "idrack");
    	name = props.getProperty("name");
    	bEnabled = props.get("enabled") != null;
    	String sql = "INSERT INTO co2_group VALUES(?,?,?);";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup, name, bEnabled });
    	// add group utilities
    	String strUtilities = props.getProperty("utilities");
    	String[] astrUtilities = strUtilities.split(";");
    	sql = "INSERT INTO co2_grouputilities VALUES(?,?);";
    	for(int i = 0; i < astrUtilities.length; i++) {
    		int idDevice = Integer.parseInt(astrUtilities[i]);
    		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup, idDevice });
    	}
    }
	

    public void deleteGroup() throws DataBaseException
    {
    	// delete group utilities
    	String sql = "DELETE FROM co2_grouputilities WHERE idgroup=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup });
    	// delete group
    	sql = "DELETE FROM co2_group WHERE idgroup=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup });
    }
    
    
    public void saveGroup(Properties props) throws DataBaseException
    {
   		// update group
    	name = props.getProperty("name");
    	bEnabled = props.get("enabled") != null;
    	String sql = "UPDATE co2_group SET name=?, enabled=? WHERE idgroup=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { name, bEnabled, idGroup });
    	// update group utilities
    	sql = "DELETE FROM co2_grouputilities WHERE idgroup=?;";
    	DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup });    	
    	String strUtilities = props.getProperty("utilities");
    	String[] astrUtilities = strUtilities.split(";");
    	sql = "INSERT INTO co2_grouputilities VALUES(?,?);";
    	for(int i = 0; i < astrUtilities.length; i++) {
    		int idDevice = Integer.parseInt(astrUtilities[i]);
    		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idGroup, idDevice });
    	}
    }
    
    
    public void loadGroup() throws DataBaseException
    {
    	String sql = "SELECT * FROM co2_group WHERE idgroup=?;";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idGroup });
    	if( rs.size() > 0 ) {
    		Record r = rs.get(0);
    		name = r.get("name").toString();
    		bEnabled = (Boolean)r.get("enabled");
    	}
    }
	
    
    public static String getHTMLGroupTable(UserSession session) throws DataBaseException
    {
    	LangService lang = LangMgr.getInstance().getLangService(session.getLanguage());
    	
    	// data
        String sql = "SELECT *,(SELECT COUNT(*) FROM co2_grouputilities WHERE co2_grouputilities.idgroup = co2_group.idgroup) AS utilities,"
        	+ " (SELECT idrack FROM co2_rackgroups WHERE co2_rackgroups.idgroup = co2_group.idgroup) AS idrack"        	
        	+ " FROM co2_group ORDER BY name;";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        HTMLElement[][] data = new HTMLElement[rs.size()][];
		String[] astrClickRowFunction = new String[rs.size()];        
		for(int i = 0; i < rs.size(); i++) {
			Record r = rs.get(i);
        	Integer idGroup = (Integer)r.get("idgroup");
        	String name = r.get("name").toString();
        	boolean bEnabled = (Boolean)r.get("enabled");
        	int nUtilities = (Integer)r.get("utilities");
			data[i] = new HTMLElement[3];
			data[i][0] = new HTMLSimpleElement(name);
			data[i][1] = new HTMLSimpleElement(String.valueOf(nUtilities));
			data[i][2] = new HTMLSimpleElement(bEnabled ? lang.getString("co2", "enabled") : lang.getString("co2", "disabled"));
			Integer idRack = (Integer)r.get("idrack");
			if( idRack == null )
				idRack = 0;
			astrClickRowFunction[i] = idGroup + "," + idRack;
		}
		
		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("co2", "group_name");
        headerTable[1] = lang.getString("co2", "group_devices");
        headerTable[2] = lang.getString("co2", "status");

        // table
        HTMLTable table = new HTMLTable("groupsTable", headerTable, data);
        table.setTableId(1);
        table.setSgClickRowAction("onSelectGroup('$1')");
        table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onModifyGroup('$1')");
        table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(session.getScreenWidth());
        table.setScreenH(session.getScreenHeight());
        table.setHeight(150);
        table.setColumnSize(0, 350);
        table.setColumnSize(1, 50);
        table.setColumnSize(2, 100);
        table.setWidth(890);
        table.setAlignType(new int[] { 0, 1, 0 });
        
        return table.getHTMLText();        
    }	
}
