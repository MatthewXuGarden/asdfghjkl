package com.carel.supervisor.presentation.vscheduler;

import java.util.ArrayList;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.dbllistbox.DblListBox;


public class VSGroup extends VSBase {
	// db columns
	private static final String GROUP_ID = "idgroup";
	private static final String GROUP_NAME = "name"; 
	private static final String CATEGORY_ID = "idcategory";
	private static final String CATEGORY_NAME = "catname";
	private static final String CATEGORY_SYMBOL = "symbol";
	// a date arbitrary chosen in the past used to init timestamp field
	// field it is used by scheduler thread to keep execution track
	private static final String TIMESTAMP0 = "2010-01-01";
	
	public VSGroup()
	{
	}
	
	
	public VSGroup(int idSite, String language)
	{
		super(idSite, language);
	}
	

	public void addGroup(String strName, int idCategory, int[] anDevs,
		int idCmdVar, int nCmdFlags)
	{
		try {
			String sql = "insert into vs_group values(DEFAULT, ?, ?)";
			Object params[] = new Object[] { strName, idCategory };
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
			if( anDevs.length > 0 ) {
				sql = "select * from vs_group order by idgroup";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				if( rs.size() > 0 ) {
					Record r = rs.get(rs.size()-1);
					int idGroup = Integer.parseInt(r.get(GROUP_ID).toString());
					sql = "insert into vs_groupdevs values(?, ?, to_timestamp(?, 'YYYY-MM-DD'))";
					for(int i = 0; i < anDevs.length; i++) {
						params = new Object[] { idGroup, anDevs[i], TIMESTAMP0 };
						DatabaseMgr.getInstance().executeStatement(null, sql, params);
					}
					if( idCmdVar > 0 ) {
						sql = "insert into vs_groupcmds values(?, ?, ?)";
						params = new Object[] { idGroup, idCmdVar, nCmdFlags };
						DatabaseMgr.getInstance().executeStatement(null, sql, params);
					}
				}
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
		}
	}
	

	public void updateGroup(int id, String strName, int idCategory, int[] anDevs,
		int idCmdVar, int nCmdFlags)
	{
		try {
			String sql = "update vs_group set name = ?, idcategory = ? where idgroup = ?";
			Object params[] = new Object[] { strName, idCategory, id };
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
			sql = "delete from vs_groupdevs where idgroup = " + id;
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			if( anDevs.length > 0 ) {
				sql = "insert into vs_groupdevs values(?, ?, to_timestamp(?, 'YYYY-MM-DD'))";
				for(int i = 0; i < anDevs.length; i++) {
					params = new Object[] { id, anDevs[i], TIMESTAMP0 };
					DatabaseMgr.getInstance().executeStatement(null, sql, params);
				}
			}
			sql = "delete from vs_groupcmds where idgroup = " + id;
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			if( idCmdVar > 0 ) {
				sql = "insert into vs_groupcmds values(?, ?, ?)";
				params = new Object[] { id, idCmdVar, nCmdFlags };
				DatabaseMgr.getInstance().executeStatement(null, sql, params);
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
		}
	}
	
	
	public void removeGroup(int id)
	{
		try {
			String sql = "delete from vs_group where idgroup = " + id;
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
			// vs_groupdevs related records are removed by db constrints
			// no need to remove them explicitly
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
		}
	}

	
	public void removeDevice(int id)
	{
		try {
			String sql = "delete from vs_groupdevs where iddevice = " + id;
			DatabaseMgr.getInstance().executeStatement(null, sql, null);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
		}
	}
	
	
	public int[] getDevices(int id)
	{
		
		try {
			String sql = "select iddevice from vs_groupdevs where idgroup = " + id;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			int anDevs[] = new int[rs.size()];
			for(int i = 0; i < anDevs.length; i++)
				anDevs[i] = Integer.parseInt(rs.get(i).get(0).toString());
			return anDevs;
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
			return new int[0];
		}
	}
	
	
	public String getHTMLGroupTable()
	{
		try {
			// data
			String sql = "select idgroup, name, category.* from vs_group inner join "
				+ "(select idcategory, name as catname, symbol from vs_category where app is NULL) as category "
				+ "on category.idcategory = vs_group.idcategory order by vs_group.name";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			HTMLElement[][] data = new HTMLElement[rs.size()][];
			String[] astrClickRowFunction = new String[rs.size()];
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				data[i] = new HTMLElement[4];
				data[i][0] = new HTMLSimpleElement(r.get(GROUP_NAME).toString());
				data[i][1] = new HTMLSimpleElement("<img src='" + SYMBOL_PATH + r.get(CATEGORY_SYMBOL).toString() + "'>");
				data[i][2] = new HTMLSimpleElement(r.get(CATEGORY_NAME).toString());
				astrClickRowFunction[i] = r.get(GROUP_ID).toString();
				data[i][3] = new HTMLSimpleElement("" + getGroupVariablesNo((Integer)r.get(GROUP_ID)));
			}
			
			// header
			String[] headerTable = new String[4];
	        headerTable[0] = lang.getString("vs", "group_name");
	        headerTable[1] = "";
	        headerTable[2] = lang.getString("vs", "category");
	        headerTable[3] = lang.getString("vs", "var_no");
	        
	        // table
	        HTMLTable table = new HTMLTable("categoryTable", headerTable, data);
	        table.setTableId(1);
	        table.setSgClickRowAction("onSelectGroup('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onModifyGroup('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(nScreenWidth);
	        table.setScreenH(nScreenHeight);
	        table.setRowHeight(24); // to match the symbol height
	        table.setHeight(156); // room for 4 groups
	        table.setColumnSize(0, 350);
	        table.setColumnSize(1, 30);
	        table.setColumnSize(2, 350);
	        table.setColumnSize(3, 60);
	        table.setWidth(860);
	        table.setAlignType(new int[] { 0, 1, 0, 1 });
	        
	        return table.getHTMLText();
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
			return "";
		}
	}
	
	
	public int getVariablesNo()
	{
		int nVars = 0;
		try {
			String sql = "select idgroup from vs_group";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				nVars += getGroupVariablesNo((Integer)rs.get(i).get(0));
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(VSGroup.class).error(e);
		}
		return nVars;
	}
	
	
	public String getHTMLGroupDevs()
	{
		DblListBox dblListBox = new DblListBox(new ArrayList(), new ArrayList(),
			false, true, true, null, true);
		dblListBox.setHeaderTable1(lang.getString("vs", "all_devs"));
		dblListBox.setHeaderTable2(lang.getString("vs", "group_devs"));
		dblListBox.setScreenW(nScreenWidth);
		dblListBox.setScreenH(nScreenHeight);
		dblListBox.setHeight(450);
		dblListBox.setWidthListBox(400);
		return dblListBox.getHtmlDblListBox();		
	}
	
	
	public String getHTMLDevTable()
	{
		try {
			// data
			HTMLElement[][] data = null;
			String[] astrClickRowFunction = null;
       		DeviceListBean beanDevList = new DeviceListBean(idSite, lang.getLanguage());
       		int idDevs[] = beanDevList.getIds();
       		if( idDevs != null ) {
    			data = new HTMLElement[idDevs.length][];
    			astrClickRowFunction = new String[idDevs.length];
       			for(int i = 0; i < idDevs.length; i++) {
       				DeviceBean beanDevice = DeviceListBean.retrieveSingleDeviceById(idSite, idDevs[i], lang.getLanguage());
    				data[i] = new HTMLElement[2];
    				data[i][0] = new HTMLSimpleElement(beanDevice.getDescription());
    				// hidden columns
    				data[i][1] = new HTMLSimpleElement("" + beanDevice.getIddevice());
    				astrClickRowFunction[i] = "" + i;
       			}
       		}
       		
			// header
			String[] headerTable = new String[1];
	        headerTable[0] = lang.getString("vs", "device");
	        
	        // table
	        HTMLTable table = new HTMLTable("deviceTable", headerTable, data);
	        table.setTableId(2);
	        table.setSgClickRowAction("onSelectDevice('$1')");
	        table.setSnglClickRowFunction(astrClickRowFunction);
	        table.setDbClickRowAction("onSelectDevice('$1')");
	        table.setDlbClickRowFunction(astrClickRowFunction);
	        table.setScreenW(nScreenWidth);
	        table.setHeight(176);
	        table.setColumnSize(0,330);
	        table.setWidth(370);
	        
	        return table.getHTMLTextBufferNoWidthCalc().toString();
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
			return "";
		}
	}
	
	
	public String getHTMLVarTable()
	{
		// data
		HTMLElement[][] data = new HTMLElement[0][];

		// header
		String[] headerTable = new String[3];
        headerTable[0] = lang.getString("vs", "var_code");
		headerTable[1] = lang.getString("vs", "var_mdl");
        headerTable[2] = lang.getString("vs", "var_type");
        
        // table
        HTMLTable table = new HTMLTable("variableTable", headerTable, data);
        table.setTableId(3);
        table.setSgClickRowAction("onSelectVariable('$1')");
        //table.setSnglClickRowFunction(astrClickRowFunction);
        table.setDbClickRowAction("onAddVariable('$1')");
        //table.setDlbClickRowFunction(astrClickRowFunction);
        table.setScreenW(nScreenWidth);
        table.setHeight(176);
        table.setColumnSize(0, 60);
        table.setColumnSize(1, 300);
        table.setColumnSize(2, 40);
        table.setWidth(460);
        
        return table.getHTMLText();
	}
	
	
	public static int getDeviceVariablesNo(int idDevice, int idCategory)
	{
		int nCount = 0;
		try {
			String sql = "select count(*) from vs_categorymap where iddevmdl="
				+ "(select iddevmdl from cfdevice where iddevice=?) and idcategory=?"; // (select idcategory from vs_group where idgroup=?)
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevice, idCategory });
			if( rs.size() > 0 )
				nCount = (Integer)rs.get(0).get(0);
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
		return nCount;
	}

	
	public static int getGroupVariablesNo(int idGroup)
	{
		int nCount = 0;
		try {
			String sql = "select iddevice, idcategory from vs_groupdevs inner join vs_group "
				+ " on vs_group.idgroup = vs_groupdevs.idgroup where vs_group.idgroup=?";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idGroup });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int idDevice = (Integer)r.get("iddevice");
				int idCategory = (Integer)r.get("idcategory");
				nCount += getDeviceVariablesNo(idDevice, idCategory);
			}
		}
		catch(Exception e) {
			LoggerMgr.getLogger(VSCategory.class).error(e);
		}
		return nCount;
	}
};
